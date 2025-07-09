export default defineNuxtRouteMiddleware(async (to) => {
  const { isAuthenticated } = useAuth()
  const token = useCookie('token')
  const user = useCookie('user')

  // Si la route est /login et l'utilisateur est déjà authentifié, rediriger vers /dashboard
  if (to.path === '/login' && isAuthenticated.value) {
    return navigateTo('/dashboard')
  }

  // Si pas de token ou pas d'utilisateur, rediriger vers /login
  if (!token.value || !user.value) {
    if (to.path !== '/login') {
      return navigateTo('/login')
    }
  }

  // Vérifier si la route nécessite une authentification
  if (to.path !== '/login' && !isAuthenticated.value) {
    return navigateTo('/login')
  }
})
