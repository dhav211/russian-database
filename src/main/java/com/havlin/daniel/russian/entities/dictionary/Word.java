package com.havlin.daniel.russian.entities.dictionary;

import com.havlin.daniel.russian.entities.generated_content.Definition;
import com.havlin.daniel.russian.entities.generated_content.Sentence;
import com.havlin.daniel.russian.entities.generated_content.WordInformation;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Word {
    @Id
    private Long id;

    private String bare;

    private String accented;

    @Enumerated(EnumType.STRING)
    private WordType type;

    @Enumerated(EnumType.STRING)
    private WordLevel wordLevel;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "noun_id")
    private Noun noun;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "verb_id")
    private Verb verb;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "information_id")
    private WordInformation wordInformation;

    @OneToMany(mappedBy = "word", fetch = FetchType.LAZY)
    private final Set<Translation> translations = new HashSet<>();

    @OneToMany(mappedBy = "word", fetch = FetchType.LAZY)
    private final Set<WordForm> wordForms = new HashSet<>();

    @OneToMany(mappedBy = "word", fetch = FetchType.LAZY)
    private final Set<Definition> definitions = new HashSet<>();

    @OneToMany(mappedBy = "word", fetch = FetchType.LAZY)
    private final Set<Sentence> sentences = new HashSet<>();

    public static Word getError(String message) {
        Word error = new Word();
        error.setBare(message);
        error.setAccented(message);
        error.setWordLevel(WordLevel.ERROR);
        error.setType(WordType.ERROR);

        return error;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getBare() {
        return bare;
    }

    public void setBare(String bare) {
        this.bare = bare;
    }

    public String getAccented() {
        return accented;
    }

    public void setAccented(String accented) {
        this.accented = accented;
    }

    public WordType getType() {
        return type;
    }

    public void setType(WordType type) {
        this.type = type;
    }

    public WordLevel getWordLevel() {
        return wordLevel;
    }

    public void setWordLevel(WordLevel wordLevel) {
        this.wordLevel = wordLevel;
    }

    public Set<Translation> getTranslations() {
        return translations;
    }


    public Noun getNoun() {
        return noun;
    }

    public void setNoun(Noun noun) {
        this.noun = noun;
    }

    public Verb getVerb() {
        return verb;
    }

    public void setVerb(Verb verb) {
        this.verb = verb;
    }

    public Set<WordForm> getWordForms() {
        return wordForms;
    }


    public Set<Definition> getDefinitions() {
        return definitions;
    }

    public Set<Sentence> getSentences() {
        return sentences;
    }

    public WordInformation getWordInformation() {
        return wordInformation;
    }

    public void setWordInformation(WordInformation wordInformation) {
        this.wordInformation = wordInformation;
    }

    @Override
    public String toString() {
        return getBare();
    }
}
