package com.aws.poc.ticketingsystem.service;

import com.aws.poc.ticketingsystem.entity.TicketCommentsTbl;
import com.aws.poc.ticketingsystem.entity.TicketTbl;
import com.aws.poc.ticketingsystem.repository.TicketCommentsRepository;
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
 * Unit tests for TicketCommentsServiceImpl
 * Tests all business logic methods for ticket comments management
 */
@ExtendWith(MockitoExtension.class)
class TicketCommentsServiceImplTest {

    @Mock
    private TicketCommentsRepository ticketCommentsRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketCommentsServiceImpl ticketCommentsService;

    private TicketTbl testTicket;
    private TicketCommentsTbl testComment;

    @BeforeEach
    void setUp() {
        testTicket = new TicketTbl();
        testTicket.setTicketId(1);
        testTicket.setTitle("Test Ticket");
        testTicket.setAuthor("Test Author");
        testTicket.setStatus("PENDING");
        testTicket.setCreatedDate(LocalDateTime.now());

        testComment = new TicketCommentsTbl();
        testComment.setCommentId(1);
        testComment.setTicket(testTicket);
        testComment.setComments("Test Comment");
        testComment.setAuthor("Comment Author");
        testComment.setCreatedDate(LocalDateTime.now());
    }

    @Test
    void testFindAll_ReturnsAllComments() {
        // Arrange
        TicketCommentsTbl comment2 = new TicketCommentsTbl();
        comment2.setCommentId(2);
        comment2.setComments("Second Comment");
        List<TicketCommentsTbl> expectedComments = Arrays.asList(testComment, comment2);
        when(ticketCommentsRepository.findAll()).thenReturn(expectedComments);

        // Act
        List<TicketCommentsTbl> actualComments = ticketCommentsService.findAll();

        // Assert
        assertNotNull(actualComments);
        assertEquals(2, actualComments.size());
        assertEquals(expectedComments, actualComments);
        verify(ticketCommentsRepository, times(1)).findAll();
    }

    @Test
    void testSaveComment_SavesSuccessfully() {
        // Arrange
        when(ticketCommentsRepository.save(any(TicketCommentsTbl.class))).thenReturn(testComment);

        // Act
        TicketCommentsTbl savedComment = ticketCommentsService.saveComment(testComment);

        // Assert
        assertNotNull(savedComment);
        assertEquals(testComment.getCommentId(), savedComment.getCommentId());
        assertEquals(testComment.getComments(), savedComment.getComments());
        verify(ticketCommentsRepository, times(1)).save(testComment);
    }

    @Test
    void testFindCommentsByTicketId_ReturnsCommentsOrderedByDate() {
        // Arrange
        TicketCommentsTbl comment2 = new TicketCommentsTbl();
        comment2.setCommentId(2);
        comment2.setComments("Second Comment");
        comment2.setCreatedDate(LocalDateTime.now().minusHours(1));
        
        List<TicketCommentsTbl> expectedComments = Arrays.asList(testComment, comment2);
        when(ticketCommentsRepository.findByTicketIdOrderByCreatedDateDesc(1)).thenReturn(expectedComments);

        // Act
        List<TicketCommentsTbl> actualComments = ticketCommentsService.findCommentsByTicketId(1);

        // Assert
        assertNotNull(actualComments);
        assertEquals(2, actualComments.size());
        assertEquals(expectedComments, actualComments);
        verify(ticketCommentsRepository, times(1)).findByTicketIdOrderByCreatedDateDesc(1);
    }

    @Test
    void testFindCommentsByTicketId_ReturnsEmptyListWhenNoComments() {
        // Arrange
        when(ticketCommentsRepository.findByTicketIdOrderByCreatedDateDesc(999)).thenReturn(Arrays.asList());

        // Act
        List<TicketCommentsTbl> actualComments = ticketCommentsService.findCommentsByTicketId(999);

        // Assert
        assertNotNull(actualComments);
        assertTrue(actualComments.isEmpty());
        verify(ticketCommentsRepository, times(1)).findByTicketIdOrderByCreatedDateDesc(999);
    }

    @Test
    void testCountCommentsByTicketId_ReturnsCorrectCount() {
        // Arrange
        when(ticketCommentsRepository.countByTicketId(1)).thenReturn(5L);

        // Act
        Long count = ticketCommentsService.countCommentsByTicketId(1);

        // Assert
        assertNotNull(count);
        assertEquals(5L, count);
        verify(ticketCommentsRepository, times(1)).countByTicketId(1);
    }

    @Test
    void testCountCommentsByTicketId_ReturnsZeroWhenNoComments() {
        // Arrange
        when(ticketCommentsRepository.countByTicketId(999)).thenReturn(0L);

        // Act
        Long count = ticketCommentsService.countCommentsByTicketId(999);

        // Assert
        assertNotNull(count);
        assertEquals(0L, count);
        verify(ticketCommentsRepository, times(1)).countByTicketId(999);
    }

    @Test
    void testAddCommentToTicket_CreatesCommentSuccessfully() {
        // Arrange
        when(ticketRepository.findById(1)).thenReturn(Optional.of(testTicket));
        when(ticketCommentsRepository.save(any(TicketCommentsTbl.class))).thenReturn(testComment);

        // Act
        TicketCommentsTbl createdComment = ticketCommentsService.addCommentToTicket(1, "New Comment", "New Author");

        // Assert
        assertNotNull(createdComment);
        assertEquals(testComment.getCommentId(), createdComment.getCommentId());
        verify(ticketRepository, times(1)).findById(1);
        verify(ticketCommentsRepository, times(1)).save(any(TicketCommentsTbl.class));
    }

    @Test
    void testAddCommentToTicket_ThrowsExceptionWhenTicketNotFound() {
        // Arrange
        when(ticketRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ticketCommentsService.addCommentToTicket(999, "Comment", "Author");
        });
        assertTrue(exception.getMessage().contains("Ticket not found with id: 999"));
        verify(ticketRepository, times(1)).findById(999);
        verify(ticketCommentsRepository, never()).save(any(TicketCommentsTbl.class));
    }

    @Test
    void testAddCommentToTicket_SetsCorrectTimestamp() {
        // Arrange
        LocalDateTime beforeCreation = LocalDateTime.now();
        when(ticketRepository.findById(1)).thenReturn(Optional.of(testTicket));
        when(ticketCommentsRepository.save(any(TicketCommentsTbl.class))).thenAnswer(invocation -> {
            TicketCommentsTbl comment = invocation.getArgument(0);
            assertNotNull(comment.getCreatedDate());
            assertTrue(comment.getCreatedDate().isAfter(beforeCreation) || 
                      comment.getCreatedDate().isEqual(beforeCreation));
            return comment;
        });

        // Act
        ticketCommentsService.addCommentToTicket(1, "Comment", "Author");

        // Assert
        verify(ticketCommentsRepository, times(1)).save(any(TicketCommentsTbl.class));
    }

    @Test
    void testAddCommentToTicket_AssociatesCommentWithTicket() {
        // Arrange
        when(ticketRepository.findById(1)).thenReturn(Optional.of(testTicket));
        when(ticketCommentsRepository.save(any(TicketCommentsTbl.class))).thenAnswer(invocation -> {
            TicketCommentsTbl comment = invocation.getArgument(0);
            assertEquals(testTicket, comment.getTicket());
            assertEquals("Test Comment Text", comment.getComments());
            assertEquals("Test Author", comment.getAuthor());
            return comment;
        });

        // Act
        ticketCommentsService.addCommentToTicket(1, "Test Comment Text", "Test Author");

        // Assert
        verify(ticketCommentsRepository, times(1)).save(any(TicketCommentsTbl.class));
    }
}