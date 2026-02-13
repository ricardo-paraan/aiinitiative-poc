package com.fullstack.controller;

import com.fullstack.entity.TicketSystem;
import com.fullstack.service.TicketSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for ticket system management.
 * チケットシステム管理のRESTコントローラー。
 */
@RestController
@RequestMapping("/api/systems")
@CrossOrigin(origins = "http://localhost:3000")
public class TicketSystemController {
    
    @Autowired
    private TicketSystemService ticketSystemService;
    
    // Get all systems
    @GetMapping
    public ResponseEntity<List<TicketSystem>> getAllSystems() {
        List<TicketSystem> systems = ticketSystemService.getAllSystems();
        return ResponseEntity.ok(systems);
    }
    
    // Get system by ID
    @GetMapping("/{id}")
    public ResponseEntity<TicketSystem> getSystemById(@PathVariable Integer id) {
        Optional<TicketSystem> system = ticketSystemService.getSystemById(id);
        return system.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Retrieves system by exact name match.
     * 完全一致する名前でシステムを取得します。
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<TicketSystem> getSystemByName(@PathVariable String name) {
        Optional<TicketSystem> system = ticketSystemService.getSystemByName(name);
        return system.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    // Create new system
    @PostMapping
    public ResponseEntity<TicketSystem> createSystem(@RequestBody TicketSystem system) {
        TicketSystem createdSystem = ticketSystemService.createSystem(system);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSystem);
    }
    
    // Update system
    @PutMapping("/{id}")
    public ResponseEntity<TicketSystem> updateSystem(@PathVariable Integer id, @RequestBody TicketSystem system) {
        TicketSystem updatedSystem = ticketSystemService.updateSystem(id, system);
        if (updatedSystem != null) {
            return ResponseEntity.ok(updatedSystem);
        }
        return ResponseEntity.notFound().build();
    }
    
    // Delete system
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSystem(@PathVariable Integer id) {
        boolean deleted = ticketSystemService.deleteSystem(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}