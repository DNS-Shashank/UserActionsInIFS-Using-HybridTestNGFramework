package com.useractionsinifs.abstractComponents;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.UUID;
import java.util.Random;
import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AbstractComponents {
    protected static WebDriver driver;
    protected static WebDriverWait wait;
    protected static final Logger logger = LogManager.getLogger(AbstractComponents.class);
    static int explicitWait = 20;


    public static String appMenuSelector = "document.querySelector('#osp-nav-launcher').shadowRoot.querySelector('button')";
    public static String searchBarSelector = "document.querySelector('#osp-al-search').shadowRoot.querySelector('#osp-al-search-internal')";
    public static String applicationSelector = "document.querySelector(\"portal-app-launcher-item[class='ng-star-inserted']\")";
    public static String colemanIcon = "document.querySelector('#infor-coleman-da-panel > ids-icon')";
    public static String userIconSelector = "document.querySelector(\"#osp-nav-user-profile\").shadowRoot.querySelector(\"button\")";
    public static String userLogoutSelector = "document.querySelector('#osp-nav-menu-signout')";

    public AbstractComponents(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    //Wait for an element to be visible
    public static void waitForElementVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    //Wait for an element to be clickable
    public static void waitForElementClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    //Wait for page to load
    public static void waitForPageLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(30)).until(
            webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete")
        );
    }
    
    //Track the current frame context
    protected static String currentFrame = null;
    
    //Enter a frame context - only switches if not already in the frame
    public static void enterFrameContext(String frameIdentifier) {
        if (currentFrame == null || !currentFrame.equals(frameIdentifier)) {
            try {
                // Switch back to main content first
                driver.switchTo().defaultContent();
                // Allow some time for the frame to be available
                Thread.sleep(1000);
                
                // Try multiple frame location strategies
                boolean frameSwitched = false;
                
                try {
                    // First try by ID or name directly
                    wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameIdentifier));
                    frameSwitched = true;
                    logger.info("Switched to frame by ID/name: " + frameIdentifier);
                } catch (Exception e) {
                    logger.info("Could not find frame by ID/name, trying CSS selectors");
                }
                
                // Check if the identifier looks like a CSS selector
                if (!frameSwitched && (frameIdentifier.contains("#") || 
                                      frameIdentifier.contains(".") || 
                                      frameIdentifier.contains("[") || 
                                      frameIdentifier.contains(">")))
                {
                    try {
                        // Try direct CSS selector
                        WebElement frameElement = driver.findElement(By.cssSelector(frameIdentifier));
                        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
                        frameSwitched = true;
                        logger.info("Switched to frame using direct CSS selector: " + frameIdentifier);
                    } catch (Exception e) {
                        logger.info("Could not find frame using direct CSS selector");
                    }
                }
                
                if (!frameSwitched) {
                    try {
                        // Try by CSS selector (for frames with id attribute)
                        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            driver.findElement(By.cssSelector("iframe#" + frameIdentifier))
                        ));
                        frameSwitched = true;
                        logger.info("Switched to frame by CSS selector #id: " + frameIdentifier);
                    } catch (Exception e) {
                        logger.info("Could not find frame by CSS #id selector");
                    }
                }
                
                if (!frameSwitched) {
                    try {
                        // Try by CSS selector (for frames with name attribute)
                        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                            driver.findElement(By.cssSelector("iframe[name='" + frameIdentifier + "']"))
                        ));
                        frameSwitched = true;
                        logger.info("Switched to frame by CSS name selector: " + frameIdentifier);
                    } catch (Exception e) {
                        logger.info("Could not find frame by CSS name selector");
                    }
                }

                if (!frameSwitched) {
                    try {
                        // Try by CSS selector with just a simple iframe tag and index
                        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("iframe")));
                        // Get all iframes, try to use the first one found
                        WebElement frameElement = driver.findElement(By.tagName("iframe"));
                        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
                        frameSwitched = true;
                        logger.info("Switched to first available iframe as fallback");
                    } catch (Exception e) {
                        logger.info("Could not find any frames at all");
                    }
                }
                
                if (!frameSwitched) {
                    throw new RuntimeException("Failed to locate frame using any strategy: " + frameIdentifier);
                }
                
                currentFrame = frameIdentifier;
                logger.info("Successfully entered frame context: " + frameIdentifier);
            } catch (Exception e) {
                logger.error("Failed to enter frame context: " + frameIdentifier, e);
                throw new RuntimeException("Failed to enter frame: " + frameIdentifier, e);
            }
        }
    }
    
    //Exit the current frame context and return to default content
    public static void exitFrameContext() {
        if (currentFrame != null) {
            logger.info("Switching back to main document");
            driver.switchTo().defaultContent();
            logger.info("Exited frame context: " + currentFrame);
            currentFrame = null;
        }
    }
    
    //switch to first available iframe
    public static boolean enterFirstAvailableIframe() {
        try {
            // Switch back to main content first
            driver.switchTo().defaultContent();
            // Allow some time for the frame to be available
            Thread.sleep(1000);
            
            // Try by CSS selector with just a simple iframe tag
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("iframe")));
            // Get all iframes, try to use the first one found
            WebElement frameElement = driver.findElement(By.tagName("iframe"));
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
            
            // Update the current frame context to a special value indicating first frame
            currentFrame = "__FIRST_AVAILABLE_IFRAME__";
            logger.info("Switched to first available iframe");
            return true;
        } catch (Exception e) {
            logger.error("Failed to switch to any iframe", e);
            return false;
        }
    }
    
    //Execute an action within a specific frame context
    public static void executeInFrameContext(String frameName, Runnable action) {
        try {
            enterFrameContext(frameName);
            action.run();
        } finally {
            exitFrameContext();
        }
    }
    
    //Wait for a shadow DOM element to be visible
    public static boolean waitForShadowElement(String jsSelector) {
        logger.info("Waiting for shadow DOM element: " + jsSelector);
        try {
            // Use explicit wait with custom condition
            new WebDriverWait(driver, Duration.ofSeconds(explicitWait))
                .until(driver -> {
                    try {
                        // Try to find the element
                        Object result = ((JavascriptExecutor) driver).executeScript(
                            "return " + jsSelector + " != null");
                            System.out.println(result + " this is for " + jsSelector);
                        // If element exists and is true, we're done waiting
                        return Boolean.TRUE.equals(result);
                    } catch (Exception e) {
                        // If there's an error executing the script, keep waiting
                        return false;
                    }
                });
            logger.info("Shadow DOM element found: " + jsSelector);
            return true; // Return true when the element is found successfully
        } catch (Exception e) {
            logger.error("Timeout waiting for shadow DOM element: " + jsSelector);
            return false; // Return false when the element is not found
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
    
    //Access shadow DOM elements using JavaScript
    public static WebElement accessDOMElement(String selector) {
        try {
            // First check if element exists
            Boolean exists = (Boolean)((JavascriptExecutor) driver).executeScript("return (" + selector + " != null)");
            
            if (!exists) {
                logger.warn("Element not found: " + selector);
                return null;
            }
            
            // Use getOuter/InnerHTML or textContent instead of direct casting
            return (WebElement)((JavascriptExecutor) driver).executeScript(
                "return arguments[0]", 
                ((JavascriptExecutor) driver).executeScript("return " + selector)
            );
        } catch (Exception e) {
            logger.error("Shadow DOM access error: " + e.getMessage());
            return null;
        }
    }
    

    //Logout
    public static void userLogout() {
        try {
            // Click logout button
            logger.info("Clicking logout button");
            accessDOMElement(userIconSelector).click();
            waitForShadowElement(userLogoutSelector);
            accessDOMElement(userLogoutSelector).click();
            logger.info("Logout successful");
        } catch (Exception e) {
            logger.error("Logout failed", e);
            throw new RuntimeException("Logout failed", e);
        }
    }

    //Upload file
    public static boolean uploadFile(WebElement fileInput, String filePath) {
        File file = new File(filePath);
        
        // Check if parent directories exist, create if not
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
            logger.info("Created directory structure for: " + file.getParentFile().getAbsolutePath());
        }
        
        // Verify file exists
        if (!file.exists()) {
            logger.error("File not found at: " + filePath);
            return false;
        }
        
        try {
            logger.info("Uploading file: " + filePath);
            fileInput.sendKeys(file.getAbsolutePath());
            logger.info("File uploaded successfully: " + file.getName());
            return true;
        } catch (Exception e) {
            logger.error("Failed to upload file: " + file.getName(), e);
            return false;
        }
    }
}

