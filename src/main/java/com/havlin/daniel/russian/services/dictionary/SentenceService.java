package com.havlin.daniel.russian.services.dictionary;

import com.havlin.daniel.russian.entities.dictionary.*;
import com.havlin.daniel.russian.repositories.dictionary.SentenceRepository;
import com.havlin.daniel.russian.repositories.dictionary.WordFormRepository;
import com.havlin.daniel.russian.repositories.dictionary.WordRepository;
import com.havlin.daniel.russian.utils.GeneratedContentChecker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    public void createSentencesForWord(Word word) {
        List<Sentence> sentences = new ArrayList<>();

        sentences.addAll(createSentenceListFromGeneratedSentences(callClaudeForSentenceGeneration(word, ReadingLevel.BEGINNER), word, ReadingLevel.BEGINNER));
        sentences.addAll(createSentenceListFromGeneratedSentences(callClaudeForSentenceGeneration(word, ReadingLevel.INTERMEDIATE), word, ReadingLevel.INTERMEDIATE));
        sentences.addAll(createSentenceListFromGeneratedSentences(callClaudeForSentenceGeneration(word, ReadingLevel.ADVANCED), word, ReadingLevel.ADVANCED));

        sentences.forEach((sentence -> word.getSentences().add(sentence)));

        sentenceRepository.saveAll(sentences);
        wordRepository.save(word);
    }

    private String callClaudeForSentenceGeneration(Word word, ReadingLevel readingLevel) {
        /* TODO claude will generate these sentences here are some sample prompts
            Verb
            Write sentences using the word VERB in LEVEL level russian, then the english translation, and then the verb form the word was in (1st Person Singular, 2nd Person Singular, 3rd Person Singular, 1st Person Plural, 2nd Person Plural, 3rd Person Plural, Past, Imperative, Gerund, Participle Active, Participle Passive). Use the word in every form. Make sure that the grammar is correct because this is for grammar exercises for learning Russian. Only write the sentences and not any numbers, or information about the sentence grammar. Every sentence should be different and unique. Each sentence should be on a different line, including the translation, and grammar case. Make the sentences unique from one another.
            Noun
            Write sentences using the word NOUN in LEVEL level russian, then the english translation, the grammar case the noun is in (Nominative, Genitive, Accusative, Prepositional, Instrumental, and Dative). Use the word in every form. Make sure that the grammar is correct because this is for grammar exercises for learning Russian. Only write the sentences and not any numbers, or information about the sentence grammar. Every sentence should be different and unique. Each sentence should be on a different line, including the translation, and grammar case.
            Adjective will be similar to noun
            For other and adverb these words do no change so just generate 6 random sentences
        */
        return "";
    }

    public List<Sentence> createSentenceListFromGeneratedSentences(String generatedSentences, Word word, ReadingLevel readingLevel) {
        // This will be the final result of separating the string value given by claude, this function will return this and will be saved later to the DB
        List<Sentence> sentences = new ArrayList<>();
        // A list of simple POJOs to make reading the separated string easier
        List<SentenceSet> sentenceSets = new ArrayList<>();
        List<String> split = generatedSentences.lines().toList();
        int sentenceIndex = -1; // starts at -1 because for iteration of the loop increases by 1, so it will be at 0

        // Split the sentences into sentence sets
        // since every 3 lines will be a new sentence we will work with the modulo to determine when a new sentence
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

        for (SentenceSet sentenceSet : sentenceSets) {
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

                if (GeneratedContentChecker.sentenceContainsLettersWithStressMarks(sentenceSet.russianText)) {
                    sentenceSet.russianText = GeneratedContentChecker.replaceStressedLetters(sentenceSet.russianText);
                }

                if (GeneratedContentChecker.sentenceContainsLatinLetters(sentenceSet.russianText))
                {
                    sentenceSet.russianText = GeneratedContentChecker.replaceEnglishCharacters(sentenceSet.russianText);
                }

                sentences.add(currentSentence);
            }
        }

        return sentences;
    }

    public String buildPromptForSentenceGeneration(Word word, ReadingLevel readingLevel) {
        StringBuilder prompt = new StringBuilder();
        List<WordForm> forms = wordFormRepository.getAllByWordId(word.getId());
        prompt.append("Write sentences using the word ");
        prompt.append(word.getAccented());
        prompt.append(" in ");

        if (readingLevel == ReadingLevel.BEGINNER) {
            prompt.append("B1 level Russian");
        } else if (readingLevel == ReadingLevel.INTERMEDIATE) {
            prompt.append("B2 level Russian");
        } else {
            prompt.append("C1 level Russian");
        }

        prompt.append(", then the english translation, and then the grammar form the word was in without writing the given word only the grammar form. ");
        prompt.append("Use the word in the following forms");

        if (word.getType() == WordType.NOUN || word.getType() == WordType.ADJECTIVE) {
            prompt.append(" and do not mention plural, singular, or the gender of the word, or write the word case");
        }

        prompt.append(": ");

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
                prompt.append(". ");
            } else {
                prompt.append(", ");
            }
        }

        prompt.append("Make sure that the grammar is correct because this is for grammar exercises for learning Russian." +
                " Only write the sentences and not any numbers, or information about the sentence grammar. Every sentence" +
                " should be different and unique. Each sentence should be on a different line, including the translation," +
                " and grammar case. Include a ' after the letter that needs to be stressed on every word. ");

        if (word.getType() != WordType.ADJECTIVE) {
            prompt.append("Create 15-25 sentences, some can be humorous or absurd for better memory retention.");
        } else {
            prompt.append("Some sentences can be humorous or absurd for better memory retention.");
        }

        return prompt.toString();
    }

    private class SentenceSet {
        private String russianText;
        private String englishText;
        private String grammarForm;
    }
}

