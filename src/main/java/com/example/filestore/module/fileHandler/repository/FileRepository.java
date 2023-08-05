package com.example.filestore.module.fileHandler.repository;

import com.example.filestore.module.fileHandler.domain.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileInfo, Long> {

    @Modifying
    @Transactional
    @Query(value = "update FileInfo u set u.deleted=true where u.id=?1")
    void deleteFile(Long fileId);

    @Query(value = "select u from FileInfo u where u.deleted is null or u.deleted=false")
    List<FileInfo> findAllActive();
}
