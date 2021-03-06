package testSuiteA;

import java.io.IOException;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import base.TestBase;


public class restApiTest extends TestBase{
	
  @Test
  public void agentEndponitTest() {
	  
	  LOG.info("INFO: Entering agentEndpointTest()");
	  // Go to rest client test site
	  //getTestUrl("restTestSiteUrl");
	  String agUrl = getInput("agEndpointUrl");
	  // Enter Url to page
	  driver.get(agUrl);
	  // Get response text
	  String respText = driver.findElement(By.xpath(getAddr("respTextAddr"))).getText();
	  LOG.debug("DEBUG: got response text: "+respText);
	  // Validate response data
	  Assert.assertTrue(respText.contains("last ping"));
	  LOG.info("INFO: Exiting agentEndpointTest()");
  }


  @BeforeTest
  public void beforeTest() throws IOException {
	  
	  initialize();
  }

  @AfterTest
  public void close() throws Exception {
	  LOG.info("INFO: Entering AfterTest close()");
	  //driver.quit();
	  LOG.info("INFO: Exiting AfterTest close()");
  }
}
