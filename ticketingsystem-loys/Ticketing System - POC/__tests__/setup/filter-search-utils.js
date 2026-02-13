/**
 * Filter and Search Test Utility Functions
 * Helper functions for testing filter and search functionality
 */

/**
 * Create filter and search HTML structure for testing
 * Simulates the search box and filter dropdowns from index.html
 */
function createFilterSearchHTML() {
  const html = `
    <div class="container">
      <!-- Search and Filters -->
      <div class="filters-section">
        <div class="search-box">
          <input type="text" id="searchInput" placeholder="Search by Ticket ID or Title..." />
        </div>

        <div class="filter-dropdowns">
          <select id="statusFilter" class="filter-select">
            <option value="">All Status</option>
            <option value="1">Pending</option>
            <option value="2">In-Progress</option>
            <option value="3">Resolved</option>
          </select>

          <select id="systemFilter" class="filter-select">
            <option value="">All Systems</option>
            <option value="1">ERP-A</option>
            <option value="2">ERP-B</option>
            <option value="3">CRM</option>
          </select>

          <select id="categoryFilter" class="filter-select">
            <option value="">All Categories</option>
            <option value="1">Bug</option>
            <option value="2">Feature</option>
            <option value="3">Performance</option>
            <option value="4">Security</option>
          </select>

          <select id="sortFilter" class="filter-select">
            <option value="newest">Date (Newest First)</option>
            <option value="oldest">Date (Oldest First)</option>
            <option value="updated">Last Updated</option>
          </select>
        </div>
      </div>

      <!-- Tickets Table -->
      <div class="table-container">
        <table class="tickets-table">
          <thead>
            <tr>
              <th>Ticket ID</th>
              <th>Title</th>
              <th>Status</th>
              <th>System</th>
              <th>Category</th>
              <th>Date Submitted</th>
              <th>Last Updated</th>
            </tr>
          </thead>
          <tbody id="ticketsTableBody">
            <tr>
              <td colspan="7" class="loading-cell">
                <div class="loading-spinner"></div>
                <p>Loading tickets...</p>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  `;
  
  document.body.innerHTML = html;
}

/**
 * Get filter elements
 * @returns {Object} Object containing filter element references
 */
function getFilterElements() {
  return {
    searchInput: document.getElementById('searchInput'),
    statusFilter: document.getElementById('statusFilter'),
    systemFilter: document.getElementById('systemFilter'),
    categoryFilter: document.getElementById('categoryFilter'),
    sortFilter: document.getElementById('sortFilter')
  };
}

/**
 * Set search input value
 * @param {string} value - Search keyword
 */
function setSearchValue(value) {
  const searchInput = document.getElementById('searchInput');
  searchInput.value = value;
  
  // Trigger input event
  const event = new Event('input', { bubbles: true });
  searchInput.dispatchEvent(event);
}

/**
 * Set filter dropdown value
 * @param {string} filterId - ID of the filter element
 * @param {string} value - Value to set
 */
function setFilterValue(filterId, value) {
  const filter = document.getElementById(filterId);
  filter.value = value;
  
  // Trigger change event
  const event = new Event('change', { bubbles: true });
  filter.dispatchEvent(event);
}

/**
 * Clear all filters
 */
function clearAllFilters() {
  const filters = getFilterElements();
  
  filters.searchInput.value = '';
  filters.statusFilter.value = '';
  filters.systemFilter.value = '';
  filters.categoryFilter.value = '';
  filters.sortFilter.value = 'newest';
}

/**
 * Get current filter values
 * @returns {Object} Current filter values
 */
function getCurrentFilterValues() {
  const filters = getFilterElements();
  
  return {
    search: filters.searchInput.value,
    status: filters.statusFilter.value,
    system: filters.systemFilter.value,
    category: filters.categoryFilter.value,
    sort: filters.sortFilter.value
  };
}

/**
 * Filter tickets by status
 * @param {Array} tickets - Array of ticket objects
 * @param {string|number} statusId - Status ID to filter by
 * @returns {Array} Filtered tickets
 */
function filterByStatus(tickets, statusId) {
  if (!statusId) return tickets;
  return tickets.filter(t => t.statusId == statusId);
}

/**
 * Filter tickets by system
 * @param {Array} tickets - Array of ticket objects
 * @param {string|number} systemId - System ID to filter by
 * @returns {Array} Filtered tickets
 */
function filterBySystem(tickets, systemId) {
  if (!systemId) return tickets;
  return tickets.filter(t => t.systemId == systemId);
}

/**
 * Filter tickets by category
 * @param {Array} tickets - Array of ticket objects
 * @param {string|number} categoryId - Category ID to filter by
 * @returns {Array} Filtered tickets
 */
function filterByCategory(tickets, categoryId) {
  if (!categoryId) return tickets;
  return tickets.filter(t => t.categoryId == categoryId);
}

/**
 * Apply multiple filters
 * @param {Array} tickets - Array of ticket objects
 * @param {Object} filters - Filter criteria
 * @returns {Array} Filtered tickets
 */
function applyMultipleFilters(tickets, filters) {
  let result = [...tickets];
  
  if (filters.statusId) {
    result = filterByStatus(result, filters.statusId);
  }
  
  if (filters.systemId) {
    result = filterBySystem(result, filters.systemId);
  }
  
  if (filters.categoryId) {
    result = filterByCategory(result, filters.categoryId);
  }
  
  return result;
}

/**
 * Search tickets by keyword
 * @param {Array} tickets - Array of ticket objects
 * @param {string} keyword - Search keyword
 * @returns {Array} Matching tickets
 */
function searchTickets(tickets, keyword) {
  if (!keyword || keyword.trim() === '') {
    return tickets;
  }
  
  const searchTerm = keyword.trim().toLowerCase();
  
  return tickets.filter(ticket => {
    const ticketId = `#${ticket.ticketId}`.toLowerCase();
    const title = ticket.title?.toLowerCase() || '';
    const description = ticket.description?.toLowerCase() || '';
    const status = ticket.statusName?.toLowerCase() || '';
    const system = ticket.systemName?.toLowerCase() || '';
    const category = ticket.categoryName?.toLowerCase() || '';
    
    return ticketId.includes(searchTerm) ||
           title.includes(searchTerm) ||
           description.includes(searchTerm) ||
           status.includes(searchTerm) ||
           system.includes(searchTerm) ||
           category.includes(searchTerm);
  });
}

/**
 * Sort tickets
 * @param {Array} tickets - Array of ticket objects
 * @param {string} sortBy - Sort criteria ('newest', 'oldest', 'updated')
 * @returns {Array} Sorted tickets
 */
function sortTickets(tickets, sortBy) {
  const sorted = [...tickets];
  
  sorted.sort((a, b) => {
    switch (sortBy) {
      case 'newest':
        return new Date(b.createdDate) - new Date(a.createdDate);
      case 'oldest':
        return new Date(a.createdDate) - new Date(b.createdDate);
      case 'updated':
        return new Date(b.updatedDate) - new Date(a.updatedDate);
      default:
        return 0;
    }
  });
  
  return sorted;
}

/**
 * Count tickets by status
 * @param {Array} tickets - Array of ticket objects
 * @returns {Object} Count by status
 */
function countByStatus(tickets) {
  const counts = {};
  
  tickets.forEach(ticket => {
    const status = ticket.statusName || 'Unknown';
    counts[status] = (counts[status] || 0) + 1;
  });
  
  return counts;
}

/**
 * Count tickets by system
 * @param {Array} tickets - Array of ticket objects
 * @returns {Object} Count by system
 */
function countBySystem(tickets) {
  const counts = {};
  
  tickets.forEach(ticket => {
    const system = ticket.systemName || 'Unknown';
    counts[system] = (counts[system] || 0) + 1;
  });
  
  return counts;
}

/**
 * Count tickets by category
 * @param {Array} tickets - Array of ticket objects
 * @returns {Object} Count by category
 */
function countByCategory(tickets) {
  const counts = {};
  
  tickets.forEach(ticket => {
    const category = ticket.categoryName || 'Unknown';
    counts[category] = (counts[category] || 0) + 1;
  });
  
  return counts;
}

/**
 * Verify filter results
 * @param {Array} filteredTickets - Filtered ticket array
 * @param {Object} criteria - Filter criteria to verify
 * @returns {boolean} True if all tickets match criteria
 */
function verifyFilterResults(filteredTickets, criteria) {
  return filteredTickets.every(ticket => {
    if (criteria.statusId && ticket.statusId != criteria.statusId) {
      return false;
    }
    if (criteria.systemId && ticket.systemId != criteria.systemId) {
      return false;
    }
    if (criteria.categoryId && ticket.categoryId != criteria.categoryId) {
      return false;
    }
    return true;
  });
}

/**
 * Verify search results contain keyword
 * @param {Array} searchResults - Search result array
 * @param {string} keyword - Search keyword
 * @returns {boolean} True if all results contain keyword
 */
function verifySearchResults(searchResults, keyword) {
  if (!keyword || keyword.trim() === '') {
    return true;
  }
  
  const searchTerm = keyword.trim().toLowerCase();
  
  return searchResults.every(ticket => {
    const ticketId = `#${ticket.ticketId}`.toLowerCase();
    const title = ticket.title?.toLowerCase() || '';
    const description = ticket.description?.toLowerCase() || '';
    const status = ticket.statusName?.toLowerCase() || '';
    const system = ticket.systemName?.toLowerCase() || '';
    const category = ticket.categoryName?.toLowerCase() || '';
    
    return ticketId.includes(searchTerm) ||
           title.includes(searchTerm) ||
           description.includes(searchTerm) ||
           status.includes(searchTerm) ||
           system.includes(searchTerm) ||
           category.includes(searchTerm);
  });
}

/**
 * Get unique values from tickets
 * @param {Array} tickets - Array of ticket objects
 * @param {string} field - Field to get unique values from
 * @returns {Array} Array of unique values
 */
function getUniqueValues(tickets, field) {
  const values = tickets.map(t => t[field]).filter(v => v != null);
  return [...new Set(values)];
}

module.exports = {
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
};