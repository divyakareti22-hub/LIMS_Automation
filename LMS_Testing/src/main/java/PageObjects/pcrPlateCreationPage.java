package PageObjects;

import java.time.Duration;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class pcrPlateCreationPage {

	WebDriver driver;
	WebDriverWait wait;

	// Locators
	private By pcrdropdown = By
			.xpath("//div[@class='MuiDrawer-root MuiDrawer-docked css-no5t1z']//div[5]//*[name()='svg']");
	private By PcrPlateCreation = By.xpath("(//a[normalize-space()='PCR Plate Creation'])[1]");
	private By pcrPlateCreationradioButton = By.cssSelector("[class='PrivateSwitchBase-input MuiSwitch-input css-1m9pwf3']");
	private By selectjobsDropdown = By.xpath("//*[name()='svg' and @data-testid='ArrowDropDownIcon']");
	private By selectAnalysicDropdown = By.xpath(
			"//div[@class='MuiInputBase-root MuiOutlinedInput-root MuiInputBase-colorPrimary MuiInputBase-fullWidth MuiInputBase-formControl MuiInputBase-adornedEnd MuiAutocomplete-inputRoot css-segi59']//button[@title='Open']//*[name()='svg']");
	private By AnalysicId = By.xpath("//*[name()='svg' and @data-testid='ArrowDropDownIcon']");
	private By PcrGeneratePlate = By.xpath("//button[normalize-space(text())='Generate Plates']");
	private By PcrResult = By.xpath("//a[normalize-space()='PCR Results']");//
//	private By PlateIdCheckbox = By.xpath("(//input[@aria-label='Select all rows'])[1]");
//	private By PlateIdCheckbox = By.xpath("(//input[@aria-label='Select all rows'])[1]");
	private By PlateIdCheckbox = By.xpath("(//input[@aria-label='Select all rows'])[1]"); // CORRECTED
private By clickOkPopUp=By.xpath("//button[contains(@class, 'swal2-confirm') and text()='Ok']\r\n"+ "");
    private By pcrCollapseButton = By.cssSelector("button[aria-label='Toggle Right Panel'] svg");
	private By firstComboBoxInput = By.xpath(
			"(//div[contains(@class, 'MuiAutocomplete-root') and contains(@class, 'MuiAutocomplete-hasPopupIcon')]//"
					+ "input[@role='combobox'])[1]");

	// Constructor
	public pcrPlateCreationPage(WebDriver driver) {
		this.driver = driver;
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	public void clickPCRdropdown() {
		try {
			WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(pcrdropdown));
			dropdown.click();
		} catch (NoSuchElementException e) {
			System.out.println("Element not found: " + e.getMessage());
			throw new RuntimeException("PCR dropdown not found.");
		} catch (Exception e) {
			System.out.println("Unexpected error: " + e.getMessage());
			throw new RuntimeException("Error clicking PCR dropdown.");
		}
	}

	public void selectPCRplatecreation() {
		try {
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(PcrPlateCreation));
			element.click();
		} catch (NoSuchElementException e) {
			System.out.println("Element not found: " + e.getMessage());
			throw new RuntimeException("PCR Plate Creation option not found.");
		} catch (Exception e) {
			System.out.println("Unexpected error: " + e.getMessage());
			throw new RuntimeException("Error selecting PCR Plate Creation.");
		}
	}

	public void selectAnalysisByText(String analysisValue) {
		try {
			WebElement dropdownButton = wait.until(ExpectedConditions.elementToBeClickable(selectAnalysicDropdown));
			dropdownButton.click();

			WebElement analysisItem = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("//li[.//span[normalize-space()='" + analysisValue + "']]")));

			Thread.sleep(1000);
			analysisItem.click();

		} catch (NoSuchElementException e) {
			System.out.println("Analysis option not found: " + e.getMessage());
			throw new RuntimeException("Analysis option not found.");
		} catch (Exception e) {
			System.out.println("Unexpected error: " + e.getMessage());
			throw new RuntimeException("Error selecting analysis.");
		}
	}

	public void searchAndSelectJob(String jobId) {
		try {
			WebElement searchInput = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//input[@type='search' and @placeholder='Search…']")));

			searchInput.clear();
			searchInput.sendKeys(jobId);

			Thread.sleep(3000);

			WebElement jobRowCheckbox = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("//input[@aria-label='Select all rows']/parent::span")));

			jobRowCheckbox.click();

		} catch (NoSuchElementException e) {
			System.out.println("Job ID not found: " + e.getMessage());
			throw new RuntimeException("Unable to find job ID: " + jobId);
		} catch (Exception e) {
			System.out.println("Unexpected error: " + e.getMessage());
			throw new RuntimeException("Error selecting job ID: " + jobId);
		}
	}

	public void ClickPcrGeneratePlate() {
		try {
			
			WebElement generateBtn = wait.until(ExpectedConditions.elementToBeClickable(PcrGeneratePlate));
			generateBtn.click();
			Thread.sleep(2000);
		} catch (NoSuchElementException e) {
			System.out.println("Element not found: " + e.getMessage());
			throw new RuntimeException("Generate Plate button not found.");
		} catch (Exception e) {
			System.out.println("Unexpected error: " + e.getMessage());
			throw new RuntimeException("Error clicking Generate Plate button.");
		}
	}

	public void validatePlateGenerationSuccessMessage() {
		try {
			
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60)); // ⏳ adjust timeout as needed
			WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//div[@class='swal2-html-container' and @id='swal2-html-container']")));
			
			String messageText = messageElement.getText().trim();
			System.out.println("Success Message: " + messageText);
             
			if (!messageText.contains("Created Successfully")) {
				throw new RuntimeException("Unexpected success message: " + messageText);
			}

		} catch (Exception e) {
			System.out.println("Validation failed: " + e.getMessage());
			throw new RuntimeException("Success message validation failed.");
		}
	}

	public void selectPCRplatecreationRadiobutton() {
		try {
			// Locate the outer span of the toggle switch
			WebElement Jobbutton = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//button[normalize-space()='Jobs']\r\n" + "")));

			Jobbutton.click();
		} catch (NoSuchElementException e) {
			System.out.println("Toggle element not found: " + e.getMessage());
			throw new RuntimeException("Toggle switch not found.");
		} catch (Exception e) {
			System.out.println("Unexpected error: " + e.getMessage());
			throw new RuntimeException("Error interacting with toggle switch.");
		}
	}

	public void ClickOnJobDropdown() {
		try {
			WebElement dropdownIcon = wait.until(
					ExpectedConditions.elementToBeClickable(By.cssSelector("svg[data-testid='ArrowDropDownIcon']")));

			dropdownIcon.click();
			Thread.sleep(4000);
			System.out.println("✅ Dropdown icon clicked successfully.");
		} catch (Exception e) {
			System.out.println("❌ Failed to click dropdown icon: " + e.getMessage());
			throw new RuntimeException("Dropdown click failed.", e);
		}
	}

	public void selectJobFromDropdown(String jobId) {
		try {
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(firstComboBoxInput));
			// WebElement element1 =
			// wait.until(ExpectedConditions.elementToBeClickable(jb));
			element.click();
			element.click();
			element.sendKeys(jobId);
			// 3. Wait for dropdown option to appear
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			// WebElement suggestion =
			// wait.until(ExpectedConditions.visibilityOfElementLocated(
			// By.xpath("//li[contains(text(), 'J1000048')]")));
			element.sendKeys(Keys.ARROW_DOWN);
			element.sendKeys(Keys.ENTER);

			// 4. Click the suggestion explicitly
			// suggestion.click();
		} catch (NoSuchElementException e) {
			System.out.println("Element not found: " + e.getMessage());
			throw new RuntimeException("Collapse button not found.");
		} catch (Exception e) {
			System.out.println("Unexpected error: " + e.getMessage());
			throw new RuntimeException("Error clicking Collapse button.");
		}
	}

	 public void collapsepcrPlateSetup() {
	        try {
	            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(pcrCollapseButton));
	            element.click();
	        } catch (NoSuchElementException e) {
	            System.out.println("Element not found: " + e.getMessage());
	            throw new RuntimeException("Collapse button not found.");
	        } catch (Exception e) {
	            System.out.println("Unexpected error: " + e.getMessage());
	            throw new RuntimeException("Error clicking Collapse button.");
	        }
	    }
	 
	 public void ClickonOkButton() {
	        try {
	            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(clickOkPopUp));
	            element.click();
	        } catch (NoSuchElementException e) {
	            System.out.println("Element not found: " + e.getMessage());
	            throw new RuntimeException("Collapse button not found.");
	        } catch (Exception e) {
	            System.out.println("Unexpected error: " + e.getMessage());
	            throw new RuntimeException("Error clicking Collapse button.");
	        }
	    }
	    
	 public void selectPlateIdCheckbox() {
		    try {
		        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(
		                By.xpath("(//input[@aria-label='Select all rows'])[1]")));

		        // Try normal click
		        checkbox.click();
		    } catch (Exception e) {
		        try {
		            // Fallback to JS click
		            WebElement checkbox = driver.findElement(
		                By.xpath("(//input[@aria-label='Select all rows'])[1]"));
		            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
		        } catch (Exception jsEx) {
		            throw new RuntimeException("Failed to click Plate ID checkbox: " + jsEx.getMessage(), jsEx);
		        }
		    }
		}


}