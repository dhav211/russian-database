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
}
