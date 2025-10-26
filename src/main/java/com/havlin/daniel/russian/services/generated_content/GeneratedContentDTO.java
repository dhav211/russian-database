package com.havlin.daniel.russian.services.generated_content;

import com.havlin.daniel.russian.entities.generated_content.ReadingLevel;

import java.util.HashMap;
import java.util.Map;

class GeneratedContentDTO {
    GeneratedContentDTO() {
        definitions = "";
        wordInformation = new GeneratedContentService.WordInformationDTO();
        sentences = new HashMap<>();
        hasError = false;
    }

    GeneratedContentDTO ERRORS() {
        GeneratedContentDTO erroredContent = new GeneratedContentDTO();

        erroredContent.definitions = null;
        erroredContent.wordInformation = null;
        erroredContent.sentences = null;
        erroredContent.hasError = true;

        return erroredContent;
    }

    String definitions;
    GeneratedContentService.WordInformationDTO wordInformation;
    Map<ReadingLevel, String> sentences;
    boolean hasError;
}
