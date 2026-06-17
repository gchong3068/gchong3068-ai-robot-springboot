package com.gchong3068.ai.robot.model.vo.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: gchong3068
 * @Date: 2026/6/17 9:53
 * @Version: v1.0.0
 * @Description: 查询对话历史信息
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindChatHistoryMessagePageListRspVO {

    /**
     * 消息 ID
     */
    private Long id;
    /**
     * 对话 ID
     */
    private String chatId;
    /**
     * 内容
     */
    private String content;
    /**
     * 消息类型
     */
    private String role;
    /**
     * 发布时间
     */
    private LocalDateTime createTime;

}
