package com.gchong3068.ai.robot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 对话响应类
 * @Author: gchong3068
 * @Date: 2025/8/21 17:54
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AIResponse {
    private String v;
}
