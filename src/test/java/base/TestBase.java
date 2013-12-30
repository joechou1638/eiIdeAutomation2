package base;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import util.TestUtil;

import com.thoughtworks.selenium.Selenium;

public class TestBase {

	//Initalize property file
	public static Properties CONFIG=null;
	public static Properties ADDR=null;
	public static Properties INPUT=null;
	public static WebDriver driver = null;
	//public static EventFiringWebDriver driver = null;
	public static WebElement wElement = null;
	public static WebDriverWait wWait = null;
	public static boolean isLoggedIn = false; // Is this needed?
	public static Logger LOG=null;
	public static Selenium selenium = null;
	public static String sysType = null;  // staging or production
	public static String Staging = null;  
	public static String Production = null;
	public static String st_prefix = null;
	public static String pr_prefix = null;
	
	
	
	public void initialize() throws IOException {
		
		System.out.println("Entering initialize()********************************");
		// If already initialized, do nothing.
		if (driver != null) {
			System.out.println("Already initialized===========================");
			return;
		} else {
			System.out.println("initializing===========================");
		}
	
		FileInputStream fn = null;
		
		// Setup logs
		LOG = Logger.getLogger("Logger");
		startLog();

		// Setup data files
		// config property file
		CONFIG = new Properties();
		fn = new FileInputStream(System.getProperty("user.dir") + "//src//test//resources//config//config.properties");
		CONFIG.load(fn);
		// OR Properties
		ADDR = new Properties();
		fn = new FileInputStream(System.getProperty("user.dir") + "//src//test/resources//config//OR.properties");
		ADDR.load(fn);
		// test input data:
		INPUT = new Properties();
		fn = new FileInputStream(System.getProperty("user.dir") + "//src//test//resources//config//testData.properties");
		INPUT.load(fn);

		// Staging or production testing setup
		testSysSetup();	
		
		// Initialize WebDriver		
        TestUtil.initWebDriver();
		System.out.println("Exiting initialize()**********************************");
	}

	public static WebElement getElement (String elementName){
		WebElement wElement = null;
		String e_addr = "";
		
		try {
			e_addr = ADDR.getProperty(elementName);
			wElement = driver.findElement(By.xpath(e_addr));
			return wElement;
		} catch (Throwable t) {
			 LOG.error("ERROR: " + t.getMessage());
			return wElement;
		} 		
	}
	 
	public static String getAddr(String eAddr){

		String addr = null;
		try {
			addr = ADDR.getProperty(eAddr);
			//LOG.debug("DEBUG: Got element address of " + eAddr + ": " + addr);
			return addr;
		} catch (Throwable t){
            //LOG.error("ERROR: Got element address of " + eAddr + "failed, " + t.getMessage());
			return addr;
		} 
	}
	
	// Setup test system type data, staging or production
	public static void testSysSetup() {
		
		sysType = INPUT.getProperty("sysType");  // staging or production
		LOG.debug("DEBUG: system type: "+sysType);
		Staging = INPUT.getProperty("staging");
		LOG.debug("DEBUG: Staging: "+Staging);
		Production = INPUT.getProperty("production");
		LOG.debug("DEBUG: Production: "+Production);
		st_prefix = INPUT.getProperty("st_prefix");
		LOG.debug("DEBUG: st_prefix: "+st_prefix);
		pr_prefix = INPUT.getProperty("pr_prefix");
		LOG.debug("DEBUG: pr_prefix: "+pr_prefix);
	}
	
	public static String getInputAddr(String addr) {
		
		String inputAddr = null;
		if (sysType.contains(Staging)) {
			inputAddr = st_prefix + addr;
		} else {
			inputAddr = pr_prefix + addr;
		}
		LOG.debug("DEBUG: got inputAddr: "+inputAddr);
        return inputAddr;
	}
	
	// Get input based on system type, staging or production
	public static String getInput(String addr) {
		
		String inAddr = getInputAddr(addr);
		String inData = INPUT.getProperty(inAddr);
		LOG.debug("DEBUG: got input data: "+inData);
		return inData;
	}
	
	public static void getTestUrl(String UrlName){

		String tUrl = getInput(UrlName);
		LOG.debug("DEBUG: got test url: "+tUrl);		
		driver.get(tUrl);
	}
	
	public void close() throws Exception{
		
		    // Do nothing for now, leave it to specific tests.
	}
	
	public void startLog(){
		LOG.fatal("");
		LOG.fatal("");
		LOG.fatal("====================== Start Logging======================");
	}
}
