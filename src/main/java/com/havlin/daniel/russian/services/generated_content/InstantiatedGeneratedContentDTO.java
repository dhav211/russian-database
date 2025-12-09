package com.havlin.daniel.russian.services.generated_content;

import com.havlin.daniel.russian.entities.generated_content.ReadingLevel;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the generated content from the LLM that is still in string values and has not been corrected. We will turn
 * these into proper entities that can be saved into the database.
 */
class InstantiatedGeneratedContentDTO {
    InstantiatedGeneratedContentDTO() {
        definitions = "";
        sentences = new HashMap<>();
        hasError = false;
    }

    InstantiatedGeneratedContentDTO ERRORS() {
        InstantiatedGeneratedContentDTO erroredContent = new InstantiatedGeneratedContentDTO();

        erroredContent.definitions = null;
        erroredContent.sentences = null;
        erroredContent.hasError = true;

        return erroredContent;
    }

    String definitions;
    Map<ReadingLevel, String> sentences;
    boolean hasError;
}
