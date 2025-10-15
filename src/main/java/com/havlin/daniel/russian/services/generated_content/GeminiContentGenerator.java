package com.havlin.daniel.russian.services.generated_content;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class GeminiContentGenerator extends LLMContentGenerator{
    private static final Logger log = LoggerFactory.getLogger(GeminiContentGenerator.class);
    private final Client client;
    public GeminiContentGenerator(String key, CountDownLatch latch, String prompt) {
        super(key, latch, prompt);
        client = new Client();
    }

    @Override
    public String call() throws Exception {
        String output = "";
        try {
            GenerateContentResponse response = client.models.generateContent("gemini-2.5-flash", prompt, null);
            output = response.text();
        } catch (Exception e) {
            log.error("e: ", e); // TODO real error here
        } finally {
            latch.countDown();
        }

        return output;
    }
}
