package com.useractionsinifs.testComponents.listeners;

import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.useractionsinifs.resources.ExtentReportsManager;
import com.useractionsinifs.testComponents.BaseTest;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TestListener implements ITestListener, IAnnotationTransformer {
    
    private ExtentReports extent = ExtentReportsManager.getReporter();
    private ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    
    @SuppressWarnings({"rawtypes"})
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        // Create a new entry in the report when test starts
        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        extentTest.set(test);
        extentTest.get().info("Test started: " + result.getMethod().getMethodName());
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        // Log test success in report
        extentTest.get().log(Status.PASS, MarkupHelper.createLabel("Test Passed: " + result.getMethod().getMethodName(), ExtentColor.GREEN));
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        // Log test failure in report
        extentTest.get().log(Status.FAIL, MarkupHelper.createLabel("Test Failed: " + result.getMethod().getMethodName(), ExtentColor.RED));
        extentTest.get().fail(result.getThrowable());
        
        // Get the BaseTest instance from the test class to access the driver
        Object testInstance = result.getInstance();
        if (testInstance instanceof BaseTest) {
            BaseTest baseTest = (BaseTest) testInstance;
            
            // Take screenshot and attach to report
            String screenshotPath = baseTest.takeScreenshot(result.getMethod().getMethodName());
            if (screenshotPath != null && !screenshotPath.isEmpty()) {
                // Add screenshot to report
                extentTest.get().log(Status.INFO, "Screenshot captured at: " + screenshotPath);
                // Use MediaEntityBuilder to attach screenshot
                try {
                    extentTest.get().addScreenCaptureFromPath(screenshotPath);
                } catch (Exception e) {
                    extentTest.get().log(Status.FAIL, "Failed to attach screenshot to report: " + e.getMessage());
                }
            } else {
                extentTest.get().log(Status.WARNING, "Failed to capture screenshot");
            }
        } else {
            extentTest.get().log(Status.WARNING, "Could not capture screenshot - test instance is not a BaseTest");
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        extentTest.get().log(Status.SKIP, MarkupHelper.createLabel("Test Skipped: " + result.getMethod().getMethodName(), ExtentColor.YELLOW));
        extentTest.get().skip(result.getThrowable());
    }
    
    @Override
    public void onFinish(ITestContext context) {
        // Flush the report when all tests are complete
        ExtentReportsManager.flushReporter();
        System.out.println("ExtentReports flushed and saved to reports directory");
    }
    
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Not implemented
    }
    
    @Override
    public void onStart(ITestContext context) {
        // Not implemented
    }
}
