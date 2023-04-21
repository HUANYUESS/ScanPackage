package com.sugoos.scanpackage.strategy;

import com.alibaba.fastjson2.JSON;
import com.sugoos.scanpackage.api.Http;
import com.sugoos.scanpackage.dto.GitFileDto;

import java.util.List;

import static com.sugoos.scanpackage.utils.ScanContentUtils.convertGitToAPIURL;

public class GitFileStrategy implements FileStrategy {

    @Override
    public List<?> listFiles(String url) {
        url = convertGitToAPIURL(url);
        String json = Http.get(url);
        return JSON.parseArray(json, GitFileDto.class);
    }

    @Override
    public String readFile(String url) {
        return Http.get(url);
    }
}
