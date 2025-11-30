package com.havlin.daniel.russian.services.generated_content;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.dictionary.WordForm;
import com.havlin.daniel.russian.entities.dictionary.WordType;
import com.havlin.daniel.russian.entities.generated_content.*;
import com.havlin.daniel.russian.repositories.dictionary.WordFormRepository;
import com.havlin.daniel.russian.services.retrieval.WordRetrievalService;
import com.havlin.daniel.russian.utils.GeneratedContentChecker;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class SentenceGenerator {
    private final GeneratedContentCorrector generatedContentCorrector;
    private final WordRetrievalService wordRetrievalService;
    private final Pattern punctuationPattern = Pattern.compile("[!”#$%&()*+,./:\"]");

    public SentenceGenerator(WordRetrievalService wordRetrievalService) {
        this.generatedContentCorrector = new GeneratedContentCorrector(wordRetrievalService);
        this.wordRetrievalService = wordRetrievalService;
    }

    /**
     * Our LLM will return a long string on multiple lines, which this function will turn into usable sentences which can be stored in the database.
     * @param generatedSentences A long string from the LLM that must be ordered in a very particular fashion, or it will produce errors.
     * @param wordsToSave A map where the word id is the key and the word is the value, used for saving the words to the database
     *                    at the end of the content generation.
     * @param readingLevel The reading level which was used when generating the sentences
     * @return All sentences that have been approved through the corrector
     */
     List<Sentence> createSentenceListFromGeneratedSentences(String generatedSentences, Map<Long, Word> wordsToSave,
                                                             ReadingLevel readingLevel, Word baseWord) {
        // This will be the final result of separating the string value given by claude, this function will return this and will be saved later to the DB
        List<Sentence> approvedSentences = new ArrayList<>();
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
        // since every 2 lines will be a new sentence, we will work with the modulo to determine when a new sentence
        // starts and ends.
        for (int i = 0; i < split.size(); i++) {
            if (i % 2 == 0) {
                sentenceIndex++;
                sentenceSets.add(new GeneratedContentService.GeneratedSentenceDTO());
                sentenceSets.get(sentenceIndex).russianText = split.get(i);
            } else {
                sentenceSets.get(sentenceIndex).englishText = split.get(i);
            }
        }

        for (GeneratedContentService.GeneratedSentenceDTO sentenceSet: sentenceSets) {
            // Break the sentence apart into individual words so we link the word and sentence together

            Set<String> splitSentence = Arrays.stream(
                    punctuationPattern.matcher(sentenceSet.russianText)
                            .replaceAll("").toUpperCase()
                            .split(" "))
                    .collect(Collectors.toSet());

            // Get all the unique words in the sentence that can be found in our database
            Set<Word> wordsInSentence = new HashSet<>();

            for (String word : splitSentence) {
                List<Word> foundWords = new ArrayList<>(wordRetrievalService.findAllWordsByAccentedForSentenceCreation(word));
                // if no words are found lets try fixing the stress here
                if (foundWords.isEmpty()) {
                    String correctedStress = generatedContentCorrector.correctStressInWrongPlaceForSingleWord(word);
                    // We were able to fix the stress so let's try fetching the word from the database once more
                    if (!correctedStress.isEmpty()) {
                        foundWords.addAll(wordRetrievalService.findAllWordsByAccentedForSentenceCreation(correctedStress));
                    }
                }
                if (foundWords.size() == 1) { // There is only 1 word so choose that one
                    wordsInSentence.add(foundWords.getFirst());
                } else if (foundWords.size() > 1) { // however if there are more than one words in the list we need to decide which to choose
                    // it would be challenging to choose the word based off the sentence context so we will choose
                    // the word based on the base word
                    // if the word matches any of the base words form then we will assume this is the word we need
                    for (WordForm wordForm : baseWord.getWordForms()) {
                        if (Objects.equals(wordForm.getAccented(), word)) {
                            wordsInSentence.add(baseWord);
                            break;
                        }
                    }
                }
            }

            Sentence currentSentence = new Sentence();

            currentSentence.setText(sentenceSet.russianText);
            currentSentence.setEnglishTranslation(sentenceSet.englishText);
            currentSentence.setReadingLevel(readingLevel);

            // Add the sentences to the word
            // We will keep them in the map, which is still referenced
            for (Word word : wordsInSentence) {
                Long wordId = word.getId();
                if (wordsToSave.containsKey(wordId)) {
                    wordsToSave.get(wordId).addSentence(currentSentence);
                } else {
                    word.addSentence(currentSentence);
                    wordsToSave.put(wordId, word);
                }
            }

            // Run the correctors to see if we can fix any obvious problems
            CorrectedContent correctedContent = generatedContentCorrector.runCorrections(List.of(
                    generatedContentCorrector::correctBuiltinStressMarks,
                    generatedContentCorrector::correctLatinLettersUsedAsCyrillic,
                    generatedContentCorrector::correctMissingStressMark,
                    generatedContentCorrector::correctSingleVowelStresses
            ), sentenceSet.russianText);

            if (correctedContent.isCorrected()) {
                currentSentence.setText(correctedContent.correctedText());
                approvedSentences.add(currentSentence);
            }
        }

        return approvedSentences;
    }
}
