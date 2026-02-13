package com.fullstack.service;

import com.fullstack.entity.TicketAttachment;
import com.fullstack.repository.TicketAttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for ticket attachment management operations.
 * チケット添付ファイル管理操作のサービス層。
 */
@Service
public class TicketAttachmentService {
    
    @Autowired
    private TicketAttachmentRepository ticketAttachmentRepository;
    
    // Get all attachments
    public List<TicketAttachment> getAllAttachments() {
        return ticketAttachmentRepository.findAll();
    }
    
    // Get attachment by ID
    public Optional<TicketAttachment> getAttachmentById(Integer id) {
        return ticketAttachmentRepository.findById(id);
    }
    
    // Get attachments by ticket ID
    public List<TicketAttachment> getAttachmentsByTicketId(Integer ticketId) {
        return ticketAttachmentRepository.findByTicket_TicketId(ticketId);
    }
    
    // Create new attachment
    public TicketAttachment createAttachment(TicketAttachment attachment) {
        return ticketAttachmentRepository.save(attachment);
    }
    
    // Update attachment
    public TicketAttachment updateAttachment(Integer id, TicketAttachment attachmentDetails) {
        Optional<TicketAttachment> attachment = ticketAttachmentRepository.findById(id);
        if (attachment.isPresent()) {
            TicketAttachment existingAttachment = attachment.get();
            existingAttachment.setFilePath(attachmentDetails.getFilePath());
            return ticketAttachmentRepository.save(existingAttachment);
        }
        return null;
    }
    
    // Delete attachment
    public boolean deleteAttachment(Integer id) {
        if (ticketAttachmentRepository.existsById(id)) {
            ticketAttachmentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}