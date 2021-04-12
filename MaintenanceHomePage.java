package com.ca.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MaintenanceHomePage {
	/** Define and initialize variables */
	private WebDriver driver;
	
	public String maintenanceAppUrl = "https://ca-prod-maintenance-ui.azurewebsites.net/rawfeedsend";
	/** Web elements available on this page */
	// Advertisement objects   
	@FindBy(id = "btn-microsoftgraph")
	public WebElement SignLink;
	
	@FindBy(name = "loginfmt")
	public WebElement Email;
	@FindBy(id = "idSIButton9")
	public WebElement Next;
	
	@FindBy(name = "passwd")
	public WebElement Password;
	
	@FindBy(id = "idTxtBx_SAOTCC_OTC")  // name = "otc"
	public WebElement OTP;
	
	@FindBy(id = "idSubmit_SAOTCC_Continue")  // name = "otc"
	public WebElement Verify;
	
	@FindBy(css = "document.querySelector('#root > div > nav > div > div:nth-child(2) > button')")  
	public WebElement Dev;
	@FindBy(css = "div.MuiPaper-root.MuiMenu-paper.MuiPaper-elevation8.MuiPopover-paper.MuiPaper-rounded > ul > li:nth-child(2)")  
	public WebElement UAT;
	
	@FindBy(id = "standard-search")
	public WebElement FeedId;
	
	
	//div.view-lines > div
	@FindBy(css ="div#rawFeedEditor > section > div > div > div.overflow-guard > div.monaco-scrollable-element.editor-scrollable.vs")
	//@FindBy(tagName = "body")
	public WebElement RawFeedEditor;
	


	public MaintenanceHomePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	public void startApp(String appUrl) {
		driver.get(appUrl);			
	}
}
