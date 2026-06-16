package com.gchong3068.ai.robot.enums;

import com.gchong3068.ai.robot.exception.BaseExceptionInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: gchong3068
 * @Date: 2026/6/16 17:29
 * @Version: v1.0.0
 * @Description: TODO
 **/
@Getter
@AllArgsConstructor
public enum ResponseCodeEnum implements BaseExceptionInterface {


    // ----------- 通用异常状态码 -----------
    SYSTEM_ERROR("10000", "出错啦，后台小哥正在努力修复中..."),
    PARAM_NOT_VALID("10001", "参数错误"),




    ;

    // 异常码
    private String errorCode;
    // 错误信息
    private String errorMessage;
}
