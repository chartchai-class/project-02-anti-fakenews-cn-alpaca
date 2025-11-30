<template>
  <section class="space-y-8">
    <router-link to="/" class="flex items-center gap-2 text-primary hover:text-primary/80 transition-colors">
      <i class="fa fa-arrow-left"></i> 返回列表
    </router-link>

    <article v-if="news" class="bg-white rounded-xl overflow-hidden card-shadow">
      <div class="relative h-64 md:h-80">
        <img :src="news.imageUrl || 'https://picsum.photos/800/400?random=default'" :alt="news.title" class="w-full h-full object-cover">
        <div :class="getStatusBadgeClass(news.status)" class="absolute top-4 right-4 px-3 py-1 rounded-full text-sm font-medium text-white">
          {{ getStatusText(news.status) }}
        </div>
      </div>
      <div class="p-6">
        <h1 class="text-2xl md:text-3xl font-bold mb-4 text-balance">{{ news.title }}</h1>
        
        <div class="flex flex-wrap items-center gap-x-6 gap-y-2 text-sm text-gray-500 mb-6">
          <div class="flex items-center gap-1">
            <i class="fa fa-user"></i>
            <span>{{ news.reporter }}</span>
          </div>
          <div class="flex items-center gap-1">
            <i class="fa fa-clock-o"></i>
            <span>{{ formatDate(news.createdAt) }}</span>
          </div>
          <div class="flex items-center gap-1">
            <i class="fa fa-bar-chart"></i>
            <span>假新闻: {{ news.fakeVotes }}票 / 真实新闻: {{ news.notFakeVotes }}票</span>
          </div>
        </div>

        <div class="prose max-w-none mb-8">
          <p class="text-gray-700 leading-relaxed">{{ news.content }}</p>
        </div>

        <!-- 投票区域 -->
        <div class="border-t border-gray-100 pt-6 pb-4">
          <h3 class="text-lg font-semibold mb-4">您认为这是假新闻吗？</h3>
          <div class="flex gap-4">
            <button 
              @click="vote('fake')"
              :disabled="hasVoted"
              class="flex-1 bg-danger/10 hover:bg-danger/20 text-danger border border-danger/30 px-4 py-3 rounded-lg transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
            >
              <i class="fa fa-times-circle"></i>
              <span>是假新闻</span>
            </button>
            <button 
              @click="vote('not-fake')"
              :disabled="hasVoted"
              class="flex-1 bg-success/10 hover:bg-success/20 text-success border border-success/30 px-4 py-3 rounded-lg transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
            >
              <i class="fa fa-check-circle"></i>
              <span>不是假新闻</span>
            </button>
          </div>
          <div v-if="showVoteComment" class="mt-4">
            <textarea v-model="voteComment" placeholder="请说明您的判断依据（可选）..." class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-primary/50 resize-none" rows="3"></textarea>
            <div class="mt-2 flex justify-between items-center">
              <div class="text-sm text-gray-500">
                <input type="text" v-model="commentImageUrl" placeholder="图片URL（可选）..." class="w-full md:w-64 border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary/50 text-sm">
              </div>
              <button @click="submitVote" class="bg-primary hover:bg-primary/90 text-white px-4 py-2 rounded-lg transition-colors">
                提交
              </button>
            </div>
          </div>
        </div>
      </div>
    </article>

    <!-- 评论区 -->
    <section class="bg-white rounded-xl p-6 card-shadow">
      <div class="flex justify-between items-center mb-6">
        <h3 class="text-xl font-bold">用户评论与投票</h3>
        <div class="flex gap-4">
          <div class="flex items-center gap-1">
            <span class="text-danger font-medium">{{ news?.fakeVotes || 0 }}</span>
            <span class="text-gray-500">人认为是假新闻</span>
          </div>
          <div class="flex items-center gap-1">
            <span class="text-success font-medium">{{ news?.notFakeVotes || 0 }}</span>
            <span class="text-gray-500">人认为是真实新闻</span>
          </div>
        </div>
      </div>

      <!-- 评论列表 -->
      <div class="space-y-6">
        <div v-if="!news || news.comments.length === 0" class="text-center py-8">
          <p class="text-gray-500">暂无评论</p>
        </div>
        <div v-for="comment in paginatedComments" :key="comment.id" class="border-b border-gray-100 pb-4">
          <div class="flex items-start gap-3">
            <div class="flex-shrink-0 w-10 h-10 bg-gray-200 rounded-full flex items-center justify-center">
              <i class="fa fa-user text-gray-500"></i>
            </div>
            <div class="flex-1">
              <div class="flex justify-between items-start">
                <div class="flex items-center gap-2">
                  <span class="font-medium">{{ comment.username || '匿名用户' }}</span>
                  <span class="text-xs text-gray-500">{{ formatDate(comment.createdAt) }}</span>
                </div>
                <span :class="comment.vote === 'fake' ? 'text-danger' : 'text-success'" class="text-xs font-medium">
                  {{ comment.vote === 'fake' ? '假新闻' : '真实新闻' }}
                </span>
              </div>
              <p class="mt-2 text-gray-700">{{ comment.comment }}</p>
              <img v-if="comment.imageUrl" :src="comment.imageUrl" :alt="'评论图片'" class="mt-3 rounded-lg max-w-full h-auto">
            </div>
          </div>
        </div>
      </div>

      <!-- 评论分页 -->
      <div v-if="news && news.comments.length > commentsPerPage" class="flex justify-center items-center mt-8">
        <button @click="goToCommentPage(commentsCurrentPage - 1)" :disabled="commentsCurrentPage === 1" class="px-4 py-2 border border-gray-300 rounded-l-lg hover:bg-gray-100 transition-colors disabled:opacity-50 disabled:cursor-not-allowed">
          <i class="fa fa-chevron-left"></i>
        </button>
        <div class="flex">
          <button 
            v-for="page in totalCommentPages" 
            :key="page"
            @click="goToCommentPage(page)"
            :class="`px-4 py-2 border-t border-b ${commentsCurrentPage === page ? 'bg-primary text-white border-primary' : 'hover:bg-gray-100 border-gray-300'}`"
          >
            {{ page }}
          </button>
        </div>
        <button @click="goToCommentPage(commentsCurrentPage + 1)" :disabled="commentsCurrentPage === totalCommentPages" class="px-4 py-2 border border-gray-300 rounded-r-lg hover:bg-gray-100 transition-colors disabled:opacity-50 disabled:cursor-not-allowed">
          <i class="fa fa-chevron-right"></i>
        </button>
      </div>
    </section>
  </section>
</template>

<script setup>
import { ref, computed, watch } from 'vue';

// Props
const props = defineProps({
  news: Object,
  userVotedNews: Array
});

// Emits
const emit = defineEmits(['vote']);

// State
const showVoteComment = ref(false);
const voteType = ref(null);
const voteComment = ref('');
const commentImageUrl = ref('');
const commentsCurrentPage = ref(1);
const commentsPerPage = ref(5);

// Computed properties
const hasVoted = computed(() => {
  return props.userVotedNews.includes(props.news?.id);
});

const totalCommentPages = computed(() => {
  return Math.ceil((props.news?.comments?.length || 0) / commentsPerPage.value);
});

const paginatedComments = computed(() => {
  if (!props.news?.comments) return [];
  const startIndex = (commentsCurrentPage.value - 1) * commentsPerPage.value;
  return props.news.comments.slice(startIndex, startIndex + commentsPerPage.value);
});

// Watch for news changes to reset comment pagination
watch(() => props.news?.id, () => {
  commentsCurrentPage.value = 1;
});

// Methods
const formatDate = (dateString) => {
  return new Date(dateString).toLocaleString();
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

const getStatusText = (status) => {
  if (status === 'fake') {
    return '假新闻';
  } else if (status === 'not-fake') {
    return '真实新闻';
  } else {
    return '待验证';
  }
};

const vote = (type) => {
  voteType.value = type;
  showVoteComment.value = true;
};

const submitVote = () => {
  if (voteType.value) {
    emit('vote', {
      newsId: props.news.id,
      voteType: voteType.value,
      comment: voteComment.value,
      imageUrl: commentImageUrl.value
    });
    showVoteComment.value = false;
    voteComment.value = '';
    commentImageUrl.value = '';
  }
};

const goToCommentPage = (page) => {
  if (page >= 1 && page <= totalCommentPages.value) {
    commentsCurrentPage.value = page;
  }
};
</script>