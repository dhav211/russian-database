package com.havlin.daniel.russian.services.exercises;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "objectType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = FillInTheBlankExercise.class, name = "FillInTheBlank"),
})
public interface Exercise {
    /**
     * Builds the exercise and sets all the data.
     */
    void create();
}
