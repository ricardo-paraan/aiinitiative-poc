package com.fullstack.controller;

import com.fullstack.entity.TicketStatus;
import com.fullstack.service.TicketStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/statuses")
@CrossOrigin(origins = "http://localhost:3000")
public class TicketStatusController {
    
    @Autowired
    private TicketStatusService ticketStatusService;
    
    // Get all statuses
    @GetMapping
    public ResponseEntity<List<TicketStatus>> getAllStatuses() {
        List<TicketStatus> statuses = ticketStatusService.getAllStatuses();
        return ResponseEntity.ok(statuses);
    }
    
    // Get status by ID
    @GetMapping("/{id}")
    public ResponseEntity<TicketStatus> getStatusById(@PathVariable Integer id) {
        Optional<TicketStatus> status = ticketStatusService.getStatusById(id);
        return status.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    // Get status by name
    @GetMapping("/name/{name}")
    public ResponseEntity<TicketStatus> getStatusByName(@PathVariable String name) {
        Optional<TicketStatus> status = ticketStatusService.getStatusByName(name);
        return status.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    // Create new status
    @PostMapping
    public ResponseEntity<TicketStatus> createStatus(@RequestBody TicketStatus status) {
        TicketStatus createdStatus = ticketStatusService.createStatus(status);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStatus);
    }
    
    // Update status
    @PutMapping("/{id}")
    public ResponseEntity<TicketStatus> updateStatus(@PathVariable Integer id, @RequestBody TicketStatus status) {
        TicketStatus updatedStatus = ticketStatusService.updateStatus(id, status);
        if (updatedStatus != null) {
            return ResponseEntity.ok(updatedStatus);
        }
        return ResponseEntity.notFound().build();
    }
    
    // Delete status
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatus(@PathVariable Integer id) {
        boolean deleted = ticketStatusService.deleteStatus(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}