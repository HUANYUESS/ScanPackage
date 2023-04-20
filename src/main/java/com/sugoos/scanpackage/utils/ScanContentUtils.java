package com.sugoos.scanpackage.utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class ScanContentUtils {

    private static final Map<String, String> rootPathPatterns = new HashMap<>();
    private static final Map<String, String> interfacePatterns = new HashMap<>();

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
        interfacePatterns.put("requestMappingGivenValAndMethod", "@RequestMapping\\(value\\s*=\\s*\"(\\S+)\",\\s*method\\s*=\\s*RequestMethod.(\\S+)\\)");

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
        switch (patternName) {
            case "getMappingGivenDefault":
                return "GET";
            case "postMappingGivenDefault":
                return "POST";
            case "deleteMappingGivenDefault":
                return "DELETE";
            case "putMappingGivenDefault":
                return "PUT";
            case "patchMappingGivenDefault":
                return "PATCH";
            default:
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



}
