@echo off
echo Starting frontend project...

cd frontend

REM Check if node_modules exists
if not exist "node_modules\" (
    echo Installing dependencies...
    call npm install
    if errorlevel 1 (
        echo Failed to install dependencies
        pause
        exit /b 1
    )
)

REM Start the development server
echo Starting development server...
call npm run dev
if errorlevel 1 (
    echo Failed to start development server
    pause
    exit /b 1
)
