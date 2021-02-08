package homesnap.page.objects;

import org.openqa.selenium.By;

import automation.common.page.object.BasePage;
import homesnap.automation.framework.WebDriverWrapper;

public class ConciergePage extends BasePage{
	
	protected WebDriverWrapper driver;
	
	public static By SPECIALS_BTN = By.id("0_0_0_buttonSpecials");
	public static By SEE_MY_RANK_BTN = By.id("0_0_0_buttonRanks");
	public static By SIX_MONTHS_BTN = By.xpath("//p/following-sibling::div[contains(@class, 'rounded-pill') and contains(string(), \"FREE\")]");
	
	public ConciergePage(WebDriverWrapper driver) {
		
		super(driver);
		this.driver = driver;
		driver.waitUntilDOMCompletes();
		verifyPage();
		
	}
	

}
