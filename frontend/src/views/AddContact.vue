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

        <el-form-item label="生日" prop="birthday">
          <el-date-picker
            v-model="form.birthday"
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

        <el-form-item label="头像">
          <el-upload
            class="avatar-uploader"
            action="#"
            :show-file-list="false"
            :before-upload="beforeAvatarUpload"
          >
            <img v-if="form.avatar" :src="form.avatar" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item label="备注" prop="notes">
          <el-input
            v-model="form.notes"
            type="textarea"
            placeholder="添加一些备注信息"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm(formRef)">
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
import { mockApi } from '../mock/contacts'
import { useRouter } from 'vue-router'

const router = useRouter()
const formRef = ref()
const form = reactive({
  name: '',
  birthday: '',
  relationship: '',
  avatar: '',
  notes: ''
})

const rules = {
  name: [
    { required: true, message: '请输入联系人姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  birthday: [
    { required: true, message: '请选择生日日期', trigger: 'change' }
  ],
  relationship: [
    { required: true, message: '请选择关系类型', trigger: 'change' }
  ]
}

const beforeAvatarUpload = (file) => {
  const isJPG = file.type === 'image/jpeg'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG) {
    ElMessage.error('头像图片只能是 JPG 格式!')
  }
  if (!isLt2M) {
    ElMessage.error('头像图片大小不能超过 2MB!')
  }

  if (isJPG && isLt2M) {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = (e) => {
      form.avatar = e.target.result
    }
  }
  return false
}

const submitForm = async (formEl) => {
  if (!formEl) return
  
  await formEl.validate(async (valid) => {
    if (valid) {
      try {
        await mockApi.createContact(form)
        ElMessage.success('联系人添加成功！')
        router.push('/')
      } catch (error) {
        ElMessage.error('添加失败：' + error.message)
      }
    }
  })
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

.avatar-uploader {
  text-align: center;
}

.avatar-uploader .avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
}

.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  text-align: center;
  border-radius: 50%;
  background: #f8f9fa;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
