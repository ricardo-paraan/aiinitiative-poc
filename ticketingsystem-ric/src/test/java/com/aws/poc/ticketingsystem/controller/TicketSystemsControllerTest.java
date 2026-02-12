package com.aws.poc.ticketingsystem.controller;

import com.aws.poc.ticketingsystem.entity.TicketSystemsTbl;
import com.aws.poc.ticketingsystem.service.TicketSystemsService;
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
 * Unit tests for TicketSystemsController
 * Tests all REST API endpoints for ticket systems operations
 */
@WebMvcTest(TicketSystemsController.class)
class TicketSystemsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketSystemsService ticketSystemsService;

    private TicketSystemsTbl system1;
    private TicketSystemsTbl system2;
    private TicketSystemsTbl system3;

    @BeforeEach
    void setUp() {
        system1 = new TicketSystemsTbl();
        system1.setSystemId(1);
        system1.setSystemName("CRM System");

        system2 = new TicketSystemsTbl();
        system2.setSystemId(2);
        system2.setSystemName("ERP System");

        system3 = new TicketSystemsTbl();
        system3.setSystemId(3);
        system3.setSystemName("HR System");
    }

    @Test
    void testFindAll_ReturnsAllTicketSystems() throws Exception {
        List<TicketSystemsTbl> systems = Arrays.asList(system1, system2, system3);
        when(ticketSystemsService.findAll()).thenReturn(systems);

        mockMvc.perform(get("/api/tickets-systems"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].systemId", is(1)))
                .andExpect(jsonPath("$[0].systemName", is("CRM System")))
                .andExpect(jsonPath("$[1].systemId", is(2)))
                .andExpect(jsonPath("$[1].systemName", is("ERP System")))
                .andExpect(jsonPath("$[2].systemId", is(3)))
                .andExpect(jsonPath("$[2].systemName", is("HR System")));

        verify(ticketSystemsService, times(1)).findAll();
    }

    @Test
    void testFindAll_ReturnsEmptyListWhenNoSystems() throws Exception {
        when(ticketSystemsService.findAll()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/tickets-systems"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(ticketSystemsService, times(1)).findAll();
    }

    @Test
    void testFindAll_ReturnsSingleSystem() throws Exception {
        List<TicketSystemsTbl> systems = Arrays.asList(system1);
        when(ticketSystemsService.findAll()).thenReturn(systems);

        mockMvc.perform(get("/api/tickets-systems"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].systemId", is(1)))
                .andExpect(jsonPath("$[0].systemName", is("CRM System")));

        verify(ticketSystemsService, times(1)).findAll();
    }

    @Test
    void testFindAll_ReturnsMultipleSystems() throws Exception {
        List<TicketSystemsTbl> systems = Arrays.asList(system1, system2);
        when(ticketSystemsService.findAll()).thenReturn(systems);

        mockMvc.perform(get("/api/tickets-systems"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].systemId", is(1)))
                .andExpect(jsonPath("$[0].systemName", is("CRM System")))
                .andExpect(jsonPath("$[1].systemId", is(2)))
                .andExpect(jsonPath("$[1].systemName", is("ERP System")));

        verify(ticketSystemsService, times(1)).findAll();
    }
}