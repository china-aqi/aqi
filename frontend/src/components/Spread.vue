<template>
  <div id="box">
    <transition name="mybox">
      <div class="box" v-show="boxshow"><rim-value></rim-value></div>
    </transition>
    <div class="spreadButton" v-if="showFlag()">
      <el-button @click="togglebox">展开 模型说明</el-button>
    </div>
    <div class="spreadButton" v-else>
      <el-button @click="togglebox">收起 模型说明</el-button>
    </div>
  </div>
</template>
<script>
import rimValue from "../assets/剩余收益估值.md";
export default {
  name: "Spread",
  components: { rimValue },
  data() {
    return {
      boxshow: false
    };
  },
  props: {
    mes2: {
      type: String,
      default: ""
    }
  },
  methods: {
    togglebox: function() {
      this.boxshow = !this.boxshow;
    },

    showFlag() {
      return !this.boxshow;
    }
  }
};
</script>

<style>
.box {
  margin-top: 50px;
  height: 1500px;
  background-color: #f2f6fc;
  overflow: hidden;
  text-align: left;
}
.mybox-leave-active,
.mybox-enter-active {
  transition: all 0.5s ease;
}
.mybox-leave-active,
.mybox-enter {
  height: 0px !important;
}
.mybox-leave,
.mybox-enter-active {
  height: 500px;
}
.spreadButton {
  margin-top: 20px;
}
</style>
