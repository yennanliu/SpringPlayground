-- MySQL initialization script for HotelApp
-- This script is automatically executed when the MySQL container starts

-- Create the database if it doesn't exist (redundant but safe)
CREATE DATABASE IF NOT EXISTS hoteldb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Use the database
USE hoteldb;

-- Root user already has all privileges, no additional grants needed

-- Optional: Create some initial configuration
SET GLOBAL sql_mode = 'STRICT_TRANS_TABLES,NO_ZERO_DATE,NO_ZERO_IN_DATE,ERROR_FOR_DIVISION_BY_ZERO';