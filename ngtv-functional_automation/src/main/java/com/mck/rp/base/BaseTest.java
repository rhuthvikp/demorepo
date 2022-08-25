#-----------------------------------------------------------------------------------------
'''
    DESCRIPTION: Base test - Initial setup and extent reports set up
    AUTHOR: Sree Karumuri
'''
#-----------------------------------------------------------------------------------------
package com.mck.rp.base;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import com.aventstack.extentreports.Status;
import com.mck.rp.listeners.ExtentReportListener;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import com.mck.rp.pageObjects.LoginPage;

public class BaseTest {

    public WebDriver driver;
    public BasePage basePage;
    public LoginPage loginPage;
    public Properties prop;

    @BeforeSuite
    public void setUpBeforeSuite() {
        try {
            FileUtils.cleanDirectory(new File("./src/test/resources/Downloads"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Parameters("browser")
    @BeforeClass
    public void setUp() throws InterruptedException, IOException {

        basePage = new BasePage();
        prop = basePage.init_prop();
        System.out.println("Property" + prop);
        prop.setProperty("browser",prop.getProperty("browser"));
        driver = basePage.init_driver(prop);
        loginPage = new LoginPage(driver);
       // loginPage.doLogin(prop.getProperty("defaultUsername"), prop.getProperty("defaultPassword"));
    }

    public void extentReportLog(String mtype, String msg){
        if( mtype.equalsIgnoreCase("info")){
            ExtentReportListener.test.get().log(Status.INFO,msg);
            System.out.println(msg);
        }
        if( mtype.equalsIgnoreCase("pass")){
            ExtentReportListener.test.get().log(Status.PASS,msg);}
        if( mtype.equalsIgnoreCase("fail")){
            ExtentReportListener.test.get().log(Status.FAIL,msg);}
        if( mtype.equalsIgnoreCase("skip")){
            ExtentReportListener.test.get().log(Status.SKIP,msg);}

    }

    @Step("{0}")
    public void allureReportLog(String message) {
        System.out.println(message);
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

}
