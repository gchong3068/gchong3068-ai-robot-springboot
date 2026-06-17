package com.gchong3068.ai.robot.controller;

import com.gchong3068.ai.robot.advisor.CustomChatMemoryAdvisor;
import com.gchong3068.ai.robot.advisor.CustomStreamLoggerAndMessage2DBAdvisor;
import com.gchong3068.ai.robot.advisor.NetworkSearchAdvisor;
import com.gchong3068.ai.robot.aspect.ApiOperationLog;
import com.gchong3068.ai.robot.domain.mapper.ChatMessageMapper;
import com.gchong3068.ai.robot.model.vo.chat.*;
import com.gchong3068.ai.robot.service.ChatService;
import com.gchong3068.ai.robot.service.SearXNGService;
import com.gchong3068.ai.robot.service.SearchResultContentFetcherService;
import com.gchong3068.ai.robot.utils.PageResponse;
import com.gchong3068.ai.robot.utils.Response;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

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
    @Resource
    private ChatMessageMapper chatMessageMapper;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private SearXNGService searXNGService;
    @Resource
    private SearchResultContentFetcherService searchResultContentFetcherService;

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
    @PostMapping(value = "/completion",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ApiOperationLog(description = "流式对话")
    public Flux<AIResponse> chat(@RequestBody @Validated AiChatReqVO aiChatReqVO){
        //用户信息
        String userMessage = aiChatReqVO.getMessage();
        //模型名称
        String modelName = aiChatReqVO.getModelName();
        //温度值
        Double temperature = aiChatReqVO.getTemperature();
        //是否开启联网搜索
        boolean networkSearch = aiChatReqVO.getNetworkSearch();

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

        // Advisor 集合
        List<Advisor> advisors = Lists.newArrayList();
        //是否开启联网搜索
        if (networkSearch){
            advisors.add(new NetworkSearchAdvisor(searXNGService,searchResultContentFetcherService));
        }else {
            //添加自定义对话记忆
            advisors.add(new CustomChatMemoryAdvisor(chatMessageMapper,aiChatReqVO,50));
        }

        // 添加自定义打印流式对话日志 Advisor
        advisors.add(new CustomStreamLoggerAndMessage2DBAdvisor(chatMessageMapper,aiChatReqVO,transactionTemplate));

        // 应用 Advisor 集合
        chatClientRequestSpec.advisors(advisors);

        //流式输出
        return chatClientRequestSpec
                .stream()
                .content()
                .mapNotNull(text -> AIResponse.builder().v(text).build());
    }

    @PostMapping("/message/list")
    @ApiOperationLog(description = "查询对话历史信息")
    public PageResponse<FindChatHistoryMessagePageListRspVO> findChatMessagePageList(FindChatHistoryMessagePageListReqVO findChatHistoryMessagePageListReqVO) {
        return chatService.findChatHistoryMessagePageList(findChatHistoryMessagePageListReqVO);
    }

    @PostMapping("/list")
    @ApiOperationLog(description = "查询历史对话")
    public PageResponse<FindChatHistoryPageListRspVO> findChatHistoryPageList (@RequestBody @Validated FindChatHistoryPageListReqVO findChatHistoryPageListReqVO){
        return chatService.findChatHistoryPageList(findChatHistoryPageListReqVO);
    }

    @PostMapping("/summary/rename")
    @ApiOperationLog(description ="重命名对话摘要")
    public Response<?> renameChatSummary(@RequestBody @Validated RenameChatReqVO renameChatReqVO) {
        return chatService.renameChatSummary(renameChatReqVO);
    }


    @PostMapping("/delete")
    @ApiOperationLog(description = "删除对话")
    public Response<?> deleteChat(@RequestBody @Validated DeleteChatReqVO deleteChatReqVO) {
        return chatService.deleteChat(deleteChatReqVO);
    }

}
