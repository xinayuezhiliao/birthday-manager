# 设置执行策略
Set-ExecutionPolicy Bypass -Scope Process -Force

# 下载Node.js安装程序
Write-Host "正在下载Node.js安装程序..."
$nodeVersion = "20.11.0"
$downloadUrl = "https://nodejs.org/dist/v$nodeVersion/node-v$nodeVersion-x64.msi"
$installerPath = "node-installer.msi"
Invoke-WebRequest -Uri $downloadUrl -OutFile $installerPath

# 安装Node.js（使用passive模式，不需要用户交互）
Write-Host "正在安装Node.js..."
$process = Start-Process msiexec.exe -ArgumentList "/i `"$installerPath`" /passive ADDLOCAL=ALL" -Wait -PassThru

# 刷新环境变量
$env:Path = [System.Environment]::GetEnvironmentVariable("Path","Machine") + ";" + [System.Environment]::GetEnvironmentVariable("Path","User")

# 添加Node.js路径到当前会话
$env:Path = "$env:Path;C:\Program Files\nodejs"

# 等待Node.js安装完成
Write-Host "等待Node.js安装完成..."
Start-Sleep -Seconds 10

# 验证安装
Write-Host "验证Node.js安装..."
try {
    $nodePath = (Get-Command node -ErrorAction Stop).Path
    $npmPath = (Get-Command npm -ErrorAction Stop).Path
    
    Write-Host "Node.js已安装在: $nodePath"
    Write-Host "npm已安装在: $npmPath"
    
    $nodeVersion = node -v
    $npmVersion = npm -v
    
    Write-Host "Node.js版本: $nodeVersion"
    Write-Host "npm版本: $npmVersion"
    
    # 配置npm使用淘宝镜像
    npm config set registry https://registry.npmmirror.com
    Write-Host "npm已配置为使用淘宝镜像"
    
    Write-Host "Node.js和npm安装成功！"
} catch {
    Write-Host "安装验证失败: $_"
    exit 1
}
