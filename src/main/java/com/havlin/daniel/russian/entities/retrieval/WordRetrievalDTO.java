package com.havlin.daniel.russian.entities.retrieval;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.dictionary.WordLevel;
import com.havlin.daniel.russian.entities.dictionary.WordType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class WordRetrievalDTO {
    protected String accentedText;
    protected String bareText;
    protected Set<String> translations = new HashSet<>();
    protected WordType wordType;
    protected WordLevel wordLevel;

    public String getAccentedText() {
        return accentedText;
    }

    public void setAccentedText(String accentedText) {
        this.accentedText = accentedText;
    }

    public String getBareText() {
        return bareText;
    }

    public void setBareText(String bareText) {
        this.bareText = bareText;
    }

    public Set<String> getTranslations() {
        return translations;
    }

    public void setTranslations(Set<String> translations) {
        this.translations = translations;
    }

    public WordType getWordType() {
        return wordType;
    }

    public void setWordType(WordType wordType) {
        this.wordType = wordType;
    }

    public WordLevel getWordLevel() {
        return wordLevel;
    }

    public void setWordLevel(WordLevel wordLevel) {
        this.wordLevel = wordLevel;
    }
}
