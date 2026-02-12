-- PostgreSQL Database Setup for Ticket System
-- Run this if you encounter sequence issues

-- Create sequence if it doesn't exist
CREATE SEQUENCE IF NOT EXISTS ticket_tbl_ticket_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Ensure the sequence is owned by the ticket_id column
ALTER SEQUENCE ticket_tbl_ticket_id_seq OWNED BY ticket_tbl.ticket_id;

-- Set the sequence to start from the next available ID
SELECT setval('ticket_tbl_ticket_id_seq', COALESCE((SELECT MAX(ticket_id) FROM ticket_tbl), 0) + 1, false);

-- Verify the sequence
SELECT * FROM ticket_tbl_ticket_id_seq;