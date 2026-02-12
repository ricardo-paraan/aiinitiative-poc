package com.aws.poc.ticketingsystem.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aws.poc.ticketingsystem.entity.TicketStatusTbl;
import com.aws.poc.ticketingsystem.service.TicketStatusService;

/**
 * Controller for managing ticket status operations チケットステータス操作を管理するコントローラー
 * Provides REST API endpoints for retrieving ticket status information
 * チケットステータス情報を取得するためのREST APIエンドポイントを提供します
 */
@Controller
public class TicketStatusController {

    // Service for ticket status operations チケットステータス操作のためのサービス
    private TicketStatusService ticketStatusService;
    
    /**
     * Constructor for TicketStatusController TicketStatusControllerのコンストラクタ
     * @param ticketStatusService Service for ticket status operations チケットステータス操作のためのサービス
     */
    public TicketStatusController(TicketStatusService ticketStatusService) {
        this.ticketStatusService = ticketStatusService;
    }

    // REST API endpoints REST APIエンドポイント
    
    /**
     * API endpoint to retrieve all ticket statuses すべてのチケットステータスを取得するAPIエンドポイント
     * @return List of all ticket statuses すべてのチケットステータスのリスト
     */
    @GetMapping("/api/tickets-status")
    @ResponseBody
    public List<TicketStatusTbl> findAll() {
        return ticketStatusService.findAll();
    }

}
