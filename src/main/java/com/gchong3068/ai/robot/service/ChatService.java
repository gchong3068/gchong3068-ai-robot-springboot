package com.gchong3068.ai.robot.service;

import com.gchong3068.ai.robot.model.vo.chat.NewChatReqVO;
import com.gchong3068.ai.robot.model.vo.chat.NewChatRspVO;
import com.gchong3068.ai.robot.utils.Response;

/**
 * @Author: gchong3068
 * @Date: 2026/6/16 18:20
 * @Version: v1.0.0
 * @Description: 对话
 **/
public interface ChatService {


    /**
     * 新建对话
     * @param newChatReqVO
     * @return
     */
    Response<NewChatRspVO> newChat(NewChatReqVO newChatReqVO);



}
