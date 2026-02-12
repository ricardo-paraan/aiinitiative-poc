const API_BASE_URL = 'http://localhost:8081/api';
let currentTicketId = null;

// Get ticket ID from URL parameters
function getTicketIdFromUrl() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('id');
}

// Load ticket details
async function loadTicketDetails(ticketId) {
    try {
        const response = await fetch(`${API_BASE_URL}/tickets/${ticketId}`);
        
        if (!response.ok) {
            throw new Error('Ticket not found');
        }
        
        const ticket = await response.json();
        
        // Populate form fields
        document.getElementById('title').value = ticket.title || '';
        document.getElementById('author').value = ticket.author || '';
        document.getElementById('systemName').value = ticket.systemName || '';
        document.getElementById('category').value = ticket.category || '';
        document.getElementById('description').value = ticket.description || '';
        document.getElementById('status').value = ticket.status || 'Open';
        document.getElementById('statusComment').value = ticket.statusComment || '';
        
    } catch (error) {
        console.error('Error loading ticket:', error);
        showError('Failed to load ticket details. Redirecting to dashboard...');
        setTimeout(() => {
            window.location.href = 'index.html';
        }, 2000);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    currentTicketId = getTicketIdFromUrl();
    
    if (!currentTicketId) {
        showError('No ticket ID provided. Redirecting to dashboard...');
        setTimeout(() => {
            window.location.href = 'index.html';
        }, 2000);
        return;
    }
    
    // Load ticket details
    loadTicketDetails(currentTicketId);
    
    // Load status history
    loadStatusHistory(currentTicketId);
    
    // Load comments
    loadComments(currentTicketId);
    
    // Attach form submit handler
    const form = document.getElementById('createTicketForm');
    form.addEventListener('submit', handleSubmit);
    
    // Attach comment form submit handler
    const commentForm = document.getElementById('addCommentForm');
    commentForm.addEventListener('submit', handleCommentSubmit);
});

async function handleSubmit(e) {
    e.preventDefault();
    
    // Get form data
    const formData = {
        title: document.getElementById('title').value.trim(),
        author: document.getElementById('author').value.trim(),
        status: document.getElementById('status').value,
        systemName: document.getElementById('systemName').value,
        category: document.getElementById('category').value,
        description: document.getElementById('description').value.trim() || null,
        statusComment: document.getElementById('statusComment').value.trim() || null
    };
    
    // Validate required fields
    if (!formData.title || !formData.author || !formData.status || !formData.systemName || !formData.category) {
        showError('Please fill in all required fields (Title, Author, Status, System, and Category)');
        return;
    }
    
    try {
        // Disable submit button
        const submitBtn = document.querySelector('button[type="submit"]');
        submitBtn.disabled = true;
        submitBtn.textContent = 'Updating...';
        
        // Send PUT request
        const response = await fetch(`${API_BASE_URL}/tickets/${currentTicketId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });
        
        if (!response.ok) {
            const errorData = await response.text();
            throw new Error(errorData || 'Failed to update ticket');
        }
        
        const updatedTicket = await response.json();
        
        // Show success message
        showSuccess(`Ticket #${updatedTicket.ticketId} updated successfully! Redirecting to dashboard...`);
        
        // Redirect to dashboard after 2 seconds
        setTimeout(() => {
            window.location.href = 'index.html';
        }, 2000);
        
    } catch (error) {
        console.error('Error:', error);
        showError(`Failed to update ticket: ${error.message}. Please try again.`);
        
        // Re-enable submit button
        const submitBtn = document.querySelector('button[type="submit"]');
        submitBtn.disabled = false;
        submitBtn.textContent = 'Update Ticket';
    }
}

function showSuccess(message) {
    const banner = document.getElementById('successBanner');
    const messageSpan = document.getElementById('successMessage');
    messageSpan.textContent = message;
    banner.classList.add('show');
    hideError();
    
    // Scroll to top to see the message
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

function showError(message) {
    const banner = document.getElementById('errorBanner');
    const messageSpan = document.getElementById('errorMessage');
    messageSpan.textContent = message;
    banner.classList.add('show');
    
    // Scroll to top to see the message
    window.scrollTo({ top: 0, behavior: 'smooth' });
    
    // Auto-hide after 5 seconds
    setTimeout(() => {
        banner.classList.remove('show');
    }, 5000);
}

function hideError() {
    const banner = document.getElementById('errorBanner');
    banner.classList.remove('show');
}

// Load comments for the ticket
async function loadComments(ticketId) {
    try {
        const response = await fetch(`${API_BASE_URL}/comments/ticket/${ticketId}`);
        
        if (!response.ok) {
            throw new Error('Failed to load comments');
        }
        
        const comments = await response.json();
        displayComments(comments);
        
    } catch (error) {
        console.error('Error loading comments:', error);
        document.getElementById('commentsList').innerHTML = 
            '<p class="no-comments">Unable to load comments.</p>';
    }
}

// Display comments
function displayComments(comments) {
    const commentsList = document.getElementById('commentsList');
    
    if (comments.length === 0) {
        commentsList.innerHTML = '<p class="no-comments">No comments yet. Be the first to comment!</p>';
        return;
    }
    
    commentsList.innerHTML = comments.map(comment => `
        <div class="comment-item">
            <div class="comment-header">
                <strong class="comment-author">${escapeHtml(comment.author || 'Anonymous')}</strong>
                <span class="comment-date">${formatDate(comment.createdAt)}</span>
            </div>
            <div class="comment-body">
                ${escapeHtml(comment.comments)}
            </div>
        </div>
    `).join('');
}

// Handle comment form submission
async function handleCommentSubmit(e) {
    e.preventDefault();
    
    const commentText = document.getElementById('commentText').value.trim();
    const commentAuthor = document.getElementById('commentAuthor').value.trim();
    
    if (!commentText || !commentAuthor) {
        showError('Please fill in both comment and author name.');
        return;
    }
    
    try {
        const submitBtn = e.target.querySelector('button[type="submit"]');
        submitBtn.disabled = true;
        submitBtn.textContent = 'Posting...';
        
        const commentData = {
            ticket: {
                ticketId: parseInt(currentTicketId)
            },
            comments: commentText,
            author: commentAuthor
        };
        
        const response = await fetch(`${API_BASE_URL}/comments`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(commentData)
        });
        
        if (!response.ok) {
            throw new Error('Failed to post comment');
        }
        
        // Clear form
        document.getElementById('commentText').value = '';
        document.getElementById('commentAuthor').value = '';
        
        // Reload comments
        await loadComments(currentTicketId);
        
        showSuccess('Comment posted successfully!');
        
        // Re-enable button
        submitBtn.disabled = false;
        submitBtn.textContent = 'Post Comment';
        
        // Hide success message after 3 seconds
        setTimeout(() => {
            const banner = document.getElementById('successBanner');
            banner.classList.remove('show');
        }, 3000);
        
    } catch (error) {
        console.error('Error posting comment:', error);
        showError('Failed to post comment. Please try again.');
        
        const submitBtn = e.target.querySelector('button[type="submit"]');
        submitBtn.disabled = false;
        submitBtn.textContent = 'Post Comment';
    }
}

// Load status history for the ticket
async function loadStatusHistory(ticketId) {
    try {
        const response = await fetch(`${API_BASE_URL}/status-history/ticket/${ticketId}`);
        
        if (!response.ok) {
            throw new Error('Failed to load status history');
        }
        
        const history = await response.json();
        displayStatusHistory(history);
        
    } catch (error) {
        console.error('Error loading status history:', error);
        document.getElementById('statusHistoryList').innerHTML = 
            '<p class="no-history">Unable to load status history.</p>';
    }
}

// Display status history
function displayStatusHistory(history) {
    const historyList = document.getElementById('statusHistoryList');
    
    if (history.length === 0) {
        historyList.innerHTML = '<p class="no-history">No status changes yet.</p>';
        return;
    }
    
    historyList.innerHTML = history.map(entry => `
        <div class="history-item">
            <div class="history-header">
                <span class="history-status status-badge status-${entry.status.toLowerCase().replace(/\s+/g, '-')}">${escapeHtml(entry.status)}</span>
                <span class="history-date">${formatDate(entry.changedAt)}</span>
            </div>
            <div class="history-body">
                ${entry.statusComment ? `<p class="history-comment">${escapeHtml(entry.statusComment)}</p>` : '<p class="history-comment"><em>No comment</em></p>'}
                <p class="history-author">Changed by: <strong>${escapeHtml(entry.changedBy || 'Unknown')}</strong></p>
            </div>
        </div>
    `).join('');
}

// Helper function to escape HTML
function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Helper function to format date
function formatDate(dateString) {
    if (!dateString) return 'Unknown date';
    
    const date = new Date(dateString);
    const now = new Date();
    const diffMs = now - date;
    const diffMins = Math.floor(diffMs / 60000);
    const diffHours = Math.floor(diffMs / 3600000);
    const diffDays = Math.floor(diffMs / 86400000);
    
    if (diffMins < 1) return 'Just now';
    if (diffMins < 60) return `${diffMins} minute${diffMins > 1 ? 's' : ''} ago`;
    if (diffHours < 24) return `${diffHours} hour${diffHours > 1 ? 's' : ''} ago`;
    if (diffDays < 7) return `${diffDays} day${diffDays > 1 ? 's' : ''} ago`;
    
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
}
