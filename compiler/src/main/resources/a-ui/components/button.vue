<template>
  <div class="button-container">
    <slot v-if="$slots.default"></slot>
    <div v-else>Button</div>
  </div>
</template>
<style scoped>
.button-container {
  padding: 10px 5px;
  background: black;
  color: yellow;
}
</style>
