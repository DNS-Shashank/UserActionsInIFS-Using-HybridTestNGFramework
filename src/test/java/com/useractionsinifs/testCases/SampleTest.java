package com.useractionsinifs.testCases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.useractionsinifs.abstractComponents.AbstractComponents;
import com.useractionsinifs.pageObjects.LandingPage;
import com.useractionsinifs.pageObjects.SecurityPage_Users;
import com.useractionsinifs.testComponents.BaseTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class SampleTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(SampleTest.class);
    
    LandingPage landingPage;
    SecurityPage_Users securityPageUsers;

    String securityRole = "System Administration";
    
    @DataProvider(name = "loginData")
    public Object[][] getLoginData() throws IOException {
        // Load test data from JSON
        List<HashMap<String, String>> testDataList = loadTestData();
        
        // Create a 2D array of objects with each HashMap as a separate test data entry
        Object[][] data = new Object[testDataList.size()][1];
        for (int i = 0; i < testDataList.size(); i++) {
            data[i][0] = testDataList.get(i);
        }
        
        return data;
    }
    
    @Test(dataProvider = "loginData")
    public void loginTest(HashMap<String, String> userData) throws InterruptedException {
        System.out.println("Running test with: " + userData.get("description"));
        System.out.println("Username: " + userData.get("username"));
        System.out.println("Password: " + userData.get("password"));
        
    // Login using data from JSON
       landingPage = loginPage.clickSignInButton(userData.get("username"), userData.get("password"));
       Thread.sleep(10000);

       //Access Security
        securityPageUsers = landingPage.navigateToComponent("Security OS");
        //Assert.assertTrue(securityPageUsers.isAtSecurityPage());
        Thread.sleep(10000);
        
        // Create user and get user data back
        String[] createdUserData = securityPageUsers.createUser();
        
        // Log user creation information
        logger.info("User created successfully with following details:");
        logger.info("First Name: " + createdUserData[0]);
        logger.info("Last Name: " + createdUserData[1]);
        logger.info("Email: " + createdUserData[2]);

        Thread.sleep(5000);
        
        /*
        //Copy User
        Thread.sleep(5000);
        securityPageUsers.copyUser(createdUserData[2]);
        Thread.sleep(5000);

        
        //Reset Password by Admin
        Thread.sleep(5000);
        String resetPassword = securityPageUsers.resetPasswordByAdmin(createdUserData[2], securityPageUsers.userPasswordGenerator());
        logger.info("Password reset successfully with password: " + resetPassword + " for user: " + createdUserData[2]);
    
        
        //Enable or Disable User
        Thread.sleep(5000);
        securityPageUsers.enableOrDisableUser(createdUserData[2], "Disable");
        Thread.sleep(5000);  
        securityPageUsers.enableOrDisableUser(createdUserData[2], "Enable"); 
        Thread.sleep(5000);

        //Export User
        securityPageUsers.exportUsersByCSVorXML(createdUserData[2], "CSV");
        Thread.sleep(5000);
        securityPageUsers.exportUsersByCSVorXML(createdUserData[2], "XML");
        Thread.sleep(5000);
        

        //Adding Security Roles to user
        securityPageUsers.addSecurityRoles(createdUserData[2], securityRole);
        Thread.sleep(5000);
        securityPageUsers.navigateToUsersFromUserDrilldownPage();
        Thread.sleep(5000);

        
        
        //Import Users
        securityPageUsers.importUsers();
        Thread.sleep(5000);
2
        */
        
        //Delete the user
        securityPageUsers.deleteUser(createdUserData[2]);

        Thread.sleep(5000);
        AbstractComponents.exitFrameContext();
        AbstractComponents.userLogout();


    }


}


