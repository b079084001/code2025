import {createApp} from 'vue'
import App from './App.vue'
import router from './router'

//����ȫ�ֱ���
import './assets/css/global.css'
//����Element-Plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
//����icon
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)

app.use(router)
//ʹ��Element-Plus
app.use(ElementPlus, {
    locale: zhCn,
})

app.mount('#app')
//ʹ��icon
for (const[key,component] of Object.entries(ElementPlusIconsVue)){
    app.component(key,component)
}