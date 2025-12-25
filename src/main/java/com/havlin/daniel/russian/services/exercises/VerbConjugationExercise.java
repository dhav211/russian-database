package com.havlin.daniel.russian.services.exercises;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.dictionary.WordForm;

import java.util.*;

class VerbConjugationExercise implements Exercise{
    private final List<ExerciseMorphTableRow> conjugationTableRows;
    private final Word word;
    private final Random random;

    VerbConjugationExercise(Word word, Random random) {
        this.conjugationTableRows = new ArrayList<>();
        this.word = word;
        this.random = random;
    }

    @Override
    public void create() {
        // Confirm the word is set, if not throw an error
        if (word == null) {
            throw new FailedToCreateExerciseException("There is no word to create exercise");
        }

        // if the word isn't a verb, throw an error
        if (word.getVerb() == null) {
            throw new FailedToCreateExerciseException(word.getBare() + " is not a verb");
        }

        // fill the conjugation table with all the verbs word forms
        conjugationTableRows.addAll(ExerciseUtils.getForms(ExerciseUtils.WordFormTypeSearchTerm.VERB, word));

        // reject the exercise if we couldn't fill the conjugation table
        if (conjugationTableRows.isEmpty()) {
            throw new FailedToCreateExerciseException("Couldn't fill conjugation table with word forms from the word " + word.getBare());
        }

        try {
            ExerciseUtils.setRandomRowsAsFilled(conjugationTableRows, random, 2);
        } catch (IndexOutOfBoundsException e) {
            throw new FailedToCreateExerciseException("Failed to create exercise, went out of bounds of table");
        }
    }

    public List<ExerciseMorphTableRow> getConjugationTableRows() {
        return conjugationTableRows;
    }
}
