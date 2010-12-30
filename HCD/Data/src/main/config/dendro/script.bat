@echo off
if "%1x" == "x" goto usage
goto doit

:usage
echo Missing argument
echo Usage: %0 ^<script to run^>
goto end

:doit
cp "C:\Dokumente und Einstellungen\Daniel Frey\.m2\repository\hsqldb\hsqldb\1.7.1\hsqldb-1.7.1.jar" .
set CP=-cp hsqldb-1.7.1.jar
set TOOL=org.hsqldb.util.ScriptTool
set URL=-url jdbc:hsqldb:D:\Daten\All\Sources\HCD\Data\src\main\config\dendro\
set DRIVER=-driver org.hsqldb.jdbcDriver
set DB=-database dendro
set USR=-user sa
set CL=%CP% %TOOL% %URL% %DRIVER% %DB% %USR% -script

echo java %CL% %1
java %CL% %1 > log.txt
grep Error log.txt
grep "update count" log.txt

rm hsqldb-1.7.1.jar
set CP=
set TOOL=
set URL=
set DRIVER=
set DB=
set USR=
set ARGS=
:end
