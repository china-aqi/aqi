<template>
  <strong class="rim_result">内在价值：{{ result }}</strong>
</template>

<script>
export default {
  name: "RIMValueResult",
  props: { result: String }
};
</script>

<style>
.rim_result {
  font-size: x-large;
}
</style>
