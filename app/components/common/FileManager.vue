<template>
  <div class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-2xl shadow-lg border border-blue-200 dark:border-blue-700 p-4 sm:p-6 min-h-[220px] flex flex-col">
    <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between mb-4 gap-2">
      <h2 class="text-lg sm:text-xl font-semibold text-slate-900 dark:text-slate-100">Fichiers</h2>
      <button v-if="canEdit" @click="openFileModal" class="inline-flex items-center justify-center px-4 py-2 w-full sm:w-auto bg-blue-50 dark:bg-blue-900/30 text-blue-600 dark:text-blue-400 hover:bg-blue-100 dark:hover:bg-blue-900/50 rounded-lg transition-all duration-200 text-base sm:text-sm">
        <svg class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor" aria-hidden="true">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
        </svg>
        Ajouter
      </button>
    </div>
    <div class="flex-1 space-y-4">
      <div v-for="file in files" :key="file.id" class="flex flex-col sm:flex-row items-center justify-between p-3 bg-slate-50 dark:bg-gray-700/50 rounded-xl group gap-2">
        <div class="flex items-center space-x-3 w-full min-w-0">
          <div class="p-2 bg-white dark:bg-gray-800 rounded-lg flex-shrink-0">
            <component :is="fileIcon(file.type)" class="h-6 w-6 text-slate-400" aria-label="Type de fichier" />
          </div>
          <div class="flex-1 min-w-0">
            <h3 class="font-medium text-slate-900 dark:text-slate-100 truncate">{{ file.nom }}</h3>
            <div class="flex flex-wrap gap-2 text-xs text-slate-500 mt-1">
              <span>{{ formatDate(file.dateAjout || file.createdAt) }}</span>
              <span v-if="file.taille">• {{ formatSize(file.taille) }}</span>
              <span v-if="file.type">• {{ file.type }}</span>
            </div>
          </div>
        </div>
        <div class="flex flex-row items-center gap-2 mt-2 sm:mt-0 ml-auto">
          <button @click="downloadFile(file)" class="p-2 text-blue-600 hover:text-blue-800 dark:text-blue-400 dark:hover:text-blue-300 hover:bg-blue-50 dark:hover:bg-blue-900/20 rounded-lg transition-all duration-200" title="Télécharger" aria-label="Télécharger le fichier">
            <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4" />
            </svg>
          </button>
          <button v-if="canEdit" @click="deleteFile(file)" class="p-2 text-red-600 hover:text-red-800 dark:text-red-400 dark:hover:text-red-300 hover:bg-red-50 dark:hover:bg-red-900/20 rounded-lg transition-all duration-200" title="Supprimer" aria-label="Supprimer le fichier">
            <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
            </svg>
          </button>
        </div>
      </div>
      <div v-if="files.length === 0" class="text-center py-8">
        <svg class="mx-auto h-12 w-12 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 21h10a2 2 0 002-2V9.414a1 1 0 00-.293-.707l-5.414-5.414A1 1 0 0012.586 3H7a2 2 0 00-2 2v14a2 2 0 002 2z" />
        </svg>
        <p class="mt-2 text-slate-600 dark:text-slate-400">Aucun fichier</p>
      </div>
    </div>
    <!-- Drag & drop + Modal d'ajout de fichiers multiples -->
    <div v-if="showFileModal" class="fixed inset-0 z-50 overflow-y-auto flex items-center justify-center">
      <div class="fixed inset-0 bg-black/50 backdrop-blur-sm" @click="showFileModal = false"></div>
      <div class="relative bg-white dark:bg-gray-800 rounded-2xl shadow-xl w-full max-w-lg mx-auto p-4 sm:p-6 flex flex-col justify-center min-h-[340px]">
        <h3 class="text-lg sm:text-xl font-semibold text-slate-900 dark:text-slate-100 mb-4">Ajouter des fichiers</h3>
        <form @submit.prevent="uploadFiles" class="flex flex-col h-full">
          <div
            class="border-2 border-dashed border-blue-300 dark:border-blue-700 rounded-lg p-4 text-center cursor-pointer transition hover:border-blue-500 dark:hover:border-blue-400 bg-blue-50/30 dark:bg-blue-900/10 min-h-[120px] max-h-40 overflow-y-auto flex flex-col justify-center"
            @dragover.prevent="dragActive = true"
            @dragleave.prevent="dragActive = false"
            @drop.prevent="onDropFiles"
            :class="{ 'border-blue-500 dark:border-blue-400 bg-blue-100/50 dark:bg-blue-900/20': dragActive }"
            tabindex="0"
            aria-label="Zone de dépôt de fichiers"
          >
            <input type="file" multiple @change="handleFilesSelect" class="hidden" ref="fileInput" />
            <div @click="$refs.fileInput.click()" class="flex flex-col items-center justify-center gap-2">
              <svg class="h-10 w-10 text-blue-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
              </svg>
              <span class="text-blue-700 dark:text-blue-300 font-medium">Glissez-déposez ou cliquez pour sélectionner</span>
              <span class="text-xs text-slate-500">(Vous pouvez sélectionner plusieurs fichiers)</span>
            </div>
            <div v-if="selectedFiles.length" class="mt-2 flex flex-wrap gap-2 justify-center">
              <span v-for="file in selectedFiles" :key="file.name" class="inline-block bg-blue-100 dark:bg-blue-900/30 text-blue-700 dark:text-blue-300 px-2 py-1 rounded text-xs">{{ file.name }}</span>
            </div>
          </div>
          <div class="flex justify-end space-x-4 mt-6 sticky bottom-0 bg-white dark:bg-gray-800 pt-4 z-10">
            <button type="button" @click="showFileModal = false" class="px-4 py-2 text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-700 rounded-lg transition-all duration-200">Annuler</button>
            <button type="submit" :disabled="uploading" class="px-4 py-2 bg-blue-600 text-white hover:bg-blue-700 rounded-lg transition-all duration-200 disabled:opacity-50">
              {{ uploading ? 'Envoi...' : 'Ajouter' }}
            </button>
          </div>
        </form>
      </div>
    </div>
    <!-- Toast feedback -->
    <transition name="fade">
      <div v-if="toast" class="fixed bottom-4 left-1/2 transform -translate-x-1/2 bg-blue-600 text-white px-4 py-2 rounded-lg shadow-lg z-50 text-sm">
        {{ toast }}
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useNuxtApp, useRuntimeConfig } from '#app'
import { useFormatters } from '~/composables/useFormatters'

// Icônes selon type de fichier
const FilePdf = { template: '<svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 21h10a2 2 0 002-2V9.414a1 1 0 00-.293-.707l-5.414-5.414A1 1 0 0012.586 3H7a2 2 0 00-2 2v14a2 2 0 002 2z" /></svg>' }
const FileImage = { template: '<svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 7v10a2 2 0 002 2h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2zm0 0l7 7 4-4 5 5" /></svg>' }
const FileDoc = { template: '<svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 21h10a2 2 0 002-2V9.414a1 1 0 00-.293-.707l-5.414-5.414A1 1 0 0012.586 3H7a2 2 0 00-2 2v14a2 2 0 002 2z" /></svg>' }
const FileGeneric = { template: '<svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 21h10a2 2 0 002-2V9.414a1 1 0 00-.293-.707l-5.414-5.414A1 1 0 0012.586 3H7a2 2 0 00-2 2v14a2 2 0 002 2z" /></svg>' }

const fileIcon = (type) => {
  if (!type) return FileGeneric
  if (type.includes('pdf')) return FilePdf
  if (type.includes('image')) return FileImage
  if (type.includes('word') || type.includes('doc')) return FileDoc
  return FileGeneric
}

const props = defineProps({
  entityType: { type: String, required: true },
  entityId: { type: [String, Number], required: true },
  canEdit: { type: Boolean, default: false }
})
const emit = defineEmits(['update'])
const { $axios } = useNuxtApp()
const config = useRuntimeConfig()
const { formatDate } = useFormatters()

const files = ref([])
const showFileModal = ref(false)
const uploading = ref(false)
const selectedFiles = ref([])
const dragActive = ref(false)
const toast = ref('')

const formatSize = (size) => {
  if (!size) return ''
  if (size < 1024) return size + ' o'
  if (size < 1024 * 1024) return (size / 1024).toFixed(1) + ' Ko'
  if (size < 1024 * 1024 * 1024) return (size / (1024 * 1024)).toFixed(1) + ' Mo'
  return (size / (1024 * 1024 * 1024)).toFixed(1) + ' Go'
}

const fetchFiles = async () => {
  const res = await $axios.get(`${config.public.apiBase}/media/entity`, { params: { entityType: props.entityType, entityId: props.entityId } })
  files.value = res.data
}

const openFileModal = () => {
  selectedFiles.value = []
  showFileModal.value = true
}

const handleFilesSelect = (event) => {
  selectedFiles.value = Array.from(event.target.files)
}

const onDropFiles = (event) => {
  dragActive.value = false
  if (event.dataTransfer && event.dataTransfer.files) {
    selectedFiles.value = Array.from(event.dataTransfer.files)
  }
}

const uploadFiles = async () => {
  if (!selectedFiles.value.length) return
  uploading.value = true
  try {
    const formData = new FormData()
    for (const file of selectedFiles.value) {
      formData.append('files', file)
    }
    formData.append('entityType', props.entityType)
    formData.append('entityId', props.entityId)
    await $axios.post(`${config.public.apiBase}/media/upload`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    showFileModal.value = false
    toast.value = 'Fichiers ajoutés avec succès !'
    setTimeout(() => toast.value = '', 2500)
    fetchFiles()
    emit('update')
  } catch (error) {
    toast.value = "Erreur lors de l'envoi des fichiers."
    setTimeout(() => toast.value = '', 2500)
    console.error('Erreur lors de l\'upload des fichiers:', error)
  } finally {
    uploading.value = false
  }
}

const downloadFile = (file) => {
  // Utilise l'endpoint backend pour le téléchargement sécurisé
  window.open(`${config.public.apiBase}/media/download/${file.id}`, '_blank')
}

const deleteFile = async (file) => {
  if (!confirm(`Êtes-vous sûr de vouloir supprimer le fichier "${file.nom}" ?`)) return
  try {
    await $axios.delete(`${config.public.apiBase}/media/${file.id}`)
    toast.value = 'Fichier supprimé.'
    setTimeout(() => toast.value = '', 2000)
    fetchFiles()
    emit('update')
  } catch (error) {
    toast.value = "Erreur lors de la suppression."
    setTimeout(() => toast.value = '', 2000)
    console.error('Erreur lors de la suppression:', error)
  }
}

onMounted(fetchFiles)
watch(() => [props.entityType, props.entityId], fetchFiles)
</script>

<style scoped>
.fade-enter-active, .fade-leave-active { transition: opacity 0.3s; }
.fade-enter-from, .fade-leave-to { opacity: 0; }
</style> 