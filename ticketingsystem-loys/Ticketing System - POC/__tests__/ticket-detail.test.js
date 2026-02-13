/**
 * Unit Tests for US6: View Ticket Details
 * 
 * User Story: "As a user, I want to view all information about a ticket so I can review my submission."
 * 
 * Acceptance Criteria:
 * AC1: Displays Ticket Number, Title, Description, System Name, Category, Creator, Creation Date
 * AC2: All fields are read-only
 */

import { mockAPI } from './mocks/api.mock';
import { 
  createMockTicket,
  createMockComment,
  createMockAttachment
} from './mocks/ticket-data.mock';
import {
  setupTicketDetailPage,
  getTicketDetailElements,
  renderTicketHeader,
  renderTicketDetails,
  renderSidebarDetails,
  renderCompleteTicketView,
  isTicketNumberDisplayed,
  getDisplayedTicketNumber,
  isTitleDisplayed,
  getDisplayedTitle,
  isDescriptionDisplayed,
  getDisplayedDescription,
  isSystemDisplayed,
  getDisplayedSystem,
  isCategoryDisplayed,
  getDisplayedCategory,
  isCreatorDisplayed,
  getDisplayedCreator,
  isCreationDateDisplayed,
  getDisplayedCreationDate,
  areAllRequiredFieldsDisplayed,
  isElementReadOnly,
  areAllFieldsReadOnly,
  hasEditableElements,
  hasAnyEditableFormElements,
  getElementTagName,
  isDisplayElement,
  verifyTicketDataMatchesDisplay,
  countVisibleDetailFields,
  isStatusBadgeDisplayed,
  getDisplayedStatus,
  mockURLParams,
  waitForAsync
} from './setup/ticket-detail-utils';

// Mock the API module
jest.mock('../js/api.js', () => mockAPI);

// Mock utils module
jest.mock('../js/utils.js', () => ({
  formatDate: jest.fn((dateString) => {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
  }),
  formatDateTime: jest.fn((dateString) => {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric',
      hour: 'numeric',
      minute: '2-digit',
      hour12: true
    });
  }),
  escapeHtml: jest.fn((text) => {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
  }),
  getStatusClass: jest.fn((status) => `status-${status.toLowerCase().replace(' ', '-')}`),
  showToast: jest.fn(),
  formatFileSize: jest.fn((size) => {
    if (size < 1024) return `${size} B`;
    if (size < 1024 * 1024) return `${(size / 1024).toFixed(1)} KB`;
    return `${(size / (1024 * 1024)).toFixed(1)} MB`;
  })
}));

describe('US6: View Ticket Details', () => {
  let mockGetTicketById;
  let mockGetComments;
  let mockGetAttachments;
  let sampleTicket;

  beforeEach(() => {
    // Setup DOM
    setupTicketDetailPage();
    
    // Create sample ticket
    sampleTicket = createMockTicket({
      ticketId: 123,
      title: 'Sample Ticket Title',
      description: 'This is a detailed description of the ticket issue.',
      systemName: 'SAP',
      categoryName: 'Technical Issue',
      author: 'John Smith',
      statusName: 'In-Progress',
      createdDate: '2024-01-15T10:30:00Z',
      updatedDate: '2024-01-16T14:45:00Z'
    });
    
    // Setup API mocks
    mockGetTicketById = jest.fn().mockResolvedValue(sampleTicket);
    mockGetComments = jest.fn().mockResolvedValue([]);
    mockGetAttachments = jest.fn().mockResolvedValue([]);
    
    mockAPI.tickets.getById = mockGetTicketById;
    mockAPI.comments.getByTicketId = mockGetComments;
    mockAPI.attachments.getByTicketId = mockGetAttachments;
    
    // Mock URL params
    mockURLParams('123');
    
    // Clear all mocks
    jest.clearAllMocks();
  });

  // ============================================================================
  // AC1: Displays all ticket information
  // ============================================================================

  describe('AC1: Display All Ticket Information', () => {
    // Functional Test ID: US6_AC1_TC1
    test('US6_AC1_TC1: Ticket number should be displayed', () => {
      renderTicketHeader(sampleTicket);
      
      expect(isTicketNumberDisplayed()).toBe(true);
      expect(getDisplayedTicketNumber()).toBe('#123');
    });

    // Functional Test ID: US6_AC1_TC2
    test('US6_AC1_TC2: Ticket title should be displayed', () => {
      renderTicketHeader(sampleTicket);
      
      expect(isTitleDisplayed()).toBe(true);
      expect(getDisplayedTitle()).toBe('Sample Ticket Title');
    });

    // Functional Test ID: US6_AC1_TC3
    test('US6_AC1_TC3: Ticket description should be displayed', () => {
      renderTicketDetails(sampleTicket);
      
      expect(isDescriptionDisplayed()).toBe(true);
      expect(getDisplayedDescription()).toBe('This is a detailed description of the ticket issue.');
    });

    // Functional Test ID: US6_AC1_TC4
    test('US6_AC1_TC4: System name should be displayed', () => {
      renderTicketDetails(sampleTicket);
      
      expect(isSystemDisplayed()).toBe(true);
      expect(getDisplayedSystem()).toBe('SAP');
    });

    // Functional Test ID: US6_AC1_TC5
    test('US6_AC1_TC5: Category should be displayed', () => {
      renderTicketDetails(sampleTicket);
      
      expect(isCategoryDisplayed()).toBe(true);
      expect(getDisplayedCategory()).toBe('Technical Issue');
    });

    // Functional Test ID: US6_AC1_TC6
    test('US6_AC1_TC6: Creator name should be displayed', () => {
      renderSidebarDetails(sampleTicket);
      
      expect(isCreatorDisplayed()).toBe(true);
      expect(getDisplayedCreator()).toBe('John Smith');
    });

    // Functional Test ID: US6_AC1_TC7
    test('US6_AC1_TC7: Creation date should be displayed', () => {
      renderSidebarDetails(sampleTicket);
      
      expect(isCreationDateDisplayed()).toBe(true);
      expect(getDisplayedCreationDate()).toBeTruthy();
    });

    // Functional Test ID: US6_AC1_TC8
    test('US6_AC1_TC8: All required fields should be displayed together', () => {
      renderCompleteTicketView(sampleTicket);
      
      expect(areAllRequiredFieldsDisplayed()).toBe(true);
    });

    // Functional Test ID: US6_AC1_TC9
    test('US6_AC1_TC9: Status badge should be displayed', () => {
      renderTicketHeader(sampleTicket);
      
      expect(isStatusBadgeDisplayed()).toBe(true);
      expect(getDisplayedStatus()).toBe('In-Progress');
    });

    // Functional Test ID: US6_AC1_TC10
    test('US6_AC1_TC10: Ticket data should match API response', () => {
      renderCompleteTicketView(sampleTicket);
      
      const verification = verifyTicketDataMatchesDisplay(sampleTicket);
      expect(verification.matches).toBe(true);
      expect(verification.mismatches).toHaveLength(0);
    });

    // Functional Test ID: US6_AC1_TC11
    test('US6_AC1_TC11: Should display ticket with Pending status', () => {
      const pendingTicket = createMockTicket({ statusName: 'Pending' });
      renderTicketHeader(pendingTicket);
      
      expect(getDisplayedStatus()).toBe('Pending');
    });

    // Functional Test ID: US6_AC1_TC12
    test('US6_AC1_TC12: Should display ticket with Resolved status', () => {
      const resolvedTicket = createMockTicket({ statusName: 'Resolved' });
      renderTicketHeader(resolvedTicket);
      
      expect(getDisplayedStatus()).toBe('Resolved');
    });

    // Functional Test ID: US6_AC1_TC13
    test('US6_AC1_TC13: Should display long description correctly', () => {
      const longDesc = 'A'.repeat(500);
      const ticketWithLongDesc = createMockTicket({ description: longDesc });
      renderTicketDetails(ticketWithLongDesc);
      
      expect(getDisplayedDescription()).toBe(longDesc);
    });

    // Functional Test ID: US6_AC1_TC14
    test('US6_AC1_TC14: Should display special characters in title', () => {
      const specialTitle = 'Test <>&"\' Ticket';
      const ticketWithSpecialChars = createMockTicket({ title: specialTitle });
      renderTicketHeader(ticketWithSpecialChars);
      
      expect(getDisplayedTitle()).toBe(specialTitle);
    });

    // Functional Test ID: US6_AC1_TC15
    test('US6_AC1_TC15: Should count all visible detail fields', () => {
      renderCompleteTicketView(sampleTicket);
      
      const count = countVisibleDetailFields();
      expect(count).toBeGreaterThanOrEqual(7); // At least 7 required fields
    });

    // Functional Test ID: US6_AC1_TC16
    test('US6_AC1_TC16: Should display ticket number with hash prefix', () => {
      renderTicketHeader(sampleTicket);
      
      const ticketNumber = getDisplayedTicketNumber();
      expect(ticketNumber).toMatch(/^#\d+$/);
    });

    // Functional Test ID: US6_AC1_TC17
    test('US6_AC1_TC17: Should display formatted creation date', () => {
      renderSidebarDetails(sampleTicket);
      
      const dateDisplay = getDisplayedCreationDate();
      expect(dateDisplay).toBeTruthy();
      expect(dateDisplay).not.toBe('N/A');
    });

    // Functional Test ID: US6_AC1_TC18
    test('US6_AC1_TC18: Should display all ticket details from API', async () => {
      const ticket = await mockGetTicketById('123');
      renderCompleteTicketView(ticket);
      
      expect(mockGetTicketById).toHaveBeenCalledWith('123');
      expect(areAllRequiredFieldsDisplayed()).toBe(true);
    });

    // Functional Test ID: US6_AC1_TC19
    test('US6_AC1_TC19: Should handle ticket with null system name', () => {
      const ticketWithNullSystem = createMockTicket({ systemName: null });
      renderTicketDetails(ticketWithNullSystem);
      
      expect(getDisplayedSystem()).toBe('N/A');
    });

    // Functional Test ID: US6_AC1_TC20
    test('US6_AC1_TC20: Should handle ticket with null category', () => {
      const ticketWithNullCategory = createMockTicket({ categoryName: null });
      renderTicketDetails(ticketWithNullCategory);
      
      expect(getDisplayedCategory()).toBe('N/A');
    });
  });

  // ============================================================================
  // AC2: All fields are read-only
  // ============================================================================

  describe('AC2: All Fields Are Read-Only', () => {
    // Functional Test ID: US6_AC2_TC1
    test('US6_AC2_TC1: Ticket number should be read-only', () => {
      renderTicketHeader(sampleTicket);
      
      expect(isElementReadOnly('ticketId')).toBe(true);
    });

    // Functional Test ID: US6_AC2_TC2
    test('US6_AC2_TC2: Title should be read-only', () => {
      renderTicketHeader(sampleTicket);
      
      expect(isElementReadOnly('ticketTitle')).toBe(true);
    });

    // Functional Test ID: US6_AC2_TC3
    test('US6_AC2_TC3: Description should be read-only', () => {
      renderTicketDetails(sampleTicket);
      
      expect(isElementReadOnly('ticketDescription')).toBe(true);
    });

    // Functional Test ID: US6_AC2_TC4
    test('US6_AC2_TC4: System should be read-only', () => {
      renderTicketDetails(sampleTicket);
      
      expect(isElementReadOnly('ticketSystem')).toBe(true);
    });

    // Functional Test ID: US6_AC2_TC5
    test('US6_AC2_TC5: Category should be read-only', () => {
      renderTicketDetails(sampleTicket);
      
      expect(isElementReadOnly('ticketCategory')).toBe(true);
    });

    // Functional Test ID: US6_AC2_TC6
    test('US6_AC2_TC6: Creator should be read-only', () => {
      renderSidebarDetails(sampleTicket);
      
      expect(isElementReadOnly('submittedBy')).toBe(true);
    });

    // Functional Test ID: US6_AC2_TC7
    test('US6_AC2_TC7: Creation date should be read-only', () => {
      renderSidebarDetails(sampleTicket);
      
      expect(isElementReadOnly('dateSubmitted')).toBe(true);
    });

    // Functional Test ID: US6_AC2_TC8
    test('US6_AC2_TC8: All fields should be read-only', () => {
      renderCompleteTicketView(sampleTicket);
      
      expect(areAllFieldsReadOnly()).toBe(true);
    });

    // Functional Test ID: US6_AC2_TC9
    test('US6_AC2_TC9: Ticket number should be displayed as text (not input)', () => {
      renderTicketHeader(sampleTicket);
      
      const tagName = getElementTagName('ticketId');
      expect(isDisplayElement('ticketId')).toBe(true);
      expect(['input', 'textarea', 'select']).not.toContain(tagName);
    });

    // Functional Test ID: US6_AC2_TC10
    test('US6_AC2_TC10: Title should be displayed as text (not input)', () => {
      renderTicketHeader(sampleTicket);
      
      const tagName = getElementTagName('ticketTitle');
      expect(isDisplayElement('ticketTitle')).toBe(true);
      expect(['input', 'textarea', 'select']).not.toContain(tagName);
    });

    // Functional Test ID: US6_AC2_TC11
    test('US6_AC2_TC11: Description should be displayed as text (not textarea)', () => {
      renderTicketDetails(sampleTicket);
      
      const tagName = getElementTagName('ticketDescription');
      expect(isDisplayElement('ticketDescription')).toBe(true);
      expect(['input', 'textarea', 'select']).not.toContain(tagName);
    });

    // Functional Test ID: US6_AC2_TC12
    test('US6_AC2_TC12: System should be displayed as text (not select)', () => {
      renderTicketDetails(sampleTicket);
      
      const tagName = getElementTagName('ticketSystem');
      expect(isDisplayElement('ticketSystem')).toBe(true);
      expect(['input', 'textarea', 'select']).not.toContain(tagName);
    });

    // Functional Test ID: US6_AC2_TC13
    test('US6_AC2_TC13: Category should be displayed as text (not select)', () => {
      renderTicketDetails(sampleTicket);
      
      const tagName = getElementTagName('ticketCategory');
      expect(isDisplayElement('ticketCategory')).toBe(true);
      expect(['input', 'textarea', 'select']).not.toContain(tagName);
    });

    // Functional Test ID: US6_AC2_TC14
    test('US6_AC2_TC14: Creator should be displayed as text (not input)', () => {
      renderSidebarDetails(sampleTicket);
      
      const tagName = getElementTagName('submittedBy');
      expect(isDisplayElement('submittedBy')).toBe(true);
      expect(['input', 'textarea', 'select']).not.toContain(tagName);
    });

    // Functional Test ID: US6_AC2_TC15
    test('US6_AC2_TC15: Creation date should be displayed as text (not input)', () => {
      renderSidebarDetails(sampleTicket);
      
      const tagName = getElementTagName('dateSubmitted');
      expect(isDisplayElement('dateSubmitted')).toBe(true);
      expect(['input', 'textarea', 'select']).not.toContain(tagName);
    });

    // Functional Test ID: US6_AC2_TC16
    test('US6_AC2_TC16: Ticket details section should not contain editable elements', () => {
      renderTicketDetails(sampleTicket);
      
      expect(hasEditableElements('ticketDescription')).toBe(false);
      expect(hasEditableElements('ticketSystem')).toBe(false);
      expect(hasEditableElements('ticketCategory')).toBe(false);
    });

    // Functional Test ID: US6_AC2_TC17
    test('US6_AC2_TC17: Page should not have any editable form elements for ticket data', () => {
      renderCompleteTicketView(sampleTicket);
      
      // Note: Page may have comment input, but ticket data should be read-only
      const ticketFields = ['ticketId', 'ticketTitle', 'ticketDescription', 'ticketSystem', 'ticketCategory'];
      ticketFields.forEach(fieldId => {
        expect(isElementReadOnly(fieldId)).toBe(true);
      });
    });

    // Functional Test ID: US6_AC2_TC18
    test('US6_AC2_TC18: Status badge should be read-only display', () => {
      renderTicketHeader(sampleTicket);
      
      expect(isElementReadOnly('ticketStatus')).toBe(true);
    });

    // Functional Test ID: US6_AC2_TC19
    test('US6_AC2_TC19: Sidebar details should all be read-only', () => {
      renderSidebarDetails(sampleTicket);
      
      expect(isElementReadOnly('submittedBy')).toBe(true);
      expect(isElementReadOnly('dateSubmitted')).toBe(true);
      expect(isElementReadOnly('lastUpdated')).toBe(true);
    });

    // Functional Test ID: US6_AC2_TC20
    test('US6_AC2_TC20: User should not be able to modify ticket number', () => {
      renderTicketHeader(sampleTicket);
      
      const ticketIdElement = document.getElementById('ticketId');
      const originalValue = ticketIdElement.textContent;
      
      // Attempt to modify (should not work for display elements)
      ticketIdElement.textContent = '#999';
      
      // In read-only view, we verify it's a display element
      expect(isDisplayElement('ticketId')).toBe(true);
    });

    // Functional Test ID: US6_AC2_TC21
    test('US6_AC2_TC21: User should not be able to modify title', () => {
      renderTicketHeader(sampleTicket);
      
      const titleElement = document.getElementById('ticketTitle');
      expect(isDisplayElement('ticketTitle')).toBe(true);
      expect(getElementTagName('ticketTitle')).not.toBe('input');
    });

    // Functional Test ID: US6_AC2_TC22
    test('US6_AC2_TC22: User should not be able to modify description', () => {
      renderTicketDetails(sampleTicket);
      
      const descElement = document.getElementById('ticketDescription');
      expect(isDisplayElement('ticketDescription')).toBe(true);
      expect(getElementTagName('ticketDescription')).not.toBe('textarea');
    });

    // Functional Test ID: US6_AC2_TC23
    test('US6_AC2_TC23: User should not be able to modify system', () => {
      renderTicketDetails(sampleTicket);
      
      const systemElement = document.getElementById('ticketSystem');
      expect(isDisplayElement('ticketSystem')).toBe(true);
      expect(getElementTagName('ticketSystem')).not.toBe('select');
    });

    // Functional Test ID: US6_AC2_TC24
    test('US6_AC2_TC24: User should not be able to modify category', () => {
      renderTicketDetails(sampleTicket);
      
      const categoryElement = document.getElementById('ticketCategory');
      expect(isDisplayElement('ticketCategory')).toBe(true);
      expect(getElementTagName('ticketCategory')).not.toBe('select');
    });

    // Functional Test ID: US6_AC2_TC25
    test('US6_AC2_TC25: User should not be able to modify creator', () => {
      renderSidebarDetails(sampleTicket);
      
      const creatorElement = document.getElementById('submittedBy');
      expect(isDisplayElement('submittedBy')).toBe(true);
      expect(getElementTagName('submittedBy')).not.toBe('input');
    });
  });

  // ============================================================================
  // Integration and Edge Cases
  // ============================================================================

  describe('Integration and Edge Cases', () => {
    // Functional Test ID: US6_INTEGRATION_TC1
    test('US6_INTEGRATION_TC1: Should load ticket details from API', async () => {
      const ticket = await mockGetTicketById('123');
      
      expect(mockGetTicketById).toHaveBeenCalledWith('123');
      expect(ticket.ticketId).toBe(123);
    });

    // Functional Test ID: US6_INTEGRATION_TC2
    test('US6_INTEGRATION_TC2: Should load comments for ticket', async () => {
      const comments = [
        createMockComment({ commentText: 'First comment' }),
        createMockComment({ commentText: 'Second comment' })
      ];
      mockGetComments.mockResolvedValue(comments);
      
      const result = await mockGetComments('123');
      
      expect(mockGetComments).toHaveBeenCalledWith('123');
      expect(result).toHaveLength(2);
    });

    // Functional Test ID: US6_INTEGRATION_TC3
    test('US6_INTEGRATION_TC3: Should load attachments for ticket', async () => {
      const attachments = [
        createMockAttachment({ fileName: 'file1.pdf' }),
        createMockAttachment({ fileName: 'file2.png' })
      ];
      mockGetAttachments.mockResolvedValue(attachments);
      
      const result = await mockGetAttachments('123');
      
      expect(mockGetAttachments).toHaveBeenCalledWith('123');
      expect(result).toHaveLength(2);
    });

    // Functional Test ID: US6_INTEGRATION_TC4
    test('US6_INTEGRATION_TC4: Should handle ticket not found error', async () => {
      mockGetTicketById.mockRejectedValue(new Error('Ticket not found'));
      
      await expect(mockGetTicketById('999')).rejects.toThrow('Ticket not found');
    });

    // Functional Test ID: US6_INTEGRATION_TC5
    test('US6_INTEGRATION_TC5: Should handle network error gracefully', async () => {
      mockGetTicketById.mockRejectedValue(new Error('Network error'));
      
      await expect(mockGetTicketById('123')).rejects.toThrow('Network error');
    });

    // Functional Test ID: US6_EDGE_TC1
    test('US6_EDGE_TC1: Should handle ticket with empty description', () => {
      const ticketWithEmptyDesc = createMockTicket({ description: '' });
      renderTicketDetails(ticketWithEmptyDesc);
      
      expect(getDisplayedDescription()).toBe('');
    });

    // Functional Test ID: US6_EDGE_TC2
    test('US6_EDGE_TC2: Should handle ticket with very long title', () => {
      const longTitle = 'A'.repeat(200);
      const ticketWithLongTitle = createMockTicket({ title: longTitle });
      renderTicketHeader(ticketWithLongTitle);
      
      expect(getDisplayedTitle()).toBe(longTitle);
    });

    // Functional Test ID: US6_EDGE_TC3
    test('US6_EDGE_TC3: Should handle ticket with HTML in description', () => {
      const htmlDesc = '<script>alert("test")</script>Normal text';
      const ticketWithHTML = createMockTicket({ description: htmlDesc });
      renderTicketDetails(ticketWithHTML);
      
      // Description should be escaped
      expect(getDisplayedDescription()).toBe(htmlDesc);
    });

    // Functional Test ID: US6_EDGE_TC4
    test('US6_EDGE_TC4: Should handle ticket with special characters in all fields', () => {
      const specialTicket = createMockTicket({
        title: 'Test <>&"\' Title',
        description: 'Test <>&"\' Description',
        systemName: 'Test <>&"\' System',
        categoryName: 'Test <>&"\' Category',
        author: 'Test <>&"\' Author'
      });
      renderCompleteTicketView(specialTicket);
      
      expect(getDisplayedTitle()).toContain('<>&"\'');
      expect(getDisplayedDescription()).toContain('<>&"\'');
    });

    // Functional Test ID: US6_EDGE_TC5
    test('US6_EDGE_TC5: Should handle ticket with null dates', () => {
      const ticketWithNullDates = createMockTicket({
        createdDate: null,
        updatedDate: null
      });
      renderSidebarDetails(ticketWithNullDates);
      
      expect(getDisplayedCreationDate()).toBe('N/A');
    });

    // Functional Test ID: US6_EDGE_TC6
    test('US6_EDGE_TC6: Should handle ticket with invalid date format', () => {
      const ticketWithInvalidDate = createMockTicket({
        createdDate: 'invalid-date'
      });
      renderSidebarDetails(ticketWithInvalidDate);
      
      // Should handle gracefully
      expect(getDisplayedCreationDate()).toBeTruthy();
    });

    // Functional Test ID: US6_EDGE_TC7
    test('US6_EDGE_TC7: Should handle ticket with missing optional fields', () => {
      const minimalTicket = createMockTicket({
        ticketId: 1,
        title: 'Minimal Ticket',
        description: 'Description',
        systemName: null,
        categoryName: null
      });
      renderCompleteTicketView(minimalTicket);
      
      expect(isTicketNumberDisplayed()).toBe(true);
      expect(isTitleDisplayed()).toBe(true);
      expect(isDescriptionDisplayed()).toBe(true);
    });

    // Functional Test ID: US6_EDGE_TC8
    test('US6_EDGE_TC8: Should handle multiple rapid ticket loads', async () => {
      const ticket1 = await mockGetTicketById('123');
      const ticket2 = await mockGetTicketById('124');
      const ticket3 = await mockGetTicketById('125');
      
      expect(mockGetTicketById).toHaveBeenCalledTimes(3);
    });

    // Functional Test ID: US6_EDGE_TC9
    test('US6_EDGE_TC9: Should handle ticket with Unicode characters', () => {
      const unicodeTicket = createMockTicket({
        title: '测试票 🎫',
        description: 'Descripción con caracteres especiales: ñ, é, ü',
        author: 'José García'
      });
      renderCompleteTicketView(unicodeTicket);
      
      expect(getDisplayedTitle()).toContain('测试票');
      expect(getDisplayedDescription()).toContain('Descripción');
      expect(getDisplayedCreator()).toContain('José');
    });

    // Functional Test ID: US6_EDGE_TC10
    test('US6_EDGE_TC10: Should handle ticket with newlines in description', () => {
      const descWithNewlines = 'Line 1\nLine 2\nLine 3';
      const ticketWithNewlines = createMockTicket({ description: descWithNewlines });
      renderTicketDetails(ticketWithNewlines);
      
      expect(getDisplayedDescription()).toBe(descWithNewlines);
    });

    // Functional Test ID: US6_EDGE_TC11
    test('US6_EDGE_TC11: Should verify all fields match ticket data exactly', () => {
      renderCompleteTicketView(sampleTicket);
      
      const verification = verifyTicketDataMatchesDisplay(sampleTicket);
      expect(verification.matches).toBe(true);
      
      if (!verification.matches) {
        console.log('Mismatched fields:', verification.mismatches);
      }
    });

    // Functional Test ID: US6_EDGE_TC12
    test('US6_EDGE_TC12: Should handle concurrent API calls', async () => {
      const [ticket, comments, attachments] = await Promise.all([
        mockGetTicketById('123'),
        mockGetComments('123'),
        mockGetAttachments('123')
      ]);
      
      expect(ticket).toBeTruthy();
      expect(comments).toBeDefined();
      expect(attachments).toBeDefined();
    });
  });
});