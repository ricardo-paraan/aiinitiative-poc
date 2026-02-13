package com.fullstack.service;

import com.fullstack.entity.Ticket;
import com.fullstack.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for ticket business logic with automatic status history tracking.
 * 自動ステータス履歴追跡機能を持つチケットビジネスロジックのサービス層。
 */
@Service
public class TicketService {
    
    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private StatusHistoryService statusHistoryService;
    
    // Get all tickets
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
    
    // Get ticket by ID
    public Optional<Ticket> getTicketById(Integer id) {
        return ticketRepository.findById(id);
    }
    
    /**
     * Creates ticket and records initial status in history.
     * チケットを作成し、初期ステータスを履歴に記録します。
     */
    public Ticket createTicket(Ticket ticket) {
        ticket.setCreatedDate(LocalDate.now());
        ticket.setUpdateDate(LocalDate.now());
        Ticket savedTicket = ticketRepository.save(ticket);
        
        statusHistoryService.createStatusHistory(
            savedTicket,
            savedTicket.getStatus(),
            savedTicket.getStatusComment(),
            savedTicket.getAuthor()
        );
        
        return savedTicket;
    }
    
    /**
     * Updates ticket and creates history entry when status or comment changes.
     * チケットを更新し、ステータスまたはコメントが変更された場合は履歴エントリを作成します。
     */
    public Ticket updateTicket(Integer id, Ticket ticketDetails) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (ticket.isPresent()) {
            Ticket existingTicket = ticket.get();
            
            boolean statusChanged = !existingTicket.getStatus().equals(ticketDetails.getStatus()) ||
                                   (existingTicket.getStatusComment() == null && ticketDetails.getStatusComment() != null) ||
                                   (existingTicket.getStatusComment() != null && !existingTicket.getStatusComment().equals(ticketDetails.getStatusComment()));
            
            existingTicket.setTitle(ticketDetails.getTitle());
            existingTicket.setAuthor(ticketDetails.getAuthor());
            existingTicket.setSystemName(ticketDetails.getSystemName());
            existingTicket.setCategory(ticketDetails.getCategory());
            existingTicket.setDescription(ticketDetails.getDescription());
            existingTicket.setStatus(ticketDetails.getStatus());
            existingTicket.setStatusComment(ticketDetails.getStatusComment());
            existingTicket.setUpdateDate(LocalDate.now());
            Ticket savedTicket = ticketRepository.save(existingTicket);
            
            // Create status history entry if status or comment changed
            if (statusChanged) {
                statusHistoryService.createStatusHistory(
                    savedTicket,
                    savedTicket.getStatus(),
                    savedTicket.getStatusComment(),
                    savedTicket.getAuthor()
                );
            }
            
            return savedTicket;
        }
        return null;
    }
    
    // Delete ticket
    public boolean deleteTicket(Integer id) {
        if (ticketRepository.existsById(id)) {
            ticketRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Find tickets by author
    public List<Ticket> getTicketsByAuthor(String author) {
        return ticketRepository.findByAuthor(author);
    }
    
    // Find tickets by status
    public List<Ticket> getTicketsByStatus(String status) {
        return ticketRepository.findByStatus(status);
    }
    
    // Find tickets by system name
    public List<Ticket> getTicketsBySystemName(String systemName) {
        return ticketRepository.findBySystemName(systemName);
    }
    
    // Find tickets by category
    public List<Ticket> getTicketsByCategory(String category) {
        return ticketRepository.findByCategory(category);
    }
    
    // Search tickets by title
    public List<Ticket> searchTicketsByTitle(String title) {
        return ticketRepository.findByTitleContainingIgnoreCase(title);
    }
}