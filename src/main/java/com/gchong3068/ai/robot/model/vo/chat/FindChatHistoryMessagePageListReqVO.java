package com.gchong3068.ai.robot.model.vo.chat;

import com.gchong3068.ai.robot.model.common.BasePageQuery;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: gchong3068
 * @Date: 2026/6/17 9:53
 * @Version: v1.0.0
 * @Description: 查询对话历史消息
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindChatHistoryMessagePageListReqVO  extends BasePageQuery {

    @NotBlank(message = "对话Id不能为空")
    private String chatId;
}
