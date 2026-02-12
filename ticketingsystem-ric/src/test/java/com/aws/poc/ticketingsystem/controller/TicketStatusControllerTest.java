package com.aws.poc.ticketingsystem.controller;

import com.aws.poc.ticketingsystem.entity.TicketStatusTbl;
import com.aws.poc.ticketingsystem.service.TicketStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for TicketStatusController
 * Tests all REST API endpoints for ticket status operations
 */
@WebMvcTest(TicketStatusController.class)
class TicketStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketStatusService ticketStatusService;

    private TicketStatusTbl pendingStatus;
    private TicketStatusTbl ongoingStatus;
    private TicketStatusTbl resolvedStatus;

    @BeforeEach
    void setUp() {
        pendingStatus = new TicketStatusTbl();
        pendingStatus.setStatusId(1);
        pendingStatus.setStatus("PENDING");

        ongoingStatus = new TicketStatusTbl();
        ongoingStatus.setStatusId(2);
        ongoingStatus.setStatus("ONGOING");

        resolvedStatus = new TicketStatusTbl();
        resolvedStatus.setStatusId(3);
        resolvedStatus.setStatus("RESOLVED");
    }

    @Test
    void testFindAll_ReturnsAllTicketStatuses() throws Exception {
        List<TicketStatusTbl> statuses = Arrays.asList(pendingStatus, ongoingStatus, resolvedStatus);
        when(ticketStatusService.findAll()).thenReturn(statuses);

        mockMvc.perform(get("/api/tickets-status"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].statusId", is(1)))
                .andExpect(jsonPath("$[0].status", is("PENDING")))
                .andExpect(jsonPath("$[1].statusId", is(2)))
                .andExpect(jsonPath("$[1].status", is("ONGOING")))
                .andExpect(jsonPath("$[2].statusId", is(3)))
                .andExpect(jsonPath("$[2].status", is("RESOLVED")));

        verify(ticketStatusService, times(1)).findAll();
    }

    @Test
    void testFindAll_ReturnsEmptyListWhenNoStatuses() throws Exception {
        when(ticketStatusService.findAll()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/tickets-status"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(ticketStatusService, times(1)).findAll();
    }

    @Test
    void testFindAll_ReturnsSingleStatus() throws Exception {
        List<TicketStatusTbl> statuses = Arrays.asList(pendingStatus);
        when(ticketStatusService.findAll()).thenReturn(statuses);

        mockMvc.perform(get("/api/tickets-status"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].statusId", is(1)))
                .andExpect(jsonPath("$[0].status", is("PENDING")));

        verify(ticketStatusService, times(1)).findAll();
    }
}