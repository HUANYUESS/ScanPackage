package com.sugoos.scanpackage.strategy;

import com.sugoos.scanpackage.dto.GitFileDto;
import com.sugoos.scanpackage.dto.ProjectPathDto;

import java.io.File;
import java.util.List;

import static com.sugoos.scanpackage.utils.ScanContentUtils.getFullPath;

public class FileContext {

    public final static String LOCAL_FILE_SYSTEM = "LOCAL";
    public final static String GIT_FILE_SYSTEM = "GIT";

    private final FileStrategy fileStrategy;

    public FileContext(FileStrategy fileStrategy) {
        this.fileStrategy = fileStrategy;
    }

    public FileContext(ProjectPathDto dto) {
        if (dto.getType().equals(LOCAL_FILE_SYSTEM)) {
            this.fileStrategy = new LocalFileStrategy();
        } else if (dto.getType().equals(GIT_FILE_SYSTEM)) {
            this.fileStrategy = new GitFileStrategy();
        } else {
            this.fileStrategy = null;
        }
    }

    public List<?> listFiles(String param) {
        return fileStrategy.listFiles(param);
    }

    public String readFile(Object file) {
        if (file instanceof File) {
            String path = ((File) file).getPath();
            return fileStrategy.readFile(path);
        } else if (file instanceof GitFileDto) {
            String url = ((GitFileDto) file).getDownload_url();
            return fileStrategy.readFile(url);
        }
        return null;
    }

    public String readSpecifiedFile(ProjectPathDto dto) {
        if (dto.getType().equals(LOCAL_FILE_SYSTEM)) {
            String path = getFullPath(dto.getPath(), dto.getFileName(), "\\");
            return fileStrategy.readFile(path);
        } else if (dto.getType().equals(GIT_FILE_SYSTEM)) {
            List<?> files = fileStrategy.listFiles(dto.getPath());
            for (Object file : files) {
                if (file instanceof GitFileDto && ((GitFileDto) file).getName().equals(dto.getFileName())) {
                    String url = ((GitFileDto) file).getDownload_url();
                    return fileStrategy.readFile(url);
                }
            }
        }
        return null;
    }

}
