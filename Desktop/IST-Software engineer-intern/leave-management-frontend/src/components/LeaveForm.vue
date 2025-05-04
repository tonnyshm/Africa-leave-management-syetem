<script setup>
import { ref, onMounted } from 'vue'
import { api } from '@/services/api'
import { useToast } from 'vue-toastification'

const toast = useToast()

const leaveTypeId = ref(null)
const leaveTypes = ref([])

const startDate = ref('')
const endDate = ref('')
const reason = ref('')
const department = ref('Engineering')
const file = ref(null)

// Fetch available leave policies from backend
onMounted(async () => {
  try {
    const res = await api.get('/leaves/types/list')
    leaveTypes.value = res.data
    if (res.data.length > 0) {
      leaveTypeId.value = res.data[0].id // Set default selected
    }
  } catch (err) {
    console.error('Failed to fetch leave policies(Types):', err)
    toast.error('Could not load leave types')
  }
})

function handleFile(e) {
  file.value = e.target.files[0]
}

async function submitLeave() {
  try {
    const leaveRequest = {
      leaveTypeId: leaveTypeId.value,
      startDate: startDate.value,
      endDate: endDate.value,
      reason: reason.value,
      department: department.value
    }

    const formData = new FormData()
    formData.append('leaveRequest', new Blob([JSON.stringify(leaveRequest)], {
      type: 'application/json'
    }))
    if (file.value) {
      formData.append('file', file.value)
    }

    await api.post('/leaves/apply', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })

    toast.success('Leave request submitted successfully!')

    // Reset form
    startDate.value = ''
    endDate.value = ''
    reason.value = ''
    file.value = null
    department.value = 'Engineering'
    leaveTypeId.value = leaveTypes.value.length > 0 ? leaveTypes.value[0].id : null

  } catch (error) {
    console.error('Submit error:', error)
    toast.error('Failed to submit leave request.')
  }
}
</script>

<template>
  <div class="max-w-2xl mx-auto bg-white p-8 shadow-md rounded mt-6">
    <h2 class="text-2xl font-bold mb-4">Apply for Leave</h2>

    <form @submit.prevent="submitLeave">
      <div class="mb-4">
        <label class="block text-gray-700 mb-2">Leave Type</label>
        <select v-model="leaveTypeId" class="w-full p-2 border rounded" required>
          <option v-for="type in leaveTypes" :value="type.id" :key="type.id">
           {{ type.type }}
          </option>
        </select>
      </div>

      <div class="mb-4">
        <label class="block text-gray-700 mb-2">Department</label>
        <select v-model="department" class="w-full p-2 border rounded">
          <option value="Engineering">Engineering</option>
          <option value="HR">HR</option>
          <option value="Finance">Finance</option>
          <option value="Marketing">Marketing</option>
          <option value="Other">Other</option>
        </select>
      </div>

      <div class="mb-4">
        <label class="block text-gray-700 mb-2">Start Date</label>
        <input type="date" v-model="startDate" class="w-full p-2 border rounded" required />
      </div>

      <div class="mb-4">
        <label class="block text-gray-700 mb-2">End Date</label>
        <input type="date" v-model="endDate" class="w-full p-2 border rounded" required />
      </div>

      <div class="mb-4">
        <label class="block text-gray-700 mb-2">Reason (optional)</label>
        <textarea v-model="reason" class="w-full p-2 border rounded"></textarea>
      </div>

      <div class="mb-4">
        <label class="block text-gray-700 mb-2">Upload Supporting Document (optional)</label>
        <input type="file" @change="handleFile" class="w-full" />
      </div>

      <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
        Submit
      </button>
    </form>
  </div>
</template>
