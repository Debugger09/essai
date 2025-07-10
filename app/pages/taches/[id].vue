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
              to="/taches"
              class="inline-flex items-center text-white bg-blue-600 hover:bg-blue-700 px-4 py-2 rounded-lg shadow font-semibold mb-4 transition"
            >
              <svg class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
              </svg>
              Retour aux tâches
            </NuxtLink>
            <h1 class="text-3xl sm:text-4xl font-bold text-blue-900 dark:text-blue-200 mt-2">
              {{ tache?.titre }}
            </h1>
          </div>
          <div class="flex items-center space-x-4">
            <span 
              :class="[
                'px-4 py-2 rounded-full text-sm font-medium shadow',
                {
                  'bg-green-100 text-green-800 border border-green-300': tache?.statutTache === 'EN_COURS',
                  'bg-blue-100 text-blue-800 border border-blue-300': tache?.statutTache === 'A_FAIRE',
                  'bg-purple-100 text-purple-800 border border-purple-300': tache?.statutTache === 'TERMINEE'
                }
              ]"
            >
              {{ formatStatus(tache?.statutTache) }}
            </span>
            <span 
              :class="[
                'px-3 py-1 rounded-full text-xs font-semibold shadow',
                {
                  'bg-red-100 text-red-700 border border-red-300': tache?.priorite === 'ELEVEE',
                  'bg-yellow-100 text-yellow-700 border border-yellow-300': tache?.priorite === 'MOYENNE',
                  'bg-blue-100 text-blue-700 border border-blue-300': tache?.priorite === 'FAIBLE'
                }
              ]"
            >
              {{ formatPriorite(tache?.priorite) }}
            </span>
          </div>
        </div>
      </div>

      <!-- Informations principales -->
      <div v-if="tache" class="grid grid-cols-1 md:grid-cols-2 gap-8 mb-8">
        <div class="bg-white rounded-2xl shadow-lg p-8 border border-blue-100">
          <h2 class="text-xl font-bold mb-4 text-blue-800">Informations générales</h2>
          <div class="space-y-4">
            <div class="flex justify-between">
              <span class="font-medium text-slate-700">Statut:</span>
              <span :class="statusClass">{{ formatStatus(tache?.statutTache) }}</span>
            </div>
            <div class="flex justify-between">
              <span class="font-medium text-slate-700">Priorité:</span>
              <span>{{ formatPriorite(tache?.priorite) }}</span>
            </div>
            <div class="flex justify-between">
              <span class="font-medium text-slate-700">Date d'échéance:</span>
              <span>{{ formatDate(tache?.dateEcheance) }}</span>
            </div>
            <div class="flex justify-between">
              <span class="font-medium text-slate-700">Projet lié :</span>
              <span class="font-semibold text-blue-700">{{ tache?.projet?.libelle || tache?.projet?.name || 'Non assigné' }}</span>
            </div>
            <div class="flex flex-col gap-2">
              <span class="font-medium text-slate-700">Membres assignés :</span>
              <ul class="pl-4 list-disc">
                <li v-for="membre in tache.membres" :key="membre.id" class="text-slate-800">
                  {{ membre.firstName }} {{ membre.lastName }}
                </li>
              </ul>
            </div>
            <div v-if="user.value && tache.membres && tache.membres.some(m => m.id === user.value.id)" class="mt-4 flex gap-4 items-center">
              <button
                v-if="tache.statutTache === 'A_FAIRE'"
                @click="changerStatutDirect('EN_COURS')"
                :disabled="statutLoading"
                class="px-5 py-2 rounded-lg bg-yellow-400 text-white font-semibold shadow hover:bg-yellow-500 transition disabled:opacity-50"
              >
                Commencer la tâche
              </button>
              <button
                v-else-if="tache.statutTache === 'EN_COURS'"
                @click="changerStatutDirect('TERMINEE')"
                :disabled="statutLoading"
                class="px-5 py-2 rounded-lg bg-green-600 text-white font-semibold shadow hover:bg-green-700 transition disabled:opacity-50"
              >
                Terminer la tâche
              </button>
              <label class="block font-medium mb-1">Changer le statut :</label>
              <select v-model="nouveauStatut" class="px-3 py-2 rounded border border-gray-300">
                <option value="A_FAIRE">À faire</option>
                <option value="EN_COURS">En cours</option>
                <option value="TERMINEE">Terminée</option>
              </select>
              <button @click="changerStatut" :disabled="statutLoading" class="ml-2 px-4 py-2 rounded bg-blue-600 text-white hover:bg-blue-700 disabled:opacity-50">
                Mettre à jour
              </button>
              <span v-if="statutSuccess" class="ml-2 text-green-600">Statut mis à jour !</span>
              <span v-if="statutError" class="ml-2 text-red-600">Erreur : {{ statutError }}</span>
            </div>
          </div>
        </div>
        <div class="bg-white rounded-2xl shadow-lg p-8 border border-blue-100">
          <h2 class="text-xl font-bold mb-4 text-blue-800">Description</h2>
          <p class="text-gray-700 dark:text-gray-300">{{ tache?.description || 'Aucune description' }}</p>
        </div>
      </div>

      <!-- Bloc Matériel supprimé -->
      <div v-else class="text-center text-slate-500 py-12">
        Chargement de la tâche...
      </div>
    </div>
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

const nouveauStatut = ref("")
const statutLoading = ref(false)
const statutSuccess = ref(false)
const statutError = ref("")

watch(tache, (val) => {
  if (val) {
    nouveauStatut.value = val.statutTache
  }
})

const changerStatut = async () => {
  statutLoading.value = true
  statutSuccess.value = false
  statutError.value = ""
  try {
    await $axios.put(`/taches/${tache.value.id}`, {
      ...tache.value,
      statutTache: nouveauStatut.value
    })
    statutSuccess.value = true
    await fetchTache()
  } catch (e) {
    statutError.value = e.response?.data?.message || 'Erreur lors de la mise à jour.'
  } finally {
    statutLoading.value = false
    setTimeout(() => { statutSuccess.value = false }, 2000)
  }
}

const changerStatutDirect = async (statut) => {
  nouveauStatut.value = statut
  await changerStatut()
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
