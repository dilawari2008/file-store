package com.example.filestore.module.fileHandler.controller;

import com.example.filestore.module.fileHandler.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/app")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/ping")
    public String pingRoute() {
        return "pong";
    }

    @GetMapping("/files")
    public ResponseEntity<List<?>> getAllFiles() {
        return new ResponseEntity<>(fileService.getAllFiles(), HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity uploadFile(MultipartFile multipartFile) {
        fileService.uploadFile(multipartFile);
        return new ResponseEntity("File being uploaded...", HttpStatus.OK);
    }


}
