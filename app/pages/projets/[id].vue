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
        <div class="flex items-center space-x-4">
          <button
            @click="printPage"
            class="inline-flex items-center px-4 py-2 bg-slate-50 dark:bg-slate-800 text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-700 rounded-lg transition-all duration-200"
          >
            <svg class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 17h2a2 2 0 002-2v-4a2 2 0 00-2-2H5a2 2 0 00-2 2v4a2 2 0 002 2h2m2 4h6a2 2 0 002-2v-4a2 2 0 00-2-2H9a2 2 0 00-2 2v4a2 2 0 002 2zm8-12V5a2 2 0 00-2-2H9a2 2 0 00-2 2v4h10z" />
            </svg>
            Imprimer
          </button>
        </div>
      </div>

      <!-- Message d'erreur -->
      <div v-if="error" class="mb-8 p-4 bg-red-50 dark:bg-red-900/30 text-red-600 dark:text-red-400 rounded-xl">
        {{ error }}
      </div>

      <!-- En-tête -->
      <div v-if="projet" class="mb-8">
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
              {{ projet?.name }}
            </h1>
          </div>
          <div class="flex items-center space-x-4">
            <span 
              :class="[
                'px-4 py-2 rounded-full text-sm font-medium',
                {
                  'bg-green-100 text-green-800 dark:bg-green-900/30 dark:text-green-400': projet?.status === 'EN_COURS',
                  'bg-blue-100 text-blue-800 dark:bg-blue-900/30 dark:text-blue-400': projet?.status === 'EN_ATTENTE',
                  'bg-purple-100 text-purple-800 dark:bg-purple-900/30 dark:text-purple-400': projet?.status === 'TERMINE'
                }
              ]"
            >
              {{ formatStatus(projet?.status) }}
            </span>
            <button 
              @click="editProjet"
              class="inline-flex items-center px-4 py-2 bg-amber-50 dark:bg-amber-900/30 text-amber-600 dark:text-amber-400 hover:bg-amber-100 dark:hover:bg-amber-900/50 rounded-lg transition-all duration-200"
            >
              <svg class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
              </svg>
              Modifier
            </button>
          </div>
        </div>
      </div>

      <!-- Informations principales -->
      <div v-if="projet" class="grid grid-cols-1 md:grid-cols-2 gap-8 mb-8">
        <div class="bg-white rounded-lg shadow p-6">
          <h2 class="text-xl font-semibold mb-4">Informations générales</h2>
          <div class="space-y-4">
            <div class="flex justify-between">
              <span class="font-medium">Statut:</span>
              <span :class="statusClass">{{ formatStatus(projet?.status) }}</span>
            </div>
            <div class="flex justify-between">
              <span class="font-medium">Date de début:</span>
              <span>{{ formatDate(projet?.dateDebut) }}</span>
            </div>
            <div class="flex justify-between">
              <span class="font-medium">Date de fin:</span>
              <span>{{ formatDate(projet?.dateFin) }}</span>
            </div>
            <div class="flex justify-between">
              <span class="font-medium">Budget:</span>
              <span>{{ formatMontant(projet?.budget) }}</span>
            </div>
            <div class="flex justify-between">
              <span class="font-medium">Chef de projet:</span>
              <span>
                <template v-if="projet?.chefProjet">
                  {{ projet.chefProjet.lastName }} {{ projet.chefProjet.firstName }}
                </template>
                <template v-else>
                  Non assigné
                </template>
              </span>
            </div>
          </div>
        </div>

        <!-- Progression et statistiques -->
        <div class="bg-white rounded-lg shadow p-6">
          <h2 class="text-xl font-semibold mb-4">Progression</h2>
          <div class="space-y-4">
            <div>
              <div class="flex justify-between mb-2">
                <span>Tâches complétées</span>
                <span>{{ completionStats.taches }}%</span>
              </div>
              <div class="w-full bg-gray-200 rounded-full h-2">
                <div class="bg-blue-600 h-2 rounded-full" :style="{ width: completionStats.taches + '%' }"></div>
              </div>
            </div>
            <div>
              <div class="flex justify-between mb-2">
                <span>Budget utilisé</span>
                <span>{{ completionStats.budget }}%</span>
              </div>
              <div class="w-full bg-gray-200 rounded-full h-2">
                <div :class="budgetBarClass" class="h-2 rounded-full" :style="{ width: completionStats.budget + '%' }"></div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Blocs principaux -->
      <div v-if="projet" class="grid grid-cols-1 lg:grid-cols-2 gap-8">
        <!-- Bloc Tâches -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-6 border-b">
            <div class="flex justify-between items-center">
              <h2 class="text-xl font-semibold">Tâches</h2>
              <button
                @click="showTacheModal = true"
                class="inline-flex items-center px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition cursor-pointer"
                v-if="canEdit"
              >
                <i class="fas fa-plus mr-2"></i>
                Ajouter
              </button>
            </div>
          </div>
          <div class="p-6">
            <div class="text-xs text-gray-500 mb-2">Tâches du projet : {{ projet.taches }}</div>
            <TaskManager 
              :projet-id="projet.id" 
              :taches="projet.taches"
              :can-edit="canEdit"
              @update="refreshProjet" 
            />
          </div>
        </div>

        <!-- Bloc Matériel -->
        <div class="bg-white rounded-lg shadow">
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
            <MaterielSelector 
              :projet-id="projet.id"
              @created="closeMaterielModal"
            />
          </div>
        </div>

        <!-- Bloc Fichiers -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-6 border-b">
            <div class="flex justify-between items-center">
              <h2 class="text-xl font-semibold">Fichiers</h2>
              <button
                @click="showFichierModal = true"
                class="inline-flex items-center px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition cursor-pointer"
                v-if="canEdit"
              >
                <i class="fas fa-upload mr-2"></i>
                Ajouter
              </button>
            </div>
          </div>
          <div class="p-6">
            <FileManager 
              :projet-id="projet.id"
              :fichiers="projet.fichiers"
              :can-edit="canEdit"
              @update="refreshProjet"
            />
          </div>
        </div>

        <!-- Bloc Dépenses -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-6 border-b">
            <div class="flex justify-between items-center">
              <h2 class="text-xl font-semibold">Dépenses</h2>
              <button
                @click="showDepenseModal = true"
                class="inline-flex items-center px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition cursor-pointer"
                v-if="canEdit"
              >
                <i class="fas fa-plus mr-2"></i>
                Ajouter
              </button>
            </div>
          </div>
          <div class="p-6">
            <div class="space-y-4">
              <div v-for="depense in projet.depenses" :key="depense.id" class="flex justify-between items-center p-3 bg-gray-50 rounded">
                <div>
                  <p class="font-medium">{{ depense.libelle }}</p>
                  <p class="text-sm text-slate-600">{{ formatDate(depense.createdAt) }}</p>
                </div>
                <span class="font-semibold">{{ formatMontant(depense.montant) }}</span>
              </div>
              <div v-if="!projet.depenses?.length" class="text-center text-slate-500 py-4">
                Aucune dépense enregistrée
              </div>
            </div>
          </div>
        </div>
      </div>
      <div v-else class="text-center text-slate-500 py-12">
        Chargement du projet...
      </div>
    </div>

    <!-- Composant d'impression (caché en mode normal) -->
    <ProjetPrint v-if="projet" :projet="projet" ref="printComponent" />

    <!-- Modals -->
    <div>showTacheModal = {{ showTacheModal }}</div>
    <Modal v-model="showEditModal" title="Modifier le projet">
      <form @submit.prevent="submitEdit" class="space-y-4">
        <div>
          <label class="block text-sm font-medium mb-1">Nom</label>
          <input v-model="editForm.name" type="text" class="form-input w-full" required>
        </div>
        <div>
          <label class="block text-sm font-medium mb-1">Description</label>
          <textarea v-model="editForm.description" class="form-textarea w-full" rows="3"></textarea>
        </div>
        <div>
          <label class="block text-sm font-medium mb-1">Budget</label>
          <input v-model="editForm.budget" type="number" step="0.01" class="form-input w-full" required>
        </div>
        <div>
          <label class="block text-sm font-medium mb-1">Date de début</label>
          <input v-model="editForm.dateDebut" type="date" class="form-input w-full" required>
        </div>
        <div>
          <label class="block text-sm font-medium mb-1">Date de fin</label>
          <input v-model="editForm.dateFin" type="date" class="form-input w-full" required>
        </div>
        <div>
          <label class="block text-sm font-medium mb-1">Statut</label>
          <select v-model="editForm.status" class="form-select w-full" required>
            <option value="EN_ATTENTE">En attente</option>
            <option value="EN_COURS">En cours</option>
            <option value="TERMINE">Terminé</option>
          </select>
        </div>
        <div class="flex justify-end gap-4">
          <button type="button" @click="showEditModal = false" class="btn btn-secondary">Annuler</button>
          <button type="submit" class="btn btn-primary" :disabled="submitting">
            {{ submitting ? 'Enregistrement...' : 'Enregistrer' }}
          </button>
        </div>
      </form>
    </Modal>

    <Modal v-model="showTacheModal" title="Ajouter une tâche">
      <TaskManager 
        v-if="projet" 
        :projet-id="projet.id" 
        mode="create"
        @created="showTacheModal = false"
      />
    </Modal>

    <Modal v-model="showDepenseModal" title="Ajouter une dépense">
      <form @submit.prevent="submitDepense" class="space-y-6 bg-gray-50 dark:bg-gray-900 p-6 rounded-xl border border-gray-200 dark:border-gray-700">
        <div>
          <label class="block text-base font-semibold mb-2 text-gray-700 dark:text-gray-200">Libellé</label>
          <input v-model="depenseForm.libelle" type="text" class="form-input w-full px-4 py-2 rounded-lg border border-gray-300 dark:border-gray-600 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-800 dark:text-white" required placeholder="Saisir le libellé">
        </div>
        <div>
          <label class="block text-base font-semibold mb-2 text-gray-700 dark:text-gray-200">Montant</label>
          <input v-model="depenseForm.montant" type="number" step="0.01" class="form-input w-full px-4 py-2 rounded-lg border border-gray-300 dark:border-gray-600 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-800 dark:text-white" required placeholder="Saisir le montant">
        </div>
        <div class="flex justify-end gap-4 pt-4">
          <button type="button" @click="showDepenseModal = false" class="px-6 py-2 rounded-lg bg-gray-200 dark:bg-gray-700 text-gray-700 dark:text-gray-200 hover:bg-gray-300 dark:hover:bg-gray-600 transition">Annuler</button>
          <button type="submit" class="px-6 py-2 rounded-lg bg-blue-600 text-white hover:bg-blue-700 transition">Enregistrer</button>
        </div>
      </form>
    </Modal>

    <Modal v-model="showMaterielModal" title="Ajouter du matériel">
      <MaterielSelector 
        v-if="projet" 
        :projet-id="projet.id"
        @created="showMaterielModal = false"
      />
    </Modal>

    <Modal v-model="showFichierModal" title="Ajouter un fichier">
      <FileModal 
        v-if="projet" 
        :projet-id="projet.id"
        @uploaded="showFichierModal = false"
      />
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
import ProjetPrint from '~/components/Projet/ProjetPrint.vue'
import Modal from '~/components/common/Modal.vue'

// Configuration
const config = useRuntimeConfig()
const { $axios } = useNuxtApp()
const route = useRoute()
const { user } = useAuth()
const { formatMontant, formatDate, formatStatus } = useFormatters()
const { userInfo, fetchUserInfo } = useUserInfo()

// État réactif
const projet = ref(null)
const projets = ref([])
const loading = ref(true)
const error = ref(null)
const searchQuery = ref('')
const projetsError = ref('')

// États des modals
const showTacheModal = ref(false)
const showDepenseModal = ref(false)
const showMaterielModal = ref(false)
const showFichierModal = ref(false)
const showFileModal = ref(false)
const selectedFile = ref(null)
const showEditModal = ref(false)
const submitting = ref(false)

// Formulaires
const tacheForm = ref({
  titre: '',
  description: '',
  status: 'EN_ATTENTE',
  dateEcheance: '',
  projetId: null
})

const depenseForm = ref({
  libelle: '',
  montant: '',
  projetId: null
})

const materielForm = ref({
  nom: '',
  quantite: 1,
  prix: 0
})

const fichierForm = ref({
  nom: '',
  file: null
})

const editForm = ref({
  name: '',
  description: '',
  budget: 0,
  dateDebut: '',
  dateFin: '',
  status: 'EN_COURS'
})

// Méthodes de recherche
const filteredTaches = computed(() => {
  if (!projet.value?.taches) return []
  if (!searchQuery.value) return projet.value.taches
  
  const query = searchQuery.value.toLowerCase()
  return projet.value.taches.filter(tache => 
    tache.titre.toLowerCase().includes(query) ||
    tache.description.toLowerCase().includes(query)
  )
})

const filteredDepenses = computed(() => {
  if (!projet.value?.depenses) return []
  if (!searchQuery.value) return projet.value.depenses
  
  const query = searchQuery.value.toLowerCase()
  return projet.value.depenses.filter(depense => 
    depense.libelle.toLowerCase().includes(query)
  )
})

const filteredMateriels = computed(() => {
  if (!projet.value?.materiels) return []
  if (!searchQuery.value) return projet.value.materiels
  
  const query = searchQuery.value.toLowerCase()
  return projet.value.materiels.filter(materiel => 
    materiel.nom.toLowerCase().includes(query)
  )
})

const filteredFichiers = computed(() => {
  if (!projet.value?.fichiers) return []
  if (!searchQuery.value) return projet.value.fichiers
  
  const query = searchQuery.value.toLowerCase()
  return projet.value.fichiers.filter(fichier => 
    fichier.nom.toLowerCase().includes(query)
  )
})

// Méthodes de chargement
const fetchProjet = async () => {
  try {
    loading.value = true
    console.log('Chargement du projet:', route.params.id)
    const response = await $axios.get(`/projets/${route.params.id}`)
    projet.value = response.data
  } catch (err) {
    console.error('Erreur lors du chargement du projet:', err)
    error.value = err.response?.data?.message || 'Impossible de charger les détails du projet.'
    if (err.response?.status === 403) {
      navigateTo('/login')
    }
  } finally {
    loading.value = false
  }
}

const loadDepenses = async () => {
  try {
    const response = await $axios.get(`/depenses`, {
      params: { projetId: route.params.id }
    })
    if (projet.value) projet.value.depenses = response.data
  } catch (err) {
    console.error('Erreur lors du chargement des dépenses:', err)
    if (!projet.value) {
      if (err.response?.status === 403) {
        navigateTo('/login')
      }
    } else {
      error.value = "Impossible de charger les dépenses."
    }
  }
}

const loadMateriels = async () => {
  try {
    const response = await $axios.get(`/listemateriels`, {
      params: { projetId: route.params.id }
    })
    if (projet.value) projet.value.materiels = response.data
  } catch (err) {
    console.error('Erreur lors du chargement du matériel:', err)
    if (!projet.value) {
      if (err.response?.status === 403) {
        navigateTo('/login')
      }
    } else {
      error.value = "Impossible de charger le matériel."
    }
  }
}

const loadFichiers = async () => {
  try {
    const response = await $axios.get(`/fichiers`, {
      params: { projetId: route.params.id }
    })
    if (projet.value) projet.value.fichiers = response.data
  } catch (err) {
    console.error('Erreur lors du chargement des fichiers:', err)
    if (!projet.value) {
      if (err.response?.status === 403) {
        navigateTo('/login')
      }
    } else {
      error.value = "Impossible de charger les fichiers."
    }
  }
}

// Charger tous les projets pour le select du modal dépense
const fetchAllProjets = async () => {
  console.log('fetchAllProjets appelé');
  try {
    const response = await $axios.get('/projets')
    projets.value = response.data
    projetsError.value = ''
    console.log('Projets chargés:', projets.value)
  } catch (err) {
    projetsError.value = 'Erreur lors du chargement des projets : ' + (err.response?.status || '') + ' ' + (err.response?.data?.message || err.message);
    console.error('Erreur lors du chargement de la liste des projets:', err);
  }
}

// Méthodes des modals
const openAddTacheModal = () => {
  tacheForm.value = {
    titre: '',
    description: '',
    status: 'EN_ATTENTE',
    dateEcheance: '',
    projetId: route.params.id
  }
  showTacheModal.value = true
}

const openAddDepenseModal = () => {
  depenseForm.value = {
    libelle: '',
    montant: '',
    projetId: route.params.id
  }
  showDepenseModal.value = true
}

const openAddMaterielModal = () => {
  materielForm.value = {
    nom: '',
    quantite: 1,
    prix: 0
  }
  showMaterielModal.value = true
}

const openAddFichierModal = () => {
  fichierForm.value = {
    nom: '',
    file: null
  }
  showFichierModal.value = true
}

// Méthodes de soumission
const submitTache = async () => {
  try {
    tacheForm.value.projetId = projet.value.id
    console.log('Payload envoyé pour création tâche:', tacheForm.value)
    const response = await $axios.post('/taches', tacheForm.value)
    console.log('Réponse API création tâche:', response.data)
    showTacheModal.value = false
    await fetchProjet()
  } catch (error) {
    console.error('Erreur lors de l\'ajout de la tâche:', error)
  }
}

const submitDepense = async () => {
  try {
    // Construction du payload attendu par le backend
    const payload = {
      libelle: depenseForm.value.libelle,
      montant: depenseForm.value.montant,
      projetId: projet.value.id
    }
    console.log('Payload dépense envoyé:', payload)
    const response = await $axios.post('/depenses', payload)
    showDepenseModal.value = false
    await fetchProjet()
  } catch (error) {
    console.error('Erreur lors de l\'ajout de la dépense:', error)
  }
}

const submitMateriel = async () => {
  try {
    const response = await $axios.post(`/projets/${route.params.id}/materiels`, materielForm.value)
    showMaterielModal.value = false
    await fetchProjet()
  } catch (error) {
    console.error('Erreur lors de l\'ajout du matériel:', error)
  }
}

const handleFileUpload = (event) => {
  fichierForm.value.file = event.target.files[0]
}

const submitFichier = async () => {
  try {
    const formData = new FormData()
    formData.append('nom', fichierForm.value.nom)
    formData.append('file', fichierForm.value.file)
    const response = await $axios.post(`/projets/${route.params.id}/fichiers`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
    showFichierModal.value = false
    await fetchProjet()
  } catch (error) {
    console.error('Erreur lors de l\'ajout du fichier:', error)
  }
}

// Méthode d'impression
const printPage = () => {
  window.print()
}

const editProjet = () => {
  editForm.value = {
    name: projet.value.name,
    description: projet.value.description,
    budget: projet.value.budget,
    dateDebut: projet.value.dateDebut ? new Date(projet.value.dateDebut).toISOString().split('T')[0] : '',
    dateFin: projet.value.dateFin ? new Date(projet.value.dateFin).toISOString().split('T')[0] : '',
    status: projet.value.status
  }
  showEditModal.value = true
}

const submitEdit = async () => {
  submitting.value = true
  try {
    const response = await $axios.put(`/projets/${route.params.id}`, editForm.value)
    projet.value = response.data
    showEditModal.value = false
  } catch (error) {
    console.error('Erreur lors de la modification:', error)
  } finally {
    submitting.value = false
  }
}

// Handlers pour les événements des composants
const handleTaskAdded = (task) => {
  if (!projet.value.taches) projet.value.taches = []
  projet.value.taches.push(task)
}

const handleTaskUpdated = (task) => {
  const index = projet.value.taches.findIndex(t => t.id === task.id)
  if (index !== -1) {
    projet.value.taches[index] = task
  }
}

const handleTaskDeleted = (taskId) => {
  projet.value.taches = projet.value.taches.filter(t => t.id !== taskId)
}

const handleMaterielAdded = (materiel) => {
  if (!projet.value.materiels) projet.value.materiels = []
  projet.value.materiels.push(materiel)
}

const handleFileAdded = (file) => {
  if (!projet.value.fichiers) projet.value.fichiers = []
  projet.value.fichiers.push(file)
}

const handleFileDeleted = (fileId) => {
  projet.value.fichiers = projet.value.fichiers.filter(f => f.id !== fileId)
}

// Initialisation
onMounted(async () => {
  console.log('onMounted appelé');
  await fetchProjet()
  await fetchUserInfo()
  await fetchAllProjets()

  if (projet.value) {
    loadMateriels()
    loadFichiers()
  }
})

// Ajouter un watcher sur l'ID du projet
watch(() => route.params.id, (newId) => {
  if (newId) {
    console.log('ID du projet changé:', newId)
    fetchProjet()
  }
})

// Computed
const canEdit = computed(() => {
  return userInfo.value?.role === 'ADMIN' || (projet.value && projet.value.chefProjet?.id === userInfo.value?.id)
})

const statusClass = computed(() => {
  if (!projet.value) return 'text-slate-600'
  const classes = {
    'EN_COURS': 'text-blue-600',
    'TERMINE': 'text-green-600',
    'EN_ATTENTE': 'text-yellow-600'
  }
  return classes[projet.value.status] || 'text-slate-600'
})

const budgetBarClass = computed(() => {
  if (!projet.value) return 'bg-green-600'
  const percentage = completionStats.value.budget
  if (percentage > 90) return 'bg-red-600'
  if (percentage > 70) return 'bg-yellow-600'
  return 'bg-green-600'
})

const completionStats = computed(() => {
  if (!projet.value?.taches?.length) return { taches: 0, budget: 0 }
  const tachesCompletes = projet.value.taches.filter(t => t.status === 'TERMINE').length
  const totalTaches = projet.value.taches.length
  const depensesTotal = (projet.value.depenses || []).reduce((sum, d) => sum + d.montant, 0)
  const materielTotal = (projet.value.materiels || []).reduce((sum, m) => sum + (m.prix * m.quantite), 0)
  const totalDepense = depensesTotal + materielTotal
  return {
    taches: Math.round((tachesCompletes / totalTaches) * 100),
    budget: projet.value.budget ? Math.round((totalDepense / projet.value.budget) * 100) : 0
  }
})

const refreshProjet = () => {
  fetchProjet()
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