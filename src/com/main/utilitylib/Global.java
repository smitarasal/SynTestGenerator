package com.main.utilitylib;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;

public class Global {
	public static int testcount = 0;
	public static int testatrow = 1; //first test is always at 2nd row of tests.xls file
	public static Sheet tests_sheet;
	public static int rowcount;
	public static boolean testresult = true;
	public static List<String> methods = new LinkedList<String>();
	public static File filename;
	public static Object[][] tableData ;
	public static List<String> TestCasesIDLst = new ArrayList<String>();
}
