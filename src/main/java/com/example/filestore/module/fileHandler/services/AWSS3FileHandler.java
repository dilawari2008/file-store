package com.example.filestore.module.fileHandler.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.TransferProgress;
import com.amazonaws.services.s3.transfer.Upload;
import com.example.filestore.enums.FileUploadStatus;
import com.example.filestore.module.fileHandler.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;

@Service
public class AWSS3FileHandler {

    private final Logger log = LoggerFactory.getLogger(FileService.class);

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private FileRepository fileRepository;



    public void upload(String bucketName, String key, ObjectMetadata objectMetadata, InputStream inputStream, Long fileId) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, objectMetadata);
        TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(amazonS3).build();
        try {
            log.info("(upload) Queued file {} for upload", key);
            fileRepository.updateFileUploadStatus(fileId, FileUploadStatus.PROCESSING.toString());
            Upload upload = transferManager.upload(putObjectRequest);
            //upload.waitForCompletion();
            // Track the upload progress using TransferProgress
            while (!upload.isDone()) {
                TransferProgress progress = upload.getProgress();
                System.out.println("Total bytes transferred: " + progress.getBytesTransferred());
                System.out.println("Total bytes to transfer: " + progress.getTotalBytesToTransfer());
                System.out.println("Percentage completed: " + progress.getPercentTransferred() + "%");
                // System.out.println("Transfer speed: " + progress.bytesPerSecond() + " bytes/s");
                // A short sleep here to avoid high CPU usage during the loop
                Thread.sleep(5000);
            }
            if(upload.isDone()) {
                fileRepository.updateFileUploadStatus(fileId, FileUploadStatus.SUCCESS.toString());
                log.debug("(upload) File Upload Completed");
            }
        } catch (InterruptedException e) {
            log.error("(upload) Interrupted Exception", e.getStackTrace());
            fileRepository.updateFileUploadStatus(fileId, FileUploadStatus.FAILED.toString());
        }
    }

    public S3Object download(String bucketName, String key) {
        return amazonS3.getObject(bucketName, key);
    }

    public void deleteFile(String bucketName, String key) {
        amazonS3.deleteObject(bucketName, key);
    }

    public String presignedS3Url(String bucketName, String key) {

        // Set expiration time for the pre-signed URL (in milliseconds)
        Date expiration = new Date(System.currentTimeMillis() + 3600000); // 1 hour from now

        // Generate the pre-signed URL
        URL url = amazonS3.generatePresignedUrl(bucketName, key, expiration);

        return url.toString();
    }
}
