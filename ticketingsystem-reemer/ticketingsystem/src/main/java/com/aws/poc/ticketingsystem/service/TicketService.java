package com.aws.poc.ticketingsystem.service;

import com.aws.poc.ticketingsystem.entity.TicketTbl;

import java.util.List;

public interface TicketService {

    List<TicketTbl> findAll();

    TicketTbl findByTicketId(int ticketId);

    TicketTbl saveTicket(TicketTbl ticketObj);

    void deleteTicket(TicketTbl ticketId);

    int getTicketsBystatus(String status);
}
