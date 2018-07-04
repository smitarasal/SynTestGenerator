package com.testsuiterunner;

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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import com.main.utilitylib.Global;
import com.main.utilitylib.Utilities;

public class EditDetails {
	public static String selectedTestCaseName = null;
	public static String selectedModule = null;
	static List<String> TestCasesIDLst = new ArrayList<String>();
	static ArrayList<TestDetail> testDetailsListView = null;
	private static List<String> excelTestData = null;
	public static List<String> excelTestCaseID = null;
	public static EditDetails selections = null;
	static String[] excelHeaders = null;
	static Boolean isdatapresent = true;
	static MyTableModelEdit mytable = null;
	public static JLabel lblInfo = null;
	static int testcount = 0;
	static JTable table;

	private static List<String> excelTestCaseName = null;
	private static List<String> excelModules = null;
	static List<String> excelTestCaseNameDropdown = null;
	static List<String> excelModulesDropdown = null;

	/*
	 * This method will set your selection
	 */
	public EditDetails(String TestCasesName, String module) {
		selectedTestCaseName = TestCasesName;
		selectedModule = module;

	}

	public static void EditButtonCode(final ActioTestGenerator actioTestGenerator, JPanel panelMenu, JPanel panelEdit) {
		new XlsTestsMapping();
		Utilities utilities = new Utilities();
		String testFileName1 = null;

		try {
			testFileName1 = utilities.getProperties("testfile");
			utilities.getXlsTestsMapping(testFileName1);
		} catch (IOException e2) {

		}
		getExcelData();

		panelEdit.setLayout(null);
		panelEdit.setVisible(false);

		JLabel lblModuleLabel = new JLabel("Select Module :");
		lblModuleLabel.setBounds(50, 20, 225, 33);
		panelEdit.add(lblModuleLabel);
		final JComboBox<Object> comboboxmodule = new JComboBox<Object>();
		excelModulesDropdown = new ArrayList<String>(Arrays.asList(Test.getExcelModules()));
		excelModulesDropdown.add("All");
		Collections.sort(excelModulesDropdown);
		comboboxmodule.setModel(new DefaultComboBoxModel<Object>(excelModulesDropdown.toArray()));
		comboboxmodule.setSelectedIndex(0);
		comboboxmodule.setBounds(135, 20, 225, 33);
		panelEdit.add(comboboxmodule);

		JLabel lblNewLabel = new JLabel("Select Test Case :");
		lblNewLabel.setBounds(389, 20, 210, 33);
		panelEdit.add(lblNewLabel);
		final JComboBox<Object> comboBoxTestCasesName = new JComboBox<Object>();
		excelTestCaseNameDropdown = new ArrayList<String>(Arrays.asList(Test.getExcelTestCaseNames()));
		excelTestCaseNameDropdown.add("All");
		Collections.sort(excelTestCaseNameDropdown);
		comboBoxTestCasesName.setModel(new DefaultComboBoxModel<Object>(excelTestCaseNameDropdown.toArray()));
		comboBoxTestCasesName.setSelectedIndex(0);
		comboBoxTestCasesName.setBounds(490, 20, 225, 33);
		panelEdit.add(comboBoxTestCasesName);

		JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setBounds(30, 85, 1300, 555);
		panelEdit.add(scrollPane1);
		table = new JTable();
		mytable = new MyTableModelEdit(testDetailsListView, excelHeaders);
		table.setModel(mytable);
		scrollPane1.setViewportView(table);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		TableColumnModel colModel = table.getColumnModel();
		changeColumnsWidth(colModel);

		JButton btnSearchSelected = new JButton("Search Selected");
		btnSearchSelected.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new EditDetails(comboBoxTestCasesName.getSelectedItem().toString(),
						comboboxmodule.getSelectedItem().toString());
				mytable = new MyTableModelEdit(testDetailsListView, excelHeaders);
				table.setModel(mytable);
				TableColumnModel colModel = table.getColumnModel();
				changeColumnsWidth(colModel);

			}
		});

		btnSearchSelected.setBounds(1100, 20, 170, 33);
		panelEdit.add(btnSearchSelected);
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dataReset(comboBoxTestCasesName, comboboxmodule, lblInfo, scrollPane1, colModel);

			}
		});
		btnReset.setBounds(300, 660, 200, 33);
		panelEdit.add(btnReset);

		JButton btnBackToMain = new JButton("Back To Main Screen");
		btnBackToMain.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dataReset(comboBoxTestCasesName, comboboxmodule, lblInfo, scrollPane1, colModel);
				panelEdit.setVisible(false);
				panelMenu.setVisible(true);
			}
		});
		btnBackToMain.setBounds(101, 660, 170, 33);
		panelEdit.add(btnBackToMain);
	}

	public static void getExcelData() {
		try {
			testDetailsListView = Test.getDataNew();
			Arrays.asList(testDetailsListView);
			excelHeaders = Test.getexcelHeader();
			testcount = Test.getexcelrowcount();
			setExcelTestCasesName(new ArrayList<String>(Arrays.asList(Test.getExcelTestCaseNames())));
			setExcelModules(new ArrayList<String>(Arrays.asList(Test.getExcelModules())));
			Global.TestCasesIDLst = new ArrayList<String>(Arrays.asList(Test.getExcelTestCasesID()));
			selections = new EditDetails("All", "All");
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

	public static void setSelectedTestCasesName(String selectedTestCasesName) {
		EditDetails.selectedTestCaseName = selectedTestCasesName;
	}

	public static String getSelectedTestCasesName() {
		return selectedTestCaseName;
	}

	public static void setSelectedModule(String selectedModule) {
		EditDetails.selectedModule = selectedModule;
	}

	public static String getSelectedModule() {
		return selectedModule;
	}

	public static List<String> getExcelTestCasesName() {
		return excelTestCaseName;
	}

	public static void setExcelTestCasesName(List<String> excelTestCasesName) {
		EditDetails.excelTestCaseName = excelTestCasesName;
	}

	public static List<String> getExcelModules() {
		return excelModules;
	}

	public static void setExcelModules(List<String> excelModules) {
		EditDetails.excelModules = excelModules;
	}

	public static List<String> getExcelTestData() {
		return excelTestData;
	}

	public static void setExcelTestData(List<String> excelTestData) {
		EditDetails.excelTestData = excelTestData;
	}

	public static void dataReset(final JComboBox<Object> comboBoxTestCasesName, final JComboBox<Object> comboboxmodule,
			JLabel lblInfo, JScrollPane scrollPane1, TableColumnModel colModel) {
		getExcelData();
		comboboxmodule.setSelectedIndex(0);
		comboBoxTestCasesName.setSelectedIndex(0);
		mytable = new MyTableModelEdit(testDetailsListView, excelHeaders);
		table.setModel(mytable);
		TableColumnModel colModel1 = table.getColumnModel();
		changeColumnsWidth(colModel1);
		scrollPane1.setViewportView(table);
	}

}
