package com.aws.poc.ticketingsystem.service;

import com.aws.poc.ticketingsystem.entity.TicketTbl;
import com.aws.poc.ticketingsystem.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {

    private TicketRepository ticketRepo;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepos) {
        this.ticketRepo = ticketRepos;
    }

    @Override
    public List<TicketTbl> findAll() {
        return ticketRepo.findAll();
    }

    @Override
    public TicketTbl findByTicketId(int ticketId) {
        Optional<TicketTbl> result = ticketRepo.findById(ticketId);

        TicketTbl ticketObj = null;

        if (result.isPresent()) {
            ticketObj = result.get();
        } else {
            throw new RuntimeException("Cannot find ticket with an id: " + ticketId);
        }

        return ticketObj;
    }

    @Override
    public TicketTbl saveTicket(TicketTbl ticketObj) {
        return ticketRepo.save(ticketObj);
    }

    @Override
    public void deleteTicket(TicketTbl ticketId) {
        ticketRepo.delete(ticketId);
    }

    @Override
    public int getTicketsBystatus(String status) {
        return ticketRepo.countTicketsByStatus(status);
    }
}
