package com.ticketing.service;

import com.ticketing.dto.*;
import com.ticketing.model.*;
import com.ticketing.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SystemRepository systemRepository;

    // Get all tickets
    public List<TicketDTO> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get ticket by ID
    public TicketDTO getTicketById(Integer id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));
        return convertToDTO(ticket);
    }

    // Create new ticket
    public TicketDTO createTicket(TicketCreateDTO createDTO) {
        Ticket ticket = new Ticket();
        ticket.setTitle(createDTO.getTitle());
        ticket.setAuthor(createDTO.getAuthor());
        ticket.setDescription(createDTO.getDescription());

        // Set relationships
        TicketStatus status = statusRepository.findById(createDTO.getStatusId())
                .orElseThrow(() -> new RuntimeException("Status not found"));
        ticket.setStatus(status);

        TicketCategory category = categoryRepository.findById(createDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        ticket.setCategory(category);

        TicketSystem system = systemRepository.findById(createDTO.getSystemId())
                .orElseThrow(() -> new RuntimeException("System not found"));
        ticket.setSystem(system);

        Ticket savedTicket = ticketRepository.save(ticket);
        return convertToDTO(savedTicket);
    }

    // Update ticket
    public TicketDTO updateTicket(Integer id, TicketUpdateDTO updateDTO) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));

        if (updateDTO.getTitle() != null) {
            ticket.setTitle(updateDTO.getTitle());
        }
        if (updateDTO.getDescription() != null) {
            ticket.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getStatusId() != null) {
            TicketStatus status = statusRepository.findById(updateDTO.getStatusId())
                    .orElseThrow(() -> new RuntimeException("Status not found"));
            ticket.setStatus(status);
        }
        if (updateDTO.getCategoryId() != null) {
            TicketCategory category = categoryRepository.findById(updateDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            ticket.setCategory(category);
        }
        if (updateDTO.getSystemId() != null) {
            TicketSystem system = systemRepository.findById(updateDTO.getSystemId())
                    .orElseThrow(() -> new RuntimeException("System not found"));
            ticket.setSystem(system);
        }

        Ticket updatedTicket = ticketRepository.save(ticket);
        return convertToDTO(updatedTicket);
    }

    // Delete ticket
    public void deleteTicket(Integer id) {
        if (!ticketRepository.existsById(id)) {
            throw new RuntimeException("Ticket not found with id: " + id);
        }
        ticketRepository.deleteById(id);
    }

    // Search tickets
    public List<TicketDTO> searchTickets(String keyword) {
        return ticketRepository.searchByKeyword(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Filter tickets
    public List<TicketDTO> filterTickets(Integer statusId, Integer categoryId, Integer systemId, String author) {
        return ticketRepository.findByFilters(statusId, categoryId, systemId, author).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get tickets by status
    public List<TicketDTO> getTicketsByStatus(Integer statusId) {
        return ticketRepository.findByStatus_StatusId(statusId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get tickets by category
    public List<TicketDTO> getTicketsByCategory(Integer categoryId) {
        return ticketRepository.findByCategory_CategoryId(categoryId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get tickets by system
    public List<TicketDTO> getTicketsBySystem(Integer systemId) {
        return ticketRepository.findBySystem_SystemId(systemId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Convert Entity to DTO
    private TicketDTO convertToDTO(Ticket ticket) {
        TicketDTO dto = new TicketDTO();
        dto.setTicketId(ticket.getTicketId());
        dto.setTitle(ticket.getTitle());
        dto.setAuthor(ticket.getAuthor());
        dto.setDescription(ticket.getDescription());
        dto.setCreatedDate(ticket.getCreatedDate());
        dto.setUpdatedDate(ticket.getUpdatedDate());

        if (ticket.getStatus() != null) {
            dto.setStatusId(ticket.getStatus().getStatusId());
            dto.setStatusName(ticket.getStatus().getStatusName());
        }

        if (ticket.getCategory() != null) {
            dto.setCategoryId(ticket.getCategory().getCategoryId());
            dto.setCategoryName(ticket.getCategory().getCategoryName());
        }

        if (ticket.getSystem() != null) {
            dto.setSystemId(ticket.getSystem().getSystemId());
            dto.setSystemName(ticket.getSystem().getSystemName());
        }

        // Convert comments
        if (ticket.getComments() != null) {
            List<CommentDTO> comments = ticket.getComments().stream()
                    .map(comment -> new CommentDTO(
                            comment.getCommentId(),
                            ticket.getTicketId(),
                            comment.getCommentText(),
                            comment.getAuthor()
                    ))
                    .collect(Collectors.toList());
            dto.setComments(comments);
        }

        // Convert attachments
        if (ticket.getAttachments() != null) {
            List<AttachmentDTO> attachments = ticket.getAttachments().stream()
                    .map(attachment -> new AttachmentDTO(
                            attachment.getAttachmentId(),
                            ticket.getTicketId(),
                            attachment.getFileName(),
                            attachment.getFilePath(),
                            attachment.getFileSize(),
                            attachment.getContentType()
                    ))
                    .collect(Collectors.toList());
            dto.setAttachments(attachments);
        }

        return dto;
    }
}