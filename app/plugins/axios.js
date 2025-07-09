import axios from 'axios'
import { useCookie } from '#app'
import { useAuth } from '~/composables/useAuth'
import { navigateTo } from '#app'

export default defineNuxtPlugin((nuxtApp) => {
  const axiosInstance = axios.create({
    baseURL: useRuntimeConfig().public.apiBase,
    // ... autres options si besoin
  })

  // Intercepteur pour ajouter le token
  axiosInstance.interceptors.request.use(config => {
    const token = useCookie('token').value
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  })

  // Intercepteur pour gérer les erreurs 401
  axiosInstance.interceptors.response.use(
    response => response,
    error => {
      if (error.response && error.response.status === 401) {
        const { clearAuth } = useAuth()
        clearAuth()
        navigateTo('/login')
      }
      return Promise.reject(error)
    }
  )

  // Rendre l'instance Axios disponible globalement
  nuxtApp.provide('axios', axiosInstance)
}) 