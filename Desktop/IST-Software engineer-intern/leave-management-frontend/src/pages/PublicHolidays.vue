<template>
    <div class="p-6">
      <h1 class="text-2xl font-bold mb-4 text-blue-800">🇷🇼 Rwanda Public Holidays</h1>
  
      <button
        @click="fetchHolidays"
        class="mb-4 bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded"
      >
        Load Holidays
      </button>
  
      <div v-if="holidays.length" class="bg-white shadow rounded overflow-hidden">
        <table class="w-full table-auto border-collapse">
          <thead class="bg-gray-100">
            <tr>
              <th class="text-left px-4 py-2 border-b">Date</th>
              <th class="text-left px-4 py-2 border-b">Holiday Name</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(holiday, index) in holidays" :key="index" class="hover:bg-gray-50">
              <td class="px-4 py-2 border-b">{{ holiday.date }}</td>
              <td class="px-4 py-2 border-b">{{ holiday.name }}</td>
            </tr>
          </tbody>
        </table>
      </div>
  
      <p v-if="loading" class="mt-4 text-gray-500">Loading...</p>
      <p v-if="error" class="mt-4 text-red-600">⚠️ {{ error }}</p>
    </div>
  </template>
  
  <script>
  import axios from 'axios'
  
  export default {
    name: 'PublicHolidays',
    data() {
      return {
        holidays: [],
        loading: false,
        error: null
      }
    },
    methods: {
      async fetchHolidays() {
        this.loading = true
        this.error = null
        this.holidays = []
  
        try {
          const response = await axios.get(`${import.meta.env.VITE_LEAVE_API_BASE_URL}/public-holidays/rwanda`);
            const parser = new DOMParser();
            const doc = parser.parseFromString(response.data, 'text/html');

  
          const tableRows = doc.querySelectorAll('.table--left tbody tr')
          const parsed = []
  
          tableRows.forEach(row => {
            const dateEl = row.querySelector('th')
            const nameEl = row.querySelector('a')
  
            if (dateEl && nameEl) {
              const date = dateEl.textContent.trim()
              const name = nameEl.textContent.trim()
              parsed.push({ date, name })
            }
          })
  
          this.holidays = parsed
        } catch (err) {
          this.error = 'Failed to fetch holidays.'
          console.error('Error fetching holidays:', err)
        } finally {
          this.loading = false
        }
      }
    }
  }
  </script>
  
  <style scoped>
  table {
    font-family: 'Inter', sans-serif;
  }
  </style>
  