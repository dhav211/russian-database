package com.havlin.daniel.russian.services.exercises;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.dictionary.WordForm;

import java.util.*;

class CaseEndingExercise implements Exercise{
    private List<ExerciseDeclensionTableRow> declensionTableRows;
    private Word word;
    private Random random;

    public CaseEndingExercise(Word word, Random random) {
        this.word = word;
        this.random = random;
    }

    @Override
    public void create() {
        declensionTableRows = new ArrayList<>();

        // There is a chance a word won't have any word forms, so we can just exit from this exercise
        if (word.getNoun().isIndeclinable()) {
            throw new FailedToCreateExerciseException(word.getBare() + " is not declinable, exercise cannot be created.");
        }

        // For this exercise we will only give the table for the plural or singular forms
        // There are words that can't be in one form or the other so let's test that here
        if (word.getNoun().isSingularOnly()) {
            declensionTableRows = getForms(GrammaticalNumber.SINGULAR);
        } else if (word.getNoun().isPluralOnly()) {
            declensionTableRows = getForms(GrammaticalNumber.PLURAL);
        } else {
            // most words can in both forms so let's just choose one at random
            boolean isRandomSingular = random.nextBoolean();
            declensionTableRows = isRandomSingular ? getForms(GrammaticalNumber.SINGULAR) : getForms(GrammaticalNumber.PLURAL);
        }

        // exit the exercise if for some chance we didn't fill the table
        if (declensionTableRows.isEmpty()) {
            throw new FailedToCreateExerciseException("Couldn't fill declension table with word forms for the word " + word.getBare());
        }

        // choose 3 indexes from the declension table, we will do this at random
        List<Integer> randomWordFormIndexes = new ArrayList<>();

        while (randomWordFormIndexes.size() < 3) {
            int randomChoice = random.nextInt(6);
            if (!randomWordFormIndexes.contains(randomChoice)) {
                randomWordFormIndexes.add(randomChoice);
            }
        }

        // loop through each of the random indexes, choose from the declension table with that index and set the
        // isFilled boolean to true, this show the word to the user instead of leaving it blank
        for (Integer index : randomWordFormIndexes) {
            declensionTableRows.get(index).setAlreadyFilled(true);
        }
    }

    private List<ExerciseDeclensionTableRow> getForms(GrammaticalNumber grammaticalNumber) {
        List<ExerciseDeclensionTableRow> rows = new ArrayList<>();
        Map<String, List<String>> typesAndForms = new HashMap<>();
        String containsSearch = grammaticalNumber == GrammaticalNumber.SINGULAR ? "_sg_" : "_pl_";

        for (WordForm wordForm : word.getWordForms()) {
            if (wordForm.getFormType().contains(containsSearch)) {
                if (typesAndForms.containsKey(wordForm.getFormType())) {
                    typesAndForms.get(wordForm.getFormType()).add(wordForm.getBare());
                } else {
                    List<String> formTypes = new ArrayList<>();
                    formTypes.add(wordForm.getBare());
                    typesAndForms.put(wordForm.getFormType(), formTypes);
                }
            }
        }

        for (Map.Entry<String, List<String>> entry : typesAndForms.entrySet()) {
            rows.add(new ExerciseDeclensionTableRow(entry.getKey(), entry.getValue(), false));
        }

        return rows;
    }

    public List<ExerciseDeclensionTableRow> getDeclensionTableRows() {
        return declensionTableRows;
    }

    private enum GrammaticalNumber {
        SINGULAR,
        PLURAL
    }
}
