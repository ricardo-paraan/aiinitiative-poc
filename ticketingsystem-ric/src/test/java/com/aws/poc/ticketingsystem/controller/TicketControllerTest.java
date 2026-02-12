package com.aws.poc.ticketingsystem.controller;

import com.aws.poc.ticketingsystem.entity.TicketCommentsTbl;
import com.aws.poc.ticketingsystem.entity.TicketStatusHistoryTbl;
import com.aws.poc.ticketingsystem.entity.TicketTbl;
import com.aws.poc.ticketingsystem.service.TicketCommentsService;
import com.aws.poc.ticketingsystem.service.TicketService;
import com.aws.poc.ticketingsystem.service.TicketStatusHistoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for TicketController
 * Tests all REST API endpoints and view mappings
 */
@WebMvcTest(TicketController.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketService ticketService;

    @MockitoBean
    private TicketCommentsService ticketCommentsService;

    @MockitoBean
    private TicketStatusHistoryService ticketStatusHistoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private TicketTbl testTicket;
    private TicketCommentsTbl testComment;
    private TicketStatusHistoryTbl testStatusHistory;

    @BeforeEach
    void setUp() {
        testTicket = new TicketTbl();
        testTicket.setTicketId(1);
        testTicket.setTitle("Test Ticket");
        testTicket.setAuthor("Test Author");
        testTicket.setSystemName("Test System");
        testTicket.setCategory("Bug");
        testTicket.setDescription("Test Description");
        testTicket.setStatus("PENDING");
        testTicket.setAttachmentId(0);
        testTicket.setCommentId(0);
        testTicket.setCreatedDate(LocalDateTime.now());
        testTicket.setUpdateDate(LocalDateTime.now());

        testComment = new TicketCommentsTbl();
        testComment.setCommentId(1);
        testComment.setTicket(testTicket);
        testComment.setComments("Test Comment");
        testComment.setAuthor("Comment Author");
        testComment.setCreatedDate(LocalDateTime.now());

        testStatusHistory = new TicketStatusHistoryTbl();
        testStatusHistory.setStatusId(1);
        testStatusHistory.setTicket(testTicket);
        testStatusHistory.setStatus("PENDING");
        testStatusHistory.setUpdateDate(LocalDateTime.now());
    }

    // View mapping tests

    @Test
    void testIndex_ReturnsIndexView() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void testShowCreateForm_ReturnsCreateTicketView() throws Exception {
        mockMvc.perform(get("/tickets/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-ticket"))
                .andExpect(model().attributeExists("ticket"));
    }

    @Test
    void testShowEditForm_ReturnsEditTicketView() throws Exception {
        when(ticketService.findByTicketId(1)).thenReturn(testTicket);

        mockMvc.perform(get("/tickets/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-ticket"))
                .andExpect(model().attributeExists("ticket"))
                .andExpect(model().attribute("ticket", testTicket));

        verify(ticketService, times(1)).findByTicketId(1);
    }

    @Test
    void testShowTicketDetails_ReturnsTicketDetailsView() throws Exception {
        List<TicketCommentsTbl> comments = Arrays.asList(testComment);
        List<TicketStatusHistoryTbl> statusHistory = Arrays.asList(testStatusHistory);

        when(ticketService.findByTicketId(1)).thenReturn(testTicket);
        when(ticketCommentsService.findCommentsByTicketId(1)).thenReturn(comments);
        when(ticketStatusHistoryService.getStatusHistoryByTicketId(1)).thenReturn(statusHistory);

        mockMvc.perform(get("/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("ticket-details"))
                .andExpect(model().attributeExists("ticket", "comments", "statusHistory"))
                .andExpect(model().attribute("ticket", testTicket))
                .andExpect(model().attribute("comments", comments))
                .andExpect(model().attribute("statusHistory", statusHistory));

        verify(ticketService, times(1)).findByTicketId(1);
        verify(ticketCommentsService, times(1)).findCommentsByTicketId(1);
        verify(ticketStatusHistoryService, times(1)).getStatusHistoryByTicketId(1);
    }

    // REST API endpoint tests

    @Test
    void testFindAll_ReturnsAllTickets() throws Exception {
        List<TicketTbl> tickets = Arrays.asList(testTicket);
        when(ticketService.findAll()).thenReturn(tickets);

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].ticketId", is(1)))
                .andExpect(jsonPath("$[0].title", is("Test Ticket")));

        verify(ticketService, times(1)).findAll();
    }

    @Test
    void testGetTicketById_ReturnsTicket() throws Exception {
        when(ticketService.findByTicketId(1)).thenReturn(testTicket);

        mockMvc.perform(get("/api/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.ticketId", is(1)))
                .andExpect(jsonPath("$.title", is("Test Ticket")))
                .andExpect(jsonPath("$.author", is("Test Author")));

        verify(ticketService, times(1)).findByTicketId(1);
    }

    @Test
    void testTicketCommentsList_ReturnsTicketWithComments() throws Exception {
        List<TicketTbl> ticketDetails = Arrays.asList(testTicket);
        when(ticketService.findByTicketId(1)).thenReturn(testTicket);
        when(ticketService.ticketDetailsList(1)).thenReturn(ticketDetails);

        mockMvc.perform(get("/api/tickets/1/comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));

        verify(ticketService, times(1)).findByTicketId(1);
        verify(ticketService, times(1)).ticketDetailsList(1);
    }

    @Test
    void testCreateTicket_CreatesSuccessfully() throws Exception {
        when(ticketService.saveTicket(any(TicketTbl.class))).thenReturn(testTicket);

        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTicket)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.ticketId", is(1)))
                .andExpect(jsonPath("$.title", is("Test Ticket")));

        verify(ticketService, times(1)).saveTicket(any(TicketTbl.class));
    }

    @Test
    void testUpdateTicket_UpdatesSuccessfully() throws Exception {
        TicketTbl updatedTicket = new TicketTbl();
        updatedTicket.setTicketId(1);
        updatedTicket.setTitle("Updated Title");
        updatedTicket.setAuthor("Test Author");
        updatedTicket.setStatus("ONGOING");

        when(ticketService.findByTicketId(1)).thenReturn(testTicket);
        when(ticketService.saveTicket(any(TicketTbl.class))).thenReturn(updatedTicket);

        mockMvc.perform(put("/api/tickets/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTicket)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.ticketId", is(1)))
                .andExpect(jsonPath("$.title", is("Updated Title")));

        verify(ticketService, times(1)).findByTicketId(1);
        verify(ticketService, times(1)).saveTicket(any(TicketTbl.class));
    }

    @Test
    void testUpdateTicket_ReturnsNullWhenTicketNotFound() throws Exception {
        when(ticketService.findByTicketId(anyInt())).thenReturn(null);

        mockMvc.perform(put("/api/tickets/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTicket)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(ticketService, times(1)).findByTicketId(999);
        verify(ticketService, never()).saveTicket(any(TicketTbl.class));
    }

    @Test
    void testDeleteTicket_DeletesSuccessfully() throws Exception {
        when(ticketService.findByTicketId(1)).thenReturn(testTicket);
        doNothing().when(ticketService).deleteTicket(any(TicketTbl.class));

        mockMvc.perform(delete("/api/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Ticket deleted successfully"));

        verify(ticketService, times(1)).findByTicketId(1);
        verify(ticketService, times(1)).deleteTicket(testTicket);
    }

    @Test
    void testGetTicketsByPending_ReturnsCount() throws Exception {
        when(ticketService.getTicketsBystatus("PENDING")).thenReturn(5);

        mockMvc.perform(get("/api/pendingstatus"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(ticketService, times(1)).getTicketsBystatus("PENDING");
    }

    @Test
    void testGetTicketsByOngoing_ReturnsCount() throws Exception {
        when(ticketService.getTicketsBystatus("ONGOING")).thenReturn(3);

        mockMvc.perform(get("/api/ongoingstatus"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));

        verify(ticketService, times(1)).getTicketsBystatus("ONGOING");
    }

    @Test
    void testGetTicketsByResolved_ReturnsCount() throws Exception {
        when(ticketService.getTicketsBystatus("RESOLVED")).thenReturn(10);

        mockMvc.perform(get("/api/resolvedstatus"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));

        verify(ticketService, times(1)).getTicketsBystatus("RESOLVED");
    }

    @Test
    void testAddComment_AddsSuccessfully() throws Exception {
        Map<String, String> commentData = new HashMap<>();
        commentData.put("comment", "New Comment");
        commentData.put("author", "Comment Author");

        when(ticketCommentsService.addCommentToTicket(anyInt(), anyString(), anyString()))
                .thenReturn(testComment);

        mockMvc.perform(post("/api/tickets/1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentData)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.commentId", is(1)))
                .andExpect(jsonPath("$.comments", is("Test Comment")));

        verify(ticketCommentsService, times(1))
                .addCommentToTicket(1, "New Comment", "Comment Author");
    }

    @Test
    void testAddComment_ReturnsBadRequestWhenCommentEmpty() throws Exception {
        Map<String, String> commentData = new HashMap<>();
        commentData.put("comment", "");
        commentData.put("author", "Author");

        mockMvc.perform(post("/api/tickets/1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentData)))
                .andExpect(status().isBadRequest());

        verify(ticketCommentsService, never()).addCommentToTicket(anyInt(), anyString(), anyString());
    }

    @Test
    void testAddComment_ReturnsBadRequestWhenAuthorEmpty() throws Exception {
        Map<String, String> commentData = new HashMap<>();
        commentData.put("comment", "Comment");
        commentData.put("author", "");

        mockMvc.perform(post("/api/tickets/1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentData)))
                .andExpect(status().isBadRequest());

        verify(ticketCommentsService, never()).addCommentToTicket(anyInt(), anyString(), anyString());
    }

    @Test
    void testGetCommentsCount_ReturnsCount() throws Exception {
        when(ticketCommentsService.countCommentsByTicketId(1)).thenReturn(5L);

        mockMvc.perform(get("/api/tickets/1/comments/count"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count", is(5)));

        verify(ticketCommentsService, times(1)).countCommentsByTicketId(1);
    }

    @Test
    void testAddStatusHistory_AddsSuccessfully() throws Exception {
        Map<String, String> statusData = new HashMap<>();
        statusData.put("status", "ONGOING");

        when(ticketStatusHistoryService.addStatusHistoryToTicket(anyInt(), anyString()))
                .thenReturn(testStatusHistory);

        mockMvc.perform(post("/api/tickets/1/status-history")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusData)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusId", is(1)))
                .andExpect(jsonPath("$.status", is("PENDING")));

        verify(ticketStatusHistoryService, times(1))
                .addStatusHistoryToTicket(1, "ONGOING");
    }

    @Test
    void testAddStatusHistory_ReturnsBadRequestWhenStatusEmpty() throws Exception {
        Map<String, String> statusData = new HashMap<>();
        statusData.put("status", "");

        mockMvc.perform(post("/api/tickets/1/status-history")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusData)))
                .andExpect(status().isBadRequest());

        verify(ticketStatusHistoryService, never()).addStatusHistoryToTicket(anyInt(), anyString());
    }

    @Test
    void testGetLatestStatus_ReturnsLatestStatus() throws Exception {
        when(ticketStatusHistoryService.getLatestStatus(1)).thenReturn(testStatusHistory);

        mockMvc.perform(get("/api/tickets/1/latest-status"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusId", is(1)))
                .andExpect(jsonPath("$.status", is("PENDING")));

        verify(ticketStatusHistoryService, times(1)).getLatestStatus(1);
    }
}