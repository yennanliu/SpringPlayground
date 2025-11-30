# Testing Guide - Chat App V2 Frontend

Comprehensive testing documentation for the Vue 3 chat application.

## Table of Contents

- [Test Framework](#test-framework)
- [Running Tests](#running-tests)
- [Test Structure](#test-structure)
- [Writing Tests](#writing-tests)
- [Test Coverage](#test-coverage)
- [Best Practices](#best-practices)
- [Common Patterns](#common-patterns)
- [Troubleshooting](#troubleshooting)

## Test Framework

### Technology Stack

- **Vitest 4.0.14** - Fast unit test framework for Vite projects
- **@vue/test-utils 2.4.6** - Official testing utilities for Vue 3
- **happy-dom 20.0.11** - Lightweight DOM implementation for Node.js
- **@vitest/ui 4.0.14** - Visual UI for test results

### Why Vitest?

- Native Vite integration (fast HMR)
- Jest-compatible API (easy migration)
- TypeScript support out of the box
- Built-in code coverage
- Parallel test execution
- Watch mode with smart re-runs

## Running Tests

### Basic Commands

```bash
# Run all tests once
npm test

# Run tests in watch mode (re-runs on file changes)
npm test -- --watch

# Run tests with UI dashboard
npm run test:ui

# Run tests with coverage report
npm run test:coverage

# Run specific test file
npm test -- Avatar.spec.js

# Run tests matching a pattern
npm test -- --grep="User Store"
```

### Command Options

```bash
# Run in silent mode (less output)
npm test -- --silent

# Run with specific reporter
npm test -- --reporter=verbose

# Update snapshots (if using snapshot testing)
npm test -- -u
```

## Test Structure

### Directory Layout

```
src/
├── components/
│   ├── Avatar.vue
│   ├── Avatar.spec.js                    # Component tests
│   ├── ToastNotification.vue
│   └── ToastNotification.spec.js
├── stores/
│   ├── user.js
│   ├── user.spec.js                      # Store tests
│   ├── messages.js
│   ├── messages.spec.js
│   ├── channels.js
│   └── channels.spec.js
└── services/
    ├── websocket.service.js
    └── websocket.service.spec.js        # Service tests
```

### Test File Naming

- Component tests: `ComponentName.spec.js`
- Store tests: `storeName.spec.js`
- Service tests: `serviceName.spec.js`
- Place test files next to the code they test

## Test Coverage

### Current Coverage

**Test Statistics:**
- **Total Tests**: 91
- **Passing**: 91 (100%)
- **Test Files**: 5

**Coverage by Module:**

| Module | Tests | Description |
|--------|-------|-------------|
| user store | 11 tests | Authentication, profile management, localStorage |
| messages store | 15 tests | Message CRUD, sorting, state management |
| channels store | 23 tests | Channel management, API integration, unread counts |
| Avatar component | 20 tests | Rendering, sizes, colors, click handling |
| ToastNotification | 22 tests | Toast display, auto-dismiss, types |

### Viewing Coverage Reports

```bash
# Generate coverage report
npm run test:coverage

# Open HTML report in browser
open coverage/index.html
```

## Writing Tests

### Component Tests

#### Basic Component Test

```javascript
import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import MyComponent from './MyComponent.vue'

describe('MyComponent', () => {
  it('should render correctly', () => {
    const wrapper = mount(MyComponent, {
      props: {
        title: 'Test Title'
      }
    })

    expect(wrapper.text()).toContain('Test Title')
  })
})
```

#### Testing User Interactions

```javascript
it('should emit click event when button is clicked', async () => {
  const wrapper = mount(MyComponent)

  await wrapper.find('button').trigger('click')

  expect(wrapper.emitted('click')).toHaveLength(1)
})
```

#### Testing Props

```javascript
it('should accept and display username prop', () => {
  const wrapper = mount(Avatar, {
    props: {
      username: 'John Doe'
    }
  })

  expect(wrapper.find('.avatar-initials').text()).toBe('JD')
})
```

#### Testing Slots

```javascript
it('should render slot content', () => {
  const wrapper = mount(MyComponent, {
    slots: {
      default: '<div class="custom">Slot content</div>'
    }
  })

  expect(wrapper.find('.custom').text()).toBe('Slot content')
})
```

### Store Tests

#### Testing Pinia Stores

```javascript
import { describe, it, expect, beforeEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useUserStore } from './user'

describe('User Store', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    localStorage.clear()
  })

  it('should initialize with correct state', () => {
    const store = useUserStore()

    expect(store.isAuthenticated).toBe(false)
    expect(store.currentUser).toBeNull()
  })

  it('should update state on login', () => {
    const store = useUserStore()

    store.login({
      username: 'testuser',
      id: 'user-123'
    })

    expect(store.isAuthenticated).toBe(true)
    expect(store.username).toBe('testuser')
  })
})
```

#### Testing Async Actions

```javascript
it('should load channels from API', async () => {
  const store = useChannelsStore()
  const mockChannels = [
    { id: 'group:tech', name: 'tech', type: 'GROUP' }
  ]

  chatService.getUserChannels.mockResolvedValue(mockChannels)

  await store.loadUserChannels()

  expect(store.channels).toHaveLength(1)
  expect(store.isLoading).toBe(false)
})
```

### Mocking

#### Mocking Services

```javascript
import { vi } from 'vitest'
import chatService from '../services/chat.service'

vi.mock('../services/chat.service')

describe('Channels Store', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('should create channel via API', async () => {
    chatService.createGroupChannel.mockResolvedValue({
      id: 'group:new',
      name: 'new'
    })

    const result = await store.createGroupChannel('new', [])

    expect(chatService.createGroupChannel).toHaveBeenCalledWith('new', [])
    expect(result.id).toBe('group:new')
  })
})
```

#### Mocking Timers

```javascript
import { vi } from 'vitest'

it('should auto-dismiss after duration', async () => {
  vi.useFakeTimers()

  wrapper.vm.success('Message', 1000)

  expect(wrapper.vm.toasts).toHaveLength(1)

  vi.advanceTimersByTime(1000)
  await wrapper.vm.$nextTick()

  expect(wrapper.vm.toasts).toHaveLength(0)

  vi.useRealTimers()
})
```

#### Mocking localStorage

```javascript
beforeEach(() => {
  // Clear localStorage before each test
  localStorage.clear()
})

it('should save to localStorage', () => {
  store.login({ username: 'test', id: '123' })

  const saved = JSON.parse(localStorage.getItem('currentUser'))
  expect(saved.username).toBe('test')
})
```

## Best Practices

### 1. Test Organization

```javascript
describe('ComponentName', () => {
  describe('Feature Group', () => {
    it('should do specific thing', () => {
      // Test implementation
    })
  })
})
```

### 2. AAA Pattern (Arrange, Act, Assert)

```javascript
it('should update message content', () => {
  // Arrange
  const store = useMessagesStore()
  store.addMessage({ id: 'msg-1', content: 'Original' })

  // Act
  store.updateMessage('msg-1', { content: 'Updated' })

  // Assert
  expect(store.messages[0].content).toBe('Updated')
})
```

### 3. Clear Test Names

❌ Bad:
```javascript
it('works', () => { ... })
it('test 1', () => { ... })
```

✅ Good:
```javascript
it('should show error toast when error occurs', () => { ... })
it('should not increment unread count for current channel', () => { ... })
```

### 4. Test Independence

```javascript
beforeEach(() => {
  // Reset state before each test
  setActivePinia(createPinia())
  localStorage.clear()
  vi.clearAllMocks()
})
```

### 5. Don't Test Implementation Details

❌ Bad (testing internal state):
```javascript
it('should set _internalFlag to true', () => {
  expect(wrapper.vm._internalFlag).toBe(true)
})
```

✅ Good (testing behavior):
```javascript
it('should show loading spinner while fetching', () => {
  expect(wrapper.find('.loading-spinner').exists()).toBe(true)
})
```

### 6. Use Data-testid for Complex Selectors

```vue
<template>
  <button data-testid="submit-button">Submit</button>
</template>
```

```javascript
it('should find button by test id', () => {
  expect(wrapper.find('[data-testid="submit-button"]').exists()).toBe(true)
})
```

## Common Patterns

### Testing Error Handling

```javascript
it('should handle API errors gracefully', async () => {
  chatService.getUserChannels.mockRejectedValue(new Error('API Error'))

  await store.loadUserChannels()

  expect(store.error).toBe('Failed to load channels')
  expect(store.channels).toHaveLength(1) // Fallback channel
})
```

### Testing Computed Properties

```javascript
it('should filter group channels', () => {
  const store = useChannelsStore()

  store.addChannel({ id: 'group:tech', type: 'GROUP' })
  store.addChannel({ id: 'dm:user1:user2', type: 'DIRECT' })

  expect(store.groupChannels).toHaveLength(1)
  expect(store.groupChannels[0].type).toBe('GROUP')
})
```

### Testing Image Error Handling

```javascript
it('should fallback to initials when image fails', async () => {
  const wrapper = mount(Avatar, {
    props: {
      username: 'John Doe',
      avatarUrl: 'invalid-url.jpg'
    }
  })

  await wrapper.find('img').trigger('error')

  expect(wrapper.find('.avatar-initials').exists()).toBe(true)
  expect(wrapper.find('.avatar-initials').text()).toBe('JD')
})
```

### Testing Teleported Components

```javascript
// Note: Teleport doesn't work well in test environment
// Test internal state instead of DOM rendering

it('should add toast to internal state', async () => {
  wrapper.vm.success('Message')
  await wrapper.vm.$nextTick()

  expect(wrapper.vm.toasts).toHaveLength(1)
  expect(wrapper.vm.toasts[0].message).toBe('Message')
})
```

## Troubleshooting

### Common Issues

#### 1. Tests Fail in CI but Pass Locally

**Cause**: Timing issues with async operations

**Solution**: Use proper async/await and `nextTick()`

```javascript
it('should update async', async () => {
  await store.loadData()
  await wrapper.vm.$nextTick()

  expect(store.data).toBeDefined()
})
```

#### 2. Cannot Find Element

**Cause**: Component hasn't rendered yet

**Solution**: Wait for next tick

```javascript
await wrapper.vm.$nextTick()
const element = wrapper.find('.my-element')
```

#### 3. localStorage Not Cleared Between Tests

**Cause**: Missing cleanup in beforeEach

**Solution**: Always clear in beforeEach

```javascript
beforeEach(() => {
  localStorage.clear()
})
```

#### 4. Mock Not Working

**Cause**: Mock not cleared between tests

**Solution**: Clear mocks in beforeEach

```javascript
beforeEach(() => {
  vi.clearAllMocks()
})
```

#### 5. Style Attributes Not Visible

**Cause**: Vue Test Utils doesn't always expose style bindings

**Solution**: Test behavior, not styling

```javascript
// Instead of checking styles
// expect(wrapper.attributes('style')).toContain('red')

// Test that the component exists with correct class
expect(wrapper.find('.error-state').exists()).toBe(true)
```

### Debugging Tests

#### 1. Print Component HTML

```javascript
console.log(wrapper.html())
```

#### 2. Print Store State

```javascript
console.log(JSON.stringify(store.$state, null, 2))
```

#### 3. Use Debugger

```javascript
it('should debug test', () => {
  debugger // Will pause when running with --inspect
  expect(true).toBe(true)
})
```

#### 4. Run Single Test

```bash
# Run only tests matching "User Store"
npm test -- --grep="User Store"
```

## Test Checklist

When adding new features, ensure you test:

- ✅ Initial state/props
- ✅ User interactions (click, input, etc.)
- ✅ Data transformations
- ✅ Edge cases (empty data, errors, etc.)
- ✅ Async operations
- ✅ Component emissions
- ✅ Computed properties
- ✅ State management
- ✅ Error handling
- ✅ Conditional rendering

## Continuous Integration

### GitHub Actions Example

```yaml
name: Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-node@v3
        with:
          node-version: '20'

      - name: Install dependencies
        run: npm install

      - name: Run tests
        run: npm test

      - name: Generate coverage
        run: npm run test:coverage

      - name: Upload coverage
        uses: codecov/codecov-action@v3
        with:
          files: ./coverage/coverage-final.json
```

## Further Reading

- [Vitest Documentation](https://vitest.dev/)
- [Vue Test Utils Documentation](https://test-utils.vuejs.org/)
- [Testing Library Best Practices](https://kentcdodds.com/blog/common-mistakes-with-react-testing-library)
- [Writing Testable Code](https://testing.googleblog.com/2008/08/by-miko-hevery-so-you-decided-to.html)

---

**Last Updated:** 2025-11-30

**Test Coverage:** 91 tests passing

**Maintained by:** Chat App V2 Team
