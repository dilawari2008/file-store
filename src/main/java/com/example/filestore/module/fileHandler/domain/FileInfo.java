package com.example.filestore.module.fileHandler.domain;

import com.example.filestore.enums.FileUploadStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FileInfo {
    @Id
    @GeneratedValue
    private Long id;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private String userName;
    private Date uploadTime;
    private String bucketName;
    private Boolean deleted;
    private String fileUploadStatus;

    public FileInfo(String fileName, String fileType, Long fileSize, String userName, Date uploadTime, String bucketName) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.userName = userName;
        this.uploadTime = uploadTime;
        this.bucketName = bucketName;
        this.deleted = false;
        this.fileUploadStatus = FileUploadStatus.QUEUED.toString();
    }
}
