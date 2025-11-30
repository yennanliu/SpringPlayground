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
    it('should add success toast to internal state', async () => {
      wrapper.vm.success('Success message')
      await wrapper.vm.$nextTick()

      // Check internal state since Teleport doesn't work well in test env
      expect(wrapper.vm.toasts).toHaveLength(1)
      expect(wrapper.vm.toasts[0].type).toBe('success')
      expect(wrapper.vm.toasts[0].message).toBe('Success message')
    })

    it('should add error toast to internal state', async () => {
      wrapper.vm.error('Error message')
      await wrapper.vm.$nextTick()

      expect(wrapper.vm.toasts).toHaveLength(1)
      expect(wrapper.vm.toasts[0].type).toBe('error')
      expect(wrapper.vm.toasts[0].message).toBe('Error message')
    })

    it('should add warning toast to internal state', async () => {
      wrapper.vm.warning('Warning message')
      await wrapper.vm.$nextTick()

      expect(wrapper.vm.toasts).toHaveLength(1)
      expect(wrapper.vm.toasts[0].type).toBe('warning')
      expect(wrapper.vm.toasts[0].message).toBe('Warning message')
    })

    it('should add info toast to internal state', async () => {
      wrapper.vm.info('Info message')
      await wrapper.vm.$nextTick()

      expect(wrapper.vm.toasts).toHaveLength(1)
      expect(wrapper.vm.toasts[0].type).toBe('info')
      expect(wrapper.vm.toasts[0].message).toBe('Info message')
    })
  })

  describe('Multiple Toasts', () => {
    it('should show multiple toasts stacked', async () => {
      wrapper.vm.success('First')
      wrapper.vm.error('Second')
      wrapper.vm.info('Third')
      await wrapper.vm.$nextTick()

      expect(wrapper.vm.toasts).toHaveLength(3)
      expect(wrapper.vm.toasts[0].message).toBe('First')
      expect(wrapper.vm.toasts[1].message).toBe('Second')
      expect(wrapper.vm.toasts[2].message).toBe('Third')
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

      expect(wrapper.vm.toasts).toHaveLength(1)

      vi.advanceTimersByTime(100)
      await wrapper.vm.$nextTick()

      expect(wrapper.vm.toasts).toHaveLength(0)

      vi.useRealTimers()
    })

    it('should not auto-dismiss when duration is 0', async () => {
      vi.useFakeTimers()

      wrapper.vm.success('No dismiss', 0)
      await wrapper.vm.$nextTick()

      expect(wrapper.vm.toasts).toHaveLength(1)

      vi.advanceTimersByTime(5000)
      await wrapper.vm.$nextTick()

      expect(wrapper.vm.toasts).toHaveLength(1)

      vi.useRealTimers()
    })
  })

  describe('Manual Close', () => {
    it('should close toast when removeToast is called', async () => {
      const id = wrapper.vm.success('Close me')
      await wrapper.vm.$nextTick()

      expect(wrapper.vm.toasts).toHaveLength(1)

      wrapper.vm.removeToast(id)
      await wrapper.vm.$nextTick()

      expect(wrapper.vm.toasts).toHaveLength(0)
    })

    it('should only close specific toast', async () => {
      const id1 = wrapper.vm.success('First')
      const id2 = wrapper.vm.success('Second')
      const id3 = wrapper.vm.success('Third')
      await wrapper.vm.$nextTick()

      expect(wrapper.vm.toasts).toHaveLength(3)

      wrapper.vm.removeToast(id2)
      await wrapper.vm.$nextTick()

      expect(wrapper.vm.toasts).toHaveLength(2)
      expect(wrapper.vm.toasts.find(t => t.id === id1)).toBeDefined()
      expect(wrapper.vm.toasts.find(t => t.id === id3)).toBeDefined()
    })
  })

  describe('Toast Types', () => {
    it('should create toast with correct type', async () => {
      wrapper.vm.success('Success')
      await wrapper.vm.$nextTick()

      expect(wrapper.vm.toasts[0].type).toBe('success')
    })

    it('should create error type toast', async () => {
      wrapper.vm.error('Error')
      await wrapper.vm.$nextTick()

      expect(wrapper.vm.toasts[0].type).toBe('error')
    })

    it('should create warning type toast', async () => {
      wrapper.vm.warning('Warning')
      await wrapper.vm.$nextTick()

      expect(wrapper.vm.toasts[0].type).toBe('warning')
    })

    it('should create info type toast', async () => {
      wrapper.vm.info('Info')
      await wrapper.vm.$nextTick()

      expect(wrapper.vm.toasts[0].type).toBe('info')
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

  describe('Component Lifecycle', () => {
    it('should initialize with empty toasts array', () => {
      expect(wrapper.vm.toasts).toEqual([])
    })

    it('should be able to access internal state', () => {
      wrapper.vm.success('Test')
      expect(wrapper.vm.toasts).toHaveLength(1)
      expect(wrapper.vm.toasts[0].message).toBe('Test')
    })
  })
})
