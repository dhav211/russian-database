package com.havlin.daniel.russian.entities.retrieval;

import com.havlin.daniel.russian.entities.dictionary.WordLevel;
import com.havlin.daniel.russian.entities.dictionary.WordType;

public class OtherDTO extends WordRetrievalDTO {
    public static OtherDTO getError() {
        OtherDTO error = new OtherDTO();

        error.setBareText("ERROR");
        error.setAccentedText("ERROR");
        error.setWordType(WordType.ERROR);
        error.setWordLevel(WordLevel.ERROR);

        return error;
    }
}
