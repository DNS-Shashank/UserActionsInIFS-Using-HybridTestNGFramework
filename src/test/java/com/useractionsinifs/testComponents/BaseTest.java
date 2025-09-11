package com.useractionsinifs.testComponents;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.useractionsinifs.pageObjects.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.DataProvider;
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
        prop.load(fis);
        
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
    
    //Get property from properties file
    public String getProperty(String key) {
        return prop.getProperty(key);
    }
    
    //Shared DataProvider for user credentials (eliminates duplication across test classes)
    @DataProvider(name = "myUserCredentials")
    public Object[][] getMyUserCredentials() throws IOException {
        List<HashMap<String, String>> data = loadTestData("valid");
        return new Object[][] {{ data.get(0) }};
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

        // Parse JSON structure directly
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
            return "";
        }
    }
    
   //Setup method to initialize WebDriver, load properties, and login
    @BeforeMethod(alwaysRun = true)
    public void initializeDriver() throws IOException {
       
        driver = configureDriver();
        
        // Initialize login page
        loginPage = new LoginPage(driver);
        

        // Navigate to application URL
        String url = getProperty("url");
        loginPage.goTo(url);
    }

    //Teardown method to logout user and quit driver
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        logger.info("Executing test teardown");
        
        if (driver != null) {
            try {
                // Attempt to logout user if logged in
                com.useractionsinifs.abstractComponents.AbstractComponents.userLogout();
            } catch (Exception e) {
                logger.warn("Logout attempt failed during teardown: " + e.getMessage());
            }
            
            // Always quit the driver
            logger.info("Closing WebDriver session");
            driver.quit();
        }
    }
}