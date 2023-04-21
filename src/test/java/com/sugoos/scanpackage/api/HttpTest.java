package com.sugoos.scanpackage.api;

import com.alibaba.fastjson2.JSON;
import com.sugoos.scanpackage.dto.GitFileDto;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.List;


class HttpTest {

    @Test
    void testGet() {
        String url = "https://gitee.com/api/v5/repos/huanyuess/qiwen-file/contents/src/main/java/com/qiwenshare/file/controller";
        String json = Http.get(url);
        List<GitFileDto> files = JSON.parseArray(json, GitFileDto.class);
        boolean flag = files.toString().contains("name=CommonFileController.java");
        Assert.isTrue(flag, "Get方法请求疑似出现错误!");
    }
}