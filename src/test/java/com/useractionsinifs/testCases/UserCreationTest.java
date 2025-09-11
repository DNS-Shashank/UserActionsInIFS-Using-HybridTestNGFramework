package com.useractionsinifs.testCases;

import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.useractionsinifs.pageObjects.LandingPage;
import com.useractionsinifs.pageObjects.SecurityPage_Users;
import com.useractionsinifs.testComponents.BaseTest;

public class UserCreationTest extends BaseTest {
    
    private static final Logger logger = LogManager.getLogger(UserCreationTest.class);
    
    // Static variables for sharing data across dependent tests (thread-safe)
    public static volatile String createdUserEmail;
    public static volatile String[] createdUserData;
    
    LandingPage landingPage;
    SecurityPage_Users securityPageUsers;
    

    @Test(dataProvider = "myUserCredentials", groups = {"smoke", "regression"}, priority = 1, description = "Create a new user")
    public void createUserTest(HashMap<String, String> userData) throws InterruptedException {
        logger.info("Starting User Creation Test");
        
        // Login
        landingPage = loginPage.clickSignInButton(userData.get("username"), userData.get("password"));
        Thread.sleep(5000);
        Assert.assertTrue(landingPage.verifyLandingPage(), "Landing page verification failed");
        
        // Navigate to Security
        securityPageUsers = landingPage.navigateToComponent("Security OS");
        Thread.sleep(3000);
        
        // Create user
        createdUserData = securityPageUsers.createUser();
        createdUserEmail = createdUserData != null && createdUserData.length > 2 ? createdUserData[2] : null;
        
        logger.info("User created successfully with email: " + createdUserEmail);
        Thread.sleep(3000); 
        
        // Verify user exists
        Assert.assertEquals(securityPageUsers.createdUserConfirmation(createdUserEmail), createdUserEmail);
        logger.info("User creation test completed successfully");
    }
}