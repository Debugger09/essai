<template>
  <div class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-2xl shadow-lg border border-white/20 dark:border-gray-700/50 p-6">
    <h2 class="text-xl font-semibold text-slate-900 dark:text-slate-100 mb-4">Informations du projet</h2>
    <div class="space-y-4">
      <div>
        <h3 class="text-sm font-medium text-slate-500 dark:text-slate-400">Description</h3>
        <p class="mt-1 text-slate-900 dark:text-slate-100">{{ projet?.description || 'Aucune description' }}</p>
      </div>
      <div class="grid grid-cols-2 gap-4">
        <div>
          <h3 class="text-sm font-medium text-slate-500 dark:text-slate-400">Date de début</h3>
          <p class="mt-1 text-slate-900 dark:text-slate-100">{{ formatDate(projet?.dateDebut) }}</p>
        </div>
        <div>
          <h3 class="text-sm font-medium text-slate-500 dark:text-slate-400">Date de fin</h3>
          <p class="mt-1 text-slate-900 dark:text-slate-100">{{ formatDate(projet?.dateFin) }}</p>
        </div>
        <div>
          <h3 class="text-sm font-medium text-slate-500 dark:text-slate-400">Budget</h3>
          <p class="mt-1 text-slate-900 dark:text-slate-100">{{ formatMontant(projet?.budget) }}</p>
        </div>
        <div>
          <h3 class="text-sm font-medium text-slate-500 dark:text-slate-400">Chef de projet</h3>
          <p class="mt-1 text-slate-900 dark:text-slate-100">{{ projet?.chefProjet?.name || 'Non assigné' }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useFormatters } from '~/composables/useFormatters'

const props = defineProps({
  projet: {
    type: Object,
    required: true
  }
})

const { formatMontant, formatDate } = useFormatters()
</script> 