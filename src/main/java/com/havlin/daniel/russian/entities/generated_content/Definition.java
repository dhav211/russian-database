package com.havlin.daniel.russian.entities.generated_content;

import com.havlin.daniel.russian.entities.dictionary.Word;
import jakarta.persistence.*;

@Entity
public class Definition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String text;

    @ManyToOne
    @JoinColumn(name = "word_id", nullable = false)
    private Word word;

    @Enumerated(EnumType.STRING)
    private GeneratedContentStatus status;

    public Long getId() {
        return id;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public GeneratedContentStatus getStatus() {
        return status;
    }

    public void setStatus(GeneratedContentStatus status) {
        this.status = status;
    }
}
