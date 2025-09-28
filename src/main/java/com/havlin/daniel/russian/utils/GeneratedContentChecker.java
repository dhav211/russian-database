package com.havlin.daniel.russian.utils;

import com.havlin.daniel.russian.entities.dictionary.WordForm;
import com.havlin.daniel.russian.repositories.dictionary.WordFormRepository;
import com.havlin.daniel.russian.repositories.dictionary.WordRepository;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GeneratedContentChecker {
    private static final Pattern latinLetterPattern = Pattern.compile(".*[a-zA-Z\\\\p{Punct}].*");
    private static final Pattern cyrllicStressPattern = Pattern.compile("[а-яёА-ЯЁ]́");
    public static String replaceEnglishCharacters(String russianText) {
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

    public static String replaceStressedLetters(String russianText) {
        return StressedWordConverter.removeStressMarks(russianText);
    }

    public static boolean doesSentenceContainLatinLetters(String russianText) {
        Matcher matcher = latinLetterPattern.matcher(russianText);
        return matcher.find();
    }

    public static boolean doesSentenceContainLettersWithStressMarks(String russianText) {
        Matcher matcher = cyrllicStressPattern.matcher(russianText);
        return matcher.find();
    }

    public static List<String> findWordsWithMissingStress(String russianText) {
        // this creates a list of words that don't have a ', which indicates it's missing stress
        List<String> stresslessWords = Arrays.stream(russianText.replaceAll("[!”#$%&()*+,./:]","").split(" "))
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


    /**
     * <p>Attempts to add the stress mark to a word with missing stress. This is only possible if there is only one word with the same wordID with this
     * word form. If all words with missing stress can be fixed, then this method will return the corrected text, if not it will return an empty string. </p>
     * @param wordFormRepository Repository from a Spring managed context
     * @param wordsMissingStress List of words missing their stress marks that will be fixed if possible
     * @param originalText The original russian text that had the words with missing stress
     * @return Corrected text if all missing stress could be added, empty string if not possible
     */
    public static String addStressToWords(WordFormRepository wordFormRepository, List<String> wordsMissingStress, String originalText) {
        List<String> addedStressWords = new ArrayList<>();
        Map<String, String> stressedUnstressPair = new HashMap<>();
        StringBuilder corrected = new StringBuilder(originalText);
        boolean canBeCorrected = false;

        // make a map, the key is the word with missing stress, the value will be an empty string by default
        // when a correction is made then set the accented version as the key

        for (String word : wordsMissingStress) {
            List<WordForm> forms = wordFormRepository.findAllByBare(word);

            if (forms.isEmpty()) // Unlikely to happen, but will prevent an out-of-bounds error
                break;


            boolean doAllFormsHaveSameAccented = forms.stream().allMatch((form) -> Objects.equals(form.getAccented(), forms.getFirst().getAccented()));

            if (!doAllFormsHaveSameAccented) { // more than word that has a word form spelt like this, lets just give up
                canBeCorrected = false;
                break;
            } else {
                addedStressWords.add(forms.getFirst().getAccented());
                stressedUnstressPair.put(word, forms.getFirst().getAccented());
                canBeCorrected = true;
            }
        }

        if (canBeCorrected) {
            for (Map.Entry<String, String> entry : stressedUnstressPair.entrySet()) {
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
        } else {
            return "";
        }
    }

    public static String removeSingleVowelStresses(String russianText) {
        return Arrays.stream(russianText.split(" ")).map((word) -> {
            if (getNumberOfVowels(word) == 1 && word.contains("'")) {
                StringBuilder corrected = new StringBuilder(word);
                int stressIndex = word.indexOf('\'');
                corrected.deleteCharAt(stressIndex);
                return corrected.toString();
            } else {
                return word;
            }
        }).collect(Collectors.joining(" "));
    }

    private static int getNumberOfVowels(String word) {
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

    private static boolean isValidAlphabeticCharacter(char[] sentenceCharacters, int index) {
        return index >= 0 && index < sentenceCharacters.length && Character.isAlphabetic(sentenceCharacters[index]);
    }
}
