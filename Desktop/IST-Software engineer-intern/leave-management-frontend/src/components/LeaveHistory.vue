<template>
  <div class="bg-white p-6 rounded shadow-md mt-6">
    <h2 class="text-xl font-bold mb-4">Leave History</h2>
    <table class="w-full text-left">
      <thead>
        <tr class="border-b">
          <th class="p-2">Type</th>
          <th class="p-2">Dates</th>
          <th class="p-2">Status</th>
          <th class="p-2" v-if="hasRejectedLeaves">Reason</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="leave in history" :key="leave.id">
          <td class="p-2">{{ leave.leaveType.type }}</td>
          <td class="p-2">{{ leave.startDate }} to {{ leave.endDate }}</td>
          <td class="p-2">
            <span :class="statusClass(leave.status)">
              {{ leave.status }}
            </span>
          </td>
          <td class="p-2" v-if="leave.status === 'REJECTED'">
            {{ leave.reason || 'No reason provided' }}
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import { api } from '@/services/api'

export default {
  data() {
    return {
      history: [],
    }
  },
  computed: {
    hasRejectedLeaves() {
      return this.history.some(leave => leave.status === 'REJECTED')
    }
  },
  async mounted() {
    try {
      const res = await api.get('/leaves/my-history')
      this.history = res.data
    } catch (err) {
      console.error('Failed to load leave history', err)
    }
  },
  methods: {
    statusClass(status) {
      switch (status) {
        case 'APPROVED': return 'text-green-600 font-bold'
        case 'REJECTED': return 'text-red-600 font-bold'
        default: return 'text-gray-600'
      }
    }
  }
}
</script>
