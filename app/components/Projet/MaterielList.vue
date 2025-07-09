<template>
  <div class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-2xl shadow-lg border border-white/20 dark:border-gray-700/50 p-6">
    <div class="flex items-center justify-between mb-4">
      <h2 class="text-xl font-semibold text-slate-900 dark:text-slate-100">Matériel</h2>
    </div>

    <!-- Liste des matériels -->
    <div class="space-y-4">
      <div v-for="materiel in materiels" 
        :key="materiel.id" 
        class="bg-slate-50 dark:bg-gray-700/50 rounded-xl p-4"
      >
        <div class="flex items-center justify-between">
          <div>
            <h3 class="font-medium text-slate-900 dark:text-slate-100">{{ materiel.libelle }}</h3>
            <div class="mt-1 flex items-center space-x-4 text-sm text-slate-600 dark:text-slate-400">
              <span>{{ materiel.quantite }} unité(s)</span>
              <span>{{ formatMontant(materiel.prix) }}</span>
            </div>
          </div>
          <div class="flex items-center space-x-2">
            <button 
              v-if="materiel.reutilisable"
              @click="returnMateriel(materiel)"
              class="p-2 text-blue-600 hover:text-blue-800 dark:text-blue-400 dark:hover:text-blue-300 hover:bg-blue-50 dark:hover:bg-blue-900/20 rounded-lg transition-all duration-200"
              title="Retourner le matériel"
            >
              <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h10a8 8 0 018 8v2M3 10l6 6m-6-6l6-6" />
              </svg>
            </button>
            <button 
              @click="removeMateriel(materiel)"
              class="p-2 text-red-600 hover:text-red-800 dark:text-red-400 dark:hover:text-red-300 hover:bg-red-50 dark:hover:bg-red-900/20 rounded-lg transition-all duration-200"
              title="Supprimer"
            >
              <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
              </svg>
            </button>
          </div>
        </div>
      </div>

      <!-- Message si aucun matériel -->
      <div v-if="materiels.length === 0" class="text-center py-8">
        <svg class="mx-auto h-12 w-12 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
        </svg>
        <p class="mt-2 text-slate-600 dark:text-slate-400">Aucun matériel</p>
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
const { formatMontant } = useFormatters()

const props = defineProps({
  projetId: {
    type: String,
    required: true
  }
})

const emit = defineEmits(['materiel-removed', 'materiel-returned'])

// État
const materiels = ref([])

// Méthodes
const loadMateriels = async () => {
  try {
    const response = await $axios.get(`/projets/${props.projetId}/materiels`)
    materiels.value = response.data
  } catch (error) {
    console.error('Erreur lors du chargement des matériels:', error)
  }
}

const returnMateriel = async (materiel) => {
  if (!confirm(`Êtes-vous sûr de vouloir retourner le matériel "${materiel.libelle}" ?`)) return

  try {
    await $axios.post(`/listemateriels/${materiel.id}/return`)
    materiels.value = materiels.value.filter(m => m.id !== materiel.id)
    emit('materiel-returned', materiel.id)
  } catch (error) {
    console.error('Erreur lors du retour du matériel:', error)
  }
}

const removeMateriel = async (materiel) => {
  if (!confirm(`Êtes-vous sûr de vouloir supprimer le matériel "${materiel.libelle}" ?`)) return

  try {
    await $axios.delete(`/listemateriels/${materiel.id}`)
    materiels.value = materiels.value.filter(m => m.id !== materiel.id)
    emit('materiel-removed', materiel.id)
  } catch (error) {
    console.error('Erreur lors de la suppression:', error)
  }
}

// Initialisation
onMounted(() => {
  loadMateriels()
})
</script> 