package com.aws.poc.ticketingsystem.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aws.poc.ticketingsystem.entity.TicketSystemsTbl;
import com.aws.poc.ticketingsystem.service.TicketSystemsService;

/**
 * Controller for managing ticket systems operations チケットシステム操作を管理するコントローラー
 * Provides REST API endpoints for retrieving ticket systems information
 * チケットシステム情報を取得するためのREST APIエンドポイントを提供します
 */
@Controller
public class TicketSystemsController {

    // Service for ticket systems operations チケットシステム操作のためのサービス
    private TicketSystemsService ticketSystemsService;
    
    /**
     * Constructor for TicketSystemsController TicketSystemsControllerのコンストラクタ
     * @param ticketSystemsService Service for ticket systems operations チケットシステム操作のためのサービス
     */
    public TicketSystemsController(TicketSystemsService ticketSystemsService) {
        this.ticketSystemsService = ticketSystemsService;
    }

    /**
     * API endpoint to retrieve all ticket systems すべてのチケットシステムを取得するAPIエンドポイント
     * @return List of all ticket systems すべてのチケットシステムのリスト
     */
    @GetMapping("/api/tickets-systems")
    @ResponseBody
    public List<TicketSystemsTbl> findAll() {
        return ticketSystemsService.findAll();
    }
}
