package com.sugoos.scanpackage.service;

import com.sugoos.scanpackage.domain.GitHubFile;
import com.sugoos.scanpackage.dto.ProjectPathDto;
import com.sugoos.scanpackage.dto.RestResult;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static com.sugoos.scanpackage.utils.ScanContentUtils.*;

@Service
public class ScanService {

    private final GitHubService gitHubService;

    @Autowired
    public ScanService (GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    public RestResult<?> findSpecifiedDirInterface(ProjectPathDto dto) {
        File dir = new File(dto.getPath());
        if (!dir.exists()) return RestResult.fail("该目录不存在, 请重新检查后再试!");
        Collection<File> files = FileUtils.listFiles(dir, new String[] {"java"}, false);
        List<String> list = new ArrayList<>();
        for (File file : files) {
            addMappingInterface2List(file, list);
        }
        return RestResult.ok(list);
    }

    public RestResult<?> findSpecifiedFileInterface(ProjectPathDto dto) {
        File file = new File(getFullPath(dto.getPath(), dto.getFileName(), "\\"));
        if (!file.exists()) return RestResult.fail("该文件不存在, 请重新检查后再试!");
        List<String> list = new ArrayList<>();
        addMappingInterface2List(file, list);
        return RestResult.ok(list);
    }

    private void addMappingInterface2List(File file, List<String> list) {
        try {
            String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            List<String> rootPath = getRootPath(content);
            if (rootPath == null) return;
            for (String path : rootPath) {
                list.addAll(getAllMapping(content, path));
            }
        } catch (IOException e) {
            System.out.println("readFileToString Error:");
            e.printStackTrace();
        }
    }


    public RestResult<?> findSpecifiedGithubDirInterface(ProjectPathDto dto) {
        List<GitHubFile> files = gitHubService.requestGitHubFiles(dto.getPath());
        List<String> list = new ArrayList<>();
        for (GitHubFile file : files) {
            addMappingInterface2List(file, list);
        }
        return RestResult.ok(list);
    }

    private void addMappingInterface2List(GitHubFile file, List<String> list) {
        String content = gitHubService.requestGitHubFileContent(file.getDownload_url());
        List<String> rootPath = getRootPath(content);
        if (rootPath == null) return;
        for (String path : rootPath) {
            list.addAll(getAllMapping(content, path));
        }
    }

    public RestResult<?> findSpecifiedGithubFileInterface(ProjectPathDto dto) {

        return null;
    }
}
