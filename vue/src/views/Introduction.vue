<template>
  <div class="card" style="margin-bottom: 5px">旅游攻略</div>
  <div class="card" style="margin-bottom: 5px">
    <el-input clearable @clear="load" style="width: 260px;margin-right: 5px" v-model="data.title"
              placeholder="请输入标题查询"
              :prefix-icon="Search"></el-input>
    <el-button type="primary" @click="load">查询</el-button>
    <el-button type="info" @click="reset">重置</el-button>
  </div>

  <div class="card" style="margin-bottom: 5px" v-if="data.user.role==='USER'">
    <el-button type="primary" @click="handleAdd">新 增</el-button>
  </div>

  <div class="card" style="margin-bottom: 5px">
    <el-table :data="data.tableData" style="width: 100%"
              :header-cell-style="{fontWeight:'bold',color:'#333',backgroundColor:'#eaf4ff'}">
      <el-table-column prop="img" label="攻略主图" width="100">
        <template #default="scope">
          <el-image v-if="scope.row.img" :src="scope.row.img" :preview-src-list="[scope.row.img]"
                    :preview-teleported="true" style="width: 50px;height: 50px;border-radius: 5px;display: block"/>
        </template>
      </el-table-column>
      <el-table-column prop="title" label="攻略标题"/>
      <el-table-column prop="categoryTitle" label="攻略分类"/>
      <el-table-column prop="userName" label="发布用户"/>
      <el-table-column prop="content" label="攻略内容">
        <template v-slot="scope">
          <el-button type="primary" @click="viewContent(scope.row.content)">点击查看</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="time" label="发布时间"/>
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

  <el-dialog v-model="data.formVisible" title="攻略信息" width="60%" destroy-on-close>
    <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" padding="20px 30px 10px 0">
      <el-form-item prop="img" label="攻略主图">
        <el-upload
            action="http://localhost:9090/files/upload"
            :headers="{token: data.user.token}"
            list-type="picture"
            :on-success="handleFileSuccess"
        >
          <el-button type="primary">上传图片</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item prop="title" label="攻略标题">
        <el-input v-model="data.form.title" autocomplete="off" placeholder="请填写攻略标题"/>
      </el-form-item>
      <el-form-item prop="categoryId" label="攻略分类">
        <el-select
            v-model="data.form.categoryId"
            placeholder="请选择攻略分类"
            style="width: 100%"
        >
          <el-option
              v-for="item in data.categoryData"
              :key="item.id"
              :label="item.title"
              :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="content" label="攻略详情">
        <el-form-item>
          <div style="border: 1px solid #ccc;width: 100%">
            <Toolbar
                style="border-bottom: 1px solid #ccc"
                :editor="editorRef"
                :mode="mode"
            />
            <Editor
                style="height: 500px;overflow-y: hidden;"
                v-model="data.form.content"
                :mode="mode"
                :defaultConfig="editorConfig"
                @onCreated="handleCreated"
            />
          </div>
        </el-form-item>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="data.formVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </div>
    </template>
  </el-dialog>

  <el-dialog v-model="data.viewVisible" title="攻略详情" width="60%" destroy-on-close>
    <div v-html="data.content" style="padding: 0 20px"></div>
  </el-dialog>

</template>

<script setup>
import {Search} from "@element-plus/icons-vue";
import {defineEmits, onBeforeUnmount, reactive, ref, shallowRef} from 'vue'
import '@wangeditor/editor/dist/css/style.css' //引入css
import {Editor, Toolbar} from '@wangeditor/editor-for-vue'

import request from "@/util/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const formRef = ref()

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  title: null,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  form: {},
  formVisible: false,
  rules: {
    title: [{required: true, message: '请填写攻略标题', trigger: 'blur'}],
    content: [{required: true, message: '请填写攻略内容', trigger: 'blur'}],
  },
  content: null,
  viewVisible: false,
  categoryData: []
})

/* wangEditor5初始化开始 */
const editorRef = shallowRef()  //编辑器实例，必须用shallowRef
const mode = 'default'
const editorConfig = {MENU_CONF: {}}
//图片上传配置
editorConfig.MENU_CONF['uploadImage'] = {
  headers: {
    token: data.user.token
  },
  server: "http://localhost:9090/files/wang/upload", //服务器段图片上传接口
  fieldName: 'file'
}
//组件销毁时，也及时销毁编辑器，否则可能会造成内存泄露
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})
//记录editor实例
const handleCreated = (editor) => {
  editorRef.value = editor
}
/* wangEditor5初始化结束 */

const emit = defineEmits(['updateUser'])

const load = () => {
  request.get('/introduction/selectPage', {
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

const handleFileSuccess = (res) => {
  data.form.img = res.data
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row)) //深度拷贝：先转字符串，在转json
  data.formVisible = true
}

const add = () => {
  request.post('/introduction/add', data.form).then(res => {
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
      request.put("/introduction/update", data.form).then(res => {
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
    request.delete("/introduction/delete/" + id).then(res => {
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

const viewContent = (content) => {
  data.viewVisible = true
  data.content = content
}

const loadCategory = () => {
  request.get('/category/selectAll').then(res => {
    if (res.code === '200') {
      data.categoryData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

loadCategory()
</script>