package com.kat.test;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.kat.base.BaseClass;
import com.kat.lib.Library;
import com.kat.util.TestResultListener;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@Listeners(TestResultListener.class)
/*
 * Test_KP190 class is a test class which validates the API end-point "hello" response along with API
 *  contract scenarios like Status code, Status line, Server type, Content type, Encoding and response time
 * @Common Class BaseClass, Library, ExcelSheetUtility,  TestResultListener and TestNG framework & features
 * @Common Methods startTest() endTest()
 * @Functional Test case TC001_KP190_HelloAPI_VerifyInputMessage
 * @author Shiv.Singh
 * @date 14th October 2020
 * 
 */
public class Test_KP190 extends BaseClass {
	// Variable declaration
	static Library lib = new Library();
	Response response=null;
	String sheetName="KP190";
	String queryParam = "";
	String inputMessageHeader;
	String inputMessage="", expectedInputMessage;
	String actualInputMessage;
	String endpointURL;
	String endPointName;
	int statusCode,expectedStatusCode;
	String statusLine,expectedStatusLine;
	String contentEncoding,expectedContentEncoding;
	String contentType, expectedContentType;
	String serverType, expectedServerType;
	@BeforeTest
	public void startTest() {
		// Get the values from test data sheet	
		lib.logInfo("Fetching endpoint url..");
		endpointURL = excelUtil.getCellData(sheetName, 1, 1);
		endPointName = excelUtil.getCellData(sheetName, 1, 2);
		inputMessageHeader = excelUtil.getCellData(sheetName, 0, 3);
		inputMessage = excelUtil.getCellData(sheetName, 1, 3);
		expectedStatusCode=200;
		expectedStatusLine="HTTP/1.1 200 OK";
		expectedContentEncoding="gzip";
		expectedContentType="application/json; charset=utf-8";
		expectedServerType="nginx";
		// Check if input message is null/ blank
		if (inputMessage == null || inputMessage.equals("")) {
			queryParam = endPointName;
		} else {
			queryParam = endPointName + "?" + inputMessageHeader + "=" + inputMessage;
		}
		// Final query parameter
		queryParam ="/" + queryParam;
		// Assign the base URI
		lib.logInfo("Configuring base URI..");
		RestAssured.baseURI = endpointURL;
		// Create a Request 
		RequestSpecification httpRequest = RestAssured.given();
		// Get the End point response
		lib.logInfo("Fetching API endpoint response..");
		response = httpRequest.get(queryParam);
	}

	/*
	 * This test case is validating the input message and response returned by the
	 * API end point.
	 * 
	 * @input param Input message
	 * 
	 * @output param Response returned
	 */
	@Test
	public void TC001_KP190_VerifyInputMessage() {
		// Get the Status code
		statusCode = response.getStatusCode();
		if (statusCode == expectedStatusCode) {
			lib.logPassed("Status Code- " + statusCode);
		} else {
			lib.logFailed("Status Code- " + statusCode);
		}
		// Get the Status Line
		statusLine = response.getStatusLine();
		if (statusLine.equals(expectedStatusLine)) {
			lib.logPassed("Status Line- " + statusLine);
		} else {
			lib.logFailed("Status Line- " + statusLine);
		}
		// Get the Headers detail
		Headers allHeaders = response.headers();
		// Show all the Headers in Key value
		for (Header header : allHeaders) {
			lib.logPassed("Key: " + header.getName() + " Value: " + header.getValue());
		}
		// Header named Content-Type
		contentType = response.header("Content-Type");
		Assert.assertEquals(contentType, expectedContentType);
		lib.logPassed("Content Type- " + contentType);
		// Header named Server
		serverType = response.header("Server");
		Assert.assertEquals(serverType, expectedServerType);
		lib.logPassed("Server Type- " + serverType);
		
		// Header named Content-Encoding
		contentEncoding = response.header("Content-Encoding");
		Assert.assertEquals(contentEncoding, expectedContentEncoding);
		lib.logPassed("Content-Encoding: " + contentEncoding);
		
		// Show the response Time
		long responseTime = response.getTime();
		lib.logPassed("ResponseTime- " + responseTime);
		
		// Verify Input Message
		expectedInputMessage = inputMessage;
		JsonPath jsonPathEvaluator = response.jsonPath();		
		actualInputMessage = jsonPathEvaluator.get("output");		
		Assert.assertEquals(actualInputMessage, expectedInputMessage);
		lib.logPassed("Input Message - " + actualInputMessage);
	}

	@AfterTest
	public void endTest() {
		lib.logInfo("Test_KP190 end Test.");

	}

}
