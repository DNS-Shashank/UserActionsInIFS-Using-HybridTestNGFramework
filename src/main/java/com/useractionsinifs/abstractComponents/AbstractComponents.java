package com.useractionsinifs.abstractComponents;

import java.io.FileInputStream;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Properties;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AbstractComponents {
    // Instance fields (new approach)
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static final Logger logger = LogManager.getLogger(AbstractComponents.class);
    protected int explicitWait;
    
    // Static fields (backward compatibility - will be deprecated)
    protected static WebDriver staticDriver;
    protected static WebDriverWait staticWait;
    static int staticExplicitWait = getExplicitWaitFromProperties();

    public static String appMenuSelector = "document.querySelector('#osp-nav-launcher').shadowRoot.querySelector('button')";
    public static String searchBarSelector = "document.querySelector('#osp-al-search').shadowRoot.querySelector('#osp-al-search-internal')";
    public static String applicationSelector = "document.querySelector(\"portal-app-launcher-item[class='ng-star-inserted']\")";
    public static String colemanIcon = "document.querySelector('#infor-coleman-da-panel > ids-icon')";
    public static String userIconSelector = "document.querySelector(\"#osp-nav-user-profile\").shadowRoot.querySelector(\"button\")";
    public static String userLogoutSelector = "document.querySelector('#osp-nav-menu-signout')";

    public AbstractComponents(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.explicitWait = getExplicitWaitFromProperties();
        
        // Update static references for backward compatibility
        staticDriver = driver;
        staticWait = this.wait;
    }
    
    //Get explicitWait value from properties file
    private static int getExplicitWaitFromProperties() {
        try {
            Properties prop = new Properties();
            FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + 
                    "/src/main/java/com/useractionsinifs/resources/GlobalProperties.properties");
            prop.load(fis);
            fis.close();
            return Integer.parseInt(prop.getProperty("explicitWait", "20"));
        } catch (Exception e) {
            logger.warn("Could not read explicitWait from properties, using default value 20");
            return 20;
        }
    }

    //Wait for an element to be visible (static - backward compatibility)
    public static void waitForElementVisible(WebElement element) {
        if (staticWait != null) {
            staticWait.until(ExpectedConditions.visibilityOf(element));
        } else {
            logger.error("Static wait is null - driver not initialized properly");
            throw new RuntimeException("Driver not initialized");
        }
    }

    //Wait for an element to be clickable (static - backward compatibility)
    public static void waitForElementClickable(WebElement element) {
        if (staticWait != null) {
            staticWait.until(ExpectedConditions.elementToBeClickable(element));
        } else {
            logger.error("Static wait is null - driver not initialized properly");
            throw new RuntimeException("Driver not initialized");
        }
    }

    //Wait for page to load (static - backward compatibility)
    public static void waitForPageLoad() {
        new WebDriverWait(staticDriver, Duration.ofSeconds(30)).until(
            webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete")
        );
    }
    
    //Track the current frame context (instance)
    protected String currentFrame = null;
    
    //Track the current frame context (static - backward compatibility)
    protected static String staticCurrentFrame = null;
    
    //Exit the current frame context and return to default content (static - backward compatibility)
    public static void exitFrameContext() {
        if (staticCurrentFrame != null) {
            logger.info("Switching back to main document");
            staticDriver.switchTo().defaultContent();
            logger.info("Exited frame context: " + staticCurrentFrame);
            staticCurrentFrame = null;
        }
    }
    
    //Switch to first available iframe (static - backward compatibility)
    public static boolean enterFirstAvailableIframe() {
        try {
            staticDriver.switchTo().defaultContent();
            Thread.sleep(1000);
            
            staticWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("iframe")));
            WebElement frameElement = staticDriver.findElement(By.tagName("iframe"));
            staticWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
            
            staticCurrentFrame = "__FIRST_AVAILABLE_IFRAME__";
            logger.info("Switched to first available iframe");
            return true;
        } catch (Exception e) {
            logger.error("Failed to switch to any iframe", e);
            return false;
        }
    }
    
    //Wait for a shadow DOM element to be visible (static - backward compatibility)
    public static boolean waitForShadowElement(String jsSelector) {
        if (staticDriver == null) {
            logger.error("Static driver is null - cannot wait for shadow element");
            return false;
        }
        
        logger.info("Waiting for shadow DOM element: " + jsSelector);
        try {
            new WebDriverWait(staticDriver, Duration.ofSeconds(staticExplicitWait))
                .until(driver -> {
                    try {
                        Object result = ((JavascriptExecutor) driver).executeScript(
                            "return " + jsSelector + " != null");
                        return Boolean.TRUE.equals(result);
                    } catch (Exception e) {
                        return false;
                    }
                });
            logger.info("Shadow DOM element found: " + jsSelector);
            return true;
        } catch (Exception e) {
            logger.error("Timeout waiting for shadow DOM element: " + jsSelector);
            return false;
        }
    }

    public static void appMenuNavigation(String componentName) {
        // Click on app menu button
        accessDOMElement(appMenuSelector).click();
        
        // Enter search text
        accessDOMElement(searchBarSelector).sendKeys(componentName);
        
        waitForShadowElement(applicationSelector);

        //Clicking on Application
        accessDOMElement(applicationSelector).click();

        waitForShadowElement(colemanIcon);
    }
    
    //Random User Data Generator
    public static String[] generateRandomUserData() {
        // Generate random alphanumeric string (limited to 5 chars for readability)
        String randomText = UUID.randomUUID().toString().substring(0, 5);
        
        // Create first name and last name with required format
        String firstName = "SeleniumUser";
        String lastName = randomText;
        
        // Create email with required format
        String email = firstName + "." + lastName + "@infor.com";
        
        logger.info("Generated test user: " + firstName + " " + lastName + " <" + email + ">");
        
        return new String[]{firstName, lastName, email};
    }
    
    //User Password Generator
    public static String userPasswordGenerator() {
        // Define character pools
        String uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercaseChars = "abcdefghijklmnopqrstuvwxyz";
        String numberChars = "0123456789";
        String specialChars = "!@#$%^&*()-_=+[]{}|;:,.<>/?";
        
        // Use SecureRandom for better randomization
        SecureRandom random = new SecureRandom();
        
        // Determine password length between 8 and 12 characters
        int passwordLength = random.nextInt(5) + 8; // Range: 8-12
        
        // Ensure we have at least one character from each required type
        char[] password = new char[passwordLength];
        
        // Add one of each required character type
        password[0] = uppercaseChars.charAt(random.nextInt(uppercaseChars.length()));
        password[1] = lowercaseChars.charAt(random.nextInt(lowercaseChars.length()));
        password[2] = numberChars.charAt(random.nextInt(numberChars.length()));
        password[3] = specialChars.charAt(random.nextInt(specialChars.length()));
        
        // Fill the rest with random chars from all pools
        String allChars = uppercaseChars + lowercaseChars + numberChars + specialChars;
        for (int i = 4; i < passwordLength; i++) {
            password[i] = allChars.charAt(random.nextInt(allChars.length()));
        }
        
        // Shuffle the array to randomize character positions
        for (int i = 0; i < passwordLength; i++) {
            int randomPosition = random.nextInt(passwordLength);
            char temp = password[i];
            password[i] = password[randomPosition];
            password[randomPosition] = temp;
        }
        
        String generatedPassword = new String(password);
        logger.info("Generated a secure random password with length: " + passwordLength);
        
        return generatedPassword;
    }
    
    //Access shadow DOM elements using JavaScript (static - backward compatibility)
    public static WebElement accessDOMElement(String selector) {
        try {
            Boolean exists = (Boolean)((JavascriptExecutor) staticDriver).executeScript("return (" + selector + " != null)");
            
            if (!exists) {
                logger.error("Shadow DOM element not found: " + selector);
                throw new RuntimeException("Shadow DOM element not found: " + selector + 
                    ". Please verify the selector or wait for element to be available.");
            }
            
            return (WebElement) ((JavascriptExecutor) staticDriver).executeScript("return " + selector);
            
        } catch (Exception e) {
            logger.error("Error accessing shadow DOM element: " + selector, e);
            throw new RuntimeException("Failed to access shadow DOM element: " + selector, e);
        }
    }
    
    //User Logout - Check if logged in and logout if needed (static - backward compatibility)
    public static void userLogout() {
        try {
            logger.info("Checking if user is logged in");
            
            // Switch to main content first to find user icon
            staticDriver.switchTo().defaultContent();
            logger.info("Switched to main content for logout");
            
            if (waitForShadowElement(userIconSelector)) {
                logger.info("User is logged in, performing logout");
                
                accessDOMElement(userIconSelector).click();
                Thread.sleep(1000);
                
                if (waitForShadowElement(userLogoutSelector)) {
                    accessDOMElement(userLogoutSelector).click();
                    logger.info("User logged out successfully");
                    Thread.sleep(2000);
                } else {
                    logger.warn("Logout option not found after clicking user icon");
                }
            } else {
                logger.info("User is not logged in or login page is displayed");
            }
        } catch (Exception e) {
            logger.warn("Logout failed or user was not logged in: " + e.getMessage());
        }
    }
}