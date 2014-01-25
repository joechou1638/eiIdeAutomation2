package util;
// Notes: In general, do not log error in util library functions, and let testing routines to decide what to do with error reporting.

//import org.apache.xpath.operations.String;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.TestBase;

public class TestUtil extends TestBase{
	
	public static void doLogin(String uname, String pword){  //Pass driver in as parameter?
		
		driver.findElement(By.xpath(getAddr("unameAddr"))).clear();
		driver.findElement(By.xpath(getAddr("unameAddr"))).sendKeys(uname);
		driver.findElement(By.xpath(getAddr("pwordAddr"))).clear();
		driver.findElement(By.xpath(getAddr("pwordAddr"))).sendKeys(pword);
		clickOnElement(getAddr("sbuttonAddr"), 2000);
	}
	
	public static void doSearch(String sText) {
		
		driver.findElement(By.xpath(getAddr("searchInputAddr"))).clear();
		driver.findElement(By.xpath(getAddr("searchInputAddr"))).sendKeys(sText);
		clickOnElement(getAddr("searchStartAddr"), 2000);
	}
	
	public static boolean clickAgLinkOK(String agResponse, String agLinkAddr) {			  
		String newWin = null;
		boolean rval = false;
		
		// First check if agent link is there (factory device does not have agent link), if not, do nothing
		if (!driver.findElement(By.xpath(agLinkAddr)).isDisplayed()) {
			LOG.info("INFO: no agent link available, return");
			rval = true;
			return rval;
		}
		// Save current window handle for returning later
		String oldWin = driver.getWindowHandle();	    		    	
	    LOG.debug("DEBUG: oldWin: "+oldWin);
	    // Click on agent link and validate result
	    clickOnElement(agLinkAddr, 5000);
		// Get agent link window handle
		Set<String> winSet = driver.getWindowHandles();
		for (String wid: winSet) {
		    if (!wid.equals(oldWin))  {
		  		newWin = wid;
		  		LOG.debug("DEBUG: newWin: "+newWin);
		  	}		
		}	    	
		driver.switchTo().window(newWin);  // switch to agent link window	
		// If response available, check response status, otherwise check url
		if (agResponse.length()!=0) {
			// get response status
			String response = driver.findElement(By.tagName("pre")).getText();
			LOG.debug("DEBUG: response text: "+response);
			if(response.contains("OK")) {
				rval = true;
				LOG.debug("DEBUG: reponse OK");
			}
		} else {
		    String cUrl = driver.getCurrentUrl();
		    LOG.debug("DEBUG: Current Url: "+cUrl);
		    String expectedUrl = getInput("expectedUrl");
		    LOG.debug("DEBUG: expected Url: "+expectedUrl);
		    if (cUrl.contains(expectedUrl)) {
			    rval = true;
			    LOG.debug("DEBUG: url OK");
		    }
		}
		driver.close(); //close agent link window
		driver.switchTo().window(oldWin); //switch back to original window (module window);	
		mySleep(1000);
		return rval;
	}
	
	public static boolean actionResultOK() {
		
		String xpathStr = getAddr("actionResultAddr");
	    wWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathStr)));
	    String actionResult = driver.findElement(By.xpath(xpathStr)).getText();
	    LOG.debug("DEBUG: action result: "+actionResult);
	    mySleep(1000);
	    if (actionResult.contains("Success")) {
	    	return true;
	    } else {
	    	return false;
	    }
	}
	
	public static void myWaitUntilVisible(String eleXpath) {
		wWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(eleXpath)));
	}
	
	public static void myWaitUntilInvisible(String eleXpath) {
		wWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(eleXpath)));
	}
	
	public static boolean isElementPresent(String xpathStr) {
		
		boolean rval = false;		
		// Shorten timer from 30 seconds to 1
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		int numElement = driver.findElements(By.xpath(xpathStr)).size();
		LOG.debug("DEBUG: got number of elements: "+numElement);
		if (numElement > 0) {
			rval = true;
		}
		// Restore timer back to 30 seconds
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return rval;
	}
	
	public static int getNumElements(String eXpath) {
	
		// Shorten timer from 30 seconds to 1
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		int numElements = driver.findElements(By.xpath(eXpath)).size();
		LOG.debug("DEBUG: got number of elements: "+numElements);
		// Restore timer back to 30 seconds
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return numElements;		
	}

	public static boolean isModelActive(String modName) {
		
		boolean rval = false;
		
		// Get list of active models
		List<WebElement> mList = driver.findElements(By.xpath(getAddr("modListAddr")));
		int mListSize = mList.size();	  
	    LOG.debug("DEBUG: number of models: "+mListSize); 
	    String mpath1 = getAddr("path1");
	    String mpath2 = getAddr("path3");
	    LOG.debug("DEBUG: model path: "+mpath1+" i "+mpath2);
	    // Loop through all models to check on their devices
	    for (int i=1; i <= mListSize; i++) {  
		    String mpath = mpath1 + i + mpath2;
		    String mTitle = driver.findElement(By.xpath(mpath)).getAttribute("title");
		    LOG.debug("DEBUG: got model "+i+"  title: "+mTitle);
		    if (mTitle.contains(modName)) {
		    	rval = true;
		    	break;
		    }
		}
		return rval;
	}

	// Check if factory imp is present.
	// (For now, assuming only one factory imp can be associated with production model.) 	
	public static boolean isFImpPresent() {
		
		boolean rval = false;
		
		// If no factory imp left in list, deleted 
		int fiSize = driver.findElements(By.xpath(getAddr("FImpMacListAddr"))).size();
		LOG.debug("DEBUG: got number of factory imps: "+fiSize);
		if (fiSize > 0) {	// Deleted
			rval = true;
		} 
		return rval;
	}
	
	
	public static void initWebDriver() {
		
		String browserType = getInput("browserType");
		LOG.debug("DEBUG: got browser type: "+browserType);
		
		if (browserType.equals("FF")){
			// Use profile
			ProfilesIni prof = new ProfilesIni();
			FirefoxProfile ffprof = prof.getProfile("default");
			driver = new FirefoxDriver(ffprof);		
		} else if (browserType.equals("CH")) { 
			String chromeDriverPath = getInput("chromeDriverPath");
			LOG.debug("DEBUG: got chrome driver path: "+chromeDriverPath);
			System.setProperty("webdriver.chrome.driver", chromeDriverPath);
			driver = new ChromeDriver();
		} else { // set to IE for now
			driver = new InternetExplorerDriver();
		}
		//EventFiringWebDriver driver = new EventFiringWebDriver(d); //for calling JS, etc.		
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);	
		//driver.manage().window().maximize();	
		wWait = new WebDriverWait(driver, 30);
		//getTestUrl(getAddr("testSiteUrl"));  // goto test home page here, or in test case?
	}
	
	public static void beOnPage(String pageName) {
		String idePageName = "IDE";
		String opsPageName = "OPs";
		
		String pTitle = driver.getTitle();
		if (pageName.contains("IDE")) {  // Need to be on IDE page
			if (!pTitle.contains(idePageName)) {
				// go to IDE page by clicking code tab
				LOG.debug("DEBUG: go to IDE page");
				clickOnElement(getAddr("codeAddr"), 2000);
			}
		} else if (pageName.contains("OPs")) { // need to be on ops page
			if (!pTitle.contains(opsPageName)) {
				// go to ops page by clicking operations tab
				LOG.debug("DEBUG: go to OPs page");
				clickOnElement(getAddr("opsAddr"), 2000);
			}
		} else {
			LOG.error("ERROR: invalid page name: "+pageName);
		}
		mySleep(1000);
	}
	
	public static void mySelect(String xpathAddr, int optionIndex) {
		
		  wElement = driver.findElement(By.xpath(xpathAddr));
		  List <WebElement> optionList = wElement.findElements(By.tagName("option"));
		  LOG.debug("DEBUG: select option size: "+optionList.size());
		  TestUtil.mySleep(500);
		  //Select option (index started with 0)
		  optionList.get(optionIndex).click();
	}
	
	public static WebElement getWebhook(String whUrl) {
		
		WebElement whe = null;
		
		// If no existing webhook, return;
		if (getNumElements(getAddr("whListAddr")) == 0) {
			return whe;
		}
		int whNum = driver.findElements(By.xpath(getAddr("whListAddr"))).size();
		LOG.debug("DEBUG: got number of webhooks: "+whNum);
		//See if target webhook (Url) already existed
		// Get all rows from table
		List<WebElement> rows = driver.findElements(By.xpath(getAddr("whListAddr")));
		for (int i = 0; i < whNum; i++) {
			WebElement row = rows.get(i);
			// Get all columns in row:
			List<WebElement> cols = row.findElements(By.tagName("td"));
			// Get url, 2nd col in row
			String urlStr = cols.get(1).getText();
			LOG.debug("DEBUG: got text: "+urlStr);
			if (urlStr.contains(whUrl)) {
				//LOG.debug("DEBUG: webhook existed");
				whe = row;
				break;
			}
		}
		return whe;
	}
	
	// Get address of target device in new device list
	public static String getNewDev(String devId) {
	
		String ndAddr = null;
		String ndListPath = getAddr("newDevListAddr");
		int numOfDev = getNumElements(ndListPath);
		// Search for target device on list (remember: li index starts at 1, not 0
		for (int i=1; i<=numOfDev; i++) {
			String dPath = ndListPath + "[" + i + "]";
			// Get device id
			String dId = driver.findElement(By.xpath(dPath+"/span")).getText();
			LOG.debug("DEBUG: got device id: "+dId);
			// if found, update return element
			if (dId.contains(devId)) {
				ndAddr = dPath;
				break;
			}
		}
		return ndAddr;
	}
	
	public static WebElement getNewMod(String modName) {
		// mod path://*[@id='select2-drop']/ul/li[2]/div
		
		WebElement we = null;	
		String path1 = "//*[@id='select2-drop']/ul/li";
		String path2 = "[";
		String path3 = "]/div";
		// Get size of model drop down list:
		int mListSize = driver.findElements(By.xpath(path1+"/div")).size();
		LOG.debug("DEBUG: got mod list size: "+mListSize);
		// Search for target model in list, index start with 1
		for (int i = 1; i <= mListSize; i++) {
			String mTitle = driver.findElement(By.xpath(path1+"[" + i + "]/div")).getText();
			LOG.debug("DEBUG: got mTitle "+i+": "+mTitle);
			if (mTitle.contains(modName)) {
				we = driver.findElement(By.xpath(path1+"[" + i + "]/div"));
				break;
			}		
		}
		return we;
	}
	
	// Click on element and then take a break to let page render
	public static void clickOnElement(String objXpath, int ms) {
		driver.findElement(By.xpath(objXpath)).click();
		mySleep(ms);  
	}
	
	// Find a model's address based on its name and type (1: active, 2: factory, 3: inactive)
	public static String findModAddr(String mName, modType mType) {

		String mAddr = null;
		String retAddr = null;
		switch (mType) {

	    case ACTIVE: 
	    	mAddr = getAddr("AMListAddr");
		    break;
	    case FACTORY: 
	    	mAddr = getAddr("FMListAddr");
		    break;
	    case INACTIVE: 
	    	mAddr = getAddr("IMListAddr");   
		    break;
		default: 
		    LOG.error("ERROR: invalid model type: " + mType);
		    break;
		}
		LOG.debug("DEBUG: got model address: "+mAddr);
		// Get list of models
		int mListSize = TestUtil.getNumElements(mAddr);	  
		// Loop through models to find target
		for (int i=1; i <= mListSize; i++) {  
			String mpath = mAddr + "[" + i + "]";
			String mTitle = driver.findElement(By.xpath(mpath+"/div/span")).getText();
			LOG.debug("DEBUG: model "+i+": "+mTitle);
			if (mTitle.contains(mName)) {
				retAddr = mpath;
				LOG.debug("DEBUG: found target model address: "+retAddr);
				break;
			}
		}
		return retAddr;
	}
	
	// Find model based on its address and type (1: active, 2: factory, 3: inactive)
	public static WebElement findModel(String mAddr, modType mType) {
		
		String modAddr = findModAddr(mAddr, mType);
		return driver.findElement(By.xpath(modAddr));
	}
	
	// Find device in a model
	public static String findDevInMod(String modName, modType mType, String devName) {
		
		String devAddr = null;
		String modAddr = findModAddr(modName, mType);
		if (modAddr == null) {
			LOG.error("ERROR: failed to find model "+modName+" in "+mType+" model list");
			//return devAddr;
		} else { // Get list of devices of model
			int numDev= driver.findElements(By.xpath(modAddr+"/ol/li")).size();
			// If model has devices, open its list of devices
			if (numDev > 0) {
				clickOnElement(modAddr+"/div/i[1]", 500);
			}
			for (int i = 1; i <= numDev; i++) {
				// Get device address
				String dAddr = modAddr+"/ol/li["+i+"]/span";
				// Get device name
				String dName = driver.findElement(By.xpath(dAddr)).getText();
				LOG.debug("DEBUG: got device "+dName+" in model "+modName);
				if (dName.contains(devName)) {
					LOG.debug("DEBUG: found it");
					devAddr = dAddr;
					clickOnElement(modAddr+"/div/i[1]", 500);  // Close model device list (restore)
					break;
				}
			}
		}
		return devAddr;
	}
	
	public static int getMaxModForNow(int numMod) {
		
		int nMod = numMod;
		int mMod = Integer.parseInt(getInput("maxModels"));
		if (numMod > mMod) {
			nMod = mMod;
			LOG.debug("DEBUG: change number of models from "+numMod+" to "+nMod);
		}
		return nMod;
	}
	
	public static int getMaxDevForNow(int numDev) {
	
		int nDev = numDev;
		int mDev = Integer.parseInt(getInput("maxDevices"));
		if (numDev > mDev) {
			nDev = mDev;
			LOG.debug("DEBUG: change number of devices from "+numDev+" to "+mDev);
		}
		return nDev;
	}
	
	public static void moveMouseOverAndClick(String eAddr) {
		
		if (isElementPresent(eAddr)) {
			wElement = driver.findElement(By.xpath(eAddr));
		    new Actions(driver).moveToElement(wElement).perform();
		    if (wElement.isDisplayed()) {
		        wElement.click();
		    }
		    mySleep(1000);
		}
	}
	
	public static WebElement findElement(String eAddr) {
		
		WebElement we = null;		
		// Shorten timer from 30 seconds to 1
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		we = driver.findElement(By.xpath(eAddr));
		// Restore timer back to 30 seconds
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return we;
		
	}
	
	// Sleep for i ms
	public static void mySleep(int i){
		try {
			Thread.sleep(i);
		} catch (Throwable e) {
			LOG.error("ERROR: " + e);
		}
	}

}