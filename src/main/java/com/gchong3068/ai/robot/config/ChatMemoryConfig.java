package com.gchong3068.ai.robot.config;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 记忆储存 配置
 * @Author: gchong3068
 * @Date: 2025/6/1 23:22
 * @Version: v1.0.0
 **/
@Configuration
public class ChatMemoryConfig {

    /**
     * 记忆储存
     */
    @Resource
    private ChatMemoryRepository chatMemoryRepository;

    /**
     * 初始化 ChatMemory
     * @Auther: gchong3068
     * @Date: 2025/6/1 23:25
     * @return org.springframework.ai.chat.memory.ChatMemory
     **/
    @Bean
    public ChatMemory chatMemory(){
        return MessageWindowChatMemory.builder()
                .maxMessages(50)    //最大信息窗口 50条
                .chatMemoryRepository(chatMemoryRepository)  //记忆储存
                .build();
    }

}
