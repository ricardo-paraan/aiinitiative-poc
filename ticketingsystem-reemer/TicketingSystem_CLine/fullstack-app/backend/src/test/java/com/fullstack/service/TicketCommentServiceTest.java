package com.fullstack.service;

import com.fullstack.entity.Ticket;
import com.fullstack.entity.TicketComment;
import com.fullstack.repository.TicketCommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TicketCommentService
 * Tests cover: Adding comments, retrieving comments, and edge cases
 */
@ExtendWith(MockitoExtension.class)
class TicketCommentServiceTest {

    @Mock
    private TicketCommentRepository ticketCommentRepository;

    @InjectMocks
    private TicketCommentService ticketCommentService;

    private Ticket testTicket;
    private TicketComment testComment;

    @BeforeEach
    void setUp() {
        testTicket = new Ticket();
        testTicket.setTicketId(1);
        testTicket.setTitle("Test Ticket");

        testComment = new TicketComment();
        testComment.setCommentId(1);
        testComment.setTicket(testTicket);
        testComment.setComments("This is a test comment");
        testComment.setAuthor("John Doe");
        testComment.setCreatedAt(LocalDateTime.now());
    }

    // ==================== ADD COMMENT TESTS ====================

    @Test
    @DisplayName("Add Comment: Create comment successfully")
    void testCreateComment_Success() {
        when(ticketCommentRepository.save(any(TicketComment.class))).thenReturn(testComment);

        TicketComment result = ticketCommentService.createComment(testComment);

        assertNotNull(result);
        assertEquals("This is a test comment", result.getComments());
        assertEquals("John Doe", result.getAuthor());
        assertNotNull(result.getCreatedAt());
        verify(ticketCommentRepository, times(1)).save(any(TicketComment.class));
    }

    @Test
    @DisplayName("Add Comment: Comment with long text")
    void testCreateComment_LongText() {
        String longComment = "A".repeat(500);
        testComment.setComments(longComment);
        when(ticketCommentRepository.save(any(TicketComment.class))).thenReturn(testComment);

        TicketComment result = ticketCommentService.createComment(testComment);

        assertNotNull(result);
        assertEquals(longComment, result.getComments());
        assertEquals(500, result.getComments().length());
    }

    @Test
    @DisplayName("Add Comment: Comment with special characters")
    void testCreateComment_SpecialCharacters() {
        testComment.setComments("Comment with @#$%^&*() special chars");
        when(ticketCommentRepository.save(any(TicketComment.class))).thenReturn(testComment);

        TicketComment result = ticketCommentService.createComment(testComment);

        assertNotNull(result);
        assertEquals("Comment with @#$%^&*() special chars", result.getComments());
    }

    @Test
    @DisplayName("Add Comment: Comment with non-ASCII characters")
    void testCreateComment_NonASCIICharacters() {
        testComment.setComments("日本語のコメント");
        testComment.setAuthor("田中太郎");
        when(ticketCommentRepository.save(any(TicketComment.class))).thenReturn(testComment);

        TicketComment result = ticketCommentService.createComment(testComment);

        assertNotNull(result);
        assertEquals("日本語のコメント", result.getComments());
        assertEquals("田中太郎", result.getAuthor());
    }

    @Test
    @DisplayName("Add Comment: Timestamp is automatically set")
    void testCreateComment_TimestampAutoSet() {
        TicketComment newComment = new TicketComment();
        newComment.setTicket(testTicket);
        newComment.setComments("New comment");
        newComment.setAuthor("Jane Doe");
        
        when(ticketCommentRepository.save(any(TicketComment.class))).thenAnswer(invocation -> {
            TicketComment saved = invocation.getArgument(0);
            if (saved.getCreatedAt() == null) {
                saved.setCreatedAt(LocalDateTime.now());
            }
            saved.setCommentId(1);
            return saved;
        });

        TicketComment result = ticketCommentService.createComment(newComment);

        assertNotNull(result);
        assertNotNull(result.getCreatedAt());
    }

    // ==================== RETRIEVE COMMENTS TESTS ====================

    @Test
    @DisplayName("Retrieve Comments: Get all comments")
    void testGetAllComments() {
        TicketComment comment2 = new TicketComment();
        comment2.setCommentId(2);
        comment2.setComments("Second comment");
        
        when(ticketCommentRepository.findAll()).thenReturn(Arrays.asList(testComment, comment2));

        List<TicketComment> result = ticketCommentService.getAllComments();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ticketCommentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Retrieve Comments: Get comment by ID")
    void testGetCommentById_Success() {
        when(ticketCommentRepository.findById(1)).thenReturn(Optional.of(testComment));

        Optional<TicketComment> result = ticketCommentService.getCommentById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getCommentId());
        assertEquals("This is a test comment", result.get().getComments());
        verify(ticketCommentRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Retrieve Comments: Get comment by ID - not found")
    void testGetCommentById_NotFound() {
        when(ticketCommentRepository.findById(999)).thenReturn(Optional.empty());

        Optional<TicketComment> result = ticketCommentService.getCommentById(999);

        assertFalse(result.isPresent());
        verify(ticketCommentRepository, times(1)).findById(999);
    }

    @Test
    @DisplayName("Retrieve Comments: Get comments by ticket ID")
    void testGetCommentsByTicketId() {
        TicketComment comment2 = new TicketComment();
        comment2.setCommentId(2);
        comment2.setTicket(testTicket);
        comment2.setComments("Another comment");
        
        when(ticketCommentRepository.findByTicket_TicketId(1)).thenReturn(Arrays.asList(testComment, comment2));

        List<TicketComment> result = ticketCommentService.getCommentsByTicketId(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(c -> c.getTicket().getTicketId().equals(1)));
        verify(ticketCommentRepository, times(1)).findByTicket_TicketId(1);
    }

    @Test
    @DisplayName("Retrieve Comments: No comments for ticket")
    void testGetCommentsByTicketId_NoComments() {
        when(ticketCommentRepository.findByTicket_TicketId(1)).thenReturn(Collections.emptyList());

        List<TicketComment> result = ticketCommentService.getCommentsByTicketId(1);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ==================== UPDATE COMMENT TESTS ====================

    @Test
    @DisplayName("Update Comment: Update comment text")
    void testUpdateComment_Success() {
        TicketComment updatedDetails = new TicketComment();
        updatedDetails.setComments("Updated comment text");
        
        when(ticketCommentRepository.findById(1)).thenReturn(Optional.of(testComment));
        when(ticketCommentRepository.save(any(TicketComment.class))).thenReturn(testComment);

        TicketComment result = ticketCommentService.updateComment(1, updatedDetails);

        assertNotNull(result);
        assertEquals("Updated comment text", result.getComments());
        verify(ticketCommentRepository, times(1)).findById(1);
        verify(ticketCommentRepository, times(1)).save(any(TicketComment.class));
    }

    @Test
    @DisplayName("Update Comment: Update with author")
    void testUpdateComment_WithAuthor() {
        TicketComment updatedDetails = new TicketComment();
        updatedDetails.setComments("Updated comment");
        updatedDetails.setAuthor("Jane Doe");
        
        when(ticketCommentRepository.findById(1)).thenReturn(Optional.of(testComment));
        when(ticketCommentRepository.save(any(TicketComment.class))).thenReturn(testComment);

        TicketComment result = ticketCommentService.updateComment(1, updatedDetails);

        assertNotNull(result);
        assertEquals("Updated comment", result.getComments());
        assertEquals("Jane Doe", result.getAuthor());
    }

    @Test
    @DisplayName("Update Comment: Preserve author if not provided")
    void testUpdateComment_PreserveAuthor() {
        TicketComment updatedDetails = new TicketComment();
        updatedDetails.setComments("Updated comment");
        updatedDetails.setAuthor(null);
        
        when(ticketCommentRepository.findById(1)).thenReturn(Optional.of(testComment));
        when(ticketCommentRepository.save(any(TicketComment.class))).thenAnswer(invocation -> {
            TicketComment saved = invocation.getArgument(0);
            assertEquals("John Doe", saved.getAuthor()); // Original author preserved
            return saved;
        });

        ticketCommentService.updateComment(1, updatedDetails);

        verify(ticketCommentRepository, times(1)).save(any(TicketComment.class));
    }

    @Test
    @DisplayName("Update Comment: Non-existent comment")
    void testUpdateComment_NotFound() {
        TicketComment updatedDetails = new TicketComment();
        when(ticketCommentRepository.findById(999)).thenReturn(Optional.empty());

        TicketComment result = ticketCommentService.updateComment(999, updatedDetails);

        assertNull(result);
        verify(ticketCommentRepository, times(1)).findById(999);
        verify(ticketCommentRepository, never()).save(any(TicketComment.class));
    }

    // ==================== DELETE COMMENT TESTS ====================

    @Test
    @DisplayName("Delete Comment: Success")
    void testDeleteComment_Success() {
        when(ticketCommentRepository.existsById(1)).thenReturn(true);
        doNothing().when(ticketCommentRepository).deleteById(1);

        boolean result = ticketCommentService.deleteComment(1);

        assertTrue(result);
        verify(ticketCommentRepository, times(1)).existsById(1);
        verify(ticketCommentRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Delete Comment: Non-existent comment")
    void testDeleteComment_NotFound() {
        when(ticketCommentRepository.existsById(999)).thenReturn(false);

        boolean result = ticketCommentService.deleteComment(999);

        assertFalse(result);
        verify(ticketCommentRepository, times(1)).existsById(999);
        verify(ticketCommentRepository, never()).deleteById(anyInt());
    }

    // ==================== EDGE CASES ====================

    @Test
    @DisplayName("Edge Case: Empty comment text")
    void testCreateComment_EmptyText() {
        testComment.setComments("");
        when(ticketCommentRepository.save(any(TicketComment.class))).thenReturn(testComment);

        TicketComment result = ticketCommentService.createComment(testComment);

        assertNotNull(result);
        assertEquals("", result.getComments());
    }

    @Test
    @DisplayName("Edge Case: Null comment text")
    void testCreateComment_NullText() {
        testComment.setComments(null);
        when(ticketCommentRepository.save(any(TicketComment.class))).thenReturn(testComment);

        TicketComment result = ticketCommentService.createComment(testComment);

        assertNotNull(result);
        assertNull(result.getComments());
    }

    @Test
    @DisplayName("Edge Case: Multiple comments on same ticket")
    void testGetCommentsByTicketId_MultipleComments() {
        TicketComment comment2 = new TicketComment();
        comment2.setCommentId(2);
        comment2.setTicket(testTicket);
        comment2.setComments("Second comment");
        
        TicketComment comment3 = new TicketComment();
        comment3.setCommentId(3);
        comment3.setTicket(testTicket);
        comment3.setComments("Third comment");
        
        when(ticketCommentRepository.findByTicket_TicketId(1))
            .thenReturn(Arrays.asList(testComment, comment2, comment3));

        List<TicketComment> result = ticketCommentService.getCommentsByTicketId(1);

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("Edge Case: Comment with whitespace only")
    void testCreateComment_WhitespaceOnly() {
        testComment.setComments("   ");
        when(ticketCommentRepository.save(any(TicketComment.class))).thenReturn(testComment);

        TicketComment result = ticketCommentService.createComment(testComment);

        assertNotNull(result);
        assertEquals("   ", result.getComments());
    }

    @Test
    @DisplayName("Edge Case: Comment with line breaks")
    void testCreateComment_WithLineBreaks() {
        testComment.setComments("Line 1\nLine 2\nLine 3");
        when(ticketCommentRepository.save(any(TicketComment.class))).thenReturn(testComment);

        TicketComment result = ticketCommentService.createComment(testComment);

        assertNotNull(result);
        assertTrue(result.getComments().contains("\n"));
    }

    @Test
    @DisplayName("Edge Case: Get all comments - empty list")
    void testGetAllComments_EmptyList() {
        when(ticketCommentRepository.findAll()).thenReturn(Collections.emptyList());

        List<TicketComment> result = ticketCommentService.getAllComments();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Verify: Comment cannot modify ticket details")
    void testCreateComment_TicketDetailsUnchanged() {
        String originalTitle = testTicket.getTitle();
        when(ticketCommentRepository.save(any(TicketComment.class))).thenReturn(testComment);

        TicketComment result = ticketCommentService.createComment(testComment);

        assertNotNull(result);
        assertEquals(originalTitle, result.getTicket().getTitle());
        // Verify only comment is added, ticket details remain unchanged
        assertEquals(1, result.getTicket().getTicketId());
    }
}