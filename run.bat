@echo off
set PATH=%USERPROFILE%\apache-maven\apache-maven-3.9.6\bin;%PATH%
call mvn clean install
if %ERRORLEVEL% EQU 0 (
    echo Build successful! Starting the application...
    java -jar target/birthday-manager-1.0-SNAPSHOT.jar
) else (
    echo Build failed! Please check the error messages above.
    pause
)
