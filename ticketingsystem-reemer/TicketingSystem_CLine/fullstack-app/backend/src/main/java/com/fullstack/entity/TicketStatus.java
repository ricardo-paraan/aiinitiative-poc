package com.fullstack.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ticket_status_tbl")
public class TicketStatus {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Integer statusId;
    
    @Column(name = "status", length = 20)
    private String status;
    
    // Constructors
    public TicketStatus() {}
    
    public TicketStatus(String status) {
        this.status = status;
    }
    
    // Getters and Setters
    public Integer getStatusId() {
        return statusId;
    }
    
    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}