package com.havlin.daniel.russian.entities.dictionary;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Sentence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "sentences")
    private Set<Word> words;

    private String text;

    private String englishTranslation;

    private WordLevel wordLevel;

    public Long getId() {
        return id;
    }

    public Set<Word> getWords() {
        return words;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getEnglishTranslation() {
        return englishTranslation;
    }

    public void setEnglishTranslation(String englishTranslation) {
        this.englishTranslation = englishTranslation;
    }

    public WordLevel getWordLevel() {
        return wordLevel;
    }

    public void setWordLevel(WordLevel wordLevel) {
        this.wordLevel = wordLevel;
    }
}
