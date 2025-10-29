package com.havlin.daniel.russian.services.generated_content;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.generated_content.Definition;
import com.havlin.daniel.russian.entities.generated_content.GeneratedContentError;
import com.havlin.daniel.russian.entities.generated_content.GeneratedContentStatus;
import com.havlin.daniel.russian.entities.generated_content.WordInformation;
import com.havlin.daniel.russian.repositories.dictionary.WordFormRepository;
import com.havlin.daniel.russian.utils.GeneratedContentChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DefinitionGenerator {
    private final GeneratedContentCorrector generatedContentCorrector;
    private final GeneratedContentErrorService generatedContentErrorService;

    public DefinitionGenerator(WordFormRepository wordFormRepository, GeneratedContentErrorService generatedContentErrorService) {
        this.generatedContentCorrector = new GeneratedContentCorrector(wordFormRepository);
        this.generatedContentErrorService = generatedContentErrorService;
    }

    /**
     * The LLM will send back a string with a short one-sentence definition on each line. Here we will try and correct
     * any of the errors it may have and reject it if it can't be fixed. A list of definition objects will be created, ready
     * to be saved to the repository.
     * @param definitionText A set of one-sentence definitions created by an LLM, will be converted to definition objects.
     * @param word The word that the definitions are trying to describe.
     * @return A list of definition objects that have been corrected but still may contain errors, the list of errors are
     * included with the returning object
     */
    List<GeneratedContentService.DefinitionWithErrors> createShortDefinitions(String definitionText, Word word) {
        return definitionText.lines()
                // remove any lines that have too many latin letters or empty lines
                .filter((line) -> !GeneratedContentChecker.isOverLatinLetterThreshold(line, 0.5) && !line.isEmpty())
                .map((line) -> {
            Definition definition = new Definition();
            List<GeneratedContentErrorMessage> errorMessages = new ArrayList<>();

            List<Function<String, CorrectedContent>> correctors = List.of(
                    generatedContentCorrector::correctBuiltinStressMarks,
                    generatedContentCorrector::correctLatinLettersUsedAsCyrillic,
                    generatedContentCorrector::correctMissingStressMark,
                    generatedContentCorrector::correctSingleVowelStresses
            );

            for (Function<String, CorrectedContent> correction : correctors) {
                CorrectedContent correctedContent = correction.apply(line);
                errorMessages.addAll(correctedContent.errors());
                line = correctedContent.correctedText();
            }

            definition.setText(line);
            definition.setWord(word);
            word.getDefinitions().add(definition);

            // Convert the error messages to Error entities which will be later saved to the database
            // We cannot save them now, as we need the sentence ID which won't exist until the db is flushed
            List<GeneratedContentError> errors = errorMessages.stream()
                    .map((message) -> generatedContentErrorService.createError(
                            GeneratedContentErrorOrigin.DEFINITION,
                            message.errorType(),
                            message.message(),
                            definition.getText()
                    )).toList();

            // If any errorMessages are found we need to correct them by hand before the user can see them.
            if (!errorMessages.isEmpty())
                definition.setStatus(GeneratedContentStatus.NEEDS_APPROVAL);
            else
                definition.setStatus(GeneratedContentStatus.APPROVED);

            return new GeneratedContentService.DefinitionWithErrors(definition, errors);
        }).toList();
    }


    public WordInformation createWordInformation(String definition,
                                                 String etymology,
                                                 String usageContext,
                                                 String formation,
                                                 Word word) {
        WordInformation wordInformation = new WordInformation();
        wordInformation.setWord(word);
        wordInformation.setFormation(formation);
        wordInformation.setDefinition(definition);
        wordInformation.setUsageContext(usageContext);
        wordInformation.setEtymology(etymology);
        word.setWordInformation(wordInformation);

        return wordInformation;
    }
}
