package com.havlin.daniel.russian.entities.retrieval;

import com.havlin.daniel.russian.entities.dictionary.*;
import com.havlin.daniel.russian.utils.StressedWordConverter;

import java.util.ArrayList;
import java.util.List;

public class VerbDTO  {
    private List<String> partners = new ArrayList<>();
    private VerbAspect aspect;
    private String singularImperative;
    private String pluralImperative;
    private String masculinePast;
    private String femininePast;
    private String neuterPast;
    private String pluralPast;
    private String presentFutureSingularFirst;
    private String presentFutureSingularSecond;
    private String presentFutureSingularThird;
    private String presentFuturePluralFirst;
    private String presentFuturePluralSecond;
    private String presentFuturePluralThird;
    private String gerundPresent;
    private String gerundPast;
    private String participleActivePresent;
    private String participleActivePast;
    private String participlePassivePresent;
    private String participlePassivePast;



    public VerbDTO(Word word) {
        this.aspect = word.getVerb().getAspect();

        // We need the word object from the verb partner, for easier access later
        for (VerbPartner partner : word.getVerb().getPartners()) {
            this.partners.add(partner.getText());
        }

        // Set all individual word forms for the verb
        for (WordForm wordForm : word.getWordForms()) {
            String stressAddedForm = StressedWordConverter.addStressMarks(wordForm.getAccented());
            switch (wordForm.getFormType()) {
                case "ru_verb_imperative_sg":
                    this.setSingularImperative(stressAddedForm);
                    break;
                case "ru_verb_imperative_pl":
                    this.setPluralImperative(stressAddedForm);
                    break;
                case "ru_verb_past_m":
                    this.setMasculinePast(stressAddedForm);
                    break;
                case "ru_verb_past_f":
                    this.setFemininePast(stressAddedForm);
                    break;
                case "ru_verb_past_n":
                    this.setNeuterPast(stressAddedForm);
                    break;
                case "ru_verb_past_pl":
                    this.setPluralPast(stressAddedForm);
                    break;
                case "ru_verb_presfut_sg1":
                    this.setPresentFutureSingularFirst(stressAddedForm);
                    break;
                case "ru_verb_presfut_sg2":
                    this.setPresentFutureSingularSecond(stressAddedForm);
                    break;
                case "ru_verb_presfut_sg3":
                    this.setPresentFutureSingularThird(stressAddedForm);
                    break;
                case "ru_verb_presfut_pl1":
                    this.setPresentFuturePluralFirst(stressAddedForm);
                    break;
                case "ru_verb_presfut_pl2":
                    this.setPresentFuturePluralSecond(stressAddedForm);
                    break;
                case "ru_verb_presfut_pl3":
                    this.setPresentFuturePluralThird(stressAddedForm);
                    break;
                case "ru_verb_gerund_present":
                    this.setGerundPresent(stressAddedForm);
                    break;
                case "ru_verb_gerund_past":
                    this.setGerundPast(stressAddedForm);
                    break;
                case "ru_verb_participle_active_present":
                    this.setParticipleActivePresent(stressAddedForm);
                    break;
                case "ru_verb_participle_active_past":
                    this.setParticipleActivePast(stressAddedForm);
                    break;
                case "ru_verb_participle_passive_present":
                    this.setParticiplePassivePresent(stressAddedForm);
                    break;
                case "ru_verb_participle_passive_past":
                    this.setParticiplePassivePast(stressAddedForm);
                    break;
            }
        }
    }

    public List<String> getPartners() {
        return partners;
    }

    public void setPartners(List<String> partners) {
        this.partners = partners;
    }

    public VerbAspect getAspect() {
        return aspect;
    }

    public void setAspect(VerbAspect aspect) {
        this.aspect = aspect;
    }

    public String getSingularImperative() {
        return singularImperative;
    }

    public void setSingularImperative(String singularImperative) {
        this.singularImperative = singularImperative;
    }

    public String getPluralImperative() {
        return pluralImperative;
    }

    public void setPluralImperative(String pluralImperative) {
        this.pluralImperative = pluralImperative;
    }

    public String getMasculinePast() {
        return masculinePast;
    }

    public void setMasculinePast(String masculinePast) {
        this.masculinePast = masculinePast;
    }

    public String getFemininePast() {
        return femininePast;
    }

    public void setFemininePast(String femininePast) {
        this.femininePast = femininePast;
    }

    public String getNeuterPast() {
        return neuterPast;
    }

    public void setNeuterPast(String neuterPast) {
        this.neuterPast = neuterPast;
    }

    public String getPluralPast() {
        return pluralPast;
    }

    public void setPluralPast(String pluralPast) {
        this.pluralPast = pluralPast;
    }

    public String getPresentFutureSingularFirst() {
        return presentFutureSingularFirst;
    }

    public void setPresentFutureSingularFirst(String presentFutureSingularFirst) {
        this.presentFutureSingularFirst = presentFutureSingularFirst;
    }

    public String getPresentFutureSingularSecond() {
        return presentFutureSingularSecond;
    }

    public void setPresentFutureSingularSecond(String presentFutureSingularSecond) {
        this.presentFutureSingularSecond = presentFutureSingularSecond;
    }

    public String getPresentFutureSingularThird() {
        return presentFutureSingularThird;
    }

    public void setPresentFutureSingularThird(String presentFutureSingularThird) {
        this.presentFutureSingularThird = presentFutureSingularThird;
    }

    public String getPresentFuturePluralFirst() {
        return presentFuturePluralFirst;
    }

    public void setPresentFuturePluralFirst(String presentFuturePluralFirst) {
        this.presentFuturePluralFirst = presentFuturePluralFirst;
    }

    public String getPresentFuturePluralSecond() {
        return presentFuturePluralSecond;
    }

    public void setPresentFuturePluralSecond(String presentFuturePluralSecond) {
        this.presentFuturePluralSecond = presentFuturePluralSecond;
    }

    public String getPresentFuturePluralThird() {
        return presentFuturePluralThird;
    }

    public void setPresentFuturePluralThird(String presentFuturePluralThird) {
        this.presentFuturePluralThird = presentFuturePluralThird;
    }

    public String getGerundPresent() {
        return gerundPresent;
    }

    public void setGerundPresent(String gerundPresent) {
        this.gerundPresent = gerundPresent;
    }

    public String getGerundPast() {
        return gerundPast;
    }

    public void setGerundPast(String gerundPast) {
        this.gerundPast = gerundPast;
    }

    public String getParticipleActivePresent() {
        return participleActivePresent;
    }

    public void setParticipleActivePresent(String participleActivePresent) {
        this.participleActivePresent = participleActivePresent;
    }

    public String getParticipleActivePast() {
        return participleActivePast;
    }

    public void setParticipleActivePast(String participleActivePast) {
        this.participleActivePast = participleActivePast;
    }

    public String getParticiplePassivePresent() {
        return participlePassivePresent;
    }

    public void setParticiplePassivePresent(String participlePassivePresent) {
        this.participlePassivePresent = participlePassivePresent;
    }

    public String getParticiplePassivePast() {
        return participlePassivePast;
    }

    public void setParticiplePassivePast(String participlePassivePast) {
        this.participlePassivePast = participlePassivePast;
    }
}
