<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input style="width:260px;margin-right: 5px" v-mdel="data.name" placeholder="please input"
                :prefix-icon="Search"></el-input>
      <el-button type="primary">Search</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="danger">Delete</el-button>
      <el-button type="info">Add</el-button>
      <el-button type="success">Import</el-button>
      <el-button type="info">Export</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%" :header-cell-style="{fontWeight:'bold',color:'#f60707'}">
        <el-table-column prop="date" label="Date" width="180"/>
        <el-table-column prop="name" label="Name" width="180"/>
        <el-table-column prop="address" label="Address"/>
      </el-table>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-pagination
          v-model:current-page="data.pageNum"
          :page-size="data.pageSize"
          layout="total, prev, pager, next"
          :total="data.total"
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
  pageSize: 2,
  total: 4,
  tableData: [
    {date: '2016-05-03', name: 'Tom', address: 'No. 189, Grove St, Los Angeles',},
    {date: '2016-05-02', name: 'Tom', address: 'No. 189, Grove St, Los Angeles',},
    {date: '2016-05-04', name: 'Tom', address: 'No. 189, Grove St, Los Angeles',},
    {date: '2016-05-01', name: 'Tom', address: 'No. 189, Grove St, Los Angeles',},
  ]
})

axios.get("http://localhost:9090/admin/selectAll").then(res => {
  // console.log(res)
  console.log(res.data.data)
})

// axios.get("http://localhost:9090/admin/selectAll").then(res => {
//   if (res.code !== '200' ){
//     //错误提示
//   }else{
//     // console.log(res)
//     console.log(res.data.data)
//   }
// })


request.get('/admin/selectAll').then(res => {
  console.log(res)
})
request.get('/admin/selectAll').then(res => {
  if (res.code === '200') {
    console.log(res)
  } else {
    ElMessage.error(res.msg)
  }

})
</script>
