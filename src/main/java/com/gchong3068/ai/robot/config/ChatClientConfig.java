package com.gchong3068.ai.robot.config;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
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
    public ChatClient chatClient(DeepSeekChatModel chatModel, ToolCallbackProvider tools) {
        return ChatClient.builder(chatModel)
                .defaultToolCallbacks(tools) // MCP
//                .defaultSystem("请你扮演一名犬小哈 Java 项目实战专栏的客服人员")
                .defaultAdvisors(new SimpleLoggerAdvisor(), // 添加 Spring AI 内置的日志记录功能
//                                 new MyLoggerAdvisor(), // 添加自定义的日志打印 Advisor
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }



}
