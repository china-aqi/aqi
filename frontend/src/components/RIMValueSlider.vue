<template>
  <div class="block">
    <el-row>
      <el-col :span="24">
        <div class="detailbar">{{ name }}</div>
      </el-col>
    </el-row>
    <el-slider
      v-model="value"
      :step="step"
      :min="min"
      :max="max"
      :show-tooltip="false"
    >
    </el-slider>
  </div>
</template>

<script>
export default {
  name: "RIMValueSlider",
  props: { ConfigInfo: Object, CaptionFn: Function },
  data() {
    return {
      name: this.CaptionFn(this.ConfigInfo.defaultValue),
      value: this.ConfigInfo.defaultValue,
      min: this.ConfigInfo.min,
      max: this.ConfigInfo.max,
      step: this.ConfigInfo.step
    };
  },
  methods: {
    sendValue() {
      //func: 是父组件指定的传数据绑定的函数，this.msg:子组件给父组件传递的数据
      this.$emit("func", this.value);
    }
  },
  watch: {
    value() {
      this.sendValue();
      this.name = this.CaptionFn(this.value);
    }
  }
};
</script>

<style>
.detailbar {
  height: 16px;
  text-align: left;
}
</style>
