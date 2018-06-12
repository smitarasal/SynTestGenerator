package com.testsuiterunner;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import com.main.utilitylib.Global;
import com.main.utilitylib.Utilities;

public class MyTransformer implements IAnnotationTransformer {
	public static int testcount = 0;
	InputStream tests;
	Workbook wb;
	Sheet testssheet;
	Utilities serve = new Utilities();

	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		try {
			if (Global.tableData != null) {
				testcount = Utilities.getUITestCount();
			} else {
				serve.getXlsTestsMapping(serve.getProperties("testfile"));
				testcount = serve.getTestCount();
			}
		} catch (IOException e) {
		}

	}

}
