package com.sugoos.scanpackage.domain;

import lombok.Data;

@Data
public class GitHubFile {

    private String name;
    private String path;
    private String size;
    private String sha;
    private String url;
    private String download_url;
    private String type;

}
