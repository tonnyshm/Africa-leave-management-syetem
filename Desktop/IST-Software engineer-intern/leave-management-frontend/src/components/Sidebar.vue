<template>
  <div class="w-64 h-full bg-gray-800 text-white flex flex-col py-6 space-y-6">
    <router-link to="/dashboard" class="px-6 py-2 hover:bg-gray-700">Dashboard</router-link>
    <router-link to="/calendar" class="px-6 py-2 hover:bg-gray-700">Team Calendar</router-link>
    <router-link to="/reports" class="px-6 py-2 hover:bg-gray-700">Reports</router-link>
    <router-link to="/holidays" class="block px-4 py-2 hover:bg-blue-100">📅 Public Holidays</router-link>
  </div>
</template>

<script>
export default {}
</script>
