const API_BASE_URL = 'http://localhost:8081/api';

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('createTicketForm');
    form.addEventListener('submit', handleSubmit);
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
        submitBtn.textContent = 'Creating...';
        
        // Send POST request
        const response = await fetch(`${API_BASE_URL}/tickets`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });
        
        if (!response.ok) {
            const errorData = await response.text();
            throw new Error(errorData || 'Failed to create ticket');
        }
        
        const createdTicket = await response.json();
        
        // Show success message
        showSuccess(`Ticket #${createdTicket.ticketId} created successfully! Redirecting to dashboard...`);
        
        // Redirect to dashboard after 2 seconds
        setTimeout(() => {
            window.location.href = 'index.html';
        }, 2000);
        
    } catch (error) {
        console.error('Error:', error);
        showError(`Failed to create ticket: ${error.message}. Please try again.`);
        
        // Re-enable submit button
        const submitBtn = document.querySelector('button[type="submit"]');
        submitBtn.disabled = false;
        submitBtn.textContent = 'Create Ticket';
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