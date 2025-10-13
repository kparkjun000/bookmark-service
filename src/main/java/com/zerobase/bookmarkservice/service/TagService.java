package com.zerobase.bookmarkservice.service;

import com.zerobase.bookmarkservice.dto.TagDto;
import com.zerobase.bookmarkservice.entity.Tag;
import com.zerobase.bookmarkservice.entity.User;
import com.zerobase.bookmarkservice.exception.CustomException;
import com.zerobase.bookmarkservice.exception.ErrorCode;
import com.zerobase.bookmarkservice.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {
    
    private final TagRepository tagRepository;
    private final UserService userService;
    
    @Transactional
    public TagDto.Response createTag(Long userId, TagDto.Create request) {
        log.info("Creating tag for user ID: {}, tag name: {}", userId, request.getName());
        
        User user = userService.findUserById(userId);
        
        if (tagRepository.existsByNameAndUser(request.getName(), user)) {
            throw new CustomException(ErrorCode.TAG_ALREADY_EXISTS);
        }
        
        Tag tag = Tag.builder()
                .name(request.getName())
                .user(user)
                .build();
        
        Tag savedTag = tagRepository.save(tag);
        log.info("Tag created successfully with ID: {}", savedTag.getId());
        
        return TagDto.Response.from(savedTag);
    }
    
    public List<TagDto.Response> getUserTags(Long userId) {
        log.info("Getting tags for user ID: {}", userId);
        userService.findUserById(userId); // Verify user exists
        
        return tagRepository.findByUserId(userId).stream()
                .map(TagDto.Response::from)
                .collect(Collectors.toList());
    }
    
    public TagDto.Response getTag(Long userId, Long tagId) {
        log.info("Getting tag ID: {} for user ID: {}", tagId, userId);
        Tag tag = findTagByIdAndUserId(tagId, userId);
        return TagDto.Response.from(tag);
    }
    
    @Transactional
    public void deleteTag(Long userId, Long tagId) {
        log.info("Deleting tag ID: {} for user ID: {}", tagId, userId);
        Tag tag = findTagByIdAndUserId(tagId, userId);
        tagRepository.delete(tag);
        log.info("Tag deleted successfully with ID: {}", tagId);
    }
    
    public Tag findTagByIdAndUserId(Long tagId, Long userId) {
        return tagRepository.findByIdAndUserId(tagId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.TAG_NOT_FOUND));
    }
    
    @Transactional
    public Tag findOrCreateTag(String tagName, User user) {
        return tagRepository.findByNameAndUser(tagName, user)
                .orElseGet(() -> {
                    Tag newTag = Tag.builder()
                            .name(tagName)
                            .user(user)
                            .build();
                    return tagRepository.save(newTag);
                });
    }
}

