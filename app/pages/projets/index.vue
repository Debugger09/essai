<template>
  <div class="min-h-screen bg-gradient-to-br from-slate-50 via-blue-50/30 to-cyan-50/30 dark:from-gray-900 dark:via-blue-950/50 dark:to-indigo-950/30 transition-all duration-500">
    <!-- Background Elements -->
    <div class="absolute inset-0 overflow-hidden pointer-events-none">
      <div class="absolute top-1/4 left-1/4 w-96 h-96 bg-gradient-to-r from-blue-400/10 to-indigo-400/10 dark:from-blue-600/20 dark:to-indigo-600/20 rounded-full blur-3xl animate-pulse"></div>
      <div class="absolute bottom-1/4 right-1/4 w-80 h-80 bg-gradient-to-r from-indigo-400/8 to-purple-400/8 dark:from-indigo-600/15 dark:to-purple-600/15 rounded-full blur-3xl animate-pulse delay-1000"></div>
    </div>

    <div class="relative z-10 container mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-8 lg:py-12">
      <!-- Header Section -->
      <div class="mb-8 sm:mb-12">
        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <div>
            <h1 class="text-3xl sm:text-4xl lg:text-5xl font-bold bg-gradient-to-r from-blue-900 via-blue-700 to-indigo-600 dark:from-blue-400 dark:via-blue-300 dark:to-indigo-300 bg-clip-text text-transparent mb-3 sm:mb-4">
              Gestion des Projets
            </h1>
            <p class="text-slate-600 dark:text-slate-400 text-base sm:text-lg lg:text-xl max-w-2xl">
              Suivez et gérez vos projets en cours
            </p>
          </div>
          
          <!-- Bouton Ajouter -->
          <button 
            @click="openAddModal"
            :disabled="loading"
            class="group relative bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 disabled:from-gray-400 disabled:to-gray-500 text-white px-6 py-3 rounded-2xl font-semibold shadow-lg hover:shadow-xl transition-all duration-300 hover:scale-105 disabled:hover:scale-100 flex items-center space-x-2"
          >
            <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
            </svg>
            <span>{{ loading ? 'Chargement...' : 'Ajouter un projet' }}</span>
          </button>
        </div>
      </div>

      <!-- Stats Cards -->
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 sm:gap-6 mb-8 sm:mb-12">
        <div class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-2xl shadow-lg border border-white/20 dark:border-gray-700/50 p-4 sm:p-6 transition-all duration-300 hover:scale-[1.02]">
          <div class="flex items-center space-x-3">
            <div class="p-3 bg-blue-100 dark:bg-blue-900/30 rounded-xl">
              <svg class="h-6 w-6 text-blue-600 dark:text-blue-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
              </svg>
            </div>
            <div>
              <p class="text-2xl font-bold text-slate-800 dark:text-slate-100">{{ totalProjets }}</p>
              <p class="text-sm text-slate-600 dark:text-slate-400">Total des projets</p>
            </div>
          </div>
        </div>

        <div class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-2xl shadow-lg border border-white/20 dark:border-gray-700/50 p-4 sm:p-6 transition-all duration-300 hover:scale-[1.02]">
          <div class="flex items-center space-x-3">
            <div class="p-3 bg-green-100 dark:bg-green-900/30 rounded-xl">
              <svg class="h-6 w-6 text-green-600 dark:text-green-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <div>
              <p class="text-2xl font-bold text-slate-800 dark:text-slate-100">{{ projetsEnCours }}</p>
              <p class="text-sm text-slate-600 dark:text-slate-400">Projets en cours</p>
            </div>
          </div>
        </div>

        <div class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-2xl shadow-lg border border-white/20 dark:border-gray-700/50 p-4 sm:p-6 transition-all duration-300 hover:scale-[1.02]">
          <div class="flex items-center space-x-3">
            <div class="p-3 bg-amber-100 dark:bg-amber-900/30 rounded-xl">
              <svg class="h-6 w-6 text-amber-600 dark:text-amber-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <div>
              <p class="text-2xl font-bold text-slate-800 dark:text-slate-100">{{ formatMontant(totalBudget) }}</p>
              <p class="text-sm text-slate-600 dark:text-slate-400">Budget total</p>
            </div>
          </div>
        </div>

        <div class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-2xl shadow-lg border border-white/20 dark:border-gray-700/50 p-4 sm:p-6 transition-all duration-300 hover:scale-[1.02]">
          <div class="flex items-center space-x-3">
            <div class="p-3 bg-purple-100 dark:bg-purple-900/30 rounded-xl">
              <svg class="h-6 w-6 text-purple-600 dark:text-purple-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
              </svg>
            </div>
            <div>
              <p class="text-2xl font-bold text-slate-800 dark:text-slate-100">{{ totalChefsProjet }}</p>
              <p class="text-sm text-slate-600 dark:text-slate-400">Chefs de projet</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Filtres et Recherche -->
      <div class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-2xl shadow-lg border border-white/20 dark:border-gray-700/50 p-4 sm:p-6 mb-8">
        <div class="flex flex-col sm:flex-row gap-4">
          <div class="flex-1">
            <div class="relative">
              <svg class="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
              </svg>
              <input
                v-model="searchTerm"
                type="text"
                placeholder="Rechercher un projet..."
                class="w-full pl-10 pr-4 py-3 bg-slate-50 dark:bg-gray-700 border border-slate-200 dark:border-gray-600 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200"
              />
            </div>
          </div>
          
          <div class="flex gap-2">
            <select 
              v-model="selectedStatus"
              class="px-4 py-3 bg-slate-50 dark:bg-gray-700 border border-slate-200 dark:border-gray-600 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200"
            >
              <option value="">Tous les statuts</option>
              <option value="EN_COURS">En cours</option>
              <option value="TERMINE">Terminé</option>
              <option value="A_VENIR">À venir</option>
            </select>
          </div>
        </div>
      </div>

      <!-- Grille des Projets -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div 
          v-for="projet in filteredProjets" 
          :key="projet.id"
          class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm rounded-2xl shadow-lg border border-white/20 dark:border-gray-700/50 overflow-hidden transition-all duration-300 hover:scale-[1.02] hover:shadow-xl"
        >
          <!-- En-tête du projet -->
          <div class="p-6 border-b border-slate-200 dark:border-gray-700">
            <div class="flex items-center justify-between mb-4">
              <h3 class="text-xl font-bold text-slate-900 dark:text-slate-100">{{ projet.name }}</h3>
              <span 
                :class="[
                  'px-3 py-1 rounded-full text-sm font-medium',
                  {
                    'bg-green-100 text-green-800 dark:bg-green-900/30 dark:text-green-400': projet.status === 'EN_COURS',
                    'bg-blue-100 text-blue-800 dark:bg-blue-900/30 dark:text-blue-400': projet.status === 'A_VENIR',
                    'bg-purple-100 text-purple-800 dark:bg-purple-900/30 dark:text-purple-400': projet.status === 'TERMINE'
                  }
                ]"
              >
                {{ formatStatus(projet.status) }}
              </span>
            </div>
            <p class="text-slate-600 dark:text-slate-400 line-clamp-2">{{ projet.description }}</p>
          </div>

          <!-- Informations du projet -->
          <div class="p-6 space-y-4">
            <div class="flex items-center space-x-3">
              <svg class="h-5 w-5 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              <div>
                <p class="text-sm text-slate-500 dark:text-slate-400">Date de début</p>
                <p class="text-slate-900 dark:text-slate-100">{{ formatDate(projet.dateDebut) }}</p>
              </div>
            </div>

            <div class="flex items-center space-x-3">
              <svg class="h-5 w-5 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
              </svg>
              <div>
                <p class="text-sm text-slate-500 dark:text-slate-400">Budget</p>
                <p class="text-slate-900 dark:text-slate-100">{{ formatMontant(projet.budget) }}</p>
              </div>
            </div>

            <div class="flex items-center space-x-3">
              <svg class="h-5 w-5 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
              </svg>
              <div>
                <p class="text-sm text-slate-500 dark:text-slate-400">Chef de projet</p>
                <p class="text-slate-900 dark:text-slate-100">
                  <span v-if="projet.chefProjet">
                    {{ projet.chefProjet.lastName }} {{ projet.chefProjet.firstName }}
                  </span>
                  <span v-else>Non assigné</span>
                </p>
              </div>
            </div>
          </div>

          <!-- Actions -->
          <div class="px-6 py-4 bg-slate-50 dark:bg-gray-700/50 border-t border-slate-200 dark:border-gray-700 flex justify-between items-center sticky bottom-0 z-10">
            <NuxtLink 
              :to="`/projets/${projet.id}`"
              class="inline-flex items-center px-4 py-2 bg-blue-50 dark:bg-blue-900/30 text-blue-600 dark:text-blue-400 hover:bg-blue-100 dark:hover:bg-blue-900/50 rounded-lg transition-all duration-200"
            >
              <svg class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
              </svg>
              Voir détails
            </NuxtLink>
            <button 
              @click="deleteProjet(projet)"
              :disabled="deleting === projet.id"
              class="p-2 text-red-600 hover:text-red-800 dark:text-red-400 dark:hover:text-red-300 hover:bg-red-50 dark:hover:bg-red-900/20 rounded-lg transition-all duration-200 disabled:opacity-50"
              title="Supprimer"
            >
              <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
              </svg>
            </button>
          </div>
        </div>
      </div>

      <!-- Message si aucun projet -->
      <div v-if="filteredProjets.length === 0 && !loading" class="text-center py-12">
        <svg class="mx-auto h-12 w-12 text-slate-400 dark:text-slate-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
        </svg>
        <h3 class="mt-2 text-sm font-medium text-slate-900 dark:text-slate-100">Aucun projet trouvé</h3>
      </div>
    </div>

    <!-- Modal d'ajout/modification -->
    <div v-if="showModal" class="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-start justify-center z-[9999] p-4" @click.self="closeModal">
      <div class="bg-white/95 dark:bg-gray-800/95 backdrop-blur-sm rounded-3xl shadow-2xl max-w-lg w-full p-6 sm:p-8 border border-white/20 dark:border-gray-700/50 transform transition-all duration-300 scale-100 my-8 max-h-screen overflow-y-auto">
        <!-- Header du modal -->
        <div class="flex items-center justify-between mb-6">
          <div class="flex items-center space-x-3">
            <div class="p-3 bg-blue-100 dark:bg-blue-900/30 rounded-2xl">
              <svg class="h-6 w-6 text-blue-600 dark:text-blue-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
              </svg>
            </div>
            <h3 class="text-xl font-bold text-slate-900 dark:text-slate-100">
              {{ isEditing ? 'Modifier le projet' : 'Ajouter un projet' }}
            </h3>
          </div>
          <button 
            @click="closeModal"
            class="p-2 text-slate-400 hover:text-slate-600 dark:hover:text-slate-300 hover:bg-slate-100 dark:hover:bg-gray-700 rounded-xl transition-all duration-200"
          >
            <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        <form @submit.prevent="submitForm" class="space-y-6">
          <div>
            <label for="name" class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">
              Titre du projet <span class="text-red-500">*</span>
            </label>
            <input
              type="text"
              id="name"
              v-model="form.name"
              required
              placeholder="Titre du projet"
              class="w-full px-4 py-3 bg-slate-50 dark:bg-gray-700 border border-slate-200 dark:border-gray-600 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200"
            />
          </div>

          <div>
            <label for="description" class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">
              Description
            </label>
            <textarea
              id="description"
              v-model="form.description"
              rows="3"
              class="w-full px-4 py-3 bg-slate-50 dark:bg-gray-700 border border-slate-200 dark:border-gray-600 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200"
            ></textarea>
          </div>

          <div>
            <label for="budget" class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">
              Budget (FCFA) <span class="text-red-500">*</span>
            </label>
            <div class="relative">
              <input
                type="number"
                id="budget"
                v-model="form.budget"
                required
                step="1"
                min="0"
                class="w-full px-4 py-3 bg-slate-50 dark:bg-gray-700 border border-slate-200 dark:border-gray-600 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200 pr-12"
              />
              <div class="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none">
                <span class="text-slate-500 dark:text-slate-400">FCFA</span>
              </div>
            </div>
          </div>

          <div>
            <label for="dateDebut" class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">
              Date de début <span class="text-red-500">*</span>
            </label>
            <input
              type="date"
              id="dateDebut"
              v-model="form.dateDebut"
              required
              class="w-full px-4 py-3 bg-slate-50 dark:bg-gray-700 border border-slate-200 dark:border-gray-600 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200"
            />
          </div>

          <div>
            <label for="dateFin" class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">
              Date de fin
            </label>
            <input
              type="date"
              id="dateFin"
              v-model="form.dateFin"
              class="w-full px-4 py-3 bg-slate-50 dark:bg-gray-700 border border-slate-200 dark:border-gray-600 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200"
            />
          </div>

    <div>
            <label for="status" class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">
              Statut <span class="text-red-500">*</span>
            </label>
            <select
              id="status"
              v-model="form.status"
              required
              class="w-full px-4 py-3 bg-slate-50 dark:bg-gray-700 border border-slate-200 dark:border-gray-600 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200"
            >
              <option value="EN_COURS">En cours</option>
              <option value="TERMINE">Terminé</option>
              <option value="A_VENIR">À venir</option>
            </select>
          </div>

          <div>
            <label for="chefProjetId" class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">
              Chef de projet <span class="text-red-500">*</span>
            </label>
            <select
              id="chefProjetId"
              v-model="form.chefProjetId"
              required
              class="w-full px-4 py-3 bg-slate-50 dark:bg-gray-700 border border-slate-200 dark:border-gray-600 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200"
            >
              <option value="" disabled>Sélectionner un chef de projet</option>
              <option v-for="u in chefsProjet" :key="u.id" :value="u.id">
                {{ u.lastName }} {{ u.firstName }}
              </option>
            </select>
          </div>

          <!-- Boutons d'action -->
          <div class="flex flex-col sm:flex-row justify-end space-y-3 sm:space-y-0 sm:space-x-3 pt-6 border-t border-slate-200 dark:border-gray-600">
            <button 
              type="button"
              @click="closeModal"
              :disabled="submitting"
              class="px-6 py-3 text-slate-600 dark:text-slate-400 hover:text-slate-800 dark:hover:text-slate-200 hover:bg-slate-100 dark:hover:bg-gray-700 rounded-xl transition-all duration-200 font-medium disabled:opacity-50"
            >
              Annuler
            </button>
            <button 
              type="submit"
              :disabled="submitting || !isFormValid"
              class="px-6 py-3 bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 disabled:from-gray-400 disabled:to-gray-500 text-white rounded-xl font-semibold shadow-lg hover:shadow-xl transition-all duration-300 hover:scale-105 disabled:hover:scale-100 flex items-center justify-center space-x-2"
            >
              <svg v-if="submitting" class="animate-spin h-4 w-4" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              <span>{{ submitting ? 'Enregistrement...' : (isEditing ? 'Modifier' : 'Ajouter') }}</span>
            </button>
          </div>
        </form>
      </div>
    </div>
    </div>
</template>

<script setup>
import { ref, computed, onMounted, reactive } from 'vue'
import { useRuntimeConfig } from '#app'
import { useNuxtApp } from '#app'
import { useAuth } from '~/composables/useAuth'
import { useUserInfo } from '~/composables/useUserInfo'
const { users, fetchUsers } = useUserInfo()
const { user } = useAuth()

// Configuration
const config = useRuntimeConfig()
const { $axios } = useNuxtApp()
const apiBase = config.public.apiBase || 'http://localhost:8080/api'

// État réactif
const projets = ref([])
const loading = ref(false)
const deleting = ref(null)
const searchTerm = ref('')
const selectedStatus = ref('')

// Modal
const showModal = ref(false)
const isEditing = ref(false)
const submitting = ref(false)
const submitError = ref('')

// Formulaire
const form = reactive({
  id: null,
  name: '',
  description: '',
  budget: 0,
  dateDebut: '',
  dateFin: '',
  status: 'EN_COURS',
  chefProjetId: null
})

// Computed properties
const filteredProjets = computed(() => {
  return projets.value.filter(projet => {
    const matchesSearch = projet.name.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
                         projet.description?.toLowerCase().includes(searchTerm.value.toLowerCase())
    const matchesStatus = !selectedStatus.value || projet.status === selectedStatus.value
    return matchesSearch && matchesStatus
  })
})

const totalProjets = computed(() => projets.value.length)

const projetsEnCours = computed(() => {
  return projets.value.filter(p => p.status === 'EN_COURS').length
})

const totalBudget = computed(() => {
  return projets.value.reduce((total, projet) => total + projet.budget, 0)
})

const totalChefsProjet = computed(() => {
  const chefsProjet = new Set(projets.value.map(p => p.chefProjet?.id).filter(Boolean))
  return chefsProjet.size
})

const chefsProjet = computed(() => users.value.filter(u => u.role === 'CHEF_PROJET'))

const isFormValid = computed(() => {
  return form.name && form.name.trim().length > 0 && form.budget > 0 && form.dateDebut && form.status
})

// Méthodes utilitaires
const formatDate = (date) => {
  if (!date) return 'Non définie'
  return new Date(date).toLocaleDateString('fr-FR', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  })
}

const formatMontant = (montant) => {
  return new Intl.NumberFormat('fr-FR', {
    minimumFractionDigits: 0,
    maximumFractionDigits: 0
  }).format(montant) + ' FCFA'
}

const formatStatus = (status) => {
  const statusMap = {
    'EN_COURS': 'En cours',
    'TERMINE': 'Terminé',
    'A_VENIR': 'À venir'
  }
  return statusMap[status] || status
}

const resetForm = () => {
  form.id = null
  form.name = ''
  form.description = ''
  form.budget = 0
  form.dateDebut = ''
  form.dateFin = ''
  form.status = 'EN_COURS'
  form.chefProjetId = null
  submitError.value = ''
}

// Méthodes du modal
const openAddModal = () => {
  resetForm()
  isEditing.value = false
  showModal.value = true
  console.log('Modal ouvert pour ajout de projet', showModal.value)
}

const openEditModal = (projet) => {
  resetForm()
  form.id = projet.id
  form.name = projet.name
  form.description = projet.description
  form.budget = projet.budget
  form.dateDebut = projet.dateDebut ? new Date(projet.dateDebut).toISOString().split('T')[0] : ''
  form.dateFin = projet.dateFin ? new Date(projet.dateFin).toISOString().split('T')[0] : ''
  form.status = projet.status
  form.chefProjetId = projet.chefProjet?.id || null
  isEditing.value = true
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
  resetForm()
}

// Méthodes API
const loadProjets = async () => {
  loading.value = true
  try {
    const response = await $axios.get('/projets')
    projets.value = response.data || []
  } catch (error) {
    console.error('Erreur lors du chargement des projets:', error)
    projets.value = []
  } finally {
    loading.value = false
  }
}

const submitForm = async () => {
  console.log('submitForm called', form)
  if (!isFormValid.value) {
    submitError.value = 'Veuillez remplir tous les champs obligatoires'
    return
  }

  submitting.value = true
  submitError.value = ''

  try {
    const payload = {
      name: form.name.trim(),
      description: form.description.trim(),
      budget: form.budget,
      dateDebut: form.dateDebut,
      dateFin: form.dateFin || null,
      status: form.status,
      chefProjet: form.chefProjetId ? { id: form.chefProjetId } : (user.value ? { id: user.value.id } : null)
    }

    let result
    if (isEditing.value) {
      const response = await $axios.put(`/projets/${form.id}`, payload)
      result = response.data
      // Mettre à jour dans la liste
      const index = projets.value.findIndex(p => p.id === form.id)
      if (index !== -1) {
        projets.value[index] = result
      }
    } else {
      const response = await $axios.post('/projets', payload)
      result = response.data
      // Ajouter à la liste
      projets.value.push(result)
    }

    closeModal()
  } catch (error) {
    console.error('Erreur lors de la soumission:', error)
    submitError.value = 'Une erreur est survenue lors de l\'enregistrement'
  } finally {
    submitting.value = false
  }
}

const deleteProjet = async (projet) => {
  if (!confirm(`Êtes-vous sûr de vouloir supprimer le projet "${projet.name}" ?`)) {
    return
  }

  deleting.value = projet.id
  try {
    await $axios.delete(`/projets/${projet.id}`)
    projets.value = projets.value.filter(p => p.id !== projet.id)
  } catch (error) {
    console.error('Erreur lors de la suppression:', error)
    alert('Une erreur est survenue lors de la suppression')
  } finally {
    deleting.value = null
  }
}

// Initialisation
onMounted(() => {
  loadProjets()
  fetchUsers()
})
</script>