@echo off
echo Starting Birthday Manager Frontend...

cd frontend

REM Install dependencies if needed
if not exist "node_modules\" (
    echo Installing dependencies...
    "C:\Program Files\nodejs\npm.cmd" install
)

REM Start development server
echo Starting development server...
"C:\Program Files\nodejs\node.exe" .\node_modules\vite\bin\vite.js

echo If the browser doesn't open automatically, visit http://localhost:5173
