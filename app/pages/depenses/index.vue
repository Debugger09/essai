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
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
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

      <!-- Filtres et Recherche -->
      <div class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-2xl shadow-lg border border-white/20 dark:border-gray-700/50 p-4 sm:p-6 mb-8">
        <div class="flex flex-col sm:flex-row gap-4">
          <div class="flex-1">
            <div class="relative">
              <svg class="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
              </svg>
              <input
                v-model="searchTerm"
                type="text"
                placeholder="Rechercher une dépense..."
                class="w-full pl-10 pr-4 py-3 bg-slate-50 dark:bg-gray-700 border border-slate-200 dark:border-gray-600 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200"
              />
            </div>
          </div>
          
          <div class="flex gap-2">
            <select 
              v-model="selectedProjet"
              class="px-4 py-3 bg-slate-50 dark:bg-gray-700 border border-slate-200 dark:border-gray-600 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200"
            >
              <option value="">Tous les projets</option>
              <option v-for="projet in projets" :key="projet.id" :value="projet.id">
                {{ projet.nom }}
              </option>
            </select>
          </div>
        </div>
      </div>

      <!-- Tableau des Dépenses -->
      <div class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-2xl shadow-lg border border-white/20 dark:border-gray-700/50 overflow-hidden">
        <div class="overflow-x-auto">
          <table class="w-full">
            <thead class="bg-slate-50/50 dark:bg-gray-700/50">
              <tr>
                <th class="px-6 py-4 text-left text-xs font-semibold text-slate-600 dark:text-slate-300 uppercase tracking-wider">
                  Libellé
                </th>
                <th class="px-6 py-4 text-left text-xs font-semibold text-slate-600 dark:text-slate-300 uppercase tracking-wider">
                  Montant
                </th>
                <th class="px-6 py-4 text-left text-xs font-semibold text-slate-600 dark:text-slate-300 uppercase tracking-wider">
                  Projet
                </th>
                <th class="px-6 py-4 text-left text-xs font-semibold text-slate-600 dark:text-slate-300 uppercase tracking-wider">
                  Date
                </th>
                <th class="px-6 py-4 text-center text-xs font-semibold text-slate-600 dark:text-slate-300 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody class="divide-y divide-slate-200 dark:divide-gray-600">
              <tr 
                v-for="depense in paginatedDepenses" 
                :key="depense.id"
                class="hover:bg-slate-50/50 dark:hover:bg-gray-700/30 transition-colors duration-200"
              >
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm font-medium text-slate-900 dark:text-slate-100">
                    {{ depense.libelle }}
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm text-slate-600 dark:text-slate-300">
                    {{ formatMontant(depense.montant) }}
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm text-slate-600 dark:text-slate-300">
                    {{ depense.projet?.nom || 'Non assigné' }}
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-slate-600 dark:text-slate-300">
                  {{ formatDate(depense.createdAt) }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-center">
                  <div class="flex items-center justify-center space-x-2">
                    <!-- Modifier -->
                    <button 
                      @click="editDepense(depense)"
                      class="p-2 text-amber-600 hover:text-amber-800 dark:text-amber-400 dark:hover:text-amber-300 hover:bg-amber-50 dark:hover:bg-amber-900/20 rounded-lg transition-all duration-200"
                      title="Modifier"
                    >
                      <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                      </svg>
                    </button>
                    
                    <!-- Supprimer -->
                    <button 
                      @click="deleteDepense(depense)"
                      :disabled="deleting === depense.id"
                      class="p-2 text-red-600 hover:text-red-800 dark:text-red-400 dark:hover:text-red-300 hover:bg-red-50 dark:hover:bg-red-900/20 rounded-lg transition-all duration-200 disabled:opacity-50"
                      title="Supprimer"
                    >
                      <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                      </svg>
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
          
          <!-- Message si aucune dépense -->
          <div v-if="paginatedDepenses.length === 0 && !loading" class="text-center py-12">
            <svg class="mx-auto h-12 w-12 text-slate-400 dark:text-slate-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
            </svg>
            <h3 class="mt-2 text-sm font-medium text-slate-900 dark:text-slate-100">Aucune dépense trouvée</h3>
          </div>
        </div>

        <!-- Pagination -->
        <div class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm border-t border-white/20 dark:border-gray-700/50 px-4 py-3 flex items-center justify-between">
          <div class="flex-1 flex justify-between sm:hidden">
            <button
              @click="previousPage"
              :disabled="currentPage === 1"
              class="relative inline-flex items-center px-4 py-2 border border-gray-300 dark:border-gray-600 text-sm font-medium rounded-md text-gray-700 dark:text-gray-200 bg-white dark:bg-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600 disabled:opacity-50"
            >
              Précédent
            </button>
            <button
              @click="nextPage"
              :disabled="currentPage === totalPages"
              class="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 dark:border-gray-600 text-sm font-medium rounded-md text-gray-700 dark:text-gray-200 bg-white dark:bg-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600 disabled:opacity-50"
            >
              Suivant
            </button>
          </div>
          <div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
            <div>
              <p class="text-sm text-gray-700 dark:text-gray-300">
                Affichage de <span class="font-medium">{{ (currentPage - 1) * itemsPerPage + 1 }}</span> à 
                <span class="font-medium">{{ Math.min(currentPage * itemsPerPage, filteredDepenses.length) }}</span> sur 
                <span class="font-medium">{{ filteredDepenses.length }}</span> résultats
              </p>
            </div>
            <div>
              <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px" aria-label="Pagination">
                <button
                  @click="previousPage"
                  :disabled="currentPage === 1"
                  class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-700 text-sm font-medium text-gray-500 dark:text-gray-400 hover:bg-gray-50 dark:hover:bg-gray-600 disabled:opacity-50"
                >
                  <span class="sr-only">Précédent</span>
                  <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
                  </svg>
                </button>
                
                <button
                  v-for="page in totalPages"
                  :key="page"
                  @click="goToPage(page)"
                  :class="[
                    currentPage === page
                      ? 'z-10 bg-blue-50 dark:bg-blue-900/30 border-blue-500 dark:border-blue-400 text-blue-600 dark:text-blue-400'
                      : 'bg-white dark:bg-gray-700 border-gray-300 dark:border-gray-600 text-gray-500 dark:text-gray-400 hover:bg-gray-50 dark:hover:bg-gray-600',
                    'relative inline-flex items-center px-4 py-2 border text-sm font-medium'
                  ]"
                >
                  {{ page }}
                </button>
                
                <button
                  @click="nextPage"
                  :disabled="currentPage === totalPages"
                  class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-700 text-sm font-medium text-gray-500 dark:text-gray-400 hover:bg-gray-50 dark:hover:bg-gray-600 disabled:opacity-50"
                >
                  <span class="sr-only">Suivant</span>
                  <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                  </svg>
                </button>
              </nav>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal d'ajout/modification -->
    <div v-if="showModal" class="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center z-50 p-4" @click.self="closeModal">
      <div class="bg-white/95 dark:bg-gray-800/95 backdrop-blur-sm rounded-3xl shadow-2xl max-w-lg w-full p-6 sm:p-8 border border-white/20 dark:border-gray-700/50 transform transition-all duration-300 scale-100">
        <!-- Header du modal -->
        <div class="flex items-center justify-between mb-6">
          <div class="flex items-center space-x-3">
            <div class="p-3 bg-blue-100 dark:bg-blue-900/30 rounded-2xl">
              <svg class="h-6 w-6 text-blue-600 dark:text-blue-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
              </svg>
            </div>
            <h3 class="text-xl font-bold text-slate-900 dark:text-slate-100">
              {{ isEditing ? 'Modifier la dépense' : 'Ajouter une dépense' }}
            </h3>
          </div>
          <button 
            @click="closeModal"
            class="p-2 text-slate-400 hover:text-slate-600 dark:hover:text-slate-300 hover:bg-slate-100 dark:hover:bg-gray-700 rounded-xl transition-all duration-200"
          >
            <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

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
              Projet
            </label>
            <select
              id="projet"
              v-model="form.projetId"
              class="w-full px-4 py-3 bg-slate-50 dark:bg-gray-700 border border-slate-200 dark:border-gray-600 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200"
            >
              <option value="">Sélectionner un projet</option>
              <option v-for="projet in projets" :key="projet.id" :value="projet.id">
                {{ projet.nom }}
              </option>
            </select>
          </div>

          <!-- Boutons d'action -->
          <div class="flex flex-col sm:flex-row justify-end space-y-3 sm:space-y-0 sm:space-x-3 pt-6 border-t border-slate-200 dark:border-gray-600">
            <button 
              type="button"
              @click="closeModal"
              :disabled="submitting"
              class="px-6 py-3 text-slate-600 dark:text-slate-400 hover:text-slate-800 dark:hover:text-slate-200 hover:bg-slate-100 dark:hover:bg-gray-700 rounded-xl transition-all duration-200 font-medium disabled:opacity-50"
            >
              Annuler
            </button>
            <button 
              type="submit"
              :disabled="submitting || !isFormValid"
              class="px-6 py-3 bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 disabled:from-gray-400 disabled:to-gray-500 text-white rounded-xl font-semibold shadow-lg hover:shadow-xl transition-all duration-300 hover:scale-105 disabled:hover:scale-100 flex items-center justify-center space-x-2"
            >
              <svg v-if="submitting" class="animate-spin h-4 w-4" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              <span>{{ submitting ? 'Enregistrement...' : (isEditing ? 'Modifier' : 'Ajouter') }}</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, reactive, watch } from 'vue'
import { useRuntimeConfig } from '#app'
import { useNuxtApp } from '#app'

// Configuration
const config = useRuntimeConfig()
const { $axios } = useNuxtApp()
const apiBase = config.public.apiBase || 'http://localhost:8080/api'

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
    const matchesProjet = !selectedProjet.value || depense.projet?.id === selectedProjet.value
    return matchesSearch && matchesProjet
  })
})

const paginatedDepenses = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage
  const end = start + itemsPerPage
  return filteredDepenses.value.slice(start, end)
})

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
  return form.libelle.trim() && form.montant > 0
})

// Watchers
watch([searchTerm, selectedProjet], () => {
  currentPage.value = 1
})

// Méthodes utilitaires
const formatDate = (date) => {
  return new Date(date).toLocaleDateString('fr-FR', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  })
}

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
  if (!isFormValid.value) return

  submitting.value = true
  submitError.value = ''

  try {
    const payload = {
      libelle: form.libelle.trim(),
      montant: form.montant,
      projet: form.projetId ? { id: form.projetId } : null
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

const deleteDepense = async (depense) => {
  if (!confirm(`Êtes-vous sûr de vouloir supprimer la dépense "${depense.libelle}" ?`)) {
    return
  }

  deleting.value = depense.id
  try {
    await $axios.delete(`/depenses/${depense.id}`)
    depenses.value = depenses.value.filter(d => d.id !== depense.id)
  } catch (error) {
    console.error('Erreur lors de la suppression:', error)
    alert('Une erreur est survenue lors de la suppression')
  } finally {
    deleting.value = null
  }
}

// Méthodes de pagination
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

// Initialisation
onMounted(() => {
  loadDepenses()
  loadProjets()
})
</script>
