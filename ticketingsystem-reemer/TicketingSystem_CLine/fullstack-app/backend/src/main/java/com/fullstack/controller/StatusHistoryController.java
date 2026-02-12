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

@RestController
@RequestMapping("/api/status-history")
@CrossOrigin(origins = "*")
public class StatusHistoryController {
    
    @Autowired
    private StatusHistoryService statusHistoryService;
    
    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<Map<String, Object>>> getStatusHistoryByTicketId(@PathVariable Integer ticketId) {
        List<StatusHistory> historyList = statusHistoryService.getStatusHistoryByTicketId(ticketId);
        
        // Convert to simplified DTO to avoid circular reference issues
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