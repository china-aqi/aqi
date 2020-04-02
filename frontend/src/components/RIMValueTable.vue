<template>
  <div>
    <h4>{{ tblTitle }}</h4>
    <div class="rim_table">
      <el-table
        :data="tblData"
        style="width: 95%"
        :show-header="false"
      >
        <el-table-column prop="des" label="标题" width="95"> </el-table-column>
        <el-table-column prop="value" label="数据" width="95">
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
export default {
  name: "RIMValueTable",
  props: { tblData: Array, tblTitle: String }
};
</script>

<style>
.rim_table {
  font-size: small;
  padding-left: 10px;
}
</style>
