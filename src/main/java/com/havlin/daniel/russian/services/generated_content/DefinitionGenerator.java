package com.havlin.daniel.russian.services.generated_content;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.generated_content.Definition;
import com.havlin.daniel.russian.entities.generated_content.GeneratedContentError;
import com.havlin.daniel.russian.entities.generated_content.GeneratedContentStatus;
import com.havlin.daniel.russian.entities.generated_content.WordInformation;
import com.havlin.daniel.russian.repositories.dictionary.WordFormRepository;
import com.havlin.daniel.russian.services.retrieval.WordRetrievalService;
import com.havlin.daniel.russian.utils.GeneratedContentChecker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class DefinitionGenerator {
    private final GeneratedContentCorrector generatedContentCorrector;

    public DefinitionGenerator(WordRetrievalService wordRetrievalService) {
        this.generatedContentCorrector = new GeneratedContentCorrector(wordRetrievalService);
    }

    /**
     * The LLM will send back a string with a short one-sentence definition on each line. Here we will try and correct
     * any of the errors it may have and reject it if it can't be fixed. A list of definition objects will be created, ready
     * to be saved to the repository.
     * @param definitionText A set of one-sentence definitions created by an LLM, will be converted to definition objects.
     * @param word The word that the definitions are trying to describe.
     * @return A list of definition objects that have been corrected.
     */
    Set<Definition> createShortDefinitions(String definitionText, Word word) {
        Set<Definition> approvedDefinitions = new HashSet<>();
        // Break down the definition text into individual lines,
        List<String> lines = definitionText.lines()
                // remove any lines that have any latin letters or empty lines
                .filter((line) -> !GeneratedContentChecker.isOverLatinLetterThreshold(line, 0.0) && !line.isEmpty())
                .toList();

        // Run the correctors to see if we can fix any obvious problems
        for (String line : lines) {
            CorrectedContent correctedContent = generatedContentCorrector.runCorrections(List.of(
                    generatedContentCorrector::correctBuiltinStressMarks,
                    generatedContentCorrector::correctLatinLettersUsedAsCyrillic,
                    generatedContentCorrector::correctMissingStressMark,
                    generatedContentCorrector::correctSingleVowelStresses,
                    generatedContentCorrector::correctStressInWrongPlace
            ), line);

            // We will only add the definition if we haven't found any mistakes or fixed the mistakes
            if (correctedContent.isCorrected()) {
                Definition definition = new Definition();
                definition.setText(correctedContent.correctedText());
                definition.setWord(word);
                word.getDefinitions().add(definition);
                approvedDefinitions.add(definition);
            }
        }
        return approvedDefinitions;
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
