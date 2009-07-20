@echo off

set GROUPID=%1
set ARTIFACTID=%2
set VERSION=%3
set GROUPDIR=%GROUPID:.=\%
set DIR=C:\Dokumente und Einstellungen\Daniel Frey\.m2\repository-bak\%GROUPDIR%\%ARTIFACTID%\%VERSION%
set CURRENT=%~dp0

set JAR=%DIR%\%ARTIFACTID%-%VERSION%.jar
set SRC=%DIR%\%ARTIFACTID%-%VERSION%-sources.jar
set DOC=%DIR%\%ARTIFACTID%-%VERSION%-javadoc.jar

echo looking for %JAR%
echo looking for %SRC%
echo looking for %DOC%

set CMD=call mvn deploy:deploy-file -DgroupId=%GROUPID% -DartifactId=%ARTIFACTID% -Dversion=%VERSION% -Dpackaging=jar -DrepositoryId=local -Durl=file://%CURRENT%/repository

if exist "%JAR%" %CMD% -Dfile="%JAR%"
if exist "%SRC%" %CMD% -Dfile="%SRC%" -Dclassifier=sources
if exist "%DOC%" %CMD% -Dfile="%DOC%" -Dclassifier=javadoc
