package com.sugoos.scanpackage.utils;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

import static com.sugoos.scanpackage.utils.ScanContentUtils.*;

class ScanContentUtilsTest {

//  String content = FileUtils.readFileToString(new File("F:\\TEST\\DeptController.java"), StandardCharsets.UTF_8);

    @Test
    void testGetRootPathWhenGivenOnePath() {
        String content = "@RequestMapping(path = \"/system/dept\") public class DeptController {";
        testGetRootPath(content, "[/system/dept]");
    }

    @Test
    void testGetRootPathWhenGivenOneValue() {
        String content = "@RequestMapping(value = \"/system/dept\") public class DeptController {";
        testGetRootPath(content, "[/system/dept]");
    }

    @Test
    void testGetRootPathWhenGivenDefaultValue() {
        String content = "@RequestMapping(\"/system/dept\") public class DeptController {";
        testGetRootPath(content, "[/system/dept]");
    }

    @Test
    void testGetRootPathWhenGivenManyValue() {
        String content = "@RequestMapping(value = {\"/system/dept\",  \"/system/dept1\",\"/system/dept3\"}) public class DeptController {";
        testGetRootPath(content, "[\"/system/dept\", \"/system/dept1\", \"/system/dept3\"]");
    }

    void testGetRootPath(String content, String expect) {
        List<String> rootPath = getRootPath(content);
        Assert.notNull(rootPath, "ERROR: GIVEN CONTENT MISSING IMPORTANT MSG!");
        assertTextEqual(rootPath.toString(), expect, "testGetRootPath 出错");
    }

    @Test
    void testGetAllMapping() {
        String content = "@PostMapping(\"create\") @PutMapping(\"update\")";
        List<String> allMapping = getAllMapping(content, "/system/dept");
        assertTextEqual(allMapping.toString(), "[PUT /system/dept/update, POST /system/dept/create]", "testGetAllMapping 出错");
    }

    @Test
    void testGetFullPath() {
        String exp = "/user/hello";
        assertTextEqual(getFullPath("/user/", "/hello", "/"), exp, "testGetFullPath 1 出错");
        assertTextEqual(getFullPath("/user/", "hello", "/"), exp, "testGetFullPath 2 出错");
        assertTextEqual(getFullPath("/user", "/hello", "/"), exp, "testGetFullPath 3 出错");
        assertTextEqual(getFullPath("/user", "hello", "/"), exp, "testGetFullPath 4 出错");

        exp = "PostController.java";
        assertTextEqual(getFullPath("F:\\TEST\\", exp, "\\"), "F:\\TEST\\PostController.java", "testGetFullPath 5 出错");
        assertTextEqual(getFullPath("F:\\TEST", exp, "\\"), "F:\\TEST\\PostController.java", "testGetFullPath 6 出错");
    }

    @Test
    void testRequestMapping() {
         String content = "@PostMapping(\"create\") @RequestMapping(value = \"/example0\", method = RequestMethod.POST)";
        List<String> allMapping = getAllMapping(content, "/system/dept");
        assertTextEqual(allMapping.toString(), "[POST /system/dept/create, POST /system/dept/example0]", "testRequestMapping 出错");
    }


    @Test
    void testGetGithubAPIPathGivenDefault() {
        String content = "https://github.com/qiwenshare/qiwen-file/tree/master/src/main/java/com/qiwenshare/file/controller";
        String mapping = getGitAPIPath(content);
        assertTextEqual(mapping, "https://api.github.com/repos/qiwenshare/qiwen-file/contents/src/main/java/com/qiwenshare/file/controller", "testGetGithubAPIPathGivenDefault 出错");
    }

    @Test
    void testGetGithubAPIPathGivenAPI() {
        String content = "https://api.github.com/repos/qiwenshare/qiwen-file/contents/src/main/java/com/qiwenshare/file/controller";
        String mapping = getGitAPIPath(content);
        assertTextEqual(mapping, "https://api.github.com/repos/qiwenshare/qiwen-file/contents/src/main/java/com/qiwenshare/file/controller", "testGetGithubAPIPathGivenAPI 出错");

    }

    @Test
    void testGetGiteeAPIPathGivenDefault() {
        String content = "https://gitee.com/huanyuess/qiwen-file/tree/master/src/main/java/com/qiwenshare/file/controller";
        String mapping = getGitAPIPath(content);
        assertTextEqual(mapping, "https://gitee.com/api/v5/repos/huanyuess/qiwen-file/contents/src/main/java/com/qiwenshare/file/controller", "testGetGiteeAPIPathGivenDefault 出错");
    }

    void assertTextEqual(String out, String exp, String msg) {
        boolean flag = Objects.equals(out, exp);
        Assert.isTrue(flag, msg);
    }


}