# Fullstack Application Template

A minimal template for Java + JavaScript + PostgreSQL projects.

## Tech Stack

- **Backend**: Java 17 + Spring Boot + Maven
- **Frontend**: JavaScript (React)
- **Database**: PostgreSQL

## Project Structure

```
fullstack-app/
├── backend/                    # Java Spring Boot backend
│   ├── pom.xml                # Maven dependencies
│   └── src/main/
│       ├── java/com/fullstack/
│       │   └── Application.java
│       └── resources/
│           └── application.properties
├── frontend/                   # React frontend
│   ├── package.json           # npm dependencies
│   ├── public/
│   └── src/
│       ├── App.js
│       ├── App.css
│       ├── index.js
│       └── services/
│           └── api.js
└── database/
    └── init.sql               # Database schema
```

## Setup Instructions

### 1. Database Setup
```bash
# Create PostgreSQL database
psql -U postgres
CREATE DATABASE your_database;

# Run initialization script
psql -U postgres -d your_database -f database/init.sql
```

### 2. Backend Setup
```bash
cd backend

# Update application.properties with your database credentials

# Run the backend
mvn spring-boot:run
```

Backend will run on: http://localhost:8080

### 3. Frontend Setup
```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm start
```

Frontend will run on: http://localhost:3000

## Configuration

- **Database**: Edit `backend/src/main/resources/application.properties`
- **API URL**: Edit `frontend/src/services/api.js`

## Next Steps

1. Create your database schema in `database/init.sql`
2. Add your Java models, repositories, services, and controllers in `backend/src/main/java/com/fullstack/`
3. Build your React components in `frontend/src/`
4. Configure API endpoints in `frontend/src/services/api.js`