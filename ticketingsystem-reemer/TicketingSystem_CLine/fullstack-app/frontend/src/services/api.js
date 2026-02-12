const API_BASE_URL = 'http://localhost:8080/api';

// Ticket API calls
export const getAllTickets = async () => {
  const response = await fetch(`${API_BASE_URL}/tickets`);
  if (!response.ok) throw new Error('Failed to fetch tickets');
  return response.json();
};

export const getTicketById = async (id) => {
  const response = await fetch(`${API_BASE_URL}/tickets/${id}`);
  if (!response.ok) throw new Error('Failed to fetch ticket');
  return response.json();
};

export const createTicket = async (ticket) => {
  const response = await fetch(`${API_BASE_URL}/tickets`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(ticket),
  });
  if (!response.ok) throw new Error('Failed to create ticket');
  return response.json();
};

export const updateTicket = async (id, ticket) => {
  const response = await fetch(`${API_BASE_URL}/tickets/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(ticket),
  });
  if (!response.ok) throw new Error('Failed to update ticket');
  return response.json();
};

export const deleteTicket = async (id) => {
  const response = await fetch(`${API_BASE_URL}/tickets/${id}`, {
    method: 'DELETE',
  });
  if (!response.ok) throw new Error('Failed to delete ticket');
};

export const searchTickets = async (title) => {
  const response = await fetch(`${API_BASE_URL}/tickets/search?title=${title}`);
  if (!response.ok) throw new Error('Failed to search tickets');
  return response.json();
};