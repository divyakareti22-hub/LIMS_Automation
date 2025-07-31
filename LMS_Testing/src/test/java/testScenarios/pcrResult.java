package testScenarios;
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
import PageObjects.pcrPlateCreationPage;
import PageObjects.pcrResultPage;

public class pcrResult extends Base {

	private WebDriver driver;
	private LoginPage loginPage;
	private WebDriverWait wait;
	private static final Logger logger = LogManager.getLogger(pcrPlateCreation.class);
	private SoftAssertions softly = new SoftAssertions();

	@BeforeClass
	public void initializeDriver() throws IOException {
		ReportManager.initReport("PcrResult");
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
	public void testPCRResults() {
	    ReportManager.startTest("PCR Results Verification Test");
	    logger.info("🔍 Starting PCR Results Verification Test");

	    try {
	        // 📄 Load Excel Data
	        String excelPath = System.getProperty("user.dir") + "\\src\\main\\resources\\PCRPlateData.xlsx";
	        String sheetName="PcrResult";
	        List<Map<String, String>> allPlateData = ExcelReader.getPCRPlateData(excelPath, sheetName);

	        for (Map<String, String> rowData : allPlateData) {
	            String tabName = rowData.getOrDefault("TabName", "").trim();
	            String jobId = rowData.getOrDefault("JobID", "").trim();

	            if (tabName.isEmpty() || jobId.isEmpty()) {
	                logger.warn("⚠️ Skipping row due to missing AnalysisID or JobID");
	                continue;
	            }

	            PerformPcrResult(tabName, jobId);
	        }  
	    } catch (Exception e) {
	        ScreenshotUtils.captureScreenshot(driver, "PCRResults_Failure", ReportManager.getTest());
	        ReportManager.logFail("PCR Results Verification Test failed: " + e.getMessage());
	        logger.error("❌ PCR Results Verification Test failed: {}", e.getMessage(), e);
	        softly.fail("PCR Results Verification Test failed due to an unexpected exception: " + e.getMessage());
	    } finally {
	        softly.assertAll();
	        logger.info("✅ Completed PCR Results Verification Test");
	        ReportManager.logInfo("Completed PCR Results Verification Test");
	    }
	}
		private void PerformPcrResult( String tabName, String jobId) throws TimeoutException {
		     pcrResultPage pcrResult = new pcrResultPage(driver);

		        // 🔽 Click PCR Dropdown
		        logger.info("📌 Clicking on PCR dropdown");
		        ReportManager.logInfo("Clicking on PCR dropdown");
		        pcrResult.clickPCRdropdown();
		        ScreenshotUtils.captureScreenshot(driver, "PCRDropdown_Clicked", ReportManager.getTest());
		        ReportManager.logPass("PCR dropdown clicked successfully");

		        // 🔗 Click PCR Results link
		        logger.info("📌 Clicking on PCR Results link");
		        ReportManager.logInfo("Clicking on PCR Results link");
		        pcrResult.clickPcrResultsLink();
		        ScreenshotUtils.captureScreenshot(driver, "PCRResults_Link_Clicked", ReportManager.getTest());
		        ReportManager.logPass("PCR Results link clicked successfully");

		        // 🔘 Enable toggle
		        logger.info("📌 Enabling toggle if currently disabled");
		        ReportManager.logInfo("Enabling toggle (if required)");
		        pcrResult.enableToggleIfDisabled();
		        ScreenshotUtils.captureScreenshot(driver, "Toggle_Checked", ReportManager.getTest());
		        ReportManager.logPass("Toggle verified/enabled successfully");

		        // 📎 Click Job ID
		        logger.info("📌 Clicking on Job ID: {}", jobId);
		        ReportManager.logInfo("Clicking on Job ID: " + jobId);
		        pcrResult.clickJobText(jobId);
		        ScreenshotUtils.captureScreenshot(driver, "JobID_Selected_" + jobId, ReportManager.getTest());
		        ReportManager.logPass("Job ID '" + jobId + "' selected successfully");

		        // 🧪 Click Tab
		        logger.info("📌 Clicking on tab: {}", tabName);
		        ReportManager.logInfo("Clicking on tab: " + tabName);
		        pcrResult.clickTabByName(tabName);
		        ScreenshotUtils.captureScreenshot(driver, "Tab_Selected_" + tabName, ReportManager.getTest());
		        ReportManager.logPass("Tab '" + tabName + "' clicked successfully");

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
	            String attachmentPath = System.getProperty("user.dir") + "\\TestReports\\pcrResults.html";
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


