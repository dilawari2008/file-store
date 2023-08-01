package com.example.filestore.module.fileHandler.services;

import com.example.filestore.module.fileHandler.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;


}
