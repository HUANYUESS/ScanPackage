package com.sugoos.scanpackage.strategy;

import com.alibaba.fastjson2.JSON;
import com.sugoos.scanpackage.api.Http;
import com.sugoos.scanpackage.dto.GitFileDto;

import java.util.ArrayList;
import java.util.List;

import static com.sugoos.scanpackage.utils.ScanContentUtils.convertGitToAPIURL;
import static com.sugoos.scanpackage.utils.ScanContentUtils.getFullPath;

public class GitFileStrategy implements FileStrategy {

    @Override
    public List<?> listTopLevelFiles(String url) {
        url = convertGitToAPIURL(url);
        return JSON.parseArray(Http.get(url), GitFileDto.class);
    }

    @Override
    public List<?> listRecursiveFiles(String url) {
        url = convertGitToAPIURL(url);
        List<? super Object> list = new ArrayList<>();
        List<GitFileDto> subFiles = JSON.parseArray(Http.get(url), GitFileDto.class);
        for (GitFileDto file : subFiles) {
            if (file.getType().equals("file")) {
                list.add(file);
            } else {
                String subFileUrl = getFullPath(url, file.getName(), "/");
                list.addAll(listRecursiveFiles(subFileUrl));
            }
        }
        return list;
    }

    @Override
    public String readFile(String url) {
        return Http.get(url);
    }
}
