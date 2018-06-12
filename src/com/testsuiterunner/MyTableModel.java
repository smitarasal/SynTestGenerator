package com.testsuiterunner;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
class MyTableModel extends AbstractTableModel {

	private String[] columnNames = Test.getexcelHeader();

	private Object[][] tableData = null, displayData = null;

	public MyTableModel(ArrayList<TestDetail> data, String[] headers) {
		String selectedsuite = SelectionDetails.getSelectedSuite();
		String selectedModule = SelectionDetails.getSelectedModule();
		String selectedPriority = SelectionDetails.getSelectedPriority();
		String selectedplatform = SelectionDetails.getSelectedplatform();
		columnNames = headers;

		int j = 0;
		int size = data.size();

		Iterator<TestDetail> testcases = data.iterator();
		tableData = new Object[size][size];
		displayData = new Object[size][size];

		while (testcases.hasNext()) {

			TestDetail testCase = testcases.next();
			if (selectedplatform.equals("All")) {
				if (testCase.testSuite != null && (selectedsuite.equals("All") && selectedModule.equals("All")
						&& selectedPriority.equals("All"))) {
					combination(j, testCase);
					j++;
				} else if (testCase.testSuite.equals(selectedsuite) && testCase.testModule.equals(selectedModule)
						&& testCase.priority.equals(selectedPriority)) {
					combination(j, testCase);
					j++;
				} else if (selectedsuite.equals("All") && selectedModule.equals("All")
						&& testCase.priority.equals(selectedPriority)) {
					combination(j, testCase);
					j++;
				} else if (selectedsuite.equals("All") && testCase.testModule.equals(selectedModule)
						&& selectedPriority.equals("All")) {
					combination(j, testCase);
					j++;
				} else if (selectedsuite.equals("All") && testCase.testModule.equals(selectedModule)
						&& testCase.priority.equals(selectedPriority)) {
					combination(j, testCase);
					j++;
				} else if (testCase.testSuite.equals(selectedsuite) && selectedModule.equals("All")
						&& selectedPriority.equals("All")) {
					combination(j, testCase);
					j++;
				} else if (testCase.testSuite.equals(selectedsuite) && selectedModule.equals("All")
						&& testCase.priority.equals(selectedPriority)) {
					combination(j, testCase);
					j++;
				} else if (testCase.testSuite.equals(selectedsuite) && testCase.testModule.equals(selectedModule)
						&& selectedPriority.equals("All")) {
					combination(j, testCase);
					j++;
				}

			} else if (testCase.testPlatform.equals(selectedplatform)) {
				if (testCase.testSuite != null && (selectedsuite.equals("All") && selectedModule.equals("All")
						&& selectedPriority.equals("All"))) {
					combination(j, testCase);
					j++;
				} else if (testCase.testSuite.equals(selectedsuite) && testCase.testModule.equals(selectedModule)
						&& testCase.priority.equals(selectedPriority)) {
					combination(j, testCase);
					j++;
				} else if (selectedsuite.equals("All") && selectedModule.equals("All")
						&& testCase.priority.equals(selectedPriority)) {
					combination(j, testCase);
					j++;
				} else if (selectedsuite.equals("All") && testCase.testModule.equals(selectedModule)
						&& selectedPriority.equals("All")) {
					combination(j, testCase);
					j++;
				} else if (selectedsuite.equals("All") && testCase.testModule.equals(selectedModule)
						&& testCase.priority.equals(selectedPriority)) {
					combination(j, testCase);
					j++;
				} else if (testCase.testSuite.equals(selectedsuite) && selectedModule.equals("All")
						&& selectedPriority.equals("All")) {
					combination(j, testCase);
					j++;
				} else if (testCase.testSuite.equals(selectedsuite) && selectedModule.equals("All")
						&& testCase.priority.equals(selectedPriority)) {
					combination(j, testCase);
					j++;
				} else if (testCase.testSuite.equals(selectedsuite) && testCase.testModule.equals(selectedModule)
						&& selectedPriority.equals("All")) {
					combination(j, testCase);
					j++;
				}
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
				tableData[i][0] = "";
				tableData[i][1] = "";
				tableData[i][2] = "";
				tableData[i][3] = "";
				tableData[i][4] = "";
				tableData[i][5] = "";
				tableData[i][6] = "";
				tableData[i][7] = displayData[j][7];
				tableData[i][8] = displayData[j][8];
				tableData[i][9] = "";
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
		if (tableData[0][0] == null) {
			tableData[0][0] = "There ";
			tableData[0][1] = "is ";
			tableData[0][2] = "no ";
			tableData[0][3] = "Data ";
			tableData[0][4] = "to ";
			tableData[0][5] = "display ";
			tableData[0][6] = " !!!";
			tableData[0][7] = "Sorry ";
			tableData[0][8] = "!! ";
			tableData[0][9] = "!! ";
			SelectionDetails.isdatapresent = false;
		}
	}

	private void tableDataMethod(int j, int i) {
		tableData[i][0] = displayData[j][0];
		tableData[i][1] = displayData[j][1];
		tableData[i][2] = displayData[j][2];
		tableData[i][3] = displayData[j][3];
		tableData[i][4] = displayData[j][4];
		tableData[i][5] = displayData[j][5];
		tableData[i][6] = displayData[j][6];
		tableData[i][7] = displayData[j][7];
		tableData[i][8] = displayData[j][8];
		tableData[i][9] = displayData[j][9];
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
		return this.tableData;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return tableData.length;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	public String getColumnNames1(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		return tableData[row][col];
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