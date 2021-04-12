package com.ca.base;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.ca.lib.XLSWriter;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * This class initializes all the variables and web driver used across the
 * classes.
 * 
 * @author Shiv.Singh
 */
public class BaseClass {
	/** Define and initialize variables */
	public static WebDriver driver;
	String browserName = "chrome";
	
	public static Properties config = new Properties();
	String userDirectory = System.getProperty("user.dir");
	String driverFolder = "\\src\\drivers\\";
	String driverPath = userDirectory + driverFolder;
	//String strConfigFilePath = "\\src\\main\\resources\\config\\config.properties";
	String strExtentReportPath = "\\output\\testReport.html";
	public static ExtentReports report = null;
	public static ExtentTest test = null;
	protected Logger logger = Logger.getLogger(BaseClass.class.getName());

	public static final String PASSED = "Passed: ";
	public static final String FAILED = "Failed: ";
	public static final String EXCEPTION = "Exception: ";
	public static final String EXPECTED_TITLE = " EXPECTED_TITLE: ";
	public static final String ACTUAL_TITLE = " ACTUAL_TITLE: ";

	/**
	 * This function executes first to initialize the different variables used
	 * across the test suites.
	 * 
	 */
	@BeforeSuite
	// @Parameters({"browserName"})
	public void init() throws Throwable {
		initReport();
		//FileInputStream inputFile = new FileInputStream(userDirectory + strConfigFilePath);
		//config.load(inputFile);		
		initWebDriver();
	}

	public void initWebDriver() {
		driver = getDriver(browserName, driverPath);
		System.out.println("Starting " + browserName + " browser...");
		//test.log(LogStatus.INFO, "Starting " + browserName + " browser...");
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		// driver.manage().timeouts().pageLoadTimeout(TestUtil.PAGE_LOAD_TIMEOUT,
		// TimeUnit.SECONDS);
	}

	public WebDriver getDriver(String browserName, String driverPath) {
		
		WebDriver webDriver = null;
		// Select the specified driver
		if (browserName.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver", driverPath + "chromedriver.exe");
			webDriver = new ChromeDriver();
		} else if (browserName.equals("firefox")) {
			System.setProperty("webdriver.gecko.driver", driverPath + "geckodriver.exe");
			webDriver = new ChromeDriver();
		} else if (browserName.equals("edge")) {
			System.setProperty("webdriver.edge.driver", driverPath + "MicrosoftWebDriver.exe");
			webDriver = new EdgeDriver();
		} else {
			webDriver = null;
		}
		return webDriver;
	}

	@AfterSuite
	public void quitDriver() {
		//driver.quit();
		endReport();
		
		config = null;
	}

	/**
	 * 
	 * initReport() function is for test reporting purpose
	 * 
	 */
	public void initReport() {
		String strReportName = System.getProperty("user.dir") + strExtentReportPath;
		report = new ExtentReports(strReportName);
		test = report.startTest("CA Automation - Test Report");
		report.addSystemInfo("Browser Name", "Chrome");
		report.addSystemInfo("Project Name", "CA Test Automation");

	}

	/**
	 * 
	 * This function is for testing purpose
	 * 
	 */
	public void endReport() {
		report.endTest(test);
		report.flush();

	}

	/**
	 * 
	 * This main function is for testing purpose
	 * 
	 */

	public static void main(String[] args)

	{
		BaseClass base = new BaseClass();

		try {
			System.out.println("Info: BaseClass testing...");
			base.init();
			System.out.println("Info: init() done...");
			driver.get("https://google.co.in");
			driver.findElement(By.name("q")).sendKeys("hello");
			System.out.println("Info: Driver initialization and browser open done...");
			base.quitDriver();
			System.out.println("Info: BaseClass testing done.");
		} catch (Throwable e) {

			e.printStackTrace();
		}
	}

}
