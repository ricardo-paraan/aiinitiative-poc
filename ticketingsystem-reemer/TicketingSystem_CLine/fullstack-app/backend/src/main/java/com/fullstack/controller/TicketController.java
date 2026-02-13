package com.fullstack.controller;

import com.fullstack.entity.Ticket;
import com.fullstack.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for ticket management operations.
 * チケット管理操作のRESTコントローラー。
 */
@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "http://localhost:3000")
public class TicketController {
    
    @Autowired
    private TicketService ticketService;
    
    // Get all tickets
    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        List<Ticket> tickets = ticketService.getAllTickets();
        return ResponseEntity.ok(tickets);
    }
    
    // Get ticket by ID
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Integer id) {
        Optional<Ticket> ticket = ticketService.getTicketById(id);
        return ticket.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Creates a new ticket with initial status history.
     * 初期ステータス履歴を持つ新しいチケットを作成します。
     */
    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
        Ticket createdTicket = ticketService.createTicket(ticket);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }
    
    /**
     * Updates ticket and records status history if status changes.
     * チケットを更新し、ステータスが変更された場合は履歴を記録します。
     */
    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Integer id, @RequestBody Ticket ticket) {
        Ticket updatedTicket = ticketService.updateTicket(id, ticket);
        if (updatedTicket != null) {
            return ResponseEntity.ok(updatedTicket);
        }
        return ResponseEntity.notFound().build();
    }
    
    // Delete ticket
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Integer id) {
        boolean deleted = ticketService.deleteTicket(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * Retrieves tickets filtered by author name.
     * 作成者名でフィルタリングされたチケットを取得します。
     */
    @GetMapping("/author/{author}")
    public ResponseEntity<List<Ticket>> getTicketsByAuthor(@PathVariable String author) {
        List<Ticket> tickets = ticketService.getTicketsByAuthor(author);
        return ResponseEntity.ok(tickets);
    }
    
    /**
     * Retrieves tickets filtered by status.
     * ステータスでフィルタリングされたチケットを取得します。
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Ticket>> getTicketsByStatus(@PathVariable String status) {
        List<Ticket> tickets = ticketService.getTicketsByStatus(status);
        return ResponseEntity.ok(tickets);
    }
    
    /**
     * Retrieves tickets filtered by system name.
     * システム名でフィルタリングされたチケットを取得します。
     */
    @GetMapping("/system/{systemName}")
    public ResponseEntity<List<Ticket>> getTicketsBySystemName(@PathVariable String systemName) {
        List<Ticket> tickets = ticketService.getTicketsBySystemName(systemName);
        return ResponseEntity.ok(tickets);
    }
    
    /**
     * Retrieves tickets filtered by category.
     * カテゴリでフィルタリングされたチケットを取得します。
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Ticket>> getTicketsByCategory(@PathVariable String category) {
        List<Ticket> tickets = ticketService.getTicketsByCategory(category);
        return ResponseEntity.ok(tickets);
    }
    
    /**
     * Searches tickets by title using case-insensitive partial matching.
     * 大文字小文字を区別しない部分一致でチケットをタイトル検索します。
     */
    @GetMapping("/search")
    public ResponseEntity<List<Ticket>> searchTickets(@RequestParam String title) {
        List<Ticket> tickets = ticketService.searchTicketsByTitle(title);
        return ResponseEntity.ok(tickets);
    }
}