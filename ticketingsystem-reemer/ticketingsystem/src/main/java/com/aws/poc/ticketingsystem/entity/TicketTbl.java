package com.aws.poc.ticketingsystem.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "ticket_tbl")
public class TicketTbl {

    public TicketTbl() {
    }

    public TicketTbl(Integer ticketId, String title, String author, String systemName, String category, String description, int attachmentId, String status, int commentId, LocalDateTime createdDate, LocalDateTime updateDate) {
        this.ticketId = ticketId;
        this.title = title;
        this.author = author;
        this.systemName = systemName;
        this.category = category;
        this.description = description;
        this.attachmentId = attachmentId;
        this.status = status;
        this.commentId = commentId;
        this.createdDate = createdDate;
        this.updateDate = updateDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_seq")
    @SequenceGenerator(name = "ticket_seq", sequenceName = "ticket_tbl_ticket_id_seq", allocationSize = 1)
    @Column(name="ticket_id")
    private Integer ticketId;

    @Column(name="title")
    private String title;

    @Column(name="author")
    private String author;

    @Column(name="system_name")
    private String systemName;

    @Column(name="category")
    private String category;

    @Column(name="description")
    private String description;

    @Column(name="attachment_id")
    private int attachmentId;

    @Column(name="status")
    private String status;

    @Column(name="comment_id")
    private int commentId;

    @Column(name="created_date")
    private LocalDateTime createdDate;

    @Column(name="update_date")
    private LocalDateTime updateDate;

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(int attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}