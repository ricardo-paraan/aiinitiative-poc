package com.fullstack.controller;

import com.fullstack.entity.Ticket;
import com.fullstack.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TicketController
 * Tests cover: REST API endpoints for ticket operations
 */
@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    private Ticket testTicket;
    private Ticket pendingTicket;
    private Ticket inProgressTicket;
    private Ticket resolvedTicket;

    @BeforeEach
    void setUp() {
        testTicket = new Ticket();
        testTicket.setTicketId(1);
        testTicket.setTitle("Test Ticket");
        testTicket.setAuthor("John Doe");
        testTicket.setSystemName("CRM System");
        testTicket.setCategory("Bug");
        testTicket.setDescription("Test description");
        testTicket.setStatus("Pending");
        testTicket.setCreatedDate(LocalDate.now());
        testTicket.setUpdateDate(LocalDate.now());

        pendingTicket = createTicket(1, "Pending Ticket", "Pending");
        inProgressTicket = createTicket(2, "In Progress Ticket", "In Progress");
        resolvedTicket = createTicket(3, "Resolved Ticket", "Resolved");
    }

    private Ticket createTicket(Integer id, String title, String status) {
        Ticket ticket = new Ticket();
        ticket.setTicketId(id);
        ticket.setTitle(title);
        ticket.setAuthor("Test Author");
        ticket.setSystemName("Test System");
        ticket.setCategory("Test Category");
        ticket.setDescription("Test Description");
        ticket.setStatus(status);
        ticket.setCreatedDate(LocalDate.now());
        ticket.setUpdateDate(LocalDate.now());
        return ticket;
    }

    // ==================== GET ALL TICKETS TESTS ====================

    @Test
    @DisplayName("GET /api/tickets - Get all tickets successfully")
    void testGetAllTickets_Success() {
        List<Ticket> tickets = Arrays.asList(pendingTicket, inProgressTicket, resolvedTicket);
        when(ticketService.getAllTickets()).thenReturn(tickets);

        ResponseEntity<List<Ticket>> response = ticketController.getAllTickets();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
        verify(ticketService, times(1)).getAllTickets();
    }

    @Test
    @DisplayName("GET /api/tickets - Empty list")
    void testGetAllTickets_EmptyList() {
        when(ticketService.getAllTickets()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Ticket>> response = ticketController.getAllTickets();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    // ==================== GET TICKET BY ID TESTS ====================

    @Test
    @DisplayName("GET /api/tickets/{id} - Get ticket by ID successfully")
    void testGetTicketById_Success() {
        when(ticketService.getTicketById(1)).thenReturn(Optional.of(testTicket));

        ResponseEntity<Ticket> response = ticketController.getTicketById(1);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTicketId());
        assertEquals("Test Ticket", response.getBody().getTitle());
        verify(ticketService, times(1)).getTicketById(1);
    }

    @Test
    @DisplayName("GET /api/tickets/{id} - Ticket not found")
    void testGetTicketById_NotFound() {
        when(ticketService.getTicketById(999)).thenReturn(Optional.empty());

        ResponseEntity<Ticket> response = ticketController.getTicketById(999);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(ticketService, times(1)).getTicketById(999);
    }

    @Test
    @DisplayName("GET /api/tickets/{id} - Verify all ticket details")
    void testGetTicketById_VerifyDetails() {
        when(ticketService.getTicketById(1)).thenReturn(Optional.of(testTicket));

        ResponseEntity<Ticket> response = ticketController.getTicketById(1);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Ticket ticket = response.getBody();
        assertNotNull(ticket);
        assertEquals("Test Ticket", ticket.getTitle());
        assertEquals("Test description", ticket.getDescription());
        assertEquals("CRM System", ticket.getSystemName());
        assertEquals("Bug", ticket.getCategory());
        assertEquals("John Doe", ticket.getAuthor());
        assertEquals("Pending", ticket.getStatus());
        assertNotNull(ticket.getCreatedDate());
    }

    // ==================== CREATE TICKET TESTS ====================

    @Test
    @DisplayName("POST /api/tickets - Create ticket successfully")
    void testCreateTicket_Success() {
        when(ticketService.createTicket(any(Ticket.class))).thenReturn(testTicket);

        ResponseEntity<Ticket> response = ticketController.createTicket(testTicket);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Ticket", response.getBody().getTitle());
        verify(ticketService, times(1)).createTicket(any(Ticket.class));
    }

    @Test
    @DisplayName("POST /api/tickets - Create with valid inputs")
    void testCreateTicket_ValidInputs() {
        Ticket newTicket = new Ticket();
        newTicket.setTitle("New Ticket");
        newTicket.setSystemName("ERP System");
        newTicket.setCategory("Feature");
        newTicket.setDescription("New feature request");
        newTicket.setStatus("Pending");
        newTicket.setAuthor("Jane Doe");

        when(ticketService.createTicket(any(Ticket.class))).thenReturn(newTicket);

        ResponseEntity<Ticket> response = ticketController.createTicket(newTicket);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("POST /api/tickets - Create with long title")
    void testCreateTicket_LongTitle() {
        String longTitle = "A".repeat(100);
        testTicket.setTitle(longTitle);
        when(ticketService.createTicket(any(Ticket.class))).thenReturn(testTicket);

        ResponseEntity<Ticket> response = ticketController.createTicket(testTicket);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(longTitle, response.getBody().getTitle());
    }

    @Test
    @DisplayName("POST /api/tickets - Create with special characters")
    void testCreateTicket_SpecialCharacters() {
        testTicket.setTitle("Test @#$%^&*() Ticket");
        when(ticketService.createTicket(any(Ticket.class))).thenReturn(testTicket);

        ResponseEntity<Ticket> response = ticketController.createTicket(testTicket);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Test @#$%^&*() Ticket", response.getBody().getTitle());
    }

    @Test
    @DisplayName("POST /api/tickets - Create with non-ASCII characters")
    void testCreateTicket_NonASCIICharacters() {
        testTicket.setTitle("テストチケット");
        testTicket.setDescription("日本語の説明");
        when(ticketService.createTicket(any(Ticket.class))).thenReturn(testTicket);

        ResponseEntity<Ticket> response = ticketController.createTicket(testTicket);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("テストチケット", response.getBody().getTitle());
    }

    @Test
    @DisplayName("POST /api/tickets - Immediate retrieval after creation")
    void testCreateTicket_ImmediateRetrieval() {
        when(ticketService.createTicket(any(Ticket.class))).thenReturn(testTicket);
        when(ticketService.getTicketById(1)).thenReturn(Optional.of(testTicket));

        // Create ticket
        ResponseEntity<Ticket> createResponse = ticketController.createTicket(testTicket);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        // Immediately retrieve
        ResponseEntity<Ticket> getResponse = ticketController.getTicketById(1);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals(createResponse.getBody().getTicketId(), getResponse.getBody().getTicketId());
    }

    // ==================== UPDATE TICKET TESTS ====================

    @Test
    @DisplayName("PUT /api/tickets/{id} - Update ticket successfully")
    void testUpdateTicket_Success() {
        Ticket updatedTicket = new Ticket();
        updatedTicket.setTitle("Updated Title");
        updatedTicket.setStatus("In Progress");

        when(ticketService.updateTicket(eq(1), any(Ticket.class))).thenReturn(testTicket);

        ResponseEntity<Ticket> response = ticketController.updateTicket(1, updatedTicket);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(ticketService, times(1)).updateTicket(eq(1), any(Ticket.class));
    }

    @Test
    @DisplayName("PUT /api/tickets/{id} - Update non-existent ticket")
    void testUpdateTicket_NotFound() {
        when(ticketService.updateTicket(eq(999), any(Ticket.class))).thenReturn(null);

        ResponseEntity<Ticket> response = ticketController.updateTicket(999, testTicket);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // ==================== DELETE TICKET TESTS ====================

    @Test
    @DisplayName("DELETE /api/tickets/{id} - Delete ticket successfully")
    void testDeleteTicket_Success() {
        when(ticketService.deleteTicket(1)).thenReturn(true);

        ResponseEntity<Void> response = ticketController.deleteTicket(1);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(ticketService, times(1)).deleteTicket(1);
    }

    @Test
    @DisplayName("DELETE /api/tickets/{id} - Delete non-existent ticket")
    void testDeleteTicket_NotFound() {
        when(ticketService.deleteTicket(999)).thenReturn(false);

        ResponseEntity<Void> response = ticketController.deleteTicket(999);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // ==================== FILTER BY STATUS TESTS ====================

    @Test
    @DisplayName("GET /api/tickets/status/{status} - Filter by Pending")
    void testGetTicketsByStatus_Pending() {
        when(ticketService.getTicketsByStatus("Pending")).thenReturn(Collections.singletonList(pendingTicket));

        ResponseEntity<List<Ticket>> response = ticketController.getTicketsByStatus("Pending");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Pending", response.getBody().get(0).getStatus());
    }

    @Test
    @DisplayName("GET /api/tickets/status/{status} - Filter by In Progress")
    void testGetTicketsByStatus_InProgress() {
        when(ticketService.getTicketsByStatus("In Progress")).thenReturn(Collections.singletonList(inProgressTicket));

        ResponseEntity<List<Ticket>> response = ticketController.getTicketsByStatus("In Progress");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("In Progress", response.getBody().get(0).getStatus());
    }

    @Test
    @DisplayName("GET /api/tickets/status/{status} - Filter by Resolved")
    void testGetTicketsByStatus_Resolved() {
        when(ticketService.getTicketsByStatus("Resolved")).thenReturn(Collections.singletonList(resolvedTicket));

        ResponseEntity<List<Ticket>> response = ticketController.getTicketsByStatus("Resolved");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Resolved", response.getBody().get(0).getStatus());
    }

    @Test
    @DisplayName("GET /api/tickets/status/{status} - No tickets with status")
    void testGetTicketsByStatus_NoTickets() {
        when(ticketService.getTicketsByStatus("Closed")).thenReturn(Collections.emptyList());

        ResponseEntity<List<Ticket>> response = ticketController.getTicketsByStatus("Closed");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    // ==================== FILTER BY SYSTEM TESTS ====================

    @Test
    @DisplayName("GET /api/tickets/system/{systemName} - Filter by system")
    void testGetTicketsBySystemName() {
        when(ticketService.getTicketsBySystemName("CRM System")).thenReturn(Collections.singletonList(testTicket));

        ResponseEntity<List<Ticket>> response = ticketController.getTicketsBySystemName("CRM System");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("CRM System", response.getBody().get(0).getSystemName());
    }

    @Test
    @DisplayName("GET /api/tickets/system/{systemName} - No tickets for system")
    void testGetTicketsBySystemName_NoTickets() {
        when(ticketService.getTicketsBySystemName("Unknown System")).thenReturn(Collections.emptyList());

        ResponseEntity<List<Ticket>> response = ticketController.getTicketsBySystemName("Unknown System");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    // ==================== FILTER BY CATEGORY TESTS ====================

    @Test
    @DisplayName("GET /api/tickets/category/{category} - Filter by category")
    void testGetTicketsByCategory() {
        when(ticketService.getTicketsByCategory("Bug")).thenReturn(Collections.singletonList(testTicket));

        ResponseEntity<List<Ticket>> response = ticketController.getTicketsByCategory("Bug");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Bug", response.getBody().get(0).getCategory());
    }

    @Test
    @DisplayName("GET /api/tickets/category/{category} - No tickets for category")
    void testGetTicketsByCategory_NoTickets() {
        when(ticketService.getTicketsByCategory("Unknown")).thenReturn(Collections.emptyList());

        ResponseEntity<List<Ticket>> response = ticketController.getTicketsByCategory("Unknown");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    // ==================== FILTER BY AUTHOR TESTS ====================

    @Test
    @DisplayName("GET /api/tickets/author/{author} - Filter by author")
    void testGetTicketsByAuthor() {
        when(ticketService.getTicketsByAuthor("John Doe")).thenReturn(Collections.singletonList(testTicket));

        ResponseEntity<List<Ticket>> response = ticketController.getTicketsByAuthor("John Doe");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("John Doe", response.getBody().get(0).getAuthor());
    }

    // ==================== SEARCH TESTS ====================

    @Test
    @DisplayName("GET /api/tickets/search?title={title} - Search by title")
    void testSearchTickets() {
        when(ticketService.searchTicketsByTitle("Test")).thenReturn(Collections.singletonList(testTicket));

        ResponseEntity<List<Ticket>> response = ticketController.searchTickets("Test");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().get(0).getTitle().contains("Test"));
    }

    @Test
    @DisplayName("GET /api/tickets/search?title={title} - Case insensitive search")
    void testSearchTickets_CaseInsensitive() {
        when(ticketService.searchTicketsByTitle("test")).thenReturn(Collections.singletonList(testTicket));

        ResponseEntity<List<Ticket>> response = ticketController.searchTickets("test");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("GET /api/tickets/search?title={title} - No results")
    void testSearchTickets_NoResults() {
        when(ticketService.searchTicketsByTitle("NonExistent")).thenReturn(Collections.emptyList());

        ResponseEntity<List<Ticket>> response = ticketController.searchTickets("NonExistent");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("GET /api/tickets/search?title={title} - Empty search string")
    void testSearchTickets_EmptyString() {
        when(ticketService.searchTicketsByTitle("")).thenReturn(Collections.emptyList());

        ResponseEntity<List<Ticket>> response = ticketController.searchTickets("");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    // ==================== EDGE CASES ====================

    @Test
    @DisplayName("Edge Case: Multiple tickets with same status")
    void testGetTicketsByStatus_MultipleTickets() {
        Ticket ticket2 = createTicket(4, "Another Pending", "Pending");
        when(ticketService.getTicketsByStatus("Pending")).thenReturn(Arrays.asList(pendingTicket, ticket2));

        ResponseEntity<List<Ticket>> response = ticketController.getTicketsByStatus("Pending");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    @DisplayName("Edge Case: Verify response contains all required fields")
    void testGetTicketById_AllFieldsPresent() {
        when(ticketService.getTicketById(1)).thenReturn(Optional.of(testTicket));

        ResponseEntity<Ticket> response = ticketController.getTicketById(1);

        Ticket ticket = response.getBody();
        assertNotNull(ticket);
        assertNotNull(ticket.getTicketId());
        assertNotNull(ticket.getTitle());
        assertNotNull(ticket.getStatus());
        assertNotNull(ticket.getSystemName());
        assertNotNull(ticket.getCategory());
        assertNotNull(ticket.getCreatedDate());
        assertNotNull(ticket.getUpdateDate());
    }
}