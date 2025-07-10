<template>
  <div class="min-h-screen bg-gradient-to-br from-slate-50 via-blue-50/30 to-cyan-50/30 dark:from-gray-900 dark:via-blue-950/50 dark:to-indigo-950/30 transition-all duration-500">
    <!-- Background Elements -->
    <div class="absolute inset-0 overflow-hidden pointer-events-none">
      <div class="absolute top-1/4 left-1/4 w-96 h-96 bg-gradient-to-r from-blue-400/10 to-indigo-400/10 dark:from-blue-600/20 dark:to-indigo-600/20 rounded-full blur-3xl animate-pulse"></div>
      <div class="absolute bottom-1/4 right-1/4 w-80 h-80 bg-gradient-to-r from-indigo-400/8 to-purple-400/8 dark:from-indigo-600/15 dark:to-purple-600/15 rounded-full blur-3xl animate-pulse delay-1000"></div>
    </div>

    <div class="relative z-10 container mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-8 lg:py-12">
      <!-- Barre de recherche et actions -->
      <div class="mb-8 flex flex-col sm:flex-row gap-4 items-center justify-between">
        <div class="w-full sm:w-96">
          <div class="relative">
            <input
              v-model="searchQuery"
              type="text"
              placeholder="Rechercher..."
              class="w-full pl-10 pr-4 py-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm focus:outline-none focus:ring-2 focus:ring-blue-500 dark:focus:ring-blue-400"
            />
            <svg class="absolute left-3 top-2.5 h-5 w-5 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
          </div>
        </div>
      </div>

      <!-- Message d'erreur -->
      <div v-if="error" class="mb-8 p-4 bg-red-50 dark:bg-red-900/30 text-red-600 dark:text-red-400 rounded-xl">
        {{ error }}
      </div>

      <!-- En-tête -->
      <div v-if="tache" class="mb-8">
        <div class="flex items-center justify-between">
          <div>
            <NuxtLink 
              to="/projets"
              class="inline-flex items-center text-blue-600 dark:text-blue-400 hover:text-blue-800 dark:hover:text-blue-300 mb-4"
            >
              <svg class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
              </svg>
              Retour aux projets
            </NuxtLink>
            <h1 class="text-3xl sm:text-4xl font-bold text-slate-900 dark:text-slate-100">
              {{ tache?.titre }}
            </h1>
          </div>
          <div class="flex items-center space-x-4">
            <span 
              :class="[
                'px-4 py-2 rounded-full text-sm font-medium',
                {
                  'bg-green-100 text-green-800 dark:bg-green-900/30 dark:text-green-400': tache?.status === 'EN_COURS',
                  'bg-blue-100 text-blue-800 dark:bg-blue-900/30 dark:text-blue-400': tache?.status === 'EN_ATTENTE',
                  'bg-purple-100 text-purple-800 dark:bg-purple-900/30 dark:text-purple-400': tache?.status === 'TERMINE'
                }
              ]"
            >
              {{ formatStatus(tache?.status) }}
            </span>
          </div>
        </div>
      </div>

      <!-- Informations principales -->
      <div v-if="tache" class="grid grid-cols-1 md:grid-cols-2 gap-8 mb-8">
        <div class="bg-white rounded-lg shadow p-6">
          <h2 class="text-xl font-semibold mb-4">Informations générales</h2>
          <div class="space-y-4">
            <div class="flex justify-between">
              <span class="font-medium">Statut:</span>
              <span :class="statusClass">{{ formatStatus(tache?.status) }}</span>
            </div>
            <div class="flex justify-between">
              <span class="font-medium">Priorité:</span>
              <span>{{ formatPriorite(tache?.priorite) }}</span>
            </div>
            <div class="flex justify-between">
              <span class="font-medium">Date d'échéance:</span>
              <span>{{ formatDate(tache?.dateEcheance) }}</span>
            </div>
            <div class="flex justify-between">
              <span class="font-medium">Projet:</span>
              <span>{{ tache?.projet?.name || 'Non assigné' }}</span>
            </div>
            <div class="flex justify-between">
              <span class="font-medium">Assigné à:</span>
              <span>
                <template v-if="tache?.membres && tache.membres.length > 0">
                  {{ tache.membres.map(m => `${m.user.lastName} ${m.user.firstName}`).join(', ') }}
                </template>
                <template v-else>
                  Non assigné
                </template>
              </span>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-lg shadow p-6">
          <h2 class="text-xl font-semibold mb-4">Description</h2>
          <p class="text-gray-700 dark:text-gray-300">{{ tache?.description || 'Aucune description' }}</p>
        </div>
      </div>

      <!-- Bloc Matériel -->
      <div v-if="tache" class="bg-white rounded-lg shadow mb-8">
        <div class="p-6 border-b">
          <div class="flex justify-between items-center">
            <h2 class="text-xl font-semibold">Matériel</h2>
            <button
              @click="showMaterielModal = true"
              class="inline-flex items-center px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition cursor-pointer"
              v-if="canEdit"
            >
              <i class="fas fa-plus mr-2"></i>
              Ajouter
            </button>
          </div>
        </div>
        <div class="p-6">
          <MaterielList 
            :tache-id="tache.id" 
            ref="materielListRef"
          />
        </div>
      </div>

      <div v-else class="text-center text-slate-500 py-12">
        Chargement de la tâche...
      </div>
    </div>

    <!-- Modal Matériel -->
    <Modal v-model="showMaterielModal" title="Ajouter du matériel">
      <form @submit.prevent="submitMateriel" class="space-y-6 bg-gray-50 dark:bg-gray-900 p-6 rounded-xl border border-gray-200 dark:border-gray-700">
        <!-- Champ de recherche/autocomplétion -->
        <div class="relative">
          <label class="block text-base font-semibold mb-2 text-gray-700 dark:text-gray-200">Sélectionner un matériel</label>
          <input
            v-model="materielSearchQuery"
            @input="onMaterielSearchInput"
            @focus="showMaterielSuggestions = true"
            @blur="onMaterielSearchBlur"
            type="text"
            class="form-input w-full px-4 py-2 rounded-lg border border-gray-300 dark:border-gray-600 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-800 dark:text-white"
            placeholder="Rechercher un matériel..."
            autocomplete="off"
          />
          <!-- Suggestions de matériels -->
          <div v-if="showMaterielSuggestions && filteredMaterielsDisponibles.length > 0" 
            class="absolute z-10 w-full mt-1 bg-white dark:bg-gray-800 border border-gray-300 dark:border-gray-600 rounded-lg shadow-lg">
            <ul class="max-h-60 overflow-auto">
              <li v-for="materiel in filteredMaterielsDisponibles" 
                :key="materiel.id"
                @click="selectMateriel(materiel)"
                class="px-4 py-2 hover:bg-gray-50 dark:hover:bg-gray-700 cursor-pointer"
                :class="{ 'opacity-50 cursor-not-allowed': !materiel.reutilisable && materiel.quantite <= 0 }">
                <div class="flex justify-between items-center">
                  <span>{{ materiel.libelle }}</span>
                  <span class="text-xs text-gray-500">
                    {{ materiel.quantite }} dispo{{ materiel.reutilisable ? ', réutilisable' : '' }}
                  </span>
                </div>
              </li>
            </ul>
          </div>
          <!-- Message si aucun résultat -->
          <div v-if="showMaterielSuggestions && materielSearchQuery && materielSearchQuery.trim() !== '' && filteredMaterielsDisponibles.length === 0" 
            class="absolute z-10 w-full mt-1 bg-white dark:bg-gray-800 border border-gray-300 dark:border-gray-600 rounded-lg shadow-lg">
            <div class="px-4 py-2 text-gray-500 text-sm">
              Aucun matériel trouvé
            </div>
          </div>
        </div>

        <!-- Matériel sélectionné -->
        <div v-if="selectedMateriel" class="p-4 bg-blue-50 dark:bg-blue-900/20 rounded-lg border border-blue-200 dark:border-blue-800">
          <div class="flex justify-between items-center">
            <h3 class="font-medium text-blue-900 dark:text-blue-100">{{ selectedMateriel.libelle }}</h3>
            <button @click="selectedMateriel = null" class="text-blue-400 hover:text-blue-600">
              <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
          <p class="text-sm text-blue-700 dark:text-blue-300 mt-1">
            {{ selectedMateriel.quantite }} disponible(s){{ selectedMateriel.reutilisable ? ', réutilisable' : '' }}
          </p>
        </div>

        <!-- Champ quantité -->
        <div v-if="selectedMateriel">
          <label class="block text-base font-semibold mb-2 text-gray-700 dark:text-gray-200">
            Quantité requise (max: {{ selectedMateriel.quantite }})
          </label>
          <input
            v-model="materielQuantite"
            type="number"
            :max="selectedMateriel.quantite"
            min="1"
            class="form-input w-full px-4 py-2 rounded-lg border border-gray-300 dark:border-gray-600 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-800 dark:text-white"
            :class="{ 'border-red-500': !isMaterielQuantiteValide }"
            required
          />
          <p v-if="!isMaterielQuantiteValide" class="mt-1 text-sm text-red-500">
            Quantité non disponible
          </p>
        </div>

        <!-- Boutons -->
        <div class="flex justify-end gap-4 pt-4">
          <button type="button" @click="showMaterielModal = false" class="px-6 py-2 rounded-lg bg-gray-200 dark:bg-gray-700 text-gray-700 dark:text-gray-200 hover:bg-gray-300 dark:hover:bg-gray-600 transition">
            Annuler
          </button>
          <button 
            type="submit" 
            :disabled="!canSubmitMateriel"
            class="px-6 py-2 rounded-lg bg-blue-600 text-white hover:bg-blue-700 transition disabled:opacity-50 disabled:cursor-not-allowed"
          >
            Ajouter
          </button>
        </div>
      </form>
    </Modal>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useRuntimeConfig } from '#app'
import { useNuxtApp } from '#app'
import { useAuth } from '~/composables/useAuth'
import { useFormatters } from '~/composables/useFormatters'
import { useModal } from '~/composables/useModal'
import { useUserInfo } from '~/composables/useUserInfo'
import Modal from '~/components/common/Modal.vue'
import MaterielList from '~/components/Projet/MaterielList.vue'

// Configuration
const config = useRuntimeConfig()
const { $axios } = useNuxtApp()
const route = useRoute()
const { user } = useAuth()
const { formatMontant, formatDate, formatStatus } = useFormatters()
const { userInfo, fetchUserInfo } = useUserInfo()

// État réactif
const tache = ref(null)
const loading = ref(true)
const error = ref(null)
const searchQuery = ref('')

// États des modals
const showMaterielModal = ref(false)
const materielListRef = ref(null)

// Variables pour le formulaire de matériel
const materielSearchQuery = ref('')
const showMaterielSuggestions = ref(false)
const selectedMateriel = ref(null)
const materielQuantite = ref(1)
const materielsDisponibles = ref([])

// Computed properties pour le formulaire de matériel
const filteredMaterielsDisponibles = computed(() => {
  console.log('Filtering matériels:', materielsDisponibles.value.length, 'query:', materielSearchQuery.value)
  
  // Si pas de matériels chargés, retourner tableau vide
  if (!materielsDisponibles.value || materielsDisponibles.value.length === 0) {
    console.log('Aucun matériel disponible')
    return []
  }
  
  // Si pas de requête, retourner tous les matériels
  if (!materielSearchQuery.value || materielSearchQuery.value.trim() === '') {
    console.log('Retourne tous les matériels:', materielsDisponibles.value.length)
    return materielsDisponibles.value
  }
  
  // Filtrer par libellé
  const query = materielSearchQuery.value.toLowerCase().trim()
  const filtered = materielsDisponibles.value.filter(materiel => 
    materiel.libelle && materiel.libelle.toLowerCase().includes(query)
  )
  console.log('Filtered résultats:', filtered.length, 'pour query:', query)
  return filtered
})

const isMaterielQuantiteValide = computed(() => {
  if (!selectedMateriel.value) return false
  return materielQuantite.value > 0 && materielQuantite.value <= selectedMateriel.value.quantite
})

const canSubmitMateriel = computed(() => {
  const canSubmit = selectedMateriel.value && isMaterielQuantiteValide.value
  console.log('canSubmitMateriel computed:', canSubmit)
  console.log('selectedMateriel.value:', !!selectedMateriel.value)
  console.log('isMaterielQuantiteValide.value:', isMaterielQuantiteValide.value)
  return canSubmit
})

// Méthodes de chargement
const fetchTache = async () => {
  try {
    loading.value = true
    console.log('Chargement de la tâche:', route.params.id)
    const response = await $axios.get(`/taches/${route.params.id}`)
    tache.value = response.data
  } catch (err) {
    console.error('Erreur lors du chargement de la tâche:', err)
    error.value = err.response?.data?.message || 'Impossible de charger les détails de la tâche.'
    if (err.response?.status === 403) {
      navigateTo('/login')
    }
  } finally {
    loading.value = false
  }
}

// Méthodes pour le formulaire de matériel
const fetchMaterielsDisponibles = async () => {
  try {
    console.log('Chargement des matériels disponibles...')
    console.log('Token disponible:', !!useCookie('token').value)
    const response = await $axios.get('/materiels')
    console.log('Matériels disponibles reçus:', response.data)
    materielsDisponibles.value = response.data
  } catch (error) {
    console.error('Erreur lors du chargement des matériels disponibles:', error)
    console.error('Status:', error.response?.status)
    console.error('Message:', error.response?.data)
  }
}

const onMaterielSearchInput = () => {
  console.log('Recherche matériel:', materielSearchQuery.value)
  console.log('Matériels disponibles:', materielsDisponibles.value.length)
  showMaterielSuggestions.value = true
}

const onMaterielSearchBlur = () => {
  setTimeout(() => {
    showMaterielSuggestions.value = false
  }, 200)
}

const selectMateriel = (materiel) => {
  if (!materiel.reutilisable && materiel.quantite <= 0) return
  selectedMateriel.value = materiel
  materielQuantite.value = 1
  showMaterielSuggestions.value = false
  materielSearchQuery.value = materiel.libelle
}

const submitMateriel = async () => {
  try {
    console.log('Début submitMateriel')
    console.log('selectedMateriel:', selectedMateriel.value)
    console.log('materielQuantite:', materielQuantite.value)
    console.log('tache.value.id:', tache.value.id)
    
    const payload = {
      materielId: selectedMateriel.value.id,
      tacheId: tache.value.id,
      quantite: materielQuantite.value
    }
    console.log('Payload matériel envoyé:', payload)
    const response = await $axios.post('/listemateriels', payload)
    console.log('Réponse API:', response.data)
    
    showMaterielModal.value = false
    // Reset form
    selectedMateriel.value = null
    materielQuantite.value = 1
    materielSearchQuery.value = ''
    // Refresh la liste des matériels
    if (materielListRef.value) materielListRef.value.loadMateriels()
    await fetchTache()
    console.log('Matériel ajouté avec succès')
  } catch (error) {
    console.error('Erreur lors de l\'ajout du matériel:', error)
    console.error('Status:', error.response?.status)
    console.error('Message:', error.response?.data)
  }
}

// Initialisation
onMounted(async () => {
  console.log('onMounted appelé');
  await fetchTache()
  await fetchUserInfo()
  console.log('Appel fetchMaterielsDisponibles...')
  await fetchMaterielsDisponibles()
  console.log('fetchMaterielsDisponibles terminé')
})

// Ajouter un watcher sur l'ID de la tâche
watch(() => route.params.id, (newId) => {
  if (newId) {
    console.log('ID de la tâche changé:', newId)
    fetchTache()
  }
})

// Computed
const canEdit = computed(() => {
  return userInfo.value?.role === 'ADMIN' || (tache.value && tache.value.projet?.chefProjet?.id === userInfo.value?.id)
})

const statusClass = computed(() => {
  if (!tache.value) return 'text-slate-600'
  const classes = {
    'EN_COURS': 'text-blue-600',
    'TERMINE': 'text-green-600',
    'EN_ATTENTE': 'text-yellow-600'
  }
  return classes[tache.value.status] || 'text-slate-600'
})

const formatPriorite = (priorite) => {
  const priorites = {
    'BASSE': 'Basse',
    'MOYENNE': 'Moyenne',
    'HAUTE': 'Haute',
    'URGENTE': 'Urgente'
  }
  return priorites[priorite] || priorite
}
</script>

<style>
@media print {
  .no-print {
    display: none !important;
  }
  
  body {
    background: white !important;
  }
  
  .container {
    max-width: none !important;
    padding: 0 !important;
  }
  
  .bg-white\/90,
  .bg-slate-50,
  .bg-white {
    background: white !important;
  }
  
  .text-slate-900,
  .text-slate-700,
  .text-slate-600,
  .text-slate-500,
  .text-slate-400 {
    color: black !important;
  }
  
  .border-slate-200,
  .border-slate-700 {
    border-color: #e5e7eb !important;
  }
  
  .shadow-lg {
    box-shadow: none !important;
  }
  
  .rounded-2xl,
  .rounded-xl,
  .rounded-lg {
    border-radius: 0 !important;
  }
}
</style>
