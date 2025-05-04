<template>
    <div class="p-6">
      <h2 class="text-xl font-bold mb-4">Leave Reports</h2>
      <p>Click below to export the latest leave report in CSV format.</p>
      <button @click="download" class="bg-green-600 text-white mt-4 px-4 py-2 rounded hover:bg-green-700">
        Download CSV
      </button>
    </div>
  </template>
  
  <script>
  import { api } from '@/services/api'
  
  export default {
    methods: {
      async download() {
        try {
          const response = await api.get('/leaves/admin/reports/export', {
            responseType: 'blob',
          })
  
          const url = window.URL.createObjectURL(new Blob([response.data]))
          const link = document.createElement('a')
          link.href = url
          link.setAttribute('download', 'leave-report.csv')
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
        } catch (err) {
          console.error('Failed to export CSV:', err)
        }
      },
    }
  }
  </script>
  