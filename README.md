# CMS Data Access Driver - Custom Samples

[![REUSE status](https://api.reuse.software/badge/github.com/SAP-samples/boe-custom-cms-data-access-driver)](https://api.reuse.software/info/github.com/SAP-samples/boe-custom-cms-data-access-driver)

## Description
The [Central Management Server Database Driver](https://wiki.scn.sap.com/wiki/display/BOBJ/Unlock+the+CMS+database+with+new+data+access+driver+for+BI+4.2)
 allows you to use a universe and native reporting clients to query the metadata of the  Central Management Server (CMS) repository database.
A `Standard Universe` and a set of standard `Web Intelligence documents` are available for CMS Reporting & Analysis.  
You can extend and customize the solution: 
 * **Information Design Tool** - add objects to the universe,  see Community Wiki [add simple objects](https://wiki.scn.sap.com/wiki/display/BOBJ/BIRA+universe%3A+01+-+add+simple+objects)
 * **Java Plug-in(s)** - add virtual tables to the Data Foundation of the CMS DB universe using the `CMS Data Access Driver SDK`
 
This project provides content to demonstrate use cases from different areas and can also be used as **Quick Start** for the development of additional `Virtual Tables`. 
 
 * **Get** - more sample `Web Intelligence documents` for CMS Reporting & Analysis  
 * **Learn** -  how easy `Custom Virtual Tables` can be developed and advanced functionality can be added to the `CMS Universe`  
 * **Try** - deploy and run the samples **on your BI platform**, get excited about the capabilities and inspired to build your own solutions  

 ## Table of Contents
[List of samples](#samples)  
[Requirements](#requirements)  
[Installation & Configuration](#installation)  
[Tutorials](#tutorials)  
[Known Issues](#knownissues)  
[Support](#support)  
[To-Do](#todo)  
[License](#license)  

<a name='samples'>

## List of samples

  Virtual Table          | Description
  ----------------- | -------------------------------------------------------------------------------------------------
  TemplateTableResults | Table with 2 Columns, returning the *<ID> and *<ID>: text of the associated InfoObject
   RestJSONTableResults | Table with the results of a JSON request, returning the response body information in the columns
  
  
<a name='requirements'>

## Requirements
* SAP BusinessObjects Business Intelligence Platform 4.2 SP04 or higher
* Central Management Server Database Driver installed
* Sample Universe `BI Platform CMS System Database Sample.lcmbiar` imported from `<BOE folder>\SAP BusinessObjects Enterprise XI 4.0\Samples\bionbi\`
* For development: Information Design Tool and Eclipse IDE 

<a name='installation'>

## Installation & Configuration
Clone or download the sample project and follow the instructions in [deploy](deploy/README.md). You will be able to test the samples on your BI platform.

<a name='tutorials'>

## Tutorials
* Fast and easy start in working with the samples, learn more about architecture, features & content and the development in [tutorials](tutorials/README.md)  
* Additional details and the source code of the samples can be found in [source](source) 

<a name='knownissues'>

## Known Issues
None


<a name='support'>

## Support
This project is provided "as-is": there is no guarantee that raised issues will be answered or addressed in future releases.

<a name='todo'>

## To-Do
It is planned to add 
* more samples for common CMS reporting use cases  
* more tutorials for the Java development, e.g. Simplify virtual table development using remote debugging in Eclipse and Information Design Tool  

<a name='license'>

## License
Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved. This file is licensed under the Apache Software License, version 2.0 except as noted otherwise in the [LICENSE](LICENSES/Apache-2.0.txt) file.
