package com.mck.rp.tetscases.Login;

import com.mck.rp.base.BaseTest;
import com.mck.rp.pageObjects.CommonPage;
import com.mck.rp.utilities.ElementUtil;
import org.testng.Assert;
import org.testng.annotations.Test;


//@Epic("Epic - 101 : design login page features...")
//@Feature("US - 201: desgin login page title, sign up link and login form modules...")
//@Listeners(TestAllureListener.class)
public class LoginTest extends BaseTest{
    ElementUtil elementUtil;
    CommonPage rp;
    // TestNG -- unit testing framework
    // PreConditions ---> Test Cases(steps) (Act vs Exp) -- Assertions ---> Tear
    // Down
    // @BeforeTest ---> @Test --Assertions --> @AfterTest
    // launchBrowser, url --- > title test --> close the browser

    //@Description("verify sign up link on login page...")
   // @Severity(SeverityLevel.CRITICAL)
    //@Test(priority=1)
    public void verifySignUpLinkTest() {
        Assert.assertEquals(loginPage.isSignUpLinkExist(), true);
    }

    //@Description("verify login page title login page...")
    //@Severity(SeverityLevel.NORMAL)
    @Test(priority=2)
    public void verifyLoginPageTitleTest() {
        System.out.println("running login page title test...");
        String title = loginPage.getLoginPageTitle();
        System.out.println("Login page title is: " + title);
        Assert.assertEquals(title, "Regimen Profiler");
    }

    //@Description("verify user is able to login page...")
   // @Severity(SeverityLevel.BLOCKER)

    @Test(priority=4)
    public void verifyLogoutLinkTest() {
        System.out.println("After Logging in: " + loginPage.logoutText());
        //Assert.assertEquals(loginPage.isLogoutLinkExist(), true);
    }

}

