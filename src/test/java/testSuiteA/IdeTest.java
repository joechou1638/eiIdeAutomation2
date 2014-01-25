package testSuiteA;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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
	  
	  LOG.info("INFO: Entering loginTest1()");	  
	  getTestUrl("testSiteUrl");
	  String username = getInput("uname");
	  String password = getInput("pword");
	  LOG.debug("DEBUG:  login user name: "+username+", password: "+password+"=======");
      TestUtil.doLogin(username, password);
	  String title = driver.getTitle();
	  LOG.debug("DEBUG:  page title: "+title+"=======");
	  TestUtil.mySleep(1000);
	  Assert.assertTrue(title.contains("Electric Imp - IDE"));
	  LOG.info("INFO: Exiting loginTest1()");
  }
  
  // Test device search box on top of Model panel (sidebar)
  @Test (dependsOnMethods = {"loginTest"})
  public void searchTest() {
	  
	  LOG.info("INFO: Entering searchTest()");
	  TestUtil.mySleep(1000);
	  // search for devices
	  String searchText = getInput("searchKey1");
	  LOG.debug("DEBUG:  searchText: "+searchText+"======================================");
	  TestUtil.doSearch(searchText);
	  // Get number of new devices (if any) in search results
	  int numDevices = TestUtil.getNumElements(getAddr("newDevicesAddr"));
	  numDevices = TestUtil.getMaxDevForNow(numDevices);  // If more than max, use max instead
	  // Loop through new devices found by search
	  String cancelButtonAddr = getAddr("cancelButtonAddr");
	  for (int i=1; i<=numDevices; i++) {
		  String newDevicesAddr = getAddr("newDevicesAddr")+"["+i+"]/i";
		  LOG.debug("DEBUG: got new devices addr: "+newDevicesAddr);
		  TestUtil.clickOnElement(newDevicesAddr, 1000); 
		  //simply click Cancel button for now
		  TestUtil.myWaitUntilVisible(cancelButtonAddr);
		  TestUtil.clickOnElement(cancelButtonAddr, 1000);
	  }
	  // Get number of model devices
	  numDevices = TestUtil.getNumElements(getAddr("modDevicesAddr"));
	  if (numDevices > maxDevices) {
		  numDevices = maxDevices;
		  LOG.debug("DEBUG: max number of model devices allowed (for now): "+numDevices);
	  }
	  // Loop through model devices
	  for (int j=1; j<=numDevices; j++){
		  String modDevicesAddr = getAddr("modDevicesAddr")+"["+j+"]";
		  LOG.debug("DEBUG: got model devices addr: "+modDevicesAddr);
		  TestUtil.clickOnElement(modDevicesAddr, 1000);
		  // Run device code
		  TestUtil.clickOnElement(getAddr("runButtonAddr"), 1000);
		  Assert.assertTrue(TestUtil.actionResultOK());
		  // Click on agent link
		  TestUtil.mySleep(1000);
		  Assert.assertTrue(TestUtil.clickAgLinkOK("", getAddr("agLinkAddr")));
	  }
	  // Close search result list
	  TestUtil.clickOnElement(getAddr("searchDoneAddr"), 500);
	  TestUtil.mySleep(3000);
	  LOG.info("INFO: Exiting searchTest()");
  }
  
  // Test models and their devices in active model section
  @Test (dependsOnMethods = {"loginTest"})
  public void updateActiveModTest() {
	  
	  LOG.info("INFO: Entering updateActiveModTest()");
	  TestUtil.mySleep(2000);
	  // Get list of models
	  int mlSize = TestUtil.getNumElements(getAddr("AMListAddr"));	  
	  int mListSize = TestUtil.getMaxModForNow(mlSize);  
	  if (mListSize == 0) {
		  LOG.error("ERROR: no models in active model section, done");
		  return;
	  } 
	  // Open active model section
	  TestUtil.moveMouseOverAndClick(getAddr("secAMOpenAddr"));
	  // Loop through all models to check on their devices
	  for (int i=1; i <= mListSize; i++) {  
		  String mpath = getAddr("AMListAddr") + "[" + i + "]";
		  LOG.debug("DEBUG: got model address: "+mpath);
		  String mTitle = driver.findElement(By.xpath(mpath+"/div/span")).getText();
		  LOG.debug("DEBUG: model "+i+": "+mTitle);
		  TestUtil.mySleep(1000);
		  // Click on model to show its devices
		  TestUtil.clickOnElement(mpath+"/div/i", 500);
          // Get number of model's devices
		  int dListSize = TestUtil.getNumElements(mpath+"/ol/li");
		  //Temp code: set max number of device till scroll issue resoled
		  if (dListSize > maxDevices) {
			  dListSize = maxDevices;
			  LOG.debug("DEBUG: max number of devices allowed (for now):"+dListSize);
		  }
		  // Loop through devices of model
		  for (int j=1; j<=dListSize; j++) {
			  // Get device title
			  String dPath = mpath + "/ol/li" + "[" + j + "]";
		      String dTitle = driver.findElement(By.xpath(dPath+"/span")).getText();
		      LOG.debug("DEBUG: device "+j+": "+dTitle);
		      TestUtil.mySleep(1000);
		      // Click on device to show code
		      TestUtil.clickOnElement(dPath, 500);
		      // Build and run code
		      TestUtil.clickOnElement(getAddr("runButtonAddr"), 500);
		      Assert.assertTrue(TestUtil.actionResultOK());
		      // Click on agent link
		      Assert.assertTrue(TestUtil.clickAgLinkOK("", getAddr("agLinkAddr")));
		  }	  
		  // Close devices drop-down of model (restore)
		  TestUtil.clickOnElement(mpath+"/div/i", 500);
		  TestUtil.mySleep(3000);
	  }
	  // Close active model section
	  TestUtil.moveMouseOverAndClick(getAddr("secAMCloseAddr"));
	  LOG.info("INFO: Exiting updateActiveModTest()");
  }
  

  // Test if agent server is responsive, and this test is aiming at Model #3, "ModJoe3".
  // This test requires device be connected, therefore it is a semi-automatic test, and not
  // included in automation test suite. Therefore, comment it out for now.
  /*
  @Test (dependsOnMethods = {"loginTest"})
  public void agentServerTest() {
	  
	  LOG.info("INFO: entering agentServerTest()");
	  TestUtil.mySleep(2000);
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
  */
  
  // Test deploying a build in ops, requires model, "ModJoe3" and its device, "DevJoe3". 
  @Test (dependsOnMethods = {"loginTest"})
  public void opsDeployTest() {

	  LOG.info("INFO: Entering opsDeployTest()");
	  TestUtil.mySleep(2000);
	  // Open active model section
	  TestUtil.moveMouseOverAndClick(getAddr("secAMOpenAddr"));
	  // Search for target model
	  String dpName = getInput("dpModName");
	  String dpModAddr = TestUtil.findModAddr(dpName, modType.ACTIVE);
      if (dpModAddr == null) {
    	  LOG.error("ERROR: failed to find target active model: "+dpName);
    	  return;
      }
      // Click model to show code
      TestUtil.clickOnElement(dpModAddr+"/div/span",1000);
      // Build code
      TestUtil.clickOnElement(getAddr("runButtonAddr"),500);
      Assert.assertTrue(TestUtil.actionResultOK());
	  // Get build number
	  TestUtil.myWaitUntilVisible(getAddr("buildNumAddr"));
	  String bnStr = driver.findElement(By.xpath(getAddr("buildNumAddr"))).getText();
	  LOG.debug("DEBUG: got build number string: "+bnStr);
	  // Extract only the number out of the string
	  String buildNumStr = bnStr.substring(6);
	  LOG.debug("DEBUG: got build number: "+buildNumStr);
	  // Promote bulid to Ops
	  TestUtil.clickOnElement(getAddr("promoteButtonAddr"),500);
	  Assert.assertTrue(TestUtil.actionResultOK()); 
	  LOG.debug("Taking 5 seconds break before clicking Operations button");
	  TestUtil.mySleep(5000);
	  // Close active model section
	  TestUtil.moveMouseOverAndClick(getAddr("secAMCloseAddr"));
	  // Go to ops page
	  TestUtil.clickOnElement(getAddr("opsAddr"),2000);
	  // Click on target model to show its operations
	  TestUtil.clickOnElement(getAddr("opsModAddr"),500);
	  //Validate ide build number matches staged build number
	  String stagedBuildNum = driver.findElement(By.xpath(getAddr("stagedBuildNumAddr"))).getText();
	  LOG.debug("DEBUG: got staged build number: "+stagedBuildNum);
	  //Assert.assertTrue(stagedBuildNum.equals(buildNumStr));  // Commented out till build number bug fixed
	  // Deploy staged build
	  TestUtil.clickOnElement(getAddr("deployButtonAddr"),1000);
	  TestUtil.clickOnElement(getAddr("yesDeployAddr"),200);
	  TestUtil.myWaitUntilVisible(getAddr("deployResultAddr"));
	  String dResult = driver.findElement(By.xpath(getAddr("deployResultAddr"))).getText();
	  LOG.debug("DEBUG: got deploy result: "+dResult);
	  Assert.assertTrue(dResult.contains("Success"));
	  //Validate deployed build number
	  TestUtil.mySleep(1000);
	  String deployedBuildNum = driver.findElement(By.xpath(getAddr("deployedBuildNumAddr"))).getText();
	  LOG.debug("DEBUG: got deployed build number: "+deployedBuildNum);
	  //Assert.assertTrue(deployedBuildNum.equals(stagedBuildNum)); // Comment out till duplicate "ModJoe3" on staging resolved
	  // Go back to IDE page (restore)
	  TestUtil.beOnPage(idePageName);
	  TestUtil.mySleep(3000);
	  LOG.info("INFO: Exiting opsDeployTest()");  
  }
  
  // Test changing factory firmware of production model, required target model "FWMod1"
  @Test (dependsOnMethods = {"loginTest"})
  public void changeFWTest() {
	  
	  LOG.info("INFO: Entering changeFWTest()");
	  TestUtil.mySleep(2000);
	  // Open factory firmware model section
	  TestUtil.moveMouseOverAndClick(getAddr("secFMOpenAddr"));
	  // Search for target model, "FWMod1"
	  String fwName = getInput("fwModName");
	  String ffModAddr = TestUtil.findModAddr(fwName, modType.FACTORY);
      if (ffModAddr == null) {
    	  LOG.error("ERROR: failed to find target factory model: "+fwName);
    	  return;
      }
      // Click model to show code (Factory firmware models do not have devices)
      TestUtil.clickOnElement(ffModAddr+"/div/span",1000);	  
      //Build and Run device code
      TestUtil.clickOnElement(getAddr("runButtonAddr"),1000);
      Assert.assertTrue(TestUtil.actionResultOK());
      TestUtil.mySleep(1000);      
	  // Promote bulid to Ops
      TestUtil.clickOnElement(getAddr("promoteButtonAddr"),1000);
	  Assert.assertTrue(TestUtil.actionResultOK());  
	  TestUtil.mySleep(3000);  
	  // Close factory firmware model section
	  TestUtil.moveMouseOverAndClick(getAddr("secFMCloseAddr"));
	  // Go to ops page
	  TestUtil.clickOnElement(getAddr("opsAddr"), 2000);
	  //Click on production model to show operations
	  TestUtil.clickOnElement(getAddr("prodModAddr"), 1000);
	  //Click on Change FirMware button
	  TestUtil.clickOnElement(getAddr("changeFWAddr"), 1000);
	  //Select FW version, 2nd in option list, which starts with 0
	  TestUtil.mySelect(getAddr("fwSelectBoxAddr"), 1);
	  //Update selected firmware
	  TestUtil.clickOnElement(getAddr("fwUpdateButtonAddr"), 500);
	  Assert.assertTrue(TestUtil.actionResultOK());   
	  // Go back to IDE page (restore)
	  TestUtil.beOnPage(idePageName);
	  TestUtil.mySleep(3000);
	  LOG.info("INFO: Exiting changeFWTest()"); 
  }
  
  // Test deleting a device
  @Test (dependsOnMethods = {"loginTest"})
  public void deleteDevTest() {
	  
	  LOG.info("INFO: Entering deleteDevTest()");
	  TestUtil.mySleep(2000);
	  // Open active model section (if not open yet)
	  TestUtil.moveMouseOverAndClick(getAddr("secAMOpenAddr"));
	  // Get target model name from input
	  String modName = getInput("newModName");
	  LOG.debug("DEBUG: got target model name: "+modName);
	  // If no target model to be deleted, return.
	  String modAddr = TestUtil.findModAddr(modName, modType.ACTIVE);
	  if (modAddr == null) {
		  LOG.error("ERROR: no model of device to be deleted");
		  return;
	  }
	  // Click on model to show device to be deleted
	  TestUtil.clickOnElement(modAddr+"/div/i[1]", 1000);
	  // Get device title for logging.
	  String dTitle = driver.findElement(By.xpath(modAddr+"/ol/li/span")).getText();
	  LOG.debug("DEBUG: got target device title: "+dTitle);
	  // Move over on device to show icon for displaying delete option
	  TestUtil.clickOnElement(modAddr+"/ol/li/i", 1000);
	  TestUtil.myWaitUntilVisible(getAddr("delDevButtonAddr"));
	  TestUtil.clickOnElement(getAddr("delDevButtonAddr"), 1000);
	  Alert alert = driver.switchTo().alert();
	  alert.accept();
	  driver.navigate().refresh();
      // Validate device has been deleted and model is inactive
	  Assert.assertTrue(TestUtil.findModAddr(modName, modType.INACTIVE)!=null);
	  // Also make sure deleted device NOT in New Device list (need push-n-push to do so)
	  Assert.assertTrue(TestUtil.getNewDev(getInput("newDevId"))==null);
	  LOG.debug("DEBUG: deleted device with title "+dTitle);
	  // Close active models section (if not close yet)
	  TestUtil.moveMouseOverAndClick(getAddr("secAMCloseAddr"));
	  TestUtil.mySleep(3000);
	  LOG.info("INFO: Exiting deleteDevTest()");
  }
  
  // Test deleting a Factory imp
  @Test (dependsOnMethods = {"loginTest"})
  public void deleteFImpTest() {
      
	  LOG.info("INFO: Entering deleteFIMpTest()");
	  TestUtil.mySleep(2000);
	  // Go to Operations page
	  driver.navigate().refresh();
	  TestUtil.clickOnElement(getAddr("opsAddr"), 2000);
	  // Click on target production model to open its operation page
	  TestUtil.clickOnElement(getAddr("prodModAddr"), 1000);
	  // If no target factory imp to be deleted, return.
	  if (!TestUtil.isElementPresent(getAddr("delFImpButtonAddr"))) {
		  LOG.error("ERROR: no target factory imp to be deleted");
		  return;
	  }
	  // Delete target factory imp
	  TestUtil.clickOnElement(getAddr("delFImpButtonAddr"), 500);
	  Assert.assertTrue(TestUtil.actionResultOK());
	  // Make suer factory imp deleted
	  Assert.assertTrue(!TestUtil.isElementPresent(getAddr("FImpMacListAddr")));
	  // Go back to IDE page (restore)
	  TestUtil.beOnPage(idePageName);
	  TestUtil.mySleep(3000);
	  LOG.info("INFO: Exiting deleteFIMpTest()");
  }
  
  // Test adding a factory imp
  @Test (dependsOnMethods = {"loginTest"})
  public void addFImpTest() {
      
	  LOG.info("INFO: Entering addFIMpTest()");
	  TestUtil.mySleep(2000);
	  // Go to Operations page
	  TestUtil.clickOnElement(getAddr("opsAddr"), 2000);
	  // Click on target production model to open its operation page
	  TestUtil.clickOnElement(getAddr("prodModAddr"), 500);
	  // If already added, return
	  if (TestUtil.isElementPresent(getAddr("delFImpButtonAddr"))) {
		  LOG.error("ERROR: target factory imp already added");
		  return;
	  }	 
	  // Click on add button ('+' sign) to open mac input
	  TestUtil.clickOnElement(getAddr("addFImpButtonAddr"), 500);
	  // Get factory imp mac from input
	  String mac = getInput("fimpMac");
	  LOG.debug("DEBUG: got factory imp mac: "+mac);
	  // Enter target factory imp mac
	  TestUtil.myWaitUntilVisible(getAddr("macInputAddr"));
	  driver.findElement(By.xpath(getAddr("macInputAddr"))).clear();
	  driver.findElement(By.xpath(getAddr("macInputAddr"))).sendKeys(mac);
	  TestUtil.clickOnElement(getAddr("macSubmitAddr"), 500);
	  Assert.assertTrue(TestUtil.actionResultOK());
	  // Make sure factory imp added
	  Assert.assertTrue(TestUtil.isElementPresent(getAddr("FImpMacListAddr")));
	  // Go back to IDE page (restore)
	  TestUtil.beOnPage(idePageName);
	  TestUtil.mySleep(3000);
	  LOG.info("INFO: Exiting addFIMpTest()");
  }
  
  @Test (dependsOnMethods = {"loginTest"})
  public void rmBondTest() {
      
	  LOG.info("INFO: Entering rmBondTest()");
	  TestUtil.mySleep(2000);
	  // Get device id and bond removal password from input file
	  String devId = getInput("rmDevId");
	  LOG.debug("DEBUG: got rmDevId: "+devId);
	  String bondPW = getInput("rmBondPW");
	  LOG.debug("DEBUG: got rmBondPW: "+bondPW);
	  // Go to Operations page
	  TestUtil.clickOnElement(getAddr("opsAddr"), 2000);
	  TestUtil.clickOnElement(getAddr("prodModAddr"), 500);
	  // Click on Remove Global Bond button to open input window
	  TestUtil.clickOnElement(getAddr("rmBondButtonAddr"), 500);
	  // Enter device id and bond removal password
	  wWait.until(ExpectedConditions.elementToBeClickable(By.xpath(getAddr("rmDevIdAddr"))));
	  driver.findElement(By.xpath(getAddr("rmDevIdAddr"))).clear();
	  driver.findElement(By.xpath(getAddr("rmDevIdAddr"))).sendKeys(devId);
	  driver.findElement(By.xpath(getAddr("rmBondPWAddr"))).clear();
	  driver.findElement(By.xpath(getAddr("rmBondPWAddr"))).sendKeys(bondPW);
	  TestUtil.clickOnElement(getAddr("rmSubmitAddr"), 500);
	  // Expected to be rejected
	  Assert.assertTrue(!TestUtil.actionResultOK());	
	  // Go back to IDE page (restore)
	  TestUtil.beOnPage(idePageName);
	  TestUtil.mySleep(3000);
	  LOG.info("INFO: Exiting rmBondTest()");
  }
  
  // Add test enrollment webhook
  @Test (dependsOnMethods = {"loginTest"})
  public void addWebhookTest() {
	  
	  
	  LOG.info("INFO: Entering addWebhookTest()");
	  TestUtil.mySleep(2000);
	  // Go to Operations page
	  TestUtil.clickOnElement(getAddr("opsAddr"), 2000);
	  // Click on target model to open operations
	  TestUtil.clickOnElement(getAddr("prodModAddr"), 500);
	  // Get webhook url 
	  String whUrl = getInput("webhookUrl");
	  LOG.debug("DEBUG: got test webhook URL: "+whUrl);
	  // Click on Edit button
	  TestUtil.clickOnElement(getAddr("enrollEditAddr"), 500);
	  driver.findElement(By.xpath(getAddr("enrollInputAddr"))).clear();
	  driver.findElement(By.xpath(getAddr("enrollInputAddr"))).sendKeys(whUrl);
	  TestUtil.clickOnElement(getAddr("enrollSubmitAddr"), 500);
      // Validate result OK
	  Assert.assertTrue(TestUtil.actionResultOK());
	  TestUtil.beOnPage(idePageName);
	  TestUtil.mySleep(3000);
	  LOG.info("INFO: Exiting addWebhookTest()");
  }
  
  // Remove test enrollment webhook
  @Test (dependsOnMethods = {"loginTest"})
  public void rmWebhookTest() {	  
	  
	  LOG.info("INFO: Entering rmWebhookTest()");
	  TestUtil.mySleep(2000);
	  // Go to Operations page
	  TestUtil.clickOnElement(getAddr("opsAddr"), 2000);
	  // Click on target model to open operations
	  TestUtil.clickOnElement(getAddr("prodModAddr"), 500);
	  // Click on Edit button
	  TestUtil.clickOnElement(getAddr("enrollEditAddr"), 500);
	  driver.findElement(By.xpath(getAddr("enrollInputAddr"))).clear();
	  TestUtil.clickOnElement(getAddr("enrollSubmitAddr"), 500);
	  Assert.assertTrue(TestUtil.actionResultOK());
	  // Go back to IDE page (restore)
	  TestUtil.beOnPage(idePageName);
	  TestUtil.mySleep(3000);
	  LOG.info("INFO: Exiting rmWebhookTest()");
  }
  
  // Assign a new device to a model
  @Test (dependsOnMethods = {"loginTest"})
  public void assignDevTest() {
	  
	  LOG.info("INFO: Entering assignModelTest()");
	  TestUtil.mySleep(2000);
	  // Open new device drop down list
	  TestUtil.moveMouseOverAndClick(getAddr("secNDOpenAddr")); 
	  String nDevId = getInput("newDevId");
	  // Select target device from list
	  String ndAddr = TestUtil.getNewDev(nDevId);
	  if (ndAddr == null) {
		  LOG.error("ERROR: failed to find required device in new device, id:"+nDevId);
		  return;
	  }
	  // Click on target device to open its settings 
	  TestUtil.clickOnElement(ndAddr+"/i", 1000);
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
	  TestUtil.clickOnElement(getAddr("modDropdownAddr"), 1000);
	  // Select model from drop down list
	  wElement = TestUtil.getNewMod(nmName);
	  Assert.assertTrue((wElement != null));
	  wElement.click();
	  // Save changes
	  TestUtil.clickOnElement(getAddr("modSaveAddr"), 1000);
	  Assert.assertTrue(TestUtil.actionResultOK());
	  // Validate newly added device is with target model in active model list
	  Assert.assertTrue(TestUtil.findDevInMod(nmName, modType.ACTIVE, ndName) != null);
	  TestUtil.mySleep(3000);
	  // Close new devices section
	  TestUtil.moveMouseOverAndClick(getAddr("secNDCloseAddr"));
	  LOG.info("INFO: Exiting assignModelTest()");
  }
  
  // For now un-check factory firmware in model and validate model ends up in inactive, then restore
  @Test (dependsOnMethods = {"loginTest"})
  public void changeFFTypeTest() {
	  
	  LOG.info("INFO: Entering factoryModTest()");
	  TestUtil.mySleep(2000);
	  // Find target factory model
	  String modName = getInput("fwModName");
	  String modAddr = TestUtil.findModAddr(modName, modType.FACTORY);
	  // If target model not found, log error and return
	  if (modAddr == null) {
		  LOG.error("ERROR: failed to find target factory model: "+modName);
		  return;
	  }
	  // Open factory firmware section
	  TestUtil.moveMouseOverAndClick(getAddr("secFMOpenAddr"));
	  // Open model settings
	  TestUtil.clickOnElement(modAddr+"/div/i[2]", 1000);
	  // Un-check factory type
	  TestUtil.clickOnElement(getAddr("fwCheckboxAddr"), 500);
	  TestUtil.clickOnElement(getAddr("fwSaveChangeAddr"), 1000);
	  // Validate model not in factory model list
	  Assert.assertTrue(TestUtil.findModAddr(modName, modType.FACTORY)==null);
	  // Validate model in inactive model list
	  modAddr = TestUtil.findModAddr(modName, modType.INACTIVE);
	  Assert.assertTrue(modAddr!=null);
	  // Open model settings
	  TestUtil.clickOnElement(modAddr+"/div/i[2]", 1000);
	  // Check factory type
	  TestUtil.clickOnElement(getAddr("fwCheckboxAddr"), 500);	  
	  TestUtil.clickOnElement(getAddr("fwSaveChangeAddr"),1000);
	  // Validate model not in inactive model list
	  Assert.assertTrue(TestUtil.findModAddr(modName, modType.INACTIVE)==null);
	  // Validate model in factory model list	  
	  Assert.assertTrue(TestUtil.findModAddr(modName, modType.FACTORY)!=null);
	  TestUtil.mySleep(3000);
	  // Close factory firmware section
	  TestUtil.moveMouseOverAndClick(getAddr("secFMCloseAddr"));
	  LOG.info("INFO: Exiting factoryModTest()");
  }
  
  // Loop through Factory Firmware models and then Inactive models, doing build and promote.
  @Test (dependsOnMethods = {"loginTest"})
  public void updateOtherModTest() {
	  
	  String modType = null;
	  for (int n = 1; n <= 2; n++) {
		  if (n == 1) {
			  modType = "FM"; // Factory model
		  } else {
			  modType = "IM"; // Inactive model
		  }

	      LOG.info("INFO: Entering updateOtherModTest()"+" for "+modType);
	      TestUtil.mySleep(2000);
	      // Get list of models in Factory Firmware section
	      String modAddr = getAddr(modType+"ListAddr");
	      int numMod = TestUtil.getNumElements(modAddr);
	      if (numMod == 0){
		      LOG.error("ERROR: no models in "+modType+" section");
		       return;
	      } else { 
		      // Open model list
		      TestUtil.moveMouseOverAndClick(getAddr("sec"+modType+"OpenAddr"));
		      numMod = TestUtil.getMaxModForNow(numMod);
	      }
	      // Loop through models and do build and promote for now
	      for (int i = 1; i <= numMod; i++) {
		      String mAddr = modAddr + "[" + i + "]";
		      // Click on model to show code
		      TestUtil.clickOnElement(mAddr+"/div/span", 500);
		      // Build model code
		      TestUtil.clickOnElement(getAddr("runButtonAddr"), 500);
	          Assert.assertTrue(TestUtil.actionResultOK());
		      // Promote model code to Ops
	          TestUtil.clickOnElement(getAddr("promoteButtonAddr"),500);
		      Assert.assertTrue(TestUtil.actionResultOK()); 
	      }	  
	      // Close model list
	      TestUtil.moveMouseOverAndClick(getAddr("sec"+modType+"CloseAddr"));
	      TestUtil.mySleep(3000);
	      LOG.info("INFO: Exiting updateOtherModTest()"+" for "+modType);
	  }
	  
  }
  
  @Test (dependsOnMethods = {"loginTest"})
  public void checkDevTagTest() {
	  
	  LOG.info("INFO: Entering checkDevTagTest()");
	  TestUtil.mySleep(2000);
	  // Open target active model code	  
	  String modAddr = TestUtil.findModAddr("ModJoe3", modType.ACTIVE);
	  TestUtil.clickOnElement(modAddr+"/div/span", 1000);
	  // Click on Devices tag to open drop-down list
	  String dtAddr = getAddr("devTagAddr");
	  TestUtil.clickOnElement(dtAddr+"/div/div/a", 500);
	  // Click on device tab to open device data
	  TestUtil.clickOnElement(dtAddr+"/div/div/ul/li/a", 500);
	  // Click on device agent link
	  Assert.assertTrue(TestUtil.clickAgLinkOK("", getAddr("devAgLinkAddr")));
	  // Click on Devices tag to restore
	  TestUtil.clickOnElement(dtAddr+"/div/div/a", 500);
	  TestUtil.clickOnElement(dtAddr+"/div/div/ul/li/a[1]", 500);
  
	  TestUtil.mySleep(3000);
	  LOG.info("INFO: Exiting checkDevTagTest()");
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
      //Open user drop-down to show logout tab
      TestUtil.clickOnElement(getAddr("userAddr"), 500);
      TestUtil.clickOnElement(getAddr("logoutAddr"), 500);
	  Thread.sleep(500);
	  // Close all windows
	  //driver.quit();  // delay till end of all tests
	  LOG.info("INFO: Exiting afterTest end()");
  }
  
  @AfterSuite
  public void quit() {
	  
	  LOG.info("INFO: Entering AfterSuite quit()");
	  // Close all windows
	  driver.quit();
	  LOG.info("INFO: Exiting AfterSuite quit()");
  }
}
