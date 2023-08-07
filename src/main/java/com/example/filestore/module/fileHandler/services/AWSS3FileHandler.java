package com.example.filestore.module.fileHandler.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class AWSS3FileHandler {

    private final Logger log = LoggerFactory.getLogger(FileService.class);

    @Autowired
    private AmazonS3 amazonS3;

    public void upload(
            String bucketName,
            String key,
            ObjectMetadata objectMetadata,
            InputStream inputStream) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, objectMetadata);
        TransferManager transferManager = TransferManagerBuilder.standard()
                .withS3Client(amazonS3)
                .build();
        try {
            log.info("(upload) Queued file for upload");
            Upload upload = transferManager.upload(putObjectRequest);
            upload.waitForCompletion();
            if(upload.isDone()) {
                log.info("(upload) File Upload Completed");
            }
        } catch (InterruptedException e) {
            log.error("(upload) Interrupted Exception", e.getStackTrace());
        }
        //return amazonS3.putObject(bucketName, key, inputStream, objectMetadata);
    }

    public S3Object download(String bucketName, String key) {
        return amazonS3.getObject(bucketName, key);
    }

    public void deleteFile(String bucketName, String key) {
        amazonS3.deleteObject(bucketName, key);
    }
}
