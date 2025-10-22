package com.fileservice.controller;

import com.fileservice.dto.ApiResponse;
import com.fileservice.dto.FileListResponse;
import com.fileservice.dto.FileMetadataResponse;
import com.fileservice.dto.FileUploadResponse;
import com.fileservice.entity.FileMetadata;
import com.fileservice.service.FileStorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);
    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    /**
     * Upload a file
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "uploadedBy", required = false) String uploadedBy,
            @RequestParam(value = "generateThumbnail", defaultValue = "true") Boolean generateThumbnail,
            @RequestParam(value = "resizeImage", defaultValue = "false") Boolean resizeImage) {
        
        log.info("Received file upload request: {}", file.getOriginalFilename());
        
        FileUploadResponse response = fileStorageService.storeFile(
                file, description, uploadedBy, generateThumbnail, resizeImage);
        
        return ResponseEntity.ok(ApiResponse.success(response, "File uploaded successfully"));
    }

    /**
     * Upload multiple files
     */
    @PostMapping(value = "/upload-multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<List<FileUploadResponse>>> uploadMultipleFiles(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "uploadedBy", required = false) String uploadedBy,
            @RequestParam(value = "generateThumbnail", defaultValue = "true") Boolean generateThumbnail,
            @RequestParam(value = "resizeImage", defaultValue = "false") Boolean resizeImage) {
        
        log.info("Received multiple file upload request: {} files", files.length);
        
        List<FileUploadResponse> responses = List.of(files).stream()
                .map(file -> fileStorageService.storeFile(file, description, uploadedBy, generateThumbnail, resizeImage))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(responses, files.length + " files uploaded successfully"));
    }

    /**
     * Download a file
     */
    @GetMapping("/{fileId}/download")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String fileId, 
            HttpServletRequest request) {
        
        log.info("Received file download request: {}", fileId);
        
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileId);
        FileMetadata metadata = fileStorageService.getFileMetadata(fileId);

        // Try to determine file's content type
        String contentType = metadata.getContentType();
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + metadata.getOriginalFileName() + "\"")
                .body(resource);
    }

    /**
     * View a file (inline display)
     */
    @GetMapping("/{fileId}/view")
    public ResponseEntity<Resource> viewFile(
            @PathVariable String fileId, 
            HttpServletRequest request) {
        
        log.info("Received file view request: {}", fileId);
        
        Resource resource = fileStorageService.loadFileAsResource(fileId);
        FileMetadata metadata = fileStorageService.getFileMetadata(fileId);

        String contentType = metadata.getContentType();
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(resource);
    }

    /**
     * Get thumbnail
     */
    @GetMapping("/{fileId}/thumbnail")
    public ResponseEntity<Resource> getThumbnail(@PathVariable String fileId) {
        log.info("Received thumbnail request: {}", fileId);
        
        Resource resource = fileStorageService.loadThumbnailAsResource(fileId);
        FileMetadata metadata = fileStorageService.getFileMetadata(fileId);

        String contentType = metadata.getContentType();
        if (contentType == null) {
            contentType = "image/jpeg";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(resource);
    }

    /**
     * Get file metadata
     */
    @GetMapping("/{fileId}")
    public ResponseEntity<ApiResponse<FileMetadataResponse>> getFileMetadata(@PathVariable String fileId) {
        log.info("Received file metadata request: {}", fileId);
        
        FileMetadata metadata = fileStorageService.getFileMetadata(fileId);
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        FileMetadataResponse response = FileMetadataResponse.fromEntity(metadata, baseUrl);
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get all files with pagination
     */
    @GetMapping
    public ResponseEntity<ApiResponse<FileListResponse>> getAllFiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {
        
        log.info("Received files list request: page={}, size={}", page, size);
        
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<FileMetadata> filePage = fileStorageService.getAllFiles(pageable);
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        
        List<FileMetadataResponse> files = filePage.getContent().stream()
                .map(metadata -> FileMetadataResponse.fromEntity(metadata, baseUrl))
                .collect(Collectors.toList());
        
        FileListResponse response = FileListResponse.builder()
                .files(files)
                .currentPage(filePage.getNumber())
                .totalPages(filePage.getTotalPages())
                .totalElements(filePage.getTotalElements())
                .pageSize(filePage.getSize())
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Search files
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<FileListResponse>> searchFiles(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("Received file search request: keyword={}", keyword);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<FileMetadata> filePage = fileStorageService.searchFiles(keyword, pageable);
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        
        List<FileMetadataResponse> files = filePage.getContent().stream()
                .map(metadata -> FileMetadataResponse.fromEntity(metadata, baseUrl))
                .collect(Collectors.toList());
        
        FileListResponse response = FileListResponse.builder()
                .files(files)
                .currentPage(filePage.getNumber())
                .totalPages(filePage.getTotalPages())
                .totalElements(filePage.getTotalElements())
                .pageSize(filePage.getSize())
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Delete a file
     */
    @DeleteMapping("/{fileId}")
    public ResponseEntity<ApiResponse<String>> deleteFile(@PathVariable String fileId) {
        log.info("Received file delete request: {}", fileId);
        
        fileStorageService.deleteFile(fileId);
        
        return ResponseEntity.ok(ApiResponse.success(
                "File deleted successfully", 
                "File with ID " + fileId + " has been deleted"));
    }
}

