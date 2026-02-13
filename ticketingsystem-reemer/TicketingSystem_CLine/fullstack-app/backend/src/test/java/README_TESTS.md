# Unit Test Documentation

## Overview
This document describes the comprehensive unit test suite for the Ticketing Management System. All tests are written using JUnit 5 and Mockito, following the requirements document specifications.

## Test Coverage

### 1. TicketServiceTest.java
**Purpose**: Tests core ticket service operations

**Coverage Areas**:
- **User Dashboard**
  - Verify correct counts of pending, in-progress, and resolved tickets
  - Verify ticket list fields (Ticket ID, Title, Status, System, Category, Date submitted, Last Updated)
  - Filter by Status, System, Category
  - Search across all columns
  - Behavior when no tickets exist

- **Create New Ticket**
  - Creation with valid inputs (Title, System name, Category, Description, optional attachments)
  - Timestamps are set correctly
  - Long inputs (title, description)
  - Special characters and non-ASCII characters

- **View Ticket**
  - Retrieve ticket details by ID
  - Verify all ticket details are present
  - Behavior when ticket does not exist

- **Update Ticket**
  - Update ticket details
  - Status change triggers history
  - Update date is refreshed
  - Non-existent ticket handling

- **Delete Ticket**
  - Successful deletion
  - Non-existent ticket handling

- **Edge Cases**
  - Filter by non-existent status/system/category
  - Search with empty string
  - Multiple tickets with same status
  - Case-insensitive search

**Test Count**: 40+ tests

---

### 2. TicketCommentServiceTest.java
**Purpose**: Tests ticket comment functionality

**Coverage Areas**:
- **Add Comment**
  - Create comment successfully
  - Comment with long text (500 chars)
  - Special characters
  - Non-ASCII characters
  - Timestamp automatically set

- **Retrieve Comments**
  - Get all comments
  - Get comment by ID
  - Get comments by ticket ID
  - No comments for ticket

- **Update Comment**
  - Update comment text
  - Update with author
  - Preserve author if not provided
  - Non-existent comment

- **Delete Comment**
  - Successful deletion
  - Non-existent comment

- **Edge Cases**
  - Empty/null comment text
  - Multiple comments on same ticket
  - Whitespace only
  - Line breaks in comments

- **Verification**
  - Comment cannot modify ticket details

**Test Count**: 25+ tests

---

### 3. TicketAttachmentServiceTest.java
**Purpose**: Tests ticket attachment functionality

**Coverage Areas**:
- **Create Attachment**
  - Single attachment
  - Different file types (PDF, PNG, DOCX, etc.)
  - Long file path
  - Special characters in path
  - Spaces in path

- **Retrieve Attachments**
  - Get all attachments
  - Get attachment by ID
  - Get attachments by ticket ID
  - No attachments for ticket

- **Update Attachment**
  - Update file path
  - Non-existent attachment

- **Delete Attachment**
  - Successful deletion
  - Non-existent attachment

- **Edge Cases**
  - No attachments
  - Single attachment
  - Multiple attachments (4+)
  - Empty/null file path
  - Different file types
  - Non-ASCII characters in path

- **Verification**
  - Attachment associated with correct ticket

**Test Count**: 25+ tests

---

### 4. TicketControllerTest.java
**Purpose**: Tests REST API endpoints

**Coverage Areas**:
- **GET /api/tickets**
  - Get all tickets successfully
  - Empty list

- **GET /api/tickets/{id}**
  - Get ticket by ID successfully
  - Ticket not found
  - Verify all ticket details

- **POST /api/tickets**
  - Create ticket successfully
  - Create with valid inputs
  - Long title
  - Special characters
  - Non-ASCII characters
  - Immediate retrieval after creation

- **PUT /api/tickets/{id}**
  - Update ticket successfully
  - Update non-existent ticket

- **DELETE /api/tickets/{id}**
  - Delete ticket successfully
  - Delete non-existent ticket

- **Filter Endpoints**
  - Filter by Status (Pending, In Progress, Resolved)
  - Filter by System Name
  - Filter by Category
  - Filter by Author
  - No tickets with filter

- **Search Endpoint**
  - Search by title
  - Case insensitive search
  - No results
  - Empty search string

- **Edge Cases**
  - Multiple tickets with same status
  - Verify response contains all required fields

**Test Count**: 35+ tests

---

### 5. TicketDashboardTest.java
**Purpose**: Tests dashboard functionality and statistics

**Coverage Areas**:
- **Dashboard Counts**
  - Verify correct count of pending tickets
  - Verify correct count of in-progress tickets
  - Verify correct count of resolved tickets
  - Verify total ticket count
  - Verify status distribution

- **Dashboard Filtering**
  - Filter by Status and System
  - Filter by Status and Category
  - Filter by Date Range
  - Filter by System Name
  - Filter by Category

- **Dashboard Sorting**
  - Sort by Date Submitted (newest first)
  - Sort by Date Submitted (oldest first)
  - Sort by Last Updated

- **Pagination**
  - Zero tickets
  - One ticket
  - Many tickets (page 1)
  - Many tickets (page 2)
  - Last page with partial results

- **Search Across Columns**
  - Search across title
  - Search with partial match
  - Search with no results

- **Edge Cases**
  - All tickets have same status
  - Filter with no matching results
  - Multiple filters applied
  - Verify all required fields present

**Test Count**: 25+ tests

---

### 6. TicketValidationTest.java
**Purpose**: Tests input validation and edge cases

**Coverage Areas**:
- **Empty/Null Input Validation**
  - Empty/null title
  - Empty/null system name
  - Empty/null category
  - Empty/null description

- **Long Input Validation**
  - Title at maximum length (100 chars)
  - Description at maximum length (1000 chars)
  - System name at maximum length (50 chars)
  - Category at maximum length (50 chars)

- **Special Characters Validation**
  - Title with special characters
  - Description with special characters
  - Title with line breaks
  - Description with line breaks

- **Non-ASCII Characters Validation**
  - Japanese characters
  - Chinese characters
  - Mixed ASCII and non-ASCII

- **Duplicate Prevention**
  - Check for existing ticket with same title
  - Verify unique ticket creation

- **Whitespace Handling**
  - Title with leading/trailing whitespace
  - Title with only whitespace

- **Timestamp Validation**
  - Created date is set on creation
  - Update date is set on creation
  - Update date is refreshed on update

**Test Count**: 30+ tests

---

## Running the Tests

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- All dependencies in pom.xml

### Run All Tests
```bash
cd backend
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=TicketServiceTest
mvn test -Dtest=TicketCommentServiceTest
mvn test -Dtest=TicketAttachmentServiceTest
mvn test -Dtest=TicketControllerTest
mvn test -Dtest=TicketDashboardTest
mvn test -Dtest=TicketValidationTest
```

### Run Tests with Coverage Report
```bash
mvn clean test jacoco:report
```

### Run Specific Test Method
```bash
mvn test -Dtest=TicketServiceTest#testCreateTicket_ValidInputs
```

## Test Structure

All tests follow this structure:
1. **Setup** (`@BeforeEach`): Initialize test data and mock objects
2. **Execution**: Call the method under test
3. **Verification**: Assert expected results using JUnit assertions
4. **Mock Verification**: Verify mock interactions using Mockito

## Assertions Used
- `assertNotNull()` - Verify object is not null
- `assertEquals()` - Verify expected vs actual values
- `assertTrue()` / `assertFalse()` - Verify boolean conditions
- `verify()` - Verify mock method calls
- `times()` - Verify number of mock invocations

## Mock Framework
- **Mockito** is used for mocking dependencies
- `@Mock` - Creates mock objects
- `@InjectMocks` - Injects mocks into the class under test
- `when().thenReturn()` - Defines mock behavior
- `verify()` - Verifies mock interactions

## Test Isolation
- Each test is independent and can run in any order
- Mocks are reset between tests
- No database or external dependencies required
- Tests run in memory only

## Total Test Coverage
- **Total Test Classes**: 6
- **Total Test Methods**: 180+
- **Coverage Areas**: 
  - User Dashboard ✓
  - Create New Ticket ✓
  - View Ticket ✓
  - Ticket Comments ✓
  - Ticket Attachments ✓
  - Input Validation ✓
  - Edge Cases ✓
  - REST API Endpoints ✓

## Requirements Compliance

All tests strictly follow the functional requirements, test cases, edge cases, and review criteria defined in the Ticketing Management System Requirements Document:

✓ User Dashboard functionality
✓ Create New Ticket with validation
✓ View Ticket details
✓ Ticket Comments (only comments allowed, no ticket updates)
✓ Filtering by Status, System, Category, Date
✓ Search functionality
✓ Empty/null/long inputs
✓ Special and non-ASCII characters
✓ Attachment handling (none, single, multiple)
✓ Pagination scenarios
✓ Duplicate prevention
✓ Timestamp management

## Notes
- All tests are unit tests (no integration or UI tests)
- Tests use JUnit 5 and Mockito
- No actual database connections required
- Tests are fast and can be run frequently during development
- All tests are isolated and independent