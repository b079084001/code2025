import {createApp} from 'vue'
import App from './App.vue'
import router from './router'

//引入全局变量
import './assets/css/global.css'
//引入Element-Plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
//引入icon
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)

app.use(router)
//使用Element-Plus
app.use(ElementPlus, {
    locale: zhCn,
})

app.mount('#app')
//使用icon
for (const[key,component] of Object.entries(ElementPlusIconsVue)){
    app.component(key,component)
}