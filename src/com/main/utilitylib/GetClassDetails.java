package com.main.utilitylib;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

public class GetClassDetails {
	int x;
	int y;
	Utilities serve = new Utilities();
	String testFolderName;
	public Dictionary<String, String> classDetailsDictionary = new Hashtable<String, String>();
	public List actionList = new ArrayList();
	public String mName = null;
	public String mRetunType = null;
	public int mArgCount;
	public String className = null;
	public String ramClassName = null;

	public List actionClassNameLst = new ArrayList();

	public List listFilesAndFolders(String directoryName) {
		File directory = new File(directoryName);
		// get all the files from a directory
		File[] fList = directory.listFiles();
		int i = 0;
		for (File file : fList) {
			String currentTestName = file.getName().replace(".java", "");
			actionClassNameLst.add(currentTestName);
		}

		return actionClassNameLst;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {

		this.className = className;
	}

	public void getAttributes() {
		Field[] Attributes = this.getClass().getDeclaredFields();
		for (int i = 0; i < Attributes.length; i++) {
			System.out.println("Declared Fields " + Attributes[i]);
		}
	}

	public void getClassName(Object abc) {
		String className = abc.getClass().getSimpleName();
		// System.out.println("Name:"+className);
	}

	public void getAttributes(Object abc) {
		Field[] Attributes = abc.getClass().getDeclaredFields();
		for (int i = 0; i < Attributes.length; i++) {
			// System.out.println("Declared Fields "+Attributes[i]);
		}
	}

	public Method[] getMethodNames(Object abc) {
		// CallMethodName test = new CallMethodName();
		Method[] method = abc.getClass().getMethods();
		return method;
	}

	public List getActionClasses() throws IOException {
		testFolderName = serve.getProperties("action_folder_path").toString();
		// System.out.println("In Get Action Class Method");
		return listFilesAndFolders(testFolderName);

	}

	public void addObjectLst(Object obj) {
		String key = null, value = null;
		StringTokenizer st = null;
		int count = 0;

		for (Method m : obj.getClass().getMethods()) {
			key = m.getName();
			// instance.getClass().getName();
			// Class Name Method Name + Return Type + Arguement
			value = obj.getClass().getName() + "|" + m.getName() + "|" + m.getReturnType() + "|" + m.getParameterCount()
					+ "|" + m.getParameters();
			classDetailsDictionary.put(key, value);
		}

	}

	public String searchMethod(String methodName) {
		// TODO Auto-generated method stub
		for (int i = 0; i < classDetailsDictionary.size(); i++) {

			if (classDetailsDictionary.get(methodName) != null) {
				// System.out.println("CSV Method Name :: "+ methodName + "Class
				// ::" + classDetailsDictionary.get(methodName));

				String dictMethodName = getMethodName(classDetailsDictionary.get(methodName), methodName);
				if (methodName.equals(dictMethodName)) {
					// this.setRamClassName(dictMethodName.getClass().getName());
					break;
				}
			}
		}

		return classDetailsDictionary.get(methodName);

	}

	private String getMethodName(String dictValue, String methodName) {
		// TODO Auto-generated method stub

		// System.out.println("Actual Value with | >> "+ dictValue);
		StringTokenizer st = new StringTokenizer(dictValue);

		String dictMethodName = null;

		// keep this sop.
		// System.out.println("Next token is 1 : " + );
		this.setClassName(st.nextToken("|"));
		this.setmName(st.nextToken("|"));
		// this.setClassName(methodClass.get(getmName()));
		// this.setClassName();
		// Return Type
		this.setmRetunType(st.nextToken("|"));

		// Arguments
		mArgCount = Integer.parseInt(st.nextToken("|"));
		if (mArgCount != 0) {
			// actionList.add(st.nextToken("|"));
			// actionList.add(st.nextToken("|"));
		}
		return dictMethodName;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getmRetunType() {
		/*
		 * StringTokenizer st2 = new StringTokenizer(mRetunType);
		 * if(mRetunType!=null){ System.out.println("Class Name " +
		 * st2.nextToken(" ")); mRetunType = st2.nextToken(" "); }
		 */
		return mRetunType;
	}

	public void setmRetunType(String mRetunType) {
		this.mRetunType = mRetunType;
	}

	public int getmArgCount() {
		return mArgCount;
	}

	public void setmArgCount(int mArgCount) {
		this.mArgCount = mArgCount;
	}

}