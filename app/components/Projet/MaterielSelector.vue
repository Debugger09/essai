<template>
  <div class="space-y-4">
    <div class="relative">
      <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
        Sélectionner un matériel existant
      </label>
      <div class="relative">
        <input
          type="text"
          v-model="searchQuery"
          @input="searchMateriels"
          placeholder="Rechercher un matériel..."
          class="w-full px-4 py-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
        <!-- Liste des suggestions -->
        <div v-if="showSuggestions && filteredMateriels.length > 0" 
          class="absolute z-10 w-full mt-1 bg-white dark:bg-gray-800 border border-slate-200 dark:border-slate-700 rounded-lg shadow-lg">
          <ul class="max-h-60 overflow-auto">
            <li v-for="materiel in filteredMateriels" 
              :key="materiel.id"
              @click="selectMateriel(materiel)"
              class="px-4 py-2 hover:bg-slate-50 dark:hover:bg-gray-700 cursor-pointer"
              :class="{ 'opacity-50 cursor-not-allowed': !materiel.reutilisable && !materiel.quantite }">
              <div class="flex justify-between items-center">
                <span>{{ materiel.libelle }}</span>
                <span class="text-sm text-slate-500">
                  {{ materiel.quantite }} disponible(s)
                </span>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </div>

    <!-- Formulaire pour le matériel sélectionné -->
    <div v-if="selectedMateriel" class="space-y-4 p-4 bg-slate-50 dark:bg-gray-700/50 rounded-xl">
      <div class="flex justify-between items-center">
        <h3 class="font-medium text-slate-900 dark:text-slate-100">
          {{ selectedMateriel.libelle }}
        </h3>
        <button @click="selectedMateriel = null" class="text-slate-400 hover:text-slate-600">
          <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>
      
      <div>
        <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
          Quantité requise
        </label>
        <input
          type="number"
          v-model="quantiteRequise"
          :max="selectedMateriel.quantite"
          min="1"
          class="w-full px-4 py-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500"
          :class="{ 'border-red-500': !isQuantiteValide }"
        />
        <p v-if="!isQuantiteValide" class="mt-1 text-sm text-red-500">
          Quantité non disponible
        </p>
      </div>
    </div>

    <!-- Bouton d'ajout -->
    <div class="flex justify-end">
      <button
        @click="ajouterMateriel"
        :disabled="!canAdd"
        class="px-4 py-2 bg-blue-600 text-white hover:bg-blue-700 rounded-lg transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
      >
        Ajouter à la liste
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRuntimeConfig } from '#app'
import { useNuxtApp } from '#app'

const config = useRuntimeConfig()
const { $axios } = useNuxtApp()

const props = defineProps({
  projetId: {
    type: [String, Number],
    required: true
  }
})

const emit = defineEmits(['materiel-added'])

// État local
const searchQuery = ref('')
const materiels = ref([])
const showSuggestions = ref(false)
const selectedMateriel = ref(null)
const quantiteRequise = ref(1)

// Computed properties
const filteredMateriels = computed(() => {
  if (!searchQuery.value) return materiels.value
  return materiels.value.filter(materiel => 
    materiel.libelle.toLowerCase().includes(searchQuery.value.toLowerCase())
  )
})

const isQuantiteValide = computed(() => {
  if (!selectedMateriel.value) return false
  return quantiteRequise.value > 0 && quantiteRequise.value <= selectedMateriel.value.quantite
})

const canAdd = computed(() => {
  return selectedMateriel.value && isQuantiteValide.value
})

// Méthodes
const searchMateriels = async () => {
  showSuggestions.value = true
  try {
    const response = await $axios.get('/materiels', {
      params: { search: searchQuery.value }
    })
    materiels.value = response.data
  } catch (error) {
    console.error('Erreur lors de la recherche des matériels:', error)
  }
}

const selectMateriel = (materiel) => {
  if (!materiel.reutilisable && !materiel.quantite) return
  selectedMateriel.value = materiel
  quantiteRequise.value = 1
  showSuggestions.value = false
}

const ajouterMateriel = async () => {
  if (!canAdd.value) return

  try {
    const payload = {
      materielId: selectedMateriel.value.id,
      projetId: props.projetId,
      quantite: quantiteRequise.value
    }

    const response = await $axios.post('/listemateriels', payload)
    emit('materiel-added', response.data)
    
    // Reset form
    selectedMateriel.value = null
    quantiteRequise.value = 1
    searchQuery.value = ''
  } catch (error) {
    console.error('Erreur lors de l\'ajout du matériel:', error)
  }
}

// Initialisation
onMounted(() => {
  searchMateriels()
})
</script> 