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
            <span v-if="onlineUserIds.includes(otherUserId)">En ligne</span>
            <span v-else>Hors ligne</span>
          </p>
        </div>
      </div>
    </div>

    <!-- ChatWindow -->
    <ChatWindow
      :key="conversationId"
      :conversation-id="conversationId"
      :current-user-id="userId"
      :other-user-id="otherUserId"
      :other-user="otherUser"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick, watch, computed } from 'vue'
import { Client } from '@stomp/stompjs'
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