# Dashboard Unit Tests - User Story 1: View Dashboard Summary

## Overview

This test suite provides comprehensive unit testing for the Dashboard component of the Ticketing System frontend application. The tests are written using **Jest** with **jsdom** to simulate browser DOM interactions.

## User Story

**US1: View Dashboard Summary**

> "As a user, I want to see the total number of tickets by status so that I can understand my ticket workload."

### Acceptance Criteria

1. **AC1**: Pending, In-Progress, and Resolved counts are visible.
2. **AC2**: Counts match the number of tickets in each status.

## Test Coverage

### Acceptance Criterion 1: Status Counts Visibility (5 tests)

| Test ID | Test Name | Description |
|---------|-----------|-------------|
| US1_AC1_TC1 | `US1_AC1_pendingCountVisible` | Verifies pending count element exists and is visible |
| US1_AC1_TC2 | `US1_AC1_inProgressCountVisible` | Verifies in-progress count element exists and is visible |
| US1_AC1_TC3 | `US1_AC1_resolvedCountVisible` | Verifies resolved count element exists and is visible |
| US1_AC1_TC4 | `US1_AC1_allStatCardsVisible` | Verifies all three stat cards are present |
| US1_AC1_TC5 | `US1_AC1_initialCountsDisplayed` | Verifies initial count values are displayed |

### Acceptance Criterion 2: Count Accuracy (8 tests)

| Test ID | Test Name | Description |
|---------|-----------|-------------|
| US1_AC2_TC1 | `US1_AC2_pendingCountMatchesTickets` | Verifies pending count equals number of pending tickets |
| US1_AC2_TC2 | `US1_AC2_inProgressCountMatchesTickets` | Verifies in-progress count equals number of in-progress tickets |
| US1_AC2_TC3 | `US1_AC2_resolvedCountMatchesTickets` | Verifies resolved count equals number of resolved tickets |
| US1_AC2_TC4 | `US1_AC2_allCountsAccurate` | Verifies all counts with mixed ticket statuses |
| US1_AC2_TC5 | `US1_AC2_allPendingTickets` | Tests when all tickets are pending |
| US1_AC2_TC6 | `US1_AC2_allInProgressTickets` | Tests when all tickets are in progress |
| US1_AC2_TC7 | `US1_AC2_allResolvedTickets` | Tests when all tickets are resolved |
| US1_AC2_TC8 | `US1_AC2_emptyTickets` | Tests when no tickets exist |

### Edge Cases (7 tests)

| Test ID | Test Name | Description |
|---------|-----------|-------------|
| US1_EDGE_TC1 | `US1_EDGE_nullStatusNames` | Handles tickets with null status names |
| US1_EDGE_TC2 | `US1_EDGE_undefinedStatusNames` | Handles tickets with undefined status names |
| US1_EDGE_TC3 | `US1_EDGE_emptyStatusNames` | Handles tickets with empty string status names |
| US1_EDGE_TC4 | `US1_EDGE_caseInsensitiveStatus` | Verifies case-insensitive status matching |
| US1_EDGE_TC5 | `US1_EDGE_statusWithWhitespace` | Handles status names with whitespace |
| US1_EDGE_TC6 | `US1_EDGE_alternativeStatusNames` | Recognizes alternative status names (Open, Closed) |
| US1_EDGE_TC7 | `US1_EDGE_unknownStatusNames` | Handles unknown status names gracefully |

### Performance Tests (2 tests)

| Test ID | Test Name | Description |
|---------|-----------|-------------|
| US1_PERF_TC1 | `US1_PERF_largeDataset` | Tests with 1000 tickets |
| US1_PERF_TC2 | `US1_PERF_rapidUpdates` | Tests multiple rapid updates |

**Total Tests: 22**

## Project Structure

```
Ticketing System - POC/
├── __tests__/
│   ├── setup/
│   │   ├── jest.setup.js          # Global test setup and utilities
│   │   └── test-utils.js          # Helper functions for testing
│   ├── mocks/
│   │   ├── api.mock.js            # Mock API service
│   │   └── ticket-data.mock.js    # Mock ticket data
│   ├── dashboard.test.js          # Dashboard unit tests
│   └── README.md                  # This file
├── jest.config.js                 # Jest configuration
├── package.json                   # Dependencies and scripts
└── js/
    ├── api.js                     # API service (production code)
    ├── tickets.js                 # Tickets logic (production code)
    └── utils.js                   # Utility functions (production code)
```

## Installation

### Prerequisites

- Node.js (v14 or higher)
- npm or yarn

### Install Dependencies

```bash
npm install
```

This will install:
- `jest` (v29.7.0) - Testing framework
- `jest-environment-jsdom` (v29.7.0) - DOM simulation

## Running Tests

### Run All Tests

```bash
npm test
```

### Run Tests in Watch Mode

```bash
npm run test:watch
```

### Run Tests with Coverage Report

```bash
npm run test:coverage
```

### Run Dashboard Tests Only

```bash
npm run test:dashboard
```

### Run Tests with Verbose Output

```bash
npm run test:verbose
```

### Run Tests in CI Environment

```bash
npm run test:ci
```

## Test Output Examples

### Successful Test Run

```
PASS  __tests__/dashboard.test.js
  User Story 1: View Dashboard Summary
    AC1: Status counts are visible
      ✓ US1_AC1_pendingCountVisible - Pending count element exists and is visible (5ms)
      ✓ US1_AC1_inProgressCountVisible - In Progress count element exists and is visible (2ms)
      ✓ US1_AC1_resolvedCountVisible - Resolved count element exists and is visible (2ms)
      ✓ US1_AC1_allStatCardsVisible - All three stat cards are visible on dashboard (3ms)
      ✓ US1_AC1_initialCountsDisplayed - Initial count values are displayed (2ms)
    AC2: Counts match ticket statuses
      ✓ US1_AC2_pendingCountMatchesTickets - Pending count equals number of pending tickets (3ms)
      ✓ US1_AC2_inProgressCountMatchesTickets - In Progress count equals number of in-progress tickets (2ms)
      ✓ US1_AC2_resolvedCountMatchesTickets - Resolved count equals number of resolved tickets (2ms)
      ✓ US1_AC2_allCountsAccurate - All counts are accurate with mixed ticket statuses (3ms)
      ✓ US1_AC2_allPendingTickets - Counts are correct when all tickets are pending (2ms)
      ✓ US1_AC2_allInProgressTickets - Counts are correct when all tickets are in progress (2ms)
      ✓ US1_AC2_allResolvedTickets - Counts are correct when all tickets are resolved (2ms)
      ✓ US1_AC2_emptyTickets - Counts are zero when no tickets exist (2ms)
    Edge Cases: Status name variations
      ✓ US1_EDGE_nullStatusNames - Handles tickets with null status names gracefully (2ms)
      ✓ US1_EDGE_undefinedStatusNames - Handles tickets with undefined status names gracefully (2ms)
      ✓ US1_EDGE_emptyStatusNames - Handles tickets with empty string status names (2ms)
      ✓ US1_EDGE_caseInsensitiveStatus - Status matching is case-insensitive (3ms)
      ✓ US1_EDGE_statusWithWhitespace - Handles status names with whitespace (2ms)
      ✓ US1_EDGE_alternativeStatusNames - Recognizes alternative status names (2ms)
      ✓ US1_EDGE_unknownStatusNames - Unknown status names are not counted (2ms)
    Performance: Large datasets
      ✓ US1_PERF_largeDataset - Handles large number of tickets (15ms)
      ✓ US1_PERF_rapidUpdates - Handles multiple rapid updates (3ms)

Test Suites: 1 passed, 1 total
Tests:       22 passed, 22 total
Snapshots:   0 total
Time:        2.345s
```

### Coverage Report

```
--------------------|---------|----------|---------|---------|-------------------
File                | % Stmts | % Branch | % Funcs | % Lines | Uncovered Line #s
--------------------|---------|----------|---------|---------|-------------------
All files           |   95.23 |    88.46 |   100.0 |   95.23 |
 dashboard.test.js  |   100.0 |    100.0 |   100.0 |   100.0 |
 api.mock.js        |   92.85 |    85.71 |   100.0 |   92.85 | 45-47
 test-utils.js      |   94.44 |    87.50 |   100.0 |   94.44 | 89-91
--------------------|---------|----------|---------|---------|-------------------
```

## Mock Data

### Mock Ticket Datasets

The test suite uses several predefined mock datasets:

1. **mockTicketsAllStatuses** - Mixed statuses (2 pending, 1 in-progress, 2 resolved)
2. **mockTicketsAllPending** - All pending tickets (3 tickets)
3. **mockTicketsAllInProgress** - All in-progress tickets (2 tickets)
4. **mockTicketsAllResolved** - All resolved tickets (4 tickets)
5. **mockTicketsEmpty** - Empty array (0 tickets)
6. **mockTicketsEdgeCases** - Edge case scenarios (null, undefined, empty statuses)

### Example Mock Ticket

```javascript
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
}
```

## Status Categorization Logic

The dashboard categorizes tickets based on their `statusName` field:

- **Pending**: Status contains "open" or "pending" (case-insensitive)
- **In Progress**: Status contains "progress" (case-insensitive)
- **Resolved**: Status contains "resolved" or "closed" (case-insensitive)

## Key Testing Strategies

### 1. DOM Simulation
- Uses jsdom to create a realistic browser environment
- Simulates the actual HTML structure from `index.html`
- Tests DOM element visibility and content

### 2. API Mocking
- Mocks `API.tickets.getAll()` to avoid real backend calls
- Provides controlled test data for predictable results
- Simulates both success and error scenarios

### 3. Isolation
- Each test is independent and isolated
- Setup and teardown ensure clean state
- No test depends on another test's execution

### 4. Comprehensive Coverage
- Tests happy paths (normal operation)
- Tests edge cases (null, undefined, empty values)
- Tests error handling
- Tests performance with large datasets

## Troubleshooting

### Common Issues

#### Issue: Tests fail with "Cannot find module"
**Solution**: Run `npm install` to ensure all dependencies are installed.

#### Issue: jsdom errors
**Solution**: Ensure `jest-environment-jsdom` is installed:
```bash
npm install --save-dev jest-environment-jsdom
```

#### Issue: Tests timeout
**Solution**: Increase Jest timeout in `jest.config.js`:
```javascript
testTimeout: 10000
```

#### Issue: Coverage not generated
**Solution**: Run with coverage flag:
```bash
npm run test:coverage
```

## Best Practices

1. **Test Naming**: Use descriptive names that reference functional test IDs
2. **Comments**: Include comments linking tests to acceptance criteria
3. **Arrange-Act-Assert**: Follow AAA pattern for test structure
4. **Mock Data**: Use realistic mock data that represents actual use cases
5. **Assertions**: Use specific assertions that clearly indicate what's being tested
6. **Cleanup**: Always clean up after tests to prevent side effects

## Continuous Integration

These tests are designed to run in CI/CD pipelines:

```yaml
# Example GitHub Actions workflow
- name: Run Tests
  run: npm run test:ci
  
- name: Upload Coverage
  uses: codecov/codecov-action@v3
  with:
    files: ./coverage/lcov.info
```

## Future Enhancements

- [ ] Add integration tests for full page load
- [ ] Add visual regression tests
- [ ] Add accessibility tests
- [ ] Add performance benchmarks
- [ ] Add E2E tests with Playwright or Cypress

## Support

For questions or issues with the tests, please contact the QA team or refer to the Jest documentation:
- [Jest Documentation](https://jestjs.io/docs/getting-started)
- [jsdom Documentation](https://github.com/jsdom/jsdom)

## License

This test suite is part of the Ticketing System project.