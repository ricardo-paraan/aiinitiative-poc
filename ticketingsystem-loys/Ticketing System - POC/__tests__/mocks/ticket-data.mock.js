/**
 * Mock Ticket Data for Testing
 * Provides various ticket datasets for different test scenarios
 */

// Mock tickets with various statuses
const mockTicketsAllStatuses = [
  {
    ticketId: 1,
    title: 'Login issue on production',
    description: 'Users cannot login',
    statusId: 1,
    statusName: 'Pending',
    categoryId: 1,
    categoryName: 'Bug',
    systemId: 1,
    systemName: 'Authentication System',
    createdDate: '2024-01-15T10:00:00Z',
    updatedDate: '2024-01-15T10:00:00Z'
  },
  {
    ticketId: 2,
    title: 'Database connection timeout',
    description: 'Connection pool exhausted',
    statusId: 1,
    statusName: 'Open',
    categoryId: 1,
    categoryName: 'Bug',
    systemId: 2,
    systemName: 'Database',
    createdDate: '2024-01-16T11:00:00Z',
    updatedDate: '2024-01-16T11:00:00Z'
  },
  {
    ticketId: 3,
    title: 'Add new feature for reporting',
    description: 'Need custom reports',
    statusId: 2,
    statusName: 'In Progress',
    categoryId: 2,
    categoryName: 'Feature Request',
    systemId: 3,
    systemName: 'Reporting System',
    createdDate: '2024-01-17T12:00:00Z',
    updatedDate: '2024-01-18T09:00:00Z'
  },
  {
    ticketId: 4,
    title: 'Performance optimization needed',
    description: 'Slow page load times',
    statusId: 3,
    statusName: 'Resolved',
    categoryId: 3,
    categoryName: 'Performance',
    systemId: 1,
    systemName: 'Web Application',
    createdDate: '2024-01-18T13:00:00Z',
    updatedDate: '2024-01-20T15:00:00Z'
  },
  {
    ticketId: 5,
    title: 'Security vulnerability found',
    description: 'XSS vulnerability detected',
    statusId: 3,
    statusName: 'Closed',
    categoryId: 4,
    categoryName: 'Security',
    systemId: 1,
    systemName: 'Web Application',
    createdDate: '2024-01-19T14:00:00Z',
    updatedDate: '2024-01-21T16:00:00Z'
  }
];

// Mock tickets - all pending
const mockTicketsAllPending = [
  {
    ticketId: 1,
    title: 'Issue 1',
    statusName: 'Pending',
    createdDate: '2024-01-15T10:00:00Z',
    updatedDate: '2024-01-15T10:00:00Z'
  },
  {
    ticketId: 2,
    title: 'Issue 2',
    statusName: 'Open',
    createdDate: '2024-01-16T11:00:00Z',
    updatedDate: '2024-01-16T11:00:00Z'
  },
  {
    ticketId: 3,
    title: 'Issue 3',
    statusName: 'Pending',
    createdDate: '2024-01-17T12:00:00Z',
    updatedDate: '2024-01-17T12:00:00Z'
  }
];

// Mock tickets - all in progress
const mockTicketsAllInProgress = [
  {
    ticketId: 1,
    title: 'Task 1',
    statusName: 'In Progress',
    createdDate: '2024-01-15T10:00:00Z',
    updatedDate: '2024-01-16T10:00:00Z'
  },
  {
    ticketId: 2,
    title: 'Task 2',
    statusName: 'In Progress',
    createdDate: '2024-01-16T11:00:00Z',
    updatedDate: '2024-01-17T11:00:00Z'
  }
];

// Mock tickets - all resolved
const mockTicketsAllResolved = [
  {
    ticketId: 1,
    title: 'Completed 1',
    statusName: 'Resolved',
    createdDate: '2024-01-15T10:00:00Z',
    updatedDate: '2024-01-20T10:00:00Z'
  },
  {
    ticketId: 2,
    title: 'Completed 2',
    statusName: 'Closed',
    createdDate: '2024-01-16T11:00:00Z',
    updatedDate: '2024-01-21T11:00:00Z'
  },
  {
    ticketId: 3,
    title: 'Completed 3',
    statusName: 'Resolved',
    createdDate: '2024-01-17T12:00:00Z',
    updatedDate: '2024-01-22T12:00:00Z'
  },
  {
    ticketId: 4,
    title: 'Completed 4',
    statusName: 'Closed',
    createdDate: '2024-01-18T13:00:00Z',
    updatedDate: '2024-01-23T13:00:00Z'
  }
];

// Empty tickets array
const mockTicketsEmpty = [];

// Mock tickets with edge cases
const mockTicketsEdgeCases = [
  {
    ticketId: 1,
    title: 'Ticket with null status',
    statusName: null,
    createdDate: '2024-01-15T10:00:00Z',
    updatedDate: '2024-01-15T10:00:00Z'
  },
  {
    ticketId: 2,
    title: 'Ticket with undefined status',
    statusName: undefined,
    createdDate: '2024-01-16T11:00:00Z',
    updatedDate: '2024-01-16T11:00:00Z'
  },
  {
    ticketId: 3,
    title: 'Ticket with empty status',
    statusName: '',
    createdDate: '2024-01-17T12:00:00Z',
    updatedDate: '2024-01-17T12:00:00Z'
  },
  {
    ticketId: 4,
    title: 'Ticket with unknown status',
    statusName: 'Unknown Status',
    createdDate: '2024-01-18T13:00:00Z',
    updatedDate: '2024-01-18T13:00:00Z'
  }
];

// Export mock data
// Mock tickets for User Story 2: View Ticket List
// All tickets are assumed to be created by the current user
const mockTicketsForList = [
  {
    ticketId: 1,
    title: 'Login issue on production',
    description: 'Users cannot login',
    statusId: 1,
    statusName: 'Pending',
    categoryId: 1,
    categoryName: 'Bug',
    systemId: 1,
    systemName: 'ERP-A',
    author: 'currentUser',
    createdDate: '2026-02-13T10:00:00Z',
    updatedDate: '2026-02-13T10:00:00Z'
  },
  {
    ticketId: 2,
    title: 'Database connection timeout',
    description: 'Connection pool exhausted',
    statusId: 2,
    statusName: 'In-Progress',
    categoryId: 1,
    categoryName: 'Bug',
    systemId: 2,
    systemName: 'ERP-B',
    author: 'currentUser',
    createdDate: '2026-02-12T11:00:00Z',
    updatedDate: '2026-02-12T15:00:00Z'
  },
  {
    ticketId: 3,
    title: 'Add new feature for reporting',
    description: 'Need custom reports',
    statusId: 2,
    statusName: 'In Progress',
    categoryId: 2,
    categoryName: 'Feature',
    systemId: 3,
    systemName: 'CRM',
    author: 'currentUser',
    createdDate: '2026-02-11T12:00:00Z',
    updatedDate: '2026-02-13T09:00:00Z'
  },
  {
    ticketId: 4,
    title: 'Performance optimization needed',
    description: 'Slow page load times',
    statusId: 3,
    statusName: 'Resolved',
    categoryId: 3,
    categoryName: 'Performance',
    systemId: 1,
    systemName: 'ERP-A',
    author: 'currentUser',
    createdDate: '2026-02-10T13:00:00Z',
    updatedDate: '2026-02-13T15:00:00Z'
  },
  {
    ticketId: 5,
    title: 'Security vulnerability found',
    description: 'XSS vulnerability detected',
    statusId: 3,
    statusName: 'Closed',
    categoryId: 4,
    categoryName: 'Security',
    systemId: 2,
    systemName: 'ERP-B',
    author: 'currentUser',
    createdDate: '2026-02-09T14:00:00Z',
    updatedDate: '2026-02-12T16:00:00Z'
  }
];

// Mock tickets with missing fields
const mockTicketsWithMissingFields = [
  {
    ticketId: 1,
    title: 'Ticket with missing status',
    statusId: null,
    statusName: null,
    categoryName: 'Bug',
    systemName: 'ERP-A',
    author: 'currentUser',
    createdDate: '2026-02-13T10:00:00Z',
    updatedDate: '2026-02-13T10:00:00Z'
  },
  {
    ticketId: 2,
    title: 'Ticket with missing system',
    statusName: 'Pending',
    categoryName: 'Feature',
    systemName: null,
    author: 'currentUser',
    createdDate: '2026-02-12T11:00:00Z',
    updatedDate: '2026-02-12T11:00:00Z'
  },
  {
    ticketId: 3,
    title: 'Ticket with missing category',
    statusName: 'In Progress',
    categoryName: null,
    systemName: 'CRM',
    author: 'currentUser',
    createdDate: '2026-02-11T12:00:00Z',
    updatedDate: '2026-02-11T12:00:00Z'
  },
  {
    ticketId: 4,
    title: 'Ticket with missing dates',
    statusName: 'Resolved',
    categoryName: 'Bug',
    systemName: 'ERP-A',
    author: 'currentUser',
    createdDate: null,
    updatedDate: null
  }
];

// Mock tickets from different users (for filtering tests)
const mockTicketsMixedAuthors = [
  {
    ticketId: 1,
    title: 'My Ticket 1',
    statusName: 'Pending',
    categoryName: 'Bug',
    systemName: 'ERP-A',
    author: 'currentUser',
    createdDate: '2026-02-13T10:00:00Z',
    updatedDate: '2026-02-13T10:00:00Z'
  },
  {
    ticketId: 2,
    title: 'Other User Ticket',
    statusName: 'In Progress',
    categoryName: 'Feature',
    systemName: 'ERP-B',
    author: 'otherUser',
    createdDate: '2026-02-12T11:00:00Z',
    updatedDate: '2026-02-12T11:00:00Z'
  },
  {
    ticketId: 3,
    title: 'My Ticket 2',
    statusName: 'Resolved',
    categoryName: 'Bug',
    systemName: 'CRM',
    author: 'currentUser',
    createdDate: '2026-02-11T12:00:00Z',
    updatedDate: '2026-02-11T12:00:00Z'
  },
  {
    ticketId: 4,
    title: 'Another User Ticket',
    statusName: 'Pending',
    categoryName: 'Performance',
    systemName: 'ERP-A',
    author: 'anotherUser',
    createdDate: '2026-02-10T13:00:00Z',
    updatedDate: '2026-02-10T13:00:00Z'
  }
];

// Mock tickets for User Story 3 & 4: Filter and Search
const mockTicketsForFilterAndSearch = [
  {
    ticketId: 1,
    title: 'Login issue on production',
    description: 'Users cannot login to the system',
    statusId: 1,
    statusName: 'Pending',
    categoryId: 1,
    categoryName: 'Bug',
    systemId: 1,
    systemName: 'ERP-A',
    author: 'currentUser',
    createdDate: '2026-02-13T10:00:00Z',
    updatedDate: '2026-02-13T10:00:00Z'
  },
  {
    ticketId: 2,
    title: 'Database connection timeout',
    description: 'Connection pool exhausted during peak hours',
    statusId: 2,
    statusName: 'In-Progress',
    categoryId: 1,
    categoryName: 'Bug',
    systemId: 2,
    systemName: 'ERP-B',
    author: 'currentUser',
    createdDate: '2026-02-12T11:00:00Z',
    updatedDate: '2026-02-12T15:00:00Z'
  },
  {
    ticketId: 3,
    title: 'Add new reporting feature',
    description: 'Need custom reports for management',
    statusId: 3,
    statusName: 'Resolved',
    categoryId: 2,
    categoryName: 'Feature',
    systemId: 1,
    systemName: 'ERP-A',
    author: 'currentUser',
    createdDate: '2026-02-11T12:00:00Z',
    updatedDate: '2026-02-13T09:00:00Z'
  },
  {
    ticketId: 4,
    title: 'Performance optimization needed',
    description: 'Slow page load times affecting users',
    statusId: 1,
    statusName: 'Pending',
    categoryId: 3,
    categoryName: 'Performance',
    systemId: 2,
    systemName: 'ERP-B',
    author: 'currentUser',
    createdDate: '2026-02-10T13:00:00Z',
    updatedDate: '2026-02-10T13:00:00Z'
  },
  {
    ticketId: 5,
    title: 'Security vulnerability found',
    description: 'XSS vulnerability detected in forms',
    statusId: 2,
    statusName: 'In-Progress',
    categoryId: 4,
    categoryName: 'Security',
    systemId: 3,
    systemName: 'CRM',
    author: 'currentUser',
    createdDate: '2026-02-09T14:00:00Z',
    updatedDate: '2026-02-12T16:00:00Z'
  },
  {
    ticketId: 6,
    title: 'Email notification not working',
    description: 'Users not receiving email alerts',
    statusId: 3,
    statusName: 'Resolved',
    categoryId: 1,
    categoryName: 'Bug',
    systemId: 1,
    systemName: 'ERP-A',
    author: 'currentUser',
    createdDate: '2026-02-08T15:00:00Z',
    updatedDate: '2026-02-11T17:00:00Z'
  },
  {
    ticketId: 7,
    title: 'Mobile app crashes on startup',
    description: 'App crashes immediately after launch',
    statusId: 1,
    statusName: 'Pending',
    categoryId: 1,
    categoryName: 'Bug',
    systemId: 3,
    systemName: 'CRM',
    author: 'currentUser',
    createdDate: '2026-02-07T16:00:00Z',
    updatedDate: '2026-02-07T16:00:00Z'
  },
  {
    ticketId: 8,
    title: 'Request for dark mode',
    description: 'Users want dark mode option',
    statusId: 2,
    statusName: 'In-Progress',
    categoryId: 2,
    categoryName: 'Feature',
    systemId: 2,
    systemName: 'ERP-B',
    author: 'currentUser',
    createdDate: '2026-02-06T17:00:00Z',
    updatedDate: '2026-02-10T18:00:00Z'
  }
];

// Mock tickets for testing date filtering
const mockTicketsForDateFilter = [
  {
    ticketId: 101,
    title: 'Today ticket',
    statusName: 'Pending',
    categoryName: 'Bug',
    systemName: 'ERP-A',
    createdDate: '2026-02-13T10:00:00Z',
    updatedDate: '2026-02-13T10:00:00Z'
  },
  {
    ticketId: 102,
    title: 'Yesterday ticket',
    statusName: 'In-Progress',
    categoryName: 'Feature',
    systemName: 'ERP-B',
    createdDate: '2026-02-12T10:00:00Z',
    updatedDate: '2026-02-12T10:00:00Z'
  },
  {
    ticketId: 103,
    title: 'Last week ticket',
    statusName: 'Resolved',
    categoryName: 'Bug',
    systemName: 'CRM',
    createdDate: '2026-02-06T10:00:00Z',
    updatedDate: '2026-02-06T10:00:00Z'
  },
  {
    ticketId: 104,
    title: 'Last month ticket',
    statusName: 'Pending',
    categoryName: 'Performance',
    systemName: 'ERP-A',
    createdDate: '2026-01-15T10:00:00Z',
    updatedDate: '2026-01-15T10:00:00Z'
  }
];

// Export mock data
module.exports = {
  mockTicketsAllStatuses,
  mockTicketsAllPending,
  mockTicketsAllInProgress,
  mockTicketsAllResolved,
  mockTicketsEmpty,
  mockTicketsEdgeCases,
  // US2: View Ticket List
  mockTicketsForList,
  mockTicketsWithMissingFields,
  mockTicketsMixedAuthors,
  // US3 & US4: Filter and Search
  mockTicketsForFilterAndSearch,
  mockTicketsForDateFilter
};