package com.useractionsinifs.testCases;

import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.useractionsinifs.pageObjects.LandingPage;
import com.useractionsinifs.pageObjects.SecurityPage_Users;
import com.useractionsinifs.testComponents.BaseTest;

public class UserPasswordResetTest extends BaseTest {
    
    private static final Logger logger = LogManager.getLogger(UserPasswordResetTest.class);
    
    LandingPage landingPage;
    SecurityPage_Users securityPageUsers;
    

    @Test(dataProvider = "myUserCredentials", groups = "smoke", priority = 2, dependsOnMethods = "com.useractionsinifs.testCases.UserCreationTest.createUserTest", description = "Reset password for created user")
    public void resetPasswordTest(HashMap<String, String> userData) throws InterruptedException {
        logger.info("Starting Password Reset Test");
        
        // Login
        landingPage = loginPage.clickSignInButton(userData.get("username"), userData.get("password"));
        Thread.sleep(5000);
        Assert.assertTrue(landingPage.verifyLandingPage(), "Landing page verification failed");
        
        // Navigate to Security
        securityPageUsers = landingPage.navigateToComponent("Security OS");
        Thread.sleep(8000);
        
        // Force frame switch before performing actions
        securityPageUsers.enterFirstAvailableIframe();
        Thread.sleep(8000); 
        
        // Reset password for created user
        String newPassword = securityPageUsers.resetPasswordByAdmin(UserCreationTest.createdUserEmail, securityPageUsers.userPasswordGenerator());
        logger.info("Password reset successfully for user: " + UserCreationTest.createdUserEmail);
        
        Assert.assertNotNull(newPassword, "Password reset failed");
        logger.info("Password reset test completed successfully");
    }
}