package com.gchong3068.ai.robot;

import com.gchong3068.ai.robot.domain.dos.ChatDO;
import com.gchong3068.ai.robot.domain.mapper.ChatMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @Author: gchong3068
 * @Date: 2026/6/16 16:54
 * @Version: v1.0.0
 * @Description: TODO
 **/
@SpringBootTest
class MybatisPlusTests {

    @Resource
    private ChatMapper chatMapper;

    @Test
    void testInsert() {
        chatMapper.insert(ChatDO.builder()
                .uuid(UUID.randomUUID().toString())
                .summary("新对话")
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build());
    }


}
