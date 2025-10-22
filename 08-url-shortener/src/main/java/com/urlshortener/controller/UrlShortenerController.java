package com.urlshortener.controller;

import com.google.zxing.WriterException;
import com.urlshortener.dto.CreateUrlRequest;
import com.urlshortener.dto.UrlResponse;
import com.urlshortener.entity.ShortenedUrl;
import com.urlshortener.service.QrCodeService;
import com.urlshortener.service.UrlShortenerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "URL Shortener", description = "URL 단축 및 리디렉션 API")
public class UrlShortenerController {

    private static final Logger log = LoggerFactory.getLogger(UrlShortenerController.class);

    private final UrlShortenerService urlShortenerService;
    private final QrCodeService qrCodeService;

    @Value("${app.base-url}")
    private String baseUrl;

    public UrlShortenerController(UrlShortenerService urlShortenerService, QrCodeService qrCodeService) {
        this.urlShortenerService = urlShortenerService;
        this.qrCodeService = qrCodeService;
    }

    /**
     * Create a shortened URL
     */
    @Operation(summary = "URL 단축 생성", description = "긴 URL을 짧은 URL로 변환합니다. 커스텀 별칭, 제목, 설명, 만료일을 설정할 수 있습니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "URL 단축 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "409", description = "커스텀 별칭 중복")
    })
    @PostMapping("/api/urls")
    public ResponseEntity<UrlResponse> createShortUrl(@Valid @RequestBody CreateUrlRequest request) {
        log.info("Creating shortened URL for: {}", request.getOriginalUrl());
        ShortenedUrl shortenedUrl = urlShortenerService.createShortenedUrl(request);
        UrlResponse response = UrlResponse.from(shortenedUrl, baseUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get shortened URL details
     */
    @Operation(summary = "URL 정보 조회", description = "단축 코드로 URL 상세 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "URL을 찾을 수 없음")
    })
    @GetMapping("/api/urls/{shortCode}")
    public ResponseEntity<UrlResponse> getShortUrl(
            @Parameter(description = "단축 코드", required = true) @PathVariable String shortCode) {
        log.info("Getting shortened URL details for: {}", shortCode);
        ShortenedUrl shortenedUrl = urlShortenerService.getShortenedUrl(shortCode);
        UrlResponse response = UrlResponse.from(shortenedUrl, baseUrl);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all shortened URLs
     */
    @GetMapping("/api/urls")
    public ResponseEntity<List<UrlResponse>> getAllUrls() {
        log.info("Getting all shortened URLs");
        List<ShortenedUrl> urls = urlShortenerService.getAllUrls();
        List<UrlResponse> responses = urls.stream()
                .map(url -> UrlResponse.from(url, baseUrl))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    /**
     * Delete shortened URL
     */
    @DeleteMapping("/api/urls/{shortCode}")
    public ResponseEntity<Void> deleteShortUrl(@PathVariable String shortCode) {
        log.info("Deleting shortened URL: {}", shortCode);
        urlShortenerService.deleteShortenedUrl(shortCode);
        return ResponseEntity.noContent().build();
    }

    /**
     * Redirect to original URL
     */
    @Operation(summary = "단축 URL 리디렉션", description = "단축 코드로 원본 URL로 리디렉션합니다. 클릭 이벤트가 자동으로 기록됩니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "리디렉션 성공"),
        @ApiResponse(responseCode = "404", description = "URL을 찾을 수 없음")
    })
    @GetMapping("/{shortCode}")
    public void redirectToOriginalUrl(
            @Parameter(description = "단축 코드", required = true) @PathVariable String shortCode,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        log.info("Redirecting short code: {}", shortCode);
        String originalUrl = urlShortenerService.getOriginalUrlAndRecordClick(shortCode, request);
        response.sendRedirect(originalUrl);
    }

    /**
     * Generate QR code for shortened URL
     */
    @Operation(summary = "QR 코드 생성", description = "단축 URL의 QR 코드를 PNG 이미지로 생성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "QR 코드 생성 성공", content = @Content(mediaType = "image/png")),
        @ApiResponse(responseCode = "404", description = "URL을 찾을 수 없음")
    })
    @GetMapping("/api/urls/{shortCode}/qr")
    public ResponseEntity<byte[]> generateQrCode(
            @Parameter(description = "단축 코드", required = true) @PathVariable String shortCode,
            @Parameter(description = "QR 코드 너비 (픽셀)") @RequestParam(defaultValue = "300") int width,
            @Parameter(description = "QR 코드 높이 (픽셀)") @RequestParam(defaultValue = "300") int height) throws WriterException, IOException {
        log.info("Generating QR code for: {}", shortCode);
        
        // Verify that the URL exists
        urlShortenerService.getShortenedUrl(shortCode);
        
        String shortUrl = baseUrl + "/" + shortCode;
        byte[] qrCode = qrCodeService.generateQrCode(shortUrl, width, height);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(qrCode.length);
        headers.set("Content-Disposition", "inline; filename=qr-" + shortCode + ".png");

        return ResponseEntity.ok()
                .headers(headers)
                .body(qrCode);
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/api/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}

