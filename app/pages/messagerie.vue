<template>
  <div class="flex h-screen">
    <ConversationList
      v-if="isAuthenticated"
      :user-id="user?.id"
      @select="onSelectConversation"
    />
    <Chat
      v-if="selectedConversationId && selectedConversation"
      :conversation-id="selectedConversationId"
      :user-id="user?.id"
      :other-user-id="selectedConversation.userAId === user?.id ? selectedConversation.userBId : selectedConversation.userAId"
    />
    <div v-else class="flex-1 flex items-center justify-center">
      <p class="text-gray-500 text-lg">Sélectionnez une conversation pour commencer</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useAuth } from '~/composables/useAuth'
import ConversationList from '~/components/Chat/ConversationList.vue'
import Chat from '~/components/Chat/Chat.vue'

const { user, isAuthenticated } = useAuth()
const selectedConversationId = ref(null)
const selectedConversation = ref(null)
const { $axios } = useNuxtApp()

const onSelectConversation = async (conversationId) => {
  selectedConversationId.value = conversationId
  try {
    const response = await $axios.get(`/conversations/${conversationId}`)
    selectedConversation.value = response.data
    console.log('DEBUG selectedConversation:', response.data)
  } catch (error) {
    console.error('Erreur lors du chargement de la conversation:', error)
  }
}
</script>

<style scoped>
.flex {
  display: flex;
  width: 100%;
  height: 100vh;
}
</style> 