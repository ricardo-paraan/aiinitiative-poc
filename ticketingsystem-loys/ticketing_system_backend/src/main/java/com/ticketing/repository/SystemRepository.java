package com.ticketing.repository;

import com.ticketing.model.TicketSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemRepository extends JpaRepository<TicketSystem, Integer> {

    // Find system by name
    Optional<TicketSystem> findBySystemName(String systemName);
}