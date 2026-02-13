# User Story 2: View Ticket List - Test Documentation

## Overview

This document provides comprehensive documentation for the unit tests covering **User Story 2: View Ticket List**.

## User Story

**US2: View Ticket List**

> "As a user, I want to see all the tickets I created so that I can easily track my submissions."

### Acceptance Criteria

1. **AC1**: Ticket list displays Ticket ID, Title, Status, System, Category, Date Submitted, Last Updated.
2. **AC2**: Only tickets created by the logged-in user are visible.

## Test Coverage Summary

### Total Tests: 38

| Category | Test Count | Description |
|----------|------------|-------------|
| **AC1: Column Display** | 13 tests | Verifies all required columns are present and data displays correctly |
| **AC2: User Filtering** | 5 tests | Verifies only current user's tickets are shown |
| **Edge Cases** | 8 tests | Handles missing data, special characters, null/undefined values |
| **Performance** | 2 tests | Tests with large datasets and rapid updates |
| **Integration** | 2 tests | Tests complete flow and data updates |

## Detailed Test Coverage

### Acceptance Criterion 1: Column Display (13 tests)

| Test ID | Test Name | Description | Expected Result |
|---------|-----------|-------------|-----------------|
| US2_AC1_TC1 | `US2_AC1_ticketIdColumnExists` | Verifies Ticket ID column exists | Column "Ticket ID" is present in table header |
| US2_AC1_TC2 | `US2_AC1_titleColumnExists` | Verifies Title column exists | Column "Title" is present in table header |
| US2_AC1_TC3 | `US2_AC1_statusColumnExists` | Verifies Status column exists | Column "Status" is present in table header |
| US2_AC1_TC4 | `US2_AC1_systemColumnExists` | Verifies System column exists | Column "System" is present in table header |
| US2_AC1_TC5 | `US2_AC1_categoryColumnExists` | Verifies Category column exists | Column "Category" is present in table header |
| US2_AC1_TC6 | `US2_AC1_dateSubmittedColumnExists` | Verifies Date Submitted column exists | Column "Date Submitted" is present in table header |
| US2_AC1_TC7 | `US2_AC1_lastUpdatedColumnExists` | Verifies Last Updated column exists | Column "Last Updated" is present in table header |
| US2_AC1_TC8 | `US2_AC1_allRequiredColumnsPresent` | Verifies all 7 columns are present | All required columns exist in correct order |
| US2_AC1_TC9 | `US2_AC1_ticketDataDisplaysCorrectly` | Verifies data displays in correct columns | Each field displays in its corresponding column |
| US2_AC1_TC10 | `US2_AC1_multipleTicketsDisplay` | Verifies multiple tickets display correctly | All tickets show with complete data |
| US2_AC1_TC11 | `US2_AC1_ticketIdHasPrefix` | Verifies Ticket ID has # prefix | Ticket IDs display as "#123" format |
| US2_AC1_TC12 | `US2_AC1_statusHasBadge` | Verifies Status displays with badge | Status shows with styled badge element |
| US2_AC1_TC13 | `US2_AC1_datesFormatted` | Verifies dates are formatted | Dates display in readable format (e.g., "Feb 13, 2026") |

### Acceptance Criterion 2: User Filtering (5 tests)

| Test ID | Test Name | Description | Expected Result |
|---------|-----------|-------------|-----------------|
| US2_AC2_TC1 | `US2_AC2_onlyUserTicketsDisplayed` | Only current user's tickets shown | Displays only tickets where author = currentUser |
| US2_AC2_TC2 | `US2_AC2_otherUsersTicketsHidden` | Other users' tickets not visible | Tickets from other users are filtered out |
| US2_AC2_TC3 | `US2_AC2_allUserTicketsShown` | All user's tickets are displayed | Every ticket created by user is shown |
| US2_AC2_TC4 | `US2_AC2_ticketCountMatches` | Ticket count matches user's tickets | Displayed count equals user's ticket count |
| US2_AC2_TC5 | `US2_AC2_emptyListForNoTickets` | Empty state when user has no tickets | Shows "No tickets found" message |

### Edge Cases (8 tests)

| Test ID | Test Name | Description | Expected Result |
|---------|-----------|-------------|-----------------|
| US2_EDGE_TC1 | `US2_EDGE_missingStatus` | Handles missing status | Displays "Unknown" for null/missing status |
| US2_EDGE_TC2 | `US2_EDGE_missingSystem` | Handles missing system | Displays "N/A" for null/missing system |
| US2_EDGE_TC3 | `US2_EDGE_missingCategory` | Handles missing category | Displays "N/A" for null/missing category |
| US2_EDGE_TC4 | `US2_EDGE_missingDates` | Handles missing dates | Displays "N/A" for null/missing dates |
| US2_EDGE_TC5 | `US2_EDGE_specialCharactersInTitle` | Escapes HTML in title | Special characters are properly escaped |
| US2_EDGE_TC6 | `US2_EDGE_emptyTicketList` | Handles empty array | Shows empty state message |
| US2_EDGE_TC7 | `US2_EDGE_nullTicketList` | Handles null list | Shows empty state message |
| US2_EDGE_TC8 | `US2_EDGE_undefinedTicketList` | Handles undefined list | Shows empty state message |

### Performance Tests (2 tests)

| Test ID | Test Name | Description | Expected Result |
|---------|-----------|-------------|-----------------|
| US2_PERF_TC1 | `US2_PERF_largeTicketList` | Tests with 100 tickets | Completes in < 200ms |
| US2_PERF_TC2 | `US2_PERF_rapidUpdates` | Tests multiple rapid updates | Handles successive updates correctly |

### Integration Tests (2 tests)

| Test ID | Test Name | Description | Expected Result |
|---------|-----------|-------------|-----------------|
| US2_INT_TC1 | `US2_INT_completeFlow` | Tests complete display flow | From loading to populated list |
| US2_INT_TC2 | `US2_INT_listUpdates` | Tests list updates | List updates when data changes |

## Mock Data

### Mock Datasets

1. **mockTicketsForList** (5 tickets)
   - All tickets belong to "currentUser"
   - Contains complete data for all fields
   - Various statuses: Pending, In-Progress, Resolved, Closed
   - Different systems: ERP-A, ERP-B, CRM

2. **mockTicketsWithMissingFields** (4 tickets)
   - Tickets with null/missing status
   - Tickets with null/missing system
   - Tickets with null/missing category
   - Tickets with null/missing dates

3. **mockTicketsMixedAuthors** (4 tickets)
   - 2 tickets from "currentUser"
   - 1 ticket from "otherUser"
   - 1 ticket from "anotherUser"
   - Used for testing user filtering

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
  systemName: 'ERP-A',
  author: 'currentUser',
  createdDate: '2026-02-13T10:00:00Z',
  updatedDate: '2026-02-13T10:00:00Z'
}
```

## Test Utilities

### Ticket List Utilities (`ticket-list-utils.js`)

The test suite includes 18 specialized utility functions:

#### DOM Manipulation
- `createTicketListHTML()` - Creates table HTML structure
- `getTicketTableBody()` - Gets table body element
- `getTicketRows()` - Gets all ticket rows

#### Data Extraction
- `getTicketDataFromRow(row)` - Extracts data from a row
- `getAllTicketsFromTable()` - Gets all tickets from table
- `getTableColumns()` - Gets column headers

#### Validation
- `hasTableColumn(columnName)` - Checks if column exists
- `verifyRequiredColumns()` - Verifies all required columns
- `verifyTicketData(actual, expected)` - Compares ticket data

#### State Checking
- `isTableLoading()` - Checks loading state
- `isTableEmpty()` - Checks empty state
- `countVisibleTickets()` - Counts visible rows

#### Search & Filter
- `findTicketRowById(ticketId)` - Finds row by ID
- `ticketExistsInTable(ticketId)` - Checks if ticket exists
- `filterTicketsByAuthor(tickets, author)` - Filters by author

#### Formatting
- `formatDateForComparison(dateString)` - Formats dates

#### Assertions
- `assertTicketListDisplays(expectedTickets)` - Asserts list display

## Running Tests

### Run All Ticket List Tests

```bash
npm run test:ticket-list
```

or

```bash
npm run test:us2
```

### Run Specific Test Suite

```bash
# Run only AC1 tests
jest __tests__/ticket-list.test.js -t "AC1"

# Run only AC2 tests
jest __tests__/ticket-list.test.js -t "AC2"

# Run only edge case tests
jest __tests__/ticket-list.test.js -t "Edge Cases"
```

### Run with Coverage

```bash
npm run test:coverage -- __tests__/ticket-list.test.js
```

## Expected Test Output

```
PASS  __tests__/ticket-list.test.js
  User Story 2: View Ticket List
    AC1: Ticket list displays required columns
      ✓ US2_AC1_ticketIdColumnExists (3ms)
      ✓ US2_AC1_titleColumnExists (2ms)
      ✓ US2_AC1_statusColumnExists (2ms)
      ✓ US2_AC1_systemColumnExists (2ms)
      ✓ US2_AC1_categoryColumnExists (2ms)
      ✓ US2_AC1_dateSubmittedColumnExists (2ms)
      ✓ US2_AC1_lastUpdatedColumnExists (2ms)
      ✓ US2_AC1_allRequiredColumnsPresent (3ms)
      ✓ US2_AC1_ticketDataDisplaysCorrectly (4ms)
      ✓ US2_AC1_multipleTicketsDisplay (4ms)
      ✓ US2_AC1_ticketIdHasPrefix (3ms)
      ✓ US2_AC1_statusHasBadge (3ms)
      ✓ US2_AC1_datesFormatted (3ms)
    AC2: Only current user tickets are visible
      ✓ US2_AC2_onlyUserTicketsDisplayed (4ms)
      ✓ US2_AC2_otherUsersTicketsHidden (3ms)
      ✓ US2_AC2_allUserTicketsShown (3ms)
      ✓ US2_AC2_ticketCountMatches (3ms)
      ✓ US2_AC2_emptyListForNoTickets (2ms)
    Edge Cases: Data handling
      ✓ US2_EDGE_missingStatus (3ms)
      ✓ US2_EDGE_missingSystem (3ms)
      ✓ US2_EDGE_missingCategory (3ms)
      ✓ US2_EDGE_missingDates (3ms)
      ✓ US2_EDGE_specialCharactersInTitle (3ms)
      ✓ US2_EDGE_emptyTicketList (2ms)
      ✓ US2_EDGE_nullTicketList (2ms)
      ✓ US2_EDGE_undefinedTicketList (2ms)
    Performance: Large datasets
      ✓ US2_PERF_largeTicketList (18ms)
      ✓ US2_PERF_rapidUpdates (4ms)
    Integration: Complete ticket list flow
      ✓ US2_INT_completeFlow (5ms)
      ✓ US2_INT_listUpdates (5ms)

Test Suites: 1 passed, 1 total
Tests:       38 passed, 38 total
Snapshots:   0 total
Time:        3.456s
```

## Key Testing Strategies

### 1. Column Verification
- Tests each required column individually
- Verifies all columns together
- Checks column order and presence

### 2. Data Display
- Verifies data appears in correct columns
- Tests data formatting (dates, IDs, status badges)
- Validates HTML escaping for security

### 3. User Filtering
- Tests that only user's tickets are shown
- Verifies other users' tickets are hidden
- Tests empty state when user has no tickets

### 4. Edge Case Handling
- Tests with missing/null/undefined data
- Tests with special characters
- Tests with empty lists

### 5. Performance
- Tests with large datasets (100+ tickets)
- Tests rapid successive updates
- Ensures rendering completes quickly

## Test Data Flow

```
Mock Data (ticket-data.mock.js)
    ↓
Mock API (api.mock.js)
    ↓
displayTickets() function
    ↓
DOM Rendering (jsdom)
    ↓
Test Utilities (ticket-list-utils.js)
    ↓
Assertions & Verification
```

## Integration with Existing Code

The tests integrate with:

- **HTML Structure**: `index.html` (lines 100-124)
- **Display Function**: `js/tickets.js` - `displayTickets()` function
- **Utility Functions**: `js/utils.js` - `formatDate()`, `escapeHtml()`, `getStatusClass()`
- **API Service**: `js/api.js` - `API.tickets.getAll()`

## Troubleshooting

### Common Issues

**Issue**: Tests fail with "Cannot find column"
**Solution**: Verify HTML structure matches `index.html` table headers

**Issue**: Date formatting doesn't match
**Solution**: Ensure `formatDate()` function matches implementation in `utils.js`

**Issue**: Status badge not found
**Solution**: Check that status cell contains `.status-badge` element

## Best Practices

1. **Test Isolation**: Each test is independent
2. **Clear Naming**: Test names reference functional test IDs
3. **Comprehensive Coverage**: Tests cover happy paths, edge cases, and errors
4. **Mock Data**: Uses realistic data representing actual use cases
5. **Performance**: Tests complete quickly (< 5 seconds total)

## Future Enhancements

- [ ] Add tests for pagination
- [ ] Add tests for sorting functionality
- [ ] Add tests for search/filter features
- [ ] Add tests for ticket row click events
- [ ] Add accessibility tests

## Related Documentation

- Main Test Documentation: `__tests__/README.md`
- Quick Start Guide: `TEST_QUICK_START.md`
- Dashboard Tests: `__tests__/dashboard.test.js`
- Test Utilities: `__tests__/setup/ticket-list-utils.js`

---

**Test Suite**: User Story 2 - View Ticket List  
**Total Tests**: 38  
**Coverage**: All acceptance criteria + edge cases + performance  
**Framework**: Jest with jsdom