package com.gchong3068.ai.robot.aspect;

import java.lang.annotation.*;

/**
 * @Author: gchong3068
 * @Date: 2026/6/16 17:52
 * @Version: v1.0.0
 * @Description: TODO
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ApiOperationLog {

    /**
     * API 功能描述
     * @return
     */
    String description() default "";

}
