package com.useractionsinifs.testCases;

import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.useractionsinifs.pageObjects.LandingPage;
import com.useractionsinifs.pageObjects.SecurityPage_Users;
import com.useractionsinifs.testComponents.BaseTest;

public class UserEnableDisableTest extends BaseTest {
    
    private static final Logger logger = LogManager.getLogger(UserEnableDisableTest.class);
    
    LandingPage landingPage;
    SecurityPage_Users securityPageUsers;
    

    @Test(dataProvider = "myUserCredentials", groups = "regression", priority = 2, dependsOnMethods = "com.useractionsinifs.testCases.UserCreationTest.createUserTest", description = "Test user enable and disable functionality")
    public void enableDisableUserTest(HashMap<String, String> userData) throws InterruptedException {
        logger.info("Starting User Enable/Disable Test");
        
        // Login
        landingPage = loginPage.clickSignInButton(userData.get("username"), userData.get("password"));
        Thread.sleep(5000);
        Assert.assertTrue(landingPage.verifyLandingPage(), "Landing page verification failed");
        
        // Navigate to Security
        securityPageUsers = landingPage.navigateToComponent("Security OS");
        Thread.sleep(8000);
        
        // Force frame switch before performing actions
        securityPageUsers.enterFirstAvailableIframe();
        Thread.sleep(2000);
        
        // Use created user from UserCreationTest
        String testUserEmail = UserCreationTest.createdUserEmail;
        logger.info("Using created user with email: " + testUserEmail);
        
        // Disable user
        securityPageUsers.enableOrDisableUser(testUserEmail, "Disable");
        logger.info("User disabled successfully");
        Thread.sleep(3000);
        
        // Enable user
        securityPageUsers.enableOrDisableUser(testUserEmail, "Enable");
        logger.info("User enabled successfully");
        Thread.sleep(3000);
        
        logger.info("User enable/disable test completed successfully");
    }
}