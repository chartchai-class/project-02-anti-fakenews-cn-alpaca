<template>
  <section class="p-6">
    <!-- 页面标题和操作区 -->
    <div class="flex flex-col md:flex-row justify-between items-start md:items-center mb-8 gap-4">
      <div>
        <h2 class="text-[clamp(1.5rem,3vw,2.5rem)] font-bold text-dark">新闻列表</h2>
        <p class="text-gray-500 mt-1">共同辨别新闻真伪，维护信息真实性</p>
      </div>
      <router-link to="/submit" class="bg-primary hover:bg-primary/90 text-white px-6 py-3 rounded-full transition-all flex items-center gap-2 hover:shadow-lg hover:-translate-y-1">
        <i class="fa fa-plus"></i> 提交新闻
      </router-link>
    </div>

    <!-- 过滤器和搜索区 -->
    <div class="bg-white p-6 rounded-2xl shadow-md mb-8">
      <div class="flex flex-col md:flex-row justify-between gap-4">
        <div class="flex flex-wrap gap-4 items-center">
          <!-- 状态过滤器 -->
          <div class="flex items-center">
            <label for="status-filter" class="mr-3 text-gray-700 font-medium">新闻状态：</label>
            <select id="status-filter" v-model="currentFilter" class="border border-gray-300 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 transition-all">
              <option value="all">全部新闻</option>
              <option value="fake">假新闻</option>
              <option value="not-fake">真实新闻</option>
            </select>
          </div>
          
          <!-- 每页数量 -->
          <div class="flex items-center">
            <label for="per-page" class="mr-3 text-gray-700 font-medium">每页数量：</label>
            <select id="per-page" v-model="itemsPerPage" class="border border-gray-300 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 transition-all">
              <option value="5">5条/页</option>
              <option value="10" selected>10条/页</option>
              <option value="20">20条/页</option>
              <option value="30">30条/页</option>
            </select>
          </div>
        </div>
        
        <!-- 搜索框 -->
        <div class="w-full md:w-64">
          <div class="relative">
            <input type="text" v-model="currentSearch" placeholder="搜索新闻标题、内容或发布者..." class="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 transition-all shadow-sm">
            <i class="fa fa-search absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"></i>
          </div>
        </div>
      </div>
      
      <!-- 统计信息 -->
      <div class="mt-4 text-sm text-gray-500">
        共找到 <span class="font-medium text-primary">{{ filteredNews.length }}</span> 条新闻，当前显示第 <span class="font-medium text-primary">{{ currentPage }}</span> 页，共 <span class="font-medium text-primary">{{ totalPages }}</span> 页
      </div>
    </div>

    <!-- 新闻卡片列表 -->
    <div class="grid grid-cols-3 lg:grid-cols-4 gap-6">
      <!-- 无结果提示 -->
      <div v-if="paginatedNews.length === 0" class="col-span-full py-20 text-center">
        <i class="fa fa-search text-gray-300 text-6xl mb-4"></i>
        <p class="text-gray-500 text-lg font-medium">未找到符合条件的新闻</p>
        <p class="text-gray-400 mt-2">请尝试调整筛选条件或搜索关键词</p>
      </div>
      
      <!-- 新闻卡片 -->
      <div v-for="news in paginatedNews" :key="news.id" class="bg-white rounded-2xl overflow-hidden shadow-md hover:shadow-xl transition-all duration-300 hover:-translate-y-1 group">
        <!-- 新闻图片 -->
        <div class="relative h-56 overflow-hidden">
          <img :src="news.imageUrl || 'https://picsum.photos/800/400?random=default'" :alt="news.title" class="w-full h-full object-cover transition-transform duration-700 group-hover:scale-110">
          <span :class="getStatusBadgeClass(news.status)" class="absolute top-4 left-4 px-3 py-1 rounded-full text-xs font-medium text-white">
            {{ getStatusText(news.status) }}
          </span>
        </div>
        
        <!-- 新闻内容 -->
        <div class="p-5">
          <h3 @click="viewNewsDetail(news.id)" class="font-bold text-lg line-clamp-2 hover:text-primary transition-colors cursor-pointer mb-3 group-hover:text-primary">
            {{ news.title }}
          </h3>
          <p class="text-gray-600 text-sm line-clamp-3 mb-4 leading-relaxed">{{ news.content }}</p>
          
          <!-- 新闻元信息 -->
          <div class="flex justify-between items-center text-xs text-gray-500 pt-3 border-t border-gray-100">
            <div class="flex items-center">
              <i class="fa fa-user mr-1 text-gray-400"></i>
              <span>{{ news.reporter }}</span>
            </div>
            <div class="flex items-center mr-4">
              <i class="fa fa-clock-o mr-1 text-gray-400"></i>
              <span>{{ formatDate(news.createdAt) }}</span>
            </div>
            <div class="flex items-center space-x-4">
              <div class="flex items-center text-danger">
                <i class="fa fa-thumbs-down mr-1"></i>
                <span>{{ news.fakeVotes }}</span>
              </div>
              <div class="flex items-center text-success">
                <i class="fa fa-thumbs-up mr-1"></i>
                <span>{{ news.notFakeVotes }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 分页控件 -->
    <div class="flex justify-center items-center mt-10" v-if="totalPages > 1">
      <div class="flex items-center space-x-2 bg-white px-4 py-2 rounded-full shadow-sm">
        <!-- 上一页 -->
        <button @click="goToPage(currentPage - 1)" :disabled="currentPage === 1" class="px-4 py-2 rounded-lg transition-all disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-100">
          <i class="fa fa-chevron-left"></i>
        </button>
        
        <!-- 页码 -->
        <div class="flex space-x-1">
          <button 
            v-for="page in visiblePages" 
            :key="page"
            @click="goToPage(page)"
            :class="`px-4 py-2 rounded-lg transition-all ${currentPage === page ? 'bg-primary text-white font-medium' : 'hover:bg-gray-100'}`"
          >
            {{ page }}
          </button>
        </div>
        
        <!-- 下一页 -->
        <button @click="goToPage(currentPage + 1)" :disabled="currentPage === totalPages" class="px-4 py-2 rounded-lg transition-all disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-100">
          <i class="fa fa-chevron-right"></i>
        </button>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

// Props
const props = defineProps({
  news: Array
});

// Emits
const emit = defineEmits(['view-detail']);

// State
const currentPage = ref(1);
const itemsPerPage = ref(10);
const currentFilter = ref('all');
const currentSearch = ref('');

// Computed properties
const filteredNews = computed(() => {
  return props.news.filter(news => {
    // 状态过滤
    if (currentFilter.value !== 'all' && news.status !== currentFilter.value) {
      return false;
    }
    
    // 搜索过滤
    if (currentSearch.value && !(
      news.title.toLowerCase().includes(currentSearch.value.toLowerCase()) || 
      news.content.toLowerCase().includes(currentSearch.value.toLowerCase()) ||
      news.reporter.toLowerCase().includes(currentSearch.value.toLowerCase())
    )) {
      return false;
    }
    
    return true;
  });
});

const totalPages = computed(() => {
  return Math.ceil(filteredNews.value.length / itemsPerPage.value);
});

const paginatedNews = computed(() => {
  const startIndex = (currentPage.value - 1) * itemsPerPage.value;
  return filteredNews.value.slice(startIndex, startIndex + itemsPerPage.value);
});

const visiblePages = computed(() => {
  const maxVisiblePages = 5;
  let startPage = Math.max(1, currentPage.value - Math.floor(maxVisiblePages / 2));
  let endPage = startPage + maxVisiblePages - 1;
  
  if (endPage > totalPages.value) {
    endPage = totalPages.value;
    startPage = Math.max(1, endPage - maxVisiblePages + 1);
  }
  
  const pages = [];
  for (let i = startPage; i <= endPage; i++) {
    pages.push(i);
  }
  return pages;
});

// Watch for changes that should reset pagination
watch([currentFilter, currentSearch, itemsPerPage], () => {
  currentPage.value = 1;
});

// Methods
const formatDate = (dateString) => {
  return new Date(dateString).toLocaleString();
};

const getStatusClass = (status) => {
  if (status === 'fake') {
    return 'bg-danger/10 text-danger';
  } else if (status === 'not-fake') {
    return 'bg-success/10 text-success';
  } else {
    return 'bg-warning/10 text-warning';
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

const getStatusBadgeClass = (status) => {
  if (status === 'fake') {
    return 'bg-danger';
  } else if (status === 'not-fake') {
    return 'bg-success';
  } else {
    return 'bg-warning';
  }
};

const viewNewsDetail = (newsId) => {
  emit('view-detail', newsId);
  router.push(`/news/${newsId}`);
};

const goToPage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page;
  }
};
</script>