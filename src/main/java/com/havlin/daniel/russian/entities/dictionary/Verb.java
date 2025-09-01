package com.havlin.daniel.russian.entities.dictionary;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Verb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "verb")
    private Word word;

    @Enumerated(EnumType.STRING)
    private VerbAspect aspect;

    @OneToMany(mappedBy = "verb", fetch = FetchType.EAGER)
    private Set<VerbPartner> partners = new HashSet<>();

    public Long getId() {
        return id;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public VerbAspect getAspect() {
        return aspect;
    }

    public void setAspect(VerbAspect aspect) {
        this.aspect = aspect;
    }

    public Set<VerbPartner> getPartners() {
        return partners;
    }
}
