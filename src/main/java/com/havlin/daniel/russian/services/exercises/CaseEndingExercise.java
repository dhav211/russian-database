package com.havlin.daniel.russian.services.exercises;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.dictionary.WordForm;

import java.util.*;

class CaseEndingExercise implements Exercise{
    private List<ExerciseMorphTableRow> declensionTableRows;
    private final Word word;
    private final Random random;

    public CaseEndingExercise(Word word, Random random) {
        this.word = word;
        this.random = random;
    }

    @Override
    public void create() {
        declensionTableRows = new ArrayList<>();

        if (word == null) {
            throw new FailedToCreateExerciseException("There is no word to create exercise");
        }

        if (word.getNoun() == null) {
            throw new FailedToCreateExerciseException(word.getBare() + " is not a noun");
        }

        // There is a chance a word won't have any word forms, so we can just exit from this exercise
        if (word.getNoun().isIndeclinable()) {
            throw new FailedToCreateExerciseException(word.getBare() + " is not declinable, exercise cannot be created.");
        }

        // For this exercise we will only give the table for the plural or singular forms
        // There are words that can't be in one form or the other so let's test that here
        if (word.getNoun().isSingularOnly()) {
            declensionTableRows = ExerciseUtils.getForms(ExerciseUtils.WordFormTypeSearchTerm.SINGULAR, word);
        } else if (word.getNoun().isPluralOnly()) {
            declensionTableRows = ExerciseUtils.getForms(ExerciseUtils.WordFormTypeSearchTerm.PLURAL, word);
        } else {
            // most words can in both forms so let's just choose one at random
            boolean isRandomSingular = random.nextBoolean();
            declensionTableRows = isRandomSingular
                    ? ExerciseUtils.getForms(ExerciseUtils.WordFormTypeSearchTerm.SINGULAR, word)
                    : ExerciseUtils.getForms(ExerciseUtils.WordFormTypeSearchTerm.PLURAL, word);
        }

        // exit the exercise if for some chance we didn't fill the table
        if (declensionTableRows.isEmpty()) {
            throw new FailedToCreateExerciseException("Couldn't fill declension table with word forms for the word " + word.getBare());
        }

        // Mark the fields the user needs to fill out
        try {
            ExerciseUtils.setRandomRowsAsFilled(declensionTableRows, random, 3);
        } catch (IndexOutOfBoundsException e) {
            throw new FailedToCreateExerciseException("Failed to create exercise, went out of bounds of table");
        }
    }

    public List<ExerciseMorphTableRow> getDeclensionTableRows() {
        return declensionTableRows;
    }
}
