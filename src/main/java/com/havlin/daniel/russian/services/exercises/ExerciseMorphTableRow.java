package com.havlin.daniel.russian.services.exercises;

import java.util.List;

class ExerciseMorphTableRow {
    private String wordFormType;
    private List<String> wordForms;
    private boolean isAlreadyFilled;

    ExerciseMorphTableRow(String wordFormType, List<String> wordForms, boolean isAlreadyFilled) {
        this.wordFormType = wordFormType;
        this.wordForms = wordForms;
        this.isAlreadyFilled = isAlreadyFilled;
    }

    public String getWordFormType() {
        return wordFormType;
    }

    public List<String> getWordForms() {
        return wordForms;
    }

    public boolean isAlreadyFilled() {
        return isAlreadyFilled;
    }

    public void setAlreadyFilled(boolean alreadyFilled) {
        isAlreadyFilled = alreadyFilled;
    }
}
