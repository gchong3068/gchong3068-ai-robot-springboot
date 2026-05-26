package com.gchong3068.ai.robot.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: gchong3068
 * @Date: 2026/5/26 10:18
 * @Version: v1.0.0
 * @Description: 日期 Tool
 **/
@Slf4j
public class DateTimeTools {

    @Tool(description = "获取当前时间")
    String getCurrenDateTime(){
        return LocalDateTime.now().toString();
    }

}
