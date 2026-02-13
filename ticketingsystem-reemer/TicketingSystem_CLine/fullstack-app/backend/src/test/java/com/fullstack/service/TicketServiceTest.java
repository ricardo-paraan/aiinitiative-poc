package com.fullstack.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fullstack.entity.Ticket;
import com.fullstack.repository.TicketRepository;

/**
 * Unit tests for TicketService
 * Tests cover: User Dashboard, Create New Ticket, View Ticket, and Edge Cases
 */
@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private StatusHistoryService statusHistoryService;

    @InjectMocks
    private TicketService ticketService;

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

    // ==================== USER DASHBOARD TESTS ====================

    @Test
    @DisplayName("Dashboard: Get all tickets - verify correct counts")
    void testGetAllTickets_VerifyCorrectCounts() {
        List<Ticket> tickets = Arrays.asList(pendingTicket, inProgressTicket, resolvedTicket);
        when(ticketRepository.findAll()).thenReturn(tickets);

        List<Ticket> result = ticketService.getAllTickets();

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(ticketRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Dashboard: Get tickets by status - Pending")
    void testGetTicketsByStatus_Pending() {
        when(ticketRepository.findByStatus("Pending")).thenReturn(Collections.singletonList(pendingTicket));

        List<Ticket> result = ticketService.getTicketsByStatus("Pending");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Pending", result.get(0).getStatus());
        verify(ticketRepository, times(1)).findByStatus("Pending");
    }

    @Test
    @DisplayName("Dashboard: Get tickets by status - In Progress")
    void testGetTicketsByStatus_InProgress() {
        when(ticketRepository.findByStatus("In Progress")).thenReturn(Collections.singletonList(inProgressTicket));

        List<Ticket> result = ticketService.getTicketsByStatus("In Progress");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("In Progress", result.get(0).getStatus());
    }

    @Test
    @DisplayName("Dashboard: Get tickets by status - Resolved")
    void testGetTicketsByStatus_Resolved() {
        when(ticketRepository.findByStatus("Resolved")).thenReturn(Collections.singletonList(resolvedTicket));

        List<Ticket> result = ticketService.getTicketsByStatus("Resolved");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Resolved", result.get(0).getStatus());
    }

    @Test
    @DisplayName("Dashboard: Filter by System Name")
    void testGetTicketsBySystemName() {
        when(ticketRepository.findBySystemName("CRM System")).thenReturn(Collections.singletonList(testTicket));

        List<Ticket> result = ticketService.getTicketsBySystemName("CRM System");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("CRM System", result.get(0).getSystemName());
        verify(ticketRepository, times(1)).findBySystemName("CRM System");
    }

    @Test
    @DisplayName("Dashboard: Filter by Category")
    void testGetTicketsByCategory() {
        when(ticketRepository.findByCategory("Bug")).thenReturn(Collections.singletonList(testTicket));

        List<Ticket> result = ticketService.getTicketsByCategory("Bug");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Bug", result.get(0).getCategory());
        verify(ticketRepository, times(1)).findByCategory("Bug");
    }

    @Test
    @DisplayName("Dashboard: Search tickets by title")
    void testSearchTicketsByTitle() {
        when(ticketRepository.findByTitleContainingIgnoreCase("Test")).thenReturn(Collections.singletonList(testTicket));

        List<Ticket> result = ticketService.searchTicketsByTitle("Test");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getTitle().contains("Test"));
        verify(ticketRepository, times(1)).findByTitleContainingIgnoreCase("Test");
    }

    @Test
    @DisplayName("Dashboard: Verify ticket list fields are present")
    void testGetAllTickets_VerifyFields() {
        when(ticketRepository.findAll()).thenReturn(Collections.singletonList(testTicket));

        List<Ticket> result = ticketService.getAllTickets();

        assertNotNull(result);
        assertEquals(1, result.size());
        Ticket ticket = result.get(0);
        assertNotNull(ticket.getTicketId());
        assertNotNull(ticket.getTitle());
        assertNotNull(ticket.getStatus());
        assertNotNull(ticket.getSystemName());
        assertNotNull(ticket.getCategory());
        assertNotNull(ticket.getCreatedDate());
        assertNotNull(ticket.getUpdateDate());
    }

    @Test
    @DisplayName("Dashboard: No tickets exist - empty list")
    void testGetAllTickets_EmptyList() {
        when(ticketRepository.findAll()).thenReturn(Collections.emptyList());

        List<Ticket> result = ticketService.getAllTickets();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(ticketRepository, times(1)).findAll();
    }

    // ==================== CREATE NEW TICKET TESTS ====================

    @Test
    @DisplayName("Create Ticket: Valid inputs - success")
    void testCreateTicket_ValidInputs() {
        when(ticketRepository.save(any(Ticket.class))).thenReturn(testTicket);
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(testTicket);

        assertNotNull(result);
        assertEquals("Test Ticket", result.getTitle());
        assertEquals("CRM System", result.getSystemName());
        assertEquals("Bug", result.getCategory());
        assertEquals("Test description", result.getDescription());
        assertNotNull(result.getCreatedDate());
        assertNotNull(result.getUpdateDate());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
        verify(statusHistoryService, times(1)).createStatusHistory(any(), any(), any(), any());
    }

    @Test
    @DisplayName("Create Ticket: Timestamps are set correctly")
    void testCreateTicket_TimestampsSet() {
        Ticket newTicket = new Ticket();
        newTicket.setTitle("New Ticket");
        newTicket.setStatus("Pending");
        
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> {
            Ticket saved = invocation.getArgument(0);
            saved.setTicketId(1);
            return saved;
        });
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(newTicket);

        assertNotNull(result.getCreatedDate());
        assertNotNull(result.getUpdateDate());
        assertEquals(LocalDate.now(), result.getCreatedDate());
        assertEquals(LocalDate.now(), result.getUpdateDate());
    }

    @Test
    @DisplayName("Create Ticket: With optional attachments")
    void testCreateTicket_WithAttachments() {
        testTicket.setAttachmentId(1);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(testTicket);
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(testTicket);

        assertNotNull(result);
        assertNotNull(result.getAttachmentId());
        assertEquals(1, result.getAttachmentId());
    }

    @Test
    @DisplayName("Create Ticket: Long title input")
    void testCreateTicket_LongTitle() {
        String longTitle = "A".repeat(100);
        testTicket.setTitle(longTitle);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(testTicket);
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(testTicket);

        assertNotNull(result);
        assertEquals(longTitle, result.getTitle());
    }

    @Test
    @DisplayName("Create Ticket: Long description input")
    void testCreateTicket_LongDescription() {
        String longDescription = "A".repeat(1000);
        testTicket.setDescription(longDescription);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(testTicket);
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(testTicket);

        assertNotNull(result);
        assertEquals(longDescription, result.getDescription());
    }

    @Test
    @DisplayName("Create Ticket: Special characters in title")
    void testCreateTicket_SpecialCharactersInTitle() {
        testTicket.setTitle("Test @#$%^&*() Ticket");
        when(ticketRepository.save(any(Ticket.class))).thenReturn(testTicket);
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(testTicket);

        assertNotNull(result);
        assertEquals("Test @#$%^&*() Ticket", result.getTitle());
    }

    @Test
    @DisplayName("Create Ticket: Non-ASCII characters")
    void testCreateTicket_NonASCIICharacters() {
        testTicket.setTitle("テストチケット");
        testTicket.setDescription("日本語の説明");
        when(ticketRepository.save(any(Ticket.class))).thenReturn(testTicket);
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(testTicket);

        assertNotNull(result);
        assertEquals("テストチケット", result.getTitle());
        assertEquals("日本語の説明", result.getDescription());
    }

    // ==================== VIEW TICKET TESTS ====================

    @Test
    @DisplayName("View Ticket: Retrieve ticket by ID - success")
    void testGetTicketById_Success() {
        when(ticketRepository.findById(1)).thenReturn(Optional.of(testTicket));

        Optional<Ticket> result = ticketService.getTicketById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getTicketId());
        assertEquals("Test Ticket", result.get().getTitle());
        assertEquals("Test description", result.get().getDescription());
        assertEquals("CRM System", result.get().getSystemName());
        assertEquals("Bug", result.get().getCategory());
        assertEquals("John Doe", result.get().getAuthor());
        assertNotNull(result.get().getCreatedDate());
        verify(ticketRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("View Ticket: Ticket does not exist")
    void testGetTicketById_NotFound() {
        when(ticketRepository.findById(999)).thenReturn(Optional.empty());

        Optional<Ticket> result = ticketService.getTicketById(999);

        assertFalse(result.isPresent());
        verify(ticketRepository, times(1)).findById(999);
    }

    @Test
    @DisplayName("View Ticket: Verify all ticket details")
    void testGetTicketById_VerifyAllDetails() {
        testTicket.setStatusComment("Initial status");
        when(ticketRepository.findById(1)).thenReturn(Optional.of(testTicket));

        Optional<Ticket> result = ticketService.getTicketById(1);

        assertTrue(result.isPresent());
        Ticket ticket = result.get();
        assertNotNull(ticket.getTicketId());
        assertNotNull(ticket.getTitle());
        assertNotNull(ticket.getDescription());
        assertNotNull(ticket.getSystemName());
        assertNotNull(ticket.getCategory());
        assertNotNull(ticket.getAuthor());
        assertNotNull(ticket.getCreatedDate());
        assertNotNull(ticket.getStatus());
        assertNotNull(ticket.getStatusComment());
    }

    // ==================== UPDATE TICKET TESTS ====================

    @Test
    @DisplayName("Update Ticket: Update ticket details")
    void testUpdateTicket_Success() {
        Ticket updatedDetails = new Ticket();
        updatedDetails.setTitle("Updated Title");
        updatedDetails.setAuthor("Jane Doe");
        updatedDetails.setSystemName("ERP System");
        updatedDetails.setCategory("Feature");
        updatedDetails.setDescription("Updated description");
        updatedDetails.setStatus("Pending");

        when(ticketRepository.findById(1)).thenReturn(Optional.of(testTicket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(testTicket);

        Ticket result = ticketService.updateTicket(1, updatedDetails);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Jane Doe", result.getAuthor());
        assertEquals("ERP System", result.getSystemName());
        assertEquals("Feature", result.getCategory());
        assertEquals("Updated description", result.getDescription());
        verify(ticketRepository, times(1)).findById(1);
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    @DisplayName("Update Ticket: Status change triggers history")
    void testUpdateTicket_StatusChangeTriggersHistory() {
        Ticket updatedDetails = new Ticket();
        updatedDetails.setTitle("Test Ticket");
        updatedDetails.setAuthor("John Doe");
        updatedDetails.setSystemName("CRM System");
        updatedDetails.setCategory("Bug");
        updatedDetails.setDescription("Test description");
        updatedDetails.setStatus("In Progress");
        updatedDetails.setStatusComment("Working on it");

        when(ticketRepository.findById(1)).thenReturn(Optional.of(testTicket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(testTicket);
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.updateTicket(1, updatedDetails);

        assertNotNull(result);
        verify(statusHistoryService, times(1)).createStatusHistory(any(), any(), any(), any());
    }

    @Test
    @DisplayName("Update Ticket: Update date is refreshed")
    void testUpdateTicket_UpdateDateRefreshed() {
        Ticket updatedDetails = new Ticket();
        updatedDetails.setTitle("Updated Title");
        updatedDetails.setAuthor("John Doe");
        updatedDetails.setSystemName("CRM System");
        updatedDetails.setCategory("Bug");
        updatedDetails.setDescription("Test description");
        updatedDetails.setStatus("Pending");

        when(ticketRepository.findById(1)).thenReturn(Optional.of(testTicket));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> {
            Ticket saved = invocation.getArgument(0);
            assertEquals(LocalDate.now(), saved.getUpdateDate());
            return saved;
        });

        ticketService.updateTicket(1, updatedDetails);

        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    @DisplayName("Update Ticket: Non-existent ticket")
    void testUpdateTicket_NotFound() {
        Ticket updatedDetails = new Ticket();
        when(ticketRepository.findById(999)).thenReturn(Optional.empty());

        Ticket result = ticketService.updateTicket(999, updatedDetails);

        assertNull(result);
        verify(ticketRepository, times(1)).findById(999);
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    // ==================== DELETE TICKET TESTS ====================

    @Test
    @DisplayName("Delete Ticket: Success")
    void testDeleteTicket_Success() {
        when(ticketRepository.existsById(1)).thenReturn(true);
        doNothing().when(ticketRepository).deleteById(1);

        boolean result = ticketService.deleteTicket(1);

        assertTrue(result);
        verify(ticketRepository, times(1)).existsById(1);
        verify(ticketRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Delete Ticket: Non-existent ticket")
    void testDeleteTicket_NotFound() {
        when(ticketRepository.existsById(999)).thenReturn(false);

        boolean result = ticketService.deleteTicket(999);

        assertFalse(result);
        verify(ticketRepository, times(1)).existsById(999);
        verify(ticketRepository, never()).deleteById(anyInt());
    }

    // ==================== EDGE CASES ====================

    @Test
    @DisplayName("Edge Case: Filter by non-existent status")
    void testGetTicketsByStatus_NonExistent() {
        when(ticketRepository.findByStatus("NonExistent")).thenReturn(Collections.emptyList());

        List<Ticket> result = ticketService.getTicketsByStatus("NonExistent");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Edge Case: Filter by non-existent system")
    void testGetTicketsBySystemName_NonExistent() {
        when(ticketRepository.findBySystemName("NonExistent")).thenReturn(Collections.emptyList());

        List<Ticket> result = ticketService.getTicketsBySystemName("NonExistent");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Edge Case: Filter by non-existent category")
    void testGetTicketsByCategory_NonExistent() {
        when(ticketRepository.findByCategory("NonExistent")).thenReturn(Collections.emptyList());

        List<Ticket> result = ticketService.getTicketsByCategory("NonExistent");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Edge Case: Search with empty string")
    void testSearchTicketsByTitle_EmptyString() {
        when(ticketRepository.findByTitleContainingIgnoreCase("")).thenReturn(Collections.emptyList());

        List<Ticket> result = ticketService.searchTicketsByTitle("");

        assertNotNull(result);
        verify(ticketRepository, times(1)).findByTitleContainingIgnoreCase("");
    }

    @Test
    @DisplayName("Edge Case: Get tickets by author")
    void testGetTicketsByAuthor() {
        when(ticketRepository.findByAuthor("John Doe")).thenReturn(Collections.singletonList(testTicket));

        List<Ticket> result = ticketService.getTicketsByAuthor("John Doe");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getAuthor());
    }

    @Test
    @DisplayName("Edge Case: Multiple tickets with same status")
    void testGetTicketsByStatus_MultipleTickets() {
        Ticket ticket2 = createTicket(4, "Another Pending", "Pending");
        when(ticketRepository.findByStatus("Pending")).thenReturn(Arrays.asList(pendingTicket, ticket2));

        List<Ticket> result = ticketService.getTicketsByStatus("Pending");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(t -> "Pending".equals(t.getStatus())));
    }

    @Test
    @DisplayName("Edge Case: Case-insensitive search")
    void testSearchTicketsByTitle_CaseInsensitive() {
        when(ticketRepository.findByTitleContainingIgnoreCase("test")).thenReturn(Collections.singletonList(testTicket));

        List<Ticket> result = ticketService.searchTicketsByTitle("test");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(ticketRepository, times(1)).findByTitleContainingIgnoreCase("test");
    }
}