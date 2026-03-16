import { createRouter, createWebHistory } from 'vue-router'
import Chat from '@/views/Chat.vue'
import Login from '@/views/Login.vue'
import Layout from '@/layout/index.vue'

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: Login
    },
    {
        path: '/',
        component: Layout,
        children: [
            { path: '', redirect: '/chat' },
            { path: 'chat', name: 'Chat', component: Chat }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    const userId = localStorage.getItem('currentUserId')

    console.log('路由守卫检查:', {
        path: to.path,
        name: to.name,
        userId: userId,
        shouldRedirect: to.path !== '/login' && !userId
    })

    if (to.path !== '/login' && !userId) {
        console.log('未登录，跳转到登录页')
        next('/login')
    } else if (to.path === '/login' && userId) {
        console.log('已登录，跳转到聊天页')
        next('/chat')
    } else {
        console.log('正常访问')
        next()
    }
})

export default router
