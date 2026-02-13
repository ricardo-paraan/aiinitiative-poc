/**
 * Filter and Search Unit Tests
 * Tests for User Story 3: Filter Tickets & User Story 4: Search Tickets
 * 
 * User Story 3: "As a user, I want to filter tickets by Status, System, Category, 
 * and Date Submitted so I can narrow down the list."
 * 
 * Acceptance Criteria:
 * 1. Each filter limits results correctly.
 * 2. Multiple filters can be applied together.
 * 3. Removing a filter restores full ticket list.
 * 
 * User Story 4: "As a user, I want to search across all ticket fields 
 * so I can quickly find a specific ticket."
 * 
 * Acceptance Criteria:
 * 1. Search checks all ticket columns.
 * 2. Partial matches return results.
 * 3. No-match searches show empty results.
 */

// Import test utilities
const {
  createFilterSearchHTML,
  getFilterElements,
  setSearchValue,
  setFilterValue,
  clearAllFilters,
  getCurrentFilterValues,
  filterByStatus,
  filterBySystem,
  filterByCategory,
  applyMultipleFilters,
  searchTickets,
  sortTickets,
  countByStatus,
  countBySystem,
  countByCategory,
  verifyFilterResults,
  verifySearchResults,
  getUniqueValues
} = require('./setup/filter-search-utils');

const {
  getTicketTableBody,
  getTicketRows,
  countVisibleTickets
} = require('./setup/ticket-list-utils');

// Import mock data
const {
  mockTicketsForFilterAndSearch,
  mockTicketsForDateFilter
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
 */
function displayTickets(tickets) {
  const tbody = getTicketTableBody();
  
  if (!tickets || tickets.length === 0) {
    tbody.innerHTML = `
      <tr>
        <td colspan="7" class="empty-state">
          <h3>No tickets found</h3>
          <p>Try adjusting your search or filters</p>
        </td>
      </tr>
    `;
    return;
  }

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

// Test Suite: Filter and Search
describe('User Story 3 & 4: Filter and Search Tickets', () => {
  
  let allTickets;
  
  // Setup before each test
  beforeEach(() => {
    // Create HTML structure
    createFilterSearchHTML();
    
    // Setup mock data
    allTickets = mockTicketsForFilterAndSearch;
    
    // Setup mock API
    const mockAPI = createMockAPI({ ticketsData: allTickets });
    setupGlobalAPIMock(mockAPI);
  });

  // Cleanup after each test
  afterEach(() => {
    document.body.innerHTML = '';
    jest.clearAllMocks();
  });

  /**
   * User Story 3: Filter Tickets
   * Acceptance Criterion 1: Each filter limits results correctly
   */
  describe('US3 AC1: Individual filters work correctly', () => {
    
    /**
     * Functional Test ID: US3_AC1_TC1
     * Test: Filter by Status works correctly
     */
    test('US3_filterStatusCorrectly - Status filter limits results to selected status', () => {
      // Arrange
      const statusId = 1; // Pending
      
      // Act
      const filtered = filterByStatus(allTickets, statusId);
      
      // Assert
      expect(filtered.length).toBeGreaterThan(0);
      expect(filtered.length).toBeLessThan(allTickets.length);
      
      // Verify all filtered tickets have the correct status
      filtered.forEach(ticket => {
        expect(ticket.statusId).toBe(statusId);
        expect(ticket.statusName).toBe('Pending');
      });
      
      // Verify filter results
      expect(verifyFilterResults(filtered, { statusId })).toBe(true);
    });

    /**
     * Functional Test ID: US3_AC1_TC2
     * Test: Filter by System works correctly
     */
    test('US3_filterSystemCorrectly - System filter limits results to selected system', () => {
      // Arrange
      const systemId = 1; // ERP-A
      
      // Act
      const filtered = filterBySystem(allTickets, systemId);
      
      // Assert
      expect(filtered.length).toBeGreaterThan(0);
      expect(filtered.length).toBeLessThan(allTickets.length);
      
      // Verify all filtered tickets have the correct system
      filtered.forEach(ticket => {
        expect(ticket.systemId).toBe(systemId);
        expect(ticket.systemName).toBe('ERP-A');
      });
      
      // Verify filter results
      expect(verifyFilterResults(filtered, { systemId })).toBe(true);
    });

    /**
     * Functional Test ID: US3_AC1_TC3
     * Test: Filter by Category works correctly
     */
    test('US3_filterCategoryCorrectly - Category filter limits results to selected category', () => {
      // Arrange
      const categoryId = 1; // Bug
      
      // Act
      const filtered = filterByCategory(allTickets, categoryId);
      
      // Assert
      expect(filtered.length).toBeGreaterThan(0);
      expect(filtered.length).toBeLessThan(allTickets.length);
      
      // Verify all filtered tickets have the correct category
      filtered.forEach(ticket => {
        expect(ticket.categoryId).toBe(categoryId);
        expect(ticket.categoryName).toBe('Bug');
      });
      
      // Verify filter results
      expect(verifyFilterResults(filtered, { categoryId })).toBe(true);
    });

    /**
     * Functional Test ID: US3_AC1_TC4
     * Test: Each filter type returns different results
     */
    test('US3_eachFilterReturnsUniqueResults - Different filters return different result sets', () => {
      // Act
      const byStatus = filterByStatus(allTickets, 1);
      const bySystem = filterBySystem(allTickets, 1);
      const byCategory = filterByCategory(allTickets, 1);
      
      // Assert - Each filter returns results
      expect(byStatus.length).toBeGreaterThan(0);
      expect(bySystem.length).toBeGreaterThan(0);
      expect(byCategory.length).toBeGreaterThan(0);
      
      // Assert - Results are different (unless by coincidence)
      const statusIds = byStatus.map(t => t.ticketId).sort();
      const systemIds = bySystem.map(t => t.ticketId).sort();
      const categoryIds = byCategory.map(t => t.ticketId).sort();
      
      // At least one should be different
      const allSame = JSON.stringify(statusIds) === JSON.stringify(systemIds) &&
                      JSON.stringify(systemIds) === JSON.stringify(categoryIds);
      expect(allSame).toBe(false);
    });

    /**
     * Functional Test ID: US3_AC1_TC5
     * Test: Filter with no matches returns empty array
     */
    test('US3_filterNoMatches - Filter with no matching tickets returns empty array', () => {
      // Arrange - Use a non-existent ID
      const nonExistentId = 999;
      
      // Act
      const byStatus = filterByStatus(allTickets, nonExistentId);
      const bySystem = filterBySystem(allTickets, nonExistentId);
      const byCategory = filterByCategory(allTickets, nonExistentId);
      
      // Assert
      expect(byStatus.length).toBe(0);
      expect(bySystem.length).toBe(0);
      expect(byCategory.length).toBe(0);
    });

    /**
     * Functional Test ID: US3_AC1_TC6
     * Test: Filter counts are accurate
     */
    test('US3_filterCountsAccurate - Filter result counts match expected values', () => {
      // Act
      const statusCounts = countByStatus(allTickets);
      const systemCounts = countBySystem(allTickets);
      const categoryCounts = countByCategory(allTickets);
      
      // Assert - Counts are positive
      Object.values(statusCounts).forEach(count => {
        expect(count).toBeGreaterThan(0);
      });
      
      Object.values(systemCounts).forEach(count => {
        expect(count).toBeGreaterThan(0);
      });
      
      Object.values(categoryCounts).forEach(count => {
        expect(count).toBeGreaterThan(0);
      });
      
      // Assert - Total counts match
      const totalByStatus = Object.values(statusCounts).reduce((a, b) => a + b, 0);
      const totalBySystem = Object.values(systemCounts).reduce((a, b) => a + b, 0);
      const totalByCategory = Object.values(categoryCounts).reduce((a, b) => a + b, 0);
      
      expect(totalByStatus).toBe(allTickets.length);
      expect(totalBySystem).toBe(allTickets.length);
      expect(totalByCategory).toBe(allTickets.length);
    });
  });

  /**
   * User Story 3: Filter Tickets
   * Acceptance Criterion 2: Multiple filters can be applied together
   */
  describe('US3 AC2: Multiple filters work together', () => {
    
    /**
     * Functional Test ID: US3_AC2_TC1
     * Test: Status and System filters work together
     */
    test('US3_multipleFiltersStatusSystem - Status and System filters combine correctly', () => {
      // Arrange
      const filters = {
        statusId: 1, // Pending
        systemId: 1  // ERP-A
      };
      
      // Act
      const filtered = applyMultipleFilters(allTickets, filters);
      
      // Assert
      expect(filtered.length).toBeGreaterThan(0);
      expect(filtered.length).toBeLessThanOrEqual(allTickets.length);
      
      // Verify all tickets match both filters
      filtered.forEach(ticket => {
        expect(ticket.statusId).toBe(filters.statusId);
        expect(ticket.systemId).toBe(filters.systemId);
      });
      
      expect(verifyFilterResults(filtered, filters)).toBe(true);
    });

    /**
     * Functional Test ID: US3_AC2_TC2
     * Test: Status and Category filters work together
     */
    test('US3_multipleFiltersStatusCategory - Status and Category filters combine correctly', () => {
      // Arrange
      const filters = {
        statusId: 1,   // Pending
        categoryId: 1  // Bug
      };
      
      // Act
      const filtered = applyMultipleFilters(allTickets, filters);
      
      // Assert
      expect(filtered.length).toBeGreaterThan(0);
      
      // Verify all tickets match both filters
      filtered.forEach(ticket => {
        expect(ticket.statusId).toBe(filters.statusId);
        expect(ticket.categoryId).toBe(filters.categoryId);
      });
      
      expect(verifyFilterResults(filtered, filters)).toBe(true);
    });

    /**
     * Functional Test ID: US3_AC2_TC3
     * Test: All three filters work together
     */
    test('US3_multipleFiltersAll - Status, System, and Category filters combine correctly', () => {
      // Arrange
      const filters = {
        statusId: 1,   // Pending
        systemId: 1,   // ERP-A
        categoryId: 1  // Bug
      };
      
      // Act
      const filtered = applyMultipleFilters(allTickets, filters);
      
      // Assert - May be 0 if no tickets match all criteria
      expect(filtered.length).toBeGreaterThanOrEqual(0);
      
      // Verify all tickets match all filters
      filtered.forEach(ticket => {
        expect(ticket.statusId).toBe(filters.statusId);
        expect(ticket.systemId).toBe(filters.systemId);
        expect(ticket.categoryId).toBe(filters.categoryId);
      });
      
      expect(verifyFilterResults(filtered, filters)).toBe(true);
    });

    /**
     * Functional Test ID: US3_AC2_TC4
     * Test: Multiple filters narrow down results
     */
    test('US3_multipleFiltersNarrowResults - Multiple filters reduce result count', () => {
      // Arrange
      const singleFilter = { statusId: 1 };
      const doubleFilter = { statusId: 1, systemId: 1 };
      const tripleFilter = { statusId: 1, systemId: 1, categoryId: 1 };
      
      // Act
      const single = applyMultipleFilters(allTickets, singleFilter);
      const double = applyMultipleFilters(allTickets, doubleFilter);
      const triple = applyMultipleFilters(allTickets, tripleFilter);
      
      // Assert - Each additional filter should reduce or maintain count
      expect(double.length).toBeLessThanOrEqual(single.length);
      expect(triple.length).toBeLessThanOrEqual(double.length);
    });

    /**
     * Functional Test ID: US3_AC2_TC5
     * Test: Multiple filters with no matches return empty
     */
    test('US3_multipleFiltersNoMatches - Incompatible filters return empty results', () => {
      // Arrange - Use filters that won't match any ticket
      const filters = {
        statusId: 999,
        systemId: 999,
        categoryId: 999
      };
      
      // Act
      const filtered = applyMultipleFilters(allTickets, filters);
      
      // Assert
      expect(filtered.length).toBe(0);
    });
  });

  /**
   * User Story 3: Filter Tickets
   * Acceptance Criterion 3: Removing a filter restores full ticket list
   */
  describe('US3 AC3: Removing filters restores full list', () => {
    
    /**
     * Functional Test ID: US3_AC3_TC1
     * Test: Removing status filter restores full list
     */
    test('US3_removeFilterRestoresFullList - Removing filter shows all tickets', () => {
      // Arrange
      const withFilter = filterByStatus(allTickets, 1);
      
      // Act - Remove filter (pass empty/null value)
      const withoutFilter = filterByStatus(allTickets, '');
      
      // Assert
      expect(withFilter.length).toBeLessThan(allTickets.length);
      expect(withoutFilter.length).toBe(allTickets.length);
      expect(withoutFilter).toEqual(allTickets);
    });

    /**
     * Functional Test ID: US3_AC3_TC2
     * Test: Removing one of multiple filters expands results
     */
    test('US3_removeOneFilterExpands - Removing one filter from multiple expands results', () => {
      // Arrange
      const bothFilters = applyMultipleFilters(allTickets, {
        statusId: 1,
        systemId: 1
      });
      
      // Act - Remove system filter
      const oneFilter = applyMultipleFilters(allTickets, {
        statusId: 1
      });
      
      // Assert
      expect(oneFilter.length).toBeGreaterThanOrEqual(bothFilters.length);
    });

    /**
     * Functional Test ID: US3_AC3_TC3
     * Test: Removing all filters shows complete list
     */
    test('US3_removeAllFiltersShowsAll - Removing all filters shows complete ticket list', () => {
      // Arrange
      const filtered = applyMultipleFilters(allTickets, {
        statusId: 1,
        systemId: 1,
        categoryId: 1
      });
      
      // Act - Remove all filters
      const unfiltered = applyMultipleFilters(allTickets, {});
      
      // Assert
      expect(filtered.length).toBeLessThanOrEqual(allTickets.length);
      expect(unfiltered.length).toBe(allTickets.length);
      expect(unfiltered).toEqual(allTickets);
    });

    /**
     * Functional Test ID: US3_AC3_TC4
     * Test: Filter state can be toggled
     */
    test('US3_filterToggle - Filters can be applied and removed repeatedly', () => {
      // Act & Assert - Apply filter
      const filtered1 = filterByStatus(allTickets, 1);
      expect(filtered1.length).toBeLessThan(allTickets.length);
      
      // Remove filter
      const unfiltered1 = filterByStatus(allTickets, '');
      expect(unfiltered1.length).toBe(allTickets.length);
      
      // Apply filter again
      const filtered2 = filterByStatus(allTickets, 1);
      expect(filtered2.length).toBe(filtered1.length);
      expect(filtered2).toEqual(filtered1);
      
      // Remove filter again
      const unfiltered2 = filterByStatus(allTickets, '');
      expect(unfiltered2.length).toBe(allTickets.length);
    });
  });

  /**
   * User Story 4: Search Tickets
   * Acceptance Criterion 1: Search checks all ticket columns
   */
  describe('US4 AC1: Search checks all columns', () => {
    
    /**
     * Functional Test ID: US4_AC1_TC1
     * Test: Search finds tickets by ID
     */
    test('US4_searchByTicketId - Search finds tickets by ticket ID', () => {
      // Arrange
      const keyword = '1'; // Should match ticket IDs containing 1
      
      // Act
      const results = searchTickets(allTickets, keyword);
      
      // Assert
      expect(results.length).toBeGreaterThan(0);
      expect(verifySearchResults(results, keyword)).toBe(true);
      
      // Verify at least one result has matching ID
      const hasMatchingId = results.some(t => 
        `#${t.ticketId}`.toLowerCase().includes(keyword.toLowerCase())
      );
      expect(hasMatchingId).toBe(true);
    });

    /**
     * Functional Test ID: US4_AC1_TC2
     * Test: Search finds tickets by title
     */
    test('US4_searchByTitle - Search finds tickets by title', () => {
      // Arrange
      const keyword = 'login'; // Should match "Login issue"
      
      // Act
      const results = searchTickets(allTickets, keyword);
      
      // Assert
      expect(results.length).toBeGreaterThan(0);
      expect(verifySearchResults(results, keyword)).toBe(true);
      
      // Verify at least one result has matching title
      const hasMatchingTitle = results.some(t => 
        t.title.toLowerCase().includes(keyword.toLowerCase())
      );
      expect(hasMatchingTitle).toBe(true);
    });

    /**
     * Functional Test ID: US4_AC1_TC3
     * Test: Search finds tickets by description
     */
    test('US4_searchByDescription - Search finds tickets by description', () => {
      // Arrange
      const keyword = 'users'; // Should match descriptions containing "users"
      
      // Act
      const results = searchTickets(allTickets, keyword);
      
      // Assert
      expect(results.length).toBeGreaterThan(0);
      expect(verifySearchResults(results, keyword)).toBe(true);
      
      // Verify at least one result has matching description
      const hasMatchingDesc = results.some(t => 
        t.description?.toLowerCase().includes(keyword.toLowerCase())
      );
      expect(hasMatchingDesc).toBe(true);
    });

    /**
     * Functional Test ID: US4_AC1_TC4
     * Test: Search finds tickets by status
     */
    test('US4_searchByStatus - Search finds tickets by status name', () => {
      // Arrange
      const keyword = 'pending';
      
      // Act
      const results = searchTickets(allTickets, keyword);
      
      // Assert
      expect(results.length).toBeGreaterThan(0);
      expect(verifySearchResults(results, keyword)).toBe(true);
      
      // Verify all results have matching status
      results.forEach(ticket => {
        expect(ticket.statusName.toLowerCase()).toContain(keyword.toLowerCase());
      });
    });

    /**
     * Functional Test ID: US4_AC1_TC5
     * Test: Search finds tickets by system
     */
    test('US4_searchBySystem - Search finds tickets by system name', () => {
      // Arrange
      const keyword = 'erp';
      
      // Act
      const results = searchTickets(allTickets, keyword);
      
      // Assert
      expect(results.length).toBeGreaterThan(0);
      expect(verifySearchResults(results, keyword)).toBe(true);
      
      // Verify at least one result has matching system
      const hasMatchingSystem = results.some(t => 
        t.systemName.toLowerCase().includes(keyword.toLowerCase())
      );
      expect(hasMatchingSystem).toBe(true);
    });

    /**
     * Functional Test ID: US4_AC1_TC6
     * Test: Search finds tickets by category
     */
    test('US4_searchByCategory - Search finds tickets by category name', () => {
      // Arrange
      const keyword = 'bug';
      
      // Act
      const results = searchTickets(allTickets, keyword);
      
      // Assert
      expect(results.length).toBeGreaterThan(0);
      expect(verifySearchResults(results, keyword)).toBe(true);
      
      // Verify at least one result has matching category
      const hasMatchingCategory = results.some(t => 
        t.categoryName.toLowerCase().includes(keyword.toLowerCase())
      );
      expect(hasMatchingCategory).toBe(true);
    });
  });

  /**
   * User Story 4: Search Tickets
   * Acceptance Criterion 2: Partial matches return results
   */
  describe('US4 AC2: Partial matches work', () => {
    
    /**
     * Functional Test ID: US4_AC2_TC1
     * Test: Partial word match returns results
     */
    test('US4_searchPartialMatch - Partial word matches return results', () => {
      // Arrange
      const keyword = 'log'; // Should match "login"
      
      // Act
      const results = searchTickets(allTickets, keyword);
      
      // Assert
      expect(results.length).toBeGreaterThan(0);
      expect(verifySearchResults(results, keyword)).toBe(true);
    });

    /**
     * Functional Test ID: US4_AC2_TC2
     * Test: Single character search works
     */
    test('US4_searchSingleCharacter - Single character search returns results', () => {
      // Arrange
      const keyword = 'e'; // Should match many tickets
      
      // Act
      const results = searchTickets(allTickets, keyword);
      
      // Assert
      expect(results.length).toBeGreaterThan(0);
      expect(verifySearchResults(results, keyword)).toBe(true);
    });

    /**
     * Functional Test ID: US4_AC2_TC3
     * Test: Case-insensitive search
     */
    test('US4_searchCaseInsensitive - Search is case-insensitive', () => {
      // Arrange
      const lowerCase = 'login';
      const upperCase = 'LOGIN';
      const mixedCase = 'LoGiN';
      
      // Act
      const resultsLower = searchTickets(allTickets, lowerCase);
      const resultsUpper = searchTickets(allTickets, upperCase);
      const resultsMixed = searchTickets(allTickets, mixedCase);
      
      // Assert - All should return same results
      expect(resultsLower.length).toBe(resultsUpper.length);
      expect(resultsLower.length).toBe(resultsMixed.length);
      expect(resultsLower).toEqual(resultsUpper);
      expect(resultsLower).toEqual(resultsMixed);
    });

    /**
     * Functional Test ID: US4_AC2_TC4
     * Test: Search with spaces
     */
    test('US4_searchWithSpaces - Search handles phrases with spaces', () => {
      // Arrange
      const keyword = 'login issue'; // Multi-word search
      
      // Act
      const results = searchTickets(allTickets, keyword);
      
      // Assert
      expect(results.length).toBeGreaterThan(0);
      expect(verifySearchResults(results, keyword)).toBe(true);
    });

    /**
     * Functional Test ID: US4_AC2_TC5
     * Test: Search trims whitespace
     */
    test('US4_searchTrimsWhitespace - Search trims leading/trailing whitespace', () => {
      // Arrange
      const withSpaces = '  login  ';
      const withoutSpaces = 'login';
      
      // Act
      const resultsWithSpaces = searchTickets(allTickets, withSpaces);
      const resultsWithoutSpaces = searchTickets(allTickets, withoutSpaces);
      
      // Assert - Should return same results
      expect(resultsWithSpaces.length).toBe(resultsWithoutSpaces.length);
      expect(resultsWithSpaces).toEqual(resultsWithoutSpaces);
    });
  });

  /**
   * User Story 4: Search Tickets
   * Acceptance Criterion 3: No-match searches show empty results
   */
  describe('US4 AC3: No-match searches return empty', () => {
    
    /**
     * Functional Test ID: US4_AC3_TC1
     * Test: Non-existent keyword returns empty
     */
    test('US4_searchNoMatch - Non-existent keyword returns empty results', () => {
      // Arrange
      const keyword = 'xyzabc123nonexistent';
      
      // Act
      const results = searchTickets(allTickets, keyword);
      
      // Assert
      expect(results.length).toBe(0);
      expect(results).toEqual([]);
    });

    /**
     * Functional Test ID: US4_AC3_TC2
     * Test: Empty search returns all tickets
     */
    test('US4_searchEmpty - Empty search returns all tickets', () => {
      // Arrange
      const keyword = '';
      
      // Act
      const results = searchTickets(allTickets, keyword);
      
      // Assert
      expect(results.length).toBe(allTickets.length);
      expect(results).toEqual(allTickets);
    });

    /**
     * Functional Test ID: US4_AC3_TC3
     * Test: Whitespace-only search returns all tickets
     */
    test('US4_searchWhitespaceOnly - Whitespace-only search returns all tickets', () => {
      // Arrange
      const keyword = '   ';
      
      // Act
      const results = searchTickets(allTickets, keyword);
      
      // Assert
      expect(results.length).toBe(allTickets.length);
      expect(results).toEqual(allTickets);
    });

    /**
     * Functional Test ID: US4_AC3_TC4
     * Test: Special characters with no match return empty
     */
    test('US4_searchSpecialCharsNoMatch - Special characters with no match return empty', () => {
      // Arrange
      const keyword = '@#$%^&*()';
      
      // Act
      const results = searchTickets(allTickets, keyword);
      
      // Assert
      expect(results.length).toBe(0);
    });

    /**
     * Functional Test ID: US4_AC3_TC5
     * Test: Display shows empty state for no results
     */
    test('US4_displayEmptyState - Display shows empty state when no search results', () => {
      // Arrange
      const keyword = 'nonexistent';
      const results = searchTickets(allTickets, keyword);
      
      // Act
      displayTickets(results);
      
      // Assert
      const tbody = getTicketTableBody();
      expect(tbody.querySelector('.empty-state')).toBeTruthy();
      expect(tbody.textContent).toContain('No tickets found');
      expect(countVisibleTickets()).toBe(0);
    });
  });

  /**
   * Integration Tests: Search and Filter Together
   */
  describe('Integration: Search and Filter combined', () => {
    
    /**
     * Functional Test ID: US3_US4_INT_TC1
     * Test: Search and filter work together
     */
    test('US3_US4_searchAndFilterTogether - Search and filter can be combined', () => {
      // Arrange
      const searchKeyword = 'bug';
      const filters = { statusId: 1 }; // Pending
      
      // Act - First search, then filter
      const searched = searchTickets(allTickets, searchKeyword);
      const searchedAndFiltered = applyMultipleFilters(searched, filters);
      
      // Assert
      expect(searchedAndFiltered.length).toBeGreaterThanOrEqual(0);
      expect(searchedAndFiltered.length).toBeLessThanOrEqual(searched.length);
      
      // Verify results match both search and filter
      searchedAndFiltered.forEach(ticket => {
        expect(ticket.statusId).toBe(filters.statusId);
        expect(verifySearchResults([ticket], searchKeyword)).toBe(true);
      });
    });

    /**
     * Functional Test ID: US3_US4_INT_TC2
     * Test: Clearing search with active filters
     */
    test('US3_US4_clearSearchKeepsFilters - Clearing search maintains active filters', () => {
      // Arrange
      const filters = { statusId: 1 };
      const filtered = applyMultipleFilters(allTickets, filters);
      
      // Act - Search then clear
      const searched = searchTickets(filtered, 'bug');
      const clearedSearch = searchTickets(filtered, '');
      
      // Assert
      expect(searched.length).toBeLessThanOrEqual(filtered.length);
      expect(clearedSearch.length).toBe(filtered.length);
      expect(clearedSearch).toEqual(filtered);
    });

    /**
     * Functional Test ID: US3_US4_INT_TC3
     * Test: Clearing filters with active search
     */
    test('US3_US4_clearFiltersKeepsSearch - Clearing filters maintains active search', () => {
      // Arrange
      const searchKeyword = 'login';
      const searched = searchTickets(allTickets, searchKeyword);
      
      // Act - Apply filter then remove
      const filtered = applyMultipleFilters(searched, { statusId: 1 });
      const clearedFilter = applyMultipleFilters(searched, {});
      
      // Assert
      expect(filtered.length).toBeLessThanOrEqual(searched.length);
      expect(clearedFilter.length).toBe(searched.length);
      expect(clearedFilter).toEqual(searched);
    });
  });

  /**
   * Performance Tests
   */
  describe('Performance: Large datasets', () => {
    
    /**
     * Functional Test ID: US3_US4_PERF_TC1
     * Test: Filter performance with large dataset
     */
    test('US3_US4_filterPerformance - Filters work efficiently with large dataset', () => {
      // Arrange - Create 100 tickets
      const largeDataset = Array.from({ length: 100 }, (_, i) => ({
        ticketId: i + 1,
        title: `Ticket ${i + 1}`,
        statusId: (i % 3) + 1,
        statusName: ['Pending', 'In-Progress', 'Resolved'][i % 3],
        systemId: (i % 3) + 1,
        systemName: ['ERP-A', 'ERP-B', 'CRM'][i % 3],
        categoryId: (i % 4) + 1,
        categoryName: ['Bug', 'Feature', 'Performance', 'Security'][i % 4],
        createdDate: '2026-02-13T10:00:00Z',
        updatedDate: '2026-02-13T10:00:00Z'
      }));
      
      // Act
      const startTime = performance.now();
      const filtered = applyMultipleFilters(largeDataset, {
        statusId: 1,
        systemId: 1
      });
      const endTime = performance.now();
      
      // Assert - Should complete quickly (under 50ms)
      expect(endTime - startTime).toBeLessThan(50);
      expect(filtered.length).toBeGreaterThan(0);
    });

    /**
     * Functional Test ID: US3_US4_PERF_TC2
     * Test: Search performance with large dataset
     */
    test('US3_US4_searchPerformance - Search works efficiently with large dataset', () => {
      // Arrange - Create 100 tickets
      const largeDataset = Array.from({ length: 100 }, (_, i) => ({
        ticketId: i + 1,
        title: `Ticket ${i + 1}`,
        description: `Description for ticket ${i + 1}`,
        statusName: 'Pending',
        systemName: 'ERP-A',
        categoryName: 'Bug',
        createdDate: '2026-02-13T10:00:00Z',
        updatedDate: '2026-02-13T10:00:00Z'
      }));
      
      // Act
      const startTime = performance.now();
      const results = searchTickets(largeDataset, 'ticket');
      const endTime = performance.now();
      
      // Assert - Should complete quickly (under 50ms)
      expect(endTime - startTime).toBeLessThan(50);
      expect(results.length).toBe(100); // All should match
    });
  });
});