package com.useractionsinifs.testComponents.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    
    private static final Logger logger = LogManager.getLogger(RetryAnalyzer.class);
    
    private static int count = 0;
    private static String lastTestMethod = "";
    private static final int MAX_RETRY = 1;
    
    @Override
    public boolean retry(ITestResult result) {
        String currentTestMethod = result.getMethod().getMethodName() + result.getParameters().toString();
        
        logger.info("RetryAnalyzer - Test: " + currentTestMethod);
        logger.info("RetryAnalyzer - Status: " + result.getStatus());
        logger.info("RetryAnalyzer - Count: " + count);
        
        synchronized(RetryAnalyzer.class) {
            if (!currentTestMethod.equals(lastTestMethod)) {
                count = 0;  // Reset for new test method
                lastTestMethod = currentTestMethod;
                logger.info("RetryAnalyzer - Reset count for new test");
            }
        }
        
        if (result.getStatus() == ITestResult.FAILURE && count < MAX_RETRY) {
            count++;
            logger.info("RetryAnalyzer - Retrying failed test, count: " + count);
            return true;
        }
        
        logger.info("RetryAnalyzer - Not retrying");
        return false;
    }
}
