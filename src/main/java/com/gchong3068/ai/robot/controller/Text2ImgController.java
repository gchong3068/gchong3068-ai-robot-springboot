package com.gchong3068.ai.robot.controller;

import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesis;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisParam;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author: gchong3068
 * @Date: 2026/5/26 9:21
 * @Version: v1.0.0
 * @Description: 文生图
 **/
@RestController
@RequestMapping("/v10/ai")
@Slf4j
public class Text2ImgController {

    private String  apiKey = "sk-4e6a86bdc5ae420e944dae86c9d4675b";


    @GetMapping("/text2img")
    public String text2Image(@RequestParam(value = "prompt") String prompt) {

        ImageSynthesisParam param = ImageSynthesisParam.builder()
                .apiKey(apiKey)
                .model("wanx2.1-t2i-plus")
                .prompt(prompt)
                .n(1)
                .size("1024*1024")
                .build();

        ImageSynthesis imageSynthesis = new ImageSynthesis();
        ImageSynthesisResult result = null;

        try{
            result = imageSynthesis.call(param);

        }catch (ApiException | NoApiKeyException e){
            log.error("",e);
        }
        return JsonUtils.toJson(result);
    }

}
