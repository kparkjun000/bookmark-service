package com.fileservice.repository;

import com.fileservice.entity.FileMetadata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {

    Optional<FileMetadata> findByFileId(String fileId);

    Optional<FileMetadata> findByFileIdAndStatus(String fileId, FileMetadata.FileStatus status);

    List<FileMetadata> findByStatus(FileMetadata.FileStatus status);

    Page<FileMetadata> findByStatus(FileMetadata.FileStatus status, Pageable pageable);

    @Query("SELECT f FROM FileMetadata f WHERE f.status = :status AND f.contentType LIKE 'image/%'")
    Page<FileMetadata> findImagesByStatus(@Param("status") FileMetadata.FileStatus status, Pageable pageable);

    @Query("SELECT f FROM FileMetadata f WHERE f.originalFileName LIKE %:keyword% OR f.description LIKE %:keyword%")
    Page<FileMetadata> searchFiles(@Param("keyword") String keyword, Pageable pageable);

    boolean existsByFileId(String fileId);

    @Query("SELECT SUM(f.fileSize) FROM FileMetadata f WHERE f.status = :status")
    Long getTotalFileSizeByStatus(@Param("status") FileMetadata.FileStatus status);
}

