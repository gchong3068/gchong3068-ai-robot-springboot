package com.gchong3068.ai.robot.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: gchong3068
 * @Date: 2026/6/16 16:52
 * @Version: v1.0.0
 * @Description: TODO
 **/
@Configuration
@MapperScan("com.gchong3068.ai.robot.domain.mapper")
public class MybatisPlusConfig {
}
