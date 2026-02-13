package com.fullstack.controller;

import com.fullstack.entity.TicketAttachment;
import com.fullstack.service.TicketAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for ticket attachment management.
 * チケット添付ファイル管理のRESTコントローラー。
 */
@RestController
@RequestMapping("/api/attachments")
@CrossOrigin(origins = "http://localhost:3000")
public class TicketAttachmentController {
    
    @Autowired
    private TicketAttachmentService ticketAttachmentService;
    
    // Get all attachments
    @GetMapping
    public ResponseEntity<List<TicketAttachment>> getAllAttachments() {
        List<TicketAttachment> attachments = ticketAttachmentService.getAllAttachments();
        return ResponseEntity.ok(attachments);
    }
    
    // Get attachment by ID
    @GetMapping("/{id}")
    public ResponseEntity<TicketAttachment> getAttachmentById(@PathVariable Integer id) {
        Optional<TicketAttachment> attachment = ticketAttachmentService.getAttachmentById(id);
        return attachment.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Retrieves all attachments for a specific ticket.
     * 特定のチケットのすべての添付ファイルを取得します。
     */
    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<TicketAttachment>> getAttachmentsByTicketId(@PathVariable Integer ticketId) {
        List<TicketAttachment> attachments = ticketAttachmentService.getAttachmentsByTicketId(ticketId);
        return ResponseEntity.ok(attachments);
    }
    
    // Create new attachment
    @PostMapping
    public ResponseEntity<TicketAttachment> createAttachment(@RequestBody TicketAttachment attachment) {
        TicketAttachment createdAttachment = ticketAttachmentService.createAttachment(attachment);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAttachment);
    }
    
    // Update attachment
    @PutMapping("/{id}")
    public ResponseEntity<TicketAttachment> updateAttachment(@PathVariable Integer id, @RequestBody TicketAttachment attachment) {
        TicketAttachment updatedAttachment = ticketAttachmentService.updateAttachment(id, attachment);
        if (updatedAttachment != null) {
            return ResponseEntity.ok(updatedAttachment);
        }
        return ResponseEntity.notFound().build();
    }
    
    // Delete attachment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable Integer id) {
        boolean deleted = ticketAttachmentService.deleteAttachment(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}