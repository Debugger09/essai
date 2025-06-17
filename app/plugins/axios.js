import axios from 'axios'

export default defineNuxtPlugin((nuxtApp) => {
  const config = useRuntimeConfig()
  
  // Créer une instance Axios avec la configuration de base
  const axiosInstance = axios.create({
    baseURL: config.public.apiBase,
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    }
  })

  // Intercepteur pour ajouter le token d'authentification
  axiosInstance.interceptors.request.use((config) => {
    const token = useCookie('token').value
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  })

  // Intercepteur pour gérer les erreurs
  axiosInstance.interceptors.response.use(
    (response) => response,
    (error) => {
      if (error.response?.status === 401 || error.response?.status === 403) {
        // Rediriger vers la page de connexion si le token est invalide ou expiré
        const { logout } = useAuth()
        logout()
      }
      return Promise.reject(error)
    }
  )

  // Rendre l'instance Axios disponible globalement
  nuxtApp.provide('axios', axiosInstance)
}) 