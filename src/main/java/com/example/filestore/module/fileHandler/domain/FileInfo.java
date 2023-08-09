package com.example.filestore.module.fileHandler.domain;

import com.example.enums.FileUploadStatus;
import software.amazon.ion.Decimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

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

    public FileInfo() {
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }


    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileUploadStatus() {
        return fileUploadStatus;
    }

    public void setFileUploadStatus(String fileUploadStatus) {
        this.fileUploadStatus = fileUploadStatus;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize=" + fileSize +
                ", userName='" + userName + '\'' +
                ", uploadTime=" + uploadTime +
                ", bucketName='" + bucketName + '\'' +
                ", deleted=" + deleted +
                ", fileUploadStatus='" + fileUploadStatus + '\'' +
                '}';
    }
}
