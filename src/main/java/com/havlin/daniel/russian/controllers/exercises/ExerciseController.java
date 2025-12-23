package com.havlin.daniel.russian.controllers.exercises;

import com.havlin.daniel.russian.entities.users.User;
import com.havlin.daniel.russian.services.exercises.Exercise;
import com.havlin.daniel.russian.services.exercises.ExerciseService;
import com.havlin.daniel.russian.services.users.LearnedWordService;
import com.havlin.daniel.russian.services.users.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UserService userService;

    public ExerciseController(LearnedWordService learnedWordService, ExerciseService exerciseService,
                              UserService userService) {
        this.learnedWordService = learnedWordService;
        this.exerciseService = exerciseService;
        this.userService = userService;
    }

    @GetMapping("/generate")
    public ResponseEntity<List<Exercise>> generateExercises() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = null;

        if (principal instanceof UserDetails) {
            currentUser = userService.loadDatabaseUserByUsername(((UserDetails) principal).getUsername());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
        }

        List<Exercise> exercises = exerciseService.createExercises(currentUser);

        //AdjectiveNounCaseEndingExercise exercise1 = new AdjectiveNounCaseEndingExercise()

        return ResponseEntity.ok(exercises);
    }
}
