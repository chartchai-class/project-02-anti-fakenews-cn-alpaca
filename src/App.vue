<script setup>
import { ref, onMounted, provide } from 'vue';
import { useRouter } from 'vue-router';
import router from './router';
import Navbar from './components/Navbar.vue';
import AboutModal from './components/AboutModal.vue';
import { authApi, newsApi, userApi } from './utils/api.js';

// 全局状态
const news = ref([]);
const users = ref([]);
const members = ref([]);
const currentUser = ref(null);
const isAdmin = ref(false);
const userVotedNews = ref([]);

// 模态框状态
const showAboutModal = ref(false);

// 详细新闻内容模板 - 用于生成更真实的模拟数据
const newsContentTemplates = [
  {
    title: "科学家成功研发新型抗癌药物，临床试验治愈率达90%",
    content: "近日，由国家生物医学研究所牵头的研究团队宣布，他们成功研发出一种新型抗癌药物，在二期临床试验中对晚期肺癌患者的治愈率达到了90%。该药物通过靶向癌细胞特定蛋白，能够精准消灭癌细胞而不损伤健康细胞。\n\n研究负责人李教授表示，这种药物的研发历时8年，采用了最新的基因编辑技术和纳米载体递送系统。临床试验显示，接受治疗的120名患者中，108名在3个月内癌细胞完全消失，且未出现明显副作用。\n\n该成果已发表在《自然医学》杂志上，专家预计该药物将在2024年底获得上市批准，届时将为癌症患者带来新的希望。",
    imageUrl: "https://picsum.photos/seed/medical/800/400"
  },
  {
    title: "政府将投入500亿建设乡村振兴基础设施",
    content: "国务院今日召开新闻发布会，宣布将在未来三年内投入500亿元专项资金，用于农村基础设施建设，助力乡村振兴战略实施。\n\n据介绍，这笔资金将主要用于农村道路硬化、水利设施改造、清洁能源推广和网络基础设施升级等方面。预计将惠及全国832个贫困县和12万个行政村。\n\n农业农村部部长表示，此次投资将重点解决农村地区'出行难、灌溉难、用电难、上网难'等问题，为农村产业发展和农民生活改善提供基础保障。同时，项目将优先雇佣当地农民，预计可创造超过200万个就业岗位。",
    imageUrl: "https://picsum.photos/seed/rural/800/400"
  },
  {
    title: "研究发现：每天饮用咖啡可降低心脏病风险",
    content: "一项发表在《美国医学协会杂志》上的最新研究表明，每天饮用2-3杯咖啡的人，患心脏病的风险比不喝咖啡的人低21%。\n\n该研究由哈佛大学公共卫生学院主导，跟踪调查了超过10万名参与者长达15年。研究人员控制了年龄、吸烟、饮食习惯等变量后发现，适度饮用咖啡与心血管健康存在显著正相关。\n\n研究负责人解释，咖啡中含有多种抗氧化物质和多酚，可能有助于改善血管功能和降低炎症反应。但专家也提醒，过量饮用（每天超过5杯）可能会带来负面影响，且应避免添加过多糖和奶油。",
    imageUrl: "https://picsum.photos/seed/coffee/800/400"
  }
];

// 生成更真实的模拟数据
function generateMockData() {
  const statuses = ['pending', 'fake', 'not-fake'];
  const categories = ['政治', '经济', '科技', '娱乐', '体育', '健康', '教育', '环境', '国际', '文化'];
  const reporters = [
    '张三', '李四', '王五', '赵六', '钱七', '孙八', '周九', '吴十',
    '郑一', '王二', '刘芳', '陈明', '杨丽', '张强', '赵敏'
  ];
  
  const mockNews = [];
  
  // 先添加我们的详细新闻模板
  newsContentTemplates.forEach((template, index) => {
    const status = index % 3 === 0 ? 'fake' : index % 4 === 0 ? 'not-fake' : 'pending';
    const fakeVotes = status === 'fake' ? Math.floor(Math.random() * 50) + 20 : Math.floor(Math.random() * 30);
    const notFakeVotes = status === 'not-fake' ? Math.floor(Math.random() * 50) + 20 : Math.floor(Math.random() * 30);
    
    // 生成评论
    const comments = generateComments(index + 1);
    
    mockNews.push({
      id: index + 1,
      title: template.title,
      content: template.content,
      reporter: reporters[Math.floor(Math.random() * reporters.length)],
      imageUrl: template.imageUrl,
      status: status,
      fakeVotes: fakeVotes,
      notFakeVotes: notFakeVotes,
      createdAt: new Date(Date.now() - Math.floor(Math.random() * 30) * 24 * 60 * 60 * 1000).toISOString(),
      comments: comments
    });
  });
  
  // 再添加一些额外的新闻，确保总数达到25条
  for (let i = newsContentTemplates.length + 1; i <= 25; i++) {
    const category = categories[Math.floor(Math.random() * categories.length)];
    const status = i % 5 === 0 ? 'fake' : i % 7 === 0 ? 'not-fake' : 'pending';
    const fakeVotes = status === 'fake' ? Math.floor(Math.random() * 50) + 20 : Math.floor(Math.random() * 30);
    const notFakeVotes = status === 'not-fake' ? Math.floor(Math.random() * 50) + 20 : Math.floor(Math.random() * 30);
    
    // 生成评论
    const comments = generateComments(i);
    
    // 生成更丰富的内容
    let content = `这是一篇关于${category}领域的最新报道。`;
    
    if (category === '政治') {
      content += `近日，${['市议会', '省政府', '全国人大'][Math.floor(Math.random() * 3)]}召开了关于${['公共卫生', '教育改革', '环境保护'][Math.floor(Math.random() * 3)]}的专题会议，提出了一系列新的政策措施。据了解，这些政策将在${['一个月内', '本季度末', '明年初'][Math.floor(Math.random() * 3)]}正式实施，预计将对${['市民生活', '经济发展', '社会治理'][Math.floor(Math.random() * 3)]}产生重大影响。`;
    } else if (category === '经济') {
      content += `最新数据显示，${['本地', '全国', '全球'][Math.floor(Math.random() * 3)]}${['房地产市场', '股票市场', '就业市场'][Math.floor(Math.random() * 3)]}出现了${['回暖', '下滑', '稳定'][Math.floor(Math.random() * 3)]}趋势。专家分析认为，这一变化主要受到${['政策调整', '国际形势', '市场供需'][Math.floor(Math.random() * 3)]}的影响，未来${['三个月', '半年', '一年'][Math.floor(Math.random() * 3)]}可能会${['持续改善', '面临挑战', '保持稳定'][Math.floor(Math.random() * 3)]}。`;
    } else if (category === '科技') {
      content += `${['国内', '国际', '知名'][Math.floor(Math.random() * 3)]}科技公司宣布研发成功${['新型芯片', '人工智能系统', '新能源技术'][Math.floor(Math.random() * 3)]}，该技术${['在性能上', '在效率上', '在成本上'][Math.floor(Math.random() * 3)]}较现有技术有${['显著提升', '重大突破', '明显优势'][Math.floor(Math.random() * 3)]}。业内人士预计，这项技术将${['改变行业格局', '推动相关产业发展', '带来新的投资机会'][Math.floor(Math.random() * 3)]}。`;
    } else {
      content += `${['最近', '近日', '本月'][Math.floor(Math.random() * 3)]}，${category}领域发生了${['一件重大事件', '几项重要变化', '一系列新动态'][Math.floor(Math.random() * 3)]}，引起了${['公众', '业内人士', '专家学者'][Math.floor(Math.random() * 3)]}的广泛关注。相关方面已经${['做出回应', '采取措施', '发表声明'][Math.floor(Math.random() * 3)]}，表示将${['密切关注', '积极应对', '深入研究'][Math.floor(Math.random() * 3)]}事态发展。`;
    }
    
    content += `\n\n${['有关部门', '业内专家', '相关人士'][Math.floor(Math.random() * 3)]}表示，${['将持续关注事件进展', '会及时发布更多信息', '呼吁公众保持理性'][Math.floor(Math.random() * 3)]}。${i % 3 === 0 ? '本报将继续跟踪报道。' : ''}`;
    
    mockNews.push({
      id: i,
      title: `${category}：${i} - ${['最新动态', '重大突破', '重要通知', '深度分析'][Math.floor(Math.random() * 4)]}`,
      content: content,
      reporter: reporters[Math.floor(Math.random() * reporters.length)],
      imageUrl: `https://picsum.photos/seed/${category}${i}/800/400`,
      status: status,
      fakeVotes: fakeVotes,
      notFakeVotes: notFakeVotes,
      createdAt: new Date(Date.now() - Math.floor(Math.random() * 30) * 24 * 60 * 60 * 1000).toISOString(),
      comments: comments
    });
  }
  
  return mockNews;
}

// 生成更真实的评论
function generateComments(newsId) {
  const comments = [];
  const commentCount = Math.floor(Math.random() * 15) + 1;
  
  // 评论模板
  const fakeReasons = [
    "这个新闻我查证过，来源不可靠，明显是编造的",
    "内容与事实不符，官方已经辟谣了",
    "数据有问题，我查了相关统计，根本不是这个数字",
    "存在明显逻辑漏洞，稍微分析一下就知道不合理",
    "这是断章取义，完整的情况完全不是这样",
    "图片是经过修改的，原图根本不是这个场景",
    "时间线不对，事件发生的时间顺序有问题",
    "引用的专家根本不存在，或者从未说过这样的话"
  ];
  
  const notFakeReasons = [
    "有多个可靠来源证实了这个消息",
    "内容符合事实，我有朋友就在现场",
    "数据准确，与官方发布的统计一致",
    "逻辑清晰，事件发展合理",
    "已经被多家权威媒体报道，可信度高",
    "图片真实，我在其他地方也看到过",
    "时间线正确，与已知事件发展相符",
    "引用的专家确实发表过相关观点"
  ];
  
  for (let j = 0; j < commentCount; j++) {
    const isFake = Math.random() > 0.5;
    const reasonIndex = Math.floor(Math.random() * (isFake ? fakeReasons.length : notFakeReasons.length));
    
    // 有时添加额外评论内容
    let extraComment = "";
    if (Math.random() > 0.6) {
      extraComment = "\n\n" + (isFake ? 
        "希望大家不要轻信这类消息，多查证再传播。" : 
        "这个消息很重要，值得更多人关注。");
    }
    
    comments.push({
      id: `comment-${newsId}-${j}`,
      newsId: newsId,
      vote: isFake ? 'fake' : 'not-fake',
      comment: (isFake ? fakeReasons[reasonIndex] : notFakeReasons[reasonIndex]) + extraComment,
      imageUrl: Math.random() > 0.7 ? `https://picsum.photos/seed/comment${newsId}${j}/400/300` : null,
      createdAt: new Date(Date.now() - Math.floor(Math.random() * 30) * 24 * 60 * 60 * 1000).toISOString()
    });
  }
  
  return comments;
}

// 生成模拟用户数据
function generateMockUsers() {
  const firstNames = ['张', '李', '王', '赵', '陈', '刘', '杨', '黄', '周', '吴'];
  const lastNames = ['明', '华', '强', '伟', '芳', '娜', '杰', '磊', '军', '勇'];
  const domains = ['gmail.com', 'yahoo.com', 'hotmail.com', 'outlook.com', 'qq.com'];
  
  const users = [];
  
  // 添加管理员用户
  users.push({
    id: 1,
    username: 'admin',
    password: 'admin123', // 在实际应用中，密码应该加密存储
    firstName: '管理',
    lastName: '员',
    email: 'admin@system.com',
    profileImage: null,
    role: 'admin',
    isMember: true,
    createdAt: new Date().toISOString()
  });
  
  // 添加普通用户
  for (let i = 2; i <= 20; i++) {
    const firstName = firstNames[Math.floor(Math.random() * firstNames.length)];
    const lastName = lastNames[Math.floor(Math.random() * lastNames.length)];
    const username = `${firstName}${lastName}${i}`;
    const email = `${username}@${domains[Math.floor(Math.random() * domains.length)]}`;
    
    users.push({
      id: i,
      username: username,
      password: 'user123', // 在实际应用中，密码应该加密存储
      firstName: firstName,
      lastName: lastName,
      email: email,
      profileImage: Math.random() > 0.7 ? `https://picsum.photos/seed/user${i}/100/100` : null,
      role: 'user',
      isMember: Math.random() > 0.7, // 30%的用户是成员
      createdAt: new Date(Date.now() - Math.floor(Math.random() * 365) * 24 * 60 * 60 * 1000).toISOString()
    });
  }
  
  return users;
}

// 保存数据到localStorage
function saveData() {
  localStorage.setItem('newsData', JSON.stringify(news.value));
  localStorage.setItem('userData', JSON.stringify(users.value));
  localStorage.setItem('memberData', JSON.stringify(members.value));
  localStorage.setItem('userVotedNews', JSON.stringify(userVotedNews.value));
  if (currentUser.value) {
    localStorage.setItem('currentUser', JSON.stringify(currentUser.value));
    localStorage.setItem('isAdmin', JSON.stringify(isAdmin.value));
  }
}

// 显示成功提示
function showToast(message) {
  // 这里可以实现一个toast组件，暂时用alert代替
  alert(message);
}



// 退出登录
function logout() {
  currentUser.value = null;
  isAdmin.value = false;
  localStorage.removeItem('currentUser');
  localStorage.removeItem('isAdmin');
  localStorage.removeItem('token');
  showToast('已退出登录');
}

// 提交新闻
async function submitNews(newsData) {
  try {
    const response = await newsApi.submitNews(newsData);
    if (response) {
      news.value.push(response);
      saveData();
      showToast('新闻提交成功');
      return true;
    }
    showToast('新闻提交失败');
    return false;
  } catch (error) {
    console.error('新闻提交失败:', error);
    showToast('新闻提交失败，请重试');
    return false;
  }
}

// 投票
async function handleVote(voteData) {
  try {
    const response = await newsApi.voteNews(voteData.newsId, voteData);
    if (response) {
      // 更新本地新闻数据
      const newsItem = news.value.find(n => n.id === voteData.newsId);
      if (newsItem) {
        newsItem.fakeVotes = response.fakeVotes;
        newsItem.notFakeVotes = response.notFakeVotes;
        newsItem.comments.push(response.comment);
      }
      
      // 记录用户已投票
      if (!userVotedNews.value.includes(voteData.newsId)) {
        userVotedNews.value.push(voteData.newsId);
      }
      
      saveData();
      showToast('投票成功');
      return true;
    }
    showToast('投票失败');
    return false;
  } catch (error) {
    console.error('投票失败:', error);
    showToast('投票失败，请重试');
    return false;
  }
}

// 初始化应用
async function initApp() {
  try {
    // 从API获取新闻数据
    const newsData = await newsApi.getNews();
    news.value = newsData;
    
    // 从API获取用户数据
    const usersData = await userApi.getUsers();
    users.value = usersData;
    
    // 加载成员列表
    members.value = users.value.filter(user => user.isMember).map(user => user.id);
    
    // 加载用户已投票记录
    const savedVotes = localStorage.getItem('userVotedNews');
    if (savedVotes) {
      userVotedNews.value = JSON.parse(savedVotes);
    }
    
    // 加载当前用户信息
    const savedCurrentUser = localStorage.getItem('currentUser');
    if (savedCurrentUser) {
      currentUser.value = JSON.parse(savedCurrentUser);
      isAdmin.value = currentUser.value.role === 'admin';
    }
  } catch (error) {
    console.error('初始化应用失败:', error);
    // 如果API调用失败，使用模拟数据作为 fallback
    news.value = generateMockData();
    users.value = generateMockUsers();
    members.value = users.value.filter(user => user.isMember).map(user => user.id);
    const savedVotes = localStorage.getItem('userVotedNews');
    if (savedVotes) {
      userVotedNews.value = JSON.parse(savedVotes);
    }
    const savedCurrentUser = localStorage.getItem('currentUser');
    if (savedCurrentUser) {
      currentUser.value = JSON.parse(savedCurrentUser);
      isAdmin.value = currentUser.value.role === 'admin';
    }
  }
}

// 生命周期钩子
onMounted(() => {
  initApp();
});
</script>

<template>
  <div id="app" class="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100">
    <!-- 导航栏 -->
    <Navbar 
      :current-user="currentUser"
      :is-admin="isAdmin"
      @show-about="showAboutModal = true"
      @logout="logout"
    />
    
    <!-- 路由视图 -->
    <main class="container mx-auto px-4 py-8 max-w-7xl">
      <router-view 
        :news="news"
        :news-list="news"
        :users="users"
        :user-voted-news="userVotedNews"
        @view-detail="(id) => router.push(`/news/${id}`)"
        @vote="handleVote"
        @submit-news="submitNews"
      />
    </main>
    
    <!-- 页脚 -->
    <footer class="bg-gradient-to-r from-dark to-dark/95 text-white py-12 mt-16 shadow-inner">
      <div class="container mx-auto px-4 max-w-7xl">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
          <div class="flex flex-col items-center md:items-start">
            <div class="flex items-center space-x-2 mb-4">
              <i class="fa fa-newspaper-o text-secondary text-2xl"></i>
              <h2 class="text-2xl font-bold bg-gradient-to-r from-white to-gray-300 bg-clip-text text-transparent">真相卫士</h2>
            </div>
            <p class="text-gray-300 text-center md:text-left">共同维护真实、健康的信息环境，让真相不再被埋没</p>
          </div>
          <div class="flex flex-col items-center md:items-center">
            <h3 class="text-lg font-semibold mb-4">快速链接</h3>
            <div class="flex flex-col space-y-2 text-gray-300">
              <router-link to="/" class="hover:text-white transition-colors">首页</router-link>
              <router-link to="/submit" class="hover:text-white transition-colors">提交新闻</router-link>
              <button @click="showAboutModal = true" class="hover:text-white transition-colors text-left">关于我们</button>
            </div>
          </div>
          <div class="flex flex-col items-center md:items-end">
            <h3 class="text-lg font-semibold mb-4">联系我们</h3>
            <p class="text-gray-300 text-center md:text-right">
              <i class="fa fa-envelope mr-1"></i> contact@truthguardian.com<br>
              <i class="fa fa-phone mr-1"></i> +86 123 4567 8910
            </p>
          </div>
        </div>
        <div class="border-t border-gray-700 mt-8 pt-8 text-center text-gray-400">
          <p>© 2023 真相卫士 - 社会反假新闻系统</p>
          <p class="mt-1">使用 Vue 3 + Spring Boot 构建</p>
        </div>
      </div>
    </footer>
    
    <!-- 模态框 -->
    <AboutModal 
      :is-open="showAboutModal"
      @close="showAboutModal = false"
    />
  </div>
</template>


