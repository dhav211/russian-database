package com.havlin.daniel.russian.services.exercises;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.dictionary.WordType;
import com.havlin.daniel.russian.entities.users.LearnedWord;
import com.havlin.daniel.russian.entities.users.SecurityUser;
import com.havlin.daniel.russian.entities.users.User;
import com.havlin.daniel.russian.entities.users.UserRole;
import com.havlin.daniel.russian.services.dictionary.WordService;
import com.havlin.daniel.russian.services.retrieval.WordRetrievalService;
import com.havlin.daniel.russian.services.users.LearnedWordService;
import com.havlin.daniel.russian.services.users.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
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

    @Autowired
    private UserService userService;

    @Autowired
    private LearnedWordService learnedWordService;

    @Test
    @Transactional
    public void addWordsToUserDictionary() {
        User testUser = userService.createUser("learnedword_tester5782", "password",
                "tttesstt4234@gmail.com", UserRole.BASIC);
        Long[] wordIds = {
                114L, 120L, 121L, 123L, 127L, 154L, 158L, 164L, 165L, 179L, 213L, 214L, 215L, 236L, 269L, 272L,
                274L, 291L, 305L, 309L, 334L, 444L, 457L, 489L, 521L, 548L, 639L, 688L, 729L, 731L
        };

        for (Long id : wordIds) {
            LearnedWord learnedWord = learnedWordService.createLearnedWord(testUser, id);
            Assertions.assertNotNull(learnedWord);
        }

        Assertions.assertFalse(testUser.getDictionary().isEmpty());
    }

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

    @Test
    void fillInTheBlank() {
        Random random = new Random();
        User user = userService.loadDatabaseUserByUsername("test_man");
        Word word1 = wordRetrievalService.getWordByAccentedTextForSentenceCreation("сторона'").get();
        FillInTheBlankExercise fillInTheBlankExercise1 = new FillInTheBlankExercise(word1, random, user.getDictionary(), wordRetrievalService);
        fillInTheBlankExercise1.create();
    }
}
