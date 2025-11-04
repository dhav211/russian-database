package com.havlin.daniel.russian.utils;

import org.jetbrains.annotations.NotNull;

public class StressedWordConverter {
    private static final String[] stressedLetters = {"А́", "Б́", "В́", "Ѓ", "Д́", "Е́", "Ё", "Ж́", "З́", "И́", "Й́", "Ќ", "Л́", "М́", "Н́",
            "О́", "П́", "Р́", "С́", "Т́", "У́", "Ф́", "Х́", "Ц́", "Ч́", "Ш́", "Щ́", "Ъ́", "Ы́", "Ь́", "Э́", "Ю́", "Я́", "а́", "б́", "в́", "ѓ",
            "д́", "е́", "ё", "ж́", "з́", "и́", "й́", "ќ", "л́", "м́", "н́", "о́", "п́", "р́", "с́", "т́", "у́", "ф́", "х́", "ц́", "ч́", "ш́",
            "щ́", "ъ́", "ы́", "ь́", "э́", "ю́", "я́"};

    public static String addStressMarks(@NotNull String word) {
        StringBuilder stringBuilder = new StringBuilder(word);

        // Loop through the string until we find the ' indicating the stress
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == '\'') {
                char unstressed = word.charAt(i - 1);
                // remove the letter that is stressed and the ' from the string
                if (i + 1 <= word.length() - 1) { // the stress mark is at the start or middle of the word
                    stringBuilder.delete(i - 1, i + 1);
                } else { // stress mark is at the end, we can't use delete, or we will experience overflow errors
                    stringBuilder.setLength(word.length() - 2);
                }

                // insert the stressed letter combination here
                stringBuilder.insert(i - 1, getStressedVariant(unstressed));
                break;
            }
        }

        return stringBuilder.toString();
    }

    public static String removeStressMarks(@NotNull String stressedWord) {
        StringBuilder stringBuilder = new StringBuilder(stressedWord);

        // Loop through each of the possible stress letters, we will attempt to remove any possible occurrence of this
        // letter in the sentence. The while loop will repeatably attempt to remove the letter, and if one isn't located
        // it will break from the inner loop and go to the next letter
        for (String letter : stressedLetters) {
            while (stringBuilder.toString().contains(letter)) {
                int index = stringBuilder.toString().indexOf(letter);
                stringBuilder.replace(index, index + letter.length(), getUnstressedVariant(letter));
            }
        }

        return stringBuilder.toString();
    }

    private static String getStressedVariant(char unstressedVariant) {
        return switch (unstressedVariant) {
            case 'Е' -> "Е́";
            case 'А' -> "А́";
            case 'И' -> "И́";
            case 'О' -> "О́";
            case 'У' -> "У́";
            case 'Ы' -> "Ы́";
            case 'Э' -> "Э́";
            case 'Ю' -> "Ю́";
            case 'Я' -> "Я́";
            case 'а' -> "а́";
            case 'е' -> "е́";
            case 'и' -> "и́";
            case 'о' -> "о́";
            case 'у' -> "у́";
            case 'ы' -> "ы́";
            case 'э' -> "э́";
            case 'ю' -> "ю́";
            case 'я' -> "я́";
            default -> "";
        };
    }

    private static String getUnstressedVariant(String stressedVariant) {
        return switch (stressedVariant) {
            case "Е́" -> "Е'";
            case "А́" -> "А'";
            case "И́" -> "И'";
            case "О́" -> "О'";
            case "У́" -> "У'";
            case "Ы́" -> "Ы'";
            case "Э́" -> "Э'";
            case "Ю́" -> "Ю'";
            case "Я́" -> "Я'";
            case "а́" -> "а'";
            case "е́" -> "е'";
            case "и́" -> "и'";
            case "о́" -> "о'";
            case "у́" -> "у'";
            case "ы́" -> "ы'";
            case "э́" -> "э'";
            case "ю́" -> "ю'";
            case "я́" -> "я'";
            // Constants just in case the AI generator is off it's rocker
            case "Б́" -> "Б";
            case "б́" -> "б";
            case "В́" -> "В";
            case "в́" -> "в";
            case "Ѓ" -> "Г";
            case "ѓ" -> "г";
            case "Д́" -> "Д";
            case "д́" -> "д";
            case "Ж́" -> "Ж";
            case "ж́" -> "ж";
            case "З́" -> "З";
            case "з́" -> "з";
            case "Й́" -> "Й";
            case "й́" -> "й";
            case "Ќ" -> "К";
            case "ќ" -> "к";
            case "Л́" -> "Л";
            case "л́" -> "л";
            case "М́" -> "М";
            case "м́" -> "м";
            case "Н́" -> "Н";
            case "н́" -> "н";
            case "П́" -> "П";
            case "п́" -> "п";
            case "Р́" -> "Р";
            case "р́" -> "р";
            case "С́" -> "С";
            case "с́" -> "с";
            case "Т́" -> "Т";
            case "т́" -> "т";
            case "Ф́" -> "Ф";
            case "ф́" -> "ф";
            case "Х́" -> "Х";
            case "х́" -> "х";
            case "Ц́" -> "Ц";
            case "Ч́" -> "Ч";
            case "ч́" -> "ч";
            case "ц́" -> "ц";
            case "Ш́" -> "Ш";
            case "ш́" -> "ш";
            case "Щ́" -> "Щ";
            case "щ́" -> "щ";
            case "Ъ́" -> "Ъ";
            case "ъ́" -> "ъ";
            case "Ь́" -> "Ь";
            case "ь́" -> "ь";
            default -> "";
        };
    }
}