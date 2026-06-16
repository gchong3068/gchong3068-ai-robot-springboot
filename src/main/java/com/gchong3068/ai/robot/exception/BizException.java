package com.gchong3068.ai.robot.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: gchong3068
 * @Date: 2026/6/16 17:27
 * @Version: v1.0.0
 * @Description: 业务异常
 **/
@Getter
@Setter
public class BizException extends RuntimeException{

    private String errorCode;

    private String errorMessage;

    public BizException(BaseExceptionInterface baseExceptionInterface){
        this.errorCode = baseExceptionInterface.getErrorCode();
        this.errorMessage = baseExceptionInterface.getErrorMessage();
    }


}
