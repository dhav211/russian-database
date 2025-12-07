package com.havlin.daniel.russian;

import com.havlin.daniel.russian.entities.dictionary.*;
import com.havlin.daniel.russian.entities.retrieval.*;
import com.havlin.daniel.russian.repositories.dictionary.WordFormRepository;
import com.havlin.daniel.russian.repositories.dictionary.WordRepository;
import com.havlin.daniel.russian.services.dictionary.DefinitionService;
import com.havlin.daniel.russian.services.dictionary.FormTypeDoesNotHaveACaseException;
import com.havlin.daniel.russian.services.dictionary.WordService;
import com.havlin.daniel.russian.services.retrieval.WordRetrievalService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Arrays.stream;

@SpringBootTest
public class DictionaryTests {
    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private WordRetrievalService wordRetrievalService;

    @Autowired
    private DefinitionService definitionService;

    @Autowired
    private WordFormRepository wordFormRepository;

    @Autowired
    WordService wordService;

    private ClickedWordDTO getFirstFromSetOfWordType(Set<ClickedWordDTO> dtoSet, WordType wordType) {
        for (ClickedWordDTO wordDTO : dtoSet) {
            if (wordDTO.getWordType() == wordType)
                return wordDTO;
        }

        return  null;
    }

    @Test
    void getClickedWordDTO() {
        Set<ClickedWordDTO> thoughtSet = wordRetrievalService.getClickedWord("мысль");
        ClickedWordDTO thought = thoughtSet.stream().findFirst().get();
        ClickedWordDTO ruskyAdjective = getFirstFromSetOfWordType(wordRetrievalService.getClickedWord("русских"), WordType.ADJECTIVE);
        ClickedWordDTO throwVerb = getFirstFromSetOfWordType(wordRetrievalService.getClickedWord("бросишь"), WordType.VERB);
        ClickedWordDTO slishkom = getFirstFromSetOfWordType(wordRetrievalService.getClickedWord("слишком"), WordType.ADVERB);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, thoughtSet.size()),
                () -> Assertions.assertNotNull(thought.getNounDTO()),
                () -> Assertions.assertEquals("мы́сли", thought.getNounDTO().getAccusativePlural()),
                () -> Assertions.assertEquals("ру́сскому", ruskyAdjective.getAdjectiveDTO().getMasculineDative()),
                () -> Assertions.assertEquals("бро́сишь", throwVerb.getVerbDTO().getPresentFutureSingularSecond()),
                () -> Assertions.assertEquals("сли́шком", slishkom.getText())
        );
    }

    @Test
    void findAllAccentedByBareText() {
        List<String> accented = wordFormRepository.findAccentedByBare("достижения");
        List<String> jumbled = wordFormRepository.findAccentedByBare("фывапцук");

        Assertions.assertEquals(3, accented.size());
        Assertions.assertEquals(0, jumbled.size());
    }

    @Test
    void wordTypesFromId() {
        WordType verb = wordRetrievalService.getWordTypeFromWordId(53L);
        WordType noun = wordRetrievalService.getWordTypeFromWordId(12L);
        WordType adjective = wordRetrievalService.getWordTypeFromWordId(65L);
        WordType error = wordRetrievalService.getWordTypeFromWordId(93745L);

        Assertions.assertEquals(verb, WordType.VERB);
        Assertions.assertEquals(noun, WordType.NOUN);
        Assertions.assertEquals(adjective, WordType.ADJECTIVE);
        Assertions.assertEquals(error, WordType.ERROR);
    }

    @Test
    public void getWordsFromAccentedText() {
        Set<Word> words1 = wordRetrievalService.getWordsFromAccentedText("водяны'х");
        Set<Word> words2 = wordRetrievalService.getWordsFromAccentedText("просро'ченных");
        Set<Word> words3 = wordRetrievalService.getWordsFromAccentedText("чёртовые");
        Set<Word> word4 = wordRetrievalService.getWordsFromAccentedText("сосе'д");
        Set<Word> word5 = wordRetrievalService.findAllWordsByAccentedForSentenceCreation("всегда'");

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, words1.size()),
                () -> Assertions.assertEquals(1, words2.size()),
                () -> Assertions.assertEquals(1, words3.size()),
                () -> Assertions.assertEquals(1, word4.size()),
                () -> Assertions.assertEquals(1, word5.size())
        );
    }

    @Test
    public void getRandomNoun() {
        Optional<Word> random1 = wordRetrievalService.getARandomNoun();

        Assertions.assertTrue(random1.isPresent());
    }

    @Test
    public void findWordFormsCase() {
        WordForm wordForm1 = wordFormRepository.findById(1489049L).get();
        WordForm wordForm2 = wordFormRepository.findById(1175512L).get();

        Assertions.assertAll(
                () -> Assertions.assertEquals(WordCase.DATIVE, wordService.getWordFormsCase(wordForm1)),
                () -> Assertions.assertThrows(FormTypeDoesNotHaveACaseException.class,
                        () -> wordService.getWordFormsCase(wordForm2))
        );
    }
}
