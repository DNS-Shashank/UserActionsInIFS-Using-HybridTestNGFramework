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

public class UserImportExportTest extends BaseTest {
    
    private static final Logger logger = LogManager.getLogger(UserImportExportTest.class);
    
    LandingPage landingPage;
    SecurityPage_Users securityPageUsers;
    
    @DataProvider(name = "myUserCredentials")
    public Object[][] getMyUserCredentials() {
        HashMap<String, String> credentials = new HashMap<>();
        credentials.put("username", getValidUsername());
        credentials.put("password", getValidPassword());
        credentials.put("description", "Valid user credentials from environment");
        
        return new Object[][] {{ credentials }};
    }
    
    @Test(dataProvider = "myUserCredentials", groups = "regression", priority = 2, description = "Test user import functionality")
    public void importUsersTest(HashMap<String, String> userData) throws InterruptedException {
        logger.info("Starting User Import Test");
        
        // Login
        landingPage = loginPage.clickSignInButton(userData.get("username"), userData.get("password"));
        Thread.sleep(5000);
        Assert.assertTrue(landingPage.verifyLandingPage(), "Landing page verification failed");
        
        // Navigate to Security
        securityPageUsers = landingPage.navigateToComponent("Security OS");
        Thread.sleep(8000);
        
        // Import users - Note: This test assumes import template exists and is valid
        // Imported users will need manual cleanup as we cannot track them automatically
        securityPageUsers.importUsers();
        logger.info("Users imported successfully");
        Thread.sleep(3000);
        
        logger.info("User import test completed successfully");
    }
    
    @Test(dataProvider = "myUserCredentials", groups = "regression", priority = 3, description = "Test user export functionality")
    public void exportUsersTest(HashMap<String, String> userData) throws InterruptedException {
        logger.info("Starting User Export Test");
        
        // Login
        landingPage = loginPage.clickSignInButton(userData.get("username"), userData.get("password"));
        Thread.sleep(5000);
        Assert.assertTrue(landingPage.verifyLandingPage(), "Landing page verification failed");
        
        // Navigate to Security
        securityPageUsers = landingPage.navigateToComponent("Security OS");
        Thread.sleep(3000);
        
        // Create test user for export
        String[] testUserData = securityPageUsers.createUser();
        String testUserEmail = testUserData[2];
        logger.info("Test user created with email: " + testUserEmail);
        Thread.sleep(3000);
        
        // Export user in CSV format
        securityPageUsers.exportUsersByCSVorXML(testUserEmail, "CSV");
        logger.info("User exported in CSV format");
        Thread.sleep(3000);
        
        // Export user in XML format
        securityPageUsers.exportUsersByCSVorXML(testUserEmail, "XML");
        logger.info("User exported in XML format");
        Thread.sleep(3000);
        
        // Export all users in CSV format
        securityPageUsers.exportAllUsersByCSVorXML("CSV");
        logger.info("All users exported successfully in CSV format");
        Thread.sleep(3000);
        
        // Export all users in XML format
        securityPageUsers.exportAllUsersByCSVorXML("XML");
        logger.info("All users exported successfully in XML format");
        Thread.sleep(3000);
        
        // Cleanup - Delete test user
        securityPageUsers.deleteUser(testUserEmail);
        Assert.assertFalse(securityPageUsers.deleteUserConfirmation(testUserEmail), "Test user cleanup failed");
        logger.info("User export test completed successfully");
    }
}