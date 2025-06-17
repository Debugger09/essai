<template>
  <div>
    <!-- Overlay pour mobile -->
    <div 
      v-if="isOpen" 
      class="fixed inset-0 bg-black/20 backdrop-blur-sm z-40 lg:hidden" 
      @click="closeSidebar"
    />

    <!-- Sidebar -->
    <aside
      :class="[
        { '-translate-x-full': !isOpen },
        { 'translate-x-0': isOpen },
        'fixed inset-y-0 left-0 w-72 bg-white/95 backdrop-blur-xl shadow-md border-r border-gray-200/50 z-50 transform transition-all duration-300 ease-out'
      ]"
    >
      <!-- Header avec gradient -->
      <div class="relative h-16 bg-gradient-to-br from-blue-900 via-blue-800 to-cyan-700 border-b border-blue-700/30">
        <!-- Effets de flou décoratifs -->
        <div class="absolute top-2 right-4 w-16 h-16 bg-cyan-400/20 rounded-full blur-xl" />
        <div class="absolute bottom-2 left-4 w-12 h-12 bg-blue-300/15 rounded-full blur-lg" />

        <div class="relative flex items-center justify-between h-full px-6">
          <NuxtLink to="/dashboard" class="flex items-center group">
            <div class="w-8 h-8 bg-white/10 backdrop-blur-sm rounded-xl flex items-center justify-center border border-white/20 group-hover:scale-105 transition-transform duration-300">
              <img src="/assets/images/logo.png" alt="Logo" class="w-5 h-5 filter brightness-0 invert" />
            </div>
            <div class="ml-3">
              <span class="text-lg font-bold text-white">
                Collab<span class="text-cyan-300">Space</span>
              </span>
            </div>
          </NuxtLink>

          <button
            @click="closeSidebar"
            class="p-2 rounded-xl text-white/80 hover:text-white hover:bg-white/10 transition-all duration-200"
          >
            <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
      </div>

      <!-- Navigation -->
      <nav class="p-3 space-y-1.5 overflow-y-auto max-h-[calc(100vh-4rem)]">
        <NuxtLink
          v-for="link in navigationLinks"
          :key="link.name"
          :to="link.to"
          :class="[
            'group flex items-center px-3 py-2.5 text-sm font-medium rounded-lg transition-all duration-200 transform hover:scale-[1.01] active:scale-[0.99]',
            $route.path === link.to
              ? 'bg-gradient-to-r from-blue-900 to-blue-700 text-white shadow-md'
              : 'text-slate-700 hover:bg-gradient-to-r hover:from-blue-50 hover:to-cyan-50 hover:text-blue-900'
          ]"
        >
          <div
            :class="[
              'p-1.5 rounded-lg transition-all duration-200',
              $route.path === link.to
                ? 'bg-white/20 text-white'
                : 'bg-slate-100 text-slate-600 group-hover:bg-blue-100 group-hover:text-blue-700'
            ]"
          >
            <component :is="link.icon" class="h-4 w-4" />
          </div>
          <span class="ml-3">{{ link.name }}</span>
          <div v-if="$route.path === link.to" class="ml-auto w-1.5 h-1.5 bg-cyan-300 rounded-full animate-pulse" />
        </NuxtLink>
      </nav>
    </aside>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useAuth } from '~/composables/useAuth'
import {
  HomeIcon,
  FolderIcon,
  UsersIcon,
  BriefcaseIcon,
  DocumentTextIcon,
  Cog6ToothIcon,
  CurrencyDollarIcon,
  ComputerDesktopIcon,
  ChatBubbleLeftRightIcon
} from '@heroicons/vue/24/outline'

const props = defineProps({
  isOpen: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['close-sidebar'])

const { user } = useAuth()

const hasAccess = (requiredRoles) => {
  if (!user.value || !user.value.role) {
    return false
  }
  return requiredRoles.includes(user.value.role)
}

const navigationLinks = computed(() => {
  return [
    { name: 'Dashboard', to: '/dashboard', icon: HomeIcon, roles: ['ADMIN', 'CHEF_PROJET', 'EMPLOYE'] },
    { name: 'Utilisateurs', to: '/users', icon: UsersIcon, roles: ['ADMIN'] },
    { name: 'Projets', to: '/projets', icon: FolderIcon, roles: ['ADMIN', 'CHEF_PROJET'] },
    { name: 'Tâches', to: '/taches', icon: BriefcaseIcon, roles: ['ADMIN', 'CHEF_PROJET', 'EMPLOYE'] },
    { name: 'Dépenses', to: '/depenses', icon: CurrencyDollarIcon, roles: ['ADMIN', 'CHEF_PROJET'] },
    { name: 'Matériels', to: '/materiels', icon: ComputerDesktopIcon, roles: ['ADMIN', 'CHEF_PROJET', 'EMPLOYE'] },
    { name: 'Messagerie', to: '/messagerie', icon: ChatBubbleLeftRightIcon, roles: ['ADMIN', 'CHEF_PROJET', 'EMPLOYE'] },
    { name: 'Paramètres', to: '/parametres', icon: Cog6ToothIcon, roles: ['ADMIN', 'CHEF_PROJET', 'EMPLOYE'] },
  ].filter(link => hasAccess(link.roles))
})

const closeSidebar = () => {
  emit('close-sidebar')
}
</script>
