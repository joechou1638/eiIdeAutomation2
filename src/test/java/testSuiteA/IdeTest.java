package testSuiteA;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import util.TestUtil;
import base.TestBase;


public class IdeTest extends TestBase {
	public static final int maxModels = 2;
	public static final int maxDevices = 2;
	public static String agResponse = null;
	public static boolean chk_response = false;
	public static String mod3Title = "ModJoe3";
	public static int modIndex = 3;
	public static int devIndex = 1;
	public static String idePageName="IDE";
	public static String opsPageName="OPs";
	
  @Test
  public void loginTest()  { 
	  
	  LOG.info("INFO: entering loginTest1()");	  
	  getTestUrl("testSiteUrl");
	  String username = getInput("uname");
	  String password = getInput("pword");
	  LOG.debug("DEBUG:  login user name: "+username+", password: "+password+"=======");
      TestUtil.doLogin(username, password);
	  String title = driver.getTitle();
	  LOG.debug("DEBUG:  page title: "+title+"=======");
	  TestUtil.mySleep(1000);
	  Assert.assertTrue(title.contains("Electric Imp - IDE"));
	  LOG.info("INFO: exiting loginTest1()");
  }
  
  // Test device search box on top of Model panel (sidebar)
  @Test (dependsOnMethods = {"loginTest"})
  public void searchTest() {
	  
	  int numDevices = 0;
	  List<WebElement> dList = null;
	  
	  LOG.info("INFO: entering searchTest()");
	  TestUtil.mySleep(2000);
	  // Make sure on IDE page
	  TestUtil.beOnPage(idePageName);

	  // search for devices
	  String searchText = getInput("searchKey1");
	  LOG.debug("DEBUG:  searchText: "+searchText+"======================================");
	  TestUtil.doSearch(searchText);
	  // Get new devices (if any) in search results
	  if (TestUtil.isElementPresent(getAddr("newDevicesAddr"))){
	      dList = driver.findElements(By.xpath(getAddr("newDevicesAddr")));
	      numDevices = dList.size();
	  }
	  LOG.debug("DEBUG: number of new devices found: "+numDevices);
	  //Temp code, remove later		  
	  if (numDevices > maxDevices) {
		  numDevices = maxDevices;
		  LOG.debug("DEBUG: max number of new devices allowed (for now): "+numDevices);
	  }
	  // first loop through new devices found by search
	  String cancelButtonAddr = getAddr("cancelButtonAddr");
	  for (int i=1; i<=numDevices; i++) {
		  String newDevicesAddr = getAddr("newDevicesAddr")+"["+i+"]";
		  LOG.debug("DEBUG: got new devices addr: "+newDevicesAddr);
		  //wWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(newDevicesAddr)));
		  TestUtil.myWaitUntilVisible(newDevicesAddr);
		  driver.findElement(By.xpath(newDevicesAddr)).click(); 
		  //simply click Cancel button for now
		  TestUtil.myWaitUntilVisible(cancelButtonAddr);
	      //wWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cancelButtonAddr)));
		  driver.findElement(By.xpath(cancelButtonAddr)).click(); //Cancel for now
		  TestUtil.mySleep(1000);
	  }
	  // then loop through models' devices found by search
	  // Get model devices (if any) in search results
	  if (TestUtil.isElementPresent(getAddr("modDevicesAddr"))) {
	      dList = driver.findElements(By.xpath(getAddr("modDevicesAddr")));
	      numDevices = dList.size();
	  }
	  LOG.debug("DEBUG: number of model devices found: "+numDevices);  
	  //Temp code, remove later		  
	  if (numDevices > maxDevices) {
		  numDevices = maxDevices;
		  LOG.debug("DEBUG: max number of model devices allowed (for now): "+numDevices);
	  }
	  for (int j=1; j<=numDevices; j++){
		  String modDevicesAddr = getAddr("modDevicesAddr")+"["+j+"]";
		  LOG.debug("DEBUG: got model devices addr: "+modDevicesAddr);
		  driver.findElement(By.xpath(modDevicesAddr)).click();
		  TestUtil.mySleep(1000);
		  // Run device code
		  driver.findElement(By.xpath(getAddr("runButtonAddr"))).click(); 
		  Assert.assertTrue(TestUtil.actionResultOK());
		  // Click on agent link
		  TestUtil.mySleep(1000);
		  agResponse="";
		  Assert.assertTrue(TestUtil.clickAgLinkOK(agResponse));
	  }
	  // Close search result list
	  driver.findElement(By.xpath(getAddr("searchDoneAddr"))).click();
	  TestUtil.mySleep(3000);
	  LOG.info("INFO: exiting searchTest()");
  }
  
  // Test models and their devices in sidebar (model panel)
  @Test (dependsOnMethods = {"loginTest"})
  public void sidebarTest() {
	  String mpath = null;
	  String dpath = null;
	  
	  LOG.info("INFO: entering sidebarTest()");
	  TestUtil.mySleep(2000);
	  // Get list of models
	  List<WebElement> mList = driver.findElements(By.xpath(getAddr("modListAddr")));
	  int mListSize = mList.size();	  
	  LOG.debug("DEBUG: number of models: "+mListSize);
      //Temp code: to be removed lated when scroll issue resolved
	  if (mListSize > maxModels) {
		  mListSize = maxModels;
		  LOG.debug("DEBUG: max number of models allowed(for now): "+mListSize);
	  }	  
	  String mpath1 = getAddr("path1");
	  String mpath2 = getAddr("path3");
	  LOG.debug("DEBUG: model path: "+mpath1+" i "+mpath2);
	  // Loop through all models to check on their devices
	  for (int i=1; i <= mListSize; i++) {  
		  mpath = mpath1 + i + mpath2;
		  String mTitle = driver.findElement(By.xpath(mpath)).getAttribute("title");
		  LOG.debug("DEBUG: model "+i+": "+mTitle);
		  TestUtil.mySleep(1000);
		  driver.findElement(By.xpath(mpath)).click(); // Click on model to get its devices
		  //Get list of devices of mode
		  String dpath2 = getAddr("path4");
		  dpath = mpath1 + i + dpath2;
		  LOG.debug("DEBUG: device path: "+dpath);
		  List<WebElement> dList = driver.findElements(By.xpath(dpath));
		  int dListSize = dList.size();
		  LOG.debug("DEBUG: number of devices: "+dListSize);
		  //Temp code: set max number of device till scroll issue resoled
		  if (dListSize > maxDevices) {
			  dListSize = maxDevices;
			  LOG.debug("DEBUG: max number of devices allowed (for now):"+dListSize);
		  }
		  //LOG.debug("DEBUG: number of devices: "+dListSize);
		  // Loop through all devices of model
		  for (int j=1; j<=dListSize; j++) {
			  // Get device title
			  String dpath1 = getAddr("path5");
			  String dPath = mpath1 + i + dpath1+j+mpath2;
		      String dTitle = driver.findElement(By.xpath(dPath)).getAttribute("title");
		      LOG.debug("DEBUG: device "+j+": "+dTitle);
		      TestUtil.mySleep(1000);
		      // Click on device to show code
		      driver.findElement(By.xpath(dPath)).click();
		      // Run code
		      driver.findElement(By.xpath(getAddr("runButtonAddr"))).click(); 
		      Assert.assertTrue(TestUtil.actionResultOK());
		      // Click on agent link
		      agResponse = "";
		      Assert.assertTrue(TestUtil.clickAgLinkOK(agResponse));
		  }	  
		  // Close devices of model (restore to previous state) by reclick on model
		  driver.findElement(By.xpath(mpath)).click();
		  TestUtil.mySleep(3000);
	  }
	  LOG.info("INFO: exiting sidebarTest()");
  }
  

  // Test if agent server is responsive, and this test is aiming at Model #3, "ModJoe3".
  // This test requires device be connected, therefore it is a semi-automatic test, and not
  // included in automation test suite.
  @Test (dependsOnMethods = {"loginTest"})
  public void agentServerTest() {
	  
	  LOG.info("INFO: entering agentServerTest()");
	  TestUtil.mySleep(2000);
	  // Make sure on IDE page
	  TestUtil.beOnPage(idePageName);
	  //Click on target model to show device
	  driver.findElement(By.xpath(getAddr("targetModAddr"))).click();
	  //Click on device to show code
	  driver.findElement(By.xpath(getAddr("targetDevAddr"))).click();
      // Run device code
	  driver.findElement(By.xpath(getAddr("runButtonAddr"))).click(); 
      Assert.assertTrue(TestUtil.actionResultOK());
      // Click on agent link
	  agResponse = "OK";  // In this case, expected response is available
      Assert.assertTrue(TestUtil.clickAgLinkOK(agResponse));
	  // Close devices of model (restore to previous state) by re-click on model
	  driver.findElement(By.xpath(getAddr("targetModAddr"))).click();
      TestUtil.mySleep(3000);
	  LOG.info("INFO: exiting agentServerTest()");
  }
  
  // Test deploying a build in ops, requires model, "ModJoe3" and its device, "DevJoe3". 
  @Test (dependsOnMethods = {"loginTest"})
  public void opsDeployTest() {

	  LOG.info("INFO: entering opsDeployTest()");
	  TestUtil.mySleep(2000);
	  // Make sure on IDE page
	  TestUtil.beOnPage(idePageName);
	  //Make sure current current build is of ModJoe3
	  driver.findElement(By.xpath(getAddr("targetModAddr"))).click();
	  driver.findElement(By.xpath(getAddr("targetDevAddr"))).click();
      // Run device code
	  driver.findElement(By.xpath(getAddr("runButtonAddr"))).click(); 
      Assert.assertTrue(TestUtil.actionResultOK());
	  // Get build number
	  TestUtil.myWaitUntilVisible(getAddr("buildNumAddr"));
	  String bnStr = driver.findElement(By.xpath(getAddr("buildNumAddr"))).getText();
	  LOG.debug("DEBUG: got build number string: "+bnStr);
	  // Extract only the number out of the string
	  String buildNumStr = bnStr.substring(6);
	  LOG.debug("DEBUG: got build number: "+buildNumStr);
	  // Promote bulid to Ops
	  driver.findElement(By.xpath(getAddr("promoteButtonAddr"))).click(); 
	  Assert.assertTrue(TestUtil.actionResultOK());
	  LOG.debug("Taking 5 seconds break before clicking Operations button");
	  TestUtil.mySleep(5000);
	  // Go to ops page
	  driver.findElement(By.xpath(getAddr("opsAddr"))).click();
	  TestUtil.mySleep(2000);
	  // Click on target model to show its operations
	  driver.findElement(By.xpath(getAddr("opsModAddr"))).click();
	  //Validate ide build number matches staged build number
	  String stagedBuildNum = driver.findElement(By.xpath(getAddr("stagedBuildNumAddr"))).getText();
	  LOG.debug("DEBUG: got staged build number: "+stagedBuildNum);
	  Assert.assertTrue(stagedBuildNum.equals(buildNumStr));
	  // Deploy staged build
	  driver.findElement(By.xpath(getAddr("deployButtonAddr"))).click();
	  TestUtil.mySleep(1000);
	  //TestUtil.myWaitUntilVisible(getAddr("yesDeployAddr"));
	  driver.findElement(By.xpath(getAddr("yesDeployAddr"))).click();
	  //Validate deploy result
	  //TestUtil.mySleep(1000);
	  TestUtil.myWaitUntilVisible(getAddr("deployResultAddr"));
	  String dResult = driver.findElement(By.xpath(getAddr("deployResultAddr"))).getText();
	  LOG.debug("DEBUG: got deploy result: "+dResult);
	  Assert.assertTrue(dResult.contains("Success"));
	  //Validate deployed build number
	  TestUtil.mySleep(1000);
	  String deployedBuildNum = driver.findElement(By.xpath(getAddr("deployedBuildNumAddr"))).getText();
	  LOG.debug("DEBUG: got deployed build number: "+deployedBuildNum);
	  Assert.assertTrue(deployedBuildNum.equals(stagedBuildNum));
	  TestUtil.mySleep(3000);
	  LOG.info("INFO: exiting opsDeployTest()");  
  }
  
  // Test changing firmware of production model
  @Test (dependsOnMethods = {"loginTest"})
  public void changeFWTest() {
	  
	  LOG.info("INFO: entering changeFWTest()");
	  TestUtil.mySleep(2000);
	  // Make sure on IDE page
	  TestUtil.beOnPage(idePageName);
	  //Click on target model to show device
	  driver.findElement(By.xpath(getAddr("FWModAddr"))).click();
	  //Click on device to show code
	  driver.findElement(By.xpath(getAddr("FWDevAddr"))).click();
      //Build and Run device code
	  driver.findElement(By.xpath(getAddr("runButtonAddr"))).click(); 
      Assert.assertTrue(TestUtil.actionResultOK());
	  // Close devices of model (restore to previous state) by re-click on model
	  driver.findElement(By.xpath(getAddr("FWModAddr"))).click();
      TestUtil.mySleep(1000);
	  // Promote bulid to Ops
      driver.findElement(By.xpath(getAddr("promoteButtonAddr"))).click(); 
	  Assert.assertTrue(TestUtil.actionResultOK());
	  TestUtil.mySleep(3000);
	  // Go to ops page
	  //TestUtil.myWaitUntilVisible(getAddr("opsAddr"));
	  driver.findElement(By.xpath(getAddr("opsAddr"))).click();
	  TestUtil.mySleep(2000);
	  //Click on production model to show operations
	  driver.findElement(By.xpath(getAddr("prodModAddr"))).click();
	  TestUtil.mySleep(1000);
	  //Click on Change FirMware button
	  driver.findElement(By.xpath(getAddr("changeFWAddr"))).click();
	  TestUtil.mySleep(1000);
	  //Select FW version, 2nd in option list, which starts with 0
	  TestUtil.mySelect(getAddr("fwSelectBoxAddr"), 1);
	  //Update selected firmware
	  driver.findElement(By.xpath(getAddr("fwUpdateButtonAddr"))).click();
	  Assert.assertTrue(TestUtil.actionResultOK());   
	  //driver.findElement(By.xpath(getAddr("fwSelectBoxAddr"))).click();
	  TestUtil.mySleep(3000);
	  LOG.info("INFO: exiting changeFWTest()"); 
  }
  
  // Test deleting a device
  @Test (dependsOnMethods = {"loginTest"})
  public void deleteDevTest() {
	  
	  LOG.info("INFO: entering deleteDevTest()");
	  TestUtil.mySleep(2000);
	  // Make sure on IDE page
	  TestUtil.beOnPage(idePageName);
	  // Get target model name from input
	  String modName = getInput("newModName");
	  LOG.debug("DEBUG: got target model name: "+modName);
	  // If no target model to be deleted, return.
	  if (!TestUtil.isModelActive(modName)) {
		  LOG.error("ERROR: no model of device to be deleted");
		  return;
	  }
	  // Click on model to show device to be deleted
	  driver.findElement(By.xpath(getAddr("delModAddr"))).click();
	  //If no target device to be deleted, return.
	  if (!TestUtil.isElementPresent(getAddr("delDevAddr"))) {
		  LOG.error("ERROR: no device to be deleted");
		  return;
	  }
	  // Get device title for logging.
	  String dTitle = driver.findElement(By.xpath(getAddr("delDevAddr"))).getAttribute("title");
	  LOG.debug("DEBUG: got target device title: "+dTitle);
	  // Move over on device to show icon for displaying delete option
	  driver.findElement(By.xpath(getAddr("delDevAddr"))).click();
	  driver.findElement(By.xpath(getAddr("delDevIconAddr"))).click();
	  TestUtil.myWaitUntilVisible(getAddr("delDevButtonAddr"));
	  driver.findElement(By.xpath(getAddr("delDevButtonAddr"))).click();
	  Alert alert = driver.switchTo().alert();
	  alert.accept();
	  driver.navigate().refresh();
      // Validate device has been deleted
	  Assert.assertFalse(TestUtil.isElementPresent(getAddr("delDevAddr")));
	  LOG.debug("DEBUG: deleted device with title "+dTitle);
	  TestUtil.mySleep(3000);
	  LOG.info("INFO: exiting deleteDevTest()");
  }
  
  // Test deleting a Factory imp
  @Test (dependsOnMethods = {"loginTest"})
  public void deleteFImpTest() {
      
	  LOG.info("INFO: Entering deleteFIMpTest()");
	  TestUtil.mySleep(2000);
	  // Make sure on IDE page
	  TestUtil.beOnPage(idePageName);
	  // Go to Operations page
	  driver.navigate().refresh();
	  driver.findElement(By.xpath(getAddr("opsAddr"))).click();
	  TestUtil.mySleep(2000);
	  // Click on target production model to open its operation page
	  driver.findElement(By.xpath(getAddr("prodModAddr"))).click();
	  // If no target factory imp to be deleted, return.
	  if (!TestUtil.isElementPresent(getAddr("delFImpButtonAddr"))) {
		  LOG.error("ERROR: no target factory imp to be deleted");
		  return;
	  }
	  // Delete target factory imp
	  driver.findElement(By.xpath(getAddr("delFImpButtonAddr"))).click();
	  Assert.assertTrue(TestUtil.actionResultOK());
	  // Make suer factory imp deleted
	  Assert.assertTrue(!TestUtil.isElementPresent(getAddr("FImpMacListAddr")));
	  TestUtil.mySleep(3000);
	  LOG.info("INFO: exiting deleteFIMpTest()");
  }
  
  // Test adding a factory imp
  @Test (dependsOnMethods = {"loginTest"})
  public void addFImpTest() {
      
	  LOG.info("INFO: Entering addFIMpTest()");
	  TestUtil.mySleep(2000);
	  // Make sure on IDE page
	  TestUtil.beOnPage(idePageName);
	  // Go to Operations page
	  driver.findElement(By.xpath(getAddr("opsAddr"))).click();
	  TestUtil.mySleep(2000);
	  // Click on target production model to open its operation page
	  driver.findElement(By.xpath(getAddr("prodModAddr"))).click();
	  // If already added, return
	  if (TestUtil.isElementPresent(getAddr("delFImpButtonAddr"))) {
		  LOG.error("ERROR: target factory imp already added");
		  return;
	  }	 
	  // Click on add button ('+' sign) to open mac input
	  driver.findElement(By.xpath(getAddr("addFImpButtonAddr"))).click();
	  // Get factory imp mac from input
	  String mac = getInput("fimpMac");
	  LOG.debug("DEBUG: got factory imp mac: "+mac);
	  // Enter target factory imp mac
	  TestUtil.myWaitUntilVisible(getAddr("macInputAddr"));
	  driver.findElement(By.xpath(getAddr("macInputAddr"))).clear();
	  driver.findElement(By.xpath(getAddr("macInputAddr"))).sendKeys(mac);
	  driver.findElement(By.xpath(getAddr("macSubmitAddr"))).click();
	  Assert.assertTrue(TestUtil.actionResultOK());
	  // Make sure factory imp added
	  Assert.assertTrue(TestUtil.isElementPresent(getAddr("FImpMacListAddr")));
	  TestUtil.mySleep(3000);
	  LOG.info("INFO: exiting addFIMpTest()");
  }
  
  @Test (dependsOnMethods = {"loginTest"})
  public void rmBondTest() {
      
	  LOG.info("INFO: Entering rmBondTest()");
	  TestUtil.mySleep(2000);
	  // Make sure on IDE page
	  TestUtil.beOnPage(idePageName);
	  // Get device id and bond removal password from input file
	  String devId = getInput("rmDevId");
	  LOG.debug("DEBUG: got rmDevId: "+devId);
	  String bondPW = getInput("rmBondPW");
	  LOG.debug("DEBUG: got rmBondPW: "+bondPW);
	  // Go to Operations page
	  driver.findElement(By.xpath(getAddr("opsAddr"))).click();
	  TestUtil.mySleep(2000); 
	  driver.findElement(By.xpath(getAddr("prodModAddr"))).click();
	  // Click on Remove Global Bond button to open input window
	  driver.findElement(By.xpath(getAddr("rmBondButtonAddr"))).click();
	  // Enter device id and bond removal password
	  wWait.until(ExpectedConditions.elementToBeClickable(By.xpath(getAddr("rmDevIdAddr"))));
	  driver.findElement(By.xpath(getAddr("rmDevIdAddr"))).clear();
	  driver.findElement(By.xpath(getAddr("rmDevIdAddr"))).sendKeys(devId);
	  driver.findElement(By.xpath(getAddr("rmBondPWAddr"))).clear();
	  driver.findElement(By.xpath(getAddr("rmBondPWAddr"))).sendKeys(bondPW);
	  driver.findElement(By.xpath(getAddr("rmSubmitAddr"))).click();
	  // Expected to be rejected
	  Assert.assertTrue(!TestUtil.actionResultOK());	  
	  TestUtil.mySleep(3000);
	  LOG.info("INFO: Exiting rmBondTest()");
  }
  
  @Test (dependsOnMethods = {"loginTest"})
  public void addWebhookTest() {
	  
	  
	  LOG.info("INFO: Entering addWebhookTest()");
	  TestUtil.mySleep(2000);
	  // Make sure on IDE page
	  TestUtil.beOnPage(idePageName); 
	  // Go to Operations page
	  driver.findElement(By.xpath(getAddr("opsAddr"))).click();
	  // Click on target model to open operations
	  TestUtil.mySleep(2000); 
	  driver.findElement(By.xpath(getAddr("prodModAddr"))).click();
	  // Get webhook url 
	  String whUrl = getInput("webhookUrl");
	  LOG.debug("DEBUG: got test webhook URL: "+whUrl);
	  // If target webhook already exists, return
	  if (TestUtil.getWebhook(whUrl) != null) {
		  LOG.error("ERROR: webhook already existed, no adding.");
		  return;
	  }
	  // Click on add button of webhook
	  driver.findElement(By.xpath(getAddr("whAddAddr"))).click();
	  // Enter url
	  wWait.until(ExpectedConditions.elementToBeClickable(By.xpath(getAddr("whUrlAddr"))));
	  driver.findElement(By.xpath(getAddr("whUrlAddr"))).clear();
	  driver.findElement(By.xpath(getAddr("whUrlAddr"))).sendKeys(whUrl);
	  // Select type json, 2nd in option list, which starting 0 
	  TestUtil.mySelect(getAddr("whTypeSelectAddr"), 1);
	  // Select name Enrollment, 1st in option list
	  TestUtil.mySelect(getAddr("whNameSelectAddr"), 0);
	  // Submit 
	  driver.findElement(By.xpath(getAddr("whSubmitAddr"))).click();
	  // Validate addition OK
	  Assert.assertTrue(TestUtil.actionResultOK());
	  // Make sure target webhook existed now
	  Assert.assertTrue((TestUtil.getWebhook(whUrl) != null));
	  TestUtil.mySleep(3000);
	  LOG.info("INFO: Exiting addWebhookTest()");
  }
  
  @Test (dependsOnMethods = {"loginTest"})
  public void rmWebhookTest() {	  
	  
	  LOG.info("INFO: Entering rmWebhookTest()");
	  TestUtil.mySleep(2000);
	  // Make sure on IDE page
	  TestUtil.beOnPage(idePageName); 
	  // Go to Operations page
	  driver.findElement(By.xpath(getAddr("opsAddr"))).click();
	  TestUtil.mySleep(2000); 
	  // Click on target model to open operations
	  driver.findElement(By.xpath(getAddr("prodModAddr"))).click();
	  TestUtil.mySleep(1000);
	  // Get target webhook url 
	  String whUrl = getInput("webhookUrl");
	  LOG.debug("DEBUG: got webhook URL: "+whUrl);
	  // If target webhook not exist, done
	  WebElement wh = TestUtil.getWebhook(whUrl);
	  if ( wh == null) {
		  LOG.error("ERROR: webhook not exist, no deleting.");
		  return;
	  }
	  // Get delete button of target webhook
	  List<WebElement> cols = wh.findElements(By.tagName("td"));
	  // Delete target webhook
	  cols.get(4).click();	  
          // Validate target webhook removed OK
	  Assert.assertTrue(TestUtil.actionResultOK());
	  TestUtil.mySleep(1000);
	  // Make sure target webhook no longer existed
	  Assert.assertTrue((TestUtil.getWebhook(whUrl) == null));
	  TestUtil.mySleep(3000);
	  LOG.info("INFO: Exiting rmWebhookTest()");
  }
  
  // Assign a new device to a model
  @Test (dependsOnMethods = {"loginTest"})
  public void assignDevTest() {
	  
	  LOG.info("INFO: Entering assignModelTest()");
	  TestUtil.mySleep(2000);
	  // Make sure on IDE page
	  TestUtil.beOnPage(idePageName); 
	  // Click on new device tab to open new device drop down list
	  driver.findElement(By.xpath(getAddr("newDevAddr"))).click();
	  //TestUtil.mySleep(1000);
	  String nDevId = getInput("newDevId");
	  // Select target device from list
	  wElement = TestUtil.getNewDev(nDevId);
	  //Assert.assertTrue((wElement != null));
	  if (wElement == null) {
		  LOG.error("ERROR: failed to find required device in new device, id:"+nDevId);
		  return;
	  }
	  // Click on target device to open its settings 
	  wElement.click();	  
	  TestUtil.mySleep(1000);
	  // Get target new device name
	  String ndName = getInput("newDevName");
	  LOG.debug("DEBUG: got target new device name: "+ndName);
	  // Enter device name 
	  driver.findElement(By.xpath(getAddr("devInputAddr"))).clear();
	  driver.findElement(By.xpath(getAddr("devInputAddr"))).sendKeys(ndName);
	  // Get device associated model name
	  String nmName = getInput("newModName");
	  LOG.debug("DEBUG: got device associated model name: "+nmName);
	  // Click on drop down button to open model name input field
	  driver.findElement(By.xpath(getAddr("modDropdownAddr"))).click();
	  TestUtil.mySleep(1000);
	  // Select model from drop down list
	  wElement = TestUtil.getNewMod(nmName);
	  Assert.assertTrue((wElement != null));
	  wElement.click();
	  // Save changes
	  driver.findElement(By.xpath(getAddr("modSaveAddr"))).click();
	  TestUtil.mySleep(1000);
	  Assert.assertTrue(TestUtil.actionResultOK());
	  // Validate newly added model and its device exist i sidebar model list?
	  TestUtil.mySleep(3000);
	  LOG.info("INFO: Exiting assignModelTest()");
  }
  
  @BeforeTest
  public void beforeTest() throws IOException {
	  
	  initialize();
  }

  @AfterClass
  public void end() throws Exception {

	  LOG.info("INFO: Entering AfterTest end()");
	  TestUtil.mySleep(1000);
      // Logout 
      driver.navigate().refresh();
      TestUtil.mySleep(1000);
      //LOG.debug("DEBUG: num of userAddr: "+TestUtil.numOfElements(getAddr("userAddr")));
	  driver.findElement(By.xpath(getAddr("userAddr"))).click(); //Click on user drop down
	  Thread.sleep(500);
	  driver.findElement(By.xpath(getAddr("logoutAddr"))).click();
	  Thread.sleep(500);
	  // Close all windows
	  //driver.quit();  // delay till end of all tests
	  LOG.info("INFO: Exiting afterTest end()");
  }
  
  @AfterSuite
  public void quit() {
	  
	  LOG.info("INFO: Entering AfterSuite quit()");
	  LOG.info("INFO: init_enter_count: "+init_enter_count+", init_run_count: "+init_run_count);
	  // Close all windows
	  driver.quit();
	  LOG.info("INFO: Exiting AfterSuite quit()");
  }
}
