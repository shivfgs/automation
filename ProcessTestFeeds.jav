package com.ca.tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ca.base.BaseClass;
import com.ca.lib.ReadWriteExcel;
import com.ca.pages.MaintenanceHomePage;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ProcessTestFeeds extends BaseClass {
	MaintenanceHomePage maintenanceHomePage;
	ReadWriteExcel readFeedsExcel, writeFeedsExcel;
	static String inOptaFeedsPath = "\\src\\testdata\\inFeeds.xlsx";
	static String outOptaFeedsPath = "\\src\\testdata\\outoptafeeds.xlsx";
	String inSheetName;
	String outSheetName = "feeds";
	Document document;	
	@BeforeTest
	public void startTest() {
		System.out.println("Feeds extraction process started..");
		maintenanceHomePage = new MaintenanceHomePage(driver);
	}

	@Test
	public void GetProcessFeed() {
		readFeedsExcel = new ReadWriteExcel(inOptaFeedsPath);
		writeFeedsExcel = new ReadWriteExcel(outOptaFeedsPath);
		String xmlFeed = "";
		String feed_id = "";
		String feedType = "";
		String game_id = "";
		String gameDate="";
		String simulationDate="";
		String selectEnv = JOptionPane.showInputDialog("Enter Environment", "UAT");
		String gameDate1 = JOptionPane.showInputDialog("Game Date yyyy-mm-dd", "Default");
		String simulationDate1 = JOptionPane.showInputDialog("Simulation Date yyyy-mm-dd", "Default");
		
		String fixture_id = JOptionPane.showInputDialog("Enter Fixture Id", "47974");
		String delay_time = JOptionPane.showInputDialog("Enter time delay. Default 10 secs/feed T20", "6");
		inSheetName=fixture_id;
		int delayTime = Integer.parseInt(delay_time);
		double feedid;
		String overs = "";
		String timestamp = "";
		login();

		int rowCount = readFeedsExcel.getRowCount(inSheetName);
		for (int i = 790; i <= rowCount; i++) {
			try {

				feed_id = readFeedsExcel.getCellData(inSheetName, i, 0);
				maintenanceHomePage.FeedId.sendKeys(Keys.CONTROL + "a");
				maintenanceHomePage.FeedId.sendKeys(Keys.DELETE);
				cWait(1);
				maintenanceHomePage.FeedId.sendKeys(feed_id);
				maintenanceHomePage.FeedId.sendKeys(Keys.ENTER);
				cWait(1);
				maintenanceHomePage.RawFeedEditor.click();
				JavascriptExecutor js = (JavascriptExecutor) driver;
				Object monacoEditor = js.executeScript("return this.monaco.editor.getModels()[0].getValue();");
				xmlFeed = monacoEditor.toString();
				xmlFeed = xmlFeed.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
				if(!gameDate.equalsIgnoreCase("Default")) {
				//xmlFeed = xmlFeed.replace("game_date=\""+gameDate+"\"", "game_date=\""+simulationDate+"\"");
				xmlFeed = xmlFeed.replace("\""+gameDate1, "\""+simulationDate1);
				gameDate=gameDate1;				
				gameDate=gameDate.replace("-", "");
				simulationDate=simulationDate1;
				simulationDate=simulationDate.replace("-", "");
				xmlFeed = xmlFeed.replace("\""+gameDate, "\""+simulationDate);
				}
				game_id = getGameId(xmlFeed);
				feedType = readFeedsExcel.getCellData(inSheetName, i, 1);
				String strOutput = "";
				timestamp = " " + timeStamp();
				if (fixture_id.equalsIgnoreCase(game_id) || fixture_id.equalsIgnoreCase("All")) {
					// Save xml feed into text file matchId + FeedType+FeedId
					//String fileName = game_id + "_" + feedType + "_" + feed_id + ".txt";
					//saveLogs("\\src\\testdata\\feeds\\" + fileName, xmlFeed);
					if (feedType.equalsIgnoreCase("C2"))
						overs = "Overs " + getOvers(xmlFeed);		
					
					int statusCode = processOptaFeeds(selectEnv, xmlFeed);
					if (statusCode == 201) {
						strOutput = "Processed feed  Game Id " + game_id + " " + feedType + " " + overs;
					} else {
						strOutput = "Failed feed Game Id- " + game_id + " " + feedType + " " + overs;
					}
				} else {
					strOutput = "Skipped feed  Game Id " + game_id;
				}
				System.out.println(strOutput + timestamp);
				cWait(delayTime);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public String getGameId(String xmlFeed) {
		String game_id = null;
		String game_xpath = "//@game_id"; // "/BallSummary/BallDetail/@game_id";
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		if (xmlFeed.contains("Cricket::C3")) {
			game_xpath = "//@id";
		}

		try {
			db = dbf.newDocumentBuilder();

			document = db.parse(new InputSource(new StringReader(xmlFeed)));
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xp = xpf.newXPath();

		try {
			game_id = xp.evaluate(game_xpath, document.getDocumentElement());
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		return game_id;

	}

	public void login() {
		try {
			maintenanceHomePage.startApp(maintenanceHomePage.maintenanceAppUrl);
			JOptionPane.showMessageDialog(null, "Click SignIn to continoue");
			maintenanceHomePage.SignLink.click();
			// Complete the login steps manually
			String emailId = JOptionPane.showInputDialog("Email Id", "va_ssingh@australiancricket.onmicrosoft.com");
			maintenanceHomePage.Email.sendKeys(emailId);
			maintenanceHomePage.Email.sendKeys(Keys.ENTER);
			String password = JOptionPane.showInputDialog("Password", "Rest#2021");
			maintenanceHomePage.Password.sendKeys(password);
			maintenanceHomePage.Password.sendKeys(Keys.ENTER);
			String otp = JOptionPane.showInputDialog("Enter OTP", "123456");
			maintenanceHomePage.OTP.sendKeys(otp);
			maintenanceHomePage.Verify.submit();
			JOptionPane.showMessageDialog(null, "Click OK to Continoue");
		} catch (Exception ex) {

		}

	}

	public void cWait(int timeSeconds) {
		try {
			Thread.sleep(1000 * timeSeconds);
		} catch (InterruptedException e) {
			System.out.println(e.toString());
		}
	}

	private static int processOptaFeeds(String selectEnv, String optaFeed) {
		int statusCode = 0;
		String baseURI = "";
		String functionKey = "";
		String uatBaseUIR = "https://cauatfeedstorefunction.azurewebsites.net/api/OptaFeed";
		String uatFunctionKey = "O2NuDqWDq2wUacqaLa7eqD5rfj3fF0VixeO/UmytuYk9xhzGXxL75w==";
		String devBaseUIR = "https://cadevfeedstorefunction.azurewebsites.net/api/OptaFeed";
		String devFunctionKey = "38GgCRh7sUBKBOXyRu5JYrk7owowaX3HIK/8RB4f9df8UVaGXgWnVw==";

		try {
			if (selectEnv.equalsIgnoreCase("UAT")) {
				baseURI = uatBaseUIR;
				functionKey = uatFunctionKey;
			} else if (selectEnv.equalsIgnoreCase("DEV")) {
				baseURI = devBaseUIR;
				functionKey = devFunctionKey;
			} else {
				System.out.println("Selected Environment is not available.");
				return statusCode;
			}

			RestAssured.baseURI = baseURI;
			// Get the RequestSpecification of the request
			RequestSpecification httpRequest = RestAssured.given();
			// Add the request headers
			httpRequest.headers("contentType", "application/xml", "x-functions-key", functionKey);
			httpRequest.body(optaFeed);
			Response response = httpRequest.request(Method.POST);
			// Get the Status code and return the value
			statusCode = response.getStatusCode();
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		return statusCode;
	}

	public String getOvers(String feedText) {
		String overs = "0.0";
		Document doc = convertStringToXMLDocument(feedText);
		try {
			NodeList nodeList = doc.getElementsByTagName("Innings");
			NodeList inningList = nodeList.item(nodeList.getLength() - 1).getChildNodes();
			for (int y = 0, size1 = inningList.getLength(); y < size1; y++) {
				if (inningList.item(y).getNodeName().equalsIgnoreCase("Total")) {
					overs = inningList.item(y).getAttributes().getNamedItem("overs").getNodeValue();
					break;
				}
			}
		} catch (Exception ex) {
			// System.out.println("Node exception- " + ex.toString());
		}
		return overs;
	}
	public String getGameDate(String feedText) {
		String gamedate = "";
		Document doc = convertStringToXMLDocument(feedText);
		try {
			NodeList nodeList = doc.getElementsByTagName("MatchDetail");
			NodeList inningList = nodeList.item(nodeList.getLength() - 1).getChildNodes();
			for (int y = 0, size1 = inningList.getLength(); y < size1; y++) {
				if (inningList.item(y).getNodeName().equalsIgnoreCase("game_date")) {
					gamedate = inningList.item(y).getAttributes().getNamedItem("game_date").getNodeValue();
					break;
				}
			}
		} catch (Exception ex) {
			// System.out.println("Node exception- " + ex.toString());
		}
		return gamedate;
	}
	
	private static Document convertStringToXMLDocument(String xmlString) {
		// Parser that produces DOM object trees from XML content
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// API to obtain DOM Document instance
		DocumentBuilder builder = null;
		try {
			// Create DocumentBuilder with default configuration
			builder = factory.newDocumentBuilder();
			// Parse the content to Document object
			Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String timeStamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // :SSSSSS
		return sdf.format(new Date());
	}

	public void saveLogs(String fileName, String strLogs) {
		String filename = System.getProperty("user.dir") + fileName;		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filename)))) {			
			bw.write(strLogs);
			bw.close();
		} catch (IOException ex) {
			System.out.println(ex.toString());
		}
	}
}
