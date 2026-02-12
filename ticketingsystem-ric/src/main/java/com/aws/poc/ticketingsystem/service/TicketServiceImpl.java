package com.aws.poc.ticketingsystem.service;

import com.aws.poc.ticketingsystem.entity.TicketTbl;
import com.aws.poc.ticketingsystem.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of TicketService interface TicketServiceインターフェースの実装
 * Provides business logic for ticket management operations
 * チケット管理操作のためのビジネスロジックを提供します
 */
@Service
public class TicketServiceImpl implements TicketService {

    // Repository for ticket database operations チケットデータベース操作のためのリポジトリ
    private TicketRepository ticketRepo;

    /**
     * Constructor with dependency injection 依存性注入を使用したコンストラクタ
     * @param ticketRepos The ticket repository チケットリポジトリ
     */
    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepos) {
        this.ticketRepo = ticketRepos;
    }

    /**
     * Find all tickets すべてのチケットを検索します
     * @return List of all tickets すべてのチケットのリスト
     */
    @Override
    public List<TicketTbl> findAll() {
        return ticketRepo.findAll();
    }

    /**
     * Find ticket by ID IDでチケットを検索します
     * @param ticketId The ID of the ticket チケットのID
     * @return The ticket entity チケットエンティティ
     * @throws RuntimeException if ticket not found チケットが見つからない場合
     */
    @Override
    public TicketTbl findByTicketId(int ticketId) {
        // Try to find the ticket by ID IDでチケットを検索しようとします
        Optional<TicketTbl> result = ticketRepo.findById(ticketId);

        TicketTbl ticketObj = null;

        if (result.isPresent()) {
            ticketObj = result.get();
        } else {
            // Throw exception if ticket not found チケットが見つからない場合は例外をスローします
            throw new RuntimeException("Cannot find ticket with an id: " + ticketId);
        }

        return ticketObj;
    }

    /**
     * Save or update ticket チケットを保存または更新します
     * @param ticketObj The ticket object to save 保存するチケットオブジェクト
     * @return The saved ticket entity 保存されたチケットエンティティ
     */
    @Override
    public TicketTbl saveTicket(TicketTbl ticketObj) {
        return ticketRepo.save(ticketObj);
    }

    /**
     * Delete ticket チケットを削除します
     * @param ticketId The ticket to delete 削除するチケット
     */
    @Override
    public void deleteTicket(TicketTbl ticketId) {
        ticketRepo.delete(ticketId);
    }

    /**
     * Get count of tickets by status ステータス別にチケット数を取得します
     * @param status The status to filter by フィルタリングするステータス
     * @return Count of tickets with the specified status 指定されたステータスのチケット数
     */
    @Override
    public int getTicketsBystatus(String status) {
        return ticketRepo.countTicketsByStatus(status);
    }

    /**
     * Get ticket details list チケット詳細リストを取得します
     * @param ticketId The ID of the ticket チケットのID
     * @return List of ticket details チケット詳細のリスト
     */
    @Override
    public List<TicketTbl> ticketDetailsList(Integer ticketId) {
        return ticketRepo.ticketDetailsList(ticketId);
    }
}
