package com.ticketing.controller;

import com.ticketing.dto.AttachmentDTO;
import com.ticketing.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;

    // Get all attachments for a ticket
    @GetMapping("/tickets/{ticketId}/attachments")
    public ResponseEntity<List<AttachmentDTO>> getAttachmentsByTicketId(@PathVariable Integer ticketId) {
        List<AttachmentDTO> attachments = attachmentService.getAttachmentsByTicketId(ticketId);
        return ResponseEntity.ok(attachments);
    }

    // Upload attachment
    @PostMapping("/tickets/{ticketId}/attachments")
    public ResponseEntity<AttachmentDTO> uploadAttachment(
            @PathVariable Integer ticketId,
            @RequestParam("file") MultipartFile file) {
        AttachmentDTO attachment = attachmentService.uploadAttachment(ticketId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(attachment);
    }

    // Download attachment
    @GetMapping("/attachments/{attachmentId}/download")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable Integer attachmentId) {
        Resource resource = attachmentService.downloadAttachment(attachmentId);
        AttachmentDTO attachment = attachmentService.getAttachmentById(attachmentId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + attachment.getFileName() + "\"")
                .body(resource);
    }

    // Delete attachment
    @DeleteMapping("/attachments/{attachmentId}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable Integer attachmentId) {
        attachmentService.deleteAttachment(attachmentId);
        return ResponseEntity.noContent().build();
    }
}