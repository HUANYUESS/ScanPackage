package com.sugoos.scanpackage.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import com.sugoos.scanpackage.factory.RestTemplateFactory;
import com.sugoos.scanpackage.dto.GitHubFileDto;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;

public class GitHubFileStrategy implements FileStrategy {

    private static final RestTemplate restTemplate = RestTemplateFactory.getInstance();

    @Override
    public List<?> listFiles(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<JsonNode[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, JsonNode[].class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JsonNode[] jsonArray = responseEntity.getBody();
            List<GitHubFileDto> files = new ArrayList<>();
            // 遍历JSON数组
            if (jsonArray == null) return files;
            for (JsonNode jsonNode : jsonArray) {
                GitHubFileDto gitHubFileDto = new GitHubFileDto();
                gitHubFileDto.setName(jsonNode.get("name").asText());
                gitHubFileDto.setPath(jsonNode.get("path").asText());
                gitHubFileDto.setDownload_url(jsonNode.get("download_url").asText());
                gitHubFileDto.setType(jsonNode.get("type").asText());
                files.add(gitHubFileDto);
            }
            return files;
        } else {
            System.out.println("ERROR");
            return null;
        }
    }

    @Override
    public String readFile(String url) {
        return restTemplate.getForObject(url, String.class);
    }
}
