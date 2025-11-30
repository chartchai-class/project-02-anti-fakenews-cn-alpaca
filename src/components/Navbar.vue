<template>
  <header class="sticky top-0 z-50 bg-gradient-to-r from-primary to-primary/90 shadow-md transition-all duration-300 hover:shadow-lg">
    <div class="container mx-auto px-4 py-3 flex justify-between items-center">
      <div class="flex items-center space-x-2">
        <i class="fa fa-newspaper-o text-white text-2xl"></i>
        <h1 class="text-2xl font-bold text-white">真相卫士</h1>
      </div>
      <nav class="flex items-center space-x-6" style="z-index: 100;">
        <router-link to="/" class="font-medium text-white hover:text-gray-100 transition-colors hover:underline underline-offset-4">首页</router-link>
        <router-link to="/submit" class="font-medium text-white hover:text-gray-100 transition-colors hover:underline underline-offset-4">提交新闻</router-link>
        <button class="bg-white/20 hover:bg-white/30 text-white px-5 py-2 rounded-full transition-all backdrop-blur-sm hover:shadow-lg" @click="showAbout">
          <i class="fa fa-info-circle mr-1"></i> 关于系统
        </button>
        <button v-if="isAdmin" class="bg-secondary hover:bg-secondary/90 text-white px-5 py-2 rounded-full transition-all hover:shadow-lg" @click="goToAdminPanel">
          <i class="fa fa-cog mr-1"></i> 管理员面板
        </button>
        <div v-if="currentUser" class="flex items-center space-x-3">
          <div class="flex items-center space-x-2 cursor-pointer group">
            <div class="w-10 h-10 bg-white/20 rounded-full flex items-center justify-center text-white font-medium">
              {{ (currentUser.firstName || '用')[0] }}{{ (currentUser.lastName || '户')[0] }}
            </div>
            <span class="text-white font-medium group-hover:underline">
              {{ currentUser.firstName }}{{ currentUser.lastName }}
            </span>
          </div>
          <button class="bg-white/20 hover:bg-white/30 text-white px-5 py-2 rounded-full transition-all backdrop-blur-sm hover:shadow-lg" @click="logout">
            <i class="fa fa-sign-out mr-1"></i> 退出登录
          </button>
        </div>
        <button v-else class="bg-white hover:bg-gray-100 text-primary px-5 py-2 rounded-full transition-all font-medium hover:shadow-lg" @click="showLogin">
          <i class="fa fa-user mr-1"></i> 登录/注册
        </button>
      </nav>
    </div>
  </header>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

// Props
const props = defineProps({
  currentUser: Object,
  isAdmin: Boolean
});

// Emits
const emit = defineEmits(['show-about', 'logout']);

// Methods
const showLogin = () => {
  router.push('/login');
};

const showAbout = () => {
  emit('show-about');
};

const goToAdminPanel = () => {
  router.push('/admin');
};

const logout = () => {
  emit('logout');
};
</script>