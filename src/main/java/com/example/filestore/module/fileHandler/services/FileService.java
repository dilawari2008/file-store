package com.example.filestore.module.fileHandler.services;

import com.example.filestore.module.fileHandler.domain.FileInfo;
import com.example.filestore.module.fileHandler.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    // paginate
    public List<FileInfo> getAllFiles() {
        List<FileInfo> fileInfoList = fileRepository.findAll();
        return fileInfoList;
    }

    public void uploadFile(MultipartFile multipartFile) {}


}
