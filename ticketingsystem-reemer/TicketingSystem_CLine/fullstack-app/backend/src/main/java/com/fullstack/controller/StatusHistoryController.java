package com.fullstack.controller;

import com.fullstack.entity.StatusHistory;
import com.fullstack.service.StatusHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

/**
 * REST controller for ticket status history tracking.
 * チケットステータス履歴追跡のRESTコントローラー。
 */
@RestController
@RequestMapping("/api/status-history")
@CrossOrigin(origins = "*")
public class StatusHistoryController {
    
    @Autowired
    private StatusHistoryService statusHistoryService;
    
    /**
     * Retrieves status change history for a ticket, converted to DTOs to prevent circular references.
     * チケットのステータス変更履歴を取得し、循環参照を防ぐためにDTOに変換します。
     */
    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<Map<String, Object>>> getStatusHistoryByTicketId(@PathVariable Integer ticketId) {
        List<StatusHistory> historyList = statusHistoryService.getStatusHistoryByTicketId(ticketId);
        
        List<Map<String, Object>> response = historyList.stream()
            .map(history -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", history.getId());
                map.put("status", history.getStatus());
                map.put("statusComment", history.getStatusComment());
                map.put("changedBy", history.getChangedBy());
                map.put("changedAt", history.getChangedAt());
                return map;
            })
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
}