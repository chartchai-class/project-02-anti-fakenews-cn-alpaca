<template>
  <section class="space-y-8 container mx-auto px-4 py-8">
    <div class="flex justify-between items-center">
      <h2 class="text-2xl font-bold">管理员面板</h2>
      <router-link to="/" class="bg-gray-500 hover:bg-gray-600 text-white px-4 py-2 rounded-lg transition-colors">
        返回用户界面
      </router-link>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <!-- 用户管理 -->
      <div class="bg-white rounded-xl p-6 card-shadow">
        <h3 class="text-xl font-bold mb-4">用户管理</h3>
        <div class="space-y-3 max-h-96 overflow-y-auto">
          <div v-for="user in users" :key="user.id" class="flex justify-between items-center p-3 border rounded-lg">
            <div class="flex items-center gap-3">
              <div class="w-8 h-8 bg-gray-200 rounded-full flex items-center justify-center">
                <i class="fa fa-user text-gray-500"></i>
              </div>
              <div>
                <div class="font-medium">{{ user.username }}</div>
                <div class="text-xs text-gray-500">{{ user.email }}</div>
              </div>
            </div>
            <span :class="user.role === 'admin' ? 'bg-secondary text-white' : 'bg-primary text-white'" class="px-2 py-1 rounded-full text-xs font-medium">
              {{ user.role === 'admin' ? '管理员' : '用户' }}
            </span>
          </div>
        </div>
      </div>

      <!-- 新闻管理 -->
      <div class="bg-white rounded-xl p-6 card-shadow">
        <h3 class="text-xl font-bold mb-4">新闻管理</h3>
        <div class="mb-4">
          <button @click="addNews" class="bg-primary hover:bg-primary/90 text-white px-4 py-2 rounded-lg transition-colors">
            添加新闻
          </button>
        </div>
        <div class="space-y-3 max-h-96 overflow-y-auto">
          <div v-for="news in newsList" :key="news.id" class="p-3 border rounded-lg">
            <div class="flex justify-between items-start mb-2">
              <h4 class="font-medium line-clamp-1">{{ news.title }}</h4>
              <span :class="getStatusBadgeClass(news.status)" class="px-2 py-1 rounded-full text-xs font-medium text-white">
                {{ getStatusText(news.status) }}
              </span>
            </div>
            <div class="flex justify-between items-center text-xs text-gray-500">
              <span>{{ news.reporter }}</span>
              <div class="flex items-center gap-2">
                <button @click="editNews(news)" class="text-primary hover:text-primary/80">
                  <i class="fa fa-pencil"></i>
                </button>
                <button @click="deleteNews(news.id)" class="text-danger hover:text-danger/80">
                  <i class="fa fa-trash"></i>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 统计数据 -->
      <div class="bg-white rounded-xl p-6 card-shadow">
        <h3 class="text-xl font-bold mb-4">统计数据</h3>
        <div class="space-y-4">
          <div class="p-4 bg-gray-50 rounded-lg">
            <div class="text-sm text-gray-500 mb-1">总用户数</div>
            <div class="text-2xl font-bold">{{ users.length }}</div>
          </div>
          <div class="p-4 bg-gray-50 rounded-lg">
            <div class="text-sm text-gray-500 mb-1">总新闻数</div>
            <div class="text-2xl font-bold">{{ newsList.length }}</div>
          </div>
          <div class="p-4 bg-gray-50 rounded-lg">
            <div class="text-sm text-gray-500 mb-1">假新闻数</div>
            <div class="text-2xl font-bold text-danger">{{ fakeNewsCount }}</div>
          </div>
          <div class="p-4 bg-gray-50 rounded-lg">
            <div class="text-sm text-gray-500 mb-1">真实新闻数</div>
            <div class="text-2xl font-bold text-success">{{ notFakeNewsCount }}</div>
          </div>
          <div class="p-4 bg-gray-50 rounded-lg">
            <div class="text-sm text-gray-500 mb-1">待验证新闻数</div>
            <div class="text-2xl font-bold text-warning">{{ pendingNewsCount }}</div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { computed } from 'vue';

// Props
const props = defineProps({
  users: Array,
  newsList: Array
});

// Emits
const emit = defineEmits(['add-news', 'edit-news', 'delete-news']);

// Computed properties
const fakeNewsCount = computed(() => {
  return props.newsList.filter(news => news.status === 'fake').length;
});

const notFakeNewsCount = computed(() => {
  return props.newsList.filter(news => news.status === 'not-fake').length;
});

const pendingNewsCount = computed(() => {
  return props.newsList.filter(news => news.status === 'pending').length;
});

// Methods
const getStatusBadgeClass = (status) => {
  if (status === 'fake') {
    return 'bg-danger';
  } else if (status === 'not-fake') {
    return 'bg-success';
  } else {
    return 'bg-warning';
  }
};

const getStatusText = (status) => {
  if (status === 'fake') {
    return '假新闻';
  } else if (status === 'not-fake') {
    return '真实新闻';
  } else {
    return '待验证';
  }
};

const addNews = () => {
  emit('add-news');
};

const editNews = (news) => {
  emit('edit-news', news);
};

const deleteNews = (newsId) => {
  emit('delete-news', newsId);
};
</script>