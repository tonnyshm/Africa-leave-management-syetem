<template>
  <div class="space-y-6">
    <!-- Team Leave Panel -->
    <section class="bg-white p-4 rounded shadow">
      <h3 class="text-xl font-semibold mb-4">Team Leave Panel</h3>
      <table class="min-w-full table-auto border">
        <thead class="bg-gray-100">
          <tr>
            <th class="px-4 py-2">Employee</th>
            <th class="px-4 py-2">Type</th>
            <th class="px-4 py-2">Dates</th>
            <th class="px-4 py-2">Reason</th>
            <th class="px-4 py-2">Attachment</th>
            <th class="px-4 py-2">Status</th>
            <th class="px-4 py-2">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="leave in teamLeaves" :key="leave.id" class="text-sm text-center">
            <td class="px-4 py-2">{{ leave.userEmail }}</td>
            <td class="px-4 py-2">{{ leave.leaveType }}</td>
            <td class="px-4 py-2">{{ formatDate(leave.startDate) }} - {{ formatDate(leave.endDate) }}</td>
            <td class="px-4 py-2">{{ leave.reason }}</td>

            <!-- Attachment column -->
            <td class="px-4 py-2">
              <a
                v-if="leave.attachmentUrl"
                :href="leave.attachmentUrl"
                target="_blank"
                class="text-blue-600 underline"
              >
                View
              </a>
              <span v-else class="text-gray-400">None</span>
            </td>

            <td class="px-4 py-2 capitalize">{{ leave.status.toLowerCase() }}</td>
            <!-- Actions -->
            <td class="px-4 py-2 space-x-2" v-if="leave.status === 'PENDING'">
              <button @click="approve(leave.leaveType.type)" class="btn bg-green-600 text-white">Approve</button>
              <button @click="reject(leave.leaveType.type)" class="btn bg-red-600 text-white">Reject</button>
            </td>
            <td v-else class="px-4 py-2 text-gray-400">Processed</td>
          </tr>
        </tbody>
      </table>
    </section>
  </div>
</template>

<script>
import { api } from '@/services/api'
import { useToast } from 'vue-toastification'

export default {
  data() {
    return {
      teamLeaves: [],
    }
  },
  async created() {
    try {
      const res = await api.get('/leaves/team-on-leave-for-admin')
      this.teamLeaves = res.data
    } catch (err) {
      console.error('Failed to fetch team leaves:', err)
      this.$toast.error('Could not load team leave data')
    }
  },
  methods: {
    async approve(id) {
      try {
        await api.post(`/leaves/${id}/approve`)
        this.teamLeaves = this.teamLeaves.map(l => l.id === id ? { ...l, status: 'APPROVED' } : l)
        this.$toast.success('Leave approved')
      } catch (err) {
        console.error(err)
        this.$toast.error('Approval failed')
      }
    },
    async reject(id) {
      const comment = prompt('Enter rejection reason:')
      if (!comment) return

      try {
        await api.post(`/leaves/${id}/reject`, { comment })
        this.teamLeaves = this.teamLeaves.map(l => l.id === id ? { ...l, status: 'REJECTED' } : l)
        this.$toast.success('Leave rejected')
      } catch (err) {
        console.error(err)
        this.$toast.error('Rejection failed')
      }
    },
    formatDate(dateStr) {
      return new Date(dateStr).toLocaleDateString()
    }
  },
  setup() {
    const toast = useToast()
    return { $toast: toast }
  }
}
</script>

<style scoped>
.btn {
  @apply px-3 py-1 rounded hover:opacity-90;
}
</style>
