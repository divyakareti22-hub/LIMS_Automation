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
public class elisaresultsPage {
	WebDriver driver;
	WebDriverWait wait;

	public elisaresultsPage(WebDriver driver) {
		this.driver = driver;
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	 private By elisadropdown =By.xpath("//p[normalize-space()='ELISA']");

		private By elisaResult =By.xpath("//a[contains(text(), 'Elisa Results')]");
		private By elisaResultLeftarrow =By.xpath("//div[@class='MuiGrid-root MuiGrid-item MuiGrid-grid-xs-1 css-1doag2i']//*[name()='svg']");
		private  By readMoreIcon = By.xpath("//button[@title='Info']//*[name()='svg']");

		//(//*[name()='svg'][@class='MuiSvgIcon-root MuiSvgIcon-fontSizeMedium css-1szvnok'])[1]
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
	    
		public void clickElisaResultsLink() {
		    try {
		     
		        WebElement linkElement = wait.until(ExpectedConditions.elementToBeClickable(elisaResult));
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
		public void clickLeftArrow() {
		    try {
		        WebElement backArrow = wait.until(ExpectedConditions.elementToBeClickable(elisaResultLeftarrow));
		        backArrow.click();
		        System.out.println("✅ Left arrow clicked successfully.");
		    } catch (Exception e) {
		        System.out.println("❌ Failed to click the left arrow: " + e.getMessage());
		        // Optionally take a screenshot here for debugging
		    }
		}
		public void clickReadMoreIcon() {
		    try {
		   
		        WebElement iconElement = wait.until(ExpectedConditions.elementToBeClickable(readMoreIcon));
		        iconElement.click();  // Direct click without JS
		        Thread.sleep(2000);
		        System.out.println("✅ 'Read More' icon clicked successfully.");
		    } catch (Exception e) {
		        System.out.println("❌ Failed to click 'Read More' icon: " + e.getMessage());
		        throw new RuntimeException("Error clicking 'Read More' icon", e);
		    }
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
