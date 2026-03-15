import { createRouter, createWebHistory } from 'vue-router'
import Chat from '@/views/Chat.vue'
import Layout from '@/layout/index.vue'

const routes = [
    {
        path: '/',
        component: Layout,
        children: [
            { path: '', redirect: '/chat' },
            { path: 'chat', component: Chat }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router