package com.example.filestore.module.fileHandler.repository;

import com.example.filestore.module.fileHandler.domain.FileInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<FileInfo> findAllActive(Pageable pageable);

    @Query(value = "select u from FileInfo u where (u.deleted is null or u.deleted = false) and lower(u.fileName) like lower(concat('%', ?1, '%'))")
    Page<FileInfo> searchAllActive(String searchKeyword, Pageable pageable);

    @Query(value = "update FileInfo u set u.fileUploadStatus = ?2 where id = ?1")
    void updateFileUploadStatus(Long id, String status);
}
