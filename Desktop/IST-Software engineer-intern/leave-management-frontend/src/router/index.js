import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/pages/Login.vue'
import Dashboard from '@/pages/Dashboard.vue'
import Reports from '@/pages/Reports.vue'
import TeamCalendarPage from '@/pages/TeamCalendarPage.vue'
import NotFound from '@/pages/NotFound.vue'
import AdminPanel from '../components/AdminPanel.vue'
import ManagerPanel from '../components/ManagerPanel.vue'
import PublicHolidays from '@/pages/PublicHolidays.vue'

const routes = [
  { path: '/', component: Login },
  { path: '/dashboard', component: Dashboard },
  { path: '/reports', component: Reports },
  { path: '/admin_panel', component: AdminPanel },
  { path: '/manager_panel', component: ManagerPanel },
  { path: '/calendar', component: TeamCalendarPage },
  { path: '/holidays', component: PublicHolidays },
  { path: '/:pathMatch(.*)*', name: 'NotFound', component: NotFound },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
