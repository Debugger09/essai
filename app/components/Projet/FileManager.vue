<template>
  <div class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-2xl shadow-lg border border-white/20 dark:border-gray-700/50 p-6">
    <div class="flex items-center justify-between mb-4">
      <h2 class="text-xl font-semibold text-slate-900 dark:text-slate-100">Fichiers</h2>
      <button 
        @click="openFileModal"
        class="inline-flex items-center px-4 py-2 bg-blue-50 dark:bg-blue-900/30 text-blue-600 dark:text-blue-400 hover:bg-blue-100 dark:hover:bg-blue-900/50 rounded-lg transition-all duration-200"
      >
        <svg class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
        </svg>
        Ajouter
      </button>
    </div>

    <!-- Liste des fichiers -->
    <div class="space-y-4">
      <div v-for="fichier in props.fichiers" 
        :key="fichier.id" 
        class="flex items-center justify-between p-3 bg-slate-50 dark:bg-gray-700/50 rounded-xl group"
      >
        <div class="flex items-center space-x-3">
          <div class="p-2 bg-white dark:bg-gray-800 rounded-lg">
            <svg class="h-6 w-6 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 21h10a2 2 0 002-2V9.414a1 1 0 00-.293-.707l-5.414-5.414A1 1 0 0012.586 3H7a2 2 0 00-2 2v14a2 2 0 002 2z" />
            </svg>
          </div>
          <div>
            <h3 class="font-medium text-slate-900 dark:text-slate-100">{{ fichier.nom }}</h3>
            <p class="text-sm text-slate-500">{{ formatDate(fichier.createdAt) }}</p>
          </div>
        </div>
        <div class="flex items-center space-x-2 opacity-0 group-hover:opacity-100 transition-opacity">
          <button 
            @click="downloadFile(fichier)"
            class="p-2 text-blue-600 hover:text-blue-800 dark:text-blue-400 dark:hover:text-blue-300 hover:bg-blue-50 dark:hover:bg-blue-900/20 rounded-lg transition-all duration-200"
            title="Télécharger"
          >
            <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4" />
            </svg>
          </button>
          <button 
            @click="deleteFile(fichier)"
            class="p-2 text-red-600 hover:text-red-800 dark:text-red-400 dark:hover:text-red-300 hover:bg-red-50 dark:hover:bg-red-900/20 rounded-lg transition-all duration-200"
            title="Supprimer"
          >
            <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
            </svg>
          </button>
        </div>
      </div>

      <!-- Message si aucun fichier -->
      <div v-if="props.fichiers.length === 0" class="text-center py-8">
        <svg class="mx-auto h-12 w-12 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 21h10a2 2 0 002-2V9.414a1 1 0 00-.293-.707l-5.414-5.414A1 1 0 0012.586 3H7a2 2 0 00-2 2v14a2 2 0 002 2z" />
        </svg>
        <p class="mt-2 text-slate-600 dark:text-slate-400">Aucun fichier</p>
      </div>
    </div>

    <!-- Modal d'ajout de fichier -->
    <div v-if="showFileModal" class="fixed inset-0 z-50 overflow-y-auto">
      <div class="flex items-center justify-center min-h-screen px-4">
        <div class="fixed inset-0 bg-black/50 backdrop-blur-sm" @click="showFileModal = false"></div>
        <div class="relative bg-white dark:bg-gray-800 rounded-2xl shadow-xl max-w-lg w-full p-6">
          <h3 class="text-xl font-semibold text-slate-900 dark:text-slate-100 mb-4">Ajouter un fichier</h3>
          <form @submit.prevent="uploadFile" class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">Nom du fichier</label>
              <input
                v-model="fileForm.nom"
                type="text"
                required
                class="w-full px-4 py-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">Fichier</label>
              <input
                type="file"
                @change="handleFileSelect"
                required
                class="w-full px-4 py-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
            <div class="flex justify-end space-x-4 mt-6">
              <button
                type="button"
                @click="showFileModal = false"
                class="px-4 py-2 text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-700 rounded-lg transition-all duration-200"
              >
                Annuler
              </button>
              <button
                type="submit"
                :disabled="uploading"
                class="px-4 py-2 bg-blue-600 text-white hover:bg-blue-700 rounded-lg transition-all duration-200 disabled:opacity-50"
              >
                {{ uploading ? 'Envoi en cours...' : 'Ajouter' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
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
  },
  fichiers: {
    type: Array,
    default: []
  }
})

const emit = defineEmits(['file-added', 'file-deleted', 'update'])

// État
const showFileModal = ref(false)
const uploading = ref(false)
const fileForm = ref({
  nom: '',
  file: null
})

// Méthodes
const openFileModal = () => {
  fileForm.value = { nom: '', file: null }
  showFileModal.value = true
}

const handleFileSelect = (event) => {
  fileForm.value.file = event.target.files[0]
  if (!fileForm.value.nom && fileForm.value.file) {
    fileForm.value.nom = fileForm.value.file.name
  }
}

const uploadFile = async () => {
  if (!fileForm.value.file) return

  uploading.value = true
  try {
    const formData = new FormData()
    formData.append('nom', fileForm.value.nom)
    formData.append('file', fileForm.value.file)
    formData.append('projetId', props.projetId)

    const response = await $axios.post(`/fichiers`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })

    emit('update')
    showFileModal.value = false
  } catch (error) {
    console.error('Erreur lors de l\'upload du fichier:', error)
  } finally {
    uploading.value = false
  }
}

const downloadFile = async (fichier) => {
  try {
    const response = await $axios.get(`/fichiers/${fichier.id}/download`, {
      responseType: 'blob'
    })

    const url = window.URL.createObjectURL(new Blob([response.data]))
    const link = document.createElement('a')
    link.href = url
    link.setAttribute('download', fichier.nom)
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
  } catch (error) {
    console.error('Erreur lors du téléchargement:', error)
  }
}

const deleteFile = async (fichier) => {
  if (!confirm(`Êtes-vous sûr de vouloir supprimer le fichier "${fichier.nom}" ?`)) return

  try {
    await $axios.delete(`/fichiers/${fichier.id}`)
    emit('update')
  } catch (error) {
    console.error('Erreur lors de la suppression:', error)
  }
}
</script> 