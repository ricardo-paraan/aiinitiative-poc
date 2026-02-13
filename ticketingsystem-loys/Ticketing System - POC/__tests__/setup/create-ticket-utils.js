/**
 * Utility functions for Create Ticket tests (US5)
 * Provides helpers for form interaction, validation, and file handling
 */

/**
 * Setup create ticket form in DOM
 */
export function setupCreateTicketForm() {
  document.body.innerHTML = `
    <div class="container">
      <form id="createTicketForm" class="ticket-form">
        <div class="form-section">
          <!-- Title Field -->
          <div class="form-group">
            <label for="title" class="form-label">
              Title <span class="required">*</span>
            </label>
            <input 
              type="text" 
              id="title" 
              name="title" 
              class="form-input" 
              placeholder="Brief description of the issue"
              maxlength="80"
              required
            />
            <div class="char-counter">
              <span id="titleCounter">0</span>/80 characters
            </div>
          </div>

          <!-- System Field -->
          <div class="form-group">
            <label for="system" class="form-label">
              System <span class="required">*</span>
            </label>
            <select id="system" name="system" class="form-select" required>
              <option value="">Select the system related to your issue</option>
            </select>
          </div>

          <!-- Category Field -->
          <div class="form-group">
            <label for="category" class="form-label">
              Category <span class="required">*</span>
            </label>
            <select id="category" name="category" class="form-select" required>
              <option value="">Select a system first</option>
            </select>
          </div>

          <!-- Description Field -->
          <div class="form-group">
            <label for="description" class="form-label">
              Description <span class="required">*</span>
            </label>
            <textarea 
              id="description" 
              name="description" 
              class="form-textarea" 
              placeholder="Provide a detailed description"
              maxlength="5000"
              rows="6"
              required
            ></textarea>
            <div class="char-counter">
              <span id="descCounter">0</span>/5000 characters
            </div>
          </div>

          <!-- Attachments Field -->
          <div class="form-group">
            <label for="attachments" class="form-label">
              Attachments (Optional)
            </label>
            <div class="file-upload-area" id="fileUploadArea">
              <input 
                type="file" 
                id="attachments" 
                name="attachments" 
                class="file-input" 
                multiple
                accept=".pdf,.doc,.docx,.png,.jpg,.jpeg,.txt,.xlsx,.xls"
              />
            </div>
            <div id="fileList" class="file-list"></div>
          </div>
        </div>

        <!-- Form Actions -->
        <div class="form-actions">
          <button type="submit" class="btn-submit" id="submitBtn">
            <span class="btn-text">Submit Ticket</span>
            <span class="btn-loading" style="display: none;">
              <span class="spinner"></span>
              Submitting...
            </span>
          </button>
          <button type="button" class="btn-cancel">Cancel</button>
        </div>
      </form>
    </div>

    <!-- Success Modal -->
    <div id="successModal" class="modal" style="display: none;">
      <div class="modal-content success-modal">
        <h2>Ticket Created Successfully!</h2>
        <p>Your ticket has been submitted and assigned ID: <strong id="ticketId"></strong></p>
      </div>
    </div>
  `;
}

/**
 * Get form elements
 */
export function getFormElements() {
  return {
    form: document.getElementById('createTicketForm'),
    titleInput: document.getElementById('title'),
    systemSelect: document.getElementById('system'),
    categorySelect: document.getElementById('category'),
    descriptionTextarea: document.getElementById('description'),
    attachmentsInput: document.getElementById('attachments'),
    submitBtn: document.getElementById('submitBtn'),
    titleCounter: document.getElementById('titleCounter'),
    descCounter: document.getElementById('descCounter'),
    fileList: document.getElementById('fileList'),
    successModal: document.getElementById('successModal'),
    ticketIdDisplay: document.getElementById('ticketId')
  };
}

/**
 * Populate system dropdown
 */
export function populateSystemDropdown(systems) {
  const systemSelect = document.getElementById('system');
  systems.forEach(system => {
    const option = document.createElement('option');
    option.value = system.systemId;
    option.textContent = system.systemName;
    systemSelect.appendChild(option);
  });
}

/**
 * Populate category dropdown
 */
export function populateCategoryDropdown(categories) {
  const categorySelect = document.getElementById('category');
  categorySelect.innerHTML = '<option value="">Select a category</option>';
  categories.forEach(category => {
    const option = document.createElement('option');
    option.value = category.categoryId;
    option.textContent = category.categoryName;
    categorySelect.appendChild(option);
  });
}

/**
 * Fill form with valid data
 */
export function fillFormWithValidData(data = {}) {
  const elements = getFormElements();
  
  elements.titleInput.value = data.title || 'Test Ticket Title';
  elements.systemSelect.value = data.systemId || '1';
  elements.categorySelect.value = data.categoryId || '1';
  elements.descriptionTextarea.value = data.description || 'This is a test description with enough characters.';
}

/**
 * Fill form with partial data (missing required fields)
 */
export function fillFormWithPartialData(missingField) {
  const elements = getFormElements();
  
  if (missingField !== 'title') {
    elements.titleInput.value = 'Test Ticket Title';
  }
  if (missingField !== 'system') {
    elements.systemSelect.value = '1';
  }
  if (missingField !== 'category') {
    elements.categorySelect.value = '1';
  }
  if (missingField !== 'description') {
    elements.descriptionTextarea.value = 'This is a test description.';
  }
}

/**
 * Clear all form fields
 */
export function clearFormFields() {
  const elements = getFormElements();
  elements.titleInput.value = '';
  elements.systemSelect.value = '';
  elements.categorySelect.value = '';
  elements.descriptionTextarea.value = '';
  elements.attachmentsInput.value = '';
  elements.fileList.innerHTML = '';
}

/**
 * Check if form fields are blank
 */
export function areFormFieldsBlank() {
  const elements = getFormElements();
  return (
    elements.titleInput.value === '' &&
    elements.systemSelect.value === '' &&
    elements.categorySelect.value === '' &&
    elements.descriptionTextarea.value === ''
  );
}

/**
 * Trigger form submission
 */
export function submitForm() {
  const form = document.getElementById('createTicketForm');
  const submitEvent = new Event('submit', { bubbles: true, cancelable: true });
  form.dispatchEvent(submitEvent);
}

/**
 * Check if submit button is in loading state
 */
export function isSubmitButtonLoading() {
  const submitBtn = document.getElementById('submitBtn');
  const btnText = submitBtn.querySelector('.btn-text');
  const btnLoading = submitBtn.querySelector('.btn-loading');
  
  return (
    btnText.style.display === 'none' &&
    btnLoading.style.display === 'flex' &&
    submitBtn.disabled === true
  );
}

/**
 * Check if success modal is visible
 */
export function isSuccessModalVisible() {
  const modal = document.getElementById('successModal');
  return modal.style.display === 'flex';
}

/**
 * Get ticket ID from success modal
 */
export function getTicketIdFromModal() {
  const ticketIdElement = document.getElementById('ticketId');
  return ticketIdElement ? ticketIdElement.textContent : null;
}

/**
 * Create mock file object
 */
export function createMockFile(name, size, type) {
  const file = new File(['a'.repeat(size)], name, { type });
  Object.defineProperty(file, 'size', { value: size });
  return file;
}

/**
 * Simulate file selection
 */
export function simulateFileSelection(files) {
  const fileInput = document.getElementById('attachments');
  const dataTransfer = new DataTransfer();
  
  files.forEach(file => {
    dataTransfer.items.add(file);
  });
  
  fileInput.files = dataTransfer.files;
  const changeEvent = new Event('change', { bubbles: true });
  fileInput.dispatchEvent(changeEvent);
}

/**
 * Get file list display
 */
export function getFileListDisplay() {
  const fileList = document.getElementById('fileList');
  return fileList.innerHTML;
}

/**
 * Count displayed files
 */
export function countDisplayedFiles() {
  const fileList = document.getElementById('fileList');
  return fileList.querySelectorAll('.file-item').length;
}

/**
 * Validate form data
 */
export function validateFormData(formData) {
  const errors = [];
  
  if (!formData.title || formData.title.length < 5) {
    errors.push('Title must be at least 5 characters');
  }
  
  if (!formData.systemId) {
    errors.push('System is required');
  }
  
  if (!formData.categoryId) {
    errors.push('Category is required');
  }
  
  if (!formData.description || formData.description.length < 10) {
    errors.push('Description must be at least 10 characters');
  }
  
  return {
    isValid: errors.length === 0,
    errors
  };
}

/**
 * Get form data as object
 */
export function getFormData() {
  const elements = getFormElements();
  return {
    title: elements.titleInput.value.trim(),
    systemId: parseInt(elements.systemSelect.value) || null,
    categoryId: parseInt(elements.categorySelect.value) || null,
    description: elements.descriptionTextarea.value.trim()
  };
}

/**
 * Check if field has required indicator
 */
export function hasRequiredIndicator(fieldId) {
  const field = document.getElementById(fieldId);
  if (!field) return false;
  
  const label = field.closest('.form-group')?.querySelector('.form-label');
  return label?.querySelector('.required') !== null;
}

/**
 * Check if field is marked as required
 */
export function isFieldRequired(fieldId) {
  const field = document.getElementById(fieldId);
  return field?.hasAttribute('required') || false;
}

/**
 * Trigger input event for character counter
 */
export function triggerInputEvent(fieldId) {
  const field = document.getElementById(fieldId);
  const inputEvent = new Event('input', { bubbles: true });
  field.dispatchEvent(inputEvent);
}

/**
 * Get character counter value
 */
export function getCharacterCount(counterId) {
  const counter = document.getElementById(counterId);
  return counter ? parseInt(counter.textContent) : 0;
}

/**
 * Check if attachments field is optional
 */
export function isAttachmentsOptional() {
  const attachmentsInput = document.getElementById('attachments');
  const label = attachmentsInput?.closest('.form-group')?.querySelector('.form-label');
  return !attachmentsInput?.hasAttribute('required') && 
         label?.textContent.includes('Optional');
}

/**
 * Wait for async operations
 */
export function waitForAsync(ms = 0) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

/**
 * Mock window.location.href
 */
export function mockWindowLocation() {
  delete window.location;
  window.location = { href: '' };
}

/**
 * Get current location href
 */
export function getCurrentLocation() {
  return window.location.href;
}