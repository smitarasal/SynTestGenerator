//Hashmap
package com.testsuiterunner;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ActioTestGenerator extends JFrame {
	static JFrame mainFrame;

	static ImageIcon icon = new ImageIcon("syn-icon.png");

	static File testCaseFile = new File(
			"E:\\E- Drive\\Performance\\Automation\\WorkiQ_working77_v11\\testrepo\\testsuites\\web\\workiq\\tests_web.XLS");
	static File parameterFile = new File(
			"E:\\E- Drive\\Performance\\Automation\\WorkiQ_working77_v117\\testrepo\\testsuites\\web\\workiq\\ParameterFormat.xls");
	static File actionLibraryFile = new File(
			"E:\\E- Drive\\Performance\\Automation\\WorkiQ_working77_v11\\testrepo\\testsuites\\web\\workiq\\ActionLibrary.xls");
	Map<String, String> mapValues = new HashMap<String, String>();

	// All four panels in the UI
	static JPanel panelMenu;
	static JPanel panelCreateTests;
	static JPanel panelViewTests;
	static JPanel panelRunTests;

	// Controls to be displayed on panelMenu
	static JButton btnCreateTests;
	static JButton btnViewTests;
	static JButton btnRunTests;

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
		btnViewTests = new JButton("View Tests");
		btnRunTests = new JButton("Run Tests");
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
		lblSave.setForeground(Color.blue);

		comboActions = new JComboBox();

		String[] priorities = new String[] { "", "1", "2", "3" };
		comboPriority = new JComboBox(priorities);

		String[] isEnabledStatus = new String[] { "", "Y", "N" };
		comboIsEnabled = new JComboBox(isEnabledStatus);

		String[] testSuites = new String[] { "", "Smoke Suite", "Sanity Suite", "Regression" };
		comboTestSuite = new JComboBox(testSuites);

		String[] platforms = new String[] { "", "Web", "IOS", "Android" };
		comboPlatform = new JComboBox(platforms);

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

		parameterTable = new JTable();
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
		JPanel panelRunTests = new JPanel();
		panelMenu.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panelMenu.setBackground(SystemColor.inactiveCaptionBorder);

		mainFrame.getContentPane().add(panelMenu, "name_12027148937960");
		mainFrame.getContentPane().add(panelCreateTests, "name_497328569415918");
		mainFrame.getContentPane().add(panelRunTests, "name_497328600132521");
		mainFrame.getContentPane().add(panelViewTests, "name_12030013541046");

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

		// ***************************************************************************************
		panelMenu.setLayout(null);
		panelCreateTests.setLayout(null);
		panelRunTests.setLayout(null);

		panelMenu.setVisible(true);
		panelMenu.add(btnCreateTests);
		panelMenu.add(btnViewTests);
		panelMenu.add(btnRunTests);

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
		lblModule.setBounds(565, 88, 63, 33);
		lblTestCaseID.setBounds(253, 88, 102, 33);
		lblTestCaseName.setBounds(843, 39, 114, 14);
		lblTestData.setBounds(859, 88, 86, 33);
		lblTestDescription.setBounds(265, 242, 143, 33);
		lblTestSuite.setBounds(556, 30, 72, 33);
		lblPriority.setBounds(265, 30, 86, 33);
		lblPlatform.setBounds(9, 88, 102, 33);
		lblTableTitle.setBounds(29, 312, 361, 25);
		lblSave.setBounds(1174, 913, 375, 100);

		comboIsEnabled.setBounds(100, 30, 148, 33);
		comboActions.setBounds(100, 242, 143, 33);
		textModule.setBounds(648, 88, 171, 33);
		textTestCaseID.setBounds(361, 88, 165, 33);
		textTestCaseName.setBounds(984, 30, 200, 33);
		textTestData.setBounds(981, 88, 211, 33);
		textDescription.setBounds(393, 242, 211, 33);

		btnCreateTests.setBounds(565, 269, 116, 35);
		btnViewTests.setBounds(565, 339, 116, 35);
		btnRunTests.setBounds(565, 410, 116, 35);
		btnAddStep.setBounds(333, 307, 116, 35);
		btnDeleteStep.setBounds(459, 307, 116, 35);

		comboTestSuite.setBounds(648, 30, 171, 33);

		comboPriority.setBounds(361, 30, 165, 33);
		comboPlatform.setBounds(100, 88, 143, 33);

		btnCreateSaveTestButton.setBounds(1041, 611, 200, 33);
		btnBackToMain.setBounds(808, 615, 205, 25);
		scrollPaneParameters.setBounds(793, 291, 349, 275);
		scrollPaneMaster.setBounds(29, 348, 705, 315);

		JSeparator separator = new JSeparator();
		separator.setBounds(0, 207, 1340, 2);
		panelCreateTests.add(separator);

		separatorVertical = new JSeparator();
		separatorVertical.setOrientation(SwingConstants.VERTICAL);
		separatorVertical.setBounds(764, 207, 2, 682);
		panelCreateTests.add(separatorVertical);

		btnCreateSaveTestButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
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

					Workbook workbook2 = Workbook.getWorkbook(parameterFile);
					WritableWorkbook copy2 = Workbook.createWorkbook(parameterFile, workbook2);
					WritableSheet sheet2 = copy2.getSheet(0);

					Set<String> keys = mapValues.keySet();
					Label cell;
					for (String key : keys) {
						int size1 = sheet2.getRows();
						System.out.println("key = " + key + "value = " + mapValues.get(key));
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
					System.out.println("the values :" + mapValues);
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
				// boolean b = false;
				// comboActions.setEnabled(false);
				// textDescription.setEditable(b);
				// parameterTable.setEnabled(b);
				// masterTable.setEnabled(b);

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
				panelRunTests.setVisible(false);
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
				parameterTable.setModel(model);

			}
		});

		Object[] row = new Object[10];

		btnAddStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
					row[7] = "";
					row[8] = comboActions.getSelectedItem();
					row[9] = "";
					masterModel.addRow(row);

				}

				int nRow = model.getRowCount(), nCol = model.getColumnCount();

				for (int i = 0; i < nRow; i++) {
					for (int j = 0; j < nCol; j += 2) {
						mapValues.put(model.getValueAt(i, j).toString(), model.getValueAt(i, j + 1).toString());

					}

				}

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
		});

	}

	static void populateActions(JComboBox combo, File file, int columnNumber) {
		Workbook workbook = null;
		try {

			workbook = Workbook.getWorkbook(file);
			Sheet sheet = workbook.getSheet(0);
			actions.clear();
			for (int i = 1; i < sheet.getRows(); i++) {
				Cell cell = sheet.getCell(columnNumber, i);
				actions.add(cell.getContents());
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
			// Get parameters from ForTestGeneratorFile
			// File testGenFile = new
			// File("E:\\WorkiQ_2_HRMS\\WorkiQ_2_HRMS\\WorkiQ\\testrepo\\docs\\dictionaries\\ForTestGenerator.xls");
			Workbook workbook = null;
			ArrayList<String> temp = new ArrayList<String>();
			workbook = Workbook.getWorkbook(actionLibraryFile);
			Sheet sheet = workbook.getSheet(0);
			for (int j = 2; j < sheet.getColumns(); j++) {
				Cell cell = sheet.getCell(j, row);
				if (cell.getContents() != null && cell.getContents().length() != 0)
					temp.add(cell.getContents());
			}

			// File testFile = new
			// File("E:\\WorkiQ_2_HRMS\\WorkiQ_2_HRMS\\WorkiQ\\testrepo\\docs\\dictionaries\\Test.xls");
			Workbook workbook2 = null;
			workbook2 = Workbook.getWorkbook(parameterFile);
			Sheet sheet2 = workbook2.getSheet(0);
			ArrayList<String> paraValues = new ArrayList<String>();
			String str;
			Iterator<String> itr = temp.iterator();
			Cell cell1 = null, cell2 = null;

			while (itr.hasNext()) {
				str = itr.next();
				for (int row3 = 1; row3 < sheet2.getRows(); row3++) {

					cell1 = sheet2.getCell(1, row3);
					cell2 = sheet2.getCell(2, row3);
					if (!cell1.getContents().isEmpty()) {
						if (cell1.getContents().equalsIgnoreCase(str)) {
							paraValues.add((String) cell2.getContents());
						}
					} else {
						paraValues.add(" ");
						break;
					}
				} // end of for
			} // end of while

			String[] parametersArray = temp.toArray(new String[temp.size()]);
			String[] parametersValues = paraValues.toArray(new String[paraValues.size()]);
			dataValues = new String[parametersArray.length][2];

			for (int r = 0; r < parametersArray.length; r++)
				dataValues[r][0] = parametersArray[r];

			for (int r = 0; r < parametersValues.length; r++)
				dataValues[r][1] = parametersValues[r];

			data = dataValues;
		} catch (Exception e) {
			System.out.println("In populateTable : Exception " + e.getMessage());
		}

	}

	public static void main(String args[]) {
		try {
			ActioTestGenerator obj3 = new ActioTestGenerator();
			mainFrame.setVisible(true);

		} catch (Exception e) {
			System.out.println("Exception" + e.getMessage());

		}
	}

}
