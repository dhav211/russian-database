package com.havlin.daniel.russian.services.exercises;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.users.User;
import com.havlin.daniel.russian.services.dictionary.WordService;
import com.havlin.daniel.russian.services.retrieval.WordRetrievalService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ExerciseService {
    private final Random random;
    private final WordRetrievalService wordRetrievalService;
    private final WordService wordService;

    public ExerciseService(WordService wordService, WordRetrievalService wordRetrievalService) {
        this.wordService = wordService;
        this.wordRetrievalService = wordRetrievalService;
        this.random = new Random();
    }

    public List<Exercise> createExercises(User user) {
        // Choose 5 words from user dictionary
        // create 5 exercises for each word
        // each exercise should get more difficult each time, for example we will start with matching,
            // then move onto case ending, then to fill in the blank
        // these exercise data objects will be sent as a get request and the front end will be able to create
        // exercises based on the json objects
        List<Exercise> exercises = new ArrayList<>();

        try {
            Word word1 = wordRetrievalService.getWordByAccentedTextForSentenceCreation("большо'й").get();
            AdjectiveNounCaseEndingExercise exercise1 = new AdjectiveNounCaseEndingExercise(word1, wordRetrievalService);
            exercise1.create();
            exercises.add(exercise1);

            Word word2 = wordRetrievalService.getWordByAccentedTextForSentenceCreation("коне'ц").get();
            FillInTheBlankExercise exercise2 = new FillInTheBlankExercise(word2, random, user.getDictionary(), wordRetrievalService);
            exercise2.create();
            exercises.add(exercise2);
        } catch (FailedToCreateExerciseException e) {
            System.out.println(e.getMessage());
        }

        return exercises;
    }
}
