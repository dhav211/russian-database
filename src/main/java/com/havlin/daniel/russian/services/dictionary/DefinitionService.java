package com.havlin.daniel.russian.services.dictionary;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.models.messages.*;
import com.havlin.daniel.russian.entities.generated_content.Definition;
import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.repositories.generated_content.DefinitionRepository;
import com.havlin.daniel.russian.repositories.dictionary.WordRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefinitionService {
    private WordRepository wordRepository;
    private DefinitionRepository definitionRepository;

    public DefinitionService(WordRepository wordRepository, DefinitionRepository definitionRepository) {
        this.wordRepository = wordRepository;
        this.definitionRepository = definitionRepository;
    }

    public void createDefinitionForWord(Word word, AnthropicClient client) {
        DefinitionSet definitionSet = generateDefinition(word.getAccented(), client);
        Definition definition = new Definition();
        definition.setLongDefinition(definitionSet.longDefinition);
        definition.setShortDefinition(definitionSet.shortDefinition);

        word.setDefinition(definition);

        // TODO uncomment to save to database
        //definitionRepository.save(definition);
        //wordRepository.save(word);

    }

    // TODO uncouple this, we should have an interface for calling LLM, which we will just send the prompt string
    private DefinitionSet generateDefinition(String word, AnthropicClient client) {
        DefinitionSet set = new DefinitionSet();
        try {
            String shortDefinitionPrompt = "You need to write a short one sentence definition in Russian for the following word:\n" +
                    "\n" +
                    "<word>\n" +
                    word +
                    "</word>\n" +
                    "\n" +
                    "Your task is to create a definition for the that meets these specific requirements:\n" +
                    "- Write the definition in B1 level Russian (intermediate level - use clear, accessible vocabulary and grammar)\n" +
                    "- The definition must be exactly one sentence\n" +
                    "- Mark the stress by putting an apostrophe (') after the stressed vowel in your definition\n" +
                    "- Do not use the word itself anywhere in your definition\n" +
                    "- Do not include a dash (—) anywhere in your definition";

            MessageCreateParams params = MessageCreateParams.builder()
                    .maxTokens(1024L)
                    .addUserMessage(shortDefinitionPrompt)
                    .model(Model.CLAUDE_SONNET_4_20250514)
                    .build();
            Message message = client.messages().create(params);
            Optional<TextBlock> shortDefinitionTextBlock = message.content().getFirst().text();

            shortDefinitionTextBlock.ifPresent(textBlock -> set.shortDefinition = textBlock.text());

            String longDefinitionPrompt = "You need to write a multiple sentence definition in Russian for the following word:\n" +
                    "\n" +
                    "<word>\n" +
                    word +
                    "</word>\n" +
                    "\n" +
                    "Your task is to create a definition that meets these requirements:\n" +
                    "\n" +
                    "- Write the definition using B1 level Russian (intermediate level vocabulary and grammar)\n" +
                    "- The definition must be multiple sentences long\n" +
                    "- Do not use the word itself anywhere in your definition\n" +
                    "- Do not include an em dash (—) in your definition\n" +
                    "- Mark stress by placing an apostrophe (') immediately after the stressed vowel in each word that needs stress marking\n" +
                    "- Use clear, accessible language that a B1 Russian learner would understand\n" +
                    "\n" +
                    "For stress marking, place the apostrophe directly after the vowel that carries the stress. For example, if the stressed vowel is \"а\", write it as \"а'\".";

            params = MessageCreateParams.builder()
                    .maxTokens(1024L)
                    .addUserMessage(longDefinitionPrompt)
                    .model(Model.CLAUDE_3_5_SONNET_LATEST)
                    .build();
            message = client.messages().create(params);
            Optional<TextBlock> longDefinitionTextBlock = message.content().getFirst().text();

            longDefinitionTextBlock.ifPresent(textBlock -> set.longDefinition = textBlock.text());

        } catch (Exception e) {
            // TODO return a real error here
            String errormessage = e.getMessage();
            System.out.println(errormessage);
        }

        return set;
    }

    private class DefinitionSet {
        private String shortDefinition;
        private String longDefinition;
    }
}
