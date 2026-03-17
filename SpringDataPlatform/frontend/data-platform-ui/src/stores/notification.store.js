import { defineStore } from 'pinia'

export const useNotificationStore = defineStore('notification', {
  state: () => ({
    notifications: [],
    nextId: 1
  }),

  actions: {
    /**
     * Show a success notification
     * @param {string} message - Notification message
     * @param {number} duration - Auto-dismiss duration in ms (0 to disable)
     */
    success(message, duration = 3000) {
      this.show({ type: 'success', message, duration })
    },

    /**
     * Show an error notification
     * @param {string} message - Notification message
     * @param {number} duration - Auto-dismiss duration in ms (0 to disable)
     */
    error(message, duration = 5000) {
      this.show({ type: 'error', message, duration })
    },

    /**
     * Show a warning notification
     * @param {string} message - Notification message
     * @param {number} duration - Auto-dismiss duration in ms (0 to disable)
     */
    warning(message, duration = 4000) {
      this.show({ type: 'warning', message, duration })
    },

    /**
     * Show an info notification
     * @param {string} message - Notification message
     * @param {number} duration - Auto-dismiss duration in ms (0 to disable)
     */
    info(message, duration = 3000) {
      this.show({ type: 'info', message, duration })
    },

    /**
     * Show a notification
     * @param {Object} options - Notification options
     */
    show({ type = 'info', message, duration = 3000 }) {
      const id = this.nextId++
      const notification = { id, type, message }

      this.notifications.push(notification)

      if (duration > 0) {
        setTimeout(() => {
          this.dismiss(id)
        }, duration)
      }
    },

    /**
     * Dismiss a notification by ID
     * @param {number} id - Notification ID
     */
    dismiss(id) {
      const index = this.notifications.findIndex(n => n.id === id)
      if (index > -1) {
        this.notifications.splice(index, 1)
      }
    },

    /**
     * Clear all notifications
     */
    clearAll() {
      this.notifications = []
    }
  }
})
