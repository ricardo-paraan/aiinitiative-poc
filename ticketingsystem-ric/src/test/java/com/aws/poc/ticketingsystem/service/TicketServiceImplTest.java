package com.aws.poc.ticketingsystem.service;

import com.aws.poc.ticketingsystem.entity.TicketTbl;
import com.aws.poc.ticketingsystem.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TicketServiceImpl
 * Tests all business logic methods for ticket management
 */
@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private TicketTbl testTicket;

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
    }

    @Test
    void testFindAll_ReturnsAllTickets() {
        // Arrange
        TicketTbl ticket2 = new TicketTbl();
        ticket2.setTicketId(2);
        ticket2.setTitle("Second Ticket");
        List<TicketTbl> expectedTickets = Arrays.asList(testTicket, ticket2);
        when(ticketRepository.findAll()).thenReturn(expectedTickets);

        // Act
        List<TicketTbl> actualTickets = ticketService.findAll();

        // Assert
        assertNotNull(actualTickets);
        assertEquals(2, actualTickets.size());
        assertEquals(expectedTickets, actualTickets);
        verify(ticketRepository, times(1)).findAll();
    }

    @Test
    void testFindByTicketId_WhenTicketExists_ReturnsTicket() {
        // Arrange
        when(ticketRepository.findById(1)).thenReturn(Optional.of(testTicket));

        // Act
        TicketTbl result = ticketService.findByTicketId(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTicketId());
        assertEquals("Test Ticket", result.getTitle());
        verify(ticketRepository, times(1)).findById(1);
    }

    @Test
    void testFindByTicketId_WhenTicketDoesNotExist_ThrowsException() {
        // Arrange
        when(ticketRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ticketService.findByTicketId(999);
        });
        assertTrue(exception.getMessage().contains("Cannot find ticket with an id: 999"));
        verify(ticketRepository, times(1)).findById(999);
    }

    @Test
    void testSaveTicket_CreatesNewTicket() {
        // Arrange
        when(ticketRepository.save(any(TicketTbl.class))).thenReturn(testTicket);

        // Act
        TicketTbl savedTicket = ticketService.saveTicket(testTicket);

        // Assert
        assertNotNull(savedTicket);
        assertEquals(testTicket.getTicketId(), savedTicket.getTicketId());
        assertEquals(testTicket.getTitle(), savedTicket.getTitle());
        verify(ticketRepository, times(1)).save(testTicket);
    }

    @Test
    void testSaveTicket_UpdatesExistingTicket() {
        // Arrange
        testTicket.setTitle("Updated Title");
        when(ticketRepository.save(any(TicketTbl.class))).thenReturn(testTicket);

        // Act
        TicketTbl updatedTicket = ticketService.saveTicket(testTicket);

        // Assert
        assertNotNull(updatedTicket);
        assertEquals("Updated Title", updatedTicket.getTitle());
        verify(ticketRepository, times(1)).save(testTicket);
    }

    @Test
    void testDeleteTicket_DeletesSuccessfully() {
        // Arrange
        doNothing().when(ticketRepository).delete(any(TicketTbl.class));

        // Act
        ticketService.deleteTicket(testTicket);

        // Assert
        verify(ticketRepository, times(1)).delete(testTicket);
    }

    @Test
    void testGetTicketsByStatus_ReturnsPendingCount() {
        // Arrange
        when(ticketRepository.countTicketsByStatus("PENDING")).thenReturn(5);

        // Act
        int count = ticketService.getTicketsBystatus("PENDING");

        // Assert
        assertEquals(5, count);
        verify(ticketRepository, times(1)).countTicketsByStatus("PENDING");
    }

    @Test
    void testGetTicketsByStatus_ReturnsOngoingCount() {
        // Arrange
        when(ticketRepository.countTicketsByStatus("ONGOING")).thenReturn(3);

        // Act
        int count = ticketService.getTicketsBystatus("ONGOING");

        // Assert
        assertEquals(3, count);
        verify(ticketRepository, times(1)).countTicketsByStatus("ONGOING");
    }

    @Test
    void testGetTicketsByStatus_ReturnsResolvedCount() {
        // Arrange
        when(ticketRepository.countTicketsByStatus("RESOLVED")).thenReturn(10);

        // Act
        int count = ticketService.getTicketsBystatus("RESOLVED");

        // Assert
        assertEquals(10, count);
        verify(ticketRepository, times(1)).countTicketsByStatus("RESOLVED");
    }

    @Test
    void testGetTicketsByStatus_ReturnsZeroWhenNoTickets() {
        // Arrange
        when(ticketRepository.countTicketsByStatus("PENDING")).thenReturn(0);

        // Act
        int count = ticketService.getTicketsBystatus("PENDING");

        // Assert
        assertEquals(0, count);
        verify(ticketRepository, times(1)).countTicketsByStatus("PENDING");
    }

    @Test
    void testTicketDetailsList_ReturnsTicketDetails() {
        // Arrange
        List<TicketTbl> expectedDetails = Arrays.asList(testTicket);
        when(ticketRepository.ticketDetailsList(1)).thenReturn(expectedDetails);

        // Act
        List<TicketTbl> actualDetails = ticketService.ticketDetailsList(1);

        // Assert
        assertNotNull(actualDetails);
        assertEquals(1, actualDetails.size());
        assertEquals(expectedDetails, actualDetails);
        verify(ticketRepository, times(1)).ticketDetailsList(1);
    }

    @Test
    void testTicketDetailsList_ReturnsEmptyListWhenNoDetails() {
        // Arrange
        when(ticketRepository.ticketDetailsList(999)).thenReturn(Arrays.asList());

        // Act
        List<TicketTbl> actualDetails = ticketService.ticketDetailsList(999);

        // Assert
        assertNotNull(actualDetails);
        assertTrue(actualDetails.isEmpty());
        verify(ticketRepository, times(1)).ticketDetailsList(999);
    }
}