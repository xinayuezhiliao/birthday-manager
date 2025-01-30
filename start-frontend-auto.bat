@echo off
cd frontend

REM Install dependencies
echo Installing dependencies...
call npm install

REM Start development server
echo Starting development server...
call npm run dev
