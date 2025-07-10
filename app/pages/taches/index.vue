<template>
  <div class="min-h-screen bg-gradient-to-br from-slate-50 via-blue-50/30 to-cyan-50/30 dark:from-gray-900 dark:via-blue-950/50 dark:to-indigo-950/30 transition-all duration-500">
    <!-- Background Elements -->
    <div class="absolute inset-0 overflow-hidden pointer-events-none">
      <div class="absolute top-1/4 left-1/4 w-96 h-96 bg-gradient-to-r from-blue-400/10 to-indigo-400/10 dark:from-blue-600/20 dark:to-indigo-600/20 rounded-full blur-3xl animate-pulse"></div>
      <div class="absolute bottom-1/4 right-1/4 w-80 h-80 bg-gradient-to-r from-indigo-400/8 to-purple-400/8 dark:from-indigo-600/15 dark:to-purple-600/15 rounded-full blur-3xl animate-pulse delay-1000"></div>
    </div>

    <div class="relative z-10 container mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-8 lg:py-12">
      <!-- En-tête -->
      <div class="mb-8">
        <h1 class="text-3xl sm:text-4xl font-bold text-slate-900 dark:text-slate-100">
          {{ user.value?.role === 'ADMIN' ? 'Toutes les tâches' : 'Mes tâches' }}
        </h1>
        <p v-if="user.value?.role !== 'ADMIN' && userInfo" class="text-slate-600 dark:text-slate-400 mt-2">
          Tâches assignées à {{ userInfo.firstName }} {{ userInfo.lastName }}
        </p>
      </div>

      <!-- Barre de recherche et filtres -->
      <div class="mb-8 flex flex-col sm:flex-row gap-4 items-center justify-between">
        <div class="w-full sm:w-96">
          <div class="relative">
            <input
              v-model="searchQuery"
              type="text"
              placeholder="Rechercher une tâche..."
              class="w-full pl-10 pr-4 py-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm focus:outline-none focus:ring-2 focus:ring-blue-500 dark:focus:ring-blue-400"
            />
            <svg class="absolute left-3 top-2.5 h-5 w-5 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
          </div>
        </div>
        <div class="flex items-center space-x-4">
          <select 
            v-model="projetFilter" 
            class="px-4 py-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm focus:outline-none focus:ring-2 focus:ring-blue-500 dark:focus:ring-blue-400"
          >
            <option value="">Tous les projets</option>
            <option v-for="projet in projetsUniques" :key="projet.id" :value="projet.id">
              {{ projet.name }}
            </option>
          </select>
          <select 
            v-model="statusFilter" 
            class="px-4 py-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm focus:outline-none focus:ring-2 focus:ring-blue-500 dark:focus:ring-blue-400"
          >
            <option value="">Tous les statuts</option>
            <option value="A_FAIRE">À faire</option>
            <option value="EN_COURS">En cours</option>
            <option value="TERMINEE">Terminé</option>
          </select>
          <select 
            v-model="prioriteFilter" 
            class="px-4 py-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm focus:outline-none focus:ring-2 focus:ring-blue-500 dark:focus:ring-blue-400"
          >
            <option value="">Toutes les priorités</option>
            <option value="FAIBLE">Faible</option>
            <option value="MOYENNE">Moyenne</option>
            <option value="ELEVEE">Élevée</option>
          </select>
          <select 
            v-model="triFilter" 
            class="px-4 py-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm focus:outline-none focus:ring-2 focus:ring-blue-500 dark:focus:ring-blue-400"
          >
            <option value="priorite">Trier par priorité</option>
            <option value="delai">Trier par délai</option>
            <option value="projet">Trier par projet</option>
            <option value="statut">Trier par statut</option>
          </select>
        </div>
      </div>

      <!-- Message d'erreur -->
      <div v-if="error" class="mb-8 p-4 bg-red-50 dark:bg-red-900/30 text-red-600 dark:text-red-400 rounded-xl">
        {{ error }}
      </div>

      <!-- Statistiques rapides (toujours sur la liste filtrée) -->
      <div v-if="!loading && filteredAndSortedTaches.length > 0" class="mb-8 grid grid-cols-1 md:grid-cols-4 gap-4">
        <div class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-xl p-4 border border-slate-200/50 dark:border-slate-700/50">
          <div class="flex items-center">
            <div class="p-2 bg-blue-100 dark:bg-blue-900/30 rounded-lg">
              <svg class="h-6 w-6 text-blue-600 dark:text-blue-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
              </svg>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-slate-600 dark:text-slate-400">Total</p>
              <p class="text-2xl font-bold text-slate-900 dark:text-slate-100">{{ filteredAndSortedTaches.length }}</p>
            </div>
          </div>
        </div>
        <div class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-xl p-4 border border-slate-200/50 dark:border-slate-700/50">
          <div class="flex items-center">
            <div class="p-2 bg-yellow-100 dark:bg-yellow-900/30 rounded-lg">
              <svg class="h-6 w-6 text-yellow-600 dark:text-yellow-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-slate-600 dark:text-slate-400">À faire</p>
              <p class="text-2xl font-bold text-slate-900 dark:text-slate-100">{{ filteredAndSortedTaches.filter(t => t.status === 'A_FAIRE').length }}</p>
            </div>
          </div>
        </div>
        <div class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-xl p-4 border border-slate-200/50 dark:border-slate-700/50">
          <div class="flex items-center">
            <div class="p-2 bg-blue-100 dark:bg-blue-900/30 rounded-lg">
              <svg class="h-6 w-6 text-blue-600 dark:text-blue-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
              </svg>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-slate-600 dark:text-slate-400">En cours</p>
              <p class="text-2xl font-bold text-slate-900 dark:text-slate-100">{{ filteredAndSortedTaches.filter(t => t.status === 'EN_COURS').length }}</p>
            </div>
          </div>
        </div>
        <div class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-xl p-4 border border-slate-200/50 dark:border-slate-700/50">
          <div class="flex items-center">
            <div class="p-2 bg-green-100 dark:bg-green-900/30 rounded-lg">
              <svg class="h-6 w-6 text-green-600 dark:text-green-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-slate-600 dark:text-slate-400">Terminées</p>
              <p class="text-2xl font-bold text-slate-900 dark:text-slate-100">{{ filteredAndSortedTaches.filter(t => t.status === 'TERMINEE').length }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Liste des tâches -->
      <div v-if="!loading" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div 
          v-for="tache in filteredAndSortedTaches" 
          :key="tache.id"
          class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-xl shadow-lg hover:shadow-xl transition-all duration-300 border border-slate-200/50 dark:border-slate-700/50"
        >
          <div class="p-6">
            <div class="flex items-start justify-between mb-4">
              <h3 class="text-lg font-semibold text-slate-900 dark:text-slate-100 line-clamp-2">
                {{ tache.titre }}
              </h3>
              <div class="flex flex-col items-end space-y-2">
                <span 
                  :class="[
                    'px-2 py-1 rounded-full text-xs font-medium',
                    {
                      'bg-green-100 text-green-800 dark:bg-green-900/30 dark:text-green-400': tache.status === 'EN_COURS',
                      'bg-blue-100 text-blue-800 dark:bg-blue-900/30 dark:text-blue-400': tache.status === 'A_FAIRE',
                      'bg-purple-100 text-purple-800 dark:bg-purple-900/30 dark:text-purple-400': tache.status === 'TERMINEE'
                    }
                  ]"
                >
                  {{ formatStatus(tache.status) }}
                </span>
                <span 
                  :class="[
                    'px-2 py-1 rounded-full text-xs font-medium',
                    {
                      'bg-blue-100 text-blue-800 dark:bg-blue-900/30 dark:text-blue-400': tache.priorite === 'FAIBLE',
                      'bg-yellow-100 text-yellow-800 dark:bg-yellow-900/30 dark:text-yellow-400': tache.priorite === 'MOYENNE',
                      'bg-red-100 text-red-800 dark:bg-red-900/30 dark:text-red-400': tache.priorite === 'ELEVEE'
                    }
                  ]"
                >
                  {{ formatPriorite(tache.priorite) }}
                </span>
              </div>
            </div>
            
            <p class="text-slate-600 dark:text-slate-400 text-sm mb-4 line-clamp-3">
              {{ tache.description || 'Aucune description' }}
            </p>
            
            <div class="space-y-2 mb-4">
              <div class="flex items-center text-sm text-slate-500 dark:text-slate-400">
                <svg class="h-4 w-4 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
                </svg>
                <span class="font-medium">Projet:</span>
                <span class="ml-1">{{ tache.projet?.name || 'Non assigné' }}</span>
              </div>
              
              <div class="flex items-center text-sm text-slate-500 dark:text-slate-400">
                <svg class="h-4 w-4 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
                <span class="font-medium">Échéance:</span>
                <span class="ml-1" :class="{ 'text-red-600 font-semibold': isTacheEnRetard(tache) }">
                  {{ formatDate(tache.dateEcheance) }}
                </span>
              </div>
            </div>
            
            <div class="flex items-center justify-between">
              <div class="flex items-center space-x-2">
                <div class="flex -space-x-2">
                  <div 
                    v-for="membre in tache.membres?.slice(0, 3)" 
                    :key="membre?.id"
                    v-if="membre"
                    class="w-8 h-8 rounded-full bg-blue-500 text-white flex items-center justify-center text-xs font-medium border-2 border-white dark:border-gray-800"
                  >
                    {{ membre.firstName?.charAt(0) }}{{ membre.lastName?.charAt(0) }}
                  </div>
                  <div 
                    v-if="tache.membres && tache.membres.length > 3"
                    class="w-8 h-8 rounded-full bg-gray-300 dark:bg-gray-600 text-gray-700 dark:text-gray-300 flex items-center justify-center text-xs font-medium border-2 border-white dark:border-gray-800"
                  >
                    +{{ tache.membres.length - 3 }}
                  </div>
                </div>
              </div>
              
              <NuxtLink 
                :to="`/taches/${tache.id}`"
                class="inline-flex items-center px-3 py-2 bg-blue-600 text-white text-sm rounded-lg hover:bg-blue-700 transition"
              >
                Voir détails
                <svg class="h-4 w-4 ml-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                </svg>
              </NuxtLink>
            </div>
          </div>
        </div>
      </div>

      <!-- État de chargement -->
      <div v-if="loading" class="text-center py-12">
        <div class="inline-flex items-center">
          <svg class="animate-spin -ml-1 mr-3 h-5 w-5 text-blue-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
          </svg>
          Chargement de vos tâches...
        </div>
      </div>

      <!-- Message si aucune tâche -->
      <div v-if="!loading && filteredAndSortedTaches.length === 0" class="text-center py-12">
        <svg class="mx-auto h-12 w-12 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
        </svg>
        <h3 class="mt-2 text-sm font-medium text-slate-900 dark:text-slate-100">Aucune tâche trouvée</h3>
        <p class="mt-1 text-sm text-slate-500 dark:text-slate-400">
          {{ searchQuery || statusFilter || prioriteFilter || projetFilter ? 'Essayez de modifier vos filtres.' : (user.value?.role === 'ADMIN' ? 'Aucune tâche dans le système.' : 'Vous n\'avez aucune tâche assignée pour le moment.') }}
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRuntimeConfig } from '#app'
import { useNuxtApp } from '#app'
import { useAuth } from '~/composables/useAuth'
import { useFormatters } from '~/composables/useFormatters'
import { useUserInfo } from '~/composables/useUserInfo'

// Configuration
const config = useRuntimeConfig()
const { $axios } = useNuxtApp()
const { user } = useAuth()
const { formatDate, formatStatus } = useFormatters()
const { userInfo, fetchUserInfo } = useUserInfo()

// État réactif
const taches = ref([])
const loading = ref(true)
const error = ref(null)
const searchQuery = ref('')
const statusFilter = ref('')
const prioriteFilter = ref('')
const projetFilter = ref('')
const triFilter = ref('priorite')

// Computed
const filteredAndSortedTaches = computed(() => {
  let filtered = taches.value
  if (user.value?.role === 'MEMBRE_PROJET') {
    filtered = filtered.filter(tache =>
      Array.isArray(tache.membres) && tache.membres.some(m => m.id === user.value.id)
    )
  }

  // Filtre par recherche
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    filtered = filtered.filter(tache => 
      tache.titre?.toLowerCase().includes(query) ||
      tache.description?.toLowerCase().includes(query) ||
      tache.projet?.name?.toLowerCase().includes(query)
    )
  }

  // Filtre par statut
  if (statusFilter.value) {
    filtered = filtered.filter(tache => tache.status === statusFilter.value)
  }

  // Filtre par priorité
  if (prioriteFilter.value) {
    filtered = filtered.filter(tache => tache.priorite === prioriteFilter.value)
  }

  // Filtre par projet
  if (projetFilter.value) {
    filtered = filtered.filter(tache => tache.projet?.id === projetFilter.value)
  }

  // Tri
  filtered.sort((a, b) => {
    switch (triFilter.value) {
      case 'priorite':
        const prioriteOrder = { 'ELEVEE': 4, 'MOYENNE': 3, 'FAIBLE': 1 }
        return prioriteOrder[b.priorite] - prioriteOrder[a.priorite]
      case 'delai':
        return new Date(a.dateEcheance) - new Date(b.dateEcheance)
      case 'projet':
        return (a.projet?.name || '').localeCompare(b.projet?.name || '')
      case 'statut':
        const statutOrder = { 'A_FAIRE': 1, 'EN_COURS': 2, 'TERMINEE': 3 }
        return statutOrder[a.status] - statutOrder[b.status]
      default:
        return 0
    }
  })

  return filtered
})

const projetsUniques = computed(() => {
  const projets = taches.value.map(tache => tache.projet).filter(Boolean)
  return [...new Map(projets.map(projet => [projet.id, projet])).values()]
})

// Méthodes
const fetchTaches = async () => {
  try {
    loading.value = true
    const res = await $axios.get('/taches')
    taches.value = Array.isArray(res.data) ? res.data : []
  } catch (err) {
    error.value = err.response?.data?.message || 'Erreur de chargement'
    taches.value = []
  } finally {
    loading.value = false
  }
}

const formatPriorite = (priorite) => {
  const priorites = {
    'FAIBLE': 'Faible',
    'MOYENNE': 'Moyenne',
    'ELEVEE': 'Élevée'
  }
  return priorites[priorite] || priorite
}

const isTacheEnRetard = (tache) => {
  if (!tache.dateEcheance || tache.statutTache === 'TERMINEE') return false
  const aujourdHui = new Date()
  const echeance = new Date(tache.dateEcheance)
  return echeance < aujourdHui
}

// Initialisation
onMounted(async () => {
  await fetchTaches()
  await fetchUserInfo()
})
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>

