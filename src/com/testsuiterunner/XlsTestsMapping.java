package com.testsuiterunner;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.main.exceptions.NoParameterFileException;
import com.main.exceptions.NoSuchApplicationException;
import com.main.exceptions.NoSuchFunctionException;
import com.main.utilitylib.Global;
import com.main.utilitylib.LoggerUtilities;
import com.main.utilitylib.Utilities;

public class XlsTestsMapping {
	Iterator<Row> tests_rowIt;
	LoggerUtilities log = new LoggerUtilities();

	Object objAction;
	Utilities serve = new Utilities();

	/*
	 * UI related additional variables
	 */
	public static String[] excelHeader = null, excelModules = null;
	public static int count;

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
		String testSuite, IsEnabled, module, priority, testID, testName, TestData, TestStepDescription = null,
				xlsActions = null, Platform;

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
				Iterator<Cell> cells = row.cellIterator();
				List<String> cellData = new ArrayList<String>();
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