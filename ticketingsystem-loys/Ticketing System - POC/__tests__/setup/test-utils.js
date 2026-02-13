/**
 * Test Utility Functions
 * Helper functions for setting up and testing DOM elements
 */

/**
 * Create dashboard HTML structure for testing
 * Simulates the stat cards from index.html
 */
function createDashboardHTML() {
  const html = `
    <div class="container">
      <!-- Stats Cards -->
      <div class="stats-container">
        <div class="stat-card stat-pending">
          <div class="stat-content">
            <div class="stat-label">Pending</div>
            <div class="stat-value" id="pendingCount">0</div>
          </div>
        </div>

        <div class="stat-card stat-progress">
          <div class="stat-content">
            <div class="stat-label">In Progress</div>
            <div class="stat-value" id="progressCount">0</div>
          </div>
        </div>

        <div class="stat-card stat-resolved">
          <div class="stat-content">
            <div class="stat-label">Resolved</div>
            <div class="stat-value" id="resolvedCount">0</div>
          </div>
        </div>
      </div>
    </div>
  `;
  
  document.body.innerHTML = html;
}

/**
 * Get stat card elements
 * @returns {Object} Object containing references to stat elements
 */
function getStatElements() {
  return {
    pendingCount: document.getElementById('pendingCount'),
    progressCount: document.getElementById('progressCount'),
    resolvedCount: document.getElementById('resolvedCount')
  };
}

/**
 * Check if element is visible
 * @param {HTMLElement} element - DOM element to check
 * @returns {boolean} True if element is visible
 */
function isElementVisible(element) {
  if (!element) return false;
  
  const style = window.getComputedStyle(element);
  return style.display !== 'none' && 
         style.visibility !== 'hidden' && 
         style.opacity !== '0';
}

/**
 * Get text content of element
 * @param {HTMLElement} element - DOM element
 * @returns {string} Text content or empty string
 */
function getElementText(element) {
  return element ? element.textContent.trim() : '';
}

/**
 * Get numeric value from element
 * @param {HTMLElement} element - DOM element containing a number
 * @returns {number} Parsed number or 0
 */
function getElementNumber(element) {
  const text = getElementText(element);
  const num = parseInt(text, 10);
  return isNaN(num) ? 0 : num;
}

/**
 * Wait for element to be updated
 * @param {HTMLElement} element - Element to watch
 * @param {number} timeout - Timeout in milliseconds
 * @returns {Promise} Promise that resolves when element updates
 */
function waitForElementUpdate(element, timeout = 1000) {
  return new Promise((resolve, reject) => {
    const initialValue = element.textContent;
    const startTime = Date.now();
    
    const checkInterval = setInterval(() => {
      if (element.textContent !== initialValue) {
        clearInterval(checkInterval);
        resolve(element.textContent);
      } else if (Date.now() - startTime > timeout) {
        clearInterval(checkInterval);
        reject(new Error('Element did not update within timeout'));
      }
    }, 50);
  });
}

/**
 * Simulate async operation completion
 * @param {number} delay - Delay in milliseconds
 * @returns {Promise} Promise that resolves after delay
 */
function waitFor(delay = 0) {
  return new Promise(resolve => setTimeout(resolve, delay));
}

/**
 * Count tickets by status
 * @param {Array} tickets - Array of ticket objects
 * @returns {Object} Object with counts for each status
 */
function countTicketsByStatus(tickets) {
  const counts = {
    pending: 0,
    progress: 0,
    resolved: 0
  };
  
  tickets.forEach(ticket => {
    const statusName = ticket.statusName?.toLowerCase() || '';
    if (statusName.includes('open') || statusName.includes('pending')) {
      counts.pending++;
    } else if (statusName.includes('progress')) {
      counts.progress++;
    } else if (statusName.includes('resolved') || statusName.includes('closed')) {
      counts.resolved++;
    }
  });
  
  return counts;
}

/**
 * Assert element exists and is visible
 * @param {HTMLElement} element - Element to check
 * @param {string} elementName - Name for error message
 */
function assertElementVisible(element, elementName) {
  expect(element).toBeTruthy();
  expect(element).toBeInTheDocument();
  expect(isElementVisible(element)).toBe(true);
}

/**
 * Assert element has expected text
 * @param {HTMLElement} element - Element to check
 * @param {string} expectedText - Expected text content
 */
function assertElementText(element, expectedText) {
  expect(element).toBeTruthy();
  expect(getElementText(element)).toBe(expectedText);
}

/**
 * Assert element has expected number
 * @param {HTMLElement} element - Element to check
 * @param {number} expectedNumber - Expected numeric value
 */
function assertElementNumber(element, expectedNumber) {
  expect(element).toBeTruthy();
  expect(getElementNumber(element)).toBe(expectedNumber);
}

module.exports = {
  createDashboardHTML,
  getStatElements,
  isElementVisible,
  getElementText,
  getElementNumber,
  waitForElementUpdate,
  waitFor,
  countTicketsByStatus,
  assertElementVisible,
  assertElementText,
  assertElementNumber
};