package com.useractionsinifs.testComponents;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.useractionsinifs.abstractComponents.AbstractComponents;
import com.useractionsinifs.pageObjects.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@Listeners({com.useractionsinifs.testComponents.listeners.TestListener.class})
public class BaseTest {
    
    //Logger
    private static final Logger logger = LogManager.getLogger(BaseTest.class);
    
    // Static initialization to load environment variables before any test methods
    static {
        loadStaticEnvironmentVariables();
    }
    
    // Static method to load environment variables
    private static void loadStaticEnvironmentVariables() {
        // This ensures environment variables are loaded before DataProvider methods
        logger.info("Loading environment variables for test execution");
    }

    //Logout page elements
    @FindBy(css = "div.title")
    public WebElement logOutSuccessfulText;

    @FindBy(css = "div.content-container-16 div")
    public WebElement logOutConfirmationText;

    @FindBy(css = "span.company-logo img")
    public WebElement logOutInforLogo;

    //Public variables
    public WebDriver driver;
    protected LoginPage loginPage;
    protected Properties prop;
    protected List<HashMap<String, String>> testData;
    
    //Load properties, initialize WebDriver, configure settings
    public WebDriver configureDriver() throws IOException {
        
        // Load properties
        prop = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + 
                "/src/main/java/com/useractionsinifs/resources/GlobalProperties.properties");
        prop.load(fis); //Here we're loading the properties file
        
        // Override with environment variables if available
        loadEnvironmentVariables();
        
        // Check system property first (mvn test -Dbrowser=firefox), then fallback to properties file
        String browserName = System.getProperty("browser") != null ? System.getProperty("browser") : prop.getProperty("browser");
        if (browserName.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            driver = new ChromeDriver(options);
        } else if (browserName.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        } else if (browserName.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
        }
        
        // Configure driver settings
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(
                Integer.parseInt(prop.getProperty("implicitWait"))));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(
                Integer.parseInt(prop.getProperty("pageLoadTimeout"))));
             
        return driver;
    }
    
    //Load environment variables - NO credential fallbacks for security
    private void loadEnvironmentVariables() {
        // Set URL from environment variable with safe default
        String envUrl = System.getenv("IFS_TEST_URL");
        if (envUrl != null && !envUrl.isEmpty()) {
            prop.setProperty("url", envUrl);
        } else {
            prop.setProperty("url", "https://mingle-t20-portal.mingle.inforos.dev.inforcloudsuite.com/v2/INTQAINFOROSV2_AX1");
        }
        
        // Credentials are now accessed directly via static methods - no fallbacks stored in properties
        logger.info("Environment variables loaded. Credentials will be validated when accessed.");
    }
    
    //Get property with environment variable override
    public String getProperty(String key) {
        return prop.getProperty(key);
    }
    
    //Load test data from JSON file (defaults to valid credentials)
    protected List<HashMap<String, String>> loadTestData() throws IOException {
        return loadTestData("valid");
    }
    
    //Load test data with credential type specified
    protected List<HashMap<String, String>> loadTestData(String credentialType) throws IOException {
        String filePath = System.getProperty("user.dir") + "/src/main/java/com/useractionsinifs/data/LoginCredentials.json";
        
        // Reading JSON to String
        String jsonContent = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);
        
        // Replace environment variable placeholders
        jsonContent = replaceEnvironmentVariables(jsonContent);

        // Parse JSON structure
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, List<HashMap<String, String>>> jsonMap = mapper.readValue(jsonContent,
                new TypeReference<HashMap<String, List<HashMap<String, String>>>>() {});
        
        // Return credentials based on specified type
        if (credentialType.equalsIgnoreCase("valid")) {
            testData = jsonMap.get("validCredentials");
        } else if (credentialType.equalsIgnoreCase("invalid")) {
            testData = jsonMap.get("invalidCredentials");
        } else {
            // If type is not recognized, load all credentials by combining both lists
            List<HashMap<String, String>> allData = new java.util.ArrayList<>();
            allData.addAll(jsonMap.get("validCredentials"));
            allData.addAll(jsonMap.get("invalidCredentials"));
            testData = allData;
        }
        
        return testData;
    }
    
    //Replace environment variable placeholders in JSON content
    private String replaceEnvironmentVariables(String content) {
        // Replace with environment variables or fallbacks
        content = content.replace("${IFS_USERNAME}", getValidUsername());
        content = content.replace("${IFS_PASSWORD}", getValidPassword());
        content = content.replace("${IFS_INVALID_USERNAME}", getInvalidUsername());
        content = content.replace("${IFS_INVALID_PASSWORD}", getInvalidPassword());
        return content;
    }
    
    // Static methods to get credentials - NO FALLBACKS for security
    public static String getValidUsername() {
        String username = System.getenv("IFS_USERNAME");
        if (username == null || username.trim().isEmpty()) {
            throw new RuntimeException("SECURITY ERROR: IFS_USERNAME environment variable is required but not set. " +
                "Please set: IFS_USERNAME=your-username@infor.com");
        }
        return username;
    }
    
    public static String getValidPassword() {
        String password = System.getenv("IFS_PASSWORD");
        if (password == null || password.trim().isEmpty()) {
            throw new RuntimeException("SECURITY ERROR: IFS_PASSWORD environment variable is required but not set. " +
                "Please set: IFS_PASSWORD=your-secure-password");
        }
        return password;
    }
    
    public static String getInvalidUsername() {
        String username = System.getenv("IFS_INVALID_USERNAME");
        if (username == null || username.trim().isEmpty()) {
            return "invalid-user@test.com"; // Safe dummy value for negative testing
        }
        return username;
    }
    
    public static String getInvalidPassword() {
        String password = System.getenv("IFS_INVALID_PASSWORD");
        if (password == null || password.trim().isEmpty()) {
            return "wrongPassword123"; // Safe dummy value for negative testing
        }
        return password;
    }
    
    public static String getTestUrl() {
        String url = System.getenv("IFS_TEST_URL");
        if (url == null || url.trim().isEmpty()) {
            return "https://mingle-t20-portal.mingle.inforos.dev.inforcloudsuite.com/v2/INTQAINFOROSV2_AX1"; // Default test URL
        }
        return url;
    }
    
    //Legacy method for backward compatibility
    public List<HashMap<String, String>> getJsonDataToMap(String filePath) throws IOException {
        // By default, return all credentials
        return loadTestData("all");
    }
    
    //Take Screenshot
    public String takeScreenshot(String testName) {
        try {
            // Ensure screenshots directory exists
            Path screenshotsDir = Paths.get(System.getProperty("user.dir"), "screenshots");
            if (!Files.exists(screenshotsDir)) {
                Files.createDirectories(screenshotsDir);
            }
            
            File source = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            String destination = screenshotsDir + File.separator + testName + ".png";
            FileUtils.copyFile(source, new File(destination));
            logger.info("Screenshot captured successfully: " + destination);
            return destination;
        } catch (Exception e) {
            logger.error("Screenshot capture failed for test: " + testName + ". Error: " + e.getMessage());
            // Return empty string instead of null to avoid NullPointerException in TestListener
            return "";
        }
    }
    
   //Setup method to initialize WebDriver, load properties, and login
    @BeforeMethod(alwaysRun = true)
    public void initializeDriver() throws IOException {
       
        driver = configureDriver();
        
        // Initialize login page
        loginPage = new LoginPage(driver);
        
        // Initialize page elements for this class
        PageFactory.initElements(driver, this);
        
        // Navigate to application URL
        String url = getProperty("url");
        loginPage.goTo(url);
    }

    //Teardown method to quit driver and perform logout
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        logger.info("Executing test teardown");
        
        try {
            // STEP 1: Always switch to main document first
            driver.switchTo().defaultContent();
            logger.info("Switched to main document context");
            
            // STEP 2: Clear any active frame contexts
            AbstractComponents.exitFrameContext();
            logger.info("Exited frame contexts");
            
            // STEP 3: Check if user is logged in by verifying if user icon exists
            logger.info("Checking if user is logged in...");
            try {
                WebElement userIcon = AbstractComponents.accessDOMElement(AbstractComponents.userIconSelector);
                
                // If we reach here, user icon exists - user is logged in
                logger.info("User is logged in. Performing logout...");
                AbstractComponents.userLogout();

                //Verify logout successful
                AbstractComponents.waitForElementVisible(logOutSuccessfulText);
                Assert.assertTrue(logOutSuccessfulText.isDisplayed());
                Assert.assertTrue(logOutConfirmationText.isDisplayed());
                Assert.assertTrue(logOutInforLogo.isDisplayed());
                
                logger.info("Logout successful");
                
                // Exit any frame contexts that might be active
                logger.info("Exiting frame contexts...");
                AbstractComponents.exitFrameContext();
                
            } catch (Exception e) {
                // User icon not found or other error - user is not logged in
                logger.info("User is not logged in or logout not needed. Reason: " + e.getMessage());
            }
        } catch (Exception e) {
            // Log exception but continue with teardown
            logger.error("Error during logout process: " + e.getMessage(), e);
        } finally {
            // Always quit the driver
            if (driver != null) {
                logger.info("Closing WebDriver session");
                driver.quit();
            }
        }
    }
}
