# 下载Node.js安装程序
$nodeVersion = "20.11.0"
$downloadUrl = "https://nodejs.org/dist/v$nodeVersion/node-v$nodeVersion-x64.msi"
$installerPath = "$env:TEMP\node-installer.msi"

Write-Host "Downloading Node.js installer..."
Invoke-WebRequest -Uri $downloadUrl -OutFile $installerPath

# 卸载现有的Node.js（如果存在）
Write-Host "Removing existing Node.js installation..."
Start-Process msiexec.exe -Wait -ArgumentList "/x {1E78F2E4-9F32-4434-8A6A-2D7C8B5C2BF1} /qn"

# 安装Node.js
Write-Host "Installing Node.js..."
$arguments = "/i `"$installerPath`" /qn ADDLOCAL=ALL"
$process = Start-Process msiexec.exe -ArgumentList $arguments -Wait -PassThru

# 等待安装完成
Start-Sleep -Seconds 30

# 刷新环境变量
$env:Path = [System.Environment]::GetEnvironmentVariable("Path","Machine") + ";" + [System.Environment]::GetEnvironmentVariable("Path","User")

# 验证安装
Write-Host "Verifying installation..."
try {
    $nodePath = (Get-Command node).Path
    $npmPath = (Get-Command npm).Path
    
    Write-Host "Node.js installed at: $nodePath"
    Write-Host "npm installed at: $npmPath"
    
    $nodeVersion = node -v
    $npmVersion = npm -v
    
    Write-Host "Node.js version: $nodeVersion"
    Write-Host "npm version: $npmVersion"
    
    # 设置npm镜像
    npm config set registry https://registry.npmmirror.com
    Write-Host "npm mirror configured"
    
    Write-Host "Installation successful!"
} catch {
    Write-Host "Installation verification failed: $_"
    exit 1
}
