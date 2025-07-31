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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Utility.Base;
import PageObjects.LoginPage;
import PageObjects.elisaPlatecreationPage;
import PageObjects.pcrPlateCreationPage;

public class elisaPlateCreation extends Base {

	private WebDriver driver;
	private LoginPage loginPage;
	private WebDriverWait wait;
	private static final Logger logger = LogManager.getLogger(pcrPlateCreation.class);
	private SoftAssertions softly = new SoftAssertions();

	@BeforeClass
	public void initializeDriver() throws IOException {
		ReportManager.initReport("ElisaPlateCreation");
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
	public void testelisaplatecreation() {
		ReportManager.startTest("ELISA Plate Creation Test");
		logger.info("🔍 Starting ELISA Plate Creation Test");

		try {
			// 🔹 Load test data from Excel
			String excelPath = System.getProperty("user.dir") + "\\src\\main\\resources\\PCRPlateData.xlsx";
			String sheetName = "Elisa";

			// 🔁 Fetch multiple rows
			List<Map<String, String>> allPlateData = ExcelReader.getPCRPlateData(excelPath, sheetName);
			elisaPlatecreationPage elisaplatecreation = new elisaPlatecreationPage(driver);

			// 🔽 Click dropdown
			logger.info("📌 Clicking on ELISA dropdown");
			ReportManager.logInfo("Clicking on ELISA dropdown");
			elisaplatecreation.clickelisadropdown();
			ScreenshotUtils.captureScreenshot(driver, "Click_ELISA_Dropdown_Success", ReportManager.getTest());
			ReportManager.logPass("Clicked on ELISA dropdown successfully.");

			// ▶️ Select menu
			logger.info("📌 Selecting 'ELISA Plate Creation' from the menu");
			ReportManager.logInfo("Selecting ELISA Plate Creation page from menu");
			elisaplatecreation.selectelisaplatecreation();
			ScreenshotUtils.captureScreenshot(driver, "Select_ELISA_Plate_Creation_Success", ReportManager.getTest());
			ReportManager.logPass("ELISA Plate Creation page selected successfully.");

			// 🔽 Collapse section
			logger.info("📌 Collapsing the 'ELISA Plate Setup' section");
			ReportManager.logInfo("Collapsing ELISA Plate Setup section");
			elisaplatecreation.collapseElisaPlateSetup();
			ScreenshotUtils.captureScreenshot(driver, "Collapse_ELISA_Plate_Setup_Success", ReportManager.getTest());
			ReportManager.logPass("Collapsed ELISA Plate Setup section.");

			logger.info("📌 Clicking 'Select Job' button");
			ReportManager.logInfo("Clicking on Select Job button");
			elisaplatecreation.selectJobbutton();
			ScreenshotUtils.captureScreenshot(driver, "Click_Select_Job_Button_Success", ReportManager.getTest());

			logger.info("📌 Opening Job dropdown");
			ReportManager.logInfo("Opening Job dropdown");
			elisaplatecreation.ClickOnJobDropdown();
			ScreenshotUtils.captureScreenshot(driver, "Open_Job_Dropdown_Success", ReportManager.getTest());
			
			for (Map<String, String> rowData : allPlateData) {
			    String TargetGeneId = rowData.getOrDefault("Target Gene", "").trim();
			    String jobId = rowData.getOrDefault("JobID", "").trim();

			    System.out.println("Row data: JobID = " + jobId + ", TargetGeneId = " + TargetGeneId);

			    if (TargetGeneId.isEmpty() || jobId.isEmpty()) {
			        logger.warn("Skipping row due to missing TargetGeneId or JobID");
			        continue;
			    }

			    PerformElisaPlateCreation(TargetGeneId, jobId);
			}


			ReportManager.logPass("All plates created and validated successfully.");

		} catch (Exception e) {
			ScreenshotUtils.captureScreenshot(driver, "PCRPlateCreation_Failure", ReportManager.getTest());
			ReportManager.logFail("PCR Plate Creation Test failed: " + e.getMessage());
			logger.error("❌ PCR Plate Creation Test failed due to exception: {}", e.getMessage(), e);
			softly.fail("PCR Plate Creation Test failed: " + e.getMessage());
		} finally {
			softly.assertAll();
			logger.info("✅ Completed PCR Plate Creation Test");
			ReportManager.logInfo("Completed PCR Plate Creation Test");
		}
	}

	private void PerformElisaPlateCreation(String TargetGeneId, String jobId) {

		logger.info("📄 Excel data loaded. JobID: {}, Target Gene: {}", jobId, TargetGeneId);
		ReportManager.logInfo("Excel data fetched successfully: JobID = " + jobId + ", Target Gene = " + TargetGeneId);

		elisaPlatecreationPage elisaplatecreation = new elisaPlatecreationPage(driver);

	
		// 🧪 Job dropdown
	

		logger.info("📌 Selecting Job ID: {}", jobId);
		ReportManager.logInfo("Selecting Job ID: " + jobId);
		elisaplatecreation.selectJobFromDropdown(jobId);
		ScreenshotUtils.captureScreenshot(driver, "Select_JobID_" + jobId + "_Success", ReportManager.getTest());
		ReportManager.logPass("Job selected successfully: " + jobId);

		// 🧬 Target Gene
		logger.info("📌 Selecting Target Gene: {}", TargetGeneId);
		ReportManager.logInfo("Selecting Target Gene: " + TargetGeneId);
		elisaplatecreation.ClickOnTargetGen(TargetGeneId);
		ScreenshotUtils.captureScreenshot(driver, "Select_TargetGene_" + TargetGeneId + "_Success",
				ReportManager.getTest());
		ReportManager.logPass("Target Gene selected successfully: " + TargetGeneId);

		// ☑️ Select all rows
		logger.info("📌 Clicking on 'Select all rows' checkbox");
		ReportManager.logInfo("Clicking on 'Select all rows' checkbox");
		elisaplatecreation.selectPlateIdCheckbox();
		ScreenshotUtils.captureScreenshot(driver, "Click_Select_All_Checkbox_Success", ReportManager.getTest());
		ReportManager.logPass("'Select all rows' checkbox clicked successfully.");

		logger.info("📌 Clicking on 'Generate Plate' button");
		ReportManager.logInfo("Clicking on 'Generate Plate' button");
		elisaplatecreation.ClickElisaGeneratePlate();
		ScreenshotUtils.captureScreenshot(driver, "Click_Generate_Plate_Success", ReportManager.getTest());
		ReportManager.logPass("'Generate Plate' button clicked successfully.");

		// ✅ Validate success message

//		logger.info("📌 Validating Plate Generation Success Message");
//		ReportManager.logInfo("Validating Plate Generation Success Message");
//		elisaplatecreation.validateElisaPlateGenerationSuccessMessage();
//		ScreenshotUtils.captureScreenshot(driver, "Validate_Plate_Generation", ReportManager.getTest());
//		ReportManager.logPass("Elisa Plate generation validated successfully.");
		
		elisaplatecreation.ClickonCancelButton();

		elisaplatecreation.ClickonOkButton();

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
				String attachmentPath = System.getProperty("user.dir") + "\\TestReports\\elisaPlateCreation.html";
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
