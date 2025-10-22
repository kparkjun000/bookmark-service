package com.example.todoapi;

import com.example.todoapi.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
public class ApiHealthCheckTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSignupAndLogin() throws Exception {
        // 1. 회원가입 테스트
        UserDto.SignupRequest signupRequest = UserDto.SignupRequest.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .build();

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        System.out.println("✓ 회원가입 API: 201 Created");

        // 2. 로그인 테스트
        UserDto.LoginRequest loginRequest = UserDto.LoginRequest.builder()
                .username("testuser")
                .password("password123")
                .build();

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        System.out.println("✓ 로그인 API: 200 OK");

        String responseBody = loginResult.getResponse().getContentAsString();
        UserDto.AuthResponse authResponse = objectMapper.readValue(responseBody, UserDto.AuthResponse.class);
        String token = authResponse.getToken();

        // 3. Todo 목록 조회 테스트 (인증 필요)
        mockMvc.perform(get("/api/todos")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        System.out.println("✓ Todo 목록 조회 API: 200 OK");

        // 4. 통계 조회 테스트
        mockMvc.perform(get("/api/statistics")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalTodos").exists())
                .andExpect(jsonPath("$.completedTodos").exists());

        System.out.println("✓ 통계 API: 200 OK");

        System.out.println("\n모든 API 엔드포인트가 정상 작동합니다!");
    }
}