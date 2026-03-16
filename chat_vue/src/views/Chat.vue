<template>
  <div class="chat-container">
    <div class="chat-header">
      <div class="chat-avatar">
        <img v-if="currentChat.avatar" :src="currentChat.avatar" :alt="currentChat.name" />
        <span v-else>{{ currentChat.name.charAt(0) }}</span>
      </div>
      <div class="chat-info">
        <div class="chat-name">{{ currentChat.name }}</div>
        <div class="chat-status">在线</div>
      </div>
    </div>

    <div class="chat-messages" ref="messagesContainer">
      <div 
        v-for="message in messages" 
        :key="message.id" 
        class="message-item"
        :class="message.type"
      >
        <div class="message-avatar">
          {{ message.avatar }}
        </div>
        <div class="message-content">
          <div class="message-text">{{ message.text }}</div>
          <div class="message-time">{{ message.time }}</div>
        </div>
      </div>
    </div>

    <div class="chat-input">
      <el-input
        v-model="inputMessage"
        type="textarea"
        :rows="3"
        placeholder="输入消息..."
        @keydown.enter.prevent="sendMessage"
      />
      <el-button type="primary" @click="sendMessage" class="send-button">
        发送
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted, onUnmounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getUserById, getCurrentUser, getChatMessages } from '../api/user'

const route = useRoute()
const inputMessage = ref('')
const messagesContainer = ref(null)
const websocket = ref(null)

const savedUserId = localStorage.getItem('currentUserId')
const currentUserId = ref(savedUserId ? parseInt(savedUserId) : 2)
const currentUser = ref(null)

const currentChat = ref({
  id: 1,
  name: '张三',
  avatar: null
})

const messages = ref([])

const loadCurrentUserInfo = async () => {
  try {
    const response = await getCurrentUser(currentUserId.value)
    currentUser.value = response.data
    console.log('当前用户信息:', currentUser.value)
    console.log('当前用户ID:', currentUserId.value)
    console.log('当前用户名称:', currentUser.value?.nickname || currentUser.value?.username)
  } catch (error) {
    console.error('获取当前用户信息失败:', error)
  }
}

const connectWebSocket = () => {
  // const wsUrl = `ws://129.211.0.210:8082/chat/${currentUserId.value}`
  const wsUrl = `ws://localhost:8080/chat/${currentUserId.value}`
  console.log('正在连接WebSocket:', wsUrl)
  websocket.value = new WebSocket(wsUrl)

  websocket.value.onopen = () => {
    console.log('WebSocket连接成功，用户ID:', currentUserId.value)
  }

  websocket.value.onmessage = (event) => {
    try {
      const msg = JSON.parse(event.data)
      console.log('收到消息:', msg)
      const time = new Date()
      const timeStr = `${time.getHours().toString().padStart(2, '0')}:${time.getMinutes().toString().padStart(2, '0')}`
      
      const isFromMe = msg.senderId === currentUserId.value
      messages.value.push({
        id: msg.id || Date.now(),
        type: isFromMe ? 'sent' : 'received',
        avatar: isFromMe ? '我' : currentChat.value.name.charAt(0),
        text: msg.content,
        time: timeStr
      })
      
      scrollToBottom()
    } catch (error) {
      console.error('解析消息失败:', error)
    }
  }

  websocket.value.onerror = (error) => {
    console.error('WebSocket错误:', error)
  }

  websocket.value.onclose = () => {
    console.log('WebSocket连接关闭')
  }
}

const disconnectWebSocket = () => {
  if (websocket.value) {
    websocket.value.close()
    websocket.value = null
  }
}

const loadUserInfo = async (userId) => {
  try {
    const response = await getUserById(userId)
    const user = response.data
    currentChat.value = {
      id: user.id,
      name: user.nickname || user.username,
      avatar: user.avatar
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}

const loadChatMessages = async (otherUserId) => {
  try {
    const response = await getChatMessages(currentUserId.value, otherUserId)
    messages.value = response.data.map(msg => {
      const time = new Date(msg.createdAt)
      const timeStr = `${time.getHours().toString().padStart(2, '0')}:${time.getMinutes().toString().padStart(2, '0')}`
      
      return {
        id: msg.id,
        type: msg.senderId === currentUserId.value ? 'sent' : 'received',
        avatar: msg.senderId === currentUserId.value ? '我' : currentChat.value.name.charAt(0),
        text: msg.content,
        time: timeStr
      }
    })
    scrollToBottom()
  } catch (error) {
    console.error('获取聊天记录失败:', error)
  }
}

const sendMessage = () => {
  if (!inputMessage.value.trim()) return

  const message = {
    receiverId: currentChat.value.id,
    content: inputMessage.value
  }

  if (websocket.value && websocket.value.readyState === WebSocket.OPEN) {
    websocket.value.send(JSON.stringify(message))
  } else {
    console.error('WebSocket未连接')
  }

  const now = new Date()
  const time = `${now.getHours().toString().padStart(2, '0')}:${now.getMinutes().toString().padStart(2, '0')}`

  messages.value.push({
    id: Date.now(),
    type: 'sent',
    avatar: '我',
    text: inputMessage.value,
    time: time
  })

  inputMessage.value = ''
  scrollToBottom()
}

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

onMounted(() => {
  const userId = parseInt(route.query.userId) || 1
  const userName = route.query.userName || '张三'
  
  currentChat.value = {
    id: userId,
    name: decodeURIComponent(userName),
    avatar: null
  }
  
  loadChatMessages(userId)
  loadUserInfo(userId)
  loadCurrentUserInfo()
  connectWebSocket()
})

onUnmounted(() => {
  disconnectWebSocket()
})

watch(() => route.query.userId, (newUserId) => {
  if (newUserId) {
    const userId = parseInt(newUserId)
    const userName = route.query.userName || '用户'
    
    currentChat.value = {
      id: userId,
      name: decodeURIComponent(userName),
      avatar: null
    }
    
    loadChatMessages(userId)
    loadUserInfo(userId)
  }
})
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: #fafbfc;
}

.chat-header {
  display: flex;
  align-items: center;
  padding: 15px 20px;
  background: white;
  border-bottom: 1px solid #e4e7ed;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.chat-avatar {
  width: 45px;
  height: 45px;
  border-radius: 50%;
  background: #409eff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 18px;
  margin-right: 12px;
  overflow: hidden;
}

.chat-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.chat-info {
  flex: 1;
}

.chat-name {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.chat-status {
  font-size: 12px;
  color: #67c23a;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background: #fafbfc;
}

.message-item {
  display: flex;
  margin-bottom: 20px;
  align-items: flex-start;
}

.message-item.sent {
  flex-direction: row-reverse;
}

.message-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #409eff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 14px;
  flex-shrink: 0;
}

.message-item.received .message-avatar {
  background: #909399;
}

.message-item.sent .message-avatar {
  background: #67c23a;
}

.message-content {
  max-width: 60%;
  margin: 0 12px;
}

.message-item.sent .message-content {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.message-text {
  padding: 10px 14px;
  border-radius: 8px;
  font-size: 14px;
  line-height: 1.5;
  word-wrap: break-word;
}

.message-item.received .message-text {
  background: white;
  color: #303133;
  border: 1px solid #e4e7ed;
}

.message-item.sent .message-text {
  background: #409eff;
  color: white;
}

.message-time {
  font-size: 11px;
  color: #909399;
  margin-top: 4px;
}

.chat-input {
  display: flex;
  gap: 10px;
  padding: 15px 20px;
  background: white;
  border-top: 1px solid #e4e7ed;
}

.send-button {
  align-self: flex-end;
  height: 75px;
}
</style>