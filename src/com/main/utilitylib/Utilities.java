package com.main.utilitylib;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class Utilities {
	Properties prop = new Properties();
	InputStream input = null;
	Timestamp timestamp;
	File logdir;
	File logfile;
	FileWriter fw;
	BufferedWriter bw;
	java.util.Date date;
	BufferedWriter logger = null;
	LoggerUtilities log = new LoggerUtilities();
	InputStream tests;
	Workbook wb;
	Iterator<Row> tests_rowIt;

	public void getXlsTestsMapping(String testfile) throws IOException {
		// Read test input file
		File file = new File(testfile);
		tests = new FileInputStream(testfile);
		wb = new HSSFWorkbook(tests);
		// Get tests sheet
		Global.tests_sheet = wb.getSheetAt(0);
		Global.TestCasesIDLst = new ArrayList();
	}

	// Returns rows from tests input file
	public int getTestCount() throws IOException {
		int testcount = 0;
		// Get test count
		tests_rowIt = Global.tests_sheet.iterator();
		int rowcount = 0;
		while (tests_rowIt.hasNext()) {
			Row row = tests_rowIt.next();
			if (rowcount >= 1) {
				if (row.getCell(3) != null)
					if (!row.getCell(3).toString().equals("")) {
						testcount++;
					}
			}
			rowcount++;
		}
		System.out.println("TestCount is" + testcount);
		return testcount;
	}


	// Get main config
	public String getConfigFile(String key) throws IOException {
		key = "project_conf_file_path";
		input = new FileInputStream("./src/com/conf/env/config.properties");
		prop.load(input);
		//System.out.println(prop.getProperty(key));
		return prop.getProperty(key).trim();
	
	}


	// Get project specific config Properties
	public String getProperties(String key) throws IOException {
		input = new FileInputStream(getConfigFile("project_conf_file_path"));
		prop.load(input);

		return prop.getProperty(key).trim();
	}

	// Get project specific config Properties
	public File getPropertiesfile(String key) throws IOException {
		input = new FileInputStream(getConfigFile("project_conf_file_path"));
		prop.load(input);
		File parameterFileNameC = new File(prop.getProperty(key).trim());

		return parameterFileNameC;
	}

	// Get attribute properties // This method is Deprecated!
	public String getAttributeValue(String key) throws IOException {
		// input = new
		// FileInputStream("src/com/objectrepo/demo/attribute.properties"); //
		// Need to remove hard coded path of attributes.properties
		input = new FileInputStream("src/com/objectrepo/demo/attribute.properties");
		prop.load(input);

		return prop.getProperty(key);
	}
	
	

	///new shradha
	
		public File getExcelFile(String key) throws FileNotFoundException, IOException{
			
			input = new FileInputStream(getConfigFile("/com/conf/web/workiq/config.properties"));
			prop.load(input);
			 File excelFile = new File(prop.getProperty(key));
			 return excelFile;
		}


	// Explicit Wait
	public void waitUntil(ExpectedCondition<WebElement> expectedCondition, RemoteWebDriver driver, int time) {
		WebDriverWait wait = new WebDriverWait(driver, time);
		wait.until(expectedCondition);
	}

	// wait until an element get invisible
	public void waitUntilInvisibleElement(ExpectedCondition<Boolean> invisibilityOfElementLocated,
			RemoteWebDriver driver, int time) {
		WebDriverWait wait = new WebDriverWait(driver, time);
		wait.until(invisibilityOfElementLocated);
	}

	// click event
	public void click(RemoteWebDriver driver, By by) throws IOException, InterruptedException {
		findElement(driver, by).click();
		Thread.sleep(1000);
	}

	// send key event
	public void sendKeys(RemoteWebDriver driver, By by, String key) throws IOException {
		findElement(driver, by).sendKeys(key);
	}

	// clear text field event
	public void clear(RemoteWebDriver driver, By by, String key) throws IOException {
		findElement(driver, by).clear();
	}

	// find element
	public WebElement findElement(RemoteWebDriver driver, By by) throws IOException {
		WebElement element = null;
		try {
			element = driver.findElement(by);
		} catch (Exception | AssertionError e) {
			Global.testresult = false;
			log.logError(logger, e.getMessage() + "\n" + "#XX# TEST FAILED!!! #XX#");
			log.logSummary(getTimestamp() + "	ERROR: " + e.getMessage() + "\n" + getTimestamp()
					+ "	#XX# TEST FAILED!!! #XX#");
			Assert.fail(e.getMessage());
		}
		return element;
	}

	// get Time stamp
	public Timestamp getTimestamp() {
		date = new java.util.Date();
		return new Timestamp(date.getTime());
	}

	// Set next test at row number
	public void setNextTestAtRow(BufferedWriter logger) throws IOException {
		Iterator<Row> tests_rowIt = Global.tests_sheet.iterator();
		// read tests sheet
		while (tests_rowIt.hasNext()) {
			try {
				// if next row contains new test then store the row count
				// holding next test and exit the loop
				if (Global.tests_sheet.getRow(Global.rowcount + 1).getCell(3) != null) {
					Global.testatrow = Global.rowcount + 1;
					break;
				}
			} catch (NullPointerException npe) {
				log.logInfo(logger, "End of the test suite.");
				break;
			}
			Global.rowcount++;
		}
	}

	// Launch a application
	public void launchWebApplication(WebDriver driver, String url) {
		driver.get(url);
	}

	public static int getUITestCount() {
		int testCount = 0;
		for (int row = 0; row < Global.tableData.length; row++) {
			for (int col = 0; col < Global.tableData[row].length; col++) {
				try {
					if (!Global.tableData[row][4].equals(null) && col == 4 && !Global.tableData[row][4].equals("")) {
						testCount++;

					}
				} catch (NullPointerException e) {
					/*
					 * Just get out of the loop to avoid null pointer exception
					 */
					break;
				}
			}
		}
		return testCount;
	}

	public static List<String> getUITestIDList() {
		int testCount = 0;
		Global.TestCasesIDLst.clear();
		for (int row = 0; row < Global.tableData.length; row++) {
			for (int col = 0; col < Global.tableData[row].length; col++) {
				try {
					if (!Global.tableData[row][4].equals(null) && col == 4 && !Global.tableData[row][4].equals("")) {
						Global.TestCasesIDLst.add(Global.tableData[row][4].toString().trim());
						testCount++;
					}
				} catch (NullPointerException e) {
					/*
					 * Just get out of the loop to avoid null pointer exception
					 */
					break;
				}
			}
		}
		return Global.TestCasesIDLst;
	}

}
