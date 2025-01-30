<template>
  <div class="add-contact">
    <el-card>
      <template #header>
        <div class="card-header">
          <h2>
            <el-icon><UserPlus /></el-icon>
            添加新联系人
          </h2>
        </div>
      </template>

      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        class="contact-form"
      >
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入联系人姓名" />
        </el-form-item>

        <el-form-item label="生日" prop="birthDate">
          <el-date-picker
            v-model="form.birthDate"
            type="date"
            placeholder="选择生日日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>

        <el-form-item label="关系" prop="relationship">
          <el-select v-model="form.relationship" placeholder="选择关系类型">
            <el-option label="家人" value="家人" />
            <el-option label="朋友" value="朋友" />
            <el-option label="同事" value="同事" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>

        <el-form-item label="备注" prop="notes">
          <el-input
            v-model="form.notes"
            type="textarea"
            placeholder="添加一些备注信息"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm(formRef)" :loading="loading">
            保存联系人
          </el-button>
          <el-button @click="$router.push('/')">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { UserPlus, Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { birthdayApi } from '../api/birthday'
import { useRouter } from 'vue-router'

const router = useRouter()
const formRef = ref()
const loading = ref(false)

const form = reactive({
  name: '',
  birthDate: '',
  relationship: '',
  notes: ''
})

const rules = {
  name: [
    { required: true, message: '请输入联系人姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  birthDate: [
    { required: true, message: '请选择生日日期', trigger: 'change' }
  ],
  relationship: [
    { required: true, message: '请选择关系类型', trigger: 'change' }
  ]
}

const submitForm = async (formEl) => {
  if (!formEl) return
  
  try {
    await formEl.validate()
    loading.value = true
    
    await birthdayApi.createBirthday({
      name: form.name,
      birthDate: form.birthDate,
      relationship: form.relationship,
      notes: form.notes || ''
    })
    
    ElMessage.success('联系人添加成功！')
    router.push('/')
  } catch (error) {
    if (error.name === 'ValidationError') {
      ElMessage.warning('请检查表单信息是否填写正确')
    } else {
      ElMessage.error('添加联系人失败，请稍后重试')
      console.error('Error submitting form:', error)
    }
  } finally {
    loading.value = false
  }
}

const resetForm = (formEl) => {
  if (!formEl) return
  formEl.resetFields()
}
</script>

<style scoped>
.add-contact {
  max-width: 800px;
  margin: 20px auto;
  padding: 0 20px;
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

.contact-form {
  margin-top: 20px;
}
</style>
