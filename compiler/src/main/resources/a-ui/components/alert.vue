<template>
  <div class="alert-container">
    <div class="title">{{ title }}</div>
    <div class="body">
      <slot></slot>
    </div>
  </div>
</template>
<script setup>
defineProps(["title"])
import sense from "sense";

mounted(() => {
  sense.add(el)
})
beforeUnmounted(() => {
  sense.remote(el)
})
</script>
<style scoped>
.alert-container {
  background: black;
  color: yellow;
  width: 50px;
  height: 50px;
  display: flex;
  flex-direction: column;
}
.title {
  padding: 5px 2px;
}
.body{
  padding: 5px 2px;
  flex: 1;
}
</style>