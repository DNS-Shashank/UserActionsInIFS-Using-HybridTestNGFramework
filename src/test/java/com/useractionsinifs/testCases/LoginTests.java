package com.useractionsinifs.testCases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.useractionsinifs.abstractComponents.AbstractComponents;
import com.useractionsinifs.pageObjects.LandingPage;
import com.useractionsinifs.testComponents.BaseTest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class LoginTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(LoginTests.class);
    
    LandingPage landingPage;
    
    @DataProvider(name = "validLoginData")
    public Object[][] getValidLoginData() throws IOException {
        List<HashMap<String, String>> data = loadTestData("valid");
        return new Object[][] {{ data.get(0) }};
    }
    
    @DataProvider(name = "invalidLoginData")
    public Object[][] getInvalidLoginData() throws IOException {
        List<HashMap<String, String>> data = loadTestData("invalid");
        return data.stream().map(item -> new Object[]{item}).toArray(Object[][]::new);
    }
    
    @Test(dataProvider = "validLoginData", groups = "login", priority = 1, description = "Verify user can login and logout successfully with valid credentials")
    public void validLoginTest(HashMap<String, String> userData) throws InterruptedException {
        logger.info("Starting Valid Login Test");
        logger.info("Using credentials: " + userData.get("description"));
        
        // Login using data from JSON
        landingPage = loginPage.clickSignInButton(userData.get("username"), userData.get("password"));
        logger.info("Verifying successful login");
        Thread.sleep(8000);
        Assert.assertTrue(landingPage.verifyLandingPage());
        logger.info("Login test completed successfully");
    }
    
    @Test(dataProvider = "invalidLoginData", groups = "login", priority = 2, description = "Verify system properly handles invalid login attempts")
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