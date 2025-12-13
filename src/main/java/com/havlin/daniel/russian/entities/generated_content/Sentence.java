package com.havlin.daniel.russian.entities.generated_content;

import com.havlin.daniel.russian.entities.dictionary.Word;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Sentence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private String englishTranslation;

    @Enumerated(EnumType.STRING)
    private ReadingLevel readingLevel;

    @Enumerated(EnumType.STRING)
    private GeneratedContentStatus status;

    @ManyToMany(mappedBy = "containingSentences", fetch = FetchType.EAGER)
    private final Set<Word> containingWords = new HashSet<>();

    public Long getId() {
        return id;
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

    public ReadingLevel getReadingLevel() {
        return readingLevel;
    }

    public void setReadingLevel(ReadingLevel readingLevel) {
        this.readingLevel = readingLevel;
    }

    public Set<Word> getContainingWords() {
        return containingWords;
    }

    public void addWord(Word word) {
        this.containingWords.add(word);
        word.getSentences().add(this);
    }

    public void removeWord(Word word) {
        this.containingWords.remove(word);
        word.getSentences().remove(this);
    }

    @PreRemove
    public void removeWords() {
        for (Word word : containingWords) {
            word.getSentences().remove(this);
        }
    }

    public GeneratedContentStatus getStatus() {
        return status;
    }

    public void setStatus(GeneratedContentStatus status) {
        this.status = status;
    }
}
