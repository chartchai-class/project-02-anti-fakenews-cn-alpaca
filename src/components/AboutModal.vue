<template>
  <div v-if="isOpen" class="fixed inset-0 bg-black/50 z-50 flex items-center justify-center">
    <div class="bg-white rounded-xl max-w-2xl w-full max-h-[90vh] overflow-y-auto m-4">
      <div class="p-6 border-b flex justify-between items-center">
        <h3 class="text-xl font-bold">关于真相卫士</h3>
        <button @click="closeModal" class="text-gray-500 hover:text-gray-700">
          <i class="fa fa-times text-xl"></i>
        </button>
      </div>
      <div class="p-6 space-y-4">
        <p>真相卫士是一个社会反假新闻系统，旨在通过集体智慧帮助公众辨别新闻真伪。</p>
        <p>系统功能：</p>
        <ul class="list-disc pl-5 space-y-1">
          <li>用户可以提交新闻供公众评判</li>
          <li>查看所有新闻并按真实性筛选</li>
          <li>对新闻的真实性进行投票和评论</li>
          <li>查看新闻的详细信息和公众投票结果</li>
        </ul>
        <p>我们相信，通过集体的力量，能够有效识别和抵制假新闻，维护健康的信息环境。</p>
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
const emit = defineEmits(['close']);

// Methods
const closeModal = () => {
  emit('close');
};
</script>