package homesnap.page.objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import automation.common.page.object.BasePage;
import homesnap.automation.framework.FrameworkSetting;
import homesnap.automation.framework.WebDriverWrapper;

public class UserHomePage extends BasePage { 
	
	protected WebDriverWrapper driver; 
	
	public static By NEWS_TAB = By.xpath("//span[text()= 'News']");
	public static By MESSAGES_TAB = By.xpath("//span[text()='Messages']");;
	public static By PRO_AGENTS_TAB = By.xpath("//span[text()= 'agents']");;
	public static By ME_TAB = By.xpath("//span[text()= 'Me']");;
	public static By MY_ACCOUNT = By.id("0_2_0_6_9_0_spanInitials");
	public static By PUBLIC_PROFILE_LINK = By.xpath("//span[text()='My Public Profile']/ancestor::a");
	public static By SEARCH_INPUT = By.xpath("//input[contains(@id,'inputSearch') and @type='text']");
	public static By MY_AGENT_ONLY_PROFILE = By.xpath("//span[text()='My Agent-Only Profile']/ancestor::a");
	
	public UserHomePage(WebDriverWrapper driver) {
		
		super(driver);
		this.driver = driver;
		driver.waitUntilDOMCompletes();
		System.out.println("**Debug");
		verifyPage(MESSAGES_TAB,SEARCH_INPUT,PRO_AGENTS_TAB,ME_TAB);
		
	}

	public UserHomePage clickMyAccount() {
		driver.clickElement(MY_ACCOUNT);
		return this;
	}

	public UserHomePage clickProfileButton() {
		
		String idProfileLocator = driver.findElement(By.xpath("//div[contains(@id,'CurrentUser')]/a")).getAttribute("id");
		driver.findElement(By.id(idProfileLocator)).click();
		
		WebDriverWait wait = new WebDriverWait(driver.driver, FrameworkSetting.maxTimeout);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@id, 'DropDownLabel')]")));
		return this;
		
	}
	
	public UserHomePage clickMyPublicProfile() {
		
		driver.clickElement(PUBLIC_PROFILE_LINK);
		return this;
	}

	public String getUserInfo() {
		// TODO Auto-generated method stub
		return driver.findElement(By.xpath("//div[contains(@id,'divDropDownLabel')]")).getText();
	}
	
	public UserHomePage clickMyAgentOnlyProfile() {
		
		driver.clickElement(MY_AGENT_ONLY_PROFILE);
		return this;
	}
	
	//public UserHomePage
}
