<template>
  <div id="box">
    <transition name="mybox">
      <div class="box" v-show="boxshow">
        <VueShowdown   :markdown="markdown"
                       flavor="github"
                       :options="{ emoji: true }"/>
      </div>
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
import { VueShowdown } from 'vue-showdown'

export default {
  name: "Spread",

  components: { VueShowdown },
  data() {
    return {
      boxshow: false,
      md: "## 你好showdown",
      test: 1
    };
  },
  props: {
    markdown: {
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
