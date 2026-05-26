package com.gchong3068.ai.robot.controller;

import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesisParam;
import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @Author: gchong3068
 * @Date: 2026/5/26 9:41
 * @Version: v1.0.0
 * @Description: 文生音频
 **/
@RestController
@RequestMapping("/v11/ai")
@Slf4j
public class Text2AudioController {

    private String  apiKey = "sk-4e6a86bdc5ae420e944dae86c9d4675b";

    @GetMapping("/text2audio")
    public String text2audio(@RequestParam(value = "prompt") String prompt) {

        SpeechSynthesisParam param = SpeechSynthesisParam.builder()
                .apiKey(apiKey)
                .model("cosyvoice-v2")
                .voice("longanran") // 音色
                .build();


        // 同步调用语音合成大模型，并获字节流
        SpeechSynthesizer synthesizer = new SpeechSynthesizer(param, null);
        ByteBuffer audio = synthesizer.call(prompt);


        // 音频文件存储路径
        String path = "E:\\result-audio.mp3";
        File file = new File(path);


        // 存储字节流
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(audio.array());
        } catch (IOException e) {
            log.error("", e);
        }

        return "success";
    }


}
