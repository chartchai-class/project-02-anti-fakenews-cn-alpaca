<template>
  <section class="p-6">
    <!-- 返回按钮 -->
    <router-link to="/" class="inline-flex items-center gap-2 text-primary hover:text-primary/80 transition-colors mb-8 hover:underline">
      <i class="fa fa-arrow-left"></i> 返回新闻列表
    </router-link>

    <!-- 表单容器 -->
    <div class="bg-white rounded-2xl shadow-md overflow-hidden">
      <!-- 表单标题 -->
      <div class="bg-gradient-to-r from-primary to-primary/90 px-8 py-6">
        <h2 class="text-2xl font-bold text-white">提交新闻</h2>
        <p class="text-white/80 mt-1">请填写完整的新闻信息，我们将进行审核</p>
      </div>
      
      <!-- 表单内容 -->
      <form @submit.prevent="submitNews" class="p-8 space-y-8">
        <!-- 新闻标题 -->
        <div class="space-y-3">
          <label for="news-title" class="block text-gray-700 font-medium">
            新闻标题 <span class="text-danger">*</span>
          </label>
          <input 
            type="text" 
            id="news-title" 
            v-model="formData.title" 
            required 
            placeholder="请输入新闻标题" 
            class="w-full border border-gray-300 rounded-xl px-5 py-3 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 transition-all shadow-sm hover:shadow-md"
          >
        </div>

        <!-- 新闻内容 -->
        <div class="space-y-3">
          <label for="news-content" class="block text-gray-700 font-medium">
            新闻内容 <span class="text-danger">*</span>
          </label>
          <textarea 
            id="news-content" 
            v-model="formData.content" 
            required 
            rows="10" 
            placeholder="请详细描述新闻内容，包括时间、地点、人物、事件等关键信息"
            class="w-full border border-gray-300 rounded-xl px-5 py-3 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 transition-all shadow-sm hover:shadow-md resize-none"
          ></textarea>
        </div>

        <!-- 发布者和图片URL -->
        <div class="grid grid-cols-2 gap-8">
          <!-- 发布者 -->
          <div class="space-y-3">
            <label for="news-reporter" class="block text-gray-700 font-medium">
              发布者 <span class="text-danger">*</span>
            </label>
            <input 
              type="text" 
              id="news-reporter" 
              v-model="formData.reporter" 
              required 
              placeholder="请输入您的姓名或机构名称"
              class="w-full border border-gray-300 rounded-xl px-5 py-3 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 transition-all shadow-sm hover:shadow-md"
            >
          </div>
          
          <!-- 新闻图片URL -->
          <div class="space-y-3">
            <label for="news-image" class="block text-gray-700 font-medium">
              新闻图片URL（可选）
            </label>
            <input 
              type="url" 
              id="news-image" 
              v-model="formData.imageUrl" 
              placeholder="https://example.com/image.jpg" 
              class="w-full border border-gray-300 rounded-xl px-5 py-3 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 transition-all shadow-sm hover:shadow-md"
            >
            <p class="text-xs text-gray-500">
              请提供新闻相关图片的URL，支持JPG、PNG等格式
            </p>
          </div>
        </div>

        <!-- 表单操作按钮 -->
        <div class="flex justify-end gap-4 pt-4 border-t border-gray-100">
          <router-link 
            to="/" 
            class="px-6 py-3 border border-gray-300 rounded-xl text-gray-700 hover:bg-gray-50 transition-all hover:shadow-md"
          >
            取消
          </router-link>
          <button 
            type="submit" 
            class="bg-primary hover:bg-primary/90 text-white px-8 py-3 rounded-xl transition-all hover:shadow-lg hover:-translate-y-1 flex items-center gap-2"
          >
            <i class="fa fa-paper-plane"></i> 提交新闻
          </button>
        </div>
      </form>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

// Emits
const emit = defineEmits(['submit-news']);

// State
const formData = ref({
  title: '',
  content: '',
  reporter: '',
  imageUrl: ''
});

// Methods
const submitNews = () => {
  emit('submit-news', formData.value);
  // 重置表单
  formData.value = {
    title: '',
    content: '',
    reporter: '',
    imageUrl: ''
  };
  // 返回新闻列表
  router.push('/');
};
</script>