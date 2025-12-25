package com.havlin.daniel.russian.services.exercises;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.dictionary.WordForm;
import org.jetbrains.annotations.NotNull;

import java.util.*;

class ExerciseUtils {
    enum WordFormTypeSearchTerm {
        VERB,
        SINGULAR,
        PLURAL
    }

    /**
     * Put the word forms in a more accessible state for exercises that require users to fill out missing word forms.
     * This method will add all word form types from the word that match the search term.
     * @param searchTerm An enum that will be converted to a string which will be used in a contains search
     * @param word The word which be used to get all the word forms from
     * @return A table of word forms which will be easily from in a JSON return for the exercise
     */
    static List<ExerciseMorphTableRow> getForms(WordFormTypeSearchTerm searchTerm, Word word) {
        List<ExerciseMorphTableRow> rows = new ArrayList<>();
        Map<String, List<String>> typesAndForms = new HashMap<>();

        String containingSearch = convertWordFormTypeSearchTermToStringSearch(searchTerm);

        for (WordForm wordForm : word.getWordForms()) {
            if (wordForm.getFormType().contains(containingSearch)) {
                if (typesAndForms.containsKey(wordForm.getFormType())) {
                    typesAndForms.get(wordForm.getFormType()).add(wordForm.getAccented());
                } else {
                    List<String> formTypes = new ArrayList<>();
                    formTypes.add(wordForm.getAccented());
                    typesAndForms.put(wordForm.getFormType(), formTypes);
                }
            }
        }

        for (Map.Entry<String, List<String>> entry : typesAndForms.entrySet()) {
            rows.add(new ExerciseMorphTableRow(entry.getKey(), entry.getValue(), false));
        }

        return rows;
    }

    /**
     * Set rows of a table as already filled so the user won't need to fill it during the exercise
     * @param exerciseTable The morph table for the word forms
     * @param random Used to set a random int
     * @param amount The amount of rows that will be already filled for user
     */
    static void setRandomRowsAsFilled(List<ExerciseMorphTableRow> exerciseTable, Random random, int amount) {
        // choose indexes from the declension table, we will do this at random
        List<Integer> randomWordFormIndexes = new ArrayList<>();

        // Clamp the amount so it won't below 1 or over the table size, causing out of bounds errors
        amount = Math.clamp(amount, 1, exerciseTable.size());

        while (randomWordFormIndexes.size() < amount) {
            int randomChoice = random.nextInt(exerciseTable.size());
            if (!randomWordFormIndexes.contains(randomChoice)) {
                randomWordFormIndexes.add(randomChoice);
            }
        }

        // loop through each of the random indexes, choose from the declension table with that index and set the
        // isFilled boolean to true, this show the word to the user instead of leaving it blank
        for (Integer index : randomWordFormIndexes) {
            exerciseTable.get(index).setAlreadyFilled(true);
        }
    }

    static private String convertWordFormTypeSearchTermToStringSearch(@NotNull WordFormTypeSearchTerm wordFormTypeSearchTerm) {
        switch (wordFormTypeSearchTerm) {
            case VERB -> {
                return "_presfut_";
            }
            case PLURAL -> {
                return "_pl_";
            }
            case SINGULAR -> {
                return "_sg_";
            }
        }

        return "";
    }
}
