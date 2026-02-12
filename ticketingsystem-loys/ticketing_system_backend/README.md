# Ticketing System Backend

Spring Boot REST API for the Ticketing System application.

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+
- IDE (IntelliJ IDEA, Eclipse, or VS Code with Java extensions)

## Database Setup

1. **Create PostgreSQL Database**
   ```bash
   # Connect to PostgreSQL
   psql -U postgres
   
   # Create database (if not using default 'postgres' database)
   CREATE DATABASE ticketing_system;
   ```

2. **Run the Schema Script**
   - Navigate to the root directory where `schema.sql` is located
   - Execute the SQL script:
   ```bash
   psql -U postgres -d postgres -f schema.sql
   ```
   
   Or run it directly in pgAdmin or any PostgreSQL client.

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/ticketing/
│   │   │   ├── TicketingSystemApplication.java    # Main application class
│   │   │   ├── model/                             # JPA entities
│   │   │   │   ├── Ticket.java
│   │   │   │   ├── TicketStatus.java
│   │   │   │   ├── TicketCategory.java
│   │   │   │   ├── TicketSystem.java
│   │   │   │   ├── TicketComment.java
│   │   │   │   └── TicketAttachment.java
│   │   │   ├── repository/                        # Data access layer
│   │   │   │   ├── TicketRepository.java
│   │   │   │   ├── CommentRepository.java
│   │   │   │   ├── AttachmentRepository.java
│   │   │   │   ├── StatusRepository.java
│   │   │   │   ├── CategoryRepository.java
│   │   │   │   └── SystemRepository.java
│   │   │   ├── service/                           # Business logic (to be created)
│   │   │   ├── controller/                        # REST controllers (to be created)
│   │   │   ├── dto/                               # Data transfer objects (to be created)
│   │   │   └── config/                            # Configuration classes (to be created)
│   │   └── resources/
│   │       └── application.properties             # Application configuration
│   └── test/                                      # Test classes
├── pom.xml                                        # Maven dependencies (to be created)
└── README.md                                      # This file
```

## Configuration

The application is configured in `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=12345678

# Server runs on port 8080
server.port=8080

# File uploads stored in ./uploads directory
file.upload-dir=./uploads
```

## Building the Project

### Using Maven Command Line

```bash
# Navigate to backend directory
cd backend

# Clean and build
mvn clean install

# Run the application
mvn spring-boot:run
```

### Using IDE

1. **IntelliJ IDEA**
   - Open the `backend` folder as a project
   - Wait for Maven to download dependencies
   - Right-click on `TicketingSystemApplication.java`
   - Select "Run 'TicketingSystemApplication'"

2. **Eclipse**
   - Import as "Existing Maven Project"
   - Select the `backend` folder
   - Right-click on project → Run As → Spring Boot App

3. **VS Code**
   - Open the `backend` folder
   - Install "Extension Pack for Java" and "Spring Boot Extension Pack"
   - Press F5 or use the Run menu

## API Endpoints (To Be Implemented)

### Tickets
- `GET /api/tickets` - Get all tickets
- `GET /api/tickets/{id}` - Get ticket by ID
- `POST /api/tickets` - Create new ticket
- `PUT /api/tickets/{id}` - Update ticket
- `DELETE /api/tickets/{id}` - Delete ticket
- `GET /api/tickets/search?keyword={keyword}` - Search tickets

### Comments
- `GET /api/tickets/{id}/comments` - Get comments for ticket
- `POST /api/tickets/{id}/comments` - Add comment
- `DELETE /api/comments/{id}` - Delete comment

### Attachments
- `GET /api/tickets/{id}/attachments` - Get attachments
- `POST /api/tickets/{id}/attachments` - Upload attachment
- `GET /api/attachments/{id}/download` - Download attachment
- `DELETE /api/attachments/{id}` - Delete attachment

### Reference Data
- `GET /api/statuses` - Get all statuses
- `GET /api/categories` - Get all categories
- `GET /api/systems` - Get all systems

### Dashboard & Reports
- `GET /api/dashboard/stats` - Get dashboard statistics
- `GET /api/reports/tickets-by-status` - Tickets by status
- `GET /api/reports/tickets-by-category` - Tickets by category
- `GET /api/reports/tickets-by-system` - Tickets by system

## Testing the API

Once the application is running, you can test it using:

1. **Browser** - For GET requests: `http://localhost:8080/api/tickets`

2. **Postman** - Import the API collection (to be created)

3. **cURL** - Command line testing:
   ```bash
   # Get all tickets
   curl http://localhost:8080/api/tickets
   
   # Create a ticket
   curl -X POST http://localhost:8080/api/tickets \
     -H "Content-Type: application/json" \
     -d '{"title":"Test Ticket","author":"John","description":"Test"}'
   ```

## Next Steps

1. Create Service layer classes
2. Create Controller classes
3. Create DTO classes
4. Implement file upload/download functionality
5. Add CORS configuration
6. Add exception handling
7. Write unit tests
8. Create API documentation

## Troubleshooting

### Database Connection Issues
- Verify PostgreSQL is running: `pg_isready`
- Check credentials in `application.properties`
- Ensure database exists and schema is loaded

### Port Already in Use
- Change port in `application.properties`: `server.port=8081`
- Or kill the process using port 8080

### Maven Build Errors
- Clean Maven cache: `mvn clean`
- Update dependencies: `mvn clean install -U`

## License

This project is for educational/POC purposes.