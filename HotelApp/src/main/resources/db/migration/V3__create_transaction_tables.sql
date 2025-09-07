-- Create transaction_records table for tracking overall transaction state
CREATE TABLE transaction_records (
    transaction_id VARCHAR(255) PRIMARY KEY,
    state VARCHAR(50) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NULL,
    timeout_time TIMESTAMP NOT NULL,
    participant_ids TEXT NOT NULL,
    transaction_data TEXT NULL,
    initiator_id VARCHAR(255) NULL,
    error_message TEXT NULL,
    INDEX idx_transaction_state (state),
    INDEX idx_transaction_start_time (start_time),
    INDEX idx_transaction_timeout (timeout_time)
);

-- Create transaction_log table for detailed transaction logging
CREATE TABLE transaction_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_id VARCHAR(255) NOT NULL,
    participant_id VARCHAR(255) NOT NULL,
    phase VARCHAR(50) NOT NULL,
    participant_state VARCHAR(50) NOT NULL,
    transaction_state VARCHAR(50) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    payload TEXT NULL,
    error_message TEXT NULL,
    INDEX idx_transaction_id (transaction_id),
    INDEX idx_participant_id (participant_id),
    INDEX idx_phase (phase),
    INDEX idx_timestamp (timestamp),
    FOREIGN KEY (transaction_id) REFERENCES transaction_records(transaction_id)
);

-- Create prepared_transactions table for tracking participant prepared states
CREATE TABLE prepared_transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_id VARCHAR(255) NOT NULL,
    participant_id VARCHAR(255) NOT NULL,
    prepared_data TEXT NULL,
    prepared_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    INDEX idx_prepared_transaction (transaction_id),
    INDEX idx_prepared_participant (participant_id),
    INDEX idx_prepared_expires (expires_at),
    UNIQUE KEY unique_transaction_participant (transaction_id, participant_id),
    FOREIGN KEY (transaction_id) REFERENCES transaction_records(transaction_id)
);

-- Add transaction tracking to existing tables
ALTER TABLE rooms ADD COLUMN prepared_transaction_id VARCHAR(255) NULL;
ALTER TABLE rooms ADD INDEX idx_room_prepared_transaction (prepared_transaction_id);

ALTER TABLE bookings ADD COLUMN prepared_transaction_id VARCHAR(255) NULL;
ALTER TABLE bookings ADD INDEX idx_booking_prepared_transaction (prepared_transaction_id);