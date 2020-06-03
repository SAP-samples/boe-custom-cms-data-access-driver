set SCRIPT="E:\Program Files (x86)\SAP BusinessObjects\SAP BusinessObjects Enterprise XI 4.0\win64_x64\scripts\lcm_cli.bat"

call %SCRIPT% -lcmproperty "%~dp0\content.export.properties" >>export2LCMBIAR.txt
pause