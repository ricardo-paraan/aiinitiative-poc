package com.ticketing.repository;

import com.ticketing.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<TicketStatus, Integer> {

    // Find status by name
    Optional<TicketStatus> findByStatusName(String statusName);
}