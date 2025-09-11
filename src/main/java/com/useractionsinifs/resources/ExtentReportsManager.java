package com.useractionsinifs.resources;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentReportsManager {
    private static ExtentReports extent;
    private static ExtentTest test;
    private static final Logger logger = LogManager.getLogger(ExtentReportsManager.class);
    
    // Get the ExtentReports instance
    public static ExtentReports getReporter() {
        if (extent == null) {
            try {
                // Create reports directory if it doesn't exist
                File reportsDir = new File(System.getProperty("user.dir") + File.separator + "reports");
                if (!reportsDir.exists()) {
                    reportsDir.mkdirs();
                }
                
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                String path = reportsDir.getAbsolutePath() + File.separator + "TestReport_" + timestamp + ".html";
                
                ExtentSparkReporter reporter = new ExtentSparkReporter(path);
                reporter.config().setReportName("User Actions In IFS Test Results");
                reporter.config().setDocumentTitle("IFS Automation Test Report");
                
                extent = new ExtentReports();
                extent.attachReporter(reporter);
                extent.setSystemInfo("OS", System.getProperty("os.name"));
                extent.setSystemInfo("Java Version", System.getProperty("java.version"));
                extent.setSystemInfo("Tester", "Shashank");
                
                logger.info("ExtentReports initialized. Report will be saved at: " + path);
            } catch (Exception e) {
                logger.error("Failed to initialize ExtentReports", e);
            }
        }
        return extent;
    }
    
   // Create a new test in the report
    public static ExtentTest createTest(String testName) {
        logger.info("Creating test: " + testName);
        test = getReporter().createTest(testName);
        return test;
    }
    
    // Method to flush the report
    public static void flushReporter() {
        if (extent != null) {
            logger.info("Flushing ExtentReports");
            extent.flush();
        } else {
            logger.warn("Cannot flush ExtentReports: reporter is null");
        }
    }
}
