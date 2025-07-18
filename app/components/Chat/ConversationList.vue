<template>
  <div class="w-80 border-r h-full bg-gradient-to-br from-slate-50 via-blue-50/30 to-cyan-50/30">
    <!-- En-tête -->
    <div class="p-4 border-b bg-white/90 backdrop-blur-sm shadow-sm">
      <div class="flex items-center justify-between">
        <h2 class="text-xl font-bold bg-gradient-to-r from-blue-900 via-blue-700 to-indigo-600 bg-clip-text text-transparent">
          Messages
        </h2>
        <button 
          @click="openNewConversationModal"
          class="p-2 rounded-full hover:bg-blue-50 transition-colors duration-200"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-blue-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
          </svg>
        </button>
      </div>
    </div>

    <!-- Liste des conversations -->
    <div class="overflow-y-auto h-[calc(100%-4rem)]">
      <div
        v-for="conversation in conversations"
        :key="conversation.id"
        @click="onSelectConversation(conversation.id)"
        class="p-4 border-b hover:bg-white/50 cursor-pointer transition-colors duration-200"
        :class="{ 'bg-white/50': selectedConversationId === conversation.id }"
      >
        <div class="flex items-center space-x-4">
          <div class="w-12 h-12 rounded-full bg-gradient-to-r from-blue-600 to-indigo-600 flex items-center justify-center text-white font-semibold shadow-lg">
            {{ getInitials(getOtherParticipant(conversation).id) }}
          </div>
          <div class="flex-1 min-w-0">
            <div class="flex justify-between items-start">
              <h3 class="font-medium text-gray-900 truncate">
                {{ getOtherParticipant(conversation).firstName }} {{ getOtherParticipant(conversation).lastName }}
              </h3>
              <span class="text-xs text-gray-500">
                {{ conversation.updatedAt ? new Date(conversation.updatedAt).toLocaleTimeString() : '' }}
              </span>
            </div>
            <p class="text-sm text-gray-500 truncate">
              {{ conversation.lastMessage || 'Aucun message' }}
            </p>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal Nouvelle Conversation -->
    <div v-if="showNewConversationModal" class="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center z-50 p-4">
      <div class="bg-white/95 backdrop-blur-sm rounded-3xl shadow-2xl max-w-lg w-full p-6 sm:p-8 border border-white/20 transform transition-all duration-300 scale-100">
        <div class="flex items-center justify-between mb-6">
          <div class="flex items-center space-x-3">
            <div class="p-3 bg-blue-100 rounded-2xl">
              <svg class="h-6 w-6 text-blue-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
              </svg>
            </div>
            <h3 class="text-xl font-bold text-gray-900">Nouvelle conversation</h3>
          </div>
          <button @click="showNewConversationModal = false" class="p-2 text-gray-400 hover:text-gray-600 hover:bg-gray-100 rounded-xl transition-all duration-200">
            <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        <div class="space-y-4">
          <div class="relative">
            <input
              v-model="searchUser"
              type="text"
              placeholder="Rechercher un utilisateur..."
              class="w-full px-4 py-3 rounded-xl border border-gray-200 bg-gray-50 focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200"
            />
          </div>

          <div class="max-h-60 overflow-y-auto space-y-2">
            <div
              v-for="user in filteredUsers"
              :key="user.id"
              @click="startNewConversation(user.id)"
              class="p-3 hover:bg-gray-50 rounded-xl cursor-pointer transition-colors duration-200"
            >
              <div class="flex items-center space-x-3">
                <div class="w-10 h-10 rounded-full bg-gradient-to-r from-blue-600 to-indigo-600 flex items-center justify-center text-white font-semibold">
                  {{ getInitials(user.id) }}
                </div>
                <div>
                  <p class="font-medium text-gray-900">{{ getDisplayName(user.id) }}</p>
                  <p class="text-sm text-gray-500">{{ getUserEmail(user.id) }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { Client } from '@stomp/stompjs'
import SockJS from 'sockjs-client'
import { useAuth } from '~/composables/useAuth'
import { useUserInfo } from '~/composables/useUserInfo'

const props = defineProps({
  userId: {
    type: Number,
    required: true
  }
})

const emit = defineEmits(['select'])

const conversations = ref([])
const client = ref(null)
const showNewConversationModal = ref(false)
const searchUser = ref('')
const selectedConversationId = ref(null)
const { $axios } = useNuxtApp()
const { token } = useAuth()
const { users, fetchUsers, getInitials, getDisplayName, getUserEmail } = useUserInfo()

onMounted(async () => {
  await fetchConversations()
  await fetchUsers($axios)
  setupWebSocket()
})

onUnmounted(() => {
  if (client.value) {
    client.value.deactivate()
  }
})

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
    client.value?.subscribe(`/topic/user/${props.userId}/conversations`, (message) => {
      fetchConversations()
    })
  }

  client.value.activate()
}

const fetchConversations = async () => {
  try {
    const response = await $axios.get(`/conversations/user/${props.userId}`)
    conversations.value = response.data
  } catch (error) {
    console.error('Erreur lors du chargement des conversations:', error)
  }
}

const filteredUsers = computed(() => {
  if (!searchUser.value) return users.value.filter(user => user.id !== props.userId)
  const search = searchUser.value.toLowerCase()
  return users.value.filter(user => 
    user.id !== props.userId && (
      getUserEmail(user.id).toLowerCase().includes(search) ||
      getDisplayName(user.id).toLowerCase().includes(search)
    )
  )
})

const openNewConversationModal = () => {
  showNewConversationModal.value = true
  searchUser.value = ''
}

const startNewConversation = async (otherUserId) => {
  try {
    const response = await $axios.post('/conversations', {
      userAId: props.userId,
      userBId: otherUserId
    })
    showNewConversationModal.value = false
    const existing = conversations.value.find(c => c.id === response.data.id)
    if (!existing) {
      addOrUpdateConversation(response.data)
    }
    selectedConversationId.value = response.data.id
    emit('select', response.data.id)
  } catch (error) {
    console.error('Erreur lors de la création de la conversation:', error)
  }
}

const onSelectConversation = (conversationId) => {
  if (conversationId) {
    selectedConversationId.value = conversationId
    emit('select', conversationId)
  }
}

// Ajout des méthodes utilitaires
function addOrUpdateConversation(newConv) {
  const idx = conversations.value.findIndex(c => c.id === newConv.id);
  if (idx === -1) {
    conversations.value.push(newConv);
  } else {
    conversations.value[idx] = newConv;
  }
}

function getOtherParticipant(conversation) {
  if (conversation.userA && conversation.userB) {
    return conversation.userA.id !== props.userId ? conversation.userA : conversation.userB;
  }
  return { firstName: '', lastName: '' };
}
</script>

<style scoped>
@media (max-width: 640px) {
  .w-80 {
    width: 100%;
  }
}
</style> 