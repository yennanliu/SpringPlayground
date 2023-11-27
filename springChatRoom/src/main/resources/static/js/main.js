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

var stompClient = null;
var username = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
    username = document.querySelector('#name').value.trim();

    if(username) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}


function onConnected() {

    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);

    // Subscribe to the "/private" destination // TODO : make it general
    //stompClient.subscribe('/private/user123', onPrivateMessageReceived);

    console.log(">>> subscribe /app/private/"+username );
    stompClient.subscribe('/app/private/'+username);

    // Tell your username to the server
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )

    connectingElement.classList.add('hidden');
}


function onError(error) {
    console.log(">>> error = " + error)
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


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

// online user
function fetchUserList() {
    fetch('/user/online_user') // Replace with the actual endpoint URL
        .then(response => response.json())
        .then(data => {
            updateOnlineUsers(data);
        })
        .catch(error => {
            console.error('Error fetching user list: ', error);
        });
}

function updateOnlineUsers(users) {
    const userList = document.getElementById('userList');
    userList.innerHTML = ''; // Clear the list first

//    users.forEach(user => {
//        const listItem = document.createElement('li');
//        listItem.textContent = user;
//        userList.appendChild(listItem);
//    });

    users.forEach(user => {
        const listItem = document.createElement('li');

        // Create a span for the green light
        const greenLight = document.createElement('span');
        greenLight.classList.add('green-light'); // You may need to define a CSS class for styling

        // Append the green light span to the list item
        listItem.appendChild(greenLight);

        // Create a span for the username
        const usernameSpan = document.createElement('span');
        usernameSpan.textContent = user;

        // Append the username span to the list item
        listItem.appendChild(usernameSpan);

        // Create a "Chat" button
        const chatButton = document.createElement('button');
        chatButton.textContent = 'Chat';
        chatButton.addEventListener('click', () => startChat(user)); // Replace with your chat initiation logic

        // Append the "Chat" button to the list item
        listItem.appendChild(chatButton);

        // Append the list item to the user list
        userList.appendChild(listItem);
    });

}

// Function to handle chat initiation
function startChat(username) {
    // Open a popup window for the chat
    const popupWindow = window.open('', '_blank', 'width=400,height=300');

    // Add your logic to customize the popup window content
    popupWindow.document.write('<html><head><title>Chat with ' + username + '</title></head><body>');
    popupWindow.document.write('<h2>Chat with ' + username + '</h2>');
    popupWindow.document.write('<div id="chatMessages"></div>');
    popupWindow.document.write('<input type="text" id="messageInput" placeholder="Type a message..."/>');
    popupWindow.document.write('<button onclick="sendMessage()">Send</button>');
    popupWindow.document.write('</body></html>');

    // Function to send a message from the popup window
    popupWindow.sendMessage = function() {

        console.log(">>> popupWindow.sendMessage")
        const messageInput = popupWindow.document.getElementById('messageInput');
        const chatMessages = popupWindow.document.getElementById('chatMessages');

        const message = messageInput.value.trim();
        if (message !== '') {
            // Customize the way messages are displayed in the popup window
            chatMessages.innerHTML += '<p><strong>You:</strong> ' + message + '</p>';

            // TODO: Fetch and display chat history
            //fetchChatHistory(username, chatMessages);

            // TODO : implement below in BE
            // Add your logic to send the message to the other user
            // Example: stompClient.send('/app/private/' + username, {}, JSON.stringify({ sender: 'You', content: message, type: 'CHAT' }));

            // send msg to BE
            //stompClient.subscribe('/app/private/' + username, onPrivateMessageReceived);
            //stompClient.subscribe('/app/private/' + username);
            console.log(">>> send msg to /app/private/" + username + ", message = " + message);
            stompClient.send('/app/private/' + username, {}, JSON.stringify({ sender: 'You', content: message, type: 'CHAT' }));

            console.log("send private msg end")

            // Clear the input field
            messageInput.value = '';
        }
    };
}

// Function to fetch and display chat history
function fetchChatHistory(username, chatMessages) {
    fetch('/app/chat/history/' + username)
        .then(response => response.json())
        .then(history => {
            history.forEach(message => {
                chatMessages.innerHTML += '<p><strong>' + message.sender + ':</strong> ' + message.content + '</p>';
            });
        })
        .catch(error => {
            console.error('Error fetching chat history: ', error);
        });
}


// Call the fetchUserList function to initially populate the user list
fetchUserList();

// You can also periodically update the user list using a timer or other events
setInterval(fetchUserList, 5000); // Update every 5 seconds (adjust as needed)

usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)