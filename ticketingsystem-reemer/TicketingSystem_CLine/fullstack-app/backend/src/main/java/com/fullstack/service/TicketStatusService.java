package com.fullstack.service;

import com.fullstack.entity.TicketStatus;
import com.fullstack.repository.TicketStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for ticket status management operations.
 * チケットステータス管理操作のサービス層。
 */
@Service
public class TicketStatusService {
    
    @Autowired
    private TicketStatusRepository ticketStatusRepository;
    
    // Get all statuses
    public List<TicketStatus> getAllStatuses() {
        return ticketStatusRepository.findAll();
    }
    
    // Get status by ID
    public Optional<TicketStatus> getStatusById(Integer id) {
        return ticketStatusRepository.findById(id);
    }
    
    // Get status by name
    public Optional<TicketStatus> getStatusByName(String status) {
        return ticketStatusRepository.findByStatus(status);
    }
    
    // Create new status
    public TicketStatus createStatus(TicketStatus status) {
        return ticketStatusRepository.save(status);
    }
    
    // Update status
    public TicketStatus updateStatus(Integer id, TicketStatus statusDetails) {
        Optional<TicketStatus> status = ticketStatusRepository.findById(id);
        if (status.isPresent()) {
            TicketStatus existingStatus = status.get();
            existingStatus.setStatus(statusDetails.getStatus());
            return ticketStatusRepository.save(existingStatus);
        }
        return null;
    }
    
    // Delete status
    public boolean deleteStatus(Integer id) {
        if (ticketStatusRepository.existsById(id)) {
            ticketStatusRepository.deleteById(id);
            return true;
        }
        return false;
    }
}