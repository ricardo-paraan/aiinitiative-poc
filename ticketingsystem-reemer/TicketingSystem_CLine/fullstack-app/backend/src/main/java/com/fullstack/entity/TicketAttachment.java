package com.fullstack.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ticket_attachments_tbl")
public class TicketAttachment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachment_id")
    private Integer attachmentId;
    
    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;
    
    @Column(name = "file_path", length = 100)
    private String filePath;
    
    // Constructors
    public TicketAttachment() {}
    
    public TicketAttachment(Ticket ticket, String filePath) {
        this.ticket = ticket;
        this.filePath = filePath;
    }
    
    // Getters and Setters
    public Integer getAttachmentId() {
        return attachmentId;
    }
    
    public void setAttachmentId(Integer attachmentId) {
        this.attachmentId = attachmentId;
    }
    
    public Ticket getTicket() {
        return ticket;
    }
    
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}