package com.example.filestore.module.fileHandler.repository;

import com.example.filestore.module.fileHandler.domain.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileInfo, Long> {
}
