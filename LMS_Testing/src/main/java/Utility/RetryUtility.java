package Utility;

import org.testng.ITestResult;

public class RetryUtility {
	
	
    public static boolean performWithRetry(Runnable action, int maxRetries, long delayBetweenRetries) {
	        int attempt = 0;
	        while (attempt < maxRetries) {
	            try {
	                action.run();  // Try executing the action
	                return true;  // If successful, return true
	            } catch (Exception e) {
	                attempt++;
	                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());
	                if (attempt >= maxRetries) {
	                    System.out.println("Max retry attempts reached.");
	                    return false;  // If max attempts reached, return false
	                }
	                try {
	                    Thread.sleep(delayBetweenRetries);  // Wait before retrying
	                } catch (InterruptedException ie) {
	                    Thread.currentThread().interrupt();
	                }
	            }
	        }
	        return false;
	    }
	}

