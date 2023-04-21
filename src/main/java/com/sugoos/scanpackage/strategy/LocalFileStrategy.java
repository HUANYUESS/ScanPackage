package com.sugoos.scanpackage.strategy;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class LocalFileStrategy implements FileStrategy {

    @Override
    public List<?> listFiles(String dirPath) {
        File dir = new File(dirPath);
        return (List<?>) FileUtils.listFiles(dir, new String[] {"java"}, false);
    }

    @Override
    public String readFile(String filePath) {
        File file = new File(filePath);
        try {
            return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
