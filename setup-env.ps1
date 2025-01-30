# 检查Node.js安装路径
$nodePath = "C:\Program Files\nodejs"
$nodeModulesPath = "$nodePath\node_modules"
$npmPath = "$nodePath\node_modules\npm\bin"

Write-Host "Setting up Node.js environment..."

# 添加到系统Path
$currentPath = [Environment]::GetEnvironmentVariable("Path", "Machine")
if (-not $currentPath.Contains($nodePath)) {
    [Environment]::SetEnvironmentVariable("Path", "$currentPath;$nodePath", "Machine")
}
if (-not $currentPath.Contains($npmPath)) {
    [Environment]::SetEnvironmentVariable("Path", "$currentPath;$npmPath", "Machine")
}

# 添加到用户Path
$userPath = [Environment]::GetEnvironmentVariable("Path", "User")
if (-not $userPath.Contains($nodePath)) {
    [Environment]::SetEnvironmentVariable("Path", "$userPath;$nodePath", "User")
}

# 刷新当前会话的Path
$env:Path = [Environment]::GetEnvironmentVariable("Path", "Machine") + ";" + [Environment]::GetEnvironmentVariable("Path", "User")

Write-Host "Environment variables updated. Testing Node.js and npm..."

# 测试Node.js和npm
try {
    $nodeVersion = & node -v
    Write-Host "Node.js version: $nodeVersion"
} catch {
    Write-Host "Error: Node.js not found. Please reinstall Node.js"
    exit 1
}

try {
    $npmVersion = & npm -v
    Write-Host "npm version: $npmVersion"
} catch {
    Write-Host "Error: npm not found. Please reinstall Node.js"
    exit 1
}

Write-Host "Setup completed successfully!"
