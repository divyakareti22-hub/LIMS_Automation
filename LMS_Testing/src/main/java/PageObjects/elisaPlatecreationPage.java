package PageObjects;

import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class elisaPlatecreationPage {

	WebDriver driver;
	WebDriverWait wait;

	public elisaPlatecreationPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Add this line
	}

	private By elisadropdown = By.xpath("//p[normalize-space()='ELISA']");
	private By elisaplatecreation = By.xpath("//a[normalize-space()='Elisa Plate Creation']");
	private By searchDNAPlates = By.id(":r67:");
	private By jobradioButton = By.xpath("(//input[@id='Job'])");
	private By TargetGneDropdown = By.cssSelector(
			"div[class='MuiAutocomplete-root MuiAutocomplete-hasPopupIcon css-edk5b6'] button[title='Open'] svg");
	private By selectjobs = By.cssSelector(
			"div[class='MuiAutocomplete-root MuiAutocomplete-hasPopupIcon css-13skho3'] button[title='Open'] svg");
	private By selectPlates = By.cssSelector("input[aria-label=\"Select all rows\"]");
	private By ElisageneratePlates = By.xpath("//button[normalize-space()='Generate Plates']");
	private By elisaCollapseButton = By.cssSelector("svg[data-testid='ChevronRightIcon']");
	private By firstComboBoxInput = By.xpath(
			"(//div[contains(@class, 'MuiAutocomplete-root') and contains(@class, 'MuiAutocomplete-hasPopupIcon')]//"
					+ "input[@role='combobox'])[1]");
	private By SecondComboBoxInput = By.xpath(
			"(//div[contains(@class, 'MuiAutocomplete-root') and contains(@class, 'MuiAutocomplete-hasPopupIcon')]//"
					+ "input[@role='combobox'])[2]");
	private By selectAllCheckbox = By.xpath("//input[@type='checkbox' and @aria-label='Select all rows']");
	private By clickOkPopUp = By.xpath("//button[contains(@class, 'swal2-confirm') and text()='Ok']\r\n" + "");
	private By clickCancelPopUp = By.xpath(
			"(//button[@class='MuiButtonBase-root MuiButton-root MuiButton-outlined MuiButton-outlinedSecondary MuiButton-sizeMedium MuiButton-outlinedSizeMedium MuiButton-colorSecondary MuiButton-root MuiButton-outlined MuiButton-outlinedSecondary MuiButton-sizeMedium MuiButton-outlinedSizeMedium MuiButton-colorSecondary css-ju4h6t'])[1]");

	// private By PcrGeneratePlate =
	// By.xpath("//button[normalize-space(text())='Generate Plates']");

	public void clickelisadropdown() {
		try {
			WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(elisadropdown));
			dropdown.click();
		} catch (NoSuchElementException e) {
			System.out.println("Element not found: " + e.getMessage());
			throw new RuntimeException("PCR dropdown not found.");
		} catch (Exception e) {
			System.out.println("Unexpected error: " + e.getMessage());
			throw new RuntimeException("Error clicking PCR dropdown.");
		}
	}

	public void selectelisaplatecreation() {
		try {
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(elisaplatecreation));
			element.click();
		} catch (NoSuchElementException e) {
			System.out.println("Element not found: " + e.getMessage());
			throw new RuntimeException("PCR Plate Creation option not found.");
		} catch (Exception e) {
			System.out.println("Unexpected error: " + e.getMessage());
			throw new RuntimeException("Error selecting PCR Plate Creation.");
		}
	}

	public void Search(String DNAPlates) {
		try {
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(searchDNAPlates));
			element.sendKeys(DNAPlates);
		} catch (NoSuchElementException e) {
			System.out.println("Element not found: " + e.getMessage());
			throw new RuntimeException("PCR Plate Creation option not found.");
		} catch (Exception e) {
			System.out.println("Unexpected error: " + e.getMessage());
			throw new RuntimeException("Error selecting PCR Plate Creation.");
		}
	}

	public void collapseElisaPlateSetup() {
		try {
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(elisaCollapseButton));
			element.click();
		} catch (NoSuchElementException e) {
			System.out.println("Element not found: " + e.getMessage());
			throw new RuntimeException("Collapse button not found.");
		} catch (Exception e) {
			System.out.println("Unexpected error: " + e.getMessage());
			throw new RuntimeException("Error clicking Collapse button.");
		}
	}

	public void selectJobbutton() {
		try {
			// Locate the outer span of the toggle switch
			WebElement Jobbutton = wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='Job']/ancestor::label")));

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
			element.clear();
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

	public void ClickOnTargetGen(String TragetGen) {
		try {
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(SecondComboBoxInput));
		
			element.click();

			Thread.sleep(2000);
			element.clear();
			Thread.sleep(2000);
			element.click();
			element.sendKeys(TragetGen);
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

	public void ClickElisaGeneratePlate() {
		try {
			WebElement generateBtn = wait.until(ExpectedConditions.elementToBeClickable(ElisageneratePlates));
			generateBtn.click();
		} catch (NoSuchElementException e) {
			System.out.println("Element not found: " + e.getMessage());
			throw new RuntimeException("Generate Plate button not found.");
		} catch (Exception e) {
			System.out.println("Unexpected error: " + e.getMessage());
			throw new RuntimeException("Error clicking Generate Plate button.");
		}
	}

	public void validateElisaPlateGenerationSuccessMessage() {
		try {
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

	public void ClickonCancelButton() {
		try {
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(clickCancelPopUp));
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
			WebElement checkbox = wait.until(
					ExpectedConditions.elementToBeClickable(By.xpath("(//input[@aria-label='Select all rows'])[1]")));

			// Try normal click
			checkbox.click();
		} catch (Exception e) {
			try {
				// Fallback to JS click
				WebElement checkbox = driver.findElement(By.xpath("(//input[@aria-label='Select all rows'])[1]"));
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
			} catch (Exception jsEx) {
				throw new RuntimeException("Failed to click Plate ID checkbox: " + jsEx.getMessage(), jsEx);
			}

		}

	}
}
