package com.havlin.daniel.russian.entities.retrieval;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.dictionary.WordForm;
import com.havlin.daniel.russian.entities.dictionary.WordLevel;
import com.havlin.daniel.russian.entities.dictionary.WordType;
import com.havlin.daniel.russian.utils.StressedWordConverter;

public class NounDTO {
    private String nominativeSingular;
    private String genitiveSingular;
    private String dativeSingular;
    private String instrumentalSingular;
    private String accusativeSingular;
    private String prepositionalSingular;
    private String nominativePlural;
    private String genitivePlural;
    private String dativePlural;
    private String instrumentalPlural;
    private String accusativePlural;
    private String prepositionalPlural;

    public NounDTO(Word word) {
        for (WordForm wordForm : word.getWordForms()) {
            String stressAddedForm = StressedWordConverter.addStressMarks(wordForm.getAccented());
            
            switch (wordForm.getFormType()) {
                case "ru_noun_sg_nom":
                    this.setNominativeSingular(stressAddedForm);
                    break;
                case "ru_noun_sg_gen":
                    this.setGenitiveSingular(stressAddedForm);
                    break;
                case "ru_noun_sg_dat":
                    this.setDativeSingular(stressAddedForm);
                    break;
                case "ru_noun_sg_acc":
                    this.setAccusativeSingular(stressAddedForm);
                    break;
                case "ru_noun_sg_inst":
                    this.setInstrumentalSingular(stressAddedForm);
                    break;
                case "ru_noun_sg_prep":
                    this.setPrepositionalSingular(stressAddedForm);
                    break;
                case "ru_noun_pl_nom":
                    this.setNominativePlural(stressAddedForm);
                    break;
                case "ru_noun_pl_gen":
                    this.setGenitivePlural(stressAddedForm);
                    break;
                case "ru_noun_pl_dat":
                    this.setDativePlural(stressAddedForm);
                    break;
                case "ru_noun_pl_acc":
                    this.setAccusativePlural(stressAddedForm);
                    break;
                case "ru_noun_pl_inst":
                    this.setInstrumentalPlural(stressAddedForm);
                    break;
                case "ru_noun_pl_prep":
                    this.setPrepositionalPlural(stressAddedForm);
                    break;
            }
        }
    }

    public String getNominativeSingular() {
        return nominativeSingular;
    }

    public void setNominativeSingular(String nominativeSingular) {
        this.nominativeSingular = nominativeSingular;
    }

    public String getGenitiveSingular() {
        return genitiveSingular;
    }

    public void setGenitiveSingular(String genitiveSingular) {
        this.genitiveSingular = genitiveSingular;
    }

    public String getDativeSingular() {
        return dativeSingular;
    }

    public void setDativeSingular(String dativeSingular) {
        this.dativeSingular = dativeSingular;
    }

    public String getInstrumentalSingular() {
        return instrumentalSingular;
    }

    public void setInstrumentalSingular(String instrumentalSingular) {
        this.instrumentalSingular = instrumentalSingular;
    }

    public String getAccusativeSingular() {
        return accusativeSingular;
    }

    public void setAccusativeSingular(String accusativeSingular) {
        this.accusativeSingular = accusativeSingular;
    }

    public String getPrepositionalSingular() {
        return prepositionalSingular;
    }

    public void setPrepositionalSingular(String prepositionalSingular) {
        this.prepositionalSingular = prepositionalSingular;
    }

    public String getNominativePlural() {
        return nominativePlural;
    }

    public void setNominativePlural(String nominativePlural) {
        this.nominativePlural = nominativePlural;
    }

    public String getGenitivePlural() {
        return genitivePlural;
    }

    public void setGenitivePlural(String genitivePlural) {
        this.genitivePlural = genitivePlural;
    }

    public String getDativePlural() {
        return dativePlural;
    }

    public void setDativePlural(String dativePlural) {
        this.dativePlural = dativePlural;
    }

    public String getInstrumentalPlural() {
        return instrumentalPlural;
    }

    public void setInstrumentalPlural(String instrumentalPlural) {
        this.instrumentalPlural = instrumentalPlural;
    }

    public String getAccusativePlural() {
        return accusativePlural;
    }

    public void setAccusativePlural(String accusativePlural) {
        this.accusativePlural = accusativePlural;
    }

    public String getPrepositionalPlural() {
        return prepositionalPlural;
    }

    public void setPrepositionalPlural(String prepositionalPlural) {
        this.prepositionalPlural = prepositionalPlural;
    }
}
