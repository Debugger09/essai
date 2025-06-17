<template>
  <div class="min-h-screen bg-gradient-to-br from-slate-50 via-blue-50/30 to-indigo-50/20 dark:from-gray-900 dark:via-blue-950/50 dark:to-indigo-950/30 transition-all duration-500">
    <!-- Background Elements -->
    <div class="absolute inset-0 overflow-hidden pointer-events-none">
      <div class="absolute top-1/4 left-1/4 w-96 h-96 bg-gradient-to-r from-blue-400/10 to-indigo-400/10 dark:from-blue-600/20 dark:to-indigo-600/20 rounded-full blur-3xl animate-pulse"></div>
      <div class="absolute bottom-1/4 right-1/4 w-80 h-80 bg-gradient-to-r from-indigo-400/8 to-purple-400/8 dark:from-indigo-600/15 dark:to-purple-600/15 rounded-full blur-3xl animate-pulse delay-1000"></div>
    </div>

    <div class="relative z-10 container mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-8 lg:py-12">
      <!-- Header Section -->
      <div class="mb-8 sm:mb-12">
        <div class="text-center sm:text-left">
          <h1 class="text-3xl sm:text-4xl lg:text-5xl font-bold bg-gradient-to-r from-blue-900 via-blue-700 to-indigo-600 dark:from-blue-400 dark:via-blue-300 dark:to-indigo-300 bg-clip-text text-transparent mb-3 sm:mb-4">
            Tableau de Bord
          </h1>
          <p class="text-slate-600 dark:text-slate-400 text-base sm:text-lg lg:text-xl max-w-2xl">
            Bienvenue dans votre espace de travail collaboratif
          </p>
        </div>
      </div>

      <!-- Stats Cards Grid -->
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 sm:gap-6 lg:gap-8 mb-8 sm:mb-12">
        <!-- Mes Tâches Card -->
        <div class="group relative bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-2xl sm:rounded-3xl shadow-lg hover:shadow-2xl border border-white/20 dark:border-gray-700/50 p-4 sm:p-6 lg:p-8 transition-all duration-300 hover:scale-[1.02] hover:-translate-y-2">
          <div class="absolute inset-0 bg-gradient-to-br from-blue-500/5 to-indigo-500/5 dark:from-blue-400/10 dark:to-indigo-400/10 rounded-2xl sm:rounded-3xl opacity-0 group-hover:opacity-100 transition-opacity duration-300"></div>
          
          <div class="relative z-10">
            <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between mb-4 sm:mb-6">
              <div class="flex items-center space-x-3 mb-3 sm:mb-0">
                <div class="p-2 sm:p-3 bg-blue-100 dark:bg-blue-900/30 rounded-xl sm:rounded-2xl">
                  <svg class="h-5 w-5 sm:h-6 sm:w-6 text-blue-600 dark:text-blue-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4" />
                  </svg>
                </div>
                <h2 class="text-lg sm:text-xl font-semibold text-slate-800 dark:text-slate-100">Mes Tâches</h2>
              </div>
              <span class="bg-gradient-to-r from-blue-500 to-blue-600 text-white text-xs font-semibold px-3 py-1.5 rounded-full shadow-sm self-start sm:self-auto">
                5 en cours
              </span>
            </div>
            
            <p class="text-slate-600 dark:text-slate-300 mb-4 sm:mb-6 leading-relaxed text-sm sm:text-base">
              Aperçu rapide de vos tâches assignées et leur progression.
            </p>
            
            <NuxtLink to="/my-tasks" class="inline-flex items-center text-blue-600 dark:text-blue-400 hover:text-blue-700 dark:hover:text-blue-300 font-medium text-sm sm:text-base group-hover:translate-x-1 transition-all duration-200">
              Voir toutes les tâches
              <svg class="h-4 w-4 ml-2 transition-transform duration-200 group-hover:translate-x-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8l4 4m0 0l-4 4m4-4H3" />
              </svg>
            </NuxtLink>
          </div>
        </div>

        <!-- Projets Actifs Card -->
        <div v-if="hasAccess(['ADMIN', 'CHEF_PROJET'])" class="group relative bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-2xl sm:rounded-3xl shadow-lg hover:shadow-2xl border border-white/20 dark:border-gray-700/50 p-4 sm:p-6 lg:p-8 transition-all duration-300 hover:scale-[1.02] hover:-translate-y-2">
          <div class="absolute inset-0 bg-gradient-to-br from-emerald-500/5 to-green-500/5 dark:from-emerald-400/10 dark:to-green-400/10 rounded-2xl sm:rounded-3xl opacity-0 group-hover:opacity-100 transition-opacity duration-300"></div>
          
          <div class="relative z-10">
            <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between mb-4 sm:mb-6">
              <div class="flex items-center space-x-3 mb-3 sm:mb-0">
                <div class="p-2 sm:p-3 bg-emerald-100 dark:bg-emerald-900/30 rounded-xl sm:rounded-2xl">
                  <svg class="h-5 w-5 sm:h-6 sm:w-6 text-emerald-600 dark:text-emerald-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
                  </svg>
                </div>
                <h2 class="text-lg sm:text-xl font-semibold text-slate-800 dark:text-slate-100">Projets Actifs</h2>
              </div>
              <span class="bg-gradient-to-r from-emerald-500 to-green-600 text-white text-xs font-semibold px-3 py-1.5 rounded-full shadow-sm self-start sm:self-auto">
                3 actifs
              </span>
            </div>
            
            <p class="text-slate-600 dark:text-slate-300 mb-4 sm:mb-6 leading-relaxed text-sm sm:text-base">
              Suivez l'avancement et gérez vos projets en cours.
            </p>
            
            <NuxtLink to="/projects" class="inline-flex items-center text-emerald-600 dark:text-emerald-400 hover:text-emerald-700 dark:hover:text-emerald-300 font-medium text-sm sm:text-base group-hover:translate-x-1 transition-all duration-200">
              Gérer les projets
              <svg class="h-4 w-4 ml-2 transition-transform duration-200 group-hover:translate-x-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8l4 4m0 0l-4 4m4-4H3" />
              </svg>
            </NuxtLink>
          </div>
        </div>

        <!-- Nouveaux Utilisateurs Card -->
        <div v-if="hasAccess(['ADMIN'])" class="group relative bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-2xl sm:rounded-3xl shadow-lg hover:shadow-2xl border border-white/20 dark:border-gray-700/50 p-4 sm:p-6 lg:p-8 transition-all duration-300 hover:scale-[1.02] hover:-translate-y-2">
          <div class="absolute inset-0 bg-gradient-to-br from-amber-500/5 to-orange-500/5 dark:from-amber-400/10 dark:to-orange-400/10 rounded-2xl sm:rounded-3xl opacity-0 group-hover:opacity-100 transition-opacity duration-300"></div>
          
          <div class="relative z-10">
            <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between mb-4 sm:mb-6">
              <div class="flex items-center space-x-3 mb-3 sm:mb-0">
                <div class="p-2 sm:p-3 bg-amber-100 dark:bg-amber-900/30 rounded-xl sm:rounded-2xl">
                  <svg class="h-5 w-5 sm:h-6 sm:w-6 text-amber-600 dark:text-amber-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197m13.5-9a2.5 2.5 0 11-5 0 2.5 2.5 0 015 0z" />
                  </svg>
                </div>
                <h2 class="text-lg sm:text-xl font-semibold text-slate-800 dark:text-slate-100">Utilisateurs</h2>
              </div>
              <span class="bg-gradient-to-r from-amber-500 to-orange-600 text-white text-xs font-semibold px-3 py-1.5 rounded-full shadow-sm self-start sm:self-auto">
                2 en attente
              </span>
            </div>
            
            <p class="text-slate-600 dark:text-slate-300 mb-4 sm:mb-6 leading-relaxed text-sm sm:text-base">
              Gérez les nouveaux utilisateurs et leurs permissions.
            </p>
            
            <NuxtLink to="/users" class="inline-flex items-center text-amber-600 dark:text-amber-400 hover:text-amber-700 dark:hover:text-amber-300 font-medium text-sm sm:text-base group-hover:translate-x-1 transition-all duration-200">
              Gérer les utilisateurs
              <svg class="h-4 w-4 ml-2 transition-transform duration-200 group-hover:translate-x-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8l4 4m0 0l-4 4m4-4H3" />
              </svg>
            </NuxtLink>
          </div>
        </div>
      </div>

      <!-- Activités Récentes Section -->
      <div class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-2xl sm:rounded-3xl shadow-lg border border-white/20 dark:border-gray-700/50 p-4 sm:p-6 lg:p-8 transition-all duration-300">
        <div class="flex items-center space-x-3 mb-6 sm:mb-8">
          <div class="p-2 sm:p-3 bg-indigo-100 dark:bg-indigo-900/30 rounded-xl sm:rounded-2xl">
            <svg class="h-5 w-5 sm:h-6 sm:w-6 text-indigo-600 dark:text-indigo-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
          <h2 class="text-lg sm:text-xl lg:text-2xl font-semibold text-slate-800 dark:text-slate-100">Activités Récentes</h2>
        </div>
        
        <div class="space-y-3 sm:space-y-4">
          <div class="flex flex-col sm:flex-row sm:items-start sm:justify-between p-3 sm:p-4 rounded-xl bg-slate-50/50 dark:bg-gray-700/30 border border-slate-200/50 dark:border-gray-600/30 transition-all duration-200 hover:bg-slate-100/50 dark:hover:bg-gray-700/50 hover:scale-[1.01]">
            <div class="flex items-start space-x-3 flex-1">
              <div class="flex-shrink-0 w-2 h-2 bg-green-500 rounded-full mt-2"></div>
              <div class="flex-1 min-w-0">
                <p class="text-sm sm:text-base text-slate-700 dark:text-slate-200 font-medium">
                  Tâche "Finaliser le rapport" complétée par John Doe
                </p>
                <p class="text-xs sm:text-sm text-slate-500 dark:text-slate-400 mt-1 sm:hidden">il y a 5 minutes</p>
              </div>
            </div>
            <p class="text-xs sm:text-sm text-slate-500 dark:text-slate-400 mt-2 sm:mt-0 sm:ml-4 hidden sm:block">il y a 5 minutes</p>
          </div>
          
          <div class="flex flex-col sm:flex-row sm:items-start sm:justify-between p-3 sm:p-4 rounded-xl bg-slate-50/50 dark:bg-gray-700/30 border border-slate-200/50 dark:border-gray-600/30 transition-all duration-200 hover:bg-slate-100/50 dark:hover:bg-gray-700/50 hover:scale-[1.01]">
            <div class="flex items-start space-x-3 flex-1">
              <div class="flex-shrink-0 w-2 h-2 bg-blue-500 rounded-full mt-2"></div>
              <div class="flex-1 min-w-0">
                <p class="text-sm sm:text-base text-slate-700 dark:text-slate-200 font-medium">
                  Nouveau projet "Migration V2" créé
                </p>
                <p class="text-xs sm:text-sm text-slate-500 dark:text-slate-400 mt-1 sm:hidden">il y a 1 heure</p>
              </div>
            </div>
            <p class="text-xs sm:text-sm text-slate-500 dark:text-slate-400 mt-2 sm:mt-0 sm:ml-4 hidden sm:block">il y a 1 heure</p>
          </div>
          
          <div class="flex flex-col sm:flex-row sm:items-start sm:justify-between p-3 sm:p-4 rounded-xl bg-slate-50/50 dark:bg-gray-700/30 border border-slate-200/50 dark:border-gray-600/30 transition-all duration-200 hover:bg-slate-100/50 dark:hover:bg-gray-700/50 hover:scale-[1.01]">
            <div class="flex items-start space-x-3 flex-1">
              <div class="flex-shrink-0 w-2 h-2 bg-amber-500 rounded-full mt-2"></div>
              <div class="flex-1 min-w-0">
                <p class="text-sm sm:text-base text-slate-700 dark:text-slate-200 font-medium">
                  3 nouvelles notifications en attente
                </p>
                <p class="text-xs sm:text-sm text-slate-500 dark:text-slate-400 mt-1 sm:hidden">il y a 2 heures</p>
              </div>
            </div>
            <p class="text-xs sm:text-sm text-slate-500 dark:text-slate-400 mt-2 sm:mt-0 sm:ml-4 hidden sm:block">il y a 2 heures</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { definePageMeta } from '#imports'
import { useAuth } from '~/composables/useAuth'

definePageMeta({
  middleware: 'auth',
  layout: 'default'
})

const { user } = useAuth()

const hasAccess = (requiredRoles) => {
  if (!user.value || !user.value.role) {
    return false
  }
  return requiredRoles.includes(user.value.role)
}
</script>

<style scoped>
@supports (backdrop-filter: blur(10px)) {
  .backdrop-blur-sm {
    backdrop-filter: blur(8px);
  }
}

@supports not (backdrop-filter: blur(10px)) {
  .backdrop-blur-sm {
    background-color: rgba(255, 255, 255, 0.9);
  }
  .dark .backdrop-blur-sm {
    background-color: rgba(31, 41, 55, 0.9);
  }
}

.group:hover .group-hover\:translate-x-1 {
  transform: translateX(0.25rem);
}

@media (max-width: 640px) {
  .container {
    padding-left: 1rem;
    padding-right: 1rem;
  }
}
</style>