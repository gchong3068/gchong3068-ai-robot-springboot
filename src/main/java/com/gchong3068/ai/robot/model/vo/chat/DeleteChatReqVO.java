package com.gchong3068.ai.robot.model.vo.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: gchong3068
 * @Date: 2026/6/17 10:30
 * @Version: v1.0.0
 * @Description: 删除对话
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteChatReqVO {


    @NotBlank(message = "对话 UUID 不能为空")
    private String uuid;

}
