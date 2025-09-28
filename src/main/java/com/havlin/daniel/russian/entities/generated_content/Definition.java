package com.havlin.daniel.russian.entities.generated_content;

import com.havlin.daniel.russian.entities.dictionary.Word;
import jakarta.persistence.*;

@Entity
public class Definition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String shortDefinition;

    @Lob
    private String longDefinition;

    @OneToOne(mappedBy = "definition")
    private Word word;

    public Long getId() {
        return id;
    }

    public String getShortDefinition() {
        return shortDefinition;
    }

    public void setShortDefinition(String shortDefinition) {
        this.shortDefinition = shortDefinition;
    }

    public String getLongDefinition() {
        return longDefinition;
    }

    public void setLongDefinition(String longDefinition) {
        this.longDefinition = longDefinition;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }
}
