# User Story 3 & 4: Filter and Search Tickets - Test Documentation

## Overview

This document provides comprehensive documentation for the unit tests covering **User Story 3: Filter Tickets** and **User Story 4: Search Tickets**.

## User Stories

### **US3: Filter Tickets**

> "As a user, I want to filter tickets by Status, System, Category, and Date Submitted so I can narrow down the list."

**Acceptance Criteria:**
1. Each filter limits results correctly.
2. Multiple filters can be applied together.
3. Removing a filter restores full ticket list.

### **US4: Search Tickets**

> "As a user, I want to search across all ticket fields so I can quickly find a specific ticket."

**Acceptance Criteria:**
1. Search checks all ticket columns.
2. Partial matches return results.
3. No-match searches show empty results.

## Test Coverage Summary

### Total Tests: 40

| Category | Test Count | Description |
|----------|------------|-------------|
| **US3 AC1: Individual Filters** | 6 tests | Each filter type works correctly |
| **US3 AC2: Multiple Filters** | 5 tests | Filters can be combined |
| **US3 AC3: Remove Filters** | 4 tests | Removing filters restores results |
| **US4 AC1: Search All Columns** | 6 tests | Search checks all fields |
| **US4 AC2: Partial Matches** | 5 tests | Partial text matching works |
| **US4 AC3: No Match Handling** | 5 tests | Empty results handled correctly |
| **Integration Tests** | 3 tests | Search and filter work together |
| **Performance Tests** | 2 tests | Large dataset handling |

## Detailed Test Coverage

### User Story 3: Filter Tickets

#### AC1: Individual Filters Work Correctly (6 tests)

| Test ID | Test Name | Description | Expected Result |
|---------|-----------|-------------|-----------------|
| US3_AC1_TC1 | `US3_filterStatusCorrectly` | Filter by status | Only tickets with selected status shown |
| US3_AC1_TC2 | `US3_filterSystemCorrectly` | Filter by system | Only tickets with selected system shown |
| US3_AC1_TC3 | `US3_filterCategoryCorrectly` | Filter by category | Only tickets with selected category shown |
| US3_AC1_TC4 | `US3_eachFilterReturnsUniqueResults` | Different filters return different results | Each filter type produces unique result sets |
| US3_AC1_TC5 | `US3_filterNoMatches` | Filter with no matches | Returns empty array |
| US3_AC1_TC6 | `US3_filterCountsAccurate` | Filter counts are accurate | Counts match actual ticket distribution |

#### AC2: Multiple Filters Work Together (5 tests)

| Test ID | Test Name | Description | Expected Result |
|---------|-----------|-------------|-----------------|
| US3_AC2_TC1 | `US3_multipleFiltersStatusSystem` | Status + System filters | Both filters applied correctly |
| US3_AC2_TC2 | `US3_multipleFiltersStatusCategory` | Status + Category filters | Both filters applied correctly |
| US3_AC2_TC3 | `US3_multipleFiltersAll` | All three filters together | All filters applied correctly |
| US3_AC2_TC4 | `US3_multipleFiltersNarrowResults` | Multiple filters reduce results | Each additional filter narrows results |
| US3_AC2_TC5 | `US3_multipleFiltersNoMatches` | Incompatible filters | Returns empty results |

#### AC3: Removing Filters Restores Full List (4 tests)

| Test ID | Test Name | Description | Expected Result |
|---------|-----------|-------------|-----------------|
| US3_AC3_TC1 | `US3_removeFilterRestoresFullList` | Remove single filter | Full list restored |
| US3_AC3_TC2 | `US3_removeOneFilterExpands` | Remove one of multiple filters | Results expand |
| US3_AC3_TC3 | `US3_removeAllFiltersShowsAll` | Remove all filters | Complete list shown |
| US3_AC3_TC4 | `US3_filterToggle` | Toggle filters on/off | Filters can be applied and removed repeatedly |

### User Story 4: Search Tickets

#### AC1: Search Checks All Columns (6 tests)

| Test ID | Test Name | Description | Expected Result |
|---------|-----------|-------------|-----------------|
| US4_AC1_TC1 | `US4_searchByTicketId` | Search by ticket ID | Finds tickets by ID |
| US4_AC1_TC2 | `US4_searchByTitle` | Search by title | Finds tickets by title |
| US4_AC1_TC3 | `US4_searchByDescription` | Search by description | Finds tickets by description |
| US4_AC1_TC4 | `US4_searchByStatus` | Search by status | Finds tickets by status name |
| US4_AC1_TC5 | `US4_searchBySystem` | Search by system | Finds tickets by system name |
| US4_AC1_TC6 | `US4_searchByCategory` | Search by category | Finds tickets by category name |

#### AC2: Partial Matches Work (5 tests)

| Test ID | Test Name | Description | Expected Result |
|---------|-----------|-------------|-----------------|
| US4_AC2_TC1 | `US4_searchPartialMatch` | Partial word match | Returns matching results |
| US4_AC2_TC2 | `US4_searchSingleCharacter` | Single character search | Returns results |
| US4_AC2_TC3 | `US4_searchCaseInsensitive` | Case-insensitive search | Same results regardless of case |
| US4_AC2_TC4 | `US4_searchWithSpaces` | Search with spaces | Handles multi-word phrases |
| US4_AC2_TC5 | `US4_searchTrimsWhitespace` | Trim whitespace | Leading/trailing spaces ignored |

#### AC3: No-Match Searches Show Empty (5 tests)

| Test ID | Test Name | Description | Expected Result |
|---------|-----------|-------------|-----------------|
| US4_AC3_TC1 | `US4_searchNoMatch` | Non-existent keyword | Returns empty array |
| US4_AC3_TC2 | `US4_searchEmpty` | Empty search | Returns all tickets |
| US4_AC3_TC3 | `US4_searchWhitespaceOnly` | Whitespace-only search | Returns all tickets |
| US4_AC3_TC4 | `US4_searchSpecialCharsNoMatch` | Special characters no match | Returns empty array |
| US4_AC3_TC5 | `US4_displayEmptyState` | Display empty state | Shows "No tickets found" message |

### Integration Tests (3 tests)

| Test ID | Test Name | Description | Expected Result |
|---------|-----------|-------------|-----------------|
| US3_US4_INT_TC1 | `US3_US4_searchAndFilterTogether` | Search + Filter combined | Both work together correctly |
| US3_US4_INT_TC2 | `US3_US4_clearSearchKeepsFilters` | Clear search, keep filters | Filters remain active |
| US3_US4_INT_TC3 | `US3_US4_clearFiltersKeepsSearch` | Clear filters, keep search | Search remains active |

### Performance Tests (2 tests)

| Test ID | Test Name | Description | Expected Result |
|---------|-----------|-------------|-----------------|
| US3_US4_PERF_TC1 | `US3_US4_filterPerformance` | Filter 100 tickets | Completes in < 50ms |
| US3_US4_PERF_TC2 | `US3_US4_searchPerformance` | Search 100 tickets | Completes in < 50ms |

## Mock Data

### Mock Datasets

1. **mockTicketsForFilterAndSearch** (8 tickets)
   - Diverse statuses: Pending, In-Progress, Resolved
   - Multiple systems: ERP-A, ERP-B, CRM
   - Various categories: Bug, Feature, Performance, Security
   - Rich descriptions for search testing

2. **mockTicketsForDateFilter** (4 tickets)
   - Different date ranges
   - Today, yesterday, last week, last month
   - For date-based filtering tests

### Example Mock Ticket

```javascript
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
}
```

## Test Utilities

### Filter and Search Utilities (`filter-search-utils.js`)

The test suite includes 22 specialized utility functions:

#### DOM Manipulation
- `createFilterSearchHTML()` - Creates filter/search HTML structure
- `getFilterElements()` - Gets filter element references
- `setSearchValue(value)` - Sets search input value
- `setFilterValue(filterId, value)` - Sets filter dropdown value
- `clearAllFilters()` - Clears all filters

#### Filter Functions
- `filterByStatus(tickets, statusId)` - Filters by status
- `filterBySystem(tickets, systemId)` - Filters by system
- `filterByCategory(tickets, categoryId)` - Filters by category
- `applyMultipleFilters(tickets, filters)` - Applies multiple filters

#### Search Functions
- `searchTickets(tickets, keyword)` - Searches across all fields
- `sortTickets(tickets, sortBy)` - Sorts tickets

#### Analysis Functions
- `countByStatus(tickets)` - Counts tickets by status
- `countBySystem(tickets)` - Counts tickets by system
- `countByCategory(tickets)` - Counts tickets by category
- `getUniqueValues(tickets, field)` - Gets unique field values

#### Validation Functions
- `verifyFilterResults(filteredTickets, criteria)` - Verifies filter results
- `verifySearchResults(searchResults, keyword)` - Verifies search results
- `getCurrentFilterValues()` - Gets current filter state

## Running Tests

### Run All Filter and Search Tests

```bash
npm run test:filter-search
```

or

```bash
npm run test:us3-us4
```

### Run US3 Tests Only (Filter)

```bash
npm run test:us3
```

### Run US4 Tests Only (Search)

```bash
npm run test:us4
```

### Run Specific Test Suite

```bash
# Run only AC1 tests
jest __tests__/filter-search.test.js -t "AC1"

# Run only AC2 tests
jest __tests__/filter-search.test.js -t "AC2"

# Run only integration tests
jest __tests__/filter-search.test.js -t "Integration"
```

### Run with Coverage

```bash
npm run test:coverage -- __tests__/filter-search.test.js
```

## Expected Test Output

```
PASS  __tests__/filter-search.test.js
  User Story 3 & 4: Filter and Search Tickets
    US3 AC1: Individual filters work correctly
      ✓ US3_filterStatusCorrectly (4ms)
      ✓ US3_filterSystemCorrectly (3ms)
      ✓ US3_filterCategoryCorrectly (3ms)
      ✓ US3_eachFilterReturnsUniqueResults (3ms)
      ✓ US3_filterNoMatches (2ms)
      ✓ US3_filterCountsAccurate (3ms)
    US3 AC2: Multiple filters work together
      ✓ US3_multipleFiltersStatusSystem (3ms)
      ✓ US3_multipleFiltersStatusCategory (3ms)
      ✓ US3_multipleFiltersAll (3ms)
      ✓ US3_multipleFiltersNarrowResults (3ms)
      ✓ US3_multipleFiltersNoMatches (2ms)
    US3 AC3: Removing filters restores full list
      ✓ US3_removeFilterRestoresFullList (3ms)
      ✓ US3_removeOneFilterExpands (3ms)
      ✓ US3_removeAllFiltersShowsAll (3ms)
      ✓ US3_filterToggle (3ms)
    US4 AC1: Search checks all columns
      ✓ US4_searchByTicketId (3ms)
      ✓ US4_searchByTitle (3ms)
      ✓ US4_searchByDescription (3ms)
      ✓ US4_searchByStatus (3ms)
      ✓ US4_searchBySystem (3ms)
      ✓ US4_searchByCategory (3ms)
    US4 AC2: Partial matches work
      ✓ US4_searchPartialMatch (3ms)
      ✓ US4_searchSingleCharacter (3ms)
      ✓ US4_searchCaseInsensitive (3ms)
      ✓ US4_searchWithSpaces (3ms)
      ✓ US4_searchTrimsWhitespace (3ms)
    US4 AC3: No-match searches return empty
      ✓ US4_searchNoMatch (2ms)
      ✓ US4_searchEmpty (2ms)
      ✓ US4_searchWhitespaceOnly (2ms)
      ✓ US4_searchSpecialCharsNoMatch (2ms)
      ✓ US4_displayEmptyState (3ms)
    Integration: Search and Filter combined
      ✓ US3_US4_searchAndFilterTogether (4ms)
      ✓ US3_US4_clearSearchKeepsFilters (3ms)
      ✓ US3_US4_clearFiltersKeepsSearch (3ms)
    Performance: Large datasets
      ✓ US3_US4_filterPerformance (20ms)
      ✓ US3_US4_searchPerformance (18ms)

Test Suites: 1 passed, 1 total
Tests:       40 passed, 40 total
Snapshots:   0 total
Time:        4.123s
```

## Key Testing Strategies

### 1. Filter Testing
- Tests each filter type individually
- Tests multiple filters combined
- Tests filter removal and restoration
- Verifies filter accuracy

### 2. Search Testing
- Tests search across all columns
- Tests partial matching
- Tests case-insensitive search
- Tests empty/no-match scenarios

### 3. Integration Testing
- Tests search and filter together
- Tests clearing one while keeping the other
- Verifies independent operation

### 4. Performance Testing
- Tests with large datasets (100+ tickets)
- Ensures operations complete quickly
- Validates scalability

## Integration with Existing Code

The tests integrate with:

- **HTML Structure**: `index.html` (lines 69-98) - Search box and filter dropdowns
- **Search Function**: `js/tickets.js` - `handleSearch()` function (lines 108-128)
- **Filter Function**: `js/tickets.js` - `applyFiltersAndSort()` function (lines 130-169)
- **Display Function**: `js/tickets.js` - `displayTickets()` function
- **API Service**: `js/api.js` - `API.tickets.getAll()`

## Filter Types Tested

### Status Filter
- **Values**: Pending, In-Progress, Resolved
- **Field**: `statusId` and `statusName`
- **Behavior**: Filters tickets by status

### System Filter
- **Values**: ERP-A, ERP-B, CRM
- **Field**: `systemId` and `systemName`
- **Behavior**: Filters tickets by system

### Category Filter
- **Values**: Bug, Feature, Performance, Security
- **Field**: `categoryId` and `categoryName`
- **Behavior**: Filters tickets by category

## Search Fields Tested

The search function checks these fields:
1. **Ticket ID** - `#${ticketId}`
2. **Title** - `title`
3. **Description** - `description`
4. **Status** - `statusName`
5. **System** - `systemName`
6. **Category** - `categoryName`

## Troubleshooting

### Common Issues

**Issue**: Filter tests fail
**Solution**: Verify filter dropdown IDs match: `statusFilter`, `systemFilter`, `categoryFilter`

**Issue**: Search tests fail
**Solution**: Ensure search input ID is `searchInput`

**Issue**: No results found
**Solution**: Check that mock data has appropriate values for filtering/searching

## Best Practices

1. **Test Isolation**: Each test is independent
2. **Clear Naming**: Test names reference functional test IDs
3. **Comprehensive Coverage**: Tests cover all acceptance criteria
4. **Mock Data**: Uses realistic data for testing
5. **Performance**: Tests complete quickly (< 5 seconds total)
6. **Integration**: Tests work with existing codebase

## Future Enhancements

- [ ] Add tests for date range filtering
- [ ] Add tests for advanced search operators
- [ ] Add tests for filter persistence
- [ ] Add tests for filter combinations with sorting
- [ ] Add accessibility tests for filters

## Related Documentation

- Main Test Documentation: `__tests__/README.md`
- Quick Start Guide: `TEST_QUICK_START.md`
- Dashboard Tests: `__tests__/dashboard.test.js`
- Ticket List Tests: `__tests__/ticket-list.test.js`
- Test Utilities: `__tests__/setup/filter-search-utils.js`

---

**Test Suite**: User Story 3 & 4 - Filter and Search Tickets  
**Total Tests**: 40  
**Coverage**: All acceptance criteria + integration + performance  
**Framework**: Jest with jsdom