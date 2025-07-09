<template>
  <div class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-2xl shadow-lg border border-white/20 dark:border-gray-700/50 p-6">
    <div class="flex items-center justify-between mb-4">
      <h2 class="text-xl font-semibold text-slate-900 dark:text-slate-100">Tâches</h2>
      <button 
        @click="openTaskModal"
        class="inline-flex items-center px-4 py-2 bg-blue-50 dark:bg-blue-900/30 text-blue-600 dark:text-blue-400 hover:bg-blue-100 dark:hover:bg-blue-900/50 rounded-lg transition-all duration-200"
      >
        <svg class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
        </svg>
        Ajouter une tâche
      </button>
    </div>

    <!-- Liste des tâches -->
    <div class="space-y-4">
      <div v-for="tache in taches" 
        :key="tache.id" 
        class="bg-slate-50 dark:bg-gray-700/50 rounded-xl p-4 group"
      >
        <div class="flex items-center justify-between mb-2">
          <div class="flex items-center space-x-3">
            <h3 class="font-medium text-slate-900 dark:text-slate-100">{{ tache.titre }}</h3>
            <span 
              :class="[
                'px-3 py-1 rounded-full text-sm font-medium',
                {
                  'bg-green-100 text-green-800 dark:bg-green-900/30 dark:text-green-400': tache.status === 'EN_COURS',
                  'bg-blue-100 text-blue-800 dark:bg-blue-900/30 dark:text-blue-400': tache.status === 'EN_ATTENTE',
                  'bg-purple-100 text-purple-800 dark:bg-purple-900/30 dark:text-purple-400': tache.status === 'TERMINE'
                }
              ]"
            >
              {{ formatStatus(tache.status) }}
            </span>
          </div>
          <div class="flex items-center space-x-2 opacity-0 group-hover:opacity-100 transition-opacity">
            <button 
              @click="editTask(tache)"
              class="p-2 text-amber-600 hover:text-amber-800 dark:text-amber-400 dark:hover:text-amber-300 hover:bg-amber-50 dark:hover:bg-amber-900/20 rounded-lg transition-all duration-200"
            >
              <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
              </svg>
            </button>
            <button 
              @click="deleteTask(tache)"
              class="p-2 text-red-600 hover:text-red-800 dark:text-red-400 dark:hover:text-red-300 hover:bg-red-50 dark:hover:bg-red-900/20 rounded-lg transition-all duration-200"
            >
              <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
              </svg>
            </button>
          </div>
        </div>
        
        <p class="text-sm text-slate-600 dark:text-slate-400 mb-3">{{ tache.description }}</p>
        
        <div class="flex flex-wrap items-center gap-4 text-sm">
          <div class="flex items-center space-x-2">
            <svg class="h-4 w-4 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <span class="text-slate-600 dark:text-slate-400">{{ formatDate(tache.dateEcheance) }}</span>
          </div>
          
          <div class="flex items-center space-x-2">
            <svg class="h-4 w-4 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
            </svg>
            <span class="text-slate-600 dark:text-slate-400">
              {{ tache.membre?.name || 'Non assigné' }}
              <button 
                v-if="!tache.membre"
                @click="assignTask(tache)"
                class="ml-2 text-blue-600 hover:text-blue-800 dark:text-blue-400 dark:hover:text-blue-300"
              >
                Assigner
              </button>
            </span>
          </div>
        </div>
      </div>

      <!-- Message si aucune tâche -->
      <div v-if="taches.length === 0" class="text-center py-8">
        <svg class="mx-auto h-12 w-12 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
        </svg>
        <p class="mt-2 text-slate-600 dark:text-slate-400">Aucune tâche</p>
      </div>
    </div>

    <!-- Modal Tâche -->
    <div v-if="showTaskModal" class="fixed inset-0 z-50 overflow-y-auto">
      <div class="flex items-center justify-center min-h-screen px-4">
        <div class="fixed inset-0 bg-black/50 backdrop-blur-sm" @click="showTaskModal = false"></div>
        <div class="relative bg-white dark:bg-gray-800 rounded-2xl shadow-xl max-w-lg w-full p-6">
          <h3 class="text-xl font-semibold text-slate-900 dark:text-slate-100 mb-4">
            {{ isEditing ? 'Modifier la tâche' : 'Ajouter une tâche' }}
          </h3>
          <form @submit.prevent="submitTask" class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">Titre</label>
              <input
                v-model="taskForm.titre"
                type="text"
                required
                class="w-full px-4 py-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
            
            <div>
              <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">Description</label>
              <textarea
                v-model="taskForm.description"
                rows="3"
                class="w-full px-4 py-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500"
              ></textarea>
            </div>
            
            <div>
              <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">Statut</label>
              <select
                v-model="taskForm.status"
                required
                class="w-full px-4 py-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="EN_ATTENTE">En attente</option>
                <option value="EN_COURS">En cours</option>
                <option value="TERMINE">Terminé</option>
              </select>
            </div>
            
            <div>
              <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">Date d'échéance</label>
              <input
                v-model="taskForm.dateEcheance"
                type="date"
                required
                class="w-full px-4 py-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div v-if="membres.length > 0">
              <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">Assigner à</label>
              <select
                v-model="taskForm.membreId"
                class="w-full px-4 py-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Non assigné</option>
                <option v-for="membre in membres" :key="membre.id" :value="membre.id">
                  {{ membre.name }}
                </option>
              </select>
            </div>

            <div class="flex justify-end space-x-4 mt-6">
              <button
                type="button"
                @click="showTaskModal = false"
                class="px-4 py-2 text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-700 rounded-lg transition-all duration-200"
              >
                Annuler
              </button>
              <button
                type="submit"
                :disabled="submitting"
                class="px-4 py-2 bg-blue-600 text-white hover:bg-blue-700 rounded-lg transition-all duration-200 disabled:opacity-50"
              >
                {{ submitting ? 'Enregistrement...' : (isEditing ? 'Modifier' : 'Ajouter') }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- Modal Assignation -->
    <div v-if="showAssignModal" class="fixed inset-0 z-50 overflow-y-auto">
      <div class="flex items-center justify-center min-h-screen px-4">
        <div class="fixed inset-0 bg-black/50 backdrop-blur-sm" @click="showAssignModal = false"></div>
        <div class="relative bg-white dark:bg-gray-800 rounded-2xl shadow-xl max-w-lg w-full p-6">
          <h3 class="text-xl font-semibold text-slate-900 dark:text-slate-100 mb-4">
            Assigner la tâche
          </h3>
          <div class="space-y-4">
            <div v-if="membres.length > 0">
              <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">Sélectionner un membre</label>
              <select
                v-model="selectedMembre"
                class="w-full px-4 py-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Sélectionner un membre</option>
                <option v-for="membre in membres" :key="membre.id" :value="membre.id">
                  {{ membre.name }}
                </option>
              </select>
            </div>
            <div v-else class="text-center py-4">
              <p class="text-slate-600 dark:text-slate-400">Aucun membre disponible</p>
            </div>

            <div class="flex justify-end space-x-4 mt-6">
              <button
                type="button"
                @click="showAssignModal = false"
                class="px-4 py-2 text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-700 rounded-lg transition-all duration-200"
              >
                Annuler
              </button>
              <button
                @click="confirmAssign"
                :disabled="!selectedMembre"
                class="px-4 py-2 bg-blue-600 text-white hover:bg-blue-700 rounded-lg transition-all duration-200 disabled:opacity-50"
              >
                Assigner
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRuntimeConfig } from '#app'
import { useNuxtApp } from '#app'
import { useFormatters } from '~/composables/useFormatters'

const config = useRuntimeConfig()
const { $axios } = useNuxtApp()
const { formatDate } = useFormatters()

const props = defineProps({
  projetId: {
    type: String,
    required: true
  }
})

const emit = defineEmits(['task-added', 'task-updated', 'task-deleted'])

// État
const taches = ref([])
const membres = ref([])
const showTaskModal = ref(false)
const showAssignModal = ref(false)
const isEditing = ref(false)
const submitting = ref(false)
const selectedMembre = ref('')
const selectedTask = ref(null)

// Formulaire
const taskForm = ref({
  titre: '',
  description: '',
  status: 'EN_ATTENTE',
  dateEcheance: '',
  membreId: '',
  projetId: props.projetId
})

// Méthodes
const loadTasks = async () => {
  try {
    const response = await $axios.get(`/projets/${props.projetId}/taches`)
    taches.value = response.data
  } catch (error) {
    console.error('Erreur lors du chargement des tâches:', error)
  }
}

const loadMembres = async () => {
  try {
    const response = await $axios.get('/users')
    membres.value = response.data
  } catch (error) {
    console.error('Erreur lors du chargement des membres:', error)
  }
}

const openTaskModal = () => {
  isEditing.value = false
  taskForm.value = {
    titre: '',
    description: '',
    status: 'EN_ATTENTE',
    dateEcheance: '',
    membreId: '',
    projetId: props.projetId
  }
  showTaskModal.value = true
}

const editTask = (tache) => {
  isEditing.value = true
  taskForm.value = {
    ...tache,
    membreId: tache.membre?.id || '',
    projetId: props.projetId
  }
  showTaskModal.value = true
}

const submitTask = async () => {
  submitting.value = true
  try {
    let response
    if (isEditing.value) {
      response = await $axios.put(`/taches/${taskForm.value.id}`, taskForm.value)
      const index = taches.value.findIndex(t => t.id === taskForm.value.id)
      if (index !== -1) {
        taches.value[index] = response.data
      }
      emit('task-updated', response.data)
    } else {
      response = await $axios.post('/taches', taskForm.value)
      taches.value.push(response.data)
      emit('task-added', response.data)
    }
    showTaskModal.value = false
  } catch (error) {
    console.error('Erreur lors de la soumission:', error)
  } finally {
    submitting.value = false
  }
}

const deleteTask = async (tache) => {
  if (!confirm(`Êtes-vous sûr de vouloir supprimer la tâche "${tache.titre}" ?`)) return

  try {
    await $axios.delete(`/taches/${tache.id}`)
    taches.value = taches.value.filter(t => t.id !== tache.id)
    emit('task-deleted', tache.id)
  } catch (error) {
    console.error('Erreur lors de la suppression:', error)
  }
}

const assignTask = (tache) => {
  selectedTask.value = tache
  selectedMembre.value = ''
  showAssignModal.value = true
}

const confirmAssign = async () => {
  if (!selectedTask.value || !selectedMembre.value) return

  try {
    const response = await $axios.put(`/taches/${selectedTask.value.id}/assign/${selectedMembre.value}`)
    const index = taches.value.findIndex(t => t.id === selectedTask.value.id)
    if (index !== -1) {
      taches.value[index] = response.data
    }
    showAssignModal.value = false
    emit('task-updated', response.data)
  } catch (error) {
    console.error('Erreur lors de l\'assignation:', error)
  }
}

const formatStatus = (status) => {
  const statusMap = {
    'EN_COURS': 'En cours',
    'TERMINE': 'Terminé',
    'EN_ATTENTE': 'En attente'
  }
  return statusMap[status] || status
}

// Initialisation
onMounted(() => {
  loadTasks()
  loadMembres()
})
</script> 