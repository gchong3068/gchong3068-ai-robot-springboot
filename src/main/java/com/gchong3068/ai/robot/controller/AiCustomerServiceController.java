package com.gchong3068.ai.robot.controller;

import com.gchong3068.ai.robot.aspect.ApiOperationLog;
import com.gchong3068.ai.robot.model.vo.customerService.DeleteMarkdownFileReqVO;
import com.gchong3068.ai.robot.service.CustomerService;
import com.gchong3068.ai.robot.utils.Response;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: gchong3068
 * @Date: 2026/6/22 14:20
 * @Version: v1.0.0
 * @Description: Ai 客服
 **/
@RestController
@RequestMapping("/customer-service")
@Slf4j
public class AiCustomerServiceController {

    @Resource
    private CustomerService customerService;

    /**
     * 问答 MD 文件上传
     * @param file
     * @return
     */
    @PostMapping(value = "/md/upload" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperationLog(description = "添加 Markdown 问答文件")
    public Response<?> uploadMarkdownFile(@RequestPart(value = "file", required = false) MultipartFile file) {
        return customerService.uploadMarkdownFile(file);
    }


    @PostMapping("/md/delete")
    @ApiOperationLog(description = "删除 Markdown 问答文件")
    public Response<?> deleteMarkdownFile(@RequestBody @Validated DeleteMarkdownFileReqVO deleteMarkdownFileReqVO) {
        return customerService.deleteMarkdownFile(deleteMarkdownFileReqVO);
    }

}
