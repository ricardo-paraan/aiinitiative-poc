package com.aws.poc.ticketingsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aws.poc.ticketingsystem.entity.TicketSystemsTbl;
import com.aws.poc.ticketingsystem.repository.TicketSystemsRepository;

/**
 * Implementation of TicketSystemsService interface TicketSystemsServiceインターフェースの実装
 * Manages different systems that tickets can be categorized under
 * チケットを分類できる異なるシステムを管理します
 */
@Service
public class TicketSystemsServiceImpl implements TicketSystemsService {

    // Repository for ticket systems operations チケットシステム操作のためのリポジトリ
    private TicketSystemsRepository ticketSystemsRepository;

    /**
     * Constructor with dependency injection 依存性注入を使用したコンストラクタ
     * @param ticketSystemsRepository The ticket systems repository チケットシステムリポジトリ
     */
    @Autowired
    public TicketSystemsServiceImpl(TicketSystemsRepository ticketSystemsRepository) {
        this.ticketSystemsRepository = ticketSystemsRepository;
    }

    /**
     * Find all ticket systems すべてのチケットシステムを検索します
     * @return List of all available systems 利用可能なすべてのシステムのリスト
     */
    @Override
    public List<TicketSystemsTbl> findAll() {
        return ticketSystemsRepository.findAll();
    }
    
}
