package com.useractionsinifs.testComponents.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RetryAnalyzer implements IRetryAnalyzer {
    
    private static final Logger logger = LogManager.getLogger(RetryAnalyzer.class);
    private static final int MAX_RETRY = 1;
    private static final Map<String, Integer> retryCount = new ConcurrentHashMap<>();
    
    @Override
    public boolean retry(ITestResult result) {
        String testKey = result.getMethod().getMethodName();
        int currentCount = retryCount.getOrDefault(testKey, 0);
        
        logger.info("RetryAnalyzer - Test: " + testKey + ", Status: " + result.getStatus() + ", Count: " + currentCount);
        
        if (result.getStatus() == ITestResult.FAILURE && currentCount < MAX_RETRY) {
            retryCount.put(testKey, currentCount + 1);
            logger.info("Retrying test: " + testKey + ", attempt: " + (currentCount + 1));
            return true;
        }
        
        retryCount.remove(testKey); // Clean up
        logger.info("Not retrying test: " + testKey);
        return false;
    }
}
