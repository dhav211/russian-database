package com.havlin.daniel.russian.controllers.exercises;

import com.havlin.daniel.russian.services.exercises.AdjectiveNounCaseEndingExercise;
import com.havlin.daniel.russian.services.exercises.Exercise;
import com.havlin.daniel.russian.services.exercises.ExerciseService;
import com.havlin.daniel.russian.services.users.LearnedWordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/exercise")
public class ExerciseController {
    private final LearnedWordService learnedWordService;
    private final ExerciseService exerciseService;

    public ExerciseController(LearnedWordService learnedWordService, ExerciseService exerciseService) {
        this.learnedWordService = learnedWordService;
        this.exerciseService = exerciseService;
    }

    @GetMapping("/generate")
    public List<Exercise> generateExercises() {
        List<Exercise> exercises = exerciseService.createExercises();

        //AdjectiveNounCaseEndingExercise exercise1 = new AdjectiveNounCaseEndingExercise()

        return exercises;
    }
}
