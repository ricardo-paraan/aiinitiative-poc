package com.aws.poc.ticketingsystem.service;

import java.util.List;

import com.aws.poc.ticketingsystem.entity.TicketStatusHistoryTbl;

/**
 * Service interface for Ticket Status History operations これはチケットステータス履歴操作のためのサービスインターフェースです
 * Manages the history and tracking of ticket status changes
 * チケットステータス変更の履歴と追跡を管理します
 */
public interface TicketStatusHistoryService {
    
    /**
     * Find all status history entries すべてのステータス履歴エントリを検索します
     * @return List of all status history entries すべてのステータス履歴エントリのリスト
     */
    List<TicketStatusHistoryTbl> findAll();

    /**
     * Add status history to ticket チケットにステータス履歴を追加します
     * @param ticketId The ID of the ticket チケットのID
     * @param status The new status 新しいステータス
     * @return The created status history entry 作成されたステータス履歴エントリ
     */
    TicketStatusHistoryTbl addStatusHistoryToTicket(Integer ticketId, String status);

    /**
     * Get latest status 最新のステータスを取得します
     * @param ticketId The ID of the ticket チケットのID
     * @return The latest status history entry 最新のステータス履歴エントリ
     */
    TicketStatusHistoryTbl getLatestStatus(Integer ticketId);
    
    /**
     * Get status history by ticket ID チケットIDでステータス履歴を取得します
     * @param ticketId The ID of the ticket チケットのID
     * @return List of status history entries ステータス履歴エントリのリスト
     */
    List<TicketStatusHistoryTbl> getStatusHistoryByTicketId(Integer ticketId);
}
