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
import com.havlin.daniel.russian.services.generated_content.AiModel;
import com.havlin.daniel.russian.services.generated_content.GeneratedContentService;
import com.havlin.daniel.russian.services.retrieval.WordRetrievalService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class GenerateContentController {
    @Value("${api.claude.key}")
    private String claudeKey;

    private final WordRetrievalService wordRetrievalService;
    private final GeneratedContentService generatedContentService;

    public GenerateContentController(WordRetrievalService wordRetrievalService,
                                     GeneratedContentService generatedContentService) {
        this.wordRetrievalService = wordRetrievalService;
        this.generatedContentService = generatedContentService;
    }

    @PostMapping(value = "/addAll/{id}")
    public void addAllInitialContentForWord(@PathVariable("id") Long id) {
        try {
            Optional<Word> retrievedWord = wordRetrievalService.getWordByIdForSentenceCreation(id);

            if (retrievedWord.isPresent()) {
                Word word = retrievedWord.get();
                
                if (word.getSentences().isEmpty()) {
                    generatedContentService.generateContentForWord(word, AiModel.GEMINI);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
