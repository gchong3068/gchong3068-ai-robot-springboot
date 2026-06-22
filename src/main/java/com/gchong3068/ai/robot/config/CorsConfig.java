package com.gchong3068.ai.robot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description: 跨域配置
 * @Author: gchong3068
 * @Date: 2025/8/16 20:33
 * @Version: v1.0.0
 **/
@Configuration
public class CorsConfig implements WebMvcConfigurer  {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //匹配所有路径
                .allowedOriginPatterns("*") //允许所有域名
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") //允许所有方法
                .allowedHeaders("*") //允许所有请求头
                .allowCredentials(true)  //允许携带cookie
                .maxAge(3600); //预检请求的有效期
    }
}
