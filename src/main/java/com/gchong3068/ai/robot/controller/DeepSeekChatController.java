package com.gchong3068.ai.robot.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @Description: DeepSeek 聊天 V3
 * @Author: gchong3068
 * @Date: 2025/5/30 20:53
 * @Version: v1.0.0
 **/
@RestController
@RequestMapping("/ai")
public class DeepSeekChatController {

    @Resource
    private DeepSeekChatModel chatModel;

    /**
     * 普通对话
     * @Auther: gchong3068
     * @Date: 2025/5/30 21:02
     * @param message
     * @return java.lang.String
     **/
    @GetMapping("/generate")
    public String generate(@RequestParam(value = "message",defaultValue = "测试") String message){
        return chatModel.call(message);
    }


    /**
     * 流式输出
     * @Auther: gchong3068
     * @Date: 2025/5/30 21:05
 	 * @param message
 	 * @return reactor.core.publisher.Flux<java.lang.String>
     **/
    @GetMapping(value = "/generateStream",produces = "text/html;charset=utf-8")
    public Flux<String> generateStream(@RequestParam(value = "message" , defaultValue = "测试")String message){
        Prompt prompt = new Prompt(new UserMessage(message));

        return chatModel.stream(prompt)
                .mapNotNull(chatResponse -> chatResponse.getResult().getOutput().getText());
    }

}
