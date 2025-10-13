package com.zerobase.bookmarkservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    
    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 사용자입니다"),
    
    // Bookmark
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "북마크를 찾을 수 없습니다"),
    INVALID_URL(HttpStatus.BAD_REQUEST, "유효하지 않은 URL입니다"),
    METADATA_EXTRACTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "메타데이터 추출에 실패했습니다"),
    
    // Tag
    TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "태그를 찾을 수 없습니다"),
    TAG_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 태그입니다"),
    
    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 입력값입니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다");
    
    private final HttpStatus status;
    private final String message;
}

