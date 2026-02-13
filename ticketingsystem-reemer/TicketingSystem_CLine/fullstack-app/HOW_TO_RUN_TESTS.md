# How to Run Unit Tests - Step by Step Guide

## Prerequisites

Before running the tests, ensure you have:

1. **Java Development Kit (JDK) 17 or higher**
   - Check version: `java -version`
   - Download from: https://www.oracle.com/java/technologies/downloads/

2. **Apache Maven 3.6 or higher**
   - Check version: `mvn -version`
   - Download from: https://maven.apache.org/download.cgi

3. **Git** (if cloning the repository)
   - Check version: `git --version`

## Step 1: Navigate to Backend Directory

Open your terminal/command prompt and navigate to the backend folder:

```bash
cd backend
```

Or if you're in the project root:

```bash
cd fullstack-app-bob/backend
```

## Step 2: Run All Tests

### Option A: Run All Tests (Recommended)
```bash
mvn test
```

This will:
- Compile the source code
- Compile the test code
- Run all 135 unit tests
- Display results in the terminal

**Expected Output:**
```
[INFO] Tests run: 135, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### Option B: Clean and Run Tests
```bash
mvn clean test
```

This will clean previous builds before running tests (recommended if you made changes).

## Step 3: Run Specific Test Classes

### Run a Single Test Class
```bash
# Run TicketServiceTest only
mvn test -Dtest=TicketServiceTest

# Run TicketCommentServiceTest only
mvn test -Dtest=TicketCommentServiceTest

# Run TicketAttachmentServiceTest only
mvn test -Dtest=TicketAttachmentServiceTest

# Run TicketControllerTest only
mvn test -Dtest=TicketControllerTest

# Run TicketDashboardTest only
mvn test -Dtest=TicketDashboardTest

# Run TicketValidationTest only
mvn test -Dtest=TicketValidationTest
```

### Run Multiple Test Classes
```bash
mvn test -Dtest=TicketServiceTest,TicketCommentServiceTest
```

## Step 4: Run Specific Test Methods

### Run a Single Test Method
```bash
# Format: -Dtest=ClassName#methodName
mvn test -Dtest=TicketServiceTest#testCreateTicket_ValidInputs

mvn test -Dtest=TicketCommentServiceTest#testCreateComment_Success

mvn test -Dtest=TicketControllerTest#testGetAllTickets_Success
```

### Run Multiple Test Methods
```bash
mvn test -Dtest=TicketServiceTest#testCreateTicket_ValidInputs+testGetTicketById_Success
```

## Step 5: Run Tests with Coverage Report

### Generate Code Coverage Report
```bash
mvn clean test jacoco:report
```

After running, open the coverage report:
```
backend/target/site/jacoco/index.html
```

## Step 6: Run Tests in Your IDE

### IntelliJ IDEA
1. Open the project in IntelliJ IDEA
2. Navigate to `backend/src/test/java/com/fullstack/service/`
3. Right-click on a test class (e.g., `TicketServiceTest.java`)
4. Select **"Run 'TicketServiceTest'"**
5. Or click the green play button next to the class name or individual test method

### Eclipse
1. Open the project in Eclipse
2. Navigate to `backend/src/test/java/com/fullstack/service/`
3. Right-click on a test class
4. Select **"Run As" → "JUnit Test"**

### Visual Studio Code
1. Install the "Java Test Runner" extension
2. Open the test file
3. Click the "Run Test" button that appears above each test method
4. Or right-click and select "Run Test"

## Step 7: View Test Results

### Terminal Output
After running tests, you'll see:
```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.fullstack.service.TicketServiceTest
[INFO] Tests run: 32, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.fullstack.service.TicketCommentServiceTest
[INFO] Tests run: 23, Failures: 0, Errors: 0, Skipped: 0
...
[INFO] Results:
[INFO] Tests run: 135, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### Detailed Reports
Test reports are generated in:
```
backend/target/surefire-reports/
```

Open these files to see detailed test results:
- `TEST-*.xml` - XML format reports
- `*.txt` - Text format reports

## Step 8: Troubleshooting

### Issue: "mvn: command not found"
**Solution:** Maven is not installed or not in PATH
- Install Maven: https://maven.apache.org/install.html
- Add Maven to PATH environment variable

### Issue: "JAVA_HOME not set"
**Solution:** Set JAVA_HOME environment variable
```bash
# Windows
set JAVA_HOME=C:\Program Files\Java\jdk-17

# Linux/Mac
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
```

### Issue: Tests fail with compilation errors
**Solution:** Clean and rebuild
```bash
mvn clean compile test-compile test
```

### Issue: "No tests were executed"
**Solution:** Check test class naming
- Test classes must end with `Test` (e.g., `TicketServiceTest`)
- Test methods must be annotated with `@Test`

## Quick Reference Commands

```bash
# Run all tests
mvn test

# Run all tests with clean build
mvn clean test

# Run specific test class
mvn test -Dtest=TicketServiceTest

# Run specific test method
mvn test -Dtest=TicketServiceTest#testCreateTicket_ValidInputs

# Run tests with coverage
mvn clean test jacoco:report

# Skip tests (when building)
mvn clean install -DskipTests

# Run tests in parallel (faster)
mvn test -T 4

# Run tests with verbose output
mvn test -X

# Run only failed tests
mvn test -Dsurefire.rerunFailingTestsCount=2
```

## Test Structure

All test files are located in:
```
backend/src/test/java/com/fullstack/
├── controller/
│   └── TicketControllerTest.java       (30 tests)
└── service/
    ├── TicketServiceTest.java          (32 tests)
    ├── TicketCommentServiceTest.java   (23 tests)
    ├── TicketAttachmentServiceTest.java(25 tests)
    ├── TicketDashboardTest.java        (25 tests)
    └── TicketValidationTest.java       (25 tests)
```

## Additional Resources

- **JUnit 5 Documentation:** https://junit.org/junit5/docs/current/user-guide/
- **Mockito Documentation:** https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html
- **Maven Surefire Plugin:** https://maven.apache.org/surefire/maven-surefire-plugin/

## Need Help?

If you encounter any issues:
1. Check the error message in the terminal
2. Review the test reports in `backend/target/surefire-reports/`
3. Ensure all dependencies are downloaded: `mvn dependency:resolve`
4. Try cleaning the project: `mvn clean`

---

**Total Tests:** 135
**Test Classes:** 6
**All Tests Passing:** ✅