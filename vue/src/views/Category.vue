<template>
  <div class="card" style="margin-bottom: 5px">攻略分类</div>
  <div class="card" style="margin-bottom: 5px">
    <el-input clearable @clear="load" style="width: 260px;margin-right: 5px" v-model="data.title"
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
      <el-table-column prop="title" label="分类标题"/>
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

  <el-dialog v-model="data.formVisible" title="分类信息" width="500" destroy-on-close>
    <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" padding="20px 30px 10px 0">
      <el-form-item prop="title" label="分类标题">
        <el-input v-model="data.form.title" autocomplete="off" placeholder="请填写分类标题"/>
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
  title: null,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  formVisible: false,
  rules: {
    title: [{required: true, message: '请填写分类标题', trigger: 'blur'}],
  }
})

const load = () => {
  request.get('/category/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title
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
  data.title = ''
  load()
}

const handleAdd = () => {
  data.formVisible = true
  data.form = {}
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row)) //深度拷贝：先转字符串，在转json
  data.formVisible = true
}


const add = () => {
  request.post('/category/add', data.form).then(res => {
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
      request.put("/category/update", data.form).then(res => {
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
    request.delete("/category/delete/" + id).then(res => {
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
</script>