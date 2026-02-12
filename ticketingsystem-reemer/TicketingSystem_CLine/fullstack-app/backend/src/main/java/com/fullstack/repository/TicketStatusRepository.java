package com.fullstack.repository;

import com.fullstack.entity.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TicketStatusRepository extends JpaRepository<TicketStatus, Integer> {
    
    // Find status by name
    Optional<TicketStatus> findByStatus(String status);
}