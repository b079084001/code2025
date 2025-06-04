import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {path: '/', redirect: '/manager/home'},
        {
            path: '/manager', meta: {name: '管理员'}, component: () => import('../views/Manager.vue'),
            children: [
                {path: 'home', meta: {name: '主页'}, component: () => import('../views/Home.vue'),},
                {path: 'admin', meta: {name: '管理员信息'}, component: () => import('../views/Admin.vue'),},
            ]
        },
        {path: '/NotFound', meta: {name: '404'}, component: () => import('../views/404.vue'),},
        {path: '/:pathMatch(.*)', redirect: '/NotFound'},
    ],
})

export default router
