# Frontend WebSocket Quick Start Guide

## For Frontend Engineer

This guide provides everything you need to integrate WebSocket messaging in the Vue.js frontend.

## Backend Endpoints (Ready to Use)

### Connection
```
WebSocket URL: ws://localhost:8080/ws
Protocol: STOMP over SockJS
```

### Send Message
```
Destination: /app/chat/{channelId}
Format: {
  "channelId": "group:1",
  "senderId": 1,
  "content": "Hello World",
  "messageType": "TEXT"
}
```

### Receive Messages
```
Subscribe to: /topic/channel/{channelId}
Format: {
  "id": 1,
  "channelId": "group:1",
  "senderId": 1,
  "senderName": "Alice",
  "content": "Hello World",
  "messageType": "TEXT",
  "timestamp": "2025-02-20T10:30:00"
}
```

## Installation

```bash
cd frontend/chatAppV2UI
npm install @stomp/stompjs sockjs-client
```

## Implementation

### 1. Create WebSocket Service

Create `src/services/websocket.js`:

```javascript
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

class WebSocketService {
  constructor() {
    this.client = null;
    this.subscriptions = new Map();
  }

  connect(userId) {
    return new Promise((resolve, reject) => {
      this.client = new Client({
        webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
        connectHeaders: {
          userId: userId.toString()
        },
        onConnect: () => {
          console.log('WebSocket connected');
          resolve();
        },
        onStompError: (error) => {
          console.error('WebSocket error:', error);
          reject(error);
        },
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
        debug: (str) => {
          console.log('STOMP:', str);
        }
      });

      this.client.activate();
    });
  }

  disconnect() {
    if (this.client) {
      this.subscriptions.forEach(sub => sub.unsubscribe());
      this.subscriptions.clear();
      this.client.deactivate();
    }
  }

  subscribeToChannel(channelId, onMessage) {
    if (!this.client?.connected) {
      console.error('WebSocket not connected');
      return null;
    }

    const subscription = this.client.subscribe(
      `/topic/channel/${channelId}`,
      (message) => {
        try {
          const data = JSON.parse(message.body);
          onMessage(data);
        } catch (error) {
          console.error('Error parsing message:', error);
        }
      }
    );

    this.subscriptions.set(channelId, subscription);
    return subscription;
  }

  unsubscribeFromChannel(channelId) {
    const subscription = this.subscriptions.get(channelId);
    if (subscription) {
      subscription.unsubscribe();
      this.subscriptions.delete(channelId);
    }
  }

  sendMessage(channelId, senderId, content, messageType = 'TEXT') {
    if (!this.client?.connected) {
      throw new Error('WebSocket not connected');
    }

    this.client.publish({
      destination: `/app/chat/${channelId}`,
      body: JSON.stringify({
        channelId,
        senderId,
        content,
        messageType
      })
    });
  }

  startTyping(channelId, userId, username) {
    if (!this.client?.connected) return;

    this.client.publish({
      destination: `/app/typing/${channelId}/start`,
      body: JSON.stringify({ userId, username })
    });
  }

  stopTyping(channelId, userId) {
    if (!this.client?.connected) return;

    this.client.publish({
      destination: `/app/typing/${channelId}/stop`,
      body: JSON.stringify({ userId })
    });
  }
}

export default new WebSocketService();
```

### 2. Use in Vue Component

```vue
<template>
  <div class="chat-container">
    <div class="messages">
      <div
        v-for="message in messages"
        :key="message.id"
        class="message"
      >
        <strong>{{ message.senderName }}:</strong> {{ message.content }}
        <span class="timestamp">{{ formatTime(message.timestamp) }}</span>
      </div>
    </div>

    <div class="input-area">
      <input
        v-model="newMessage"
        @keyup.enter="sendMessage"
        @input="handleTyping"
        placeholder="Type a message..."
      />
      <button @click="sendMessage">Send</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue';
import { useUserStore } from '@/stores/user';
import websocket from '@/services/websocket';

const props = defineProps({
  channelId: {
    type: String,
    required: true
  }
});

const userStore = useUserStore();
const messages = ref([]);
const newMessage = ref('');
let typingTimeout = null;

onMounted(async () => {
  try {
    // Connect to WebSocket
    await websocket.connect(userStore.currentUser.id);

    // Subscribe to channel
    websocket.subscribeToChannel(props.channelId, (message) => {
      messages.value.push(message);
      scrollToBottom();
    });

    // Load message history from REST API
    await loadMessageHistory();
  } catch (error) {
    console.error('Failed to connect:', error);
  }
});

onUnmounted(() => {
  websocket.unsubscribeFromChannel(props.channelId);
});

function sendMessage() {
  if (!newMessage.value.trim()) return;

  try {
    websocket.sendMessage(
      props.channelId,
      userStore.currentUser.id,
      newMessage.value,
      'TEXT'
    );

    newMessage.value = '';
    websocket.stopTyping(props.channelId, userStore.currentUser.id);
  } catch (error) {
    console.error('Failed to send message:', error);
  }
}

function handleTyping() {
  clearTimeout(typingTimeout);

  websocket.startTyping(
    props.channelId,
    userStore.currentUser.id,
    userStore.currentUser.name
  );

  typingTimeout = setTimeout(() => {
    websocket.stopTyping(props.channelId, userStore.currentUser.id);
  }, 2000);
}

async function loadMessageHistory() {
  try {
    const response = await fetch(
      `http://localhost:8080/api/messages/${props.channelId}?page=0&size=50`
    );
    const data = await response.json();
    messages.value = data.content.reverse();
  } catch (error) {
    console.error('Failed to load history:', error);
  }
}

function formatTime(timestamp) {
  return new Date(timestamp).toLocaleTimeString();
}

function scrollToBottom() {
  // Implement auto-scroll
}
</script>
```

### 3. Initialize in App.vue

```vue
<script setup>
import { onMounted, onUnmounted } from 'vue';
import { useUserStore } from '@/stores/user';
import websocket from '@/services/websocket';

const userStore = useUserStore();

onMounted(async () => {
  if (userStore.isLoggedIn) {
    try {
      await websocket.connect(userStore.currentUser.id);
    } catch (error) {
      console.error('WebSocket connection failed:', error);
    }
  }
});

onUnmounted(() => {
  websocket.disconnect();
});
</script>
```

## Channel Types

### Group Channels
```javascript
const channelId = `group:${groupId}`;  // e.g., "group:1"
```

### Direct Messages
```javascript
// Always sort user IDs
const [userId1, userId2] = [myId, otherId].sort((a, b) => a - b);
const channelId = `dm:${userId1}:${userId2}`;  // e.g., "dm:1:5"
```

## Message Types

```javascript
// Text message
messageType: 'TEXT'

// Image message
messageType: 'IMAGE'
content: 'https://example.com/image.jpg'

// File message
messageType: 'FILE'
content: 'https://example.com/file.pdf'
```

## API Endpoints (REST)

### Load Message History
```
GET /api/messages/{channelId}?page=0&size=50
Response: {
  content: [...messages],
  totalPages: 5,
  totalElements: 230
}
```

### Edit Message
```
PUT /api/messages/{messageId}
Body: { content: "edited content" }
```

### Delete Message
```
DELETE /api/messages/{messageId}
```

## Error Handling

```javascript
// Connection errors
websocket.connect(userId).catch(error => {
  console.error('Connection failed:', error);
  // Show error message to user
  // Attempt reconnection
});

// Send errors
try {
  websocket.sendMessage(channelId, userId, content);
} catch (error) {
  console.error('Failed to send:', error);
  // Show error message
  // Queue message for retry
}
```

## Testing

### Manual Testing

1. Start backend:
   ```bash
   cd backend/ChatAppV2
   ./mvnw spring-boot:run
   ```

2. Start frontend:
   ```bash
   cd frontend/chatAppV2UI
   npm run dev
   ```

3. Open multiple browser tabs
4. Send messages from one tab
5. Verify they appear in all tabs

### Browser Console Testing

```javascript
// Test connection
websocket.connect(1).then(() => console.log('Connected'));

// Subscribe to channel
websocket.subscribeToChannel('group:1', (msg) => console.log('Received:', msg));

// Send message
websocket.sendMessage('group:1', 1, 'Test message');
```

## Troubleshooting

### Connection Fails
- Ensure backend is running on port 8080
- Check browser console for errors
- Verify CORS settings

### Messages Not Received
- Check subscription topic matches channel ID
- Verify user is member of channel
- Check backend logs for errors

### SockJS Issues
- Try disabling browser extensions
- Check firewall settings
- Use native WebSocket (no SockJS) for testing

## Next Steps

1. Implement message list component
2. Add typing indicators UI
3. Handle connection status in UI
4. Add message delivery indicators
5. Implement message editing/deletion UI

## Resources

- Backend Integration Guide: `/backend/ChatAppV2/WEBSOCKET_INTEGRATION_GUIDE.md`
- STOMP.js Documentation: https://stomp-js.github.io/
- SockJS Documentation: https://github.com/sockjs/sockjs-client

---

**Contact**: Backend team for WebSocket issues
**Last Updated**: 2026-02-20
