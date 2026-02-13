/**
 * Ticket List Test Utility Functions
 * Helper functions specifically for testing ticket list functionality
 */

/**
 * Create ticket list HTML structure for testing
 * Simulates the table structure from index.html
 */
function createTicketListHTML() {
  const html = `
    <div class="container">
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
            <!-- Tickets will be loaded here -->
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
 * Get ticket table body element
 * @returns {HTMLElement} Table body element
 */
function getTicketTableBody() {
  return document.getElementById('ticketsTableBody');
}

/**
 * Get all ticket rows from the table
 * @returns {NodeList} List of ticket row elements
 */
function getTicketRows() {
  const tbody = getTicketTableBody();
  return tbody.querySelectorAll('tr:not(.loading-cell):not(.empty-state)');
}

/**
 * Get ticket data from a table row
 * @param {HTMLElement} row - Table row element
 * @returns {Object} Ticket data extracted from row
 */
function getTicketDataFromRow(row) {
  const cells = row.querySelectorAll('td');
  
  if (cells.length < 7) {
    return null;
  }
  
  return {
    ticketId: cells[0].textContent.trim(),
    title: cells[1].textContent.trim(),
    status: cells[2].textContent.trim(),
    system: cells[3].textContent.trim(),
    category: cells[4].textContent.trim(),
    dateSubmitted: cells[5].textContent.trim(),
    lastUpdated: cells[6].textContent.trim()
  };
}

/**
 * Get all tickets data from the table
 * @returns {Array} Array of ticket data objects
 */
function getAllTicketsFromTable() {
  const rows = getTicketRows();
  const tickets = [];
  
  rows.forEach(row => {
    const ticketData = getTicketDataFromRow(row);
    if (ticketData) {
      tickets.push(ticketData);
    }
  });
  
  return tickets;
}

/**
 * Check if table is showing loading state
 * @returns {boolean} True if loading state is visible
 */
function isTableLoading() {
  const tbody = getTicketTableBody();
  const loadingCell = tbody.querySelector('.loading-cell');
  return loadingCell !== null;
}

/**
 * Check if table is showing empty state
 * @returns {boolean} True if empty state is visible
 */
function isTableEmpty() {
  const tbody = getTicketTableBody();
  const emptyState = tbody.querySelector('.empty-state');
  return emptyState !== null;
}

/**
 * Count visible ticket rows
 * @returns {number} Number of visible ticket rows
 */
function countVisibleTickets() {
  return getTicketRows().length;
}

/**
 * Check if a column exists in the table header
 * @param {string} columnName - Name of the column to check
 * @returns {boolean} True if column exists
 */
function hasTableColumn(columnName) {
  const headers = document.querySelectorAll('.tickets-table thead th');
  return Array.from(headers).some(th => 
    th.textContent.trim().toLowerCase() === columnName.toLowerCase()
  );
}

/**
 * Get all column headers from the table
 * @returns {Array<string>} Array of column header names
 */
function getTableColumns() {
  const headers = document.querySelectorAll('.tickets-table thead th');
  return Array.from(headers).map(th => th.textContent.trim());
}

/**
 * Find ticket row by ticket ID
 * @param {number|string} ticketId - Ticket ID to find
 * @returns {HTMLElement|null} Table row element or null
 */
function findTicketRowById(ticketId) {
  const rows = getTicketRows();
  const idToFind = `#${ticketId}`;
  
  for (let row of rows) {
    const cells = row.querySelectorAll('td');
    if (cells.length > 0 && cells[0].textContent.trim() === idToFind) {
      return row;
    }
  }
  
  return null;
}

/**
 * Check if ticket exists in the table
 * @param {number|string} ticketId - Ticket ID to check
 * @returns {boolean} True if ticket exists
 */
function ticketExistsInTable(ticketId) {
  return findTicketRowById(ticketId) !== null;
}

/**
 * Verify all required columns are present
 * @returns {Object} Object with column presence status
 */
function verifyRequiredColumns() {
  return {
    ticketId: hasTableColumn('Ticket ID'),
    title: hasTableColumn('Title'),
    status: hasTableColumn('Status'),
    system: hasTableColumn('System'),
    category: hasTableColumn('Category'),
    dateSubmitted: hasTableColumn('Date Submitted'),
    lastUpdated: hasTableColumn('Last Updated')
  };
}

/**
 * Filter tickets by author (simulates user filtering)
 * @param {Array} tickets - Array of ticket objects
 * @param {string} author - Author to filter by
 * @returns {Array} Filtered tickets
 */
function filterTicketsByAuthor(tickets, author) {
  return tickets.filter(ticket => ticket.author === author);
}

/**
 * Format date for comparison (matches formatDate from utils.js)
 * @param {string} dateString - ISO date string
 * @returns {string} Formatted date string
 */
function formatDateForComparison(dateString) {
  if (!dateString) return 'N/A';
  const date = new Date(dateString);
  const options = { year: 'numeric', month: 'short', day: 'numeric' };
  return date.toLocaleDateString('en-US', options);
}

/**
 * Verify ticket data matches expected values
 * @param {Object} actualTicket - Actual ticket data from table
 * @param {Object} expectedTicket - Expected ticket data
 * @returns {Object} Verification result with details
 */
function verifyTicketData(actualTicket, expectedTicket) {
  const result = {
    matches: true,
    differences: []
  };
  
  // Check Ticket ID
  const expectedId = `#${expectedTicket.ticketId}`;
  if (actualTicket.ticketId !== expectedId) {
    result.matches = false;
    result.differences.push({
      field: 'ticketId',
      expected: expectedId,
      actual: actualTicket.ticketId
    });
  }
  
  // Check Title
  if (actualTicket.title !== expectedTicket.title) {
    result.matches = false;
    result.differences.push({
      field: 'title',
      expected: expectedTicket.title,
      actual: actualTicket.title
    });
  }
  
  // Check Status (may include badge text)
  const statusMatch = actualTicket.status.includes(expectedTicket.statusName);
  if (!statusMatch) {
    result.matches = false;
    result.differences.push({
      field: 'status',
      expected: expectedTicket.statusName,
      actual: actualTicket.status
    });
  }
  
  // Check System
  const expectedSystem = expectedTicket.systemName || 'N/A';
  if (actualTicket.system !== expectedSystem) {
    result.matches = false;
    result.differences.push({
      field: 'system',
      expected: expectedSystem,
      actual: actualTicket.system
    });
  }
  
  // Check Category
  const expectedCategory = expectedTicket.categoryName || 'N/A';
  if (actualTicket.category !== expectedCategory) {
    result.matches = false;
    result.differences.push({
      field: 'category',
      expected: expectedCategory,
      actual: actualTicket.category
    });
  }
  
  return result;
}

/**
 * Assert that ticket list displays correctly
 * @param {Array} expectedTickets - Expected tickets to be displayed
 */
function assertTicketListDisplays(expectedTickets) {
  const actualTickets = getAllTicketsFromTable();
  
  expect(actualTickets.length).toBe(expectedTickets.length);
  
  expectedTickets.forEach((expectedTicket, index) => {
    const actualTicket = actualTickets[index];
    expect(actualTicket).toBeTruthy();
    
    // Verify ticket ID
    expect(actualTicket.ticketId).toBe(`#${expectedTicket.ticketId}`);
    
    // Verify title
    expect(actualTicket.title).toBe(expectedTicket.title);
  });
}

module.exports = {
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
};