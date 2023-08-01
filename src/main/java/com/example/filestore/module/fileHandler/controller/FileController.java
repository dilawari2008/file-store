package com.example.filestore.module.fileHandler.controller;

import com.example.filestore.module.fileHandler.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/ping")
    public String pingRoute() {
        return "pong";
    }


}
