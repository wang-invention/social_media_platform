import axios from 'axios'

const userRequest = axios.create({
  // baseURL: 'http://129.211.0.210:8081',
  baseURL: 'http://localhost:8080',
  timeout: 5000
})

const chatRequest = axios.create({
  // baseURL: 'http://129.211.0.210:8082',
  baseURL: 'http://localhost:8080',
  timeout: 5000
})

export const getUserList = () => {
  return userRequest({
    url: '/users/list',
    method: 'get'
  })
}

export const getUserById = (userId) => {
  return userRequest({
    url: `/users/${userId}`,
    method: 'get'
  })
}

export const getCurrentUser = (userId) => {
  return userRequest({
    url: `/users/${userId}`,
    method: 'get'
  })
}

export const getChatMessages = (userId, otherUserId) => {
  return chatRequest({
    url: '/chat-messages',
    method: 'get',
    params: {
      userId,
      otherUserId
    }
  })
}

export const sendMessage = (message) => {
  return chatRequest({
    url: '/api/chat/send',
    method: 'post',
    data: message
  })
}

export const login = (data) => {
  return userRequest({
    url: '/users/login',
    method: 'post',
    data
  })
}

export const register = (data) => {
  return userRequest({
    url: '/users/register',
    method: 'post',
    data
  })
}

export const heartbeat = (userId) => {
  return userRequest({
    url: '/users/heartbeat',
    method: 'post',
    data: {
      userId
    }
  })
}
