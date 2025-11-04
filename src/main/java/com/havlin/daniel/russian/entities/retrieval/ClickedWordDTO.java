package com.havlin.daniel.russian.entities.retrieval;

import com.havlin.daniel.russian.entities.dictionary.Translation;
import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.dictionary.WordLevel;
import com.havlin.daniel.russian.entities.dictionary.WordType;
import com.havlin.daniel.russian.utils.StressedWordConverter;

import java.util.List;

/**
 *
 */

public class ClickedWordDTO {
    String text;
    WordLevel wordLevel;
    WordType wordType;
    VerbDTO verbDTO;
    NounDTO nounDTO;
    AdjectiveDTO adjectiveDTO;
    List<String> translations;

    public static ClickedWordDTO error(String errorMessage) {
        ClickedWordDTO error = new ClickedWordDTO();
        error.text = errorMessage;

        return error;
    }

    public static ClickedWordDTO translation(String translation) {
        ClickedWordDTO translationOnly = new ClickedWordDTO();
        translationOnly.text = translation;

        return translationOnly;
    }

    public ClickedWordDTO() {}

    public ClickedWordDTO(Word word) {
        text = StressedWordConverter.addStressMarks(word.getAccented());
        wordLevel = word.getWordLevel();
        wordType = word.getType();
        translations = word.getTranslations().stream().map((Translation::getTranslation)).toList();

        switch (wordType) {
            case VERB -> verbDTO = new VerbDTO(word);
            case NOUN -> nounDTO = new NounDTO(word);
            case ADJECTIVE -> adjectiveDTO = new AdjectiveDTO(word);
        }
    }

    public String getText() {
        return text;
    }

    public WordLevel getWordLevel() {
        return wordLevel;
    }

    public WordType getWordType() {
        return wordType;
    }

    public VerbDTO getVerbDTO() {
        return verbDTO;
    }

    public NounDTO getNounDTO() {
        return nounDTO;
    }

    public AdjectiveDTO getAdjectiveDTO() {
        return adjectiveDTO;
    }

    public List<String> getTranslations() {
        return translations;
    }
}
