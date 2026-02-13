package com.aws.poc.ticketingsystem.controller;

import com.aws.poc.ticketingsystem.entity.TicketCommentsTbl;
import com.aws.poc.ticketingsystem.entity.TicketStatusHistoryTbl;
import com.aws.poc.ticketingsystem.entity.TicketTbl;
import com.aws.poc.ticketingsystem.service.TicketCommentsService;
import com.aws.poc.ticketingsystem.service.TicketService;
import com.aws.poc.ticketingsystem.service.TicketStatusHistoryService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Comprehensive test class for TicketController
 * Tests all user stories and edge cases for the ticketing system
 */
@WebMvcTest(TicketController.class)
@DisplayName("Ticket Controller Tests")
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

    private TicketTbl sampleTicket;
    private List<TicketTbl> sampleTickets;
    private TicketCommentsTbl sampleComment;
    private TicketStatusHistoryTbl sampleStatusHistory;

    // 1. Manually define the missing bean for the test context
    @TestConfiguration
    static class TestConfig {
        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper().findAndRegisterModules();
        }
    }

    @BeforeEach
    void setUp() {
        // Initialize sample ticket
        sampleTicket = new TicketTbl();
        sampleTicket.setTicketId(1);
        sampleTicket.setTitle("Sample Ticket");
        sampleTicket.setAuthor("Test User");
        sampleTicket.setSystemName("ERP System");
        sampleTicket.setCategory("Bug");
        sampleTicket.setDescription("Sample description");
        sampleTicket.setStatus("PENDING");
        sampleTicket.setAttachmentId(0);
        sampleTicket.setCommentId(0);
        sampleTicket.setCreatedDate(LocalDateTime.now());
        sampleTicket.setUpdateDate(LocalDateTime.now());

        // Initialize sample tickets list
        sampleTickets = new ArrayList<>();
        sampleTickets.add(sampleTicket);

        // Initialize sample comment
        sampleComment = new TicketCommentsTbl();
        sampleComment.setCommentId(1);
        sampleComment.setComments("Test comment");
        sampleComment.setAuthor("Test User");
        sampleComment.setCreatedDate(LocalDateTime.now());
        sampleComment.setTicket(sampleTicket);

        // Initialize sample status history
        sampleStatusHistory = new TicketStatusHistoryTbl();
        sampleStatusHistory.setStatusId(1);
        sampleStatusHistory.setStatus("PENDING");
        sampleStatusHistory.setUpdateDate(LocalDateTime.now());
    }

    // ========================================
    // USER STORY 1: View Dashboard Summary
    // ========================================

    @Test
    @DisplayName("US1: Get pending tickets count - should return correct count")
    void testGetPendingTicketsCount() throws Exception {
        when(ticketService.getTicketsBystatus("PENDING")).thenReturn(5);

        mockMvc.perform(get("/api/pendingstatus"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(ticketService).getTicketsBystatus("PENDING");
    }

    @Test
    @DisplayName("US1: Get ongoing tickets count - should return correct count")
    void testGetOngoingTicketsCount() throws Exception {
        when(ticketService.getTicketsBystatus("ONGOING")).thenReturn(3);

        mockMvc.perform(get("/api/ongoingstatus"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));

        verify(ticketService).getTicketsBystatus("ONGOING");
    }

    @Test
    @DisplayName("US1: Get resolved tickets count - should return correct count")
    void testGetResolvedTicketsCount() throws Exception {
        when(ticketService.getTicketsBystatus("RESOLVED")).thenReturn(10);

        mockMvc.perform(get("/api/resolvedstatus"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));

        verify(ticketService).getTicketsBystatus("RESOLVED");
    }

    @Test
    @DisplayName("US1 Edge Case: Dashboard shows zero tickets for new user")
    void testDashboardWithZeroTickets() throws Exception {
        when(ticketService.getTicketsBystatus("PENDING")).thenReturn(0);
        when(ticketService.getTicketsBystatus("ONGOING")).thenReturn(0);
        when(ticketService.getTicketsBystatus("RESOLVED")).thenReturn(0);

        mockMvc.perform(get("/api/pendingstatus"))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));

        mockMvc.perform(get("/api/ongoingstatus"))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));

        mockMvc.perform(get("/api/resolvedstatus"))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

    // ========================================
    // USER STORY 2: View Ticket List
    // ========================================

    @Test
    @DisplayName("US2: Get all tickets - should return list of tickets")
    void testGetAllTickets() throws Exception {
        when(ticketService.findAll()).thenReturn(sampleTickets);

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].ticketId").value(1))
                .andExpect(jsonPath("$[0].title").value("Sample Ticket"))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[0].systemName").value("ERP System"))
                .andExpect(jsonPath("$[0].category").value("Bug"));

        verify(ticketService).findAll();
    }

    @Test
    @DisplayName("US2: Get all tickets - empty list when no tickets exist")
    void testGetAllTicketsEmpty() throws Exception {
        when(ticketService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(ticketService).findAll();
    }

    @Test
    @DisplayName("US2 Edge Case: Dashboard table loads with 100+ tickets")
    void testGetAllTicketsLargeDataset() throws Exception {
        List<TicketTbl> largeTicketList = new ArrayList<>();
        for (int i = 1; i <= 150; i++) {
            TicketTbl ticket = new TicketTbl();
            ticket.setTicketId(i);
            ticket.setTitle("Ticket " + i);
            ticket.setStatus("PENDING");
            largeTicketList.add(ticket);
        }

        when(ticketService.findAll()).thenReturn(largeTicketList);

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(150)));

        verify(ticketService).findAll();
    }

    // ========================================
    // USER STORY 3 & 4: Filter and Search Tickets
    // (Note: Filtering/searching logic would be in frontend or service layer)
    // ========================================

    @Test
    @DisplayName("US4 Edge Case: Search for special characters")
    void testSearchSpecialCharacters() throws Exception {
        // This test verifies the API can handle special characters in requests
        TicketTbl specialTicket = new TicketTbl();
        specialTicket.setTicketId(1);
        specialTicket.setTitle("Test @#$% Ticket");
        specialTicket.setDescription("Special chars: ;DROP TABLE");

        when(ticketService.findAll()).thenReturn(Arrays.asList(specialTicket));

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test @#$% Ticket"));
    }

    // ========================================
    // USER STORY 5: Create New Ticket
    // ========================================

    @Test
    @DisplayName("US5: Create new ticket - should save and return ticket")
    void testCreateTicket() throws Exception {
        TicketTbl newTicket = new TicketTbl();
        newTicket.setTitle("New Ticket");
        newTicket.setSystemName("ERP System");
        newTicket.setCategory("Feature Request");
        newTicket.setDescription("New feature description");
        newTicket.setAuthor("Test User");
        newTicket.setStatus("PENDING");

        TicketTbl savedTicket = new TicketTbl();
        savedTicket.setTicketId(2);
        savedTicket.setTitle(newTicket.getTitle());
        savedTicket.setSystemName(newTicket.getSystemName());
        savedTicket.setCategory(newTicket.getCategory());
        savedTicket.setDescription(newTicket.getDescription());
        savedTicket.setAuthor(newTicket.getAuthor());
        savedTicket.setStatus(newTicket.getStatus());
        savedTicket.setAttachmentId(0);
        savedTicket.setCommentId(0);
        savedTicket.setCreatedDate(LocalDateTime.now());
        savedTicket.setUpdateDate(LocalDateTime.now());

        when(ticketService.saveTicket(any(TicketTbl.class))).thenReturn(savedTicket);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTicket)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketId").value(2))
                .andExpect(jsonPath("$.title").value("New Ticket"))
                .andExpect(jsonPath("$.systemName").value("ERP System"))
                .andExpect(jsonPath("$.category").value("Feature Request"));

        verify(ticketService).saveTicket(any(TicketTbl.class));
    }

    @Test
    @DisplayName("US5 Edge Case: Submit ticket with minimum valid input (1-character title)")
    void testCreateTicketMinimumInput() throws Exception {
        TicketTbl minTicket = new TicketTbl();
        minTicket.setTitle("A");
        minTicket.setSystemName("ERP");
        minTicket.setCategory("Bug");
        minTicket.setDescription("Min");
        minTicket.setStatus("PENDING");

        TicketTbl savedTicket = new TicketTbl();
        savedTicket.setTicketId(1);
        savedTicket.setTitle("A");
        savedTicket.setCreatedDate(LocalDateTime.now());
        savedTicket.setUpdateDate(LocalDateTime.now());

        when(ticketService.saveTicket(any(TicketTbl.class))).thenReturn(savedTicket);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(minTicket)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("A"));
    }

    @Test
    @DisplayName("US5 Edge Case: Submit ticket with maximum-length text")
    void testCreateTicketMaximumLength() throws Exception {
        String longTitle = "A".repeat(500);
        String longDescription = "B".repeat(5000);

        TicketTbl longTicket = new TicketTbl();
        longTicket.setTitle(longTitle);
        longTicket.setSystemName("ERP System");
        longTicket.setCategory("Bug");
        longTicket.setDescription(longDescription);
        longTicket.setStatus("PENDING");

        TicketTbl savedTicket = new TicketTbl();
        savedTicket.setTicketId(1);
        savedTicket.setTitle(longTitle);
        savedTicket.setDescription(longDescription);
        savedTicket.setCreatedDate(LocalDateTime.now());
        savedTicket.setUpdateDate(LocalDateTime.now());

        when(ticketService.saveTicket(any(TicketTbl.class))).thenReturn(savedTicket);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(longTicket)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(longTitle));
    }

    @Test
    @DisplayName("US5 Edge Case: Submitting multiple tickets quickly in succession")
    void testCreateMultipleTicketsRapidly() throws Exception {
        for (int i = 1; i <= 5; i++) {
            TicketTbl ticket = new TicketTbl();
            ticket.setTitle("Rapid Ticket " + i);
            ticket.setSystemName("ERP");
            ticket.setCategory("Bug");
            ticket.setDescription("Description " + i);
            ticket.setStatus("PENDING");

            TicketTbl savedTicket = new TicketTbl();
            savedTicket.setTicketId(i);
            savedTicket.setTitle(ticket.getTitle());
            savedTicket.setCreatedDate(LocalDateTime.now());
            savedTicket.setUpdateDate(LocalDateTime.now());

            when(ticketService.saveTicket(any(TicketTbl.class))).thenReturn(savedTicket);

            mockMvc.perform(post("/api/tickets")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(ticket)))
                    .andExpect(status().isOk());
        }

        verify(ticketService, times(5)).saveTicket(any(TicketTbl.class));
    }

    // ========================================
    // USER STORY 6: View Ticket Details
    // ========================================

    @Test
    @DisplayName("US6: Get ticket by ID - should return ticket details")
    void testGetTicketById() throws Exception {
        when(ticketService.findByTicketId(1)).thenReturn(sampleTicket);

        mockMvc.perform(get("/api/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketId").value(1))
                .andExpect(jsonPath("$.title").value("Sample Ticket"))
                .andExpect(jsonPath("$.author").value("Test User"))
                .andExpect(jsonPath("$.systemName").value("ERP System"))
                .andExpect(jsonPath("$.category").value("Bug"))
                .andExpect(jsonPath("$.description").value("Sample description"))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(ticketService).findByTicketId(1);
    }

    @Test
    @DisplayName("US6: View ticket details page - should load with all data")
    void testShowTicketDetailsPage() throws Exception {
        List<TicketCommentsTbl> comments = Arrays.asList(sampleComment);
        List<TicketStatusHistoryTbl> statusHistory = Arrays.asList(sampleStatusHistory);

        when(ticketService.findByTicketId(1)).thenReturn(sampleTicket);
        when(ticketCommentsService.findCommentsByTicketId(1)).thenReturn(comments);
        when(ticketStatusHistoryService.getStatusHistoryByTicketId(1)).thenReturn(statusHistory);

        mockMvc.perform(get("/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("ticket-details"))
                .andExpect(model().attributeExists("ticket"))
                .andExpect(model().attributeExists("comments"))
                .andExpect(model().attributeExists("statusHistory"));

        verify(ticketService).findByTicketId(1);
        verify(ticketCommentsService).findCommentsByTicketId(1);
        verify(ticketStatusHistoryService).getStatusHistoryByTicketId(1);
    }

    @Test
    @DisplayName("US6 Edge Case: Ticket displays correctly with long description")
    void testViewTicketWithLongDescription() throws Exception {
        TicketTbl longDescTicket = new TicketTbl();
        longDescTicket.setTicketId(1);
        longDescTicket.setTitle("Ticket");
        longDescTicket.setDescription("A".repeat(10000));

        when(ticketService.findByTicketId(1)).thenReturn(longDescTicket);

        mockMvc.perform(get("/api/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("A".repeat(10000)));
    }

    @Test
    @DisplayName("US6 Edge Case: Ticket created with no attachment viewed normally")
    void testViewTicketWithNoAttachment() throws Exception {
        sampleTicket.setAttachmentId(0);
        when(ticketService.findByTicketId(1)).thenReturn(sampleTicket);

        mockMvc.perform(get("/api/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketId").value(1));
    }

    @Test
    @DisplayName("US6 Edge Case: Attempt to access ticket URL that does not exist")
    void testGetNonExistentTicket() throws Exception {
        when(ticketService.findByTicketId(999)).thenThrow(new RuntimeException("Cannot find ticket with an id: 999"));

        ServletException exception = assertThrows(ServletException.class, () -> mockMvc.perform(get("/api/tickets/999")));

        assertTrue(exception.getCause() instanceof RuntimeException);
        assertEquals("Cannot find ticket with an id: 999", exception.getCause().getMessage());
    }

    // ========================================
    // USER STORY 7: Add Comment to Ticket
    // ========================================

    @Test
    @DisplayName("US7: Add comment to ticket - should save and return comment")
    void testAddCommentToTicket() throws Exception {
        Map<String, String> commentData = new HashMap<>();
        commentData.put("comment", "This is a test comment");
        commentData.put("author", "Test User");

        when(ticketCommentsService.addCommentToTicket(eq(1), eq("This is a test comment"), eq("Test User")))
                .thenReturn(sampleComment);

        mockMvc.perform(post("/api/tickets/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentId").value(1))
                .andExpect(jsonPath("$.comments").value("Test comment"))
                .andExpect(jsonPath("$.author").value("Test User"));

        verify(ticketCommentsService).addCommentToTicket(1, "This is a test comment", "Test User");
    }

    @Test
    @DisplayName("US7: Add comment - should reject empty comment")
    void testAddEmptyComment() throws Exception {
        Map<String, String> commentData = new HashMap<>();
        commentData.put("comment", "");
        commentData.put("author", "Test User");

        mockMvc.perform(post("/api/tickets/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentData)))
                .andExpect(status().isBadRequest());

        verify(ticketCommentsService, never()).addCommentToTicket(anyInt(), anyString(), anyString());
    }

    @Test
    @DisplayName("US7: Add comment - should reject whitespace-only comment")
    void testAddWhitespaceOnlyComment() throws Exception {
        Map<String, String> commentData = new HashMap<>();
        commentData.put("comment", "   ");
        commentData.put("author", "Test User");

        mockMvc.perform(post("/api/tickets/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentData)))
                .andExpect(status().isBadRequest());

        verify(ticketCommentsService, never()).addCommentToTicket(anyInt(), anyString(), anyString());
    }

    @Test
    @DisplayName("US7: Add comment - should reject missing author")
    void testAddCommentWithoutAuthor() throws Exception {
        Map<String, String> commentData = new HashMap<>();
        commentData.put("comment", "Test comment");
        commentData.put("author", "");

        mockMvc.perform(post("/api/tickets/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentData)))
                .andExpect(status().isBadRequest());

        verify(ticketCommentsService, never()).addCommentToTicket(anyInt(), anyString(), anyString());
    }

    @Test
    @DisplayName("US7 Edge Case: Very long comment text")
    void testAddVeryLongComment() throws Exception {
        String longComment = "A".repeat(10000);
        Map<String, String> commentData = new HashMap<>();
        commentData.put("comment", longComment);
        commentData.put("author", "Test User");

        TicketCommentsTbl longCommentObj = new TicketCommentsTbl();
        longCommentObj.setCommentId(1);
        longCommentObj.setComments(longComment);
        longCommentObj.setAuthor("Test User");

        when(ticketCommentsService.addCommentToTicket(eq(1), eq(longComment), eq("Test User")))
                .thenReturn(longCommentObj);

        mockMvc.perform(post("/api/tickets/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments").value(longComment));
    }

    @Test
    @DisplayName("US7 Edge Case: Ticket has many comments added in rapid succession")
    void testAddMultipleCommentsRapidly() throws Exception {
        for (int i = 1; i <= 10; i++) {
            Map<String, String> commentData = new HashMap<>();
            commentData.put("comment", "Comment " + i);
            commentData.put("author", "Test User");

            TicketCommentsTbl comment = new TicketCommentsTbl();
            comment.setCommentId(i);
            comment.setComments("Comment " + i);
            comment.setAuthor("Test User");

            when(ticketCommentsService.addCommentToTicket(eq(1), eq("Comment " + i), eq("Test User")))
                    .thenReturn(comment);

            mockMvc.perform(post("/api/tickets/1/comments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(commentData)))
                    .andExpect(status().isOk());
        }

        verify(ticketCommentsService, times(10)).addCommentToTicket(eq(1), anyString(), eq("Test User"));
    }

    @Test
    @DisplayName("US7: Get comments count for ticket")
    void testGetCommentsCount() throws Exception {
        when(ticketCommentsService.countCommentsByTicketId(1)).thenReturn(5L);

        mockMvc.perform(get("/api/tickets/1/comments/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(5));

        verify(ticketCommentsService).countCommentsByTicketId(1);
    }

    @Test
    @DisplayName("US7 Edge Case: Ticket has no comments")
    void testGetCommentsCountZero() throws Exception {
        when(ticketCommentsService.countCommentsByTicketId(1)).thenReturn(0L);

        mockMvc.perform(get("/api/tickets/1/comments/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(0));
    }

    // ========================================
    // USER STORY 8: Navigation to Create Ticket
    // ========================================

    @Test
    @DisplayName("US8: Show create ticket form - should load with empty ticket")
    void testShowCreateTicketForm() throws Exception {
        mockMvc.perform(get("/tickets/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-ticket"))
                .andExpect(model().attributeExists("ticket"));
    }

    @Test
    @DisplayName("US8: Show index page")
    void testShowIndexPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    // ========================================
    // Additional API Tests
    // ========================================

    @Test
    @DisplayName("Update ticket - should update and return ticket")
    void testUpdateTicket() throws Exception {
        TicketTbl updatedTicket = new TicketTbl();
        updatedTicket.setTitle("Updated Title");
        updatedTicket.setStatus("ONGOING");
        updatedTicket.setDescription("Updated description");

        TicketTbl savedTicket = new TicketTbl();
        savedTicket.setTicketId(1);
        savedTicket.setTitle("Updated Title");
        savedTicket.setStatus("ONGOING");
        savedTicket.setDescription("Updated description");
        savedTicket.setCreatedDate(sampleTicket.getCreatedDate());
        savedTicket.setUpdateDate(LocalDateTime.now());

        when(ticketService.findByTicketId(1)).thenReturn(sampleTicket);
        when(ticketService.saveTicket(any(TicketTbl.class))).thenReturn(savedTicket);

        mockMvc.perform(put("/api/tickets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTicket)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketId").value(1))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.status").value("ONGOING"));

        verify(ticketService).findByTicketId(1);
        verify(ticketService).saveTicket(any(TicketTbl.class));
    }

    @Test
    @DisplayName("Update ticket - should return null for non-existent ticket")
    void testUpdateNonExistentTicket() throws Exception {
        TicketTbl updatedTicket = new TicketTbl();
        updatedTicket.setTitle("Updated Title");

        when(ticketService.findByTicketId(999)).thenReturn(null);

        mockMvc.perform(put("/api/tickets/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTicket)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(ticketService).findByTicketId(999);
        verify(ticketService, never()).saveTicket(any(TicketTbl.class));
    }

    @Test
    @DisplayName("Delete ticket - should delete successfully")
    void testDeleteTicket() throws Exception {
        when(ticketService.findByTicketId(1)).thenReturn(sampleTicket);
        doNothing().when(ticketService).deleteTicket(sampleTicket);

        mockMvc.perform(delete("/api/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Ticket deleted successfully"));

        verify(ticketService).findByTicketId(1);
        verify(ticketService).deleteTicket(sampleTicket);
    }

    @Test
    @DisplayName("Show edit ticket form - should load with ticket data")
    void testShowEditTicketForm() throws Exception {
        when(ticketService.findByTicketId(1)).thenReturn(sampleTicket);

        mockMvc.perform(get("/tickets/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-ticket"))
                .andExpect(model().attributeExists("ticket"))
                .andExpect(model().attribute("ticket", sampleTicket));

        verify(ticketService).findByTicketId(1);
    }

    @Test
    @DisplayName("Get ticket comments list")
    void testGetTicketCommentsList() throws Exception {
        when(ticketService.findByTicketId(1)).thenReturn(sampleTicket);
        when(ticketService.ticketDetailsList(1)).thenReturn(sampleTickets);

        mockMvc.perform(get("/api/tickets/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(ticketService).findByTicketId(1);
        verify(ticketService).ticketDetailsList(1);
    }

    // ========================================
    // Status History Tests
    // ========================================

    @Test
    @DisplayName("Add status history to ticket - should save and return status history")
    void testAddStatusHistory() throws Exception {
        Map<String, String> statusData = new HashMap<>();
        statusData.put("status", "ONGOING");

        when(ticketStatusHistoryService.addStatusHistoryToTicket(1, "ONGOING"))
                .thenReturn(sampleStatusHistory);

        mockMvc.perform(post("/api/tickets/1/status-history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusId").value(1))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(ticketStatusHistoryService).addStatusHistoryToTicket(1, "ONGOING");
    }

    @Test
    @DisplayName("Add status history - should reject empty status")
    void testAddEmptyStatusHistory() throws Exception {
        Map<String, String> statusData = new HashMap<>();
        statusData.put("status", "");

        mockMvc.perform(post("/api/tickets/1/status-history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusData)))
                .andExpect(status().isBadRequest());

        verify(ticketStatusHistoryService, never()).addStatusHistoryToTicket(anyInt(), anyString());
    }

    @Test
    @DisplayName("Get latest status for ticket")
    void testGetLatestStatus() throws Exception {
        when(ticketStatusHistoryService.getLatestStatus(1)).thenReturn(sampleStatusHistory);

        mockMvc.perform(get("/api/tickets/1/latest-status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusId").value(1))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(ticketStatusHistoryService).getLatestStatus(1);
    }

    @Test
    @DisplayName("Edge Case: Handle exception when adding comment")
    void testAddCommentException() throws Exception {
        Map<String, String> commentData = new HashMap<>();
        commentData.put("comment", "Test comment");
        commentData.put("author", "Test User");

        when(ticketCommentsService.addCommentToTicket(anyInt(), anyString(), anyString()))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/api/tickets/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentData)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Edge Case: Handle exception when adding status history")
    void testAddStatusHistoryException() throws Exception {
        Map<String, String> statusData = new HashMap<>();
        statusData.put("status", "ONGOING");

        when(ticketStatusHistoryService.addStatusHistoryToTicket(anyInt(), anyString()))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/api/tickets/1/status-history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusData)))
                .andExpect(status().isInternalServerError());
    }
}