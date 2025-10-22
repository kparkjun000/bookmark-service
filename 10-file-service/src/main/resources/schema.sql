-- File Metadata Table
CREATE TABLE IF NOT EXISTS file_metadata (
    id BIGSERIAL PRIMARY KEY,
    file_id VARCHAR(255) NOT NULL UNIQUE,
    original_file_name VARCHAR(255) NOT NULL,
    stored_file_name VARCHAR(255) NOT NULL,
    content_type VARCHAR(255) NOT NULL,
    file_size BIGINT NOT NULL,
    file_extension VARCHAR(50),
    file_path VARCHAR(1000),
    width INTEGER,
    height INTEGER,
    has_thumbnail BOOLEAN DEFAULT FALSE,
    thumbnail_path VARCHAR(1000),
    thumbnail_file_name VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    uploaded_by VARCHAR(255),
    description VARCHAR(1000),
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE'
);

-- Indexes for better performance
CREATE INDEX IF NOT EXISTS idx_file_id ON file_metadata(file_id);
CREATE INDEX IF NOT EXISTS idx_status ON file_metadata(status);
CREATE INDEX IF NOT EXISTS idx_created_at ON file_metadata(created_at);
CREATE INDEX IF NOT EXISTS idx_uploaded_by ON file_metadata(uploaded_by);

