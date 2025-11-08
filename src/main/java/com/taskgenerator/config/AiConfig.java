package com.taskgenerator.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AI Configuration with fallback to local LLM
 */
@Configuration
public class AiConfig {

    private static final Logger logger = LoggerFactory.getLogger(AiConfig.class);

    @Value("${spring.ai.openai.api-key:#{null}}")
    private String openAiApiKey;

    @Value("${spring.ai.openai.chat.options.model:gpt-3.5-turbo}")
    private String openAiModel;

    @Value("${spring.ai.openai.chat.options.temperature:0.7}")
    private Double temperature;

    @Value("${app.ai.use-local-llm:false}")
    private boolean useLocalLlm;

    @Value("${app.ai.ollama.base-url:http://localhost:11434}")
    private String ollamaBaseUrl;

    @Value("${app.ai.ollama.model:llama2}")
    private String ollamaModel;

    @Bean
    @Primary
    public ChatModel chatModel() {
        // Check if should use local LLM or if OpenAI key is not configured
        boolean shouldUseLocal = useLocalLlm || !isOpenAiConfigured();

        if (shouldUseLocal) {
            logger.info("🤖 Configuring Local LLM (Ollama) - Model: {}", ollamaModel);
            return createOllamaChatModel();
        } else {
            logger.info("🌐 Configuring OpenAI - Model: {}", openAiModel);
            return createOpenAiChatModel();
        }
    }

    @Bean
    @Primary
    public ChatClient.Builder chatClientBuilder(ChatModel chatModel) {
        logger.info("🔧 Creating ChatClient.Builder with ChatModel: {}", chatModel.getClass().getSimpleName());
        return ChatClient.builder(chatModel);
    }

    @Bean
    @Primary
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        logger.info("🔧 Creating ChatClient");
        return chatClientBuilder.build();
    }

    /**
     * Checks if OpenAI is configured
     */
    private boolean isOpenAiConfigured() {
        if (openAiApiKey == null || openAiApiKey.isBlank() ||
                openAiApiKey.equals("your-api-key-here") ||
                openAiApiKey.equals("${OPENAI_API_KEY}")) {
            logger.warn("⚠️  OpenAI key not configured. Using local LLM as fallback.");
            return false;
        }
        return true;
    }

    /**
     * Creates ChatModel for OpenAI
     */
    private ChatModel createOpenAiChatModel() {
        try {
            OpenAiApi openAiApi = new OpenAiApi(openAiApiKey);

            OpenAiChatOptions options = OpenAiChatOptions.builder()
                    .withModel(openAiModel)
                    .withTemperature(temperature)
                    .build();

            return new OpenAiChatModel(openAiApi, options);
        } catch (Exception e) {
            logger.error("❌ Error configuring OpenAI: {}. Trying fallback to Ollama...", e.getMessage());
            return createOllamaChatModel();
        }
    }

    /**
     * Creates ChatModel for Ollama (Local LLM)
     */
    private ChatModel createOllamaChatModel() {
        try {
            logger.info("🚀 Starting connection to Ollama at: {}", ollamaBaseUrl);

            OllamaApi ollamaApi = new OllamaApi(ollamaBaseUrl);

            OllamaOptions options = OllamaOptions.create()
                    .withModel(ollamaModel)
                    .withTemperature(temperature);

            OllamaChatModel chatModel = new OllamaChatModel(ollamaApi, options);

            logger.info("✅ Ollama configured successfully!");
            return chatModel;
        } catch (Exception e) {
            logger.error("❌ Error configuring Ollama: {}", e.getMessage());
            logger.error("💡 Make sure Ollama is running: ollama serve");
            logger.error("💡 Install the model: ollama pull {}", ollamaModel);
            throw new RuntimeException("Failed to configure LLM. Check OpenAI or Ollama.", e);
        }
    }

    @Bean
    public String aiProviderInfo() {
        if (useLocalLlm || !isOpenAiConfigured()) {
            return String.format("Ollama (%s) - %s", ollamaModel, ollamaBaseUrl);
        } else {
            return String.format("OpenAI (%s)", openAiModel);
        }
    }
}
