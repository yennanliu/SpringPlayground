import SockJS from 'sockjs-client'
import { Client } from '@stomp/stompjs'
import { useWebSocketStore } from '../stores/websocket'

class WebSocketService {
  constructor() {
    this.client = null
    this.subscriptions = new Map()
    this.isConnecting = false
    this.reconnectAttempts = 0
    this.maxReconnectAttempts = 5
    this.reconnectDelay = 3000
  }

  connect(userId) {
    return new Promise((resolve, reject) => {
      if (this.client?.connected) {
        console.log('WebSocket already connected')
        resolve()
        return
      }

      if (this.isConnecting) {
        console.log('Connection already in progress')
        return
      }

      this.isConnecting = true
      const wsStore = useWebSocketStore()
      wsStore.setConnectionStatus('connecting')
      wsStore.clearError()

      const wsUrl = import.meta.env.VITE_WS_URL || 'http://localhost:8080/ws'

      this.client = new Client({
        webSocketFactory: () => new SockJS(wsUrl),
        connectHeaders: {
          userId: userId
        },
        debug: (str) => {
          console.log('STOMP Debug:', str)
        },
        reconnectDelay: this.reconnectDelay,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,

        onConnect: (frame) => {
          console.log('WebSocket connected:', frame)
          this.isConnecting = false
          this.reconnectAttempts = 0
          wsStore.setConnectionStatus('connected')
          resolve()
        },

        onStompError: (frame) => {
          console.error('STOMP error:', frame)
          this.isConnecting = false
          const errorMsg = frame.headers?.message || 'Connection error'
          wsStore.setError(errorMsg)
          reject(new Error(errorMsg))
        },

        onWebSocketError: (error) => {
          console.error('WebSocket error:', error)
          this.isConnecting = false
          wsStore.setError('WebSocket connection failed')
        },

        onDisconnect: () => {
          console.log('WebSocket disconnected')
          this.isConnecting = false
          wsStore.setConnectionStatus('disconnected')
          this.subscriptions.clear()
        }
      })

      this.client.activate()
    })
  }

  disconnect() {
    if (this.client) {
      this.subscriptions.forEach((subscription) => {
        try {
          subscription.unsubscribe()
        } catch (e) {
          console.error('Error unsubscribing:', e)
        }
      })
      this.subscriptions.clear()

      this.client.deactivate()
      this.client = null

      const wsStore = useWebSocketStore()
      wsStore.setConnectionStatus('disconnected')
    }
  }

  subscribeToChannel(channelId, callback) {
    if (!this.client?.connected) {
      console.error('Cannot subscribe: WebSocket not connected')
      return null
    }

    const destination = `/topic/channel/${channelId}`
    console.log('Subscribing to:', destination)

    try {
      const subscription = this.client.subscribe(destination, (message) => {
        try {
          const data = JSON.parse(message.body)
          callback(data)
        } catch (e) {
          console.error('Error parsing message:', e)
        }
      })

      this.subscriptions.set(channelId, subscription)
      return subscription
    } catch (e) {
      console.error('Error subscribing to channel:', e)
      return null
    }
  }

  unsubscribeFromChannel(channelId) {
    const subscription = this.subscriptions.get(channelId)
    if (subscription) {
      try {
        subscription.unsubscribe()
        this.subscriptions.delete(channelId)
        console.log('Unsubscribed from channel:', channelId)
      } catch (e) {
        console.error('Error unsubscribing from channel:', e)
      }
    }
  }

  sendMessage(channelId, message) {
    if (!this.client?.connected) {
      console.error('Cannot send message: WebSocket not connected')
      return false
    }

    const destination = `/app/chat/${channelId}`
    console.log('Sending message to:', destination, message)

    try {
      this.client.publish({
        destination: destination,
        body: JSON.stringify(message)
      })
      return true
    } catch (e) {
      console.error('Error sending message:', e)
      return false
    }
  }

  isConnected() {
    return this.client?.connected || false
  }
}

// Export singleton instance
export default new WebSocketService()
