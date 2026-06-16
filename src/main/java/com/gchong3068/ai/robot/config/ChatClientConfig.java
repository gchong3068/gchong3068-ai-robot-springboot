package com.gchong3068.ai.robot.config;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: ChatClient 配置
 * @Author: gchong3068
 * @Date: 2025/6/1 22:57
 * @Version: v1.0.0
 **/
@Configuration
public class ChatClientConfig {

    @Resource
    private ChatMemory chatMemory;

    /**
     * 初始化ChatClient 客户端
     * @Auther: gchong3068
     * @Date: 2025/6/1 22:59
     * @param chatModel 模型
     * @return org.springframework.ai.chat.client.ChatClient
     **/
    @Bean
    public ChatClient chatClient(OpenAiChatModel chatModel ) {
        return ChatClient.builder(chatModel)
                .build();
    }
}
