<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <h2>{{ isLogin ? '登录' : '注册' }}</h2>
      </div>
      
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        
        <el-form-item v-if="!isLogin" label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        
        <el-form-item v-if="!isLogin" label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        
        <el-form-item v-if="!isLogin" label="昵称" prop="nickname">
          <el-input v-model="form.nickname" placeholder="请输入昵称" />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSubmit" style="width: 100%">
            {{ isLogin ? '登录' : '注册' }}
          </el-button>
        </el-form-item>
        
        <el-form-item>
          <div class="switch-mode">
            <span v-if="isLogin">还没有账号？</span>
            <span v-else>已有账号？</span>
            <el-button type="text" @click="toggleMode">
              {{ isLogin ? '立即注册' : '立即登录' }}
            </el-button>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login, register } from '../api/user'

const router = useRouter()
const formRef = ref(null)
const isLogin = ref(true)

const form = ref({
  username: '',
  password: '',
  email: '',
  phone: '',
  nickname: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' }
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ]
}

const toggleMode = () => {
  isLogin.value = !isLogin.value
  form.value = {
    username: '',
    password: '',
    email: '',
    phone: '',
    nickname: ''
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    try {
      let response
      if (isLogin.value) {
        response = await login({
          username: form.value.username,
          password: form.value.password
        })
      } else {
        response = await register({
          username: form.value.username,
          password: form.value.password,
          email: form.value.email,
          phone: form.value.phone,
          nickname: form.value.nickname
        })
      }
      
      if (response.data.success) {
        ElMessage.success(response.data.message)
        
        if (isLogin.value) {
          const user = response.data.user
          localStorage.setItem('currentUserId', user.id)
          localStorage.setItem('currentUser', JSON.stringify({
            id: user.id,
            name: user.nickname || user.username,
            avatar: user.avatar
          }))
          router.push('/chat')
        } else {
          isLogin.value = true
          form.value = {
            username: '',
            password: '',
            email: '',
            phone: '',
            nickname: ''
          }
        }
      } else {
        ElMessage.error(response.data.message)
      }
    } catch (error) {
      ElMessage.error('操作失败，请重试')
    }
  })
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: #f0f2f5;
}

.login-box {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h2 {
  margin: 0;
  color: #333;
  font-size: 24px;
  font-weight: 500;
}

.switch-mode {
  text-align: center;
  width: 100%;
}

.switch-mode span {
  color: #666;
  margin-right: 5px;
}
</style>
