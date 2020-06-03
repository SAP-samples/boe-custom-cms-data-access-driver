# Installation & Configuration

1. Download the sample
1. Copy the Custom Data Access Driver plug-ins to the Data Access folder of your SAP BI platform server
1. Deploy the content using the Promotion Management in the Central Management Console
1. Test the sample Web Intelligence document and universe

### Table of Contents  
[Preparation](#prepare)  
[Install plug-ins](#install)  
[Deploy content](#deploy)  
[Test content - Web Intelligence](#test-webi)    
[Test content - Universe](#test-unv)  

<a name='prepare'>  

## Preparation  
1. Clone or download the sample project.  
1. If you download the ZIP, please extract the files.   
1. Use the files from folder `..\deploy` in the next steps.


<a name='install'>  

## Install plug-ins  
1. Copy the Custom Data Access plug-ins to your system  
   **Source**:  `<your folder>\deploy\install\SAP BusinessObjects Enterprise XI 4.0\dataAccess`  
   **Target**: `<BOE folder>\SAP BusinessObjects Enterprise XI 4.0\dataAccess`  
   ![](z-images/install-Copy.png)
   
 1. Check the target folder for JAR files  
   ![](z-images/install-Check.png)
 
 1. Open the CMC and restart the Web Intelligence Process server for changes to take effect  
   ![](z-images/install-RestartWPS.png)
 
 
<a name='deploy'>  

## Deploy content
1. Open the CMC  
1. Select Promotion Management  
  
1. Select Menu &gt; Import &gt; Import file - choose Browse  
![](z-images/PM-importFile.png)  

1. Select the sample content file (LCMBIAR) - choose Open - choose Ok  
![](z-images/PM-browseLCMBIAR.png)  

1. Enter your System as Destination - choose Create  
![](z-images/PM-Destination.png)  

1. Select Menu &gt; Promote - choose Promote  
![](z-images/PM-Promote.png)  

1. Select the Job in the list - choose Menu &gt; History  
The status should be success  
![](z-images/PM-JobHistory.png)  

<a name='test-webi'>  

## Test content - Web Intelligence

1. Open the BI Launchpad  

1. Select Documents - Folders - SAP Samples - BI on BI  

1. View Document `Custom Data Access - Sample- Tutorial Results`  
![](z-images/Test-browseWebi.png)

1. Refresh the Document  
Column No and Text will show the results of the tutorial table.  
![](z-images/Test-refreshWebi.png)


<a name='test-unv'>  

## Test content - Universe

1. Open the Information Design Tool  

1. Create a new Project: BI on BI - sample  
![](z-images/Unv-CreateProject.png)

1. Connect to your Repository  
![](z-images/Unv-Repository.png)

1. Retrieve the sample universe - Select Local Project: BI on BI - sample  
![](z-images/Unv-Import.png)

1. Check the Business Layer and Data Foundation  
Contains the `Tutorial Results` Objects and Table  
![](z-images/Unv-Content.png)

1. Check the Queries  
Contains the Query `Test-Sample`
![](z-images/Unv-Query.png)

1. Choose Execute Query to show the results  
![](z-images/Unv-Results.png)
