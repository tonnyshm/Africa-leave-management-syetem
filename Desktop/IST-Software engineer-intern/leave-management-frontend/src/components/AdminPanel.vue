
<template>
  <div class="space-y-8">
    <!-- 👤 Admin Info Header -->
    <div class="flex items-center justify-between bg-white shadow p-4 rounded">
      <div>
        <h2 class="text-2xl font-bold">Admin Panel</h2>
        <p class="text-gray-600 capitalize">
          Welcome back, {{ user.fullName }} ({{ user.role.toLowerCase() }})
        </p>
      </div>
      <img
        :src="user.profilePictureUrl || '/default-avatar.png'"
        class="h-16 w-16 rounded-full object-cover border"
        alt="Profile"
      />
    </div>

    <!-- 👥 Manage Users -->
    <section class="bg-white p-4 rounded shadow">
      <h3 class="text-xl font-semibold mb-2">All Users</h3>
      <table class="min-w-full table-auto border">
        <thead>
          <tr class="bg-gray-100">
            <th class="px-4 py-2">Email</th>
            <th class="px-4 py-2">Full Name</th>
            <th class="px-4 py-2">Department</th>
            <th class="px-4 py-2">Role</th>
            <th class="px-4 py-2">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="u in users" :key="u.email">
            <td class="px-4 py-2">{{ u.email }}</td>
            <td class="px-4 py-2">{{ u.fullName }}</td>
            <td class="px-4 py-2">{{ u.department }}</td>
            <td class="px-4 py-2">{{ u.role }}</td>
            <td class="px-4 py-2 space-x-2">
              <button
                v-if="u.role !== 'ADMIN'"
                @click="promote(u.email, 'ADMIN')"
                class="btn"
              >
                To Admin
              </button>
              <button
                v-if="u.role !== 'MANAGER'"
                @click="promote(u.email, 'MANAGER')"
                class="btn"
              >
                To Manager
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </section>


<!-- 🧑‍💼 Manager Functionalities -->
    <section class="bg-white p-4 rounded shadow">
      <h3 class="text-xl font-semibold mb-2"></h3>
      <ManagerPanel />
    </section>
    <!-- ✏️ Adjust Leave Balance -->
    <section class="bg-white p-4 rounded shadow">
      <h3 class="text-xl font-semibold mb-2">Adjust Leave Balance for a User</h3>
      <form @submit.prevent="adjustBalance">
        <div class="space-y-2">
          <input
            v-model="balanceForm.email"
            type="email"
            placeholder="User Email"
            class="input"
            required
          />
          <input
            v-model.number="balanceForm.days"
            type="number"
            step="0.1"
            placeholder="Days to adjust"
            class="input"
            required
          />
          <button type="submit" class="btn bg-blue-600 text-white">Adjust</button>
        </div>
      </form>
    </section>

    <!-- 🏢 Set Organization-Wide Balance -->
<section class="bg-white p-4 rounded shadow">
  <h3 class="text-xl font-semibold mb-2">Set PTO Balance for All Users</h3>
  <form @submit.prevent="setOrgBalance">
    <div class="space-y-2">
      <input
        v-model.number="orgBalance"
        type="number"
        step="0.1"
        placeholder="New Balance (e.g. 20)"
        class="input"
        required
      />
      <button type="submit" class="btn bg-purple-600 text-white">Set Balance for All</button>
    </div>
  </form>
</section>


    <!-- ✏️ Manage Leave Types -->
<section class="bg-white p-4 rounded shadow">
  <h3 class="text-xl font-semibold mb-2">Manage Leave Types</h3>
  <form @submit.prevent="createLeaveType" class="space-y-2 mb-4">
    <input v-model="leaveTypeForm.type" placeholder="New Leave Type (e.g. PTO)" class="input" required />
    <button class="btn bg-green-600 text-white">Add Leave Type</button>
  </form>

  <table class="w-full table-auto border">
    <thead>
      <tr class="bg-gray-100">
        <th class="px-4 py-2">Type</th>
        <th class="px-4 py-2">Actions</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="lt in leaveTypes" :key="lt.id">
        <td class="px-4 py-2">
          <input v-model="lt.type" class="input" />
        </td>
        <td class="px-4 py-2 space-x-2">
          <button class="btn bg-blue-500 text-white" @click="updateLeaveType(lt)">Update</button>
          <button class="btn bg-red-500 text-white" @click="deleteLeaveType(lt.id)">Delete</button>
        </td>
      </tr>
    </tbody>
  </table>
</section>

<!-- ⚙️ Manage Leave Policies -->
<section class="bg-white p-4 rounded shadow">
  <h3 class="text-xl font-semibold mb-2">Configure Leave Policies</h3>

  <!-- 🆕 New Policy Form -->
  <form @submit.prevent="createPolicy" class="space-y-2 mb-4">
    <input
      v-model.number="policyForm.monthlyAccrualRate"
      type="number"
      placeholder="Monthly Accrual"
      class="input"
      required
    />
    <input
      v-model.number="policyForm.maxCarryover"
      type="number"
      placeholder="Max Carryover"
      class="input"
      required
    />
    <button type="submit" class="btn bg-green-600 text-white">Create Policy</button>
  </form>

  <!-- 📝 Editable List -->
  <table v-if="policies.length > 0" class="w-full table-auto border">
    <thead>
      <tr class="bg-gray-100">
        <th class="px-4 py-2">Monthly Accrual</th>
        <th class="px-4 py-2">Max Carryover</th>
        <th class="px-4 py-2">Actions</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="p in policies" :key="p.id">
        <td class="px-4 py-2">
          <input v-model.number="p.monthlyAccrualRate" class="input" />
        </td>
        <td class="px-4 py-2">
          <input v-model.number="p.maxCarryover" class="input" />
        </td>
        <td class="px-4 py-2 space-x-2">
          <button class="btn bg-blue-500 text-white" @click="updatePolicy(p)">Update</button>
        </td>
      </tr>
    </tbody>
  </table>
</section>




    <!-- 📊 Export Report -->
    <section class="bg-white p-4 rounded shadow">
      <h3 class="text-xl font-semibold mb-2">Export CSV Report</h3>
      <div class="flex gap-4 mb-4">
        <input
          v-model="reportFilter.email"
          type="email"
          placeholder="Filter by Email"
          class="input"
        />
        <input
          v-model="reportFilter.leaveType"
          type="text"
          placeholder="Filter by Leave Type"
          class="input"
        />
        <input
          v-model="reportFilter.department"
          type="text"
          placeholder="Filter by Department"
          class="input"
        />
        <button @click="exportCSV" class="btn bg-green-600 text-white">
          Export CSV
        </button>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { api, authApi } from '@/services/api';
import { useToast } from 'vue-toastification';
import ManagerPanel from '@/components/ManagerPanel.vue'; // Import ManagerPanel
const orgBalance = ref(0);

const toast = useToast();

const users = ref([]);
const policies = ref([]);
const user = ref(JSON.parse(localStorage.getItem('user')) || {});

const balanceForm = ref({ email: '', days: 0 });
const policyForm = ref({
  monthlyAccrualRate: 0,
  maxCarryover: 0,
});
const reportFilter = ref({
  email: '',
  leaveType: '',
  department: '',
});

const fetchData = async () => {
  const token = localStorage.getItem('id_token');
  authApi.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  api.defaults.headers.common['Authorization'] = `Bearer ${token}`;

  try {
    const [usersRes, typesRes, policiesRes] = await Promise.all([
      api.get('/leaves/admin/users'),
      api.get('/leaves/types/list'),
      api.get('/admin/manage/policies'),
    ]);
    users.value = usersRes.data;
    leaveTypes.value = typesRes.data;
    policies.value = policiesRes.data;
  } catch (err) {
    console.error('Init error:', err);
    toast.error('Failed to load data');
  }
};


onMounted(fetchData);

const promote = async (email, role) => {
  try {
    await authApi.put('/users/promote', { email, role });
    toast.success(`${email} promoted to ${role}`);
    await fetchData();
  } catch (err) {
    console.error(err);
    toast.error('Failed to promote user');
  }
};

const adjustBalance = async () => {
  try {
    const { email, days } = balanceForm.value;
    await api.patch(`/leaves/admin/users/${email}/balance`, null, {
      params: { amount: days },
    });
    toast.success('Balance adjusted');
    balanceForm.value = { email: '', days: 0 };
  } catch (err) {
    console.error(err);
    toast.error('Failed to adjust balance');
  }
};

const leaveTypeForm = ref({ type: '' });
const leaveTypes = ref([]); // For existing leave types

const createLeaveType = async () => {
  try {
    await api.post('/leaves/types', leaveTypeForm.value);
    toast.success('Leave type created');
    leaveTypeForm.value = { type: '' };
    await fetchData();
  } catch (err) {
    console.error(err);
    toast.error('Failed to create leave type');
  }
};

const updateLeaveType = async (type) => {
  try {
    await api.put(`/leaves/types/${type.id}`, type);
    toast.success('Leave type updated');
    await fetchData();
  } catch (err) {
    console.error(err);
    toast.error('Update failed');
  }
};

const deleteLeaveType = async (id) => {
  try {
    await api.delete(`/leaves/types/${id}`);
    toast.success('Leave type deleted');
    await fetchData();
  } catch (err) {
    console.error(err);
    toast.error('Delete failed');
  }
};

const createPolicy = async () => {
  try {
    const res = await api.post('/admin/manage/policies', policyForm.value);
    policies.value.push(res.data); // add it to the list
    policyForm.value = { type: '', monthlyAccrualRate: 0, maxCarryover: 0 };
    toast.success('Policy created');
  } catch (err) {
    toast.error('Failed to create policy');
    console.error(err);
  }
};

const updatePolicy = async (p) => {
  try {
    await api.put(`/admin/manage/policies/${p.id}`, p);
    toast.success('Policy updated');
    await fetchData();
  } catch (err) {
    console.error(err);
    toast.error('Failed to update policy');
  }
};


const exportCSV = async () => {
  try {
    const params = new URLSearchParams(reportFilter.value).toString();
    const res = await api.get(`/leaves/admin/reports/export?${params}`, {
      responseType: 'blob',
    });
    const url = window.URL.createObjectURL(new Blob([res.data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', 'leave-report.csv');
    document.body.appendChild(link);
    link.click();
    link.remove();
  } catch (err) {
    console.error(err);
    toast.error('Export failed');
  }
};

const setOrgBalance = async () => {
  try {
    await api.post(`/leaves/admin/set-org-balance?balance=${orgBalance.value}`);
    toast.success('Organization-wide balance updated');
    orgBalance.value = 0;
  } catch (err) {
    console.error(err);
    toast.error('Failed to update org-wide balance');
  }
};

</script>

<style scoped>
.input {
  @apply w-full border p-2 rounded;
}
.btn {
  @apply px-4 py-2 rounded bg-gray-200 hover:bg-gray-300;
}
</style>