package homesnap.automation.framework;

import java.io.FileInputStream;
import java.util.Properties;

public class FrameworkSetting {
	
	
	public static String baseDir = System.getProperty("user.dir");
	public static String fileSeparator = System.getProperty("file.separator");
	public static String configFilePath = baseDir + fileSeparator + "config.properties";
			
	public static String browserName = null;
	public static String url = null;

	public static int maxTimeout;
	public static int pageLoadTimeout;
	public static int maxElementTimeout;
	
	public static String testDataSpreadsheet;
	
	public static Properties prop;
	
	public static String run_time;
	
	public static void initProperties() throws InitPropertiesException {
		
		try {
			
			prop = new Properties();
			prop.load(new FileInputStream(configFilePath));
			browserName = getProperty("browser");
			url = getProperty("qa.url");
			maxTimeout = Integer.parseInt(getProperty("maxWaitInSeconds").trim()); //Converts String "60" to integer 60
			pageLoadTimeout = Integer.parseInt(getProperty("pageLoadTimeout").trim()); 
			maxElementTimeout = Integer.parseInt(getProperty("maxElementTimeout").trim()); 
			testDataSpreadsheet = baseDir + System.getProperty("file.separator") + "test-data" + System.getProperty("file.separator") + getProperty("test.datafile").trim();
			
			
		} catch (Exception FrameowrkInitationException) {
			// Enter log information
			System.out.println("check config.property variables ");
			throw new InitPropertiesException("TOO HIGH");		}
		
	}
	
	public static String getProperty(String propertyName) {
		
		String propertyValue = prop.getProperty(propertyName);
		
		return propertyValue.trim();
		
	}


}
