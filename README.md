# Vrit Signup Automation

## Project Overview
Automated testing of the Vrit signup workflow using Selenium WebDriver, Java, and TestNG. The project includes email OTP verification using Mailosaur and test execution reporting using Extent Reports.

## Technologies Used
- Java
- Selenium WebDriver
- TestNG
- Maven
- Mailosaur
- Extent Reports
- Chrome WebDriver

## Project Structure
```text
signup-automation/
├── src/test/java/com/vrit/
│   ├── base/
│   ├── listeners/
│   ├── tests/
│   └── utils/
├── reports/
├── screenshots/
├── pom.xml
├── README.md
└── .gitignore
```

## Prerequisites
- Java JDK
- Maven
- Google Chrome
- Eclipse IDE (optional)
- Mailosaur account and API key

## Configuration
Configure your Mailosaur API key as an environment variable:

`MAILOSAUR_API_KEY`

## How to Run
1. Clone this repository.
2. Open the project in Eclipse or your preferred Java IDE.
3. Configure the required Mailosaur credentials.
4. Install Maven dependencies.
5. Run `SignupTest.java` as a TestNG test in Eclipse.

Alternatively, run using Maven if the project is configured for Maven test execution:

```bash
mvn clean test
```

## Test Reporting
Extent Reports are generated after test execution. The report provides execution details and test results.

## Author
Aashish Pandey

GitHub: https://github.com/aashish4222
