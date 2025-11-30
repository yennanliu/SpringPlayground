-- Initialize database for ChatAppV2
-- This script runs automatically when the PostgreSQL container is first created

-- The database 'chatapp' is already created by POSTGRES_DB env variable
-- This file can contain additional initialization SQL if needed

-- Create extensions if needed
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- You can add seed data here if needed
-- For example:
-- INSERT INTO users (username, email, display_name, created_at)
-- VALUES ('admin', 'admin@chatapp.com', 'Administrator', NOW());

-- Table creation is handled by Hibernate with ddl-auto: update
-- So we don't need to define tables here
