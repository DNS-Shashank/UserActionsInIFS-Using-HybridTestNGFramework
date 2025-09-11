package com.useractionsinifs.pageObjects;

import com.useractionsinifs.abstractComponents.AbstractComponents;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends AbstractComponents {

    LoginPage loginPage;

    String userIconSelector = "document.querySelector('#osp-nav-user-profile').shadowRoot.querySelector('button')";


    @FindBy(id = "div_authentication_service_guid")
     WebElement cloudIdentity;

    @FindBy(id = "username")
     WebElement userName;

    @FindBy(id = "pass")
     WebElement Password;

    @FindBy(css = "button[type='submit']")
     WebElement signInButton;

     @FindBy(css = "span[class='error text-strong']")
    public WebElement invalidUserLoginErrorText;

    @FindBy(xpath = "//span[text()='Username cannot be empty']")
    public WebElement emprtyUserNameErrorText;

    @FindBy(xpath = "//span[text()='Password cannot be empty']")
    public WebElement emprtyPasswordErrorText;

    public LoginPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

   //Click on Cloud Identity
    public void clickCloudIdentity() {
        waitForElementClickable(cloudIdentity);
        cloudIdentity.click();
    }

    //Enter User Credentials
    public void enterUserCredentials(String username, String password) {
        waitForElementVisible(userName);
        userName.sendKeys(username);
        Password.sendKeys(password);
    }

    //Click on Sign In Button
    public LandingPage clickSignInButton(String username, String password) {

        clickCloudIdentity();
        enterUserCredentials(username, password);
        waitForElementClickable(signInButton);
        signInButton.click();

        // Wait for the shadow DOM element to exist in the page
        //waitForShadowElement(userIconSelector);      
        LandingPage landingPage = new LandingPage(driver);
        return landingPage;
    }


    
    //Navigate to application URL
    public void goTo(String url) {
        driver.get(url);
    }
}
