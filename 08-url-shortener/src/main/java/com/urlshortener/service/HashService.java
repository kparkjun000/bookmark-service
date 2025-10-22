package com.urlshortener.service;

import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * Hash service for generating short codes using MurmurHash3 and Base62 encoding
 */
@Service
public class HashService {

    private static final String BASE62_ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE = 62;

    @Value("${app.short-code-length:6}")
    private int shortCodeLength;

    /**
     * Generate a short code from the original URL using MurmurHash3
     * 
     * @param originalUrl The original URL
     * @param counter Counter for collision resolution
     * @return Short code
     */
    public String generateShortCode(String originalUrl, int counter) {
        String input = originalUrl + counter;
        
        // Use MurmurHash3 128-bit hash
        long hash = Hashing.murmur3_128()
                .hashString(input, StandardCharsets.UTF_8)
                .asLong();

        // Make sure hash is positive
        hash = Math.abs(hash);

        // Convert to Base62
        return encodeBase62(hash);
    }

    /**
     * Encode a number to Base62 string
     * 
     * @param number The number to encode
     * @return Base62 encoded string
     */
    private String encodeBase62(long number) {
        if (number == 0) {
            return String.valueOf(BASE62_ALPHABET.charAt(0));
        }

        StringBuilder result = new StringBuilder();
        while (number > 0) {
            int remainder = (int) (number % BASE);
            result.append(BASE62_ALPHABET.charAt(remainder));
            number = number / BASE;
        }

        // Pad with zeros if needed
        while (result.length() < shortCodeLength) {
            result.append(BASE62_ALPHABET.charAt(0));
        }

        // Reverse the string
        String encoded = result.reverse().toString();

        // Ensure the length is correct
        if (encoded.length() > shortCodeLength) {
            encoded = encoded.substring(0, shortCodeLength);
        }

        return encoded;
    }

    /**
     * Validate if a string is a valid Base62 encoded string
     * 
     * @param shortCode The short code to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidShortCode(String shortCode) {
        if (shortCode == null || shortCode.isEmpty()) {
            return false;
        }

        for (char c : shortCode.toCharArray()) {
            if (BASE62_ALPHABET.indexOf(c) == -1) {
                return false;
            }
        }

        return true;
    }
}

