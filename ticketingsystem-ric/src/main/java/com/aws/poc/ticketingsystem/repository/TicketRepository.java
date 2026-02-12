package com.aws.poc.ticketingsystem.repository;

import com.aws.poc.ticketingsystem.entity.TicketTbl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for Ticket entity operations これはチケットエンティティ操作のためのリポジトリインターフェースです
 * Extends JpaRepository to provide CRUD operations for TicketTbl entity
 * JpaRepositoryを継承してTicketTblエンティティのCRUD操作を提供します
 */
public interface TicketRepository extends JpaRepository<TicketTbl, Integer> {

    /**
     * Count tickets by status ステータス別にチケット数をカウントします
     * @param status The status to search for 検索するステータス
     * @return Count of tickets matching the status ステータスに一致するチケットの数
     */
    @Query(value = "SELECT COUNT(*) from ticket_tbl t WHERE t.STATUS LIKE %:status%", nativeQuery = true)
    int countTicketsByStatus(@Param("status") String status);

    /**
     * Get ticket details with comments チケット詳細とコメントを取得します
     * Retrieves ticket information along with associated comments
     * チケット情報と関連コメントを取得します
     * @param ticket_id The ID of the ticket チケットのID
     * @return List of ticket details with comments コメント付きチケット詳細のリスト
     */
    @Query(value = "select a.ticket_id, a.title, a.author, a.system_name, a.category, a.description, a.status, a.attachment_id, a.comment_id, a.created_date, a.update_date, b.comments from ticket_tbl a left join ticket_comments_tbl b on a.ticket_id = b.ticket_id where a.ticket_id = :ticket_id", nativeQuery = true)
    List<TicketTbl> ticketDetailsList(@Param("ticket_id") Integer ticket_id);
}
