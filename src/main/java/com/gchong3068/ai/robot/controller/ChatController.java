package com.gchong3068.ai.robot.controller;

import com.gchong3068.ai.robot.aspect.ApiOperationLog;
import com.gchong3068.ai.robot.model.vo.chat.AIResponse;
import com.gchong3068.ai.robot.model.vo.chat.AiChatReqVO;
import com.gchong3068.ai.robot.model.vo.chat.NewChatReqVO;
import com.gchong3068.ai.robot.service.ChatService;
import com.gchong3068.ai.robot.utils.Response;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @Author: gchong3068
 * @Date: 2026/6/16 18:26
 * @Version: v1.0.0
 * @Description: 对话
 **/
@RestController
@RequestMapping("/chat")
@Slf4j
public class ChatController {

    @Resource
    private ChatService chatService;
    @Value("${spring.ai.openai.base-url}")
    private String baseUrl;
    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @PostMapping("/new")
    @ApiOperationLog(description = "新建对话")
    public Response<?> newChat(@RequestBody @Validated NewChatReqVO newChatReqVO){
        return chatService.newChat(newChatReqVO);
    }


    /**
     * 流式对话
     * @param aiChatReqVO
     * @return
     */
    public Flux<AIResponse> chat(@RequestBody @Validated AiChatReqVO aiChatReqVO){
        //用户信息
        String userMessage = aiChatReqVO.getMessage();
        //模型名称
        String modelName = aiChatReqVO.getModelName();
        //温度值
        Double temperature = aiChatReqVO.getTemperature();

        //构建ChatModel
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .openAiApi(OpenAiApi.builder()
                        .baseUrl(baseUrl)
                        .apiKey(apiKey)
                        .build())
                .build();

        //动态设置调用模型名称、温度值
        ChatClient.ChatClientRequestSpec chatClientRequestSpec = ChatClient.create(chatModel)
                .prompt()
                .options(OpenAiChatOptions.builder()
                        .model(modelName)
                        .temperature(temperature)
                        .build())
                .user(userMessage);

        //流式输出
        return chatClientRequestSpec
                .stream()
                .content()
                .mapNotNull(text -> AIResponse.builder().v(text).build());
    }

}
