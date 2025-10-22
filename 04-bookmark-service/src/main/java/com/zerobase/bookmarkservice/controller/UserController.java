package com.zerobase.bookmarkservice.controller;

import com.zerobase.bookmarkservice.dto.UserDto;
import com.zerobase.bookmarkservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User", description = "사용자 관리 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @Operation(summary = "사용자 생성", description = "새로운 사용자를 생성합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "사용자 생성 성공",
                     content = @Content(schema = @Schema(implementation = UserDto.Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 사용자")
    })
    @PostMapping
    public ResponseEntity<UserDto.Response> createUser(
            @Valid @RequestBody UserDto.Create request) {
        UserDto.Response response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "모든 사용자 조회", description = "모든 사용자 목록을 조회합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping
    public ResponseEntity<List<UserDto.Response>> getAllUsers() {
        List<UserDto.Response> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @Operation(summary = "사용자 조회", description = "ID로 사용자를 조회합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
                     content = @Content(schema = @Schema(implementation = UserDto.Response.class))),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto.Response> getUser(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable Long userId) {
        UserDto.Response response = userService.getUser(userId);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "이메일로 사용자 조회", description = "이메일로 사용자를 조회합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
                     content = @Content(schema = @Schema(implementation = UserDto.Response.class))),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto.Response> getUserByEmail(
            @Parameter(description = "사용자 이메일", required = true)
            @PathVariable String email) {
        UserDto.Response response = userService.getUserByEmail(email);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "사용자 삭제", description = "사용자를 삭제합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "삭제 성공"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}

