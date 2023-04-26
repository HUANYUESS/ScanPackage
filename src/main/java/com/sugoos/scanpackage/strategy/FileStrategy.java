package com.sugoos.scanpackage.strategy;

import java.util.List;

public interface FileStrategy {

    List<?> listTopLevelFiles(String param);

    List<?> listRecursiveFiles(String param);

    String readFile(String file);

}
