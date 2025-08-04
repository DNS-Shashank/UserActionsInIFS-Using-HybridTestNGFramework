# UserActionsInIFS - Selenium Test Automation Framework

A complete Java + Selenium WebDriver + TestNG automation project skeleton for QA testing.

## Project Structure

- `src/main/java/com/useractionsinifs/abstractComponents`: Common components for reuse across page objects
- `src/main/java/com/useractionsinifs/pageObjects`: Page Object Models for application screens
- `src/main/java/com/useractionsinifs/resources`: Configuration and reporting utilities
- `src/main/java/com/useractionsinifs/data`: Test data in JSON format
- `src/test/java/com/useractionsinifs/testComponents`: Base test setup and listeners
- `src/test/java/com/useractionsinifs/testCases`: Test case implementations

## Dependencies

- Selenium WebDriver 4.15.0
- TestNG 7.8.0
- WebDriverManager 5.5.3
- ExtentReports 5.1.1
- Jackson Databind for JSON parsing
- Log4j 2 for logging

## Requirements

- Java JDK 11 or higher
- Maven 3.8+ (or use the included Maven wrapper)

## How to Run Tests

### Using Maven

```bash
# Clean and compile the project
mvn clean compile

# Run all tests
mvn test

# Run a specific test class
mvn test -Dtest=TC_1_LoginTest
```

### Using Maven Wrapper (No Maven Installation Required)

```bash
# Windows
mvnw.cmd clean test

# Linux/Mac
./mvnw clean test
```

## Configuration

Edit the following files to configure the framework:

- `src/main/java/com/useractionsinifs/resources/GlobalProperties.properties`: Browser and URL configuration
- `src/main/java/com/useractionsinifs/data/TestData.json`: Test data
- `src/main/resources/log4j2.xml`: Logging configuration
- `testng.xml`: Test suite configuration

## Reports

Test execution reports are generated in the `reports` directory using ExtentReports.
Screenshots for failed tests are saved in the `screenshots` directory.

## Notes for Implementation

The current project is a skeleton with stub methods. To implement functional tests:

1. Add actual implementation to page object methods
2. Update the test data JSON file with your application data
3. Implement the test assertions in the test classes
4. Update GlobalProperties.properties with your application URL and credentials
