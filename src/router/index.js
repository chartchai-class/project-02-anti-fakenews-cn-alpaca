import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import NewsDetailView from '../views/NewsDetailView.vue'
import SubmitNewsView from '../views/SubmitNewsView.vue'
import AdminPanelView from '../views/AdminPanelView.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import AdminLoginView from '../views/AdminLoginView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/news/:id',
      name: 'news-detail',
      component: NewsDetailView
    },
    {
      path: '/submit',
      name: 'submit-news',
      component: SubmitNewsView
    },
    {
      path: '/admin',
      name: 'admin-panel',
      component: AdminPanelView,
      meta: { requiresAdmin: true }
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView
    },
    {
      path: '/admin-login',
      name: 'admin-login',
      component: AdminLoginView
    }
  ]
})

// 路由守卫，检查管理员权限
router.beforeEach((to, from, next) => {
  const isAdmin = localStorage.getItem('isAdmin') === 'true';
  if (to.matched.some(record => record.meta.requiresAdmin)) {
    if (isAdmin) {
      next();
    } else {
      next('/');
    }
  } else {
    next();
  }
});

export default router