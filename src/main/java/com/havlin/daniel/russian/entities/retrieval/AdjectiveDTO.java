package com.havlin.daniel.russian.entities.retrieval;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.dictionary.WordForm;
import com.havlin.daniel.russian.entities.dictionary.WordLevel;
import com.havlin.daniel.russian.entities.dictionary.WordType;
import com.havlin.daniel.russian.utils.StressedWordConverter;

public class AdjectiveDTO {
    private String comparative;
    private String superlative;
    private String masculineShort;
    private String feminineShort;
    private String neuterShort;
    private String pluralShort;
    private String masculineNominative;
    private String masculineGenitive;
    private String masculineDative;
    private String masculineInstrumental;
    private String masculineAccusative;
    private String masculinePrepositional;
    private String feminineNominative;
    private String feminineGenitive;
    private String feminineDative;
    private String feminineInstrumental;
    private String feminineAccusative;
    private String femininePrepositional;
    private String neuterNominative;
    private String neuterGenitive;
    private String neuterDative;
    private String neuterInstrumental;
    private String neuterAccusative;
    private String neuterPrepositional;
    private String pluralNominative;
    private String pluralGenitive;
    private String pluralDative;
    private String pluralInstrumental;
    private String pluralAccusative;
    private String pluralPrepositional;

    public AdjectiveDTO(Word word) {
        for (WordForm wordForm : word.getWordForms()) {
            if (wordForm.getAccented() != null) {
                String stressAddedForm = StressedWordConverter.addStressMarks(wordForm.getAccented());

                switch (wordForm.getFormType()) {
                    case "ru_adj_comparative":
                        this.setComparative(stressAddedForm);
                        break;
                    case "ru_adj_superlative":
                        this.setSuperlative(stressAddedForm);
                        break;
                    case "ru_adj_short_m":
                        this.setMasculineShort(stressAddedForm);
                        break;
                    case "ru_adj_short_f":
                        this.setFeminineShort(stressAddedForm);
                        break;
                    case "ru_adj_short_n":
                        this.setNeuterShort(stressAddedForm);
                        break;
                    case "ru_adj_short_pl":
                        this.setPluralShort(stressAddedForm);
                        break;
                    case "ru_adj_m_nom":
                        this.setMasculineNominative(stressAddedForm);
                        break;
                    case "ru_adj_m_gen":
                        this.setMasculineGenitive(stressAddedForm);
                        break;
                    case "ru_adj_m_dat":
                        this.setMasculineDative(stressAddedForm);
                        break;
                    case "ru_adj_m_acc":
                        // animate male nouns will change case differently than inanimate, this will affect adjectives
                        if (this.getMasculineAccusative() == null) {
                            this.setMasculineAccusative(stressAddedForm);
                        } else {

                            this.setMasculineAccusative(this.getMasculineAccusative() + ", " + stressAddedForm);
                        }
                        break;
                    case "ru_adj_m_inst":
                        this.setMasculineInstrumental(stressAddedForm);
                        break;
                    case "ru_adj_m_prep":
                        this.setMasculinePrepositional(stressAddedForm);
                        break;
                    case "ru_adj_f_nom":
                        this.setFeminineNominative(stressAddedForm);
                        break;
                    case "ru_adj_f_gen":
                        this.setFeminineGenitive(stressAddedForm);
                        break;
                    case "ru_adj_f_dat":
                        this.setFeminineDative(stressAddedForm);
                        break;
                    case "ru_adj_f_acc":
                        this.setFeminineAccusative(stressAddedForm);
                        break;
                    case "ru_adj_f_inst":
                        // There can be multiple entries in this case, here we will display them with a comma in between
                        if (this.getFeminineInstrumental() == null) {
                            this.setFeminineInstrumental(stressAddedForm);
                        } else {
                            this.setFeminineInstrumental(this.getFeminineInstrumental() + ", " + stressAddedForm);
                        }
                        break;
                    case "ru_adj_f_prep":
                        this.setFemininePrepositional(stressAddedForm);
                        break;
                    case "ru_adj_n_nom":
                        this.setNeuterNominative(stressAddedForm);
                        break;
                    case "ru_adj_n_gen":
                        this.setNeuterGenitive(stressAddedForm);
                        break;
                    case "ru_adj_n_dat":
                        this.setNeuterDative(stressAddedForm);
                        break;
                    case "ru_adj_n_acc":
                        this.setNeuterAccusative(stressAddedForm);
                        break;
                    case "ru_adj_n_inst":
                        this.setNeuterInstrumental(stressAddedForm);
                        break;
                    case "ru_adj_n_prep":
                        this.setNeuterPrepositional(stressAddedForm);
                        break;
                    case "ru_adj_pl_nom":
                        this.setPluralNominative(stressAddedForm);
                        break;
                    case "ru_adj_pl_gen":
                        this.setPluralGenitive(stressAddedForm);
                        break;
                    case "ru_adj_pl_dat":
                        this.setPluralDative(stressAddedForm);
                        break;
                    case "ru_adj_pl_acc":
                        // animate male nouns will change case differently than inanimate, this will affect adjectives
                        if (this.getPluralAccusative() == null) {
                            this.setPluralAccusative(stressAddedForm);
                        } else {
                            this.setPluralAccusative(this.getPluralAccusative() + ", " + stressAddedForm);
                        }
                        break;
                    case "ru_adj_pl_inst":
                        this.setPluralInstrumental(stressAddedForm);
                        break;
                    case "ru_adj_pl_prep":
                        this.setPluralPrepositional(stressAddedForm);
                        break;
                }
            }
        }
    }

    public String getComparative() {
        return comparative;
    }

    public void setComparative(String comparative) {
        this.comparative = comparative;
    }

    public String getSuperlative() {
        return superlative;
    }

    public void setSuperlative(String superlative) {
        this.superlative = superlative;
    }

    public String getMasculineShort() {
        return masculineShort;
    }

    public void setMasculineShort(String masculineShort) {
        this.masculineShort = masculineShort;
    }

    public String getFeminineShort() {
        return feminineShort;
    }

    public void setFeminineShort(String feminineShort) {
        this.feminineShort = feminineShort;
    }

    public String getNeuterShort() {
        return neuterShort;
    }

    public void setNeuterShort(String neuterShort) {
        this.neuterShort = neuterShort;
    }

    public String getPluralShort() {
        return pluralShort;
    }

    public void setPluralShort(String pluralShort) {
        this.pluralShort = pluralShort;
    }

    public String getMasculineNominative() {
        return masculineNominative;
    }

    public void setMasculineNominative(String masculineNominative) {
        this.masculineNominative = masculineNominative;
    }

    public String getMasculineGenitive() {
        return masculineGenitive;
    }

    public void setMasculineGenitive(String masculineGenitive) {
        this.masculineGenitive = masculineGenitive;
    }

    public String getMasculineDative() {
        return masculineDative;
    }

    public void setMasculineDative(String masculineDative) {
        this.masculineDative = masculineDative;
    }

    public String getMasculineInstrumental() {
        return masculineInstrumental;
    }

    public void setMasculineInstrumental(String masculineInstrumental) {
        this.masculineInstrumental = masculineInstrumental;
    }

    public String getMasculineAccusative() {
        return masculineAccusative;
    }

    public void setMasculineAccusative(String masculineAccusative) {
        this.masculineAccusative = masculineAccusative;
    }

    public String getMasculinePrepositional() {
        return masculinePrepositional;
    }

    public void setMasculinePrepositional(String masculinePrepositional) {
        this.masculinePrepositional = masculinePrepositional;
    }

    public String getFeminineNominative() {
        return feminineNominative;
    }

    public void setFeminineNominative(String feminineNominative) {
        this.feminineNominative = feminineNominative;
    }

    public String getFeminineGenitive() {
        return feminineGenitive;
    }

    public void setFeminineGenitive(String feminineGenitive) {
        this.feminineGenitive = feminineGenitive;
    }

    public String getFeminineDative() {
        return feminineDative;
    }

    public void setFeminineDative(String feminineDative) {
        this.feminineDative = feminineDative;
    }

    public String getFeminineInstrumental() {
        return feminineInstrumental;
    }

    public void setFeminineInstrumental(String feminineInstrumental) {
        this.feminineInstrumental = feminineInstrumental;
    }

    public String getFeminineAccusative() {
        return feminineAccusative;
    }

    public void setFeminineAccusative(String feminineAccusative) {
        this.feminineAccusative = feminineAccusative;
    }

    public String getFemininePrepositional() {
        return femininePrepositional;
    }

    public void setFemininePrepositional(String femininePrepositional) {
        this.femininePrepositional = femininePrepositional;
    }

    public String getNeuterNominative() {
        return neuterNominative;
    }

    public void setNeuterNominative(String neuterNominative) {
        this.neuterNominative = neuterNominative;
    }

    public String getNeuterGenitive() {
        return neuterGenitive;
    }

    public void setNeuterGenitive(String neuterGenitive) {
        this.neuterGenitive = neuterGenitive;
    }

    public String getNeuterDative() {
        return neuterDative;
    }

    public void setNeuterDative(String neuterDative) {
        this.neuterDative = neuterDative;
    }

    public String getNeuterInstrumental() {
        return neuterInstrumental;
    }

    public void setNeuterInstrumental(String neuterInstrumental) {
        this.neuterInstrumental = neuterInstrumental;
    }

    public String getNeuterAccusative() {
        return neuterAccusative;
    }

    public void setNeuterAccusative(String neuterAccusative) {
        this.neuterAccusative = neuterAccusative;
    }

    public String getNeuterPrepositional() {
        return neuterPrepositional;
    }

    public void setNeuterPrepositional(String neuterPrepositional) {
        this.neuterPrepositional = neuterPrepositional;
    }

    public String getPluralNominative() {
        return pluralNominative;
    }

    public void setPluralNominative(String pluralNominative) {
        this.pluralNominative = pluralNominative;
    }

    public String getPluralGenitive() {
        return pluralGenitive;
    }

    public void setPluralGenitive(String pluralGenitive) {
        this.pluralGenitive = pluralGenitive;
    }

    public String getPluralDative() {
        return pluralDative;
    }

    public void setPluralDative(String pluralDative) {
        this.pluralDative = pluralDative;
    }

    public String getPluralInstrumental() {
        return pluralInstrumental;
    }

    public void setPluralInstrumental(String pluralInstrumental) {
        this.pluralInstrumental = pluralInstrumental;
    }

    public String getPluralAccusative() {
        return pluralAccusative;
    }

    public void setPluralAccusative(String pluralAccusative) {
        this.pluralAccusative = pluralAccusative;
    }

    public String getPluralPrepositional() {
        return pluralPrepositional;
    }

    public void setPluralPrepositional(String pluralPrepositional) {
        this.pluralPrepositional = pluralPrepositional;
    }
}
