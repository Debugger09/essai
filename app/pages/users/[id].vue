<template>
  <div class="container mx-auto py-8">
    <NuxtLink to="/users" class="inline-flex items-center mb-4 text-blue-600 hover:underline">
      <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" /></svg>
      Retour à la liste des utilisateurs
    </NuxtLink>
    <div class="bg-white rounded-xl shadow p-6 mb-8">
      <h1 class="text-2xl font-bold mb-2">Fiche utilisateur</h1>
      <div v-if="user">
        <div class="mb-4">
          <span class="font-semibold">Nom :</span> {{ user.lastName }} {{ user.firstName }}
        </div>
        <div class="mb-4">
          <span class="font-semibold">Email :</span> {{ user.email }}
        </div>
        <div class="mb-4">
          <span class="font-semibold">Rôle :</span> {{ user.role }}
        </div>
        <div class="mb-4">
          <span class="font-semibold">Statut :</span> {{ user.status ? 'Actif' : 'Inactif' }}
        </div>
        <div class="mb-4">
          <span class="font-semibold">Téléphone :</span> {{ user.phone || '—' }}
        </div>
        <div class="mb-4">
          <span class="font-semibold">Job :</span> {{ user.job || '—' }}
        </div>
      </div>
      <div v-else class="text-red-500">Utilisateur introuvable.</div>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
      <div class="bg-white rounded-xl shadow p-6">
        <h2 class="text-xl font-semibold mb-4">Projets en tant que chef de projet</h2>
        <ul v-if="projetsChef.length">
          <li v-for="projet in projetsChef" :key="projet.id">
            <NuxtLink :to="`/projets/${projet.id}`" class="text-blue-600 hover:underline">{{ projet.name }}</NuxtLink>
          </li>
        </ul>
        <div v-else class="text-gray-500">Aucun projet en tant que chef.</div>
      </div>
      <div class="bg-white rounded-xl shadow p-6">
        <h2 class="text-xl font-semibold mb-4">Projets en tant que membre</h2>
        <ul v-if="projetsMembre.length">
          <li v-for="projet in projetsMembre" :key="projet.id">
            <NuxtLink :to="`/projets/${projet.id}`" class="text-blue-600 hover:underline">{{ projet.name }}</NuxtLink>
          </li>
        </ul>
        <div v-else class="text-gray-500">Aucun projet en tant que membre.</div>
      </div>
    </div>
    <div class="bg-white rounded-xl shadow p-6 mt-8">
      <h2 class="text-xl font-semibold mb-4">Tâches assignées</h2>
      <ul v-if="tachesMembre.length">
        <li v-for="tache in tachesMembre" :key="tache.id">
          <NuxtLink :to="`/taches/${tache.id}`" class="text-blue-600 hover:underline">{{ tache.libelle }}</NuxtLink>
          <span class="text-xs text-gray-500 ml-2">(Projet : <NuxtLink :to="`/projets/${tache.projetId}`" class="text-blue-400 hover:underline">{{ tache.projetName }}</NuxtLink>)</span>
        </li>
      </ul>
      <div v-else class="text-gray-500">Aucune tâche assignée.</div>
    </div>
    <div class="bg-white rounded-xl shadow p-6 mt-8">
      <h2 class="text-xl font-semibold mb-4">Statistiques</h2>
      <ul>
        <li><span class="font-semibold">Projets chef :</span> {{ projetsChef.length }}</li>
        <li><span class="font-semibold">Projets membre :</span> {{ projetsMembre.length }}</li>
        <li><span class="font-semibold">Tâches assignées :</span> {{ tachesMembre.length }}</li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useNuxtApp } from '#app'

const route = useRoute()
const { $axios } = useNuxtApp()

const user = ref(null)
const projetsChef = ref([])
const projetsMembre = ref([])
const tachesMembre = ref([])

onMounted(async () => {
  // Charger l'utilisateur
  const resUser = await $axios.get(`/users/${route.params.id}`)
  user.value = resUser.data

  // Charger tous les projets et tâches
  const resProjets = await $axios.get('/projets')
  const allProjets = resProjets.data || []
  const resTaches = await $axios.get('/taches')
  const allTaches = resTaches.data || []

  // Projets où il est chef
  projetsChef.value = allProjets.filter(p => p.chefProjet && p.chefProjet.id === user.value.id)

  // Projets où il est membre
  projetsMembre.value = allProjets.filter(p => p.membres && p.membres.some(m => m.id === user.value.id))

  // Tâches où il est membre
  tachesMembre.value = allTaches.filter(t => t.membres && t.membres.some(m => m.id === user.value.id)).map(t => ({
    ...t,
    projetName: t.projet?.name || t.projet?.libelle || '',
    projetId: t.projet?.id
  }))
})
</script> 