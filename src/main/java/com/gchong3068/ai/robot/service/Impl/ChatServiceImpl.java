package com.gchong3068.ai.robot.service.Impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gchong3068.ai.robot.domain.dos.ChatDO;
import com.gchong3068.ai.robot.domain.dos.ChatMessageDO;
import com.gchong3068.ai.robot.domain.mapper.ChatMapper;
import com.gchong3068.ai.robot.domain.mapper.ChatMessageMapper;
import com.gchong3068.ai.robot.enums.ResponseCodeEnum;
import com.gchong3068.ai.robot.exception.BizException;
import com.gchong3068.ai.robot.model.vo.chat.*;
import com.gchong3068.ai.robot.service.ChatService;
import com.gchong3068.ai.robot.utils.PageResponse;
import com.gchong3068.ai.robot.utils.Response;
import com.gchong3068.ai.robot.utils.StringUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author: gchong3068
 * @Date: 2026/6/16 18:21
 * @Version: v1.0.0
 * @Description: TODO
 **/
@Service
@Slf4j
public class ChatServiceImpl implements ChatService {

    @Resource
    private ChatMapper chatMapper;
    @Resource
    private ChatMessageMapper chatMessageMapper;

    /**
     * 新建对话
     * @param newChatReqVO
     * @return
     */
    @Override
    public Response<NewChatRspVO> newChat(NewChatReqVO newChatReqVO) {

        //用户发送的信息
        String message = newChatReqVO.getMessage();
        //生成对话UUID
        String uuid = UUID.randomUUID().toString();

        // 截取用户发送的消息，作为对话摘要
        String summary = StringUtil.truncate(message, 20);

        //储存对话到数据库中
        chatMapper.insert(ChatDO.builder()
                        .summary(summary)
                        .uuid(uuid)
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                .build());

        //返回摘要
        return Response.success(NewChatRspVO.builder()
                        .uuid(uuid)
                        .summary(summary)
                .build());

    }


    /**
     * 查询历史信息
     * @param findChatHistoryMessagePageListReqVO
     * @return
     */
    @Override
    public PageResponse<FindChatHistoryMessagePageListRspVO> findChatHistoryMessagePageList(FindChatHistoryMessagePageListReqVO findChatHistoryMessagePageListReqVO) {
        // 获取当前页、以及每页需要展示的数据数量
        Long current = findChatHistoryMessagePageListReqVO.getCurrent();
        Long size = findChatHistoryMessagePageListReqVO.getSize();
        String chatId = findChatHistoryMessagePageListReqVO.getChatId();

        // 执行分页查询
        Page<ChatMessageDO> chatMessageDOPage = chatMessageMapper.selectPageList(current, size, chatId);

        List<ChatMessageDO> chatMessageDOS = chatMessageDOPage.getRecords();
        // DO 转 VO
        List<FindChatHistoryMessagePageListRspVO> vos = null;
        if (CollUtil.isNotEmpty(chatMessageDOS)) {
            vos = chatMessageDOS.stream()
                    .map(chatMessageDO -> FindChatHistoryMessagePageListRspVO.builder() // 构建返参 VO 实体类
                            .id(chatMessageDO.getId())
                            .chatId(chatMessageDO.getChatUuid())
                            .content(chatMessageDO.getContent())
                            .role(chatMessageDO.getRole())
                            .createTime(chatMessageDO.getCreateTime())
                            .build())
                    // 升序排序
                    .sorted(Comparator.comparing(FindChatHistoryMessagePageListRspVO::getCreateTime))
                    .collect(Collectors.toList());
        }

        return PageResponse.success(chatMessageDOPage, vos);
    }

    /**
     * 查询历史对话
     * @param findChatHistoryPageListReqVO
     * @return
     */
    @Override
    public PageResponse<FindChatHistoryPageListRspVO> findChatHistoryPageList(FindChatHistoryPageListReqVO findChatHistoryPageListReqVO) {

        //获取当前页
        Long current = findChatHistoryPageListReqVO.getCurrent();
        Long size = findChatHistoryPageListReqVO.getSize();

        Page<ChatDO> chatDOPage = chatMapper.selectPageList(current, size);

        List<ChatDO> chatDOS = chatDOPage.getRecords();

        // DO 转 VO
        List<FindChatHistoryPageListRspVO> vos = null;
        if (CollUtil.isNotEmpty(chatDOS)) {
            vos = chatDOS.stream()
                    .map(chatDO -> FindChatHistoryPageListRspVO.builder() // 构建返参 VO
                            .id(chatDO.getId())
                            .uuid(chatDO.getUuid())
                            .summary(chatDO.getSummary())
                            .updateTime(chatDO.getUpdateTime())
                            .build())
                    .collect(Collectors.toList());
        }

        return PageResponse.success(chatDOPage, vos);
    }


    /**
     * 重命名对话摘要
     * @param renameChatReqVO
     * @return
     */
    @Override
    public Response<?> renameChatSummary(RenameChatReqVO renameChatReqVO) {

        Long chatId = renameChatReqVO.getId();

        String summary = renameChatReqVO.getSummary();

        chatMapper.updateById(ChatDO.builder()
                        .id(chatId)
                        .summary(summary)
                .build());

        return Response.success();
    }


    /**
     * 删除对话
     * @param deleteChatReqVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<?> deleteChat(DeleteChatReqVO deleteChatReqVO) {

        // 对话 UUID
        String uuid = deleteChatReqVO.getUuid();

        // 删除对话
        int count = chatMapper.delete(Wrappers.<ChatDO>lambdaQuery()
                .eq(ChatDO::getUuid, uuid));

        // 如果删除操作影响的行数为 0，说明想要删除的对话不存在
        if (count == 0) {
            throw new BizException(ResponseCodeEnum.CHAT_NOT_EXISTED);
        }

        // 批量删除对话下的所有消息
        chatMessageMapper.delete(Wrappers.<ChatMessageDO>lambdaQuery()
                .eq(ChatMessageDO::getChatUuid, uuid));

        return Response.success();
    }
}
