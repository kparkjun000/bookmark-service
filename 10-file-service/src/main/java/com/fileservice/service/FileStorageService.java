package com.fileservice.service;

import com.fileservice.config.FileStorageProperties;
import com.fileservice.dto.FileMetadataResponse;
import com.fileservice.dto.FileUploadResponse;
import com.fileservice.entity.FileMetadata;
import com.fileservice.exception.FileNotFoundException;
import com.fileservice.exception.FileStorageException;
import com.fileservice.exception.InvalidFileException;
import com.fileservice.repository.FileMetadataRepository;
import jakarta.annotation.PostConstruct;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Logger log = LoggerFactory.getLogger(FileStorageService.class);

    private final Path fileStorageLocation;
    private final FileStorageProperties fileStorageProperties;
    private final FileMetadataRepository fileMetadataRepository;
    private final ImageProcessingService imageProcessingService;

    public FileStorageService(FileStorageProperties fileStorageProperties,
                              FileMetadataRepository fileMetadataRepository,
                              ImageProcessingService imageProcessingService) {
        this.fileStorageProperties = fileStorageProperties;
        this.fileMetadataRepository = fileMetadataRepository;
        this.imageProcessingService = imageProcessingService;
        
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(this.fileStorageLocation);
            Files.createDirectories(this.fileStorageLocation.resolve("thumbnails"));
            log.info("File storage location initialized: {}", this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Transactional
    public FileUploadResponse storeFile(MultipartFile file, String description, String uploadedBy, 
                                       Boolean generateThumbnail, Boolean resizeImage) {
        // Validate file
        validateFile(file);
        
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = FilenameUtils.getExtension(originalFileName);
        String fileId = UUID.randomUUID().toString();
        String storedFileName = fileId + "." + fileExtension;
        
        try {
            // Check if the file's name contains invalid characters
            if (originalFileName.contains("..")) {
                throw new FileStorageException("Filename contains invalid path sequence: " + originalFileName);
            }

            // Copy file to the target location
            Path targetLocation = this.fileStorageLocation.resolve(storedFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            log.info("File stored successfully: {}", storedFileName);

            // Create metadata
            FileMetadata metadata = FileMetadata.builder()
                    .fileId(fileId)
                    .originalFileName(originalFileName)
                    .storedFileName(storedFileName)
                    .contentType(file.getContentType())
                    .fileSize(file.getSize())
                    .fileExtension(fileExtension)
                    .filePath(targetLocation.toString())
                    .uploadedBy(uploadedBy)
                    .description(description)
                    .status(FileMetadata.FileStatus.ACTIVE)
                    .hasThumbnail(false)
                    .build();

            // Process image if it's an image file
            if (imageProcessingService.isImage(file.getContentType())) {
                processImage(metadata, targetLocation, generateThumbnail, resizeImage);
            }

            // Save metadata to database
            metadata = fileMetadataRepository.save(metadata);
            
            log.info("File metadata saved: {}", fileId);

            // Build response
            return FileUploadResponse.builder()
                    .fileId(fileId)
                    .originalFileName(originalFileName)
                    .contentType(file.getContentType())
                    .fileSize(file.getSize())
                    .width(metadata.getWidth())
                    .height(metadata.getHeight())
                    .thumbnailUrl(metadata.getHasThumbnail() ? "/api/files/" + fileId + "/thumbnail" : null)
                    .downloadUrl("/api/files/" + fileId + "/download")
                    .uploadedAt(LocalDateTime.now())
                    .message("File uploaded successfully")
                    .build();

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + originalFileName + ". Please try again!", ex);
        }
    }

    private void processImage(FileMetadata metadata, Path targetLocation, 
                             Boolean generateThumbnail, Boolean resizeImage) {
        try {
            // Get image dimensions
            ImageProcessingService.ImageDimensions dimensions = 
                    imageProcessingService.getImageDimensions(targetLocation);
            metadata.setWidth(dimensions.width());
            metadata.setHeight(dimensions.height());

            // Resize image if requested
            if (Boolean.TRUE.equals(resizeImage)) {
                imageProcessingService.resizeImage(targetLocation, targetLocation);
                // Update dimensions after resize
                dimensions = imageProcessingService.getImageDimensions(targetLocation);
                metadata.setWidth(dimensions.width());
                metadata.setHeight(dimensions.height());
            }

            // Generate thumbnail if requested
            if (Boolean.TRUE.equals(generateThumbnail)) {
                String thumbnailFileName = "thumb_" + metadata.getStoredFileName();
                Path thumbnailPath = this.fileStorageLocation.resolve("thumbnails").resolve(thumbnailFileName);
                
                imageProcessingService.generateThumbnail(targetLocation, thumbnailPath);
                
                metadata.setHasThumbnail(true);
                metadata.setThumbnailFileName(thumbnailFileName);
                metadata.setThumbnailPath(thumbnailPath.toString());
            }
        } catch (Exception e) {
            log.error("Error processing image: {}", e.getMessage(), e);
            // Don't fail the upload, just log the error
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileException("Cannot upload empty file");
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new InvalidFileException("File name is empty");
        }

        String fileExtension = FilenameUtils.getExtension(originalFileName).toLowerCase();
        if (!fileStorageProperties.getAllowedExtensions().contains(fileExtension)) {
            throw new InvalidFileException(
                    "File extension '" + fileExtension + "' is not allowed. Allowed extensions: " + 
                    String.join(", ", fileStorageProperties.getAllowedExtensions())
            );
        }

        if (file.getSize() > fileStorageProperties.getMaxSize()) {
            throw new InvalidFileException(
                    "File size exceeds maximum allowed size of " + 
                    (fileStorageProperties.getMaxSize() / 1024 / 1024) + " MB"
            );
        }
    }

    @Transactional(readOnly = true)
    public Resource loadFileAsResource(String fileId) {
        try {
            FileMetadata metadata = fileMetadataRepository.findByFileIdAndStatus(
                    fileId, FileMetadata.FileStatus.ACTIVE)
                    .orElseThrow(() -> new FileNotFoundException("File not found with id: " + fileId));

            Path filePath = this.fileStorageLocation.resolve(metadata.getStoredFileName()).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found: " + fileId);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found: " + fileId, ex);
        }
    }

    @Transactional(readOnly = true)
    public Resource loadThumbnailAsResource(String fileId) {
        try {
            FileMetadata metadata = fileMetadataRepository.findByFileIdAndStatus(
                    fileId, FileMetadata.FileStatus.ACTIVE)
                    .orElseThrow(() -> new FileNotFoundException("File not found with id: " + fileId));

            if (!Boolean.TRUE.equals(metadata.getHasThumbnail())) {
                throw new FileNotFoundException("Thumbnail not found for file: " + fileId);
            }

            Path thumbnailPath = this.fileStorageLocation.resolve("thumbnails")
                    .resolve(metadata.getThumbnailFileName()).normalize();
            Resource resource = new UrlResource(thumbnailPath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException("Thumbnail file not found: " + fileId);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("Thumbnail not found: " + fileId, ex);
        }
    }

    @Transactional(readOnly = true)
    public FileMetadata getFileMetadata(String fileId) {
        return fileMetadataRepository.findByFileIdAndStatus(fileId, FileMetadata.FileStatus.ACTIVE)
                .orElseThrow(() -> new FileNotFoundException("File not found with id: " + fileId));
    }

    @Transactional(readOnly = true)
    public Page<FileMetadata> getAllFiles(Pageable pageable) {
        return fileMetadataRepository.findByStatus(FileMetadata.FileStatus.ACTIVE, pageable);
    }

    @Transactional(readOnly = true)
    public Page<FileMetadata> searchFiles(String keyword, Pageable pageable) {
        return fileMetadataRepository.searchFiles(keyword, pageable);
    }

    @Transactional
    public void deleteFile(String fileId) {
        FileMetadata metadata = fileMetadataRepository.findByFileIdAndStatus(
                fileId, FileMetadata.FileStatus.ACTIVE)
                .orElseThrow(() -> new FileNotFoundException("File not found with id: " + fileId));

        try {
            // Delete physical file
            Path filePath = this.fileStorageLocation.resolve(metadata.getStoredFileName()).normalize();
            Files.deleteIfExists(filePath);

            // Delete thumbnail if exists
            if (Boolean.TRUE.equals(metadata.getHasThumbnail())) {
                Path thumbnailPath = this.fileStorageLocation.resolve("thumbnails")
                        .resolve(metadata.getThumbnailFileName()).normalize();
                Files.deleteIfExists(thumbnailPath);
            }

            // Update status in database
            metadata.setStatus(FileMetadata.FileStatus.DELETED);
            fileMetadataRepository.save(metadata);
            
            log.info("File deleted successfully: {}", fileId);
        } catch (IOException ex) {
            throw new FileStorageException("Could not delete file: " + fileId, ex);
        }
    }
}

