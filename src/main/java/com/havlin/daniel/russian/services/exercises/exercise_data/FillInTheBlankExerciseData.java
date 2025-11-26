package com.havlin.daniel.russian.services.exercises.exercise_data;

import com.havlin.daniel.russian.entities.dictionary.WordType;
import com.havlin.daniel.russian.services.exercises.ExerciseData;
import com.havlin.daniel.russian.services.exercises.ExerciseType;

public class FillInTheBlankExerciseData extends ExerciseData {
    public FillInTheBlankExerciseData(ExerciseType exerciseType, String word, WordType wordType) {
        this.exerciseType = exerciseType;
        this.word = word;
        this.wordType = wordType;
    }
    /*
        PUT THIS SOMEWHERE!!!

        public void setWordPosition(String sentence, String wordForm) {
        // If this remains as -1 it will mean an error occurred and sentence will be rejected
        wordPosition = -1;
        // Remove the punctuation from the sentence. Not all of it as some will still be needed for word detection
        sentence = sentence.replaceAll("[.,!?]", "").toUpperCase();
        // Break down sentence into individual words and loop through and find the match with word form
        String[] sentenceSplit = sentence.split(" ");
        for (int i = 0; i < sentenceSplit.length; i++) {
            if (Objects.equals(sentenceSplit[i], wordForm)) {
                wordPosition = i;
                break;
            }
        }
    }
     */
}
