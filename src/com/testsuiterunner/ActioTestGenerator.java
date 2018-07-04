//Hashmap
package com.testsuiterunner;

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

public class ActioTestGenerator extends JFrame {
	static JFrame mainFrame;

	static ImageIcon icon = new ImageIcon("syn-icon.png");
	Utilities utilities = new Utilities();
	Map<String, String> mapValues = new HashMap<String, String>();
	static File testCaseFile = null;
	static File parameterFile = null;
	static File actionLibraryFile = null;
	// All four panels in the UI
	static JPanel panelMenu;
	static JPanel panelCreateTests;
	static JPanel panelViewTests;
	static JPanel panelEditTests;

	// Controls to be displayed on panelMenu
	static JButton btnCreateTests;
	static JButton btnViewTests;
	static JButton btnEditTests;

	// Controls to be displayed on panelCreateTests [Create Tests Panel]
	static JLabel lblisEnabled;
	static JLabel lblTestSuite;
	static JLabel lblActions;
	static JLabel lblPriority;
	static JLabel lblTestCaseID;
	static JLabel lblTestCaseName;
	static JLabel lblTestDescription;
	static JLabel lblTestData;
	static JLabel lblPlatform;
	static JLabel lblTableTitle;
	static JLabel lblModule;
	static JLabel lblSave;

	static JComboBox comboActions;
	static JComboBox comboTestSuite;
	static JComboBox comboIsEnabled;
	static JComboBox comboPriority;
	static JComboBox comboPlatform;

	static JTextField textTestCaseID;
	static JTextField textTestCaseName;
	static JTextField textDescription;
	static JTextField textModule;
	static JTextField textTestData;
	static JTextField textTestSuite;

	static JButton btnCreateSaveTestButton;
	static JButton btnBackToMain;
	static JButton btnAddStep;
	static JButton btnDeleteStep;

	private JTable parameterTable;
	private JScrollPane scrollPaneParameters;
	String[] columnNames = { "Parameters", "Values" };
	static DefaultTableModel model = null;

	private JTable masterTable = new JTable();
	private JScrollPane scrollPaneMaster = new JScrollPane(masterTable);
	static DefaultTableModel masterModel = new DefaultTableModel();
	static Object[] masterColumns = { "IsEnabled", "Test Suite", "Module", "Priority", "Test ID", "Test Name",
			"	Test Data", "Test Step Description", "Actions", "Platform" };
	static Object[][] masterData = new Object[][] { { "", "", "", "", "", "", "", "", "", "" } };

	static Object[][] data = null;

	private ArrayList<String> parametersCol;
	private ArrayList<String> parametersValues;
	private static String[][] dataValues;

	// static File file;
	static Vector actions = new Vector();
	static Vector parameters = new Vector();
	private JSeparator separator_1;

	private JSeparator separatorVertical;

	ActioTestGenerator() {
		initialize();
	}

	public void initialize() {

		mainFrame = new JFrame();
		// mainFrame.setSize(1000, 1000);

		btnCreateTests = new JButton("Create Tests");
		btnEditTests = new JButton(" Edit Tests");
		btnViewTests = new JButton("View Tests");
		btnAddStep = new JButton("Add Step");

		lblisEnabled = new JLabel("Is Enabled :");
		lblTestSuite = new JLabel("Test Suites :");
		lblActions = new JLabel("Select Actions :");
		lblPriority = new JLabel("Select Priority :");
		lblTestCaseID = new JLabel("Enter Test Case ID :");
		lblTestDescription = new JLabel("Enter Test Description :");
		lblTableTitle = new JLabel("Your Test Case With Steps With Parameter");
		lblModule = new JLabel("Module :");
		lblTestCaseName = new JLabel("Enter Test Case Name :");
		lblTestData = new JLabel("Enter Test Data :");
		lblPlatform = new JLabel("Enter Platform :");
		lblSave = new JLabel("Saved The TestCase Successfully :");
		lblSave.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblSave.setForeground(Color.blue);

		comboActions = new JComboBox();

		String[] priorities = new String[] { "", "1", "2", "3" };
		JComboBox<Object> comboPriority = new JComboBox<Object>(priorities);

		String[] isEnabledStatus = new String[] { "", "Y", "N" };
		JComboBox<Object> comboIsEnabled = new JComboBox<Object>(isEnabledStatus);

		String[] testSuites = new String[] { "", "Smoke Suite", "Sanity Suite", "Regression" };
		JComboBox<Object> comboTestSuite = new JComboBox<Object>(testSuites);

		String[] platforms = new String[] { "", "Web", "IOS", "Android", "IOSBrowser", "API", "Android Browser" };
		JComboBox<Object> comboPlatform = new JComboBox<Object>(platforms);

		textTestCaseID = new JTextField();
		textDescription = new JTextField();
		textTestCaseName = new JTextField();
		textModule = new JTextField();
		textTestData = new JTextField();
		textTestSuite = new JTextField();
		btnCreateSaveTestButton = new JButton("Create and Save Test");
		btnBackToMain = new JButton("Back to Main Screen");
		btnDeleteStep = new JButton("Delete Step");

		comboActions.addItem("");

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
		populateActions(comboActions, actionLibraryFile, 0);

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

		masterModel.setColumnIdentifiers(masterColumns);
		masterTable.setModel(masterModel);

		// ***************************************************************************************
		mainFrame.setTitle("Actio - Test Generator ");
		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		mainFrame.setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
				(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(new CardLayout(0, 0));
		ImageIcon img = new ImageIcon("syn-icon.png");
		mainFrame.setIconImage(img.getImage());
		JPanel panelMenu = new JPanel();
		JPanel panelCreateTests = new JPanel();
		JPanel panelViewTests = new JPanel();
		JPanel panelEditTests = new JPanel();
		JPanel panelEdit = new JPanel();
		panelMenu.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panelMenu.setBackground(SystemColor.inactiveCaptionBorder);

		mainFrame.getContentPane().add(panelMenu, "name_12027148937960");
		mainFrame.getContentPane().add(panelCreateTests, "name_497328569415918");
		mainFrame.getContentPane().add(panelEditTests, "name_497328600132521");
		mainFrame.getContentPane().add(panelViewTests, "name_12030013541046");
		mainFrame.getContentPane().add(panelEdit, "");

		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {

		}

		ActioTestGenerator self = this;
		btnViewTests.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SelectionDetails.ViewButtonCode(self, panelMenu, panelViewTests);
				panelMenu.setVisible(false);
				panelViewTests.setVisible(true);

			}
		});

		// #########################################EditPAge#####################################################

		btnEditTests.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// EditPage.EditButton(self, panelMenu, panelEdit);
				EditDetails.EditButtonCode(self, panelMenu, panelEdit);
				panelMenu.setVisible(false);
				panelEdit.setVisible(true);

			}
		});
		// ***************************************************************************************
		panelMenu.setLayout(null);
		panelCreateTests.setLayout(null);
		panelEditTests.setLayout(null);
		panelEdit.setLayout(null);

		panelMenu.setVisible(true);
		panelMenu.add(btnCreateTests);
		panelMenu.add(btnViewTests);
		panelMenu.add(btnEditTests);

		panelCreateTests.add(lblisEnabled);
		panelCreateTests.add(lblTestSuite);
		panelCreateTests.add(lblActions);
		panelCreateTests.add(lblPriority);
		panelCreateTests.add(lblTestCaseID);
		panelCreateTests.add(lblTestDescription);
		panelCreateTests.add(lblTableTitle);
		panelCreateTests.add(lblModule);
		panelCreateTests.add(lblTestCaseName);
		panelCreateTests.add(comboIsEnabled);
		panelCreateTests.add(comboTestSuite);
		panelCreateTests.add(comboActions);
		panelCreateTests.add(comboPriority);
		panelCreateTests.add(textTestCaseID);
		panelCreateTests.add(textDescription);
		panelCreateTests.add(textModule);
		panelCreateTests.add(textTestCaseName);
		panelCreateTests.add(btnCreateSaveTestButton);

		panelCreateTests.add(btnBackToMain);
		panelCreateTests.add(btnAddStep);
		panelCreateTests.add(btnDeleteStep);
		panelCreateTests.add(lblPlatform);
		panelCreateTests.add(lblTestData);
		panelCreateTests.add(textTestData);
		panelCreateTests.add(comboPlatform);
		panelCreateTests.add(scrollPaneParameters, BorderLayout.CENTER);
		panelCreateTests.add(scrollPaneMaster, BorderLayout.CENTER);
		lblSave.setVisible(false);
		panelCreateTests.add(lblSave);

		lblisEnabled.setBounds(9, 30, 86, 33);
		lblActions.setBounds(9, 242, 86, 33);
		lblModule.setBounds(731, 88, 63, 33);
		lblTestCaseID.setBounds(347, 88, 143, 33);
		lblTestCaseName.setBounds(1047, 39, 164, 14);
		lblTestData.setBounds(1047, 88, 148, 33);
		lblTestDescription.setBounds(326, 242, 143, 33);
		lblTestSuite.setBounds(731, 30, 72, 33);
		lblPriority.setBounds(347, 30, 86, 33);
		lblPlatform.setBounds(9, 88, 102, 33);
		lblTableTitle.setBounds(29, 312, 361, 25);
		lblSave.setBounds(1099, 730, 286, 87);

		comboIsEnabled.setBounds(123, 30, 148, 33);
		comboActions.setBounds(123, 242, 148, 33);
		textModule.setBounds(842, 88, 171, 33);
		textTestCaseID.setBounds(500, 88, 143, 33);
		textTestCaseName.setBounds(1221, 30, 263, 33);
		textTestData.setBounds(1221, 88, 263, 33);
		textDescription.setBounds(473, 242, 439, 33);

		btnCreateTests.setBounds(565, 269, 116, 35);
		btnEditTests.setBounds(565, 339, 116, 35);
		btnViewTests.setBounds(565, 410, 116, 35);
		btnAddStep.setBounds(1097, 612, 116, 35);
		btnDeleteStep.setBounds(1263, 612, 116, 35);

		comboTestSuite.setBounds(842, 30, 171, 33);

		comboPriority.setBounds(500, 30, 143, 33);
		comboPlatform.setBounds(123, 88, 148, 33);

		btnCreateSaveTestButton.setBounds(579, 699, 200, 37);
		btnBackToMain.setBounds(206, 696, 205, 42);
		scrollPaneParameters.setBounds(1099, 256, 349, 324);
		scrollPaneMaster.setBounds(29, 348, 938, 315);

		JSeparator separator = new JSeparator();
		separator.setBounds(0, 207, 1850, 2);
		panelCreateTests.add(separator);

		separatorVertical = new JSeparator();
		separatorVertical.setOrientation(SwingConstants.VERTICAL);
		separatorVertical.setBounds(1011, 214, 2, 575);
		panelCreateTests.add(separatorVertical);

		btnCreateSaveTestButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Utilities utilities = new Utilities();
					testCaseFile = utilities.getExcelFile("testfile");
					Workbook workbook = Workbook.getWorkbook(testCaseFile);
					WritableWorkbook copy = Workbook.createWorkbook(testCaseFile, workbook);
					WritableSheet sheet1 = copy.getSheet(0);
					TableModel model = masterTable.getModel();
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
					parameterFile = utilities.getExcelFile("parameterFile");
					Workbook workbook2 = Workbook.getWorkbook(parameterFile);
					WritableWorkbook copy2 = Workbook.createWorkbook(parameterFile, workbook2);
					WritableSheet sheet2 = copy2.getSheet(0);

					Set<String> keys = mapValues.keySet();
					Label cell;
					for (String key : keys) {
						int size1 = sheet2.getRows();

						cell = new Label(0, size1, textTestCaseID.getText());
						sheet2.addCell(cell);
						cell = new Label(1, size1, key);
						sheet2.addCell(cell);
						cell = new Label(2, size1, mapValues.get(key));
						sheet2.addCell(cell);

					}
					copy2.write();
					copy2.close();
					workbook2.close();
					mapValues.clear();

				} catch (Exception e1) {

					e1.printStackTrace();
				}
				// after saving the test case, clear the fields and enable them
				JOptionPane.showMessageDialog(mainFrame.getComponent(0), "Saved SuccessFully.");

				comboActions.setSelectedIndex(0);
				comboIsEnabled.setSelectedIndex(0);
				comboPriority.setSelectedIndex(0);
				comboPlatform.setSelectedIndex(0);
				textModule.setText("");
				textTestCaseID.setText("");
				textTestCaseName.setText("");
				textTestData.setText("");
				textTestSuite.setText("");
				textDescription.setText("");

				comboIsEnabled.setEnabled(true);
				comboPriority.setEnabled(true);
				comboPlatform.setEnabled(true);
				boolean b = true;
				textModule.setEditable(b);
				textTestCaseID.setEditable(b);
				textTestCaseName.setEditable(b);
				textTestData.setEditable(b);
				textTestSuite.setEditable(b);
				comboActions.setEnabled(true);
				textDescription.setEditable(b);
				parameterTable.setEnabled(b);
				masterTable.setEnabled(b);

				// clearing the table contents when user re-navigates to the
				// create page.
				DefaultTableModel model = (DefaultTableModel) parameterTable.getModel();
				while (model.getRowCount() > 0) {
					for (int i = 0; i < model.getRowCount(); i++) {
						model.removeRow(i);
					}
				}

				DefaultTableModel model2 = (DefaultTableModel) masterTable.getModel();
				while (model2.getRowCount() > 0) {
					for (int i = 0; i < model2.getRowCount(); i++) {
						model2.removeRow(i);
					}
				}

				lblSave.setVisible(true);

			}
		});

		btnCreateTests.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelCreateTests.setVisible(true);
				panelMenu.setVisible(false);
			}
		});

		btnDeleteStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model = new DefaultTableModel();
				model = (DefaultTableModel) masterTable.getModel();
				model.removeRow(masterTable.getSelectedRow());
			}
		});

		btnBackToMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelViewTests.setVisible(false);
				panelCreateTests.setVisible(false);
				panelEditTests.setVisible(false);
				panelMenu.setVisible(true);

				/// set all the fields enabled and clear the same.
				comboIsEnabled.setEnabled(true);
				comboPriority.setEnabled(true);
				comboPlatform.setEnabled(true);
				boolean b = true;
				textModule.setEditable(b);
				textTestCaseID.setEditable(b);
				textTestCaseName.setEditable(b);
				textTestData.setEditable(b);
				textTestSuite.setEditable(b);
				comboActions.setEnabled(true);
				textDescription.setEditable(b);
				parameterTable.setEnabled(b);
				masterTable.setEnabled(b);

				comboActions.setSelectedIndex(0);
				comboIsEnabled.setSelectedIndex(0);
				comboPriority.setSelectedIndex(0);
				comboPlatform.setSelectedIndex(0);
				textModule.setText("");
				textTestCaseID.setText("");
				textTestCaseName.setText("");
				textTestData.setText("");
				textTestSuite.setText("");
				textDescription.setText("");
				lblSave.setVisible(false);
				// clearing the table contents when user re-navigates to the
				// create page.
				DefaultTableModel model = (DefaultTableModel) parameterTable.getModel();
				while (model.getRowCount() > 0) {
					for (int i = 0; i < model.getRowCount(); i++) {
						model.removeRow(i);
					}
				}

				DefaultTableModel model2 = (DefaultTableModel) masterTable.getModel();
				while (model2.getRowCount() > 0) {
					for (int i = 0; i < model2.getRowCount(); i++) {
						model2.removeRow(i);
					}
				}

			}
		});

		comboActions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				populateTable(comboActions.getSelectedIndex());
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

				String[] str = { "Confirm", "" };
				parameterTable.setModel(model);
				model.addRow(str);

			}
		});

		comboIsEnabled.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				lblSave.setVisible(false);
			}
		});

		textDescription.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				if (textDescription.getText().trim().isEmpty()) {
					btnAddStep.setEnabled(false);
					return;
				}

				btnAddStep.setEnabled(true);
			}
		});

		Object[] row = new Object[10];

		btnAddStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String testcaseID = textTestCaseID.getText();

				boolean flag = false;

				if (testcaseID.equalsIgnoreCase("")) {
					JOptionPane.showMessageDialog(mainFrame.getComponent(0), "Please Enter TestCaseID!!!");
				} else {
					try {
						testCaseFile = utilities.getExcelFile("testfile");
						Workbook workbook = null;
						workbook = Workbook.getWorkbook(testCaseFile);
						Sheet sheet = workbook.getSheet(0);
						for (int i = 1; i < sheet.getRows(); i++) {
							Cell cell = sheet.getCell(4, i);
							if (cell.getContents().equalsIgnoreCase(testcaseID)
									&& (!cell.getContents().equalsIgnoreCase(""))) {
								flag = true;

								break;
							}
						}

					} catch (Exception e1) {
						e1.printStackTrace();
					}

					if (flag == true) {
						JOptionPane.showMessageDialog(mainFrame.getComponent(0), "TestCaseID Already Present!!!");
						textTestCaseID.setText("");
					} else {

						if (masterTable.getRowCount() == 0) {
							row[0] = comboIsEnabled.getSelectedItem();
							row[1] = comboTestSuite.getSelectedItem();
							row[2] = textModule.getText();
							row[3] = comboPriority.getSelectedItem();
							row[4] = textTestCaseID.getText();
							row[5] = textTestCaseName.getText();
							row[6] = textTestData.getText();
							row[7] = textDescription.getText();
							row[8] = comboActions.getSelectedItem();
							row[9] = comboPlatform.getSelectedItem();
							masterModel.addRow(row);

						} else if (masterTable.getRowCount() > 0) {
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
							masterModel.addRow(row);

						}

						int nRow = model.getRowCount(), nCol = model.getColumnCount();

						for (int i = 0; i < nRow - 1; i++) {
							for (int j = 0; j < nCol; j += 2) {

								mapValues.put(model.getValueAt(i, j).toString(), model.getValueAt(i, j + 1).toString());

							}

						}
						textDescription.setText("");
						comboIsEnabled.setEnabled(false);
						comboPriority.setEnabled(false);
						comboPlatform.setEnabled(false);
						boolean b = false;
						textModule.setEditable(b);
						textTestCaseID.setEditable(b);
						textTestCaseName.setEditable(b);
						textTestData.setEditable(b);
						textTestSuite.setEditable(b);
					}
				}
			}

		});

	}

	public void checkFieldsFull() {
		// TODO Auto-generated method stub

	}

	static void populateActions(JComboBox combo, File file, int columnNumber) {
		Workbook workbook = null;
		try {

			workbook = Workbook.getWorkbook(file);
			Sheet sheet = workbook.getSheet(0);
			actions.clear();
			ArrayList details = new ArrayList();
			for (int i = 1; i < sheet.getRows(); i++) {
				Cell cell = sheet.getCell(columnNumber, i);

				String con = cell.getContents();
				if (con != null && con.length() != 0 && cell.getContents().equalsIgnoreCase(con)) {

					System.out.print(con);
					System.out.print("|");
				} else {
					continue;
				}
				actions.add(con);
			}
			Iterator<String> itr = actions.iterator();
			while (itr.hasNext()) {
				combo.addItem(itr.next());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	} // end of fillColumn method

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

	public static void main(String args[]) {
		try {
			ActioTestGenerator obj3 = new ActioTestGenerator();
			mainFrame.setVisible(true);

		} catch (Exception e) {

		}
	}

}
