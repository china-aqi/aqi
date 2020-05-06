<template>
  <div>
    <Search></Search>
    <el-radio-group v-model="radio">
      <el-radio :label="1">公司质量分析</el-radio>
      <el-radio :label="2">内在价值估值</el-radio>
    </el-radio-group>
  </div>
</template>

<script>
  import Search from "../components/Search";

  export default {
    name: "Home",
    components: {
      Search: Search
    },
    data () {
      return {
        radio: 2
      };
    }
  };
</script>

<style scoped></style>
