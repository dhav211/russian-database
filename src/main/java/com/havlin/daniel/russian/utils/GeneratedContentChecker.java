package com.havlin.daniel.russian.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneratedContentChecker {
    public static String replaceEnglishCharacters(String russianText) {
        StringBuilder rebuildRussianText = new StringBuilder(russianText);
        char[] englishLetters = {'A', 'a', 'B', 'b', 'C', 'c', 'O', 'o', 'E', 'e', 'Y', 'y', 'R', 'r', 'S', 's', 'T', 't', 'K', 'k', 'M', 'm', 'P', 'p'}; // Possible latin letters claude likes to mix up
        for (char c : englishLetters) {
            int charIndex = rebuildRussianText.toString().indexOf(c); // check to see if this char is found in the sentence
            if (charIndex >= 0) { // if the char is not in the sentence it will return -1, otherwise it will return the index
                for (int i = charIndex; i < rebuildRussianText.toString().length(); i++) {
                    if (rebuildRussianText.toString().charAt(i) == c) { // first round will always be true, however as it goes through the loop it won't always be true, if ever
                        char russianReplacement = 'a';

                        switch (c) {
                            case 'A' -> russianReplacement = 'А';
                            case 'a' -> russianReplacement = 'а';
                            case 'B' -> russianReplacement = 'В';
                            case 'b' -> russianReplacement = 'в';
                            case 'O' -> russianReplacement = 'О';
                            case 'o' -> russianReplacement = 'о';
                            case 'E' -> russianReplacement = 'Е';
                            case 'e' -> russianReplacement = 'е';
                            case 'C', 'S' -> russianReplacement = 'С';
                            case 'c', 's' -> russianReplacement = 'с';
                            case 'R', 'P' -> russianReplacement = 'Р';
                            case 'r', 'p' -> russianReplacement = 'р';
                            case 'Y' -> russianReplacement = 'У';
                            case 'y' -> russianReplacement = 'у';
                            case 'K' -> russianReplacement = 'К';
                            case 'k' -> russianReplacement = 'к';
                            case 'M' -> russianReplacement = 'М';
                            case 'm' -> russianReplacement = 'м';
                        }

                        rebuildRussianText.setCharAt(i, russianReplacement);
                    }
                }
            }
        }
        return rebuildRussianText.toString();
    }

    public static String replaceStressedLetters(String russianText) {
        return StressedWordConverter.removeStressMarks(russianText);
    }

    public static boolean sentenceContainsLatinLetters(String russianText) {
        Pattern pattern = Pattern.compile(".*[a-zA-Z\\\\p{Punct}].*");
        Matcher matcher = pattern.matcher(russianText);
        return matcher.find();
    }

    public static boolean sentenceContainsLettersWithStressMarks(String russianText) {
        Pattern pattern = Pattern.compile("[а-яёА-ЯЁ]́");
        Matcher matcher = pattern.matcher(russianText);
        return matcher.find();
    }
}
