package com.havlin.daniel.russian.entities.generated_content;

import com.havlin.daniel.russian.entities.dictionary.Word;
import jakarta.persistence.*;

@Entity
public class WordInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String definition;

    @Lob
    private String etymology;

    @Lob
    private String usageContext;

    @Lob
    private String formation;

    @Enumerated(EnumType.STRING)
    private GeneratedContentStatus status;

    @OneToOne(mappedBy = "wordInformation")
    private Word word;

    public Long getId() {
        return id;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getEtymology() {
        return etymology;
    }

    public void setEtymology(String etymology) {
        this.etymology = etymology;
    }

    public String getUsageContext() {
        return usageContext;
    }

    public void setUsageContext(String usageContext) {
        this.usageContext = usageContext;
    }

    public String getFormation() {
        return formation;
    }

    public void setFormation(String formation) {
        this.formation = formation;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public GeneratedContentStatus getStatus() {
        return status;
    }

    public void setStatus(GeneratedContentStatus status) {
        this.status = status;
    }
}
