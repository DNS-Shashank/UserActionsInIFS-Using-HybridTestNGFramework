# 🚀 UserActionsInIFS - Enterprise Test Automation Framework

<div align="center">

[![Java](https://img.shields.io/badge/Java-11+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Selenium](https://img.shields.io/badge/Selenium-4.33.0-43B02A?style=for-the-badge&logo=selenium&logoColor=white)](https://selenium.dev/)
[![TestNG](https://img.shields.io/badge/TestNG-7.11.0-FF6C37?style=for-the-badge&logo=testng&logoColor=white)](https://testng.org/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)

[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen?style=for-the-badge)]()
[![Test Coverage](https://img.shields.io/badge/Coverage-85%25-green?style=for-the-badge)]()
[![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)]()
[![Maintained](https://img.shields.io/badge/Maintained-Yes-brightgreen?style=for-the-badge)]()

**🏆 Production-ready test automation framework for IFS (Industrial and Financial Systems)**  
*Built with modern design patterns, enterprise security, and scalable architecture*

[Quick Start](#-quick-start) • [Features](#-features) • [Documentation](#-documentation) • [Contributing](#-contributing)

</div>

## ✨ Why Choose This Framework?

<table>
<tr>
<td width="50%">

### 🎯 **Core Capabilities**
- 👥 **Complete User Lifecycle** - End-to-end user management testing
- 🌐 **Cross-Browser Support** - Chrome, Firefox, Edge automation
- 📊 **Rich Reporting** - ExtentReports with failure screenshots
- 🔒 **Enterprise Security** - Environment-based credential management
- ⚡ **Advanced Web Handling** - Shadow DOM, iframes, dynamic elements
- 🚀 **Parallel Execution** - Faster test execution with TestNG

</td>
<td width="50%">

### 🏗️ **Architecture Highlights**
- 📐 **Page Object Model** - Maintainable and scalable design
- 🔄 **Data-Driven Testing** - JSON-based test data management
- 🎭 **Smart Waits** - Intelligent element synchronization
- 📝 **Comprehensive Logging** - Log4j2 integration
- 🔧 **Auto WebDriver Management** - Zero manual driver setup
- 🎨 **Modern Java Practices** - Java 11+ features

</td>
</tr>
</table>

## 🚀 Quick Start

> **⚡ Get running in under 2 minutes!**

### Prerequisites
```bash
# Check Java version (11+ required)
java -version

# Check Maven installation
mvn -version
```

### 🔧 Setup & Configuration

```bash
# 1️⃣ Clone the repository
git clone <your-repository-url>
cd UserActionsInIFS

# 2️⃣ Configure test credentials (IMPORTANT: Use test environment only)
# Edit: src/main/java/com/useractionsinifs/data/LoginCredentials.json
{
  "validCredentials": [{
    "username": "your-test-username@company.com",
    "password": "your-secure-test-password",
    "description": "Test environment credentials"
  }]
}

# 3️⃣ Update application URL
# Edit: src/main/java/com/useractionsinifs/resources/GlobalProperties.properties
url=https://your-test-environment-url.com

# 4️⃣ Run tests
mvn clean test
```

### 🎯 Execution Options

```bash
# Full test suite (recommended for CI/CD)
mvn clean test

# Smoke tests only (quick validation)
mvn test -Dgroups=smoke

# Specific browser
mvn test -Dbrowser=firefox

# Parallel execution
mvn test -DthreadCount=3
```

**🎉 Results**: Check `reports/TestReport_[timestamp].html` for detailed results!

## 📁 Project Architecture

```
📦 UserActionsInIFS/
├── 🧪 src/test/java/testCases/          # Test Implementation Layer
│   ├── LoginTests.java                  # 🔐 Authentication & Authorization
│   ├── UserCreationTest.java           # 👤 User Provisioning
│   ├── UserDeletionTest.java           # 🗑️ User Deprovisioning  
│   ├── UserPasswordResetTest.java      # 🔑 Password Management
│   ├── UserEnableDisableTest.java      # 🔄 Account Status Management
│   ├── UserSecurityRolesTest.java      # 🛡️ Permission Management
│   └── UserCopyTest.java               # 📋 User Duplication
├── 🏗️ src/main/java/pageObjects/       # Page Object Model
│   ├── LoginPage.java                  # Login page interactions
│   ├── LandingPage.java                # Dashboard navigation
│   └── SecurityPage_Users.java         # User management operations
├── 🔧 src/main/java/abstractComponents/ # Reusable Components
├── 📊 reports/                         # ExtentReports Output
├── 📸 screenshots/                     # Failure Evidence
├── 📝 logs/                           # Execution Logs
└── ⚙️ Configuration Files              # Environment Settings
```

## 🎯 Test Coverage Matrix

<details>
<summary><b>🔐 Authentication & Security (Click to expand)</b></summary>

| Scenario | Test Cases | Business Impact |
|----------|------------|----------------|
| **Valid Login** | ✅ Successful authentication | Core system access |
| **Invalid Login** | ❌ Wrong credentials, empty fields | Security validation |
| **Session Management** | 🔄 Timeout, concurrent sessions | User experience |

</details>

<details>
<summary><b>👥 User Lifecycle Management (Click to expand)</b></summary>

| Operation | Validation Points | Risk Mitigation |
|-----------|------------------|----------------|
| **User Creation** | 📝 Data validation, duplicate prevention | Data integrity |
| **Password Reset** | 🔑 Admin privileges, notification | Security compliance |
| **User Deletion** | 🗑️ Soft delete, audit trail | Data protection |
| **Enable/Disable** | 🔄 Status changes, access control | Account management |

</details>

<details>
<summary><b>🛡️ Permission & Role Management (Click to expand)</b></summary>

| Feature | Test Scope | Compliance |
|---------|------------|------------|
| **Security Roles** | 🎭 Role assignment, inheritance | RBAC validation |
| **User Copy** | 📋 Permission replication | Consistency checks |
| **Access Control** | 🚪 Resource permissions | Authorization testing |

</details>

## 🏃♂️ Execution Strategies

### 🎯 **Test Suite Execution**

```bash
# 🚀 Full Regression Suite (~15 min)
mvn clean test

# ⚡ Smoke Tests (~5 min) - Critical path validation
mvn test -Dgroups=smoke

# 🔐 Login Tests Only (~2 min) - Authentication focus
mvn test -Dgroups=login

# 🔄 Regression Tests (~12 min) - Comprehensive coverage
mvn test -Dgroups=regression
```

### 🌐 **Cross-Browser Testing**

```bash
# Chrome (default)
mvn test -Dbrowser=chrome

# Firefox
mvn test -Dbrowser=firefox

# Edge
mvn test -Dbrowser=edge
```

### ⚡ **Performance Optimization**

```bash
# Parallel execution (3 threads)
mvn test -DthreadCount=3

# Specific test class
mvn test -Dtest=UserCreationTest

# Custom TestNG suite
mvn test -DsuiteXmlFile=custom-suite.xml
```

## 📊 Results & Reporting

### 🎨 **Rich HTML Reports**
```bash
# 📈 ExtentReports Dashboard
open reports/TestReport_[timestamp].html

# 📊 TestNG Native Reports  
open target/surefire-reports/index.html
```

### 🔍 **Debugging & Troubleshooting**
```bash
# 📸 Failure Screenshots (auto-captured)
ls screenshots/

# 📝 Detailed Execution Logs
tail -f logs/app.log

# 🐛 Surefire Reports (Maven)
open target/surefire-reports/emailable-report.html
```

### 📈 **Report Features**
- ✅ **Test Status Dashboard** - Pass/Fail/Skip metrics
- 📸 **Automatic Screenshots** - Failure evidence capture  
- ⏱️ **Execution Timeline** - Performance insights
- 🏷️ **Test Categorization** - Group-wise results
- 📱 **Mobile-Responsive** - View on any device


## 🛠️ Technology Stack

<div align="center">

### 🏗️ **Core Framework**

| Technology | Version | Purpose | Benefits |
|------------|---------|---------|----------|
| ☕ **Java** | 11+ | Core Language | Modern features, LTS support |
| 🌐 **Selenium WebDriver** | 4.33.0 | Browser Automation | W3C standard, cross-browser |
| 🧪 **TestNG** | 7.11.0 | Test Framework | Parallel execution, data-driven |
| 🏗️ **Maven** | 3.8+ | Build Tool | Dependency management, lifecycle |

### 📊 **Reporting & Utilities**

| Component | Version | Capability | Value |
|-----------|---------|------------|-------|
| 📈 **ExtentReports** | 5.1.2 | Rich HTML Reports | Executive dashboards |
| 🚗 **WebDriverManager** | 6.1.0 | Driver Management | Zero-config setup |
| 📝 **Log4j2** | 2.20.0 | Logging Framework | Structured logging |
| 🔄 **Jackson** | 2.15.2 | JSON Processing | Test data management |
| 📁 **Apache Commons IO** | 2.13.0 | File Operations | Screenshot handling |

</div>

### 🎯 **Architecture Benefits**
- 🔄 **Maintainable**: Page Object Model with clear separation
- 🚀 **Scalable**: Parallel execution and modular design  
- 🔒 **Secure**: Environment-based configuration
- 📊 **Observable**: Comprehensive logging and reporting
- 🧪 **Testable**: Data-driven with multiple test scenarios


### 💝 **Show Your Support**

⭐ **Star this repository** if it helped you!  
🐛 **Report issues** to help us improve  
💡 **Share ideas** for new features  
🤝 **Contribute code** to make it better  

**Built with ❤️ by the automation community**

</div>