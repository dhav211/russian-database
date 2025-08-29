package com.havlin.daniel.russian.entities.dictionary;

import jakarta.persistence.*;

@Entity
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bare;

    private String accented;

    @Enumerated(EnumType.STRING)
    private WordType type;

    @Enumerated(EnumType.STRING)
    private WordLevel wordLevel;

    public Long getId() {
        return id;
    }
}
