package com.fileservice.service;

import com.fileservice.config.ImageProcessingProperties;
import com.fileservice.exception.FileStorageException;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Service
public class ImageProcessingService {

    private static final Logger log = LoggerFactory.getLogger(ImageProcessingService.class);
    private final ImageProcessingProperties imageProperties;

    public ImageProcessingService(ImageProcessingProperties imageProperties) {
        this.imageProperties = imageProperties;
    }

    /**
     * Get image dimensions (width and height)
     */
    public ImageDimensions getImageDimensions(Path filePath) {
        try {
            BufferedImage image = ImageIO.read(filePath.toFile());
            if (image == null) {
                throw new FileStorageException("Unable to read image file: " + filePath);
            }
            return new ImageDimensions(image.getWidth(), image.getHeight());
        } catch (IOException e) {
            log.error("Error reading image dimensions: {}", e.getMessage());
            throw new FileStorageException("Failed to read image dimensions", e);
        }
    }

    /**
     * Generate thumbnail for the given image
     */
    public void generateThumbnail(Path sourcePath, Path thumbnailPath) {
        try {
            Integer width = imageProperties.getThumbnail().getWidth();
            Integer height = imageProperties.getThumbnail().getHeight();
            
            log.debug("Generating thumbnail: {}x{} for file: {}", width, height, sourcePath);
            
            Thumbnails.of(sourcePath.toFile())
                    .size(width, height)
                    .keepAspectRatio(true)
                    .toFile(thumbnailPath.toFile());
            
            log.info("Thumbnail generated successfully: {}", thumbnailPath);
        } catch (IOException e) {
            log.error("Error generating thumbnail: {}", e.getMessage());
            throw new FileStorageException("Failed to generate thumbnail", e);
        }
    }

    /**
     * Resize image to maximum dimensions while keeping aspect ratio
     */
    public void resizeImage(Path sourcePath, Path outputPath) {
        try {
            Integer maxWidth = imageProperties.getResize().getMaxWidth();
            Integer maxHeight = imageProperties.getResize().getMaxHeight();
            
            BufferedImage originalImage = ImageIO.read(sourcePath.toFile());
            
            // Only resize if image is larger than max dimensions
            if (originalImage.getWidth() > maxWidth || originalImage.getHeight() > maxHeight) {
                log.debug("Resizing image to max {}x{} for file: {}", maxWidth, maxHeight, sourcePath);
                
                Thumbnails.of(sourcePath.toFile())
                        .size(maxWidth, maxHeight)
                        .keepAspectRatio(true)
                        .toFile(outputPath.toFile());
                
                log.info("Image resized successfully: {}", outputPath);
            } else {
                log.debug("Image is within max dimensions, no resizing needed");
            }
        } catch (IOException e) {
            log.error("Error resizing image: {}", e.getMessage());
            throw new FileStorageException("Failed to resize image", e);
        }
    }

    /**
     * Check if file is a valid image
     */
    public boolean isImage(String contentType) {
        return contentType != null && contentType.startsWith("image/");
    }

    /**
     * Validate image file
     */
    public void validateImage(Path filePath) {
        try {
            BufferedImage image = ImageIO.read(filePath.toFile());
            if (image == null) {
                throw new FileStorageException("Invalid image file");
            }
        } catch (IOException e) {
            throw new FileStorageException("Failed to validate image", e);
        }
    }

    /**
     * Image dimensions holder class
     */
    public record ImageDimensions(Integer width, Integer height) {}
}

