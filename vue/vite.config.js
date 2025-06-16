import {fileURLToPath, URL} from 'node:url'

import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// ����ɫ
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import {ElementPlusResolver} from 'unplugin-vue-components/resolvers'

// https://vite.dev/config/
export default defineConfig({
    plugins: [
        vue(),
        vueDevTools(),
        AutoImport({
            resolvers: [ElementPlusResolver(
                {importStyle: 'sass'}
            )],
        }),
        Components({
            resolvers: [ElementPlusResolver(
                {importStyle: 'sass'}
            )],
        }),
    ],
    css: {
        preprocessorOptions: {
            scss: {
                // �Զ����붨�ƻ���ʽ�ļ�������ʽ����
                additionalData: `
          @use "@/assets/css/index.scss" as *;
        `,
            }
        }
    },
    resolve: {
        alias: {
            '@': fileURLToPath(new URL('./src', import.meta.url))
        },
    },
})
