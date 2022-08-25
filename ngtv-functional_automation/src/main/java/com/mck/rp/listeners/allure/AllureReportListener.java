#-----------------------------------------------------------------------------------------
'''
    DESCRIPTION: Allurereport set up
    AUTHOR: Sree Karumuri
'''
#-----------------------------------------------------------------------------------------
        
package com.mck.rp.listeners.allure;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.mck.rp.base.BasePage;

public class AllureReportListener extends BasePage implements ITestListener {

    //commenting all the allure report logs except the failed tests where it takes the screenshot along with output
    //Uncomment the logs when needed

    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    // Take screenshot and add attachments for Allure report
    @Attachment(value = "Failed page screenshot", type = "image/png")
    public byte[] takeScreenshot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    // Text message attachments for Allure
    @Attachment(value = "{0}", type = "text/plain")
    public static String saveLogs(String message) {
        return message;
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        System.out.println("onStart method " + iTestContext.getName());
        saveLogs(iTestContext.getName() + " - Started");
        iTestContext.setAttribute("WebDriver", BasePage.getDriver());
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        System.out.println("onFinish method " + iTestContext.getName());
        saveLogs(iTestContext.getName() + " - Completed");
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        System.out.println("onTestStart method " + getTestMethodName(iTestResult) + " start");
        //saveLogs(getTestMethodName(iTestResult) + " - Started");
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        //saveLogs(getTestMethodName(iTestResult) + " - Passed");
        System.out.println("onTestSuccess method " + getTestMethodName(iTestResult) + " Passed");
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        System.out.println("onTestFailure method " + getTestMethodName(iTestResult) + " failed");
        //saveLogs(getTestMethodName(iTestResult) + " - Failed");
        //Object testClass = iTestResult.getInstance();
        takeScreenshot(getDriver());
        // writing/logging a message for allure report.
        saveLogs(getTestMethodName(iTestResult) + " - test failed and screenshot is attached!");
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        System.out.println("onTestSkipped method " + getTestMethodName(iTestResult) + " skipped");
        //saveLogs(getTestMethodName(iTestResult) + " - Skipped");
    }

}
