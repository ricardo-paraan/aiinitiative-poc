/**
 * Ticket List Unit Tests
 * Tests for User Story 2: View Ticket List
 * 
 * User Story: "As a user, I want to see all the tickets I created 
 * so that I can easily track my submissions."
 * 
 * Acceptance Criteria:
 * 1. Ticket list displays Ticket ID, Title, Status, System, Category, 
 *    Date Submitted, Last Updated.
 * 2. Only tickets created by the logged-in user are visible.
 */

// Import test utilities
const {
  createTicketListHTML,
  getTicketTableBody,
  getTicketRows,
  getTicketDataFromRow,
  getAllTicketsFromTable,
  isTableLoading,
  isTableEmpty,
  countVisibleTickets,
  hasTableColumn,
  getTableColumns,
  findTicketRowById,
  ticketExistsInTable,
  verifyRequiredColumns,
  filterTicketsByAuthor,
  formatDateForComparison,
  verifyTicketData,
  assertTicketListDisplays
} = require('./setup/ticket-list-utils');

// Import mock data
const {
  mockTicketsForList,
  mockTicketsWithMissingFields,
  mockTicketsMixedAuthors,
  mockTicketsEmpty
} = require('./mocks/ticket-data.mock');

// Import mock API
const { createMockAPI, setupGlobalAPIMock } = require('./mocks/api.mock');

/**
 * Utility functions from utils.js
 */
function formatDate(dateString) {
  if (!dateString) return 'N/A';
  const date = new Date(dateString);
  const options = { year: 'numeric', month: 'short', day: 'numeric' };
  return date.toLocaleDateString('en-US', options);
}

function escapeHtml(text) {
  const map = {
    '&': '&',
    '<': '<',
    '>': '>',
    '"': '"',
    "'": '&#039;'
  };
  return text.replace(/[&<>"']/g, m => map[m]);
}

function getStatusClass(statusName) {
  if (!statusName) return 'status-pending';
  const status = statusName.toLowerCase().replace(/\s+/g, '-');
  
  if (status.includes('open') || status.includes('pending')) {
    return 'status-pending';
  } else if (status.includes('progress')) {
    return 'status-progress';
  } else if (status.includes('resolved')) {
    return 'status-resolved';
  } else if (status.includes('closed')) {
    return 'status-closed';
  }
  return 'status-pending';
}

/**
 * displayTickets function from tickets.js
 * This is the function under test
 */
function displayTickets(tickets) {
  const tbody = getTicketTableBody();
  
  if (!tickets || tickets.length === 0) {
    tbody.innerHTML = `
      <tr>
        <td colspan="7" class="empty-state">
          <div class="empty-state-icon">
            <svg width="80" height="80" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M9 11l3 3L22 4"></path>
              <path d="M21 12v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11"></path>
            </svg>
          </div>
          <h3>No tickets found</h3>
          <p>Try adjusting your search or filters</p>
        </td>
      </tr>
    `;
    return;
  }

  // Generate table rows
  tbody.innerHTML = tickets.map(ticket => `
    <tr onclick="viewTicket(${ticket.ticketId})">
      <td class="ticket-id">#${ticket.ticketId}</td>
      <td class="ticket-title">${escapeHtml(ticket.title)}</td>
      <td>
        <span class="status-badge ${getStatusClass(ticket.statusName)}">
          ${ticket.statusName || 'Unknown'}
        </span>
      </td>
      <td>${ticket.systemName || 'N/A'}</td>
      <td>${ticket.categoryName || 'N/A'}</td>
      <td class="ticket-date">${formatDate(ticket.createdDate)}</td>
      <td class="ticket-date">${formatDate(ticket.updatedDate)}</td>
    </tr>
  `).join('');
}

// Test Suite: Ticket List
describe('User Story 2: View Ticket List', () => {
  
  // Setup before each test
  beforeEach(() => {
    // Create ticket list HTML structure
    createTicketListHTML();
    
    // Setup mock API
    const mockAPI = createMockAPI({ ticketsData: mockTicketsForList });
    setupGlobalAPIMock(mockAPI);
  });

  // Cleanup after each test
  afterEach(() => {
    document.body.innerHTML = '';
    jest.clearAllMocks();
  });

  /**
   * Acceptance Criterion 1: Ticket list displays all required columns
   */
  describe('AC1: Ticket list displays required columns', () => {
    
    /**
     * Functional Test ID: US2_AC1_TC1
     * Test: Table has Ticket ID column
     */
    test('US2_AC1_ticketIdColumnExists - Ticket ID column is present in table header', () => {
      // Assert
      expect(hasTableColumn('Ticket ID')).toBe(true);
      
      const columns = getTableColumns();
      expect(columns).toContain('Ticket ID');
    });

    /**
     * Functional Test ID: US2_AC1_TC2
     * Test: Table has Title column
     */
    test('US2_AC1_titleColumnExists - Title column is present in table header', () => {
      // Assert
      expect(hasTableColumn('Title')).toBe(true);
      
      const columns = getTableColumns();
      expect(columns).toContain('Title');
    });

    /**
     * Functional Test ID: US2_AC1_TC3
     * Test: Table has Status column
     */
    test('US2_AC1_statusColumnExists - Status column is present in table header', () => {
      // Assert
      expect(hasTableColumn('Status')).toBe(true);
      
      const columns = getTableColumns();
      expect(columns).toContain('Status');
    });

    /**
     * Functional Test ID: US2_AC1_TC4
     * Test: Table has System column
     */
    test('US2_AC1_systemColumnExists - System column is present in table header', () => {
      // Assert
      expect(hasTableColumn('System')).toBe(true);
      
      const columns = getTableColumns();
      expect(columns).toContain('System');
    });

    /**
     * Functional Test ID: US2_AC1_TC5
     * Test: Table has Category column
     */
    test('US2_AC1_categoryColumnExists - Category column is present in table header', () => {
      // Assert
      expect(hasTableColumn('Category')).toBe(true);
      
      const columns = getTableColumns();
      expect(columns).toContain('Category');
    });

    /**
     * Functional Test ID: US2_AC1_TC6
     * Test: Table has Date Submitted column
     */
    test('US2_AC1_dateSubmittedColumnExists - Date Submitted column is present in table header', () => {
      // Assert
      expect(hasTableColumn('Date Submitted')).toBe(true);
      
      const columns = getTableColumns();
      expect(columns).toContain('Date Submitted');
    });

    /**
     * Functional Test ID: US2_AC1_TC7
     * Test: Table has Last Updated column
     */
    test('US2_AC1_lastUpdatedColumnExists - Last Updated column is present in table header', () => {
      // Assert
      expect(hasTableColumn('Last Updated')).toBe(true);
      
      const columns = getTableColumns();
      expect(columns).toContain('Last Updated');
    });

    /**
     * Functional Test ID: US2_AC1_TC8
     * Test: All required columns are present
     */
    test('US2_AC1_allRequiredColumnsPresent - All 7 required columns are present', () => {
      // Arrange
      const requiredColumns = [
        'Ticket ID',
        'Title',
        'Status',
        'System',
        'Category',
        'Date Submitted',
        'Last Updated'
      ];
      
      // Act
      const actualColumns = getTableColumns();
      const columnStatus = verifyRequiredColumns();
      
      // Assert - All required columns exist
      requiredColumns.forEach(column => {
        expect(actualColumns).toContain(column);
      });
      
      // Assert - Column status verification
      expect(columnStatus.ticketId).toBe(true);
      expect(columnStatus.title).toBe(true);
      expect(columnStatus.status).toBe(true);
      expect(columnStatus.system).toBe(true);
      expect(columnStatus.category).toBe(true);
      expect(columnStatus.dateSubmitted).toBe(true);
      expect(columnStatus.lastUpdated).toBe(true);
      
      // Assert - Exactly 7 columns
      expect(actualColumns.length).toBe(7);
    });

    /**
     * Functional Test ID: US2_AC1_TC9
     * Test: Ticket data displays correctly in each column
     */
    test('US2_AC1_ticketDataDisplaysCorrectly - Ticket data displays in correct columns', () => {
      // Arrange
      const testTickets = [mockTicketsForList[0]]; // Use first ticket
      
      // Act
      displayTickets(testTickets);
      
      // Assert
      const rows = getTicketRows();
      expect(rows.length).toBe(1);
      
      const ticketData = getTicketDataFromRow(rows[0]);
      expect(ticketData).toBeTruthy();
      
      // Verify each column
      expect(ticketData.ticketId).toBe(`#${testTickets[0].ticketId}`);
      expect(ticketData.title).toBe(testTickets[0].title);
      expect(ticketData.status).toContain(testTickets[0].statusName);
      expect(ticketData.system).toBe(testTickets[0].systemName);
      expect(ticketData.category).toBe(testTickets[0].categoryName);
      expect(ticketData.dateSubmitted).toBe(formatDate(testTickets[0].createdDate));
      expect(ticketData.lastUpdated).toBe(formatDate(testTickets[0].updatedDate));
    });

    /**
     * Functional Test ID: US2_AC1_TC10
     * Test: Multiple tickets display correctly
     */
    test('US2_AC1_multipleTicketsDisplay - Multiple tickets display with all columns', () => {
      // Arrange
      const testTickets = mockTicketsForList.slice(0, 3); // Use first 3 tickets
      
      // Act
      displayTickets(testTickets);
      
      // Assert
      const rows = getTicketRows();
      expect(rows.length).toBe(3);
      
      // Verify each ticket
      testTickets.forEach((expectedTicket, index) => {
        const actualTicket = getTicketDataFromRow(rows[index]);
        expect(actualTicket).toBeTruthy();
        
        expect(actualTicket.ticketId).toBe(`#${expectedTicket.ticketId}`);
        expect(actualTicket.title).toBe(expectedTicket.title);
        expect(actualTicket.system).toBe(expectedTicket.systemName);
        expect(actualTicket.category).toBe(expectedTicket.categoryName);
      });
    });

    /**
     * Functional Test ID: US2_AC1_TC11
     * Test: Ticket ID displays with # prefix
     */
    test('US2_AC1_ticketIdHasPrefix - Ticket ID displays with # prefix', () => {
      // Arrange
      const testTickets = [mockTicketsForList[0]];
      
      // Act
      displayTickets(testTickets);
      
      // Assert
      const rows = getTicketRows();
      const ticketData = getTicketDataFromRow(rows[0]);
      
      expect(ticketData.ticketId).toMatch(/^#\d+$/);
      expect(ticketData.ticketId).toBe(`#${testTickets[0].ticketId}`);
    });

    /**
     * Functional Test ID: US2_AC1_TC12
     * Test: Status displays with badge styling
     */
    test('US2_AC1_statusHasBadge - Status displays with badge styling', () => {
      // Arrange
      const testTickets = [mockTicketsForList[0]];
      
      // Act
      displayTickets(testTickets);
      
      // Assert
      const rows = getTicketRows();
      const statusCell = rows[0].querySelectorAll('td')[2];
      const statusBadge = statusCell.querySelector('.status-badge');
      
      expect(statusBadge).toBeTruthy();
      expect(statusBadge.textContent.trim()).toBe(testTickets[0].statusName);
    });

    /**
     * Functional Test ID: US2_AC1_TC13
     * Test: Dates are formatted correctly
     */
    test('US2_AC1_datesFormatted - Dates are formatted in readable format', () => {
      // Arrange
      const testTickets = [mockTicketsForList[0]];
      
      // Act
      displayTickets(testTickets);
      
      // Assert
      const rows = getTicketRows();
      const ticketData = getTicketDataFromRow(rows[0]);
      
      // Dates should be formatted (e.g., "Feb 13, 2026")
      expect(ticketData.dateSubmitted).not.toContain('T');
      expect(ticketData.dateSubmitted).not.toContain('Z');
      expect(ticketData.lastUpdated).not.toContain('T');
      expect(ticketData.lastUpdated).not.toContain('Z');
      
      // Should match expected format
      expect(ticketData.dateSubmitted).toBe(formatDate(testTickets[0].createdDate));
      expect(ticketData.lastUpdated).toBe(formatDate(testTickets[0].updatedDate));
    });
  });

  /**
   * Acceptance Criterion 2: Only user's tickets are visible
   */
  describe('AC2: Only current user tickets are visible', () => {
    
    /**
     * Functional Test ID: US2_AC2_TC1
     * Test: Only current user's tickets are displayed
     */
    test('US2_AC2_onlyUserTicketsDisplayed - Only tickets created by current user are shown', () => {
      // Arrange
      const currentUser = 'currentUser';
      const userTickets = filterTicketsByAuthor(mockTicketsMixedAuthors, currentUser);
      
      // Act
      displayTickets(userTickets);
      
      // Assert
      const displayedTickets = getAllTicketsFromTable();
      expect(displayedTickets.length).toBe(2); // Only 2 tickets belong to currentUser
      
      // Verify only user's tickets are shown
      const displayedIds = displayedTickets.map(t => t.ticketId);
      expect(displayedIds).toContain('#1');
      expect(displayedIds).toContain('#3');
      expect(displayedIds).not.toContain('#2'); // otherUser's ticket
      expect(displayedIds).not.toContain('#4'); // anotherUser's ticket
    });

    /**
     * Functional Test ID: US2_AC2_TC2
     * Test: Other users' tickets are not displayed
     */
    test('US2_AC2_otherUsersTicketsHidden - Other users tickets are not visible', () => {
      // Arrange
      const currentUser = 'currentUser';
      const allTickets = mockTicketsMixedAuthors;
      const userTickets = filterTicketsByAuthor(allTickets, currentUser);
      const otherTickets = allTickets.filter(t => t.author !== currentUser);
      
      // Act
      displayTickets(userTickets);
      
      // Assert
      const displayedTickets = getAllTicketsFromTable();
      
      // Verify other users' tickets are not shown
      otherTickets.forEach(otherTicket => {
        expect(ticketExistsInTable(otherTicket.ticketId)).toBe(false);
      });
      
      // Verify only user's tickets count
      expect(displayedTickets.length).toBe(userTickets.length);
    });

    /**
     * Functional Test ID: US2_AC2_TC3
     * Test: All user's tickets are displayed
     */
    test('US2_AC2_allUserTicketsShown - All tickets created by user are displayed', () => {
      // Arrange
      const userTickets = mockTicketsForList; // All tickets are from currentUser
      
      // Act
      displayTickets(userTickets);
      
      // Assert
      const displayedTickets = getAllTicketsFromTable();
      expect(displayedTickets.length).toBe(userTickets.length);
      
      // Verify all user tickets are present
      userTickets.forEach(ticket => {
        expect(ticketExistsInTable(ticket.ticketId)).toBe(true);
      });
    });

    /**
     * Functional Test ID: US2_AC2_TC4
     * Test: Ticket count matches user's tickets
     */
    test('US2_AC2_ticketCountMatches - Displayed ticket count matches user ticket count', () => {
      // Arrange
      const userTickets = mockTicketsForList;
      
      // Act
      displayTickets(userTickets);
      
      // Assert
      const visibleCount = countVisibleTickets();
      expect(visibleCount).toBe(userTickets.length);
      expect(visibleCount).toBe(5); // mockTicketsForList has 5 tickets
    });

    /**
     * Functional Test ID: US2_AC2_TC5
     * Test: Empty list when user has no tickets
     */
    test('US2_AC2_emptyListForNoTickets - Empty state shown when user has no tickets', () => {
      // Arrange
      const userTickets = [];
      
      // Act
      displayTickets(userTickets);
      
      // Assert
      expect(isTableEmpty()).toBe(true);
      expect(countVisibleTickets()).toBe(0);
      
      const tbody = getTicketTableBody();
      expect(tbody.textContent).toContain('No tickets found');
    });
  });

  /**
   * Edge Cases and Data Validation
   */
  describe('Edge Cases: Data handling', () => {
    
    /**
     * Functional Test ID: US2_EDGE_TC1
     * Test: Handles tickets with missing status
     */
    test('US2_EDGE_missingStatus - Displays Unknown for missing status', () => {
      // Arrange
      const ticketsWithMissingStatus = [mockTicketsWithMissingFields[0]];
      
      // Act
      displayTickets(ticketsWithMissingStatus);
      
      // Assert
      const rows = getTicketRows();
      const ticketData = getTicketDataFromRow(rows[0]);
      
      expect(ticketData.status).toContain('Unknown');
    });

    /**
     * Functional Test ID: US2_EDGE_TC2
     * Test: Handles tickets with missing system
     */
    test('US2_EDGE_missingSystem - Displays N/A for missing system', () => {
      // Arrange
      const ticketsWithMissingSystem = [mockTicketsWithMissingFields[1]];
      
      // Act
      displayTickets(ticketsWithMissingSystem);
      
      // Assert
      const rows = getTicketRows();
      const ticketData = getTicketDataFromRow(rows[0]);
      
      expect(ticketData.system).toBe('N/A');
    });

    /**
     * Functional Test ID: US2_EDGE_TC3
     * Test: Handles tickets with missing category
     */
    test('US2_EDGE_missingCategory - Displays N/A for missing category', () => {
      // Arrange
      const ticketsWithMissingCategory = [mockTicketsWithMissingFields[2]];
      
      // Act
      displayTickets(ticketsWithMissingCategory);
      
      // Assert
      const rows = getTicketRows();
      const ticketData = getTicketDataFromRow(rows[0]);
      
      expect(ticketData.category).toBe('N/A');
    });

    /**
     * Functional Test ID: US2_EDGE_TC4
     * Test: Handles tickets with missing dates
     */
    test('US2_EDGE_missingDates - Displays N/A for missing dates', () => {
      // Arrange
      const ticketsWithMissingDates = [mockTicketsWithMissingFields[3]];
      
      // Act
      displayTickets(ticketsWithMissingDates);
      
      // Assert
      const rows = getTicketRows();
      const ticketData = getTicketDataFromRow(rows[0]);
      
      expect(ticketData.dateSubmitted).toBe('N/A');
      expect(ticketData.lastUpdated).toBe('N/A');
    });

    /**
     * Functional Test ID: US2_EDGE_TC5
     * Test: Handles special characters in title
     */
    test('US2_EDGE_specialCharactersInTitle - Escapes HTML in ticket title', () => {
      // Arrange
      const ticketWithSpecialChars = [{
        ticketId: 999,
        title: '<script>alert("XSS")</script>',
        statusName: 'Pending',
        systemName: 'Test',
        categoryName: 'Bug',
        author: 'currentUser',
        createdDate: '2026-02-13T10:00:00Z',
        updatedDate: '2026-02-13T10:00:00Z'
      }];
      
      // Act
      displayTickets(ticketWithSpecialChars);
      
      // Assert
      const tbody = getTicketTableBody();
      const html = tbody.innerHTML;
      
      // Should not contain unescaped script tags
      expect(html).not.toContain('<script>');
      expect(html).toContain('<script>');
    });

    /**
     * Functional Test ID: US2_EDGE_TC6
     * Test: Handles empty ticket list
     */
    test('US2_EDGE_emptyTicketList - Shows empty state for empty list', () => {
      // Arrange
      const emptyTickets = [];
      
      // Act
      displayTickets(emptyTickets);
      
      // Assert
      expect(isTableEmpty()).toBe(true);
      expect(countVisibleTickets()).toBe(0);
      
      const tbody = getTicketTableBody();
      expect(tbody.querySelector('.empty-state')).toBeTruthy();
      expect(tbody.textContent).toContain('No tickets found');
    });

    /**
     * Functional Test ID: US2_EDGE_TC7
     * Test: Handles null ticket list
     */
    test('US2_EDGE_nullTicketList - Shows empty state for null list', () => {
      // Arrange
      const nullTickets = null;
      
      // Act
      displayTickets(nullTickets);
      
      // Assert
      expect(isTableEmpty()).toBe(true);
      expect(countVisibleTickets()).toBe(0);
    });

    /**
     * Functional Test ID: US2_EDGE_TC8
     * Test: Handles undefined ticket list
     */
    test('US2_EDGE_undefinedTicketList - Shows empty state for undefined list', () => {
      // Arrange
      const undefinedTickets = undefined;
      
      // Act
      displayTickets(undefinedTickets);
      
      // Assert
      expect(isTableEmpty()).toBe(true);
      expect(countVisibleTickets()).toBe(0);
    });
  });

  /**
   * Performance and Reliability Tests
   */
  describe('Performance: Large datasets', () => {
    
    /**
     * Functional Test ID: US2_PERF_TC1
     * Test: Handles large number of tickets
     */
    test('US2_PERF_largeTicketList - Handles large number of tickets efficiently', () => {
      // Arrange - Create 100 tickets
      const largeTicketList = Array.from({ length: 100 }, (_, i) => ({
        ticketId: i + 1,
        title: `Ticket ${i + 1}`,
        statusName: ['Pending', 'In-Progress', 'Resolved'][i % 3],
        systemName: `System ${(i % 5) + 1}`,
        categoryName: `Category ${(i % 4) + 1}`,
        author: 'currentUser',
        createdDate: '2026-02-13T10:00:00Z',
        updatedDate: '2026-02-13T10:00:00Z'
      }));
      
      // Act
      const startTime = performance.now();
      displayTickets(largeTicketList);
      const endTime = performance.now();
      
      // Assert - Should complete quickly (under 200ms)
      expect(endTime - startTime).toBeLessThan(200);
      
      // Assert - All tickets displayed
      expect(countVisibleTickets()).toBe(100);
    });

    /**
     * Functional Test ID: US2_PERF_TC2
     * Test: Multiple rapid updates
     */
    test('US2_PERF_rapidUpdates - Handles multiple rapid display updates', () => {
      // Arrange
      const tickets1 = mockTicketsForList.slice(0, 2);
      const tickets2 = mockTicketsForList.slice(2, 4);
      const tickets3 = mockTicketsForList.slice(4, 5);
      
      // Act - Rapid successive updates
      displayTickets(tickets1);
      displayTickets(tickets2);
      displayTickets(tickets3);
      
      // Assert - Final state should reflect last update
      expect(countVisibleTickets()).toBe(1);
      
      const displayedTickets = getAllTicketsFromTable();
      expect(displayedTickets[0].ticketId).toBe('#5');
    });
  });

  /**
   * Integration Tests
   */
  describe('Integration: Complete ticket list flow', () => {
    
    /**
     * Functional Test ID: US2_INT_TC1
     * Test: Complete ticket list display flow
     */
    test('US2_INT_completeFlow - Complete flow from empty to populated list', () => {
      // Arrange
      const tbody = getTicketTableBody();
      
      // Assert - Initial loading state
      expect(tbody.querySelector('.loading-cell')).toBeTruthy();
      
      // Act - Display tickets
      displayTickets(mockTicketsForList);
      
      // Assert - Tickets displayed
      expect(countVisibleTickets()).toBe(5);
      expect(tbody.querySelector('.loading-cell')).toBeFalsy();
      
      // Assert - All columns present
      const columns = verifyRequiredColumns();
      expect(Object.values(columns).every(v => v === true)).toBe(true);
      
      // Assert - Data accuracy
      const displayedTickets = getAllTicketsFromTable();
      expect(displayedTickets.length).toBe(mockTicketsForList.length);
    });

    /**
     * Functional Test ID: US2_INT_TC2
     * Test: Ticket list updates correctly
     */
    test('US2_INT_listUpdates - Ticket list updates when data changes', () => {
      // Arrange - Initial display
      const initialTickets = mockTicketsForList.slice(0, 2);
      displayTickets(initialTickets);
      
      // Assert - Initial state
      expect(countVisibleTickets()).toBe(2);
      
      // Act - Update with more tickets
      const updatedTickets = mockTicketsForList;
      displayTickets(updatedTickets);
      
      // Assert - Updated state
      expect(countVisibleTickets()).toBe(5);
      
      // Act - Update with fewer tickets
      const reducedTickets = mockTicketsForList.slice(0, 1);
      displayTickets(reducedTickets);
      
      // Assert - Reduced state
      expect(countVisibleTickets()).toBe(1);
    });
  });
});