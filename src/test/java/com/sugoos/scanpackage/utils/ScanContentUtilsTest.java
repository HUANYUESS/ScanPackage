package com.sugoos.scanpackage.utils;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.List;

import static com.sugoos.scanpackage.utils.ScanContentUtils.*;

class ScanContentUtilsTest {

//  String content = FileUtils.readFileToString(new File("F:\\TEST\\DeptController.java"), StandardCharsets.UTF_8);

    @Test
    void testGetRootPathWhenGivenOnePath() {
        String content = "@RequestMapping(path = \"/system/dept\") public class DeptController {";
        testGetRootPath(content, "/system/dept");
    }

    @Test
    void testGetRootPathWhenGivenOneValue() {
        String content = "@RequestMapping(value = \"/system/dept\") public class DeptController {";
        testGetRootPath(content, "/system/dept");
    }

    @Test
    void testGetRootPathWhenGivenDefaultValue() {
        String content = "@RequestMapping(\"/system/dept\") public class DeptController {";
        testGetRootPath(content, "/system/dept");
    }

    @Test
    void testGetRootPathWhenGivenManyValue() {
        String content = "@RequestMapping(value = {\"/system/dept\",  \"/system/dept1\",\"/system/dept3\"}) public class DeptController {";
        testGetRootPath(content, "[\"/system/dept\", \"/system/dept1\", \"/system/dept3\"]");
    }

    void testGetRootPath(String content, String expect) {
        List<String> rootPath = getRootPath(content);
        Assert.notNull(rootPath, "ERROR: GIVEN CONTENT MISSING IMPORTANT MSG!");
        Assert.hasText(rootPath.toString(), expect);
    }

    @Test
    void testGetAllMapping() {
        String content = "@PostMapping(\"create\") @PutMapping(\"update\")";
        List<String> allMapping = getAllMapping(content, "/system/dept");
        Assert.hasText(allMapping.toString(), "[PUT /system/dept/update, POST /system/dept/create]");
    }

    @Test
    void testGetFullPath() {
        Assert.hasText(getFullPath("/user/", "/hello", "/"), "/user/hello");
        Assert.hasText(getFullPath("/user/", "hello", "/"), "/user/hello");
        Assert.hasText(getFullPath("/user", "/hello", "/"), "/user/hello");
        Assert.hasText(getFullPath("/user", "hello", "/"), "/user/hello");

        Assert.hasText(getFullPath("F:\\TEST\\", "PostController.java", "\\"), "F:\\TEST\\PostController.java");
        Assert.hasText(getFullPath("F:\\TEST", "PostController.java", "\\"), "F:\\TEST\\PostController.java");

    }

    @Test
    void testRequestMapping() {
         String content = "@PostMapping(\"create\") @RequestMapping(value = \"/example0\", method = RequestMethod.POST)";
        List<String> allMapping = getAllMapping(content, "/system/dept");
        Assert.hasText(allMapping.toString(), "[POST /system/dept/create, POST /system/dept/example0]");
    }

}