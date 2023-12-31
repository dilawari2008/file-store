package com.example.filestore.configuration;


import com.example.filestore.module.fileHandler.controller.FileController;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(FileController.class);

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<String> handleFileSizeLimitExceeded(FileSizeLimitExceededException ex) {
        log.error("(handleFileSizeLimitExceeded) file size exceeded"+ ex.toString());
        String errorMessage = "File size exceeds the maximum permitted size.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(SizeLimitExceededException.class)
    public ResponseEntity<String> handleRequestSizeLimitExceeded(SizeLimitExceededException ex) {
        log.error("(handleRequestSizeLimitExceeded) request size exceeded" + ex.toString());
        String errorMessage = "Request size exceeds the maximum permitted size.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
}
