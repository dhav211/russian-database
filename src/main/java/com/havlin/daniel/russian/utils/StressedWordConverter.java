package com.havlin.daniel.russian.utils;

public class StressedWordConverter {
    public static String addStressMarks(String word) {
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

    public static String removeStressMarks(String stressedWord) {
        String[] containingLetters = { "Е́", "А́", "И́", "О́", "У́", "Ы́", "Э́", "Ю́", "Я́", "а́", "е́", "и́", "о́", "у́", "ы́", "э́", "ю́", "я́" };
        StringBuilder stringBuilder = new StringBuilder(stressedWord);

        for (String letter : containingLetters) {
            if (stressedWord.contains(letter)) {
                int index = stressedWord.indexOf(letter);
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
            default -> "";
        };
    }

    /*
 ( with acute accent)
 (о with acute accent)
 (у with acute accent)
 ( with acute accent)
 (э with acute accent)
( with acute accent)
( with acute accent)
     */
}
