<template>
  <div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-gray-50 to-gray-100 py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-8 bg-white p-10 rounded-xl shadow-lg">
      <div>
        <div class="flex items-center justify-center">
          <i class="fa fa-newspaper-o text-primary text-4xl"></i>
        </div>
        <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-900">
          创建新账号
        </h2>
        <p class="mt-2 text-center text-sm text-gray-600">
          或
          <router-link to="/login" class="font-medium text-primary hover:text-primary/80">
            已有账号？立即登录
          </router-link>
        </p>
      </div>
      <form class="mt-8 space-y-6" @submit.prevent="register">
        <div class="grid grid-cols-1 gap-4">
          <div>
            <label for="firstName" class="sr-only">名</label>
            <input
              id="firstName"
              name="firstName"
              type="text"
              v-model="firstName"
              required
              class="appearance-none rounded-none relative block w-full px-3 py-4 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-primary focus:border-primary focus:z-10 sm:text-sm"
              placeholder="名"
            />
          </div>
          <div>
            <label for="lastName" class="sr-only">姓</label>
            <input
              id="lastName"
              name="lastName"
              type="text"
              v-model="lastName"
              required
              class="appearance-none rounded-none relative block w-full px-3 py-4 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-primary focus:border-primary focus:z-10 sm:text-sm"
              placeholder="姓"
            />
          </div>
        </div>
        <div>
          <label for="username" class="sr-only">用户名</label>
          <input
            id="username"
            name="username"
            type="text"
            v-model="username"
            required
            class="appearance-none rounded-none relative block w-full px-3 py-4 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-primary focus:border-primary focus:z-10 sm:text-sm"
            placeholder="用户名"
          />
        </div>
        <div>
          <label for="email" class="sr-only">邮箱</label>
          <input
            id="email"
            name="email"
            type="email"
            v-model="email"
            required
            class="appearance-none rounded-none relative block w-full px-3 py-4 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-primary focus:border-primary focus:z-10 sm:text-sm"
            placeholder="邮箱"
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
            class="appearance-none rounded-none relative block w-full px-3 py-4 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-primary focus:border-primary focus:z-10 sm:text-sm"
            placeholder="密码"
          />
        </div>
        <div>
          <label for="confirmPassword" class="sr-only">确认密码</label>
          <input
            id="confirmPassword"
            name="confirmPassword"
            type="password"
            v-model="confirmPassword"
            required
            class="appearance-none rounded-none relative block w-full px-3 py-4 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-primary focus:border-primary focus:z-10 sm:text-sm"
            placeholder="确认密码"
          />
        </div>

        <div>
          <button
            type="submit"
            class="group relative w-full flex justify-center py-3 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-primary hover:bg-primary/90 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary"
          >
            <span class="absolute left-0 inset-y-0 flex items-center pl-3">
              <i class="fa fa-user-plus text-white opacity-50"></i>
            </span>
            注册
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { authApi } from '../utils/api.js';

const router = useRouter();

// State
const firstName = ref('');
const lastName = ref('');
const username = ref('');
const email = ref('');
const password = ref('');
const confirmPassword = ref('');

// Methods
const register = async () => {
  if (password.value !== confirmPassword.value) {
    alert('两次输入的密码不一致');
    return;
  }

  try {
    const response = await authApi.register({
      firstName: firstName.value,
      lastName: lastName.value,
      username: username.value,
      email: email.value,
      password: password.value
    });
    if (response.user) {
      alert('注册成功，请登录');
      router.push('/login');
    } else {
      alert('注册失败');
    }
  } catch (error) {
    console.error('注册失败:', error);
    alert('注册失败，请检查输入信息');
  }
};
</script>