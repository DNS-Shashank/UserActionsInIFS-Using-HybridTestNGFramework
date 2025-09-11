package com.useractionsinifs.pageObjects;

import com.useractionsinifs.abstractComponents.Security_AbstractComponents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

// Extend Security_AbstractComponents since this is a security page
public class SecurityPage_Users extends Security_AbstractComponents {
    
    private static final Logger logger = LogManager.getLogger(SecurityPage_Users.class);

   //WebElements
   @FindBy(id="addIFSItem")
   WebElement adduserBtn;

   @FindBy(id="txtFirstName1")
   WebElement fName;

   @FindBy(id="txtLastName1")
   WebElement lName;

   @FindBy(id="txtEmailId1")
   WebElement email;

   @FindBy(id="chkInvitation1")
   WebElement sendInviteBtn;

   @FindBy(id="btnSaveUser")
   WebElement saveUserBtn;

   @FindBy(id="txtSelectSearchUser")
   WebElement searchBox;

   @FindBy(id="ActiveUsers")
   WebElement activateUserActionBarElement;

   @FindBy(id="menuButton")
   WebElement actionBtn;    

   @FindBy(css = "div.datagrid-cell-wrapper")
   WebElement selectAllSelectionCheckbox;

   @FindBy(css = "table[class='datagrid'] th[data-column-id='selectionCheckbox']")
   WebElement securityRolesSelectAllCheckbox;

   @FindBy(id="DeactiveUsers")
   WebElement deactivateUserActionBarElement;

   @FindBy(id="DeleteUsers")
   WebElement deleteBtnActionBarElement;

   @FindBy(xpath="//button[text()='Yes']")
   WebElement yesBtn;

   @FindBy(xpath="//button[text()='Save']")
   WebElement popupSaveBtn;

   @FindBy(id="SetResetPasswordUsers")
   WebElement resetPasswordByAdminBtn;
   
   @FindBy(id="txtsetresetPassword")
   WebElement passwordInputBox;

   @FindBy(id="txtsetresetConfirmPassword")
   WebElement confirmPasswordInputBox;

   @FindBy(id="copyUser")
   WebElement copyUserActionBarElement;

   @FindBy(id="txtFirstName")
   WebElement firstNameCopyUserPopup;

   @FindBy(id="txtLastName")
   WebElement lastNameCopyUserPopup;

   @FindBy(id="txtEmailAddress")
   WebElement emailCopyUserPopup;

   @FindBy(id="sendinvitation")
   WebElement sendInvitationCopyUserPopup;

   @FindBy(xpath="//button[text()='ADD']")
   WebElement addCopyUserPopup;

   @FindBy(css=".btn-icon.small.datagrid-drilldown")
   WebElement userDrillDownBtn;

   @FindBy(id="tab01")
   WebElement drilledUserSecurityRolesTab;

   @FindBy(id="btnAddSecurityRoles")
   WebElement drilledUserAddSecurityRolesBtn;

   @FindBy(id="txtSelectSecurityRolesSearch")
   WebElement drilledUserAddSecurityRolesSearchBox;

   @FindBy(id="btnSearchSelectAddCloseSecurityRoles")
   WebElement drilledUserAddnCloseBtn;

   @FindBy(id="btnSaveSecurityRoles")
   WebElement drilledUserSaveSecurityRolesBtn;

   @FindBy(id="userHome")
   WebElement userHomeBtn;

   @FindBy(id="contentArea")
   WebElement userPageCotentArea;

   @FindBy(css = "td[data-idx=\"5\"] div")
   WebElement searchedUserEmailAddress;


    public SecurityPage_Users(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    //User Creation
    public String[] createUser() throws InterruptedException {
        navigateToUsers();
        Thread.sleep(5000);
        waitForElementVisible(adduserBtn);
        adduserBtn.click();
        waitForElementVisible(fName);
        
        // Generate random user data
        String[] userData = generateRandomUserData();
        String firstName = userData[0];
        String lastName = userData[1];
        String userEmail = userData[2];
        
        // Fill in the form with generated data
        fName.sendKeys(firstName);
        Thread.sleep(1000);
        lName.sendKeys(lastName);
        Thread.sleep(1000);
        email.sendKeys(userEmail);
        Thread.sleep(1000);
        sendInviteBtn.click();
        Thread.sleep(1000);
        saveUserBtn.click();
        
        // User data generated and form filled successfully
        logger.info("User created successfully: " + firstName + " " + lastName + " <" + userEmail + ">");
        
        return userData;
    }

    //User Deletion
    public void deleteUser(String emailID) throws InterruptedException {
        searchBox.sendKeys(emailID);
        searchBox.sendKeys(Keys.ENTER);
        Thread.sleep(2000);
        selectAllSelectionCheckbox.click();
        actionBtn.click();
        deleteBtnActionBarElement.click();
        Thread.sleep(2000);
        yesBtn.click();
        logger.info("User deleted successfully");
    }

    //User Enable/Disable
    public void enableOrDisableUser(String emailID, String action) throws InterruptedException {
    searchBox.click();
    searchBox.sendKeys(emailID);
    searchBox.sendKeys(Keys.ENTER);
    Thread.sleep(2000);
    selectAllSelectionCheckbox.click();
    Thread.sleep(4000);
    actionBtn.click();
    if (action.equals("Enable")) {
        activateUserActionBarElement.click();
    } if (action.equals("Disable")) {
        deactivateUserActionBarElement.click();
    }
    Thread.sleep(1000);
    yesBtn.click();
    logger.info("User " + emailID + " " + action + "d successfully");    
    }

    //Reset Password by Admin
    public String resetPasswordByAdmin(String emailID, String password) throws InterruptedException {
        searchBox.click();
        searchBox.clear();
        searchBox.sendKeys(emailID);
        searchBox.sendKeys(Keys.ENTER);
        Thread.sleep(1000);
        selectAllSelectionCheckbox.click();
        Thread.sleep(500);
        actionBtn.click();
        Thread.sleep(500);
        resetPasswordByAdminBtn.click();
        Thread.sleep(500);
        passwordInputBox.sendKeys(password);
        Thread.sleep(500);
        confirmPasswordInputBox.sendKeys(password);
        Thread.sleep(1000); 
        popupSaveBtn.click();
        logger.info("Password reset successfully");
        return password;
    }

    //Copy User
    public String copyUser(String existingUserEmailID) throws InterruptedException {
        searchBox.click();
        searchBox.clear();
        searchBox.sendKeys(existingUserEmailID);
        searchBox.sendKeys(Keys.ENTER);
        Thread.sleep(1000);
        selectAllSelectionCheckbox.click();
        Thread.sleep(500);
        actionBtn.click();
        Thread.sleep(500);
        copyUserActionBarElement.click();
        Thread.sleep(500);

        // Generate random user data
        String[] userData = generateRandomUserData();
        String firstName = userData[0];
        String lastName = userData[1];
        String userEmail = userData[2];

        firstNameCopyUserPopup.sendKeys(firstName);
        Thread.sleep(500);
        lastNameCopyUserPopup.sendKeys(lastName);
        Thread.sleep(500);
        emailCopyUserPopup.sendKeys(userEmail);
        Thread.sleep(500);
        sendInvitationCopyUserPopup.click();
        Thread.sleep(500);
        addCopyUserPopup.click();
        logger.info("User copied successfully");
        return userEmail;
    }

    //User Drill Down
    public void userDrillDown(String existingUserEmailID) throws InterruptedException {
        searchBox.click();
        searchBox.clear();  
        searchBox.sendKeys(existingUserEmailID);
        searchBox.sendKeys(Keys.ENTER);
        Thread.sleep(1000);
        userDrillDownBtn.click();
        logger.info("User drilled down successfully");
        Thread.sleep(3000);
        enterFirstAvailableIframe();
    }

    //Adding Security Roles to user
    public String addSecurityRoles(String existingUserEmailID, String securityRole) throws InterruptedException {
        userDrillDown(existingUserEmailID);
        Thread.sleep(4000);
        drilledUserSecurityRolesTab.click();
        Thread.sleep(1000);
        drilledUserAddSecurityRolesBtn.click();
        Thread.sleep(1000);
        drilledUserAddSecurityRolesSearchBox.clear();
        drilledUserAddSecurityRolesSearchBox.sendKeys(securityRole);
        drilledUserAddSecurityRolesSearchBox.sendKeys(Keys.ENTER);
        Thread.sleep(2000);
        securityRolesSelectAllCheckbox.click();
        Thread.sleep(500);
        drilledUserAddnCloseBtn.click();
        Thread.sleep(2000);
        drilledUserSaveSecurityRolesBtn.click();
        Thread.sleep(2000);
        logger.info("Security roles added successfully");
        return securityRole;
    }

    //User deletion confirmation
    public boolean deleteUserConfirmation(String existingUserEmailID) throws InterruptedException {
        searchBox.click();
        searchBox.clear();
        searchBox.sendKeys(existingUserEmailID);
        searchBox.sendKeys(Keys.ENTER);
        Thread.sleep(2000);
        
        try {
            return userDrillDownBtn.isDisplayed();
        } catch (Exception e) {
            // If drill-down button not found, user is deleted
            return false;
        }
}

    public String createdUserConfirmation(String existingUserEmailID) throws InterruptedException {
        searchBox.click();
        searchBox.sendKeys(existingUserEmailID);
        searchBox.sendKeys(Keys.ENTER);
        Thread.sleep(2000);
        return searchedUserEmailAddress.getText();
    }

    public void navigateToUsersFromUserDrilldownPage() {
        logger.info("Navigating to Users section from User Drilldown page");
        userHomeBtn.click();
        logger.info("Clicked on User Home button");
        waitForElementVisible(userPageCotentArea);
        logger.info("Navigated to Users section from User Drilldown page");
    }
}