<template>
  <div class="flex-1 flex flex-col h-full bg-gradient-to-br from-slate-50 via-blue-50/30 to-cyan-50/30">
    <!-- En-tête -->
    <div class="p-4 border-b bg-white/90 backdrop-blur-sm shadow-sm">
      <div class="flex items-center space-x-4">
        <div class="w-12 h-12 rounded-full bg-gradient-to-r from-blue-600 to-indigo-600 flex items-center justify-center text-white font-semibold shadow-lg">
          {{ getInitials(otherUserId) }}
        </div>
        <div>
          <h2 class="text-xl font-bold bg-gradient-to-r from-blue-900 via-blue-700 to-indigo-600 bg-clip-text text-transparent">
            {{ otherUser.firstName ? otherUser.firstName + ' ' + otherUser.lastName : getDisplayName(otherUserId) }}
          </h2>
          <p class="text-sm text-gray-500">
            <span v-if="Array.isArray(onlineUserIds) && onlineUserIds.includes(otherUserId)">En ligne</span>
            <span v-else>Hors ligne</span>
          </p>
        </div>
      </div>
    </div>

    <!-- Zone des messages -->
    <div v-if="loading" class="flex-1 flex items-center justify-center bg-[#E5DDD5]">
      <svg class="animate-spin h-8 w-8 text-blue-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
        <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
      </svg>
    </div>
    <div v-if="error && !loading" class="bg-red-100 text-red-700 px-4 py-2 text-sm text-center">{{ error }}</div>
    <div v-show="!loading" ref="messagesContainer" class="flex-1 overflow-y-auto p-2 sm:p-4 space-y-3 bg-[#E5DDD5]">
      <transition-group name="fade-slide" tag="div">
        <div
          v-for="message in messages"
          :key="message.id"
          :class="[ 'flex items-end', message.sender && message.sender.id === userId ? 'justify-end' : 'justify-start']"
        >
          <div class="flex items-end gap-2 max-w-full">
            <div v-if="message.sender && message.sender.id !== userId" class="hidden sm:block">
              <div class="w-8 h-8 rounded-full bg-gradient-to-r from-blue-600 to-indigo-600 flex items-center justify-center text-white font-semibold text-xs">
                {{ message.sender.firstName ? message.sender.firstName[0] : '?' }}{{ message.sender.lastName ? message.sender.lastName[0] : '' }}
              </div>
            </div>
            <div
              :class="[
                'rounded-2xl px-4 py-2 shadow-md break-words',
                message.sender && message.sender.id === userId
                  ? 'bg-gradient-to-r from-blue-600 to-indigo-600 text-white self-end'
                  : 'bg-white text-gray-800 border border-gray-200'
              ]"
              style="max-width: 80vw; min-width: 40px;"
            >
              <p class="text-xs font-semibold mb-1" v-if="message.sender && message.sender.id !== userId">
                {{ message.sender.firstName }} {{ message.sender.lastName }}
              </p>
              <p class="text-sm">{{ message.content }}</p>
              <span class="text-[10px] text-gray-400 mt-1 block text-right">
                {{ message.createdAt ? new Date(message.createdAt).toLocaleTimeString() : '' }}
              </span>
            </div>
          </div>
        </div>
      </transition-group>
    </div>
    <!-- Zone de saisie -->
    <div class="bg-white border-t p-2 sm:p-4">
      <form @submit.prevent="sendMessage" class="flex items-center gap-2 sm:gap-4">
        <input
          v-model="newMessage"
          type="text"
          class="flex-1 border rounded-full px-4 py-2 sm:px-6 sm:py-3 focus:outline-none focus:border-blue-500 bg-gray-50 text-sm sm:text-base"
          placeholder="Écrivez votre message..."
          :disabled="sending"
          @keyup.enter.exact.prevent="sendMessage"
        />
        <button
          type="submit"
          :disabled="!newMessage.trim() || sending"
          class="bg-gradient-to-r from-blue-600 to-indigo-600 text-white px-4 py-2 sm:px-6 sm:py-3 rounded-full hover:from-blue-700 hover:to-indigo-700 focus:outline-none transition-all duration-200 flex items-center justify-center disabled:opacity-50 disabled:cursor-not-allowed"
        >
          <svg v-if="!sending" xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
            <path d="M10.894 2.553a1 1 0 00-1.788 0l-7 14a1 1 0 001.169 1.409l5-1.429A1 1 0 009 15.571V11a1 1 0 112 0v4.571a1 1 0 00.725.962l5 1.428a1 1 0 001.17-1.408l-7-14z" />
          </svg>
          <svg v-else class="animate-spin h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
          </svg>
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick, watch, computed } from 'vue'
import { Client } from '@stomp/stompjs'
import SockJS from 'sockjs-client'
import { useAuth } from '~/composables/useAuth'
import { useUserInfo } from '~/composables/useUserInfo'
import { usePresence } from '~/composables/usePresence'

const props = defineProps({
  conversationId: {
    type: Number,
    required: true
  },
  userId: {
    type: Number,
    required: true
  },
  otherUserId: {
    type: Number,
    required: true
  }
})

const messages = ref([])
const newMessage = ref('')
const client = ref(null)
const messagesContainer = ref(null)
const loading = ref(false)
const error = ref('')
const sending = ref(false)
const { $axios } = useNuxtApp()
const { token } = useAuth()
const { getInitials, getDisplayName, users } = useUserInfo()
const { onlineUserIds } = usePresence()

const otherUser = computed(() => users.value.find(u => u.id === props.otherUserId) || {})

onMounted(async () => {
  await fetchMessages()
  setupWebSocket()
})

onUnmounted(() => {
  if (client.value) {
    client.value.deactivate()
  }
})

const setupWebSocket = () => {
  client.value = new Client({
    webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
    connectHeaders: {
      Authorization: `Bearer ${token.value}`
    },
    debug: (str) => {
      console.log(str)
    },
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
  })

  client.value.onConnect = () => {
    console.log('Connected to WebSocket')
    client.value?.subscribe(`/topic/conversation/${props.conversationId}`, (message) => {
      console.log('[WebSocket][Chat.vue] message reçu:', message.body)
      const newMessage = JSON.parse(message.body)
      // Filtre anti-doublon par id
      if (!messages.value.some(m => m.id === newMessage.id)) {
        messages.value.push(newMessage)
        scrollToBottom()
      }
    })
  }

  client.value.activate()
}

const fetchMessages = async () => {
  loading.value = true
  error.value = ''
  try {
    const response = await $axios.get(`/messages/conversation/${props.conversationId}`)
    messages.value = response.data
    await nextTick()
    scrollToBottom()
  } catch (e) {
    console.error('Erreur lors du chargement des messages:', e)
    error.value = 'Erreur lors du chargement des messages.'
    messages.value = []
  } finally {
    loading.value = false
  }
}

const sendMessage = async () => {
  if (!newMessage.value.trim()) return
  sending.value = true
  error.value = ''
  try {
    const message = {
      conversationId: props.conversationId,
      sender: { id: props.userId },
      receiver: { id: props.otherUserId },
      content: newMessage.value.trim()
    }
    await $axios.post('/messages', message)
    newMessage.value = ''
    // On NE FAIT PAS : messages.value.push(...)
    // On attend la notification WebSocket
  } catch (e) {
    console.error('Erreur lors de l\'envoi du message:', e)
    error.value = 'Erreur lors de l\'envoi du message.'
  } finally {
    sending.value = false
  }
}

const scrollToBottom = () => {
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
  }
}

watch(() => props.conversationId, async () => {
  messages.value = []
  await fetchMessages()
})
</script> 