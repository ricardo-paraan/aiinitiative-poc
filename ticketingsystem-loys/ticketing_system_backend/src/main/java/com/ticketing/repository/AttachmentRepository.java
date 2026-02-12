package com.ticketing.repository;

import com.ticketing.model.TicketAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<TicketAttachment, Integer> {

    // Find all attachments for a specific ticket
    List<TicketAttachment> findByTicket_TicketId(Integer ticketId);
}