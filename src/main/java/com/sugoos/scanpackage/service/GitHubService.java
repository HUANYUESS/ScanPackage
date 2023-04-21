package com.sugoos.scanpackage.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sugoos.scanpackage.domain.GitHubFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class GitHubService {
    private final RestTemplate restTemplate;

    @Autowired
    public GitHubService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<GitHubFile> requestGitHubFiles(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<JsonNode[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, JsonNode[].class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JsonNode[] jsonArray = responseEntity.getBody();
            List<GitHubFile> files = new ArrayList<>();
            // 遍历JSON数组
            if (jsonArray == null) return files;
            for (JsonNode jsonNode : jsonArray) {
                GitHubFile gitHubFile = new GitHubFile();
                gitHubFile.setName(jsonNode.get("name").asText());
                gitHubFile.setPath(jsonNode.get("path").asText());
                gitHubFile.setDownload_url(jsonNode.get("download_url").asText());
                gitHubFile.setType(jsonNode.get("type").asText());
                files.add(gitHubFile);
            }
            return files;
        } else {
            System.out.println("ERROR");
            return null;
        }
    }

    public String requestGitHubFileContent(String url) {
        return restTemplate.getForObject(url, String.class);
    }


}
