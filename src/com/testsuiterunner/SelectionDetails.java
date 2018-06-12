package com.testsuiterunner;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import com.main.utilitylib.Global;
import com.main.utilitylib.Utilities;

public class SelectionDetails {
	public static String selectedsuite = null;
	public static String selectedModule = null;
	public static String selectedPriority = null;
	public static String selectedPlatform = null;
	static List<String> TestCasesIDLst = new ArrayList<String>();
	static ArrayList<TestDetail> testDetailsListView = null;
	private static List<String> excelTestData = null;
	public static List<String> excelTestCaseID = null;
	public static SelectionDetails selections = null;
	static String[] excelHeaders = null;
	static Boolean isdatapresent = true;
	static MyTableModel mytable = null;

	static int testcount = 0;
	static JTable table;

	private static List<String> excelSuites = null;
	private static List<String> excelModules = null;
	private static List<String> excelPriority = null;
	private static List<String> excelPlatform = null;

	static List<String> excelSuitesDropdown = null;
	static List<String> excelModulesDropdown = null;
	static List<String> excelPlatformDropdown = null;

	/*
	 * This method will set your selection
	 */
	public SelectionDetails(String suite, String module, String priority, String platform) {
		selectedsuite = suite;
		selectedModule = module;
		selectedPriority = priority;
		selectedPlatform = platform;

	}

	public static void ViewButtonCode(final ActioTestGenerator actioTestGenerator, JPanel panelMenu,
			JPanel panelViewTests) {
		new XlsTestsMapping();
		Utilities utilities = new Utilities();
		String testFileName1 = null;

		try {
			testFileName1 = utilities.getProperties("testfile");
			utilities.getXlsTestsMapping(testFileName1);
		} catch (IOException e2) {

		}
		getExcelData();

		panelViewTests.setLayout(null);
		panelViewTests.setVisible(false);

		JLabel lblNewLabel = new JLabel("Select Suite :");
		lblNewLabel.setBounds(50, 20, 160, 33);
		panelViewTests.add(lblNewLabel);
		final JComboBox<Object> comboBoxSuite = new JComboBox<Object>();
		excelSuitesDropdown = new ArrayList<String>(Arrays.asList(Test.getExcelSuites()));
		excelSuitesDropdown.add("All");
		Collections.sort(excelSuitesDropdown);
		comboBoxSuite.setModel(new DefaultComboBoxModel<Object>(excelSuitesDropdown.toArray()));
		comboBoxSuite.setSelectedIndex(0);
		comboBoxSuite.setBounds(130, 20, 125, 33);
		panelViewTests.add(comboBoxSuite);

		JLabel lblModuleLabel = new JLabel("Select Module :");
		lblModuleLabel.setBounds(295, 20, 105, 33);
		panelViewTests.add(lblModuleLabel);
		final JComboBox<Object> comboboxmodule = new JComboBox<Object>();
		excelModulesDropdown = new ArrayList<String>(Arrays.asList(Test.getExcelModules()));
		excelModulesDropdown.add("All");
		Collections.sort(excelModulesDropdown);
		comboboxmodule.setModel(new DefaultComboBoxModel<Object>(excelModulesDropdown.toArray()));
		comboboxmodule.setSelectedIndex(0);
		comboboxmodule.setBounds(385, 20, 125, 33);
		panelViewTests.add(comboboxmodule);

		JLabel lblPriority = new JLabel("Select Priority :");
		lblPriority.setBounds(540, 20, 160, 33);
		panelViewTests.add(lblPriority);
		final JComboBox<Object> comboboxpriority = new JComboBox<Object>();
		comboboxpriority.setModel(new DefaultComboBoxModel<Object>(new String[] { "All", "P1", "P2", "P3" }));
		comboboxpriority.setBounds(625, 20, 125, 33);
		comboboxpriority.setSelectedIndex(0);
		panelViewTests.add(comboboxpriority);

		JLabel label = new JLabel("Select Platform :");
		label.setBounds(770, 20, 90, 33);
		panelViewTests.add(label);

		final JComboBox<Object> comboBoxPlatform = new JComboBox<Object>();
		excelPlatformDropdown = new ArrayList<String>(Arrays.asList(Test.getExcelplatformSet()));
		excelPlatformDropdown.add("All");
		Collections.sort(excelPlatformDropdown);
		comboBoxPlatform.setModel(new DefaultComboBoxModel<Object>(excelPlatformDropdown.toArray()));
		comboBoxPlatform.setBounds(860, 20, 153, 33);
		comboBoxPlatform.setSelectedIndex(0);
		panelViewTests.add(comboBoxPlatform);

		JLabel lblInfo = new JLabel();
		testCountlable(lblInfo);
		lblInfo.setBounds(30, 60, 500, 33);
		lblInfo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 15));
		panelViewTests.add(lblInfo);

		JLabel lblnote = new JLabel();
		lblnote.setText("Note: Only 'IsEnabled = Y' test cases are displayed here.");
		lblnote.setBounds(30, 630, 450, 33);
		lblnote.setFont(new java.awt.Font("Arial", java.awt.Font.ITALIC, 12));
		lblnote.setForeground(Color.RED);
		panelViewTests.add(lblnote);

		JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setBounds(30, 85, 1300, 555);
		panelViewTests.add(scrollPane1);
		table = new JTable();
		mytable = new MyTableModel(testDetailsListView, excelHeaders);
		table.setModel(mytable);
		scrollPane1.setViewportView(table);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		TableColumnModel colModel = table.getColumnModel();
		changeColumnsWidth(colModel);
		JButton btnSearchSelected = new JButton("Search Selected");
		btnSearchSelected.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				new SelectionDetails(comboBoxSuite.getSelectedItem().toString(),
						comboboxmodule.getSelectedItem().toString(), comboboxpriority.getSelectedItem().toString(),
						comboBoxPlatform.getSelectedItem().toString());
				mytable = new MyTableModel(testDetailsListView, excelHeaders);
				Global.TestCasesIDLst.clear();
				try {
					Object[][] tableData = mytable.getdisplayData();
					Global.tableData = tableData;
					if (Global.tableData != null) {
						testcount = Utilities.getUITestCount();
						TestCasesIDLst = Utilities.getUITestIDList();

					} else {
						testcount = utilities.getTestCount();
						Global.TestCasesIDLst.clear();
					}

				} catch (Exception e1) {

				}
				if (isdatapresent.equals(true)) {
				} else {
					testcount = 0;
					Global.TestCasesIDLst.clear();
				}

				table.setModel(mytable);
				TableColumnModel colModel = table.getColumnModel();
				changeColumnsWidth(colModel);
				testCountlable(lblInfo);
			}
		});

		btnSearchSelected.setBounds(1100, 20, 170, 33);
		panelViewTests.add(btnSearchSelected);

		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dataReset(comboBoxSuite, comboboxmodule, comboboxpriority, comboBoxPlatform, lblInfo, scrollPane1,
						colModel);

			}
		});
		btnReset.setBounds(300, 660, 200, 33);
		// panelViewTests.add(btnReset);

		JButton btnBackToMain = new JButton("Back To Main Screen");
		btnBackToMain.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dataReset(comboBoxSuite, comboboxmodule, comboboxpriority, comboBoxPlatform, lblInfo, scrollPane1,
						colModel);
				panelViewTests.setVisible(false);
				panelMenu.setVisible(true);
			}
		});
		btnBackToMain.setBounds(101, 660, 170, 33);
		panelViewTests.add(btnBackToMain);

		JButton btnExecuteButton = new JButton("Execute Selected Test Cases");
		btnExecuteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Global.TestCasesIDLst.isEmpty()) {
					System.out.println("No test case is selected");
					JOptionPane.showMessageDialog(panelViewTests,
							"<html> <b> Please select test cases to execute </b> </html>", "Execution error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					String mydata = Global.TestCasesIDLst.toString().replace(",", "").replace("[", "").replace("]", "");
					System.out.println("--------------------------------------------------------------");
					System.out.println("data::" + mydata);
					System.out.println("--------------------------------------------------------------");
					JOptionPane.showMessageDialog(panelViewTests,
							"<html> <b> Execution Started Successfully, Click 'Ok' button to return Main Screen </b> </html>");
					panelViewTests.setVisible(false);
					panelMenu.setVisible(true);
					dataReset(comboBoxSuite, comboboxmodule, comboboxpriority, comboBoxPlatform, lblInfo, scrollPane1,
							colModel);
				}

			}
		});
		btnExecuteButton.setBounds(1100, 660, 230, 33);
		panelViewTests.add(btnExecuteButton);
	}

	public static void getExcelData() {
		try {
			testDetailsListView = Test.getDataNew();
			Arrays.asList(testDetailsListView);
			excelHeaders = Test.getexcelHeader();
			testcount = Test.getexcelrowcount();
			setExcelSuites(new ArrayList<String>(Arrays.asList(Test.getExcelSuites())));
			setExcelModules(new ArrayList<String>(Arrays.asList(Test.getExcelModules())));
			setExcelPlatform(new ArrayList<String>(Arrays.asList(Test.getExcelplatformSet())));
			Global.TestCasesIDLst = new ArrayList<String>(Arrays.asList(Test.getExcelTestCasesID()));
			selections = new SelectionDetails("All", "All", "All", "All");

		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	static void changeColumnsWidth(TableColumnModel colModel) {
		colModel.getColumn(0).setPreferredWidth(00);
		colModel.getColumn(2).setPreferredWidth(35);
		colModel.getColumn(3).setPreferredWidth(10);
		colModel.getColumn(5).setPreferredWidth(125);
		colModel.getColumn(6).setPreferredWidth(125);
		colModel.getColumn(7).setPreferredWidth(235);
		colModel.getColumn(8).setPreferredWidth(200);
		table.removeColumn(colModel.getColumn(0));
	}

	public static void setSelectedsuite(String selectedsuite) {
		SelectionDetails.selectedsuite = selectedsuite;
	}

	public static String getSelectedsuite() {
		return selectedsuite;
	}

	public static String getSelectedSuite() {
		return getSelectedsuite();
	}

	public static void setSelectedModule1(String selectedModule) {
		SelectionDetails.selectedModule = selectedModule;
	}

	public static String getSelectedModule1() {
		return selectedModule;
	}

	public static String getSelectedModule() {
		return getSelectedModule1();
	}

	public static String getSelectedPriority() {
		return selectedPriority;
	}

	public static String getSelectedplatform() {
		return selectedPlatform;
	}

	public static List<String> getExcelSuites() {
		return excelSuites;
	}

	public static void setExcelSuites(List<String> excelSuites) {
		SelectionDetails.excelSuites = excelSuites;
	}

	public static List<String> getExcelModules() {
		return excelModules;
	}

	public static void setExcelModules(List<String> excelModules) {
		SelectionDetails.excelModules = excelModules;
	}

	public static List<String> getExcelPriority() {
		return excelPriority;
	}

	public static void setExcelPriority(List<String> excelPriority) {
		SelectionDetails.excelPriority = excelPriority;
	}

	public static List<String> getExcelPlatform() {
		return excelPlatform;
	}

	public static void setExcelPlatform(List<String> excelPlatform) {
		SelectionDetails.excelPlatform = excelPlatform;
	}

	public static List<String> getExcelPlatformDropdown() {
		return excelPlatformDropdown;
	}

	public static void setExcelPlatformDropdown(List<String> excelPlatformDropdown) {
		SelectionDetails.excelPlatformDropdown = excelPlatformDropdown;
	}

	public static List<String> getExcelTestData() {
		return excelTestData;
	}

	public static void setExcelTestData(List<String> excelTestData) {
		SelectionDetails.excelTestData = excelTestData;
	}

	public static void dataReset(final JComboBox<Object> comboBoxSuite, final JComboBox<Object> comboboxmodule,
			final JComboBox<Object> comboboxpriority, final JComboBox<Object> comboBoxPlatform, JLabel lblInfo,
			JScrollPane scrollPane1, TableColumnModel colModel) {
		getExcelData();
		comboBoxPlatform.setSelectedIndex(0);
		comboboxpriority.setSelectedIndex(0);
		comboboxmodule.setSelectedIndex(0);
		comboBoxSuite.setSelectedIndex(0);
		mytable = new MyTableModel(testDetailsListView, excelHeaders);
		table.setModel(mytable);
		TableColumnModel colModel1 = table.getColumnModel();
		changeColumnsWidth(colModel1);
		testCountlable(lblInfo);
		scrollPane1.setViewportView(table);
	}

	public static void testCountlable(JLabel lblInfo) {
		lblInfo.removeAll();
		lblInfo.validate();
		lblInfo.repaint();
		lblInfo.setText("List of selected Test Cases : ");
		System.out.println("testcount:_____________________________________");
		System.out.println("TestCasesIDLst:" + Global.TestCasesIDLst.size());
		System.out.println("testcount:" + testcount);
		System.out.println("testcount:_____________________________________");
		lblInfo.setText("List of selected Test Cases : " + Global.TestCasesIDLst.size());
	}

}
