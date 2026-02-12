package com.aws.poc.ticketingsystem.service;

import java.util.List;

import com.aws.poc.ticketingsystem.entity.TicketSystemsTbl;

/**
 * Service interface for Ticket Systems operations これはチケットシステム操作のためのサービスインターフェースです
 * Manages different systems that tickets can be categorized under
 * チケットを分類できる異なるシステムを管理します
 */
public interface TicketSystemsService {
    
    /**
     * Find all ticket systems すべてのチケットシステムを検索します
     * @return List of all available systems 利用可能なすべてのシステムのリスト
     */
    List<TicketSystemsTbl> findAll();
}
