<template>
  <div class="min-h-screen flex flex-col items-center justify-center bg-gradient-to-br from-blue-50 via-cyan-50 to-indigo-50 dark:from-gray-900 dark:via-blue-950/50 dark:to-indigo-950/30">
    <div class="w-full max-w-md bg-white dark:bg-gray-900 rounded-2xl shadow-lg p-8 border border-blue-100 dark:border-blue-800">
      <div class="flex flex-col items-center mb-6">
        <img src="/assets/images/logo.png" alt="CollabSpace Logo" class="h-16 mb-2" />
        <h1 class="text-2xl font-bold text-blue-700 dark:text-blue-200 mb-1">Mot de passe oublié</h1>
        <p class="text-gray-600 dark:text-gray-400 text-center">Saisis ton adresse e-mail pour recevoir un lien de réinitialisation.</p>
      </div>
      <form @submit.prevent="submit" class="space-y-4">
        <div>
          <label for="email" class="block text-sm font-medium text-gray-700 dark:text-gray-300">Adresse e-mail</label>
          <input v-model="email" id="email" type="email" required autocomplete="email" class="mt-1 w-full px-4 py-2 rounded-lg border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500" />
        </div>
        <button type="submit" :disabled="loading" class="w-full py-2 px-4 rounded-lg bg-blue-600 text-white font-semibold hover:bg-blue-700 transition disabled:opacity-50">
          Envoyer le lien
        </button>
      </form>
      <div v-if="success" class="mt-6 p-4 bg-green-50 dark:bg-green-900/30 text-green-700 dark:text-green-300 rounded-lg text-center">
        Un e-mail de réinitialisation a été envoyé si l'adresse existe dans notre système.<br/>
        <span class="block mt-2 text-xs text-gray-500">Vérifie tes spams si tu ne le trouves pas.</span>
      </div>
      <div v-if="error" class="mt-6 p-4 bg-red-50 dark:bg-red-900/30 text-red-700 dark:text-red-300 rounded-lg text-center">
        {{ error }}
      </div>
      <div class="mt-6 text-center">
        <NuxtLink to="/login" class="text-blue-600 hover:underline font-medium">Retour à la connexion</NuxtLink>
      </div>
      <div v-if="showMailPreview" class="mt-8 border-t pt-6">
        <h2 class="text-lg font-semibold mb-2 text-blue-700 dark:text-blue-200">Aperçu du mail envoyé</h2>
        <div class="bg-white dark:bg-gray-800 rounded-lg shadow p-4 border border-blue-100 dark:border-blue-700">
          <div class="flex items-center mb-4">
            <img src="/assets/images/logo.png" alt="CollabSpace Logo" class="h-10 mr-3" />
            <span class="text-xl font-bold text-blue-700">CollabSpace</span>
          </div>
          <p class="text-gray-700 dark:text-gray-200 mb-2">Bonjour,</p>
          <p class="text-gray-700 dark:text-gray-200 mb-2">
            Vous avez demandé la réinitialisation de votre mot de passe sur <span class="font-semibold text-blue-700">CollabSpace</span>.<br/>
            Cliquez sur le bouton ci-dessous pour choisir un nouveau mot de passe :
          </p>
          <div class="my-4 text-center">
            <a href="#LIEN_RESET#" class="inline-block px-6 py-2 rounded-lg bg-blue-600 text-white font-semibold shadow hover:bg-blue-700 transition">Réinitialiser mon mot de passe</a>
          </div>
          <p class="text-gray-700 dark:text-gray-200 mb-2">
            Si vous n'êtes pas à l'origine de cette demande, ignorez simplement ce message.
          </p>
          <p class="text-gray-500 dark:text-gray-400 text-xs mt-4">L'équipe CollabSpace</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useNuxtApp } from '#app'

const email = ref('')
const loading = ref(false)
const success = ref(false)
const error = ref('')
const showMailPreview = ref(false)
const { $axios } = useNuxtApp()

const submit = async () => {
  error.value = ''
  success.value = false
  showMailPreview.value = false
  loading.value = true
  try {
    await $axios.post('/resetPassword/forgot', { email: email.value })
    success.value = true
    showMailPreview.value = true
  } catch (e) {
    error.value = e.response?.data?.message || "Erreur lors de l'envoi du mail."
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
body {
  background: linear-gradient(135deg, #e0e7ff 0%, #f0f9ff 100%);
}
</style> 