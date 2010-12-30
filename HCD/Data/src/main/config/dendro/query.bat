@echo off

cp "C:\Dokumente und Einstellungen\Daniel Frey\.m2\repository\hsqldb\hsqldb\1.7.1\hsqldb-1.7.1.jar" .
set CP=-cp hsqldb-1.7.1.jar
set TOOL=org.hsqldb.util.QueryTool
REM set URL=-url jdbc:hsqldb:D:\Daten\All\Sources\HCD\Data\src\main\config\dendro\
set URL=-url jdbc:hsqldb:D:\Daten\All\Sources\HCD\Data\src\main\resources\
set DRIVER=-driver org.hsqldb.jdbcDriver
REM set DB=-database dendro
set DB=-database hcdsqlsc
set USR=-user sa

set CL=%CP% %TOOL% %URL% %DRIVER% %DB% %USR%

java %CL%

rm hsqldb-1.7.1.jar