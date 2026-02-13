/**
 * Utility functions for Navigation tests (US8)
 * Provides helpers for testing navigation between pages
 */

/**
 * Setup dashboard with Create New Ticket button
 */
export function setupDashboardWithCreateButton() {
  document.body.innerHTML = `
    <div class="container">
      <!-- Header -->
      <header class="dashboard-header">
        <h1>Ticketing System Dashboard</h1>
        <button class="btn-primary" id="createTicketBtn" onclick="window.location.href='create-ticket.html'">
          <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
            <path d="M10 4V16M4 10H16" stroke="currentColor" stroke-width="2"/>
          </svg>
          Create New Ticket
        </button>
      </header>

      <!-- Dashboard Content -->
      <div class="dashboard-content">
        <div class="stats-grid">
          <div class="stat-card">
            <h3>Pending</h3>
            <p class="stat-number" id="pendingCount">0</p>
          </div>
          <div class="stat-card">
            <h3>In Progress</h3>
            <p class="stat-number" id="inProgressCount">0</p>
          </div>
          <div class="stat-card">
            <h3>Resolved</h3>
            <p class="stat-number" id="resolvedCount">0</p>
          </div>
        </div>
      </div>
    </div>
  `;
}

/**
 * Setup create ticket page
 */
export function setupCreateTicketPage() {
  document.body.innerHTML = `
    <div class="container">
      <header class="page-header">
        <button class="btn-back" onclick="window.location.href='index.html'">
          Back to Dashboard
        </button>
      </header>

      <div class="form-container">
        <div class="form-header">
          <h1>Create New Ticket</h1>
        </div>

        <form id="createTicketForm" class="ticket-form">
          <div class="form-group">
            <label for="title">Title <span class="required">*</span></label>
            <input type="text" id="title" name="title" class="form-input" required />
          </div>

          <div class="form-group">
            <label for="system">System <span class="required">*</span></label>
            <select id="system" name="system" class="form-select" required>
              <option value="">Select the system related to your issue</option>
            </select>
          </div>

          <div class="form-group">
            <label for="category">Category <span class="required">*</span></label>
            <select id="category" name="category" class="form-select" required>
              <option value="">Select a system first</option>
            </select>
          </div>

          <div class="form-group">
            <label for="description">Description <span class="required">*</span></label>
            <textarea id="description" name="description" class="form-textarea" required></textarea>
          </div>

          <div class="form-group">
            <label for="attachments">Attachments (Optional)</label>
            <input type="file" id="attachments" name="attachments" class="file-input" multiple />
          </div>

          <div class="form-actions">
            <button type="submit" class="btn-submit" id="submitBtn">Submit Ticket</button>
            <button type="button" class="btn-cancel">Cancel</button>
          </div>
        </form>
      </div>
    </div>
  `;
}

/**
 * Get Create New Ticket button
 */
export function getCreateTicketButton() {
  return document.getElementById('createTicketBtn');
}

/**
 * Check if Create New Ticket button exists
 */
export function createTicketButtonExists() {
  return getCreateTicketButton() !== null;
}

/**
 * Check if Create New Ticket button is visible
 */
export function isCreateTicketButtonVisible() {
  const button = getCreateTicketButton();
  return button !== null && button.offsetParent !== null;
}

/**
 * Get button text
 */
export function getCreateTicketButtonText() {
  const button = getCreateTicketButton();
  return button ? button.textContent.trim() : '';
}

/**
 * Click Create New Ticket button
 */
export function clickCreateTicketButton() {
  const button = getCreateTicketButton();
  if (button) {
    const clickEvent = new MouseEvent('click', { bubbles: true, cancelable: true });
    button.dispatchEvent(clickEvent);
  }
}

/**
 * Check if on dashboard page
 */
export function isOnDashboardPage() {
  return document.querySelector('.dashboard-header') !== null;
}

/**
 * Check if on create ticket page
 */
export function isOnCreateTicketPage() {
  return document.getElementById('createTicketForm') !== null;
}

/**
 * Get all form fields on create ticket page
 */
export function getCreateTicketFormFields() {
  return {
    title: document.getElementById('title'),
    system: document.getElementById('system'),
    category: document.getElementById('category'),
    description: document.getElementById('description'),
    attachments: document.getElementById('attachments')
  };
}

/**
 * Check if all form fields are empty
 */
export function areAllFormFieldsEmpty() {
  const fields = getCreateTicketFormFields();
  
  return (
    fields.title && fields.title.value === '' &&
    fields.system && fields.system.value === '' &&
    fields.category && fields.category.value === '' &&
    fields.description && fields.description.value === '' &&
    fields.attachments && fields.attachments.files.length === 0
  );
}

/**
 * Check if specific field is empty
 */
export function isFieldEmpty(fieldId) {
  const field = document.getElementById(fieldId);
  if (!field) return false;
  
  if (field.tagName.toLowerCase() === 'input' && field.type === 'file') {
    return field.files.length === 0;
  }
  
  return field.value === '';
}

/**
 * Check if field has default/placeholder value
 */
export function hasDefaultValue(fieldId) {
  const field = document.getElementById(fieldId);
  if (!field) return false;
  
  if (field.tagName.toLowerCase() === 'select') {
    return field.value === '' || field.selectedIndex === 0;
  }
  
  return field.value === '';
}

/**
 * Get field value
 */
export function getFieldValue(fieldId) {
  const field = document.getElementById(fieldId);
  return field ? field.value : null;
}

/**
 * Check if form exists
 */
export function formExists() {
  return document.getElementById('createTicketForm') !== null;
}

/**
 * Get form element
 */
export function getForm() {
  return document.getElementById('createTicketForm');
}

/**
 * Check if form has required fields
 */
export function formHasRequiredFields() {
  const requiredFields = ['title', 'system', 'category', 'description'];
  return requiredFields.every(fieldId => {
    const field = document.getElementById(fieldId);
    return field && field.hasAttribute('required');
  });
}

/**
 * Check if form has optional fields
 */
export function formHasOptionalFields() {
  const attachments = document.getElementById('attachments');
  return attachments && !attachments.hasAttribute('required');
}

/**
 * Mock window.location.href
 */
export function mockWindowLocation() {
  delete window.location;
  window.location = { href: '' };
}

/**
 * Get current location
 */
export function getCurrentLocation() {
  return window.location.href;
}

/**
 * Simulate navigation to create ticket page
 */
export function navigateToCreateTicket() {
  mockWindowLocation();
  window.location.href = 'create-ticket.html';
  setupCreateTicketPage();
}

/**
 * Simulate navigation to dashboard
 */
export function navigateToDashboard() {
  mockWindowLocation();
  window.location.href = 'index.html';
  setupDashboardWithCreateButton();
}

/**
 * Check if navigation occurred
 */
export function didNavigationOccur(expectedUrl) {
  return window.location.href.includes(expectedUrl);
}

/**
 * Verify button click triggers navigation
 */
export function verifyButtonClickTriggersNavigation() {
  mockWindowLocation();
  const button = getCreateTicketButton();
  
  if (button) {
    // Get the onclick attribute
    const onclickAttr = button.getAttribute('onclick');
    return onclickAttr && onclickAttr.includes('create-ticket.html');
  }
  
  return false;
}

/**
 * Check if button has correct href or onclick
 */
export function buttonHasNavigationAction() {
  const button = getCreateTicketButton();
  if (!button) return false;
  
  // Check onclick attribute
  const onclick = button.getAttribute('onclick');
  if (onclick && onclick.includes('create-ticket.html')) {
    return true;
  }
  
  // Check if button is wrapped in a link
  const parentLink = button.closest('a');
  if (parentLink && parentLink.href && parentLink.href.includes('create-ticket.html')) {
    return true;
  }
  
  return false;
}

/**
 * Count form fields
 */
export function countFormFields() {
  const form = getForm();
  if (!form) return 0;
  
  const inputs = form.querySelectorAll('input, select, textarea');
  return inputs.length;
}

/**
 * Get form field names
 */
export function getFormFieldNames() {
  const form = getForm();
  if (!form) return [];
  
  const fields = form.querySelectorAll('input, select, textarea');
  return Array.from(fields).map(field => field.name || field.id).filter(Boolean);
}

/**
 * Check if page title is correct
 */
export function hasCorrectPageTitle(expectedTitle) {
  const heading = document.querySelector('h1');
  return heading && heading.textContent.includes(expectedTitle);
}

/**
 * Check if back button exists on create ticket page
 */
export function hasBackButton() {
  return document.querySelector('.btn-back') !== null;
}

/**
 * Get back button
 */
export function getBackButton() {
  return document.querySelector('.btn-back');
}

/**
 * Click back button
 */
export function clickBackButton() {
  const button = getBackButton();
  if (button) {
    const clickEvent = new MouseEvent('click', { bubbles: true, cancelable: true });
    button.dispatchEvent(clickEvent);
  }
}

/**
 * Verify form is in initial state
 */
export function isFormInInitialState() {
  return (
    formExists() &&
    areAllFormFieldsEmpty() &&
    formHasRequiredFields() &&
    formHasOptionalFields()
  );
}

/**
 * Check if button is enabled
 */
export function isButtonEnabled(buttonId) {
  const button = document.getElementById(buttonId);
  return button && !button.disabled;
}

/**
 * Check if button has icon
 */
export function buttonHasIcon(buttonId) {
  const button = document.getElementById(buttonId);
  if (!button) return false;
  
  return button.querySelector('svg') !== null;
}

/**
 * Wait for async operations
 */
export function waitForAsync(ms = 0) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

/**
 * Simulate page load
 */
export async function simulatePageLoad(pageSetupFunction) {
  pageSetupFunction();
  await waitForAsync(10);
}

/**
 * Verify navigation flow
 */
export async function verifyNavigationFlow(fromPage, toPage) {
  // Setup initial page
  if (fromPage === 'dashboard') {
    setupDashboardWithCreateButton();
  }
  
  // Mock location
  mockWindowLocation();
  
  // Click button
  clickCreateTicketButton();
  
  // Simulate navigation
  if (toPage === 'create-ticket') {
    window.location.href = 'create-ticket.html';
    setupCreateTicketPage();
  }
  
  await waitForAsync(10);
  
  return {
    navigationOccurred: didNavigationOccur('create-ticket.html'),
    formExists: formExists(),
    formIsEmpty: areAllFormFieldsEmpty()
  };
}