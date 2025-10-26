package com.havlin.daniel.russian.services.generated_content;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.generated_content.*;
import com.havlin.daniel.russian.repositories.dictionary.WordFormRepository;
import com.havlin.daniel.russian.utils.GeneratedContentChecker;

import java.util.*;
import java.util.stream.Collectors;

class SentenceGenerator {
    private final WordFormRepository wordFormRepository;
    private final GeneratedContentCorrector generatedContentCorrector;
    private final GeneratedContentErrorService generatedContentErrorService;

    public SentenceGenerator(WordFormRepository wordFormRepository, GeneratedContentErrorService generatedContentErrorService) {
        this.wordFormRepository = wordFormRepository;
        this.generatedContentCorrector = new GeneratedContentCorrector(wordFormRepository);
        this.generatedContentErrorService = generatedContentErrorService;
    }

    /**
     * Our LLM will return a long string on multiple lines, which this function will turn into usable sentences which can be stored in the database.
     * @param generatedSentences A long string from the LLM that must be ordered in a very particular fashion, or it will produce errors.
     * @param word The word which will be linked to the sentence in the database, should be the word used to generate the sentences
     * @param readingLevel The reading level which was used when generating the sentences
     * @return All sentences along with their possible errors
     */
     List<GeneratedContentService.SentenceWithErrors> createSentenceListFromGeneratedSentences(String generatedSentences,
                                                                                               Word word,
                                                                                               ReadingLevel readingLevel) {
        // This will be the final result of separating the string value given by claude, this function will return this and will be saved later to the DB
        List<GeneratedContentService.SentenceWithErrors> sentences = new ArrayList<>();
        // A list of simple POJOs to make reading the separated string easier
        List<GeneratedContentService.GeneratedSentenceDTO> sentenceSets = new ArrayList<>();
        int sentenceIndex = -1; // starts at -1 because for iteration of the loop increases by 1, so it will be at 0

        // We can't just convert the split lines into a String list because it will be immutable
        // occasionally claude will want to explain itself on the first line, we will just remove it because it will be in english
        List<String> split = new ArrayList<String>();
        generatedSentences.lines()
                .forEach(split::add);
        if (GeneratedContentChecker.isOverLatinLetterThreshold(split.getFirst(), 0.5))
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
                sentenceSets.add(new GeneratedContentService.GeneratedSentenceDTO());
                sentenceSets.get(sentenceIndex).russianText = split.get(i);
            } else if (i % 3 == 1) {
                sentenceSets.get(sentenceIndex).englishText = split.get(i);
            } else if (i % 3 == 2) {
                sentenceSets.get(sentenceIndex).grammarForm = split.get(i);
            }
        }

        // Get all forms of the word from the DB so we can figure out where or even if the word was used in the sentence
        List<String> formsInUppercase = word.getWordForms()
                .stream()
                .map((wordForm -> wordForm.getAccented().toUpperCase()))
                .toList();

        for (GeneratedContentService.GeneratedSentenceDTO sentenceSet: sentenceSets) {
            // Here we want to figure out which word form is used in the sentence
            // Split the sentence into a
            Set<String> splitSentence = Arrays.stream(sentenceSet.russianText
                    .toUpperCase().replaceAll("[.!,]", "").split(" ")).collect(Collectors.toSet());
            // there should only ever be one or no values, this is still a list to avoid any sort of out of bounds errors
            List<String> containingWordForm = formsInUppercase.stream()
                    .filter(splitSentence::contains)
                    .toList();

            if (!containingWordForm.isEmpty()) { // if the list is empty that means we couldn't find the word form in the sentence
                List<GeneratedContentErrorMessage> errorMessages = new ArrayList<>();
                Sentence currentSentence = new Sentence();
                currentSentence.setWord(word);
                word.getSentences().add(currentSentence);

                currentSentence.setText(sentenceSet.russianText);
                currentSentence.setEnglishTranslation(sentenceSet.englishText);
                currentSentence.setReadingLevel(readingLevel);
                currentSentence.setGrammarFormFromString(sentenceSet.grammarForm);

                if (currentSentence.getGrammarForm() == GeneratedSentenceGrammarForm.ERROR)
                    errorMessages.add(new GeneratedContentErrorMessage(GeneratedContentErrorType.MISSING_GRAMMAR_FORM,
                            word + " is missing it's grammar form"));

                currentSentence.setWordPosition(sentenceSet.russianText, containingWordForm.getFirst());
                if (currentSentence.getWordPosition() == -1) { // negative number means wordForm was not found in sentence
                    errorMessages.add(new GeneratedContentErrorMessage(GeneratedContentErrorType.WORD_FORM_NOT_IN_SENTENCE,
                            "The word form " + containingWordForm.getFirst() + " was not found"));
                }

                GeneratedContentCorrector.CorrectedContent correctedContent = generatedContentCorrector
                        .correctTextIssuesAndLogErrors(sentenceSet.russianText);

                errorMessages.addAll(correctedContent.errors());
                sentenceSet.russianText = correctedContent.correctedText();

                // Convert the error messages to Error entities which will be later saved to the database
                // We cannot save them now, as we need the sentence ID which won't exist until the db is flushed
                List<GeneratedContentError> errors = errorMessages.stream()
                        .map((message) -> generatedContentErrorService.createError(
                                GeneratedContentErrorOrigin.SENTENCE,
                                message.errorType(),
                                message.message(),
                                currentSentence.getText()
                        )).toList();

                if (!errorMessages.isEmpty())
                    currentSentence.setStatus(GeneratedContentStatus.NEEDS_APPROVAL);
                else
                    currentSentence.setStatus(GeneratedContentStatus.APPROVED);

                sentences.add(new GeneratedContentService.SentenceWithErrors(currentSentence, errors));
            }
        }

        return sentences;
    }
}
