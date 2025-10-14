package com.havlin.daniel.russian.utils;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.dictionary.WordForm;
import com.havlin.daniel.russian.repositories.dictionary.WordFormRepository;
import com.havlin.daniel.russian.repositories.dictionary.WordRepository;
import com.havlin.daniel.russian.services.generated_content.GeneratedContentErrorMessage;
import com.havlin.daniel.russian.services.generated_content.GeneratedContentErrorType;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GeneratedContentChecker {
    private static final Pattern latinLetterPattern = Pattern.compile(".*[a-zA-Z\\\\p{Punct}].*");
    private static final Pattern cyrllicStressPattern = Pattern.compile("[а-яёА-ЯЁ]́");

    public static boolean doesSentenceContainLatinLetters(String russianText) {
        Matcher matcher = latinLetterPattern.matcher(russianText);
        return matcher.find();
    }

    public static boolean doesSentenceContainLettersWithStressMarks(String russianText) {
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
