String osName=System.getProperty("os.name");
String userDirectory= System.getProperty("user.dir");

public static Properties config = null;
public void loadConfigProperty() throws IOException {
		config = new Properties();
		FileInputStream ip = new FileInputStream(
				System.getProperty("user.dir") + "//src//test//resources//config//config.properties");
		config.load(ip);
	}
 
 loadConfigProperty();
 String browserType=config.getProperty("browserType");
 // Set Properties
 System.setProperty("webdriver.chrome.driver", chromeDriverPath);
