<template>
  <main class="container mx-auto px-4 py-8 max-w-7xl">
    <div class="bg-white rounded-2xl shadow-lg overflow-hidden">
      <AdminPanel 
        :users="users" 
        :news-list="newsList"
        @add-news="handleAddNews"
        @edit-news="handleEditNews"
        @delete-news="handleDeleteNews"
      />
    </div>
  </main>
</template>

<script setup>
import AdminPanel from '../components/AdminPanel.vue';

// Props
const props = defineProps({
  users: Array,
  newsList: Array
});

// Emits
const emit = defineEmits(['add-news', 'edit-news', 'delete-news']);

// Methods
const handleAddNews = () => {
  emit('add-news');
};

const handleEditNews = (news) => {
  emit('edit-news', news);
};

const handleDeleteNews = (newsId) => {
  emit('delete-news', newsId);
};
</script>