package com.ticketing.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ticket_status_tbl", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Integer statusId;

    @Column(name = "status_name", nullable = false, unique = true)
    private String statusName;

    @Column(name = "description")
    private String description;
}