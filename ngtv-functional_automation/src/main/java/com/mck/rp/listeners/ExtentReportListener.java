package com.mck.rp.listeners;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.mck.rp.base.BasePage;

public class ExtentReportListener extends BasePage implements ITestListener {

    private static final String OUTPUT_FOLDER = "./extentReports/";
    private static String FILE_NAME = "Extent_" + new Date().toString().replace(":", "_").replace(" ", "_") + ".html";

    private static ExtentReports extent = init();
    public static ThreadLocal<ExtentTest> test = new ThreadLocal<ExtentTest>();

    private static ExtentReports init() {

        Path path = Paths.get(OUTPUT_FOLDER);
        // if directory doesnt exists
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter(OUTPUT_FOLDER + FILE_NAME);
        htmlReporter.config().setDocumentTitle("Automation Test Results");
        htmlReporter.config().setReportName("Automation Test Results");
        htmlReporter.config().setTheme(Theme.STANDARD);

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setReportUsesManualConfiguration(true);
        extent.setSystemInfo("Organization", "McKesson");
        return extent;
    }

    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }

    public synchronized void onStart(ITestContext context) {
        System.out.println((" Test Suite started!"));
    }

    public synchronized void onTestStart(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String qualifiedName = result.getMethod().getQualifiedName();
        int last = qualifiedName.lastIndexOf(".");
        int mid = qualifiedName.substring(0, last).lastIndexOf(".");
        String className = qualifiedName.substring(mid + 1, last);

        //This outputs only the method's test name. If we need the entire method name to show up uncomment this below line
       // ExtentTest test = extent.createTest(result.getTestClass().getName()+"     @TestCase : "+result.getMethod().getMethodName());

        //System.out.println(methodName + " started!");

        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName(),
                result.getMethod().getDescription());

        extentTest.assignCategory(result.getTestContext().getSuite().getName());
        extentTest.assignCategory(className);

        test.set(extentTest);
        test.get().log(Status.INFO, methodName+" - started!");
        test.get().getModel().setStartTime(getTime(result.getStartMillis()));
    }

    public synchronized void onTestSuccess(ITestResult result) {
        System.out.println((result.getMethod().getMethodName() + " passed!"));
        test.get().pass(result.getMethod().getMethodName() + " - completed and passed");
        test.get().getModel().setEndTime(getTime(result.getEndMillis()));
    }

    public synchronized void onTestFailure(ITestResult result) {
        System.out.println((result.getMethod().getMethodName() + " failed!"));
        //test.get().log(Status.FAIL,result.getMethod().getMethodName() + " failed!");

        try {
            test.get().fail(result.getThrowable(),
                    MediaEntityBuilder.createScreenCaptureFromPath(getScreenshot()).build());
        } catch (Exception e) {
            System.err
                    .println("Exception thrown while updating test fail status " + Arrays.toString(e.getStackTrace()));
        }
        test.get().getModel().setEndTime(getTime(result.getEndMillis()));
        test.get().fail(result.getMethod().getMethodName()+" - Test Failed");
    }

    public synchronized void onTestSkipped(ITestResult result) {
        System.out.println((result.getMethod().getMethodName() + " skipped!"));
        //test.get().log(Status.SKIP, result.getMethod().getMethodName() + "skipped!");

        try {
            test.get().skip(result.getThrowable(),
                    MediaEntityBuilder.createScreenCaptureFromPath(getScreenshot()).build());
        } catch (Exception e) {
            System.err
                    .println("Exception thrown while updating test skip status " + Arrays.toString(e.getStackTrace()));
        }
        test.get().getModel().setEndTime(getTime(result.getEndMillis()));
        test.get().skip(result.getMethod().getMethodName() + " - Test Skipped");
    }

    public synchronized void onFinish(ITestContext context) {
        System.out.println(("Test Suite completed execution!"));
        extent.flush();
        test.remove();
    }

}
