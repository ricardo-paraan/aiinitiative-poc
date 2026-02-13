package com.aws.poc.ticketingsystem.service;

import com.aws.poc.ticketingsystem.entity.TicketTbl;
import com.aws.poc.ticketingsystem.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TicketServiceImpl
 * Tests business logic for ticket operations
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Ticket Service Implementation Tests")
class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private TicketTbl sampleTicket;
    private List<TicketTbl> sampleTickets;

    @BeforeEach
    void setUp() {
        sampleTicket = new TicketTbl();
        sampleTicket.setTicketId(1);
        sampleTicket.setTitle("Test Ticket");
        sampleTicket.setAuthor("Test User");
        sampleTicket.setSystemName("ERP System");
        sampleTicket.setCategory("Bug");
        sampleTicket.setDescription("Test description");
        sampleTicket.setStatus("PENDING");
        sampleTicket.setAttachmentId(0);
        sampleTicket.setCommentId(0);
        sampleTicket.setCreatedDate(LocalDateTime.now());
        sampleTicket.setUpdateDate(LocalDateTime.now());

        sampleTickets = new ArrayList<>();
        sampleTickets.add(sampleTicket);
    }

    // ========================================
    // Find All Tickets Tests
    // ========================================

    @Test
    @DisplayName("Find all tickets - should return list of tickets")
    void testFindAll() {
        when(ticketRepository.findAll()).thenReturn(sampleTickets);

        List<TicketTbl> result = ticketService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Ticket", result.get(0).getTitle());
        verify(ticketRepository).findAll();
    }

    @Test
    @DisplayName("Find all tickets - should return empty list when no tickets exist")
    void testFindAllEmpty() {
        when(ticketRepository.findAll()).thenReturn(new ArrayList<>());

        List<TicketTbl> result = ticketService.findAll();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(ticketRepository).findAll();
    }

    @Test
    @DisplayName("Find all tickets - should handle large dataset")
    void testFindAllLargeDataset() {
        List<TicketTbl> largeList = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            TicketTbl ticket = new TicketTbl();
            ticket.setTicketId(i);
            ticket.setTitle("Ticket " + i);
            largeList.add(ticket);
        }

        when(ticketRepository.findAll()).thenReturn(largeList);

        List<TicketTbl> result = ticketService.findAll();

        assertNotNull(result);
        assertEquals(1000, result.size());
        verify(ticketRepository).findAll();
    }

    // ========================================
    // Find Ticket By ID Tests
    // ========================================

    @Test
    @DisplayName("Find ticket by ID - should return ticket when found")
    void testFindByTicketId() {
        when(ticketRepository.findById(1)).thenReturn(Optional.of(sampleTicket));

        TicketTbl result = ticketService.findByTicketId(1);

        assertNotNull(result);
        assertEquals(1, result.getTicketId());
        assertEquals("Test Ticket", result.getTitle());
        assertEquals("Test User", result.getAuthor());
        verify(ticketRepository).findById(1);
    }

    @Test
    @DisplayName("Find ticket by ID - should throw exception when not found")
    void testFindByTicketIdNotFound() {
        when(ticketRepository.findById(999)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ticketService.findByTicketId(999);
        });

        assertEquals("Cannot find ticket with an id: 999", exception.getMessage());
        verify(ticketRepository).findById(999);
    }

    @Test
    @DisplayName("Find ticket by ID - should handle zero ID")
    void testFindByTicketIdZero() {
        when(ticketRepository.findById(0)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ticketService.findByTicketId(0);
        });

        assertTrue(exception.getMessage().contains("Cannot find ticket"));
        verify(ticketRepository).findById(0);
    }

    @Test
    @DisplayName("Find ticket by ID - should handle negative ID")
    void testFindByTicketIdNegative() {
        when(ticketRepository.findById(-1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ticketService.findByTicketId(-1);
        });

        assertTrue(exception.getMessage().contains("Cannot find ticket"));
        verify(ticketRepository).findById(-1);
    }

    // ========================================
    // Save Ticket Tests
    // ========================================

    @Test
    @DisplayName("Save ticket - should save new ticket successfully")
    void testSaveNewTicket() {
        TicketTbl newTicket = new TicketTbl();
        newTicket.setTitle("New Ticket");
        newTicket.setSystemName("ERP");
        newTicket.setCategory("Feature");
        newTicket.setDescription("New feature request");
        newTicket.setStatus("PENDING");

        TicketTbl savedTicket = new TicketTbl();
        savedTicket.setTicketId(2);
        savedTicket.setTitle(newTicket.getTitle());
        savedTicket.setSystemName(newTicket.getSystemName());
        savedTicket.setCategory(newTicket.getCategory());
        savedTicket.setDescription(newTicket.getDescription());
        savedTicket.setStatus(newTicket.getStatus());
        savedTicket.setCreatedDate(LocalDateTime.now());
        savedTicket.setUpdateDate(LocalDateTime.now());

        when(ticketRepository.save(any(TicketTbl.class))).thenReturn(savedTicket);

        TicketTbl result = ticketService.saveTicket(newTicket);

        assertNotNull(result);
        assertEquals(2, result.getTicketId());
        assertEquals("New Ticket", result.getTitle());
        assertNotNull(result.getCreatedDate());
        assertNotNull(result.getUpdateDate());
        verify(ticketRepository).save(any(TicketTbl.class));
    }

    @Test
    @DisplayName("Save ticket - should update existing ticket")
    void testUpdateExistingTicket() {
        sampleTicket.setTitle("Updated Title");
        sampleTicket.setStatus("ONGOING");
        sampleTicket.setUpdateDate(LocalDateTime.now());

        when(ticketRepository.save(any(TicketTbl.class))).thenReturn(sampleTicket);

        TicketTbl result = ticketService.saveTicket(sampleTicket);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("ONGOING", result.getStatus());
        verify(ticketRepository).save(any(TicketTbl.class));
    }

    @Test
    @DisplayName("Save ticket - should handle ticket with minimum fields")
    void testSaveTicketMinimumFields() {
        TicketTbl minTicket = new TicketTbl();
        minTicket.setTitle("A");
        minTicket.setSystemName("E");
        minTicket.setCategory("B");
        minTicket.setDescription("D");

        when(ticketRepository.save(any(TicketTbl.class))).thenReturn(minTicket);

        TicketTbl result = ticketService.saveTicket(minTicket);

        assertNotNull(result);
        assertEquals("A", result.getTitle());
        verify(ticketRepository).save(any(TicketTbl.class));
    }

    @Test
    @DisplayName("Save ticket - should handle ticket with very long text")
    void testSaveTicketLongText() {
        String longTitle = "A".repeat(1000);
        String longDescription = "B".repeat(10000);

        TicketTbl longTicket = new TicketTbl();
        longTicket.setTitle(longTitle);
        longTicket.setDescription(longDescription);
        longTicket.setSystemName("ERP");
        longTicket.setCategory("Bug");

        when(ticketRepository.save(any(TicketTbl.class))).thenReturn(longTicket);

        TicketTbl result = ticketService.saveTicket(longTicket);

        assertNotNull(result);
        assertEquals(longTitle, result.getTitle());
        assertEquals(longDescription, result.getDescription());
        verify(ticketRepository).save(any(TicketTbl.class));
    }

    @Test
    @DisplayName("Save ticket - should handle special characters in fields")
    void testSaveTicketSpecialCharacters() {
        TicketTbl specialTicket = new TicketTbl();
        specialTicket.setTitle("Test @#$% Ticket");
        specialTicket.setDescription("Special chars: ;DROP TABLE; <script>alert('xss')</script>");
        specialTicket.setSystemName("ERP");
        specialTicket.setCategory("Bug");

        when(ticketRepository.save(any(TicketTbl.class))).thenReturn(specialTicket);

        TicketTbl result = ticketService.saveTicket(specialTicket);

        assertNotNull(result);
        assertEquals("Test @#$% Ticket", result.getTitle());
        assertTrue(result.getDescription().contains(";DROP TABLE;"));
        verify(ticketRepository).save(any(TicketTbl.class));
    }

    // ========================================
    // Delete Ticket Tests
    // ========================================

    @Test
    @DisplayName("Delete ticket - should delete successfully")
    void testDeleteTicket() {
        doNothing().when(ticketRepository).delete(any(TicketTbl.class));

        ticketService.deleteTicket(sampleTicket);

        verify(ticketRepository).delete(sampleTicket);
    }

    @Test
    @DisplayName("Delete ticket - should handle null ticket gracefully")
    void testDeleteNullTicket() {
        doNothing().when(ticketRepository).delete(any());

        assertDoesNotThrow(() -> ticketService.deleteTicket(null));

        verify(ticketRepository).delete(null);
    }

    // ========================================
    // Get Tickets By Status Tests
    // ========================================

    @Test
    @DisplayName("Get tickets by status - PENDING should return correct count")
    void testGetTicketsByStatusPending() {
        when(ticketRepository.countTicketsByStatus("PENDING")).thenReturn(5);

        int result = ticketService.getTicketsBystatus("PENDING");

        assertEquals(5, result);
        verify(ticketRepository).countTicketsByStatus("PENDING");
    }

    @Test
    @DisplayName("Get tickets by status - ONGOING should return correct count")
    void testGetTicketsByStatusOngoing() {
        when(ticketRepository.countTicketsByStatus("ONGOING")).thenReturn(3);

        int result = ticketService.getTicketsBystatus("ONGOING");

        assertEquals(3, result);
        verify(ticketRepository).countTicketsByStatus("ONGOING");
    }

    @Test
    @DisplayName("Get tickets by status - RESOLVED should return correct count")
    void testGetTicketsByStatusResolved() {
        when(ticketRepository.countTicketsByStatus("RESOLVED")).thenReturn(10);

        int result = ticketService.getTicketsBystatus("RESOLVED");

        assertEquals(10, result);
        verify(ticketRepository).countTicketsByStatus("RESOLVED");
    }

    @Test
    @DisplayName("Get tickets by status - should return zero for new user")
    void testGetTicketsByStatusZero() {
        when(ticketRepository.countTicketsByStatus("PENDING")).thenReturn(0);
        when(ticketRepository.countTicketsByStatus("ONGOING")).thenReturn(0);
        when(ticketRepository.countTicketsByStatus("RESOLVED")).thenReturn(0);

        assertEquals(0, ticketService.getTicketsBystatus("PENDING"));
        assertEquals(0, ticketService.getTicketsBystatus("ONGOING"));
        assertEquals(0, ticketService.getTicketsBystatus("RESOLVED"));
    }

    @Test
    @DisplayName("Get tickets by status - should handle invalid status")
    void testGetTicketsByStatusInvalid() {
        when(ticketRepository.countTicketsByStatus("INVALID_STATUS")).thenReturn(0);

        int result = ticketService.getTicketsBystatus("INVALID_STATUS");

        assertEquals(0, result);
        verify(ticketRepository).countTicketsByStatus("INVALID_STATUS");
    }

    @Test
    @DisplayName("Get tickets by status - should handle null status")
    void testGetTicketsByStatusNull() {
        when(ticketRepository.countTicketsByStatus(null)).thenReturn(0);

        int result = ticketService.getTicketsBystatus(null);

        assertEquals(0, result);
        verify(ticketRepository).countTicketsByStatus(null);
    }

    @Test
    @DisplayName("Get tickets by status - should handle empty string status")
    void testGetTicketsByStatusEmpty() {
        when(ticketRepository.countTicketsByStatus("")).thenReturn(0);

        int result = ticketService.getTicketsBystatus("");

        assertEquals(0, result);
        verify(ticketRepository).countTicketsByStatus("");
    }

    @Test
    @DisplayName("Get tickets by status - should handle large count")
    void testGetTicketsByStatusLargeCount() {
        when(ticketRepository.countTicketsByStatus("PENDING")).thenReturn(10000);

        int result = ticketService.getTicketsBystatus("PENDING");

        assertEquals(10000, result);
        verify(ticketRepository).countTicketsByStatus("PENDING");
    }

    // ========================================
    // Ticket Details List Tests
    // ========================================

    @Test
    @DisplayName("Get ticket details list - should return ticket with comments")
    void testTicketDetailsList() {
        when(ticketRepository.ticketDetailsList(1)).thenReturn(sampleTickets);

        List<TicketTbl> result = ticketService.ticketDetailsList(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getTicketId());
        verify(ticketRepository).ticketDetailsList(1);
    }

    @Test
    @DisplayName("Get ticket details list - should return empty list for non-existent ticket")
    void testTicketDetailsListNotFound() {
        when(ticketRepository.ticketDetailsList(999)).thenReturn(new ArrayList<>());

        List<TicketTbl> result = ticketService.ticketDetailsList(999);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(ticketRepository).ticketDetailsList(999);
    }

    @Test
    @DisplayName("Get ticket details list - should handle null ticket ID")
    void testTicketDetailsListNullId() {
        when(ticketRepository.ticketDetailsList(null)).thenReturn(new ArrayList<>());

        List<TicketTbl> result = ticketService.ticketDetailsList(null);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(ticketRepository).ticketDetailsList(null);
    }

    // ========================================
    // Edge Case Tests
    // ========================================

    @Test
    @DisplayName("Edge Case: Multiple rapid saves should all succeed")
    void testMultipleRapidSaves() {
        for (int i = 1; i <= 10; i++) {
            TicketTbl ticket = new TicketTbl();
            ticket.setTicketId(i);
            ticket.setTitle("Rapid Ticket " + i);

            when(ticketRepository.save(any(TicketTbl.class))).thenReturn(ticket);

            TicketTbl result = ticketService.saveTicket(ticket);
            assertNotNull(result);
        }

        verify(ticketRepository, times(10)).save(any(TicketTbl.class));
    }

    @Test
    @DisplayName("Edge Case: Save ticket with all fields populated")
    void testSaveTicketAllFields() {
        TicketTbl fullTicket = new TicketTbl();
        fullTicket.setTicketId(1);
        fullTicket.setTitle("Full Ticket");
        fullTicket.setAuthor("Author");
        fullTicket.setSystemName("System");
        fullTicket.setCategory("Category");
        fullTicket.setDescription("Description");
        fullTicket.setAttachmentId(1);
        fullTicket.setCommentId(1);
        fullTicket.setStatus("PENDING");
        fullTicket.setCreatedDate(LocalDateTime.now());
        fullTicket.setUpdateDate(LocalDateTime.now());

        when(ticketRepository.save(any(TicketTbl.class))).thenReturn(fullTicket);

        TicketTbl result = ticketService.saveTicket(fullTicket);

        assertNotNull(result);
        assertEquals("Full Ticket", result.getTitle());
        assertEquals("Author", result.getAuthor());
        assertEquals("System", result.getSystemName());
        assertEquals("Category", result.getCategory());
        assertEquals("Description", result.getDescription());
        assertEquals(1, result.getAttachmentId());
        assertEquals(1, result.getCommentId());
        assertEquals("PENDING", result.getStatus());
        assertNotNull(result.getCreatedDate());
        assertNotNull(result.getUpdateDate());
    }

    @Test
    @DisplayName("Edge Case: Repository throws exception")
    void testRepositoryException() {
        when(ticketRepository.findById(anyInt())).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> {
            ticketService.findByTicketId(1);
        });
    }

    @Test
    @DisplayName("Edge Case: Save operation throws exception")
    void testSaveException() {
        when(ticketRepository.save(any(TicketTbl.class))).thenThrow(new RuntimeException("Save failed"));

        assertThrows(RuntimeException.class, () -> {
            ticketService.saveTicket(sampleTicket);
        });
    }
}