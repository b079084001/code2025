<template>
  <!--头部区域开始-->
  <div style="height: 60px;display: flex">
    <div style="width: 240px;display: flex;align-items: center;padding-left: 10px;background-color: #3a456b;">
      <img style="width: 40px;height: 40px;border-radius: 50%" src="@/assets/imgs/logo.png" alt="">
      <div>
        <span style="font-size: 20px;font-weight: bold;color: #2c82ff;margin-left: 5px">Graduation2025</span>
      </div>
    </div>

    <div style="flex:1;border-bottom: 1px solid #ddd;display: flex;align-items: center;">
      Home/Navigator One
    </div>

    <div style="width: fit-content;display: flex;align-items: center;padding-right: 20px;border-bottom: 1px solid #ddd">
      <el-dropdown>
        <div style="display: flex;align-items: center;">
          <img style="width: 40px;height: 40px;"src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" alt="">
          <span style="margin-left: 5px" >Administrator</span>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item>Action 11111</el-dropdown-item>
            <el-dropdown-item>Action 21111</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>

    </div>
  </div>
  <!--头部区域结束-->

  <!--下方区域开始-->
  <div style="display: flex">
    <!--菜单区域开始-->
    <div style="width: 240px">
      <el-menu router :default-openeds="['1']" :default-active="router.currentRoute.value.path" style="min-height: calc(100vh - 60px)">
        <el-menu-item index="/manager/home">
          <el-icon><House /></el-icon>
          <span>Home</span>
        </el-menu-item>
        <el-sub-menu index="1">
          <template #title>
            <el-icon><location /></el-icon>
            <span>Navigator One</span>
          </template>
          <el-menu-item index="/manager/about">About</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </div>
    <!--菜单区域结束-->

    <!--数据渲染区域开始-->
    <div style="flex: 1;width: 0;padding: 10px;background-color: #f2f4ff;">
      <RouterView />
    </div>
    <!--数据渲染区域结束-->
  </div>
  <!--下方区域结束-->

</template>


<script setup>

import router from "@/router/index.js";
</script>

<style>
.el-menu {
  background-color: #3a456b;
  border: none;
  /**border-color: #3a456b;**/
}

.el-sub-menu__title {
  background-color: #3a456b;
  color: #ddd;
}

.el-menu-item {
  height: 50px;
  color: #ddd;
}

.el-menu .is-active{
  background-color: #537bee;
  color: #fff;
}
.el-sub-menu__title:hover {
  background-color: #3a456b;
}

.el-menu-item:not(.is-active):hover {
  background-color: #537bee;
  color: #333;
}

.el-dropdown {
  cursor: pointer;
}
.el-tooltip__trigger {
  outline: none;
}

.el-menu--inline .el-menu-item {
  padding-left: 48px !important;
}
</style>