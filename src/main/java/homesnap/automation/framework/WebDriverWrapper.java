package homesnap.automation.framework;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebDriverWrapper {
	
	public static Logger log = LogManager.getLogger(BaseTest.class);
    public WebDriver driver;
    public WebDriverWait wait;

    public WebDriverWrapper(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    //this is a method for maximizing the window 
    public void maximizeWindow() {
        log.info("Maximize Window");
        driver.manage().window().maximize();
    }

    public void get(String url) {
        log.info("----Opening page to : " + url);
        driver.get(url);
    }

    public void close() {
        driver.close();
    }
    //refactor where find element is implemented as private 
    public WebElement findElement(By by) {

        log.info("Finding WebElement..." + by);
        WebElement elem = wait.until(ExpectedConditions.presenceOfElementLocated(by));
        highlightWebElement(elem);
        return elem;
    }

    public List < WebElement > findElements(By by) {

        log.info("Finding List of WebElement..." + by);
        return driver.findElements(by);
    }

    private void highlightWebElement(WebElement elem) {

        try {

            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", elem);

        } catch (Exception e) {

            // log if something goes wrong
        }

    }

    public void clearAndSendKeys(By by, String value) {

        // delete what is currently in the element
        String selectAll = Keys.chord(Keys.CONTROL, "a");
        WebElement elem = findElement(by);
        elem.sendKeys(selectAll);
        elem.sendKeys(Keys.DELETE);

        // send keys to element
        elem.sendKeys(value);
    }

    public void selectFromDropdown(By by, String selection) {

        //this is from selenium
        Select select = new Select(driver.findElement(by));
        select.selectByValue(selection);

    }

    public void clickElement(By by) {
    	
    	log.info("Clicking element " +by.toString());
        waitForElementToAppear(by);
        driver.findElement(by).click();
        log.info("Done clicking element " +by.toString());
    }

    public void hoverElement(String by) {

        Actions act = new Actions(driver);
        act.moveToElement(driver.findElement(By.xpath(by))).build().perform();

    }

    public String getTextFrom(By by) {

        return driver.findElement(by).getText();
    }

    public boolean switchToWindowByTitle(String title) {

        boolean foundWindow = false;

        String winHandleBefore = driver.getWindowHandle();

        // Switch to new window handle opened then check title
        for (String winHandle: driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
            if (driver.getTitle().contains(title)) {
            	log.info("Switched to window -->" +driver.getTitle());
                foundWindow = true;
                break;
            } else {
                foundWindow = false;
            }
        }
        return foundWindow;

    }

    public void clickElement(WebElement elem) {
        elem.click();
    }

    public void quit() {

        driver.quit();
        return;
        
    }

    public String getCurrentUrl() {

        return driver.getCurrentUrl();
    }

    public void waitForPageLoad() {

        driver.manage().timeouts().pageLoadTimeout(FrameworkSetting.pageLoadTimeout, TimeUnit.SECONDS);

    }
    
    
    public void waitUntilDOMCompletes() {
        ExpectedCondition<Boolean> pageLoadCondition = new
            ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driver) {
                    return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
                }
            };
        WebDriverWait wait = new WebDriverWait(driver, FrameworkSetting.pageLoadTimeout);
        wait.until(pageLoadCondition);
    }
    

    public void waitForElementToAppear(By by) {

        try {
            new WebDriverWait(driver, FrameworkSetting.maxElementTimeout).until(ExpectedConditions.visibilityOfElementLocated(by));
            log.info("Finished Waiting for Page to load and found element " + by.toString());

        } catch (NoSuchElementException e) {
            log.error("Waited too long for element on Page" + by.toString());

        }

    }

    public void waitForElementToAppear(By...by) {

        int size = by.length;
    

        for (int i = 0; i < size; i++) {
     
            try {
            	
                WebElement elem = new WebDriverWait(driver, FrameworkSetting.maxElementTimeout).until(ExpectedConditions.visibilityOfElementLocated(by[i]));
                highlightWebElement(elem);
                log.info("Finished Waiting for Page to load and found element " +by[i].toString());

            } catch (NoSuchElementException | TimeoutException e) {
            	//System.out.println(driver.getPageSource());
            	log.error("Could not find Element on Page " + by[i].toString());
            	log.info("Checking again..."+ by[i].toString().toString());
            		//waitForElementToAppear(by);
            	
            }

        }
    }
    
    
    public void waitForElementToDisappear(By...by) {

        int size = by.length;
    

        for (int i = 0; i < size; i++) {
     
            try {
            	
                boolean notPresent = new WebDriverWait(driver, FrameworkSetting.maxElementTimeout).until(ExpectedConditions.invisibilityOfElementLocated(by[i]));
                if (notPresent) {
                	log.info("Finished Waiting for Element to dissapear " +by[i].toString());
                }
            } catch (NoSuchElementException e) {
            	
            	log.error("Could not Wait for Element to dissapear " + by[i].toString());
        
            }

        }
    }
    
    
    public void waitForElementToBeClickable(By...by) {

        int size = by.length;

        for (int i = 0; i < size; i++) {

            try {
            	
                WebElement elem = new WebDriverWait(driver, FrameworkSetting.maxElementTimeout).until(ExpectedConditions.elementToBeClickable(by[i]));
                highlightWebElement(elem);
                log.info("Finished Waiting for Element to be clickable" +by.toString());

            } catch (NoSuchElementException e) {
                log.error("Could not wait for element to be clickable" + by.toString());

            }

        }

    }

    public void switchToIFrame(String idValue) {
        driver.switchTo().frame(idValue);
    }

    public void switchToParentFrame() {
        driver.switchTo().parentFrame();
    }

    public void pauseBrowser(long milli) {

        try {
            Thread.sleep(milli);
        } catch (Exception PauseException) {

            log.info("Exception occured during pause browser");
        }

    }
    
    public void waitForTextToBe(By by, String textToBe) {
    	
    	try {

            boolean isWaitComplete = new WebDriverWait(driver, FrameworkSetting.maxElementTimeout).until(ExpectedConditions.textToBe(by, textToBe));
            
            if (isWaitComplete) {
            	
            	return; 
            	
            } else {
            	
            	log.error("There is no text present in the element");
            }
            
        } catch (NoSuchElementException e) {
            log.error("Could not find Element on Page" + by.toString());

        }

    	
    	
    }
    
    public void selectFromDropdownByVisibleText(By by, String visibleText) {
    	
    	// in order to create select obj, we need to find the element using the by
    	WebElement elem = findElement(by);
    	
    	// then use the element that was found, and create a select object
    	Select dropdown = new Select(elem);
    	
    	// using the method select by visible text must be the text that is visible on the UI
    	dropdown.selectByVisibleText(visibleText);
    	
    }
    
    public void selectFromDropdownByIndex(By by, int indexValue) {
    	
    	Select dropdown = new Select(findElement(by));
    	dropdown.selectByIndex(indexValue);
    	
    }
    
    public void selectFromDropdownByValue(By by, String value) {
    	Select dropdown = new Select(findElement(by));
    	dropdown.selectByValue(value);
    	
    }

}
