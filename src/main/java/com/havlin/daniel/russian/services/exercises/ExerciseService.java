package com.havlin.daniel.russian.services.exercises;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExerciseService {
    public List<ExerciseData> createExercises() {
        // Choose 5 words from user dictionary
        // create 5 exercises for each word
        // each exercise should get more difficult each time, for example we will start with matching,
            // then move onto case ending, then to fill in the blank
        // these exercise data objects will be sent as a get request and the front end will be able to create
        // exercises based on the json objects
        return new ArrayList<>();
    }
}
