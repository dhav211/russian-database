package com.havlin.daniel.russian.services.generated_content;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.generated_content.GeneratedContentError;
import com.havlin.daniel.russian.repositories.generated_content.GeneratedContentErrorRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public final class GeneratedContentErrorService {
    GeneratedContentErrorRepository generatedContentErrorRepository;

    public GeneratedContentErrorService(GeneratedContentErrorRepository generatedContentErrorRepository) {
        this.generatedContentErrorRepository = generatedContentErrorRepository;
    }

    public GeneratedContentError createError(GeneratedContentErrorOrigin origin, GeneratedContentErrorType errorType, String message, String fullText) {
        GeneratedContentError error = new GeneratedContentError();

        error.setErrorOrigin(origin);
        error.setErrorType(errorType);
        error.setMessage(message);
        error.setFullText(fullText);

        return error;
    }

    void addErrors(List<GeneratedContentService.SentenceWithErrors> sentencesWithErrors,
                   List<GeneratedContentService.DefinitionWithErrors> definitionsWithErrors) {
        List<GeneratedContentError> allErrors = new ArrayList<>();
        for (GeneratedContentService.SentenceWithErrors sentenceWithErrors : sentencesWithErrors) {
            sentenceWithErrors.errors.forEach((e) -> {
                e.setOriginatingEntityId(sentenceWithErrors.sentence.getId());
                allErrors.add(e);
            });
        }

        for (GeneratedContentService.DefinitionWithErrors definitionWithErrors : definitionsWithErrors) {
            definitionWithErrors.errors.forEach((e) -> {
                e.setOriginatingEntityId(definitionWithErrors.definition.getId());
                allErrors.add(e);
            });
        }

        generatedContentErrorRepository.saveAll(allErrors);
    }
}
