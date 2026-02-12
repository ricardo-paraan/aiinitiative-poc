-- Ticket Management System Database Schema

CREATE TABLE ticket_tbl (
	ticket_id INT PRIMARY KEY,
	title VARCHAR(100),
	author VARCHAR(50),
	system_name VARCHAR(50),
	category VARCHAR(50),
	description VARCHAR(1000),
	attachment_id INT,
	status VARCHAR(20),
	comment_id INT,
	created_date DATE,
	update_date DATE,
	status_comment VARCHAR(255)
);

CREATE TABLE ticket_systems_tbl (
	system_id INT PRIMARY KEY,
	system_name VARCHAR(50)
);

CREATE TABLE ticket_status_tbl (
	status_id INT PRIMARY KEY,
	status VARCHAR(20)
);

CREATE TABLE ticket_attachments_tbl (
	attachment_id INT PRIMARY KEY,
	ticket_id INT,
	file_path VARCHAR(100),
	FOREIGN KEY (ticket_id) REFERENCES ticket_tbl(ticket_id)
);

CREATE TABLE ticket_comments_tbl (
	comment_id INT PRIMARY KEY AUTO_INCREMENT,
	ticket_id INT,
	comments VARCHAR(500),
	author VARCHAR(100),
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (ticket_id) REFERENCES ticket_tbl(ticket_id)
);

CREATE TABLE status_history (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
	ticket_id INT NOT NULL,
	status VARCHAR(20) NOT NULL,
	status_comment VARCHAR(255),
	changed_by VARCHAR(50),
	changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (ticket_id) REFERENCES ticket_tbl(ticket_id)
);
