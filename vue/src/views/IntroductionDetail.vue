<template>
  <div style="width: 70%;margin: 50px auto;">
    <div style="text-align: center;font-size: 22px;font-weight: bold">{{ data.introductionData.title }}</div>
    <div style="margin-top: 15px;display: flex;align-items: center;justify-content: center;grid-gap: 10px">
      <img :src="data.introductionData.userAvatar" alt="" style="width: 25px;height: 25px;border-radius: 50%">
      <div>{{ data.introductionData.userName }}</div>
      <div>所属分类：{{data.introductionData.categoryTitle}}</div>
      <div>发布时间：{{ data.introductionData.time }}</div>
    </div>
    <div v-html="data.introductionData.content" style="margin-top: 50px;padding: 0 50px;"></div>
  </div>
</template>
<script setup>
import {reactive} from "vue";
import router from '@/router/index.js';
import request from "@/util/request.js";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  introductionId: router.currentRoute.value.query.id,
  introductionData: {}
})

const loadIntroduction = () => {
  // console.log(data.introductionId)
  request.get('/introduction/selectById/' + data.introductionId).then(res => {
    if (res.code === '200') {
      data.introductionData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

loadIntroduction()

</script>
