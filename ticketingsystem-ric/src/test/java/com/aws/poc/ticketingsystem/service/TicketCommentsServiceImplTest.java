package com.aws.poc.ticketingsystem.service;

import com.aws.poc.ticketingsystem.entity.TicketCommentsTbl;
import com.aws.poc.ticketingsystem.entity.TicketTbl;
import com.aws.poc.ticketingsystem.repository.TicketCommentsRepository;
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
 * Unit tests for TicketCommentsServiceImpl
 * Tests business logic for ticket comments operations
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Ticket Comments Service Implementation Tests")
class TicketCommentsServiceImplTest {

    @Mock
    private TicketCommentsRepository ticketCommentsRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketCommentsServiceImpl ticketCommentsService;

    private TicketTbl sampleTicket;
    private TicketCommentsTbl sampleComment;
    private List<TicketCommentsTbl> sampleComments;

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

        sampleComment = new TicketCommentsTbl();
        sampleComment.setCommentId(1);
        sampleComment.setComments("Test comment");
        sampleComment.setAuthor("Test User");
        sampleComment.setCreatedDate(LocalDateTime.now());
        sampleComment.setTicket(sampleTicket);

        sampleComments = new ArrayList<>();
        sampleComments.add(sampleComment);
    }

    // ========================================
    // Find Comments By Ticket ID Tests
    // ========================================

    @Test
    @DisplayName("Find comments by ticket ID - should return list of comments")
    void testFindCommentsByTicketId() {
        when(ticketCommentsRepository.findByTicketIdOrderByCreatedDateDesc(1)).thenReturn(sampleComments);

        List<TicketCommentsTbl> result = ticketCommentsService.findCommentsByTicketId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test comment", result.get(0).getComments());
        assertEquals("Test User", result.get(0).getAuthor());
        verify(ticketCommentsRepository).findByTicketIdOrderByCreatedDateDesc(1);
    }

    @Test
    @DisplayName("Find comments by ticket ID - should return empty list when no comments exist")
    void testFindCommentsByTicketIdEmpty() {
        when(ticketCommentsRepository.findByTicketIdOrderByCreatedDateDesc(1)).thenReturn(new ArrayList<>());

        List<TicketCommentsTbl> result = ticketCommentsService.findCommentsByTicketId(1);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(ticketCommentsRepository).findByTicketIdOrderByCreatedDateDesc(1);
    }

    @Test
    @DisplayName("Find comments by ticket ID - should handle non-existent ticket")
    void testFindCommentsByNonExistentTicket() {
        when(ticketCommentsRepository.findByTicketIdOrderByCreatedDateDesc(999)).thenReturn(new ArrayList<>());

        List<TicketCommentsTbl> result = ticketCommentsService.findCommentsByTicketId(999);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(ticketCommentsRepository).findByTicketIdOrderByCreatedDateDesc(999);
    }

    @Test
    @DisplayName("Find comments by ticket ID - should handle multiple comments")
    void testFindMultipleComments() {
        List<TicketCommentsTbl> multipleComments = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            TicketCommentsTbl comment = new TicketCommentsTbl();
            comment.setCommentId(i);
            comment.setComments("Comment " + i);
            comment.setAuthor("User " + i);
            comment.setCreatedDate(LocalDateTime.now());
            multipleComments.add(comment);
        }

        when(ticketCommentsRepository.findByTicketIdOrderByCreatedDateDesc(1)).thenReturn(multipleComments);

        List<TicketCommentsTbl> result = ticketCommentsService.findCommentsByTicketId(1);

        assertNotNull(result);
        assertEquals(10, result.size());
        verify(ticketCommentsRepository).findByTicketIdOrderByCreatedDateDesc(1);
    }

    // ========================================
    // Add Comment To Ticket Tests
    // ========================================

    @Test
    @DisplayName("Add comment to ticket - should save and return comment")
    void testAddCommentToTicket() {
        when(ticketRepository.findById(1)).thenReturn(Optional.of(sampleTicket));
        when(ticketCommentsRepository.save(any(TicketCommentsTbl.class))).thenReturn(sampleComment);

        TicketCommentsTbl result = ticketCommentsService.addCommentToTicket(1, "Test comment", "Test User");

        assertNotNull(result);
        assertEquals("Test comment", result.getComments());
        assertEquals("Test User", result.getAuthor());
        assertNotNull(result.getCreatedDate());
        verify(ticketRepository).findById(1);
        verify(ticketCommentsRepository).save(any(TicketCommentsTbl.class));
    }

    @Test
    @DisplayName("Add comment to ticket - should throw exception for non-existent ticket")
    void testAddCommentToNonExistentTicket() {
        when(ticketRepository.findById(999)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ticketCommentsService.addCommentToTicket(999, "Test comment", "Test User");
        });

        assertTrue(exception.getMessage().contains("Ticket not found"));
        verify(ticketRepository).findById(999);
        verify(ticketCommentsRepository, never()).save(any(TicketCommentsTbl.class));
    }

    @Test
    @DisplayName("Add comment to ticket - should handle very long comment text")
    void testAddVeryLongComment() {
        String longComment = "A".repeat(10000);
        TicketCommentsTbl longCommentObj = new TicketCommentsTbl();
        longCommentObj.setCommentId(1);
        longCommentObj.setComments(longComment);
        longCommentObj.setAuthor("Test User");
        longCommentObj.setCreatedDate(LocalDateTime.now());

        when(ticketRepository.findById(1)).thenReturn(Optional.of(sampleTicket));
        when(ticketCommentsRepository.save(any(TicketCommentsTbl.class))).thenReturn(longCommentObj);

        TicketCommentsTbl result = ticketCommentsService.addCommentToTicket(1, longComment, "Test User");

        assertNotNull(result);
        assertEquals(longComment, result.getComments());
        verify(ticketCommentsRepository).save(any(TicketCommentsTbl.class));
    }

    @Test
    @DisplayName("Add comment to ticket - should handle special characters")
    void testAddCommentWithSpecialCharacters() {
        String specialComment = "Test @#$% comment with ;DROP TABLE; and <script>alert('xss')</script>";
        TicketCommentsTbl specialCommentObj = new TicketCommentsTbl();
        specialCommentObj.setCommentId(1);
        specialCommentObj.setComments(specialComment);
        specialCommentObj.setAuthor("Test User");
        specialCommentObj.setCreatedDate(LocalDateTime.now());

        when(ticketRepository.findById(1)).thenReturn(Optional.of(sampleTicket));
        when(ticketCommentsRepository.save(any(TicketCommentsTbl.class))).thenReturn(specialCommentObj);

        TicketCommentsTbl result = ticketCommentsService.addCommentToTicket(1, specialComment, "Test User");

        assertNotNull(result);
        assertEquals(specialComment, result.getComments());
        verify(ticketCommentsRepository).save(any(TicketCommentsTbl.class));
    }

    @Test
    @DisplayName("Add comment to ticket - should handle minimum valid comment")
    void testAddMinimumComment() {
        String minComment = "A";
        TicketCommentsTbl minCommentObj = new TicketCommentsTbl();
        minCommentObj.setCommentId(1);
        minCommentObj.setComments(minComment);
        minCommentObj.setAuthor("U");
        minCommentObj.setCreatedDate(LocalDateTime.now());

        when(ticketRepository.findById(1)).thenReturn(Optional.of(sampleTicket));
        when(ticketCommentsRepository.save(any(TicketCommentsTbl.class))).thenReturn(minCommentObj);

        TicketCommentsTbl result = ticketCommentsService.addCommentToTicket(1, minComment, "U");

        assertNotNull(result);
        assertEquals(minComment, result.getComments());
        assertEquals("U", result.getAuthor());
        verify(ticketCommentsRepository).save(any(TicketCommentsTbl.class));
    }

    @Test
    @DisplayName("Add comment to ticket - should handle rapid succession of comments")
    void testAddMultipleCommentsRapidly() {
        when(ticketRepository.findById(1)).thenReturn(Optional.of(sampleTicket));

        for (int i = 1; i <= 10; i++) {
            TicketCommentsTbl comment = new TicketCommentsTbl();
            comment.setCommentId(i);
            comment.setComments("Comment " + i);
            comment.setAuthor("Test User");
            comment.setCreatedDate(LocalDateTime.now());

            when(ticketCommentsRepository.save(any(TicketCommentsTbl.class))).thenReturn(comment);

            TicketCommentsTbl result = ticketCommentsService.addCommentToTicket(1, "Comment " + i, "Test User");
            assertNotNull(result);
        }

        verify(ticketRepository, times(10)).findById(1);
        verify(ticketCommentsRepository, times(10)).save(any(TicketCommentsTbl.class));
    }

    @Test
    @DisplayName("Add comment to ticket - should handle multiline comment")
    void testAddMultilineComment() {
        String multilineComment = "Line 1\nLine 2\nLine 3\nLine 4";
        TicketCommentsTbl multilineCommentObj = new TicketCommentsTbl();
        multilineCommentObj.setCommentId(1);
        multilineCommentObj.setComments(multilineComment);
        multilineCommentObj.setAuthor("Test User");
        multilineCommentObj.setCreatedDate(LocalDateTime.now());

        when(ticketRepository.findById(1)).thenReturn(Optional.of(sampleTicket));
        when(ticketCommentsRepository.save(any(TicketCommentsTbl.class))).thenReturn(multilineCommentObj);

        TicketCommentsTbl result = ticketCommentsService.addCommentToTicket(1, multilineComment, "Test User");

        assertNotNull(result);
        assertEquals(multilineComment, result.getComments());
        verify(ticketCommentsRepository).save(any(TicketCommentsTbl.class));
    }

    // ========================================
    // Count Comments By Ticket ID Tests
    // ========================================

    @Test
    @DisplayName("Count comments by ticket ID - should return correct count")
    void testCountCommentsByTicketId() {
        when(ticketCommentsRepository.countByTicketId(1)).thenReturn(5L);

        Long result = ticketCommentsService.countCommentsByTicketId(1);

        assertEquals(5L, result);
        verify(ticketCommentsRepository).countByTicketId(1);
    }

    @Test
    @DisplayName("Count comments by ticket ID - should return zero when no comments exist")
    void testCountCommentsByTicketIdZero() {
        when(ticketCommentsRepository.countByTicketId(1)).thenReturn(0L);

        Long result = ticketCommentsService.countCommentsByTicketId(1);

        assertEquals(0L, result);
        verify(ticketCommentsRepository).countByTicketId(1);
    }

    @Test
    @DisplayName("Count comments by ticket ID - should handle non-existent ticket")
    void testCountCommentsByNonExistentTicket() {
        when(ticketCommentsRepository.countByTicketId(999)).thenReturn(0L);

        Long result = ticketCommentsService.countCommentsByTicketId(999);

        assertEquals(0L, result);
        verify(ticketCommentsRepository).countByTicketId(999);
    }

    @Test
    @DisplayName("Count comments by ticket ID - should handle large count")
    void testCountCommentsLargeCount() {
        when(ticketCommentsRepository.countByTicketId(1)).thenReturn(1000L);

        Long result = ticketCommentsService.countCommentsByTicketId(1);

        assertEquals(1000L, result);
        verify(ticketCommentsRepository).countByTicketId(1);
    }

    // ========================================
    // Edge Case Tests
    // ========================================

    @Test
    @DisplayName("Edge Case: Repository throws exception when finding comments")
    void testFindCommentsException() {
        when(ticketCommentsRepository.findByTicketIdOrderByCreatedDateDesc(anyInt()))
                .thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> {
            ticketCommentsService.findCommentsByTicketId(1);
        });
    }

    @Test
    @DisplayName("Edge Case: Repository throws exception when saving comment")
    void testSaveCommentException() {
        when(ticketRepository.findById(1)).thenReturn(Optional.of(sampleTicket));
        when(ticketCommentsRepository.save(any(TicketCommentsTbl.class)))
                .thenThrow(new RuntimeException("Save failed"));

        assertThrows(RuntimeException.class, () -> {
            ticketCommentsService.addCommentToTicket(1, "Test comment", "Test User");
        });
    }

    @Test
    @DisplayName("Edge Case: Repository throws exception when counting comments")
    void testCountCommentsException() {
        when(ticketCommentsRepository.countByTicketId(anyInt()))
                .thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> {
            ticketCommentsService.countCommentsByTicketId(1);
        });
    }

    @Test
    @DisplayName("Edge Case: Add comment with Unicode characters")
    void testAddCommentWithUnicode() {
        String unicodeComment = "Test comment with Unicode: 你好 مرحبا שלום";
        TicketCommentsTbl unicodeCommentObj = new TicketCommentsTbl();
        unicodeCommentObj.setCommentId(1);
        unicodeCommentObj.setComments(unicodeComment);
        unicodeCommentObj.setAuthor("Test User");
        unicodeCommentObj.setCreatedDate(LocalDateTime.now());

        when(ticketRepository.findById(1)).thenReturn(Optional.of(sampleTicket));
        when(ticketCommentsRepository.save(any(TicketCommentsTbl.class))).thenReturn(unicodeCommentObj);

        TicketCommentsTbl result = ticketCommentsService.addCommentToTicket(1, unicodeComment, "Test User");

        assertNotNull(result);
        assertEquals(unicodeComment, result.getComments());
        verify(ticketCommentsRepository).save(any(TicketCommentsTbl.class));
    }

    @Test
    @DisplayName("Edge Case: Add comment with emojis")
    void testAddCommentWithEmojis() {
        String emojiComment = "Great work! 👍 😊 🎉";
        TicketCommentsTbl emojiCommentObj = new TicketCommentsTbl();
        emojiCommentObj.setCommentId(1);
        emojiCommentObj.setComments(emojiComment);
        emojiCommentObj.setAuthor("Test User");
        emojiCommentObj.setCreatedDate(LocalDateTime.now());

        when(ticketRepository.findById(1)).thenReturn(Optional.of(sampleTicket));
        when(ticketCommentsRepository.save(any(TicketCommentsTbl.class))).thenReturn(emojiCommentObj);

        TicketCommentsTbl result = ticketCommentsService.addCommentToTicket(1, emojiComment, "Test User");

        assertNotNull(result);
        assertEquals(emojiComment, result.getComments());
        verify(ticketCommentsRepository).save(any(TicketCommentsTbl.class));
    }

    @Test
    @DisplayName("Edge Case: Verify comment timestamp is set correctly")
    void testCommentTimestampSet() {
        LocalDateTime beforeSave = LocalDateTime.now();
        
        when(ticketRepository.findById(1)).thenReturn(Optional.of(sampleTicket));
        when(ticketCommentsRepository.save(any(TicketCommentsTbl.class))).thenAnswer(invocation -> {
            TicketCommentsTbl comment = invocation.getArgument(0);
            comment.setCommentId(1);
            return comment;
        });

        TicketCommentsTbl result = ticketCommentsService.addCommentToTicket(1, "Test comment", "Test User");

        assertNotNull(result.getCreatedDate());
        assertTrue(result.getCreatedDate().isAfter(beforeSave.minusSeconds(1)));
        assertTrue(result.getCreatedDate().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    @DisplayName("Edge Case: Verify comment is associated with correct ticket")
    void testCommentTicketAssociation() {
        when(ticketRepository.findById(1)).thenReturn(Optional.of(sampleTicket));
        when(ticketCommentsRepository.save(any(TicketCommentsTbl.class))).thenAnswer(invocation -> {
            TicketCommentsTbl comment = invocation.getArgument(0);
            comment.setCommentId(1);
            return comment;
        });

        TicketCommentsTbl result = ticketCommentsService.addCommentToTicket(1, "Test comment", "Test User");

        assertNotNull(result.getTicket());
        assertEquals(1, result.getTicket().getTicketId());
        verify(ticketRepository).findById(1);
    }
}