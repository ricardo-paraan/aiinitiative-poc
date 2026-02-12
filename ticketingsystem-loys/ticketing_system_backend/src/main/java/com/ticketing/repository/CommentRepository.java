package com.ticketing.repository;

import com.ticketing.model.TicketComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<TicketComment, Integer> {

    // Find all comments for a specific ticket
    List<TicketComment> findByTicket_TicketId(Integer ticketId);

    // Find comments by author
    List<TicketComment> findByAuthor(String author);
}