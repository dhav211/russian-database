package com.havlin.daniel.russian.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneratedContentChecker {
    private static final Pattern latinLetterPattern = Pattern.compile(".*[a-zA-Z\\\\p{Punct}].*");
    private static final Pattern cyrllicStressPattern = Pattern.compile("[а-яёА-ЯЁ]́");

    public static boolean doesSentenceContainLatinLetters(String russianText) {
        Matcher matcher = latinLetterPattern.matcher(russianText);
        return matcher.find();
    }

    public static boolean doesSentenceContainLettersWithBuiltinStressMarks(String russianText) {
        Matcher matcher = cyrllicStressPattern.matcher(russianText);
        return matcher.find();
    }

    public static boolean isOverLatinLetterThreshold(String sentence, double threshold) {
        double latinLetterSum = 0;
        for (char letter : sentence.toCharArray()) {
            if ((letter >= 'a' && letter <= 'z') || (letter >= 'A' && letter <= 'Z')) {
                latinLetterSum++;
            }
        }

        int length = sentence.length();
        double percentage = latinLetterSum / sentence.length();

        return percentage > threshold;
    }
}
