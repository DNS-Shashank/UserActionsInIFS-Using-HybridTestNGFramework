package com.useractionsinifs.abstractComponents;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * This class contains common elements and methods for Security module sidebar navigation
 * It extends AbstractComponents to inherit all common methods for the application
 */
public class Security_AbstractComponents extends AbstractComponents {
    
    //JS variables
    private static final Logger logger = LogManager.getLogger(Security_AbstractComponents.class);
    
    public static String hamburgerIcon = "document.querySelector(\".btn-icon.application-menu-trigger.applicationMenuRTL\")";    
    

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


    
    //Navigation to Users
    public void navigateToUsers() {
        logger.info("Navigating to Users section");
        
        try {
            enterFirstAvailableIframe();
            
            // Wait for and verify Users page
            waitForShadowElement(exportAllUsersButton);
            
            // Toggle sidebar
            logger.info("Toggling security sidebar");
            waitForShadowElement(hamburgerIcon);
            accessDOMElement(hamburgerIcon).click();
            logger.info("Clicked on hamburger icon");
            Thread.sleep(1000);
            
            waitForShadowElement(manageButton);
            accessDOMElement(manageButton).click();
            logger.info("Clicked on Manage button");
            
            waitForShadowElement(usersButton);
            accessDOMElement(usersButton).click();
            logger.info("Clicked on Users button");
            
            waitForShadowElement(exportAllUsersButton);
            waitForPageLoad();
        } catch (Exception e) {
            logger.error("Failed to navigate to Users: " + e.getMessage(), e);
            throw new RuntimeException("Failed to navigate to Users", e);
        }
    }
    
    
}
