<template>
  <div class="flex items-center space-x-2">
    <!-- Indicateur de connexion WebSocket -->
    <div class="flex items-center space-x-1">
      <div 
        :class="[
          'w-2 h-2 rounded-full',
          isConnected ? 'bg-green-500' : 'bg-red-500'
        ]"
      ></div>
      <span class="text-xs text-gray-600">
        {{ isConnected ? 'Connecté' : 'Déconnecté' }}
      </span>
    </div>
    
    <!-- Nombre d'utilisateurs en ligne -->
    <div class="flex items-center space-x-1">
      <div class="w-2 h-2 rounded-full bg-blue-500"></div>
      <span class="text-xs text-gray-600">
        {{ onlineUsersCount }} en ligne
      </span>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { usePresence } from '~/composables/usePresence'

const isConnected = ref(false)
const { onlineUserIds } = usePresence()

const onlineUsersCount = computed(() => {
  return onlineUserIds.value.size
})

// Simuler la connexion WebSocket (à remplacer par la vraie logique)
onMounted(() => {
  // Simuler une connexion après 1 seconde
  setTimeout(() => {
    isConnected.value = true
  }, 1000)
})

onUnmounted(() => {
  isConnected.value = false
})
</script> 