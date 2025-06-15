<template>
  <div class="card" style="margin-bottom: 5px">系统公告</div>
  <div class="card" style="margin-bottom: 5px">
    <el-input clearable @clear="load" style="width: 260px;margin-right: 5px" v-model="data.userName"
              placeholder="请输入借阅人查询"
              :prefix-icon="Search"></el-input>
    <el-button type="primary" @click="load">查询</el-button>
    <el-button type="info" @click="reset">重置</el-button>
  </div>

  <div class="card" style="margin-bottom: 5px">
    <el-table :data="data.tableData" style="width: 100%"
              :header-cell-style="{fontWeight:'bold',color:'#333',backgroundColor:'#eaf4ff'}">
      <el-table-column prop="bookImg" label="图书封面" width="100">
        <template #default="scope">
          <el-image v-if="scope.row.bookImg" :src="scope.row.bookImg" :preview-src-list="[scope.row.bookImg]"
                    :preview-teleported="true" style="width: 40px;height: 40px;border-radius: 5px;display: block"/>
        </template>
      </el-table-column>
      <el-table-column prop="bookName" label="图书名字"/>
      <el-table-column prop="bookAuthor" label="图书作者"/>
      <el-table-column prop="userName" label="借阅人"/>
      <el-table-column prop="time" label="借阅时间"/>
      <el-table-column prop="status" label="审核状态">
        <template v-slot="scope">
          <el-tag type="warning" v-if="scope.row.status==='待审核'">{{scope.row.status}}</el-tag>
          <el-tag type="success" v-if="scope.row.status==='审核通过'">{{scope.row.status}}</el-tag>
          <el-tag type="danger" v-if="scope.row.status==='审核拒绝'">{{scope.row.status}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="reason" label="审核说明"/>
      <el-table-column label="操作" width="200" v-if="data.user.role==='ADMIN'">
        <template #default="scope">
          <el-button :disabled="scope.row.status!=='待审核'" type="primary" @click="handleEdit(scope.row)">审核</el-button>
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

  <el-dialog v-model="data.formVisible" title="审核" width="500" destroy-on-close>
    <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" padding="20px 30px 10px 0">
      <el-form-item prop="status" label="审核状态" v-if="data.user.role==='ADMIN'">
        <el-radio-group v-model="data.form.status">
          <el-radio-button label="待审核" value="待审核"/>
          <el-radio-button label="审核通过" value="审核通过"/>
          <el-radio-button label="审核拒绝" value="审核拒绝"/>
        </el-radio-group>
      </el-form-item>
      <el-form-item prop="reason" label="审核说明" v-if="data.user.role==='ADMIN' && data.form.status==='审核拒绝'">
        <el-input type="textarea" rows="3" v-model="data.form.reason" autocomplete="off" placeholder="请填写审核说明"/>
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
  userName: null,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  formVisible: false
})

const load = () => {
  request.get('/record/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      userName: data.userName
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
  data.userName = ''
  load()
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row)) //深度拷贝：先转字符串，在转json
  data.formVisible = true
}

const update = () => {
  //formRef是表单的引用
  formRef.value.validate((valid) => {
    if (valid) {  //验证通过
      request.put("/record/update", data.form).then(res => {
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
      update()
    }
  })
}

const del = (id) => {
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', {type: 'warning'}).then(res => {
    request.delete("/record/delete/" + id).then(res => {
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