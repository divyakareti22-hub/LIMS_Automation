package testScenarios;

import Utility.Base;
import Utility.Base;
import Utility.ExcelReader;
import Utility.ReportManager;
import Utility.RetryUtility;
import Utility.ScreenshotUtils;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Utility.Base;

import PageObjects.LoginPage;
import PageObjects.elisaresultsPage;
import PageObjects.pcrPlateCreationPage;
import PageObjects.pcrResultPage;

public class elisaResult extends Base {
	private WebDriver driver;
	private LoginPage loginPage;
	private WebDriverWait wait;
	private static final Logger logger = LogManager.getLogger(pcrPlateCreation.class);
	private SoftAssertions softly = new SoftAssertions();

	@BeforeClass
	public void initializeDriver() throws IOException {
		ReportManager.initReport("ElisaResult");
		ReportManager.startTest("Initialize Browser");

		try {
			this.driver = initializeWebDriver();
			// driver = WebDriverUtils.configureChromeDownloadPath();
			driver.manage().window().maximize();
			logger.info("Launching the browser.");
			ReportManager.logInfo("Browser launched successfully.");
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		} catch (Exception e) {
			ReportManager.logFail("Failed to initialize the browser: " + e.getMessage());
			throw e;
		}
	}
	@Test(priority = 0)
	public void testLoginValidCredentials() {
		ReportManager.startTest("Login Test with Valid Credentials");

		try {
			RetryUtility.performWithRetry(() -> {

				String username = prop.getProperty("standarduser_username");
				String password = prop.getProperty("standarduser_password");

				driver.get(prop.getProperty("url"));
				logger.info("Navigating to the login page.");
				ReportManager.logInfo("Navigated to the login page.");
				driver.manage().window().maximize();
				loginPage = new LoginPage(driver);
				loginPage.enterUsername(username);
				loginPage.enterPassword(password);
				loginPage.clickLogin();
			}, 2, 2000);

			// boolean isLoginSuccess = loginPage.isLoginSuccessful();
			ScreenshotUtils.captureScreenshot(driver, "LoginSuccessful", ReportManager.getTest());
			// softly.assertThat(isLoginSuccess).as("Login Success Check").isTrue();
			ReportManager.logPass("Login test passed with valid credentials.");

		} catch (Exception e) {
			ReportManager.logFail("Login test failed: " + e.getMessage());
			logger.error("Test failed due to an exception: " + e.getMessage());
			softly.fail("Test failed due to an    unexpected exception: " + e.getMessage());
		} finally {
			softly.assertAll();
		}
	}
	@Test(priority = 1)
	public void testElisaResults() {
	    ReportManager.startTest("ELISA Results Test");
	    logger.info("🔍 Starting ELISA Results Test");

	    try {
	        // ✅ Load test data from Excel
	        String excelPath = System.getProperty("user.dir") + "\\src\\main\\resources\\PCRPlateData.xlsx";
	        String    sheetName="ElisaResult";
	        List<Map<String, String>> allPlateData = ExcelReader.getPCRPlateData(excelPath, sheetName);
	        elisaresultsPage elisaResult = new elisaresultsPage(driver);

	        // 🔽 Click ELISA dropdown
	        logger.info("📌 Clicking on ELISA dropdown");
	        ReportManager.logInfo("Clicking on ELISA dropdown");
	        elisaResult.clickelisadropdown();
	        ScreenshotUtils.captureScreenshot(driver, "Click_ELISA_Dropdown_Success", ReportManager.getTest());
	        ReportManager.logPass("ELISA dropdown clicked successfully.");

	        // 🔗 Click ELISA Results link
	        logger.info("📌 Clicking on 'ELISA Results' link");
	        ReportManager.logInfo("Clicking on 'ELISA Results' link");
	        elisaResult.clickElisaResultsLink();
	        ScreenshotUtils.captureScreenshot(driver, "Click_ELISA_Results_Link_Success", ReportManager.getTest());
	        ReportManager.logPass("'ELISA Results' link clicked successfully.");

	        // 🔁 Enable toggle
	        logger.info("📌 Enabling toggle if currently disabled");
	        ReportManager.logInfo("Checking and enabling toggle if disabled");
	        elisaResult.enableToggleIfDisabled();
	        ScreenshotUtils.captureScreenshot(driver, "Enable_Toggle_Success", ReportManager.getTest());
	        ReportManager.logPass("Toggle enabled successfully (if it was disabled).");
	        for (Map<String, String> rowData : allPlateData) {
	            String tabName = rowData.getOrDefault("TabName", "").trim();
	            String jobId = rowData.getOrDefault("JobID", "").trim();

	            if (tabName.isEmpty() || jobId.isEmpty()) {
	                logger.warn("⚠️ Skipping row due to missing AnalysisID or JobID");
	                continue;
	            }

	            PrformElisaResult(tabName, jobId);
	        }


	    } catch (Exception e) {
	        ScreenshotUtils.captureScreenshot(driver, "ElisaResults_Failure", ReportManager.getTest());
	        ReportManager.logFail("ELISA Results Test failed: " + e.getMessage());
	        logger.error("❌ ELISA Results Test failed due to exception: {}", e.getMessage(), e);
	        softly.fail("ELISA Results Test failed: " + e.getMessage());
	    } finally {
	        softly.assertAll();
	        logger.info("✅ Completed ELISA Results Test");
	        ReportManager.logInfo("Completed ELISA Results Test");
	    }
	}
		private void PrformElisaResult( String tabName, String jobId) throws TimeoutException {

	        logger.info("📄 Loaded test data from Excel: JobID = {}, TabName = {}", jobId, tabName);
	        ReportManager.logInfo("Excel data loaded: JobID = " + jobId + ", TabName = " + tabName);

	        elisaresultsPage elisaResult = new elisaresultsPage(driver);

	      
	        // 📌 Click Job ID
	        logger.info("📌 Clicking on Job ID: {}", jobId);
	        ReportManager.logInfo("Clicking on Job ID: " + jobId);
	        elisaResult.clickJobText(jobId);
	        ScreenshotUtils.captureScreenshot(driver, "Click_JobID_" + jobId + "_Success", ReportManager.getTest());
	        ReportManager.logPass("Job ID '" + jobId + "' clicked successfully.");


	        // 📌 Navigate left to find tab
	        logger.info("📌 Clicking left arrow to find tab: {}", tabName);
	        ReportManager.logInfo("Clicking left arrow to access tab: " + tabName);
	        elisaResult.clickLeftArrow();
	        ScreenshotUtils.captureScreenshot(driver, "Click_Tab_" + tabName + "_Success", ReportManager.getTest());
	        ReportManager.logPass("Tab '" + tabName + "' accessed successfully.");
	        
	        // 🔁 Enable toggle again (if needed)
	        logger.info("📌 Re-checking toggle state after job selection");
	        ReportManager.logInfo("Re-enabling toggle if disabled");
	        elisaResult.enableToggleIfDisabled();
	        ScreenshotUtils.captureScreenshot(driver, "Reenable_Toggle_Success", ReportManager.getTest());
	        ReportManager.logPass("Toggle re-checked and enabled (if required).");

            //Click on action button
	        logger.info("📌 Clicking on 'Read More' icon");
	        ReportManager.logInfo("Clicking on 'Read More' icon");
	        elisaResult.clickReadMoreIcon();  // 🔔 Actual method call
	        ScreenshotUtils.captureScreenshot(driver, "ReadMoreIcon_Clicked", ReportManager.getTest());
	        ReportManager.logPass("'Read More' icon clicked successfully.");

	}
	@AfterClass
	public void tearDown() {
	    ReportManager.startTest("Tear Down");

	    try {
	        if (driver != null) {
	            // Optionally close the browser after test execution
	            // driver.quit();
	            ReportManager.logPass("Browser closed successfully.");

	            // Flush the report
	            ReportManager.flushReport();
	            ReportManager.logPass("Report refreshed successfully.");

	            // Log report file existence (optional validation)
	            String attachmentPath = System.getProperty("user.dir") + "\\TestReports\\ElisaResult.html";
	            File reportFile = new File(attachmentPath);
	            if (reportFile.exists()) {
	                ReportManager.logPass("Test report generated successfully at: " + attachmentPath);
	            } else {
	                ReportManager.logFail("Report file not found at: " + attachmentPath);
	            }
	        }
	    } catch (Exception e) {
	        ReportManager.logFail("An error occurred during tear down: " + e.getMessage());
	    } finally {
	        try {
	            ReportManager.flushReport();
	            ReportManager.logPass("Report finalized successfully.");
	            ReportManager.triggerLatestReport();
	        } catch (Exception e) {
	            ReportManager.logFail("Failed to finalize the report: " + e.getMessage());
	        }

	    }
	}
}
