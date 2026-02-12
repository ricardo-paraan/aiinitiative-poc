package com.fullstack.service;

import com.fullstack.entity.StatusHistory;
import com.fullstack.entity.Ticket;
import com.fullstack.repository.StatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusHistoryService {
    
    @Autowired
    private StatusHistoryRepository statusHistoryRepository;
    
    public List<StatusHistory> getStatusHistoryByTicketId(Integer ticketId) {
        return statusHistoryRepository.findByTicketTicketIdOrderByChangedAtDesc(ticketId);
    }
    
    public StatusHistory createStatusHistory(Ticket ticket, String status, String statusComment, String changedBy) {
        StatusHistory history = new StatusHistory(ticket, status, statusComment, changedBy);
        return statusHistoryRepository.save(history);
    }
    
    public StatusHistory saveStatusHistory(StatusHistory statusHistory) {
        return statusHistoryRepository.save(statusHistory);
    }
}