/**
 * Utility functions for Comment tests (US7)
 * Provides helpers for comment interaction, submission, and display
 */

/**
 * Setup comment section in DOM
 */
export function setupCommentSection() {
  document.body.innerHTML = `
    <div class="container">
      <!-- Ticket Header (read-only) -->
      <div class="ticket-header">
        <div class="ticket-id-status">
          <h1 class="ticket-id" id="ticketId">#001</h1>
          <span class="status-badge" id="ticketStatus">pending</span>
        </div>
        <h2 class="ticket-title" id="ticketTitle">Sample Ticket</h2>
      </div>

      <!-- Ticket Details (read-only) -->
      <section class="card">
        <h3 class="section-title">Ticket Details</h3>
        <div class="detail-section">
          <h4 class="detail-label">Description</h4>
          <p class="detail-text" id="ticketDescription">Sample description</p>
        </div>
        <div class="detail-grid">
          <div class="detail-item">
            <h4 class="detail-label">System</h4>
            <p class="detail-value" id="ticketSystem">SAP</p>
          </div>
          <div class="detail-item">
            <h4 class="detail-label">Category</h4>
            <p class="detail-value" id="ticketCategory">Technical Issue</p>
          </div>
        </div>
      </section>

      <!-- Comments Section -->
      <section class="card">
        <h3 class="section-title">Comments & Updates</h3>
        
        <div class="comments-list" id="commentsList">
          <!-- Comments will be inserted here -->
        </div>

        <!-- Add Comment Form -->
        <div class="comment-form">
          <div class="comment-input-wrapper">
            <textarea 
              id="commentInput" 
              class="comment-input" 
              placeholder="Add a comment..."
              rows="3"
              maxlength="1000"
            ></textarea>
            <div class="comment-footer">
              <span class="char-counter">
                <span id="commentCharCount">0</span>/1000 characters
              </span>
              <div class="comment-actions">
                <button class="btn-secondary" id="cancelCommentBtn">Cancel</button>
                <button class="btn-primary" id="addCommentBtn">
                  Add Comment
                </button>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  `;
}

/**
 * Get comment elements
 */
export function getCommentElements() {
  return {
    commentInput: document.getElementById('commentInput'),
    addCommentBtn: document.getElementById('addCommentBtn'),
    cancelCommentBtn: document.getElementById('cancelCommentBtn'),
    commentsList: document.getElementById('commentsList'),
    commentCharCount: document.getElementById('commentCharCount'),
    ticketId: document.getElementById('ticketId'),
    ticketTitle: document.getElementById('ticketTitle'),
    ticketDescription: document.getElementById('ticketDescription'),
    ticketSystem: document.getElementById('ticketSystem'),
    ticketCategory: document.getElementById('ticketCategory')
  };
}

/**
 * Type comment text
 */
export function typeComment(text) {
  const commentInput = document.getElementById('commentInput');
  commentInput.value = text;
  
  // Trigger input event for character counter
  const inputEvent = new Event('input', { bubbles: true });
  commentInput.dispatchEvent(inputEvent);
}

/**
 * Click add comment button
 */
export function clickAddComment() {
  const addCommentBtn = document.getElementById('addCommentBtn');
  const clickEvent = new MouseEvent('click', { bubbles: true });
  addCommentBtn.dispatchEvent(clickEvent);
}

/**
 * Click cancel button
 */
export function clickCancelComment() {
  const cancelBtn = document.getElementById('cancelCommentBtn');
  const clickEvent = new MouseEvent('click', { bubbles: true });
  cancelBtn.dispatchEvent(clickEvent);
}

/**
 * Get comment input value
 */
export function getCommentInputValue() {
  const commentInput = document.getElementById('commentInput');
  return commentInput ? commentInput.value : '';
}

/**
 * Check if comment input is empty
 */
export function isCommentInputEmpty() {
  return getCommentInputValue().trim() === '';
}

/**
 * Get character count
 */
export function getCharacterCount() {
  const charCount = document.getElementById('commentCharCount');
  return charCount ? parseInt(charCount.textContent) : 0;
}

/**
 * Render comments in the list
 */
export function renderComments(comments) {
  const commentsList = document.getElementById('commentsList');
  
  if (!comments || comments.length === 0) {
    commentsList.innerHTML = '<div class="empty-state"><p>No comments yet. Be the first to comment!</p></div>';
    return;
  }

  commentsList.innerHTML = comments.map(comment => {
    const initials = getInitials(comment.author || 'User');
    const isAdmin = comment.author && comment.author.toLowerCase().includes('admin');
    
    return `
      <div class="comment-item" data-comment-id="${comment.commentId || ''}">
        <div class="comment-avatar">${initials}</div>
        <div class="comment-content">
          <div class="comment-header">
            <span class="comment-author">${escapeHtml(comment.author || 'Anonymous')}</span>
            ${isAdmin ? '<span class="comment-badge">Admin</span>' : ''}
            <span class="comment-date">${formatDateTime(comment.createdDate || comment.commentDate)}</span>
          </div>
          <p class="comment-text">${escapeHtml(comment.commentText || comment.comments || '')}</p>
        </div>
      </div>
    `;
  }).join('');
}

/**
 * Get displayed comments count
 */
export function getDisplayedCommentsCount() {
  const commentsList = document.getElementById('commentsList');
  const commentItems = commentsList.querySelectorAll('.comment-item');
  return commentItems.length;
}

/**
 * Get all displayed comments
 */
export function getDisplayedComments() {
  const commentsList = document.getElementById('commentsList');
  const commentItems = commentsList.querySelectorAll('.comment-item');
  
  return Array.from(commentItems).map(item => {
    const author = item.querySelector('.comment-author')?.textContent || '';
    const text = item.querySelector('.comment-text')?.textContent || '';
    const date = item.querySelector('.comment-date')?.textContent || '';
    
    return { author, text, date };
  });
}

/**
 * Check if comment is displayed
 */
export function isCommentDisplayed(commentText) {
  const comments = getDisplayedComments();
  return comments.some(comment => comment.text === commentText);
}

/**
 * Get latest displayed comment
 */
export function getLatestComment() {
  const comments = getDisplayedComments();
  return comments.length > 0 ? comments[comments.length - 1] : null;
}

/**
 * Check if empty state is shown
 */
export function isEmptyStateShown() {
  const commentsList = document.getElementById('commentsList');
  return commentsList.querySelector('.empty-state') !== null;
}

/**
 * Check if add comment button is enabled
 */
export function isAddCommentButtonEnabled() {
  const addCommentBtn = document.getElementById('addCommentBtn');
  return addCommentBtn && !addCommentBtn.disabled;
}

/**
 * Check if add comment button is disabled
 */
export function isAddCommentButtonDisabled() {
  const addCommentBtn = document.getElementById('addCommentBtn');
  return addCommentBtn && addCommentBtn.disabled;
}

/**
 * Clear comment input
 */
export function clearCommentInput() {
  const commentInput = document.getElementById('commentInput');
  if (commentInput) {
    commentInput.value = '';
    const inputEvent = new Event('input', { bubbles: true });
    commentInput.dispatchEvent(inputEvent);
  }
}

/**
 * Check if ticket details are read-only
 */
export function areTicketDetailsReadOnly() {
  const ticketFields = ['ticketId', 'ticketTitle', 'ticketDescription', 'ticketSystem', 'ticketCategory'];
  
  return ticketFields.every(fieldId => {
    const element = document.getElementById(fieldId);
    if (!element) return false;
    
    const tagName = element.tagName.toLowerCase();
    // Check if it's a display element (not input/textarea/select)
    return ['p', 'span', 'div', 'h1', 'h2', 'h3', 'h4'].includes(tagName);
  });
}

/**
 * Check if ticket field is editable
 */
export function isTicketFieldEditable(fieldId) {
  const element = document.getElementById(fieldId);
  if (!element) return false;
  
  const tagName = element.tagName.toLowerCase();
  const editableTags = ['input', 'textarea', 'select'];
  
  if (editableTags.includes(tagName)) {
    return !element.hasAttribute('readonly') && !element.hasAttribute('disabled');
  }
  
  return element.hasAttribute('contenteditable') && element.getAttribute('contenteditable') === 'true';
}

/**
 * Verify comment data matches display
 */
export function verifyCommentMatchesDisplay(comment, displayedComment) {
  return (
    displayedComment.author === comment.author &&
    displayedComment.text === comment.commentText
  );
}

/**
 * Get comment by index
 */
export function getCommentByIndex(index) {
  const comments = getDisplayedComments();
  return comments[index] || null;
}

/**
 * Check if comment input has placeholder
 */
export function hasCommentPlaceholder() {
  const commentInput = document.getElementById('commentInput');
  return commentInput && commentInput.hasAttribute('placeholder');
}

/**
 * Get comment input placeholder text
 */
export function getCommentPlaceholder() {
  const commentInput = document.getElementById('commentInput');
  return commentInput ? commentInput.getAttribute('placeholder') : '';
}

/**
 * Check if comment input has max length
 */
export function hasCommentMaxLength() {
  const commentInput = document.getElementById('commentInput');
  return commentInput && commentInput.hasAttribute('maxlength');
}

/**
 * Get comment input max length
 */
export function getCommentMaxLength() {
  const commentInput = document.getElementById('commentInput');
  return commentInput ? parseInt(commentInput.getAttribute('maxlength')) : 0;
}

/**
 * Check if character counter is visible
 */
export function isCharacterCounterVisible() {
  const charCounter = document.querySelector('.char-counter');
  return charCounter !== null;
}

/**
 * Simulate typing with delay (for realistic testing)
 */
export async function typeCommentWithDelay(text, delayMs = 10) {
  const commentInput = document.getElementById('commentInput');
  
  for (let i = 0; i < text.length; i++) {
    commentInput.value = text.substring(0, i + 1);
    const inputEvent = new Event('input', { bubbles: true });
    commentInput.dispatchEvent(inputEvent);
    
    if (delayMs > 0) {
      await new Promise(resolve => setTimeout(resolve, delayMs));
    }
  }
}

/**
 * Check if cancel button is visible
 */
export function isCancelButtonVisible() {
  const cancelBtn = document.getElementById('cancelCommentBtn');
  return cancelBtn !== null && cancelBtn.offsetParent !== null;
}

/**
 * Check if add comment button is visible
 */
export function isAddCommentButtonVisible() {
  const addBtn = document.getElementById('addCommentBtn');
  return addBtn !== null && addBtn.offsetParent !== null;
}

/**
 * Get comment form elements
 */
export function getCommentFormElements() {
  return {
    input: document.getElementById('commentInput'),
    addButton: document.getElementById('addCommentBtn'),
    cancelButton: document.getElementById('cancelCommentBtn'),
    charCounter: document.getElementById('commentCharCount')
  };
}

/**
 * Check if comment form is visible
 */
export function isCommentFormVisible() {
  const commentForm = document.querySelector('.comment-form');
  return commentForm !== null;
}

/**
 * Helper: Get initials from name
 */
function getInitials(name) {
  if (!name) return 'U';
  const parts = name.trim().split(' ');
  if (parts.length === 1) return parts[0].charAt(0).toUpperCase();
  return (parts[0].charAt(0) + parts[parts.length - 1].charAt(0)).toUpperCase();
}

/**
 * Helper: Escape HTML
 */
function escapeHtml(text) {
  const div = document.createElement('div');
  div.textContent = text;
  return div.innerHTML;
}

/**
 * Helper: Format date time
 */
function formatDateTime(dateString) {
  if (!dateString) return 'Just now';
  
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
 * Wait for async operations
 */
export function waitForAsync(ms = 0) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

/**
 * Create mock comment object
 */
export function createMockComment(overrides = {}) {
  return {
    commentId: 1,
    ticketId: 1,
    author: 'Test User',
    commentText: 'This is a test comment',
    createdDate: new Date().toISOString(),
    ...overrides
  };
}

/**
 * Verify comment submission flow
 */
export async function verifyCommentSubmissionFlow(commentText, mockCreateComment) {
  // Type comment
  typeComment(commentText);
  
  // Click add button
  clickAddComment();
  
  // Wait for async operation
  await waitForAsync(10);
  
  // Verify API was called
  return mockCreateComment.mock.calls.length > 0;
}