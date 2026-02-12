package com.fullstack.repository;

import com.fullstack.entity.TicketComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketCommentRepository extends JpaRepository<TicketComment, Integer> {
    
    // Find comments by ticket ID
    List<TicketComment> findByTicket_TicketId(Integer ticketId);
}