@echo off
echo Installing Node.js...

REM Install Node.js silently
msiexec /i "node-installer.msi" /passive ADDLOCAL=ALL

REM Wait for installation
timeout /t 10 /nobreak > nul

REM Add Node.js to PATH for current session
set PATH=%PATH%;C:\Program Files\nodejs\

REM Configure npm to use taobao mirror
call npm config set registry https://registry.npmmirror.com

echo Installation completed!
