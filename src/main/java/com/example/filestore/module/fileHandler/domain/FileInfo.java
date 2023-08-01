package com.example.filestore.module.fileHandler.domain;

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
    private String userName;
    private Date uploadTime;
    private String fileUrl;

    public FileInfo() {
    }

    public FileInfo(String fileName, String fileType, String userName, Date uploadTime, String fileUrl) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.userName = userName;
        this.uploadTime = uploadTime;
        this.fileUrl = fileUrl;
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

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public String toString() {
        return "fileInfo{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", userName='" + userName + '\'' +
                ", uploadTime=" + uploadTime +
                ", fileUrl='" + fileUrl + '\'' +
                '}';
    }
}
