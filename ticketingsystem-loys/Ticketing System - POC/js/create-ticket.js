// Create Ticket Page Logic

let selectedFiles = [];
const MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

// Initialize page
document.addEventListener('DOMContentLoaded', async () => {
    await loadReferenceData();
    setupEventListeners();
    setupFileUpload();
});

// Load reference data for dropdowns
async function loadReferenceData() {
    try {
        // Load systems
        const systems = await API.systems.getAll();
        const systemSelect = document.getElementById('system');
        systems.forEach(system => {
            const option = document.createElement('option');
            option.value = system.systemId;
            option.textContent = system.systemName;
            systemSelect.appendChild(option);
        });

        // Load categories
        const categories = await API.categories.getAll();
        const categorySelect = document.getElementById('category');
        categories.forEach(category => {
            const option = document.createElement('option');
            option.value = category.categoryId;
            option.textContent = category.categoryName;
            categorySelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error loading reference data:', error);
        showToast('Failed to load form options', 'error');
    }
}

// Setup event listeners
function setupEventListeners() {
    // Form submission
    const form = document.getElementById('createTicketForm');
    form.addEventListener('submit', handleSubmit);

    // Character counters
    const titleInput = document.getElementById('title');
    const descInput = document.getElementById('description');
    
    titleInput.addEventListener('input', () => {
        updateCharCounter('titleCounter', titleInput.value.length);
    });
    
    descInput.addEventListener('input', () => {
        updateCharCounter('descCounter', descInput.value.length);
    });

    // System change - enable category
    document.getElementById('system').addEventListener('change', (e) => {
        const categorySelect = document.getElementById('category');
        if (e.target.value) {
            categorySelect.disabled = false;
        }
    });
}

// Setup file upload
function setupFileUpload() {
    const fileInput = document.getElementById('attachments');
    const uploadArea = document.getElementById('fileUploadArea');

    // File input change
    fileInput.addEventListener('change', handleFileSelect);

    // Drag and drop
    uploadArea.addEventListener('dragover', (e) => {
        e.preventDefault();
        uploadArea.classList.add('drag-over');
    });

    uploadArea.addEventListener('dragleave', () => {
        uploadArea.classList.remove('drag-over');
    });

    uploadArea.addEventListener('drop', (e) => {
        e.preventDefault();
        uploadArea.classList.remove('drag-over');
        
        const files = Array.from(e.dataTransfer.files);
        handleFiles(files);
    });
}

// Handle file selection
function handleFileSelect(event) {
    const files = Array.from(event.target.files);
    handleFiles(files);
}

// Handle files
function handleFiles(files) {
    files.forEach(file => {
        // Validate file type
        if (!isAllowedFileType(file.name)) {
            showToast(`File type not allowed: ${file.name}`, 'error');
            return;
        }

        // Validate file size
        if (!isFileSizeValid(file.size, 5)) {
            showToast(`File too large: ${file.name} (Max 5MB)`, 'error');
            return;
        }

        // Add to selected files
        selectedFiles.push(file);
    });

    displayFileList();
}

// Display file list
function displayFileList() {
    const fileList = document.getElementById('fileList');
    
    if (selectedFiles.length === 0) {
        fileList.innerHTML = '';
        return;
    }

    fileList.innerHTML = selectedFiles.map((file, index) => `
        <div class="file-item">
            <div class="file-info">
                <div class="file-icon">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M13 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V9z"></path>
                        <polyline points="13 2 13 9 20 9"></polyline>
                    </svg>
                </div>
                <div class="file-details">
                    <div class="file-name">${escapeHtml(file.name)}</div>
                    <div class="file-size">${formatFileSize(file.size)}</div>
                </div>
            </div>
            <button type="button" class="btn-remove-file" onclick="removeFile(${index})">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <line x1="18" y1="6" x2="6" y2="18"></line>
                    <line x1="6" y1="6" x2="18" y2="18"></line>
                </svg>
            </button>
        </div>
    `).join('');
}

// Remove file
function removeFile(index) {
    selectedFiles.splice(index, 1);
    displayFileList();
}

// Update character counter
function updateCharCounter(counterId, length) {
    document.getElementById(counterId).textContent = length;
}

// Handle form submission
async function handleSubmit(event) {
    event.preventDefault();

    // Get form data
    const formData = {
        title: document.getElementById('title').value.trim(),
        author: 'Current User', // In a real app, this would come from authentication
        description: document.getElementById('description').value.trim(),
        statusId: 1, // Default to "Open" status
        categoryId: parseInt(document.getElementById('category').value),
        systemId: parseInt(document.getElementById('system').value)
    };

    // Validate
    if (!validateForm(formData)) {
        return;
    }

    // Show loading state
    const submitBtn = document.getElementById('submitBtn');
    const btnText = submitBtn.querySelector('.btn-text');
    const btnLoading = submitBtn.querySelector('.btn-loading');
    
    btnText.style.display = 'none';
    btnLoading.style.display = 'flex';
    submitBtn.disabled = true;

    try {
        // Create ticket
        const ticket = await API.tickets.create(formData);
        
        // Upload attachments if any
        if (selectedFiles.length > 0) {
            await uploadAttachments(ticket.ticketId);
        }

        // Show success modal
        showSuccessModal(ticket.ticketId);
    } catch (error) {
        console.error('Error creating ticket:', error);
        showToast('Failed to create ticket. Please try again.', 'error');
        
        // Reset button
        btnText.style.display = 'block';
        btnLoading.style.display = 'none';
        submitBtn.disabled = false;
    }
}

// Validate form
function validateForm(formData) {
    // Title validation
    if (!formData.title || formData.title.length < 5) {
        showToast('Title must be at least 5 characters', 'error');
        document.getElementById('title').focus();
        return false;
    }

    // System validation
    if (!formData.systemId) {
        showToast('Please select a system', 'error');
        document.getElementById('system').focus();
        return false;
    }

    // Category validation
    if (!formData.categoryId) {
        showToast('Please select a category', 'error');
        document.getElementById('category').focus();
        return false;
    }

    // Description validation
    if (!formData.description || formData.description.length < 10) {
        showToast('Description must be at least 10 characters', 'error');
        document.getElementById('description').focus();
        return false;
    }

    return true;
}

// Upload attachments
async function uploadAttachments(ticketId) {
    const uploadPromises = selectedFiles.map(file => 
        API.attachments.upload(ticketId, file)
    );

    try {
        await Promise.all(uploadPromises);
    } catch (error) {
        console.error('Error uploading attachments:', error);
        showToast('Ticket created but some attachments failed to upload', 'warning');
    }
}

// Show success modal
function showSuccessModal(ticketId) {
    document.getElementById('ticketId').textContent = `#${ticketId}`;
    document.getElementById('successModal').style.display = 'flex';
}

// Make removeFile function global
window.removeFile = removeFile;