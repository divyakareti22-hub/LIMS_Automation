package PageObjects;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
	
	WebDriver driver;
	WebDriverWait wait;
	WebDriver actions;
	
	// Create an instance of Actions class

	private By usernameField = By.name("email");
	private By passwordField = By.id("password");
	private By loginButton = By.cssSelector("button[type='submit']");

	public LoginPage(WebDriver driver) {
		this.driver = driver;
		wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // 10 seconds timeout

	}
	
	// Method to enter the username with explicit wait
	public void enterUsername(String username) {
	    try {
	        // Wait for the username field to be visible on the page
	        WebElement usernameElem = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
	        
	        // Clear any existing text in the username field
	        usernameElem.clear();
	        
	        // Enter the provided username into the username field
	        usernameElem.sendKeys(username);
	        Actions actions = new Actions(driver);
	        actions.moveToElement(usernameElem).click().perform(); 
	    } catch (Exception e) {
	        // Log and throw a runtime exception if an error occurs during username entry
	        System.out.println("Error: Unable to enter the username. " + e.getMessage());
	        throw new RuntimeException("Username entry failed: " + e.getMessage());
	    }
	}

	public void enterPassword(String password) {
	    try {
	        // Wait for the password field to be visible on the page
	        WebElement passwordElem = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
	        
	        // Clear any existing text in the password field
	        passwordElem.clear();
	        
	        // Enter the provided password into the password field
	        passwordElem.sendKeys(password);
	    } catch (Exception e) {
	        // Log and throw a runtime exception if an error occurs during password entry
	        System.out.println("Error: Unable to enter the password. " + e.getMessage());
	        throw new RuntimeException("Password entry failed: " + e.getMessage());
	    }
	}

	 
	// Method to click the login button
	 public void clickLogin() {
	     try {
	         // Wait until the login button is clickable
	         WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(loginButton));
	         
	         // Click the login button to submit the login form
	         loginBtn.click();
	     } catch (Exception e) {
	         // Log and throw an exception with the error message
	         System.out.println("Error: Unable to click the login button. " + e.getMessage());
	         throw new RuntimeException("Login button click failed: " + e.getMessage());
	     }
	 }


	

	 // Method to perform login using provided username and password
	    public void login(String username, String password) {
	        // Enter the username into the username field
	        enterUsername(username);
	        
	        // Enter the password into the password field
	        enterPassword(password);
	        
	        
	        // Click the login button to submit the login form
	        clickLogin();
	    }

}
