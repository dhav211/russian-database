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
    @Value("${api.claude.key}")
    private String claudeKey;

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

    // TODO this should be removed, everything

//    // TODO this shouldn't require an AnthropicClient but a LLMContentGenerator, which can be claude or gemini
//    public void createSentencesForWord(Word word, AnthropicClient anthropicClient) {
//        List<Sentence> sentences = new ArrayList<>();
//
//        sentences.addAll(createSentenceListFromGeneratedSentences(callClaudeForSentenceGeneration(word, ReadingLevel.BEGINNER, anthropicClient), word, ReadingLevel.BEGINNER));
//        sentences.addAll(createSentenceListFromGeneratedSentences(callClaudeForSentenceGeneration(word, ReadingLevel.INTERMEDIATE, anthropicClient), word, ReadingLevel.INTERMEDIATE));
//        sentences.addAll(createSentenceListFromGeneratedSentences(callClaudeForSentenceGeneration(word, ReadingLevel.ADVANCED, anthropicClient), word, ReadingLevel.ADVANCED));
//
//        sentences.forEach((sentence -> word.getSentences().add(sentence)));
//
//        // TODO uncomment this actually save to the database
//        //sentenceRepository.saveAll(sentences);
//        //wordRepository.save(word);
//    }

//    // TODO extract this into the ClaudeContentGenerator class
//    private String callClaudeForSentenceGeneration(Word word, ReadingLevel readingLevel, AnthropicClient anthropicClient) {
//        String sentences = "";
//        try{
//            MessageCreateParams params = MessageCreateParams.builder()
//                    .maxTokens(8192L)
//                    .addUserMessage(buildPromptForSentenceGeneration(word, readingLevel))
//                    .model(Model.CLAUDE_3_5_HAIKU_20241022)
//                    .build();
//            Message message = anthropicClient.messages().create(params);
//            Optional<TextBlock> textBlock = message.content().getFirst().text();
//
//            if (textBlock.isPresent()) {
//                sentences = textBlock.get().text();
//            }
//        } catch (Exception e) {
//            // TODO return a real error here
//            System.out.println(e.getMessage());
//        }
//        return sentences;
//    }

}

