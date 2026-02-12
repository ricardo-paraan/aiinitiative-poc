package com.aws.poc.ticketingsystem.service;

import com.aws.poc.ticketingsystem.entity.TicketTbl;

import java.util.List;

/**
 * Service interface for Ticket operations これはチケット操作のためのサービスインターフェースです
 * Defines business logic methods for managing tickets
 * チケット管理のためのビジネスロジックメソッドを定義します
 */
public interface TicketService {

    /**
     * Find all tickets すべてのチケットを検索します
     * @return List of all tickets すべてのチケットのリスト
     */
    List<TicketTbl> findAll();

    /**
     * Find ticket by ID IDでチケットを検索します
     * @param ticketId The ID of the ticket チケットのID
     * @return The ticket entity チケットエンティティ
     */
    TicketTbl findByTicketId(int ticketId);

    /**
     * Save or update ticket チケットを保存または更新します
     * @param ticketObj The ticket object to save 保存するチケットオブジェクト
     * @return The saved ticket entity 保存されたチケットエンティティ
     */
    TicketTbl saveTicket(TicketTbl ticketObj);

    /**
     * Delete ticket チケットを削除します
     * @param ticketId The ticket to delete 削除するチケット
     */
    void deleteTicket(TicketTbl ticketId);

    /**
     * Get count of tickets by status ステータス別にチケット数を取得します
     * @param status The status to filter by フィルタリングするステータス
     * @return Count of tickets with the specified status 指定されたステータスのチケット数
     */
    int getTicketsBystatus(String status);

    /**
     * Get ticket details list チケット詳細リストを取得します
     * @param ticketId The ID of the ticket チケットのID
     * @return List of ticket details チケット詳細のリスト
     */
    List<TicketTbl> ticketDetailsList(Integer ticketId);
}
