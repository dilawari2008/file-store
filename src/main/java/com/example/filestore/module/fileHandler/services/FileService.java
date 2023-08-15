package com.example.filestore.module.fileHandler.services;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.example.filestore.enums.FileUploadStatus;
import com.example.filestore.module.fileHandler.domain.FileInfo;
import com.example.filestore.module.fileHandler.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class FileService {

    private final Logger log = LoggerFactory.getLogger(FileService.class);

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private AWSS3FileHandler awss3FileHandler;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Value("${download.directory}")
    private String downloadDirectory;

    @Value("${download.buffer_size}")
    private Integer bufferSize;

    // paginate
    public Page<FileInfo> getAllFiles(Pageable pageable) {
        Page<FileInfo> fileInfoList = fileRepository.findAllActive(pageable);
        return fileInfoList;
    }

    public Page<FileInfo> searchAllFiles(Pageable pageable, String searchKeyword) {
        Page<FileInfo> fileInfoList = fileRepository.searchAllActive(searchKeyword, pageable);
        return fileInfoList;
    }

    public void uploadFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty())
            throw new IllegalStateException("(uploadFile) Cannot upload empty file");

        log.debug("(uploadFile) File details to be uploaded : name {} , file-type {}, file-size (in bytes) {} ", multipartFile.getName(), multipartFile.getContentType(), multipartFile.getSize());

        // multipartFile.getInputStream().available() - how much can be read without blocking (stopping, or waiting for the data to be available like buffering)
        // log.debug("(uploadFile) multipartFile.getInputStream().available() : "+ multipartFile.getInputStream().available());

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        String fileName = String.format("%s", multipartFile.getOriginalFilename());

        // enter the details in db
        FileInfo savedFile = fileRepository.save(new FileInfo(fileName, multipartFile.getContentType(),multipartFile.getSize(), "", new Date(), bucketName));

        try {
            // Uploading file to s3
            awss3FileHandler.upload(bucketName, fileName, metadata, multipartFile.getInputStream(), savedFile.getId());
        } catch (Exception e) {
            log.error("(uploadFile) Exception occurred while attempting to upload file : {}", e.toString());
            fileRepository.updateFileUploadStatus(savedFile.getId(), FileUploadStatus.FAILED.toString());
        }
    }

    private S3Object getDownloadObject(Long fileId) {
        Optional<FileInfo> fileInfo = fileRepository.findByIdIfActive(fileId);
        if(fileInfo.isPresent())
            return awss3FileHandler.download(fileInfo.get().getBucketName(),fileInfo.get().getFileName());
        else
            return null;
    }

    public String download(Long fileId) {
        S3Object s3Object = getDownloadObject(fileId);
        if(s3Object == null) return "File does not exist or is deleted";
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(downloadDirectory+s3Object.getKey()));

            // how many bytes to be read from the inputStream in one go, after which the next network call can be made to fetch more info
            byte[] buffer = new byte[bufferSize];
            int bytesRead;
            while (true) {
                if (!((bytesRead = inputStream.read(buffer)) != -1)) break;
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "Download queued...";
    }


    public void deleteFile(Long fileId) {
        Optional<FileInfo> fileInfo = fileRepository.findById(fileId);
        if(fileInfo.isPresent()) {
            awss3FileHandler.deleteFile(bucketName, fileInfo.get().getFileName());
            fileRepository.deleteFile(fileId);
        }
    }


}
