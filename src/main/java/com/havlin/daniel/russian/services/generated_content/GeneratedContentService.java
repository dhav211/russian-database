package com.havlin.daniel.russian.services.generated_content;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.generated_content.*;
import com.havlin.daniel.russian.repositories.dictionary.WordFormRepository;
import com.havlin.daniel.russian.services.dictionary.WordService;
import com.havlin.daniel.russian.services.retrieval.WordRetrievalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;


@Service
public class GeneratedContentService {
    private static final Logger log = LoggerFactory.getLogger(GeneratedContentService.class);
    @Value("${api.claude.key}")
    private String claudeKey;

    private final PromptGenerator promptGenerator;
    private final SentenceGenerator sentenceGenerator;
    private final DefinitionGenerator definitionGenerator;
    private final WordService wordService;
    private final WordRetrievalService wordRetrievalService;

    public GeneratedContentService(WordFormRepository wordFormRepository,
                                   GeneratedContentErrorService generatedContentErrorService,
                                   WordService wordService,
                                   WordRetrievalService wordRetrievalService
                                   ) {
        this.promptGenerator = new PromptGenerator(wordFormRepository);
        this.wordRetrievalService = wordRetrievalService;
        this.sentenceGenerator = new SentenceGenerator(wordRetrievalService);
        this.definitionGenerator = new DefinitionGenerator(wordRetrievalService);
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
        try {
            if (word == null) { // Since the word supplied was null we cannot go any further
                return;
            }

            InstantiatedGeneratedContentDTO generatedContentDTO = getGeneratedContent(word, aiModel);

            if (generatedContentDTO.hasError) { // There was an error in calling the API, exit from the function
                return;
            }
            ApprovedGeneratedContent approvedContent = createCorrectedContentEntities(generatedContentDTO, word);
            wordService.saveGeneratedContentToWord(
                    approvedContent.sentences(),
                    approvedContent.definitions(),
                    approvedContent.words());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Call the LLM and get all the data for the requested word.
     * @param word The word we want to generate content for
     * @param aiModel The ai model we will use, either Gemini or Claude.
     * @return All the generated content in string form which will later be corrected and turned into entities
     */
    private InstantiatedGeneratedContentDTO getGeneratedContent(Word word, AiModel aiModel) {
        int numberOfThreads = 3;
        InstantiatedGeneratedContentDTO generatedContentDTO = new InstantiatedGeneratedContentDTO();

        try(ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads)) {

            // Once this countdown reaches zero we know all the threads have completed and we can move forward
            // with parsing the responses
            CountDownLatch latch = new CountDownLatch(numberOfThreads);

            // Here we start calling the llm api on threads
            LLMContentGenerator shortDefinitionGenerator = aiModel == AiModel.CLAUDE ?
                    new ClaudeContentGenerator(claudeKey, latch, promptGenerator.getShortDefinitionPrompt(word)) :
                    new GeminiContentGenerator(latch, promptGenerator.getShortDefinitionPrompt(word));
            Future<String> definitionFuture = executor.submit(shortDefinitionGenerator);

            // We are keeping the sentence futures in a map so we can keep track of the reading level
            Map<ReadingLevel, Future<String>> uneditedGeneratedSentenceFutures = new HashMap<>();
            for (ReadingLevel readingLevel : new ReadingLevel[]{ReadingLevel.BEGINNER, ReadingLevel.INTERMEDIATE}) {
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
        } catch (InterruptedException | ExecutionException | NullPointerException e) {
            e.printStackTrace();
            return generatedContentDTO.ERRORS();
        }

        return generatedContentDTO;
    }

    /**
     * We will take the string values given from the LLM, parse and correct them, finally turning them into entities
     * that can be saved into the database.
     * @param generatedContentDTO A POJO holds all the uncorrected string values given by the LLM
     * @param word The word that all the content is based upon
     * @return All the corrected entities ready to be saved to the database
     */
    ApprovedGeneratedContent createCorrectedContentEntities(InstantiatedGeneratedContentDTO generatedContentDTO, Word word) {
        Set<Sentence> sentences = new HashSet<>();
        // This will be passed into sentence generator method as a reference and continually added to
        Map<Long, Word> wordsToSave = new HashMap<>();

        for (Map.Entry<ReadingLevel, String> entry : generatedContentDTO.sentences.entrySet()) {
            sentences.addAll(sentenceGenerator.createSentenceListFromGeneratedSentences(entry.getValue(), wordsToSave, entry.getKey(), word));
        }

        Set<Definition> definitions = definitionGenerator.createShortDefinitions(generatedContentDTO.definitions, word);
        Set<Word> words = new HashSet<>(wordsToSave.values());

        return new ApprovedGeneratedContent(sentences, definitions, words);
    }

    static class GeneratedSentenceDTO {
        String russianText;
        String englishText;
    }

}
