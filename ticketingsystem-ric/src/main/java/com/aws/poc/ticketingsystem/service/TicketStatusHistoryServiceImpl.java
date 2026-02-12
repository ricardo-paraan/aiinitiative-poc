package com.aws.poc.ticketingsystem.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aws.poc.ticketingsystem.entity.TicketStatusHistoryTbl;
import com.aws.poc.ticketingsystem.entity.TicketTbl;
import com.aws.poc.ticketingsystem.repository.TicketRepository;
import com.aws.poc.ticketingsystem.repository.TicketStatusHistoryRepository;

import jakarta.transaction.Transactional;

/**
 * Implementation of TicketStatusHistoryService interface TicketStatusHistoryServiceインターフェースの実装
 * Manages the complete lifecycle of ticket status history
 * チケットステータス履歴の完全なライフサイクルを管理します
 */
@Service
public class TicketStatusHistoryServiceImpl implements TicketStatusHistoryService {

    // Repository for status history operations ステータス履歴操作のためのリポジトリ
    @Autowired
    private TicketStatusHistoryRepository ticketStatusHistoryRepo;

    // Repository for ticket operations チケット操作のためのリポジトリ
    @Autowired
    private TicketRepository ticketRepository;

    /**
     * Constructor with dependency injection 依存性注入を使用したコンストラクタ
     * @param ticketStatusHistoryRepo The status history repository ステータス履歴リポジトリ
     */
    @Autowired
    public TicketStatusHistoryServiceImpl(TicketStatusHistoryRepository ticketStatusHistoryRepo) {
        this.ticketStatusHistoryRepo = ticketStatusHistoryRepo;
    }

    /**
     * Find all status history entries すべてのステータス履歴エントリを検索します
     * @return List of all status history entries すべてのステータス履歴エントリのリスト
     */
    @Override
    public List<TicketStatusHistoryTbl> findAll() {
        return ticketStatusHistoryRepo.findAll();
    }

    /**
     * Add status history to ticket チケットにステータス履歴を追加します
     * Creates a new status history entry for the ticket
     * チケットの新しいステータス履歴エントリを作成します
     * @param ticketId The ID of the ticket チケットのID
     * @param status The new status 新しいステータス
     * @return The created status history entry 作成されたステータス履歴エントリ
     * @throws RuntimeException if ticket not found チケットが見つからない場合
     */
    @Transactional
    @Override
    public TicketStatusHistoryTbl addStatusHistoryToTicket(Integer ticketId, String status) {
        // Find the ticket or throw exception チケットを検索するか、例外をスローします
        TicketTbl ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + ticketId));
        
            // Create new status history entry 新しいステータス履歴エントリを作成します
            TicketStatusHistoryTbl statusHistory = new TicketStatusHistoryTbl();
            statusHistory.setTicket(ticket);
            statusHistory.setStatus(status);
            statusHistory.setUpdateDate(LocalDateTime.now());
            // Save and return the status history ステータス履歴を保存して返します
            return ticketStatusHistoryRepo.save(statusHistory);
    }

    /**
     * Get latest status 最新のステータスを取得します
     * Retrieves the most recent status update for the ticket
     * チケットの最新のステータス更新を取得します
     * @param ticketId The ID of the ticket チケットのID
     * @return The latest status history entry 最新のステータス履歴エントリ
     * @throws RuntimeException if ticket not found チケットが見つからない場合
     */
    @Override
    public TicketStatusHistoryTbl getLatestStatus(Integer ticketId) {
        // Verify ticket exists チケットが存在することを確認します
        TicketTbl ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + ticketId));

            // Return the latest status entry 最新のステータスエントリを返します
            return ticketStatusHistoryRepo.getLatestStatus(ticket.getTicketId());
    }
    
    /**
     * Get status history by ticket ID チケットIDでステータス履歴を取得します
     * @param ticketId The ID of the ticket チケットのID
     * @return List of status history entries ordered by update date 更新日順のステータス履歴エントリリスト
     */
    @Override
    public List<TicketStatusHistoryTbl> getStatusHistoryByTicketId(Integer ticketId) {
        return ticketStatusHistoryRepo.findByTicketIdOrderByUpdateDateDesc(ticketId);
    }
    
}
