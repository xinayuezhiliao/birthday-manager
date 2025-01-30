<template>
  <el-container class="app-container">
    <el-header class="app-header">
      <div class="logo">
        <img src="./assets/logo.png" alt="生日管家" />
        <h1>生日管家</h1>
      </div>
      <el-menu
        mode="horizontal"
        :router="true"
        class="nav-menu"
      >
        <el-menu-item index="/">首页</el-menu-item>
        <el-menu-item index="/add">添加联系人</el-menu-item>
        <el-menu-item index="/calendar">生日日历</el-menu-item>
      </el-menu>
    </el-header>
    
    <el-main class="app-main">
      <router-view></router-view>
    </el-main>
    
    <el-footer class="app-footer">
      <p>生日管家 &copy; 2025 - 让每个生日都被温暖记住</p>
    </el-footer>
  </el-container>
</template>

<script setup>
</script>

<style>
html, body {
  margin: 0;
  padding: 0;
  height: 100%;
  font-family: 'Noto Sans SC', sans-serif;
}

#app {
  height: 100%;
}

.app-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.app-header {
  background: linear-gradient(135deg, #4b6cb7 0%, #182848 100%);
  color: white;
  padding: 0 20px;
  height: 60px !important;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
}

.logo img {
  height: 40px;
  width: 40px;
  object-fit: contain;
}

.logo h1 {
  margin: 0;
  font-size: 24px;
  font-weight: 500;
}

.nav-menu {
  border: none;
  background: transparent;
}

.el-menu--horizontal > .el-menu-item {
  color: white !important;
  font-size: 16px;
  border-bottom: none !important;
}

.el-menu--horizontal > .el-menu-item:hover,
.el-menu--horizontal > .el-menu-item.is-active {
  background-color: rgba(255, 255, 255, 0.1) !important;
  color: white !important;
  border-bottom: 2px solid white !important;
}

.app-main {
  flex: 1;
  background: #f5f7fa;
  padding: 0;
}

.app-footer {
  background: #2c3e50;
  color: white;
  text-align: center;
  padding: 20px;
  height: auto !important;
}
</style>
