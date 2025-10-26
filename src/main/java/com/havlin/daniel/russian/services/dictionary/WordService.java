package com.havlin.daniel.russian.services.dictionary;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.generated_content.Definition;
import com.havlin.daniel.russian.entities.generated_content.Sentence;
import com.havlin.daniel.russian.entities.generated_content.WordInformation;
import com.havlin.daniel.russian.repositories.dictionary.WordRepository;
import com.havlin.daniel.russian.repositories.generated_content.DefinitionRepository;
import com.havlin.daniel.russian.repositories.generated_content.SentenceRepository;
import com.havlin.daniel.russian.repositories.generated_content.WordInformationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WordService {
    private final SentenceRepository sentenceRepository;
    private final DefinitionRepository definitionRepository;
    private final WordInformationRepository wordInformationRepository;
    private final WordRepository wordRepository;

    public WordService(SentenceRepository sentenceRepository, DefinitionRepository definitionRepository,
                       WordInformationRepository wordInformationRepository, WordRepository wordRepository) {
        this.sentenceRepository = sentenceRepository;
        this.definitionRepository = definitionRepository;
        this.wordInformationRepository = wordInformationRepository;
        this.wordRepository = wordRepository;
    }

    @Transactional
    public void saveGeneratedContentToWord(List<Sentence> sentences, List<Definition> definitions,
                                           WordInformation wordInformation, Word word) {
        sentenceRepository.saveAll(sentences);
        definitionRepository.saveAll(definitions);
        wordInformationRepository.save(wordInformation);
        wordRepository.saveAndFlush(word); // We need to get the IDs generated for the sentences and definitions, so flush it
    }
}
