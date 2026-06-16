package com.gchong3068.ai.robot.model.vo.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

/**
 * @Author: gchong3068
 * @Date: 2026/6/16 18:28
 * @Version: v1.0.0
 * @Description: TODO
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AiChatReqVO {


    @NotBlank(message = "用户信息不能为空")
    private String message;

    /**
     * 对话 ID
     */
    private String chatId;

    /**
     * 联网搜索
     */
    private Boolean networkSearch = false;

    @NotBlank(message = "调用的 AI 大模型名称不能为空")
    private String modelName;

    /**
     * 温度值，默认为 0.7
     */
    private Double temperature = 0.7;

}
