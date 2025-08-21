package com.useractionsinifs.testCases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.useractionsinifs.abstractComponents.AbstractComponents;
// AbstractComponents import removed as logout is now handled in BaseTest
import com.useractionsinifs.pageObjects.LandingPage;
import com.useractionsinifs.testComponents.BaseTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class LoginTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(LoginTests.class);
    
    LandingPage landingPage;
    //New comment
    @DataProvider(name = "validLoginData")
    public Object[][] getValidLoginData() {
        // Create credential HashMap directly from environment variables
        HashMap<String, String> validCredentials = new HashMap<>();
        validCredentials.put("username", getValidUsername());
        validCredentials.put("password", getValidPassword());
        validCredentials.put("description", "Valid user credentials from environment");
        
        return new Object[][] {{ validCredentials }};
    }
    
    @DataProvider(name = "invalidLoginData")
    public Object[][] getInvalidLoginData() {
        // Create multiple invalid credential scenarios
        return new Object[][] {
            { createCredentialMap(getInvalidUsername(), getInvalidPassword(), "Invalid username and password") },
            { createCredentialMap(getValidUsername(), "wrongPassword123", "Valid username but invalid password") },
            { createCredentialMap("invaliduser@test.com", getValidPassword(), "Invalid username but valid password") },
            { createCredentialMap("", "validPassword1", "Empty username") },
            { createCredentialMap("testuser@infor.com", "", "Empty password") }
        };
    }
    
    private HashMap<String, String> createCredentialMap(String username, String password, String description) {
        HashMap<String, String> credentials = new HashMap<>();
        credentials.put("username", username);
        credentials.put("password", password);
        credentials.put("description", description);
        return credentials;
    }
    
    @Test(dataProvider = "validLoginData", groups = "login",priority = 1,description = "Verify user can login and logout successfully with valid credentials")
    public void validLoginTest(HashMap<String, String> userData) throws InterruptedException {
        logger.info("Starting Valid Login Test");
        logger.info("Using credentials: " + userData.get("description"));
        
        // Login using data from JSON
        landingPage = loginPage.clickSignInButton(userData.get("username"), userData.get("password"));
        logger.info("Verifying successful login");
        Thread.sleep(8000);
        Assert.assertTrue(landingPage.verifyLandingPage());
        // Test is complete - logout will be handled by BaseTest tearDown
        logger.info("Login test completed successfully");
    }
    
    @Test(dataProvider = "invalidLoginData", groups = "login",priority = 2,description = "Verify system properly handles invalid login attempts")
    public void invalidLoginTest(HashMap<String, String> userData) throws InterruptedException {
        logger.info("Starting Invalid Login Test");
        logger.info("Using invalid credentials: " + userData.get("description"));
        
        try {
            // Attempt login using invalid data from JSON
            loginPage.clickSignInButton(userData.get("username"), userData.get("password"));

            if (userData.get("description").contains("Empty username")) {
                AbstractComponents.waitForElementVisible(loginPage.emprtyUserNameErrorText);
                Assert.assertTrue(loginPage.emprtyUserNameErrorText.isDisplayed());
                logger.info("Invalid login test completed successfully");
            } else if (userData.get("description").contains("Empty password")) {
                AbstractComponents.waitForElementVisible(loginPage.emprtyPasswordErrorText);
                Assert.assertTrue(loginPage.emprtyPasswordErrorText.isDisplayed());
                logger.info("Invalid login test completed successfully");
            } else{
                AbstractComponents.waitForElementVisible(loginPage.invalidUserLoginErrorText);
                Assert.assertTrue(loginPage.invalidUserLoginErrorText.isDisplayed());
                logger.info("Invalid login test completed successfully");
            }
        } catch (Exception e) {
            logger.error("Exception during invalid login test: " + e.getMessage());
            Assert.fail("Invalid login test failed: " + e.getMessage());
        }
    }
}
