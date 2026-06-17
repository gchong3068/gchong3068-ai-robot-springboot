package com.gchong3068.ai.robot.model.vo.chat;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: gchong3068
 * @Date: 2026/6/17 10:19
 * @Version: v1.0.0
 * @Description: 对话重命名
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RenameChatReqVO {

    @NotNull(message = "对话Id不能为空")
    private Long id;

    @NotNull(message = "对话摘要半不能为空")
    private String summary;
}
