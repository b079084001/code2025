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
                {path: 'user', meta: {name: '普通用户信息'}, component: () => import('../views/User.vue'),},
                {path: 'person', meta: {name: '个人信息'}, component: () => import('../views/Person.vue'),},
                {path: 'updatePassword', meta: {name: '修改密码'}, component: () => import('../views/UpdatePassword.vue'),},
                {path: 'notice', meta: {name: '系统公告'}, component: () => import('../views/Notice.vue'),},
                {path: 'introduction', meta: {name: '旅游攻略'}, component: () => import('../views/Introduction.vue'),},
                {path: 'category', meta: {name: '攻略分类'}, component: () => import('../views/Category.vue'),},
            ]
        },
        {path: '/login', component:() => import('../views/Login.vue'),},
        {path: '/register', component:() => import('../views/Register.vue'),},
        {path: '/NotFound', component: () =>import('../views/404.vue'),},
        {path: '/:pathMatch(.*)', redirect: '/NotFound'},
    ],
})

export default router
