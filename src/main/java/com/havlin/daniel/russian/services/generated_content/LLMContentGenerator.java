package com.havlin.daniel.russian.services.generated_content;

import com.anthropic.client.okhttp.AnthropicOkHttpClient;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public abstract class LLMContentGenerator implements Callable<String> {
    protected final String key;
    protected final CountDownLatch latch;
    protected final String prompt;

    public LLMContentGenerator(String key, CountDownLatch latch, String prompt) {
        this.key = key;
        this.latch = latch;
        this.prompt = prompt;
    }
    public abstract String call() throws Exception;
}
