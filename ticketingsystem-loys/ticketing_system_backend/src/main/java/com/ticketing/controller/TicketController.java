package com.ticketing.controller;

import com.ticketing.dto.TicketCreateDTO;
import com.ticketing.dto.TicketDTO;
import com.ticketing.dto.TicketUpdateDTO;
import com.ticketing.service.TicketService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    // Get all tickets
    @GetMapping
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        List<TicketDTO> tickets = ticketService.getAllTickets();
        return ResponseEntity.ok(tickets);
    }

    // Get ticket by ID
    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Integer id) {
        TicketDTO ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(ticket);
    }

    // Create new ticket
    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@Valid @RequestBody TicketCreateDTO createDTO) {
        TicketDTO createdTicket = ticketService.createTicket(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }

    // Update ticket
    @PutMapping("/{id}")
    public ResponseEntity<TicketDTO> updateTicket(
            @PathVariable Integer id,
            @RequestBody TicketUpdateDTO updateDTO) {
        TicketDTO updatedTicket = ticketService.updateTicket(id, updateDTO);
        return ResponseEntity.ok(updatedTicket);
    }

    // Delete ticket
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Integer id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    // Search tickets
    @GetMapping("/search")
    public ResponseEntity<List<TicketDTO>> searchTickets(@RequestParam String keyword) {
        List<TicketDTO> tickets = ticketService.searchTickets(keyword);
        return ResponseEntity.ok(tickets);
    }

    // Filter tickets
    @GetMapping("/filter")
    public ResponseEntity<List<TicketDTO>> filterTickets(
            @RequestParam(required = false) Integer statusId,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer systemId,
            @RequestParam(required = false) String author) {
        List<TicketDTO> tickets = ticketService.filterTickets(statusId, categoryId, systemId, author);
        return ResponseEntity.ok(tickets);
    }

    // Get tickets by status
    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<TicketDTO>> getTicketsByStatus(@PathVariable Integer statusId) {
        List<TicketDTO> tickets = ticketService.getTicketsByStatus(statusId);
        return ResponseEntity.ok(tickets);
    }

    // Get tickets by category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<TicketDTO>> getTicketsByCategory(@PathVariable Integer categoryId) {
        List<TicketDTO> tickets = ticketService.getTicketsByCategory(categoryId);
        return ResponseEntity.ok(tickets);
    }

    // Get tickets by system
    @GetMapping("/system/{systemId}")
    public ResponseEntity<List<TicketDTO>> getTicketsBySystem(@PathVariable Integer systemId) {
        List<TicketDTO> tickets = ticketService.getTicketsBySystem(systemId);
        return ResponseEntity.ok(tickets);
    }
}