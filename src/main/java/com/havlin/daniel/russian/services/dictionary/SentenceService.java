package com.havlin.daniel.russian.services.dictionary;

import com.anthropic.client.AnthropicClient;
import com.anthropic.models.messages.Message;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.Model;
import com.anthropic.models.messages.TextBlock;
import com.havlin.daniel.russian.entities.dictionary.*;
import com.havlin.daniel.russian.entities.generated_content.GeneratedSentenceGrammarForm;
import com.havlin.daniel.russian.entities.generated_content.ReadingLevel;
import com.havlin.daniel.russian.entities.generated_content.Sentence;
import com.havlin.daniel.russian.repositories.generated_content.SentenceRepository;
import com.havlin.daniel.russian.repositories.dictionary.WordFormRepository;
import com.havlin.daniel.russian.repositories.dictionary.WordRepository;
import com.havlin.daniel.russian.utils.GeneratedContentChecker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SentenceService {
    @Value("${api.claude.key}")
    private String claudeKey;

    private final WordFormRepository wordFormRepository;
    private final WordRepository wordRepository;
    private final SentenceRepository sentenceRepository;

    public SentenceService(WordFormRepository wordFormRepository,
                           WordRepository wordRepository,
                           SentenceRepository sentenceRepository) {
        this.wordFormRepository = wordFormRepository;
        this.wordRepository = wordRepository;
        this.sentenceRepository = sentenceRepository;
    }

    public void createSentencesForWord(Word word, AnthropicClient anthropicClient) {
        List<Sentence> sentences = new ArrayList<>();

        sentences.addAll(createSentenceListFromGeneratedSentences(callClaudeForSentenceGeneration(word, ReadingLevel.BEGINNER, anthropicClient), word, ReadingLevel.BEGINNER));
        sentences.addAll(createSentenceListFromGeneratedSentences(callClaudeForSentenceGeneration(word, ReadingLevel.INTERMEDIATE, anthropicClient), word, ReadingLevel.INTERMEDIATE));
        sentences.addAll(createSentenceListFromGeneratedSentences(callClaudeForSentenceGeneration(word, ReadingLevel.ADVANCED, anthropicClient), word, ReadingLevel.ADVANCED));

        sentences.forEach((sentence -> word.getSentences().add(sentence)));

        // TODO uncomment this actually save to the database
        //sentenceRepository.saveAll(sentences);
        //wordRepository.save(word);
    }

    private String callClaudeForSentenceGeneration(Word word, ReadingLevel readingLevel, AnthropicClient anthropicClient) {
        String sentences = "";
        try{
            MessageCreateParams params = MessageCreateParams.builder()
                    .maxTokens(8192L)
                    .addUserMessage(buildPromptForSentenceGeneration(word, readingLevel))
                    .model(Model.CLAUDE_3_5_HAIKU_20241022)
                    .build();
            Message message = anthropicClient.messages().create(params);
            Optional<TextBlock> textBlock = message.content().getFirst().text();

            if (textBlock.isPresent()) {
                sentences = textBlock.get().text();
            }
        } catch (Exception e) {
            // TODO return a real error here
            System.out.println(e.getMessage());
        }
        return sentences;
    }

    public List<Sentence> createSentenceListFromGeneratedSentences(String generatedSentences, Word word, ReadingLevel readingLevel) {
        // This will be the final result of separating the string value given by claude, this function will return this and will be saved later to the DB
        List<Sentence> sentences = new ArrayList<>();
        // A list of simple POJOs to make reading the separated string easier
        List<SentenceSet> sentenceSets = new ArrayList<>();
        int sentenceIndex = -1; // starts at -1 because for iteration of the loop increases by 1, so it will be at 0

        // We can't just convert the split lines into a String list because it will be immutable
        // occasionally claude will want to explain itself on the first line, we will just remove it because it will be in english
        List<String> split = new ArrayList<String>();
        generatedSentences.lines()
                .forEach(split::add);
        if (GeneratedContentChecker.doesSentenceContainLatinLetters(split.getFirst()))
            split.removeFirst();

        split = split.stream()
                .filter((line) -> !Objects.equals(line, ""))
                .toList();

        // Split the sentences into sentence sets
        // since every 3 lines will be a new sentence, we will work with the modulo to determine when a new sentence
        // starts and ends.
        for (int i = 0; i < split.size(); i++) {
            if (i % 3 == 0) {
                sentenceIndex++;
                sentenceSets.add(new SentenceSet());
                sentenceSets.get(sentenceIndex).russianText = split.get(i);
            } else if (i % 3 == 1) {
                sentenceSets.get(sentenceIndex).englishText = split.get(i);
            } else if (i % 3 == 2) {
                sentenceSets.get(sentenceIndex).grammarForm = split.get(i);
            }
        }

        // Get all forms of the word from the DB so we can figure out where or even if the word was used in the sentence
        List<String> forms = wordFormRepository.getAllByWordId(word.getId())
                .stream()
                .filter((wordForm -> wordForm.getAccented() != null))
                .map((wordForm -> wordForm.getAccented().toUpperCase()))
                .toList();

        for (SentenceSet sentenceSet: sentenceSets) {
            // Here we want to figure out which word form is used in the sentence
            // Split the sentence into a
            Set<String> splitSentence = Arrays.stream(sentenceSet.russianText
                    .toUpperCase().replaceAll("[.!,]", "").split(" ")).collect(Collectors.toSet());
            // there should only ever be one or no values, this is still a list to avoid any sort of out of bounds errors
            List<String> containingWordForm = forms.stream()
                    .filter(splitSentence::contains)
                    .toList();

            if (!containingWordForm.isEmpty()) { // if the list is empty that means we couldn't find the word form in the sentence
                Sentence currentSentence = new Sentence();
                currentSentence.setWord(word);

                currentSentence.setText(sentenceSet.russianText);
                currentSentence.setEnglishTranslation(sentenceSet.englishText);
                currentSentence.setReadingLevel(readingLevel);

                currentSentence.setGrammarFormFromString(sentenceSet.grammarForm);
                if (currentSentence.getGrammarForm() == GeneratedSentenceGrammarForm.ERROR)
                    continue;

                currentSentence.setWordPosition(sentenceSet.russianText, containingWordForm.getFirst());
                if (currentSentence.getWordPosition() == -1) { // negative number means wordForm was not found in sentence
                    continue;
                }

                if (GeneratedContentChecker.doesSentenceContainLettersWithStressMarks(sentenceSet.russianText)) {
                    sentenceSet.russianText = GeneratedContentChecker.replaceStressedLetters(sentenceSet.russianText);
                    if (!wordFormRepository.existsByAccented(sentenceSet.russianText)) { // even after being fixed the word still does not exist
                        continue;
                    }
                }

                if (GeneratedContentChecker.doesSentenceContainLatinLetters(sentenceSet.russianText))
                {
                    sentenceSet.russianText = GeneratedContentChecker.replaceEnglishCharacters(sentenceSet.russianText);
                    if (!wordFormRepository.existsByAccented(sentenceSet.russianText)) { // even after being fixed the word still does not exist
                        continue;
                    }
                }

                List<String> wordsMissingStress = GeneratedContentChecker.findWordsWithMissingStress(sentenceSet.russianText);
                if (!wordsMissingStress.isEmpty()) {
                    String sentenceStressCorrected = GeneratedContentChecker.addStressToWords(wordFormRepository, wordsMissingStress, sentenceSet.russianText);
                    continue;
                }

                // Occasionally the LLM wants to stress a word with a single vowel, this will correct that issue
                GeneratedContentChecker.removeSingleVowelStresses(sentenceSet.russianText);

                sentences.add(currentSentence);
            }
        }

        return sentences;
    }

    public String buildPromptForSentenceGeneration(Word word, ReadingLevel readingLevel) {
        StringBuilder prompt = new StringBuilder();
        List<WordForm> forms = wordFormRepository.getAllByWordId(word.getId());

        String level = "";
        if (readingLevel == ReadingLevel.BEGINNER) {
            level = ("B1");
        } else if (readingLevel == ReadingLevel.INTERMEDIATE) {
            level = "B2";
        } else {
            level = "C1";
        }

        prompt.append("You will be creating Russian grammar exercises using different forms of the word "
        + word.getAccented() + ". You need to write sentences at " + level + " level Russian proficiency using each specified form of the word.\n" +
                "Here are the word forms you must use:\n");


        for (int i = 0; i < forms.size(); i++) {
            if (forms.get(i).getBare() == null) {
                continue;
            }

            switch (forms.get(i).getFormType()) {
                // Verbs
                case "ru_verb_past_m", "ru_verb_past_f", "ru_verb_past_n", "ru_verb_past_pl":
                    prompt.append("Past: ");
                    break;
                case "ru_verb_presfut_sg1":
                    prompt.append("First Person Singular: " );
                    break;
                case "ru_verb_presfut_sg2":
                    prompt.append("Second Person Singular: " );
                    break;
                case "ru_verb_presfut_sg3":
                    prompt.append("Third Person Singular: " );
                    break;
                case "ru_verb_presfut_pl1":
                    prompt.append("First Person Plural: " );
                    break;
                case "ru_verb_presfut_pl2":
                    prompt.append("Second Person Plural: " );
                    break;
                case "ru_verb_presfut_pl3":
                    prompt.append("Third Person Plural: " );
                    break;
                case "ru_verb_gerund_present":
                    prompt.append("Gerund Present: " );
                    break;
                case "ru_verb_gerund_past":
                    prompt.append("Gerund Past: ");
                    break;
                case "ru_verb_participle_active_present":
                    prompt.append("Participle Active Present: " );
                    break;
                case "ru_verb_participle_active_past":
                    prompt.append("Participle Active Past: ");
                    break;
                case "ru_verb_participle_passive_present":
                    prompt.append("Participle Passive Present: ");
                    break;
                case "ru_verb_participle_passive_past":
                    prompt.append("Participle Passive Past: ");
                    break;
                case "ru_verb_imperative_sg":
                    prompt.append("Imperative Singular: ");
                    break;
                case "ru_verb_imperative_pl":
                    prompt.append("Imperative Plural: ");
                    break;
                    // Nouns and Adjectives
                case "ru_noun_sg_nom", "ru_noun_pl_nom", "ru_adj_m_nom", "ru_adj_f_nom", "ru_adj_n_nom", "ru_adj_pl_nom":
                    prompt.append("Nominative: ");
                    break;
                case "ru_noun_sg_gen", "ru_noun_pl_gen", "ru_adj_m_gen", "ru_adj_f_gen", "ru_adj_n_gen", "ru_adj_pl_gen":
                    prompt.append("Genitive: ");
                    break;
                case "ru_noun_sg_dat", "ru_noun_pl_dat", "ru_adj_m_dat", "ru_adj_f_dat", "ru_adj_n_dat", "ru_adj_pl_dat":
                    prompt.append("Dative: ");
                    break;
                case "ru_noun_sg_acc", "ru_noun_pl_acc", "ru_adj_m_acc", "ru_adj_f_acc", "ru_adj_n_acc", "ru_adj_pl_acc":
                    prompt.append("Accusative: ");
                    break;
                case "ru_noun_sg_inst", "ru_noun_pl_inst", "ru_adj_m_inst", "ru_adj_f_inst", "ru_adj_n_inst", "ru_adj_pl_inst":
                    prompt.append("Instrumental: ");
                    break;
                case "ru_noun_sg_prep", "ru_noun_pl_prep", "ru_adj_m_prep", "ru_adj_f_prep", "ru_adj_n_prep", "ru_adj_pl_prep":
                    prompt.append("Prepositional: ");
                    break;
                case "ru_adj_short_m", "ru_adj_short_f", "ru_adj_short_n", "ru_adj_short_pl":
                    prompt.append("Short: ");
                    break;
                case "ru_adj_comparative":
                    prompt.append("Comparative: ");
                    break;
                case "ru_adj_superlative":
                    prompt.append("Superlative: ");
                    break;
            }

            prompt.append(forms.get(i).getAccented());
            if (i == forms.size() - 1) {
                prompt.append(". \n");
            } else {
                prompt.append(", \n");
            }
        }

        prompt.append("""
                For each word form listed above, you must create 2-3 unique sentences. Each sentence should be followed by its English translation, then the grammatical case/form (without mentioning the specific word, gender, number, or using the word "case").
                
                Requirements:
                """ + " - Write sentences at ").append(level).append(" level Russian").append(""" 
                - Each sentence must be grammatically correct
                - Include stress marks (') on ALL Russian words where needed, not just the target word. The stress is very important, do not forget.
                - Make sentences diverse and interesting - some can be humorous or absurd for better memory retention
                - Every sentence should be completely different and unique
                - Do not write any numbers, explanations, or additional information
                - Do not mention plural, singular, gender, or write "case" in your grammar descriptions
                - Only identify the grammatical form (e.g., "Instrumental", "Genitive", "Short")
                
                Format your output exactly like this example:
                Кресть'яне торгу'ют зе'млями уже' не'сколько лет.
                Peasants have been trading lands for several years.
                Instrumental
                
                Each sentence, its translation, and grammar form should be on separate lines. Work through each word form in the order provided, creating 2-3 sentences for each form. Include every single form listed in the word forms.
                
                Begin writing the sentences immediately without any preamble or additional text.
                """);

        return prompt.toString();
    }

    private class SentenceSet {
        private String russianText;
        private String englishText;
        private String grammarForm;
    }
}

