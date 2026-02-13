package com.fullstack.service;

import com.fullstack.entity.StatusHistory;
import com.fullstack.entity.Ticket;
import com.fullstack.repository.StatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for tracking and retrieving ticket status change history.
 * チケットステータス変更履歴の追跡と取得のサービス層。
 */
@Service
public class StatusHistoryService {
    
    @Autowired
    private StatusHistoryRepository statusHistoryRepository;
    
    /**
     * Retrieves status history ordered by most recent first.
     * 最新順に並べられたステータス履歴を取得します。
     */
    public List<StatusHistory> getStatusHistoryByTicketId(Integer ticketId) {
        return statusHistoryRepository.findByTicketTicketIdOrderByChangedAtDesc(ticketId);
    }
    
    /**
     * Creates new status history entry with automatic timestamp.
     * 自動タイムスタンプ付きの新しいステータス履歴エントリを作成します。
     */
    public StatusHistory createStatusHistory(Ticket ticket, String status, String statusComment, String changedBy) {
        StatusHistory history = new StatusHistory(ticket, status, statusComment, changedBy);
        return statusHistoryRepository.save(history);
    }
    
    public StatusHistory saveStatusHistory(StatusHistory statusHistory) {
        return statusHistoryRepository.save(statusHistory);
    }
}