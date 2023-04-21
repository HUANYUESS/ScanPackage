package com.sugoos.scanpackage.utils;

import com.sugoos.scanpackage.dto.ProjectPathDto;
import com.sugoos.scanpackage.strategy.FileContext;
import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class ScanContentUtils {

    private static final Map<String, String> rootPathPatterns = new HashMap<>();
    private static final Map<String, String> interfacePatterns = new HashMap<>();

    private static final Map<String, String> githubPathPatterns = new HashMap<>();



    static {
        rootPathPatterns.put("patternGivenDefault", "@RequestMapping\\s*\\(\\s*\"\\s*([^\"\\s]*)\\s*\"\\s*\\)[\\s\\S]*?public\\s+class\\s+");
        rootPathPatterns.put("patternGivenValue", "@RequestMapping\\s*\\(\\s*value\\s*=\\s*\"\\s*([^\"\\s]*)\\s*\"\\s*\\)[\\s\\S]*?public\\s+class\\s+");
        rootPathPatterns.put("patternGivenPath", "@RequestMapping\\s*\\(\\s*path\\s*=\\s*\"\\s*([^\"\\s]*)\\s*\"\\s*\\)[\\s\\S]*?public\\s+class\\s+");
        rootPathPatterns.put("patternGivenValueList", "@RequestMapping\\s*\\(\\s*value\\s*=\\s*\\{\\s*(\"\\s*[^\"\\s]*\\s*\"\\s*(,\\s*\"\\s*[^\"\\s]*\\s*\")*)?\\s*\\}\\s*\\)[\\s\\S]*?public\\s+class\\s+");

        interfacePatterns.put("getMappingGivenDefault", "@GetMapping\\s*\\(\\s*\"(\\S+)\"\\s*\\)");
        interfacePatterns.put("postMappingGivenDefault", "@PostMapping\\s*\\(\\s*\"(\\S+)\"\\s*\\)");
        interfacePatterns.put("deleteMappingGivenDefault", "@DeleteMapping\\s*\\(\\s*\"(\\S+)\"\\s*\\)");
        interfacePatterns.put("putMappingGivenDefault", "@PutMapping\\s*\\(\\s*\"(\\S+)\"\\s*\\)");
        interfacePatterns.put("patchMappingGivenDefault", "@PatchMapping\\s*\\(\\s*\"(\\S+)\"\\s*\\)");

        interfacePatterns.put("getMappingGivenValue", "@GetMapping\\s*\\(\\s*value\\s*=\\s*\"(\\S+)\"\\s*\\)");
        interfacePatterns.put("postMappingGivenValue", "@PostMapping\\s*\\(\\s*value\\s*=\\s*\"(\\S+)\"\\s*\\)");
        interfacePatterns.put("deleteMappingGivenValue", "@DeleteMapping\\s*\\(\\s*value\\s*=\\s*\"(\\S+)\"\\s*\\)");
        interfacePatterns.put("putMappingGivenValue", "@PutMapping\\s*\\(\\s*value\\s*=\\s*\"(\\S+)\"\\s*\\)");
        interfacePatterns.put("patchMappingGivenValue", "@PatchMapping\\s*\\(\\s*value\\s*=\\s*\"(\\S+)\"\\s*\\)");
        interfacePatterns.put("requestMappingGivenValAndMethod", "@RequestMapping\\(value\\s*=\\s*\"(\\S+)\",\\s*method\\s*=\\s*RequestMethod.(\\S+)\\)");

        githubPathPatterns.put("defaultMapping", "https://github.com/(\\w+[-\\w]*)/(\\w+[-\\w]*)/tree/\\w+[-\\w]/(.*)");
        githubPathPatterns.put("apiMapping", "https://api.github.com/repos/(\\w+[-\\w])*/(\\w+[-\\w])*/contents/(.*)");

    }


    public static List<String> getRootPath(String content) {
        Matcher matcher = null;
        List<String> list = new ArrayList<>();
        for (String patternName : rootPathPatterns.keySet()) {
            String pattern = rootPathPatterns.get(patternName);
            matcher = Pattern.compile(pattern).matcher(content);
            if (matcher.find()) {
                String val = matcher.group(1);
                String[] split = val.split(",");
                for (String s : split) {
                    list.add(s.trim());
                }
                return list;
            }
        }
        return null;
    }

    public static List<String> getAllMapping(String content, String root) {
        Matcher matcher = null;
        List<String> list = new ArrayList<>();
        for (String patternName : interfacePatterns.keySet()) {
            String pattern = interfacePatterns.get(patternName);
            matcher = Pattern.compile(pattern).matcher(content);
            while (matcher.find()) {
                String method = getMethodName(patternName);
                if (method.equals("REQUEST")) method = matcher.group(2);
                String path = matcher.group(1);
                list.add(method + " " + getFullPath(root, path, "/"));
            }
        }
        return list;
    }

    private static String getMethodName(String patternName) {
        if (patternName.contains("getMapping")) {
            return "GET";
        } else if (patternName.contains("postMapping")) {
            return "POST";
        } else if (patternName.contains("deleteMapping")) {
            return "DELETE";
        } else if (patternName.contains("putMapping")) {
            return "PUT";
        } else if (patternName.contains("patchMapping")) {
            return "PATCH";
        } else {
            return "REQUEST";
        }
    }

    public static String getFullPath(String root, String path, String segment) {
        if (root.endsWith(segment) && path.startsWith(segment)) {
            return root.substring(0, root.length() - 1) + path;
        } else if (root.endsWith(segment) && !path.startsWith(segment)) {
            return root + path;
        } else if (!root.endsWith(segment) && path.startsWith(segment)) {
            return root + path;
        } else if (!root.endsWith(segment) && !path.startsWith(segment)) {
            return root + segment + path;
        }
        return "";
    }

    public static boolean checkFileStatus(ProjectPathDto dto) {
        if (dto.getType().equals(FileContext.LOCAL_FILE_SYSTEM)) {
            File file = null;
            if (dto.getFileName() != null) {
                file = new File(getFullPath(dto.getPath(), dto.getFileName(), "\\"));
            } else {
                file = new File(dto.getPath());
            }
            return file.exists();
        }
        return true;
    }

    private static final String GITHUB_API_URL_TEMPLATE = "https://api.github.com/repos/%s/%s/contents/%s";

    public static String getGithubAPIPath(String url) {
        Matcher matcher = null;
        for (String patternName : githubPathPatterns.keySet()) {
            String pattern = githubPathPatterns.get(patternName);
            matcher = Pattern.compile(pattern).matcher(url);
            if (matcher.matches()) {
                String owner = matcher.group(1);
                String repo = matcher.group(2);
                String path = matcher.group(3);
                return String.format(GITHUB_API_URL_TEMPLATE, owner, repo, path);
            }
        }
        return null;
    }

    public static void convertGithubToAPIURL(ProjectPathDto dto) {
        dto.setPath(getGithubAPIPath(dto.getPath()));
    }
}
