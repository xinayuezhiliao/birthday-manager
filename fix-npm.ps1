# 检查并修复 Node.js 环境

$nodejsPath = "C:\Program Files\nodejs"
$npmPath = "$nodejsPath\node_modules\npm"

Write-Host "正在检查 Node.js 安装..."

# 1. 检查核心文件
if (-not (Test-Path "$nodejsPath\node.exe")) {
    Write-Host "错误: 未找到 node.exe"
    exit 1
}

# 2. 检查 npm
if (-not (Test-Path "$nodejsPath\npm.cmd")) {
    Write-Host "错误: 未找到 npm.cmd，尝试修复..."
    # 尝试重新安装 npm
    & "$nodejsPath\node.exe" "$nodejsPath\node_modules\npm\bin\npm-cli.js" install -g npm
}

# 3. 修复环境变量
$paths = @(
    $nodejsPath,
    "$env:APPDATA\npm"
)

$currentPath = [Environment]::GetEnvironmentVariable("Path", "Machine")
$pathsToAdd = @()

foreach ($path in $paths) {
    if (-not $currentPath.Contains($path)) {
        $pathsToAdd += $path
    }
}

if ($pathsToAdd.Count -gt 0) {
    $newPath = $currentPath + ";" + ($pathsToAdd -join ";")
    [Environment]::SetEnvironmentVariable("Path", $newPath, "Machine")
    $env:Path = $newPath
}

# 4. 验证安装
Write-Host "验证 Node.js 和 npm..."
& "$nodejsPath\node.exe" -v
& "$nodejsPath\npm.cmd" -v

Write-Host "修复完成。请重新打开 PowerShell 窗口测试 npm 命令。"
