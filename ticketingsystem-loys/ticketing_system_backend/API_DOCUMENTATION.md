# Ticketing System API Documentation

Base URL: `http://localhost:8080/api`

## Table of Contents
- [Tickets](#tickets)
- [Comments](#comments)
- [Attachments](#attachments)
- [Reference Data](#reference-data)
- [Dashboard](#dashboard)

---

## Tickets

### Get All Tickets
```http
GET /api/tickets
```

**Response:**
```json
[
  {
    "ticketId": 1,
    "title": "Bug in login page",
    "author": "John Doe",
    "description": "Users cannot login",
    "statusId": 1,
    "statusName": "Open",
    "categoryId": 1,
    "categoryName": "Bug",
    "systemId": 1,
    "systemName": "Web Application",
    "createdDate": "2024-01-20T10:30:00",
    "updatedDate": "2024-01-20T10:30:00",
    "comments": [],
    "attachments": []
  }
]
```

### Get Ticket by ID
```http
GET /api/tickets/{id}
```

**Response:** Same as single ticket object above

### Create Ticket
```http
POST /api/tickets
Content-Type: application/json
```

**Request Body:**
```json
{
  "title": "Bug in login page",
  "author": "John Doe",
  "description": "Users cannot login",
  "statusId": 1,
  "categoryId": 1,
  "systemId": 1
}
```

**Response:** Created ticket object (201 Created)

### Update Ticket
```http
PUT /api/tickets/{id}
Content-Type: application/json
```

**Request Body:**
```json
{
  "title": "Updated title",
  "description": "Updated description",
  "statusId": 2,
  "categoryId": 1,
  "systemId": 1
}
```

**Response:** Updated ticket object

### Delete Ticket
```http
DELETE /api/tickets/{id}
```

**Response:** 204 No Content

### Search Tickets
```http
GET /api/tickets/search?keyword={keyword}
```

**Parameters:**
- `keyword` (required): Search term for title or description

**Response:** Array of matching tickets

### Filter Tickets
```http
GET /api/tickets/filter?statusId={statusId}&categoryId={categoryId}&systemId={systemId}&author={author}
```

**Parameters:**
- `statusId` (optional): Filter by status ID
- `categoryId` (optional): Filter by category ID
- `systemId` (optional): Filter by system ID
- `author` (optional): Filter by author name

**Response:** Array of filtered tickets

### Get Tickets by Status
```http
GET /api/tickets/status/{statusId}
```

**Response:** Array of tickets with specified status

### Get Tickets by Category
```http
GET /api/tickets/category/{categoryId}
```

**Response:** Array of tickets with specified category

### Get Tickets by System
```http
GET /api/tickets/system/{systemId}
```

**Response:** Array of tickets with specified system

---

## Comments

### Get Comments for Ticket
```http
GET /api/tickets/{ticketId}/comments
```

**Response:**
```json
[
  {
    "commentId": 1,
    "ticketId": 1,
    "commentText": "This is a comment",
    "author": "Jane Smith"
  }
]
```

### Add Comment to Ticket
```http
POST /api/tickets/{ticketId}/comments
Content-Type: application/json
```

**Request Body:**
```json
{
  "commentText": "This is a new comment",
  "author": "Jane Smith"
}
```

**Response:** Created comment object (201 Created)

### Delete Comment
```http
DELETE /api/comments/{commentId}
```

**Response:** 204 No Content

---

## Attachments

### Get Attachments for Ticket
```http
GET /api/tickets/{ticketId}/attachments
```

**Response:**
```json
[
  {
    "attachmentId": 1,
    "ticketId": 1,
    "fileName": "screenshot.png",
    "filePath": "./uploads/uuid-filename.png",
    "fileSize": 102400,
    "contentType": "image/png"
  }
]
```

### Upload Attachment
```http
POST /api/tickets/{ticketId}/attachments
Content-Type: multipart/form-data
```

**Form Data:**
- `file`: The file to upload

**Response:** Created attachment object (201 Created)

### Download Attachment
```http
GET /api/attachments/{attachmentId}/download
```

**Response:** File download with appropriate headers

### Delete Attachment
```http
DELETE /api/attachments/{attachmentId}
```

**Response:** 204 No Content

---

## Reference Data

### Get All Statuses
```http
GET /api/statuses
```

**Response:**
```json
[
  {
    "statusId": 1,
    "statusName": "Open",
    "description": "Ticket is newly created"
  },
  {
    "statusId": 2,
    "statusName": "In Progress",
    "description": "Ticket is being worked on"
  },
  {
    "statusId": 3,
    "statusName": "Resolved",
    "description": "Ticket has been resolved"
  },
  {
    "statusId": 4,
    "statusName": "Closed",
    "description": "Ticket is closed"
  }
]
```

### Get Status by ID
```http
GET /api/statuses/{id}
```

**Response:** Single status object

### Get All Categories
```http
GET /api/categories
```

**Response:**
```json
[
  {
    "categoryId": 1,
    "categoryName": "Bug",
    "description": "Software bugs and errors"
  },
  {
    "categoryId": 2,
    "categoryName": "Feature Request",
    "description": "New feature requests"
  }
]
```

### Get Category by ID
```http
GET /api/categories/{id}
```

**Response:** Single category object

### Get All Systems
```http
GET /api/systems
```

**Response:**
```json
[
  {
    "systemId": 1,
    "systemName": "Web Application",
    "description": "Main web application system"
  },
  {
    "systemId": 2,
    "systemName": "Mobile App",
    "description": "Mobile application system"
  }
]
```

### Get System by ID
```http
GET /api/systems/{id}
```

**Response:** Single system object

---

## Dashboard

### Get Dashboard Statistics
```http
GET /api/dashboard/stats
```

**Response:**
```json
{
  "totalTickets": 150,
  "openTickets": 45,
  "inProgressTickets": 30,
  "resolvedTickets": 50,
  "closedTickets": 25,
  "ticketsByCategory": {
    "Bug": 60,
    "Feature Request": 40,
    "Support": 30,
    "Documentation": 10,
    "Performance": 10
  },
  "ticketsBySystem": {
    "Web Application": 80,
    "Mobile App": 40,
    "API": 20,
    "Database": 5,
    "Infrastructure": 5
  }
}
```

---

## Error Responses

All endpoints may return the following error responses:

### 400 Bad Request
```json
{
  "status": 400,
  "message": "Error message describing what went wrong",
  "timestamp": "2024-01-20T10:30:00"
}
```

### 404 Not Found
```json
{
  "status": 404,
  "message": "Resource not found with id: 123",
  "timestamp": "2024-01-20T10:30:00"
}
```

### 500 Internal Server Error
```json
{
  "status": 500,
  "message": "An unexpected error occurred",
  "timestamp": "2024-01-20T10:30:00"
}
```

### Validation Errors (422)
```json
{
  "title": "Title is required",
  "author": "Author is required",
  "statusId": "Status ID is required"
}
```

---

## Testing with cURL

### Create a Ticket
```bash
curl -X POST http://localhost:8080/api/tickets \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Ticket",
    "author": "John Doe",
    "description": "This is a test ticket",
    "statusId": 1,
    "categoryId": 1,
    "systemId": 1
  }'
```

### Get All Tickets
```bash
curl http://localhost:8080/api/tickets
```

### Upload Attachment
```bash
curl -X POST http://localhost:8080/api/tickets/1/attachments \
  -F "file=@/path/to/file.png"
```

### Search Tickets
```bash
curl "http://localhost:8080/api/tickets/search?keyword=bug"
```

### Filter Tickets
```bash
curl "http://localhost:8080/api/tickets/filter?statusId=1&categoryId=1"
```

---

## Notes

- All timestamps are in ISO 8601 format
- File uploads are limited to 10MB (configurable in application.properties)
- CORS is enabled for all origins (configure for production)
- All endpoints support JSON request/response format
- Authentication is not implemented in this POC version