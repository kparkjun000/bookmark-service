package com.zerobase.bookmarkservice.service;

import com.zerobase.bookmarkservice.dto.BookmarkDto;
import com.zerobase.bookmarkservice.entity.Bookmark;
import com.zerobase.bookmarkservice.entity.Tag;
import com.zerobase.bookmarkservice.entity.User;
import com.zerobase.bookmarkservice.exception.CustomException;
import com.zerobase.bookmarkservice.exception.ErrorCode;
import com.zerobase.bookmarkservice.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {
    
    private final BookmarkRepository bookmarkRepository;
    private final UserService userService;
    private final TagService tagService;
    private final MetadataExtractor metadataExtractor;
    
    @Transactional
    public BookmarkDto.Response createBookmark(Long userId, BookmarkDto.Create request) {
        log.info("Creating bookmark for user ID: {}, URL: {}", userId, request.getUrl());
        
        User user = userService.findUserById(userId);
        
        // Extract metadata if title or description is not provided
        BookmarkDto.MetadataResponse metadata = null;
        if (request.getTitle() == null || request.getDescription() == null) {
            try {
                metadata = metadataExtractor.extractMetadata(request.getUrl());
            } catch (CustomException e) {
                log.warn("Failed to extract metadata, continuing without it: {}", e.getMessage());
            }
        }
        
        Bookmark bookmark = Bookmark.builder()
                .url(request.getUrl())
                .title(request.getTitle() != null ? request.getTitle() : 
                       (metadata != null ? metadata.getTitle() : null))
                .description(request.getDescription() != null ? request.getDescription() : 
                            (metadata != null ? metadata.getDescription() : null))
                .imageUrl(metadata != null ? metadata.getImageUrl() : null)
                .siteName(metadata != null ? metadata.getSiteName() : null)
                .isPublic(request.getIsPublic() != null ? request.getIsPublic() : false)
                .isFavorite(request.getIsFavorite() != null ? request.getIsFavorite() : false)
                .user(user)
                .build();
        
        // Add tags
        if (request.getTagNames() != null && !request.getTagNames().isEmpty()) {
            Set<Tag> tags = new HashSet<>();
            for (String tagName : request.getTagNames()) {
                Tag tag = tagService.findOrCreateTag(tagName, user);
                tags.add(tag);
            }
            bookmark.setTags(tags);
        }
        
        Bookmark savedBookmark = bookmarkRepository.save(bookmark);
        log.info("Bookmark created successfully with ID: {}", savedBookmark.getId());
        
        return BookmarkDto.Response.from(savedBookmark);
    }
    
    public BookmarkDto.Response getBookmark(Long userId, Long bookmarkId) {
        log.info("Getting bookmark ID: {} for user ID: {}", bookmarkId, userId);
        Bookmark bookmark = findBookmarkByIdAndUserId(bookmarkId, userId);
        return BookmarkDto.Response.from(bookmark);
    }
    
    public Page<BookmarkDto.Response> getUserBookmarks(Long userId, Pageable pageable) {
        log.info("Getting bookmarks for user ID: {} with page: {}", userId, pageable.getPageNumber());
        userService.findUserById(userId); // Verify user exists
        
        return bookmarkRepository.findByUserId(userId, pageable)
                .map(BookmarkDto.Response::from);
    }
    
    public Page<BookmarkDto.Response> getFavoriteBookmarks(Long userId, Pageable pageable) {
        log.info("Getting favorite bookmarks for user ID: {}", userId);
        userService.findUserById(userId); // Verify user exists
        
        return bookmarkRepository.findByUserIdAndIsFavoriteTrue(userId, pageable)
                .map(BookmarkDto.Response::from);
    }
    
    public Page<BookmarkDto.Response> getBookmarksByTag(Long userId, Long tagId, Pageable pageable) {
        log.info("Getting bookmarks for user ID: {} with tag ID: {}", userId, tagId);
        userService.findUserById(userId); // Verify user exists
        tagService.findTagByIdAndUserId(tagId, userId); // Verify tag exists
        
        return bookmarkRepository.findByUserIdAndTagId(userId, tagId, pageable)
                .map(BookmarkDto.Response::from);
    }
    
    public Page<BookmarkDto.Response> searchBookmarks(Long userId, String keyword, Pageable pageable) {
        log.info("Searching bookmarks for user ID: {} with keyword: {}", userId, keyword);
        userService.findUserById(userId); // Verify user exists
        
        return bookmarkRepository.searchByKeyword(userId, keyword, pageable)
                .map(BookmarkDto.Response::from);
    }
    
    public Page<BookmarkDto.Response> getPublicBookmarks(Pageable pageable) {
        log.info("Getting public bookmarks");
        
        return bookmarkRepository.findByIsPublicTrue(pageable)
                .map(BookmarkDto.Response::from);
    }
    
    @Transactional
    public BookmarkDto.Response updateBookmark(Long userId, Long bookmarkId, BookmarkDto.Update request) {
        log.info("Updating bookmark ID: {} for user ID: {}", bookmarkId, userId);
        
        Bookmark bookmark = findBookmarkByIdAndUserId(bookmarkId, userId);
        
        if (request.getTitle() != null) {
            bookmark.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            bookmark.setDescription(request.getDescription());
        }
        if (request.getIsPublic() != null) {
            bookmark.setIsPublic(request.getIsPublic());
        }
        if (request.getIsFavorite() != null) {
            bookmark.setIsFavorite(request.getIsFavorite());
        }
        
        // Update tags
        if (request.getTagNames() != null) {
            bookmark.getTags().clear();
            Set<Tag> newTags = new HashSet<>();
            for (String tagName : request.getTagNames()) {
                Tag tag = tagService.findOrCreateTag(tagName, bookmark.getUser());
                newTags.add(tag);
            }
            bookmark.setTags(newTags);
        }
        
        Bookmark updatedBookmark = bookmarkRepository.save(bookmark);
        log.info("Bookmark updated successfully with ID: {}", updatedBookmark.getId());
        
        return BookmarkDto.Response.from(updatedBookmark);
    }
    
    @Transactional
    public void deleteBookmark(Long userId, Long bookmarkId) {
        log.info("Deleting bookmark ID: {} for user ID: {}", bookmarkId, userId);
        Bookmark bookmark = findBookmarkByIdAndUserId(bookmarkId, userId);
        bookmarkRepository.delete(bookmark);
        log.info("Bookmark deleted successfully with ID: {}", bookmarkId);
    }
    
    public BookmarkDto.MetadataResponse extractMetadata(String url) {
        log.info("Extracting metadata from URL: {}", url);
        return metadataExtractor.extractMetadata(url);
    }
    
    private Bookmark findBookmarkByIdAndUserId(Long bookmarkId, Long userId) {
        return bookmarkRepository.findByIdAndUserId(bookmarkId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOOKMARK_NOT_FOUND));
    }
}

