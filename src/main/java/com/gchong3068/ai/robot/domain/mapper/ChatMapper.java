package com.gchong3068.ai.robot.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gchong3068.ai.robot.domain.dos.ChatDO;

/**
 * @Author: gchong3068
 * @Date: 2026/6/16 16:53
 * @Version: v1.0.0
 * @Description: TODO
 **/
public interface ChatMapper extends BaseMapper<ChatDO> {

    /**
     * 分页查询
     * @param current
     * @param size
     * @return
     */
    default Page<ChatDO> selectPageList(Long current ,Long size){

        Page<ChatDO> page = new Page<>(current, size);

        LambdaQueryWrapper<ChatDO> wrapper = Wrappers.<ChatDO>lambdaQuery()
                .orderByDesc(ChatDO::getUpdateTime); // 按更新时间倒序

        return selectPage(page,wrapper);
    }



}
