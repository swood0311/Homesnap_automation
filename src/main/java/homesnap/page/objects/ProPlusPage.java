package homesnap.page.objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import automation.common.page.object.BasePage;
import homesnap.automation.framework.WebDriverWrapper;

public class ProPlusPage extends BasePage {
	 
	public ProPlusPage(WebDriverWrapper driver) {
		super(driver);
		// TODO Auto-generated constructor stub
		this.driver = driver;
	}
	protected WebDriverWrapper driver;
	
	public static By PRO_PLUS_HOME = By.id("0_0_0_5_1_0_divMenuItemContainer");
	public static By PRO_PLUS_HOME_ICON = By.id("0_0_0_5_6_aIcon");
	public static By PRO_PLUS_INPUT_NAME = By.id("0_0_2_0_1_1_1_1_0_0_inputName");
	public static By PRO_PLUS_PHONE_NUMBER = By.id("0_0_2_0_1_1_1_1_0_0_inputPhone");

}

	