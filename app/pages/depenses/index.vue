<template>
  <div class="min-h-screen bg-gradient-to-br from-slate-50 via-blue-50/30 to-cyan-50/30 dark:from-gray-900 dark:via-blue-950/50 dark:to-indigo-950/30 transition-all duration-500">
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
              Gestion des Dépenses
            </h1>
            <p class="text-slate-600 dark:text-slate-400 text-base sm:text-lg lg:text-xl max-w-2xl">
              Suivez et gérez les dépenses de vos projets
            </p>
          </div>
          
          <!-- Bouton Ajouter -->
          <button 
            @click="openAddModal"
            :disabled="loading"
            class="group relative bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 disabled:from-gray-400 disabled:to-gray-500 text-white px-6 py-3 rounded-2xl font-semibold shadow-lg hover:shadow-xl transition-all duration-300 hover:scale-105 disabled:hover:scale-100 flex items-center space-x-2"
          >
            <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
            </svg>
            <span>{{ loading ? 'Chargement...' : 'Ajouter une dépense' }}</span>
          </button>
        </div>
      </div>

      <!-- Stats Cards -->
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 sm:gap-6 mb-8 sm:mb-12">
        <div class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-2xl shadow-lg border border-white/20 dark:border-gray-700/50 p-4 sm:p-6 transition-all duration-300 hover:scale-[1.02]">
          <div class="flex items-center space-x-3">
            <div class="p-3 bg-blue-100 dark:bg-blue-900/30 rounded-xl">
              <svg class="h-6 w-6 text-blue-600 dark:text-blue-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
              </svg>
            </div>
            <div>
              <p class="text-2xl font-bold text-slate-800 dark:text-slate-100">{{ formatMontant(totalDepenses) }}</p>
              <p class="text-sm text-slate-600 dark:text-slate-400">Total des dépenses</p>
            </div>
          </div>
        </div>

        <div class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-2xl shadow-lg border border-white/20 dark:border-gray-700/50 p-4 sm:p-6 transition-all duration-300 hover:scale-[1.02]">
          <div class="flex items-center space-x-3">
            <div class="p-3 bg-green-100 dark:bg-green-900/30 rounded-xl">
              <svg class="h-6 w-6 text-green-600 dark:text-green-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <div>
              <p class="text-2xl font-bold text-slate-800 dark:text-slate-100">{{ nombreDepenses }}</p>
              <p class="text-sm text-slate-600 dark:text-slate-400">Nombre de dépenses</p>
            </div>
          </div>
        </div>

        <div class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-2xl shadow-lg border border-white/20 dark:border-gray-700/50 p-4 sm:p-6 transition-all duration-300 hover:scale-[1.02]">
          <div class="flex items-center space-x-3">
            <div class="p-3 bg-emerald-100 dark:bg-emerald-900/30 rounded-xl">
              <svg class="h-6 w-6 text-emerald-600 dark:text-emerald-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" />
              </svg>
            </div>
            <div>
              <p class="text-2xl font-bold text-slate-800 dark:text-slate-100">{{ formatMontant(moyenneDepenses) }}</p>
              <p class="text-sm text-slate-600 dark:text-slate-400">Moyenne par dépense</p>
            </div>
          </div>
        </div>
      </div>

      <!-- SearchForm -->
      <SearchForm
        v-model="searchTerm"
        placeholder="Rechercher une dépense..."
        class="mb-8"
      >
        <select 
          v-model="selectedProjet"
          class="px-4 py-3 bg-slate-50 dark:bg-gray-700 border border-slate-200 dark:border-gray-600 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200"
        >
          <option value="">Tous les projets</option>
          <option v-for="projet in projets" :key="projet.id" :value="projet.id">
            {{ projet.name || projet.nom }}
          </option>
        </select>
      </SearchForm>

      <!-- DataTable -->
      <DataTable
        :columns="columns"
        :items="filteredDepenses"
        :loading="loading"
        :current-page="currentPage"
        :total-pages="totalPages"
        :items-per-page="itemsPerPage"
        :total-items="filteredDepenses.length"
        @previous-page="previousPage"
        @next-page="nextPage"
        @go-to-page="goToPage"
      >
        <!-- Custom column slots -->
        <template #montant="{ item }">
          <span class="text-sm text-slate-600 dark:text-slate-300">
            {{ formatMontant(item.montant) }}
          </span>
        </template>

        <template #projet="{ item }">
          <span class="text-sm text-slate-600 dark:text-slate-300">
            {{ item.projet?.nom || 'Non assigné' }}
          </span>
        </template>

        <template #actions="{ item }">
          <div class="flex items-center justify-center space-x-2">
            <button 
              @click="editDepense(item)"
              class="p-2 text-amber-600 hover:text-amber-800 dark:text-amber-400 dark:hover:text-amber-300 hover:bg-amber-50 dark:hover:bg-amber-900/20 rounded-lg transition-all duration-200"
              title="Modifier"
            >
              <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
              </svg>
            </button>
            
            <button 
              @click="deleteDepense(item)"
              :disabled="deleting === item.id"
              class="p-2 text-red-600 hover:text-red-800 dark:text-red-400 dark:hover:text-red-300 hover:bg-red-50 dark:hover:bg-red-900/20 rounded-lg transition-all duration-200 disabled:opacity-50"
              title="Supprimer"
            >
              <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
              </svg>
            </button>
          </div>
        </template>
      </DataTable>

      <!-- Modal -->
      <Modal
        :show="showModal"
        :title="isEditing ? 'Modifier la dépense' : 'Ajouter une dépense'"
        :loading="submitting"
        :disabled="!isFormValid"
        :confirm-text="isEditing ? 'Modifier' : 'Ajouter'"
        @close="closeModal"
        @confirm="submitForm"
      >
        <form @submit.prevent="submitForm" class="space-y-6">
          <div>
            <label for="libelle" class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">
              Libellé
            </label>
            <input
              type="text"
              id="libelle"
              v-model="form.libelle"
              class="w-full px-4 py-3 bg-slate-50 dark:bg-gray-700 border border-slate-200 dark:border-gray-600 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200"
            />
          </div>

          <div>
            <label for="montant" class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">
              Montant (FCFA)
            </label>
            <div class="relative">
              <input
                type="number"
                id="montant"
                v-model="form.montant"
                step="1"
                min="0"
                class="w-full px-4 py-3 bg-slate-50 dark:bg-gray-700 border border-slate-200 dark:border-gray-600 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200 pr-12"
              />
              <div class="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none">
                <span class="text-slate-500 dark:text-slate-400">FCFA</span>
              </div>
            </div>
          </div>

          <div>
            <label for="projet" class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">
              Projet <span class="text-red-500">*</span>
            </label>
            <select
              id="projet"
              v-model="form.projetId"
              required
              class="w-full px-4 py-3 bg-slate-50 dark:bg-gray-700 border border-slate-200 dark:border-gray-600 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200"
            >
              <option value="">Sélectionner un projet</option>
              <option v-for="projet in projets" :key="projet.id" :value="projet.id">
                {{ projet.name || projet.nom }}
              </option>
            </select>
            <p v-if="!form.projetId && submitError" class="mt-1 text-sm text-red-500">
              Veuillez sélectionner un projet
            </p>
          </div>
        </form>
      </Modal>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, reactive, watch } from 'vue'
import { useRuntimeConfig } from '#app'
import { useNuxtApp } from '#app'
import { useFormatters } from '~/composables/useFormatters'
import { usePagination } from '~/composables/usePagination'
import { useModal } from '~/composables/useModal'
import Modal from '~/components/common/Modal.vue'
import DataTable from '~/components/common/DataTable.vue'
import SearchForm from '~/components/common/SearchForm.vue'

// Configuration
const config = useRuntimeConfig()
const { $axios } = useNuxtApp()
const { formatDate } = useFormatters()

// Configuration de la table
const columns = [
  { key: 'libelle', label: 'Libellé', contentClass: 'text-sm font-medium text-slate-900 dark:text-slate-100' },
  { key: 'montant', label: 'Montant' },
  { key: 'projet', label: 'Projet' },
  { key: 'createdAt', label: 'Date création', format: formatDate },
  { key: 'actions', label: 'Actions', class: 'text-center' }
]

// État réactif
const depenses = ref([])
const projets = ref([])
const loading = ref(false)
const deleting = ref(null)
const searchTerm = ref('')
const selectedProjet = ref('')

// Variables de pagination
const currentPage = ref(1)
const itemsPerPage = 10

// Modal
const showModal = ref(false)
const isEditing = ref(false)
const submitting = ref(false)
const submitError = ref('')

// Formulaire
const form = reactive({
  id: null,
  libelle: '',
  montant: 0,
  projetId: ''
})

// Computed properties
const filteredDepenses = computed(() => {
  return depenses.value.filter(depense => {
    const matchesSearch = depense.libelle.toLowerCase().includes(searchTerm.value.toLowerCase())
    const matchesProjet = !selectedProjet.value || String(depense.projetId ?? '') === String(selectedProjet.value)
    return matchesSearch && matchesProjet
  })
})

// Pagination computed
const totalPages = computed(() => {
  return Math.ceil(filteredDepenses.value.length / itemsPerPage)
})

// Statistiques
const totalDepenses = computed(() => {
  return depenses.value.reduce((total, depense) => total + depense.montant, 0)
})

const nombreDepenses = computed(() => depenses.value.length)

const moyenneDepenses = computed(() => {
  return nombreDepenses.value > 0 ? totalDepenses.value / nombreDepenses.value : 0
})

const isFormValid = computed(() => {
  return form.libelle.trim() && form.montant > 0 && form.projetId
})

// Watchers
watch([searchTerm, selectedProjet], () => {
  currentPage.value = 1
})

// Méthodes utilitaires
const formatMontant = (montant) => {
  return new Intl.NumberFormat('fr-FR', {
    minimumFractionDigits: 0,
    maximumFractionDigits: 0
  }).format(montant) + ' FCFA'
}

const resetForm = () => {
  form.id = null
  form.libelle = ''
  form.montant = 0
  form.projetId = ''
  submitError.value = ''
}

// Méthodes du modal
const openAddModal = () => {
  resetForm()
  isEditing.value = false
  showModal.value = true
}

const openEditModal = (depense) => {
  resetForm()
  form.id = depense.id
  form.libelle = depense.libelle
  form.montant = depense.montant
  form.projetId = depense.projet?.id || ''
  isEditing.value = true
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
  resetForm()
}

// Méthodes API
const loadDepenses = async () => {
  loading.value = true
  try {
    const response = await $axios.get('/depenses')
    depenses.value = response.data || []
  } catch (error) {
    console.error('Erreur lors du chargement des dépenses:', error)
    depenses.value = []
  } finally {
    loading.value = false
  }
}

const loadProjets = async () => {
  try {
    const response = await $axios.get('/projets')
    projets.value = response.data || []
  } catch (error) {
    console.error('Erreur lors du chargement des projets:', error)
    projets.value = []
  }
}

const submitForm = async () => {
  if (!isFormValid.value) {
    submitError.value = 'Veuillez remplir tous les champs obligatoires'
    return
  }

  submitting.value = true
  submitError.value = ''

  try {
    const payload = {
      libelle: form.libelle.trim(),
      montant: form.montant,
      projet: { id: form.projetId }
    }

    let result
    if (isEditing.value) {
      const response = await $axios.put(`/depenses/${form.id}`, payload)
      result = response.data
      // Mettre à jour dans la liste
      const index = depenses.value.findIndex(d => d.id === form.id)
      if (index !== -1) {
        depenses.value[index] = result
      }
    } else {
      const response = await $axios.post('/depenses', payload)
      result = response.data
      // Ajouter à la liste
      depenses.value.push(result)
    }

    closeModal()
  } catch (error) {
    console.error('Erreur lors de la soumission:', error)
    submitError.value = 'Une erreur est survenue lors de l\'enregistrement'
  } finally {
    submitting.value = false
  }
}

// Méthodes d'action
const editDepense = (depense) => {
  openEditModal(depense)
}

const deleteDepense = async (depense) => {
  if (!confirm(`Êtes-vous sûr de vouloir supprimer "${depense.libelle}" ?`)) {
    return
  }

  deleting.value = depense.id
  try {
    await $axios.delete(`/depenses/${depense.id}`)
    depenses.value = depenses.value.filter(d => d.id !== depense.id)
  } catch (error) {
    console.error('Erreur lors de la suppression:', error)
    alert('Erreur lors de la suppression de la dépense')
  } finally {
    deleting.value = null
  }
}

// Pagination methods
const goToPage = (page) => {
  currentPage.value = page
}

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++
  }
}

const previousPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--
  }
}

// Lifecycle
onMounted(() => {
  loadDepenses()
  loadProjets()
})

// Métadonnées de la page
definePageMeta({
  middleware: 'auth',
  layout: 'default'
})
</script>

<style scoped>
@supports (backdrop-filter: blur(10px)) {
  .backdrop-blur-sm {
    backdrop-filter: blur(8px);
  }
}

@supports not (backdrop-filter: blur(10px)) {
  .backdrop-blur-sm {
    background-color: rgba(255, 255, 255, 0.9);
  }
  .dark .backdrop-blur-sm {
    background-color: rgba(31, 41, 55, 0.9);
  }
}
</style> 