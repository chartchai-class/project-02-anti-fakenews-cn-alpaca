<template>
  <div v-if="isOpen" class="fixed inset-0 bg-black/50 z-50 flex items-center justify-center">
    <div class="bg-white rounded-xl max-w-md w-full m-4 max-h-[90vh] overflow-y-auto">
      <div class="p-6 border-b flex justify-between items-center">
        <h3 class="text-xl font-bold">用户注册</h3>
        <button @click="closeModal" class="text-gray-500 hover:text-gray-700">
          <i class="fa fa-times text-xl"></i>
        </button>
      </div>
      <div class="p-6">
        <form @submit.prevent="register" class="space-y-4">
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label for="register-firstname" class="block text-gray-700 font-medium mb-2">名字</label>
              <input type="text" id="register-firstname" v-model="formData.firstName" required 
                     class="w-full border border-gray-300 rounded-lg px-4 py-3 focus:outline-none focus:ring-2 focus:ring-primary/50">
            </div>
            <div>
              <label for="register-lastname" class="block text-gray-700 font-medium mb-2">姓氏</label>
              <input type="text" id="register-lastname" v-model="formData.lastName" required 
                     class="w-full border border-gray-300 rounded-lg px-4 py-3 focus:outline-none focus:ring-2 focus:ring-primary/50">
            </div>
          </div>
          <div>
            <label for="register-username" class="block text-gray-700 font-medium mb-2">用户名</label>
            <input type="text" id="register-username" v-model="formData.username" required 
                   class="w-full border border-gray-300 rounded-lg px-4 py-3 focus:outline-none focus:ring-2 focus:ring-primary/50">
          </div>
          <div>
            <label for="register-email" class="block text-gray-700 font-medium mb-2">邮箱</label>
            <input type="email" id="register-email" v-model="formData.email" required 
                   class="w-full border border-gray-300 rounded-lg px-4 py-3 focus:outline-none focus:ring-2 focus:ring-primary/50">
          </div>
          <div>
            <label for="register-password" class="block text-gray-700 font-medium mb-2">密码</label>
            <input type="password" id="register-password" v-model="formData.password" required 
                   class="w-full border border-gray-300 rounded-lg px-4 py-3 focus:outline-none focus:ring-2 focus:ring-primary/50">
          </div>
          <div>
            <label for="register-profile-image" class="block text-gray-700 font-medium mb-2">头像URL（可选）</label>
            <input type="url" id="register-profile-image" v-model="formData.profileImage" 
                   class="w-full border border-gray-300 rounded-lg px-4 py-3 focus:outline-none focus:ring-2 focus:ring-primary/50">
          </div>
          <button type="submit" class="w-full bg-primary hover:bg-primary/90 text-white px-6 py-3 rounded-lg transition-colors">
            注册
          </button>
        </form>
        <div class="mt-4 text-center">
          <button @click="showLogin" class="text-primary hover:text-primary/80">
            已有账号？立即登录
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
const emit = defineEmits(['close', 'show-login', 'register']);

// State
const formData = ref({
  firstName: '',
  lastName: '',
  username: '',
  email: '',
  password: '',
  profileImage: ''
});

// Methods
const closeModal = () => {
  emit('close');
  resetForm();
};

const showLogin = () => {
  emit('show-login');
  resetForm();
};

const register = () => {
  emit('register', formData.value);
  resetForm();
};

const resetForm = () => {
  formData.value = {
    firstName: '',
    lastName: '',
    username: '',
    email: '',
    password: '',
    profileImage: ''
  };
};
</script>