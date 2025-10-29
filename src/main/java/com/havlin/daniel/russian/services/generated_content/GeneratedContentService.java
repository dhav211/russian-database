package com.havlin.daniel.russian.services.generated_content;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.generated_content.*;
import com.havlin.daniel.russian.repositories.dictionary.WordFormRepository;
import com.havlin.daniel.russian.repositories.dictionary.WordRepository;
import com.havlin.daniel.russian.repositories.generated_content.DefinitionRepository;
import com.havlin.daniel.russian.repositories.generated_content.GeneratedContentErrorRepository;
import com.havlin.daniel.russian.repositories.generated_content.SentenceRepository;
import com.havlin.daniel.russian.repositories.generated_content.WordInformationRepository;
import com.havlin.daniel.russian.services.dictionary.WordService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;


@Service
public class GeneratedContentService {
    @Value("${api.claude.key}")
    private String claudeKey;

    private final PromptGenerator promptGenerator;
    private final SentenceGenerator sentenceGenerator;
    private final DefinitionGenerator definitionGenerator;
    private final WordService wordService;
    private final GeneratedContentErrorService generatedContentErrorService;

    public GeneratedContentService(WordFormRepository wordFormRepository,
                                   GeneratedContentErrorService generatedContentErrorService,
                                   WordService wordService
                                   ) {
        this.promptGenerator = new PromptGenerator(wordFormRepository);
        this.generatedContentErrorService = generatedContentErrorService;
        this.sentenceGenerator = new SentenceGenerator(wordFormRepository, generatedContentErrorService);
        this.definitionGenerator = new DefinitionGenerator(wordFormRepository, generatedContentErrorService);
        this.wordService = wordService;
    }

    /**
     * The entry point to generate content for a word. Currently, will generate sentences in all it's forms in multiple
     * reading levels, a few short definitions, and the word information (etymology, formation, etc.). If simple errors
     * are detected in any of the content it will try to correct the errors, if not the content will need approval and
     * an error will be saved to the database.
     * @param word A valid word entity that content will be generated for
     * @param aiModel LLM model used to generate the content, right now we have Gemini and Claude.
     */
    public void generateContentForWord(Word word, AiModel aiModel) {
        if (word == null) { // Since the word supplied was null we cannot go any further
            return;
        }

        GeneratedContentDTO generatedContentDTO = getGeneratedContent(word, aiModel);

        if (generatedContentDTO.hasError) { // There was an error in calling the API, exit from the function
            return;
        }
        CorrectedContentWithErrorsDTO correctedContentWithErrorsDTO = createCorrectedContentEntities(generatedContentDTO, word);
        wordService.saveGeneratedContentToWord(
                correctedContentWithErrorsDTO.sentenceWithErrors.stream().map((s) -> s.sentence).toList(),
                correctedContentWithErrorsDTO.definitionWithErrors.stream().map((d) -> d.definition).toList(),
                correctedContentWithErrorsDTO.wordInformation,
                word);
        generatedContentErrorService.addErrors(correctedContentWithErrorsDTO.sentenceWithErrors, correctedContentWithErrorsDTO.definitionWithErrors);
    }

    private GeneratedContentDTO getGeneratedContent(Word word, AiModel aiModel) {
        int numberOfThreads = 8;
        GeneratedContentDTO generatedContentDTO = new GeneratedContentDTO();

        try(ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads)) {

            // Once this countdown reaches zero we know all the threads have completed and we can move forward
            // with parsing the responses
            CountDownLatch latch = new CountDownLatch(numberOfThreads);

            // Here we start calling the llm api on threads
            LLMContentGenerator shortDefinitionGenerator = aiModel == AiModel.CLAUDE ?
                    new ClaudeContentGenerator(claudeKey, latch, promptGenerator.getShortDefinitionPrompt(word)) :
                    new GeminiContentGenerator(latch, promptGenerator.getShortDefinitionPrompt(word));
            Future<String> definitionFuture = executor.submit(shortDefinitionGenerator);

            String[] wordInformationPrompts = promptGenerator.getWordInformationPrompts(word);
            WordInformationFutureHolder wordInformationFutureHolder = new WordInformationFutureHolder();
            for (int i = 0; i < wordInformationPrompts.length; i++) {
                LLMContentGenerator wordInformationGenerator = aiModel == AiModel.CLAUDE ?
                        new ClaudeContentGenerator(claudeKey, latch, wordInformationPrompts[i]) :
                        new GeminiContentGenerator(latch, wordInformationPrompts[i]);
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
                        new GeminiContentGenerator(latch, promptGenerator.buildPromptForSentenceGeneration(word, readingLevel));
                uneditedGeneratedSentenceFutures.put(readingLevel, executor.submit(sentenceGenerator));
            }

            // Once all threads have completed the countdown latch will complete and let the main thread move forward
            latch.await();

            // Get all the generated data out of the futures and into a DTO which can be passed along for turning
            // into actual entities
            for (Map.Entry<ReadingLevel, Future<String>> entry : uneditedGeneratedSentenceFutures.entrySet()) {
                generatedContentDTO.sentences.put(entry.getKey(), entry.getValue().get());
            }

            generatedContentDTO.definitions = definitionFuture.get();

            generatedContentDTO.wordInformation.definition = wordInformationFutureHolder.definition.get();
            generatedContentDTO.wordInformation.etymology = wordInformationFutureHolder.etymology.get();
            generatedContentDTO.wordInformation.formation = wordInformationFutureHolder.formation.get();
            generatedContentDTO.wordInformation.usageContext = wordInformationFutureHolder.usageContext.get();
        } catch (InterruptedException | ExecutionException | NullPointerException e) {
            e.printStackTrace();
            return generatedContentDTO.ERRORS();
        }

        return generatedContentDTO;
    }

    CorrectedContentWithErrorsDTO createCorrectedContentEntities(GeneratedContentDTO generatedContentDTO, Word word) {
        List<SentenceWithErrors> sentences = new ArrayList<>();

        for (Map.Entry<ReadingLevel, String> entry : generatedContentDTO.sentences.entrySet()) {
            sentences.addAll(sentenceGenerator.createSentenceListFromGeneratedSentences(entry.getValue(), word, entry.getKey()));
        }

        List<DefinitionWithErrors> definitions = definitionGenerator.createShortDefinitions(generatedContentDTO.definitions, word);
        WordInformation wordInformation = definitionGenerator
                .createWordInformation(
                        generatedContentDTO.wordInformation.definition,
                        generatedContentDTO.wordInformation.etymology,
                        generatedContentDTO.wordInformation.usageContext,
                        generatedContentDTO.wordInformation.formation,
                        word);

        return new CorrectedContentWithErrorsDTO(sentences, definitions, wordInformation);
    }

    static class GeneratedSentenceDTO {
        String russianText;
        String englishText;
        String grammarForm;
    }

    static class CorrectedContentWithErrorsDTO {
        public CorrectedContentWithErrorsDTO(List<SentenceWithErrors> sentenceWithErrors,
                                             List<DefinitionWithErrors> definitionWithErrors,
                                             WordInformation wordInformation) {
            this.sentenceWithErrors = sentenceWithErrors;
            this.definitionWithErrors = definitionWithErrors;
            this.wordInformation = wordInformation;
        }

        List<SentenceWithErrors> sentenceWithErrors;
        List<DefinitionWithErrors> definitionWithErrors;
        WordInformation wordInformation;
    }

    private static class WordInformationFutureHolder {
        Future<String> definition;
        Future<String> etymology;
        Future<String> usageContext;
        Future<String> formation;
    }

    static class WordInformationDTO {
        String definition;
        String etymology;
        String usageContext;
        String formation;
    }

    static class SentenceWithErrors {
        SentenceWithErrors(Sentence sentence, List<GeneratedContentError> errors) {
            this.sentence = sentence;
            this.errors = errors;
        }

        Sentence sentence;
        List<GeneratedContentError> errors;
    }

    static class DefinitionWithErrors {
        DefinitionWithErrors(Definition definition, List<GeneratedContentError> errors) {
            this.definition = definition;
            this.errors = errors;
        }

        Definition definition;
        List<GeneratedContentError> errors;
    }
}
