package com.havlin.daniel.russian.entities.dictionary;

import jakarta.persistence.*;

@Entity
public class VerbPartner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "verb_id", nullable = false)
    private Verb verb;

    private String text;

    public Long getId() {
        return id;
    }

    public Verb getVerb() {
        return verb;
    }

    public void setVerb(Verb verb) {
        this.verb = verb;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
