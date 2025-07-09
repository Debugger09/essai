import { ref, computed, readonly } from "vue"
import { useCookie, useState, useRuntimeConfig, navigateTo, clearNuxtState } from "#app"
import axios from "axios"

export const useAuth = () => {
  // États réactifs globaux. useState initialise avec la valeur du cookie lors du premier accès.
  const user = useState("auth.user", () => useCookie("user").value || null)
  const token = useState("auth.token", () => useCookie("token").value || null)

  const isLoading = ref(false)
  const error = ref(null)

  // Cookies pour la persistance
  const userCookie = useCookie("user", {
    default: () => null,
    sameSite: "lax",
    secure: process.env.NODE_ENV === 'production',
    httpOnly: false,
  })

  const tokenCookie = useCookie("token", {
    default: () => null,
    sameSite: "lax",
    secure: process.env.NODE_ENV === 'production',
    httpOnly: false,
  })

  // NEW: Fonction pour effacer les informations d'authentification sans redirection
  const clearAuth = () => {
    user.value = null
    token.value = null
    userCookie.value = null
    tokenCookie.value = null
    clearNuxtState(["auth.user", "auth.token"])
  }

  // Définir l'authentification
  const setAuth = (newToken, newUser) => {
    token.value = newToken
    user.value = newUser
    tokenCookie.value = newToken
    userCookie.value = newUser
  }

  // Fonction de connexion
  const login = async (credentials) => {
    isLoading.value = true
    error.value = null

    try {
      const config = useRuntimeConfig()
      const response = await axios.post(`${config.public.apiBase}/auth/login`, credentials)

      setAuth(response.data.token, response.data.user)
      await navigateTo("/projets")
    } catch (err) {
      error.value = err.response?.data?.message || "Email ou mot de passe incorrect."
      console.error("Login error:", err)
    } finally {
      isLoading.value = false
    }
  }

  // Déconnexion
  const logout = async () => {
    try {
      clearAuth()
      await navigateTo("/login", { replace: true })
    } catch (error) {
      console.error("Erreur lors de la déconnexion:", error)
      await navigateTo("/", { replace: true })
    }
  }

  // Vérifier si l'utilisateur est connecté
  const isAuthenticated = computed(() => {
    return !!(user.value && token.value)
  })

  return {
    user: readonly(user),
    token: readonly(token),
    isAuthenticated,
    isLoading,
    error,
    setAuth,
    login,
    logout,
    clearAuth,
  }
}