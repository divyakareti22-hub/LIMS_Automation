package Utility;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Base {

    public FileInputStream fis;
    public Properties prop;
    public WebDriver driver;

    public WebDriver initializeWebDriver() throws IOException {
        fis = new FileInputStream(System.getProperty("user.dir") + "\\src\\main\\resources\\ConfigFile");
        prop = new Properties();
        prop.load(fis);

        String browserName = prop.getProperty("browser");
        if (browserName.equalsIgnoreCase("Chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.setAcceptInsecureCerts(true); // Example: Handle insecure certificates
            this.driver = new ChromeDriver(options); // Use ChromeOptions if needed
        } else if (browserName.equalsIgnoreCase("Internet Explorer")) {
            // Code to initialize the IE driver
        } else if (browserName.equalsIgnoreCase("Firefox")) {
            // Code to initialize the Firefox driver
        } else {
            throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }

        return driver;
    }

    // Method to get server configuration
    public String getServerConfig() {
        return prop.getProperty("selectServer"); // Fetch the server value from the configuration file
    }
}
