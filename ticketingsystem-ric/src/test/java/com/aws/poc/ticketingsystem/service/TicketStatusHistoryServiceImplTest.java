package com.aws.poc.ticketingsystem.service;

import com.aws.poc.ticketingsystem.entity.TicketStatusHistoryTbl;
import com.aws.poc.ticketingsystem.entity.TicketTbl;
import com.aws.poc.ticketingsystem.repository.TicketRepository;
import com.aws.poc.ticketingsystem.repository.TicketStatusHistoryRepository;
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
 * Unit tests for TicketStatusHistoryServiceImpl
 * Tests all business logic methods for ticket status history management
 */
@ExtendWith(MockitoExtension.class)
class TicketStatusHistoryServiceImplTest {

    @Mock
    private TicketStatusHistoryRepository ticketStatusHistoryRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketStatusHistoryServiceImpl ticketStatusHistoryService;

    private TicketTbl testTicket;
    private TicketStatusHistoryTbl testStatusHistory;

    @BeforeEach
    void setUp() {
        testTicket = new TicketTbl();
        testTicket.setTicketId(1);
        testTicket.setTitle("Test Ticket");
        testTicket.setAuthor("Test Author");
        testTicket.setStatus("PENDING");
        testTicket.setCreatedDate(LocalDateTime.now());

        testStatusHistory = new TicketStatusHistoryTbl();
        testStatusHistory.setStatusId(1);
        testStatusHistory.setTicket(testTicket);
        testStatusHistory.setStatus("PENDING");
        testStatusHistory.setUpdateDate(LocalDateTime.now());
    }

    @Test
    void testFindAll_ReturnsAllStatusHistories() {
        // Arrange
        TicketStatusHistoryTbl history2 = new TicketStatusHistoryTbl();
        history2.setStatusId(2);
        history2.setStatus("ONGOING");
        List<TicketStatusHistoryTbl> expectedHistories = Arrays.asList(testStatusHistory, history2);
        when(ticketStatusHistoryRepository.findAll()).thenReturn(expectedHistories);

        // Act
        List<TicketStatusHistoryTbl> actualHistories = ticketStatusHistoryService.findAll();

        // Assert
        assertNotNull(actualHistories);
        assertEquals(2, actualHistories.size());
        assertEquals(expectedHistories, actualHistories);
        verify(ticketStatusHistoryRepository, times(1)).findAll();
    }

    @Test
    void testAddStatusHistoryToTicket_CreatesHistorySuccessfully() {
        // Arrange
        when(ticketRepository.findById(1)).thenReturn(Optional.of(testTicket));
        when(ticketStatusHistoryRepository.save(any(TicketStatusHistoryTbl.class))).thenReturn(testStatusHistory);

        // Act
        TicketStatusHistoryTbl createdHistory = ticketStatusHistoryService.addStatusHistoryToTicket(1, "ONGOING");

        // Assert
        assertNotNull(createdHistory);
        assertEquals(testStatusHistory.getStatusId(), createdHistory.getStatusId());
        verify(ticketRepository, times(1)).findById(1);
        verify(ticketStatusHistoryRepository, times(1)).save(any(TicketStatusHistoryTbl.class));
    }

    @Test
    void testAddStatusHistoryToTicket_ThrowsExceptionWhenTicketNotFound() {
        // Arrange
        when(ticketRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ticketStatusHistoryService.addStatusHistoryToTicket(999, "ONGOING");
        });
        assertTrue(exception.getMessage().contains("Ticket not found with id: 999"));
        verify(ticketRepository, times(1)).findById(999);
        verify(ticketStatusHistoryRepository, never()).save(any(TicketStatusHistoryTbl.class));
    }

    @Test
    void testAddStatusHistoryToTicket_SetsCorrectTimestamp() {
        // Arrange
        LocalDateTime beforeCreation = LocalDateTime.now();
        when(ticketRepository.findById(1)).thenReturn(Optional.of(testTicket));
        when(ticketStatusHistoryRepository.save(any(TicketStatusHistoryTbl.class))).thenAnswer(invocation -> {
            TicketStatusHistoryTbl history = invocation.getArgument(0);
            assertNotNull(history.getUpdateDate());
            assertTrue(history.getUpdateDate().isAfter(beforeCreation) || 
                      history.getUpdateDate().isEqual(beforeCreation));
            return history;
        });

        // Act
        ticketStatusHistoryService.addStatusHistoryToTicket(1, "RESOLVED");

        // Assert
        verify(ticketStatusHistoryRepository, times(1)).save(any(TicketStatusHistoryTbl.class));
    }

    @Test
    void testAddStatusHistoryToTicket_AssociatesHistoryWithTicket() {
        // Arrange
        when(ticketRepository.findById(1)).thenReturn(Optional.of(testTicket));
        when(ticketStatusHistoryRepository.save(any(TicketStatusHistoryTbl.class))).thenAnswer(invocation -> {
            TicketStatusHistoryTbl history = invocation.getArgument(0);
            assertEquals(testTicket, history.getTicket());
            assertEquals("ONGOING", history.getStatus());
            return history;
        });

        // Act
        ticketStatusHistoryService.addStatusHistoryToTicket(1, "ONGOING");

        // Assert
        verify(ticketStatusHistoryRepository, times(1)).save(any(TicketStatusHistoryTbl.class));
    }

    @Test
    void testGetLatestStatus_ReturnsLatestStatusSuccessfully() {
        // Arrange
        when(ticketRepository.findById(1)).thenReturn(Optional.of(testTicket));
        when(ticketStatusHistoryRepository.getLatestStatus(1)).thenReturn(testStatusHistory);

        // Act
        TicketStatusHistoryTbl latestStatus = ticketStatusHistoryService.getLatestStatus(1);

        // Assert
        assertNotNull(latestStatus);
        assertEquals(testStatusHistory.getStatusId(), latestStatus.getStatusId());
        assertEquals(testStatusHistory.getStatus(), latestStatus.getStatus());
        verify(ticketRepository, times(1)).findById(1);
        verify(ticketStatusHistoryRepository, times(1)).getLatestStatus(1);
    }

    @Test
    void testGetLatestStatus_ThrowsExceptionWhenTicketNotFound() {
        // Arrange
        when(ticketRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ticketStatusHistoryService.getLatestStatus(999);
        });
        assertTrue(exception.getMessage().contains("Ticket not found with id: 999"));
        verify(ticketRepository, times(1)).findById(999);
        verify(ticketStatusHistoryRepository, never()).getLatestStatus(anyInt());
    }

    @Test
    void testGetStatusHistoryByTicketId_ReturnsHistoriesOrderedByDate() {
        // Arrange
        TicketStatusHistoryTbl history2 = new TicketStatusHistoryTbl();
        history2.setStatusId(2);
        history2.setStatus("ONGOING");
        history2.setUpdateDate(LocalDateTime.now().minusHours(1));
        
        TicketStatusHistoryTbl history3 = new TicketStatusHistoryTbl();
        history3.setStatusId(3);
        history3.setStatus("RESOLVED");
        history3.setUpdateDate(LocalDateTime.now().minusHours(2));
        
        List<TicketStatusHistoryTbl> expectedHistories = Arrays.asList(testStatusHistory, history2, history3);
        when(ticketStatusHistoryRepository.findByTicketIdOrderByUpdateDateDesc(1)).thenReturn(expectedHistories);

        // Act
        List<TicketStatusHistoryTbl> actualHistories = ticketStatusHistoryService.getStatusHistoryByTicketId(1);

        // Assert
        assertNotNull(actualHistories);
        assertEquals(3, actualHistories.size());
        assertEquals(expectedHistories, actualHistories);
        verify(ticketStatusHistoryRepository, times(1)).findByTicketIdOrderByUpdateDateDesc(1);
    }

    @Test
    void testGetStatusHistoryByTicketId_ReturnsEmptyListWhenNoHistory() {
        // Arrange
        when(ticketStatusHistoryRepository.findByTicketIdOrderByUpdateDateDesc(999)).thenReturn(Arrays.asList());

        // Act
        List<TicketStatusHistoryTbl> actualHistories = ticketStatusHistoryService.getStatusHistoryByTicketId(999);

        // Assert
        assertNotNull(actualHistories);
        assertTrue(actualHistories.isEmpty());
        verify(ticketStatusHistoryRepository, times(1)).findByTicketIdOrderByUpdateDateDesc(999);
    }

    @Test
    void testAddStatusHistoryToTicket_WithDifferentStatuses() {
        // Arrange
        when(ticketRepository.findById(1)).thenReturn(Optional.of(testTicket));
        
        // Test PENDING status
        TicketStatusHistoryTbl pendingHistory = new TicketStatusHistoryTbl();
        pendingHistory.setStatus("PENDING");
        when(ticketStatusHistoryRepository.save(any(TicketStatusHistoryTbl.class))).thenReturn(pendingHistory);
        
        TicketStatusHistoryTbl result1 = ticketStatusHistoryService.addStatusHistoryToTicket(1, "PENDING");
        assertEquals("PENDING", result1.getStatus());
        
        // Test ONGOING status
        TicketStatusHistoryTbl ongoingHistory = new TicketStatusHistoryTbl();
        ongoingHistory.setStatus("ONGOING");
        when(ticketStatusHistoryRepository.save(any(TicketStatusHistoryTbl.class))).thenReturn(ongoingHistory);
        
        TicketStatusHistoryTbl result2 = ticketStatusHistoryService.addStatusHistoryToTicket(1, "ONGOING");
        assertEquals("ONGOING", result2.getStatus());
        
        // Test RESOLVED status
        TicketStatusHistoryTbl resolvedHistory = new TicketStatusHistoryTbl();
        resolvedHistory.setStatus("RESOLVED");
        when(ticketStatusHistoryRepository.save(any(TicketStatusHistoryTbl.class))).thenReturn(resolvedHistory);
        
        TicketStatusHistoryTbl result3 = ticketStatusHistoryService.addStatusHistoryToTicket(1, "RESOLVED");
        assertEquals("RESOLVED", result3.getStatus());
        
        // Assert
        verify(ticketStatusHistoryRepository, times(3)).save(any(TicketStatusHistoryTbl.class));
    }
}