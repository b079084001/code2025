<template>
  <div class="card" style="margin-bottom: 5px">系统公告</div>
  <div class="card" style="margin-bottom: 5px">
    <el-input clearable @clear="load" style="width: 260px;margin-right: 5px" v-model="data.name"
              placeholder="请输入标题查询"
              :prefix-icon="Search"></el-input>
    <el-button type="primary" @click="load">查询</el-button>
    <el-button type="info" @click="reset">重置</el-button>
  </div>

  <div class="card" style="margin-bottom: 5px">
    <el-button type="primary" @click="handleAdd">新 增</el-button>
  </div>

  <div class="card" style="margin-bottom: 5px">
    <el-table :data="data.tableData" style="width: 100%"
              :header-cell-style="{fontWeight:'bold',color:'#333',backgroundColor:'#eaf4ff'}">
      <el-table-column prop="img" label="图书封面" width="100">
        <template #default="scope">
          <el-image v-if="scope.row.img" :src="scope.row.img" :preview-src-list="[scope.row.img]"
                    :preview-teleported="true" style="width: 40px;height: 40px;border-radius: 5px;display: block"/>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="图书名字"/>
      <el-table-column prop="price" label="图书价格"/>
      <el-table-column prop="author" label="图书作者"/>
      <el-table-column prop="num" label="剩余数量"/>
      <el-table-column label="操作" width="100">
        <template #default="scope" v-if="data.user.role==='ADMIN'">
          <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
          <el-button type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
        </template>
        <template #default="scope" v-if="data.user.role==='USER'">
          <el-button type="primary" @click="borrow(scope.row)">借阅</el-button>
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

  <el-dialog v-model="data.formVisible" title="图书信息" width="500" destroy-on-close>
    <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" padding="20px 30px 10px 0">
      <el-form-item prop="img" label="图书封面">
        <el-upload
            action="http://localhost:9090/files/upload"
            :headers="{token: data.user.token}"
            list-type="picture"
            :on-success="handleFileSuccess"
        >
          <el-button type="primary">上传图片</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item prop="name" label="图书名字">
        <el-input v-model="data.form.name" autocomplete="off" placeholder="请填写图书名字"/>
      </el-form-item>
      <el-form-item prop="price" label="图书价格">
        <el-input v-model="data.form.price" autocomplete="off" placeholder="请填写图书价格"/>
      </el-form-item>
      <el-form-item prop="author" label="图书作者">
        <el-input v-model="data.form.author" autocomplete="off" placeholder="请填写图书作者"/>
      </el-form-item>
      <el-form-item prop="num" label="剩余数量">
        <el-input v-model="data.form.num" autocomplete="off" placeholder="请填写图书数量"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="data.formVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </div>
    </template>
  </el-dialog>

</template>

<script setup>
import {Search} from "@element-plus/icons-vue";
import {defineEmits, reactive, ref} from "vue";
import request from "@/util/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const emit = defineEmits(['updateUser'])
const formRef = ref()
const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  name: null,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  formVisible: false,
  rules: {
    img: [{required: true, message: '请填写图书封面', trigger: 'blur'}],
    name: [{required: true, message: '请填写图书名字', trigger: 'blur'}],
    price: [{required: true, message: '请填写图书价格', trigger: 'blur'}],
    author: [{required: true, message: '请填写图书作者', trigger: 'blur'}],
    num: [{required: true, message: '请填写图书数量', trigger: 'blur'}],
  }
})

const load = () => {
  request.get('/book/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list
      data.total = res.data?.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}

load()

const reset = () => {
  data.name = ''
  load()
}

const handleAdd = () => {
  data.formVisible = true
  data.form = {}
}

const borrow = (row) => {
  request.post('/record/add', {
    userId: data.user.id,
    bookId: row.id,
    // status:'待审核'
  }).then(res => {
    if (res.code === '200') {
      ElMessage.success("操作成功，等带管理员审核")
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}


const add = () => {
  request.post('/book/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('新增成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => {
  //formRef是表单的引用
  formRef.value.validate((valid) => {
    if (valid) {  //验证通过
      request.put("/book/update", data.form).then(res => {
        if (res.code === '200') {
          data.formVisible = false
          ElMessage.success('更新成功')
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const save = () => {
  formRef.value.validate(valid => {
    if (valid) {
      data.form.id ? update() : add()
    }
  })
}

const del = (id) => {
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', {type: 'warning'}).then(res => {
    request.delete("/book/delete/" + id).then(res => {
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

const handleFileSuccess = (res) => {
  data.form.img = res.data
}
</script>