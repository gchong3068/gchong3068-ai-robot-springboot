package com.gchong3068.ai.robot.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author: gchong3068
 * @Date: 2026/6/16 18:18
 * @Version: v1.0.0
 * @Description: 字符串工具类
 **/
public class StringUtil {


    /**
     * 截取用户问题的前面文字作为摘要
     * @param message
     * @param maxLength
     * @return
     */
    public static String truncate(String message, int maxLength) {
        // 判空
        if (StringUtils.isBlank(message)) {
            return "";
        }

        String trimmed = message.trim();

        // 如果文本长度小于等于最大长度，直接返回
        if (trimmed.length() <= maxLength) {
            return trimmed;
        }

        // 截取指定长度
        return trimmed.substring(0, maxLength);
    }






}
