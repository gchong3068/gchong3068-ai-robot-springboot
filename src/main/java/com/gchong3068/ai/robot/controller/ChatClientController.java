package com.gchong3068.ai.robot.controller;

import com.gchong3068.ai.robot.tools.DateTimeTools;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @Description: Chat Client 客户端
 * @Author: gchong3068
 * @Date: 2025/6/1 23:02
 * @Version: v1.0.0
 **/
@RestController
@RequestMapping("/v2/ai")
public class ChatClientController {

    @Resource
    private ChatClient chatClient;

    /**
     * 普通对话
     * @Auther: gchong3068
     * @Date: 2025/6/1 23:07
 	 * @param message 输入
 	 * @return java.lang.String
     **/
    @GetMapping("/generate")
    public String generate(@RequestParam(value = "message",defaultValue = "测试") String message){
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
    /**
     * 流式对话
     * @Auther: gchong3068
     * @Date: 2025/6/1 23:09
     * @param message 输入
     * @return reactor.core.publisher.Flux<java.lang.String>
     **/
    @GetMapping(value = "/generateStream" , produces = "text/html;charset=utf-8")
    public Flux<String> generateStream(@RequestParam(value = "message",defaultValue = "测试") String message,
                                       @RequestParam(value = "chatId") String chatId){
        return chatClient.prompt()
                .tools(new DateTimeTools())
                .user(message)
                .advisors(item -> item.param(ChatMemory.CONVERSATION_ID,chatId))  //对话隔离
                .stream()
                .content();
    }
}
