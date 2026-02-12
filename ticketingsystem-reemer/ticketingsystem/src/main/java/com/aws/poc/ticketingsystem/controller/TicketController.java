package com.aws.poc.ticketingsystem.controller;


import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aws.poc.ticketingsystem.entity.TicketTbl;
import com.aws.poc.ticketingsystem.service.TicketService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class TicketController {

    private TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/tickets")
    public List<TicketTbl> findAll() {
        return ticketService.findAll();
    }

    @GetMapping("/pendingstatus")
    public int getTicketsByPending(String status) {
        status = "PENDING";
        return ticketService.getTicketsBystatus(status);
    }

    @GetMapping("/ongoingstatus")
    public int getTicketsByOngoing(String status) {
        status = "ONGOING";
        return ticketService.getTicketsBystatus(status);
    }

    @GetMapping("/resolvedstatus")
    public int getTicketsByResolved(String status) {
        status = "RESOLVED";
        return ticketService.getTicketsBystatus(status);
    }
}
