package com.havlin.daniel.russian.services.generated_content;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.models.messages.Message;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.Model;
import com.anthropic.models.messages.TextBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;

public class ClaudeContentGenerator extends LLMContentGenerator{
    private static final Logger log = LoggerFactory.getLogger(ClaudeContentGenerator.class);
    private final AnthropicClient client;

    public ClaudeContentGenerator(String key, CountDownLatch latch, String prompt) {
        super(key, latch, prompt);
        this.client = AnthropicOkHttpClient.builder()
                .apiKey(key)
                .build();
    }

    @Override
    public String call() throws Exception {
        String output = "";
        try {
            MessageCreateParams params = MessageCreateParams.builder()
                    .maxTokens(4096L)
                    .addUserMessage(prompt)
                    .model(Model.CLAUDE_3_5_HAIKU_20241022)
                    .build();
            Message message = client.messages().create(params);
            Optional<TextBlock> textBlock = message.content().getFirst().text();

            if (textBlock.isPresent()) {
                output = textBlock.get().text();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        finally {
            latch.countDown();
        }

        return output;
    }
}
