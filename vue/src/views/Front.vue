<template>
  <div style="background-color: #f6f9fc;min-height: 1000px">
    <el-menu
        :default-active="1"
        class="el-menu-demo"
        mode="horizontal"
    >
      <el-menu-item index="1">系统首页</el-menu-item>
      <el-menu-item @click="logout">退出</el-menu-item>
    </el-menu>

    <div style="width: 80%;margin: 20px auto">
      <div style="font-size: 20px;border-left: 5px Solid #2fbd67;padding-left: 10px;height: 30px;line-height: 30px">
        旅游攻略
      </div>
      <div style="margin-top: 20px">
        <el-row :gutter="20">
          <el-col v-for="item in data.introductionData" :span="6" style="margin-bottom: 20px">
            <img :src="item.img" alt="" style="width: 100%;height: 250px;border-radius: 5px;">
            <div style="font-size: 17px;font-weight: bold;margin-top: 5px">{{ item.title }}</div>
            <div style="display: flex;align-items: center;margin-top: 10px;grid-gap: 10px">
              <img :src="item.userAvatar" alt="" style="width: 30px;height: 30px;border-radius: 50%">
              <div>{{ item.userName }}</div>
              <div style="color: #666">{{ item.time }}</div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <div style="width: 80%;margin: 20px auto">
      <div style="font-size: 20px;border-left: 5px Solid #2fbd67;padding-left: 10px;height: 30px;line-height: 30px">
        旅游攻略
      </div>
      <div style="margin-top: 20px;display: flex;grid-gap: 20px" v-for="item in data.introductionData">
        <div style="flex:1">
          <img :src="item.img" alt="" style="width: 100%;height: 250px;display: block;border-radius: 5px">
        </div>
        <div style="flex:3">
          <div style="font-size: 20px;font-weight: bold">{{ item.title }}</div>
          <div style="margin-top: 10px;font-size: 15px;color: #666666;line-height: 25px;height: 125px;text-align: justify" class="line4">{{ item.description }}</div>
          <div style="display: flex;align-items: center;margin-top: 10px;grid-gap: 10px">
            <img :src="item.userAvatar" alt="" style="width: 30px;height: 30px;border-radius: 50%">
            <div>{{ item.userName }}</div>
            <div style="color: #666">{{ item.time }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import request from "@/util/request.js";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  loadIntroduction: []
})

const loadIntroduction = () => {
  request.get('/introduction/selectAll').then(res => {
    if (res.code === '200') {
      data.introductionData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadIntroduction()

const logout = () => {
  localStorage.removeItem("code_user")
  location.href = '/login'
}
</script>
<style>
.line4 {
  word-break: break-all;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 4; /*超出几行省略*/
  overflow: hidden;
}
</style>