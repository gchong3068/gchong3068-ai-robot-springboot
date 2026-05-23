package com.gchong3068.ai.robot.controller;

import com.gchong3068.ai.robot.model.AIResponse;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Description: 百炼
 * @Author: gchong3068
 * @Date: 2025/7/28 16:26
 * @Version: v1.0.0
 **/
@RestController
@RequestMapping("/v6/ai")
public class AliyunBailianController {

    @Resource
    private OpenAiChatModel chatModel;


    // 存储聊天对话
    private Map<String, List<Message>> chatMemoryStore = new ConcurrentHashMap<>();

    /**
     * 普通对话
     * @Auther: gchong3068
     * @Date: 2025/7/28 16:28
     * @param message 输入
     * @return java.lang.String
     **/
    @GetMapping("/generate")
    public String generate(@RequestParam(value = "message", defaultValue = "你是谁？") String message,
                           @RequestParam(value = "chatId") String chatId) {
        // 根据 chatId 获取对话记录
        List<Message> messages = chatMemoryStore.get(chatId);
        // 若不存在，则初始化一份
        if (CollectionUtils.isEmpty(messages)) {
            messages = new ArrayList<>();
            chatMemoryStore.put(chatId, messages);
        }

        // 添加 “用户角色消息” 到聊天记录中
        messages.add(new UserMessage(message));

        // 构建提示词
        Prompt prompt = new Prompt(messages);
        // 一次性返回结果
        String responseText = chatModel.call(prompt).getResult().getOutput().getText();

        // 添加 “助手角色消息” 到聊天记录中
        messages.add(new AssistantMessage(responseText));

        return responseText;
    }


    /**
     * 流式对话
     * @Auther: gchong3068
     * @Date: 2025/7/28 16:29
     * @param message 输入
     * @return reactor.core.publisher.Flux<java.lang.String>
     **/
    @GetMapping(value = "/generateStream", produces =  MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AIResponse> generateStream(@RequestParam(value = "message", defaultValue = "你是谁？") String message) {
        // 构建提示词
        Prompt prompt = new Prompt(new UserMessage(message));

        // 流式输出
        return chatModel.stream(prompt)
                .mapNotNull(chatResponse -> {
                    Generation generation = chatResponse.getResult();
                    String text = generation.getOutput().getText();
                    return AIResponse.builder().v(text).build();
                });
    }
}
