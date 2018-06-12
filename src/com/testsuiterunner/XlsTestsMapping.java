package com.testsuiterunner;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.main.exceptions.NoParameterFileException;
import com.main.exceptions.NoSuchApplicationException;
import com.main.exceptions.NoSuchFunctionException;
import com.main.utilitylib.GetClassDetails;
import com.main.utilitylib.Global;
import com.main.utilitylib.LoggerUtilities;
import com.main.utilitylib.Utilities;

public class XlsTestsMapping {
	Iterator<Row> tests_rowIt;
	LoggerUtilities log = new LoggerUtilities();
	public GetClassDetails classDetails = new GetClassDetails();
	Object objAction;
	Utilities serve = new Utilities();

	/*
	 * UI related additional variables
	 */
	public static String[] excelHeader = null, excelModules = null;
	public static int count;

	// Load parameter dictionary
	public Dictionary<String, String> loadParaeterDictionary(Iterator<Row> param_rowIt) {
		int rowCountparam = 1;
		Dictionary<String, String> paramDict = new Hashtable<String, String>();
		String stepName = null;
		String param_key = null;
		String param_value = null;
		// Read step_parameters sheet
		while (param_rowIt.hasNext()) {
			Row row = param_rowIt.next();
			if (rowCountparam >= 2) {
				Cell cell = row.getCell(0);
				if (cell != null)
					if (!cell.toString().equals(""))
						stepName = row.getCell(0).toString().toLowerCase().replace(" ", "_");

				param_key = (row.getCell(1) == null) ? "" : row.getCell(1).toString().toLowerCase().replace(" ", "_");
				param_value = (row.getCell(2) == null) ? "" : row.getCell(2).toString().toLowerCase();
				String key = stepName + "." + param_key;

				paramDict.put(key, param_value);

			}
			rowCountparam++;
		}
		return paramDict;
	}

	// WorkiQ step
	public void runSteps(RemoteWebDriver driver, BufferedWriter logger, String xlsStep,
			Dictionary<String, String> paramDict) throws IOException, InterruptedException, NoSuchFunctionException,
			NoParameterFileException, ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// Load Method (Action/Steps) Dictionary
		Class methodparams[] = { RemoteWebDriver.class, BufferedWriter.class, Dictionary.class };
		Class methodparams2[] = { RemoteWebDriver.class, BufferedWriter.class };
		// Create action dictionary -loadActoionDesc()
		Iterator litr = classDetails.getActionClasses().iterator();
		int objCount = 0;
		while (litr.hasNext()) {
			String clName = litr.next().toString();
			String strclassName = serve.getProperties("action_package") + "." + clName;
			Class cls = Class.forName(strclassName);
			Constructor intConstructor = cls.getConstructor(WebDriver.class);
			objAction = (Object) intConstructor.newInstance(driver);
			classDetails.addObjectLst(objAction);
		}

		// Choose and Invoke the appropriate method
		String methodDetails = classDetails.searchMethod(xlsStep.toLowerCase().replaceAll("\\s+", ""));
		if (classDetails.getmName() != null) {
			// Method method = obj.getClass().getMethod(classDetails.getmName(),
			// null);
			Class cls = Class.forName(classDetails.getClassName());
			Constructor intConstructor = cls.getConstructor(WebDriver.class);
			Object obj1 = intConstructor.newInstance(driver);
			if (classDetails.getmArgCount() == 3) { // if method has three
													// parameters
				Method method = cls.getDeclaredMethod(classDetails.getmName(), methodparams);
				method.invoke(obj1, driver, logger, paramDict);
				Global.testresult = true;
			} else if (classDetails.getmArgCount() == 2) // if method has two
															// parameters
			{
				Method method = cls.getDeclaredMethod(classDetails.getmName(), methodparams2);
				method.invoke(obj1, driver, logger);
				Global.testresult = true;
			}
		}

	}

	// Run tests
	public void runTests(RemoteWebDriver driver, BufferedWriter logger)
			throws IOException, InterruptedException, NoSuchFunctionException, NoParameterFileException,
			NoSuchApplicationException, ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		tests_rowIt = Global.tests_sheet.iterator();
		String testSuite, IsEnabled, module, priority, testID, testName, TestData, TestStepDescription = null,
				xlsActions = null, Platform;
		Dictionary<String, String> paramDict = null;
		InputStream parameterFile;
		Workbook paramwb;
		Sheet parameter_sheet;
		Iterator<Row> param_rowIt;

		Global.rowcount = 0;

		// if(Global.tableData == null){

		// Read tests sheet
		while (tests_rowIt.hasNext()) {
			Row row = tests_rowIt.next();
			String testCaseName = null;
			if (Global.rowcount >= Global.testatrow) {
				if (row.getCell(0) != null) {
					if (!row.getCell(0).toString().equals("")) {
						IsEnabled = row.getCell(0).toString();
						if (!row.getCell(4).toString().equals("")) {
							testCaseName = row.getCell(4).toString();
							log.print(logger, "==================================X===================================");
							log.logInfo(logger, "#Started running tests: \"" + testCaseName + "\"...");
						}
						log.print(logger, "==================================X===================================");
						log.logInfo(logger, "#Build Number: \"" + IsEnabled + "\"...");
						log.printSummary("==================================X===================================");
						log.logSummary("INFO: #Build Number: \"" + IsEnabled + "\"...");
					}
				}
				if (row.getCell(1) != null)
					if (!row.getCell(1).toString().equals("")) {
						testSuite = row.getCell(1).toString();
						/*
						 * if(!row.getCell(4).toString().equals("")){
						 * testCaseName=row.getCell(4).toString();
						 * log.print(logger,
						 * "==================================X==================================="
						 * ); log.logInfo(logger,
						 * "#Started running tests: \""+testCaseName+"\"..."); }
						 * log.print(logger,
						 * "==================================X==================================="
						 * );
						 */
						log.logInfo(logger, "#Running tests in Test Suite: \"" + testSuite + "\"...");
						/*
						 * log.printSummary(
						 * "==================================X==================================="
						 * );
						 */
						log.logSummary("INFO: #Running tests in Test Suite: \"" + testSuite + "\"...");

					}
				if (row.getCell(2) != null)
					if (!row.getCell(2).toString().equals("")) {
						module = row.getCell(2).toString();
						log.logInfo(logger, "#Running tests in Module: \"" + module + "\"...");
						log.logSummary("INFO: #Running tests in Module: \"" + module + "\"...");
					}
				if (row.getCell(3) != null)
					if (!row.getCell(3).toString().equals("")) {
						priority = row.getCell(3).toString();
						log.logInfo(logger, "#Running tests having Priority: \"" + priority + "\"...");
						log.logSummary("INFO: Running tests having Priority: \"" + priority + "\"...");
					}
				if (row.getCell(4) != null)
					if (!row.getCell(4).toString().equals("")) {
						// setting count of row holding current test
						Global.testatrow = Global.rowcount;
						// incrementing test count
						Global.testcount++;
						testID = row.getCell(4).toString();
						// testName = row.getCell(4).toString();
						Global.methods.add(testID);
						log.logInfo(logger, "#Running Test: \"" + testID + "\"...");
						log.logSummary("INFO: #Running Test: \"" + testID + "\"...");

						// get parameter file
						try {
							parameterFile = new FileInputStream(
									serve.getProperties("parameterfilepath") + testID + ".xls");
						} catch (Exception e) {
							throw new NoParameterFileException(
									"Parameter file " + "\"" + serve.getProperties("parameterfilepath") + testID
											+ ".xls\"" + " is missing for the Test " + testID);
						}
						paramwb = new HSSFWorkbook(parameterFile);
						// get tests sheet
						parameter_sheet = paramwb.getSheetAt(0);
						param_rowIt = parameter_sheet.iterator();

						// load dictionary
						paramDict = loadParaeterDictionary(param_rowIt);
					}
				if (row.getCell(5) != null)
					if (!row.getCell(5).toString().equals("")) {
						testName = row.getCell(5).toString();
						log.logInfo(logger, "#Running Test: \"" + testName + "\"...");
						log.logSummary("INFO: #Running Test: \"" + testName + "\"...");

					}
				if (row.getCell(6) != null)
					if (!row.getCell(6).toString().equals("")) {
						TestData = row.getCell(6).toString();
						log.logInfo(logger, "#Running tests having Priority: \"" + TestData + "\"...");
						log.logSummary("INFO: Running tests having Priority: \"" + TestData + "\"...");
					}
				if (row.getCell(7) != null)
					if (!row.getCell(7).toString().equals("")) {
						TestStepDescription = row.getCell(7).toString();
						log.logInfo(logger, "#Running tests having Priority: \"" + TestStepDescription + "\"...");
						log.logSummary("INFO: Running tests having Priority: \"" + TestStepDescription + "\"...");
					}
				if (row.getCell(8) != null)
					if (!row.getCell(8).toString().equals("")) {
						xlsActions = row.getCell(8).toString();
						log.logInfo(logger, "#Running tests having Priority: \"" + xlsActions + "\"...");
						log.logSummary("INFO: Running tests having Priority: \"" + xlsActions + "\"...");
					}
				if (row.getCell(9) != null)
					if (!row.getCell(9).toString().equals("")) {
						Platform = row.getCell(3).toString();
						log.logInfo(logger, "#Running tests having Priority: \"" + Platform + "\"...");
						log.logSummary("INFO: Running tests having Priority: \"" + Platform + "\"...");
					}

				// Run project specific test/action methods/steps
				runSteps(driver, logger, xlsActions, paramDict);

				// Set next test at row number
				try {
					// if next row contains new test then store the row count
					// holding next test and exit the loop
					if (Global.tests_sheet.getRow(Global.rowcount + 1).getCell(5) != null) {
						Global.testatrow = Global.rowcount + 1;
						break;
					}
				} catch (NullPointerException e) {
					log.logInfo(logger, "End of the test suite.");
					break;
				}
			}

			Global.rowcount++;
		}

	}

	// .xls keywords vs Project specific steps/action methods Mapping
	public void mapnExeFunctions(RemoteWebDriver driver, BufferedWriter logger)
			throws InterruptedException, IOException, NoSuchFunctionException, NoParameterFileException,
			NoSuchApplicationException, ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		if (Global.tableData == null) {
			// log.logInfo(logger, "Running tests...");
			runTests(driver, logger);
		} else {
			runTestsUI(driver, logger);
		}
		// Log test result
		if (Global.testresult) {
			log.print(logger, "----------------------------------------------------------");
			log.logInfo(logger, "#### TEST PASSED! ####");
			log.printSummary("----------------------------------------------------------");
			log.logSummary("INFO: #### TEST PASSED! ####");

		} else {
			log.print(logger, "----------------------------------------------------------");
			log.logError(logger, "#XX# TEST FAILED! #XX#");
			log.printSummary("----------------------------------------------------------");
			log.logSummary("ERROR: #XX# TEST FAILED! #XX#");
		}
	}

	public void mapnUIFunctions(RemoteWebDriver driver, BufferedWriter logger)
			throws InterruptedException, IOException, NoSuchFunctionException, NoParameterFileException,
			NoSuchApplicationException, ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		// log.logInfo(logger, "Running tests...");
		runTestsUI(driver, logger);
		// Log test result
		if (Global.testresult) {
			log.print(logger, "----------------------------------------------------------");
			log.logInfo(logger, "#### TEST PASSED! ####");
			log.printSummary("----------------------------------------------------------");
			log.logSummary("INFO: #### TEST PASSED! ####");

		} else {
			log.print(logger, "----------------------------------------------------------");
			log.logError(logger, "#XX# TEST FAILED! #XX#");
			log.printSummary("----------------------------------------------------------");
			log.logSummary("ERROR: #XX# TEST FAILED! #XX#");
		}
	}

	private void runTestsUI(RemoteWebDriver driver, BufferedWriter logger)
			throws IOException, NoParameterFileException, ClassNotFoundException, NoSuchMethodException,
			SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, InterruptedException, NoSuchFunctionException {

		InputStream parameterFile;
		Workbook paramwb;
		Sheet parameter_sheet;
		Iterator<Row> param_rowIt;
		Dictionary<String, String> paramDict = null;
		Object[][] tabledata = Global.tableData;
		Global.methods = new LinkedList<String>();
		// String uibuildnumber = null , uitestSuite = null , uimodule = null,
		// uipriority = null,uitestCaseName = null,uitestdescription,uisteps =
		// null ;
		String uiIsEnabled = null, uitestSuite = null, uimodule = null, uipriority = null, uitestID = null,
				uitestCaseName = null, uitestTestData = null, uitestdescription = null, uiActions = null,
				uiPlatform = null;

		for (int row = 0; row < tabledata.length; row++) {
			uiIsEnabled = (String) tabledata[row][0];
			uitestSuite = (String) tabledata[row][1];
			uimodule = (String) tabledata[row][2];
			uipriority = (String) tabledata[row][3];
			uitestID = (String) tabledata[row][4];
			uitestCaseName = (String) tabledata[row][5];
			uitestTestData = (String) tabledata[row][6];
			uitestdescription = (String) tabledata[row][7];
			uiActions = (String) tabledata[row][8];
			uiPlatform = (String) tabledata[row][9];

			if (uiIsEnabled != null && !uiIsEnabled.equals("")) {
				log.print(logger, "=====================================X======================================");
				log.logInfo(logger, "#Running tests in Test Suite: \"" + uiIsEnabled + "\"...");
				log.printSummary("=====================================X======================================");
				log.logSummary("INFO: #Running tests in Test Suite: \"" + uiIsEnabled + "\"...");
			}

			if (!uitestSuite.equals(null) && !uitestSuite.equals("")) {

				log.print(logger, "=====================================X======================================");
				log.logInfo(logger, "#Running tests in Test Suite: \"" + uitestSuite + "\"...");
				log.printSummary("=====================================X======================================");
				log.logSummary("INFO: #Running tests in Test Suite: \"" + uitestSuite + "\"...");
			}

			if (uimodule != null && !uimodule.equals("")) {

				log.logInfo(logger, "#Running tests in Module: \"" + uimodule + "\"...");
				log.logSummary("INFO: #Running tests in Module: \"" + uimodule + "\"...");
			}

			if (uipriority != null && !uipriority.equals("")) {
				log.logInfo(logger, "#Running tests having Priority: \"" + uipriority + "\"...");
				log.logSummary("INFO: Running tests having Priority: \"" + uipriority + "\"...");
			}
			if (uitestID != null && !uitestID.equals("")) {
				// setting count of row holding current test
				// Global.testatrow = Global.rowcount;
				// incrementing test count
				Global.testcount++;

				Global.methods.add(uitestID);
				log.logInfo(logger, "#Running Test: \"" + uitestID + "\"...");
				log.logSummary("INFO: #Running Test: \"" + uitestID + "\"...");

				// get parameter file
				try {
					parameterFile = new FileInputStream(serve.getProperties("parameterfilepath") + uitestID + ".xls");
				} catch (Exception e) {
					throw new NoParameterFileException(
							"Parameter file " + "\"" + serve.getProperties("parameterfilepath") + uitestID + ".xls\""
									+ " is missing for the Test " + uitestID);
				}
				paramwb = new HSSFWorkbook(parameterFile);
				// get tests sheet
				parameter_sheet = paramwb.getSheetAt(0);
				param_rowIt = parameter_sheet.iterator();

				// load dictionary
				paramDict = loadParaeterDictionary(param_rowIt);
			}

			if (!uitestCaseName.equals(null) && !uitestCaseName.equals("")) {
				log.print(logger, "=====================================X======================================");
				log.logInfo(logger, "#Started running tests: \"" + uitestCaseName + "\"...");
			}
			if (!uitestTestData.equals(null) && !uitestTestData.equals("")) {
				log.print(logger, "=====================================X======================================");
				log.logInfo(logger, "#Started running tests: \"" + uitestTestData + "\"...");
			}
			if (!uitestdescription.equals(null) && !uitestdescription.equals("")) {
				log.print(logger, "=====================================X======================================");
				log.logInfo(logger, "#Started running tests: \"" + uitestdescription + "\"...");
			}
			if (!uiPlatform.equals(null) && !uiPlatform.equals("")) {
				log.print(logger, "=====================================X======================================");
				log.logInfo(logger, "#Started running tests: \"" + uiPlatform + "\"...");
			}
			if (uiActions != null && !uiActions.equals("")) {
				log.print(logger, "----------------------------------------------------------");
				log.logInfo(logger, "#Running uiActions: \"" + uiActions + "\"...");
				runSteps(driver, logger, uiActions, paramDict);

			}
			// runSteps(driver, logger, uisteps, paramDict);

			// Global.rowcount++;
		}

	}

	/*
	 * Populate XLS data to UI
	 */
	public ArrayList<String[]> populateDataTOUI()
			throws IOException, InterruptedException, NoSuchFunctionException, NoParameterFileException,
			NoSuchApplicationException, ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ArrayList<String[]> testCaseList = new ArrayList<String[]>();
		String testCaseRow[] = null;
		tests_rowIt = Global.tests_sheet.iterator();
		String testSuite, IsEnabled, module, priority, testID, desc, testName, TestData, TestStepDescription = null,
				xlsActions = null, Platform;
		Dictionary<String, String> paramDict = null;
		InputStream parameterFile;
		Workbook paramwb;
		Sheet parameter_sheet;
		Iterator<Row> param_rowIt;

		Global.rowcount = 0;

		// Read tests sheet
		while (tests_rowIt.hasNext()) {

			Row row = tests_rowIt.next();
			testCaseRow = new String[row.getLastCellNum()];

			String testCaseid = null;
			if (Global.rowcount >= Global.testatrow) {
				if (row.getCell(1) != null) {

					if (!row.getCell(0).toString().equals("")) {
						IsEnabled = row.getCell(0).toString();
						testCaseRow[0] = IsEnabled.trim();
						if (!row.getCell(4).toString().equals("")) {
							testCaseid = row.getCell(4).toString();
							testCaseRow[4] = testCaseid.trim();
						}
						log.printSummary(
								"=====================================X======================================");
						log.logSummary("INFO: #Running tests in Test Suite: \"" + IsEnabled + "\"...");
					}

					if (!row.getCell(1).toString().equals("")) {
						testSuite = row.getCell(1).toString();
						testCaseRow[1] = testSuite.trim();
						if (!row.getCell(4).toString().equals("")) {
							testCaseid = row.getCell(4).toString();
							testCaseRow[4] = testCaseid.trim();
						}
						log.printSummary(
								"=====================================X======================================");
						log.logSummary("INFO: #Running tests in Test Suite: \"" + testSuite + "\"...");

					}

				}
				if (row.getCell(2) != null)
					if (!row.getCell(2).toString().equals("")) {
						module = row.getCell(2).toString();
						testCaseRow[2] = module.trim();
						log.logSummary("INFO: #Running tests in Module: \"" + module + "\"...");
					}
				if (row.getCell(3) != null)
					if (!row.getCell(3).toString().equals("")) {
						priority = row.getCell(3).toString();
						testCaseRow[3] = priority.trim();
						log.logSummary("INFO: Running tests having Priority: \"" + priority + "\"...");

					}

				if (row.getCell(4) != null)
					if (!row.getCell(4).toString().equals("")) {
						testID = row.getCell(4).toString().trim();
						Global.methods.add(testID);
						log.logSummary("INFO: #Running testID: \"" + testID + "\"...");

					}

				if (row.getCell(5) != null)
					if (!row.getCell(5).toString().equals("")) {
						testName = row.getCell(5).toString();
						testCaseRow[5] = testName.trim();
						log.logSummary("INFO: #Running test Name: \"" + testName + "\"...");
					}

				if (row.getCell(6) != null)
					if (!row.getCell(6).toString().equals("")) {
						TestData = row.getCell(6).toString();
						testCaseRow[6] = TestData.trim();
						log.logSummary("INFO: Running tests having TestData: \"" + TestData + "\"...");
					}
				if (row.getCell(7) != null)
					if (!row.getCell(7).toString().equals("")) {
						TestStepDescription = row.getCell(7).toString();
						testCaseRow[7] = TestStepDescription.trim();
						log.logSummary(
								"INFO: Running tests having TestStepDescription: \"" + TestStepDescription + "\"...");
					}
				if (row.getCell(8) != null)
					if (!row.getCell(8).toString().equals("")) {
						xlsActions = row.getCell(8).toString();
						testCaseRow[8] = xlsActions.trim();
						log.logSummary("INFO: Running tests having xlsActions: \"" + xlsActions + "\"...");
					}
				if (row.getCell(9) != null)
					if (!row.getCell(9).toString().equals("")) {
						Platform = row.getCell(9).toString();
						testCaseRow[9] = Platform.trim();
						log.logSummary("INFO: Running tests having Platform: \"" + Platform + "\"...");
					}

			} else {
				excelHeader = new String[row.getLastCellNum()];
				int i = 0;
				Iterator cells = row.cellIterator();
				List cellData = new ArrayList();
				while (cells.hasNext()) {
					HSSFCell cell = (HSSFCell) cells.next();
					cellData.add(cell.toString());
					excelHeader[i] = cell.toString();
					i++;

				}
			}
			if (Global.rowcount != 0)
				testCaseList.add(testCaseRow);
			Global.rowcount++;
		}
		return testCaseList;
	}

}