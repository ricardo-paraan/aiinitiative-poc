const API_BASE_URL = 'http://localhost:8080/api';
let allTickets = [];

// Fetch all tickets on page load
window.addEventListener('DOMContentLoaded', () => {
    fetchTickets();
    
    // Add search functionality
    document.getElementById('searchInput').addEventListener('input', (e) => {
        const searchTerm = e.target.value.toLowerCase();
        const filtered = allTickets.filter(ticket =>
            ticket.title?.toLowerCase().includes(searchTerm) ||
            ticket.author?.toLowerCase().includes(searchTerm) ||
            ticket.status?.toLowerCase().includes(searchTerm)
        );
        renderTickets(filtered);
    });
});

// Fetch all tickets from API
async function fetchTickets() {
    showLoading(true);
    hideError();
    
    try {
        const response = await fetch(`${API_BASE_URL}/tickets`);
        if (!response.ok) {
            throw new Error('Failed to fetch tickets');
        }
        allTickets = await response.json();
        renderTickets(allTickets);
        showLoading(false);
    } catch (error) {
        console.error('Error:', error);
        showError('Failed to load tickets. Make sure the backend is running on http://localhost:8080');
        showLoading(false);
        showEmptyState(true, 'Unable to connect to the server.');
    }
}

// Render tickets to the grid
function renderTickets(tickets) {
    const grid = document.getElementById('ticketsGrid');
    const count = document.getElementById('ticketCount');
    
    count.textContent = tickets.length;
    
    if (tickets.length === 0) {
        grid.innerHTML = '';
        showEmptyState(true, 'No tickets found. Try adjusting your search.');
        return;
    }
    
    showEmptyState(false);
    grid.innerHTML = tickets.map(ticket => createTicketCard(ticket)).join('');
    
    // Add event listeners to delete buttons
    document.querySelectorAll('.btn-delete').forEach(button => {
        button.addEventListener('click', () => deleteTicket(button.dataset.id));
    });
}

// Create HTML for a single ticket card
function createTicketCard(ticket) {
    const statusClass = getStatusClass(ticket.status);
    
    return `
        <div class="ticket-card">
            <div class="ticket-header">
                <span class="ticket-id">#${ticket.ticketId || 'N/A'}</span>
                <span class="ticket-status ${statusClass}">
                    ${ticket.status || 'N/A'}
                </span>
            </div>
            <h3 class="ticket-title">${escapeHtml(ticket.title) || 'Untitled'}</h3>
            <div class="ticket-details">
                <div class="detail-row">
                    <span class="detail-label">👤 Author:</span>
                    <span class="detail-value">${escapeHtml(ticket.author) || 'Unknown'}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">💻 System:</span>
                    <span class="detail-value">${escapeHtml(ticket.systemName) || 'N/A'}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">📂 Category:</span>
                    <span class="detail-value">${escapeHtml(ticket.category) || 'N/A'}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">📅 Created:</span>
                    <span class="detail-value">${ticket.createdDate || 'N/A'}</span>
                </div>
                ${ticket.description ? `
                <div class="ticket-description">
                    <p>${escapeHtml(ticket.description)}</p>
                </div>
                ` : ''}
            </div>
            <div class="ticket-actions">
                <button class="btn-view">View Details</button>
                <button class="btn-delete" data-id="${ticket.ticketId}">Delete</button>
            </div>
        </div>
    `;
}

// Get CSS class for status badge
function getStatusClass(status) {
    if (!status) return 'status-default';
    const statusLower = status.toLowerCase();
    if (statusLower === 'open' || statusLower === 'new') return 'status-open';
    if (statusLower === 'in progress' || statusLower === 'pending') return 'status-progress';
    if (statusLower === 'closed' || statusLower === 'resolved') return 'status-closed';
    return 'status-default';
}

// Delete a ticket
async function deleteTicket(id) {
    if (!confirm('Are you sure you want to delete this ticket?')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/tickets/${id}`, {
            method: 'DELETE'
        });
        
        if (!response.ok) {
            throw new Error('Failed to delete ticket');
        }
        
        // Refresh the tickets list
        fetchTickets();
    } catch (error) {
        console.error('Error:', error);
        showError('Failed to delete ticket. Please try again.');
    }
}

// Show/hide loading spinner
function showLoading(show) {
    const loading = document.getElementById('loading');
    if (show) {
        loading.classList.add('show');
    } else {
        loading.classList.remove('show');
    }
}

// Show error message
function showError(message) {
    const banner = document.getElementById('errorBanner');
    const messageSpan = document.getElementById('errorMessage');
    messageSpan.textContent = message;
    banner.classList.add('show');
}

// Hide error message
function hideError() {
    const banner = document.getElementById('errorBanner');
    banner.classList.remove('show');
}

// Show/hide empty state
function showEmptyState(show, message) {
    const emptyState = document.getElementById('emptyState');
    const emptyMessage = document.getElementById('emptyMessage');
    
    if (show) {
        emptyMessage.textContent = message || 'No tickets available.';
        emptyState.classList.add('show');
    } else {
        emptyState.classList.remove('show');
    }
}

// Escape HTML to prevent XSS
function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}