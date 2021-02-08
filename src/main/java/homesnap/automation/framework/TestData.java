package homesnap.automation.framework;

import homesnap.automation.framework.TestDataExcelUtil.TestCaseInfoBean;

public class TestData {
	
	private TestCaseInfoBean testCaseInfoBean = null;
	private TestDataExcelUtil testDataExcelUtil;
	public String testID; 
	String testDataFile;
	
	/**
	 * Constructor.
	 * @param testDataFile  excel test data spreadsheet
	 * @param Log           extent logger
	 */
	public TestData(String testDataFile)
	{
		this.testDataFile = testDataFile;
		testDataExcelUtil = new TestDataExcelUtil(testDataFile);
	
	}
	
	/**
	 * Reads all test case paramaters from test data spreadsheet into memory (TestCaseInfoBean) 
	 * @param testID
	 * @param testApp
	 * @throws Exception
	 */
	public void readTestCaseData(String testID, String testApp) throws Exception
	{
		this.testID = testID;
		try{
			testCaseInfoBean = testDataExcelUtil.getTestCaseByName(testID, testApp, true);
		}catch(Exception e)
		{
			String logMessage = "Exception occurred during finding testCase with TestID " + testID + " " + e.getMessage();
			throw new FrameworkException(logMessage);

		}
		
		if(testCaseInfoBean == null)
		{
			String logMessage = "Test Case not found in TestData spreadsheet with testID " + testID + " " + testDataFile;
			throw new Exception(logMessage);
		}
	}
	
	/**
	 * searches parameter value from TestCaseInfoBean and returns the value
	 * @param parameterName  Name of the test parameter
	 * @return               Value of the test parameter
	 * @throws FrameworkException
	 */
	public String getTestParameter(String parameterName) throws FrameworkException
	{
		try{
			return testDataExcelUtil.getParameterByName(testCaseInfoBean, parameterName).parameterValue;
		}catch(Exception e)
		{
			String logMessage = "Exception occurred during finding test parameter value for test parameter " + parameterName + " " + e.getMessage();
			throw new FrameworkException(logMessage);
		}
	}
	
	/**
	 * sets value of parameter in the test data spreadsheet 
	 * @param parameterName      Name of parameter 
	 * @param value
	 * @throws FrameworkException
	 */
	
	public void setParameter(String parameterName, String value) throws FrameworkException
	{
		
		if(!testDataExcelUtil.getParameterByName(testCaseInfoBean, parameterName).isOutputParameter)
		{
			throw new FrameworkException("Not allowed to update the value of non-output type parameter " + parameterName );
		}
		
		testDataExcelUtil.getParameterByName(testCaseInfoBean, parameterName).parameterNewValue = value;
	}
	
	/**
	 * This method should be called on test teardown. This persists parameters to the test data spreadsheet 
	 * @throws FrameworkException
	 */
	
	public void persistTestCaseOutputData() throws FrameworkException
	{
		try{
			testDataExcelUtil.persistTestCaseInfoBean(testCaseInfoBean);
		}catch(FrameworkException e)
		{
			String errorMessage = "persistTestCaseOutputData(): Exception occurred during updating test data";
			throw new FrameworkException(errorMessage + " " + e.getMessage());
		}
	}
	
	/**
	 * Prints all the test parameters and values to the console.
	 * @return Test Parameters
	 */
	public String printTestCaseData()
	{
		return testDataExcelUtil.printParameters(testCaseInfoBean.TestParameters);
	}

}
