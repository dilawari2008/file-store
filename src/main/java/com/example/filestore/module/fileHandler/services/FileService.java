package com.example.filestore.module.fileHandler.services;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
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

    // paginate
    public Page<FileInfo> getAllFiles(Pageable pageable) {
        Page<FileInfo> fileInfoList = fileRepository.findAllActive(pageable);
        return fileInfoList;
    }

    public void uploadFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty())
            throw new IllegalStateException("Cannot upload empty file");

        log.info("(uploadFile) File size (in bytes) : " + multipartFile.getSize());

        log.info("(uploadFile) multipartFile.getSize() : "+ multipartFile.getSize());
        log.info("(uploadFile) multipartFile.getInputStream().available() : "+ multipartFile.getInputStream().available());

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        String fileName = String.format("%s", multipartFile.getOriginalFilename());

        // Uploading file to s3
        /*PutObjectResult putObjectResult = */awss3FileHandler.upload(
                bucketName, fileName, metadata, multipartFile.getInputStream());

        //log this object - putObjectResult

        fileRepository.save(new FileInfo(fileName, "", "", new Date(), bucketName));
    }

    private S3Object getDownloadObject(Long fileId) {
        Optional<FileInfo> fileInfo = fileRepository.findById(fileId);
        if(fileInfo.isPresent())
            return awss3FileHandler.download(fileInfo.get().getBucketName(),fileInfo.get().getFileName());
        else
            return null;
    }

    //Squash exceptions at the end
    public void download(Long fileId) {
        S3Object s3Object = getDownloadObject(fileId);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(downloadDirectory+s3Object.getKey()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        byte[] buffer = new byte[4096];
        int bytesRead;
        while (true) {
            try {
                if (!((bytesRead = inputStream.read(buffer)) != -1)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                outputStream.write(buffer, 0, bytesRead);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void deleteFile(Long fileId) {
        Optional<FileInfo> fileInfo = fileRepository.findById(fileId);
        if(fileInfo.isPresent()) {
            awss3FileHandler.deleteFile(bucketName, fileInfo.get().getFileName());
        }
        fileRepository.deleteFile(fileId);
    }


}
