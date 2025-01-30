<template>
  <div class="home">
    <el-row :gutter="20">
      <!-- 左侧：最近生日提醒 -->
      <el-col :span="16">
        <el-card class="upcoming-birthdays">
          <template #header>
            <div class="card-header">
              <h2>
                <el-icon><Calendar /></el-icon>
                即将到来的生日
              </h2>
            </div>
          </template>
          
          <el-timeline>
            <el-timeline-item
              v-for="contact in upcomingBirthdays"
              :key="contact.id"
              :timestamp="formatDate(contact.birthday)"
              :type="getTimelineItemType(contact.daysUntil)"
            >
              <el-card class="birthday-card">
                <div class="contact-info">
                  <el-avatar :size="50" :src="contact.avatar">
                    {{ getInitials(contact.name) }}
                  </el-avatar>
                  <div class="contact-details">
                    <h3>{{ contact.name }}</h3>
                    <p>{{ contact.relationship }}</p>
                    <p class="days-until">
                      还有 {{ contact.daysUntil }} 天
                    </p>
                  </div>
                </div>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>

      <!-- 右侧：快捷操作 -->
      <el-col :span="8">
        <el-card class="quick-actions">
          <template #header>
            <div class="card-header">
              <h2>
                <el-icon><Operation /></el-icon>
                快捷操作
              </h2>
            </div>
          </template>
          
          <div class="action-buttons">
            <el-button type="primary" @click="$router.push('/add')" :icon="Plus">
              添加新联系人
            </el-button>
            <el-button type="success" @click="$router.push('/calendar')" :icon="Calendar">
              查看生日日历
            </el-button>
          </div>
        </el-card>

        <!-- 统计信息 -->
        <el-card class="statistics">
          <template #header>
            <div class="card-header">
              <h2>
                <el-icon><DataLine /></el-icon>
                统计信息
              </h2>
            </div>
          </template>
          
          <el-row :gutter="20" class="stat-row">
            <el-col :span="12">
              <div class="stat-item">
                <h3>本月生日</h3>
                <p class="stat-number">{{ stats.thisMonth }}</p>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="stat-item">
                <h3>下月生日</h3>
                <p class="stat-number">{{ stats.nextMonth }}</p>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="stat-item">
                <h3>总生日数</h3>
                <p class="stat-number">{{ stats.total }}</p>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Calendar, Operation, DataLine, Plus } from '@element-plus/icons-vue'
import { format, differenceInDays, addYears } from 'date-fns'
import { zhCN } from 'date-fns/locale'
import { birthdayApi } from '../api/birthday'
import { ElMessage } from 'element-plus'

const upcomingBirthdays = ref([])
const stats = ref({
  thisMonth: 0,
  nextMonth: 0,
  total: 0
})

// 格式化日期
const formatDate = (date) => {
  return format(new Date(date), 'MM月dd日', { locale: zhCN })
}

// 获取时间线项目类型
const getTimelineItemType = (daysUntil) => {
  if (daysUntil <= 7) return 'danger'
  if (daysUntil <= 30) return 'warning'
  return 'primary'
}

// 获取姓名缩写
const getInitials = (name) => {
  return name.charAt(0)
}

// 计算距离下一个生日的天数
const calculateDaysUntil = (birthDate) => {
  const today = new Date()
  const birth = new Date(birthDate)
  const thisYearBirthday = new Date(today.getFullYear(), birth.getMonth(), birth.getDate())
  
  if (thisYearBirthday < today) {
    return differenceInDays(addYears(thisYearBirthday, 1), today)
  }
  return differenceInDays(thisYearBirthday, today)
}

onMounted(async () => {
  try {
    // 获取即将到来的生日
    const response = await birthdayApi.getUpcomingBirthdays(60)
    upcomingBirthdays.value = response.data.map(birthday => ({
      id: birthday.id,
      name: birthday.name,
      relationship: birthday.relationship,
      birthday: birthday.birthDate,
      daysUntil: calculateDaysUntil(birthday.birthDate)
    })).sort((a, b) => a.daysUntil - b.daysUntil)

    // 更新统计信息
    const allBirthdays = await birthdayApi.getAllBirthdays()
    const today = new Date()
    const currentMonth = today.getMonth()
    const nextMonth = (currentMonth + 1) % 12

    stats.value = {
      thisMonth: allBirthdays.data.filter(b => new Date(b.birthDate).getMonth() === currentMonth).length,
      nextMonth: allBirthdays.data.filter(b => new Date(b.birthDate).getMonth() === nextMonth).length,
      total: allBirthdays.data.length
    }
  } catch (error) {
    ElMessage.error('获取数据失败，请稍后重试')
    console.error('Error fetching data:', error)
  }
})
</script>

<style scoped>
.home {
  max-width: 1400px;
  margin: 0 auto;
  padding: 30px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8eb 100%);
  min-height: calc(100vh - 100px);
  animation: fadeIn 0.5s ease-in-out;
}

.el-row {
  margin: 0 !important;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

.el-card {
  height: 100%;
  border-radius: 15px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.el-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.card-header {
  display: flex;
  align-items: center;
  background: linear-gradient(45deg, #4b6cb7 0%, #182848 100%);
  margin: -20px -20px 20px -20px;
  padding: 15px 20px;
  border-radius: 15px 15px 0 0;
  color: white;
}

.card-header h2 {
  margin: 0;
  font-size: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.card-header .el-icon {
  font-size: 24px;
  animation: bounce 2s infinite;
}

@keyframes bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-5px); }
}

.upcoming-birthdays {
  height: calc(100vh - 160px);
  overflow-y: auto;
  margin-bottom: 0;
}

.upcoming-birthdays::-webkit-scrollbar {
  width: 6px;
}

.upcoming-birthdays::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.upcoming-birthdays::-webkit-scrollbar-thumb {
  background: #4b6cb7;
  border-radius: 3px;
}

.birthday-card {
  margin-bottom: 15px;
  border-radius: 10px;
  transition: all 0.3s ease;
}

.birthday-card:hover {
  transform: scale(1.02);
}

.contact-info {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 10px;
}

.el-avatar {
  border: 3px solid #4b6cb7;
  transition: transform 0.3s ease;
}

.el-avatar:hover {
  transform: rotate(360deg);
}

.contact-details {
  flex: 1;
}

.contact-details h3 {
  margin: 0 0 8px 0;
  color: #2c3e50;
  font-size: 18px;
}

.contact-details p {
  margin: 5px 0;
  color: #666;
}

.days-until {
  color: #e74c3c !important;
  font-weight: bold;
  font-size: 16px;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.05); }
  100% { transform: scale(1); }
}

.quick-actions {
  margin-bottom: 20px;
}

.action-buttons {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.action-buttons .el-button {
  width: 100%;
  height: 45px;
  font-size: 16px;
  border-radius: 10px;
  transition: all 0.3s ease;
}

.action-buttons .el-button:hover {
  transform: translateX(5px);
}

.statistics {
  background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
}

.stat-row {
  padding: 10px 0;
}

.stat-item {
  text-align: center;
  padding: 15px;
  background: rgba(75, 108, 183, 0.1);
  border-radius: 10px;
  transition: all 0.3s ease;
}

.stat-item:hover {
  background: rgba(75, 108, 183, 0.2);
  transform: translateY(-5px);
}

.stat-item h3 {
  margin: 0 0 10px 0;
  color: #34495e;
  font-size: 16px;
}

.stat-number {
  margin: 0;
  font-size: 28px;
  font-weight: bold;
  background: linear-gradient(45deg, #4b6cb7, #182848);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.el-timeline-item {
  position: relative;
}

.el-timeline-item:hover::before {
  content: '';
  position: absolute;
  left: -2px;
  top: 0;
  height: 100%;
  width: 4px;
  background: #4b6cb7;
  animation: slideIn 0.3s ease-out;
}

@keyframes slideIn {
  from { transform: scaleY(0); }
  to { transform: scaleY(1); }
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .home {
    padding: 20px;
  }
}

@media (max-width: 768px) {
  .el-row {
    margin: 0 !important;
  }
  
  .el-col {
    padding: 0 !important;
  }
  
  .home {
    padding: 10px;
  }
  
  .card-header h2 {
    font-size: 18px;
  }
  
  .stat-number {
    font-size: 24px;
  }

  .upcoming-birthdays {
    height: auto;
    max-height: 600px;
  }
}
</style>
