package com.havlin.daniel.russian.services.generated_content;

import java.util.concurrent.CountDownLatch;

public class GeminiContentGenerator extends LLMContentGenerator{

    public GeminiContentGenerator(String key, CountDownLatch latch, String prompt) {
        super(key, latch, prompt);
        // TODO maybe there is client we have to add like for claude
    }

    @Override
    public String call() throws Exception {
        return "";
    }
}
