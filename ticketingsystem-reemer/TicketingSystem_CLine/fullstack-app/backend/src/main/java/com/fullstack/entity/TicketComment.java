package com.fullstack.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_comments_tbl")
public class TicketComment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer commentId;
    
    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;
    
    @Column(name = "comments", length = 500)
    private String comments;
    
    @Column(name = "author", length = 100)
    private String author;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Constructors
    public TicketComment() {}
    
    public TicketComment(Ticket ticket, String comments, String author) {
        this.ticket = ticket;
        this.comments = comments;
        this.author = author;
        this.createdAt = LocalDateTime.now();
    }
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
    
    // Getters and Setters
    public Integer getCommentId() {
        return commentId;
    }
    
    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }
    
    public Ticket getTicket() {
        return ticket;
    }
    
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
    
    public String getComments() {
        return comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
