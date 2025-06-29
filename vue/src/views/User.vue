<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input clearable @clear="load" style="width: 260px;margin-right: 5px" v-model="data.name"
                placeholder="请输入名称查询"
                :prefix-icon="Search"></el-input>
      <el-input clearable @clear="load" style="width: 260px;margin-right: 5px" v-model="data.username"
                placeholder="请输入账号查询"
                :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary" @click="handleAdd">新 增</el-button>
      <el-button type="danger" @click="deleteBatch">批量删除</el-button>
      <el-button type="info" @click="exportData">批量导出</el-button>
      <el-upload
          style="display: inline-block;margin-left: 10px"
          action="http://localhost:9090/user/import"
          :show-file-list="false"
          :on-success="handleImportSuccess"
      >
        <el-button type="success">批量导入</el-button>
      </el-upload>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%" @selection-change="handleCurrentChange"
                :header-cell-style="{fontWeight:'bold',color:'#333',backgroundColor:'#eaf4ff'}">
        <el-table-column type="selection" width="55"/>
        <el-table-column prop="avatar" label="头像" width="100">
          <template #default="scope">
<!--            <img v-if="scope.row.avatar" :src="scope.row.avatar"-->
<!--                 style="width: 40px;height: 40px;border-radius: 50%;display: block">-->
            <el-image v-if="scope.row.avatar" :src="scope.row.avatar" :preview-src-list="[scope.row.avatar]"
                      :preview-teleported="true" style="width: 40px;height: 40px;border-radius: 50%;display: block"/>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="账号"/>
        <el-table-column prop="name" label="名称"/>
        <el-table-column prop="phone" label="电话"/>
        <el-table-column prop="email" label="邮箱"/>
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-pagination
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          :page-sizes="[5, 10, 20]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="data.total"
          @current-change="load"
          @size-change="load"
      />
    </div>

    <el-dialog v-model="data.formVisible" title="普通用户信息" width="500" destroy-on-close>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" padding="20px 30px 10px 0">
        <el-form-item prop="username" label="账号">
          <el-input v-model="data.form.username" autocomplete="off"/>
        </el-form-item>
        <el-form-item prop="name" label="名称">
          <el-input v-model="data.form.name" autocomplete="off"/>
        </el-form-item>
        <el-form-item prop="phone" label="电话">
          <el-input v-model="data.form.phone" autocomplete="off"/>
        </el-form-item>
        <el-form-item prop="email" label="邮箱">
          <el-input v-model="data.form.email" autocomplete="off"/>
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
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {reactive, ref} from 'vue';
import {Search} from "@element-plus/icons-vue";
import request from "@/util/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  username: '',
  name: '',
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  formVisible: false,
  form: {},
  rules: {
    username: [{required: true, message: '请填写账号', trigger: 'blur'}],
    name: [{required: true, message: '请填写名称', trigger: 'blur'}],
    phone: [{required: true, message: '请填写电话', trigger: 'blur'}],
    email: [{required: true, message: '请填写邮箱', trigger: 'blur'}],
  },
  rows: [],
  ids: []
})

const formRef = ref()

const load = () => {
  request.get('/user/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
      username: data.username
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

const reset = () => {
  data.name = ''
  data.username = ''
  load()
}

const handleAdd = () => {
  data.formVisible = true
  data.form = {}
}

const add = () => {
  //formRef是表单的引用
  formRef.value.validate((valid) => {
    if (valid) {  //验证通过
      request.post('/user/add', data.form).then(res => {
        if (res.code === '200') {
          data.formVisible = false
          ElMessage.success('新增成功')
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const handleEdit = (row) => {
  // data.form = row //浅拷贝，修改会直接改变原来的数据
  data.form = JSON.parse(JSON.stringify(row)) //深度拷贝：先转字符串，在转json
  data.formVisible = true
}

const update = () => {
  //formRef是表单的引用
  formRef.value.validate((valid) => {
    if (valid) {  //验证通过
      request.put('/user/update', data.form).then(res => {
        if (res.code === '200') {
          data.formVisible = false
          ElMessage.success('修改成功')
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}


const save = () => {
  data.form.id ? update() : add()
}

const del = (id) => {
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', {type: 'warning'}).then(res => {
    request.delete('/user/delete/' + id).then(res => {
      if (res.code === '200') {
        data.formVisible = false
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
  })
}

const handleCurrentChange = (rows) => { //实际选择的数组
  data.rows = rows
  data.ids = data.rows.map(v => v.id)  //map可以把对象的数字转变成纯数字数组:[1,2,3]
  // console.log(rows)
}

const deleteBatch = () => {
  if (data.rows.length === 0) {
    ElMessage.warning('请选择数据')
    return
  }
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', {type: 'warning'}).then(res => {
    request.delete('/user/deleteBatch', {data: data.rows}).then(res => {
      if (res.code === '200') {
        data.formVisible = false
        ElMessage.success('批量删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
  })
}

const exportData = () => {
  let idsStr = data.ids.join(",") //把数组转换成字符串,[1,2,3]=>"1,2,3"
  let url = `http://localhost:9090/user/export?username=${data.username === null ? '' : data.username}`
      + `&name=${data.name === null ? '' : data.name}`
      + `&ids=${idsStr}`
      + `&token=${data.user.token}`
  window.open(url)
}


const handleImportSuccess = (res) => {
  if (res.code === '200') {
    ElMessage.success('批量导入成功')
    load()
  } else {
    ElMessage.error(res.msg)
  }
}

const handleFileSuccess = (res) => {
  data.form.avatar = res.data
}


</script>
