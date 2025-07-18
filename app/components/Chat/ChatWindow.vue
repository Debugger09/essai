<template>
  <div class="flex flex-col h-full bg-gray-50">
    <!-- En-tête de la conversation -->
    <div class="bg-white border-b px-6 py-4 flex items-center justify-between shadow-sm">
      <div class="flex items-center space-x-4">
        <div class="w-10 h-10 rounded-full bg-gradient-to-r from-blue-600 to-indigo-600 flex items-center justify-center text-white font-semibold">
          {{ getInitials(otherUserId) }}
        </div>
        <div>
          <h2 class="text-lg font-semibold text-gray-800">
            {{ otherUser.firstName ? otherUser.firstName + ' ' + otherUser.lastName : getDisplayName(otherUserId) }}
          </h2>
          <p class="text-sm text-gray-500">
            <span v-if="onlineUserIds.includes(otherUserId)">En ligne</span>
            <span v-else>Hors ligne</span>
          </p>
        </div>
      </div>
    </div>

    <!-- Zone des messages -->
    <div ref="messagesContainer" class="flex-1 overflow-y-auto p-4 space-y-4 bg-[#E5DDD5]">
      <div
        v-for="message in messages"
        :key="message.id"
        :class="['flex', message.sender && message.sender.id === currentUserId ? 'justify-end' : 'justify-start']"
      >
        <div
          :class="[
            'max-w-[70%] rounded-lg p-3 shadow-sm',
            message.sender && message.sender.id === currentUserId
              ? 'bg-[#DCF8C6] text-gray-800'
              : 'bg-white text-gray-800'
          ]"
        >
          <p class="text-xs font-semibold mb-1" v-if="message.sender">
            {{ message.sender.firstName }} {{ message.sender.lastName }}
          </p>
          <p class="text-sm">{{ message.content }}</p>
          <span class="text-xs text-gray-500 mt-1 block">
            {{ message.createdAt ? new Date(message.createdAt).toLocaleTimeString() : '' }}
          </span>
        </div>
      </div>
    </div>

    <!-- Zone de saisie -->
    <div class="bg-white border-t p-4">
      <form @submit.prevent="sendMessage" class="flex space-x-2">
        <input
          v-model="newMessage"
          type="text"
          class="flex-1 border rounded-full px-6 py-3 focus:outline-none focus:border-blue-500 bg-gray-50"
          placeholder="Écrivez votre message..."
        />
        <button
          type="submit"
          :disabled="!newMessage.trim()"
          class="bg-gradient-to-r from-blue-600 to-indigo-600 text-white px-6 py-3 rounded-full hover:from-blue-700 hover:to-indigo-700 focus:outline-none transition-all duration-200 flex items-center justify-center disabled:opacity-50 disabled:cursor-not-allowed"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
            <path d="M10.894 2.553a1 1 0 00-1.788 0l-7 14a1 1 0 001.169 1.409l5-1.429A1 1 0 009 15.571V11a1 1 0 112 0v4.571a1 1 0 00.725.962l5 1.428a1 1 0 001.17-1.408l-7-14z" />
          </svg>
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick, watch } from 'vue'
import { Client } from '@stomp/stompjs'
import SockJS from 'sockjs-client'
import { useAuth } from '~/composables/useAuth'
import { useUserInfo } from '~/composables/useUserInfo'
import { usePresence } from '~/composables/usePresence'

const props = defineProps({
  conversationId: { type: Number, required: true },
  currentUserId: { type: Number, required: true },
  otherUserId: { type: Number, required: true },
  otherUser: { type: Object, default: () => ({ firstName: '', lastName: '' }) }
})

const messages = ref([])
const newMessage = ref('')
const client = ref(null)
const messagesContainer = ref(null)
const { $axios } = useNuxtApp()
const { token } = useAuth()
const { getInitials, getDisplayName } = useUserInfo()
const { onlineUserIds } = usePresence()

onMounted(async () => {
  await fetchMessages()
  setupWebSocket()
  scrollToBottom()
})

onUnmounted(() => {
  if (client.value) {
    client.value.deactivate()
  }
})

watch(() => props.conversationId, async (newId, oldId) => {
  // Fermer l'ancien WebSocket
  if (client.value) {
    client.value.deactivate()
    client.value = null
  }
  messages.value = []
  await fetchMessages()
  setupWebSocket()
})

watch(messages, () => {
  nextTick(() => {
    scrollToBottom()
  })
})

const scrollToBottom = () => {
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
  }
}

const setupWebSocket = () => {
  if (!token.value) {
    console.error('No authentication token available')
    return
  }

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
    onStompError: (frame) => {
      console.error('STOMP error:', frame)
    },
    onWebSocketError: (event) => {
      console.error('WebSocket error:', event)
    },
    onWebSocketClose: (event) => {
      console.log('WebSocket closed:', event)
    }
  })

  client.value.onConnect = () => {
    console.log('Connected to WebSocket')
    client.value?.subscribe(`/topic/conversation/${props.conversationId}`, (message) => {
      const messageData = JSON.parse(message.body)
      console.log('Message reçu via WebSocket :', messageData)
      // Ajout uniquement si le message appartient à la bonne conversation
      if (messageData.conversationId === props.conversationId && !messages.value.some(m => m.id === messageData.id)) {
        messages.value.push(messageData)
        scrollToBottom()
      }
    })
  }

  client.value.activate()
}

const fetchMessages = async () => {
  try {
    const response = await $axios.get(`/messages/conversation/${props.conversationId}`)
    messages.value = response.data
  } catch (error) {
    console.error('Erreur lors du chargement des messages:', error)
  }
}

const sendMessage = async () => {
  if (!newMessage.value.trim()) return

  try {
    const message = {
      content: newMessage.value.trim(),
      conversationId: props.conversationId,
      sender: { id: props.currentUserId },
      receiver: { id: props.otherUserId }
    }

    const response = await $axios.post('/messages', message)
    if (response.status === 200 && response.data) {
      // Ajout optimiste : on ajoute le message si pas déjà présent
      if (!messages.value.some(m => m.id === response.data.id)) {
        messages.value.push(response.data)
      }
      newMessage.value = ''
    }
  } catch (error) {
    console.error('Erreur lors de l\'envoi du message:', error)
  }
}
</script> 

<style scoped>
.chat-window {
  display: flex;
  flex-direction: column;
  height: 100%;
  background-color: var(--bg-color);
}

.chat-header {
  padding: 1rem;
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  color: white;
  display: flex;
  align-items: center;
  gap: 1rem;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: var(--accent-color);
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  color: white;
}

.messages-container {
  flex: 1;
  overflow-y: auto;
  padding: 1rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.message {
  max-width: 70%;
  padding: 0.75rem 1rem;
  border-radius: 1rem;
  position: relative;
}

.message.sent {
  align-self: flex-end;
  background-color: var(--primary-color);
  color: white;
  border-bottom-right-radius: 0.25rem;
}

.message.received {
  align-self: flex-start;
  background-color: var(--bg-secondary);
  color: var(--text-color);
  border-bottom-left-radius: 0.25rem;
}

.message-input {
  padding: 1rem;
  background-color: var(--bg-secondary);
  border-top: 1px solid var(--border-color);
}

.input-form {
  display: flex;
  gap: 0.5rem;
}

.input-field {
  flex: 1;
  padding: 0.75rem;
  border: 1px solid var(--border-color);
  border-radius: 0.5rem;
  background-color: var(--bg-color);
  color: var(--text-color);
}

.send-button {
  padding: 0.75rem 1.5rem;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: 0.5rem;
  cursor: pointer;
  transition: background-color 0.2s;
}

.send-button:disabled {
  background-color: var(--border-color);
  cursor: not-allowed;
}

@media (max-width: 768px) {
  .chat-window {
    height: calc(100vh - 4rem);
  }

  .message {
    max-width: 85%;
  }
}
</style> 