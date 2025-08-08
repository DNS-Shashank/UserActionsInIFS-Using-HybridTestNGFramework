# 🚀 UserActionsInIFS - Test Automation Framework

[![Java](https://img.shields.io/badge/Java-11+-orange.svg)](https://www.oracle.com/java/)
[![Selenium](https://img.shields.io/badge/Selenium-4.33.0-green.svg)](https://selenium.dev/)
[![TestNG](https://img.shields.io/badge/TestNG-7.11.0-red.svg)](https://testng.org/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-blue.svg)](https://maven.apache.org/)

**Enterprise-grade test automation framework** for IFS (Industrial and Financial Systems) user management testing. Built with Java, Selenium WebDriver, TestNG, and modern design patterns.

## ✨ What This Framework Does

- **👥 Complete User Lifecycle Testing**: Login → Create Users → Manage Permissions → Delete Users
- **🌐 Multi-Browser Testing**: Automatically runs on Chrome, Firefox, Edge
- **📊 Professional Reports**: Beautiful HTML reports with screenshots on failures
- **🔒 Secure**: No hardcoded passwords - uses environment variables
- **⚡ Advanced Features**: Handles complex web elements (Shadow DOM, iframes)

## 🚀 Quick Start (3 Steps)

### Step 1: Install Requirements
- **Java 11+** ([Download here](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html))
- **Git** ([Download here](https://git-scm.com/downloads))

### Step 2: Clone & Setup
```bash
# Clone the project
git clone https://github.com/your-username/UserActionsInIFS.git
cd UserActionsInIFS

# Set your login credentials (REQUIRED)
# Windows:
set IFS_USERNAME=your-email@company.com
set IFS_PASSWORD=your-password

# Mac/Linux:
export IFS_USERNAME=your-email@company.com
export IFS_PASSWORD=your-password
```

### Step 3: Run Tests
```bash
mvn clean test
```

**That's it!** 🎉 Tests will run automatically and generate reports in the `reports/` folder.

## 📁 What's Inside

```
UserActionsInIFS/
├── 🧪 Test Cases (8 different test scenarios)
│   ├── LoginTests.java           # Login/logout testing
│   ├── UserCreationTest.java     # Create new users
│   ├── UserDeletionTest.java     # Delete users safely
│   ├── UserPasswordResetTest.java # Reset user passwords
│   └── 4 more test files...
├── 📄 Page Objects (Clean, maintainable code)
├── 📊 Reports (Auto-generated after each run)
├── 📸 Screenshots (Captured on test failures)
└── ⚙️ Configuration files
```

## 🎯 Test Scenarios Covered

| Test Type | What It Does | Why It Matters |
|-----------|--------------|----------------|
| 🔐 **Login Tests** | Valid/invalid login attempts | Ensures security works |
| 👤 **User Creation** | Creates users with random data | Tests core functionality |
| 🔑 **Password Reset** | Admin resets user passwords | Critical admin feature |
| ❌ **User Deletion** | Safely removes users | Data cleanup testing |
| 🛡️ **Security Roles** | Assigns permissions to users | Access control testing |
| 📤 **Import/Export** | Bulk user operations | Efficiency testing |

## 🏃♂️ Different Ways to Run Tests

```bash
# Run everything (takes ~10-15 minutes)
mvn test

# Run only critical tests (takes ~5 minutes)
mvn test -Dsuite=smoke

# Run only login tests (takes ~2 minutes)
mvn test -Dtest=LoginTests

# Run with Firefox instead of Chrome
mvn test -Dbrowser=firefox
```

## 📊 Understanding Your Results

After tests run, check these locations:

- **📈 Main Report**: Open `reports/TestReport_[timestamp].html` in your browser
- **📸 Failure Screenshots**: Check `screenshots/` folder if tests fail
- **📝 Detailed Logs**: View `logs/app.log` for technical details

## ⚠️ Common Issues & Solutions

### "Environment variable not set" error
```bash
# Make sure you set these before running tests:
set IFS_USERNAME=your-email@company.com
set IFS_PASSWORD=your-password
```

### Tests fail with "Element not found"
- **Cause**: Application might be slow to load
- **Solution**: Tests have built-in waits, but very slow networks might need longer waits

### Browser doesn't open
- **Cause**: WebDriver issue
- **Solution**: Framework auto-downloads drivers, ensure internet connection

## 🛠️ Technology Stack

| What We Use | Version | Why |
|-------------|---------|-----|
| ☕ **Java** | 11+ | Programming language |
| 🌐 **Selenium** | 4.33.0 | Controls web browsers |
| 🧪 **TestNG** | 7.11.0 | Organizes and runs tests |
| 📊 **ExtentReports** | 5.1.2 | Creates beautiful reports |
| 🚗 **WebDriverManager** | 6.1.0 | Auto-manages browser drivers |

## 🤝 Contributing

Want to add more tests or fix issues?

1. 🍴 Fork this repository
2. 🌿 Create your feature branch (`git checkout -b feature/new-test`)
3. 💾 Commit your changes (`git commit -m 'Add new test'`)
4. 📤 Push to branch (`git push origin feature/new-test`)
5. 🔄 Create Pull Request

## 📄 License

MIT License - feel free to use this for your projects!

---

⭐ **Found this helpful? Star the repository!**

💬 **Questions?** Open an issue or contact the maintainers.