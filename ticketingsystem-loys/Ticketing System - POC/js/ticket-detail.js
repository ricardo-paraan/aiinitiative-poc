// Ticket Detail Page JavaScript

// Get ticket ID from URL
const urlParams = new URLSearchParams(window.location.search);
const ticketId = urlParams.get('id');

// State
let currentTicket = null;
let comments = [];
let attachments = [];
let statusHistory = [];

// Initialize page
document.addEventListener('DOMContentLoaded', async () => {
    if (!ticketId) {
        showToast('No ticket ID provided', 'error');
        setTimeout(() => window.location.href = 'index.html', 2000);
        return;
    }

    await loadTicketDetails();
    initializeEventListeners();
});

// Load ticket details
async function loadTicketDetails() {
    try {
        showLoading();

        // Load ticket data
        const ticket = await API.tickets.getById(ticketId);
        currentTicket = ticket;

        // Load related data
        const [commentsData, attachmentsData] = await Promise.all([
            API.comments.getByTicketId(ticketId),
            API.attachments.getByTicketId(ticketId)
        ]);

        comments = commentsData;
        attachments = attachmentsData;

        // Render all sections
        renderTicketHeader(ticket);
        renderTicketDetails(ticket);
        renderStatusHistory(ticket);
        renderAttachments(attachmentsData);
        renderComments(commentsData);
        renderSidebar(ticket);

        hideLoading();
    } catch (error) {
        console.error('Error loading ticket:', error);
        showToast('Failed to load ticket details', 'error');
        hideLoading();
    }
}

// Render ticket header
function renderTicketHeader(ticket) {
    document.getElementById('ticketId').textContent = `#${ticket.ticketId}`;
    document.getElementById('ticketTitle').textContent = ticket.title;
    
    const statusBadge = document.getElementById('ticketStatus');
    const statusName = ticket.statusName || 'Unknown';
    statusBadge.textContent = statusName;
    statusBadge.className = `status-badge status-${statusName.toLowerCase().replace(' ', '-')}`;
}

// Render ticket details
function renderTicketDetails(ticket) {
    document.getElementById('ticketDescription').textContent = ticket.description;
    document.getElementById('ticketSystem').textContent = ticket.systemName || 'N/A';
    document.getElementById('ticketCategory').textContent = ticket.categoryName || 'N/A';
}

// Render status history
function renderStatusHistory(ticket) {
    const container = document.getElementById('statusHistory');
    
    // Create timeline from ticket data
    const timeline = [
        {
            status: 'open',
            date: ticket.createdDate,
            text: 'Ticket created',
            author: 'System'
        }
    ];

    // Add current status if different from open
    const currentStatus = (ticket.statusName || '').toLowerCase();
    if (currentStatus && currentStatus !== 'open') {
        timeline.push({
            status: currentStatus,
            date: ticket.updatedDate,
            text: `Status changed to ${ticket.statusName}`,
            author: ticket.author
        });
    }

    container.innerHTML = timeline.map(item => `
        <div class="timeline-item">
            <div class="timeline-icon">
                <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
                    <circle cx="10" cy="10" r="8" stroke="currentColor" stroke-width="2"/>
                </svg>
            </div>
            <div class="timeline-content">
                <div class="timeline-header">
                    <span class="status-badge status-${item.status.replace(' ', '-')}">${item.status}</span>
                    <span class="timeline-date">${formatDateTime(item.date)}</span>
                </div>
                <p class="timeline-text">${item.text}</p>
                <p class="timeline-author">by ${item.author}</p>
            </div>
        </div>
    `).join('');
}

// Render attachments
function renderAttachments(attachments) {
    const container = document.getElementById('attachmentsList');
    
    if (!attachments || attachments.length === 0) {
        container.innerHTML = '<div class="empty-state"><p>No attachments yet</p></div>';
        return;
    }

    container.innerHTML = attachments.map(attachment => `
        <div class="attachment-item">
            <div class="attachment-info">
                <div class="attachment-icon">
                    <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
                        <path d="M6 2H14L18 6V16C18 16.5304 17.7893 17.0391 17.4142 17.4142C17.0391 17.7893 16.5304 18 16 18H4C3.46957 18 2.96086 17.7893 2.58579 17.4142C2.21071 17.0391 2 16.5304 2 16V4C2 3.46957 2.21071 2.96086 2.58579 2.58579C2.96086 2.21071 3.46957 2 4 2H6Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                </div>
                <div class="attachment-details">
                    <p class="attachment-name">${getFileName(attachment.filePath)}</p>
                    <p class="attachment-size">${formatFileSize(attachment.fileSize || 0)}</p>
                </div>
            </div>
            <button class="attachment-download" onclick="downloadAttachment(${attachment.attachmentId})">
                <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
                    <path d="M10 13V3M10 13L6 9M10 13L14 9M3 17H17" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
            </button>
        </div>
    `).join('');
}

// Render comments
function renderComments(comments) {
    const container = document.getElementById('commentsList');
    
    if (!comments || comments.length === 0) {
        container.innerHTML = '<div class="empty-state"><p>No comments yet. Be the first to comment!</p></div>';
        return;
    }

    container.innerHTML = comments.map(comment => {
        const initials = getInitials(comment.author || 'User');
        const isAdmin = comment.author && comment.author.toLowerCase().includes('admin');
        
        return `
            <div class="comment-item">
                <div class="comment-avatar">${initials}</div>
                <div class="comment-content">
                    <div class="comment-header">
                        <span class="comment-author">${comment.author || 'Anonymous'}</span>
                        ${isAdmin ? '<span class="comment-badge">Admin</span>' : ''}
                        <span class="comment-date">${formatDateTime(comment.createdDate)}</span>
                    </div>
                    <p class="comment-text">${escapeHtml(comment.commentText || comment.comments || '')}</p>
                </div>
            </div>
        `;
    }).join('');
}

// Render sidebar
function renderSidebar(ticket) {
    document.getElementById('submittedBy').textContent = ticket.author;
    document.getElementById('submittedEmail').textContent = getEmailFromAuthor(ticket.author);
    document.getElementById('dateSubmitted').textContent = formatDateTime(ticket.createdDate);
    document.getElementById('lastUpdated').textContent = formatDateTime(ticket.updatedDate);
    
    const sidebarStatus = document.getElementById('sidebarStatus');
    const statusName = ticket.statusName || 'Unknown';
    sidebarStatus.textContent = statusName;
    sidebarStatus.className = `status-badge status-${statusName.toLowerCase().replace(' ', '-')}`;
}

// Initialize event listeners
function initializeEventListeners() {
    // Comment input character counter
    const commentInput = document.getElementById('commentInput');
    const charCount = document.getElementById('commentCharCount');
    
    commentInput.addEventListener('input', () => {
        charCount.textContent = commentInput.value.length;
    });

    // Add comment button
    document.getElementById('addCommentBtn').addEventListener('click', handleAddComment);
    
    // Cancel comment button
    document.getElementById('cancelCommentBtn').addEventListener('click', () => {
        commentInput.value = '';
        charCount.textContent = '0';
    });

    // Add attachment button
    document.getElementById('addAttachmentBtn').addEventListener('click', () => {
        document.getElementById('uploadModal').style.display = 'flex';
    });

    // Close upload modal
    document.getElementById('closeUploadModal').addEventListener('click', closeUploadModal);
    document.getElementById('cancelUploadBtn').addEventListener('click', closeUploadModal);

    // File upload area
    const fileUploadArea = document.getElementById('fileUploadArea');
    const fileInput = document.getElementById('fileInput');

    fileUploadArea.addEventListener('click', () => fileInput.click());
    fileInput.addEventListener('change', handleFileSelect);

    // Drag and drop
    fileUploadArea.addEventListener('dragover', (e) => {
        e.preventDefault();
        fileUploadArea.style.borderColor = '#3b82f6';
        fileUploadArea.style.background = '#f9fafb';
    });

    fileUploadArea.addEventListener('dragleave', (e) => {
        e.preventDefault();
        fileUploadArea.style.borderColor = '#d1d5db';
        fileUploadArea.style.background = 'transparent';
    });

    fileUploadArea.addEventListener('drop', (e) => {
        e.preventDefault();
        fileUploadArea.style.borderColor = '#d1d5db';
        fileUploadArea.style.background = 'transparent';
        
        const files = Array.from(e.dataTransfer.files);
        handleFiles(files);
    });

    // Upload files button
    document.getElementById('uploadFilesBtn').addEventListener('click', handleFileUpload);

    // Mark as completed button
    document.getElementById('markCompletedBtn').addEventListener('click', handleMarkCompleted);

    // Contact support button
    document.getElementById('contactSupportBtn').addEventListener('click', () => {
        showToast('Contact support feature coming soon', 'info');
    });
}

// Handle add comment
async function handleAddComment() {
    const commentInput = document.getElementById('commentInput');
    const commentText = commentInput.value.trim();

    if (!commentText) {
        showToast('Please enter a comment', 'error');
        return;
    }

    try {
        showLoading();

        const commentData = {
            ticketId: parseInt(ticketId),
            commentText: commentText,
            author: currentTicket.author // Use current ticket author
        };

        await API.comments.create(ticketId, commentData);
        
        // Reload comments
        const updatedComments = await API.comments.getByTicketId(ticketId);
        comments = updatedComments;
        renderComments(updatedComments);

        // Clear input
        commentInput.value = '';
        document.getElementById('commentCharCount').textContent = '0';

        showToast('Comment added successfully', 'success');
        hideLoading();
    } catch (error) {
        console.error('Error adding comment:', error);
        showToast('Failed to add comment', 'error');
        hideLoading();
    }
}

// Handle file select
function handleFileSelect(event) {
    const files = Array.from(event.target.files);
    handleFiles(files);
}

// Handle files
let selectedFiles = [];

function handleFiles(files) {
    // Validate files
    const validFiles = files.filter(file => {
        if (!validateFileType(file)) {
            showToast(`Invalid file type: ${file.name}`, 'error');
            return false;
        }
        if (!validateFileSize(file)) {
            showToast(`File too large: ${file.name} (max 5MB)`, 'error');
            return false;
        }
        return true;
    });

    selectedFiles = [...selectedFiles, ...validFiles];
    renderSelectedFiles();
}

// Render selected files
function renderSelectedFiles() {
    const container = document.getElementById('selectedFiles');
    
    if (selectedFiles.length === 0) {
        container.innerHTML = '';
        return;
    }

    container.innerHTML = selectedFiles.map((file, index) => `
        <div class="selected-file-item">
            <div class="selected-file-info">
                <span class="selected-file-name">${file.name}</span>
                <span class="selected-file-size">${formatFileSize(file.size)}</span>
            </div>
            <button class="remove-file-btn" onclick="removeSelectedFile(${index})">
                Remove
            </button>
        </div>
    `).join('');
}

// Remove selected file
function removeSelectedFile(index) {
    selectedFiles.splice(index, 1);
    renderSelectedFiles();
}

// Handle file upload
async function handleFileUpload() {
    if (selectedFiles.length === 0) {
        showToast('Please select files to upload', 'error');
        return;
    }

    try {
        showLoading();

        // Upload each file
        for (const file of selectedFiles) {
            await API.attachments.upload(ticketId, file);
        }

        // Reload attachments
        const updatedAttachments = await API.attachments.getByTicketId(ticketId);
        attachments = updatedAttachments;
        renderAttachments(updatedAttachments);

        // Close modal and reset
        closeUploadModal();
        selectedFiles = [];
        document.getElementById('fileInput').value = '';

        showToast('Files uploaded successfully', 'success');
        hideLoading();
    } catch (error) {
        console.error('Error uploading files:', error);
        showToast('Failed to upload files', 'error');
        hideLoading();
    }
}

// Close upload modal
function closeUploadModal() {
    document.getElementById('uploadModal').style.display = 'none';
    selectedFiles = [];
    renderSelectedFiles();
    document.getElementById('fileInput').value = '';
}

// Download attachment
async function downloadAttachment(attachmentId) {
    try {
        showLoading();
        const blob = await API.attachments.download(attachmentId);
        
        // Create download link
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `attachment-${attachmentId}`;
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);
        
        showToast('Download started', 'success');
        hideLoading();
    } catch (error) {
        console.error('Error downloading attachment:', error);
        showToast('Failed to download attachment', 'error');
        hideLoading();
    }
}

// Handle mark as completed
async function handleMarkCompleted() {
    if (!confirm('Are you sure you want to mark this ticket as completed?')) {
        return;
    }

    try {
        showLoading();

        // Find "Resolved" status ID (assuming it exists)
        const statuses = await API.statuses.getAll();
        const resolvedStatus = statuses.find(s => s.status.toLowerCase() === 'resolved');

        if (!resolvedStatus) {
            showToast('Resolved status not found', 'error');
            hideLoading();
            return;
        }

        // Update ticket status
        const updateData = {
            title: currentTicket.title,
            description: currentTicket.description,
            statusId: resolvedStatus.statusId,
            systemId: currentTicket.systemId,
            categoryId: currentTicket.categoryId
        };

        await API.tickets.update(ticketId, updateData);

        showToast('Ticket marked as completed', 'success');
        
        // Reload ticket details
        await loadTicketDetails();
        
        hideLoading();
    } catch (error) {
        console.error('Error marking ticket as completed:', error);
        showToast('Failed to update ticket status', 'error');
        hideLoading();
    }
}

// Helper functions
function getFileName(filePath) {
    if (!filePath) return 'Unknown file';
    return filePath.split('/').pop() || filePath;
}

function getInitials(name) {
    if (!name) return 'U';
    const parts = name.trim().split(' ');
    if (parts.length === 1) return parts[0].charAt(0).toUpperCase();
    return (parts[0].charAt(0) + parts[parts.length - 1].charAt(0)).toUpperCase();
}

function getEmailFromAuthor(author) {
    if (!author) return 'user@company.com';
    // Generate email from author name
    const name = author.toLowerCase().replace(/\s+/g, '.');
    return `${name}@company.com`;
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function showLoading() {
    document.getElementById('loadingOverlay').style.display = 'flex';
}

function hideLoading() {
    document.getElementById('loadingOverlay').style.display = 'none';
}

// Make functions globally accessible
window.removeSelectedFile = removeSelectedFile;
window.downloadAttachment = downloadAttachment;