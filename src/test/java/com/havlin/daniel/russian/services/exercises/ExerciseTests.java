package com.havlin.daniel.russian.services.exercises;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.services.dictionary.WordService;
import com.havlin.daniel.russian.services.retrieval.WordRetrievalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

@SpringBootTest
public class ExerciseTests {
    @Autowired
    private WordRetrievalService wordRetrievalService;

    @Autowired
    private  WordService wordService;

    @Test
    void adjectiveNounCaseEndingTests() {
        Random random = new Random();
        Word word1 = wordRetrievalService.getWordByAccentedTextForSentenceCreation("большо'й").get();
        AdjectiveNounCaseEndingExercise exercise1 = new AdjectiveNounCaseEndingExercise(word1, wordRetrievalService);
        exercise1.create();
        System.out.println(exercise1);

        Word word2 = wordRetrievalService.getWordByAccentedTextForSentenceCreation("второ'й").get();
        AdjectiveNounCaseEndingExercise exercise2 = new AdjectiveNounCaseEndingExercise(word2, wordRetrievalService);
        exercise2.create();
        System.out.println(exercise2);

        System.out.println("");
    }
}
