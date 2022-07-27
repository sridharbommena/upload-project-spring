package com.temp.upload.controller;

import com.temp.upload.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @GetMapping("/delete-all")
    public String getAll() throws Exception
    {
        uploadService.deleteFiles();
        return "check-logs";
    }



}
