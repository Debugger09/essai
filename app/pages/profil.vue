<template>
  <div class="min-h-screen flex flex-col items-center justify-center bg-gradient-to-br from-blue-50 via-cyan-50 to-indigo-50 dark:from-gray-900 dark:via-blue-950/50 dark:to-indigo-950/30">
    <div class="w-full max-w-md bg-white dark:bg-gray-900 rounded-2xl shadow-lg p-8 border border-blue-100 dark:border-blue-800 mt-12">
      <div class="flex flex-col items-center mb-6">
        <div class="w-24 h-24 rounded-full bg-blue-100 dark:bg-blue-800 flex items-center justify-center text-4xl font-bold text-blue-700 dark:text-blue-200 mb-2">
          {{ initials }}
        </div>
        <h1 class="text-2xl font-bold text-blue-700 dark:text-blue-200 mb-1">{{ userInfo?.lastName }} {{ userInfo?.firstName }}</h1>
        <span class="text-gray-500 dark:text-gray-400 text-sm">{{ userInfo?.role }}</span>
      </div>
      <div class="space-y-4">
        <div class="flex items-center justify-between">
          <span class="font-medium text-slate-700">Email :</span>
          <span class="text-slate-900 dark:text-slate-200">{{ userInfo?.email }}</span>
        </div>
        <div class="flex items-center justify-between">
          <span class="font-medium text-slate-700">Téléphone :</span>
          <span class="text-slate-900 dark:text-slate-200">{{ userInfo?.phone || 'Non renseigné' }}</span>
        </div>
        <div class="flex items-center justify-between">
          <span class="font-medium text-slate-700">Poste :</span>
          <span class="text-slate-900 dark:text-slate-200">{{ userInfo?.job || 'Non renseigné' }}</span>
        </div>
        <div class="flex items-center justify-between">
          <span class="font-medium text-slate-700">Statut :</span>
          <span class="text-slate-900 dark:text-slate-200">{{ userInfo?.status ? 'Actif' : 'Inactif' }}</span>
        </div>
        <div class="flex items-center justify-between">
          <span class="font-medium text-slate-700">Date d'inscription :</span>
          <span class="text-slate-900 dark:text-slate-200">{{ formatDate(userInfo?.createdAt) }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useUserInfo } from '~/composables/useUserInfo'
import { useFormatters } from '~/composables/useFormatters'
import { computed } from 'vue'

const { userInfo, fetchUserInfo } = useUserInfo()
const { formatDate } = useFormatters()

// Initiales pour l'avatar
const initials = computed(() => {
  if (!userInfo.value) return ''
  const f = userInfo.value.firstName?.charAt(0) || ''
  const l = userInfo.value.lastName?.charAt(0) || ''
  return (l + f).toUpperCase()
})

fetchUserInfo()
</script> 