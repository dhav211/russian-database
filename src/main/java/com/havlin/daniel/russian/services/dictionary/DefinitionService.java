package com.havlin.daniel.russian.services.dictionary;

import com.havlin.daniel.russian.entities.dictionary.Definition;
import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.repositories.dictionary.DefinitionRepository;
import com.havlin.daniel.russian.repositories.dictionary.WordRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DefinitionService {
    private WordRepository wordRepository;
    private DefinitionRepository definitionRepository;

    @Value("${api.claude.key}")
    private String claudeKey;

    public DefinitionService(WordRepository wordRepository, DefinitionRepository definitionRepository) {
        this.wordRepository = wordRepository;
        this.definitionRepository = definitionRepository;
    }

    public void createDefinitionForWord(Word word) {
        if (word.getDefinition() == null) {
            DefinitionSet definitionSet = generateDefinition(word.getAccented());
            Definition definition = new Definition();
            definition.setLongDefinition(definitionSet.longDefinition);
            definition.setShortDefinition(definitionSet.shortDefinition);

            word.setDefinition(definition);

            definitionRepository.save(definition);
            wordRepository.save(word);
        }
    }

    private DefinitionSet generateDefinition(String word) {
        DefinitionSet set = new DefinitionSet();

        // This is the prompt for short definition
        //Write a short one sentence definition for the word собака in B1 level Russian. Put a ' after the letter of the word that needs to be stressed. Do not use the word собака and do not include a —.

        // This is the prompt for long definition
        //Write a multiple sentence definition for the word собака using B1 level Russian. Put a ' after the letter of the word that needs to be stressed. Do not use the word собака and do not include a —


        // TODO this a temporary, claude will generate these

        set.shortDefinition = "Э'то дома'шнее живо'тное, кото'рое о'чень лю'бит люде'й и мо'жет охраня'ть дом.";
        set.longDefinition = "Э'то дома'шнее живо'тное, кото'рое живё'т с людьми'. Оно' о'чень ве'рное и у'мное. Мо'жет охраня'ть дом и игра'ть с детьми'. Лю'бит гуля'ть, бе'гать и есть специа'льную е'ду. Быва'ет ра'зных разме'ров и цвето'в. Мно'гие семьи' держа'т тако'го пито'мца дома'.";

        return set;
    }

    private class DefinitionSet {
        private String shortDefinition;
        private String longDefinition;
    }
}
