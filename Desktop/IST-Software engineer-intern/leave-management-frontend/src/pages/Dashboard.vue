<template>
  <Navbar />
  <div class="flex">
    <Sidebar />
    <div class="flex-1 p-6 space-y-6">
      <!-- Welcome Header -->
      <div class="flex items-center justify-between bg-white shadow p-4 rounded">
        <div>
          <h2 class="text-2xl font-bold">Welcome, {{ user.fullName }}</h2>
          <p class="text-gray-600 capitalize">Role: {{ user.role.toLowerCase() }}</p>
        </div>
        <img
          :src="user.profilePictureUrl || '/default-avatar.png'"
          class="h-16 w-16 rounded-full object-cover border"
          alt="Profile"
        />
      </div>

      <!-- Leave Balance (only for staff) -->
      <div v-if="user.role === 'STAFF'" class="bg-blue-50 p-4 rounded border border-blue-200 shadow">
        <p class="text-lg font-semibold">
          Your current leave balance:
          <span v-if="leaveBalance !== null" class="text-blue-600">{{ leaveBalance }} days</span>
          <span v-else class="text-red-600">Unavailable — please contact admin to resolve.</span>
        </p>
      </div>

      <!-- Role-based Content -->
      <component :is="activeComponent" />
    </div>
  </div>
</template>

<script>
import Navbar from '@/components/Navbar.vue';
import Sidebar from '@/components/Sidebar.vue';
import StaffView from '@/components/StaffView.vue';
import ManagerPanel from '@/components/ManagerPanel.vue';
import AdminPanel from '@/components/AdminPanel.vue';
import { api, authApi } from '@/services/api';

export default {
  components: {
    Navbar,
    Sidebar,
    StaffView,
    ManagerPanel,
    AdminPanel,
  },
  data() {
    return {
      user: {
        fullName: '',
        profilePictureUrl: '',
        email: '',
        role: '',
      },
      leaveBalance: null,
    };
  },
  computed: {
    activeComponent() {
      switch (this.user.role) {
        case 'ADMIN':
          return 'AdminPanel';
        case 'MANAGER':
          return 'ManagerPanel';
        default:
          return 'StaffView';
      }
    },
  },
  async created() {
    try {
      const token = localStorage.getItem('id_token');
      const storedUser = JSON.parse(localStorage.getItem('user'));

      if (!token || !storedUser?.email) {
        throw new Error('User not authenticated or email missing');
      }

      authApi.defaults.headers.common['Authorization'] = `Bearer ${token}`;

      const roleRes = await authApi.get(`/users/${storedUser.email}/role`);
      const role = roleRes.data;

      this.user = {
        ...storedUser,
        role,
      };

      if (this.user.role === 'STAFF') {
        try {
          const balanceRes = await api.get('/leaves/balance');
          this.leaveBalance = balanceRes.data?.days ?? null;
        } catch (balanceErr) {
          console.warn('Balance fetch failed:', balanceErr);
          this.leaveBalance = null;
        }
      }
    } catch (err) {
      console.error('Dashboard initialization failed:', err);
      this.$router.push('/');
    }
  },
};
</script>
