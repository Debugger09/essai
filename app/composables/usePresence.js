import { ref, readonly, onMounted, onUnmounted } from 'vue'
import { Client } from '@stomp/stompjs'
import SockJS from 'sockjs-client'
import { useAuth } from '~/composables/useAuth'

// État global des utilisateurs en ligne
const onlineUserIds = ref(new Set())
let presenceClient = null

export function usePresence() {
  const { token, user } = useAuth()

  const setupPresenceWebSocket = () => {
    if (!token.value || !user.value || presenceClient) {
      return
    }

    presenceClient = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      connectHeaders: {
        Authorization: `Bearer ${token.value}`
      },
      debug: (str) => {
        console.log('[Presence]', str)
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      onStompError: (frame) => {
        console.error('[Presence] STOMP error:', frame)
      },
      onWebSocketError: (event) => {
        console.error('[Presence] WebSocket error:', event)
      },
      onWebSocketClose: (event) => {
        console.log('[Presence] WebSocket closed:', event)
      }
    })

    presenceClient.onConnect = () => {
      console.log('[Presence] Connected to WebSocket')
      
      // Notifier que l'utilisateur rejoint
      presenceClient?.publish({
        destination: '/app/presence/join',
        body: JSON.stringify({ userId: user.value.id })
      })
      
      // S'abonner aux notifications de présence
      presenceClient?.subscribe('/topic/presence', (message) => {
        try {
          const data = JSON.parse(message.body)
          console.log('[Presence] Received presence update:', data)
          
          if (data.type === 'USER_ONLINE') {
            onlineUserIds.value.add(data.userId)
          } else if (data.type === 'USER_OFFLINE') {
            onlineUserIds.value.delete(data.userId)
          } else if (data.type === 'ONLINE_USERS') {
            onlineUserIds.value = new Set(data.userIds || [])
          }
        } catch (error) {
          console.error('[Presence] Error parsing message:', error)
        }
      })

      // Demander la liste des utilisateurs en ligne
      presenceClient?.publish({
        destination: '/app/presence/request-online-users',
        body: JSON.stringify({ request: 'online_users' })
      })
    }

    presenceClient.activate()
  }

  const cleanupPresenceWebSocket = () => {
    if (presenceClient) {
      // Notifier que l'utilisateur quitte
      if (user.value) {
        presenceClient?.publish({
          destination: '/app/presence/leave',
          body: JSON.stringify({ userId: user.value.id })
        })
      }
      
      presenceClient.deactivate()
      presenceClient = null
    }
  }

  onMounted(() => {
    setupPresenceWebSocket()
  })

  onUnmounted(() => {
    cleanupPresenceWebSocket()
  })

  return {
    onlineUserIds: readonly(onlineUserIds),
    setupPresenceWebSocket,
    cleanupPresenceWebSocket
  }
} 