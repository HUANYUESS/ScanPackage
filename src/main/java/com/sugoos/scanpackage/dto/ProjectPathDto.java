package com.sugoos.scanpackage.dto;

import com.sugoos.scanpackage.annotation.NotNullParams;
import lombok.Data;

@Data
public class ProjectPathDto {

    @NotNullParams(msg = "路径不能为空")
    private String path;

    @NotNullParams(msg = "类型不能为空")
    private String type;

    // 是否迭代子目录
    private boolean recursive = false;

    // 具体的文件名
    private String fileName;


}
