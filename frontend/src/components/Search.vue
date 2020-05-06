<template>
  <div class="searchbar">
    <el-autocomplete
      class="inline-input"
      v-model="state1"
      :fetch-suggestions="querySearch"
      placeholder="请输公司名称/公司代码/拼音简称"
      :trigger-on-focus="false"
      @select="handleSelect"
      style="width: 400px"
      ><i
            class="el-icon-search el-input__icon"
            slot="suffix"
>
    </i></el-autocomplete>
  </div>
</template>

<script>
import axios from "axios";
export default {
  name: "App",

  data() {
    return {
      securities: [],
      state1: ""
    };
  },

  methods: {
      test(x, cb) {
          this.securities=[]
          for (let i in x["data"]["_embedded"]["Security"]) {
              this.securities.unshift({value : x["data"]["_embedded"]["Security"][i]["match"]})
          }
          cb(this.securities)
      },
    querySearch(queryString, cb) {
        if (queryString.length >= 2 && queryString.length <= 6) {
            axios.get("http://106.15.137.244:8080/security/search/findFirst7ByMatchContaining?match="+this.state1,)
                .then(x => this.test(x, cb)) //this.handle_securities_4_combobox);
        }
    },
    createFilter(queryString) {
      return securities => {
        return (
          securities.value.toLowerCase().indexOf(queryString.toLowerCase()) >= 0
        );
      };
    },
    handleSelect(item) {
      console.log(item);
      this.$router.push({
        path: "/RIMValue/",
        query: {
          code: this.state1.replace(/[^0-9]/gi, ""),
          name: this.state1.slice(0, this.state1.indexOf("("))
        }
      });
    },
    jump() {
      this.$router.push("/RIMValue/" + this.state1.replace(/[^0-9]/gi, ""));
    }
  }
};
</script>

<style>
.searchbar {
  margin-top: 10%;
}
</style>
