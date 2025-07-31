package PageObjects;

import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class pcrResultPage {

	WebDriver driver;
	WebDriverWait wait;

	private By pcrdropdown = By
			.xpath("//div[@class='MuiDrawer-root MuiDrawer-docked css-no5t1z']//div[5]//*[name()='svg']");
	private By PcrResult = By.xpath("//a[@href='/pendingPcrJobs' and normalize-space()='PCR Results']");

	public pcrResultPage(WebDriver driver) {
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

	public void clickPcrResultsLink() {
	    try {
	     
	        WebElement linkElement = wait.until(ExpectedConditions.elementToBeClickable(PcrResult));
	        linkElement.click();
	        System.out.println("✅ 'PCR Results' link clicked successfully.");
	    } catch (Exception e) {
	        System.out.println("❌ Failed to click 'PCR Results' link: " + e.getMessage());
	        throw new RuntimeException("Error clicking 'PCR Results' link.", e);
	    }
	}

	

	public void enableToggleIfDisabled() {
		try {
			// Locate the toggle input (checkbox)
			WebElement toggleInput = wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//input[@type='checkbox' and contains(@class, 'MuiSwitch-input')]")));

			// Check if the toggle is already enabled
			if (!toggleInput.isSelected()) {
				// Click the parent <span> that actually toggles the switch
				WebElement toggleButton = toggleInput.findElement(By.xpath("./parent::span"));
				toggleButton.click();
				System.out.println("✅ Toggle switch is now enabled.");
			} else {
				System.out.println("ℹ️ Toggle switch was already enabled.");
			}

		} catch (Exception e) {
			System.out.println("❌ Failed to toggle switch: " + e.getMessage());
			throw new RuntimeException("Toggle action failed.", e);
		}
	}

	public void clickJobText(String jobId) throws TimeoutException {
		By jobTextLocator = By
				.xpath("//p[contains(@class, 'MuiTypography-body2') and normalize-space(text())='" + jobId + "']");

		WebElement jobElement = wait.until(ExpectedConditions.elementToBeClickable(jobTextLocator));

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", jobElement);
		jobElement.click();

		System.out.println("✅ Clicked job text: " + jobId);
	}

	public void clickTabByName(String tabName) {
	    try {
	        By tabLocator = By.xpath("//button[@role='tab' and normalize-space(text())='" + tabName + "']");
	        WebElement tabElement = wait.until(ExpectedConditions.elementToBeClickable(tabLocator));
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", tabElement);
	        tabElement.click();
	        Thread.sleep(2000);
	        System.out.println("✅ Tab '" + tabName + "' clicked successfully.");
	    } catch (Exception e) {
	        System.out.println("❌ Failed to click tab '" + tabName + "': " + e.getMessage());
	        throw new RuntimeException("Error clicking tab: " + tabName, e);
	    }
	}

	
	
}
