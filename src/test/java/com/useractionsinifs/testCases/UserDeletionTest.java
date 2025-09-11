package com.useractionsinifs.testCases;

import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.useractionsinifs.pageObjects.LandingPage;
import com.useractionsinifs.pageObjects.SecurityPage_Users;
import com.useractionsinifs.testComponents.BaseTest;

public class UserDeletionTest extends BaseTest {
    
    private static final Logger logger = LogManager.getLogger(UserDeletionTest.class);
    
    LandingPage landingPage;
    SecurityPage_Users securityPageUsers;
    

    @Test(dataProvider = "myUserCredentials", groups = {"smoke", "regression"}, priority = 10, dependsOnMethods = {"com.useractionsinifs.testCases.UserCreationTest.createUserTest", "com.useractionsinifs.testCases.UserPasswordResetTest.resetPasswordTest"}, description = "Delete the created user")
    public void deleteUserTest(HashMap<String, String> userData) throws InterruptedException {
        logger.info("Starting User Deletion Test");
        
        // Login
        landingPage = loginPage.clickSignInButton(userData.get("username"), userData.get("password"));
        Thread.sleep(5000);
        Assert.assertTrue(landingPage.verifyLandingPage(), "Landing page verification failed");
        
        // Navigate to Security
        securityPageUsers = landingPage.navigateToComponent("Security OS");
        Thread.sleep(8000);
        
        // Force frame switch before performing actions
        securityPageUsers.enterFirstAvailableIframe();
        Thread.sleep(6000);
        
        // Delete the created user
        securityPageUsers.deleteUser(UserCreationTest.createdUserEmail);
        Thread.sleep(3000);
        System.out.println(); 
        // Verify user is deleted
        Assert.assertFalse(securityPageUsers.deleteUserConfirmation(UserCreationTest.createdUserEmail));
        
        logger.info("User deletion test completed successfully");
    }
}