package com.fullstack.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "ticket_tbl")
@JsonIgnoreProperties({"attachments", "comments"})
public class Ticket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Integer ticketId;
    
    @Column(name = "title", length = 100)
    private String title;
    
    @Column(name = "author", length = 50)
    private String author;
    
    @Column(name = "system_name", length = 50)
    private String systemName;
    
    @Column(name = "category", length = 50)
    private String category;
    
    @Column(name = "description", length = 1000)
    private String description;
    
    @Column(name = "attachment_id")
    private Integer attachmentId;
    
    @Column(name = "status", length = 20)
    private String status;
    
    @Column(name = "comment_id")
    private Integer commentId;
    
    @Column(name = "created_date")
    private LocalDate createdDate;
    
    @Column(name = "update_date")
    private LocalDate updateDate;
    
    @Column(name = "status_comment", length = 255)
    private String statusComment;
    
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<TicketAttachment> attachments;
    
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<TicketComment> comments;
    
    // Constructors
    public Ticket() {}
    
    // Getters and Setters
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
    
    public Integer getAttachmentId() {
        return attachmentId;
    }
    
    public void setAttachmentId(Integer attachmentId) {
        this.attachmentId = attachmentId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Integer getCommentId() {
        return commentId;
    }
    
    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }
    
    public LocalDate getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
    
    public LocalDate getUpdateDate() {
        return updateDate;
    }
    
    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }
    
    public List<TicketAttachment> getAttachments() {
        return attachments;
    }
    
    public void setAttachments(List<TicketAttachment> attachments) {
        this.attachments = attachments;
    }
    
    public List<TicketComment> getComments() {
        return comments;
    }
    
    public void setComments(List<TicketComment> comments) {
        this.comments = comments;
    }
    
    public String getStatusComment() {
        return statusComment;
    }
    
    public void setStatusComment(String statusComment) {
        this.statusComment = statusComment;
    }
}
