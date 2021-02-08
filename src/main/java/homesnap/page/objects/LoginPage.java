package homesnap.page.objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import automation.common.page.object.BasePage;
import homesnap.automation.framework.WebDriverWrapper;

public class LoginPage extends BasePage {
	  
	protected WebDriverWrapper driver;
	
	public static By USERNAME_INPUT = By.xpath("//input[contains(@id, 'inputEmail')]");
	public static By PASSWORD_INPUT = By.xpath("//input[@name='password']");
	public static By SIGNIN_BTN = By.xpath("//button[contains(@id, 'buttonSignIn')]");
	public static By SIGN_IN_LINK = By.xpath("//a[contains(@id, 'SignIn')]");
	
	
	public LoginPage(WebDriverWrapper driver) {
		
		super(driver);
		this.driver = driver; 
		//driver.waitForPageLoad();
		System.out.println("debugA");
		verifyPage(SIGN_IN_LINK);
		System.out.println("debugB");

		
	}
	
	public LoginPage enterUsername (String usernameValue) {
		
		driver.clearAndSendKeys(USERNAME_INPUT, usernameValue);
		return this;
	}
	
	public LoginPage enterPassword (String passwordValue) {
		
		driver.clearAndSendKeys(PASSWORD_INPUT, passwordValue);
		return this;
	}
	
	public void clickSignInBtn() {
		
		driver.clickElement(SIGNIN_BTN);
		
	}
	
	
	public LoginPage clickSignInLink() {
		
		driver.clickElement(SIGN_IN_LINK);
		return this;
	}
	
	public UserHomePage login(String username, String password) {
		
		clickSignInLink();
		enterUsername(username);
		enterPassword(password);
		clickSignInBtn();
		driver.waitForElementToAppear(SIGN_IN_LINK);
		return new UserHomePage(driver);
	}
	
	
}
