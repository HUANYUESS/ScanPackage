package com.sugoos.scanpackage.strategy;

import java.util.List;

public interface FileStrategy {

    List<?> listFiles(String param);

    String readFile(String file);

}
