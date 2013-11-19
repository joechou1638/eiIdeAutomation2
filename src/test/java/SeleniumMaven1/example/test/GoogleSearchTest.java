package SeleniumMaven1.example.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class GoogleSearchTest {

    protected WebDriver driver;
    private StringBuffer verificationErrors = new StringBuffer();

    @BeforeTest
    public void setUp(){
    	driver = new FirefoxDriver();
    	driver.get("http://www.google.com");
    }

    @Test
    public void testGoogleSearch() {
    	System.out.println("Entering testGoogleSearch()-----");
    	try {
            // Find the text input element by its name
            WebElement element = driver.findElement(By.name("q")); 
            // Enter something to search for
            element.sendKeys("selenium testing tools cookbook");      
            // Now submit the form. WebDriver will find
            //the form for us from the element
            element.submit();
            // Google's search is rendered dynamically
            //with JavaScript.
            // Wait for the page to load, timeout after
            //10 seconds
            (new WebDriverWait(driver, 10)).until
                 (new ExpectedCondition<Boolean>() {
                	  public Boolean apply(WebDriver d) {
                		  return d.getTitle().toLowerCase().startsWith("selenium testing tools cookbook");                
                	  }
                 }
            );
            // Should see: selenium testing tools
            //cookbook - Google Search
            System.out.println("title: "+driver.getTitle());
            assertEquals("selenium testing tools cookbook - Google Search", driver.getTitle());          
         } catch (Error e) {
             //Capture and append Exceptions/Errors
             verificationErrors.append(e.toString());
         }
    	System.out.println("Exiting testGoogleSearch()-----");
    	/*
    	 finally {
    		 System.out.println("Close all opened windows before exit.");
    		 driver.quit();
    	 }
    	 */
    }

    @AfterTest
    public void tearDown() throws Exception {

        //Close the browser
    	System.out.println("calling driver.quit()");
	    driver.quit();
        String verificationErrorString = verificationErrors.toString();  
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        } 

    }

}