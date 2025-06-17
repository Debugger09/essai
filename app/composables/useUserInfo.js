import { ref } from 'vue'

const users = ref([])

export function useUserInfo() {
  const fetchUsers = async ($axios) => {
    try {
      const response = await $axios.get('/users')
      users.value = response.data
    } catch (error) {
      console.error('Erreur lors du chargement des utilisateurs:', error)
    }
  }

  const getInitials = (userId) => {
    const user = users.value.find(u => u.id === userId)
    if (!user) return '?'
    return user.email.substring(0, 2).toUpperCase()
  }

  const getDisplayName = (userId) => {
    const user = users.value.find(u => u.id === userId)
    if (!user) return `Utilisateur ${userId}`
    return user.email.split('@')[0]
  }

  const getUserEmail = (userId) => {
    const user = users.value.find(u => u.id === userId)
    if (!user) return ''
    return user.email
  }

  return {
    users,
    fetchUsers,
    getInitials,
    getDisplayName,
    getUserEmail
  }
} 