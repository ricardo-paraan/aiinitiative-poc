package com.fullstack.repository;

import com.fullstack.entity.TicketAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketAttachmentRepository extends JpaRepository<TicketAttachment, Integer> {
    
    // Find attachments by ticket ID
    List<TicketAttachment> findByTicket_TicketId(Integer ticketId);
}