<template>
  <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white rounded-lg p-6 w-full max-w-md">
      <h2 class="text-xl font-semibold mb-4">{{ media ? 'Modifier le média' : 'Ajouter un média' }}</h2>
      <form @submit.prevent="handleSubmit" class="space-y-4">
        <div>
          <label class="block text-sm font-medium text-gray-700">Nom du média</label>
          <input
            v-model="formData.nom"
            type="text"
            class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
            required
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">Fichier</label>
          <input
            type="file"
            @change="handleFileChange"
            class="mt-1 block w-full"
            :required="!media"
          />
        </div>
        <div class="flex justify-end space-x-2 mt-6">
          <button type="button" @click="$emit('close')" class="btn-secondary">Annuler</button>
          <button type="submit" class="btn-primary" :disabled="loading">
            {{ loading ? 'Enregistrement...' : 'Enregistrer' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useNuxtApp } from '#app'
import { config } from '#app'

const props = defineProps({
  media: {
    type: Object,
    default: null
  },
  entityId: {
    type: [String, Number],
    required: true
  },
  entityType: {
    type: String,
    required: true
  }
})

const emit = defineEmits(['close', 'saved'])
const { $axios } = useNuxtApp()
const loading = ref(false)
const formData = ref({
  nom: props.media?.nom || '',
  file: null
})

const handleFileChange = (event) => {
  formData.value.file = event.target.files[0]
}

const handleSubmit = async () => {
  try {
    loading.value = true

    const formDataToSend = new FormData()
    formDataToSend.append('nom', formData.value.nom)
    if (formData.value.file) {
      formDataToSend.append('files', formData.value.file) // <-- PLURIEL
    }
    formDataToSend.append('entityId', props.entityId)
    formDataToSend.append('entityType', props.entityType)

    if (props.media) {
      await $axios.put(`${config.public.apiBase}/media/${props.media.id}`, formDataToSend, {
        headers: { 'Content-Type': 'multipart/form-data' }
      })
    } else {
      await $axios.post(`${config.public.apiBase}/media/upload`, formDataToSend, {
        headers: { 'Content-Type': 'multipart/form-data' }
      })
    }

    emit('saved')
    emit('close')
  } catch (error) {
    console.error('Erreur lors de l\'enregistrement du média:', error)
  } finally {
    loading.value = false
  }
}
</script> 