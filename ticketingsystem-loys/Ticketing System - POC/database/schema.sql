-- Ticketing System Database Schema
-- PostgreSQL Database
-- Connection: localhost:5432, username: postgres, password: 12345678

-- Drop tables if they exist (in reverse order of dependencies)
DROP TABLE IF EXISTS ticket_attachments_tbl CASCADE;
DROP TABLE IF EXISTS ticket_comments_tbl CASCADE;
DROP TABLE IF EXISTS ticket_tbl CASCADE;
DROP TABLE IF EXISTS ticket_status_tbl CASCADE;
DROP TABLE IF EXISTS ticket_category_tbl CASCADE;
DROP TABLE IF EXISTS ticket_systems_tbl CASCADE;

-- Create reference tables first (no foreign key dependencies)

CREATE TABLE ticket_status_tbl (
    status_id SERIAL PRIMARY KEY,
    status_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE ticket_category_tbl (
    category_id SERIAL PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE ticket_systems_tbl (
    system_id SERIAL PRIMARY KEY,
    system_name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255)
);

-- Create main ticket table
CREATE TABLE ticket_tbl (
    ticket_id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(100) NOT NULL,
    system_id INTEGER REFERENCES ticket_systems_tbl(system_id),
    category_id INTEGER REFERENCES ticket_category_tbl(category_id),
    description TEXT,
    status_id INTEGER REFERENCES ticket_status_tbl(status_id),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create dependent tables
CREATE TABLE ticket_comments_tbl (
    comment_id SERIAL PRIMARY KEY,
    ticket_id INTEGER NOT NULL REFERENCES ticket_tbl(ticket_id) ON DELETE CASCADE,
    comment_text TEXT NOT NULL,
    author VARCHAR(100) NOT NULL
);

CREATE TABLE ticket_attachments_tbl (
    attachment_id SERIAL PRIMARY KEY,
    ticket_id INTEGER NOT NULL REFERENCES ticket_tbl(ticket_id) ON DELETE CASCADE,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT,
    content_type VARCHAR(100)
);

-- Create indexes for better query performance
CREATE INDEX idx_ticket_status ON ticket_tbl(status_id);
CREATE INDEX idx_ticket_category ON ticket_tbl(category_id);
CREATE INDEX idx_ticket_system ON ticket_tbl(system_id);
CREATE INDEX idx_ticket_created ON ticket_tbl(created_date);
CREATE INDEX idx_ticket_author ON ticket_tbl(author);
CREATE INDEX idx_comment_ticket ON ticket_comments_tbl(ticket_id);
CREATE INDEX idx_attachment_ticket ON ticket_attachments_tbl(ticket_id);

-- Insert default statuses
INSERT INTO ticket_status_tbl (status_name, description) VALUES
('Open', 'Ticket is newly created and awaiting assignment'),
('In Progress', 'Ticket is currently being worked on'),
('Resolved', 'Ticket issue has been resolved'),
('Closed', 'Ticket is closed and archived');

-- Insert sample categories
INSERT INTO ticket_category_tbl (category_name, description) VALUES
('Bug', 'Software bugs and errors'),
('Feature Request', 'New feature requests'),
('Support', 'General support inquiries'),
('Documentation', 'Documentation related issues'),
('Performance', 'Performance and optimization issues');

-- Insert sample systems
INSERT INTO ticket_systems_tbl (system_name, description) VALUES
('Web Application', 'Main web application system'),
('Mobile App', 'Mobile application system'),
('API', 'Backend API system'),
('Database', 'Database system'),
('Infrastructure', 'Infrastructure and deployment');

-- Create a function to automatically update the updated_date
CREATE OR REPLACE FUNCTION update_updated_date_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_date = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create trigger to auto-update updated_date on ticket updates
CREATE TRIGGER update_ticket_updated_date 
    BEFORE UPDATE ON ticket_tbl
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_date_column();

-- Verify tables were created
SELECT 'Tables created successfully!' AS status;

-- Show all tables
SELECT table_name 
FROM information_schema.tables 
WHERE table_schema = 'public' 
ORDER BY table_name;