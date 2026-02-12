package com.aws.poc.ticketingsystem.service;

import java.util.List;

import com.aws.poc.ticketingsystem.entity.TicketStatusTbl;

/**
 * Service interface for Ticket Status operations これはチケットステータス操作のためのサービスインターフェースです
 * Manages available ticket statuses in the system
 * システム内で利用可能なチケットステータスを管理します
 */
public interface TicketStatusService {

    /**
     * Find all ticket statuses すべてのチケットステータスを検索します
     * @return List of all available ticket statuses 利用可能なすべてのチケットステータスのリスト
     */
    List<TicketStatusTbl> findAll();
    
}
