package com.havlin.daniel.russian.entities.retrieval;

import com.havlin.daniel.russian.entities.dictionary.WordLevel;
import com.havlin.daniel.russian.entities.dictionary.WordType;

import java.util.ArrayList;
import java.util.HashSet;

public class WordRetrievalError extends WordRetrievalDTO {
    private String errorMessage;

    public WordRetrievalError() {
        accentedText = "ERROR";
        bareText = "ERROR";
        wordLevel = WordLevel.ERROR;
        wordType = WordType.ERROR;
        translations = new HashSet<>();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
