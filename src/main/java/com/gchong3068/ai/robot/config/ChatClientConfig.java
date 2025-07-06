package com.gchong3068.ai.robot.config;

import com.gchong3068.ai.robot.advisor.MyLoggerAdvisor;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.deepseek.DeepSeekChatModel;
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
    public ChatClient chatClient(DeepSeekChatModel chatModel){
        return ChatClient.builder(chatModel)
                .defaultSystem("扮演一名客服")
                .defaultAdvisors(new SimpleLoggerAdvisor(), //添加Spring AI 日志记录
                                 //new MyLoggerAdvisor(), //添加自定义日志记录
                                 MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }



}
