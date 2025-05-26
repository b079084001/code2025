import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/manager', name: 'manager', component: import('../views/Manager.vue'),
            children: [
                {path: 'home', name: 'home', component: import('../views/Home.vue'),},
                {path: 'about', name: 'about', component: import('../views/About.vue'),},
            ]
        },
        {path: '/NotFound', name: '404', component: import('../views/404.vue'),},
        {path: '/:pathMatch(.*)', redirect: '/NotFound'},
    ],
})

export default router
