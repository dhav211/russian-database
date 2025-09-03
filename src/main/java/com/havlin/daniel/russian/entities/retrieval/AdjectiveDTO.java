package com.havlin.daniel.russian.entities.retrieval;

import com.havlin.daniel.russian.entities.dictionary.WordLevel;
import com.havlin.daniel.russian.entities.dictionary.WordType;

public class AdjectiveDTO extends WordRetrievalDTO {
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

    public static AdjectiveDTO getError() {
        AdjectiveDTO error = new AdjectiveDTO();

        error.setBareText("ERROR");
        error.setAccentedText("ERROR");
        error.setWordType(WordType.ERROR);
        error.setWordLevel(WordLevel.ERROR);

        return error;
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
