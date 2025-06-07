<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

onMounted(() => {
  // 应用启动时检查登录状态
  userStore.checkLoginStatus()
})
</script>

<style>
#app {
  height: 100%;
}
</style>