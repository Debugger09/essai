import { ref, readonly } from 'vue'

// Simule une liste d'utilisateurs en ligne (à remplacer par une vraie logique WebSocket plus tard)
const onlineUserIds = ref([1, 2, 3]) // À adapter dynamiquement

export function usePresence() {
  // Pour une vraie app, il faudrait écouter un topic WebSocket et mettre à jour onlineUserIds dynamiquement
  return {
    onlineUserIds: readonly(onlineUserIds)
  }
} 