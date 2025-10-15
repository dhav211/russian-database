package com.havlin.daniel.russian.services.generated_content;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.dictionary.WordForm;
import com.havlin.daniel.russian.entities.generated_content.Definition;
import com.havlin.daniel.russian.entities.generated_content.ReadingLevel;
import com.havlin.daniel.russian.entities.generated_content.Sentence;
import com.havlin.daniel.russian.entities.generated_content.WordInformation;
import com.havlin.daniel.russian.repositories.dictionary.WordFormRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


@Service
public class GeneratedContentService {
    @Value("${api.claude.key}")
    private String claudeKey;

    //@Value("${api.gemini.key}")
    private String geminiKey = "CREATE_ENVIRONMENTAL_VARIABLE";

    private final PromptGenerator promptGenerator;
    private final SentenceGenerator sentenceGenerator;
    private final DefinitionGenerator definitionGenerator;

    public GeneratedContentService(WordFormRepository wordFormRepository) {
        this.promptGenerator = new PromptGenerator(wordFormRepository);
        this.sentenceGenerator = new SentenceGenerator(wordFormRepository);
        this.definitionGenerator = new DefinitionGenerator(wordFormRepository);
    }

    @Transactional
    public void generateContentForWord(Word word, AiModel aiModel) {
        int numberOfThreads = 8;

        try(ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads)) {
            // Once this countdown reaches zero we know all the threads have completed and we can move forward
            // with parsing the responses
            CountDownLatch latch = new CountDownLatch(numberOfThreads);

            // Here we start calling the llm api on threads
            LLMContentGenerator shortDefinitionGenerator = aiModel == AiModel.CLAUDE ?
                    new ClaudeContentGenerator(claudeKey, latch, promptGenerator.getShortDefinitionPrompt(word)) :
                    new GeminiContentGenerator("NEEDSKEY", latch, promptGenerator.getShortDefinitionPrompt(word));
            Future<String> shortDefinitionFuture = executor.submit(shortDefinitionGenerator);

            String[] wordInformationPrompts = promptGenerator.getWordInformationPrompts(word);
            WordInformationFutureHolder wordInformationFutureHolder = new WordInformationFutureHolder();
            for (int i = 0; i < wordInformationPrompts.length; i++) {
                LLMContentGenerator wordInformationGenerator = aiModel == AiModel.CLAUDE ?
                        new ClaudeContentGenerator(claudeKey, latch, wordInformationPrompts[i]) :
                        new GeminiContentGenerator(geminiKey, latch, wordInformationPrompts[i]);
                switch (i) {
                    case 0 -> wordInformationFutureHolder.definition = executor.submit(wordInformationGenerator);
                    case 1 -> wordInformationFutureHolder.etymology = executor.submit(wordInformationGenerator);
                    case 2 -> wordInformationFutureHolder.usageContext = executor.submit(wordInformationGenerator);
                    case 3 -> wordInformationFutureHolder.formation = executor.submit(wordInformationGenerator);
                }
            }

            // We are keeping the sentence futures in a map so we can keep track of the reading level
            Map<ReadingLevel, Future<String>> uneditedGeneratedSentenceFutures = new HashMap<>();
            for (ReadingLevel readingLevel : ReadingLevel.values()) {
                LLMContentGenerator sentenceGenerator = aiModel == AiModel.CLAUDE ?
                        new ClaudeContentGenerator(claudeKey, latch, promptGenerator.buildPromptForSentenceGeneration(word, readingLevel)) :
                        new GeminiContentGenerator(geminiKey, latch, promptGenerator.buildPromptForSentenceGeneration(word, readingLevel));
                uneditedGeneratedSentenceFutures.put(readingLevel, executor.submit(sentenceGenerator));
            }

            // Once all threads have completed the countdown latch will complete and let the main thread move forward
            latch.await();

            List<Sentence> sentences = new ArrayList<>();

            for (Map.Entry<ReadingLevel, Future<String>> entry : uneditedGeneratedSentenceFutures.entrySet()) {
                sentences.addAll(sentenceGenerator.createSentenceListFromGeneratedSentences(entry.getValue().get(), word, entry.getKey()));
            }

            for (Sentence sentence : sentences) {
                // save to the sentence repo
            }

            // TODO make sure at least 1 of the definitions passed corrections. If not, redo it
            List<Definition> definitions = definitionGenerator.createShortDefinitions(shortDefinitionFuture.get(), word);
            WordInformation wordInformation = definitionGenerator
                    .createWordInformation(
                            wordInformationFutureHolder.definition.get(),
                            wordInformationFutureHolder.etymology.get(),
                            wordInformationFutureHolder.usageContext.get(),
                            wordInformationFutureHolder.formation.get(),
                            word);


            System.out.println("MADE IT!");

            // save the sentences, definitions, word information, and the word to the database
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    static class SentenceSet {
        String russianText;
        String englishText;
        String grammarForm;
    }

    static class WordInformationFutureHolder {
        Future<String> definition;
        Future<String> etymology;
        Future<String> usageContext;
        Future<String> formation;
    }
}
