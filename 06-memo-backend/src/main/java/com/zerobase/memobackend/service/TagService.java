package com.zerobase.memobackend.service;

import com.zerobase.memobackend.dto.TagDto;
import com.zerobase.memobackend.dto.request.CreateTagRequest;
import com.zerobase.memobackend.entity.Tag;
import com.zerobase.memobackend.exception.DuplicateResourceException;
import com.zerobase.memobackend.exception.ResourceNotFoundException;
import com.zerobase.memobackend.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;

    /**
     * 모든 태그 조회
     */
    public List<TagDto> getAllTags() {
        log.info("모든 태그 조회");
        return tagRepository.findAll().stream()
                .map(TagDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * ID로 태그 조회
     */
    public TagDto getTagById(Long id) {
        log.info("태그 조회 - ID: {}", id);
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("태그를 찾을 수 없습니다. ID: " + id));
        return TagDto.fromEntity(tag);
    }

    /**
     * 태그 생성
     */
    @Transactional
    public TagDto createTag(CreateTagRequest request) {
        log.info("태그 생성 - 이름: {}", request.getName());

        if (tagRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("이미 존재하는 태그입니다: " + request.getName());
        }

        Tag tag = Tag.builder()
                .name(request.getName())
                .color(request.getColor() != null ? request.getColor() : "#3B82F6")
                .build();

        Tag savedTag = tagRepository.save(tag);
        return TagDto.fromEntity(savedTag);
    }

    /**
     * 태그 수정
     */
    @Transactional
    public TagDto updateTag(Long id, CreateTagRequest request) {
        log.info("태그 수정 - ID: {}", id);

        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("태그를 찾을 수 없습니다. ID: " + id));

        // 이름 변경 시 중복 체크
        if (request.getName() != null && !request.getName().equals(tag.getName())) {
            if (tagRepository.existsByName(request.getName())) {
                throw new DuplicateResourceException("이미 존재하는 태그입니다: " + request.getName());
            }
            tag.setName(request.getName());
        }

        if (request.getColor() != null) {
            tag.setColor(request.getColor());
        }

        Tag updatedTag = tagRepository.save(tag);
        return TagDto.fromEntity(updatedTag);
    }

    /**
     * 태그 삭제
     */
    @Transactional
    public void deleteTag(Long id) {
        log.info("태그 삭제 - ID: {}", id);

        if (!tagRepository.existsById(id)) {
            throw new ResourceNotFoundException("태그를 찾을 수 없습니다. ID: " + id);
        }

        tagRepository.deleteById(id);
    }

    /**
     * 태그 이름으로 검색
     */
    public List<TagDto> searchTags(String keyword) {
        log.info("태그 검색 - 키워드: {}", keyword);
        return tagRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(TagDto::fromEntity)
                .collect(Collectors.toList());
    }

}

