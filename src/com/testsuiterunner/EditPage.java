/**
 * 
 */
package com.testsuiterunner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

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
import javax.swing.table.TableModel;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

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
	
	
	
	static List<String> TestCasesIDLst = new ArrayList<String>();
	public static List<String> excelTestCaseID = null;
	static String[] excelHeaders = null;
	static ArrayList<TestDetail> testDetailsListView = null;
	
	
	static Utilities utilities = new Utilities();
	static Map<String, String> mapValues = new HashMap<String, String>();
	static File testCaseFile = null;
	static File parameterFile = null;
	static File actionLibraryFile = null;
	
	private static JComboBox comboActions;
	static JComboBox comboboxmodule;
	
	static JLabel lblTestSuite;
	static JLabel lblActions;
	
	
	static JButton btnCreateSaveTestButton;
	static JButton btnBackToMain;
	static JButton btnAddStep;
	static JButton btnDeleteStep;
	
	
	 static JTable parameterTable;
		static JScrollPane scrollPaneParameters;
	static String[] columnNames = { "Parameters", "Values" };
	public	static DefaultTableModel paramodel = new DefaultTableModel();
	
	
	static Vector mastersheetData = new Vector();
	static Vector headers = new Vector();
	
	private static JTable Table = new JTable();
	static DefaultTableModel model112 = new DefaultTableModel(mastersheetData,headers);

	private static JScrollPane scrollPaneMaster = new JScrollPane(Table);
	
	static Object[][] masterData = new Object[][] { { "", "", "", "", "", "", "", "", "", "" } };
	
	
   
	
	  
	  
	
	
	private ArrayList<String> parametersCol;
	private ArrayList<String> parametersValues;
	private static String[][] dataValues;
	static Object[][] data = null;
	
//	static Vector actions = new Vector();
	static Vector parameters = new Vector();
	

	
	
	static JTextField textDescription;
	static JLabel lblTestDescription;
	static String selectedItem="";
	static String idvalue="";
	 //static String deletedvalue="";
	static  ArrayList<Integer> deletedvalue = new ArrayList<Integer>();
	static  ArrayList<String> deletedvalue1 = new ArrayList<String>();
	
	static JLabel lblUpdate;

	
	static Set<String> moduleSet = new HashSet<String>();
	
	

	
	public EditPage(String suite, String module) {
		
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param actioTestGenerator
	 * @param panelMenu
	 * @param panelEdit
	 */
	/**
	 * @param actioTestGenerator
	 * @param panelMenu
	 * @param panelEdit
	 */
	/**
	 * @param actioTestGenerator
	 * @param panelMenu
	 * @param panelEdit
	 */
	public static void EditButton(final ActioTestGenerator actioTestGenerator, JPanel panelMenu, JPanel panelEdit){
		
		
		
		panelEdit.setLayout(null);
		panelEdit.setVisible(false);

		
		lblUpdate = new JLabel("Updated The TestCase Successfully.");
		lblUpdate.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblUpdate.setForeground(Color.blue);
		lblUpdate.setVisible(false);
		panelEdit.add(lblUpdate);
		lblUpdate.setBounds(50, 50, 286, 87);
		
		JLabel lblModuleLabel = new JLabel("Select Module :");
		lblModuleLabel.setBounds(50, 20, 225, 33);
	
		panelEdit.add(lblModuleLabel);
		final JComboBox<Object> comboboxmodule = new JComboBox<Object>();
		final JComboBox<Object> comboTestID = new JComboBox<Object>();
		comboboxmodule.addItem("");
		comboboxmodule.setBounds(135, 20, 225, 33);
		panelEdit.add(comboboxmodule);
		
		
		
		
		//populating the module values uniquely from excel sheet.
		Workbook workbook = null;
		 Vector actions = new Vector();
		try {
			testCaseFile = utilities.getExcelFile("testfile");
			workbook = Workbook.getWorkbook(testCaseFile);
			Sheet sheet = workbook.getSheet(0);
			actions.clear();
			//ArrayList details = new ArrayList();
			for (int i = 1; i < sheet.getRows(); i++) {
				Cell cell = sheet.getCell(2, i);
				
				
				String con = cell.getContents();
				if (con != null && con.length() != 0 && cell.getContents().equalsIgnoreCase(con)) {

					
				} else {
					continue;
				}
				actions.add(con);
				moduleSet.addAll(actions);
			//	String[] moduleList = new String[moduleSet.size()];
				  List<String> moduleList = new ArrayList<String>(moduleSet);
				  Collections.sort(moduleList);
			//	moduleList = moduleSet.toArray(new String[moduleSet.size()]);
			
				//Arrays.sort(moduleList);
			}
			Iterator<String> itr = moduleSet.iterator();
			
			
			while (itr.hasNext()) {
				
				comboboxmodule.addItem(itr.next());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}//End of populating the module values uniquely from excel sheet.
		
		
		
		
		
		
		comboboxmodule.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			 selectedItem	=comboboxmodule.getSelectedItem().toString();
			//System.out.println("selected item is=  "+selectedItem);
			//get the test case name from excel sheet
			try {
				testCaseFile = utilities.getExcelFile("testfile");
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			JLabel lblTestId = new JLabel("Select TestCase ID");
			lblTestId.setBounds(380, 20, 210, 33);
			panelEdit.add(lblTestId);
			ActioTestGenerator.populateTCid(comboTestID, testCaseFile,selectedItem ,4, panelEdit);
			
			}
		}); //end of populating the TC_IDs
		
			
			
		JButton btnBackToMain = new JButton("Back To Main Screen");
		btnBackToMain.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				lblUpdate.setVisible(false);
				panelEdit.setVisible(false);
				panelMenu.setVisible(true);
				//comboActions.setSelectedIndex(0);
				comboboxmodule.setSelectedIndex(0);
				
				
				
				DefaultTableModel model = (DefaultTableModel) parameterTable.getModel();
				while (model.getRowCount() > 0) {
					for (int i = 0; i < model.getRowCount(); i++) {
						model.removeRow(i);
					}
				}
				
				DefaultTableModel modelnew = new DefaultTableModel();
				Table.setModel(modelnew);
				//Table.getModel();
				while (modelnew.getRowCount() > 0) {
					for (int i = 0; i < modelnew.getRowCount(); i++) {
						modelnew.removeRow(i);
					}
				}
				
			}
		});
		btnBackToMain.setBounds(101, 600, 170, 33);
		panelEdit.add(btnBackToMain);
		
		
	ActioTestGenerator.btnEditTests.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			EditPage.EditButton(actioTestGenerator, panelMenu, panelEdit);
			//EditDetails.EditButtonCode(self, panelMenu, panelEdit);
			panelMenu.setVisible(false);
			panelEdit.setVisible(true);
			
		}
	});
		
				
		
		JLabel lblActions = new JLabel("Select Actions :");
		lblActions.setBounds(1100, 20, 190, 33);
		panelEdit.add(lblActions);
		//final JComboBox<Object> comboActions = new JComboBox<Object>();
		comboActions = new JComboBox();
		comboActions.addItem("");
		//comboActions.addItem("");
		comboActions.setBounds(1250, 20, 200, 33);
		panelEdit.add(comboActions);
	
		columnNames = new String[] { "Parameter", "	Parameter value" };
		
		Utilities utilities = new Utilities();
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
		
		paramodel = new DefaultTableModel(data, columnNames) {
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

		parameterTable.setModel(paramodel);

		scrollPaneParameters = new JScrollPane(parameterTable);
		panelEdit.add(scrollPaneParameters, BorderLayout.CENTER);
		scrollPaneParameters.setBounds(1100, 208, 349, 315);
		
		
	
		comboActions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				populateTable(comboActions.getSelectedIndex());
				paramodel = new DefaultTableModel(data, columnNames) {
					@Override
					public boolean isCellEditable(int row, int cols) {
						if (cols == 0) {
							return false;
						}
						// It will make the cells of Column-1 not Editable
						return true;
					}

				};

				String[] str = { "Confirm", "" };
				parameterTable.setModel(paramodel);
				paramodel.addRow(str);

			}
		});
		
		
		panelEdit.add(scrollPaneMaster, BorderLayout.CENTER);
		
		//scrollPaneMaster.setBounds(30, 100, 1000, 500);
		scrollPaneMaster.setBounds(30, 208, 1020, 315);
		scrollPaneMaster.setVisible(false);
		
		
			
			
			
			
		
		JButton btnSearchSelected = new JButton("Search Selected");
		btnSearchSelected.setBounds(800, 20, 170, 33);
		panelEdit.add(btnSearchSelected);		
	      
		
		
	
		btnSearchSelected.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				
					
				scrollPaneMaster.setVisible(true);
				List<String> list = new ArrayList<String>();
				list.add("IsEnabled");
				list.add("Test Suite");
				list.add("Module");
				list.add("Priority");
				list.add("Test ID");
				list.add("Test Name");
				list.add("Test Data");
				list.add("Test Step Description");
				list.add("Actions");
				list.add("Platform");
				
				
				try {
					int flag=0;
					idvalue =comboTestID.getSelectedItem().toString();
				//System.out.println("IdValue   "+idvalue);
					Workbook workbook = Workbook.getWorkbook(testCaseFile);
					Sheet sheet = workbook.getSheet(0);
					headers.clear();
					String current="";
					String referance="";
					String Str2="";
					deletedvalue.clear();
					// deletedvalue="";
				
					/*for (int i = 0; i < sheet.getColumns(); i++) {
					Cell cell1 = sheet.getCell(i, 0);
					//headers.add(cell1.getContents());
					
					}*/
					headers.addAll(list);
					
					mastersheetData.clear();
					for (int j = 1; j < sheet.getRows(); j++) {
					Vector d = new Vector();
					for (int i = 0; i < sheet.getColumns(); i++) {
						
					Cell cell = sheet.getCell(i, j);
					Cell cellc = sheet.getCell(4, j);
					current=cell.getContents();
					referance=cellc.getContents();
					//System.out.println("referance   "+i +" " +referance);
					Str2=referance;
					 Str2= Str2.replaceAll(" ", "");
					if(!referance.equalsIgnoreCase(idvalue)&&flag==1&&Str2.length()>0)
					{
						flag=0;
					}
					if(referance.equalsIgnoreCase(idvalue)||flag==1)
					{  //=deletedvalue+','+Integer.toString(j);// for removing data from sheets.
						
						deletedvalue.add(j);
						flag=1; 
					d.add(cell.getContents());
					/*if(referance.equalsIgnoreCase(idvalue))
					{
						flag=0;
					}*/
						}
					}
					if(referance.equalsIgnoreCase(idvalue)||flag==1)
							{
					d.add("\n");
					mastersheetData.add(d);
							}
					}
					}
					catch (Exception e1) {
					e1.printStackTrace();
					}
				
				
				
				Table.setModel(model112);
				Table.setAutoCreateRowSorter(true);
				model112 = new DefaultTableModel(mastersheetData, headers){
					public boolean isCellEditable(int row, int col) {
						if (col == 4 || col == 7 ||col==8 ||row>0 ) {
						           
						            return false;
						        } else {
						        	 return true;
						        }       
					}
				

				
			
				};
				Table.setModel(model112);
				
				TableColumnModel colModel = Table.getColumnModel();
				changeColumnsWidth(colModel);
				
				lblUpdate.setVisible(false);
				
			
			
			}
			
		});//End of Search.
		
		
		
		
		
	

	
		
		
		
		JButton btnUpdate = new JButton("Update Test Case");
		
		btnUpdate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			try {	Utilities utilities = new Utilities();
				testCaseFile = utilities.getExcelFile("testfile");
				Workbook workbook = Workbook.getWorkbook(testCaseFile);
				WritableWorkbook copy = Workbook.createWorkbook(testCaseFile, workbook);
				WritableSheet sheet1 = copy.getSheet(0);
				TableModel model = Table.getModel();
				int size = sheet1.getRows();
				for (int i = 0; i < model.getRowCount(); i++) {
					size = sheet1.getRows();
					for (int j = 0; j < model.getColumnCount(); j++) {

						Label row = new Label(j, size, model.getValueAt(i, j).toString());

						sheet1.addCell(row);
					}
				}

				copy.write();
				copy.close();
				workbook.close();
				
				
				

			} catch (Exception e1) {

				e1.printStackTrace();
			}
			
			
			
			
			
			
			
			
			
			
			
			// for removing the row from testeb.xls     .
			Workbook workbook = null;
			
				try {
					workbook = Workbook.getWorkbook(testCaseFile);
				} catch (BiffException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				} catch (IOException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
			
			WritableWorkbook copy = null;
			try {
				copy = Workbook.createWorkbook(testCaseFile, workbook);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
			
			WritableSheet sheet2 = copy.getSheet(0);
		
			int q=0;
			HashSet<Integer> uniqueValues = new HashSet<>(deletedvalue);
			for (Integer value : uniqueValues) {
			 
			   
			   	sheet2.removeRow(value-q);
			   	q++;
			  // 	System.out.println("      Value is"  +value);
			}
					
			
			try {
				copy.write();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			workbook.close();
			try {
				copy.close();
			} catch (WriteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}//end of code for removing the row
			
			
	
			
			
	
			lblUpdate.setVisible(true);
			
			
			//clear the table			
						
			}
			
			
			
		});
		
		btnUpdate.setBounds(400, 600, 170, 33);
		panelEdit.add(btnUpdate);
		
		
		//delete step will delete a particular row that is selected from the jtable.
		JButton btnDeleteStep= new JButton("Delete Step");
		panelEdit.add(btnDeleteStep);
		btnDeleteStep.setBounds(1300, 600, 170, 33);
		btnDeleteStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel deletemodel = new DefaultTableModel();
				
				deletemodel = (DefaultTableModel) Table.getModel();
		
					
				deletemodel.removeRow(Table.getSelectedRow());
			
			
			}
		});
		
		
		
		textDescription = new JTextField();
		panelEdit.add(textDescription);
		textDescription.setBounds(1250, 100, 260, 33);
		
		
		lblTestDescription = new JLabel("Enter TestDescription:");
		panelEdit.add(lblTestDescription);
		lblTestDescription.setBounds(1100, 100, 190, 33);
	
		//add step button is used add the action and description from the actions combobox and textdecription fields respectivley
		btnAddStep = new JButton("Add Step");
		panelEdit.add(btnAddStep);
		btnAddStep.setBounds(1100, 600, 170, 33);
		Object[] row = new Object[10];
			
		btnAddStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		
				 if (Table.getRowCount() > 0) {
					row[0] = "";
					row[1] = "";
					row[2] = "";
					row[3] = "";
					row[4] = "";
					row[5] = "";
					row[6] = "";
					row[7] = textDescription.getText();
					row[8] = comboActions.getSelectedItem();
					row[9] = "";
					model112.addRow(row);

				}
			
				
				 //storing the parameter table data in hashmap
				int nRow = paramodel.getRowCount(), nCol = paramodel.getColumnCount();

				for (int i = 0; i < nRow - 1; i++) {
					for (int j = 0; j < nCol; j += 2) {

						mapValues.put(paramodel.getValueAt(i, j).toString(), paramodel.getValueAt(i, j + 1).toString());
						System.out.println("mapvalues " +mapValues);

					}

				}
				
				textDescription.setText("");
				
				
				
				
				
			
		
				//ffor removing parameters
				// for removing the row from testeb.xls     .
							Workbook workbook1 = null;
							
								try {
									workbook1 = Workbook.getWorkbook(parameterFile);
								
							
							WritableWorkbook copy1 = Workbook.createWorkbook(parameterFile, workbook1);
							
							
							WritableSheet sheet3 = copy1.getSheet(0);
						
							
							
							//
							HashSet<String> uniqueValues2 = new HashSet<>(deletedvalue1);
							Iterator<String> it1 = uniqueValues2.iterator();
						     while(it1.hasNext()){
						    	 System.out.println("ak delete valuese   "+it1.next() );
						     }
							
							
							HashSet<String> uniqueValues1 = new HashSet<>(deletedvalue1);
							Iterator<String> it = uniqueValues1.iterator();
						     while(it.hasNext()){
						    	 String buff=it.next();
						    //	 System.out.println("ak delete valuese   "+it.next() );
						       
						     
							for (int b = 0; b < sheet3.getRows(); b++) {
								//size = sheet3.getRows();
								
								Cell cell = sheet3.getCell(0,b);
							
							
								System.out.println("ak col 1   "+cell.getContents() );
								
							
								Cell cellc1 = sheet3.getCell(1,b);
								//System.out.println("ak col 2   "+cellc1.getContents() );
								if(idvalue.equalsIgnoreCase(cell.getContents())&& cellc1.getContents().equalsIgnoreCase(buff))
								{
									sheet3.removeRow(b);
								}
							
								
							}
						     }
							
							
							
							
								copy1.write();
								
						
							workbook1.close();
							
								copy1.close();
							} catch (WriteException | BiffException | IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} 
				
				
				
				
				// end of paramerter delete values
				
				
				
				
				
				
				
				try {
					parameterFile = utilities.getExcelFile("parameterFile");
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Workbook workbook2 = null;
				try {
					workbook2 = Workbook.getWorkbook(parameterFile);
				} catch (BiffException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				WritableWorkbook copy2 = null;
				try {
					copy2 = Workbook.createWorkbook(parameterFile, workbook2);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				WritableSheet sheet2 = copy2.getSheet(0);

				Set<String> keys = mapValues.keySet();
				Label cell;
				for (String key : keys) {
					int size1 = sheet2.getRows();

					cell = new Label(0, size1, idvalue);
					try {
						sheet2.addCell(cell);
					} catch (WriteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					cell = new Label(1, size1, key);
					try {
						sheet2.addCell(cell);
					} catch (RowsExceededException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (WriteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					cell = new Label(2, size1, mapValues.get(key));
					try {
						sheet2.addCell(cell);
					} catch (RowsExceededException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (WriteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
				try {
					copy2.write();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					copy2.close();
				} catch (WriteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				workbook2.close();
				mapValues.clear();

			}

		});	//End of add step
	
	
	
	
	
	}
	static void changeColumnsWidth(TableColumnModel colModel) {
		colModel.getColumn(0).setPreferredWidth(55);
		colModel.getColumn(2).setPreferredWidth(55);
		colModel.getColumn(3).setPreferredWidth(55);
		colModel.getColumn(5).setPreferredWidth(125);
		colModel.getColumn(6).setPreferredWidth(125);
		colModel.getColumn(7).setPreferredWidth(235);
		colModel.getColumn(8).setPreferredWidth(200);
	}

	

	static void populateTable(int row) {
		try {
			Utilities utilities = new Utilities();
			// Get parameters from ForTestGeneratorFile

			actionLibraryFile = utilities.getExcelFile("actionLibraryFile");
			Workbook workbook = null;
			deletedvalue1.clear();
			ArrayList<String> temp = new ArrayList<String>();
			workbook = Workbook.getWorkbook(actionLibraryFile);
			Sheet sheet = workbook.getSheet(0);
			for (int j = 2; j < sheet.getColumns(); j++) {
				Cell cell = sheet.getCell(j, row);
				if (cell.getContents() != null && cell.getContents().length() != 0)
					temp.add(cell.getContents());
				deletedvalue1.add(cell.getContents());
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
				//idvalue.equalsIgnoreCase(arg0)
			}

			for (int r = 0; r < parametersValues.length; r++) {
				dataValues[r][1] = parametersValues[r];
			}

			data = dataValues;
		} catch (Exception e) {

		}

	}
	
	
	
	

	
	
	


}
