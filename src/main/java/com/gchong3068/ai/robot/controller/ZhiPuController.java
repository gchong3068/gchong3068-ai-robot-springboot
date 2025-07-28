package com.gchong3068.ai.robot.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @Description: 智谱
 * @Author: gchong3068
 * @Date: 2025/7/28 15:45
 * @Version: v1.0.0
 **/
@RestController
@RequestMapping("/v4/ai")
public class ZhiPuController {

    @Resource
    private ZhiPuAiChatModel chatModel;

    /**
     * 普通对话
     * @Auther: gchong3068
     * @Date: 2025/7/28 15:52
     * @param message 消息
     * @return java.lang.String
     **/
    @GetMapping("/generate")
    public String generate(@RequestParam(value = "message" , defaultValue = "你是谁？") String message){
        return chatModel.call(message);
    }


    /**
     * 流式对话
     * @Auther: gchong3068
     * @Date: 2025/7/28 16:08
     * @param message 消息
     * @return reactor.core.publisher.Flux<java.lang.String>
     **/
    @GetMapping(value = "/generateStream",produces = "text/html;charset=utf-8")
    public Flux<String> generateStream(@RequestParam(value = "message", defaultValue = "你是谁？") String message){
        // 构建提示词
        Prompt prompt = new Prompt(new UserMessage(message));

        return chatModel.stream(prompt)
                .mapNotNull(chatResponse ->  chatResponse.getResult().getOutput().getText());
    }
}
