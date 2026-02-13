package com.fullstack.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity tracking historical status changes for tickets with timestamps and user information.
 * タイムスタンプとユーザー情報を含むチケットの履歴ステータス変更を追跡するエンティティ。
 */
@Entity
@Table(name = "status_history")
public class StatusHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;
    
    @Column(name = "status", nullable = false)
    private String status;
    
    @Column(name = "status_comment", length = 255)
    private String statusComment;
    
    @Column(name = "changed_by")
    private String changedBy;
    
    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;
    
    // Constructors
    public StatusHistory() {
        this.changedAt = LocalDateTime.now();
    }
    
    public StatusHistory(Ticket ticket, String status, String statusComment, String changedBy) {
        this.ticket = ticket;
        this.status = status;
        this.statusComment = statusComment;
        this.changedBy = changedBy;
        this.changedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Ticket getTicket() {
        return ticket;
    }
    
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getStatusComment() {
        return statusComment;
    }
    
    public void setStatusComment(String statusComment) {
        this.statusComment = statusComment;
    }
    
    public String getChangedBy() {
        return changedBy;
    }
    
    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }
    
    public LocalDateTime getChangedAt() {
        return changedAt;
    }
    
    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }
}