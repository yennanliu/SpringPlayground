import { defineStore } from 'pinia'

type NotificationType = 'success' | 'error' | 'warning' | 'info'

interface Notification {
  id: number
  type: NotificationType
  message: string
}

interface NotificationState {
  notifications: Notification[]
  nextId: number
}

interface ShowOptions {
  type?: NotificationType
  message: string
  duration?: number
}

export const useNotificationStore = defineStore('notification', {
  state: (): NotificationState => ({
    notifications: [],
    nextId: 1
  }),

  actions: {
    /**
     * Show a success notification
     */
    success(message: string, duration = 3000): void {
      this.show({ type: 'success', message, duration })
    },

    /**
     * Show an error notification
     */
    error(message: string, duration = 5000): void {
      this.show({ type: 'error', message, duration })
    },

    /**
     * Show a warning notification
     */
    warning(message: string, duration = 4000): void {
      this.show({ type: 'warning', message, duration })
    },

    /**
     * Show an info notification
     */
    info(message: string, duration = 3000): void {
      this.show({ type: 'info', message, duration })
    },

    /**
     * Show a notification
     */
    show({ type = 'info', message, duration = 3000 }: ShowOptions): void {
      const id = this.nextId++
      const notification: Notification = { id, type, message }

      this.notifications.push(notification)

      if (duration > 0) {
        setTimeout(() => {
          this.dismiss(id)
        }, duration)
      }
    },

    /**
     * Dismiss a notification by ID
     */
    dismiss(id: number): void {
      const index = this.notifications.findIndex(n => n.id === id)
      if (index > -1) {
        this.notifications.splice(index, 1)
      }
    },

    /**
     * Clear all notifications
     */
    clearAll(): void {
      this.notifications = []
    }
  }
})
