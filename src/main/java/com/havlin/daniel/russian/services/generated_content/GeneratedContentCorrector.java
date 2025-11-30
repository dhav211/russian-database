package com.havlin.daniel.russian.services.generated_content;

import com.havlin.daniel.russian.services.retrieval.WordRetrievalService;
import com.havlin.daniel.russian.utils.GeneratedContentChecker;
import com.havlin.daniel.russian.utils.StressedWordConverter;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Corrects any of the obvious issues with LLM generation, such as latin letters, missing stress, and a built-in stress
 * mark that can't be saved in a database properly. If the correction cannot be made we can just dispose of the sentence.
 */
class GeneratedContentCorrector {
    private final WordRetrievalService wordRetrievalService;

    private final Pattern latinLetterPattern = Pattern.compile(".*[a-zA-Z\\\\p{Punct}].*");
    private final Pattern cyrllicStressPattern = Pattern.compile("[а-яёА-ЯЁ]́");
    private final Pattern punctuationPattern = Pattern.compile("[!”#$%&()*+,./:\"]");

    public GeneratedContentCorrector(WordRetrievalService wordRetrievalService) {
        this.wordRetrievalService = wordRetrievalService;
    }

    /**
     * Runs all the supplied corrector functions to try to correct the given text, if it needs correcting. The corrector
     * functions are the functions of this class. If the text is correct or can be correct this method will set the boolean
     * value to true so we can approve the sentence. There will always be text returns regardless of whether it's correct
     * or not
     * @param correctors This is a list of functions that return a CorrectContent object and must supply a string object
     *                   as a parameter. This locks it into our corrector functions of this class.
     * @param text The russian text which we will run the correctors on.
     * @return An object that has a boolean stating whether it's correct or not, and a string object containing the corrected text
     */
    CorrectedContent runCorrections(List<Function<String, CorrectedContent>> correctors, String text) {
        boolean isSentenceAccepted = true;
        StringBuilder textWithCorrections = new StringBuilder(text);
        // Run each of the error checking function, applying the changes and adding the errors to the list
        for (Function<String, CorrectedContent> correction : correctors) {
            CorrectedContent correctedContent = correction.apply(textWithCorrections.toString());
            // If there were errors that could be corrected we will accept the sentence, if not then just reject the sentence
            if (correctedContent.isCorrected()) {
                textWithCorrections.replace(0, textWithCorrections.length(), correctedContent.correctedText());
            } else {
                isSentenceAccepted = false;
                break;
            }
        }

        return new CorrectedContent(isSentenceAccepted, textWithCorrections.toString());
    }

    /**
     * Checks a russian text for words that may be missing stress marks. If a word with miss stress marks cannot be fixed
     * the algorithm will just throw the sentence out.
     * @param text The russian text to be check for missing stress
     * @return A corrected text and a boolean that states whether it was successfully corrected
     */
    CorrectedContent correctMissingStressMark(String text) {
        List<String> wordsMissingStress = findWordsWithMissingStress(text);
        if (!wordsMissingStress.isEmpty()) {
            StressLetterCorrections stressLetterCorrections = getUnstressedCorrections(wordsMissingStress);
            text = addStressToWords(stressLetterCorrections, text);

            if (!stressLetterCorrections.canBeCompletelyCorrected()) {
                for (String _ : stressLetterCorrections.uncorrectable) {
                    return new CorrectedContent(false, "");
                }
            }
        }

        return new CorrectedContent(true, text);
    }

    /**
     * The LLM will occasionally use a latin letter instead of a cyclic letter, this will replace any of the findings.
     * If a fix cannot be made the sentence will be rejected.
     * @param text The russian text which will be checked for latin letters.
     * @return A corrected text and a boolean that states whether it was successfully corrected
     */
    CorrectedContent correctLatinLettersUsedAsCyrillic(String text) {
        if (GeneratedContentChecker.doesSentenceContainLatinLetters(text))
        {
            List<String> wordsWithLatinLetters = findWordsWithLatinLetters(text);
            text = replaceEnglishCharacters(text);

            for(String wordWithLatinLetters : wordsWithLatinLetters) {
                if (!wordRetrievalService.doesWordFormExistByAccented(wordWithLatinLetters)) { // even after being fixed the word still does not exist
                    return new CorrectedContent(false, "");
                }
            }
        }

        return new CorrectedContent(true, text);
    }

    /**
     * The database cannot read built in stress marks, so we can replace the built-in own with a mark that comes after the
     * stressed letter.
     * @param text The russian text which will be checked for built-in stress marks.
     * @return A corrected text and a boolean that states whether it was successfully corrected
     */
    CorrectedContent correctBuiltinStressMarks(String text) {
        if (GeneratedContentChecker.doesSentenceContainLettersWithBuiltinStressMarks(text)) {
            List<String> stressedWords = findWordsWithBuiltinStressedLetter(text);
            text = replaceStressedLetters(text);

            for (String stressedWord : stressedWords) {
                if (!wordRetrievalService.doesWordFormExistByAccented(replaceStressedLetters(stressedWord))) { // even after being fixed the word still does not exist
                    List<String> forms = wordRetrievalService.findAllAccentedWordFormsByBare(stressedWord.replace("'", ""));
                    if (!forms.isEmpty()) {
                        boolean doAllFormsHaveSameAccented = forms.stream()
                                .allMatch((form) -> Objects.equals(form, forms.getFirst()));

                        if (!doAllFormsHaveSameAccented) {
                            return new CorrectedContent(false, "");
                        }
                    }
                }
            }
        }

        return new CorrectedContent(true, text);
    }

    /**
     * Will remove stress marks from words with single vowels, these stress marks are redundant and cause trouble searching
     * the database.
     * @param text The russian text that will be searched for single vowel stresses.
     * @return A corrected text and a boolean that states whether it was successfully corrected
     */
    CorrectedContent correctSingleVowelStresses(String text) {
        String[] split = text.split(" ");
        for (int i = 0; i < split.length; i++) {
            if (getNumberOfVowels(split[i]) == 1 && split[i].contains("'")) {
                StringBuilder corrected = new StringBuilder(split[i]);
                int stressIndex = split[i].indexOf('\'');
                corrected.deleteCharAt(stressIndex);
                split[i] = corrected.toString();
            }
        }

        return new CorrectedContent(true, String.join(" ", split));
    }

    /**
     * Checks an entire text word by word for an incorrect stress. Will always return a corrected text regardless of
     * whether anything was corrected or not, we cannot not be entirely sure because we don't have every word in the database.
     * @param text A sentence of russian text that will be checked for a stress mark in the wrong place.
     * @return The correct content object returned will always state that the text has been corrected.
     */
    CorrectedContent correctStressInWrongPlace(String text) {
        StringBuilder correctedStress = new StringBuilder();
        String[] splitText = text.split(" ");

        for (int i = 0; i < splitText.length; i++) {
            Matcher matcher = punctuationPattern.matcher(splitText[i]);
            String removedPunctuationWord = matcher.replaceAll("").toLowerCase();

            // Check to see if the accented word exists in the database, if it doesn't let's move forward with the algorithm
            if (!wordRetrievalService.doesWordFormExistByAccented(removedPunctuationWord)) {
                String removedStress = removedPunctuationWord.replace("'", "");
                splitText[i] = findCorrectStress(removedStress);
            }
        }

        // Reassemble the sentence with the string builder object
        for (int i = 0; i < splitText.length; i++) {
            correctedStress.append(splitText[i]);

            if (i < splitText.length - 1)
                correctedStress.append(" ");
        }

        return new CorrectedContent(true, correctedStress.toString());
    }

    /**
     * Attempt to correct the stress of a single word. This method assumes that the given word has no punctuation and has
     * not been found in the database. It will remove
     * @param word A string object for a single word that has stress in the wrong place
     * @return A corrected string with the stress changed or an empty string if no alternative was found
     */
    String correctStressInWrongPlaceForSingleWord(String word) {
        if (word.split(" ").length > 1) // exit the function if we have more than one word
            return "";

        String newStressWord = findCorrectStress(word.replace("'", ""));

        return newStressWord.contains("'") ? newStressWord : "";
    }

    private String findCorrectStress(String word) {
        // Check to see if the accentless word exists in the database
        List<String> uniqueAccented = wordRetrievalService.findAllWordFormsWithUniqueWordByBare(word);
        StringBuilder updateStress = new StringBuilder(word);

        // If there is a only 1 match we can be positive this is the one we are looking for
        if (uniqueAccented.size() == 1) {
            // We will need to find the position of the accent mark, then we will apply it the word in the split text
            int stressPosition = uniqueAccented.getFirst().indexOf("'");
            if (stressPosition > 0) {
                updateStress.insert(stressPosition, "'");
            }
        }

        return updateStress.toString();
    }

    private String replaceEnglishCharacters(String russianText) {
        StringBuilder rebuildRussianText = new StringBuilder(russianText);
        char[] englishLetters = {'A', 'a', 'B', 'b', 'C', 'c', 'O', 'o', 'E', 'e', 'Y', 'y', 'R', 'r', 'S', 's', 'T', 't',
                'K', 'k', 'M', 'm', 'P', 'p', 'Á', 'á', 'Ó', 'ó', 'É', 'é', 'Ý', 'ý'}; // Possible latin letters claude likes to mix up
        for (char c : englishLetters) {
            int charIndex = rebuildRussianText.toString().indexOf(c); // check to see if this char is found in the sentence
            if (charIndex >= 0) { // if the char is not in the sentence it will return -1, otherwise it will return the index
                for (int i = charIndex; i < rebuildRussianText.toString().length(); i++) {
                    if (rebuildRussianText.toString().charAt(i) == c) { // first round will always be true, however as it goes through the loop it won't always be true, if ever
                        String russianReplacement = "";

                        switch (c) {
                            case 'A' -> russianReplacement = "А";
                            case 'a' -> russianReplacement = "а";
                            case 'b' -> russianReplacement = "в";
                            case 'B' -> russianReplacement = "В";
                            case 'O' -> russianReplacement = "О";
                            case 'o' -> russianReplacement = "о";
                            case 'E' -> russianReplacement = "Е";
                            case 'e' -> russianReplacement = "е";
                            case 'C', 'S' -> russianReplacement = "С";
                            case 'c', 's' -> russianReplacement = "с";
                            case 'R', 'P' -> russianReplacement = "Р";
                            case 'r', 'p' -> russianReplacement = "р";
                            case 'Y' -> russianReplacement = "У";
                            case 'y' -> russianReplacement = "у";
                            case 'K' -> russianReplacement = "К";
                            case 'k' -> russianReplacement = "к";
                            case 'M' -> russianReplacement = "М";
                            case 'm' -> russianReplacement = "м";
                            case 'Á' -> russianReplacement = "А'";
                            case 'á' -> russianReplacement = "а'";
                            case 'Ó' -> russianReplacement = "О'";
                            case 'ó' -> russianReplacement = "о'";
                            case 'É' -> russianReplacement = "Е'";
                            case 'é' -> russianReplacement = "е'";
                            case 'Ý' -> russianReplacement = "У'";
                            case 'ý' -> russianReplacement = "у'";
                        }
                        rebuildRussianText.deleteCharAt(i);
                        rebuildRussianText.insert(i, russianReplacement);
                    }
                }
            }
        }
        return rebuildRussianText.toString();
    }

    private String replaceStressedLetters(String russianText) {
        return StressedWordConverter.removeStressMarks(russianText);
    }

    /**
     * Find if the given unstressed words have any same spelling but different stress pattern equivalents in the database.
     * If they do not have any, the word can easily be corrected. This method will keep track of the unstressed/stressed
     * pairs and also any words that cannot be corrected.
     * @param unstressedWords Words that could have a stress mark but are missing one.
     * @return An object that holds a Map which keeps track of the correctable unstressed/stressed pairs and also a list of
     * words that cannot be corrected.
     */
    StressLetterCorrections getUnstressedCorrections(List<String> unstressedWords) {
        StressLetterCorrections stressLetterCorrections = new StressLetterCorrections();

        unstressedWords.forEach((word) -> {
            List<String> forms = wordRetrievalService.findAllAccentedWordFormsByBare(word);

            if (!forms.isEmpty()) { // Unlikely to happen, but will prevent an out-of-bounds error
                boolean doAllFormsHaveSameAccented = forms.stream()
                        .allMatch((form) -> Objects.equals(form, forms.getFirst()));
                if (doAllFormsHaveSameAccented) { // Since every word in list have the same accent pattern the missing stress can easily be fixed
                    stressLetterCorrections.addCorrectableWord(word, forms.getFirst());
                } else { // cannot be easily fixed, lets just ignore it and let an error be logged later on.
                    stressLetterCorrections.addUncorrectableWord(word);
                }
            }
        });

        return stressLetterCorrections;
    }

    /**
     *
     * @param corrections An object with a Map of stressed/unstressed pairs and the list of words that cannot be corrected,
     *                    the list will not be used in this method.
     * @param originalText The text that has words with missing text, will be corrected in this method.
     * @return The complete text with the best attempt at adding stress to all words missing stress.
     */
    String addStressToWords(StressLetterCorrections corrections, String originalText) {
        StringBuilder corrected = new StringBuilder(originalText);

        for (Map.Entry<String, String> entry : corrections.correctableWords.entrySet()) {
            // loop through each character of the string looking for the first character of the key
            // when found, loop through key starting with the second character adding to the outer loops variable
            // here we are finding the start and end index of the substring so we can replace it with the string builder replace method
            char[] sentenceCharacters = corrected.toString().toCharArray();
            String unstressWord = entry.getKey();
            String stressedWord = entry.getValue();
            for (int sentenceIndex = 0; sentenceIndex < sentenceCharacters.length; sentenceIndex++) {
                boolean isWordFound = false;
                // current index of sentence matches the first letter that is unstressed, lets start seeing if we have a match
                if (sentenceCharacters[sentenceIndex] == unstressWord.charAt(0)) {
                    // some failguards to ensure we don't have an out-of-bounds error
                    if (sentenceIndex == sentenceCharacters.length - 1 || unstressWord.isEmpty())
                        break;
                    for (int wordIndex = 1; wordIndex < unstressWord.length(); wordIndex++) {
                        // a character found was not a character in the word, break from the loop and try again
                        if (sentenceCharacters[sentenceIndex + wordIndex] != unstressWord.charAt(wordIndex))
                            break;

                        // end of the word has been reached, we can now replace the unaccented words using the indices,
                        // but first we must check if it's actually the word and not appears to be the word inside another word
                        if (wordIndex == unstressWord.length() - 1) {
                            boolean isInnerWord = isValidAlphabeticCharacter(sentenceCharacters, sentenceIndex - 1) &&
                                    isValidAlphabeticCharacter(sentenceCharacters, sentenceIndex + wordIndex + 1);

                            if (isInnerWord)
                                break;

                            isWordFound = true;
                            corrected.replace(sentenceIndex, sentenceIndex + wordIndex + 1, stressedWord);
                            break;
                        }
                    }
                }

                if (isWordFound)
                    break;
            }
        }
        return corrected.toString();
    }

    /**
     * Finds any words that don't have stress but could be stressed.
     * @param text The Russian text that will be searched.
     * @return All words that are missing a stress mark.
     */
    List<String> findWordsWithMissingStress(String text) {
        // this creates a list of words that don't have a ', which indicates it's missing stress
        Matcher matcher = punctuationPattern.matcher(text);
        List<String> stresslessWords = Arrays.stream(matcher.replaceAll("").split(" "))
                .filter((word) -> !word.contains("'")) // add any words that do not have an apostrophe
                .filter((word) -> !word.equals("—")) // make sure we don't have an em dash in there
                .collect(Collectors.toList());

        // check to see if they only have one vowel, if it only has a single value it wouldn't need a stress mark
        for (int i = stresslessWords.size() - 1; i >= 0; i--) {
            int numberOfVowels = getNumberOfVowels(stresslessWords.get(i));

            // if there are more than 1 vowels or the word contains a Ё then it doesn't need a stress mark, remove it from the list
            if (numberOfVowels <= 1|| stresslessWords.get(i).toUpperCase().indexOf('Ё') > -1) {
                stresslessWords.remove(i);
            }
        }

        return stresslessWords;
    }

    private int getNumberOfVowels(String word) {
        char[] vowels = {'А', 'О', 'У', 'Ы', 'Э', 'Я', 'Ё', 'Ю', 'И', 'Е'};
        int numberOfVowels = 0;
        for (char letter : word.toUpperCase().toCharArray()) {
            for (char vowel : vowels) {  // loop through all the vowels and get the number of vowels in the word
                if (letter == vowel) {
                    numberOfVowels++;
                }
            }
        }

        return numberOfVowels;
    }

    private boolean isValidAlphabeticCharacter(char[] sentenceCharacters, int index) {
        return index >= 0 && index < sentenceCharacters.length && Character.isAlphabetic(sentenceCharacters[index]);
    }

    /**
     * Returns all words that have a stress mark that is built into a letter, this is primarily used in displaying error logs
     * for content generation.
     * @param text The text that contains a word with a stress mark.
     * @return List of all words that have a builtin stress mark.
     */
    private List<String> findWordsWithBuiltinStressedLetter(String text) {
        return Arrays.stream(splitWordsAndRemovePunctuation(text))
                .filter((word) -> {
                    Matcher matcher = cyrllicStressPattern.matcher(word);
                    return matcher.find();
                }).toList();
    }

    /**
     * Return all words that have latin letters, this is primarily used in displaying error logs
     * for content generation.
     * @param text The text that contains a word with latin letters
     * @return List of all words that have latin letters.
     */
    private List<String> findWordsWithLatinLetters(String text) {
        return Arrays.stream(splitWordsAndRemovePunctuation(text))
                .filter((word) -> {
                    Matcher matcher = latinLetterPattern.matcher(word);
                    return matcher.find();
                }).toList();
    }

    private String[] splitWordsAndRemovePunctuation(String text) {
        Matcher matcher = punctuationPattern.matcher(text);
        return matcher.replaceAll("").split(" ");
    }

    /**
     * Holds the corrections that will be to a sentence with words missing stress marks. The Map object holds the unstressed/stress
     * pair, which will make finding the word in the sentence, and then correcting it easier. The list of uncorrectable words
     * will be used for logging errors after the correction attempt has been made.
     */
    private static class StressLetterCorrections {
        private final Map<String, String> correctableWords = new HashMap<>();
        private final List<String> uncorrectable = new ArrayList<>();

        public void addCorrectableWord(String unstressed, String stressed) {
            correctableWords.put(unstressed, stressed);
        }

        public void addUncorrectableWord(String word) {
            uncorrectable.add(word);
        }

        public boolean canBeCompletelyCorrected() {
            return uncorrectable.isEmpty();
        }
    }
}
