//layouts/default.vue 
<template>
  <div class="min-h-screen bg-gradient-to-br from-slate-50 via-blue-50/30 to-cyan-50/30">
    <!-- Effets de flou décoratifs en arrière-plan -->
    <div class="fixed inset-0 overflow-hidden pointer-events-none">
      <div class="absolute top-20 left-20 w-64 h-64 bg-blue-200/10 rounded-full blur-3xl animate-pulse" />
      <div class="absolute bottom-20 right-20 w-80 h-80 bg-cyan-200/8 rounded-full blur-3xl animate-pulse delay-1000" />
      <div class="absolute top-1/2 left-1/4 w-48 h-48 bg-blue-300/5 rounded-full blur-2xl animate-pulse delay-500" />
    </div>

    <AppSidebar :is-open="isSidebarOpen" @close-sidebar="isSidebarOpen = false" class="no-print" />

    <div :class="['flex flex-col min-h-screen', isSidebarOpen ? 'lg:ml-72' : '']">
      <AppHeader 
        :is-sidebar-open="isSidebarOpen" 
        @toggle-sidebar="isSidebarOpen = !isSidebarOpen" 
        class="no-print"
      />

      <main class="flex-1 relative z-10">
        <div class="p-4 sm:p-6 lg:p-8">
          <slot />
        </div>
      </main>

      <AppFooter class="no-print" />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useAuth } from '~/composables/useAuth'

// Initialisation du state d'auth pour garantir la persistance sur toute la navigation
const { user, token } = useAuth()

const isSidebarOpen = ref(false)
</script>
