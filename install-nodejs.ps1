# 下载Node.js安装程序
$nodeVersion = "20.11.0"
$downloadUrl = "https://nodejs.org/dist/v$nodeVersion/node-v$nodeVersion-x64.msi"
$installerPath = "$env:USERPROFILE\Downloads\node-installer.msi"

Write-Host "正在下载Node.js v$nodeVersion..."
Invoke-WebRequest -Uri $downloadUrl -OutFile $installerPath

# 安装Node.js
Write-Host "正在安装Node.js..."
Start-Process msiexec.exe -Wait -ArgumentList "/i `"$installerPath`" /quiet"

# 验证安装
Write-Host "验证安装..."
$env:Path = [System.Environment]::GetEnvironmentVariable("Path","Machine") + ";" + [System.Environment]::GetEnvironmentVariable("Path","User")
node --version
npm --version

Write-Host "Node.js安装完成！"
