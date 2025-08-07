package com.useractionsinifs.testCases;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.useractionsinifs.pageObjects.LandingPage;
import com.useractionsinifs.pageObjects.SecurityPage_Users;
import com.useractionsinifs.testComponents.BaseTest;

public class UserSecurityRolesTest extends BaseTest {
    
    private static final Logger logger = LogManager.getLogger(UserSecurityRolesTest.class);
    
    LandingPage landingPage;
    SecurityPage_Users securityPageUsers;
    private static final String SECURITY_ROLE = "System Administration";
    
    @DataProvider(name = "myUserCredentials")
    public Object[][] getMyUserCredentials() throws IOException {
        List<HashMap<String, String>> allCredentials = loadTestData("valid");
        
        HashMap<String, String> myUser = null;
        for (HashMap<String, String> cred : allCredentials) {
            if (cred.get("description").contains("My user")) {
                myUser = cred;
                break;
            }
        }
        
        if (myUser != null) {
            return new Object[][] {{ myUser }};
        } else {
            logger.error("Could not find 'My user' credentials in the test data");
            return new Object[0][0];
        }
    }
    
    @Test(dataProvider = "myUserCredentials", groups = "regression", priority = 3, dependsOnMethods = "com.useractionsinifs.testCases.UserCreationTest.createUserTest", description = "Test adding security roles to user")
    public void addSecurityRolesTest(HashMap<String, String> userData) throws InterruptedException {
        logger.info("Starting Security Roles Test");
        
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
        
        // Add security roles
        String assignedRole = securityPageUsers.addSecurityRoles(testUserEmail, SECURITY_ROLE);
        Assert.assertEquals(assignedRole, SECURITY_ROLE, "Security role assignment failed");
        logger.info("Security roles added successfully");
        Thread.sleep(3000);
        
        // Navigate back to users page
        securityPageUsers.navigateToUsersFromUserDrilldownPage();
        Thread.sleep(3000);
        
        logger.info("Security roles test completed successfully");
    }
}