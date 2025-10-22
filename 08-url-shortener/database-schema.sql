-- URL Shortener Database Schema
-- This schema is automatically created by JPA, but you can use this for reference or manual setup

-- Shortened URLs Table
CREATE TABLE IF NOT EXISTS shortened_urls (
    id BIGSERIAL PRIMARY KEY,
    short_code VARCHAR(10) NOT NULL UNIQUE,
    original_url VARCHAR(2048) NOT NULL,
    click_count BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    expires_at TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT true,
    custom_alias VARCHAR(50),
    title VARCHAR(200),
    description VARCHAR(500)
);

-- Indexes for shortened_urls
CREATE INDEX IF NOT EXISTS idx_short_code ON shortened_urls(short_code);
CREATE INDEX IF NOT EXISTS idx_created_at ON shortened_urls(created_at);
CREATE INDEX IF NOT EXISTS idx_expires_at ON shortened_urls(expires_at);

-- Click Events Table
CREATE TABLE IF NOT EXISTS click_events (
    id BIGSERIAL PRIMARY KEY,
    short_code VARCHAR(10) NOT NULL,
    ip_address VARCHAR(45),
    user_agent VARCHAR(500),
    referer VARCHAR(500),
    country VARCHAR(100),
    city VARCHAR(100),
    browser VARCHAR(100),
    operating_system VARCHAR(100),
    device_type VARCHAR(50),
    clicked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for click_events
CREATE INDEX IF NOT EXISTS idx_click_short_code ON click_events(short_code);
CREATE INDEX IF NOT EXISTS idx_clicked_at ON click_events(clicked_at);
CREATE INDEX IF NOT EXISTS idx_short_code_clicked ON click_events(short_code, clicked_at);

-- Useful queries for analytics

-- Total clicks by short code
-- SELECT short_code, COUNT(*) as total_clicks 
-- FROM click_events 
-- GROUP BY short_code 
-- ORDER BY total_clicks DESC;

-- Clicks by date
-- SELECT DATE(clicked_at) as date, COUNT(*) as clicks 
-- FROM click_events 
-- WHERE short_code = 'abc123' 
-- GROUP BY DATE(clicked_at) 
-- ORDER BY date;

-- Top browsers
-- SELECT browser, COUNT(*) as count 
-- FROM click_events 
-- WHERE short_code = 'abc123' 
-- GROUP BY browser 
-- ORDER BY count DESC;

-- Top operating systems
-- SELECT operating_system, COUNT(*) as count 
-- FROM click_events 
-- WHERE short_code = 'abc123' 
-- GROUP BY operating_system 
-- ORDER BY count DESC;

-- Active URLs
-- SELECT * FROM shortened_urls 
-- WHERE is_active = true 
-- AND (expires_at IS NULL OR expires_at > CURRENT_TIMESTAMP);

-- Expired URLs
-- SELECT * FROM shortened_urls 
-- WHERE expires_at < CURRENT_TIMESTAMP;

