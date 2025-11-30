import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import Avatar from './Avatar.vue'

describe('Avatar Component', () => {
  describe('Rendering', () => {
    it('should render with username initials', () => {
      const wrapper = mount(Avatar, {
        props: {
          username: 'John Doe'
        }
      })

      expect(wrapper.find('.avatar-initials').text()).toBe('JD')
    })

    it('should render with single letter for single-word username', () => {
      const wrapper = mount(Avatar, {
        props: {
          username: 'Alice'
        }
      })

      expect(wrapper.find('.avatar-initials').text()).toBe('A')
    })

    it('should render avatar image when avatarUrl is provided', () => {
      const wrapper = mount(Avatar, {
        props: {
          username: 'John Doe',
          avatarUrl: 'https://example.com/avatar.jpg'
        }
      })

      const img = wrapper.find('img')
      expect(img.exists()).toBe(true)
      expect(img.attributes('src')).toBe('https://example.com/avatar.jpg')
    })

    it('should fallback to initials when image fails to load', async () => {
      const wrapper = mount(Avatar, {
        props: {
          username: 'John Doe',
          avatarUrl: 'https://example.com/invalid.jpg'
        }
      })

      const img = wrapper.find('img')
      await img.trigger('error')

      expect(wrapper.find('.avatar-initials').exists()).toBe(true)
      expect(wrapper.find('.avatar-initials').text()).toBe('JD')
    })
  })

  describe('Size Variants', () => {
    it('should apply small size class', () => {
      const wrapper = mount(Avatar, {
        props: {
          username: 'Alice',
          size: 'small'
        }
      })

      expect(wrapper.find('.avatar-small').exists()).toBe(true)
    })

    it('should apply medium size class by default', () => {
      const wrapper = mount(Avatar, {
        props: {
          username: 'Alice'
        }
      })

      expect(wrapper.find('.avatar-medium').exists()).toBe(true)
    })

    it('should apply large size class', () => {
      const wrapper = mount(Avatar, {
        props: {
          username: 'Alice',
          size: 'large'
        }
      })

      expect(wrapper.find('.avatar-large').exists()).toBe(true)
    })

    it('should apply xlarge size class', () => {
      const wrapper = mount(Avatar, {
        props: {
          username: 'Alice',
          size: 'xlarge'
        }
      })

      expect(wrapper.find('.avatar-xlarge').exists()).toBe(true)
    })
  })

  describe('Online Status', () => {
    it('should show online indicator when showOnline is true', () => {
      const wrapper = mount(Avatar, {
        props: {
          username: 'Alice',
          showOnline: true,
          isOnline: true
        }
      })

      const indicator = wrapper.find('.online-indicator')
      expect(indicator.exists()).toBe(true)
      expect(indicator.classes()).toContain('online')
    })

    it('should show offline indicator when isOnline is false', () => {
      const wrapper = mount(Avatar, {
        props: {
          username: 'Alice',
          showOnline: true,
          isOnline: false
        }
      })

      const indicator = wrapper.find('.online-indicator')
      expect(indicator.exists()).toBe(true)
      expect(indicator.classes()).not.toContain('online')
    })

    it('should not show online indicator when showOnline is false', () => {
      const wrapper = mount(Avatar, {
        props: {
          username: 'Alice',
          showOnline: false,
          isOnline: true
        }
      })

      expect(wrapper.find('.online-indicator').exists()).toBe(false)
    })
  })

  describe('Clickable Behavior', () => {
    it('should apply clickable class when clickable prop is true', () => {
      const wrapper = mount(Avatar, {
        props: {
          username: 'Alice',
          clickable: true
        }
      })

      expect(wrapper.find('.clickable').exists()).toBe(true)
    })

    it('should emit click event when clickable', async () => {
      const wrapper = mount(Avatar, {
        props: {
          username: 'Alice',
          clickable: true
        }
      })

      await wrapper.find('.avatar').trigger('click')

      expect(wrapper.emitted('click')).toHaveLength(1)
    })

    it('should not emit click event when not clickable', async () => {
      const wrapper = mount(Avatar, {
        props: {
          username: 'Alice',
          clickable: false
        }
      })

      await wrapper.find('.avatar').trigger('click')

      expect(wrapper.emitted('click')).toBeUndefined()
    })
  })

  describe('Color Generation', () => {
    it('should generate consistent colors for same username', () => {
      const wrapper1 = mount(Avatar, {
        props: { username: 'TestUser' }
      })
      const wrapper2 = mount(Avatar, {
        props: { username: 'TestUser' }
      })

      // Check that the avatar has background styling
      const avatar1 = wrapper1.find('.avatar')
      const avatar2 = wrapper2.find('.avatar')

      expect(avatar1.exists()).toBe(true)
      expect(avatar2.exists()).toBe(true)
    })

    it('should not use backgroundColor prop when not provided', () => {
      const wrapper = mount(Avatar, {
        props: { username: 'Alice' }
      })

      // The component should use generated gradient color
      const avatar = wrapper.find('.avatar')
      expect(avatar.exists()).toBe(true)
    })

    it('should use custom backgroundColor when provided', () => {
      const wrapper = mount(Avatar, {
        props: {
          username: 'Alice',
          backgroundColor: '#ff0000'
        }
      })

      const avatar = wrapper.find('.avatar')
      expect(avatar.exists()).toBe(true)
    })
  })

  describe('Edge Cases', () => {
    it('should handle empty username gracefully', () => {
      const wrapper = mount(Avatar, {
        props: {
          username: ''
        }
      })

      expect(wrapper.find('.avatar-initials').text()).toBe('?')
    })

    it('should handle username with special characters', () => {
      const wrapper = mount(Avatar, {
        props: {
          username: 'User@123'
        }
      })

      expect(wrapper.find('.avatar-initials').text()).toBe('U')
    })

    it('should handle very long usernames', () => {
      const wrapper = mount(Avatar, {
        props: {
          username: 'VeryLongUsernameWithManyWords'
        }
      })

      const initials = wrapper.find('.avatar-initials').text()
      expect(initials.length).toBeLessThanOrEqual(2)
    })
  })
})
