<template>
  <div v-if="isOpen" class="fixed inset-0 bg-black/50 z-50 flex items-center justify-center">
    <div class="bg-white rounded-xl max-w-md w-full m-4">
      <div class="p-6 border-b flex justify-between items-center">
        <h3 class="text-xl font-bold">用户登录</h3>
        <button @click="closeModal" class="text-gray-500 hover:text-gray-700">
          <i class="fa fa-times text-xl"></i>
        </button>
      </div>
      <div class="p-6">
        <form @submit.prevent="login" class="space-y-4">
          <div>
            <label for="login-username" class="block text-gray-700 font-medium mb-2">用户名</label>
            <input type="text" id="login-username" v-model="username" required 
                   class="w-full border border-gray-300 rounded-lg px-4 py-3 focus:outline-none focus:ring-2 focus:ring-primary/50">
          </div>
          <div>
            <label for="login-password" class="block text-gray-700 font-medium mb-2">密码</label>
            <input type="password" id="login-password" v-model="password" required 
                   class="w-full border border-gray-300 rounded-lg px-4 py-3 focus:outline-none focus:ring-2 focus:ring-primary/50">
          </div>
          <button type="submit" class="w-full bg-primary hover:bg-primary/90 text-white px-6 py-3 rounded-lg transition-colors">
            登录
          </button>
        </form>
        <div class="mt-4 text-center">
          <button @click="showRegister" class="text-primary hover:text-primary/80">
            没有账号？立即注册
          </button>
        </div>
        <div class="mt-4 pt-4 border-t">
          <button @click="showAdminLogin" class="w-full bg-gray-500 hover:bg-gray-600 text-white px-6 py-2 rounded-lg transition-colors">
            管理员登录
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

// Props
const props = defineProps({
  isOpen: Boolean
});

// Emits
const emit = defineEmits(['close', 'show-register', 'show-admin-login', 'login']);

// State
const username = ref('');
const password = ref('');

// Methods
const closeModal = () => {
  emit('close');
  resetForm();
};

const showRegister = () => {
  emit('show-register');
  resetForm();
};

const showAdminLogin = () => {
  emit('show-admin-login');
  resetForm();
};

const login = () => {
  emit('login', { username: username.value, password: password.value });
  resetForm();
};

const resetForm = () => {
  username.value = '';
  password.value = '';
};
</script>