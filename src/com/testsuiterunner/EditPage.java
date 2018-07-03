/**
 * 
 */
package com.testsuiterunner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.main.utilitylib.Global;
import com.main.utilitylib.Utilities;

/**
 * @author shradhap
 *
 */
public class EditPage {

	/**
	 * 
	 */
	
	static JFrame mainFrame;
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
	public static JLabel lblInfo = null;
	static int testcount = 0;
	static JTable table;
	static JTextField textTestCaseID;
	static JLabel lblTestCaseID;

	private static List<String> excelSuites1 = null;
	private static List<String> excelModules1 = null;
	private static List<String> excelPriority = null;
	private static List<String> excelPlatform = null;

	static List<String> excelTestNAmeDropDown = null;
	static List<String> excelModulesDropdown = null;
	static List<String> excelPlatformDropdown = null;
	
	static File testCaseFile = null;
	static File parameterFile = null;
	static File actionLibraryFile =null;
	
	static JTable parameterTable;
	static JScrollPane scrollPaneParameters;
	static String[] columnNames = { "Parameters", "Values" };
	static DefaultTableModel model = null;
	static Object[][] data = null;

	/*
	 * This method will set your selection
	 */
	
	public EditPage(String suite, String module) {
		selectedsuite = suite;
		selectedModule = module;
		
		// TODO Auto-generated constructor stub
	}

	public static void EditButton(final ActioTestGenerator actioTestGenerator, JPanel panelMenu,
			JPanel panelEdit) {
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

		
		
		
		JLabel lblNewLabel = new JLabel("Select TestCase Name :");
		lblNewLabel.setBounds(350, 20, 160, 33);
		panelEdit.add(lblNewLabel);
		final JComboBox<Object> combotestName = new JComboBox<Object>();
		excelTestNAmeDropDown = new ArrayList<String>(Arrays.asList(Test.getExcelSuites()));
		excelTestNAmeDropDown.add("All");
		Collections.sort(excelTestNAmeDropDown);
		combotestName.setModel(new DefaultComboBoxModel<Object>(excelTestNAmeDropDown.toArray()));
		combotestName.setSelectedIndex(0);
		combotestName.setBounds(500, 20, 125, 33);
		panelEdit.add(combotestName);

		JLabel lblModuleLabel = new JLabel("Select Module :");
		lblModuleLabel.setBounds(50, 20, 105, 33);
		panelEdit.add(lblModuleLabel);
		final JComboBox<Object> comboboxmodule = new JComboBox<Object>();
		
	
		/*excelModulesDropdown = new ArrayList<String>(Arrays.asList(Test.getExcelModules()));
		excelModulesDropdown.add("All");
		Collections.sort(excelModulesDropdown);
		comboboxmodule.setModel(new DefaultComboBoxModel<Object>(excelModulesDropdown.toArray()));
		comboboxmodule.setSelectedIndex(0);*/
		comboboxmodule.setBounds(160, 20, 125, 33);
		panelEdit.add(comboboxmodule);
		
		try {
			testCaseFile = utilities.getExcelFile("testfile");
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		ActioTestGenerator.populateActions(comboboxmodule, testCaseFile, 2);

		
		
		

		

		
		
		lblInfo = new JLabel();
		testCountlable(lblInfo);
		lblInfo.setBounds(30, 60, 500, 33);
		lblInfo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 15));
		panelEdit.add(lblInfo);

		
		JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setBounds(30, 85, 1300, 555);
		panelEdit.add(scrollPane1);
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
				
				
				
				new EditPage(combotestName.getSelectedItem().toString(),
						comboboxmodule.getSelectedItem().toString());
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
		

		btnSearchSelected.setBounds(800, 20, 170, 33);
		panelEdit.add(btnSearchSelected);


		JButton btnUpdate = new JButton("Update Test Case");
		
		btnUpdate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		btnUpdate.setBounds(400, 660, 170, 33);
		panelEdit.add(btnUpdate);
		

		JLabel lblActions = new JLabel("Select Actions :");
		lblActions.setBounds(1300, 20, 190, 33);
		panelEdit.add(lblActions);
		final JComboBox<Object> comboActions = new JComboBox<Object>();
		comboActions.addItem("");
		comboActions.setBounds(1400, 20, 200, 33);
		panelEdit.add(comboActions);
		columnNames = new String[] { "Parameter", "	Parameter value" };
		//Utilities utilities = new Utilities();
		try {
			actionLibraryFile = utilities.getExcelFile("actionLibraryFile");
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		ActioTestGenerator.populateActions(comboActions, actionLibraryFile, 0);

		model = new DefaultTableModel(data, columnNames) {
			@Override
			public boolean isCellEditable(int row, int cols) {
				if (cols == 0) {
					return false;
				}
				// It will make the cells of Column-1 not Editable
				return true;
			}
		};
	
	DefaultTableModel tbl = new DefaultTableModel();
	parameterTable = new JTable(tbl);
	parameterTable.setRowHeight(20);
	

		parameterTable.setModel(model);
	

	scrollPaneParameters = new JScrollPane(parameterTable);
	panelEdit.add(scrollPaneParameters, BorderLayout.CENTER);
	scrollPaneParameters.setBounds(1400, 256, 349, 324);
		
		
		JButton btnBackToMain = new JButton("Back To Main Screen");
		btnBackToMain.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
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
			setExcelSuites(new ArrayList<String>(Arrays.asList(Test.getExcelSuites())));
			setExcelModules(new ArrayList<String>(Arrays.asList(Test.getExcelModules())));
			//setExcelPlatform(new ArrayList<String>(Arrays.asList(Test.getExcelplatformSet())));
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
		EditPage.selectedsuite = selectedsuite;
	}

	public static String getSelectedsuite() {
		return selectedsuite;
	}

	public static String getSelectedSuite() {
		return getSelectedsuite();
	}

	public static void setSelectedModule1(String selectedModule) {
		EditPage.selectedModule = selectedModule;
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
		return excelSuites1;
	}

	public static void setExcelSuites(List<String> excelSuites) {
		EditPage.excelSuites1 = excelSuites;
	}

	public static List<String> getExcelModules() {
		return excelModules1;
	}

	public static void setExcelModules(List<String> excelModules) {
		EditPage.excelModules1 = excelModules;
	}

	public static List<String> getExcelPriority() {
		return excelPriority;
	}



	public static void dataReset(final JComboBox<Object> comboBoxSuite, final JComboBox<Object> comboboxmodule,
			
			JScrollPane scrollPane1, TableColumnModel colModel) {
		getExcelData();
		
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
		// System.out.println("testcount:_____________________________________");
		// System.out.println("TestCasesIDLst:" + Global.TestCasesIDLst.size());
		// System.out.println("testcount:" + testcount);
		// System.out.println("testcount:_____________________________________");
		lblInfo.setText("List of selected Test Cases : " + Global.TestCasesIDLst.size());
	}

	
	
	


}
