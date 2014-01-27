This is test automation framework, a maven project in java, mainly for testing Electric Imp's IDE, using Selenium, TestNG.

Currently the framework resides at: https://github.com/imp-hfiennes/test-automation, and the following steps will show you the how-tos.

Step1. To clone the framework from github to your local directory:

    "git clone git@github.com:imp-hfiennes/test-automation.git your-dir"

Step2. To run the tests:

    "cd project-root", and "mvn clean package"

Step3. To check test results:

    From project-root, double-click on "target/surefire-reports/index.html"

Step4. To switch test system type (staging or production):

    In "src/test/resources/config/testData.properties", comment out one sysType and un-comment the
    other. For example, to run on production:
    ...
    #sysType=ST
    sysType=PR
    ...

Step5. To browser the code, from project-root:

     pom.xml                                       #"make" file of project
     src/test/resources/testng.xml                 #test execution config file, referred in pom.xml
     src/test/resources/testSuiteA.xml             #sub test execution config file nested in testng.xml
     src/test/resources/config/OR.properties       #Page elements' locations/addresses
     src/test/resources/config/testData.properties #test input data
     src/test/resources/log4j.properties           #log4j setup
     src/test/resources/logs/LogFile.logs          #log file
     src/test/java/base/TestBase.java              #Base class inherited by all test classes
     src/test/java/testSuiteA/IdeTest.java         #IDE tests (with most tests' source code)
     src/test/java/testSuiteA/restApiTest.java     #Agent endpoint api tests (only one test for now)
     src/test/java/Util/TestUtil.java              #Utility methods
     target/surefire-reports/index.html            #Test results

History:
12/30/2013: First pushed to github test repo, @Incline Village, Lake Tahoe.
01/02/2014: Checked in the 2nd round of updates to EI automation project.
01/07/2014: Pushed to EI repo (https://github.com/imp-hfiennes/test-automation)
01/08/2014: Made a job in Jenkins, and ran the tests successfuly.
01/24/2014: checked in IDE lastest changes due to massive UI update.This is test automation framework (acutually a Java project) for testing Electric Imp's IDE, using Selenium, TestNG. 
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
