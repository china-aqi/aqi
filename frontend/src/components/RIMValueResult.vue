<template>
  <strong class="rim_result">内在价值：{{ result.toFixed(2) }}</strong>
</template>

<script>
export default {
  name: "RIMValueResult",
  props: { result: Number }
};
</script>

<style>
.rim_result {
  font-size: x-large;
}
</style>
