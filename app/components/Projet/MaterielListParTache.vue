<template>
  <div v-if="materiels.length > 0">
    <h4 class="font-semibold mb-2">Tâche : {{ tacheLibelle }}</h4>
    <ul class="list-disc ml-6">
      <li v-for="m in materiels" :key="m.id">
        {{ m.materiel?.libelle || m.libelle }} ({{ m.quantite }})
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useNuxtApp } from '#app'

const props = defineProps({
  tacheId: [String, Number],
  tacheLibelle: String
})
const { $axios } = useNuxtApp()
const materiels = ref([])

onMounted(async () => {
  try {
    const res = await $axios.get(`/taches/${props.tacheId}/materiels`)
    materiels.value = res.data
  } catch (e) {
    materiels.value = []
  }
})
</script> 