<template>
  <main class="container mx-auto px-4 py-8 max-w-7xl">
    <div class="bg-white rounded-2xl shadow-lg overflow-hidden">
      <NewsDetail 
        :news="news" 
        :user-voted-news="userVotedNews"
        @vote="handleVote"
      />
    </div>
  </main>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import { useRoute } from 'vue-router';
import NewsDetail from '../components/NewsDetail.vue';

// Props
const props = defineProps({
  newsList: Array,
  userVotedNews: Array
});

// Emits
const emit = defineEmits(['vote']);

// Route
const route = useRoute();

// Computed properties
const news = computed(() => {
  return props.newsList.find(n => n.id === parseInt(route.params.id));
});

// Methods
const handleVote = (voteData) => {
  emit('vote', voteData);
};
</script>