"# aws-auto" 

Project Description:

This project has one TestNG testcase which automates the 
CreateInstance - StopInstance and TerminateInstance workflow.
TestNG is used to have better control over supporting differnt resource configs through testng xmls and it 
also helps better management of test execution when the number of testcases sets to grow.
The below command can be wrapped in a shell, powershell, python or gruntscript
mvn clean test -DsuiteXmlFile=testngxml/testngxml.xml

Note to run the project:
aws credential profile needs to be configured on the build agent.

TODOs:
TesngXml can be created to support different instance configurations(eg number of CPUS,image Ids etc).
All hardcoded values needs to be parameterized.
Refactoring needs to be done and not Test method functions can be moved under commons folder.
Malformed id related exception needs to be investigated and fixed.
 
