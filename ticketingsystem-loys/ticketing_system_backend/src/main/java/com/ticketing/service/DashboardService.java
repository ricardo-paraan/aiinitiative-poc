package com.ticketing.service;

import com.ticketing.dto.DashboardStatsDTO;
import com.ticketing.model.TicketCategory;
import com.ticketing.model.TicketSystem;
import com.ticketing.repository.CategoryRepository;
import com.ticketing.repository.StatusRepository;
import com.ticketing.repository.SystemRepository;
import com.ticketing.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SystemRepository systemRepository;

    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        // Total tickets
        stats.setTotalTickets(ticketRepository.count());

        // Tickets by status (assuming status IDs: 1=Open, 2=In Progress, 3=Resolved, 4=Closed)
        stats.setOpenTickets(ticketRepository.countByStatusId(1));
        stats.setInProgressTickets(ticketRepository.countByStatusId(2));
        stats.setResolvedTickets(ticketRepository.countByStatusId(3));
        stats.setClosedTickets(ticketRepository.countByStatusId(4));

        // Tickets by category
        Map<String, Long> ticketsByCategory = new HashMap<>();
        List<TicketCategory> categories = categoryRepository.findAll();
        for (TicketCategory category : categories) {
            Long count = ticketRepository.countByCategoryId(category.getCategoryId());
            ticketsByCategory.put(category.getCategoryName(), count);
        }
        stats.setTicketsByCategory(ticketsByCategory);

        // Tickets by system
        Map<String, Long> ticketsBySystem = new HashMap<>();
        List<TicketSystem> systems = systemRepository.findAll();
        for (TicketSystem system : systems) {
            Long count = ticketRepository.countBySystemId(system.getSystemId());
            ticketsBySystem.put(system.getSystemName(), count);
        }
        stats.setTicketsBySystem(ticketsBySystem);

        return stats;
    }
}