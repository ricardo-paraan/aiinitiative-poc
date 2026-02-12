package com.aws.poc.ticketingsystem.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aws.poc.ticketingsystem.entity.TicketCommentsTbl;
import com.aws.poc.ticketingsystem.entity.TicketTbl;
import com.aws.poc.ticketingsystem.repository.TicketCommentsRepository;
import com.aws.poc.ticketingsystem.repository.TicketRepository;

/**
 * Implementation of TicketCommentsService interface TicketCommentsServiceインターフェースの実装
 * Handles all business logic related to ticket comments
 * チケットコメントに関連するすべてのビジネスロジックを処理します
 */
@Service
public class TicketCommentsServiceImpl implements TicketCommentsService {

    // Repository for ticket comments operations チケットコメント操作のためのリポジトリ
    @Autowired
    private TicketCommentsRepository ticketCommentsRepository;
    
    // Repository for ticket operations チケット操作のためのリポジトリ
    @Autowired
    private TicketRepository ticketRepository;

    /**
     * Constructor with dependency injection 依存性注入を使用したコンストラクタ
     * @param ticketCommentsRepository The ticket comments repository チケットコメントリポジトリ
     * @param ticketRepository The ticket repository チケットリポジトリ
     */
    public TicketCommentsServiceImpl(TicketCommentsRepository ticketCommentsRepository, TicketRepository ticketRepository) {
        this.ticketCommentsRepository = ticketCommentsRepository;
        this.ticketRepository = ticketRepository;
    }

    /**
     * Find all comments すべてのコメントを検索します
     * @return List of all comments すべてのコメントのリスト
     */
    @Override
    public List<TicketCommentsTbl> findAll() {
        return ticketCommentsRepository.findAll();
    }

    /**
     * Save comment コメントを保存します
     * @param ticketCommentObj The comment object to save 保存するコメントオブジェクト
     * @return The saved comment entity 保存されたコメントエンティティ
     */
    @Override
    public TicketCommentsTbl saveComment(TicketCommentsTbl ticketCommentObj) {
        return ticketCommentsRepository.save(ticketCommentObj);
    }
    
    /**
     * Find comments by ticket ID チケットIDでコメントを検索します
     * @param ticketId The ID of the ticket チケットのID
     * @return List of comments ordered by creation date 作成日順のコメントリスト
     */
    @Override
    public List<TicketCommentsTbl> findCommentsByTicketId(Integer ticketId) {
        return ticketCommentsRepository.findByTicketIdOrderByCreatedDateDesc(ticketId);
    }
    
    /**
     * Count comments by ticket ID チケットIDでコメント数をカウントします
     * @param ticketId The ID of the ticket チケットのID
     * @return Total count of comments コメントの総数
     */
    @Override
    public Long countCommentsByTicketId(Integer ticketId) {
        return ticketCommentsRepository.countByTicketId(ticketId);
    }
    
    /**
     * Add comment to ticket チケットにコメントを追加します
     * Creates a new comment and associates it with the specified ticket
     * 新しいコメントを作成し、指定されたチケットに関連付けます
     * @param ticketId The ID of the ticket チケットのID
     * @param commentText The comment text コメントテキスト
     * @param author The author of the comment コメントの作成者
     * @return The created comment entity 作成されたコメントエンティティ
     * @throws RuntimeException if ticket not found チケットが見つからない場合
     */
    @Override
    @Transactional
    public TicketCommentsTbl addCommentToTicket(Integer ticketId, String commentText, String author) {
        // Find the ticket or throw exception チケットを検索するか、例外をスローします
        TicketTbl ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + ticketId));
        
        // Create new comment entity 新しいコメントエンティティを作成します
        TicketCommentsTbl comment = new TicketCommentsTbl();
        comment.setTicket(ticket);
        comment.setComments(commentText);
        comment.setAuthor(author);
        comment.setCreatedDate(LocalDateTime.now());
        
        // Save and return the comment コメントを保存して返します
        return ticketCommentsRepository.save(comment);
    }
}
