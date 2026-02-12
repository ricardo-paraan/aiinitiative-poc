package com.aws.poc.ticketingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aws.poc.ticketingsystem.entity.TicketCommentsTbl;
import com.aws.poc.ticketingsystem.entity.TicketTbl;

import java.util.List;

/**
 * Repository interface for Ticket Comments entity operations これはチケットコメントエンティティ操作のためのリポジトリインターフェースです
 * Provides methods to manage and query ticket comments
 * チケットコメントを管理および照会するメソッドを提供します
 */
public interface TicketCommentsRepository extends JpaRepository<TicketCommentsTbl, Integer> {
    
    /**
     * Find all comments for a specific ticket 特定のチケットのすべてのコメントを検索します
     * @param ticket The ticket entity チケットエンティティ
     * @return List of comments for the ticket チケットのコメントリスト
     */
    List<TicketCommentsTbl> findByTicket(TicketTbl ticket);
    
    /**
     * Find all comments for a specific ticket ID, ordered by creation date 作成日順で特定のチケットIDのすべてのコメントを検索します
     * Returns comments in descending order (newest first)
     * コメントを降順（最新順）で返します
     * @param ticketId The ID of the ticket チケットのID
     * @return List of comments ordered by creation date 作成日順のコメントリスト
     */
    @Query("SELECT c FROM TicketCommentsTbl c WHERE c.ticket.ticketId = :ticketId ORDER BY c.createdDate DESC")
    List<TicketCommentsTbl> findByTicketIdOrderByCreatedDateDesc(@Param("ticketId") Integer ticketId);
    
    /**
     * Count comments for a specific ticket 特定のチケットのコメント数をカウントします
     * @param ticketId The ID of the ticket チケットのID
     * @return Total count of comments チケットのコメント総数
     */
    @Query("SELECT COUNT(c) FROM TicketCommentsTbl c WHERE c.ticket.ticketId = :ticketId")
    Long countByTicketId(@Param("ticketId") Integer ticketId);
}
