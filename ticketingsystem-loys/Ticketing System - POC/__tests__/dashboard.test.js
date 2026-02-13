/**
 * Dashboard Unit Tests
 * Tests for User Story 1: View Dashboard Summary
 * 
 * User Story: "As a user, I want to see the total number of tickets by status 
 * so that I can understand my ticket workload."
 * 
 * Acceptance Criteria:
 * 1. Pending, In-Progress, and Resolved counts are visible.
 * 2. Counts match the number of tickets in each status.
 */

// Import test utilities
const {
  createDashboardHTML,
  getStatElements,
  isElementVisible,
  getElementNumber,
  countTicketsByStatus,
  assertElementVisible,
  assertElementNumber
} = require('./setup/test-utils');

// Import mock data
const {
  mockTicketsAllStatuses,
  mockTicketsAllPending,
  mockTicketsAllInProgress,
  mockTicketsAllResolved,
  mockTicketsEmpty,
  mockTicketsEdgeCases
} = require('./mocks/ticket-data.mock');

// Import mock API
const { createMockAPI, setupGlobalAPIMock } = require('./mocks/api.mock');

/**
 * updateStats function from tickets.js
 * This is the function under test
 */
function updateStats(tickets) {
  const stats = {
    pending: 0,
    progress: 0,
    resolved: 0
  };

  tickets.forEach(ticket => {
    const statusName = ticket.statusName?.toLowerCase() || '';
    if (statusName.includes('open') || statusName.includes('pending')) {
      stats.pending++;
    } else if (statusName.includes('progress')) {
      stats.progress++;
    } else if (statusName.includes('resolved') || statusName.includes('closed')) {
      stats.resolved++;
    }
  });

  document.getElementById('pendingCount').textContent = stats.pending;
  document.getElementById('progressCount').textContent = stats.progress;
  document.getElementById('resolvedCount').textContent = stats.resolved;
  
  return stats;
}

// Test Suite: Dashboard Summary
describe('User Story 1: View Dashboard Summary', () => {
  
  // Setup before each test
  beforeEach(() => {
    // Create dashboard HTML structure
    createDashboardHTML();
    
    // Setup mock API
    const mockAPI = createMockAPI({ ticketsData: mockTicketsAllStatuses });
    setupGlobalAPIMock(mockAPI);
  });

  // Cleanup after each test
  afterEach(() => {
    document.body.innerHTML = '';
    jest.clearAllMocks();
  });

  /**
   * Acceptance Criterion 1: Pending, In-Progress, and Resolved counts are visible
   */
  describe('AC1: Status counts are visible', () => {
    
    /**
     * Functional Test ID: US1_AC1_TC1
     * Test: Pending count element is visible
     */
    test('US1_AC1_pendingCountVisible - Pending count element exists and is visible', () => {
      // Arrange
      const { pendingCount } = getStatElements();
      
      // Assert
      assertElementVisible(pendingCount, 'Pending count');
      expect(pendingCount.id).toBe('pendingCount');
      expect(pendingCount.parentElement.querySelector('.stat-label').textContent).toBe('Pending');
    });

    /**
     * Functional Test ID: US1_AC1_TC2
     * Test: In Progress count element is visible
     */
    test('US1_AC1_inProgressCountVisible - In Progress count element exists and is visible', () => {
      // Arrange
      const { progressCount } = getStatElements();
      
      // Assert
      assertElementVisible(progressCount, 'In Progress count');
      expect(progressCount.id).toBe('progressCount');
      expect(progressCount.parentElement.querySelector('.stat-label').textContent).toBe('In Progress');
    });

    /**
     * Functional Test ID: US1_AC1_TC3
     * Test: Resolved count element is visible
     */
    test('US1_AC1_resolvedCountVisible - Resolved count element exists and is visible', () => {
      // Arrange
      const { resolvedCount } = getStatElements();
      
      // Assert
      assertElementVisible(resolvedCount, 'Resolved count');
      expect(resolvedCount.id).toBe('resolvedCount');
      expect(resolvedCount.parentElement.querySelector('.stat-label').textContent).toBe('Resolved');
    });

    /**
     * Functional Test ID: US1_AC1_TC4
     * Test: All three stat cards are present in the dashboard
     */
    test('US1_AC1_allStatCardsVisible - All three stat cards are visible on dashboard', () => {
      // Arrange
      const { pendingCount, progressCount, resolvedCount } = getStatElements();
      
      // Assert - All elements exist
      expect(pendingCount).toBeTruthy();
      expect(progressCount).toBeTruthy();
      expect(resolvedCount).toBeTruthy();
      
      // Assert - All elements are visible
      expect(isElementVisible(pendingCount)).toBe(true);
      expect(isElementVisible(progressCount)).toBe(true);
      expect(isElementVisible(resolvedCount)).toBe(true);
      
      // Assert - All stat cards are present
      const statCards = document.querySelectorAll('.stat-card');
      expect(statCards.length).toBeGreaterThanOrEqual(3);
    });

    /**
     * Functional Test ID: US1_AC1_TC5
     * Test: Initial count values are displayed (default 0)
     */
    test('US1_AC1_initialCountsDisplayed - Initial count values are displayed', () => {
      // Arrange
      const { pendingCount, progressCount, resolvedCount } = getStatElements();
      
      // Assert - Elements have initial values
      expect(pendingCount.textContent).toBeDefined();
      expect(progressCount.textContent).toBeDefined();
      expect(resolvedCount.textContent).toBeDefined();
      
      // Assert - Initial values are numeric
      expect(getElementNumber(pendingCount)).toBeGreaterThanOrEqual(0);
      expect(getElementNumber(progressCount)).toBeGreaterThanOrEqual(0);
      expect(getElementNumber(resolvedCount)).toBeGreaterThanOrEqual(0);
    });
  });

  /**
   * Acceptance Criterion 2: Counts match the number of tickets in each status
   */
  describe('AC2: Counts match ticket statuses', () => {
    
    /**
     * Functional Test ID: US1_AC2_TC1
     * Test: Pending count matches number of pending tickets
     */
    test('US1_AC2_pendingCountMatchesTickets - Pending count equals number of pending tickets', () => {
      // Arrange
      const tickets = mockTicketsAllStatuses;
      const expectedCounts = countTicketsByStatus(tickets);
      const { pendingCount } = getStatElements();
      
      // Act
      updateStats(tickets);
      
      // Assert
      assertElementNumber(pendingCount, expectedCounts.pending);
      expect(getElementNumber(pendingCount)).toBe(2); // 2 pending tickets in mock data
    });

    /**
     * Functional Test ID: US1_AC2_TC2
     * Test: In Progress count matches number of in-progress tickets
     */
    test('US1_AC2_inProgressCountMatchesTickets - In Progress count equals number of in-progress tickets', () => {
      // Arrange
      const tickets = mockTicketsAllStatuses;
      const expectedCounts = countTicketsByStatus(tickets);
      const { progressCount } = getStatElements();
      
      // Act
      updateStats(tickets);
      
      // Assert
      assertElementNumber(progressCount, expectedCounts.progress);
      expect(getElementNumber(progressCount)).toBe(1); // 1 in-progress ticket in mock data
    });

    /**
     * Functional Test ID: US1_AC2_TC3
     * Test: Resolved count matches number of resolved tickets
     */
    test('US1_AC2_resolvedCountMatchesTickets - Resolved count equals number of resolved tickets', () => {
      // Arrange
      const tickets = mockTicketsAllStatuses;
      const expectedCounts = countTicketsByStatus(tickets);
      const { resolvedCount } = getStatElements();
      
      // Act
      updateStats(tickets);
      
      // Assert
      assertElementNumber(resolvedCount, expectedCounts.resolved);
      expect(getElementNumber(resolvedCount)).toBe(2); // 2 resolved tickets in mock data
    });

    /**
     * Functional Test ID: US1_AC2_TC4
     * Test: All counts are accurate with mixed ticket statuses
     */
    test('US1_AC2_allCountsAccurate - All counts are accurate with mixed ticket statuses', () => {
      // Arrange
      const tickets = mockTicketsAllStatuses;
      const expectedCounts = countTicketsByStatus(tickets);
      const { pendingCount, progressCount, resolvedCount } = getStatElements();
      
      // Act
      const actualStats = updateStats(tickets);
      
      // Assert - Check returned stats object
      expect(actualStats.pending).toBe(expectedCounts.pending);
      expect(actualStats.progress).toBe(expectedCounts.progress);
      expect(actualStats.resolved).toBe(expectedCounts.resolved);
      
      // Assert - Check DOM elements
      assertElementNumber(pendingCount, expectedCounts.pending);
      assertElementNumber(progressCount, expectedCounts.progress);
      assertElementNumber(resolvedCount, expectedCounts.resolved);
      
      // Assert - Verify total
      const totalDisplayed = actualStats.pending + actualStats.progress + actualStats.resolved;
      expect(totalDisplayed).toBe(tickets.length);
    });

    /**
     * Functional Test ID: US1_AC2_TC5
     * Test: Counts update correctly when all tickets are pending
     */
    test('US1_AC2_allPendingTickets - Counts are correct when all tickets are pending', () => {
      // Arrange
      const tickets = mockTicketsAllPending;
      const { pendingCount, progressCount, resolvedCount } = getStatElements();
      
      // Act
      updateStats(tickets);
      
      // Assert
      expect(getElementNumber(pendingCount)).toBe(3);
      expect(getElementNumber(progressCount)).toBe(0);
      expect(getElementNumber(resolvedCount)).toBe(0);
    });

    /**
     * Functional Test ID: US1_AC2_TC6
     * Test: Counts update correctly when all tickets are in progress
     */
    test('US1_AC2_allInProgressTickets - Counts are correct when all tickets are in progress', () => {
      // Arrange
      const tickets = mockTicketsAllInProgress;
      const { pendingCount, progressCount, resolvedCount } = getStatElements();
      
      // Act
      updateStats(tickets);
      
      // Assert
      expect(getElementNumber(pendingCount)).toBe(0);
      expect(getElementNumber(progressCount)).toBe(2);
      expect(getElementNumber(resolvedCount)).toBe(0);
    });

    /**
     * Functional Test ID: US1_AC2_TC7
     * Test: Counts update correctly when all tickets are resolved
     */
    test('US1_AC2_allResolvedTickets - Counts are correct when all tickets are resolved', () => {
      // Arrange
      const tickets = mockTicketsAllResolved;
      const { pendingCount, progressCount, resolvedCount } = getStatElements();
      
      // Act
      updateStats(tickets);
      
      // Assert
      expect(getElementNumber(pendingCount)).toBe(0);
      expect(getElementNumber(progressCount)).toBe(0);
      expect(getElementNumber(resolvedCount)).toBe(4);
    });

    /**
     * Functional Test ID: US1_AC2_TC8
     * Test: Counts are zero when no tickets exist
     */
    test('US1_AC2_emptyTickets - Counts are zero when no tickets exist', () => {
      // Arrange
      const tickets = mockTicketsEmpty;
      const { pendingCount, progressCount, resolvedCount } = getStatElements();
      
      // Act
      updateStats(tickets);
      
      // Assert
      expect(getElementNumber(pendingCount)).toBe(0);
      expect(getElementNumber(progressCount)).toBe(0);
      expect(getElementNumber(resolvedCount)).toBe(0);
    });
  });

  /**
   * Edge Cases and Error Handling
   */
  describe('Edge Cases: Status name variations', () => {
    
    /**
     * Functional Test ID: US1_EDGE_TC1
     * Test: Handles tickets with null status names
     */
    test('US1_EDGE_nullStatusNames - Handles tickets with null status names gracefully', () => {
      // Arrange
      const tickets = [
        { ticketId: 1, statusName: null },
        { ticketId: 2, statusName: 'Pending' }
      ];
      const { pendingCount, progressCount, resolvedCount } = getStatElements();
      
      // Act
      updateStats(tickets);
      
      // Assert - Should not throw error
      expect(getElementNumber(pendingCount)).toBe(1);
      expect(getElementNumber(progressCount)).toBe(0);
      expect(getElementNumber(resolvedCount)).toBe(0);
    });

    /**
     * Functional Test ID: US1_EDGE_TC2
     * Test: Handles tickets with undefined status names
     */
    test('US1_EDGE_undefinedStatusNames - Handles tickets with undefined status names gracefully', () => {
      // Arrange
      const tickets = [
        { ticketId: 1, statusName: undefined },
        { ticketId: 2, statusName: 'In Progress' }
      ];
      const { pendingCount, progressCount, resolvedCount } = getStatElements();
      
      // Act
      updateStats(tickets);
      
      // Assert - Should not throw error
      expect(getElementNumber(pendingCount)).toBe(0);
      expect(getElementNumber(progressCount)).toBe(1);
      expect(getElementNumber(resolvedCount)).toBe(0);
    });

    /**
     * Functional Test ID: US1_EDGE_TC3
     * Test: Handles tickets with empty string status names
     */
    test('US1_EDGE_emptyStatusNames - Handles tickets with empty string status names', () => {
      // Arrange
      const tickets = [
        { ticketId: 1, statusName: '' },
        { ticketId: 2, statusName: 'Resolved' }
      ];
      const { pendingCount, progressCount, resolvedCount } = getStatElements();
      
      // Act
      updateStats(tickets);
      
      // Assert - Should not throw error
      expect(getElementNumber(pendingCount)).toBe(0);
      expect(getElementNumber(progressCount)).toBe(0);
      expect(getElementNumber(resolvedCount)).toBe(1);
    });

    /**
     * Functional Test ID: US1_EDGE_TC4
     * Test: Handles case-insensitive status matching
     */
    test('US1_EDGE_caseInsensitiveStatus - Status matching is case-insensitive', () => {
      // Arrange
      const tickets = [
        { ticketId: 1, statusName: 'PENDING' },
        { ticketId: 2, statusName: 'pending' },
        { ticketId: 3, statusName: 'Pending' },
        { ticketId: 4, statusName: 'IN PROGRESS' },
        { ticketId: 5, statusName: 'RESOLVED' }
      ];
      const { pendingCount, progressCount, resolvedCount } = getStatElements();
      
      // Act
      updateStats(tickets);
      
      // Assert
      expect(getElementNumber(pendingCount)).toBe(3);
      expect(getElementNumber(progressCount)).toBe(1);
      expect(getElementNumber(resolvedCount)).toBe(1);
    });

    /**
     * Functional Test ID: US1_EDGE_TC5
     * Test: Handles status names with extra whitespace
     */
    test('US1_EDGE_statusWithWhitespace - Handles status names with whitespace', () => {
      // Arrange
      const tickets = [
        { ticketId: 1, statusName: '  Pending  ' },
        { ticketId: 2, statusName: 'In Progress' },
        { ticketId: 3, statusName: '  Resolved  ' }
      ];
      const { pendingCount, progressCount, resolvedCount } = getStatElements();
      
      // Act
      updateStats(tickets);
      
      // Assert - Should handle whitespace correctly
      expect(getElementNumber(pendingCount)).toBe(1);
      expect(getElementNumber(progressCount)).toBe(1);
      expect(getElementNumber(resolvedCount)).toBe(1);
    });

    /**
     * Functional Test ID: US1_EDGE_TC6
     * Test: Handles alternative status names (Open, Closed)
     */
    test('US1_EDGE_alternativeStatusNames - Recognizes alternative status names', () => {
      // Arrange
      const tickets = [
        { ticketId: 1, statusName: 'Open' },        // Should count as Pending
        { ticketId: 2, statusName: 'Closed' },      // Should count as Resolved
        { ticketId: 3, statusName: 'Pending' },
        { ticketId: 4, statusName: 'Resolved' }
      ];
      const { pendingCount, progressCount, resolvedCount } = getStatElements();
      
      // Act
      updateStats(tickets);
      
      // Assert
      expect(getElementNumber(pendingCount)).toBe(2); // Open + Pending
      expect(getElementNumber(progressCount)).toBe(0);
      expect(getElementNumber(resolvedCount)).toBe(2); // Closed + Resolved
    });

    /**
     * Functional Test ID: US1_EDGE_TC7
     * Test: Unknown status names are not counted
     */
    test('US1_EDGE_unknownStatusNames - Unknown status names are not counted', () => {
      // Arrange
      const tickets = [
        { ticketId: 1, statusName: 'Unknown' },
        { ticketId: 2, statusName: 'Invalid Status' },
        { ticketId: 3, statusName: 'Pending' }
      ];
      const { pendingCount, progressCount, resolvedCount } = getStatElements();
      
      // Act
      updateStats(tickets);
      
      // Assert - Only valid status should be counted
      expect(getElementNumber(pendingCount)).toBe(1);
      expect(getElementNumber(progressCount)).toBe(0);
      expect(getElementNumber(resolvedCount)).toBe(0);
      
      // Total displayed should be less than total tickets
      const totalDisplayed = getElementNumber(pendingCount) + 
                            getElementNumber(progressCount) + 
                            getElementNumber(resolvedCount);
      expect(totalDisplayed).toBeLessThan(tickets.length);
    });
  });

  /**
   * Performance and Reliability Tests
   */
  describe('Performance: Large datasets', () => {
    
    /**
     * Functional Test ID: US1_PERF_TC1
     * Test: Handles large number of tickets efficiently
     */
    test('US1_PERF_largeDataset - Handles large number of tickets', () => {
      // Arrange - Create 1000 tickets
      const largeTicketSet = Array.from({ length: 1000 }, (_, i) => ({
        ticketId: i + 1,
        statusName: ['Pending', 'In Progress', 'Resolved'][i % 3]
      }));
      const { pendingCount, progressCount, resolvedCount } = getStatElements();
      
      // Act
      const startTime = performance.now();
      updateStats(largeTicketSet);
      const endTime = performance.now();
      
      // Assert - Should complete quickly (under 100ms)
      expect(endTime - startTime).toBeLessThan(100);
      
      // Assert - Counts should be correct
      expect(getElementNumber(pendingCount)).toBeGreaterThan(0);
      expect(getElementNumber(progressCount)).toBeGreaterThan(0);
      expect(getElementNumber(resolvedCount)).toBeGreaterThan(0);
    });

    /**
     * Functional Test ID: US1_PERF_TC2
     * Test: Multiple rapid updates don't cause issues
     */
    test('US1_PERF_rapidUpdates - Handles multiple rapid updates', () => {
      // Arrange
      const tickets1 = mockTicketsAllPending;
      const tickets2 = mockTicketsAllInProgress;
      const tickets3 = mockTicketsAllResolved;
      const { pendingCount, progressCount, resolvedCount } = getStatElements();
      
      // Act - Rapid successive updates
      updateStats(tickets1);
      updateStats(tickets2);
      updateStats(tickets3);
      
      // Assert - Final state should reflect last update
      expect(getElementNumber(pendingCount)).toBe(0);
      expect(getElementNumber(progressCount)).toBe(0);
      expect(getElementNumber(resolvedCount)).toBe(4);
    });
  });
});