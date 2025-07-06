package com.gchong3068.ai.robot.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;

/**
 * @Description: Advisor 日志记录
 * @Author: gchong3068
 * @Date: 2025/6/1 23:15
 * @Version: v1.0.0
 **/
@Slf4j
public class MyLoggerAdvisor implements CallAdvisor {
    

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        log.info("##请求入参：{}",chatClientRequest);
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
        log.info("##请求出参：{}",chatClientResponse);
        return chatClientResponse;
    }

    /**
     * 获取类名
     * @Auther: gchong3068
     * @Date: 2025/6/1 23:19
     * @return java.lang.String
     **/
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * 优先级 order值越小，越先执行
     * @Auther: gchong3068
     * @Date: 2025/6/1 23:18 
     * @return int
     **/
    @Override
    public int getOrder() {
        return 1; 
    }
}
