package com.havlin.daniel.russian.entities.dictionary;

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

    @OneToMany(mappedBy = "word", fetch = FetchType.EAGER)
    private Set<Translation> translations;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "noun_id")
    private Noun noun;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "verb_id")
    private Verb verb;

    @OneToMany(mappedBy = "word", fetch = FetchType.EAGER)
    private Set<WordForm> wordForms = new HashSet<>();

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "definition_id")
    private Definition definition;

    @OneToMany(mappedBy = "word", fetch = FetchType.EAGER)
    private Set<Sentence> sentences = new HashSet<>();

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

    public Definition getDefinition() {
        return definition;
    }
    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    public Set<Sentence> getSentences() {
        return sentences;
    }
}
