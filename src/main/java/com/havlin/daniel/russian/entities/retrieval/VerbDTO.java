package com.havlin.daniel.russian.entities.retrieval;

import com.havlin.daniel.russian.entities.dictionary.VerbAspect;
import com.havlin.daniel.russian.entities.dictionary.WordLevel;
import com.havlin.daniel.russian.entities.dictionary.WordType;

import java.util.ArrayList;
import java.util.List;

public class VerbDTO extends WordRetrievalDTO {
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

    public static VerbDTO getError() {
        VerbDTO error = new VerbDTO();

        error.setBareText("ERROR");
        error.setAccentedText("ERROR");
        error.setWordType(WordType.ERROR);
        error.setWordLevel(WordLevel.ERROR);

        return error;
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
