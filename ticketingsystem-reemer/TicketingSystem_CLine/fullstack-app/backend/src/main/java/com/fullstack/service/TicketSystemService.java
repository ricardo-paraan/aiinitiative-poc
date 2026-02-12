package com.fullstack.service;

import com.fullstack.entity.TicketSystem;
import com.fullstack.repository.TicketSystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TicketSystemService {
    
    @Autowired
    private TicketSystemRepository ticketSystemRepository;
    
    // Get all systems
    public List<TicketSystem> getAllSystems() {
        return ticketSystemRepository.findAll();
    }
    
    // Get system by ID
    public Optional<TicketSystem> getSystemById(Integer id) {
        return ticketSystemRepository.findById(id);
    }
    
    // Get system by name
    public Optional<TicketSystem> getSystemByName(String name) {
        return ticketSystemRepository.findBySystemName(name);
    }
    
    // Create new system
    public TicketSystem createSystem(TicketSystem system) {
        return ticketSystemRepository.save(system);
    }
    
    // Update system
    public TicketSystem updateSystem(Integer id, TicketSystem systemDetails) {
        Optional<TicketSystem> system = ticketSystemRepository.findById(id);
        if (system.isPresent()) {
            TicketSystem existingSystem = system.get();
            existingSystem.setSystemName(systemDetails.getSystemName());
            return ticketSystemRepository.save(existingSystem);
        }
        return null;
    }
    
    // Delete system
    public boolean deleteSystem(Integer id) {
        if (ticketSystemRepository.existsById(id)) {
            ticketSystemRepository.deleteById(id);
            return true;
        }
        return false;
    }
}