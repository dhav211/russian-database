package com.havlin.daniel.russian.services.dictionary;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.dictionary.WordCase;
import com.havlin.daniel.russian.entities.dictionary.WordForm;
import com.havlin.daniel.russian.entities.dictionary.WordGender;
import com.havlin.daniel.russian.entities.generated_content.Definition;
import com.havlin.daniel.russian.entities.generated_content.Sentence;
import com.havlin.daniel.russian.entities.generated_content.WordInformation;
import com.havlin.daniel.russian.repositories.dictionary.WordFormRepository;
import com.havlin.daniel.russian.repositories.dictionary.WordRepository;
import com.havlin.daniel.russian.repositories.generated_content.DefinitionRepository;
import com.havlin.daniel.russian.repositories.generated_content.SentenceRepository;
import com.havlin.daniel.russian.repositories.generated_content.WordInformationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WordService {
    private final SentenceRepository sentenceRepository;
    private final DefinitionRepository definitionRepository;
    private final WordInformationRepository wordInformationRepository;
    private final WordRepository wordRepository;
    private final WordFormRepository wordFormRepository;


    public WordService(SentenceRepository sentenceRepository, DefinitionRepository definitionRepository,
                       WordInformationRepository wordInformationRepository, WordRepository wordRepository,
                       WordFormRepository wordFormRepository) {
        this.sentenceRepository = sentenceRepository;
        this.definitionRepository = definitionRepository;
        this.wordInformationRepository = wordInformationRepository;
        this.wordRepository = wordRepository;
        this.wordFormRepository = wordFormRepository;
    }

    @Transactional
    public void saveGeneratedContentToWord(Set<Sentence> sentences, Set<Definition> definitions,
                                           WordInformation wordInformation, Set<Word> words) {
        sentenceRepository.saveAll(sentences);
        definitionRepository.saveAll(definitions);
        wordInformationRepository.save(wordInformation);
        wordRepository.saveAllAndFlush(words); // We need to get the IDs generated for the sentences and definitions, so flush it
    }

    /**
     * Get the given's word's word form by the given form type.
     * @param word The word of the word form being searched
     * @param formType String value of the form type
     * @return The requested word form by form type
     */
    public Optional<WordForm> getWordsWordFormBasedOnWordFormType(Word word, String formType) {
        try {
            if (word.getWordForms() == null) { // The word you sent in didn't have the word forms initialized
                return Optional.empty();
            }

            // Search through each word form comparing the form type. Once the value has been matched we will return that word form
            for (WordForm form : word.getWordForms()) {
                String currentFormType = form.getFormType();
                if (convertFormTypeStringToWordCase(currentFormType) == convertFormTypeStringToWordCase(formType)) {
                    return Optional.of(form);
                }
            }

            // If we couldn't find anything we will just return an empty optional
            return Optional.empty();
        } catch (FormTypeDoesNotHaveACaseException | FormTypeDoesNotHaveAGenderException e) {
            return Optional.empty();
        }
    }

    public WordCase getWordFormsCase(WordForm wordForm) {
        return convertFormTypeStringToWordCase(wordForm.getFormType());
    }

    private String getAdjectiveWordFormGender(String formType) {
        String[] formTypeTestSplit = formType.split("_");
        String extractedGender = formTypeTestSplit[formTypeTestSplit.length - 2];

        switch (extractedGender) {
            case "m", "f", "n", "pl" -> {
                return extractedGender;
            }
            default -> throw new FormTypeDoesNotHaveAGenderException(formType);
        }
    }


    private WordCase convertFormTypeStringToWordCase(String formType) {
        String[] formTypeTestSplit = formType.split("_");
        String extractedType = formTypeTestSplit[formTypeTestSplit.length - 1];

        switch (extractedType) {
            case "prep" -> {
                return WordCase.PREPOSITIONAL;
            }
            case "inst" -> {
                return WordCase.INSTRUMENTAL;
            }
            case "acc" -> {
                return WordCase.ACCUSATIVE;
            }
            case "dat" -> {
                return WordCase.DATIVE;
            }
            case "gen" -> {
                return WordCase.GENITIVE;
            }
            case "nom" -> {
                return WordCase.NOMINATIVE;
            }
            default -> throw new FormTypeDoesNotHaveACaseException(formType);
        }
    }
}
