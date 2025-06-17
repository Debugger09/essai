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
            {{ getDisplayName(otherUserId) }}
          </h2>
          <p class="text-sm text-gray-500">En ligne</p>
        </div>
      </div>
    </div>

    <!-- Messages -->
    <div class="flex-1 overflow-y-auto p-4 space-y-4" ref="messagesContainer">
      <div
        v-for="message in messages"
        :key="message.id"
        :class="[
          'flex',
          message.senderId === userId ? 'justify-end' : 'justify-start'
        ]"
      >
        <div
          :class="[
            'max-w-[70%] rounded-2xl px-4 py-2 shadow-sm',
            message.senderId === userId
              ? 'bg-gradient-to-r from-blue-600 to-indigo-600 text-white'
              : 'bg-white border border-gray-100'
          ]"
        >
          <p class="text-sm">{{ message.content }}</p>
          <p class="text-xs mt-1" :class="message.senderId === userId ? 'text-blue-100' : 'text-gray-500'">
            {{ new Date(message.timestamp).toLocaleTimeString() }}
          </p>
        </div>
      </div>
    </div>

    <!-- Zone de saisie -->
    <div class="p-4 border-t bg-white/90 backdrop-blur-sm">
      <form @submit.prevent="sendMessage" class="flex items-center space-x-4">
        <div class="flex-1 relative">
          <input
            v-model="newMessage"
            type="text"
            placeholder="Écrivez votre message..."
            class="w-full px-4 py-3 rounded-xl border border-gray-200 bg-gray-50 focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200"
          />
        </div>
        <button
          type="submit"
          class="p-3 rounded-xl bg-gradient-to-r from-blue-600 to-indigo-600 text-white shadow-lg hover:shadow-xl transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
          :disabled="!newMessage.trim()"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8" />
          </svg>
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick, watch } from 'vue'
import { Client } from '@stomp/stompjs'
import { useAuth } from '~/composables/useAuth'
import { useUserInfo } from '~/composables/useUserInfo'

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
const { $axios } = useNuxtApp()
const { token } = useAuth()
const { getInitials, getDisplayName } = useUserInfo()

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
    brokerURL: 'ws://localhost:8080/ws',
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
      const newMessage = JSON.parse(message.body)
      messages.value.push(newMessage)
      scrollToBottom()
    })
  }

  client.value.activate()
}

const fetchMessages = async () => {
  try {
    const response = await $axios.get(`/messages/conversation/${props.conversationId}`)
    messages.value = response.data
    await nextTick()
    scrollToBottom()
  } catch (error) {
    console.error('Erreur lors du chargement des messages:', error)
  }
}

const sendMessage = async () => {
  if (!newMessage.value.trim()) return

  try {
    const message = {
      conversationId: props.conversationId,
      senderId: props.userId,
      content: newMessage.value.trim(),
      timestamp: new Date().toISOString()
    }

    await $axios.post('/messages', message)
    newMessage.value = ''
  } catch (error) {
    console.error('Erreur lors de l\'envoi du message:', error)
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