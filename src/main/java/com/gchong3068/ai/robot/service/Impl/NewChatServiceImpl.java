package com.gchong3068.ai.robot.service.Impl;

import com.gchong3068.ai.robot.domain.dos.ChatDO;
import com.gchong3068.ai.robot.domain.mapper.ChatMapper;
import com.gchong3068.ai.robot.model.vo.chat.NewChatReqVO;
import com.gchong3068.ai.robot.model.vo.chat.NewChatRspVO;
import com.gchong3068.ai.robot.service.ChatService;
import com.gchong3068.ai.robot.utils.Response;
import com.gchong3068.ai.robot.utils.StringUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @Author: gchong3068
 * @Date: 2026/6/16 18:21
 * @Version: v1.0.0
 * @Description: TODO
 **/
@Service
@Slf4j
public class NewChatServiceImpl implements ChatService {

    @Resource
    private ChatMapper chatMapper;

    /**
     * 新建对话
     * @param newChatReqVO
     * @return
     */
    @Override
    public Response<NewChatRspVO> newChat(NewChatReqVO newChatReqVO) {

        //用户发送的信息
        String message = newChatReqVO.getMessage();
        //生成对话UUID
        String uuid = UUID.randomUUID().toString();

        // 截取用户发送的消息，作为对话摘要
        String summary = StringUtil.truncate(message, 20);

        //储存对话到数据库中
        chatMapper.insert(ChatDO.builder()
                        .summary(summary)
                        .uuid(uuid)
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                .build());

        //返回摘要
        return Response.success(NewChatRspVO.builder()
                        .uuid(uuid)
                        .summary(summary)
                .build());

    }



}
