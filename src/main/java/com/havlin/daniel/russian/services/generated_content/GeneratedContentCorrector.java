package com.havlin.daniel.russian.services.generated_content;

import com.havlin.daniel.russian.repositories.dictionary.WordFormRepository;
import com.havlin.daniel.russian.utils.GeneratedContentChecker;
import com.havlin.daniel.russian.utils.StressedWordConverter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Corrects any of the obvious issues with LLM generation, such as latin letters, missing stress, and a built-in stress
 * mark that can't be saved in a database properly. If the correction cannot be made it will log the error for easier
 * human intervention.
 */
class GeneratedContentCorrector {
    private final WordFormRepository wordFormRepository;

    private static final Pattern latinLetterPattern = Pattern.compile(".*[a-zA-Z\\\\p{Punct}].*");
    private static final Pattern cyrllicStressPattern = Pattern.compile("[а-яёА-ЯЁ]́");

    public GeneratedContentCorrector(WordFormRepository wordFormRepository) {
        this.wordFormRepository = wordFormRepository;
    }

    CorrectedContent correctMissingStressMark(String text) {
        List<GeneratedContentErrorMessage> errors = new ArrayList<>();
        List<String> wordsMissingStress = findWordsWithMissingStress(text);
        if (!wordsMissingStress.isEmpty()) {
            StressLetterCorrections stressLetterCorrections = getUnstressedCorrections(wordsMissingStress);
            text = addStressToWords(stressLetterCorrections, text);

            if (!stressLetterCorrections.canBeCompletelyCorrected()) {
                stressLetterCorrections.uncorrectable.forEach((uncorrectable) -> {
                    errors.add(new GeneratedContentErrorMessage(GeneratedContentErrorType.MISSING_STRESS_MARK,
                            uncorrectable + " is missing a stress mark"));
                });
            }
        }

        return new CorrectedContent(errors, text);
    }

    CorrectedContent correctLatinLettersUsedAsCyrillic(String text) {
        List<GeneratedContentErrorMessage> errors = new ArrayList<>();
        if (GeneratedContentChecker.doesSentenceContainLatinLetters(text))
        {
            List<String> wordsWithLatinLetters = findWordsWithLatinLetters(text);
            text = replaceEnglishCharacters(text);

            wordsWithLatinLetters.forEach((wordWithLatinLetters) -> {
                if (!wordFormRepository.existsByAccented(wordWithLatinLetters)) { // even after being fixed the word still does not exist
                    errors.add(new GeneratedContentErrorMessage(GeneratedContentErrorType.LATIN_LETTERS,
                            "Latin letters were found in the word " + wordWithLatinLetters + " and it couldn't be corrected."));
                }
            });
        }

        return new CorrectedContent(errors, text);
    }

    CorrectedContent correctBuiltinStressMarks(String text) {
        List<GeneratedContentErrorMessage> errors = new ArrayList<>();

        if (GeneratedContentChecker.doesSentenceContainLettersWithBuiltinStressMarks(text)) {
            List<String> stressedWords = findWordsWithBuiltinStressedLetter(text);
            text = replaceStressedLetters(text);

            stressedWords.forEach((stressedWord) -> {
                if (!wordFormRepository.existsByAccented(replaceStressedLetters(stressedWord))) { // even after being fixed the word still does not exist
                    List<String> forms = wordFormRepository.findAccentedByBare(stressedWord.replace("'", ""));
                    boolean isNewStressFound = false;
                    if (!forms.isEmpty()) {
                        boolean doAllFormsHaveSameAccented = forms.stream()
                                .allMatch((form) -> Objects.equals(form, forms.getFirst()));

                        if (doAllFormsHaveSameAccented) {
                            isNewStressFound = true;
                            stressedWord = forms.getFirst();
                        }
                    }
                    if (!isNewStressFound) {
                        errors.add(new GeneratedContentErrorMessage(GeneratedContentErrorType.STRESS_MARK_ON_LETTER,
                                "There was a stress mark above a letter in the word " + stressedWord + " and it couldn't be corrected."));
                    }
                }
            });
        }

        return new CorrectedContent(errors, text);
    }

    CorrectedContent correctSingleVowelStresses(String text) {
        return new CorrectedContent(new ArrayList<>(), Arrays.stream(text.split(" ")).map((word) -> {
            if (getNumberOfVowels(word) == 1 && word.contains("'")) {
                StringBuilder corrected = new StringBuilder(word);
                int stressIndex = word.indexOf('\'');
                corrected.deleteCharAt(stressIndex);
                return corrected.toString();
            } else {
                return word;
            }
        }).collect(Collectors.joining(" ")));
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
            List<String> forms = wordFormRepository.findAccentedByBare(word);

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
        List<String> stresslessWords = Arrays.stream(text.replaceAll("[!”#$%&()*+,./:]","").split(" "))
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
        return Arrays.stream(splitWordsAndRemovePunc(text))
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
        return Arrays.stream(splitWordsAndRemovePunc(text))
                .filter((word) -> {
                    Matcher matcher = latinLetterPattern.matcher(word);
                    return matcher.find();
                }).toList();
    }

    private String[] splitWordsAndRemovePunc(String text) {
        return text.replaceAll("[!”#$%&()*+,./:]","").split(" ");
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
