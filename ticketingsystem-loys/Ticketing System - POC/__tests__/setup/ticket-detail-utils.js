/**
 * Utility functions for Ticket Detail tests (US6)
 * Provides helpers for rendering ticket details and verifying read-only state
 */

/**
 * Setup ticket detail page in DOM
 */
export function setupTicketDetailPage() {
  document.body.innerHTML = `
    <div class="container">
      <!-- Ticket Header -->
      <div class="ticket-header">
        <div class="ticket-id-status">
          <h1 class="ticket-id" id="ticketId">#001</h1>
          <span class="status-badge" id="ticketStatus">pending</span>
        </div>
        <h2 class="ticket-title" id="ticketTitle">Ticket Title</h2>
      </div>

      <!-- Main Content -->
      <div class="content-layout">
        <!-- Left Column -->
        <div class="main-content">
          <!-- Ticket Details Section -->
          <section class="card">
            <h3 class="section-title">Ticket Details</h3>
            
            <div class="detail-section">
              <h4 class="detail-label">Description</h4>
              <p class="detail-text" id="ticketDescription">
                Description text
              </p>
            </div>

            <div class="detail-grid">
              <div class="detail-item">
                <h4 class="detail-label">System</h4>
                <p class="detail-value" id="ticketSystem">System Name</p>
              </div>
              <div class="detail-item">
                <h4 class="detail-label">Category</h4>
                <p class="detail-value" id="ticketCategory">Category Name</p>
              </div>
            </div>
          </section>

          <!-- Status History Section -->
          <section class="card">
            <h3 class="section-title">Status History</h3>
            <div class="status-timeline" id="statusHistory"></div>
          </section>

          <!-- Attachments Section -->
          <section class="card">
            <h3 class="section-title">Attachments</h3>
            <div class="attachments-list" id="attachmentsList"></div>
          </section>

          <!-- Comments Section -->
          <section class="card">
            <h3 class="section-title">Comments & Updates</h3>
            <div class="comments-list" id="commentsList"></div>
          </section>
        </div>

        <!-- Right Sidebar -->
        <aside class="sidebar">
          <div class="card">
            <h3 class="section-title">Details</h3>
            
            <div class="sidebar-detail">
              <p class="sidebar-label">Submitted By</p>
              <p class="sidebar-value" id="submittedBy">John Smith</p>
              <p class="sidebar-email" id="submittedEmail">john.smith@company.com</p>
            </div>

            <div class="sidebar-detail">
              <p class="sidebar-label">Date Submitted</p>
              <p class="sidebar-value" id="dateSubmitted">Nov 16, 2024</p>
            </div>

            <div class="sidebar-detail">
              <p class="sidebar-label">Last Updated</p>
              <p class="sidebar-value" id="lastUpdated">Nov 17, 2024</p>
            </div>

            <div class="sidebar-detail">
              <p class="sidebar-label">Status</p>
              <span class="status-badge" id="sidebarStatus">pending</span>
            </div>
          </div>
        </aside>
      </div>
    </div>
  `;
}

/**
 * Get ticket detail elements
 */
export function getTicketDetailElements() {
  return {
    ticketId: document.getElementById('ticketId'),
    ticketTitle: document.getElementById('ticketTitle'),
    ticketStatus: document.getElementById('ticketStatus'),
    ticketDescription: document.getElementById('ticketDescription'),
    ticketSystem: document.getElementById('ticketSystem'),
    ticketCategory: document.getElementById('ticketCategory'),
    submittedBy: document.getElementById('submittedBy'),
    submittedEmail: document.getElementById('submittedEmail'),
    dateSubmitted: document.getElementById('dateSubmitted'),
    lastUpdated: document.getElementById('lastUpdated'),
    sidebarStatus: document.getElementById('sidebarStatus'),
    statusHistory: document.getElementById('statusHistory'),
    attachmentsList: document.getElementById('attachmentsList'),
    commentsList: document.getElementById('commentsList')
  };
}

/**
 * Render ticket header
 */
export function renderTicketHeader(ticket) {
  const elements = getTicketDetailElements();
  
  if (elements.ticketId) {
    elements.ticketId.textContent = `#${ticket.ticketId}`;
  }
  
  if (elements.ticketTitle) {
    elements.ticketTitle.textContent = ticket.title;
  }
  
  if (elements.ticketStatus) {
    const statusName = ticket.statusName || 'Unknown';
    elements.ticketStatus.textContent = statusName;
    elements.ticketStatus.className = `status-badge status-${statusName.toLowerCase().replace(' ', '-')}`;
  }
}

/**
 * Render ticket details
 */
export function renderTicketDetails(ticket) {
  const elements = getTicketDetailElements();
  
  if (elements.ticketDescription) {
    elements.ticketDescription.textContent = ticket.description;
  }
  
  if (elements.ticketSystem) {
    elements.ticketSystem.textContent = ticket.systemName || 'N/A';
  }
  
  if (elements.ticketCategory) {
    elements.ticketCategory.textContent = ticket.categoryName || 'N/A';
  }
}

/**
 * Render sidebar details
 */
export function renderSidebarDetails(ticket) {
  const elements = getTicketDetailElements();
  
  if (elements.submittedBy) {
    elements.submittedBy.textContent = ticket.author;
  }
  
  if (elements.submittedEmail) {
    const email = getEmailFromAuthor(ticket.author);
    elements.submittedEmail.textContent = email;
  }
  
  if (elements.dateSubmitted) {
    elements.dateSubmitted.textContent = formatDateTime(ticket.createdDate);
  }
  
  if (elements.lastUpdated) {
    elements.lastUpdated.textContent = formatDateTime(ticket.updatedDate);
  }
  
  if (elements.sidebarStatus) {
    const statusName = ticket.statusName || 'Unknown';
    elements.sidebarStatus.textContent = statusName;
    elements.sidebarStatus.className = `status-badge status-${statusName.toLowerCase().replace(' ', '-')}`;
  }
}

/**
 * Render complete ticket view
 */
export function renderCompleteTicketView(ticket) {
  renderTicketHeader(ticket);
  renderTicketDetails(ticket);
  renderSidebarDetails(ticket);
}

/**
 * Check if ticket number is displayed
 */
export function isTicketNumberDisplayed() {
  const ticketId = document.getElementById('ticketId');
  return ticketId && ticketId.textContent.trim() !== '';
}

/**
 * Get displayed ticket number
 */
export function getDisplayedTicketNumber() {
  const ticketId = document.getElementById('ticketId');
  return ticketId ? ticketId.textContent : null;
}

/**
 * Check if title is displayed
 */
export function isTitleDisplayed() {
  const ticketTitle = document.getElementById('ticketTitle');
  return ticketTitle && ticketTitle.textContent.trim() !== '';
}

/**
 * Get displayed title
 */
export function getDisplayedTitle() {
  const ticketTitle = document.getElementById('ticketTitle');
  return ticketTitle ? ticketTitle.textContent : null;
}

/**
 * Check if description is displayed
 */
export function isDescriptionDisplayed() {
  const description = document.getElementById('ticketDescription');
  return description && description.textContent.trim() !== '';
}

/**
 * Get displayed description
 */
export function getDisplayedDescription() {
  const description = document.getElementById('ticketDescription');
  return description ? description.textContent : null;
}

/**
 * Check if system is displayed
 */
export function isSystemDisplayed() {
  const system = document.getElementById('ticketSystem');
  return system && system.textContent.trim() !== '';
}

/**
 * Get displayed system
 */
export function getDisplayedSystem() {
  const system = document.getElementById('ticketSystem');
  return system ? system.textContent : null;
}

/**
 * Check if category is displayed
 */
export function isCategoryDisplayed() {
  const category = document.getElementById('ticketCategory');
  return category && category.textContent.trim() !== '';
}

/**
 * Get displayed category
 */
export function getDisplayedCategory() {
  const category = document.getElementById('ticketCategory');
  return category ? category.textContent : null;
}

/**
 * Check if creator is displayed
 */
export function isCreatorDisplayed() {
  const creator = document.getElementById('submittedBy');
  return creator && creator.textContent.trim() !== '';
}

/**
 * Get displayed creator
 */
export function getDisplayedCreator() {
  const creator = document.getElementById('submittedBy');
  return creator ? creator.textContent : null;
}

/**
 * Check if creation date is displayed
 */
export function isCreationDateDisplayed() {
  const date = document.getElementById('dateSubmitted');
  return date && date.textContent.trim() !== '';
}

/**
 * Get displayed creation date
 */
export function getDisplayedCreationDate() {
  const date = document.getElementById('dateSubmitted');
  return date ? date.textContent : null;
}

/**
 * Check if all required fields are displayed
 */
export function areAllRequiredFieldsDisplayed() {
  return (
    isTicketNumberDisplayed() &&
    isTitleDisplayed() &&
    isDescriptionDisplayed() &&
    isSystemDisplayed() &&
    isCategoryDisplayed() &&
    isCreatorDisplayed() &&
    isCreationDateDisplayed()
  );
}

/**
 * Check if element is read-only (no input/textarea/select)
 */
export function isElementReadOnly(elementId) {
  const element = document.getElementById(elementId);
  if (!element) return false;
  
  const tagName = element.tagName.toLowerCase();
  
  // Check if it's an input, textarea, or select element
  if (['input', 'textarea', 'select'].includes(tagName)) {
    return element.hasAttribute('readonly') || element.hasAttribute('disabled');
  }
  
  // For other elements (p, span, div), they are read-only by nature
  return ['p', 'span', 'div', 'h1', 'h2', 'h3', 'h4'].includes(tagName);
}

/**
 * Check if all fields are read-only
 */
export function areAllFieldsReadOnly() {
  const fieldIds = [
    'ticketId',
    'ticketTitle',
    'ticketDescription',
    'ticketSystem',
    'ticketCategory',
    'submittedBy',
    'dateSubmitted'
  ];
  
  return fieldIds.every(id => isElementReadOnly(id));
}

/**
 * Check if field contains editable elements
 */
export function hasEditableElements(containerId) {
  const container = document.getElementById(containerId);
  if (!container) return false;
  
  const editableElements = container.querySelectorAll('input, textarea, select, [contenteditable="true"]');
  return editableElements.length > 0;
}

/**
 * Check if page has any editable form elements
 */
export function hasAnyEditableFormElements() {
  const editableElements = document.querySelectorAll(
    'input:not([type="hidden"]):not([readonly]):not([disabled]), ' +
    'textarea:not([readonly]):not([disabled]), ' +
    'select:not([disabled]), ' +
    '[contenteditable="true"]'
  );
  return editableElements.length > 0;
}

/**
 * Get element tag name
 */
export function getElementTagName(elementId) {
  const element = document.getElementById(elementId);
  return element ? element.tagName.toLowerCase() : null;
}

/**
 * Check if element is a display element (not form input)
 */
export function isDisplayElement(elementId) {
  const tagName = getElementTagName(elementId);
  return ['p', 'span', 'div', 'h1', 'h2', 'h3', 'h4', 'label'].includes(tagName);
}

/**
 * Format date time helper
 */
function formatDateTime(dateString) {
  if (!dateString) return 'N/A';
  
  try {
    const date = new Date(dateString);
    return date.toLocaleString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric',
      hour: 'numeric',
      minute: '2-digit',
      hour12: true
    });
  } catch (error) {
    return dateString;
  }
}

/**
 * Get email from author name
 */
function getEmailFromAuthor(author) {
  if (!author) return 'user@company.com';
  const name = author.toLowerCase().replace(/\s+/g, '.');
  return `${name}@company.com`;
}

/**
 * Verify ticket data matches display
 */
export function verifyTicketDataMatchesDisplay(ticket) {
  const elements = getTicketDetailElements();
  const mismatches = [];
  
  if (elements.ticketId.textContent !== `#${ticket.ticketId}`) {
    mismatches.push('ticketId');
  }
  
  if (elements.ticketTitle.textContent !== ticket.title) {
    mismatches.push('title');
  }
  
  if (elements.ticketDescription.textContent !== ticket.description) {
    mismatches.push('description');
  }
  
  if (elements.ticketSystem.textContent !== ticket.systemName) {
    mismatches.push('system');
  }
  
  if (elements.ticketCategory.textContent !== ticket.categoryName) {
    mismatches.push('category');
  }
  
  if (elements.submittedBy.textContent !== ticket.author) {
    mismatches.push('creator');
  }
  
  return {
    matches: mismatches.length === 0,
    mismatches
  };
}

/**
 * Count visible detail fields
 */
export function countVisibleDetailFields() {
  const fields = [
    'ticketId',
    'ticketTitle',
    'ticketDescription',
    'ticketSystem',
    'ticketCategory',
    'submittedBy',
    'dateSubmitted'
  ];
  
  return fields.filter(id => {
    const element = document.getElementById(id);
    return element && element.textContent.trim() !== '';
  }).length;
}

/**
 * Check if status badge is displayed
 */
export function isStatusBadgeDisplayed() {
  const statusBadge = document.getElementById('ticketStatus');
  return statusBadge && statusBadge.textContent.trim() !== '';
}

/**
 * Get displayed status
 */
export function getDisplayedStatus() {
  const statusBadge = document.getElementById('ticketStatus');
  return statusBadge ? statusBadge.textContent : null;
}

/**
 * Mock URL parameters
 */
export function mockURLParams(ticketId) {
  delete window.URLSearchParams;
  window.URLSearchParams = class {
    constructor() {}
    get(key) {
      if (key === 'id') return ticketId;
      return null;
    }
  };
}

/**
 * Wait for async operations
 */
export function waitForAsync(ms = 0) {
  return new Promise(resolve => setTimeout(resolve, ms));
}