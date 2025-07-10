<template>
  <header class="sticky top-0 z-30 bg-white/95 backdrop-blur-xl border-b border-gray-200/50 shadow-sm">
    <div class="absolute inset-0 bg-gradient-to-r from-blue-900/5 via-transparent to-cyan-700/5" />

    <nav class="relative flex items-center justify-between h-16 px-4 sm:px-6 lg:px-8">
      <div class="flex items-center">
        <transition
          enter-active-class="transition-all duration-200 ease-out" enter-from-class="opacity-0 scale-95 -translate-x-4"
          enter-to-class="opacity-100 scale-100 translate-x-0"
          leave-active-class="transition-all duration-200 ease-in" leave-from-class="opacity-100 scale-100 translate-x-0"
          leave-to-class="opacity-0 scale-95 -translate-x-4"
        >
          <NuxtLink 
            v-if="!isSidebarOpen" 
            to="/" 
            class="flex items-center group"
          >
            <div class="w-8 h-8 bg-gradient-to-br from-blue-900 to-cyan-700 rounded-xl flex items-center justify-center shadow-md group-hover:scale-105 transition-transform duration-300">
              <img src="/assets/images/logo.png" alt="Logo" class="w-4 h-4 filter brightness-0 invert" />
            </div>
            <div class="ml-3">
              <span class="text-lg font-bold bg-gradient-to-r from-blue-900 to-cyan-700 bg-clip-text text-transparent">
                Collab<span class="text-cyan-600">Space</span>
              </span>
            </div>
          </NuxtLink>
        </transition>

        <button
          v-if="user && user.role !== 'MEMBRE_PROJET'"
          @click="toggleSidebar"
          :class="[
            'p-2 rounded-xl text-slate-600 hover:text-blue-900 hover:bg-blue-50 transition-all duration-200',
            isSidebarOpen ? 'ml-0' : 'ml-4'
          ]"
        >
          <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
          </svg>
        </button>
      </div>

      <div class="flex items-center">
        <NuxtLink
          v-if="user && user.role === 'MEMBRE_PROJET'"
          to="/messagerie"
          class="inline-flex items-center px-3 py-2 ml-2 text-sm font-medium rounded-lg bg-blue-50 text-blue-700 hover:bg-blue-100 hover:text-blue-900 transition-all duration-200"
        >
          <svg class="h-5 w-5 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 10h.01M12 10h.01M16 10h.01M21 12c0 4.418-4.03 8-9 8s-9-3.582-9-8 4.03-8 9-8 9 3.582 9 8z" />
          </svg>
          Messagerie
        </NuxtLink>
        <div class="relative">
          <button
            @click="isDropdownOpen = !isDropdownOpen"
            class="flex items-center p-2 rounded-xl hover:bg-gradient-to-r hover:from-blue-50 hover:to-cyan-50 transition-all duration-300 group"
          >
            <div class="w-8 h-8 bg-gradient-to-br from-blue-600 to-cyan-600 rounded-lg flex items-center justify-center text-white font-semibold text-sm shadow-md">
              {{ userInitials }}
            </div>

            <div v-if="user && user.firstName && user.lastName" class="hidden md:block ml-3 text-left">
              <p class="text-sm font-medium text-slate-700 group-hover:text-blue-900 transition-colors">
                {{ user.firstName }} {{ user.lastName }}
              </p>
            </div>

            <svg 
              :class="[
                'hidden md:block ml-2 h-4 w-4 text-slate-400 transition-transform duration-200',
                { 'rotate-180': isDropdownOpen }
              ]" 
              fill="none" 
              viewBox="0 0 24 24" 
              stroke="currentColor"
            >
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
            </svg>
          </button>

          <div v-if="isDropdownOpen" class="fixed inset-0 z-10" @click="isDropdownOpen = false" />
          <transition
            enter-active-class="transition ease-out duration-200"
            enter-from-class="transform opacity-0 scale-95"
            enter-to-class="transform opacity-100 scale-100"
            leave-active-class="transition ease-in duration-150"
            leave-from-class="transform opacity-100 scale-100"
            leave-to-class="transform opacity-0 scale-95"
          >
            <div
              v-if="isDropdownOpen"
              class="absolute right-0 mt-2 w-56 bg-white/95 backdrop-blur-xl rounded-xl shadow-lg border border-gray-200/50 z-20 overflow-hidden"
            >
              <div class="px-4 py-3 bg-gradient-to-r from-blue-50 to-cyan-50 border-b border-gray-200/50">
                <p class="text-sm font-medium text-slate-900">
                  {{ user?.firstName }} {{ user?.lastName }}
                </p>
                <p v-if="user?.email" class="text-xs text-slate-500 truncate">{{ user.email }}</p>
              </div>

              <div class="py-2">
                <NuxtLink
                  to="/profile"
                  class="flex items-center px-4 py-2 text-sm text-slate-700 hover:bg-gradient-to-r hover:from-blue-50 hover:to-cyan-50 hover:text-blue-900 transition-all duration-200"
                  @click="isDropdownOpen = false"
                >
                  <svg class="h-4 w-4 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                  </svg>
                  Votre profil
                </NuxtLink>

                <button
                  @click="handleLogout"
                  :disabled="isLoggingOut"
                  class="w-full flex items-center px-4 py-2 text-sm text-red-600 hover:bg-red-50 hover:text-red-700 transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  <svg v-if="!isLoggingOut" class="h-4 w-4 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                  </svg>
                  <svg v-else class="animate-spin h-4 w-4 mr-3" fill="none" viewBox="0 0 24 24">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                    <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                  </svg>
                  {{ isLoggingOut ? 'Déconnexion...' : 'Se déconnecter' }}
                </button>
              </div>
            </div>
          </transition>
        </div>
      </div>
    </nav>
  </header>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useAuth } from '~/composables/useAuth'

const props = defineProps({
  isSidebarOpen: {
    type: Boolean,
    default: false
  }
})

const { user, logout } = useAuth()
const isDropdownOpen = ref(false)
const isLoggingOut = ref(false)

const userInitials = computed(() => {
  if (!user.value || (!user.value.firstName && !user.value.lastName)) {
    return 'UN'
  }
  let initials = ''
  if (user.value.firstName) {
    initials += user.value.firstName.charAt(0).toUpperCase()
  }
  if (user.value.lastName) {
    initials += user.value.lastName.charAt(0).toUpperCase()
  }
  return initials || 'UN'
})

const handleLogout = async () => {
  if (isLoggingOut.value) return
  
  try {
    isLoggingOut.value = true
    isDropdownOpen.value = false
    
    await logout()
  } catch (error) {
    console.error('Erreur lors de la déconnexion:', error)
  } finally {
    isLoggingOut.value = false
  }
}

const emit = defineEmits(['toggle-sidebar'])

const toggleSidebar = () => {
  emit('toggle-sidebar')
}
</script>