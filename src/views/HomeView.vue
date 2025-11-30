<template>
  <main class="container mx-auto px-4 py-8 max-w-7xl">
    <div class="bg-white rounded-2xl shadow-lg overflow-hidden">
      <NewsList 
        :news="news" 
        @view-detail="viewNewsDetail"
      />
    </div>
  </main>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import NewsList from '../components/NewsList.vue';

// Props
const props = defineProps({
  news: Array
});

// Emits
const emit = defineEmits(['view-detail']);

// Methods
const viewNewsDetail = (newsId) => {
  emit('view-detail', newsId);
};
</script>