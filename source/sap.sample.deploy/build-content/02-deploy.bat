set BOE_HOME="E:\Program Files (x86)\SAP BusinessObjects\SAP BusinessObjects Enterprise XI 4.0\"

rem copy %BOE_HOME%\dataAccess\connectionServer\drivers\java\cms_extensions\com.sap.dbs.cmsdbdriver.plugin.javascript.jar ..\deploy\plugins-DataAccess
rem copy %BOE_HOME%\dataAccess\connectionServer\drivers\java\cms_extensions\com.sap.dbs.services.jar ..\deploy\plugins-DataAccess

rem copy %BOE_ADC%\Agent\plugins\task_bundle\com.sap.dbs.services.tt_1.0.0.jar ..\deploy\plugins-TaskTemplate
move content.lcmbiar ..\content
pause