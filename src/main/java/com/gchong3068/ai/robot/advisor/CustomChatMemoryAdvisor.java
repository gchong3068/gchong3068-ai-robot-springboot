package com.gchong3068.ai.robot.advisor;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gchong3068.ai.robot.domain.dos.ChatMessageDO;
import com.gchong3068.ai.robot.domain.mapper.ChatMessageMapper;
import com.gchong3068.ai.robot.model.vo.chat.AiChatReqVO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;
import reactor.core.publisher.Flux;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @Author: gchong3068
 * @Date: 2026/6/17 9:09
 * @Version: v1.0.0
 * @Description: 自定义对话记忆 Advisor
 **/
@Slf4j
public class CustomChatMemoryAdvisor implements StreamAdvisor {


    private final ChatMessageMapper chatMessageMapper;
    private final AiChatReqVO aiChatReqVO;
    private final int limit;


    public CustomChatMemoryAdvisor(ChatMessageMapper chatMessageMapper, AiChatReqVO aiChatReqVO, int limit) {
        this.chatMessageMapper = chatMessageMapper;
        this.aiChatReqVO = aiChatReqVO;
        this.limit = limit;
    }


    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {

        log.info("##自定义聊天记忆 Advisor...");

        String chatUuid = aiChatReqVO.getChatId();

        //查询数据库最新聊天信息
        List<ChatMessageDO> messages = chatMessageMapper.selectList(Wrappers.<ChatMessageDO>lambdaQuery()
                .eq(ChatMessageDO::getChatUuid, chatUuid)
                .orderByDesc(ChatMessageDO::getCreateTime)
                .last(String.format("LIMIT %d", limit)));

        //按时间排序
        List<ChatMessageDO> sortedMessages = messages.stream()
                .sorted(Comparator.comparing(ChatMessageDO::getCreateTime))
                .toList();

        List<Message> messageList = Lists.newArrayList();

        // 将数据库记录转换为对应类型的消息
        for (ChatMessageDO chatMessageDO : sortedMessages) {
            // 消息类型
            String type = chatMessageDO.getRole();
            if (Objects.equals(type, MessageType.USER.getValue())) { // 用户消息
                Message userMessage = new UserMessage(chatMessageDO.getContent());
                messageList.add(userMessage);
            } else if (Objects.equals(type, MessageType.ASSISTANT.getValue())) { // AI 助手消息
                Message assistantMessage = new AssistantMessage(chatMessageDO.getContent());
                messageList.add(assistantMessage);
            }
        }
        //添加当前用户信息
        messageList.addAll(chatClientRequest.prompt().getInstructions());

        // 构建一个新的 ChatClientRequest 请求对象
        ChatClientRequest processedChatClientRequest = chatClientRequest
                .mutate()
                .prompt(chatClientRequest.prompt().mutate().messages(messageList).build())
                .build();

        return streamAdvisorChain.nextStream(processedChatClientRequest);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return 2; // order 值越小，越先执行
    }
}
