<template>
  <div class="bg">
    <div
        style="width: 350px;background-color: #fff;border-radius: 5px;box-shadow: 0 0 10px rgba(0,0,0,0.1);padding: 40px 20px">
      <el-form ref="formRef" :model="data.form" :rules="data.rules">
        <div style="margin-bottom: 40px;text-align: center;font-weight: bold;font-size: 24px">欢 迎 登 录</div>
        <el-form-item prop="username">
          <el-input size="large" v-model="data.form.username" autocomplete="off" prefix-icon="User"
                    placeholder="请输入账号"/>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="large" show-password v-model="data.form.password" autocomplete="off" prefix-icon="Lock"
                    placeholder="请输入密码"/>
        </el-form-item>
        <el-form-item prop="role">
          <el-select style="width: 100%" v-model="data.form.role">
            <el-option label="管理员" value="ADMIN"></el-option>
            <el-option label="普通用户" value="USER"></el-option>
          </el-select>
        </el-form-item>
        <div style="margin-bottom: 20px">
          <el-button style="width: 100%; background-color:#8595f8;border-color:#8595f8" size="large" @click="login">登
            录
          </el-button>
        </div>
        <div style="text-align: right">
          还没有账号？请<a style="color:#274afa" href="/register">注册</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import request from "@/util/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const formRef = ref()
const data = reactive({
  form: {role: 'ADMIN'},
  rules: {
    username: [
      {required: true, message: '请输入账号', trigger: 'blur'},
      {min: 3, message: '账号最少3位', trigger: 'blur'}
    ],
    password: [{required: true, message: '请输入密码', trigger: 'blur'}],
  }
})

const login = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/login', data.form).then(res => {
        if (res.code === '200') {
          //存储用户信息
          localStorage.setItem("code_user", JSON.stringify(res.data || '{}'))
          ElMessage.success('登录成功')
          // router.push("/")
          if("USER"===res.data.role){
            location.href='/front/home'
          }else{
            router.push("/")
          }
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
</script>

<style scoped>
.bg {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  background-image: url("@/assets/imgs/bg.jpg");
  background-size: cover;
}
</style>