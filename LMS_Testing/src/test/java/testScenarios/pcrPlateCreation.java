package testScenarios;

import Utility.Base;
import Utility.Email_trigger;
import Utility.ExcelReader;
import Utility.ReportManager;
import Utility.RetryUtility;
import Utility.ScreenshotUtils;
import Utility.ZipUtils;

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
import PageObjects.elisaPlatecreationPage;
import PageObjects.elisaresultsPage;
import PageObjects.pcrPlateCreationPage;
import PageObjects.pcrResultPage;

public class pcrPlateCreation extends Base {
	private WebDriver driver;
	private LoginPage loginPage;
	private WebDriverWait wait;
	private static final Logger logger = LogManager.getLogger(pcrPlateCreation.class);
	private SoftAssertions softly = new SoftAssertions();

	@BeforeClass
	public void initializeDriver() throws IOException {
		ReportManager.initReport("NSLTestReports");
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

			// Wait for login to process
			Thread.sleep(1500);

			// ✅ Capture screenshot after login
			ScreenshotUtils.captureScreenshot(driver, "LoginSuccessful", ReportManager.getTest());

			ReportManager.logPass("Login test passed with valid credentials.");

		} catch (Exception e) {
			// ❌ Capture screenshot on failure
			ScreenshotUtils.captureScreenshot(driver, "LoginFailure", ReportManager.getTest());

			ReportManager.logFail("Login test failed: " + e.getMessage());
			logger.error("Test failed due to an exception: " + e.getMessage());
			softly.fail("Test failed due to an unexpected exception: " + e.getMessage());
		} finally {
			softly.assertAll();
		}
	}

	@Test(priority = 1)
	public void testtestPCRplatecreation() {
		ReportManager.startTest("PCR Plate Creation Test");
		logger.info("🔍 Starting PCR Plate Creation Test");

		try {
			String excelPath = System.getProperty("user.dir") + "\\src\\main\\resources\\PCRPlateData.xlsx";
			String sheetName = "PCR";

			// 🔁 Fetch multiple rows
			List<Map<String, String>> allPlateData = ExcelReader.getPCRPlateData(excelPath, sheetName);

			pcrPlateCreationPage pcrplatecreation = new pcrPlateCreationPage(driver);

			// 🔽 Click PCR dropdown
			logger.info("📌 Clicking on PCR dropdown");
			ReportManager.logInfo("Clicking on PCR dropdown");
			pcrplatecreation.clickPCRdropdown();
			ScreenshotUtils.captureScreenshot(driver, "Click_PCR_Dropdown_Success", ReportManager.getTest());
			ReportManager.logPass("PCR dropdown clicked successfully.");

			// ▶️ Select PCR Plate Creation page
			logger.info("📌 Selecting PCR Plate Creation from menu");
			ReportManager.logInfo("Selecting PCR Plate Creation page");
			pcrplatecreation.selectPCRplatecreation();
			ScreenshotUtils.captureScreenshot(driver, "Select_PCRPlateCreation_Page", ReportManager.getTest());
			ReportManager.logPass("PCR Plate Creation page selected successfully.");

			// 🔘 Select radio button
			logger.info("📌 Selecting PCR Plate Creation radio button");
			ReportManager.logInfo("Selecting PCR Plate Creation radio button");
			pcrplatecreation.selectPCRplatecreationRadiobutton();
			ScreenshotUtils.captureScreenshot(driver, "RadioButton_Selected", ReportManager.getTest());
			ReportManager.logPass("Radio button selected successfully.");

			// 🔽 Collapse section
			logger.info("📌 Collapsing PCR Plate Setup section");
			ReportManager.logInfo("Collapsing PCR Plate Setup section");
			pcrplatecreation.collapsepcrPlateSetup();
			ScreenshotUtils.captureScreenshot(driver, "Collapse_PCRPlateSetup", ReportManager.getTest());
			ReportManager.logPass("PCR Plate Setup section collapsed.");

			for (Map<String, String> rowData : allPlateData) {
				String analysisId = rowData.getOrDefault("AnalysisID", "").trim();
				String jobId = rowData.getOrDefault("JobID", "").trim();

				if (analysisId.isEmpty() || jobId.isEmpty()) {
					logger.warn("⚠️ Skipping row due to missing AnalysisID or JobID");
					continue;
				}

				PrformPCRPlateCreation(analysisId, jobId);
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

	private void PrformPCRPlateCreation(String analysisId, String jobId) throws InterruptedException {

		logger.info("📄 Excel data loaded. AnalysisID: {}, JobID: {}", analysisId, jobId);
		ReportManager.logInfo("Excel data fetched successfully: AnalysisID = " + analysisId + ", JobID = " + jobId);

		pcrPlateCreationPage pcrplatecreation = new pcrPlateCreationPage(driver);

		// 🔍 Click Job dropdown
		logger.info("📌 Clicking Job dropdown");
		ReportManager.logInfo("Clicking Job dropdown");
		pcrplatecreation.ClickOnJobDropdown();
		Thread.sleep(1500);
		ScreenshotUtils.captureScreenshot(driver, "Click_JobDropdown", ReportManager.getTest());
		ReportManager.logPass("Job dropdown clicked.");

		// 🔢 Select Job ID
		logger.info("📌 Selecting Job ID: {}", jobId);
		ReportManager.logInfo("Selecting Job ID: " + jobId);
		pcrplatecreation.selectJobFromDropdown(jobId);
		ScreenshotUtils.captureScreenshot(driver, "Select_Job_" + jobId, ReportManager.getTest());
		ReportManager.logPass("Job ID selected successfully: " + jobId);

		// 🧬 Select Analysis ID
		logger.info("📌 Selecting Analysis ID: {}", analysisId);
		ReportManager.logInfo("Selecting Analysis ID: " + analysisId);
		pcrplatecreation.selectAnalysisByText(analysisId);
		ScreenshotUtils.captureScreenshot(driver, "Select_Analysis_" + analysisId, ReportManager.getTest());
		ReportManager.logPass("Analysis ID selected successfully: " + analysisId);

		// ☑️ Select Plate ID checkbox
		logger.info("📌 Selecting Plate ID checkbox");
		ReportManager.logInfo("Selecting Plate ID checkbox");
		pcrplatecreation.selectPlateIdCheckbox();
		ScreenshotUtils.captureScreenshot(driver, "Select_PlateId_Checkbox", ReportManager.getTest());
		ReportManager.logPass("Plate ID checkbox selected.");

		// 🧪 Click Generate Plate
		logger.info("📌 Clicking Generate Plate button");
		ReportManager.logInfo("Clicking Generate Plate button");
		pcrplatecreation.ClickPcrGeneratePlate();
		Thread.sleep(4000);
		ScreenshotUtils.captureScreenshot(driver, "Click_Generate_Plate", ReportManager.getTest());
		ReportManager.logPass("Generate Plate button clicked.");

		// ✅ Validate success message
		logger.info("📌 Validating Plate Generation Success Message");
		ReportManager.logInfo("Validating Plate Generation Success Message");
		pcrplatecreation.validatePlateGenerationSuccessMessage();
		ScreenshotUtils.captureScreenshot(driver, "Validate_Plate_Generation", ReportManager.getTest());
		ReportManager.logPass("Plate generation validated successfully.");

		pcrplatecreation.ClickonOkButton();

	}

	@Test(priority = 2)
	public void testPCRResults() {
		ReportManager.startTest("PCR Results Test");
		logger.info("🔍 Starting PCR Results Test");

		try {

			String excelPath = System.getProperty("user.dir") + "\\src\\main\\resources\\PCRPlateData.xlsx";
			String sheetName = "PcrResult";
			List<Map<String, String>> allPlateData = ExcelReader.getPCRPlateData(excelPath, sheetName);
			pcrResultPage pcrResult = new pcrResultPage(driver);

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

	private void PerformPcrResult(String tabName, String jobId) throws TimeoutException {
		pcrResultPage pcrResult = new pcrResultPage(driver);

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

	@Test(priority = 3)
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
			Thread.sleep(1500);
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

	private void PerformElisaPlateCreation(String TargetGeneId, String jobId) throws InterruptedException {

		logger.info("📄 Excel data loaded. JobID: {}, Target Gene: {}", jobId, TargetGeneId);
		ReportManager.logInfo("Excel data fetched successfully: JobID = " + jobId + ", Target Gene = " + TargetGeneId);

		elisaPlatecreationPage elisaplatecreation = new elisaPlatecreationPage(driver);

		// 🧪 Job dropdown
		logger.info("📌 Clicking 'Select Job' button");
		ReportManager.logInfo("Clicking on Select Job button");
		elisaplatecreation.selectJobbutton();
		ScreenshotUtils.captureScreenshot(driver, "Click_Select_Job_Button_Success", ReportManager.getTest());

		logger.info("📌 Opening Job dropdown");
		ReportManager.logInfo("Opening Job dropdown");
		elisaplatecreation.ClickOnJobDropdown();
		ScreenshotUtils.captureScreenshot(driver, "Open_Job_Dropdown_Success", ReportManager.getTest());

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
		Thread.sleep(5000);
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

	@Test(priority = 4)
	public void testElisaResults() {
		ReportManager.startTest("ELISA Results Test");
		logger.info("🔍 Starting ELISA Results Test");

		try {
			// ✅ Load test data from Excel
			String excelPath = System.getProperty("user.dir") + "\\src\\main\\resources\\PCRPlateData.xlsx";
			String sheetName = "ElisaResult";
			List<Map<String, String>> allPlateData = ExcelReader.getPCRPlateData(excelPath, sheetName);
			elisaresultsPage elisaResult = new elisaresultsPage(driver);

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

	private void PrformElisaResult(String tabName, String jobId) throws TimeoutException {

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

		// Click on action button
		logger.info("📌 Clicking on 'Read More' icon");
		ReportManager.logInfo("Clicking on 'Read More' icon");
		elisaResult.clickReadMoreIcon(); // 🔔 Actual method call
		ScreenshotUtils.captureScreenshot(driver, "ReadMoreIcon_Clicked", ReportManager.getTest());
		ReportManager.logPass("'Read More' icon clicked successfully.");

	}

	@AfterClass
	public void tearDown() {
		ReportManager.startTest("Tear Down");

		try {
			if (driver != null) {
				driver.quit();
				ReportManager.logPass("Browser closed successfully.");
			}

			ReportManager.flushReport();
			//"yathishkumar.KJ@dhruvts.com", "shruthi.sd@dhruvts.com",

			List<String> recipients = List.of("divya.k@dhruvts.com");
			String subject = "NSL TestReports Execution Report";
			String reportPath = System.getProperty("user.dir") + "\\TestReports\\NSLTestReports.html";
			String zipPath = System.getProperty("user.dir") + "\\TestReports\\NSLTestReports.zip";

			File reportFile = new File(reportPath);
			if (reportFile.exists()) {
				ZipUtils.zipReportWithScreenshots(reportPath, zipPath);

				String htmlBody = "<html><body>" + "<h2>Test Execution Summary</h2>"
						+ "<p>The NSL test execution is completed. Please find the attached zipped report.</p>"
						+ "</body></html>";

				Email_trigger.sendEmail(recipients, subject, htmlBody, null, zipPath);
				ReportManager.logPass("Report zipped and email sent.");
			} else {
				ReportManager.logFail("Report not found at: " + reportPath);
			}

		} catch (Exception e) {
			ReportManager.logFail("Tear down failed: " + e.getMessage());
		} finally {
			try {
				ReportManager.flushReport();
				ReportManager.triggerLatestReport();
			} catch (Exception e) {
				ReportManager.logFail("Report flush/trigger failed: " + e.getMessage());
			}
		}
	}

}
