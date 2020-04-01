<template>
  <div id="app">
    <el-container>
      <el-header style="background-color: #2c3e50">
        <router-link
                to="/"
                tag="el-button"
                style="float: left; margin-top: 10px"
        >上市公司估值</router-link
        >
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<style>
  #app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
  }
</style>
