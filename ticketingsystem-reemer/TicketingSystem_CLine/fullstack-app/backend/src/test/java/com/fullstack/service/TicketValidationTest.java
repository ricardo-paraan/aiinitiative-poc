package com.fullstack.service;

import com.fullstack.entity.Ticket;
import com.fullstack.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for Ticket Validation and Edge Cases
 * Tests cover: Input validation, duplicate prevention, boundary conditions
 */
@ExtendWith(MockitoExtension.class)
class TicketValidationTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private StatusHistoryService statusHistoryService;

    @InjectMocks
    private TicketService ticketService;

    private Ticket validTicket;

    @BeforeEach
    void setUp() {
        validTicket = new Ticket();
        validTicket.setTicketId(1);
        validTicket.setTitle("Valid Ticket");
        validTicket.setAuthor("John Doe");
        validTicket.setSystemName("CRM System");
        validTicket.setCategory("Bug");
        validTicket.setDescription("Valid description");
        validTicket.setStatus("Pending");
        validTicket.setCreatedDate(LocalDate.now());
        validTicket.setUpdateDate(LocalDate.now());
    }

    // ==================== EMPTY/NULL INPUT VALIDATION ====================

    @Test
    @DisplayName("Validation: Empty title")
    void testValidation_EmptyTitle() {
        Ticket ticket = new Ticket();
        ticket.setTitle("");
        ticket.setSystemName("CRM System");
        ticket.setCategory("Bug");
        ticket.setDescription("Description");
        ticket.setStatus("Pending");

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(ticket);

        assertNotNull(result);
        assertEquals("", result.getTitle());
    }

    @Test
    @DisplayName("Validation: Null title")
    void testValidation_NullTitle() {
        Ticket ticket = new Ticket();
        ticket.setTitle(null);
        ticket.setSystemName("CRM System");
        ticket.setCategory("Bug");
        ticket.setDescription("Description");
        ticket.setStatus("Pending");

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(ticket);

        assertNotNull(result);
        assertNull(result.getTitle());
    }

    @Test
    @DisplayName("Validation: Empty system name")
    void testValidation_EmptySystemName() {
        Ticket ticket = new Ticket();
        ticket.setTitle("Test Ticket");
        ticket.setSystemName("");
        ticket.setCategory("Bug");
        ticket.setDescription("Description");
        ticket.setStatus("Pending");

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(ticket);

        assertNotNull(result);
        assertEquals("", result.getSystemName());
    }

    @Test
    @DisplayName("Validation: Null system name")
    void testValidation_NullSystemName() {
        Ticket ticket = new Ticket();
        ticket.setTitle("Test Ticket");
        ticket.setSystemName(null);
        ticket.setCategory("Bug");
        ticket.setDescription("Description");
        ticket.setStatus("Pending");

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(ticket);

        assertNotNull(result);
        assertNull(result.getSystemName());
    }

    @Test
    @DisplayName("Validation: Empty category")
    void testValidation_EmptyCategory() {
        Ticket ticket = new Ticket();
        ticket.setTitle("Test Ticket");
        ticket.setSystemName("CRM System");
        ticket.setCategory("");
        ticket.setDescription("Description");
        ticket.setStatus("Pending");

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(ticket);

        assertNotNull(result);
        assertEquals("", result.getCategory());
    }

    @Test
    @DisplayName("Validation: Empty description")
    void testValidation_EmptyDescription() {
        Ticket ticket = new Ticket();
        ticket.setTitle("Test Ticket");
        ticket.setSystemName("CRM System");
        ticket.setCategory("Bug");
        ticket.setDescription("");
        ticket.setStatus("Pending");

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(ticket);

        assertNotNull(result);
        assertEquals("", result.getDescription());
    }

    @Test
    @DisplayName("Validation: Null description")
    void testValidation_NullDescription() {
        Ticket ticket = new Ticket();
        ticket.setTitle("Test Ticket");
        ticket.setSystemName("CRM System");
        ticket.setCategory("Bug");
        ticket.setDescription(null);
        ticket.setStatus("Pending");

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(ticket);

        assertNotNull(result);
        assertNull(result.getDescription());
    }

    // ==================== LONG INPUT VALIDATION ====================

    @Test
    @DisplayName("Validation: Title at maximum length (100 chars)")
    void testValidation_TitleMaxLength() {
        String maxTitle = "A".repeat(100);
        Ticket ticket = new Ticket();
        ticket.setTitle(maxTitle);
        ticket.setSystemName("CRM System");
        ticket.setCategory("Bug");
        ticket.setDescription("Description");
        ticket.setStatus("Pending");

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(ticket);

        assertNotNull(result);
        assertEquals(100, result.getTitle().length());
        assertEquals(maxTitle, result.getTitle());
    }

    @Test
    @DisplayName("Validation: Description at maximum length (1000 chars)")
    void testValidation_DescriptionMaxLength() {
        String maxDescription = "A".repeat(1000);
        Ticket ticket = new Ticket();
        ticket.setTitle("Test Ticket");
        ticket.setSystemName("CRM System");
        ticket.setCategory("Bug");
        ticket.setDescription(maxDescription);
        ticket.setStatus("Pending");

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(ticket);

        assertNotNull(result);
        assertEquals(1000, result.getDescription().length());
        assertEquals(maxDescription, result.getDescription());
    }

    @Test
    @DisplayName("Validation: System name at maximum length (50 chars)")
    void testValidation_SystemNameMaxLength() {
        String maxSystemName = "A".repeat(50);
        Ticket ticket = new Ticket();
        ticket.setTitle("Test Ticket");
        ticket.setSystemName(maxSystemName);
        ticket.setCategory("Bug");
        ticket.setDescription("Description");
        ticket.setStatus("Pending");

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(ticket);

        assertNotNull(result);
        assertEquals(50, result.getSystemName().length());
    }

    @Test
    @DisplayName("Validation: Category at maximum length (50 chars)")
    void testValidation_CategoryMaxLength() {
        String maxCategory = "A".repeat(50);
        Ticket ticket = new Ticket();
        ticket.setTitle("Test Ticket");
        ticket.setSystemName("CRM System");
        ticket.setCategory(maxCategory);
        ticket.setDescription("Description");
        ticket.setStatus("Pending");

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(ticket);

        assertNotNull(result);
        assertEquals(50, result.getCategory().length());
    }

    // ==================== SPECIAL CHARACTERS VALIDATION ====================

    @Test
    @DisplayName("Validation: Title with special characters")
    void testValidation_TitleSpecialCharacters() {
        String specialTitle = "Test @#$%^&*()_+-=[]{}|;':\",./<>? Ticket";
        Ticket ticket = new Ticket();
        ticket.setTitle(specialTitle);
        ticket.setSystemName("CRM System");
        ticket.setCategory("Bug");
        ticket.setDescription("Description");
        ticket.setStatus("Pending");

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(ticket);

        assertNotNull(result);
        assertEquals(specialTitle, result.getTitle());
    }

    @Test
    @DisplayName("Validation: Description with special characters")
    void testValidation_DescriptionSpecialCharacters() {
        String specialDesc = "Description with @#$%^&*() special chars!";
        Ticket ticket = new Ticket();
        ticket.setTitle("Test Ticket");
        ticket.setSystemName("CRM System");
        ticket.setCategory("Bug");
        ticket.setDescription(specialDesc);
        ticket.setStatus("Pending");

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(ticket);

        assertNotNull(result);
        assertEquals(specialDesc, result.getDescription());
    }

    @Test
    @DisplayName("Validation: Title with line breaks")
    void testValidation_TitleWithLineBreaks() {
        String titleWithBreaks = "Line 1\nLine 2\nLine 3";
        Ticket ticket = new Ticket();
        ticket.setTitle(titleWithBreaks);
        ticket.setSystemName("CRM System");
        ticket.setCategory("Bug");
        ticket.setDescription("Description");
        ticket.setStatus("Pending");

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(ticket);

        assertNotNull(result);
        assertTrue(result.getTitle().contains("\n"));
    }

    @Test
    @DisplayName("Validation: Description with line breaks")
    void testValidation_DescriptionWithLineBreaks() {
        String descWithBreaks = "Paragraph 1\n\nParagraph 2\n\nParagraph 3";
        Ticket ticket = new Ticket();
        ticket.setTitle("Test Ticket");
        ticket.setSystemName("CRM System");
        ticket.setCategory("Bug");
        ticket.setDescription(descWithBreaks);
        ticket.setStatus("Pending");

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(ticket);

        assertNotNull(result);
        assertTrue(result.getDescription().contains("\n"));
    }

    // ==================== NON-ASCII CHARACTERS VALIDATION ====================

    @Test
    @DisplayName("Validation: Japanese characters in title")
    void testValidation_JapaneseTitle() {
        Ticket ticket = new Ticket();
        ticket.setTitle("テストチケット");
        ticket.setSystemName("CRMシステム");
        ticket.setCategory("バグ");
        ticket.setDescription("日本語の説明");
        ticket.setStatus("保留中");

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(ticket);

        assertNotNull(result);
        assertEquals("テストチケット", result.getTitle());
        assertEquals("CRMシステム", result.getSystemName());
        assertEquals("バグ", result.getCategory());
        assertEquals("日本語の説明", result.getDescription());
    }

    @Test
    @DisplayName("Validation: Chinese characters")
    void testValidation_ChineseCharacters() {
        Ticket ticket = new Ticket();
        ticket.setTitle("测试票据");
        ticket.setSystemName("CRM系统");
        ticket.setCategory("错误");
        ticket.setDescription("中文描述");
        ticket.setStatus("待处理");

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(ticket);

        assertNotNull(result);
        assertEquals("测试票据", result.getTitle());
    }

    @Test
    @DisplayName("Validation: Mixed ASCII and non-ASCII")
    void testValidation_MixedCharacters() {
        Ticket ticket = new Ticket();
        ticket.setTitle("Test チケット Ticket");
        ticket.setSystemName("CRM System システム");
        ticket.setCategory("Bug バグ");
        ticket.setDescription("Mixed 混合 description");
        ticket.setStatus("Pending");

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(ticket);

        assertNotNull(result);
        assertEquals("Test チケット Ticket", result.getTitle());
    }

    // ==================== DUPLICATE PREVENTION ====================

    @Test
    @DisplayName("Duplicate Prevention: Check for existing ticket with same title")
    void testDuplicatePrevention_SameTitle() {
        Ticket existingTicket = new Ticket();
        existingTicket.setTicketId(1);
        existingTicket.setTitle("Duplicate Title");
        existingTicket.setSystemName("CRM System");

        when(ticketRepository.findByTitleContainingIgnoreCase("Duplicate Title"))
            .thenReturn(Arrays.asList(existingTicket));

        List<Ticket> duplicates = ticketService.searchTicketsByTitle("Duplicate Title");

        assertNotNull(duplicates);
        assertFalse(duplicates.isEmpty());
        assertEquals(1, duplicates.size());
    }

    @Test
    @DisplayName("Duplicate Prevention: Verify unique ticket creation")
    void testDuplicatePrevention_UniqueTicket() {
        when(ticketRepository.findByTitleContainingIgnoreCase("Unique Title"))
            .thenReturn(Arrays.asList());

        List<Ticket> duplicates = ticketService.searchTicketsByTitle("Unique Title");

        assertNotNull(duplicates);
        assertTrue(duplicates.isEmpty());
    }

    // ==================== WHITESPACE HANDLING ====================

    @Test
    @DisplayName("Validation: Title with leading/trailing whitespace")
    void testValidation_TitleWhitespace() {
        Ticket ticket = new Ticket();
        ticket.setTitle("  Test Ticket  ");
        ticket.setSystemName("CRM System");
        ticket.setCategory("Bug");
        ticket.setDescription("Description");
        ticket.setStatus("Pending");

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(ticket);

        assertNotNull(result);
        assertEquals("  Test Ticket  ", result.getTitle());
    }

    @Test
    @DisplayName("Validation: Title with only whitespace")
    void testValidation_TitleOnlyWhitespace() {
        Ticket ticket = new Ticket();
        ticket.setTitle("     ");
        ticket.setSystemName("CRM System");
        ticket.setCategory("Bug");
        ticket.setDescription("Description");
        ticket.setStatus("Pending");

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(ticket);

        assertNotNull(result);
        assertEquals("     ", result.getTitle());
    }

    // ==================== TIMESTAMP VALIDATION ====================

    @Test
    @DisplayName("Validation: Created date is set on creation")
    void testValidation_CreatedDateSet() {
        Ticket ticket = new Ticket();
        ticket.setTitle("Test Ticket");
        ticket.setSystemName("CRM System");
        ticket.setCategory("Bug");
        ticket.setDescription("Description");
        ticket.setStatus("Pending");

        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> {
            Ticket saved = invocation.getArgument(0);
            saved.setTicketId(1);
            return saved;
        });
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(ticket);

        assertNotNull(result);
        assertNotNull(result.getCreatedDate());
        assertEquals(LocalDate.now(), result.getCreatedDate());
    }

    @Test
    @DisplayName("Validation: Update date is set on creation")
    void testValidation_UpdateDateSet() {
        Ticket ticket = new Ticket();
        ticket.setTitle("Test Ticket");
        ticket.setSystemName("CRM System");
        ticket.setCategory("Bug");
        ticket.setDescription("Description");
        ticket.setStatus("Pending");

        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> {
            Ticket saved = invocation.getArgument(0);
            saved.setTicketId(1);
            return saved;
        });
        when(statusHistoryService.createStatusHistory(any(), any(), any(), any())).thenReturn(null);

        Ticket result = ticketService.createTicket(ticket);

        assertNotNull(result);
        assertNotNull(result.getUpdateDate());
        assertEquals(LocalDate.now(), result.getUpdateDate());
    }

    @Test
    @DisplayName("Validation: Update date is refreshed on update")
    void testValidation_UpdateDateRefreshed() {
        Ticket updatedDetails = new Ticket();
        updatedDetails.setTitle("Updated Title");
        updatedDetails.setAuthor("John Doe");
        updatedDetails.setSystemName("CRM System");
        updatedDetails.setCategory("Bug");
        updatedDetails.setDescription("Description");
        updatedDetails.setStatus("Pending");

        when(ticketRepository.findById(1)).thenReturn(Optional.of(validTicket));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> {
            Ticket saved = invocation.getArgument(0);
            assertEquals(LocalDate.now(), saved.getUpdateDate());
            return saved;
        });

        ticketService.updateTicket(1, updatedDetails);

        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }
}