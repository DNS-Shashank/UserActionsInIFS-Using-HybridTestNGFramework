package com.useractionsinifs.testCases;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.useractionsinifs.abstractComponents.AbstractComponents;
import com.useractionsinifs.pageObjects.LandingPage;
import com.useractionsinifs.pageObjects.SecurityPage_Users;
import com.useractionsinifs.testComponents.BaseTest;

public class UserActionsTest extends BaseTest {
    
    private static final Logger logger = LogManager.getLogger(UserActionsTest.class);
    
    LandingPage landingPage;
    SecurityPage_Users securityPageUsers;
    
    @DataProvider(name = "myUserCredentials")
    public Object[][] getMyUserCredentials() throws IOException {
        // Load only "My user" credentials from the JSON file
        List<HashMap<String, String>> allCredentials = loadTestData("valid");
        
        // Find the credentials with description containing "My user"
        HashMap<String, String> myUser = null;
        for (HashMap<String, String> cred : allCredentials) {
            if (cred.get("description").contains("My user")) {
                myUser = cred;
                break;
            }
        }
        
        // If my user credentials are found, return them as data provider
        if (myUser != null) {
            return new Object[][] {{ myUser }};
        } else {
            logger.error("Could not find 'My user' credentials in the test data");
            return new Object[0][0]; // Empty array if no matching credentials found
        }
    }
    
    @Test(dataProvider = "myUserCredentials", description = "Create, Enable, Disable and Delete a user")
    public void userManagementTest(HashMap<String, String> userData) throws InterruptedException {
        logger.info("Starting User Management Test");
        logger.info("Using credentials: " + userData.get("description"));
        
        // Step 1: Login with My user credentials
        landingPage = loginPage.clickSignInButton(userData.get("username"), userData.get("password"));
        logger.info("Login successful");
        
        // Short wait to ensure page loads completely
        Thread.sleep(5000);
        Assert.assertTrue(landingPage.verifyLandingPage(), "Landing page verification failed");
        
        // Step 2: Navigate to Security page
        logger.info("Navigating to Security module");
        SecurityPage_Users securityPageUsers = landingPage.navigateToComponent("Security OS");
        Thread.sleep(3000);
        
        // Step 3: Create a new user
        logger.info("Creating a new test user");
        String[] newUserData = securityPageUsers.createUser();
        String userEmail = newUserData[2];
        logger.info("New user created with email: " + userEmail);
        Thread.sleep(3000);
        
        // Step 4: Disable the user
        logger.info("Disabling the user");
        securityPageUsers.enableOrDisableUser(userEmail, "Disable");
        Thread.sleep(3000);
        
        
        // Step 5: Enable the user
        logger.info("Enabling the created user");
        securityPageUsers.enableOrDisableUser(userEmail, "Enable");
        Thread.sleep(3000);
        
        // Step 6: Delete the user
        logger.info("Deleting the test user");
        securityPageUsers.deleteUser(userEmail);
        Thread.sleep(3000);
        
        // Verify user is deleted by searching for the email and checking no results appear
        Assert.assertFalse(securityPageUsers.deleteUserConfirmation(userEmail));
        logger.info("User management test completed successfully");
    }
}
