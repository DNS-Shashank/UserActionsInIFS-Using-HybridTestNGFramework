package com.useractionsinifs.testCases;


import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.useractionsinifs.pageObjects.LandingPage;
import com.useractionsinifs.pageObjects.SecurityPage_Users;
import com.useractionsinifs.testComponents.BaseTest;

public class UserCopyTest extends BaseTest {
    
    private static final Logger logger = LogManager.getLogger(UserCopyTest.class);
    
    LandingPage landingPage;
    SecurityPage_Users securityPageUsers;
    

    @Test(dataProvider = "myUserCredentials", groups = "regression", priority = 3, dependsOnMethods = "com.useractionsinifs.testCases.UserCreationTest.createUserTest", description = "Test user copy functionality")
    public void copyUserTest(HashMap<String, String> userData) throws InterruptedException {
        logger.info("Starting User Copy Test");
        
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
        
        // Use created user from UserCreationTest as base user
        String baseUserEmail = UserCreationTest.createdUserEmail;
        logger.info("Using created user as base for copy: " + baseUserEmail);
        
        // Copy user - this creates a new user with random data
        String copiedUserEmail = securityPageUsers.copyUser(baseUserEmail);
        logger.info("User copied successfully and new Email is" + copiedUserEmail);
        Thread.sleep(3000);
                
        logger.info("User copy test completed successfully");
    }
}