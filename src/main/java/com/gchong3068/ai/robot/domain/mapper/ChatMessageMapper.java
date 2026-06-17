package com.gchong3068.ai.robot.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gchong3068.ai.robot.domain.dos.ChatMessageDO;

/*
 * @Author: gchong3068
 * @Date: 2026/6/16 18:45
 * @Version: v1.0.0
 * @Description: TODO
 **/
public interface ChatMessageMapper extends BaseMapper<ChatMessageDO> {

    /**
     * 分页查询
     * @param current
     * @param size
     * @param chatId
     * @return
     */
    default Page<ChatMessageDO> selectPageList(Long current, Long size , String chatId){

        //分页对象
        Page<ChatMessageDO> page = new Page<>(current, size);

        // 构建查询条件
        LambdaQueryWrapper<ChatMessageDO> wrapper = Wrappers.<ChatMessageDO>lambdaQuery()
                .eq(ChatMessageDO::getChatUuid, chatId) // 对话 ID
                .orderByDesc(ChatMessageDO::getCreateTime); // 按创建时间倒序

        return selectPage(page, wrapper);
    }

}
