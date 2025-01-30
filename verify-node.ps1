# 刷新环境变量
$env:Path = [System.Environment]::GetEnvironmentVariable("Path","Machine") + ";" + [System.Environment]::GetEnvironmentVariable("Path","User")

Write-Host "正在验证Node.js安装..."
try {
    $nodeVersion = node -v
    $npmVersion = npm -v
    
    Write-Host "Node.js版本: $nodeVersion"
    Write-Host "npm版本: $npmVersion"
    Write-Host "Node.js和npm安装成功！"
    
    # 设置npm镜像为淘宝镜像，加快下载速度
    npm config set registry https://registry.npmmirror.com
    Write-Host "npm镜像已设置为淘宝镜像"
    
} catch {
    Write-Host "安装验证失败: $_"
    exit 1
}
