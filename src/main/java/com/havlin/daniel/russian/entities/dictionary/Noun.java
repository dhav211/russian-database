package com.havlin.daniel.russian.entities.dictionary;

import jakarta.persistence.*;

@Entity
public class Noun {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "noun")
    private Word word;

    @Enumerated(EnumType.STRING)
    private WordGender gender;

    private String partner;

    private boolean animate;

    private boolean indeclinable;

    private boolean singularOnly;

    private boolean pluralOnly;

    public Long getId() {
        return id;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public WordGender getGender() {
        return gender;
    }

    public void setGender(WordGender gender) {
        this.gender = gender;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public boolean isAnimate() {
        return animate;
    }

    public void setAnimate(boolean animate) {
        this.animate = animate;
    }

    public boolean isIndeclinable() {
        return indeclinable;
    }

    public void setIndeclinable(boolean indeclinable) {
        this.indeclinable = indeclinable;
    }

    public boolean isSingularOnly() {
        return singularOnly;
    }

    public void setSingularOnly(boolean singularOnly) {
        this.singularOnly = singularOnly;
    }

    public boolean isPluralOnly() {
        return pluralOnly;
    }

    public void setPluralOnly(boolean pluralOnly) {
        this.pluralOnly = pluralOnly;
    }
}
