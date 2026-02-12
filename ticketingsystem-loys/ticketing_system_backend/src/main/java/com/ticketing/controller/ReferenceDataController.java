package com.ticketing.controller;

import com.ticketing.model.TicketCategory;
import com.ticketing.model.TicketStatus;
import com.ticketing.model.TicketSystem;
import com.ticketing.service.ReferenceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ReferenceDataController {

    @Autowired
    private ReferenceDataService referenceDataService;

    // Get all statuses
    @GetMapping("/statuses")
    public ResponseEntity<List<TicketStatus>> getAllStatuses() {
        List<TicketStatus> statuses = referenceDataService.getAllStatuses();
        return ResponseEntity.ok(statuses);
    }

    // Get status by ID
    @GetMapping("/statuses/{id}")
    public ResponseEntity<TicketStatus> getStatusById(@PathVariable Integer id) {
        TicketStatus status = referenceDataService.getStatusById(id);
        return ResponseEntity.ok(status);
    }

    // Get all categories
    @GetMapping("/categories")
    public ResponseEntity<List<TicketCategory>> getAllCategories() {
        List<TicketCategory> categories = referenceDataService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // Get category by ID
    @GetMapping("/categories/{id}")
    public ResponseEntity<TicketCategory> getCategoryById(@PathVariable Integer id) {
        TicketCategory category = referenceDataService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    // Get all systems
    @GetMapping("/systems")
    public ResponseEntity<List<TicketSystem>> getAllSystems() {
        List<TicketSystem> systems = referenceDataService.getAllSystems();
        return ResponseEntity.ok(systems);
    }

    // Get system by ID
    @GetMapping("/systems/{id}")
    public ResponseEntity<TicketSystem> getSystemById(@PathVariable Integer id) {
        TicketSystem system = referenceDataService.getSystemById(id);
        return ResponseEntity.ok(system);
    }
}