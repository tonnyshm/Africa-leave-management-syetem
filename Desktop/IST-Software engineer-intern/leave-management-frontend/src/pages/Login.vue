<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-50 to-purple-100 flex flex-col justify-between">
    <!-- Header -->
    <header class="p-6 bg-white shadow">
      <h1 class="text-3xl font-bold text-center text-gray-800">🌿 Leave Management System</h1>
      <p class="text-center text-gray-500 mt-2">Simplify your Team's Time Off, Approvals and Reports</p>
    </header>

    <!-- Main Content -->
    <main class="flex-1 flex items-center justify-center">
      <div class="bg-white shadow-lg rounded-lg p-8 max-w-3xl w-full">
        <h2 class="text-xl font-semibold mb-4 text-gray-700 text-center">Why Use This System?</h2>
        <ul class="grid grid-cols-1 md:grid-cols-2 gap-4 text-gray-600">
          <li class="flex items-start gap-2">
            <span class="text-green-500 font-bold">✔</span>
            Apply for leave quickly & attach documents
          </li>
          <li class="flex items-start gap-2">
            <span class="text-green-500 font-bold">✔</span>
            Managers/Admin approve or reject requests
          </li>
          <li class="flex items-start gap-2">
            <span class="text-green-500 font-bold">✔</span>
            Admins configure leave types & balances and policies
          </li>
          <li class="flex items-start gap-2">
            <span class="text-green-500 font-bold">✔</span>
            Track team availability in real-time
          </li>
          <li class="flex items-start gap-2">
            <span class="text-green-500 font-bold">✔</span>
            Manage leave types, accrual rates, and carryover policies
          </li>
          <li class="flex items-start gap-2">
            <span class="text-green-500 font-bold">✔</span>
            Generate reports (by employee, leave type, department, etc.) 
          </li>

          <li class="flex items-start gap-2">
            <span class="text-green-500 font-bold">✔</span>
            Emails & in app Notifications and Alerts  
          </li>

          <li class="flex items-start gap-2">
            <span class="text-green-500 font-bold">✔</span>
            Upcoming Leaves and Holiday calender 
          </li>

          <li class="flex items-start gap-2">
            <span class="text-green-500 font-bold">✔</span>
            More 🔥  
          </li>
        </ul>

        <div class="mt-8 text-center">
          <p class="mb-2 text-gray-700 font-medium">Sign in to get started:</p>
          <div id="googleSignInDiv" class="flex justify-center"></div>
        </div>
      </div>
    </main>

    <!-- Footer -->
    <footer class="text-center py-4 text-sm text-gray-500">
      &copy; {{ new Date().getFullYear() }} Leave Management System. All rights reserved.
    </footer>
  </div>
</template>

<script setup>
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

async function handleGoogleSuccess(response) {
  const idToken = response.credential;
  if (idToken) {
    localStorage.setItem('id_token', idToken);

    try {
      const res = await fetch(`${import.meta.env.VITE_AUTH_API_BASE_URL}/users/me`, {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${idToken}`
        },
      });

      if (!res.ok) throw new Error('Failed to fetch user profile');

      const user = await res.json();
      localStorage.setItem('user', JSON.stringify(user));

      // 🔁 Redirect by role
      if (user.role === 'ADMIN') {
        router.push('/admin_panel');
      } else if (user.role === 'MANAGER') {
        router.push('/manager_panel');
      } else {
        router.push('/dashboard');
      }
    } catch (err) {
      console.error('Error during login:', err);
    }
  }
}

function initGoogleSignIn() {
  google.accounts.id.initialize({
    client_id: import.meta.env.VITE_GOOGLE_CLIENT_ID,
    callback: handleGoogleSuccess
  });
  google.accounts.id.renderButton(
    document.getElementById("googleSignInDiv"),
    { theme: "outline", size: "large", width: "300" }
  );
}

onMounted(() => {
  if (!window.google || !window.google.accounts) {
    const script = document.createElement('script');
    script.src = 'https://accounts.google.com/gsi/client';
    script.async = true;
    script.defer = true;
    script.onload = initGoogleSignIn;
    document.head.appendChild(script);
  } else {
    initGoogleSignIn();
  }
});
</script>

<style scoped>
/* You can enhance styles here if needed */
</style>
