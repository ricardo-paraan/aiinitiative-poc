package com.fullstack.repository;

import com.fullstack.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    
    // Find tickets by author
    List<Ticket> findByAuthor(String author);
    
    // Find tickets by status
    List<Ticket> findByStatus(String status);
    
    // Find tickets by system name
    List<Ticket> findBySystemName(String systemName);
    
    // Find tickets by category
    List<Ticket> findByCategory(String category);
    
    // Find tickets by title containing (search)
    List<Ticket> findByTitleContainingIgnoreCase(String title);
}