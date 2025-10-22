package com.urlshortener.service;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple User-Agent parser for extracting browser, OS, and device information
 */
@Service
public class UserAgentParser {

    private static final Pattern BROWSER_PATTERN = Pattern.compile(
            "(Chrome|Firefox|Safari|Edge|Opera|MSIE|Trident)[/\\s]([\\d.]+)", 
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern OS_PATTERN = Pattern.compile(
            "(Windows|Mac OS X|Linux|Android|iOS|iPhone OS)[\\s/]?([\\d._]+)?", 
            Pattern.CASE_INSENSITIVE
    );

    public String parseBrowser(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return "Unknown";
        }

        Matcher matcher = BROWSER_PATTERN.matcher(userAgent);
        if (matcher.find()) {
            String browser = matcher.group(1);
            if ("MSIE".equalsIgnoreCase(browser) || "Trident".equalsIgnoreCase(browser)) {
                return "Internet Explorer";
            }
            return browser;
        }

        return "Unknown";
    }

    public String parseOperatingSystem(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return "Unknown";
        }

        Matcher matcher = OS_PATTERN.matcher(userAgent);
        if (matcher.find()) {
            String os = matcher.group(1);
            if ("iPhone OS".equalsIgnoreCase(os)) {
                return "iOS";
            }
            return os;
        }

        return "Unknown";
    }

    public String parseDeviceType(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return "Unknown";
        }

        String lowerUA = userAgent.toLowerCase();

        if (lowerUA.contains("mobile") || lowerUA.contains("android") || 
            lowerUA.contains("iphone") || lowerUA.contains("ipod")) {
            return "Mobile";
        } else if (lowerUA.contains("tablet") || lowerUA.contains("ipad")) {
            return "Tablet";
        } else {
            return "Desktop";
        }
    }
}

