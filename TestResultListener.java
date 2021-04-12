package com.kat.util;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.kat.lib.Library;

public class TestResultListener extends TestListenerAdapter {
	Library lib = new Library();

	@Override
	public void onTestStart(ITestResult result) {
		lib.logInfo(result.getName() + " is started.");
	}
	@Override
	public void onTestFailure(ITestResult result) {
		lib.logFailed(result.getName() + " is failed.");
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		lib.logPassed(result.getName() + " is passed.");
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		lib.logSkipped(result.getName() + " is skipped.");
	}	
	
	public void onTestException(ITestResult result) {
		lib.logInfo(result.getName() + " is failed with exception.");
	}
	public void onTestWarning(ITestResult result) {
		lib.logInfo(result.getName() + " is passed with warning.");
	}

}
