<template>
  <div class="bg-white p-6 rounded shadow-md">
    <h2 class="text-xl font-bold mb-4">Team Members Currently on Leave</h2>

    <div class="mb-4">
      <label class="block text-sm font-medium text-gray-700 mb-1">Filter by Department</label>
      <select v-model="selectedDepartment" @change="fetchTeamOnLeave" class="p-2 border rounded w-full">
        <option value="">All Departments</option>
        <option v-for="dept in departments" :key="dept" :value="dept">{{ dept }}</option>
      </select>
    </div>

    <div v-if="loading">
      <LoadingSpinner />
    </div>

    <div v-else-if="team.length === 0" class="text-gray-500 italic">
      No one is on leave currently.
    </div>

    <div v-else class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
      <div
        v-for="member in team"
        :key="member.email"
        class="bg-gray-100 p-4 rounded shadow flex flex-col items-center text-center"
      >
        <img
          :src="member.profilePictureUrl || '/default-avatar.png'"
          class="w-16 h-16 rounded-full object-cover mb-2"
          alt="avatar"
        />
        <p class="font-semibold">{{ member.fullName }}</p>
        <p class="text-sm text-gray-600">Leave until: {{ formatDate(member.endDate) }}</p>
        <p class="text-xs text-gray-500 mt-1">{{ member.leaveType }}</p>
        <p class="text-xs text-gray-500">{{ member.department }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import { api } from '@/services/api'
import LoadingSpinner from '@/components/LoadingSpinner.vue'

export default {
  components: { LoadingSpinner },
  data() {
    return {
      team: [],
      loading: false,
      departments: ['Engineering', 'HR', 'Finance', 'Marketing', 'General'],
      selectedDepartment: ''
    }
  },
  methods: {
    async fetchTeamOnLeave() {
      this.loading = true;
      try {
        const res = await api.get('/leaves/team-on-leave', {
          params: {
            department: this.selectedDepartment
          }
        });
        this.team = res.data;
      } catch (err) {
        console.error('Failed to fetch team leave data', err);
      } finally {
        this.loading = false;
      }
    },
    formatDate(dateStr) {
      const date = new Date(dateStr);
      return date.toLocaleDateString(undefined, {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
      });
    }
  },
  mounted() {
    this.fetchTeamOnLeave();
  }
}
</script>
