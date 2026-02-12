// Tickets List Page Logic

let allTickets = [];
let filteredTickets = [];
let currentPage = 1;
const itemsPerPage = 10;

// Initialize page
document.addEventListener('DOMContentLoaded', async () => {
    await loadReferenceData();
    await loadTickets();
    setupEventListeners();
});

// Load reference data for filters
async function loadReferenceData() {
    try {
        // Load statuses
        const statuses = await API.statuses.getAll();
        const statusFilter = document.getElementById('statusFilter');
        statuses.forEach(status => {
            const option = document.createElement('option');
            option.value = status.statusId;
            option.textContent = status.statusName;
            statusFilter.appendChild(option);
        });

        // Load systems
        const systems = await API.systems.getAll();
        const systemFilter = document.getElementById('systemFilter');
        systems.forEach(system => {
            const option = document.createElement('option');
            option.value = system.systemId;
            option.textContent = system.systemName;
            systemFilter.appendChild(option);
        });

        // Load categories
        const categories = await API.categories.getAll();
        const categoryFilter = document.getElementById('categoryFilter');
        categories.forEach(category => {
            const option = document.createElement('option');
            option.value = category.categoryId;
            option.textContent = category.categoryName;
            categoryFilter.appendChild(option);
        });
    } catch (error) {
        console.error('Error loading reference data:', error);
        showToast('Failed to load filter options', 'error');
    }
}

// Load all tickets
async function loadTickets() {
    try {
        allTickets = await API.tickets.getAll();
        filteredTickets = [...allTickets];
        updateStats();
        applyFiltersAndSort();
    } catch (error) {
        console.error('Error loading tickets:', error);
        showToast('Failed to load tickets', 'error');
        displayEmptyState('Failed to load tickets. Please try again.');
    }
}

// Update statistics cards
function updateStats() {
    const stats = {
        pending: 0,
        progress: 0,
        resolved: 0
    };

    allTickets.forEach(ticket => {
        const statusName = ticket.statusName?.toLowerCase() || '';
        if (statusName.includes('open') || statusName.includes('pending')) {
            stats.pending++;
        } else if (statusName.includes('progress')) {
            stats.progress++;
        } else if (statusName.includes('resolved') || statusName.includes('closed')) {
            stats.resolved++;
        }
    });

    document.getElementById('pendingCount').textContent = stats.pending;
    document.getElementById('progressCount').textContent = stats.progress;
    document.getElementById('resolvedCount').textContent = stats.resolved;
}

// Setup event listeners
function setupEventListeners() {
    // Search
    const searchInput = document.getElementById('searchInput');
    searchInput.addEventListener('input', debounce(handleSearch, 300));

    // Filters
    document.getElementById('statusFilter').addEventListener('change', applyFiltersAndSort);
    document.getElementById('systemFilter').addEventListener('change', applyFiltersAndSort);
    document.getElementById('categoryFilter').addEventListener('change', applyFiltersAndSort);
    document.getElementById('sortFilter').addEventListener('change', applyFiltersAndSort);

    // Pagination
    document.getElementById('prevBtn').addEventListener('click', () => changePage(currentPage - 1));
    document.getElementById('nextBtn').addEventListener('click', () => changePage(currentPage + 1));
}

// Handle search
function handleSearch(event) {
    const keyword = event.target.value.trim().toLowerCase();
    
    if (keyword === '') {
        filteredTickets = [...allTickets];
    } else {
        filteredTickets = allTickets.filter(ticket => {
            const ticketId = `#${ticket.ticketId}`.toLowerCase();
            const title = ticket.title?.toLowerCase() || '';
            const description = ticket.description?.toLowerCase() || '';
            
            return ticketId.includes(keyword) || 
                   title.includes(keyword) || 
                   description.includes(keyword);
        });
    }
    
    currentPage = 1;
    applyFiltersAndSort();
}

// Apply filters and sorting
function applyFiltersAndSort() {
    let tickets = [...filteredTickets];

    // Apply status filter
    const statusId = document.getElementById('statusFilter').value;
    if (statusId) {
        tickets = tickets.filter(t => t.statusId == statusId);
    }

    // Apply system filter
    const systemId = document.getElementById('systemFilter').value;
    if (systemId) {
        tickets = tickets.filter(t => t.systemId == systemId);
    }

    // Apply category filter
    const categoryId = document.getElementById('categoryFilter').value;
    if (categoryId) {
        tickets = tickets.filter(t => t.categoryId == categoryId);
    }

    // Apply sorting
    const sortBy = document.getElementById('sortFilter').value;
    tickets.sort((a, b) => {
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

    filteredTickets = tickets;
    displayTickets();
}

// Display tickets in table
function displayTickets() {
    const tbody = document.getElementById('ticketsTableBody');
    
    if (filteredTickets.length === 0) {
        displayEmptyState('No tickets found');
        return;
    }

    // Calculate pagination
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const paginatedTickets = filteredTickets.slice(startIndex, endIndex);

    // Generate table rows
    tbody.innerHTML = paginatedTickets.map(ticket => `
        <tr onclick="viewTicket(${ticket.ticketId})">
            <td class="ticket-id">#${ticket.ticketId}</td>
            <td class="ticket-title">${escapeHtml(ticket.title)}</td>
            <td>
                <span class="status-badge ${getStatusClass(ticket.statusName)}">
                    ${ticket.statusName || 'Unknown'}
                </span>
            </td>
            <td>${ticket.systemName || 'N/A'}</td>
            <td>${ticket.categoryName || 'N/A'}</td>
            <td class="ticket-date">${formatDate(ticket.createdDate)}</td>
            <td class="ticket-date">${formatDate(ticket.updatedDate)}</td>
        </tr>
    `).join('');

    updatePagination();
}

// Display empty state
function displayEmptyState(message) {
    const tbody = document.getElementById('ticketsTableBody');
    tbody.innerHTML = `
        <tr>
            <td colspan="7" class="empty-state">
                <div class="empty-state-icon">
                    <svg width="80" height="80" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M9 11l3 3L22 4"></path>
                        <path d="M21 12v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11"></path>
                    </svg>
                </div>
                <h3>${message}</h3>
                <p>Try adjusting your search or filters</p>
            </td>
        </tr>
    `;
    
    // Hide pagination
    document.querySelector('.pagination-container').style.display = 'none';
}

// Update pagination controls
function updatePagination() {
    const totalPages = Math.ceil(filteredTickets.length / itemsPerPage);
    const startIndex = (currentPage - 1) * itemsPerPage + 1;
    const endIndex = Math.min(currentPage * itemsPerPage, filteredTickets.length);

    // Update info text
    document.getElementById('paginationInfo').textContent = 
        `Showing ${startIndex} to ${endIndex} of ${filteredTickets.length} tickets`;

    // Update buttons
    document.getElementById('prevBtn').disabled = currentPage === 1;
    document.getElementById('nextBtn').disabled = currentPage === totalPages;

    // Update page numbers
    const paginationNumbers = document.getElementById('paginationNumbers');
    paginationNumbers.innerHTML = '';

    for (let i = 1; i <= totalPages; i++) {
        if (i === 1 || i === totalPages || (i >= currentPage - 1 && i <= currentPage + 1)) {
            const pageBtn = document.createElement('button');
            pageBtn.className = `page-number ${i === currentPage ? 'active' : ''}`;
            pageBtn.textContent = i;
            pageBtn.onclick = () => changePage(i);
            paginationNumbers.appendChild(pageBtn);
        } else if (i === currentPage - 2 || i === currentPage + 2) {
            const ellipsis = document.createElement('span');
            ellipsis.textContent = '...';
            ellipsis.style.padding = '0 8px';
            paginationNumbers.appendChild(ellipsis);
        }
    }

    // Show pagination
    document.querySelector('.pagination-container').style.display = 'flex';
}

// Change page
function changePage(page) {
    const totalPages = Math.ceil(filteredTickets.length / itemsPerPage);
    if (page < 1 || page > totalPages) return;
    
    currentPage = page;
    displayTickets();
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

// View ticket details
function viewTicket(ticketId) {
    window.location.href = `ticket-detail.html?id=${ticketId}`;
}