package com.testsuiterunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;

import com.main.utilitylib.Global;
import com.main.utilitylib.Utilities;

public class Test {
	static String testFileName = null;
	static ArrayList<String[]> view = null;
	ArrayList<String[]> selectview = null;
	static XlsTestsMapping uiData = null;
	static Set<String> moduleSet = new HashSet<String>();
	static Set<String> suiteSet = new HashSet<String>();
	static Set<String> isenable = new HashSet<String>();
	static Set<String> testStepSet = new HashSet<String>();
	static Set<String> prioritySet = new HashSet<String>();
	static Set<String> platformSet = new HashSet<String>();
	static Set<String> testcaseid = new HashSet<String>();
	static Set<String> testcaseName = new HashSet<String>();
	static int tcount = 0;
	static int isecountN = 0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		uiData = new XlsTestsMapping();
		Utilities utilities = new Utilities();

		try {
			testFileName = utilities.getProperties("testfile").toString();
			System.out.println(testFileName);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			utilities.getXlsTestsMapping(testFileName);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			view = uiData.populateDataTOUI();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public static ArrayList<TestDetail> getData() {

		XlsTestsMapping uiData = new XlsTestsMapping();
		try {
			view = uiData.populateDataTOUI();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		Iterator<String[]> testcases = view.iterator();
		String excelIsEnabled = null, excelsuite = null, excelmodule = null, priority = null, exceltestcaseId = null,
				exceltestcaseName = null, exceltestData = null, excelteststepDesc = null, excelAction = null,
				excelplatform = null;
		ArrayList<TestDetail> selectedList = new ArrayList<TestDetail>();
		TestDetail testData;

		while (testcases.hasNext()) {
			String[] testCase = testcases.next();
			if (!(testCase[1] == null)) {
				excelIsEnabled = testCase[0];
				excelsuite = testCase[1];
				excelmodule = testCase[2];
				priority = testCase[3];
				exceltestcaseId = testCase[4];
				exceltestcaseName = testCase[5];
				exceltestData = testCase[6];
				excelteststepDesc = testCase[7];
				excelAction = testCase[8];
				excelplatform = testCase[9];
				suiteSet.add(testCase[1]);
				isenable.add(testCase[0]);
				moduleSet.add(testCase[2]);
				prioritySet.add(testCase[3]);
				platformSet.add(testCase[9]);
				testcaseid.add(testCase[4]);
				testcaseName.add(testCase[5]);

			} else {
				excelteststepDesc = testCase[7];
				excelAction = testCase[8];

			}

			testData = new TestDetail(excelIsEnabled, excelsuite, excelmodule, priority, exceltestcaseId,
					exceltestcaseName, exceltestData, excelteststepDesc, excelAction, excelplatform);
			selectedList.add(testData);
		}
		return selectedList;
	}

	public static ArrayList<TestDetail> getDataNew() {
		ArrayList<TestDetail> selectedListnew = new ArrayList<TestDetail>();
		selectedListnew = getData();

		Predicate<TestDetail> isEnabledfilter = new Predicate<TestDetail>() {
			@Override
			public boolean test(TestDetail t) {
				// return false;
				return t.testIsEnabled.startsWith("N");
			}
		};
		selectedListnew.removeIf(isEnabledfilter);

		Set<String> hashsetList = new HashSet<String>();

		for (TestDetail testDetail : selectedListnew) {
			hashsetList.add(testDetail.testID);
			if (selectedListnew.contains(testcaseid)) {
				System.out.println("testDetail" + testDetail);
			}
			// System.out.println("selectedListnew" + selectedListnew);
		}
		System.out.printf("\nUnique values using HashSet: %s%n", hashsetList);
		Global.TestCasesIDLst = new ArrayList<String>(hashsetList);
		return selectedListnew;
	}

	/*
	 * Get modules written in excel sheet
	 */

	public static String[] getExcelSuites() {
		String[] suiteList = new String[suiteSet.size()];
		suiteList = suiteSet.toArray(new String[suiteSet.size()]);
		Arrays.sort(suiteList);
		return suiteList;
	}

	public static String[] getExcelTestCaseNames() {
		String[] testCaseNameList = new String[testcaseName.size()];
		testCaseNameList = testcaseName.toArray(new String[testcaseName.size()]);
		Arrays.sort(testCaseNameList);
		return testCaseNameList;
	}

	public static String[] getExcelModules() {

		String[] moduleList = new String[moduleSet.size()];
		moduleList = moduleSet.toArray(new String[moduleSet.size()]);
		Arrays.sort(moduleList);
		return moduleList;
	}

	public static String[] getPriority() {

		String[] priorityList = new String[prioritySet.size()];
		priorityList = prioritySet.toArray(new String[prioritySet.size()]);
		return priorityList;
	}

	public static int getexcelrowcount() {
		tcount = testcaseid.size();
		tcount = tcount - isecountN;
		return tcount;

	}

	public static String[] getExcelplatformSet() {

		String[] platformList = new String[platformSet.size()];
		platformList = platformSet.toArray(new String[platformSet.size()]);
		Arrays.sort(platformList);
		return platformList;
	}

	public static String[] getExcelTestCasesID() {
		String[] TestCasesIDList = new String[testcaseid.size()];
		TestCasesIDList = testcaseid.toArray(new String[testcaseid.size()]);
		Arrays.sort(TestCasesIDList);
		return TestCasesIDList;
	}

	public static String[] getexcelHeader() {
		return uiData.excelHeader;
	}

	public static String[] getExcelTestCasesIDcount() {
		String[] TestCasesIDList = null;
		if (isenable.equals("Y")) {
			System.out.println("testcaseid-- :" + testcaseid);
		}
		return TestCasesIDList;

	}

}
