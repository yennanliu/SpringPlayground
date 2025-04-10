'use strict';

// Ref
// - https://www.callicoder.com/spring-boot-websocket-chat-example/
// - https://blog.csdn.net/qqxx6661/article/details/98883166

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
var privateChat = document.querySelector('#private-chat');
var privateChatUser = document.querySelector('#private-chat-user');
var privateMessageForm = document.querySelector('#privateMessageForm');
var privateMessageInput = document.querySelector('#privateMessage');
var privateMessageArea = document.querySelector('#privateMessageArea');
var closePrivateChat = document.querySelector('#close-private-chat');
var currentUserSpan = document.querySelector('#current-user');

var stompClient = null;
var username = null;
var currentPrivateUser = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
    username = document.querySelector('#name').value.trim();

    if(username) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');
        currentUserSpan.textContent = username;

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.debug = null; // Disable debug messages

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}

function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);

    // Subscribe to private messages
    stompClient.subscribe('/user/' + username + '/topic/private', onPrivateMessageReceived);
    
    // Subscribe to private chat history
    stompClient.subscribe('/user/' + username + '/topic/private.history', onPrivateHistoryReceived);

    // Tell your username to the server
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )

    connectingElement.classList.add('hidden');
}

function onError(error) {
    console.error('WebSocket Error:', error);
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT'
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function sendPrivateMessage(event) {
    var messageContent = privateMessageInput.value.trim();
    if(messageContent && stompClient && currentPrivateUser) {
        var chatMessage = {
            sender: username,
            recipient: currentPrivateUser,
            content: messageContent,
            type: 'PRIVATE'
        };
        stompClient.send("/app/chat.private", {}, JSON.stringify(chatMessage));
        
        // Add message to chat immediately for sender
        var messageElement = createMessageElement(chatMessage, true);
        privateMessageArea.appendChild(messageElement);
        privateMessageArea.scrollTop = privateMessageArea.scrollHeight;
        
        privateMessageInput.value = '';
    }
    event.preventDefault();
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    var messageElement = createMessageElement(message);
    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function onPrivateMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    var messageElement = createMessageElement(message, true);
    
    // If private chat is open with this user, add message there
    if (currentPrivateUser === message.sender || currentPrivateUser === message.recipient) {
        privateMessageArea.appendChild(messageElement);
        privateMessageArea.scrollTop = privateMessageArea.scrollHeight;
    } else {
        // Show notification or handle unopened chat message
        notifyNewPrivateMessage(message);
    }
}

function notifyNewPrivateMessage(message) {
    // Find the user in the list and highlight their name
    const userList = document.getElementById('userList');
    const users = userList.getElementsByTagName('li');
    for (let userItem of users) {
        const usernameSpan = userItem.querySelector('span:not(.green-light)');
        if (usernameSpan && usernameSpan.textContent === message.sender) {
            usernameSpan.style.fontWeight = 'bold';
            usernameSpan.style.color = '#e74c3c';
            break;
        }
    }
}

function onPrivateHistoryReceived(payload) {
    var message = JSON.parse(payload.body);
    var messageElement = createMessageElement(message, true);
    privateMessageArea.appendChild(messageElement);
    privateMessageArea.scrollTop = privateMessageArea.scrollHeight;
}

function createMessageElement(message, isPrivate = false) {
    var messageElement = document.createElement('li');
    messageElement.className = 'chat-message' + (isPrivate ? ' private' : '');

    if(message.type === 'JOIN') {
        messageElement.className = 'event-message';
        message.content = message.sender + ' joined!';
    } else if (message.type === 'LEAVE') {
        messageElement.className = 'event-message';
        message.content = message.sender + ' left!';
    } else {
        var avatarElement = document.createElement('i');
        avatarElement.textContent = message.sender[0].toUpperCase();
        avatarElement.style.backgroundColor = getAvatarColor(message.sender);
        messageElement.appendChild(avatarElement);

        var messageContentElement = document.createElement('div');
        messageContentElement.className = 'message-content';

        var messageHeader = document.createElement('div');
        messageHeader.className = 'message-header';

        var senderElement = document.createElement('span');
        senderElement.className = 'message-sender';
        senderElement.textContent = message.sender;

        var timeElement = document.createElement('span');
        timeElement.className = 'message-time';
        timeElement.textContent = new Date().toLocaleTimeString();

        messageHeader.appendChild(senderElement);
        messageHeader.appendChild(timeElement);
        messageContentElement.appendChild(messageHeader);

        var textElement = document.createElement('div');
        textElement.className = 'message-text';
        textElement.textContent = message.content;
        messageContentElement.appendChild(textElement);

        messageElement.appendChild(messageContentElement);
    }

    return messageElement;
}

function startPrivateChat(targetUser) {
    currentPrivateUser = targetUser;
    privateChatUser.textContent = targetUser;
    privateChat.classList.remove('hidden');
    privateMessageArea.innerHTML = '';
    
    // Reset notification styling
    const userList = document.getElementById('userList');
    const users = userList.getElementsByTagName('li');
    for (let userItem of users) {
        const usernameSpan = userItem.querySelector('span:not(.green-light)');
        if (usernameSpan && usernameSpan.textContent === targetUser) {
            usernameSpan.style.fontWeight = 'normal';
            usernameSpan.style.color = 'inherit';
            break;
        }
    }
    
    // Request chat history
    var historyRequest = {
        sender: username,
        recipient: targetUser,
        type: 'PRIVATE'
    };
    stompClient.send("/app/chat.getPrivateHistory", {}, JSON.stringify(historyRequest));
}

function closePrivateChat() {
    currentPrivateUser = null;
    privateChat.classList.add('hidden');
    privateMessageArea.innerHTML = '';
}

function updateOnlineUsers(users) {
    const userList = document.getElementById('userList');
    userList.innerHTML = '';

    users.forEach(user => {
        if (user !== username) {
            const listItem = document.createElement('li');
            
            const greenLight = document.createElement('span');
            greenLight.className = 'green-light';
            listItem.appendChild(greenLight);
            
            const usernameSpan = document.createElement('span');
            usernameSpan.textContent = user;
            listItem.appendChild(usernameSpan);
            
            const chatButton = document.createElement('button');
            chatButton.innerHTML = '<i class="fas fa-comment"></i>';
            chatButton.className = 'primary';
            chatButton.onclick = () => startPrivateChat(user);
            listItem.appendChild(chatButton);
            
            userList.appendChild(listItem);
        }
    });
}

function fetchUserList() {
    fetch('/user/online_user')
        .then(response => response.json())
        .then(data => {
            updateOnlineUsers(data);
        })
        .catch(error => {
            console.error('Error fetching user list:', error);
        });
}

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

// Initialize event listeners
usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);
privateMessageForm.addEventListener('submit', sendPrivateMessage, true);
closePrivateChat.addEventListener('click', closePrivateChat, true);

// Initialize user list
fetchUserList();
setInterval(fetchUserList, 5000);