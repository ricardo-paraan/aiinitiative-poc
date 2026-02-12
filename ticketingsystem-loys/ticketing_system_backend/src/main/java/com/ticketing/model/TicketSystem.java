package com.ticketing.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ticket_systems_tbl", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "system_id")
    private Integer systemId;

    @Column(name = "system_name", nullable = false, unique = true)
    private String systemName;

    @Column(name = "description")
    private String description;
}