package com.testsuiterunner;

public class TestDetail {

	public String testIsEnabled, testSuite, testModule, priority, testID, testName, testData, testStepDescription,
			testxlsActions, testPlatform;

	TestDetail(String ise, String S, String m, String P, String tId, String tn, String td, String tstepDesc,
			String taction, String tplatform) {
		testIsEnabled = ise;
		testSuite = S;
		testModule = m;
		if (Integer.parseInt(String.valueOf(P.charAt(0))) == 1) {
			priority = "P1";
		} else if (Integer.parseInt(String.valueOf(P.charAt(0))) == 2) {
			priority = "P2";
		} else if (Integer.parseInt(String.valueOf(P.charAt(0))) == 3) {
			priority = "P3";
		}
		testID = tId;
		testName = tn;
		testData = td;
		testStepDescription = tstepDesc;
		testxlsActions = taction;
		testPlatform = tplatform;
	}

	@Override
	public String toString() {
		return this.testIsEnabled + this.testSuite + this.testModule + this.priority + this.testID + this.testName
				+ this.testData + this.testStepDescription + this.testxlsActions + this.testPlatform;
	}

}
