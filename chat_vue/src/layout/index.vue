<template>
  <div class="common-layout">
    <el-container style="height: 100vh; margin: 0; padding: 0">
      <el-aside width="280px" class="sidebar">
        <div class="sidebar-header">
          <h3>聊天</h3>
        </div>
        
        <div class="current-user" v-if="currentUser">
          <div class="current-user-info" @click="showUserSwitcher = !showUserSwitcher">
            <div class="user-avatar">
              <img v-if="currentUser.avatar" :src="currentUser.avatar" :alt="currentUser.name" />
              <span v-else>{{ currentUser.name.charAt(0) }}</span>
            </div>
            <div class="user-details">
              <div class="user-name">{{ currentUser.name }}</div>
              <div class="user-id">当前用户</div>
            </div>
            <div class="switch-icon">▼</div>
          </div>
          
          <div class="user-switcher" v-if="showUserSwitcher">
            <div class="switcher-header">切换用户</div>
            <div 
              v-for="user in chatList" 
              :key="user.id" 
              class="user-option"
              :class="{ active: currentUser.id === user.id }"
              @click="switchUser(user)"
            >
              <div class="option-avatar">
                <img v-if="user.avatar" :src="user.avatar" :alt="user.name" />
                <span v-else>{{ user.name.charAt(0) }}</span>
              </div>
              <div class="option-name">{{ user.name }}</div>
            </div>
          </div>
        </div>
        
        <div class="chat-list">
          <div 
            v-for="chat in chatList" 
            :key="chat.id" 
            class="chat-item"
            :class="{ active: currentChatId === chat.id }"
            @click="selectChat(chat.id)"
          >
            <div class="chat-avatar">
              <img v-if="chat.avatar" :src="chat.avatar" :alt="chat.name" />
              <span v-else>{{ chat.name.charAt(0) }}</span>
              <div v-if="chat.isOnline" class="online-status"></div>
            </div>
            <div class="chat-info">
              <div class="chat-name">{{ chat.name }}</div>
              <div class="chat-last-message">{{ chat.lastMessage }}</div>
            </div>
          </div>
        </div>
      </el-aside>
      <el-container>
        <el-main style="background: #fafbfc; padding: 0">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getUserList } from '../api/user'

const router = useRouter()
const currentChatId = ref(1)
const chatList = ref([])
const currentUser = ref(null)
const showUserSwitcher = ref(false)

const loadUserList = async () => {
  try {
    const response = await getUserList()
    chatList.value = response.data.map(user => ({
      id: user.id,
      name: user.nickname || user.username,
      lastMessage: '暂无消息',
      avatar: user.avatar,
      isOnline: user.isOnline === 1,
      status: user.status === 1
    }))
  } catch (error) {
    console.error('获取用户列表失败:', error)
  }
}

const selectChat = (id) => {
  currentChatId.value = id
  const selectedUser = chatList.value.find(chat => chat.id === id)
  if (selectedUser) {
    router.push({
      path: '/chat',
      query: {
        userId: selectedUser.id,
        userName: selectedUser.name
      }
    })
  }
}

const switchUser = (user) => {
  currentUser.value = user
  localStorage.setItem('currentUserId', user.id)
  localStorage.setItem('currentUser', JSON.stringify(user))
  showUserSwitcher.value = false
  
  window.location.reload()
}

const loadCurrentUser = () => {
  const savedUserId = localStorage.getItem('currentUserId')
  const savedUser = localStorage.getItem('currentUser')
  
  if (savedUser) {
    currentUser.value = JSON.parse(savedUser)
  } else {
    currentUser.value = {
      id: 2,
      name: '李四',
      avatar: null
    }
    localStorage.setItem('currentUserId', 2)
    localStorage.setItem('currentUser', JSON.stringify(currentUser.value))
  }
}

onMounted(() => {
  loadUserList()
  loadCurrentUser()
})
</script>

<style scoped>
.common-layout {
  width: 100%;
  height: 100%;
}

.sidebar {
  background: #f5f7fa;
  border-right: 1px solid #e4e7ed;
  display: flex;
  flex-direction: column;
}

.sidebar-header {
  padding: 20px;
  border-bottom: 1px solid #e4e7ed;
}

.sidebar-header h3 {
  margin: 0;
  font-size: 18px;
  color: #303133;
}

.current-user {
  border-bottom: 1px solid #e4e7ed;
}

.current-user-info {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  cursor: pointer;
  transition: background 0.3s;
}

.current-user-info:hover {
  background: #e9ecef;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #67c23a;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  margin-right: 12px;
  overflow: hidden;
}

.user-avatar img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
}

.user-details {
  flex: 1;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 2px;
}

.user-id {
  font-size: 11px;
  color: #909399;
}

.switch-icon {
  color: #909399;
  font-size: 12px;
  transition: transform 0.3s;
}

.user-switcher {
  background: #fff;
  border-top: 1px solid #e4e7ed;
  max-height: 300px;
  overflow-y: auto;
}

.switcher-header {
  padding: 10px 20px;
  font-size: 12px;
  color: #909399;
  border-bottom: 1px solid #e4e7ed;
}

.user-option {
  display: flex;
  align-items: center;
  padding: 10px 20px;
  cursor: pointer;
  transition: background 0.3s;
}

.user-option:hover {
  background: #f5f7fa;
}

.user-option.active {
  background: #e6f7ff;
}

.option-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #409eff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  margin-right: 10px;
  overflow: hidden;
}

.option-avatar img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
}

.option-name {
  font-size: 13px;
  color: #303133;
}

.chat-list {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
}

.chat-item {
  display: flex;
  align-items: center;
  padding: 12px;
  margin-bottom: 8px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.chat-item:hover {
  background: #e9ecef;
}

.chat-item.active {
  background: #409eff;
  color: white;
}

.chat-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #409eff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  margin-right: 12px;
  flex-shrink: 0;
  position: relative;
}

.chat-avatar img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
}

.online-status {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 12px;
  height: 12px;
  background: #67c23a;
  border: 2px solid #fff;
  border-radius: 50%;
}

.chat-item.active .chat-avatar {
  background: white;
  color: #409eff;
}

.chat-info {
  flex: 1;
  min-width: 0;
}

.chat-name {
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.chat-last-message {
  font-size: 12px;
  color: #909399;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.chat-item.active .chat-last-message {
  color: rgba(255, 255, 255, 0.8);
}
</style>
