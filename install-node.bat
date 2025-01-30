@echo off
echo 正在安装 Node.js...

REM 检查是否已经安装
where node >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    echo Node.js 已经安装，版本为：
    node -v
    goto :end
)

REM 安装 Node.js
echo 开始安装 Node.js...
msiexec /i "node-installer.msi" /passive ADDLOCAL=ALL

REM 等待安装完成
timeout /t 30 /nobreak

REM 刷新环境变量
set PATH=%PATH%;C:\Program Files\nodejs\

REM 验证安装
echo 验证安装...
call node -v
if %ERRORLEVEL% NEQ 0 (
    echo Node.js 安装失败
    exit /b 1
)

call npm -v
if %ERRORLEVEL% NEQ 0 (
    echo npm 安装失败
    exit /b 1
)

REM 设置npm镜像
call npm config set registry https://registry.npmmirror.com

echo Node.js 安装成功！
echo 请关闭此窗口并打开新的命令行窗口以确保环境变量生效。

:end
pause
