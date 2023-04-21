package com.sugoos.scanpackage.dto;

import com.sugoos.scanpackage.annotation.NotNullParams;
import lombok.Data;

import java.util.List;

@Data
public class ProjectPathDto {

    @NotNullParams(msg = "路径不能为空")
    private String path;

    @NotNullParams(msg = "类型不能为空")
    private String type;

    private String fileName;

}
