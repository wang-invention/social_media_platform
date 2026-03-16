<template>
  <div class="common-layout">
    <el-container style="height: 100vh; margin: 0; padding: 0">
      <el-aside width="280px" class="sidebar">
        <div class="sidebar-header">
          <h3>聊天</h3>
        </div>
        
        <div class="current-user" v-if="currentUser">
          <div class="current-user-info">
            <div class="user-avatar">
              <img v-if="currentUser.avatar" :src="currentUser.avatar" :alt="currentUser.name" />
              <span v-else>{{ currentUser.name.charAt(0) }}</span>
            </div>
            <div class="user-details">
              <div class="user-name">{{ currentUser.name }}</div>
              <div class="user-id">当前用户</div>
            </div>
          </div>
        </div>
        
        <div class="friends-section">
          <div class="section-header">好友列表</div>
          <div class="chat-list">
            <div 
              v-for="friend in friends" 
              :key="friend.id" 
              class="chat-item"
              :class="{ active: currentChatId === friend.id }"
              @click="selectChat(friend.id)"
            >
              <div class="chat-avatar">
                <img v-if="friend.avatar" :src="friend.avatar" :alt="friend.name" />
                <span v-else>{{ friend.name.charAt(0) }}</span>
                <div class="status-indicator" :class="{ online: friend.isOnline, offline: !friend.isOnline }"></div>
              </div>
              <div class="chat-info">
                <div class="chat-name">{{ friend.name }}</div>
                <div class="chat-last-message">{{ friend.lastMessage }}</div>
              </div>
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
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { getUserList } from '../api/user'

const router = useRouter()
const currentChatId = ref(1)
const chatList = ref([])
const currentUser = ref(null)

const friends = computed(() => {
  return chatList.value.filter(user => user.id !== currentUser.value?.id)
})

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
  background: #fff;
}

.current-user-info {
  display: flex;
  align-items: center;
  padding: 12px 20px;
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

.friends-section {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
}

.section-header {
  font-size: 12px;
  color: #909399;
  padding: 10px 20px;
  font-weight: 500;
}

.chat-list {
  display: flex;
  flex-direction: column;
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

.status-indicator {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 12px;
  height: 12px;
  border: 2px solid #fff;
  border-radius: 50%;
}

.status-indicator.online {
  background: #67c23a;
}

.status-indicator.offline {
  background: #909399;
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
