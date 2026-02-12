package com.fullstack.repository;

import com.fullstack.entity.TicketSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TicketSystemRepository extends JpaRepository<TicketSystem, Integer> {
    
    // Find system by name
    Optional<TicketSystem> findBySystemName(String systemName);
}