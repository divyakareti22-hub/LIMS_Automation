package Utility;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ReportManager {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    // Initialize ExtentReports
    public static void initReport(String reportName) {
        // Get the user directory path (current working directory)
        String userDir = System.getProperty("user.dir");
        
        // Define the directory where reports will be saved
        String reportDir = userDir + File.separator + "TestReports";  // You can change this path as needed
        
        // Create the directory if it doesn't exist
        File reportFolder = new File(reportDir);
        if (!reportFolder.exists()) {
            reportFolder.mkdirs();  // Create the directory if it doesn't exist
        }
        
        // Define the path for the report file
        String reportPath = reportDir + File.separator + reportName + ".html";
        System.out.println("Report Path: " + reportPath);  // Debugging line to print the report path
        
        // Initialize the HTML reporter with the custom report path
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(reportPath);
        htmlReporter.config().setTheme(Theme.STANDARD);  // You can change the theme if needed
        htmlReporter.config().setDocumentTitle("Automation Test Report");  // Set the document title
        htmlReporter.config().setReportName("Automation Test Results");  // Set the report name

        // Configure visibility of charts and other settings
    //    htmlReporter.config().setChartVisibilityOnOpen(true);  // Show charts on opening the report
        
        // Initialize the ExtentReports instance and attach the HTML reporter
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
    }

    
    // Create a new test in the report
    public static void startTest(String testName) {
        ExtentTest extentTest = extent.createTest(testName);
        test.set(extentTest);
    }

    // Get the current test instance
    public static ExtentTest getTest() {
        return test.get();
    }

    // Log information
    public static void logInfo(String message) {
        getTest().info(message);
    }

    // Log pass status
    public static void logPass(String message) {
        getTest().pass(message);
    }

    // Log fail status
    public static void logFail(String message) {
        getTest().fail(message);
    }

    // Add screenshot to the report
    public static void addScreenshot(String filePath) {
        try {
            getTest().addScreenCaptureFromPath(filePath);
        } catch (Exception e) {
            logFail("Failed to attach screenshot: " + e.getMessage());
        }
    }

    // Flush the report (save the report data)
    public static void flushReport() {
        if (extent != null) {
            extent.flush();
        }
        // Wait briefly to ensure file system updates the file metadata
        try {
            Thread.sleep(3000); // 3-second delay for file system sync
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    
    public static void triggerLatestReport() throws InterruptedException {
        String reportDir = System.getProperty("user.dir") + File.separator + "TestReports";
        File reportFolder = new File(reportDir);

        refreshDirectory(reportFolder);

        File[] files = reportFolder.listFiles();
        if (files != null && files.length > 0) {
            Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

            // Get the latest report file
            File latestReport = files[0];

            // Ensure the latest file is from the current execution
            if (isFileRecentlyModified(latestReport)) {
                System.out.println("Triggering Latest Report: " + latestReport.getName());
                sendReportViaEmail(latestReport);
            } else {
                System.out.println("No recent reports found. Ensure flushReport is called.");
            }
        } else {
            System.out.println("No reports found in the directory.");
        }
    }

    private static void refreshDirectory(File directory) throws InterruptedException {
        System.out.println("Refreshing directory...");
        Thread.sleep(15000); // Increase the sleep duration to ensure files are fully flushed
        File[] files = directory.listFiles();
        if (files != null) {
            System.out.println("Directory refreshed. Files found: " + files.length);
        } else {
            System.out.println("Directory is empty or inaccessible.");
        }
    }

    private static boolean isFileRecentlyModified(File file) {
        long lastModified = file.lastModified();
        long currentTime = System.currentTimeMillis();
        // Check if the file was modified in the last 2 minutes
        return (currentTime - lastModified) < 2 * 60 * 1000;
    }


    // Example method to send the report via email (customize with your email logic)
    private static void sendReportViaEmail(File reportFile) {
        System.out.println("Sending report: " + reportFile.getName());
        // Add your email sending logic here
    }
}

//    // Trigger the latest report after a refresh
//    public static void triggerLatestReport() throws InterruptedException {
//        String reportDir = System.getProperty("user.dir") + File.separator + "TestReports";  // Same directory as used in initReport
//        File reportFolder = new File(reportDir);
//        
//        // Clear the cache/refresh the directory (if needed)
//        
//        refreshDirectory(reportFolder);
//        
//        File[] files = reportFolder.listFiles();
//
//        if (files != null && files.length > 0) {
//            // Sort files based on last modified time in descending order
//            Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
//            
//            // Get the latest report file
//            File latestReport = files[0];
//            
//            // Print the latest report name (or trigger the next action like sending email or additional processing)
//            System.out.println("Triggering Latest Report: " + latestReport.getName());
//
//            // Here, you can trigger the report action, for example:
//            // sendReportViaEmail(latestReport);
//            // Or perform any other necessary task
//        }
//    }
//
//    // Refresh the directory by re-checking its content
//    private static void refreshDirectory(File directory) throws InterruptedException {
//        // Trigger a recheck by listing files again
//        File[] files = directory.listFiles();
//        if (files != null) {
//            Thread.sleep(10000);
//
//            // You can perform additional logic to handle any files that may have been added or modified
//            System.out.println("Directory refreshed. Files found: " + files.length);
//        }
//    }
//
//    // Example method to send the report via email (you would integrate this with your existing email logic)
//    private static void sendReportViaEmail(File reportFile) {
//        // Your email logic goes here
//        System.out.println("Sending report: " + reportFile.getName());
//    }
//}
