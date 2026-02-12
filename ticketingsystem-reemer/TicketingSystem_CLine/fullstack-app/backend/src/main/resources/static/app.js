const API_BASE_URL = 'http://localhost:8081/api';
let allTickets = [];
let filteredTickets = [];
let currentPage = 1;
const itemsPerPage = 9;
let currentView = 'grid';

// Initialize on page load
window.addEventListener('DOMContentLoaded', () => {
    fetchTickets();
    setupEventListeners();
});

function setupEventListeners() {
    // Search
    document.getElementById('searchInput').addEventListener('input', applyFiltersAndSort);
    
    // Filters
    document.getElementById('statusFilter').addEventListener('change', applyFiltersAndSort);
    document.getElementById('systemFilter').addEventListener('change', applyFiltersAndSort);
    document.getElementById('categoryFilter').addEventListener('change', applyFiltersAndSort);
    
    // Sort
    document.getElementById('sortBy').addEventListener('change', applyFiltersAndSort);
    
    // View toggle
    document.querySelectorAll('.view-btn').forEach(btn => {
        btn.addEventListener('click', (e) => switchView(e.currentTarget.dataset.view));
    });
    
    // Pagination
    document.getElementById('prevPage').addEventListener('click', () => changePage(currentPage - 1));
    document.getElementById('nextPage').addEventListener('click', () => changePage(currentPage + 1));
}

// Fetch tickets from API
async function fetchTickets() {
    showLoading(true);
    hideError();
    
    try {
        const response = await fetch(`${API_BASE_URL}/tickets`);
        if (!response.ok) throw new Error('Failed to fetch tickets');
        
        allTickets = await response.json();
        
        // Fetch comments for all tickets
        await fetchCommentsForTickets();
        
        populateFilters();
        applyFiltersAndSort();
        showLoading(false);
    } catch (error) {
        console.error('Error:', error);
        showError('Failed to load tickets. Make sure the backend is running on http://localhost:8081');
        showLoading(false);
        showEmptyState(true, 'Unable to connect to the server.');
    }
}

// Fetch comments for all tickets
async function fetchCommentsForTickets() {
    try {
        const commentPromises = allTickets.map(ticket => 
            fetch(`${API_BASE_URL}/comments/ticket/${ticket.ticketId}`)
                .then(response => response.ok ? response.json() : [])
                .catch(() => [])
        );
        
        const commentsArrays = await Promise.all(commentPromises);
        
        // Attach comments to each ticket
        allTickets.forEach((ticket, index) => {
            ticket.comments = commentsArrays[index] || [];
        });
    } catch (error) {
        console.error('Error fetching comments:', error);
        // Continue without comments
        allTickets.forEach(ticket => {
            ticket.comments = [];
        });
    }
}

// Populate filter dropdowns
function populateFilters() {
    const systems = [...new Set(allTickets.map(t => t.systemName).filter(Boolean))];
    const categories = [...new Set(allTickets.map(t => t.category).filter(Boolean))];
    
    const systemFilter = document.getElementById('systemFilter');
    const categoryFilter = document.getElementById('categoryFilter');
    
    systems.forEach(system => {
        const option = document.createElement('option');
        option.value = system;
        option.textContent = system;
        systemFilter.appendChild(option);
    });
    
    categories.forEach(category => {
        const option = document.createElement('option');
        option.value = category;
        option.textContent = category;
        categoryFilter.appendChild(option);
    });
}

// Apply filters and sorting
function applyFiltersAndSort() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    const statusFilter = document.getElementById('statusFilter').value.toLowerCase();
    const systemFilter = document.getElementById('systemFilter').value;
    const categoryFilter = document.getElementById('categoryFilter').value;
    const sortBy = document.getElementById('sortBy').value;
    
    // Filter
    filteredTickets = allTickets.filter(ticket => {
        const matchesSearch = !searchTerm || 
            ticket.title?.toLowerCase().includes(searchTerm) ||
            ticket.author?.toLowerCase().includes(searchTerm) ||
            String(ticket.ticketId).includes(searchTerm);
        
        const matchesStatus = !statusFilter || ticket.status?.toLowerCase() === statusFilter;
        const matchesSystem = !systemFilter || ticket.systemName === systemFilter;
        const matchesCategory = !categoryFilter || ticket.category === categoryFilter;
        
        return matchesSearch && matchesStatus && matchesSystem && matchesCategory;
    });
    
    // Sort
    filteredTickets.sort((a, b) => {
        const [field, order] = sortBy.split('-');
        let comparison = 0;
        
        if (field === 'id') {
            comparison = a.ticketId - b.ticketId;
        } else if (field === 'title') {
            comparison = (a.title || '').localeCompare(b.title || '');
        } else if (field === 'status') {
            comparison = (a.status || '').localeCompare(b.status || '');
        }
        
        return order === 'desc' ? -comparison : comparison;
    });
    
    updateStats();
    currentPage = 1;
    renderCurrentPage();
}

// Update statistics
function updateStats() {
    document.getElementById('totalCount').textContent = allTickets.length;
    
    const openCount = allTickets.filter(t => 
        t.status?.toLowerCase() === 'open' || t.status?.toLowerCase() === 'new'
    ).length;
    
    const progressCount = allTickets.filter(t => 
        t.status?.toLowerCase() === 'in progress' || t.status?.toLowerCase() === 'pending'
    ).length;
    
    const closedCount = allTickets.filter(t => 
        t.status?.toLowerCase() === 'closed' || t.status?.toLowerCase() === 'resolved'
    ).length;
    
    document.getElementById('openCount').textContent = openCount;
    document.getElementById('progressCount').textContent = progressCount;
    document.getElementById('closedCount').textContent = closedCount;
}

// Render current page
function renderCurrentPage() {
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const pageTickets = filteredTickets.slice(startIndex, endIndex);
    
    if (filteredTickets.length === 0) {
        showEmptyState(true, 'No tickets found matching your filters.');
        document.getElementById('ticketsGrid').innerHTML = '';
        document.getElementById('tableBody').innerHTML = '';
        document.getElementById('pagination').style.display = 'none';
        return;
    }
    
    showEmptyState(false);
    document.getElementById('pagination').style.display = 'flex';
    
    if (currentView === 'grid') {
        renderGrid(pageTickets);
    } else {
        renderTable(pageTickets);
    }
    
    renderPagination();
}

// Render grid view
function renderGrid(tickets) {
    const grid = document.getElementById('ticketsGrid');
    grid.style.display = 'grid';
    document.getElementById('ticketsTable').style.display = 'none';
    
    grid.innerHTML = tickets.map(ticket => createTicketCard(ticket)).join('');
    
    // Add delete button listeners
    document.querySelectorAll('.btn-delete').forEach(button => {
        button.addEventListener('click', () => deleteTicket(button.dataset.id));
    });
}

// Render table view
function renderTable(tickets) {
    document.getElementById('ticketsGrid').style.display = 'none';
    document.getElementById('ticketsTable').style.display = 'block';
    
    const tbody = document.getElementById('tableBody');
    tbody.innerHTML = tickets.map(ticket => createTableRow(ticket)).join('');
    
    // Add delete button listeners
    document.querySelectorAll('.table-btn-delete').forEach(button => {
        button.addEventListener('click', () => deleteTicket(button.dataset.id));
    });
}

// Create ticket card HTML
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
                <button class="btn-view" onclick="window.location.href='edit-ticket.html?id=${ticket.ticketId}'">View Details</button>
                <button class="btn-delete" data-id="${ticket.ticketId}">Delete</button>
            </div>
        </div>
    `;
}

// Create table row HTML
function createTableRow(ticket) {
    const statusClass = getStatusClass(ticket.status);
    const comments = ticket.comments || [];
    const commentCount = comments.length;
    const latestComment = comments.length > 0 
        ? comments[comments.length - 1].comments 
        : '';
    const commentPreview = latestComment.length > 30 
        ? latestComment.substring(0, 30) + '...' 
        : latestComment;
    
    return `
        <tr>
            <td><strong>#${ticket.ticketId || 'N/A'}</strong></td>
            <td>${escapeHtml(ticket.title) || 'Untitled'}</td>
            <td>${escapeHtml(ticket.author) || 'Unknown'}</td>
            <td>${escapeHtml(ticket.systemName) || 'N/A'}</td>
            <td>${escapeHtml(ticket.category) || 'N/A'}</td>
            <td><span class="ticket-status ${statusClass}">${ticket.status || 'N/A'}</span></td>
            <td>${escapeHtml(ticket.statusComment) || '-'}</td>
            <td>${ticket.createdDate || 'N/A'}</td>
            <td>
                <div class="comments-info">
                    <strong>💬 ${commentCount}</strong>
                    ${commentPreview ? `<div class="comment-preview">${escapeHtml(commentPreview)}</div>` : '<div class="comment-preview">No comments</div>'}
                </div>
            </td>
            <td>
                <div class="table-actions">
                    <button class="table-btn table-btn-view" onclick="window.location.href='edit-ticket.html?id=${ticket.ticketId}'">View</button>
                    <button class="table-btn table-btn-delete" data-id="${ticket.ticketId}">Delete</button>
                </div>
            </td>
        </tr>
    `;
}

// Render pagination
function renderPagination() {
    const totalPages = Math.ceil(filteredTickets.length / itemsPerPage);
    const pageNumbers = document.getElementById('pageNumbers');
    
    document.getElementById('prevPage').disabled = currentPage === 1;
    document.getElementById('nextPage').disabled = currentPage === totalPages || totalPages === 0;
    
    pageNumbers.innerHTML = '';
    
    for (let i = 1; i <= totalPages; i++) {
        if (i === 1 || i === totalPages || (i >= currentPage - 2 && i <= currentPage + 2)) {
            const pageBtn = document.createElement('button');
            pageBtn.className = `page-number ${i === currentPage ? 'active' : ''}`;
            pageBtn.textContent = i;
            pageBtn.addEventListener('click', () => changePage(i));
            pageNumbers.appendChild(pageBtn);
        } else if (i === currentPage - 3 || i === currentPage + 3) {
            const dots = document.createElement('span');
            dots.textContent = '...';
            dots.style.padding = '10px';
            pageNumbers.appendChild(dots);
        }
    }
}

// Change page
function changePage(page) {
    const totalPages = Math.ceil(filteredTickets.length / itemsPerPage);
    if (page < 1 || page > totalPages) return;
    
    currentPage = page;
    renderCurrentPage();
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

// Switch view
function switchView(view) {
    currentView = view;
    document.querySelectorAll('.view-btn').forEach(btn => {
        btn.classList.toggle('active', btn.dataset.view === view);
    });
    renderCurrentPage();
}

// Get status CSS class
function getStatusClass(status) {
    if (!status) return 'status-default';
    const statusLower = status.toLowerCase();
    if (statusLower === 'open' || statusLower === 'new') return 'status-open';
    if (statusLower === 'in progress' || statusLower === 'pending') return 'status-progress';
    if (statusLower === 'closed' || statusLower === 'resolved') return 'status-closed';
    return 'status-default';
}

// Delete ticket
async function deleteTicket(id) {
    if (!confirm('Are you sure you want to delete this ticket?')) return;
    
    try {
        const response = await fetch(`${API_BASE_URL}/tickets/${id}`, { method: 'DELETE' });
        if (!response.ok) throw new Error('Failed to delete ticket');
        
        fetchTickets();
    } catch (error) {
        console.error('Error:', error);
        showError('Failed to delete ticket. Please try again.');
    }
}

// Helper functions
function showLoading(show) {
    document.getElementById('loading').classList.toggle('show', show);
}

function showError(message) {
    document.getElementById('errorMessage').textContent = message;
    document.getElementById('errorBanner').classList.add('show');
}

function hideError() {
    document.getElementById('errorBanner').classList.remove('show');
}

function showEmptyState(show, message) {
    const emptyState = document.getElementById('emptyState');
    if (show) {
        document.getElementById('emptyMessage').textContent = message || 'No tickets available.';
        emptyState.classList.add('show');
    } else {
        emptyState.classList.remove('show');
    }
}

function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}