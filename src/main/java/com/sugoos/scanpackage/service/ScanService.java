package com.sugoos.scanpackage.service;

import com.sugoos.scanpackage.dto.ProjectPathDto;
import com.sugoos.scanpackage.dto.RestResult;
import com.sugoos.scanpackage.strategy.FileContext;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import static com.sugoos.scanpackage.utils.ScanContentUtils.*;

@Service
public class ScanService {

    public RestResult<?> findSpecifiedDirInterface(ProjectPathDto dto) {
        if (!checkFileStatus(dto)) return RestResult.fail("该文件或目录不存在, 请重新检查后再试!");
        FileContext context = new FileContext(dto);
        List<?> files = context.listFiles(dto.getPath());
        List<String> list = new ArrayList<>();
        for (Object file : files) {
            addMappingInterfaceToListFromFileContent(context.readFile(file), list);
        }
        return RestResult.ok(list);
    }

    public RestResult<?> findSpecifiedFileInterface(ProjectPathDto dto) {
        if (!checkFileStatus(dto)) return RestResult.fail("该文件或目录不存在, 请重新检查后再试!");
        FileContext context = new FileContext(dto);
        List<String> list = new ArrayList<>();
        addMappingInterfaceToListFromFileContent(context.readSpecifiedFile(dto), list);
        return RestResult.ok(list);
    }

    private void addMappingInterfaceToListFromFileContent(String content, List<String> list) {
        List<String> rootPath = getRootPath(content);
        if (rootPath == null) return;
        for (String path : rootPath) {
            list.addAll(getAllMapping(content, path));
        }
    }

}
