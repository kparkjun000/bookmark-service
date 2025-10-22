package com.zerobase.memobackend.service;

import com.zerobase.memobackend.dto.MemoDto;
import com.zerobase.memobackend.dto.request.CreateMemoRequest;
import com.zerobase.memobackend.dto.request.UpdateMemoRequest;
import com.zerobase.memobackend.entity.Memo;
import com.zerobase.memobackend.entity.Tag;
import com.zerobase.memobackend.exception.ResourceNotFoundException;
import com.zerobase.memobackend.repository.MemoRepository;
import com.zerobase.memobackend.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemoService {

    private final MemoRepository memoRepository;
    private final TagRepository tagRepository;

    /**
     * 모든 메모 조회
     */
    public List<MemoDto> getAllMemos() {
        log.info("모든 메모 조회");
        return memoRepository.findAll().stream()
                .map(MemoDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * ID로 메모 조회
     */
    public MemoDto getMemoById(Long id) {
        log.info("메모 조회 - ID: {}", id);
        Memo memo = memoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("메모를 찾을 수 없습니다. ID: " + id));
        return MemoDto.fromEntity(memo);
    }

    /**
     * 메모 생성
     */
    @Transactional
    public MemoDto createMemo(CreateMemoRequest request) {
        log.info("메모 생성 - 제목: {}", request.getTitle());

        Memo memo = Memo.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(request.getAuthor())
                .tags(new HashSet<>())
                .build();

        // 태그 처리
        if (request.getTagNames() != null && !request.getTagNames().isEmpty()) {
            Set<Tag> tags = getOrCreateTags(request.getTagNames());
            tags.forEach(memo::addTag);
        }

        Memo savedMemo = memoRepository.save(memo);
        return MemoDto.fromEntity(savedMemo);
    }

    /**
     * 메모 수정
     */
    @Transactional
    public MemoDto updateMemo(Long id, UpdateMemoRequest request) {
        log.info("메모 수정 - ID: {}", id);

        Memo memo = memoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("메모를 찾을 수 없습니다. ID: " + id));

        if (request.getTitle() != null) {
            memo.setTitle(request.getTitle());
        }
        if (request.getContent() != null) {
            memo.setContent(request.getContent());
        }
        if (request.getAuthor() != null) {
            memo.setAuthor(request.getAuthor());
        }

        // 태그 업데이트
        if (request.getTagNames() != null) {
            memo.clearTags();
            if (!request.getTagNames().isEmpty()) {
                Set<Tag> tags = getOrCreateTags(request.getTagNames());
                tags.forEach(memo::addTag);
            }
        }

        Memo updatedMemo = memoRepository.save(memo);
        return MemoDto.fromEntity(updatedMemo);
    }

    /**
     * 메모 삭제
     */
    @Transactional
    public void deleteMemo(Long id) {
        log.info("메모 삭제 - ID: {}", id);
        
        if (!memoRepository.existsById(id)) {
            throw new ResourceNotFoundException("메모를 찾을 수 없습니다. ID: " + id);
        }
        
        memoRepository.deleteById(id);
    }

    /**
     * 키워드로 메모 검색
     */
    public List<MemoDto> searchMemos(String keyword) {
        log.info("메모 검색 - 키워드: {}", keyword);
        return memoRepository.searchByKeyword(keyword).stream()
                .map(MemoDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 태그로 메모 검색
     */
    public List<MemoDto> getMemosByTag(String tagName) {
        log.info("태그로 메모 검색 - 태그: {}", tagName);
        return memoRepository.findByTagName(tagName).stream()
                .map(MemoDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 작성자로 메모 검색
     */
    public List<MemoDto> getMemosByAuthor(String author) {
        log.info("작성자로 메모 검색 - 작성자: {}", author);
        return memoRepository.findByAuthor(author).stream()
                .map(MemoDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 태그 생성 또는 조회
     */
    private Set<Tag> getOrCreateTags(Set<String> tagNames) {
        Set<Tag> tags = new HashSet<>();
        
        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> {
                        Tag newTag = Tag.builder()
                                .name(tagName)
                                .color("#3B82F6") // 기본 파란색
                                .build();
                        return tagRepository.save(newTag);
                    });
            tags.add(tag);
        }
        
        return tags;
    }

}

