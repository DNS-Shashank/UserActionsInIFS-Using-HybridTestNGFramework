package com.useractionsinifs.abstractComponents;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * This class contains common elements and methods for Security module sidebar navigation
 * It extends AbstractComponents to inherit all common methods for the application
 */
public class Security_AbstractComponents extends AbstractComponents {
    
    //JS variables
    private static final Logger logger = LogManager.getLogger(Security_AbstractComponents.class);
    private static final String SECURITY_FRAME = "iframe[id='frame_75_infor-ifs-user-management']";
    
    public static String hamburgerIcon = "document.querySelector(\".btn-icon.application-menu-trigger.applicationMenuRTL\")";    
    
    // Toast message elements
    @FindBy(id = "toast-container")
    private WebElement toastContainer;
    
    @FindBy(css = ".toast-effect-scale")
    private WebElement toastEffectScale;
    
    @FindBy(css = ".toast-title")
    private WebElement toastTitle;
    
    @FindBy(css = ".toast-message")
    private WebElement toastMessage;
    
    @FindBy(css = ".btn-icon.btn-close")
    private WebElement toastCloseButton;
    public static String exportAllUsersButton = "document.querySelector('#btnExportAllUsers')";
    public static String manageButton = "document.querySelector(\"#inforApplicationNav1 > div.accordion-header\")";
    public static String usersButton = "document.querySelector(\"#users\")";
    public static String fullnameHeader = "document.querySelector(\"#datagrid-1-header-4\")";

    
    
    // Constructor that initializes WebDriver and PageFactory
    public Security_AbstractComponents(WebDriver driver) {
        super(driver); // Call the constructor of AbstractComponents
        PageFactory.initElements(driver, this); // Initialize elements with PageFactory
        logger.info("Security_AbstractComponents initialized");
    }

    //Switch to security frame context
    public static void enterSecurityFrame() {
        enterFrameContext(SECURITY_FRAME);
    }
    
    //Exit security frame context
    public static void exitSecurityFrame() {
        exitFrameContext();
    }
    
    //Verify security users page
    public static boolean isAtSecurityPage(String pageName) {
        try {
            waitForShadowElement(pageName);
            return true;
        } catch (Exception e) {
            logger.warn("Failed to verify security page: " + e.getMessage());
            return false;
        }
    }
    
    //Toggle security sidebar
    public static void toggleSidebar() {
        logger.info("Toggling security sidebar");
        waitForShadowElement(hamburgerIcon);
        accessDOMElement(hamburgerIcon).click();
        logger.info("Clicked on hamburger icon");
        // Wait for animation to complete
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            logger.warn("Sleep interrupted while waiting for sidebar animation", e);
        }
    }
    
    //Navigation to Users
    public void navigateToUsers() {
        logger.info("Navigating to Users section");
        
        try {
            // Enter security frame context
            //enterSecurityFrame(); ->Commented out for now, activate when needed

            enterFirstAvailableIframe();
            
            //Verify Users page in Security
            isAtSecurityPage(exportAllUsersButton);
            toggleSidebar();
            waitForShadowElement(manageButton);
            accessDOMElement(manageButton).click();
            logger.info("Clicked on Manage button");
            
            waitForShadowElement(usersButton);
            accessDOMElement(usersButton).click();
            logger.info("Clicked on Users button");
            
            isAtSecurityPage(exportAllUsersButton);
            waitForPageLoad();
        } catch (Exception e) {
            logger.error("Failed to navigate to Users: " + e.getMessage(), e);
            throw new RuntimeException("Failed to navigate to Users", e);
        }

    }
    
    
}
