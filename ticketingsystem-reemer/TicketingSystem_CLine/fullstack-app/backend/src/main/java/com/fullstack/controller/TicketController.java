package com.fullstack.controller;

import com.fullstack.entity.Ticket;
import com.fullstack.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    
    // Create new ticket
    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
        Ticket createdTicket = ticketService.createTicket(ticket);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }
    
    // Update ticket
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
    
    // Get tickets by author
    @GetMapping("/author/{author}")
    public ResponseEntity<List<Ticket>> getTicketsByAuthor(@PathVariable String author) {
        List<Ticket> tickets = ticketService.getTicketsByAuthor(author);
        return ResponseEntity.ok(tickets);
    }
    
    // Get tickets by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Ticket>> getTicketsByStatus(@PathVariable String status) {
        List<Ticket> tickets = ticketService.getTicketsByStatus(status);
        return ResponseEntity.ok(tickets);
    }
    
    // Get tickets by system name
    @GetMapping("/system/{systemName}")
    public ResponseEntity<List<Ticket>> getTicketsBySystemName(@PathVariable String systemName) {
        List<Ticket> tickets = ticketService.getTicketsBySystemName(systemName);
        return ResponseEntity.ok(tickets);
    }
    
    // Get tickets by category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Ticket>> getTicketsByCategory(@PathVariable String category) {
        List<Ticket> tickets = ticketService.getTicketsByCategory(category);
        return ResponseEntity.ok(tickets);
    }
    
    // Search tickets by title
    @GetMapping("/search")
    public ResponseEntity<List<Ticket>> searchTickets(@RequestParam String title) {
        List<Ticket> tickets = ticketService.searchTicketsByTitle(title);
        return ResponseEntity.ok(tickets);
    }
}