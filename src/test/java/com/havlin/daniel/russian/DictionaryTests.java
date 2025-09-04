package com.havlin.daniel.russian;

import com.havlin.daniel.russian.entities.dictionary.VerbAspect;
import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.dictionary.WordLevel;
import com.havlin.daniel.russian.entities.dictionary.WordType;
import com.havlin.daniel.russian.entities.retrieval.*;
import com.havlin.daniel.russian.repositories.dictionary.WordRepository;
import com.havlin.daniel.russian.services.retrieval.WordRetrievalService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest
public class DictionaryTests {
    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private WordRetrievalService wordRetrievalService;

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
    void getVerbs() {
        Word word = wordRepository.findById(32L).get();
        Word word2 = wordRepository.findById(204L).get();
        Word notVerb = wordRepository.findById(190L).get();
        VerbDTO verbDTO1 = wordRetrievalService.getVerbDtoFromWord(word);
        VerbDTO verbDTO2 = wordRetrievalService.getVerbDtoFromWord(word2);
        VerbDTO verbDTO3 = wordRetrievalService.getVerbDtoFromWord(notVerb);

        Assertions.assertAll(
                () -> Assertions.assertEquals(verbDTO1.getPresentFuturePluralSecond(), "мо'жете"),
                () -> Assertions.assertEquals(verbDTO1.getFemininePast(), "могла'"),
                () -> Assertions.assertEquals(verbDTO1.getPresentFuturePluralThird(), "мо'гут"),
                () -> Assertions.assertEquals(verbDTO2.getPartners().get(0), "смотреть"),
                () -> Assertions.assertEquals(verbDTO2.getAspect(), VerbAspect.PERFECTIVE),
                () -> Assertions.assertEquals(verbDTO2.getPresentFutureSingularFirst(), "посмотрю'"),
                () -> Assertions.assertEquals(verbDTO3.getBareText(), "ERROR")
        );
    }

    @Test
    public void getNouns() {
        Word word1 = wordRepository.findById(185L).get();
        NounDTO nounDTO1 = wordRetrievalService.getNounDtoFromWord(word1);

        Assertions.assertAll(
                () -> Assertions.assertEquals(nounDTO1.getAccusativePlural(), "стра'ны"),
                () -> Assertions.assertEquals(nounDTO1.getPrepositionalSingular(), "стране'"),
                () -> Assertions.assertEquals(nounDTO1.getDativeSingular(), "стране'"),
                () -> Assertions.assertEquals(nounDTO1.getDativePlural(), "стра'нам")
        );
    }

    @Test
    public void getAdjectives() {
        Word word1 = wordRepository.findById(232L).get();
        Word word2 = wordRepository.findById(286L).get();
        AdjectiveDTO adjectiveDTO1 = wordRetrievalService.getAdjectiveDtoFromWord(word1);
        AdjectiveDTO adjectiveDTO2 = wordRetrievalService.getAdjectiveDtoFromWord(word2);

        Assertions.assertAll(
                () -> Assertions.assertNull(adjectiveDTO1.getComparative()),
                () -> Assertions.assertEquals(adjectiveDTO1.getFeminineNominative(), "сове'тская"),
                () -> Assertions.assertEquals(adjectiveDTO1.getMasculineInstrumental(), "сове'тским"),
                () -> Assertions.assertEquals(adjectiveDTO2.getFeminineInstrumental(), "кра'сной, кра'сною")
        );
    }

    @Test
    public void getOthers() {
        Word word = wordRepository.findById(187L).get();
        OtherDTO otherDTO = wordRetrievalService.getOtherDtoByWord(word);

        Assertions.assertAll(
                () -> Assertions.assertEquals("ме'жду", otherDTO.getAccentedText()),
                () -> Assertions.assertEquals(WordLevel.A2, otherDTO.getWordLevel())
        );
    }

    @Test
    public void getAdverbs() {
        Word word = wordRepository.findById(380L).get();
        AdverbDTO adverbDTO = wordRetrievalService.getAdverbDtoByWord(word);

        Assertions.assertAll(
                () -> Assertions.assertEquals("ма'ло", adverbDTO.getAccentedText()),
                () -> Assertions.assertEquals("мало", adverbDTO.getBareText()),
                () -> Assertions.assertEquals(2, adverbDTO.getTranslations().size())
        );
    }

    @Test
    public void getWordsFromAccentedText() {
        Set<Word> words1 = wordRetrievalService.getWordsFromAccentedText("водяны'х");
        Set<Word> words2 = wordRetrievalService.getWordsFromAccentedText("просро'ченных");

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, words1.size()),
                () -> Assertions.assertEquals(1, words2.size())
        );
    }
}
