package com.aws.poc.ticketingsystem.service;

import java.util.List;

import com.aws.poc.ticketingsystem.entity.TicketCommentsTbl;

/**
 * Service interface for Ticket Comments operations これはチケットコメント操作のためのサービスインターフェースです
 * Provides business logic for managing ticket comments
 * チケットコメント管理のためのビジネスロジックを提供します
 */
public interface TicketCommentsService {
    
    /**
     * Find all comments すべてのコメントを検索します
     * @return List of all comments すべてのコメントのリスト
     */
    List<TicketCommentsTbl> findAll();

    /**
     * Save comment コメントを保存します
     * @param ticketCommentObj The comment object to save 保存するコメントオブジェクト
     * @return The saved comment entity 保存されたコメントエンティティ
     */
    TicketCommentsTbl saveComment(TicketCommentsTbl ticketCommentObj);
    
    /**
     * Find comments by ticket ID チケットIDでコメントを検索します
     * @param ticketId The ID of the ticket チケットのID
     * @return List of comments for the ticket チケットのコメントリスト
     */
    List<TicketCommentsTbl> findCommentsByTicketId(Integer ticketId);
    
    /**
     * Count comments by ticket ID チケットIDでコメント数をカウントします
     * @param ticketId The ID of the ticket チケットのID
     * @return Total count of comments コメントの総数
     */
    Long countCommentsByTicketId(Integer ticketId);
    
    /**
     * Add comment to ticket チケットにコメントを追加します
     * @param ticketId The ID of the ticket チケットのID
     * @param commentText The comment text コメントテキスト
     * @param author The author of the comment コメントの作成者
     * @return The created comment entity 作成されたコメントエンティティ
     */
    TicketCommentsTbl addCommentToTicket(Integer ticketId, String commentText, String author);
}
