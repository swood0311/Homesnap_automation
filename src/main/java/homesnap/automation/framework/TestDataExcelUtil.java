package homesnap.automation.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;



public class TestDataExcelUtil {
	
	private String testDataExcelFile = null;
	HSSFWorkbook workBook = null;
	FileInputStream fileInputStream = null;
	String sheetName;
	
	public TestDataExcelUtil(String testDataExcelFile)
	{
		this.testDataExcelFile = testDataExcelFile;
	}
	

	public TestCaseInfoBean getTestCaseByName(String testCaseName, String sheetName, boolean updateRefs) throws FrameworkException{
		
		TestCaseInfoBean testCaseInfoBean;
		try{
			testCaseInfoBean = getTestCaseByNameHelper(testCaseName, sheetName);
		}catch(Exception e)
		{
			throw new FrameworkException(e.getMessage());
		}
		if(testCaseInfoBean == null) {return null;}
		if(updateRefs){updateParameterRefValues(testCaseInfoBean.TestParameters);}
		return testCaseInfoBean;
	}
	
	
	
	public void setParameterValue(String parameterName, String value, TestCaseInfoBean testCaseInfoBean) throws Exception
	{
		
		HSSFWorkbook wb;
		HSSFSheet sheet = null;
		try{
			wb = getWorkBook();
			sheet = wb.getSheet(testCaseInfoBean.sheetName.toLowerCase());
		}catch(Exception e)
		{
			e.printStackTrace();
			closeWorkBook();
			throw new FrameworkException("Error reading TestData: check if Sheet with name: " + testCaseInfoBean.sheetName + " exists or TestData Sheet is readable: " + testDataExcelFile);
		}
		
		HSSFRow row = sheet.getRow(testCaseInfoBean.testCaseRow);
		TestParameter parm = getParameterByName(testCaseInfoBean, parameterName);
		if(parm == null){
			String errorMessage = "Test Parameter: " + parameterName + " Not found for test case: " + testCaseInfoBean.testCaseName;
			closeWorkBook();
			throw new Exception(errorMessage);
		}
		HSSFCell hSSFCell = row.getCell(parm.parameterCol);
		
		hSSFCell.setCellValue(value);
				
		
		try{
			fileInputStream.close();
		}catch(Exception e){
			//
		}
		
		FileOutputStream fileOutputStream = null;
		
		try{
			fileOutputStream = new FileOutputStream(new File(testDataExcelFile));
			
		}catch(Exception e){}
		
		wb.write(fileOutputStream);
		
		fileOutputStream.close();
		closeWorkBook();
		
	}
	
	
	public void persistTestCaseInfoBean(TestCaseInfoBean testCaseInfoBean) throws FrameworkException
	{
		HSSFWorkbook wb;
		HSSFSheet sheet = null;
		try{
			wb = getWorkBook();
			sheet = wb.getSheet(testCaseInfoBean.sheetName.toLowerCase());
		}catch(Exception e)
		{
			e.printStackTrace();
			closeWorkBook();
			throw new FrameworkException("Error reading TestData: check if Sheet with name: " + testCaseInfoBean.sheetName + " exists or TestData Sheet is readable: " + testDataExcelFile);
		}
		
		HSSFRow testCaseRow = sheet.getRow(testCaseInfoBean.testCaseRow);
		
		Iterator<TestParameter> parmsIterator = testCaseInfoBean.TestParameters.iterator();
		
		while (parmsIterator.hasNext()) {
			
			TestParameter parm = parmsIterator.next();
				
			/*If parameter is output type and new value is different than the original then update into the data sheet */
			if(parm.isOutputParameter && !parm.parameterValue.equalsIgnoreCase(parm.parameterNewValue))
			{
				HSSFCell hSSFCell;
				try{
					hSSFCell = testCaseRow.getCell(parm.parameterCol);
				}catch(Exception e)
				{
					String errorMessage = "persistTestCaseInfoBean() Exception occurred during reading Parameter"
							+ parm.parameterName + " value for TestParameterName: " + parm.parameterName + " TestCaseName: " + testCaseInfoBean.testCaseName;
					closeWorkBook();
					throw new FrameworkException(e.getMessage() + " " + errorMessage);
				}
				/*construct updated cell value */
				String newCellValue = parm.parameterName + "=" + parm.parameterNewValue;
				try{
					hSSFCell.setCellValue(newCellValue);
				}catch(Exception e)
				{
					String errorMessage = "persistTestCaseInfoBean() Exception occurred updating Parameter value to " + newCellValue + " for parameter: "
							+ parm.parameterName + " TestCaseName: " + testCaseInfoBean.testCaseName;
					closeWorkBook();
					throw new FrameworkException(e.getMessage() + " " + errorMessage);
				}
			}
		}
		
		try{
			fileInputStream.close();
		}catch(Exception e){}
		
		FileOutputStream fileOutputStream = null;
		
		try{
			fileOutputStream = new FileOutputStream(new File(testDataExcelFile));
			wb.write(fileOutputStream);
			fileOutputStream.close();
			closeWorkBook();
		}catch(Exception e){
			String errorMessage = "persistTestCaseInfoBean() Exception occurred during updating test data file " + testDataExcelFile;
			closeWorkBook();
			throw new FrameworkException(e.getMessage() + " " + errorMessage);
		}
	}

	public TestParameter getParameterByName(TestCaseInfoBean testCaseInfoBean, String parameterName) throws FrameworkException
	{
		try{
			//Log.log("Finding Parameter value for Parameter Name " + parameterName, Status.INFO);
			
						
			Iterator<TestParameter> parmsIterator = testCaseInfoBean.TestParameters.iterator();
			
			while (parmsIterator.hasNext()) {
				TestParameter parm = parmsIterator.next();
				if(parm.parameterName.trim().equalsIgnoreCase(parameterName.trim()))
				{
					return parm;
				}
				
			}
			return null;
			
		}catch(Exception e)
		{
			String message =  "Error getting parameter value for Parameter: " + parameterName;
		//	Log.log(message, Status.ERROR);
			throw new FrameworkException(message + " " + e.getMessage());
		}
	}
	

	private HSSFWorkbook getWorkBook() throws FrameworkException
	{
		if (workBook != null){ return workBook;}
		try{
			fileInputStream = new FileInputStream(new File(testDataExcelFile));
			workBook = new HSSFWorkbook(fileInputStream);
		}catch(Exception e)
		{
			String message = "Error Ocurred during reading Test Data file not found at: " + testDataExcelFile;
			e.printStackTrace();
			throw new FrameworkException(message);
		}
		
		return workBook;
	}
		
	
	private HSSFRow getRowByRowNumber(HSSFWorkbook workbook, String SheetName, int rowNum)
	{
		HSSFSheet sheet = workbook.getSheet(SheetName);
		HSSFRow hSSFRow = sheet.getRow(rowNum);
		return hSSFRow;
	}
	
	private String getCellValueFromRow(HSSFRow hSSFRow, int cellNum)
	{
		HSSFCell hSSFCell = hSSFRow.getCell(cellNum);
		return hSSFCell.getStringCellValue();
	}
	
	private String getCellValue(HSSFWorkbook workbook, String SheetName, int rowNum, int col)
	{
		HSSFRow hSSFRow = getRowByRowNumber(workbook, SheetName, rowNum);
		HSSFCell hSSFCell = hSSFRow.getCell(col);
		String value = hSSFCell.getStringCellValue();
		return value;
	}
	

	
	private TestCaseInfoBean getTestCaseByNameHelper(String testCaseName, String sheetName) throws Exception{

		this.sheetName = sheetName;
		HSSFSheet sheet = null;
		try{
			sheet = getWorkBook().getSheet(sheetName.toLowerCase());
		}catch(Exception e)
		{
			e.printStackTrace();
			closeWorkBook();
			throw new FrameworkException("Error reading TestData: check if Sheet with name: " + sheetName + " exists or TestData Sheet is readable: " + testDataExcelFile);
		}
		
		if(sheet == null){
			String message = "Error finding Sheet with name: " + sheetName + " from test data spreadsheet " + testDataExcelFile;
			System.out.println(message);
			throw new FrameworkException(message);
		}
		
		int lastRow = 0;
		try{
			lastRow = sheet.getLastRowNum();
		}catch(Exception e)
		{
			closeWorkBook();
			throw new FrameworkException("Error getting last Row number from Sheet " + sheetName + " from Test Data SpreadSheet " + testDataExcelFile);
		}
		
		int testCaseIDCol = 0;
		
		for(int i =1; i<= lastRow; i++)
		{
			HSSFRow hSSFRow = null;
			HSSFCell hSSFCell1 = null;
			
			try{
				hSSFRow = sheet.getRow(i);
				hSSFCell1 = hSSFRow.getCell(testCaseIDCol);
		
			}catch(Exception e){continue;}
			
			String cellValue = "";
			try{
				cellValue = hSSFCell1.getStringCellValue();
			}catch(Exception e) {}
			
			if(cellValue !=null && cellValue.trim().equalsIgnoreCase(testCaseName.trim()))

			{			
				TestCaseInfoBean testCaseInfoBean = new TestCaseInfoBean();
				testCaseInfoBean.testCaseName = testCaseName;
				testCaseInfoBean.sheetName = sheetName;
				testCaseInfoBean.testCaseRow = i;
						
				List<TestParameter> parameters;
				try{
					parameters = getAllTestParameters(sheet, hSSFRow, testCaseName, sheet.getSheetName());
				}catch(Exception e)
				{
					throw new FrameworkException("Error reading Test Parameters, please check if all the parameters are properly formatted " + e.getMessage());
				}
				
				if(parameters!=null)
				{
					testCaseInfoBean.TestParameters = parameters;
				}
				closeWorkBook();
				return testCaseInfoBean;
			}			
			
		}
		
		closeWorkBook();
		return null;
	}
	
	private void closeWorkBook() throws FrameworkException
	{
		try{
			if(workBook != null)
			{
				workBook.close();
				workBook = null;
			}
			if(fileInputStream !=null)
			{
				fileInputStream.close();
			}
		}catch(Exception e)
		{
			throw new FrameworkException("Exception occurred during closing TestData SpreadSheet " + testDataExcelFile);
			
		}
	}
	
	
	private List<TestParameter> getAllTestParameters(HSSFSheet sheet, HSSFRow hSSFRow, String testName, String sheetName) throws Exception
	{
		List<TestParameter> testParameterList = new ArrayList<TestParameter>();
		HSSFCell hSSFCell;
		int lastColumnInRow = 0;
		try{
			lastColumnInRow = hSSFRow.getLastCellNum();
		}catch(Exception e){
			return null;
		}
		
		int parametersStartCol = 2;
		
		for(int i =parametersStartCol; i<= lastColumnInRow; i++)
		{
			try{
				hSSFCell = hSSFRow.getCell(i);
			}catch(Exception e){
				continue;
			}
				
			TestParameter parameter = new TestParameter();
			String cellValue;
			try{
				
				parameter.parameterCol = i;
				cellValue = hSSFCell.getStringCellValue();
			}catch(Exception e){continue;}
			
			//For empty rows containing no test parameters
			if(cellValue.trim().equals(""))
			{
				continue;
			}
			
			parameter.parameterOrgRawCellValue = cellValue;
			
			if(cellValue.split("=").length == 0)
			{
				String message = "Error Reading parameters for test ID: " + testName + " sheet: " + sheetName + " make sure all parameters are present in proper format";
		//		Log.log(message, Status.FATAL);
				closeWorkBook();
				throw new FrameworkException(message);
			}
							
			parameter.parameterName = cellValue.split("=")[0];
			try{
				parameter.parameterValue = cellValue.split("=")[1];
			}catch(Exception noValueE)
			{
				parameter.parameterValue = "NOTSET";
			}
			
			if(!parameter.parameterName.toLowerCase().endsWith("output"))
			{
				if(parameter.parameterValue.equalsIgnoreCase("NOTSET") || parameter.parameterValue == "" || parameter.parameterValue == null )
				{
					closeWorkBook();
					String message = "Missing parameter values, make sure all parameters are present in proper format in TestData File " + testDataExcelFile;
		//			Log.log(message, Status.FATAL);
					throw new FrameworkException(message);
				}
			}
			
			if(parameter.parameterName.toLowerCase().endsWith("output"))
			{
				parameter.isOutputParameter = true;
			}
				
			try{				
				/*check if Parameter is referencing another TestCase parameter*/
				if(parameter.parameterValue.startsWith("$") && parameter.parameterValue.contains("."))
				{
					parameter.isRefParameter = true;
					/*extract referenced TestCaseID from the parameter */
					parameter.refTestID = parameter.parameterValue.substring(1, parameter.parameterValue.indexOf("."));
					parameter.refParameterName = parameter.parameterValue.substring(parameter.parameterValue.indexOf(".")+1, parameter.parameterValue.length());
	
				}
				
				/*check if Parameter is referencing another TestCase parameter in different sheet, it should start with #*/
				if(parameter.parameterValue.startsWith("#") && parameter.parameterValue.contains("."))
				{
					parameter.isRefParameter = true;
					parameter.isRefParameterFromAnotherSheet = true;
					/*extract referenced TestSheet from the parameter */
					parameter.refSheetName = parameter.parameterValue.substring(1, parameter.parameterValue.indexOf("."));
					
					//Removing Reference Sheet Name
					String tmpStr = parameter.parameterValue.substring(parameter.parameterValue.indexOf(".")+1, parameter.parameterValue.length());
					
					parameter.refTestID = tmpStr.substring(1, tmpStr.indexOf("."));
					
					parameter.refParameterName = tmpStr.substring(tmpStr.indexOf(".")+1, tmpStr.length());
	
					
				}
				
				testParameterList.add(parameter);
				
			}catch(Exception e){
				String message = "Error Reading parameters for test ID: " + testName + " sheet: " + sheetName + " make sure all parameters are present in proper format";
		//		Log.log(message, Status.FATAL);
				closeWorkBook();
				throw new FrameworkException(message);
			}
			
		}
		
		
		return testParameterList;
	}
		
	
	private String getParameterValueFromTestCaseInfoBean(TestCaseInfoBean testCaseInfoBean, String parameterName)
	{
		try{

			Iterator<TestParameter> parmsIterator = testCaseInfoBean.TestParameters.iterator();
			
			while (parmsIterator.hasNext()) {
				TestParameter parm = parmsIterator.next();
				if(parm.parameterName.trim().equalsIgnoreCase(parameterName.trim()))
				{
					return parm.parameterValue;
				}
				
			}
			return null;
			
		}catch(Exception e)
		{
		//	Log.log("getParameterValueFromTestCaseInfoBean: Error getting parameter value for Parameter: " + parameterName);
			return null;
		}
	}
	
	public String printParameters(List<TestParameter> parameters)
	{
		String allParameters = "";
		try{
			Iterator<TestParameter> parmsIterator = parameters.iterator();
			
			while (parmsIterator.hasNext()) {
				TestParameter parm = parmsIterator.next();
				 
				String output = parm.parameterName + " = " + parm.parameterValue + " | "
						+ " Is Output Type: " + parm.isOutputParameter + " | "
						+ " Is Ref Parameter " + parm.isRefParameter + " | "
						+ " RefParameterName: " + parm.refParameterName + " | "
						+ " RefTestID: " + parm.refTestID + " | "
						+ " isRefParameterFromAnotherSheet: " + parm.isRefParameterFromAnotherSheet
						+ " refSheetName: " + parm.refSheetName;
				allParameters = allParameters + output + "\n";
				
			}
			return allParameters;
			
		}catch(Exception e)
		{
		//	Log.log("Exception occurred during printing test parameters " + e.getMessage());
			return null;
		}
	}
	
	private void updateParameterRefValues(List<TestParameter> parameters) throws FrameworkException
	{
		try{
					
			Iterator<TestParameter> parmsIterator = parameters.iterator();
			
			while (parmsIterator.hasNext()) {
				TestParameter parm = parmsIterator.next();
				
				if(parm.isRefParameter && parm.isRefParameterFromAnotherSheet)
				{
					TestCaseInfoBean testCaseInfoBean = null;
					try
					{
						testCaseInfoBean = getTestCaseByName(parm.refTestID, parm.refSheetName, false);
					}catch(Exception e)
					{
						String message = "Error finding Referenced testCase ID " + parm.refTestID + " in Sheet: " + parm.refSheetName + " Parameter Name: " +  parm.parameterName + " please check if all parameters are properly formatted";
		//				Log.log(message, Status.FATAL);
						throw new FrameworkException(message);
					}
					
					if(testCaseInfoBean == null)
					{
						String message = "Error finding Referenced testCase ID " + parm.refTestID + " in Sheet: " + parm.refSheetName + " Parameter Name: " +  parm.parameterName + " please check if all parameters are properly formatted";
			//			Log.log(message, Status.FATAL);
						throw new FrameworkException(message);
					}
					
					parm.parameterValue = getParameterValueFromTestCaseInfoBean(testCaseInfoBean, parm.refParameterName);
					
					if(parm.parameterValue.equalsIgnoreCase("NOTSET") || parm.parameterValue.equals(null) || parm.parameterValue.equals(""))
					{
						String message = "Missing information Referenced parameter value not available " + parm.parameterName;
		//				Log.log(message, Status.FATAL);
						throw new FrameworkException(message);
					}
				}
				
				
				
				if(parm.isRefParameter && !parm.isRefParameterFromAnotherSheet)
				{
					TestCaseInfoBean testCaseInfoBean = null;
					try
					{
						testCaseInfoBean = getTestCaseByName(parm.refTestID, sheetName, false);
					}catch(Exception e)
					{
						String message = "Error finding Referenced testCase ID for Parameter " + parm.parameterName + " please check if all parameters are properly formatted";
				//		Log.log(message, Status.FATAL);
						throw new FrameworkException(message);
					}
					
					if(testCaseInfoBean == null)
					{
						String message = "Error finding Referenced testCase ID for Parameter " + parm.parameterName + " please check if all parameters are properly formatted";
				//		Log.log(message, Status.FATAL);
						throw new FrameworkException(message);
					}
					
					parm.parameterValue = getParameterValueFromTestCaseInfoBean(testCaseInfoBean, parm.refParameterName);
					
					if(parm.parameterValue.equalsIgnoreCase("NOTSET") || parm.parameterValue.equals(null) || parm.parameterValue.equals(""))
					{
						String message = "Missing information Referenced parameter value not available " + parm.parameterName + " TestID: " + parm.refTestID;
	//					Log.log(message, Status.FATAL);
						throw new FrameworkException(message);
					}
				}
			}
		}catch(Exception e)
		{
			String message = "Error Updating referenced parameter values please check if all parameters are properly formatted";
	//		Log.log(message, Status.FATAL);
			throw new FrameworkException(message);
		}
		
		
	}
	
	public class TestCaseInfoBean
	{
		String workbookPath = null;
		String sheetName = null;
		String testCaseName = null;
		int testCaseRow = -1;
		int numberOfOutputParameters = -1;
		int numberOfInputParameters = -1;
		List<TestParameter> TestParameters = null;
	}
	
	public class TestParameter
	{
		int parameterCol = -1;
		String parameterName = null;
		String parameterValue = null;
		boolean isOutputParameter = false;
		String parameterOrgRawCellValue = null;
		String parameterNewValue = "";
		
		/*If Parameter is referring to another TestCase Parameter */
		boolean isRefParameter = false;
		String refTestID = null;
		String refParameterName = null;
		
		/*If Parameter is referring to another TestCase Parameter in different sheet*/
		boolean isRefParameterFromAnotherSheet = false;
		String refSheetName = null;
		
	}

}
