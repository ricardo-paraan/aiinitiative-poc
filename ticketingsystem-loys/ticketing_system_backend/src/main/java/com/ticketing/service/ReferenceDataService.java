package com.ticketing.service;

import com.ticketing.model.TicketCategory;
import com.ticketing.model.TicketStatus;
import com.ticketing.model.TicketSystem;
import com.ticketing.repository.CategoryRepository;
import com.ticketing.repository.StatusRepository;
import com.ticketing.repository.SystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReferenceDataService {

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SystemRepository systemRepository;

    // Status operations
    public List<TicketStatus> getAllStatuses() {
        return statusRepository.findAll();
    }

    public TicketStatus getStatusById(Integer id) {
        return statusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Status not found with id: " + id));
    }

    // Category operations
    public List<TicketCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    public TicketCategory getCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    // System operations
    public List<TicketSystem> getAllSystems() {
        return systemRepository.findAll();
    }

    public TicketSystem getSystemById(Integer id) {
        return systemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("System not found with id: " + id));
    }
}