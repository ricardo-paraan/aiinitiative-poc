package com.aws.poc.ticketingsystem.service;

import com.aws.poc.ticketingsystem.entity.TicketStatusHistoryTbl;
import com.aws.poc.ticketingsystem.entity.TicketTbl;
import com.aws.poc.ticketingsystem.repository.TicketRepository;
import com.aws.poc.ticketingsystem.repository.TicketStatusHistoryRepository;
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
 * Unit tests for TicketStatusHistoryServiceImpl
 * Tests business logic for ticket status history operations
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Ticket Status History Service Implementation Tests")
class TicketStatusHistoryServiceImplTest {

    @Mock
    private TicketStatusHistoryRepository ticketStatusHistoryRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketStatusHistoryServiceImpl ticketStatusHistoryService;

    private TicketTbl sampleTicket;
    private TicketStatusHistoryTbl sampleStatusHistory;
    private List<TicketStatusHistoryTbl> sampleStatusHistories;

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
        sampleTicket.setCreatedDate(LocalDateTime.now());
        sampleTicket.setUpdateDate(LocalDateTime.now());

        sampleStatusHistory = new TicketStatusHistoryTbl();
        sampleStatusHistory.setStatusId(1);
        sampleStatusHistory.setTicket(sampleTicket);
        sampleStatusHistory.setStatus("PENDING");
        sampleStatusHistory.setUpdateDate(LocalDateTime.now());

        sampleStatusHistories = new ArrayList<>();
        sampleStatusHistories.add(sampleStatusHistory);
    }

    // ========================================
    // Find All Status History Tests
    // ========================================

    @Test
    @DisplayName("Find all status history - should return list of status histories")
    void testFindAll() {
        when(ticketStatusHistoryRepository.findAll()).thenReturn(sampleStatusHistories);

        List<TicketStatusHistoryTbl> result = ticketStatusHistoryService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PENDING", result.get(0).getStatus());
        verify(ticketStatusHistoryRepository).findAll();
    }

    @Test
    @DisplayName("Find all status history - should return empty list when no histories exist")
    void testFindAllEmpty() {
        when(ticketStatusHistoryRepository.findAll()).thenReturn(new ArrayList<>());

        List<TicketStatusHistoryTbl> result = ticketStatusHistoryService.findAll();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(ticketStatusHistoryRepository).findAll();
    }

    // ========================================
    // Add Status History To Ticket Tests
    // ========================================

    @Test
    @DisplayName("Add status history to ticket - should save and return status history")
    void testAddStatusHistoryToTicket() {
        when(ticketRepository.findById(1)).thenReturn(Optional.of(sampleTicket));
        when(ticketStatusHistoryRepository.save(any(TicketStatusHistoryTbl.class))).thenReturn(sampleStatusHistory);

        TicketStatusHistoryTbl result = ticketStatusHistoryService.addStatusHistoryToTicket(1, "PENDING");

        assertNotNull(result);
        assertEquals("PENDING", result.getStatus());
        assertNotNull(result.getUpdateDate());
        verify(ticketRepository).findById(1);
        verify(ticketStatusHistoryRepository).save(any(TicketStatusHistoryTbl.class));
    }

    @Test
    @DisplayName("Add status history to ticket - should throw exception for non-existent ticket")
    void testAddStatusHistoryToNonExistentTicket() {
        when(ticketRepository.findById(999)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ticketStatusHistoryService.addStatusHistoryToTicket(999, "PENDING");
        });

        assertTrue(exception.getMessage().contains("Ticket not found"));
        verify(ticketRepository).findById(999);
        verify(ticketStatusHistoryRepository, never()).save(any(TicketStatusHistoryTbl.class));
    }

    @Test
    @DisplayName("Add status history - should handle status change from PENDING to ONGOING")
    void testAddStatusHistoryPendingToOngoing() {
        TicketStatusHistoryTbl ongoingStatus = new TicketStatusHistoryTbl();
        ongoingStatus.setStatusId(2);
        ongoingStatus.setTicket(sampleTicket);
        ongoingStatus.setStatus("ONGOING");
        ongoingStatus.setUpdateDate(LocalDateTime.now());

        when(ticketRepository.findById(1)).thenReturn(Optional.of(sampleTicket));
        when(ticketStatusHistoryRepository.save(any(TicketStatusHistoryTbl.class))).thenReturn(ongoingStatus);

        TicketStatusHistoryTbl result = ticketStatusHistoryService.addStatusHistoryToTicket(1, "ONGOING");

        assertNotNull(result);
        assertEquals("ONGOING", result.getStatus());
        verify(ticketStatusHistoryRepository).save(any(TicketStatusHistoryTbl.class));
    }

    @Test
    @DisplayName("Add status history - should handle status change from ONGOING to RESOLVED")
    void testAddStatusHistoryOngoingToResolved() {
        TicketStatusHistoryTbl resolvedStatus = new TicketStatusHistoryTbl();
        resolvedStatus.setStatusId(3);
        resolvedStatus.setTicket(sampleTicket);
        resolvedStatus.setStatus("RESOLVED");
        resolvedStatus.setUpdateDate(LocalDateTime.now());

        when(ticketRepository.findById(1)).thenReturn(Optional.of(sampleTicket));
        when(ticketStatusHistoryRepository.save(any(TicketStatusHistoryTbl.class))).thenReturn(resolvedStatus);

        TicketStatusHistoryTbl result = ticketStatusHistoryService.addStatusHistoryToTicket(1, "RESOLVED");

        assertNotNull(result);
        assertEquals("RESOLVED", result.getStatus());
        verify(ticketStatusHistoryRepository).save(any(TicketStatusHistoryTbl.class));
    }

    @Test
    @DisplayName("Add status history - should handle rapid succession of status changes")
    void testAddMultipleStatusHistoriesRapidly() {
        when(ticketRepository.findById(1)).thenReturn(Optional.of(sampleTicket));

        String[] statuses = {"PENDING", "ONGOING", "RESOLVED", "ONGOING", "RESOLVED"};
        for (int i = 0; i < statuses.length; i++) {
            TicketStatusHistoryTbl statusHistory = new TicketStatusHistoryTbl();
            statusHistory.setStatusId(i + 1);
            statusHistory.setTicket(sampleTicket);
            statusHistory.setStatus(statuses[i]);
            statusHistory.setUpdateDate(LocalDateTime.now());

            when(ticketStatusHistoryRepository.save(any(TicketStatusHistoryTbl.class))).thenReturn(statusHistory);

            TicketStatusHistoryTbl result = ticketStatusHistoryService.addStatusHistoryToTicket(1, statuses[i]);
            assertNotNull(result);
            assertEquals(statuses[i], result.getStatus());
        }

        verify(ticketRepository, times(5)).findById(1);
        verify(ticketStatusHistoryRepository, times(5)).save(any(TicketStatusHistoryTbl.class));
    }

    @Test
    @DisplayName("Add status history - should handle empty status string")
    void testAddStatusHistoryEmptyStatus() {
        TicketStatusHistoryTbl emptyStatus = new TicketStatusHistoryTbl();
        emptyStatus.setStatusId(1);
        emptyStatus.setTicket(sampleTicket);
        emptyStatus.setStatus("");
        emptyStatus.setUpdateDate(LocalDateTime.now());

        when(ticketRepository.findById(1)).thenReturn(Optional.of(sampleTicket));
        when(ticketStatusHistoryRepository.save(any(TicketStatusHistoryTbl.class))).thenReturn(emptyStatus);

        TicketStatusHistoryTbl result = ticketStatusHistoryService.addStatusHistoryToTicket(1, "");

        assertNotNull(result);
        assertEquals("", result.getStatus());
        verify(ticketStatusHistoryRepository).save(any(TicketStatusHistoryTbl.class));
    }

    @Test
    @DisplayName("Add status history - should handle null status")
    void testAddStatusHistoryNullStatus() {
        TicketStatusHistoryTbl nullStatus = new TicketStatusHistoryTbl();
        nullStatus.setStatusId(1);
        nullStatus.setTicket(sampleTicket);
        nullStatus.setStatus(null);
        nullStatus.setUpdateDate(LocalDateTime.now());

        when(ticketRepository.findById(1)).thenReturn(Optional.of(sampleTicket));
        when(ticketStatusHistoryRepository.save(any(TicketStatusHistoryTbl.class))).thenReturn(nullStatus);

        TicketStatusHistoryTbl result = ticketStatusHistoryService.addStatusHistoryToTicket(1, null);

        assertNotNull(result);
        assertNull(result.getStatus());
        verify(ticketStatusHistoryRepository).save(any(TicketStatusHistoryTbl.class));
    }

    // ========================================
    // Get Latest Status Tests
    // ========================================

    @Test
    @DisplayName("Get latest status - should return most recent status")
    void testGetLatestStatus() {
        when(ticketRepository.findById(1)).thenReturn(Optional.of(sampleTicket));
        when(ticketStatusHistoryRepository.getLatestStatus(1)).thenReturn(sampleStatusHistory);

        TicketStatusHistoryTbl result = ticketStatusHistoryService.getLatestStatus(1);

        assertNotNull(result);
        assertEquals("PENDING", result.getStatus());
        assertEquals(1, result.getStatusId());
        verify(ticketRepository).findById(1);
        verify(ticketStatusHistoryRepository).getLatestStatus(1);
    }

    @Test
    @DisplayName("Get latest status - should throw exception for non-existent ticket")
    void testGetLatestStatusNonExistentTicket() {
        when(ticketRepository.findById(999)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ticketStatusHistoryService.getLatestStatus(999);
        });

        assertTrue(exception.getMessage().contains("Ticket not found"));
        verify(ticketRepository).findById(999);
        verify(ticketStatusHistoryRepository, never()).getLatestStatus(anyInt());
    }

    @Test
    @DisplayName("Get latest status - should return null when no status history exists")
    void testGetLatestStatusNoHistory() {
        when(ticketRepository.findById(1)).thenReturn(Optional.of(sampleTicket));
        when(ticketStatusHistoryRepository.getLatestStatus(1)).thenReturn(null);

        TicketStatusHistoryTbl result = ticketStatusHistoryService.getLatestStatus(1);

        assertNull(result);
        verify(ticketRepository).findById(1);
        verify(ticketStatusHistoryRepository).getLatestStatus(1);
    }

    // ========================================
    // Get Status History By Ticket ID Tests
    // ========================================

    @Test
    @DisplayName("Get status history by ticket ID - should return list of status histories")
    void testGetStatusHistoryByTicketId() {
        when(ticketStatusHistoryRepository.findByTicketIdOrderByUpdateDateDesc(1)).thenReturn(sampleStatusHistories);

        List<TicketStatusHistoryTbl> result = ticketStatusHistoryService.getStatusHistoryByTicketId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PENDING", result.get(0).getStatus());
        verify(ticketStatusHistoryRepository).findByTicketIdOrderByUpdateDateDesc(1);
    }

    @Test
    @DisplayName("Get status history by ticket ID - should return empty list when no history exists")
    void testGetStatusHistoryByTicketIdEmpty() {
        when(ticketStatusHistoryRepository.findByTicketIdOrderByUpdateDateDesc(1)).thenReturn(new ArrayList<>());

        List<TicketStatusHistoryTbl> result = ticketStatusHistoryService.getStatusHistoryByTicketId(1);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(ticketStatusHistoryRepository).findByTicketIdOrderByUpdateDateDesc(1);
    }

    @Test
    @DisplayName("Get status history by ticket ID - should handle multiple status changes")
    void testGetStatusHistoryMultipleChanges() {
        List<TicketStatusHistoryTbl> multipleHistories = new ArrayList<>();
        
        TicketStatusHistoryTbl history1 = new TicketStatusHistoryTbl();
        history1.setStatusId(1);
        history1.setStatus("PENDING");
        history1.setUpdateDate(LocalDateTime.now().minusDays(2));
        multipleHistories.add(history1);

        TicketStatusHistoryTbl history2 = new TicketStatusHistoryTbl();
        history2.setStatusId(2);
        history2.setStatus("ONGOING");
        history2.setUpdateDate(LocalDateTime.now().minusDays(1));
        multipleHistories.add(history2);

        TicketStatusHistoryTbl history3 = new TicketStatusHistoryTbl();
        history3.setStatusId(3);
        history3.setStatus("RESOLVED");
        history3.setUpdateDate(LocalDateTime.now());
        multipleHistories.add(history3);

        when(ticketStatusHistoryRepository.findByTicketIdOrderByUpdateDateDesc(1)).thenReturn(multipleHistories);

        List<TicketStatusHistoryTbl> result = ticketStatusHistoryService.getStatusHistoryByTicketId(1);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("PENDING", result.get(0).getStatus());
        assertEquals("ONGOING", result.get(1).getStatus());
        assertEquals("RESOLVED", result.get(2).getStatus());
        verify(ticketStatusHistoryRepository).findByTicketIdOrderByUpdateDateDesc(1);
    }

    @Test
    @DisplayName("Get status history by ticket ID - should handle non-existent ticket")
    void testGetStatusHistoryByNonExistentTicket() {
        when(ticketStatusHistoryRepository.findByTicketIdOrderByUpdateDateDesc(999)).thenReturn(new ArrayList<>());

        List<TicketStatusHistoryTbl> result = ticketStatusHistoryService.getStatusHistoryByTicketId(999);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(ticketStatusHistoryRepository).findByTicketIdOrderByUpdateDateDesc(999);
    }

    // ========================================
    // Edge Case Tests
    // ========================================

    @Test
    @DisplayName("Edge Case: Repository throws exception when finding all")
    void testFindAllException() {
        when(ticketStatusHistoryRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> {
            ticketStatusHistoryService.findAll();
        });
    }

    @Test
    @DisplayName("Edge Case: Repository throws exception when saving status history")
    void testSaveStatusHistoryException() {
        when(ticketRepository.findById(1)).thenReturn(Optional.of(sampleTicket));
        when(ticketStatusHistoryRepository.save(any(TicketStatusHistoryTbl.class)))
                .thenThrow(new RuntimeException("Save failed"));

        assertThrows(RuntimeException.class, () -> {
            ticketStatusHistoryService.addStatusHistoryToTicket(1, "PENDING");
        });
    }

    @Test
    @DisplayName("Edge Case: Repository throws exception when getting latest status")
    void testGetLatestStatusException() {
        when(ticketRepository.findById(1)).thenReturn(Optional.of(sampleTicket));
        when(ticketStatusHistoryRepository.getLatestStatus(anyInt()))
                .thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> {
            ticketStatusHistoryService.getLatestStatus(1);
        });
    }

    @Test
    @DisplayName("Edge Case: Verify status history timestamp is set correctly")
    void testStatusHistoryTimestampSet() {
        LocalDateTime beforeSave = LocalDateTime.now();
        
        when(ticketRepository.findById(1)).thenReturn(Optional.of(sampleTicket));
        when(ticketStatusHistoryRepository.save(any(TicketStatusHistoryTbl.class))).thenAnswer(invocation -> {
            TicketStatusHistoryTbl statusHistory = invocation.getArgument(0);
            statusHistory.setStatusId(1);
            return statusHistory;
        });

        TicketStatusHistoryTbl result = ticketStatusHistoryService.addStatusHistoryToTicket(1, "PENDING");

        assertNotNull(result.getUpdateDate());
        assertTrue(result.getUpdateDate().isAfter(beforeSave.minusSeconds(1)));
        assertTrue(result.getUpdateDate().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    @DisplayName("Edge Case: Verify status history is associated with correct ticket")
    void testStatusHistoryTicketAssociation() {
        when(ticketRepository.findById(1)).thenReturn(Optional.of(sampleTicket));
        when(ticketStatusHistoryRepository.save(any(TicketStatusHistoryTbl.class))).thenAnswer(invocation -> {
            TicketStatusHistoryTbl statusHistory = invocation.getArgument(0);
            statusHistory.setStatusId(1);
            return statusHistory;
        });

        TicketStatusHistoryTbl result = ticketStatusHistoryService.addStatusHistoryToTicket(1, "PENDING");

        assertNotNull(result.getTicket());
        assertEquals(1, result.getTicket().getTicketId());
        verify(ticketRepository).findById(1);
    }

    @Test
    @DisplayName("Edge Case: Add status history with very long status string")
    void testAddStatusHistoryLongStatus() {
        String longStatus = "A".repeat(1000);
        TicketStatusHistoryTbl longStatusHistory = new TicketStatusHistoryTbl();
        longStatusHistory.setStatusId(1);
        longStatusHistory.setTicket(sampleTicket);
        longStatusHistory.setStatus(longStatus);
        longStatusHistory.setUpdateDate(LocalDateTime.now());

        when(ticketRepository.findById(1)).thenReturn(Optional.of(sampleTicket));
        when(ticketStatusHistoryRepository.save(any(TicketStatusHistoryTbl.class))).thenReturn(longStatusHistory);

        TicketStatusHistoryTbl result = ticketStatusHistoryService.addStatusHistoryToTicket(1, longStatus);

        assertNotNull(result);
        assertEquals(longStatus, result.getStatus());
        verify(ticketStatusHistoryRepository).save(any(TicketStatusHistoryTbl.class));
    }

    @Test
    @DisplayName("Edge Case: Add status history with special characters")
    void testAddStatusHistorySpecialCharacters() {
        String specialStatus = "Status with @#$% and ;DROP TABLE;";
        TicketStatusHistoryTbl specialStatusHistory = new TicketStatusHistoryTbl();
        specialStatusHistory.setStatusId(1);
        specialStatusHistory.setTicket(sampleTicket);
        specialStatusHistory.setStatus(specialStatus);
        specialStatusHistory.setUpdateDate(LocalDateTime.now());

        when(ticketRepository.findById(1)).thenReturn(Optional.of(sampleTicket));
        when(ticketStatusHistoryRepository.save(any(TicketStatusHistoryTbl.class))).thenReturn(specialStatusHistory);

        TicketStatusHistoryTbl result = ticketStatusHistoryService.addStatusHistoryToTicket(1, specialStatus);

        assertNotNull(result);
        assertEquals(specialStatus, result.getStatus());
        verify(ticketStatusHistoryRepository).save(any(TicketStatusHistoryTbl.class));
    }

    @Test
    @DisplayName("Edge Case: Get status history with large number of entries")
    void testGetStatusHistoryLargeDataset() {
        List<TicketStatusHistoryTbl> largeHistoryList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            TicketStatusHistoryTbl history = new TicketStatusHistoryTbl();
            history.setStatusId(i);
            history.setStatus("Status " + i);
            history.setUpdateDate(LocalDateTime.now().minusDays(100 - i));
            largeHistoryList.add(history);
        }

        when(ticketStatusHistoryRepository.findByTicketIdOrderByUpdateDateDesc(1)).thenReturn(largeHistoryList);

        List<TicketStatusHistoryTbl> result = ticketStatusHistoryService.getStatusHistoryByTicketId(1);

        assertNotNull(result);
        assertEquals(100, result.size());
        verify(ticketStatusHistoryRepository).findByTicketIdOrderByUpdateDateDesc(1);
    }
}