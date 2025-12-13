package com.havlin.daniel.russian.services.dictionary;

import com.anthropic.client.AnthropicClient;
import com.anthropic.models.messages.Message;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.Model;
import com.anthropic.models.messages.TextBlock;
import com.havlin.daniel.russian.entities.dictionary.*;
import com.havlin.daniel.russian.entities.generated_content.GeneratedSentenceGrammarForm;
import com.havlin.daniel.russian.entities.generated_content.ReadingLevel;
import com.havlin.daniel.russian.entities.generated_content.Sentence;
import com.havlin.daniel.russian.repositories.generated_content.SentenceRepository;
import com.havlin.daniel.russian.repositories.dictionary.WordFormRepository;
import com.havlin.daniel.russian.repositories.dictionary.WordRepository;
import com.havlin.daniel.russian.utils.GeneratedContentChecker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SentenceService {
    private final WordFormRepository wordFormRepository;
    private final WordRepository wordRepository;
    private final SentenceRepository sentenceRepository;

    public SentenceService(WordFormRepository wordFormRepository,
                           WordRepository wordRepository,
                           SentenceRepository sentenceRepository) {
        this.wordFormRepository = wordFormRepository;
        this.wordRepository = wordRepository;
        this.sentenceRepository = sentenceRepository;
    }

    public boolean removeSentence(Sentence sentence) {
        try {
            sentence.removeWords();
            sentenceRepository.delete(sentence);
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public void findAndRemoveDuplicateSentences() {
        List<Sentence> duplicates = sentenceRepository.findDuplicateSentences();
        Map<String, List<Sentence>> sortedDuplicates = new HashMap<>();

        for (Sentence sentence : duplicates) {
            if (!sortedDuplicates.containsKey(sentence.getText())) {
                sortedDuplicates.put(sentence.getText(), new ArrayList<>());
            }
            sortedDuplicates.get(sentence.getText()).add(sentence);
        }

        for (String key :sortedDuplicates.keySet()) {
            int count = 0;
            for (Sentence sentence : sortedDuplicates.get(key)) {
                if (count > 0) {
                    removeSentence(sentence);
                }
                count++;
            }
        }
    }

}

