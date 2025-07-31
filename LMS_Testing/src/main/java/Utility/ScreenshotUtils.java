package Utility;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.io.IOException;

public class ScreenshotUtils {

    public static void captureScreenshot(WebDriver driver, String screenshotName, ExtentTest test) {
        try {
            // Define folder and file paths
            String folderPath = System.getProperty("user.dir") + "/screenshots/";
            String absolutePath = folderPath + screenshotName + ".png";
            String relativePath = "./screenshots/" + screenshotName + ".png";

            // Create screenshots folder if it doesn't exist
            File screenshotDir = new File(folderPath);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            // Capture and save the screenshot
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileHandler.copy(source, new File(absolutePath));

            // Attach screenshot to report using relative path
            if (test != null) {
                test.addScreenCaptureFromPath(relativePath);  // ✅ This makes it viewable in HTML report
            }

            System.out.println("📸 Screenshot captured and attached: " + absolutePath);

        } catch (IOException e) {
            System.err.println("❌ Error capturing screenshot: " + e.getMessage());
        }
    }
}
