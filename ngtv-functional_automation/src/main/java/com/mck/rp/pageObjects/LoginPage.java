package com.mck.rp.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.mck.rp.base.BasePage;
import com.mck.rp.utilities.ElementUtil;

import io.qameta.allure.Step;

public class LoginPage extends BasePage {

    private WebDriver driver;
    ElementUtil elementUtil;

    // By locators
    By username = By.xpath("//input[@name='email']");
    By password = By.xpath("//input[@name='password']");
    By btnLogin = By.xpath("//button[@type='submit']");
    By pageTile = By.xpath("//h1[contains(text(), 'Regimen')]");
    public By btnlogout = By.xpath("//span[contains(text(),'log out')]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        elementUtil = new ElementUtil(driver);
    }

    // page actions:
    @Step("getting login page title...")
    public String getLoginPageTitle()
    {
        return elementUtil.doGetPageTitleWithIsTitle(10, "Regimen Profiler");
    }

    @Step("getting sing up link presence...")
    public boolean isSignUpLinkExist() {
        System.out.println("Page Tile: " + pageTile);
        return elementUtil.doIsDisplayed(pageTile);
    }

    @Step("getting sing up link presence...")
    public boolean isLogoutLinkExist() {
        System.out.println("Page Tile: " + pageTile);
        return elementUtil.doIsDisplayed(btnlogout);
    }
    public String logoutText() {
        System.out.println("Logout Text: " + elementUtil.getElement(btnlogout).getAttribute("textContent"));
        return elementUtil.getElement(btnlogout).getAttribute("textContent");
    }


    @Step("login to app with username: {0} and password: {1}")
    public void doLogin(String username, String password) throws InterruptedException {
        elementUtil.waitForElementPresent(this.username, 10);
        elementUtil.doSendKeys(this.username, username);
        elementUtil.doSendKeys(this.password, password);
        elementUtil.doClick(this.btnLogin);
        elementUtil.syncWait(3);
    }

}

