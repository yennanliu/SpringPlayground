import { describe, it, expect, beforeEach, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import ToastNotification from './ToastNotification.vue'

describe('ToastNotification Component', () => {
  let wrapper

  beforeEach(() => {
    wrapper = mount(ToastNotification, {
      attachTo: document.body
    })
  })

  describe('Toast Display', () => {
    it('should show success toast', async () => {
      wrapper.vm.success('Success message')
      await wrapper.vm.$nextTick()

      const toast = wrapper.find('.toast-success')
      expect(toast.exists()).toBe(true)
      expect(toast.text()).toContain('Success message')
    })

    it('should show error toast', async () => {
      wrapper.vm.error('Error message')
      await wrapper.vm.$nextTick()

      const toast = wrapper.find('.toast-error')
      expect(toast.exists()).toBe(true)
      expect(toast.text()).toContain('Error message')
    })

    it('should show warning toast', async () => {
      wrapper.vm.warning('Warning message')
      await wrapper.vm.$nextTick()

      const toast = wrapper.find('.toast-warning')
      expect(toast.exists()).toBe(true)
      expect(toast.text()).toContain('Warning message')
    })

    it('should show info toast', async () => {
      wrapper.vm.info('Info message')
      await wrapper.vm.$nextTick()

      const toast = wrapper.find('.toast-info')
      expect(toast.exists()).toBe(true)
      expect(toast.text()).toContain('Info message')
    })
  })

  describe('Multiple Toasts', () => {
    it('should show multiple toasts stacked', async () => {
      wrapper.vm.success('First')
      wrapper.vm.error('Second')
      wrapper.vm.info('Third')
      await wrapper.vm.$nextTick()

      const toasts = wrapper.findAll('.toast')
      expect(toasts).toHaveLength(3)
    })

    it('should assign unique IDs to toasts', async () => {
      const id1 = wrapper.vm.success('First')
      const id2 = wrapper.vm.success('Second')

      expect(id1).not.toBe(id2)
    })
  })

  describe('Auto-dismiss', () => {
    it('should auto-dismiss toast after default duration', async () => {
      vi.useFakeTimers()

      wrapper.vm.success('Auto dismiss', 100)
      await wrapper.vm.$nextTick()

      expect(wrapper.findAll('.toast')).toHaveLength(1)

      vi.advanceTimersByTime(100)
      await wrapper.vm.$nextTick()

      expect(wrapper.findAll('.toast')).toHaveLength(0)

      vi.useRealTimers()
    })

    it('should not auto-dismiss when duration is 0', async () => {
      vi.useFakeTimers()

      wrapper.vm.success('No dismiss', 0)
      await wrapper.vm.$nextTick()

      expect(wrapper.findAll('.toast')).toHaveLength(1)

      vi.advanceTimersByTime(5000)
      await wrapper.vm.$nextTick()

      expect(wrapper.findAll('.toast')).toHaveLength(1)

      vi.useRealTimers()
    })
  })

  describe('Manual Close', () => {
    it('should close toast when close button is clicked', async () => {
      wrapper.vm.success('Close me')
      await wrapper.vm.$nextTick()

      expect(wrapper.findAll('.toast')).toHaveLength(1)

      await wrapper.find('.toast-close').trigger('click')
      await wrapper.vm.$nextTick()

      expect(wrapper.findAll('.toast')).toHaveLength(0)
    })

    it('should only close specific toast', async () => {
      wrapper.vm.success('First')
      wrapper.vm.success('Second')
      wrapper.vm.success('Third')
      await wrapper.vm.$nextTick()

      expect(wrapper.findAll('.toast')).toHaveLength(3)

      const closeButtons = wrapper.findAll('.toast-close')
      await closeButtons[1].trigger('click')
      await wrapper.vm.$nextTick()

      expect(wrapper.findAll('.toast')).toHaveLength(2)
    })
  })

  describe('Toast Icons', () => {
    it('should show success icon for success toast', async () => {
      wrapper.vm.success('Success')
      await wrapper.vm.$nextTick()

      const toast = wrapper.find('.toast-success')
      expect(toast.find('.toast-icon svg').exists()).toBe(true)
    })

    it('should show error icon for error toast', async () => {
      wrapper.vm.error('Error')
      await wrapper.vm.$nextTick()

      const toast = wrapper.find('.toast-error')
      expect(toast.find('.toast-icon svg').exists()).toBe(true)
    })

    it('should show warning icon for warning toast', async () => {
      wrapper.vm.warning('Warning')
      await wrapper.vm.$nextTick()

      const toast = wrapper.find('.toast-warning')
      expect(toast.find('.toast-icon svg').exists()).toBe(true)
    })

    it('should show info icon for info toast', async () => {
      wrapper.vm.info('Info')
      await wrapper.vm.$nextTick()

      const toast = wrapper.find('.toast-info')
      expect(toast.find('.toast-icon svg').exists()).toBe(true)
    })
  })

  describe('API Methods', () => {
    it('should expose show method', () => {
      expect(typeof wrapper.vm.show).toBe('function')
    })

    it('should expose success method', () => {
      expect(typeof wrapper.vm.success).toBe('function')
    })

    it('should expose error method', () => {
      expect(typeof wrapper.vm.error).toBe('function')
    })

    it('should expose warning method', () => {
      expect(typeof wrapper.vm.warning).toBe('function')
    })

    it('should expose info method', () => {
      expect(typeof wrapper.vm.info).toBe('function')
    })

    it('should expose removeToast method', () => {
      expect(typeof wrapper.vm.removeToast).toBe('function')
    })
  })

  describe('Teleport', () => {
    it('should teleport to body', () => {
      wrapper.vm.success('Test')

      const toastContainer = document.querySelector('.toast-container')
      expect(toastContainer).toBeTruthy()
      expect(toastContainer.parentElement).toBe(document.body)
    })
  })
})
