<template>
  <div>
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
                Gestion des Matériels
              </h1>
              <p class="text-slate-600 dark:text-slate-400 text-base sm:text-lg lg:text-xl max-w-2xl">
                Gérez votre inventaire de matériels et équipements
              </p>
            </div>
            <button
              @click="openAddModal"
              class="group relative bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 text-white px-6 py-3 rounded-2xl font-semibold shadow-lg hover:shadow-xl transition-all duration-300 hover:scale-105 flex items-center space-x-2"
            >
              <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
              </svg>
              <span>Ajouter un matériel</span>
            </button>
          </div>
        </div>

        <!-- Stats Cards -->
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 sm:gap-6 mb-8 sm:mb-12">
          <div class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-2xl shadow-lg border border-white/20 dark:border-gray-700/50 p-4 sm:p-6 transition-all duration-300 hover:scale-[1.02]">
            <div class="flex items-center space-x-3">
              <div class="p-3 bg-blue-100 dark:bg-blue-900/30 rounded-xl">
                <svg class="h-6 w-6 text-blue-600 dark:text-blue-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4-8-4m16 0v10l-8 4-8-4V7" />
                </svg>
              </div>
              <div>
                <p class="text-2xl font-bold text-slate-800 dark:text-slate-100">{{ totalMateriels }}</p>
                <p class="text-sm text-slate-600 dark:text-slate-400">Total matériels</p>
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
                <p class="text-2xl font-bold text-slate-800 dark:text-slate-100">{{ materielsActifs }}</p>
                <p class="text-sm text-slate-600 dark:text-slate-400">Actifs</p>
              </div>
            </div>
          </div>

          <div class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-2xl shadow-lg border border-white/20 dark:border-gray-700/50 p-4 sm:p-6 transition-all duration-300 hover:scale-[1.02]">
            <div class="flex items-center space-x-3">
              <div class="p-3 bg-emerald-100 dark:bg-emerald-900/30 rounded-xl">
                <svg class="h-6 w-6 text-emerald-600 dark:text-emerald-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9v-2.828l8.586-8.586z" />
                </svg>
              </div>
              <div>
                <p class="text-2xl font-bold text-slate-800 dark:text-slate-100">{{ materielsReutilisables }}</p>
                <p class="text-sm text-slate-600 dark:text-slate-400">Réutilisables</p>
              </div>
            </div>
          </div>

          <div class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-2xl shadow-lg border border-white/20 dark:border-gray-700/50 p-4 sm:p-6 transition-all duration-300 hover:scale-[1.02]">
            <div class="flex items-center space-x-3">
              <div class="p-3 bg-amber-100 dark:bg-amber-900/30 rounded-xl">
                <svg class="h-6 w-6 text-amber-600 dark:text-amber-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
                </svg>
              </div>
              <div>
                <p class="text-2xl font-bold text-slate-800 dark:text-slate-100">{{ quantiteTotale }}</p>
                <p class="text-sm text-slate-600 dark:text-slate-400">Quantité totale</p>
              </div>
            </div>
          </div>
        </div>

        <!-- SearchForm -->
        <SearchForm
          v-model="searchTerm"
          placeholder="Rechercher un matériel..."
          class="mb-8"
        >
          <select 
            v-model="filterStatus"
            class="px-4 py-3 bg-slate-50 dark:bg-gray-700 border border-slate-200 dark:border-gray-600 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200"
          >
            <option value="">Tous les statuts</option>
            <option value="actif">Actifs</option>
            <option value="inactif">Inactifs</option>
          </select>
          
          <select 
            v-model="filterReutilisable"
            class="px-4 py-3 bg-slate-50 dark:bg-gray-700 border border-slate-200 dark:border-gray-600 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200"
          >
            <option value="">Tous les types</option>
            <option value="true">Réutilisables</option>
            <option value="false">Non réutilisables</option>
          </select>
        </SearchForm>

        <!-- DataTable -->
        <DataTable
          :columns="columns"
          :items="filteredMateriels"
          :loading="loading"
          :current-page="currentPage"
          :total-pages="totalPages"
          :items-per-page="itemsPerPage"
          :total-items="filteredMateriels.length"
          @previous-page="previousPage"
          @next-page="nextPage"
          @go-to-page="goToPage"
        >
          <!-- Custom column slots -->
          <template #type="{ item }">
            <span 
              :class="[
                'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium',
                item.reutilisable 
                  ? 'bg-emerald-100 text-emerald-800 dark:bg-emerald-900/30 dark:text-emerald-300' 
                  : 'bg-gray-100 text-gray-800 dark:bg-gray-700 dark:text-gray-300'
              ]"
            >
              {{ item.reutilisable ? 'Réutilisable' : 'Usage unique' }}
            </span>
          </template>

          <template #status="{ item }">
            <span 
              :class="[
                'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium',
                item.actif 
                  ? 'bg-green-100 text-green-800 dark:bg-green-900/30 dark:text-green-300' 
                  : 'bg-red-100 text-red-800 dark:bg-red-900/30 dark:text-red-300'
              ]"
            >
              {{ item.actif ? 'Actif' : 'Inactif' }}
            </span>
          </template>

          <template #actions="{ item }">
            <div class="flex items-center justify-center space-x-2">
              <button 
                @click="viewMateriel(item)"
                class="p-2 text-blue-600 hover:text-blue-800 dark:text-blue-400 dark:hover:text-blue-300 hover:bg-blue-50 dark:hover:bg-blue-900/20 rounded-lg transition-all duration-200"
                title="Voir détails"
              >
                <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                </svg>
              </button>
              <button 
                v-if="mouvementsForMateriel(item.id).length > 0"
                @click="restituerMateriel(mouvementsForMateriel(item.id)[0].id)"
                class="p-2 text-emerald-600 hover:text-emerald-800 dark:text-emerald-400 dark:hover:text-emerald-300 hover:bg-emerald-50 dark:hover:bg-emerald-900/20 rounded-lg transition-all duration-200"
                title="Restituer"
              >
                <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v16m16-8H4" />
                </svg>
              </button>
              <button 
                @click="editMateriel(item)"
                class="p-2 text-amber-600 hover:text-amber-800 dark:text-amber-400 dark:hover:text-amber-300 hover:bg-amber-50 dark:hover:bg-amber-900/20 rounded-lg transition-all duration-200"
                title="Modifier"
              >
                <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                </svg>
              </button>
              <button 
                @click="deleteMateriel(item)"
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
          v-model="showModal"
          :title="isEditing ? 'Modifier le matériel' : 'Ajouter un matériel'"
          :loading="submitting"
          :disabled="!isFormValid"
          :confirm-text="isEditing ? 'Modifier' : 'Ajouter'"
          @close="closeModal"
          @confirm="submitForm"
        >
          <form @submit.prevent="submitForm" class="space-y-6">
            <div>
              <label for="libelle" class="block text-sm font-semibold text-slate-700 dark:text-slate-300 mb-2">
                Libellé *
              </label>
              <input
                id="libelle"
                v-model="form.libelle"
                type="text"
                required
                :class="[
                  'w-full px-4 py-3 rounded-xl border transition-all duration-200 focus:ring-2 focus:ring-blue-500 focus:border-transparent',
                  errors.libelle 
                    ? 'border-red-300 bg-red-50 dark:bg-red-900/20 dark:border-red-600' 
                    : 'border-slate-200 dark:border-gray-600 bg-slate-50 dark:bg-gray-700'
                ]"
                placeholder="Nom du matériel"
              />
              <p v-if="errors.libelle" class="mt-1 text-sm text-red-600 dark:text-red-400">{{ errors.libelle }}</p>
            </div>

            <div>
              <label for="quantite" class="block text-sm font-semibold text-slate-700 dark:text-slate-300 mb-2">
                Quantité *
              </label>
              <input
                id="quantite"
                v-model.number="form.quantite"
                type="number"
                min="0"
                step="0.01"
                required
                :class="[
                  'w-full px-4 py-3 rounded-xl border transition-all duration-200 focus:ring-2 focus:ring-blue-500 focus:border-transparent',
                  errors.quantite 
                    ? 'border-red-300 bg-red-50 dark:bg-red-900/20 dark:border-red-600' 
                    : 'border-slate-200 dark:border-gray-600 bg-slate-50 dark:bg-gray-700'
                ]"
                placeholder="0"
              />
              <p v-if="errors.quantite" class="mt-1 text-sm text-red-600 dark:text-red-400">{{ errors.quantite }}</p>
            </div>

            <div class="grid grid-cols-1 sm:grid-cols-2 gap-6">
              <div>
                <label class="block text-sm font-semibold text-slate-700 dark:text-slate-300 mb-3">
                  Type de matériel
                </label>
                <div class="space-y-2">
                  <label class="flex items-center space-x-3 cursor-pointer">
                    <input
                      v-model="form.reutilisable"
                      type="radio"
                      :value="true"
                      class="w-4 h-4 text-blue-600 border-slate-300 focus:ring-blue-500"
                    />
                    <span class="text-sm text-slate-700 dark:text-slate-300">Réutilisable</span>
                  </label>
                  <label class="flex items-center space-x-3 cursor-pointer">
                    <input
                      v-model="form.reutilisable"
                      type="radio"
                      :value="false"
                      class="w-4 h-4 text-blue-600 border-slate-300 focus:ring-blue-500"
                    />
                    <span class="text-sm text-slate-700 dark:text-slate-300">Usage unique</span>
                  </label>
                </div>
              </div>

              <div>
                <label class="block text-sm font-semibold text-slate-700 dark:text-slate-300 mb-3">
                  Statut
                </label>
                <div class="space-y-2">
                  <label class="flex items-center space-x-3 cursor-pointer">
                    <input
                      v-model="form.actif"
                      type="radio"
                      :value="true"
                      class="w-4 h-4 text-blue-600 border-slate-300 focus:ring-blue-500"
                    />
                    <span class="text-sm text-slate-700 dark:text-slate-300">Actif</span>
                  </label>
                  <label class="flex items-center space-x-3 cursor-pointer">
                    <input
                      v-model="form.actif"
                      type="radio"
                      :value="false"
                      class="w-4 h-4 text-blue-600 border-slate-300 focus:ring-blue-500"
                    />
                    <span class="text-sm text-slate-700 dark:text-slate-300">Inactif</span>
                  </label>
                </div>
              </div>
            </div>

            <!-- Message d'erreur général -->
            <div v-if="submitError" class="p-4 bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 rounded-xl">
              <div class="flex items-center space-x-2">
                <svg class="h-5 w-5 text-red-600 dark:text-red-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                <p class="text-sm text-red-600 dark:text-red-400">{{ submitError }}</p>
              </div>
            </div>
            <div class="flex justify-end gap-4 pt-4">
              <button
                type="button"
                @click="closeModal"
                class="px-6 py-2 rounded-lg bg-gray-200 dark:bg-gray-700 text-gray-700 dark:text-gray-200 hover:bg-gray-300 dark:hover:bg-gray-600 transition"
              >
                Annuler
              </button>
              <button
                type="submit"
                :disabled="submitting || !isFormValid"
                class="px-6 py-2 rounded-lg bg-blue-600 text-white hover:bg-blue-700 transition"
              >
                {{ isEditing ? 'Modifier' : 'Ajouter' }}
              </button>
            </div>
          </form>
        </Modal>
      </div>
    </div>
  </div>

  <!-- Modal de détails matériel -->
  <Modal v-model="showDetailsModal" title="Détails du matériel">
    <div v-if="selectedMateriel">
      <h2 class="text-lg font-bold mb-2">{{ selectedMateriel.libelle }}</h2>
      <p>Quantité : {{ selectedMateriel.quantite }}</p>
      <p>Réutilisable : {{ selectedMateriel.reutilisable ? 'Oui' : 'Non' }}</p>
      <p>Actif : {{ selectedMateriel.actif ? 'Oui' : 'Non' }}</p>
      <h3 class="mt-4 font-semibold">Mouvements</h3>
      <ul>
        <li v-for="m in mouvementsForMateriel(selectedMateriel.id)" :key="m.id">
          Projet : {{ m.tache?.projet?.name || 'N/A' }}, Tâche : {{ m.tache?.titre || 'N/A' }}, Quantité : {{ m.quantite }}, Date : {{ m.createdAt ? (new Date(m.createdAt)).toLocaleString() : '' }}
        </li>
        <li v-if="mouvementsForMateriel(selectedMateriel.id).length === 0">Aucun mouvement</li>
      </ul>
    </div>
  </Modal>
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
  { key: 'quantite', label: 'Quantité', contentClass: 'text-sm text-slate-600 dark:text-slate-300' },
  { key: 'type', label: 'Type' },
  { key: 'status', label: 'Statut' },
  { key: 'createdAt', label: 'Date création', format: formatDate },
  { key: 'actions', label: 'Actions', class: 'text-center' }
]

// État réactif
const materiels = ref([])
const loading = ref(false)
const deleting = ref(null)
const searchTerm = ref('')
const filterStatus = ref('')
const filterReutilisable = ref('')

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
  quantite: 0,
  reutilisable: true,
  actif: true
})

const errors = reactive({
  libelle: '',
  quantite: ''
})

// Computed properties
const filteredMateriels = computed(() => {
  return materiels.value.filter(materiel => {
    const matchesSearch = materiel.libelle.toLowerCase().includes(searchTerm.value.toLowerCase())
    const matchesStatus = !filterStatus.value || materiel.actif === (filterStatus.value === 'actif')
    const matchesReutilisable = !filterReutilisable.value || materiel.reutilisable === (filterReutilisable.value === 'true')
    return matchesSearch && matchesStatus && matchesReutilisable
  })
})

// Pagination computed
const totalPages = computed(() => {
  return Math.ceil(filteredMateriels.value.length / itemsPerPage)
})

const totalMateriels = computed(() => materiels.value.length)
const materielsActifs = computed(() => materiels.value.filter(m => m.actif).length)
const materielsReutilisables = computed(() => materiels.value.filter(m => m.reutilisable).length)
const quantiteTotale = computed(() => materiels.value.reduce((total, m) => total + (m.quantite || 0), 0))

const isFormValid = computed(() => {
  return form.libelle.trim() && form.quantite >= 0
})

// Watchers
watch([searchTerm, filterStatus, filterReutilisable], () => {
  currentPage.value = 1
})

// Méthodes utilitaires
const validateForm = () => {
  errors.libelle = ''
  errors.quantite = ''

  if (!form.libelle.trim()) {
    errors.libelle = 'Le libellé est requis'
  }

  if (form.quantite < 0) {
    errors.quantite = 'La quantité doit être positive'
  }

  return !errors.libelle && !errors.quantite
}

// Méthodes du modal
const openAddModal = () => {
  resetForm()
  isEditing.value = false
  showModal.value = true
  console.log('openAddModal')
}

const openEditModal = (materiel) => {
  resetForm()
  form.id = materiel.id
  form.libelle = materiel.libelle
  form.quantite = materiel.quantite
  form.reutilisable = materiel.reutilisable
  form.actif = materiel.actif
  isEditing.value = true
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
  resetForm()
}

const resetForm = () => {
  form.id = null
  form.libelle = ''
  form.quantite = 0
  form.reutilisable = true
  form.actif = true
  errors.libelle = ''
  errors.quantite = ''
  submitError.value = ''
}

// Méthodes API
const loadMateriels = async () => {
  loading.value = true
  try {
    const response = await $axios.get('/materiels')
    materiels.value = response.data || []
  } catch (error) {
    console.error('Erreur lors du chargement des matériels:', error)
    materiels.value = []
  } finally {
    loading.value = false
  }
}

const submitForm = async () => {
  if (!validateForm()) return

  submitting.value = true
  submitError.value = ''

  try {
    const payload = {
      libelle: form.libelle.trim(),
      quantite: form.quantite,
      reutilisable: form.reutilisable,
      actif: form.actif
    }

    let result
    if (isEditing.value) {
      const response = await $axios.put(`/materiels/${form.id}`, payload)
      result = response.data
      // Mettre à jour dans la liste
      const index = materiels.value.findIndex(m => m.id === form.id)
      if (index !== -1) {
        materiels.value[index] = result
      }
    } else {
      const response = await $axios.post('/materiels', payload)
      result = response.data
      // Ajouter à la liste
      materiels.value.push(result)
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
const viewMateriel = (materiel) => {
  selectedMateriel.value = materiel
  showDetailsModal.value = true
}

const editMateriel = (materiel) => {
  openEditModal(materiel)
}

const deleteMateriel = async (materiel) => {
  if (!confirm(`Êtes-vous sûr de vouloir supprimer "${materiel.libelle}" ?`)) {
    return
  }

  deleting.value = materiel.id
  try {
    await $axios.delete(`/materiels/${materiel.id}`)
    materiels.value = materiels.value.filter(m => m.id !== materiel.id)
  } catch (error) {
    console.error('Erreur lors de la suppression:', error)
    alert('Erreur lors de la suppression du matériel')
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

// --- Ajout gestion mouvements et modal détails ---
const mouvements = ref([])
const selectedMateriel = ref(null)
const showDetailsModal = ref(false)

const loadMouvements = async () => {
  try {
    const response = await $axios.get('/listemateriels')
    mouvements.value = response.data || []
  } catch (error) {
    console.error('Erreur lors du chargement des mouvements:', error)
    mouvements.value = []
  }
}

const mouvementsForMateriel = (materielId) => {
  return mouvements.value.filter(m => m.materiel?.id === materielId)
}

const restituerMateriel = async (mouvementId) => {
  if (!confirm('Restituer ce matériel ?')) return
  try {
    await $axios.delete(`/listemateriels/${mouvementId}`)
    await loadMouvements()
    await loadMateriels()
  } catch (error) {
    console.error('Erreur lors de la restitution:', error)
  }
}

// Lifecycle
onMounted(() => {
  loadMateriels()
  loadMouvements()
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