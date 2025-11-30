// API工具类，用于与后端通信

// API基础URL
const API_BASE_URL = 'http://localhost:8080/api';

// 请求头配置
const getHeaders = () => {
  const token = localStorage.getItem('token');
  return {
    'Content-Type': 'application/json',
    ...(token && { 'Authorization': `Bearer ${token}` })
  };
};

// 通用请求方法
const request = async (url, options = {}) => {
  try {
    const response = await fetch(`${API_BASE_URL}${url}`, {
      headers: getHeaders(),
      ...options
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return await response.json();
  } catch (error) {
    console.error('API request failed:', error);
    throw error;
  }
};

// 认证相关API
export const authApi = {
  // 用户登录
  login: (credentials) => request('/auth/login', {
    method: 'POST',
    body: JSON.stringify(credentials)
  }),

  // 用户注册
  register: (userData) => request('/auth/register', {
    method: 'POST',
    body: JSON.stringify(userData)
  }),

  // 管理员登录
  adminLogin: (credentials) => request('/auth/admin/login', {
    method: 'POST',
    body: JSON.stringify(credentials)
  })
};

// 新闻相关API
export const newsApi = {
  // 获取新闻列表
  getNews: () => request('/news'),

  // 获取新闻详情
  getNewsById: (id) => request(`/news/${id}`),

  // 提交新闻
  submitNews: (newsData) => request('/news', {
    method: 'POST',
    body: JSON.stringify(newsData)
  }),

  // 投票
  voteNews: (newsId, voteData) => request(`/news/${newsId}/vote`, {
    method: 'POST',
    body: JSON.stringify(voteData)
  })
};

// 评论相关API
export const commentApi = {
  // 获取新闻评论
  getComments: (newsId) => request(`/comments/news/${newsId}`),

  // 提交评论
  submitComment: (commentData) => request('/comments', {
    method: 'POST',
    body: JSON.stringify(commentData)
  })
};

// 用户相关API
export const userApi = {
  // 获取当前用户信息
  getCurrentUser: () => request('/users/me'),

  // 获取用户列表
  getUsers: () => request('/users')
};
