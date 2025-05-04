<template>
    <div class="space-y-6">
      <LeaveForm />
      <LeaveHistory />
      <TeamCalendar />
    </div>
  </template>
  
  <script>
  import LeaveForm from '@/components/LeaveForm.vue'
  import LeaveHistory from '@/components/LeaveHistory.vue'
  import TeamCalendar from '@/components/TeamCalendar.vue'
  
  export default {
    components: { LeaveForm, LeaveHistory, TeamCalendar }
  }
  </script>
  