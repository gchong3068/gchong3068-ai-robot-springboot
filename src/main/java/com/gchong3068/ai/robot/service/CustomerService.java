package com.gchong3068.ai.robot.service;

import com.gchong3068.ai.robot.model.vo.customerService.DeleteMarkdownFileReqVO;
import com.gchong3068.ai.robot.utils.Response;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: gchong3068
 * @Date: 2026/6/22 14:08
 * @Version: v1.0.0
 * @Description: AI 客服
 **/
public interface CustomerService {

    /**
     * 上传 Markdown 问答文件
     * @param file
     * @return
     */
    Response<?> uploadMarkdownFile(MultipartFile file);

    /**
     * 删除Markdown 问答文件
     * @param deleteMarkdownFileReqVO
     * @return
     */
    Response<?> deleteMarkdownFile(DeleteMarkdownFileReqVO deleteMarkdownFileReqVO);


}
