package com.useractionsinifs.abstractComponents;

/**
 * Custom exception for element not found scenarios
 * Provides clear error messages for debugging
 */
public class ElementNotFoundException extends RuntimeException {
    
    public ElementNotFoundException(String message) {
        super(message);
    }
    
    public ElementNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}