package com.havlin.daniel.russian.entities.generated_content;

import jakarta.persistence.*;

@Entity
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String text;

    private int part;

    // many-to-one storyQuestions

}
