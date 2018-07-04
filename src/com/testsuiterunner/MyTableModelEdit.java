package com.testsuiterunner;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
class MyTableModelEdit extends AbstractTableModel {

	private String[] columnNames = Test.getexcelHeader();

	private Object[][] tableDataEdit = null, displayData = null;

	public MyTableModelEdit(ArrayList<TestDetail> data, String[] headers) {
		String selectedTestCasesName = EditDetails.getSelectedTestCasesName();
		String selectedModule = EditDetails.getSelectedModule();
		columnNames = headers;

		int j = 0;
		int size = data.size();

		Iterator<TestDetail> testcases = data.iterator();
		tableDataEdit = new Object[size][size];
		displayData = new Object[size][size];

		while (testcases.hasNext()) {

			TestDetail testCase = testcases.next();

			if (testCase.testSuite != null && (selectedTestCasesName.equals("All") && selectedModule.equals("All"))) {
				combination(j, testCase);
				j++;
			} else if (testCase.testSuite.equals(selectedTestCasesName) && testCase.testModule.equals(selectedModule)) {
				combination(j, testCase);
				j++;
			} else if (selectedTestCasesName.equals("All") && testCase.testModule.equals(selectedModule)) {
				combination(j, testCase);
				j++;
			} else if (testCase.testSuite.equals(selectedTestCasesName) && selectedModule.equals("All")) {
				combination(j, testCase);
				j++;
			}
		}

		testcases = data.iterator();
		j = 0;
		int i = 0;

		while (testcases.hasNext()) {
			testcases.next();
			if (j == 0) {
				tableDataMethod(j, i);
				i++;
			} else if ((displayData[j - 1][4] != null) && displayData[j - 1][4].equals(displayData[j][4])) {
				tableDataEdit[i][0] = "";
				tableDataEdit[i][1] = "";
				tableDataEdit[i][2] = "";
				tableDataEdit[i][3] = "";
				tableDataEdit[i][4] = "";
				tableDataEdit[i][5] = "";
				tableDataEdit[i][6] = "";
				tableDataEdit[i][7] = displayData[j][7];
				tableDataEdit[i][8] = displayData[j][8];
				tableDataEdit[i][9] = "";
				i++;

			} else if (displayData[j][0] == null) {
				j++;
				continue;
			} else {
				tableDataMethod(j, i);
				i++;
			}
			j++;

		}
		if (tableDataEdit[0][0] == null) {
			tableDataEdit[0][0] = "There ";
			tableDataEdit[0][1] = "There ";
			tableDataEdit[0][2] = "is ";
			tableDataEdit[0][3] = "no ";
			tableDataEdit[0][4] = "Data ";
			tableDataEdit[0][5] = "to ";
			tableDataEdit[0][6] = "display ";
			tableDataEdit[0][7] = "Sorry ";
			tableDataEdit[0][8] = "!!";
			tableDataEdit[0][9] = "!! ";
			SelectionDetails.isdatapresent = false;
		}
	}

	private void tableDataMethod(int j, int i) {
		tableDataEdit[i][0] = displayData[j][0];
		tableDataEdit[i][1] = displayData[j][1];
		tableDataEdit[i][2] = displayData[j][2];
		tableDataEdit[i][3] = displayData[j][3];
		tableDataEdit[i][4] = displayData[j][4];
		tableDataEdit[i][5] = displayData[j][5];
		tableDataEdit[i][6] = displayData[j][6];
		tableDataEdit[i][7] = displayData[j][7];
		tableDataEdit[i][8] = displayData[j][8];
		tableDataEdit[i][9] = displayData[j][9];
		SelectionDetails.isdatapresent = true;
		i++;
	}

	private void combination(int j, TestDetail testCase) {
		displayData[j][0] = testCase.testIsEnabled;
		displayData[j][1] = testCase.testSuite;
		displayData[j][2] = testCase.testModule;
		displayData[j][3] = testCase.priority;
		displayData[j][4] = testCase.testID;
		displayData[j][5] = testCase.testName;
		displayData[j][6] = testCase.testData;
		displayData[j][7] = testCase.testStepDescription;
		displayData[j][8] = testCase.testxlsActions;
		displayData[j][9] = testCase.testPlatform;

	}

	public Object[][] getdisplayData() {
		return this.tableDataEdit;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return tableDataEdit.length;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	public String getColumnNames1(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		return tableDataEdit[row][col];
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		if (col < 2) {
			return false;
		} else {
			return true;
		}
	}

}