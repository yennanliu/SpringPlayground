-- This file will be executed by Spring Boot on startup to initialize data
-- It creates sample data for testing the ticket system

-- Insert sample ticket statuses into option_schema if they don't exist
INSERT IGNORE INTO option_schema (column_name, schema_name, active) VALUES
('PENDING', 'ticket_status', true),
('CREATED', 'ticket_status', true),
('REVIEWING', 'ticket_status', true),
('APPROVED', 'ticket_status', true),
('REJECTED', 'ticket_status', true),
('CANCELLED', 'ticket_status', true);

-- Insert sample ticket tags into option_schema if they don't exist
INSERT IGNORE INTO option_schema (column_name, schema_name, active) VALUES
('Bug Report', 'ticket_tag', true),
('Feature Request', 'ticket_tag', true),
('Technical Support', 'ticket_tag', true),
('HR Request', 'ticket_tag', true),
('IT Support', 'ticket_tag', true),
('General Inquiry', 'ticket_tag', true);

-- Create sample tickets if tickets table is empty
INSERT IGNORE INTO tickets (id, subject, description, user_id, assigned_to, status, tag, created_at, updated_at) VALUES
(1, 'Sample IT Support Ticket', 'This is a sample ticket for IT support request to test the system functionality.', 1, 2, 'PENDING', 'IT Support', NOW(), NOW()),
(2, 'Feature Request Example', 'Sample feature request ticket to demonstrate the ticketing system capabilities.', 2, 1, 'REVIEWING', 'Feature Request', NOW(), NOW()); 