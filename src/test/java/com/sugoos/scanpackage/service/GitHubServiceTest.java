package com.sugoos.scanpackage.service;

import com.sugoos.scanpackage.domain.GitHubFile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class GitHubServiceTest {

    @Autowired
    private GitHubService gitHubService;

    @Test
    void testRequestApi() {
//        List<GitHubFile> files = gitHubService.requestGitHubFiles("https://api.github.com/repos/HUANYUESS/ScanPackage/contents/src/main/java/com/sugoos/scanpackage");
//        for (GitHubFile file : files) {
//            if (file.getType().equals("file")) {
//                System.out.println(gitHubService.requestGitHubFileContent(file.getDownload_url()));
//            }
//        }
    }


}