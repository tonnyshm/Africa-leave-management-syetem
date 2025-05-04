<template>
  <nav class="bg-blue-600 text-white px-6 py-4 flex justify-between items-center">
    <div class="text-2xl font-bold">Leave Management System</div>
    <div class="flex items-center gap-4">
      <img
        :src="profilePicture"
        alt="User Avatar"
        class="h-10 w-10 rounded-full object-cover"
      />
      <button
        @click="logout"
        class="bg-red-500 hover:bg-red-600 px-4 py-2 rounded"
      >
        Logout
      </button>
    </div>
  </nav>
</template>

<script>
export default {
  setup() {
    const profilePicture = localStorage.getItem('profilePicture') || '/default-avatar.png'; // fallback if no picture
    function logout() {
      localStorage.clear();
      window.location.href = '/';
    }
    return { profilePicture, logout }
  }
}
</script>
