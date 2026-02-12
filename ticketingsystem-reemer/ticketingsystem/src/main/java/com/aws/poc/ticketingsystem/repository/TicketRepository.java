package com.aws.poc.ticketingsystem.repository;

import com.aws.poc.ticketingsystem.entity.TicketTbl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<TicketTbl, Integer> {

    @Query(value = "SELECT COUNT(*) from ticket_tbl t WHERE t.STATUS LIKE %:status%", nativeQuery = true)
    int countTicketsByStatus(@Param("status") String status);
}
