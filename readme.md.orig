This is test automation framework (acutually a Java project) for testing Electric Imp's IDE, using Selenium, TestNG. 
Currently the framework resides at: https://github.com/imp-hfiennes/test-automation.

To clone the framework from github repo to a local dir:
git clone git@github.com:imp-hfiennes/test-automation.git your-dir

To run the tests:
Go to your-dir and run command:
mvn clean package

To check test results:
double-click on target/surefire-reports/index.html

To read the code: 
src/test/java/base/TestBase.java  #Base class code inherited by all tests
             /testSuiteA/IdeTest.java #IDE tests
                        /restApiTest.java #Agent endpoint api tests 
             /Util/TestUtil.java #Utility methods
        /resources/config/OR.properties #Web page object locations/addresses 
                          config.properties #system configuration data
 		   	  testData.properties #test input data
                  /log4j.properties  #log4j setup
		  /testng.xml   #test execution config file
                  /testSuiteA.xml  #sub test execution config file nested in testng.xml
                  /logs/LogFile.logs    #log file

To switch test system type from staging to production:
open src/test/resources/config/testData.properties
comment out system type staging, and un-comment production as below:
...
#sysType=ST
sysType=PR
...



History:
12/30/2013: First pushed to github test repo, @Incline Village, Lake Tahoe.
01/02/2014: Checked in the 2nd round of updates to EI automation project.
01/07/2014: Pushed to EI repo (https://github.com/imp-hfiennes/test-automation)
01/08/2014: Made a job in Jenkins, and ran the tests successfuly.
