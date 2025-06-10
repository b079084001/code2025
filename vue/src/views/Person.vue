<template>
  <div class="card" style="width: 50%">
    <div style="font-size: 16px;margin-bottom: 20px">个人信息</div>
    <el-form ref="formRef" :model="data.user" label-width="80px" padding="20px 30px 10px 0">
      <el-form-item prop="username" label="账号">
        <el-input v-model="data.user.username" autocomplete="off"/>
      </el-form-item>
      <el-form-item prop="name" label="名称">
        <el-input v-model="data.user.name" autocomplete="off"/>
      </el-form-item>
      <el-form-item prop="phone" label="电话">
        <el-input v-model="data.user.phone" autocomplete="off"/>
      </el-form-item>
      <el-form-item prop="email" label="邮箱">
        <el-input v-model="data.user.email" autocomplete="off"/>
      </el-form-item>
      <el-form-item prop="avatar" label="头像">
        <el-upload
            action="http://localhost:9090/files/upload"
            :headers="{token: data.user.token}"
            list-type="picture"
            :on-success="handleFileSuccess"
        >
          <el-button type="primary">上传头像</el-button>
        </el-upload>
      </el-form-item>
    </el-form>
    <div style="text-align: center;">
      <el-button type="primary" style="padding: 20px 35px" @click="update">保存</el-button>
    </div>

  </div>
</template>

<script setup>
import {reactive, defineEmits} from "vue";
import request from "@/util/request.js";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
})

const handleFileSuccess = (res) => {
  data.user.avatar = res.data
}

const emit = defineEmits(['updateUser'])

const update = () => {
  let url
  if (data.user.role === 'ADMIN') {
    url = '/admin/update'
  }
  if (data.user.role === 'USER') {
    url = '/user/update'
  }

  request.put(url, data.user).then(res => {
    if (res.code === '200') {
      ElMessage.success('更新成功')
      localStorage.setItem('code_user', JSON.stringify(data.user))
      emit('updateUser',data.user)
    }
  })
}

</script>
