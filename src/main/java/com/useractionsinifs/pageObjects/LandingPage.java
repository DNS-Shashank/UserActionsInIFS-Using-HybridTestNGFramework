package com.useractionsinifs.pageObjects;

import com.useractionsinifs.abstractComponents.AbstractComponents;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class LandingPage extends AbstractComponents {

    String userIconSelector = "document.querySelector('#osp-nav-user-profile').shadowRoot.querySelector('button')";
    String securityIcon ="document.querySelector('portal-osmenu-app ids-layout-grid-cell:nth-child(2)')";

    public LandingPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }
    
    //Verify Landing Page
    public  boolean verifyLandingPage() {
        return waitForShadowElement(userIconSelector);
    }
    
    //Navigating To Security
    public SecurityPage_Users navigateToComponent(String componentName){

        if(componentName == "Security OS") {
            appMenuNavigation(componentName);
            SecurityPage_Users securityPageUsers = new SecurityPage_Users(driver);
            return securityPageUsers;
        }
        return null;
        
    }  
}
