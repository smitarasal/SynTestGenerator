package com.main.utilitylib;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;

public class LoggerUtilities {
	InputStream input = null;
	String timestamp;
	File logdir;
	File logfile;
	File summarylogfile;
	FileWriter fw;
	FileWriter fwsummary;
	BufferedWriter bw;
	java.util.Date date;
	BufferedWriter logger = null;
	BufferedWriter logsummary = null;

	// get Time stamp
	public Timestamp getTimestamp() {
		date = new java.util.Date();
		return new Timestamp(date.getTime());
	}

	public File getLogFile() {
		return logfile;
	}

	// create log folder and log file if doesn't exists
	public File getLogFile(String filename) throws IOException {
		File logfile;
		File logdir;

		logdir = new File("log");
		// if log directory doesn't exists, then create it
		if (!logdir.exists())
			logdir.mkdir();
		logfile = new File(logdir, filename);
		// if log file doesn't exists, then create it
		if (!logfile.exists())
			logfile.createNewFile();

		return logfile;
	}

	// Log INFO
	public void logInfo(BufferedWriter bw, String info) throws IOException {
		System.out.println(getTimestamp() + "	INFO: " + info);
		bw.write(getTimestamp() + "	INFO: " + info + "\n");
	}

	// Log ERROR
	public void logError(BufferedWriter bw, String error) throws IOException {
		System.out.println(getTimestamp() + "	ERROR: " + error);
		bw.write(getTimestamp() + "	ERROR: " + error + "\n");
	}

	// print decoration string in log file
	public void print(BufferedWriter bw, String str) throws IOException {
		System.out.println(str);
		bw.write(str + "\n");
	}

	public void logSummary(String str) throws IOException {
		fwsummary = new FileWriter(getLogFile("LogDashboard.log").getAbsolutePath(), true);
		logsummary = new BufferedWriter(fwsummary);
		logsummary.write(getTimestamp() + "	" + str + "\n");
		logsummary.close();
	}

	public void printSummary(String str) throws IOException {
		fwsummary = new FileWriter(getLogFile("LogDashboard.log").getAbsolutePath(), true);
		logsummary = new BufferedWriter(fwsummary);
		logsummary.append(str + "\n");
		logsummary.close();
	}

	// Log and throw exception
	public Error lognThrow(BufferedWriter logger, Exception e) throws Exception {
		Error error;
		// log n print error
		System.out.println("\n" + e.getMessage());
		logError(logger, "\n" + e.getMessage());
		// throw error
		error = new Error(e.getMessage());
		error.setStackTrace(e.getStackTrace());

		Global.testresult = false;
		logError(logger, e.getMessage() + "\n" + "#XX# TEST FAILED!!! #XX#");
		logSummary(
				getTimestamp() + "	ERROR: " + e.getMessage() + "\n" + getTimestamp() + "	#XX# TEST FAILED!!! #XX#");

		return error;
	}

}
