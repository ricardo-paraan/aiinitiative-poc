package com.aws.poc.ticketingsystem.controller;


import com.aws.poc.ticketingsystem.entity.TicketCommentsTbl;
import com.aws.poc.ticketingsystem.entity.TicketStatusHistoryTbl;
import com.aws.poc.ticketingsystem.entity.TicketTbl;
import com.aws.poc.ticketingsystem.service.TicketCommentsService;
import com.aws.poc.ticketingsystem.service.TicketService;
import com.aws.poc.ticketingsystem.service.TicketStatusHistoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for managing ticket operations チケット操作を管理するコントローラー
 * Handles both web views and REST API endpoints for ticket management
 * チケット管理のためのWebビューとREST APIエンドポイントの両方を処理します
 */
@Controller
public class TicketController {

    // Service for ticket operations チケット操作のためのサービス
    private TicketService ticketService;
    
    // Service for ticket comments operations チケットコメント操作のためのサービス
    @Autowired
    private TicketCommentsService ticketCommentsService;

    // Service for ticket status history operations チケットステータス履歴操作のためのサービス
    @Autowired
    private TicketStatusHistoryService ticketStatusHistoryService;

    /**
     * Constructor for TicketController TicketControllerのコンストラクタ
     * @param ticketService Service for ticket operations チケット操作のためのサービス
     */
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // View mappings ビューマッピング
    
    /**
     * Display the main index page メインインデックスページを表示
     * @return View name for index page インデックスページのビュー名
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * Display the create ticket form 新規チケット作成フォームを表示
     * @param model Model to hold ticket data チケットデータを保持するモデル
     * @return View name for create ticket page チケット作成ページのビュー名
     */
    @GetMapping("/tickets/create")
    public String showCreateForm(Model model) {
        model.addAttribute("ticket", new TicketTbl());
        return "create-ticket";
    }

    /**
     * Display the edit ticket form チケット編集フォームを表示
     * @param ticketId ID of the ticket to edit 編集するチケットのID
     * @param model Model to hold ticket data チケットデータを保持するモデル
     * @return View name for edit ticket page チケット編集ページのビュー名
     */
    @GetMapping("/tickets/edit/{ticketId}")
    public String showEditForm(@PathVariable int ticketId, Model model) {
        TicketTbl ticket = ticketService.findByTicketId(ticketId);
        model.addAttribute("ticket", ticket);
        return "edit-ticket";
    }

    /**
     * Display ticket details page チケット詳細ページを表示
     * @param ticketId ID of the ticket to display 表示するチケットのID
     * @param model Model to hold ticket, comments, and status history data チケット、コメント、ステータス履歴データを保持するモデル
     * @return View name for ticket details page チケット詳細ページのビュー名
     */
    @GetMapping("/tickets/{ticketId}")
    public String showTicketDetails(@PathVariable int ticketId, Model model) {
        TicketTbl ticket = ticketService.findByTicketId(ticketId);
        List<TicketCommentsTbl> comments = ticketCommentsService.findCommentsByTicketId(ticketId);
        List<TicketStatusHistoryTbl> statusHistory = ticketStatusHistoryService.getStatusHistoryByTicketId(ticketId);
        model.addAttribute("ticket", ticket);
        model.addAttribute("comments", comments);
        model.addAttribute("statusHistory", statusHistory);
        return "ticket-details";
    }

    // REST API endpoints REST APIエンドポイント
    
    /**
     * API endpoint to retrieve all tickets すべてのチケットを取得するAPIエンドポイント
     * @return List of all tickets すべてのチケットのリスト
     */
    @GetMapping("/api/tickets")
    @ResponseBody
    public List<TicketTbl> findAll() {
        return ticketService.findAll();
    }

    /**
     * API endpoint to retrieve a specific ticket by ID IDで特定のチケットを取得するAPIエンドポイント
     * @param ticketId ID of the ticket to retrieve 取得するチケットのID
     * @return Ticket object チケットオブジェクト
     */
    @GetMapping("/api/tickets/{ticketId}")
    @ResponseBody
    public TicketTbl getTicketById(@PathVariable int ticketId) {
        return ticketService.findByTicketId(ticketId);
    }

    /**
     * API endpoint to retrieve ticket comments list チケットコメントリストを取得するAPIエンドポイント
     * @param ticketId ID of the ticket コメントを取得するチケットのID
     * @return List of tickets with comments コメント付きチケットのリスト
     */
    @GetMapping("/api/tickets/{ticketId}/comments")
    @ResponseBody
    public List<TicketTbl> ticketCommentsList(@PathVariable int ticketId) {
        TicketTbl ticketIdObj = ticketService.findByTicketId(ticketId);
        return ticketService.ticketDetailsList(ticketIdObj.getTicketId());
    }

    /**
     * API endpoint to create a new ticket 新しいチケットを作成するAPIエンドポイント
     * @param ticket Ticket object to create 作成するチケットオブジェクト
     * @return Created ticket object 作成されたチケットオブジェクト
     */
    @PostMapping("/api/tickets")
    @ResponseBody
    public TicketTbl createTicket(@RequestBody TicketTbl ticket) {
        // Initialize attachment and comment IDs 添付ファイルとコメントIDを初期化
        ticket.setAttachmentId(0);
        ticket.setCommentId(0);
        // Set creation and update timestamps 作成日時と更新日時を設定
        ticket.setCreatedDate(LocalDateTime.now());
        ticket.setUpdateDate(LocalDateTime.now());
        return ticketService.saveTicket(ticket);
    }

    /**
     * API endpoint to update an existing ticket 既存のチケットを更新するAPIエンドポイント
     * @param ticketId ID of the ticket to update 更新するチケットのID
     * @param ticket Updated ticket object 更新されたチケットオブジェクト
     * @return Updated ticket object or null if not found 更新されたチケットオブジェクト、見つからない場合はnull
     */
    @PutMapping("/api/tickets/{ticketId}")
    @ResponseBody
    public TicketTbl updateTicket(@PathVariable int ticketId, @RequestBody TicketTbl ticket) {
        TicketTbl existingTicket = ticketService.findByTicketId(ticketId);
        if (existingTicket != null) {
            // Preserve ticket ID and creation date チケットIDと作成日を保持
            ticket.setTicketId(ticketId);
            ticket.setCreatedDate(existingTicket.getCreatedDate());
            // Update the modification timestamp 更新日時を更新
            ticket.setUpdateDate(LocalDateTime.now());
            return ticketService.saveTicket(ticket);
        }
        return null;
    }

    /**
     * API endpoint to delete a ticket チケットを削除するAPIエンドポイント
     * @param ticketId ID of the ticket to delete 削除するチケットのID
     * @return Response entity with success message 成功メッセージを含むレスポンスエンティティ
     */
    @DeleteMapping("/api/tickets/{ticketId}")
    @ResponseBody
    public ResponseEntity<String> deleteTicket(@PathVariable int ticketId) {
        TicketTbl ticket = ticketService.findByTicketId(ticketId);
        ticketService.deleteTicket(ticket);
        return ResponseEntity.ok("Ticket deleted successfully");
    }

    /**
     * API endpoint to get count of pending tickets 保留中のチケット数を取得するAPIエンドポイント
     * @param status Status parameter (overridden to "PENDING") ステータスパラメータ（「PENDING」で上書き）
     * @return Count of pending tickets 保留中のチケット数
     */
    @GetMapping("/api/pendingstatus")
    @ResponseBody
    public int getTicketsByPending(String status) {
        status = "PENDING";
        return ticketService.getTicketsBystatus(status);
    }

    /**
     * API endpoint to get count of ongoing tickets 進行中のチケット数を取得するAPIエンドポイント
     * @param status Status parameter (overridden to "ONGOING") ステータスパラメータ（「ONGOING」で上書き）
     * @return Count of ongoing tickets 進行中のチケット数
     */
    @GetMapping("/api/ongoingstatus")
    @ResponseBody
    public int getTicketsByOngoing(String status) {
        status = "ONGOING";
        return ticketService.getTicketsBystatus(status);
    }

    /**
     * API endpoint to get count of resolved tickets 解決済みのチケット数を取得するAPIエンドポイント
     * @param status Status parameter (overridden to "RESOLVED") ステータスパラメータ（「RESOLVED」で上書き）
     * @return Count of resolved tickets 解決済みのチケット数
     */
    @GetMapping("/api/resolvedstatus")
    @ResponseBody
    public int getTicketsByResolved(String status) {
        status = "RESOLVED";
        return ticketService.getTicketsBystatus(status);
    }
    
    /**
     * API endpoint to add a comment to a ticket チケットにコメントを追加するAPIエンドポイント
     * @param ticketId ID of the ticket to add comment to コメントを追加するチケットのID
     * @param commentData Map containing comment text and author コメントテキストと作成者を含むマップ
     * @return Response entity with created comment or error status 作成されたコメントまたはエラーステータスを含むレスポンスエンティティ
     */
    @PostMapping("/api/tickets/{ticketId}/comments")
    @ResponseBody
    public ResponseEntity<TicketCommentsTbl> addComment(
            @PathVariable Integer ticketId,
            @RequestBody Map<String, String> commentData) {
        try {
            String commentText = commentData.get("comment");
            String author = commentData.get("author");
            
            // Validate comment text コメントテキストを検証
            if (commentText == null || commentText.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            // Validate author 作成者を検証
            if (author == null || author.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            TicketCommentsTbl comment = ticketCommentsService.addCommentToTicket(ticketId, commentText, author);
            return ResponseEntity.ok(comment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * API endpoint to get the count of comments for a ticket チケットのコメント数を取得するAPIエンドポイント
     * @param ticketId ID of the ticket コメント数を取得するチケットのID
     * @return Map containing the comment count コメント数を含むマップ
     */
    @GetMapping("/api/tickets/{ticketId}/comments/count")
    @ResponseBody
    public Map<String, Long> getCommentsCount(@PathVariable Integer ticketId) {
        Long count = ticketCommentsService.countCommentsByTicketId(ticketId);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return response;
    }

    /**
     * API endpoint to add status history to a ticket チケットにステータス履歴を追加するAPIエンドポイント
     * @param ticketId ID of the ticket ステータス履歴を追加するチケットのID
     * @param ticketData Map containing status information ステータス情報を含むマップ
     * @return Response entity with created status history or error status 作成されたステータス履歴またはエラーステータスを含むレスポンスエンティティ
     */
    @PostMapping("/api/tickets/{ticketId}/status-history")
    @ResponseBody
    public ResponseEntity<TicketStatusHistoryTbl> addStatusHistory(
            @PathVariable Integer ticketId,
            @RequestBody Map<String, String> ticketData) {
        try {
            String status = ticketData.get("status");
            
            // Validate status ステータスを検証
            if (status == null || status.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            TicketStatusHistoryTbl statusHistory = ticketStatusHistoryService.addStatusHistoryToTicket(ticketId, status);
            return ResponseEntity.ok(statusHistory);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * API endpoint to get the latest status of a ticket チケットの最新ステータスを取得するAPIエンドポイント
     * @param ticketId ID of the ticket 最新ステータスを取得するチケットのID
     * @return Latest status history object 最新のステータス履歴オブジェクト
     */
    @GetMapping("/api/tickets/{ticketId}/latest-status")
    @ResponseBody
    public TicketStatusHistoryTbl getLatestStatus(@PathVariable Integer ticketId) {
        return ticketStatusHistoryService.getLatestStatus(ticketId);
    }
}
