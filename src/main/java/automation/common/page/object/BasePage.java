package automation.common.page.object;

import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import homesnap.automation.framework.FrameworkSetting;
import homesnap.automation.framework.WebDriverWrapper;

public class BasePage {
	
	public static Logger log = LogManager.getLogger(BasePage.class);
	protected WebDriverWrapper driver;
	
	
	public BasePage(WebDriverWrapper driver) {
		this.driver = driver;
	}
	      
	public void verifyPage(By...by) {
		log.info("Verifying page");
		driver.waitForElementToAppear(by);
	}
	
	
}
