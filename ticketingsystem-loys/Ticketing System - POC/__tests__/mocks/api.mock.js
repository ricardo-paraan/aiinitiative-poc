/**
 * Mock API Service for Testing
 * Simulates API responses without making real HTTP calls
 */

const {
  mockTicketsAllStatuses,
  mockTicketsAllPending,
  mockTicketsAllInProgress,
  mockTicketsAllResolved,
  mockTicketsEmpty
} = require('./ticket-data.mock');

// Mock API responses
const mockAPIResponses = {
  tickets: {
    all: mockTicketsAllStatuses,
    allPending: mockTicketsAllPending,
    allInProgress: mockTicketsAllInProgress,
    allResolved: mockTicketsAllResolved,
    empty: mockTicketsEmpty
  },
  statuses: [
    { statusId: 1, statusName: 'Pending' },
    { statusId: 2, statusName: 'In Progress' },
    { statusId: 3, statusName: 'Resolved' }
  ],
  categories: [
    { categoryId: 1, categoryName: 'Bug' },
    { categoryId: 2, categoryName: 'Feature Request' },
    { categoryId: 3, categoryName: 'Performance' },
    { categoryId: 4, categoryName: 'Security' }
  ],
  systems: [
    { systemId: 1, systemName: 'Web Application' },
    { systemId: 2, systemName: 'Database' },
    { systemId: 3, systemName: 'Reporting System' }
  ]
};

/**
 * Create mock API object
 * @param {Object} options - Configuration options for mock behavior
 * @returns {Object} Mock API object
 */
function createMockAPI(options = {}) {
  const {
    ticketsData = mockTicketsAllStatuses,
    shouldFail = false,
    errorMessage = 'API Error'
  } = options;

  return {
    tickets: {
      getAll: jest.fn(async () => {
        if (shouldFail) {
          throw new Error(errorMessage);
        }
        return Promise.resolve(ticketsData);
      }),
      
      getById: jest.fn(async (id) => {
        if (shouldFail) {
          throw new Error(errorMessage);
        }
        const ticket = ticketsData.find(t => t.ticketId === id);
        if (!ticket) {
          throw new Error('Ticket not found');
        }
        return Promise.resolve(ticket);
      }),
      
      create: jest.fn(async (ticketData) => {
        if (shouldFail) {
          throw new Error(errorMessage);
        }
        return Promise.resolve({ ticketId: 999, ...ticketData });
      }),
      
      update: jest.fn(async (id, ticketData) => {
        if (shouldFail) {
          throw new Error(errorMessage);
        }
        return Promise.resolve({ ticketId: id, ...ticketData });
      }),
      
      delete: jest.fn(async (id) => {
        if (shouldFail) {
          throw new Error(errorMessage);
        }
        return Promise.resolve();
      }),
      
      search: jest.fn(async (keyword) => {
        if (shouldFail) {
          throw new Error(errorMessage);
        }
        return Promise.resolve(
          ticketsData.filter(t => 
            t.title.toLowerCase().includes(keyword.toLowerCase())
          )
        );
      }),
      
      filter: jest.fn(async (filters) => {
        if (shouldFail) {
          throw new Error(errorMessage);
        }
        let filtered = [...ticketsData];
        if (filters.statusId) {
          filtered = filtered.filter(t => t.statusId == filters.statusId);
        }
        if (filters.categoryId) {
          filtered = filtered.filter(t => t.categoryId == filters.categoryId);
        }
        if (filters.systemId) {
          filtered = filtered.filter(t => t.systemId == filters.systemId);
        }
        return Promise.resolve(filtered);
      })
    },
    
    statuses: {
      getAll: jest.fn(async () => {
        if (shouldFail) {
          throw new Error(errorMessage);
        }
        return Promise.resolve(mockAPIResponses.statuses);
      })
    },
    
    categories: {
      getAll: jest.fn(async () => {
        if (shouldFail) {
          throw new Error(errorMessage);
        }
        return Promise.resolve(mockAPIResponses.categories);
      })
    },
    
    systems: {
      getAll: jest.fn(async () => {
        if (shouldFail) {
          throw new Error(errorMessage);
        }
        return Promise.resolve(mockAPIResponses.systems);
      })
    },
    
    dashboard: {
      getStats: jest.fn(async () => {
        if (shouldFail) {
          throw new Error(errorMessage);
        }
        // Calculate stats from tickets data
        const stats = {
          pending: 0,
          inProgress: 0,
          resolved: 0,
          total: ticketsData.length
        };
        
        ticketsData.forEach(ticket => {
          const statusName = ticket.statusName?.toLowerCase() || '';
          if (statusName.includes('open') || statusName.includes('pending')) {
            stats.pending++;
          } else if (statusName.includes('progress')) {
            stats.inProgress++;
          } else if (statusName.includes('resolved') || statusName.includes('closed')) {
            stats.resolved++;
          }
        });
        
        return Promise.resolve(stats);
      })
    }
  };
}

/**
 * Setup global API mock
 * @param {Object} mockAPI - Mock API object to set globally
 */
function setupGlobalAPIMock(mockAPI) {
  global.API = mockAPI;
}

/**
 * Reset all API mocks
 */
function resetAPIMocks() {
  if (global.API) {
    Object.values(global.API).forEach(service => {
      Object.values(service).forEach(method => {
        if (method.mockClear) {
          method.mockClear();
        }
      });
    });
  }
}

module.exports = {
  createMockAPI,
  setupGlobalAPIMock,
  resetAPIMocks,
  mockAPIResponses
};