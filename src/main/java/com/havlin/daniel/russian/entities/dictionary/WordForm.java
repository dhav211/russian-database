package com.havlin.daniel.russian.entities.dictionary;

import jakarta.persistence.*;

@Entity
@Table(indexes = {
        @Index(name = "idx_accented", columnList = "accented")
})
public class WordForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "word_id", nullable = false)
    private Word word;

    private String formType;

    private String bare;

    private String accented;

    public Long getId() {
        return id;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
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
}
