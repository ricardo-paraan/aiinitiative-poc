package com.aws.poc.ticketingsystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ticket_systems_tbl")
public class TicketSystemsTbl {

    public TicketSystemsTbl() {
    }

    public TicketSystemsTbl(int systemId, String systemName) {
        this.systemId = systemId;
        this.systemName = systemName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="system_id")
    private int systemId;

    @Column(name="system_name")
    private String systemName;

    public int getSystemId() {
        return systemId;
    }

    public void setSystemId(int systemId) {
        this.systemId = systemId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }
}