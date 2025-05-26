<template>
  <div>
    <el-button>Default</el-button>
    <el-button type="primary">Primary</el-button>
    <el-button type="success">Success</el-button>
    <el-button type="info">Info</el-button>
    <el-button type="warning">Warning</el-button>
    <el-button type="danger">Danger</el-button>

    <el-icon><Search /></el-icon>

    <div style="padding: 50px">
      <el-icon size="25" color="red"><Search /></el-icon>
    </div>

    <el-input placeholder="placeholder" :prefix-icon="Search" :suffix-icon="Calendar"></el-input>

    <el-button type="primary" icon="Search">°´Å¥</el-button>
  </div>
</template>

<script setup>
import {Calendar, Search} from "@element-plus/icons-vue";
</script>
