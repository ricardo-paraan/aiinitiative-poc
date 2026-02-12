package com.aws.poc.ticketingsystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ticket_status_tbl")
public class TicketStatusTbl {

    public TicketStatusTbl() {
    }

    public TicketStatusTbl(int statusId, String status) {
        this.statusId = statusId;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="status_id")
    private int statusId;

    @Column(name="status")
    private String status;

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}