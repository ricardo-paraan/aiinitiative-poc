import React, { useState, useEffect } from 'react';
import './App.css';
import { getAllTickets, deleteTicket } from './services/api';

function App() {
  const [tickets, setTickets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    fetchTickets();
  }, []);

  const fetchTickets = async () => {
    try {
      setLoading(true);
      const data = await getAllTickets();
      setTickets(data);
      setError(null);
    } catch (err) {
      setError('Failed to load tickets. Make sure the backend is running on http://localhost:8080');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this ticket?')) {
      try {
        await deleteTicket(id);
        fetchTickets();
      } catch (err) {
        setError('Failed to delete ticket');
        console.error(err);
      }
    }
  };

  const filteredTickets = tickets.filter(ticket =>
    ticket.title?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    ticket.author?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    ticket.status?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const getStatusColor = (status) => {
    const statusLower = status?.toLowerCase();
    if (statusLower === 'open' || statusLower === 'new') return 'status-open';
    if (statusLower === 'in progress' || statusLower === 'pending') return 'status-progress';
    if (statusLower === 'closed' || statusLower === 'resolved') return 'status-closed';
    return 'status-default';
  };

  return (
    <div className="App">
      <div className="dashboard">
        <header className="dashboard-header">
          <h1>🎫 Ticket Management Dashboard</h1>
          <p>View and manage all support tickets</p>
        </header>

        {error && (
          <div className="error-banner">
            ⚠️ {error}
          </div>
        )}

        <div className="dashboard-controls">
          <div className="search-box">
            <input
              type="text"
              placeholder="🔍 Search tickets by title, author, or status..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
          <div className="ticket-count">
            Total Tickets: <strong>{filteredTickets.length}</strong>
          </div>
        </div>

        {loading ? (
          <div className="loading">
            <div className="spinner"></div>
            <p>Loading tickets...</p>
          </div>
        ) : filteredTickets.length === 0 ? (
          <div className="empty-state">
            <h2>📭 No Tickets Found</h2>
            <p>
              {searchTerm 
                ? 'Try adjusting your search criteria' 
                : 'No tickets available. Start by creating your first ticket!'}
            </p>
          </div>
        ) : (
          <div className="tickets-grid">
            {filteredTickets.map(ticket => (
              <div key={ticket.ticketId} className="ticket-card">
                <div className="ticket-header">
                  <span className="ticket-id">#{ticket.ticketId}</span>
                  <span className={`ticket-status ${getStatusColor(ticket.status)}`}>
                    {ticket.status || 'N/A'}
                  </span>
                </div>
                <h3 className="ticket-title">{ticket.title || 'Untitled'}</h3>
                <div className="ticket-details">
                  <div className="detail-row">
                    <span className="detail-label">👤 Author:</span>
                    <span className="detail-value">{ticket.author || 'Unknown'}</span>
                  </div>
                  <div className="detail-row">
                    <span className="detail-label">💻 System:</span>
                    <span className="detail-value">{ticket.systemName || 'N/A'}</span>
                  </div>
                  <div className="detail-row">
                    <span className="detail-label">📂 Category:</span>
                    <span className="detail-value">{ticket.category || 'N/A'}</span>
                  </div>
                  <div className="detail-row">
                    <span className="detail-label">📅 Created:</span>
                    <span className="detail-value">{ticket.createdDate || 'N/A'}</span>
                  </div>
                  {ticket.description && (
                    <div className="ticket-description">
                      <p>{ticket.description}</p>
                    </div>
                  )}
                </div>
                <div className="ticket-actions">
                  <button className="btn-view">View Details</button>
                  <button className="btn-delete" onClick={() => handleDelete(ticket.ticketId)}>
                    Delete
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default App;