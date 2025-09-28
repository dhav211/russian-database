package com.havlin.daniel.russian.controllers.generated_content;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.dictionary.WordForm;
import com.havlin.daniel.russian.entities.generated_content.Sentence;
import com.havlin.daniel.russian.repositories.dictionary.WordFormRepository;
import com.havlin.daniel.russian.repositories.dictionary.WordRepository;
import com.havlin.daniel.russian.services.dictionary.DefinitionService;
import com.havlin.daniel.russian.services.dictionary.SentenceService;
import com.havlin.daniel.russian.services.retrieval.WordRetrievalService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class GenerateContentController {
    @Value("${api.claude.key}")
    private String claudeKey;

    private final WordRetrievalService wordRetrievalService;
    private final DefinitionService definitionService;
    private final SentenceService sentenceService;
    private AnthropicClient anthropicClient;

    public GenerateContentController(WordRetrievalService wordRetrievalService,
                                     DefinitionService definitionService,
                                     SentenceService sentenceService) {
        this.wordRetrievalService = wordRetrievalService;
        this.definitionService = definitionService;
        this.sentenceService = sentenceService;
    }

    @PostMapping(value = "/addAll/{accentedWord}")
    public void addAllInitialContentForWord(@PathVariable("accentedWord") String accentedWord) {
        anthropicClient = AnthropicOkHttpClient.builder()
                .apiKey(claudeKey)
                .build();
        try {
            Set<Word> words = wordRetrievalService.getWordsFromAccentedText(accentedWord);

            if (!words.isEmpty()) {
                for (Word word : words) {
                    if (word.getDefinition() == null) {
                        //definitionService.createDefinitionForWord(word, anthropicClient);
                    }
                    if (word.getSentences().isEmpty()) {
                        sentenceService.createSentencesForWord(word, anthropicClient);
                    }
                }
            }
        } catch (Exception e) {
            // return an empty object
        }

    }
}
