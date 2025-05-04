<template>
    <Navbar />
    <div class="flex">
      <Sidebar />
      <div class="flex-1 p-6">
        <TeamCalendar />
      </div>
    </div>
  </template>
  
  <script>
  import Navbar from '@/components/Navbar.vue'
  import Sidebar from '@/components/Sidebar.vue'
  import TeamCalendar from '@/components/TeamCalendar.vue'
  
  export default {
    components: {
      Navbar,
      Sidebar,
      TeamCalendar,
    },
  }
  </script>
  