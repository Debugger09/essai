<template>
  <div class="min-h-screen bg-gradient-to-br from-slate-50 via-blue-50/30 to-cyan-50/30 dark:from-gray-900 dark:via-blue-950/50 dark:to-indigo-950/30 transition-all duration-500 relative">
    <!-- Background Elements -->
    <div class="absolute inset-0 overflow-hidden pointer-events-none">
      <div class="absolute top-1/4 left-1/4 w-96 h-96 bg-gradient-to-r from-blue-400/10 to-indigo-400/10 dark:from-blue-600/20 dark:to-indigo-600/20 rounded-full blur-3xl animate-pulse"></div>
      <div class="absolute bottom-1/4 right-1/4 w-80 h-80 bg-gradient-to-r from-indigo-400/8 to-purple-400/8 dark:from-indigo-600/15 dark:to-purple-600/15 rounded-full blur-3xl animate-pulse delay-1000"></div>
    </div>
    <div class="relative z-10 container mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-8 lg:py-12">
      <!-- Header Section -->
      <div class="mb-8 sm:mb-12">
        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <div>
            <h1 class="text-3xl sm:text-4xl lg:text-5xl font-bold bg-gradient-to-r from-blue-900 via-blue-700 to-indigo-600 dark:from-blue-400 dark:via-blue-300 dark:to-indigo-300 bg-clip-text text-transparent mb-3 sm:mb-4">
              Gestion des Utilisateurs
            </h1>
            <p class="text-slate-600 dark:text-slate-400 text-base sm:text-lg lg:text-xl max-w-2xl">
              Gérez les membres, rôles et accès de votre espace collaboratif
            </p>
          </div>
          <button @click="openAddModal" class="group relative bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 text-white px-6 py-3 rounded-2xl font-semibold shadow-lg hover:shadow-xl transition-all duration-300 hover:scale-105 flex items-center space-x-2">
            <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
            </svg>
            <span>Ajouter un utilisateur</span>
          </button>
        </div>
      </div>
      <!-- Search -->
      <SearchForm v-model="searchQuery" placeholder="Rechercher un utilisateur..." class="mb-8" />
      <div v-if="loading" class="text-center py-12">
        <span class="text-blue-600 font-semibold">Chargement des utilisateurs...</span>
      </div>
      <div v-else class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-2xl shadow-lg border border-white/20 dark:border-gray-700/50 p-4 sm:p-8 transition-all duration-300">
        <table class="w-full table-auto border-collapse rounded-lg overflow-hidden">
          <thead class="bg-blue-50 dark:bg-blue-900/30">
            <tr>
              <th class="px-4 py-2 text-left">Nom</th>
              <th class="px-4 py-2 text-left">Prénom</th>
              <th class="px-4 py-2 text-left">Email</th>
              <th class="px-4 py-2 text-left">Rôle</th>
              <th class="px-4 py-2 text-left">Statut</th>
              <th class="px-4 py-2 text-left">Date d'inscription</th>
              <th class="px-4 py-2 text-left">Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in paginatedUsers" :key="user.id" class="border-b last:border-b-0 hover:bg-blue-50/40 dark:hover:bg-blue-900/10">
              <td class="px-4 py-2 font-semibold text-slate-800 dark:text-slate-100">{{ user.lastName }}</td>
              <td class="px-4 py-2 text-slate-700 dark:text-slate-200">{{ user.firstName }}</td>
              <td class="px-4 py-2 text-slate-700 dark:text-slate-200">{{ user.email }}</td>
              <td class="px-4 py-2">
                <span :class="[
                  'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium',
                  user.role === 'ADMIN' ? 'bg-purple-100 text-purple-800 dark:bg-purple-900/30 dark:text-purple-300' :
                  user.role === 'MEMBRE_PROJET' ? 'bg-blue-100 text-blue-800 dark:bg-blue-900/30 dark:text-blue-300' :
                  'bg-gray-100 text-gray-800 dark:bg-gray-700 dark:text-gray-300'
                ]">
                  {{ user.role }}
                </span>
              </td>
              <td class="px-4 py-2">
                <span :class="user.status ? 'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800 dark:bg-green-900/30 dark:text-green-300' : 'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-red-100 text-red-800 dark:bg-red-900/30 dark:text-red-300'">
                  {{ user.status ? 'Actif' : 'Inactif' }}
                </span>
              </td>
              <td class="px-4 py-2 text-slate-700 dark:text-slate-200">{{ formatDate(user.createdAt) }}</td>
              <td class="px-4 py-2">
                <div class="flex items-center gap-2">
                  <button @click="openEditModal(user)" class="p-2 text-amber-600 hover:text-amber-800 dark:text-amber-400 dark:hover:text-amber-300 hover:bg-amber-50 dark:hover:bg-amber-900/20 rounded-lg transition-all duration-200" title="Modifier">
                    <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                    </svg>
                  </button>
                  <button @click="openDeleteModal(user)" class="p-2 text-red-600 hover:text-red-800 dark:text-red-400 dark:hover:text-red-300 hover:bg-red-50 dark:hover:bg-red-900/20 rounded-lg transition-all duration-200" title="Supprimer">
                    <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                    </svg>
                  </button>
                </div>
              </td>
            </tr>
            <tr v-if="paginatedUsers.length === 0">
              <td colspan="7" class="text-center text-gray-500 py-6">Aucun utilisateur trouvé.</td>
            </tr>
          </tbody>
        </table>
        <!-- Pagination -->
        <div class="flex justify-center mt-6 gap-2">
          <button @click="prevPage" :disabled="page === 1" class="px-3 py-1 rounded bg-blue-100 dark:bg-blue-800 text-blue-700 dark:text-blue-200 font-semibold disabled:opacity-50">&lt;</button>
          <span class="px-3 py-1">Page {{ page }} / {{ totalPages }}</span>
          <button @click="nextPage" :disabled="page === totalPages" class="px-3 py-1 rounded bg-blue-100 dark:bg-blue-800 text-blue-700 dark:text-blue-200 font-semibold disabled:opacity-50">&gt;</button>
        </div>
      </div>
    </div>
  </div>
  <!-- Modal utilisateur (ajout/modif) -->
  <Modal :model-value="showModal" :title="isEdit ? 'Modifier un utilisateur' : 'Ajouter un utilisateur'" :loading="modalLoading" @update:modelValue="showModal = $event">
    <form @submit.prevent="submitUser" class="space-y-4">
      <div class="flex gap-4">
        <div class="flex-1">
          <label class="block text-sm font-medium mb-1">Nom</label>
          <input v-model="userForm.lastName" required class="w-full px-4 py-2 rounded-lg border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-800" />
        </div>
        <div class="flex-1">
          <label class="block text-sm font-medium mb-1">Prénom</label>
          <input v-model="userForm.firstName" required class="w-full px-4 py-2 rounded-lg border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-800" />
        </div>
      </div>
      <div>
        <label class="block text-sm font-medium mb-1">Email</label>
        <input v-model="userForm.email" type="email" required class="w-full px-4 py-2 rounded-lg border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-800" />
      </div>
      <div class="flex gap-4">
        <div class="flex-1">
          <label class="block text-sm font-medium mb-1">Rôle</label>
          <select v-model="userForm.role" class="w-full px-4 py-2 rounded-lg border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-800">
            <option value="ADMIN">Admin</option>
            <option value="CHEF_PROJET">Chef projet</option>
            <option value="MEMBRE_PROJET">Membre projet</option>
          </select>
        </div>
        <div class="flex-1">
          <label class="block text-sm font-medium mb-1">Statut</label>
          <select v-model="userForm.status" class="w-full px-4 py-2 rounded-lg border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-800">
            <option :value="true">Actif</option>
            <option :value="false">Inactif</option>
          </select>
        </div>
      </div>
      <div class="flex gap-4">
        <div class="flex-1">
          <label class="block text-sm font-medium mb-1">Téléphone</label>
          <input v-model="userForm.phone" class="w-full px-4 py-2 rounded-lg border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-800" />
        </div>
        <div class="flex-1">
          <label class="block text-sm font-medium mb-1">Poste</label>
          <input v-model="userForm.job" class="w-full px-4 py-2 rounded-lg border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-800" />
        </div>
      </div>
      <!-- Champs mot de passe -->
      <div class="flex gap-4">
        <div class="flex-1">
          <label class="block text-sm font-medium mb-1">Mot de passe <span v-if="!isEdit" class="text-red-500">*</span></label>
          <input v-model="password" :required="!isEdit" type="password" class="w-full px-4 py-2 rounded-lg border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-800" autocomplete="new-password" />
        </div>
        <div class="flex-1">
          <label class="block text-sm font-medium mb-1">Confirmer le mot de passe <span v-if="!isEdit" class="text-red-500">*</span></label>
          <input v-model="confirmPassword" :required="!isEdit" type="password" class="w-full px-4 py-2 rounded-lg border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-800" autocomplete="new-password" />
        </div>
      </div>
      <div v-if="passwordError" class="text-red-600 text-sm">{{ passwordError }}</div>
      <div class="flex justify-end gap-2 mt-6">
        <button type="button" @click="showModal = false" class="px-4 py-2 rounded-lg bg-gray-100 dark:bg-gray-700 text-gray-700 dark:text-gray-200 font-semibold">Annuler</button>
        <button type="submit" :disabled="modalLoading" class="px-4 py-2 rounded-lg bg-blue-600 text-white font-semibold hover:bg-blue-700 transition disabled:opacity-50">
          {{ isEdit ? 'Enregistrer' : 'Ajouter' }}
        </button>
      </div>
    </form>
  </Modal>
  <!-- Modal confirmation suppression -->
  <Modal :model-value="showDeleteModal" title="Supprimer l'utilisateur" :loading="modalLoading" @update:modelValue="showDeleteModal = $event">
    <div class="text-center">
      <p class="mb-4">Voulez-vous vraiment supprimer l'utilisateur <span class="font-semibold">{{ userToDelete?.lastName }} {{ userToDelete?.firstName }}</span> ?</p>
      <div class="flex justify-center gap-2 mt-6">
        <button type="button" @click="showDeleteModal = false" class="px-4 py-2 rounded-lg bg-gray-100 dark:bg-gray-700 text-gray-700 dark:text-gray-200 font-semibold">Annuler</button>
        <button type="button" @click="confirmDelete" :disabled="modalLoading" class="px-4 py-2 rounded-lg bg-red-600 text-white font-semibold hover:bg-red-700 transition disabled:opacity-50">Supprimer</button>
      </div>
    </div>
  </Modal>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useNuxtApp } from '#app'
import { useFormatters } from '~/composables/useFormatters'
import SearchForm from '~/components/common/SearchForm.vue'
import Modal from '~/components/common/Modal.vue'

const { $axios } = useNuxtApp()
const { formatDate } = useFormatters()

const users = ref([])
const loading = ref(true)
const searchQuery = ref('')
const page = ref(1)
const pageSize = 10

// Modal state
const showModal = ref(false)
const isEdit = ref(false)
const modalLoading = ref(false)
const userForm = ref({ id: null, lastName: '', firstName: '', email: '', role: 'MEMBRE_PROJET', status: true, job: '', phone: '' })
const showDeleteModal = ref(false)
const userToDelete = ref(null)

const password = ref('')
const confirmPassword = ref('')
const passwordError = ref('')

const fetchUsers = async () => {
  loading.value = true
  try {
    const res = await $axios.get('/users')
    users.value = Array.isArray(res.data) ? res.data : []
  } catch (e) {
    users.value = []
  } finally {
    loading.value = false
  }
}

onMounted(fetchUsers)

const filteredUsers = computed(() => {
  if (!searchQuery.value) return users.value
  const q = searchQuery.value.toLowerCase()
  return users.value.filter(u =>
    u.lastName?.toLowerCase().includes(q) ||
    u.firstName?.toLowerCase().includes(q) ||
    u.email?.toLowerCase().includes(q)
  )
})

const totalPages = computed(() => Math.max(1, Math.ceil(filteredUsers.value.length / pageSize)))
const paginatedUsers = computed(() => {
  const start = (page.value - 1) * pageSize
  return filteredUsers.value.slice(start, start + pageSize)
})

const prevPage = () => { if (page.value > 1) page.value-- }
const nextPage = () => { if (page.value < totalPages.value) page.value++ }

// Actions
const openAddModal = () => {
  isEdit.value = false
  userForm.value = { id: null, lastName: '', firstName: '', email: '', role: 'MEMBRE_PROJET', status: true, job: '', phone: '' }
  password.value = ''
  confirmPassword.value = ''
  passwordError.value = ''
  showModal.value = true
}
const openEditModal = (user) => {
  isEdit.value = true
  userForm.value = { ...user }
  password.value = ''
  confirmPassword.value = ''
  passwordError.value = ''
  showModal.value = true
}
const submitUser = async () => {
  passwordError.value = ''
  if (password.value || confirmPassword.value) {
    if (password.value !== confirmPassword.value) {
      passwordError.value = 'Les mots de passe ne correspondent pas.'
      return
    }
  }
  modalLoading.value = true
  try {
    const payload = { ...userForm.value }
    if (password.value) payload.password = password.value
    if (isEdit.value) {
      await $axios.put(`/users/${userForm.value.id}`, payload)
    } else {
      await $axios.post('/users', payload)
    }
    showModal.value = false
    await fetchUsers()
  } catch (e) {
    // Gérer l'erreur (optionnel)
  } finally {
    modalLoading.value = false
  }
}
const openDeleteModal = (user) => {
  userToDelete.value = user
  showDeleteModal.value = true
}
const confirmDelete = async () => {
  if (!userToDelete.value) return
  modalLoading.value = true
  try {
    await $axios.delete(`/users/${userToDelete.value.id}`)
    showDeleteModal.value = false
    await fetchUsers()
  } catch (e) {
    // Gérer l'erreur (optionnel)
  } finally {
    modalLoading.value = false
    userToDelete.value = null
  }
}
</script> 