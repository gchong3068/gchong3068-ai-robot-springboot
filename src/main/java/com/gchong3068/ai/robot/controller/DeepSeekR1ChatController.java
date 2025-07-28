package com.gchong3068.ai.robot.controller;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekAssistantMessage;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Description: DeepSeek 聊天（R1 推理大模型）
 * @Author: gchong3068
 * @Date: 2025/5/31 20:20
 * @Version: v1.0.0
 **/
@RestController
@RequestMapping("/v1/ai")
public class DeepSeekR1ChatController {

    @Resource
    private DeepSeekChatModel chatModel;

    @GetMapping(value = "/generateStream", produces = "text/html;charset=utf-8")
    public Flux<String> generateStream(@RequestParam(value = "message",defaultValue = "测试")String message){
        Prompt prompt = new Prompt(new UserMessage(message));

        // 使用原子布尔值跟踪分隔线状态（每个请求独立）
        AtomicBoolean needSeparator = new AtomicBoolean(true);

        return chatModel.stream(prompt)
                .mapNotNull(chatResponse -> {
                    // 获取响应内容
                    DeepSeekAssistantMessage deepSeekAssistantMessage = (DeepSeekAssistantMessage) chatResponse.getResult().getOutput();
                    // 推理内容
                    String reasoningContent = deepSeekAssistantMessage.getReasoningContent();
                    // 推理结束后的正式回答
                    String text = deepSeekAssistantMessage.getText();

                    boolean isTextResponse = false;

                    String rawContent;
                    if (Objects.isNull(text)){
                        rawContent = reasoningContent;
                    }else {
                        rawContent = text;
                        isTextResponse = true;
                    }

                    //换行
                    String processed = StringUtils.isNotBlank(rawContent) ? rawContent.replace("\n","<br>") : rawContent;

                    // 添加分隔线
                    if (isTextResponse && needSeparator.compareAndSet(true,false)){
                        processed = "<hr>" + processed;
                    }

                    return processed;

                });
    }
}
