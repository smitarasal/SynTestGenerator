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






import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.main.utilitylib.Utilities;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

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
	static int testcount;
	static JTable table;

	private static List<String> excelTestCaseName = null;
	private static List<String> excelModules = null;
	static List<String> excelTestCaseNameDropdown = null;
	static List<String> excelModulesDropdown = null;
	
	
	static Map<String, String> mapValues = new HashMap<String, String>();
	static File testCaseFile = null;
	static File parameterFile = null;
	static File actionLibraryFile = null;
	
	

	
	private static JTable parameterTable;
	private static JScrollPane scrollPaneParameters;
	static	String[] columnNames = { "Parameters", "Values" };
	static DefaultTableModel model = null;
	static Object[][] data = null;


	private ArrayList<String> parametersCol;
	private ArrayList<String> parametersValues;
	private static String[][] dataValues;
	
	static JButton btnAddStep;
	static JTextField textDescription;
	static JLabel lblTestDescription;
	
	
	
	private static JTable masterTable = new JTable();
	private static JScrollPane scrollPaneMaster = new JScrollPane(masterTable);
	static DefaultTableModel masterModel = new DefaultTableModel();
	

	static Object[] masterColumns = { "IsEnabled", "Test Suite", "Module", "Priority", "Test ID", "Test Name",
			"	Test Data", "Test Step Description", "Actions", "Platform" };
	static Object[][] masterData = new Object[][] { { "", "", "", "", "", "", "", "", "", "" } };

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

		JLabel lblNewLabel = new JLabel("Select Test Case Name:");
		lblNewLabel.setBounds(380, 20, 210, 33);
		panelEdit.add(lblNewLabel);
		final JComboBox<Object> comboBoxTestCasesName = new JComboBox<Object>();
		excelTestCaseNameDropdown = new ArrayList<String>(Arrays.asList(Test.getExcelTestCaseNames()));
		excelTestCaseNameDropdown.add("All");
		Collections.sort(excelTestCaseNameDropdown);
		comboBoxTestCasesName.setModel(new DefaultComboBoxModel<Object>(excelTestCaseNameDropdown.toArray()));
		comboBoxTestCasesName.setSelectedIndex(0);
		comboBoxTestCasesName.setBounds(520, 20, 225, 33);
		panelEdit.add(comboBoxTestCasesName);

		JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setBounds(30, 85, 1300, 555);
		panelEdit.add(scrollPane1);
		table = new JTable();
		mytable = new MyTableModelEdit(testDetailsListView, excelHeaders);
		table.setModel(mytable);
		table.setEnabled(false);
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
				System.out.println(mytable.getRowCount());
				System.out.println(mytable.toString());
				table.setEnabled(false);
				TableColumnModel colModel = table.getColumnModel();
				changeColumnsWidth(colModel);
				String mydata = Global.TestCasesIDLst.toString().replace(",", "").replace("[", "").replace("]", "");
				System.out.println("data:" + Global.TestCasesIDLst.toString());
				System.out.println("data for CMD:" + mydata);

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
		
		
		

		
		JButton btnUpdate = new JButton("Update Test Case");
		
		btnUpdate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		btnUpdate.setBounds(800, 850, 170, 33);
		panelEdit.add(btnUpdate);
		
		
		
		JButton btnDeleteStep= new JButton("Delete Step");
		panelEdit.add(btnDeleteStep);
		btnDeleteStep.setBounds(1600, 850, 170, 33);
		btnDeleteStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model = new DefaultTableModel();
				model = (DefaultTableModel) table.getModel();
				model.removeRow(table.getSelectedRow());
			}
		});
		
		
		
		JLabel lblActions = new JLabel("Select Actions :");
		lblActions.setBounds(1400, 40, 190, 33);
		panelEdit.add(lblActions);
		final JComboBox<Object> comboActions = new JComboBox<Object>();
		comboActions.addItem("");
		comboActions.setBounds(1600, 40, 200, 33);
		panelEdit.add(comboActions);
		columnNames = new String[] { "Parameter", "	Parameter value" };
		
		try {
			actionLibraryFile = utilities.getExcelFile("actionLibraryFile");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
	

		comboActions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				populateTable(comboActions.getSelectedIndex());
				model = new DefaultTableModel(data, columnNames) {
					@Override
					public boolean isCellEditable(int row, int cols) {
						if (cols == 0) {
							return false;
						}
						
						return true;
					}

				};

				String[] str = { "Confirm", "" };
				parameterTable.setModel(model);
				model.addRow(str);

			}
		});
		
		
		
				textDescription = new JTextField();
				panelEdit.add(textDescription);
				textDescription.setBounds(1600, 100, 260, 33);
				
				
				lblTestDescription = new JLabel("Enter TestDescription:");
				panelEdit.add(lblTestDescription);
				lblTestDescription.setBounds(1400, 100, 190, 33);
			
				btnAddStep = new JButton("Add Step");
				panelEdit.add(btnAddStep);
				btnAddStep.setBounds(1400, 850, 170, 33);
			

				
				/*masterModel.setColumnIdentifiers(masterColumns);
				masterTable.setModel(masterModel);
				panelEdit.add(scrollPaneMaster, BorderLayout.CENTER);
				scrollPaneMaster.setBounds(29, 348, 938, 315);
				*/
				
				Object[] row = new Object[10];
	
				
				btnAddStep.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					 DefaultTableModel addmodel = new DefaultTableModel ();
					if(table.getRowCount()==0){
						
					table.setModel(mytable);
					}else if(table.getRowCount()>0){
						System.out.println("ak");
						int nrow =mytable.getRowCount();
						System.out.println("ak1=="+nrow);
						//nrow=1;
						System.out.println("ak1=="+nrow);
						
						//mytable.addRow1(comboActions, textDescription, table,nrow);
						//masterModel.setColumnIdentifiers(masterColumns);
						//table.setModel(mytable);
						

						addmodel.addColumn("IsEnabled");
						addmodel.addColumn("Test Suite");
						addmodel.addColumn("Module");
						addmodel.addColumn("Priority");
						addmodel.addColumn("Test ID");
						addmodel.addColumn("Test Name");
						addmodel.addColumn("	Test Data");
						addmodel.addColumn("Test Step Description");
						addmodel.addColumn("Actions");
						addmodel.addColumn("Platform");
						table.setModel(addmodel);
						//List<Object> list = new ArrayList<Object>();
						//ArrayList<String[][]> list1 = new ArrayList<String[][]>();
						String datavalue[][] = new String[mytable.getRowCount()][mytable.getColumnCount()];
						for (int i=0;i<mytable.getRowCount();i++)
						{
							
							for(int j=0;j<mytable.getColumnCount();j++)
							{
								//model.setValueAt(mytable.getValueAt(i, j+1), i, j+1);
								System.out.println("val===="+mytable.getValueAt(i, j+1));
								 //list.add(mytable.getValueAt(i, j+1));
								String str="";
								str = mytable.getValueAt(i,j).toString()==null?"":mytable.getValueAt(i,j).toString();
								datavalue[i][j]= str;
								
							//model.addRow(list.toArray());
							//list=null;
							}
							
						}
						System.out.println("last row==="+datavalue.length);
						
						for(int k=0;k<datavalue.length;k++)
						{
							
							Object[]  tableDataEdit5= new Object[10];
							for(int j=0;j<mytable.getColumnCount();j++)
							{
								
								tableDataEdit5[j] =datavalue[k][j] ;
							}
							
							addmodel.addRow(tableDataEdit5);
							System.out.println("row added=="+k);
						}
						
						
						
										
						
					    //model.addRow(row);
					    //here is the new action and description on clck of Add step
						Object[]  tableDataEdit5= new Object[10];
						
						
						tableDataEdit5[0] = "";
						tableDataEdit5[1] = "";
						tableDataEdit5[2] = "";
						tableDataEdit5[3] = "";
						tableDataEdit5[4] = "";
						tableDataEdit5[5] = "";
						tableDataEdit5[6] = "";
						tableDataEdit5[7] = textDescription.getText();
						tableDataEdit5[8] = comboActions.getSelectedItem();
						tableDataEdit5[9] = "";
						addmodel.addRow(tableDataEdit5);
						
						
						
						
					}
					
					
					

							int nRow = model.getRowCount(), nCol = model.getColumnCount();

							for (int i = 0; i < nRow - 1; i++) {
								for (int j = 0; j < nCol; j += 2) {

									mapValues.put(model.getValueAt(i, j).toString(), model.getValueAt(i, j + 1).toString());

								}

							}
							
						
					
				}

			});
			
			
			
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

	
	
	
	
		static void populateTable(int row) {
			try {
				Utilities utilities = new Utilities();
				// Get parameters from ForTestGeneratorFile

				actionLibraryFile = utilities.getExcelFile("actionLibraryFile");
				Workbook workbook = null;
				ArrayList<String> temp = new ArrayList<String>();
				workbook = Workbook.getWorkbook(actionLibraryFile);
				Sheet sheet = workbook.getSheet(0);
				for (int j = 2; j < sheet.getColumns(); j++) {
					Cell cell = sheet.getCell(j, row);
					if (cell.getContents() != null && cell.getContents().length() != 0)
						temp.add(cell.getContents());
				}

				parameterFile = utilities.getExcelFile("parameterFile");
				Workbook workbook2 = null;

				workbook2 = Workbook.getWorkbook(parameterFile);
				Sheet sheet2 = workbook2.getSheet(0);
				ArrayList<String> paraValues = new ArrayList<String>();
				String str;
				Iterator<String> itr = temp.iterator();
				Cell cell1 = null, cell2 = null;

				while (itr.hasNext()) {

					str = itr.next();

					paraValues.add(" ");

				} // end of while

				String[] parametersArray = temp.toArray(new String[temp.size()]);
				String[] parametersValues = paraValues.toArray(new String[paraValues.size()]);
				dataValues = new String[parametersArray.length][2];

				for (int r = 0; r < parametersArray.length; r++) {
					dataValues[r][0] = parametersArray[r];
				}

				for (int r = 0; r < parametersValues.length; r++) {
					dataValues[r][1] = parametersValues[r];
				}

				data = dataValues;
			} catch (Exception e) {

			}

		}
	
	
	
	
	
	
	
	
	
	
	
	public static void getExcelData() {
		try {
			testDetailsListView = Test.getData();
			Arrays.asList(testDetailsListView);
			excelHeaders = Test.getexcelHeader();
			setExcelTestCasesName(new ArrayList<String>(Arrays.asList(Test.getExcelTestCaseNames())));
			setExcelModules(new ArrayList<String>(Arrays.asList(Test.getExcelModules())));
			// testcount = new
			// ArrayList<String>(Arrays.asList(Test.getExcelTestCasesID()));
			selections = new EditDetails("All", "All");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	static void changeColumnsWidth(TableColumnModel colModel) {
		colModel.getColumn(0).setPreferredWidth(45);
		colModel.getColumn(2).setPreferredWidth(35);
		colModel.getColumn(3).setPreferredWidth(10);
		colModel.getColumn(5).setPreferredWidth(125);
		colModel.getColumn(6).setPreferredWidth(125);
		colModel.getColumn(7).setPreferredWidth(235);
		colModel.getColumn(8).setPreferredWidth(200);
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
		table.setEnabled(false);
		TableColumnModel colModel1 = table.getColumnModel();
		changeColumnsWidth(colModel1);
		scrollPane1.setViewportView(table);
	}

}
