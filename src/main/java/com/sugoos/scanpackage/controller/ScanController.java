package com.sugoos.scanpackage.controller;

import com.sugoos.scanpackage.annotation.NotNullParams;
import com.sugoos.scanpackage.dto.ProjectPathDto;
import com.sugoos.scanpackage.dto.RestResult;
import com.sugoos.scanpackage.service.ScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/scan")
public class ScanController {

    private final ScanService scanService;

    @Autowired
    public ScanController(ScanService scanService) {
        this.scanService = scanService;
    }

    @NotNullParams
    @PostMapping(value = "/interface")
    public RestResult<?> findAllInterface(@RequestBody ProjectPathDto dto) {
        if (dto.getFileName() != null) {
            return scanService.findSpecifiedFileInterface(dto);
        }
        return scanService.findSpecifiedDirInterface(dto);
    }

    @NotNullParams
    @PostMapping(value = "/github")
    public RestResult<?> findAllGithubInterface(@RequestBody ProjectPathDto dto) {
        if (dto.getFileName() != null) {
            return scanService.findSpecifiedGithubFileInterface(dto);
        }
        return scanService.findSpecifiedGithubDirInterface(dto);
    }
}
