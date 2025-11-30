<template>
  <main class="container mx-auto px-4 py-8 max-w-4xl">
    <div class="bg-white rounded-2xl shadow-lg overflow-hidden">
      <SubmitNews @submit-news="handleSubmitNews" />
    </div>
  </main>
</template>

<script setup>
import SubmitNews from '../components/SubmitNews.vue';

// Emits
const emit = defineEmits(['submit-news']);

// Methods
const handleSubmitNews = (newsData) => {
  emit('submit-news', newsData);
};
</script>