<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input style="width:260px;margin-right: 5px" v-mdel="data.name" placeholder="please input"
                :prefix-icon="Search"></el-input>
      <el-button type="primary">Search</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="danger">批量删除</el-button>
      <el-button type="primary">新 增</el-button>
      <el-button type="success">批量导入</el-button>
      <el-button type="info">批量导出</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%" :header-cell-style="{fontWeight:'bold',color:'#f60707'}">
        <el-table-column tyle="selection" width="55"/>
        <el-table-column prop="username" label="账号"/>
        <el-table-column prop="name" label="名称"/>
        <el-table-column prop="phone" label="电话"/>
        <el-table-column prop="email" label="邮箱"/>
      </el-table>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-pagination
          v-model:current-page="data.pageNum"
          :page-size="data.pageSize"
          layout="total, prev, pager, next"
          :total="data.total"
          @current-change="load"
      />
    </div>
  </div>
</template>

<script setup>
import {reactive} from 'vue';
import {Search} from "@element-plus/icons-vue";
import axios from "axios";
import request from "@/util/request.js";
import {ElMessage} from "element-plus";

const data = reactive({
  name: null,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: []
})


const load = () => {
  request.get('/admin/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data.list
      data.total = res.data.total
      // console.log(res)
    } else {
      ElMessage.error(res.msg)
    }
  })
}

load()

</script>
