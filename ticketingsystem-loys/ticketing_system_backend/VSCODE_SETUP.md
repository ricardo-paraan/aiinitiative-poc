# VS Code Setup Guide for Spring Boot Project

## Prerequisites

1. **Java Development Kit (JDK) 17 or higher**
   - Download from: https://adoptium.net/ or https://www.oracle.com/java/technologies/downloads/
   - Verify installation: `java -version`

2. **VS Code Extensions (Required)**
   - Extension Pack for Java (by Microsoft)
   - Spring Boot Extension Pack (by VMware)
   - Lombok Annotations Support for VS Code

## Step-by-Step Setup

### 1. Install Java (if not installed)

**Windows:**
```bash
# Download and install from https://adoptium.net/
# Or use winget
winget install EclipseAdoptium.Temurin.17.JDK
```

**Verify Java installation:**
```bash
java -version
javac -version
```

### 2. Install VS Code Extensions

Open VS Code and install these extensions:

1. **Extension Pack for Java**
   - Press `Ctrl+Shift+X` to open Extensions
   - Search for "Extension Pack for Java"
   - Click Install
   - This includes: Language Support, Debugger, Test Runner, Maven, Project Manager

2. **Spring Boot Extension Pack**
   - Search for "Spring Boot Extension Pack"
   - Click Install
   - This includes: Spring Boot Tools, Spring Initializr, Spring Boot Dashboard

3. **Lombok Annotations Support**
   - Search for "Lombok Annotations Support for VS Code"
   - Click Install

### 3. Open the Project

1. Open VS Code
2. File → Open Folder
3. Navigate to and select the `backend` folder
4. VS Code will detect it's a Java/Maven project

### 4. Wait for Dependencies to Download

- VS Code will automatically start downloading Maven dependencies
- You'll see progress in the bottom right corner
- This may take 5-10 minutes on first run
- Check the "Java" output panel for progress

### 5. Configure Java Home (if needed)

If VS Code can't find Java, add this to your settings:

1. Press `Ctrl+,` to open Settings
2. Search for "java.home"
3. Or edit `settings.json`:

```json
{
    "java.home": "C:\\Program Files\\Eclipse Adoptium\\jdk-17.0.x-hotspot",
    "java.configuration.runtimes": [
        {
            "name": "JavaSE-17",
            "path": "C:\\Program Files\\Eclipse Adoptium\\jdk-17.0.x-hotspot"
        }
    ]
}
```

### 6. Run the Application

**Method 1: Using Spring Boot Dashboard**
1. Click on the Spring Boot icon in the left sidebar
2. You'll see "ticketing-system" listed
3. Click the play button (▶) next to it
4. The application will start

**Method 2: Using Run Menu**
1. Open `TicketingSystemApplication.java`
2. Press `F5` or click Run → Start Debugging
3. Select "Java" when prompted

**Method 3: Using Terminal**
1. Open Terminal in VS Code (`Ctrl+``)
2. Navigate to backend folder:
   ```bash
   cd backend
   ```
3. Run using Maven Wrapper (if available):
   ```bash
   # Windows
   mvnw.cmd spring-boot:run
   
   # Linux/Mac
   ./mvnw spring-boot:run
   ```

### 7. Verify Application is Running

- Check the Terminal/Console output
- You should see: "Started TicketingSystemApplication in X seconds"
- Application runs on: http://localhost:8080
- Test: Open browser and go to http://localhost:8080

## Troubleshooting

### Issue: "Java runtime could not be located"

**Solution:**
1. Install JDK 17 from https://adoptium.net/
2. Set JAVA_HOME environment variable:
   ```bash
   # Windows (PowerShell as Admin)
   [System.Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Eclipse Adoptium\jdk-17.0.x-hotspot", "Machine")
   
   # Or add to System Environment Variables via Control Panel
   ```
3. Restart VS Code

### Issue: "Cannot resolve dependencies"

**Solution:**
1. Open Command Palette (`Ctrl+Shift+P`)
2. Type "Java: Clean Java Language Server Workspace"
3. Select it and restart VS Code
4. Or delete `.vscode` folder and reopen project

### Issue: "Lombok not working"

**Solution:**
1. Install "Lombok Annotations Support for VS Code" extension
2. Restart VS Code
3. Clean and rebuild project

### Issue: "Port 8080 already in use"

**Solution:**
1. Change port in `application.properties`:
   ```properties
   server.port=8081
   ```
2. Or kill the process using port 8080:
   ```bash
   # Windows
   netstat -ano | findstr :8080
   taskkill /PID <PID> /F
   ```

### Issue: Maven dependencies not downloading

**Solution:**
1. Check internet connection
2. Open Terminal and run:
   ```bash
   cd backend
   # If you have Maven installed
   mvn clean install
   
   # Or use Maven Wrapper
   mvnw.cmd clean install
   ```

## Useful VS Code Shortcuts

- `F5` - Start Debugging
- `Ctrl+F5` - Run without Debugging
- `Shift+F5` - Stop Debugging
- `Ctrl+Shift+P` - Command Palette
- `Ctrl+`` - Toggle Terminal
- `Ctrl+Shift+B` - Build Project

## Next Steps

Once the application is running:
1. Test the API endpoints (when controllers are created)
2. Use REST Client extension or Postman for API testing
3. Check logs in the Terminal for any errors
4. Connect the frontend to the backend API

## Additional Resources

- VS Code Java Documentation: https://code.visualstudio.com/docs/java/java-tutorial
- Spring Boot in VS Code: https://code.visualstudio.com/docs/java/java-spring-boot
- Maven in VS Code: https://code.visualstudio.com/docs/java/java-build