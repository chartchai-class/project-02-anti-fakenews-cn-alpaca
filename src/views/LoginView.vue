<template>
  <div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-gray-50 to-gray-100 py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-8 bg-white p-10 rounded-xl shadow-lg">
      <div>
        <div class="flex items-center justify-center">
          <i class="fa fa-newspaper-o text-primary text-4xl"></i>
        </div>
        <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-900">
          用户登录
        </h2>
        <p class="mt-2 text-center text-sm text-gray-600">
          或
          <router-link to="/register" class="font-medium text-primary hover:text-primary/80">
            创建新账号
          </router-link>
        </p>
      </div>
      <form class="mt-8 space-y-6" @submit.prevent="login">
        <div class="rounded-md -space-y-px">
          <div>
            <label for="username" class="sr-only">用户名</label>
            <input
              id="username"
              name="username"
              type="text"
              v-model="username"
              required
              class="appearance-none rounded-none relative block w-full px-3 py-4 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-t-md focus:outline-none focus:ring-primary focus:border-primary focus:z-10 sm:text-sm"
              placeholder="用户名"
            />
          </div>
          <div>
            <label for="password" class="sr-only">密码</label>
            <input
              id="password"
              name="password"
              type="password"
              v-model="password"
              required
              class="appearance-none rounded-none relative block w-full px-3 py-4 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-b-md focus:outline-none focus:ring-primary focus:border-primary focus:z-10 sm:text-sm"
              placeholder="密码"
            />
          </div>
        </div>

        <div>
          <button
            type="submit"
            class="group relative w-full flex justify-center py-3 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-primary hover:bg-primary/90 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary"
          >
            <span class="absolute left-0 inset-y-0 flex items-center pl-3">
              <i class="fa fa-lock text-white opacity-50"></i>
            </span>
            登录
          </button>
        </div>
      </form>
      <div class="text-center">
        <button
          @click="goToAdminLogin"
          class="group relative w-full flex justify-center py-3 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-gray-600 hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500 mt-4"
        >
          <span class="absolute left-0 inset-y-0 flex items-center pl-3">
            <i class="fa fa-cog text-white opacity-50"></i>
          </span>
          管理员登录
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { authApi } from '../utils/api.js';

const router = useRouter();

// State
const username = ref('');
const password = ref('');

// Methods
const login = async () => {
  try {
    const response = await authApi.login({ username: username.value, password: password.value });
    if (response.token) {
      localStorage.setItem('token', response.token);
      localStorage.setItem('currentUser', JSON.stringify(response.user));
      localStorage.setItem('isAdmin', JSON.stringify(response.user.role === 'admin'));
      alert(`欢迎回来，${response.user.firstName}${response.user.lastName}`);
      router.push('/');
    } else {
      alert('登录失败');
    }
  } catch (error) {
    console.error('登录失败:', error);
    alert('用户名或密码错误');
  }
};

const goToAdminLogin = () => {
  router.push('/admin-login');
};
</script>