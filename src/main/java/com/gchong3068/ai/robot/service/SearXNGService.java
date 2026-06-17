package com.gchong3068.ai.robot.service;

import com.gchong3068.ai.robot.model.dto.SearchResultDTO;

import java.util.List;

/**
 * @Author: gchong3068
 * @Date: 2026/6/17 9:30
 * @Version: v1.0.0
 * @Description: TODO
 **/
public interface SearXNGService {


    List<SearchResultDTO> search(String query);
}
