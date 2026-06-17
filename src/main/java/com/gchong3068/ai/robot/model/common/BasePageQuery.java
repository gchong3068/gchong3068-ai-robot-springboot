package com.gchong3068.ai.robot.model.common;

import lombok.Data;

/**
 * @Author: gchong3068
 * @Date: 2026/6/17 9:51
 * @Version: v1.0.0
 * @Description: TODO
 **/
@Data
public class BasePageQuery {

    private Long current = 1L;


    private Long size = 10L;


}
