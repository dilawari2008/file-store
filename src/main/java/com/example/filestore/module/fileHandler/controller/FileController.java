package com.example.filestore.module.fileHandler.controller;

import com.example.filestore.module.fileHandler.dto.PresignedUrlDto;
import com.example.filestore.module.fileHandler.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/app")
public class FileController {

    private final Logger log = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    @GetMapping("/ping")
    public String pingRoute() {
        return "pong";
    }

    @GetMapping("/files")
    public ResponseEntity<Page<?>> getAllFiles(Pageable pageable) {
        return new ResponseEntity<>(fileService.getAllFiles(pageable), HttpStatus.OK);
    }

    @GetMapping("/search-files")
    public ResponseEntity<Page<?>> searchAllFiles(Pageable pageable,@RequestParam(name = "searchKeyword", required = false) String searchKeyword) {
        return new ResponseEntity<>(fileService.searchAllFiles(pageable, searchKeyword), HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestParam("file")MultipartFile multipartFile) {
        try {
            fileService.uploadFile(multipartFile);
            return new ResponseEntity("File has been queued for upload!", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity("Input Error", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity download(@PathVariable("id") Long fileId) {
        String downloadMessage = fileService.download(fileId);
        return new ResponseEntity(downloadMessage, HttpStatus.OK);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Long fileId) {
        fileService.deleteFile(fileId);
        return new ResponseEntity("Queueing delete...", HttpStatus.OK);
    }

    @GetMapping("/presigned-url/{key}/{type}/{fileSize}")
    public ResponseEntity<PresignedUrlDto> getPresignedUrl(@PathVariable("key") String key, @PathVariable("type") String type, @PathVariable("fileSize") Long fileSize) {
        return new ResponseEntity<>(fileService.presignedS3Url(key, type, fileSize), HttpStatus.OK);
    }

    @PutMapping("/presigned-url/status/{id}/{status}")
    public ResponseEntity<String> getPresignedUrl(@PathVariable("id") Long id, @PathVariable("status") String status) {
        return new ResponseEntity<>(fileService.statusUpdate(id, status), HttpStatus.OK);
    }

}
