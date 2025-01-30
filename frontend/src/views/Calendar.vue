<template>
  <div class="calendar-view">
    <el-card>
      <template #header>
        <div class="card-header">
          <h2>
            <el-icon><Calendar /></el-icon>
            生日日历
          </h2>
        </div>
      </template>

      <el-calendar v-model="currentDate">
        <template #dateCell="{ data }">
          <div class="calendar-cell">
            <p :class="{ 'is-today': isToday(data.day) }">
              {{ data.day.split('-').slice(2).join('') }}
            </p>
            <div class="birthday-dots">
              <template v-for="birthday in getBirthdays(data.day)" :key="birthday.id">
                <el-tooltip
                  :content="formatBirthdayTooltip(birthday)"
                  placement="top"
                  effect="light"
                >
                  <div class="birthday-dot" :style="{ backgroundColor: getRelationshipColor(birthday.relationship) }" />
                </el-tooltip>
              </template>
            </div>
          </div>
        </template>
      </el-calendar>
    </el-card>

    <!-- 图例说明 -->
    <el-card class="legend-card">
      <div class="legend">
        <div class="legend-item">
          <div class="birthday-dot" style="background-color: #409EFF"></div>
          <span>家人</span>
        </div>
        <div class="legend-item">
          <div class="birthday-dot" style="background-color: #67C23A"></div>
          <span>朋友</span>
        </div>
        <div class="legend-item">
          <div class="birthday-dot" style="background-color: #E6A23C"></div>
          <span>同事</span>
        </div>
        <div class="legend-item">
          <div class="birthday-dot" style="background-color: #909399"></div>
          <span>其他</span>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Calendar } from '@element-plus/icons-vue'
import { format, isToday as _isToday } from 'date-fns'
import { zhCN } from 'date-fns/locale'
import { mockApi } from '../mock/contacts'

const currentDate = ref(new Date())
const birthdays = ref([])

const isToday = (day) => {
  return _isToday(new Date(day))
}

const getBirthdays = (day) => {
  return birthdays.value.filter(b => {
    const bDay = new Date(b.birthday)
    const cellDay = new Date(day)
    return bDay.getMonth() === cellDay.getMonth() && 
           bDay.getDate() === cellDay.getDate()
  })
}

const getRelationshipColor = (relationship) => {
  const colors = {
    '家人': '#409EFF',
    '朋友': '#67C23A',
    '同事': '#E6A23C',
    '其他': '#909399'
  }
  return colors[relationship] || colors['其他']
}

const formatBirthdayTooltip = (birthday) => {
  const age = new Date().getFullYear() - new Date(birthday.birthday).getFullYear()
  return `${birthday.name} (${birthday.relationship})\n${age}岁生日`
}

onMounted(async () => {
  try {
    birthdays.value = await mockApi.getAllContacts()
  } catch (error) {
    console.error('获取生日数据失败:', error)
  }
})
</script>

<style scoped>
.calendar-view {
  padding: 20px;
}

.card-header {
  display: flex;
  align-items: center;
}

.card-header h2 {
  margin: 0;
  font-size: 18px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.calendar-cell {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 4px;
}

.calendar-cell p {
  margin: 0;
  text-align: center;
}

.calendar-cell .is-today {
  color: var(--el-color-primary);
  font-weight: bold;
}

.birthday-dots {
  display: flex;
  flex-wrap: wrap;
  gap: 2px;
  justify-content: center;
  margin-top: 4px;
}

.birthday-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
}

.legend-card {
  margin-top: 20px;
}

.legend {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.legend-item .birthday-dot {
  width: 8px;
  height: 8px;
}

.legend-item span {
  font-size: 14px;
  color: #666;
}
</style>
