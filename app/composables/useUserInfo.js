import { ref, readonly } from 'vue'
import { useState, useNuxtApp } from '#app'

export function useUserInfo() {
  const { $axios } = useNuxtApp()
  const users = useState('users.list', () => [])
  const userInfo = useState('user.info', () => null)

  const fetchUsers = async () => {
    try {
      const response = await $axios.get('/users')
      users.value = response.data
    } catch (error) {
      console.error('Erreur lors du chargement des utilisateurs:', error)
    }
  }

  const fetchUserInfo = async () => {
    try {
      const response = await $axios.get('/users/me')
      userInfo.value = response.data
    } catch (error) {
      console.error('Erreur lors du chargement des informations de l\'utilisateur:', error)
      userInfo.value = null
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
    users: readonly(users),
    userInfo: readonly(userInfo),
    fetchUsers,
    fetchUserInfo,
    getInitials,
    getDisplayName,
    getUserEmail
  }
} 