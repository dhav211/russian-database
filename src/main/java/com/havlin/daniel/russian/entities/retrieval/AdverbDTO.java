package com.havlin.daniel.russian.entities.retrieval;

import com.havlin.daniel.russian.entities.dictionary.WordLevel;
import com.havlin.daniel.russian.entities.dictionary.WordType;

public class AdverbDTO extends WordRetrievalDTO {
    public static AdverbDTO getError() {
        AdverbDTO error = new AdverbDTO();

        error.setBareText("ERROR");
        error.setAccentedText("ERROR");
        error.setWordType(WordType.ERROR);
        error.setWordLevel(WordLevel.ERROR);

        return error;
    }
}
