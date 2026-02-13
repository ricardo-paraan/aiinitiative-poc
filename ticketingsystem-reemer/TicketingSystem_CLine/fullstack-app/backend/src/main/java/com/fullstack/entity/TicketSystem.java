package com.fullstack.entity;

import jakarta.persistence.*;

/**
 * Entity representing available ticket systems or categories.
 * 利用可能なチケットシステムまたはカテゴリを表すエンティティ。
 */
@Entity
@Table(name = "ticket_systems_tbl")
public class TicketSystem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "system_id")
    private Integer systemId;
    
    @Column(name = "system_name", length = 50)
    private String systemName;
    
    // Constructors
    public TicketSystem() {}
    
    public TicketSystem(String systemName) {
        this.systemName = systemName;
    }
    
    // Getters and Setters
    public Integer getSystemId() {
        return systemId;
    }
    
    public void setSystemId(Integer systemId) {
        this.systemId = systemId;
    }
    
    public String getSystemName() {
        return systemName;
    }
    
    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }
}