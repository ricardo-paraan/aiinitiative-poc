package com.aws.poc.ticketingsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aws.poc.ticketingsystem.entity.TicketStatusTbl;
import com.aws.poc.ticketingsystem.repository.TicketStatusRepository;

/**
 * Implementation of TicketStatusService interface TicketStatusServiceインターフェースの実装
 * Manages available ticket status options in the system
 * システム内で利用可能なチケットステータスオプションを管理します
 */
@Service
public class TicketStatusServiceImpl implements TicketStatusService {

    // Repository for ticket status operations チケットステータス操作のためのリポジトリ
    private TicketStatusRepository ticketStatusRepo;

    /**
     * Constructor with dependency injection 依存性注入を使用したコンストラクタ
     * @param ticketStatusRepository The ticket status repository チケットステータスリポジトリ
     */
    @Autowired
    public TicketStatusServiceImpl(TicketStatusRepository ticketStatusRepository) {
        this.ticketStatusRepo = ticketStatusRepository;
    }

    /**
     * Find all ticket statuses すべてのチケットステータスを検索します
     * @return List of all available ticket statuses 利用可能なすべてのチケットステータスのリスト
     */
    @Override
    public List<TicketStatusTbl> findAll() {
        return ticketStatusRepo.findAll();
    }
    
}
