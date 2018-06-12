package com.testconfig;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class TestConfig extends JFrame {
	private File configFile = new File("src/com/conf/env/config.properties");
	private Properties configProps;
	
	private JLabel labelHost = new JLabel("Project Conf File Path: ");
	private JLabel labelPort = new JLabel("Port number: ");
	private JLabel labelUser = new JLabel("Username: ");
	private JLabel labelPass = new JLabel("Password: ");
	
	private JTextField textHost = new JTextField(20);
	private JTextField textPort = new JTextField(20);
	private JTextField textUser = new JTextField(20);
	private JTextField textPass = new JTextField(20);
	
	private JButton buttonSave = new JButton("Save");
	
	public TestConfig() {
		super("KWDF Configuration Test");
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new Insets(10, 10, 5, 10);
		constraints.anchor = GridBagConstraints.WEST;
		
		add(labelHost, constraints);
		
		constraints.gridx = 1;
		add(textHost, constraints);
		
		constraints.gridy = 1;
		constraints.gridx = 0;
		add(labelPort, constraints);
		
		constraints.gridx = 1;
		add(textPort, constraints);

		constraints.gridy = 2;
		constraints.gridx = 0;
		add(labelUser, constraints);
		
		constraints.gridx = 1;
		add(textUser, constraints);

		constraints.gridy = 3;
		constraints.gridx = 0;
		add(labelPass, constraints);
		
		constraints.gridx = 1;
		add(textPass, constraints);
		
		constraints.gridy = 4;
		constraints.gridx = 0;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		add(buttonSave, constraints);
		
		buttonSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					saveProperties();
					JOptionPane.showMessageDialog(TestConfig.this, 
							"Properties were saved successfully!");		
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(TestConfig.this, 
							"Error saving properties file: " + ex.getMessage());		
				}
			}
		});
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
		try {
			loadProperties();
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, "The config.properties file does not exist, default properties loaded.");
		}
		textHost.setText(configProps.getProperty("host"));
		textPort.setText(configProps.getProperty("port"));
		textUser.setText(configProps.getProperty("user"));
		textPass.setText(configProps.getProperty("pass"));
	}

	private void loadProperties() throws IOException {
		Properties defaultProps = new Properties();
		// sets default properties
		defaultProps.setProperty("host", "https://192.168.53.184:8443/webui/login");
		defaultProps.setProperty("port", "8843");
		defaultProps.setProperty("user", "superadmin");
		defaultProps.setProperty("pass", "changeit");
		
		configProps = new Properties(defaultProps);
		
		// loads properties from file
		InputStream inputStream = new FileInputStream(configFile);
		configProps.load(inputStream);
		inputStream.close();
	}
	
	private void saveProperties() throws IOException {
		configProps.setProperty("host", textHost.getText());
		configProps.setProperty("port", textPort.getText());
		configProps.setProperty("user", textUser.getText());
		configProps.setProperty("pass", textPass.getText());
		OutputStream outputStream = new FileOutputStream(configFile);
		configProps.store(outputStream, "host setttings");
		outputStream.close();
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new TestConfig();
			}
		});
	}
}