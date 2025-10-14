package com.havlin.daniel.russian.services.generated_content;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.generated_content.Definition;
import com.havlin.daniel.russian.entities.generated_content.GeneratedContentStatus;
import com.havlin.daniel.russian.entities.generated_content.WordInformation;
import com.havlin.daniel.russian.repositories.dictionary.WordFormRepository;
import com.havlin.daniel.russian.repositories.dictionary.WordRepository;
import com.havlin.daniel.russian.repositories.generated_content.DefinitionRepository;
import com.havlin.daniel.russian.repositories.generated_content.WordInformationRepository;
import com.havlin.daniel.russian.utils.GeneratedContentChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class DefinitionGenerator {
    private final GeneratedContentCorrector generatedContentCorrector;

    public DefinitionGenerator(WordFormRepository wordFormRepository) {
        this.generatedContentCorrector = new GeneratedContentCorrector(wordFormRepository);
    }

    /**
     * The LLM will send back a string with a short one-sentence definition on each line. Here we will try and correct
     * any of the errors it may have and reject it if it can't be fixed. A list of definition objects will be created, ready
     * to be saved to the repository.
     * @param definitionText A set of one-sentence definitions created by an LLM, will be converted to definition objects.
     * @param word The word that the definitions are trying to describe.
     * @return A list of definition objects that have been corrected and ready to be saved to the database.
     */
    public List<Definition> createShortDefinitions(String definitionText, Word word) {
        return definitionText.lines()
                // remove any lines that have too many latin letters or empty lines
                .filter((line) -> !GeneratedContentChecker.isOverLatinLetterThreshold(line, 0.5) && !line.isEmpty())
                .map((line) -> {
            Definition definition = new Definition();
            List<GeneratedContentErrorMessage> errors = new ArrayList<>();
            GeneratedContentCorrector.CorrectedContent correctedContent = generatedContentCorrector
                    .correctTextIssuesAndLogErrors(errors, line);
            errors.addAll(correctedContent.errors());

            definition.setText(correctedContent.correctedText());
            definition.setWord(word);
            word.getDefinitions().add(definition);

            // If any errors are found we need to correct them by hand before the user can see them.
            if (!errors.isEmpty())
                definition.setStatus(GeneratedContentStatus.NEEDS_APPROVAL);
            else
                definition.setStatus(GeneratedContentStatus.APPROVED);

            return definition;
        }).toList();
    }


    public WordInformation createWordInformation(String definition,
                                                 String etymology,
                                                 String usageContext,
                                                 String formation,
                                                 Word word) {
        WordInformation wordInformation = new WordInformation();
        wordInformation.setWord(word);
        word.setWordInformation(wordInformation);

        List<GeneratedContentErrorMessage> errors = new ArrayList<>();

        String[] informationFields = { definition, etymology, usageContext, formation };
        for (int i = 0; i < informationFields.length; i ++) {
            GeneratedContentCorrector.CorrectedContent corrections = generatedContentCorrector
                    .correctTextIssuesAndLogErrors(errors, informationFields[i]);
            errors.addAll(corrections.errors());

            switch (i) {
                case 0 -> wordInformation.setDefinition(corrections.correctedText());
                case 1 -> wordInformation.setEtymology(corrections.correctedText());
                case 2 -> wordInformation.setUsageContext(corrections.correctedText());
                case 3 -> wordInformation.setFormation(corrections.correctedText());
            }
        }

        // Regardless of errors, we will save it to the database. However, if there are concerns we can check on them
        // and try and correct them by hand.
        if (errors.isEmpty()) {
            wordInformation.setStatus(GeneratedContentStatus.APPROVED);
        } else {
            wordInformation.setStatus(GeneratedContentStatus.CREATION_CONCERNS);
        }

        return wordInformation;
    }
}
