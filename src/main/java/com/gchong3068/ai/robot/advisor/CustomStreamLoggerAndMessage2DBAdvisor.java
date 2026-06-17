package com.gchong3068.ai.robot.advisor;

import com.gchong3068.ai.robot.domain.dos.ChatMessageDO;
import com.gchong3068.ai.robot.domain.mapper.ChatMessageMapper;
import com.gchong3068.ai.robot.model.vo.chat.AiChatReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.transaction.support.TransactionTemplate;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author: gchong3068
 * @Date: 2026/6/16 18:48
 * @Version: v1.0.0
 * @Description: 自定义打印流式日志 Advisor
 **/
@Slf4j
public class CustomStreamLoggerAndMessage2DBAdvisor implements StreamAdvisor {

    private final ChatMessageMapper chatMessageMapper;
    private final AiChatReqVO aiChatReqVO;
    private final TransactionTemplate transactionTemplate;

    public CustomStreamLoggerAndMessage2DBAdvisor(ChatMessageMapper chatMessageMapper, AiChatReqVO aiChatReqVO, TransactionTemplate transactionTemplate) {
        this.chatMessageMapper = chatMessageMapper;
        this.aiChatReqVO = aiChatReqVO;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
        Flux<ChatClientResponse> chatClientResponseFlux = streamAdvisorChain.nextStream(chatClientRequest);

        String chatUuid = aiChatReqVO.getChatId();

        String userMessage = aiChatReqVO.getMessage();


        // 创建 AI 流式回答聚合容器（线程安全）
        AtomicReference<StringBuilder> fullContent = new AtomicReference<>(new StringBuilder());

        // 返回处理后的流
        return chatClientResponseFlux
                .doOnNext(response -> {
                    // 逐块收集内容
                    String chunk = response.chatResponse().getResult().getOutput().getText();

                    log.info("## chunk: {}", chunk);

                    // 若 chunk 块不为空，则追加到 fullContent 中
                    if (chunk != null) {
                        fullContent.get().append(chunk);
                    }
                })
                .doOnComplete(() -> {
                    // 流完成后打印完整回答
                    String completeResponse = fullContent.get().toString();
                    log.info("\n==== FULL AI RESPONSE ====\n{}\n========================", completeResponse);

                    //开启编程式事务
                    transactionTemplate.execute(status -> {
                        try{

                            //1. 储存用户信息
                            chatMessageMapper.insert(ChatMessageDO.builder()
                                            .chatUuid(chatUuid)
                                            .content(userMessage)
                                            .role(MessageType.USER.getValue())
                                            .createTime(LocalDateTime.now())
                                    .build());

                            //2. 储存AI回答
                            chatMessageMapper.insert(ChatMessageDO.builder()
                                            .chatUuid(chatUuid)
                                            .content(userMessage)
                                            .role(MessageType.ASSISTANT.getValue())
                                            .createTime(LocalDateTime.now())
                                    .build());

                            return true;
                        }catch (Exception e){
                            status.setRollbackOnly();  //标记回滚
                            log.error("",e);
                        }

                        return false;
                    });
                })
                .doOnError(error -> {
                    // 出错时打印已收集的部分
                    String partialResponse = fullContent.get().toString();
                    log.error("## Stream 流出现错误，已收集回答如下: {}", partialResponse, error);
                });
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return 99;
    }
}
