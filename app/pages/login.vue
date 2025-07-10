<template>
  <div class="min-h-screen flex items-center justify-center relative overflow-hidden bg-slate-100 dark:bg-gray-900 transition-colors duration-300">
    <!-- Background gradients -->
    <div class="absolute inset-0 bg-gradient-to-br from-slate-100 via-blue-50 to-cyan-50 dark:from-gray-900 dark:via-blue-950 dark:to-cyan-900 transition-colors duration-300">
      <div class="absolute inset-0 bg-gradient-to-tl from-blue-900/10 via-blue-600/5 to-cyan-400/10 dark:from-blue-900/30 dark:via-blue-600/10 dark:to-cyan-400/20"></div>
    </div>
    <div class="absolute top-20 left-20 w-64 h-64 bg-blue-200 bg-opacity-20 dark:bg-blue-900/30 rounded-full blur-3xl animate-pulse"></div>
    <div class="absolute bottom-20 right-20 w-80 h-80 bg-cyan-200 bg-opacity-15 dark:bg-cyan-900/20 rounded-full blur-3xl animate-pulse delay-1000"></div>
    <div class="absolute top-1/2 left-1/4 w-48 h-48 bg-blue-300 bg-opacity-10 dark:bg-blue-900/20 rounded-full blur-2xl animate-pulse delay-500"></div>

    <div class="relative z-10 w-full max-w-sm mx-4">
      <div class="bg-white dark:bg-gray-800 rounded-2xl border border-gray-200 dark:border-gray-700 shadow-2xl p-6 transition-colors duration-300">
        <div class="text-center mb-6">
          <div class="inline-flex items-center justify-center w-20 h-20 bg-white dark:bg-gray-900 rounded-2xl border border-gray-200 dark:border-gray-700 mb-4 shadow-lg">
            <img src="/assets/images/logo.png" alt="CollabSpace Logo" class="h-12 w-12" />
          </div>
          <h1 class="text-2xl font-bold mb-1">
            <span class="text-blue-900 dark:text-blue-200">Collab</span><span class="text-cyan-600 dark:text-cyan-400">Space</span>
          </h1>
          <p class="text-slate-600 dark:text-slate-300 text-sm">Bienvenue sur votre espace</p>
        </div>

        <form @submit.prevent="handleLogin" class="space-y-4">
          <div>
            <label for="email" class="block text-xs font-medium text-slate-700 dark:text-slate-200 mb-1.5">
              Adresse email
            </label>
            <div class="relative">
              <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-slate-400 dark:text-slate-500">
                <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                </svg>
              </span>
              <input
                v-model="email"
                type="email"
                id="email"
                name="email"
                autocomplete="email"
                required
                class="w-full pl-10 pr-3 py-2.5 bg-white dark:bg-gray-900 border border-gray-300 dark:border-gray-700 rounded-lg text-slate-800 dark:text-slate-100 placeholder-slate-400 dark:placeholder-slate-500 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500/30 focus:border-blue-500 transition duration-300"
                placeholder="votre@email.com"
              />
            </div>
          </div>

          <div>
            <label for="password" class="block text-xs font-medium text-slate-700 dark:text-slate-200 mb-1.5">
              Mot de passe
            </label>
            <div class="relative">
              <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-slate-400 dark:text-slate-500">
                <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                </svg>
              </span>
              <input
                v-model="password"
                :type="showPassword ? 'text' : 'password'"
                id="password"
                name="password"
                autocomplete="current-password"
                required
                class="w-full pl-10 pr-10 py-2.5 bg-white dark:bg-gray-900 border border-gray-300 dark:border-gray-700 rounded-lg text-slate-800 dark:text-slate-100 placeholder-slate-400 dark:placeholder-slate-500 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500/30 focus:border-blue-500 transition duration-300"
                placeholder="••••••••"
              />
              <button
                type="button"
                @click="showPassword = !showPassword"
                class="absolute inset-y-0 right-0 pr-3 flex items-center text-slate-500 dark:text-slate-400 hover:text-slate-700 dark:hover:text-slate-200 transition duration-200"
              >
                <svg v-if="showPassword" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                </svg>
                <svg v-else class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.878 9.878L3 3m6.878 6.878L21 21" />
                </svg>
              </button>
            </div>
          </div>

          <!-- Ajout du lien mot de passe oublié sous le formulaire -->
          <div class="mt-4 text-center">
            <NuxtLink to="/reset-password" class="text-blue-600 hover:underline font-medium">Mot de passe oublié&nbsp;?</NuxtLink>
          </div>

          <div v-if="error" class="bg-red-50 dark:bg-red-900/30 border border-red-200 dark:border-red-700 rounded-lg p-3">
            <div class="flex">
              <svg class="h-4 w-4 text-red-400 dark:text-red-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L4.082 16.5c-.77.833.192 2.5 1.732 2.5z" />
              </svg>
              <div class="ml-2">
                <p class="text-xs text-red-700 dark:text-red-200">{{ error }}</p>
              </div>
            </div>
          </div>

          <button
            type="submit"
            :disabled="isLoading || !email || !password"
            class="w-full flex justify-center py-2.5 px-4 bg-gradient-to-r from-blue-900 to-blue-700 dark:from-blue-800 dark:to-blue-600 hover:from-blue-900/90 hover:to-blue-700/90 border border-blue-800/30 rounded-lg shadow-lg text-sm font-medium text-white focus:outline-none focus:ring-2 focus:ring-blue-500/30 disabled:opacity-50 disabled:cursor-not-allowed transition duration-300 transform hover:scale-[1.02] active:scale-[0.98]"
          >
            <span v-if="!isLoading" class="flex items-center">
              <svg class="w-4 h-4 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 16l-4-4m0 0l4-4m-4 4h14m-5 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h7a3 3 0 013 3v1" />
              </svg>
              Se connecter
            </span>
            <span v-else class="flex items-center">
              <svg class="animate-spin -ml-1 mr-2 h-4 w-4 text-white" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              Connexion...
            </span>
          </button>
        </form>

      </div>

      <div class="text-center mt-4">
        <p class="text-xs text-slate-400 dark:text-slate-500">
          <span class="text-blue-900 dark:text-blue-200">Collab</span><span class="text-cyan-500 dark:text-cyan-400">Space</span> v1.0 - © {{ new Date().getFullYear() }}
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useAuth } from '~/composables/useAuth'
import { definePageMeta } from '#imports'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useRuntimeConfig } from '#app'
import axios from 'axios'

const email = ref('')
const password = ref('')
const showPassword = ref(false)
const isLoading = ref(false)
const error = ref(null)

const { setAuth } = useAuth()
const router = useRouter()
const config = useRuntimeConfig()

definePageMeta({
  layout: false
})

const handleLogin = async () => {
  isLoading.value = true
  error.value = null

  try {
    const response = await axios.post(`${config.public.apiBase}/auth/login`, {
      email: email.value,
      password: password.value
    })

    const token = response.data.token
    const user = response.data.user

    setAuth(token, user)
    await router.push('/')
  } catch (err) {
    error.value = err.response?.data?.message || 'Email ou mot de passe incorrect'
    console.error(err)
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
@supports (backdrop-filter: blur(10px)) {
  .backdrop-blur-2xl {
    backdrop-filter: blur(40px);
  }
  .backdrop-blur-lg {
    backdrop-filter: blur(16px);
  }
}
@supports not (backdrop-filter: blur(10px)) {
  .backdrop-blur-2xl {
    background-color: rgba(255, 255, 255, 0.4);
  }
  .backdrop-blur-lg {
    background-color: rgba(255, 255, 255, 0.3);
  }
}
* {
  transition-property: all;
  transition-duration: 300ms;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
}
input:focus {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.1);
}
button:hover:not(:disabled) {
  box-shadow: 0 8px 25px rgba(30, 58, 138, 0.2);
}
@keyframes gentle-float {
  0%, 100% { 
    transform: translateY(0px) scale(1); 
  }
  50% { 
    transform: translateY(-10px) scale(1.05); 
  }
}
</style>
