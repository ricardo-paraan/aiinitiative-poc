// API Configuration
const API_BASE_URL = 'http://localhost:8080/api';

// API Service
const API = {
    // Tickets
    tickets: {
        getAll: async () => {
            const response = await fetch(`${API_BASE_URL}/tickets`);
            if (!response.ok) throw new Error('Failed to fetch tickets');
            return await response.json();
        },
        
        getById: async (id) => {
            const response = await fetch(`${API_BASE_URL}/tickets/${id}`);
            if (!response.ok) throw new Error('Failed to fetch ticket');
            return await response.json();
        },
        
        create: async (ticketData) => {
            const response = await fetch(`${API_BASE_URL}/tickets`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(ticketData),
            });
            if (!response.ok) throw new Error('Failed to create ticket');
            return await response.json();
        },
        
        update: async (id, ticketData) => {
            const response = await fetch(`${API_BASE_URL}/tickets/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(ticketData),
            });
            if (!response.ok) throw new Error('Failed to update ticket');
            return await response.json();
        },
        
        delete: async (id) => {
            const response = await fetch(`${API_BASE_URL}/tickets/${id}`, {
                method: 'DELETE',
            });
            if (!response.ok) throw new Error('Failed to delete ticket');
        },
        
        search: async (keyword) => {
            const response = await fetch(`${API_BASE_URL}/tickets/search?keyword=${encodeURIComponent(keyword)}`);
            if (!response.ok) throw new Error('Failed to search tickets');
            return await response.json();
        },
        
        filter: async (filters) => {
            const params = new URLSearchParams();
            if (filters.statusId) params.append('statusId', filters.statusId);
            if (filters.categoryId) params.append('categoryId', filters.categoryId);
            if (filters.systemId) params.append('systemId', filters.systemId);
            if (filters.author) params.append('author', filters.author);
            
            const response = await fetch(`${API_BASE_URL}/tickets/filter?${params.toString()}`);
            if (!response.ok) throw new Error('Failed to filter tickets');
            return await response.json();
        },
    },
    
    // Comments
    comments: {
        getByTicketId: async (ticketId) => {
            const response = await fetch(`${API_BASE_URL}/tickets/${ticketId}/comments`);
            if (!response.ok) throw new Error('Failed to fetch comments');
            return await response.json();
        },
        
        create: async (ticketId, commentData) => {
            const response = await fetch(`${API_BASE_URL}/tickets/${ticketId}/comments`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(commentData),
            });
            if (!response.ok) throw new Error('Failed to create comment');
            return await response.json();
        },
        
        delete: async (commentId) => {
            const response = await fetch(`${API_BASE_URL}/comments/${commentId}`, {
                method: 'DELETE',
            });
            if (!response.ok) throw new Error('Failed to delete comment');
        },
    },
    
    // Attachments
    attachments: {
        getByTicketId: async (ticketId) => {
            const response = await fetch(`${API_BASE_URL}/tickets/${ticketId}/attachments`);
            if (!response.ok) throw new Error('Failed to fetch attachments');
            return await response.json();
        },
        
        upload: async (ticketId, file) => {
            const formData = new FormData();
            formData.append('file', file);
            
            const response = await fetch(`${API_BASE_URL}/tickets/${ticketId}/attachments`, {
                method: 'POST',
                body: formData,
            });
            if (!response.ok) throw new Error('Failed to upload attachment');
            return await response.json();
        },
        
        download: async (attachmentId) => {
            const response = await fetch(`${API_BASE_URL}/attachments/${attachmentId}/download`);
            if (!response.ok) throw new Error('Failed to download attachment');
            return await response.blob();
        },
        
        delete: async (attachmentId) => {
            const response = await fetch(`${API_BASE_URL}/attachments/${attachmentId}`, {
                method: 'DELETE',
            });
            if (!response.ok) throw new Error('Failed to delete attachment');
        },
    },
    
    // Reference Data
    statuses: {
        getAll: async () => {
            const response = await fetch(`${API_BASE_URL}/statuses`);
            if (!response.ok) throw new Error('Failed to fetch statuses');
            return await response.json();
        },
    },
    
    categories: {
        getAll: async () => {
            const response = await fetch(`${API_BASE_URL}/categories`);
            if (!response.ok) throw new Error('Failed to fetch categories');
            return await response.json();
        },
    },
    
    systems: {
        getAll: async () => {
            const response = await fetch(`${API_BASE_URL}/systems`);
            if (!response.ok) throw new Error('Failed to fetch systems');
            return await response.json();
        },
    },
    
    // Dashboard
    dashboard: {
        getStats: async () => {
            const response = await fetch(`${API_BASE_URL}/dashboard/stats`);
            if (!response.ok) throw new Error('Failed to fetch dashboard stats');
            return await response.json();
        },
    },
};