package homesnap.automation.framework;

import java.io.File;
import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public class BaseTest {
	
	
	
    public static Logger log = LogManager.getLogger(BaseTest.class);
    public WebDriverWrapper driver = null;
    protected WebDriverWait wait = null;
    protected String testInfoID;
    protected String testApp;
    protected String author;
    private TestData testData;
    private boolean testDataPersistanceNeeded = false;


    @BeforeSuite(alwaysRun = true)
    public void suiteSetup() throws InitPropertiesException {

        log.info("Initiation of Framework properties");
        //Any framework Properties
        FrameworkSetting.initProperties();
        log.info("Done: Initaiton of Framework properties");
    }

    private String getChromeDriver() {

        String value = "Not Set: PLEASE CHECK OS AND CHROME DRIVER";
        log.info("Getting chromedriver based on operating system");

        if (System.getProperty("os.name").startsWith("Win")) {
            value = "chromedriver.exe";
        } else {
            value = "chromedriver";
        }

        return value;
    }


    @BeforeMethod(alwaysRun = true)
    public void testSetup(Method method) throws FrameworkException {

        try {
            //read test data before start of test
       //     readTestData(method);
        } catch (Exception e) {
            log.error("Error occurred during test setup -- Test Data");
            log.error("Please specifiy test data in Excel Sheet");
            throw new FrameworkException("NO TEST DATA");
        }


        if (FrameworkSetting.browserName.equalsIgnoreCase("chrome")) {

            //System.setProperty("webdriver.chrome.driver", FrameworkSetting.baseDir + FrameworkSetting.fileSeparator + "drivers" + File.separator + getChromeDriver());
           
        	System.setProperty("webdriver.chrome.driver", "//Users//stanleywoodley//Documents//chromedriver");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-features=VizDisplayCompositor");
            
            WebDriver driverFromSelenium = new ChromeDriver(options);
            wait = new WebDriverWait(driverFromSelenium, FrameworkSetting.maxTimeout);
            this.driver = new WebDriverWrapper(driverFromSelenium, wait);
            wait = new WebDriverWait(driverFromSelenium, FrameworkSetting.maxTimeout);

        } else if (FrameworkSetting.browserName.equalsIgnoreCase("ie")) {

            System.setProperty("webdriver.ie.driver", FrameworkSetting.baseDir + FrameworkSetting.fileSeparator + "drivers\\IEDriverServer.exe");
            WebDriver driverFromSelenium = new InternetExplorerDriver();
            wait = new WebDriverWait(driverFromSelenium, FrameworkSetting.maxTimeout);
            this.driver = new WebDriverWrapper(driverFromSelenium, wait);

        } else {

            log.error("Error occurred during test setup -- Initation of web driver");
            log.error("Please specifiy correct web driver");
            throw new FrameworkException("No Web Driver");
        }


        driver.get(FrameworkSetting.url);
        driver.maximizeWindow();

    }

    @AfterMethod(alwaysRun = true)
    public void closeTest(Method method) throws FrameworkException {

        /*If test has output to write to Test Data file then persist the data. */
        try {
            persistTestParameterValues();
        } catch (FrameworkException fe) {
            throw fe;
        }

        driver.quit();
    }


    /**
     * 
     * @param method
     * @throws Exception
     * Checks if TestInfo annotation is present with required arguments such as Test Info Test ID,
     * Application ID, Test Author
     */
    public void readTestData(Method method) throws Exception {
        try {
            TestInfo testInfo = method.getAnnotation(TestInfo.class);
            testInfoID = testInfo.ID();
            testApp = testInfo.APP().toString();
            author = testInfo.AUTHOR().toString();
            Assert.assertTrue(testInfoID.length() > 0 && testApp.length() > 0 && author.length() > 0);

        } catch (Exception e) {
            String message = "Missing Test Info Annotation, please annotate test method with Test ID";

            throw new FrameworkException(message);
        }

        try {

            testData = new TestData(FrameworkSetting.testDataSpreadsheet);
            testData.readTestCaseData(testInfoID, testApp);

        } catch (Exception e) {
            String message = "Error Reading from test Data File " + FrameworkSetting.testDataSpreadsheet + " " + e.getMessage();
            throw new FrameworkException(message);
        }

    }

    public String getTestParameterValue(String parm) {

        try {
            String val = testData.getTestParameter(parm);
            return val;
        } catch (FrameworkException e) {

            return null;
        }

    }

    public void setTestParameterValue(String parm, String value) {

        try {
            testData.setParameter(parm, value);
        } catch (FrameworkException e) {

            throw new SkipException(e.getMessage());
        }
        testDataPersistanceNeeded = true;

    }

    public void persistTestParameterValues() throws FrameworkException {
        if (testDataPersistanceNeeded) {
            testData.persistTestCaseOutputData();
        }

    }
}
