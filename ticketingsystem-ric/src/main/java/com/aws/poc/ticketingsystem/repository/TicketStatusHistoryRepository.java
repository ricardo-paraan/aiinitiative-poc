package com.aws.poc.ticketingsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aws.poc.ticketingsystem.entity.TicketStatusHistoryTbl;

/**
 * Repository interface for Ticket Status History entity operations これはチケットステータス履歴エンティティ操作のためのリポジトリインターフェースです
 * Manages the history of status changes for tickets
 * チケットのステータス変更履歴を管理します
 */
public interface TicketStatusHistoryRepository extends JpaRepository<TicketStatusHistoryTbl, Integer> {
    
    /**
     * Get the latest status for a specific ticket チケットの最新ステータスを取得します
     * Returns the most recent status update entry
     * 最新のステータス更新エントリを返します
     * @param ticketId The ID of the ticket チケットのID
     * @return The latest status history entry 最新のステータス履歴エントリ
     */
    @Query("SELECT s FROM TicketStatusHistoryTbl s WHERE s.ticket.ticketId = :ticketId ORDER BY s.updateDate DESC LIMIT 1")
    TicketStatusHistoryTbl getLatestStatus(@Param("ticketId") Integer ticketId);
    
    /**
     * Find all status history entries for a specific ticket 特定のチケットのすべてのステータス履歴エントリを検索します
     * Returns entries ordered by update date in descending order
     * 更新日の降順でエントリを返します
     * @param ticketId The ID of the ticket チケットのID
     * @return List of status history entries ordered by update date 更新日順のステータス履歴エントリリスト
     */
    @Query("SELECT s FROM TicketStatusHistoryTbl s WHERE s.ticket.ticketId = :ticketId ORDER BY s.updateDate DESC")
    List<TicketStatusHistoryTbl> findByTicketIdOrderByUpdateDateDesc(@Param("ticketId") Integer ticketId);
}
