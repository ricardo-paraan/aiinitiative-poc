package com.ticketing.service;

import com.ticketing.dto.AttachmentDTO;
import com.ticketing.model.Ticket;
import com.ticketing.model.TicketAttachment;
import com.ticketing.repository.AttachmentRepository;
import com.ticketing.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AttachmentService {

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Value("${file.upload-dir:./uploads}")
    private String uploadDir;

    // Get all attachments for a ticket
    public List<AttachmentDTO> getAttachmentsByTicketId(Integer ticketId) {
        return attachmentRepository.findByTicket_TicketId(ticketId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Upload attachment
    public AttachmentDTO uploadAttachment(Integer ticketId, MultipartFile file) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + ticketId));

        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = "";
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex > 0) {
                fileExtension = originalFilename.substring(dotIndex);
            }
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

            // Save file
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Save attachment record
            TicketAttachment attachment = new TicketAttachment();
            attachment.setTicket(ticket);
            attachment.setFileName(originalFilename);
            attachment.setFilePath(filePath.toString());
            attachment.setFileSize(file.getSize());
            attachment.setContentType(file.getContentType());

            TicketAttachment savedAttachment = attachmentRepository.save(attachment);
            return convertToDTO(savedAttachment);

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage());
        }
    }

    // Download attachment
    public Resource downloadAttachment(Integer attachmentId) {
        TicketAttachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new RuntimeException("Attachment not found with id: " + attachmentId));

        try {
            Path filePath = Paths.get(attachment.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not found or not readable: " + attachment.getFileName());
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("File not found: " + attachment.getFileName());
        }
    }

    // Get attachment info
    public AttachmentDTO getAttachmentById(Integer attachmentId) {
        TicketAttachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new RuntimeException("Attachment not found with id: " + attachmentId));
        return convertToDTO(attachment);
    }

    // Delete attachment
    public void deleteAttachment(Integer attachmentId) {
        TicketAttachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new RuntimeException("Attachment not found with id: " + attachmentId));

        try {
            // Delete file from filesystem
            Path filePath = Paths.get(attachment.getFilePath());
            Files.deleteIfExists(filePath);

            // Delete record from database
            attachmentRepository.deleteById(attachmentId);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + e.getMessage());
        }
    }

    // Convert Entity to DTO
    private AttachmentDTO convertToDTO(TicketAttachment attachment) {
        return new AttachmentDTO(
                attachment.getAttachmentId(),
                attachment.getTicket().getTicketId(),
                attachment.getFileName(),
                attachment.getFilePath(),
                attachment.getFileSize(),
                attachment.getContentType()
        );
    }
}